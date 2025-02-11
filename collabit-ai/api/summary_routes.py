from flask import Blueprint, jsonify
from database.mongodb import mongodb
from services.chat_service import chat_service
import json
from collections import Counter
from auth.jwt_handler import decode_jwt_from_cookie


summary_bp = Blueprint('summary', __name__)


@summary_bp.route("/ai/portfolio/essay/wordcloud", methods=["GET"])
def get_wordcloud():
  try:
    user_code = decode_jwt_from_cookie()
    print(user_code)

    summaries = list(mongodb.summary_collection.find({"user_code": user_code}))

    strength_keywords = []
    weakness_keywords = []

    # 각 문서에서 키워드 추출
    for summary in summaries:
      for analysis in summary.get('sentiment_analysis', []):
        keywords = [k.strip() for k in analysis['keyword'].split(',')]

        if analysis['sensitive'] == 'positive':
          strength_keywords.extend(keywords)
        elif analysis['sensitive'] == 'negative':
          weakness_keywords.extend(keywords)

    # 키워드 빈도수 계산
    strength_counter = Counter(strength_keywords)
    weakness_counter = Counter(weakness_keywords)

    def normalize_word_cloud(counter, top_n=20, min_value=8, max_value=25):
      # 상위 20개 선택
      most_common = counter.most_common(top_n)

      if not most_common:  # 결과가 없는 경우 빈 리스트 반환
        return []

      # 최소/최대 빈도수 찾기
      min_count = min(count for _, count in most_common)
      max_count = max(count for _, count in most_common)

      # 정규화된 워드클라우드 데이터 생성
      normalized = []
      for word, count in most_common:
        # min_count와 max_count가 같은 경우 (모든 단어의 빈도수가 동일할 때)
        if min_count == max_count:
          normalized_value = (min_value + max_value) / 2
        else:
          # min-max 정규화 공식을 사용하여 8-25 사이의 값으로 변환
          normalized_value = (count - min_count) / (max_count - min_count) * (
                max_value - min_value) + min_value

        normalized.append({
          "text": word,
          "value": round(normalized_value, 1)  # 소수점 첫째자리까지 표현
        })

      return normalized

    # 정규화된 워드클라우드 데이터 생성
    strength_cloud = normalize_word_cloud(strength_counter)
    weakness_cloud = normalize_word_cloud(weakness_counter)

    return jsonify({
      "strength": strength_cloud,
      "weakness": weakness_cloud
    }), 200

  except Exception as e:
    return jsonify({"error": str(e)}), 500

@summary_bp.route("/ai/portfolio", methods=["POST"])
def get_ai_summary():
    try:
      user_code = decode_jwt_from_cookie()

      summaries = list(
        mongodb.summary_collection.find({"user_code": user_code}))

      strength_answers = []
      weakness_answers = []

      # 답변들을 긍정/부정으로 분류
      for summary in summaries:
        for analysis in summary.get('sentiment_analysis', []):
          if analysis['sensitive'] == 'positive':
            strength_answers.append(analysis['answer'])
          elif analysis['sensitive'] == 'negative':
            weakness_answers.append(analysis['answer'])

      # AI에게 요약 요청
      summary_messages = [
        chat_service.create_message("system", """주어진 답변들을 종합적으로 분석하여 해당 사람의 강점과 약점을 간단명료하게 요약해주세요. 
  답변을 다음과 같은 JSON 형식으로 작성해주세요:
  {
      "strength": "강점에 대한 종합적인 요약",
      "weakness": "약점에 대한 종합적인 요약"
  }"""),
        chat_service.create_message("user", f"""강점 관련 답변들:
  {' // '.join(strength_answers)}

  약점 관련 답변들:
  {' // '.join(weakness_answers)}""")
      ]

      analysis_stream = chat_service.generate_response(summary_messages)
      analysis_response = []

      for chunk in analysis_stream:
        if chunk.choices[0].delta.content:
          analysis_response.append(chunk.choices[0].delta.content)

      ai_analysis = json.loads(''.join(analysis_response))
      mongodb.save_ai_analysis(user_code, ai_analysis)
      return "", 200

    except Exception as e:
      return jsonify({"error": str(e)}), 500


@summary_bp.route("/ai/portfolio/essay/ai-summary", methods=["GET"])
def get_summary():
  try:
    user_code = decode_jwt_from_cookie()

    summary = mongodb.ai_analysis.find_one(
        {"user_code": user_code},
        {"_id": 0, "analysis_results": 1}
    )

    if summary:
      return jsonify(summary["analysis_results"]), 200
    return jsonify({"error": "Summary not found"}), 404

  except Exception as e:
    return jsonify({"error": str(e)}), 500