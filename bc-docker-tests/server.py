import os
from flask import Flask, send_from_directory

PORT = int(os.environ.get("PORT", 5050))
REPORT_DIR = os.path.abspath("allure-report")  # ✅ Путь для локального и Docker-режима

app = Flask(__name__)

@app.route('/')
def index():
    return send_from_directory(REPORT_DIR, 'index.html')

@app.route('/<path:path>')
def static_files(path):
    return send_from_directory(REPORT_DIR, path)

app.run(host='0.0.0.0', port=PORT)