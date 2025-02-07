import json
from flask import Blueprint, request, jsonify, Response
from database.mongodb import mongodb
from database.mysql import MySQL
from database.redis_client import redis_client
from auth.jwt_handler import decode_jwt_from_cookie
from services.chat_service import chat_service
from services.stream_service import stream_service

survey_bp = Blueprint('survey', __name__)

@survey_bp.route("/ai/survey/<survey_code>", methods=["POST"])
def start_survey(survey_code):
    try:
        user_code = decode_jwt_from_cookie()
        data = request.json
        nickname = data.get("nickname")

        if not nickname:
            return jsonify({"error": "Nickname is required"}), 400

        session_id = f"{survey_code}_{user_code}"

        # Store survey info in Redis
        redis_client.store_survey_info(session_id, survey_code, user_code, nickname)

        # Initialize messages
        messages = [
            chat_service.create_message("system", chat_service.get_system_prompt(nickname)),
            chat_service.create_message("user", "설문을 시작하겠습니다.")
        ]

        def generate():
            try:
                stream = chat_service.generate_response(messages)
                complete_response = []

                # Stream 처리
                for sse_message in stream_service.process_stream(stream):
                    if '"error"' not in sse_message:  # 에러가 아닌 경우만 응답 수집
                        response_data = json.loads(sse_message.replace('data: ', '').strip())
                        complete_response.append(response_data['response'])
                    yield sse_message

                # 전체 응답 저장
                bot_reply = ''.join(complete_response)
                messages.append(chat_service.create_message("assistant", bot_reply))
                redis_client.store_messages(session_id, messages)

            except Exception as e:
                yield stream_service.format_sse_message(str(e), error=True)

        return Response(generate(), mimetype='text/event-stream')

    except Exception as e:
        return jsonify({"error": str(e)}), 500


@survey_bp.route("/ai/survey/<survey_code>/essay", methods=["POST"])
def chat_survey(survey_code):
    try:
        user_code = decode_jwt_from_cookie()
        session_id = f"{survey_code}_{user_code}"
        content = request.json.get("content")

        if not content:
            return jsonify({"error": "Content is required"}), 400

        # Get messages from Redis
        messages = redis_client.get_messages(session_id)
        if not messages:
            return jsonify({"error": "Survey session not found or expired"}), 404

        messages.append(chat_service.create_message("user", content))

        def generate():
            try:
                stream = chat_service.generate_response(messages)
                complete_response = []

                # Stream 처리
                for sse_message in stream_service.process_stream(stream):
                    if '"error"' not in sse_message:  # 에러가 아닌 경우만 응답 수집
                        response_data = json.loads(sse_message.replace('data: ', '').strip())
                        complete_response.append(response_data['response'])
                    yield sse_message

                # 전체 응답 저장
                bot_reply = ''.join(complete_response)
                messages.append(chat_service.create_message("assistant", bot_reply))
                redis_client.store_messages(session_id, messages)

                if "설문을 종료합니다" in bot_reply:
                    try:
                        # Save to MongoDB
                        mongodb.save_survey(survey_code, user_code, messages)

                        # Get project owner's user_code from MySQL
                        project_user_code = MySQL.get_project_user_code(survey_code)
                        if project_user_code:
                            # Update response count in Redis
                            redis_client.update_response_count(project_user_code, survey_code)

                            summary_messages = messages.copy()
                            summary_messages.append(
                                chat_service.create_message("user", """다음 대화를 질문과 답변형태로 만들건데
답변은 원본 그대로 유지해주면서 만들어줘
형식:
Q. {target_name}님이 팀원들과 협업할 때 보여준 가장 인상적인 행동은 무엇이었나요?
A. {response_1}
Q. {target_name}님이 팀에서 발생한 어려움을 해결하기 위해 기여한 사례가 있다면 공유해 주세요.
A. {response_2}
Q. {target_name}님이 갈등이나 의견 차이가 있었던 순간, 어떤 태도를 보였나요?
A. {response_3}
Q. {target_name}님이 새로운 도전을 받아들이는 태도는 어땠나요?
A. {response_4}
Q. {target_name}님은 피드백을 받을 때 어떤 태도를 보였나요?
A. {response_5}
Q. {target_name}님이 개선해야 할 점이 있다면 구체적으로 적어 주세요.
A. {response_6}"""))

                            summary_stream = chat_service.generate_response(
                                summary_messages)
                            summary_response = []

                            for chunk in summary_stream:
                                if chunk.choices[0].delta.content:
                                    summary_response.append(
                                        chunk.choices[0].delta.content)


                            # Save to MongoDB summaries collection
                            summary_data = {
                                "messages": ''.join(summary_response),
                                "user_code": project_user_code
                            }
                            mongodb.summary_collection.insert_one(summary_data)
                        # Clean up Redis
                        redis_client.cleanup_session(session_id)

                        yield stream_service.create_completion_message()

                    except Exception as e:
                        yield stream_service.format_sse_message(str(e), error=True)

            except Exception as e:
                yield stream_service.format_sse_message(str(e), error=True)

        return Response(generate(), mimetype='text/event-stream')

    except Exception as e:
        return jsonify({"error": str(e)}), 500