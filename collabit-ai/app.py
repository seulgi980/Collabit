from flask import Flask, request, jsonify
from huggingface_hub import InferenceClient
from pymongo import MongoClient
from datetime import datetime
import os
import json
from dotenv import load_dotenv
import redis

app = Flask(__name__)

# 환경변수 설정
env_path = os.path.join(os.path.abspath(os.path.dirname(__file__)), '..',
                        'collabit-server', '.env')
load_dotenv(env_path)
app.secret_key = os.getenv('FLASK_SECRET_KEY')
MONGODB_URI = os.getenv('SPRING_DATA_MONGODB_URI')
HUGGINGFACE_API_KEY = os.getenv('HUGGINGFACE_API_KEY')
REDIS_URL = os.getenv('REDIS_URL', 'redis://localhost:6379')

# Redis 연결
redis_client = redis.from_url(REDIS_URL)
SESSION_TIMEOUT = 3600  # 세션 만료 시간 (1시간)

# MongoDB 연결
try:
  client = MongoClient(MONGODB_URI)
  db = client['mydatabase']
  survey_collection = db['surveys']
  summary_collection = db['summaries']
except Exception as e:
  print(f"MongoDB 연결 실패: {e}")
  raise

# Hugging Face 클라이언트
hf_client = InferenceClient(api_key=HUGGINGFACE_API_KEY)


def save_to_redis(key, value, expire_time=SESSION_TIMEOUT):
  """Redis에 데이터 저장"""
  try:
    # datetime 객체를 ISO 형식 문자열로 변환
    if isinstance(value, dict):
      value = {k: v.isoformat() if isinstance(v, datetime) else v for k, v in
               value.items()}
    redis_client.setex(key, expire_time, json.dumps(value))
  except Exception as e:
    print(f"Redis 저장 실패: {e}")
    raise


def get_from_redis(key):
  """Redis에서 데이터 조회"""
  try:
    data = redis_client.get(key)
    if data:
      return json.loads(data)
    return None
  except Exception as e:
    print(f"Redis 조회 실패: {e}")
    return None


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
6개의 질문에 대한 대답을 모두 받으면
설문을 종료합니다.
라고 꼭 메세지에 넣어줘.

{target_name}님의 협업능력에 대해 요약은 내가 요청하기 전에는 언급하지마."""

@app.route("/api/survey/start", methods=["POST"])
def start_survey():
  try:
    data = request.json
    project_code = data.get("projectCode")
    surveyor_code = data.get("surveyorCode")
    target_code = data.get("targetCode")
    target_name = data.get("targetName", "대상자")
    print(f"data : {data}")

    if not all([project_code, surveyor_code, target_code]):
      return jsonify({"error": "필수 파라미터가 누락되었습니다."}), 400

    session_id = f"{project_code}_{surveyor_code}_{target_code}"

    # 설문 정보 Redis에 저장
    survey_info = {
      "projectCode": project_code,
      "surveyorCode": surveyor_code,
      "targetCode": target_code,
      "targetName": target_name,
      "startTime": datetime.now().isoformat()
    }
    save_to_redis(f"survey_info_{session_id}", survey_info)

    # 초기 메시지 설정 및 AI 응답 생성
    messages = [
      create_message("system", get_system_prompt(target_name)),
      create_message("user", "설문을 시작하겠습니다.")
    ]

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

      response = []
      for chunk in stream:
        if chunk.choices[0].delta.content:
          response.append(chunk.choices[0].delta.content)

      initial_response = "".join(response)
      messages.append(create_message("assistant", initial_response))

      # 메시지 Redis에 저장
      save_to_redis(f"messages_{session_id}", messages)

      return jsonify({
        "sessionId": session_id,
        "response": initial_response,
        "timestamp": messages[-1]["timestamp"]
      }), 201

    except Exception as e:
      return jsonify({"error": f"AI 응답 생성 중 오류가 발생했습니다: {str(e)}"}), 500

  except Exception as e:
    return jsonify({"error": f"서버 에러가 발생했습니다: {str(e)}"}), 500


@app.route("/api/survey/chat", methods=["POST"])
def chat():
  try:
    data = request.json
    session_id = data.get("sessionId")
    content = data.get("content")
    print(f"data : {data}")

    if not session_id or not content:
      return jsonify({"error": "필수 파라미터가 누락되었습니다."}), 400

    # Redis에서 메시지 히스토리 조회
    messages = get_from_redis(f"messages_{session_id}")
    if not messages:
      return jsonify({"error": "세션이 만료되었거나 존재하지 않습니다."}), 404

    messages.append(create_message("user", content))

    try:
      # AI 응답 생성
      ai_messages = [{"role": m["role"], "content": m["content"]} for m in
                     messages]
      stream = hf_client.chat.completions.create(
          model="Qwen/Qwen2.5-72B-Instruct",
          messages=ai_messages,
          temperature=0.5,
          max_tokens=2048,
          top_p=0.7,
          stream=True
      )

      response = []
      for chunk in stream:
        if chunk.choices[0].delta.content:
          response.append(chunk.choices[0].delta.content)

      bot_reply = "".join(response)
      assistant_message = create_message("assistant", bot_reply)
      messages.append(assistant_message)

      # 메시지 Redis에 저장
      save_to_redis(f"messages_{session_id}", messages)

      # 설문 종료 처리
      if "설문을 종료합니다" in bot_reply:
        try:
          survey_info = get_from_redis(f"survey_info_{session_id}")
          survey_data = {
            "projectCode": survey_info["projectCode"],
            "surveyorCode": survey_info["surveyorCode"],
            "targetCode": survey_info["targetCode"],
            "targetName": survey_info["targetName"],
            "startTime": survey_info["startTime"],
            "endTime": datetime.now().isoformat(),
            "messages": messages
          }
          survey_collection.insert_one(survey_data)

          # Redis 세션 정리
          redis_client.delete(f"messages_{session_id}",
                              f"survey_info_{session_id}")

          return jsonify({
            "response": "설문을 종료합니다.",
            "timestamp": assistant_message["timestamp"]
          }), 204

        except Exception as e:
          print(f"설문 종료 처리 중 오류 발생: {str(e)}")
          return jsonify({"error": "설문 종료 처리 중 오류가 발생했습니다."}), 500

      return jsonify({
        "response": bot_reply,
        "timestamp": assistant_message["timestamp"]
      }), 201

    except Exception as e:
      return jsonify({"error": f"AI 응답 생성 중 오류가 발생했습니다: {str(e)}"}), 500

  except Exception as e:
    return jsonify({"error": f"서버 에러가 발생했습니다: {str(e)}"}), 500


if __name__ == "__main__":
  app.run(debug=True)