#!/bin/bash
set -e

echo "🧪 Running tests with timeout control..."

# Run JAR with timeout (120s max)
timeout 120s java -jar calc-test-framework-fat-1.0.jar
EXIT_CODE=$?

if [ $EXIT_CODE -eq 124 ]; then
  echo "⚠️ Test execution timed out after 120 seconds"
elif [ $EXIT_CODE -ne 0 ]; then
  echo "❌ Test execution failed with exit code $EXIT_CODE"
  exit $EXIT_CODE
else
  echo "✅ Test execution completed successfully"
fi

echo "🧾 Generating Allure report..."
allure generate /app/allure-results --clean -o /app/allure-report || {
  echo "❌ Failed to generate Allure report"
  ls -lah /app/allure-results
  exit 1
}

echo "📁 Contents of /app/allure-report:"
ls -lah /app/allure-report

echo "🚀 Starting Flask server on port $PORT"
echo "🌐 Access the report at: http://localhost:$PORT"

python3 server.py