from flask import Flask, request, jsonify, Response
from huggingface_hub import InferenceClient
from pymongo import MongoClient
from datetime import datetime
import os
import json
import jwt
from dotenv import load_dotenv
import redis
import base64
import pymysql

app = Flask(__name__)


# Environment variables setup
env_path = os.path.join(os.path.abspath(os.path.dirname(__file__)), '..',
                        'collabit-server', '.env')
load_dotenv(env_path)
app.secret_key = os.getenv('FLASK_SECRET_KEY')
JWT_SECRET_KEY = os.getenv('JWT_SECRET_KEY')
MONGODB_URI = os.getenv('SPRING_DATA_MONGODB_URI')
HUGGINGFACE_API_KEY = os.getenv('HUGGINGFACE_API_KEY')
REDIS_URL = os.getenv('REDIS_URL', 'redis://localhost:6379')

# Redis connection
redis_client = redis.from_url(REDIS_URL)
SESSION_TIMEOUT = 3600  # 1 hour session timeout


def get_project_user_code(code):
  try:
    connection = pymysql.connect(
        host=os.getenv('SPRING_DATASOURCE_URL').split('/')[-2].split(':')[0],
        database=os.getenv('SPRING_DATASOURCE_URL').split('/')[-1],
        user=os.getenv('SPRING_DATASOURCE_USERNAME'),
        password=os.getenv('SPRING_DATASOURCE_PASSWORD'),
        cursorclass=pymysql.cursors.DictCursor  # dictionary=True 대신 이걸로 변경
    )
    cursor = connection.cursor()  # 커서 생성 시 파라미터 제거

    query = "SELECT user_code FROM project_info WHERE code = %s"
    cursor.execute(query, (int(code),))

    result = cursor.fetchone()

    cursor.close()
    connection.close()
    if result:
      return result['user_code']
    return None
  except Exception as e:
    print(f"General Error: {str(e)}")
    raise

# MongoDB connection
try:
  client = MongoClient(MONGODB_URI)
  db = client['mydatabase']
  survey_essay = db['survey_essay']
  summary_collection = db['summaries']
except Exception as e:
  print(f"MongoDB connection failed: {e}")
  raise

# Hugging Face client
hf_client = InferenceClient(api_key=HUGGINGFACE_API_KEY)


def decode_jwt_from_cookie():
  """Extract and decode JWT from cookie"""
  try:
    token = request.cookies.get('accessToken')
    if not token:
      raise ValueError("No token found in cookies")

    # Fix Base64 padding
    def fix_base64_padding(s):
      """Add padding to Base64 string if needed"""
      missing_padding = len(s) % 4
      if missing_padding:
        s += '=' * (4 - missing_padding)
      return s

    # Base64 decode the secret key with padding fix
    decoded_secret = base64.b64decode(fix_base64_padding(JWT_SECRET_KEY))

    # Decode JWT with decoded secret
    decoded = jwt.decode(token, decoded_secret, algorithms=["HS512"])
    return decoded.get('sub')
  except Exception as e:
    print(f"JWT decode error: {e}")
    raise


def create_message(role, content):
  return {
    "role": role,
    "content": content,
    "timestamp": datetime.now().isoformat()
  }


def get_system_prompt(target_name):
  return f"""너는 협업능력에 대해 분석을 하는 분석가야. 설문응답자에게 질문을 통해서 {target_name}님의 협업능력 대해서 리포트를 작성해야해.
처음에 설문을 시작하겠습니다. 메세지를 통해 시작할거야. 이 때는 네 알겠습니다. 대답 없이 설문을 진행해줘.

물어봐야하는 질문 6개는 다음과 같아.

- {target_name}님이 팀원들과 협업할 때 보여준 가장 인상적인 행동은 무엇이었나요?
- {target_name}님이 팀에서 발생한 어려움을 해결하기 위해 기여한 사례가 있다면 공유해 주세요.
- 갈등이나 의견 차이가 있었던 순간, {target_name}님은 어떤 태도를 보였나요?
- {target_name}님이 새로운 도전을 받아들이는 태도는 어땠나요?
- {target_name}님은 피드백을 받을 때 어떤 태도를 보였나요?
- {target_name}님이 개선해야 할 점이 있다면 구체적으로 적어 주세요.

이 질문들을 딱딱하게 느껴지지 않도록 대화형태로 하나씩 질문을 해줘.
6개의 질문을 전부 한 후에 대답을 받고나면
설문을 종료합니다.
라고 꼭 메세지에 넣어줘.

{target_name}님의 협업능력에 대해 요약은 내가 요청하기 전에는 언급하지마."""


@app.route("/api/survey/<survey_code>", methods=["POST"])
def start_survey(survey_code):
  try:
    user_code = decode_jwt_from_cookie()
    data = request.json
    nickname = data.get("nickname")

    if not nickname:
      return jsonify({"error": "Nickname is required"}), 400

    session_id = f"{survey_code}_{user_code}"

    # Store survey info in Redis
    survey_info = {
      "surveyCode": survey_code,
      "userCode": user_code,
      "nickname": nickname,
      "startTime": datetime.now().isoformat()
    }
    redis_client.setex(f"survey_info_{session_id}", SESSION_TIMEOUT,
                       json.dumps(survey_info))

    # Initialize messages
    messages = [
      create_message("system", get_system_prompt(nickname)),
      create_message("user", "설문을 시작하겠습니다.")
    ]

    def generate():
      try:
        stream = hf_client.chat.completions.create(
            model="Qwen/Qwen2.5-72B-Instruct",
            messages=[{"role": m["role"], "content": m["content"]} for m in
                      messages],
            temperature=0.5,
            max_tokens=2048,
            top_p=0.7,
            stream=True
        )

        complete_response = []
        for chunk in stream:
          if chunk.choices[0].delta.content:
            content = chunk.choices[0].delta.content
            complete_response.append(content)
            yield f"data: {json.dumps({'response': content,'timestamp': datetime.now().isoformat()})}\n\n"

        bot_reply = "".join(complete_response)
        messages.append(create_message("assistant", bot_reply))
        redis_client.setex(f"messages_{session_id}", SESSION_TIMEOUT,
                           json.dumps(messages))

      except Exception as e:
        yield f"data: {json.dumps({'error': str(e)})}\n\n"

    return Response(generate(), mimetype='text/event-stream')

  except Exception as e:
    return jsonify({"error": str(e)}), 500


@app.route("/api/survey/<survey_code>/essay", methods=["POST"])
def chat_survey(survey_code):
  try:
    user_code = decode_jwt_from_cookie()
    session_id = f"{survey_code}_{user_code}"
    content = request.json.get("content")

    if not content:
      return jsonify({"error": "Content is required"}), 400

    # Get messages from Redis
    messages_json = redis_client.get(f"messages_{session_id}")
    if not messages_json:
      return jsonify({"error": "Survey session not found or expired"}), 404

    messages = json.loads(messages_json)
    messages.append(create_message("user", content))

    def generate():
      try:
        stream = hf_client.chat.completions.create(
            model="Qwen/Qwen2.5-72B-Instruct",
            messages=[{"role": m["role"], "content": m["content"]} for m in
                      messages],
            temperature=0.5,
            max_tokens=2048,
            top_p=0.7,
            stream=True
        )

        complete_response = []
        for chunk in stream:
          if chunk.choices[0].delta.content:
            content = chunk.choices[0].delta.content
            complete_response.append(content)
            yield f"data: {json.dumps({'response': content,'timestamp': datetime.now().isoformat()})}\n\n"

        bot_reply = "".join(complete_response)
        messages.append(create_message("assistant", bot_reply))
        redis_client.setex(f"messages_{session_id}", SESSION_TIMEOUT,
                           json.dumps(messages))

        if "설문을 종료합니다" in bot_reply:
          try:
            survey_info = json.loads(
              redis_client.get(f"survey_info_{session_id}"))

            # Save to MongoDB
            survey_data = {
              "submittedAt": datetime.now(),
              "messages": messages,
              "projectInfoCode": survey_code,
              "userCode": user_code
            }
            survey_essay.insert_one(survey_data)

            # Get project owner's user_code from MySQL
            project_user_code = get_project_user_code(survey_code)
            if project_user_code:
              # Update response count in Redis for project owner
              response_count_key = f"newSurveyResponse::{project_user_code}::{survey_code}"
              current_count = redis_client.get(response_count_key)
              if current_count:
                redis_client.set(response_count_key, int(current_count) + 1)
              else:
                redis_client.set(response_count_key, 1)

            # Clean up Redis
            redis_client.delete(f"messages_{session_id}",
                                f"survey_info_{session_id}")

            yield f"data: {json.dumps({'response': '설문이 완료되었습니다.', 'status': 'completed','timestamp': datetime.now().isoformat()})}\n\n"

          except Exception as e:
            yield f"data: {json.dumps({'error': f'Survey completion error: {str(e)}'})}\n\n"

      except Exception as e:
        yield f"data: {json.dumps({'error': str(e)})}\n\n"

    return Response(generate(), mimetype='text/event-stream')

  except Exception as e:
    return jsonify({"error": str(e)}), 500


if __name__ == "__main__":
  app.run(debug=True)