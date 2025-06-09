#!/bin/bash
set -e

START_PORT=5050
END_PORT=5099

# === Check Java 21 ===
echo "🔍 Checking Java version..."
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
if [[ "$JAVA_VERSION" != 21* ]]; then
  echo "❌ Java 21 required. Found: $JAVA_VERSION"
  echo "Please install Java 21 manually and ensure 'java' points to it."
  exit 1
fi

# === Check Python 3 ===
echo "🔍 Checking Python 3..."
if ! command -v python3 > /dev/null; then
  echo "❌ Python 3 is not installed. Please install it manually."
  exit 1
fi

# === Setup virtual environment ===
echo "🧰 Creating virtual environment..."
python3 -m venv .venv
source .venv/bin/activate

# === Install Flask ===
echo "📦 Installing Flask inside venv..."
pip install flask

# === Check Allure CLI ===
echo "🔍 Checking Allure CLI..."
if ! command -v allure > /dev/null; then
  echo "❌ Allure CLI is not installed."
  echo "Please install Allure CLI manually: https://docs.qameta.io/allure/#_installing_a_commandline"
  exit 1
fi

# === Find an available port ===
is_port_free() {
  ! lsof -i :"$1" >/dev/null 2>&1
}

find_free_port() {
  for ((port=$START_PORT; port<=$END_PORT; port++)); do
    if is_port_free "$port"; then
      echo "$port"
      return 0
    fi
  done
  return 1
}

PORT=$(find_free_port)
if [ -z "$PORT" ]; then
  echo "❌ No free port found in range $START_PORT–$END_PORT"
  exit 1
fi

export PORT
echo "✅ Using port: $PORT"

# === Run tests ===
echo "🧪 Running tests..."
java -jar calc-test-framework-fat-1.0.jar

# === Generate Allure report ===
echo "🧾 Generating Allure report..."
allure generate allure-results --clean -o allure-report || {
  echo "❌ Failed to generate Allure report"
  ls -lah allure-results
  exit 1
}

# === Launch Flask server in background ===
echo "🚀 Starting Flask server on port $PORT"
python3 server.py &

# === Wait for server to respond ===
echo "⏳ Waiting for report to be available..."
for i in {1..30}; do
  if curl -s --head "http://localhost:$PORT" | grep "200 OK" > /dev/null; then
    echo "🌐 Report is live at: http://localhost:$PORT"
    break
  fi
  sleep 1
done

# === Open in browser ===
open_browser() {
  URL="http://localhost:$PORT"
  OS=$(uname)

  case "$OS" in
    Darwin)
      open "$URL"
      ;;
    Linux)
      if command -v xdg-open > /dev/null; then
        xdg-open "$URL"
      else
        echo "⚠️ Could not open browser automatically. Please visit:"
        echo "$URL"
      fi
      ;;
    *)
      echo "⚠️ Unsupported OS. Please open manually:"
      echo "$URL"
      ;;
  esac
}

open_browser