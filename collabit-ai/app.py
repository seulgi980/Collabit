from flask import Flask
from config.settings import FLASK_SECRET_KEY
from api.routes import survey_bp

app = Flask(__name__)
app.secret_key = FLASK_SECRET_KEY

# Register blueprints
app.register_blueprint(survey_bp)

if __name__ == "__main__":
    app.run(debug=True)