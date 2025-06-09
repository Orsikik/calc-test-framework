#!/bin/bash
set -e

START_PORT=5050
END_PORT=5099
IMAGE_NAME="calc-test-server"

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

echo "✅ Using port: $PORT"

# === Run Docker container ===
docker run --rm -e PORT="$PORT" -p "$PORT:$PORT" "$IMAGE_NAME" &

# === Wait until the Flask server becomes available ===
echo "⏳ Waiting for the report to become available at http://localhost:$PORT..."
for i in {1..30}; do
    if curl -s --head "http://localhost:$PORT" | grep "200 OK" > /dev/null; then
        echo "🌐 Report is live!"
        break
    fi
    sleep 1
done

# === Detect OS and open browser ===
open_browser() {
    URL="http://localhost:$PORT"
    OS=$(uname)

    case "$OS" in
        Darwin)
            echo "🧭 Detected macOS — using 'open'"
            open "$URL"
            ;;
        Linux)
            if command -v xdg-open > /dev/null; then
                echo "🧭 Detected Linux — using 'xdg-open'"
                xdg-open "$URL"
            else
                echo "⚠️ xdg-open not found. Attempting to install..."
                if command -v apt-get > /dev/null; then
                    sudo apt-get update && sudo apt-get install -y xdg-utils
                    xdg-open "$URL"
                elif command -v yum > /dev/null; then
                    sudo yum install -y xdg-utils
                    xdg-open "$URL"
                else
                    echo "❌ Unsupported package manager. Please open the browser manually:"
                    echo "$URL"
                fi
            fi
            ;;
        *)
            echo "❌ Unsupported OS. Please open the report manually:"
            echo "$URL"
            ;;
    esac
}

open_browser