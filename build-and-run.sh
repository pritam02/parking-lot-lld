#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "Building parking lot project..."
mvn clean package -q

JAR="target/parking-lot-lld-1.0.0-jar-with-dependencies.jar"

if [[ ! -f "$JAR" ]]; then
  echo "Error: JAR not found at $JAR"
  exit 1
fi

echo "Running parking lot demo..."
echo ""
java -jar "$JAR"
