from pymongo import MongoClient
from config.settings import MONGODB_URI
from datetime import datetime

class MongoDB:
    def __init__(self):
        try:
            self.client = MongoClient(MONGODB_URI)
            self.db = self.client['mydatabase']
            self.survey_essay = self.db['survey_essay']
            self.summary_collection = self.db['summaries']
        except Exception as e:
            print(f"MongoDB connection failed: {e}")
            raise

    def save_survey(self, survey_code, user_code, messages):
        """Save survey data to MongoDB"""
        try:
            survey_data = {
                "submittedAt": datetime.now(),
                "messages": messages,
                "projectInfoCode": survey_code,
                "userCode": user_code
            }
            return self.survey_essay.insert_one(survey_data)
        except Exception as e:
            print(f"Failed to save survey: {e}")
            raise

# Create a singleton instance
mongodb = MongoDB()