#!/bin/bash

set -e

APP_ID="com.gdomingues.androidapp"
COVERAGE_FILE="coverage.ec"
LOCAL_COVERAGE_PATH="app/build/outputs/code-coverage/connected/$COVERAGE_FILE"
REPORT_PATH="app/build/reports/jacoco/jacocoTestReport/html/index.html"

echo "📦 Installing debug and test APKs with coverage enabled..."
./gradlew installDebug installDebugAndroidTest -Pcoverage

echo "🧪 Running unit tests with coverage enabled..."
./gradlew testDebugUnitTest -Pcoverage

echo "📱 Running instrumentation tests with coverage enabled..."
adb shell am instrument -w \
  -e coverage true \
  -e coverageFile "/data/data/$APP_ID/files/$COVERAGE_FILE" \
  "$APP_ID.test/androidx.test.runner.AndroidJUnitRunner"

echo "📥 Attempting to pull coverage file via run-as..."
mkdir -p "$(dirname "$LOCAL_COVERAGE_PATH")"
if adb shell run-as "$APP_ID" ls "files/$COVERAGE_FILE" >/dev/null 2>&1; then
  adb shell run-as "$APP_ID" cat "files/$COVERAGE_FILE" > "$LOCAL_COVERAGE_PATH"
else
  echo "⚠️ run-as failed, attempting to copy to /sdcard and pull..."
  adb shell cp "/data/data/$APP_ID/files/$COVERAGE_FILE" "/sdcard/$COVERAGE_FILE" || {
    echo "❌ Failed to copy coverage.ec to /sdcard"
    exit 1
  }
  adb pull "/sdcard/$COVERAGE_FILE" "$LOCAL_COVERAGE_PATH"
fi

echo "📊 Generating merged Jacoco report..."
./gradlew jacocoTestReport -Pcoverage

echo "✅ Done! View the coverage report at:"
echo "$REPORT_PATH"

# Only open the report locally (not in CI)
if [[ -z "$CI" ]]; then
  open "$REPORT_PATH" 2>/dev/null || xdg-open "$REPORT_PATH" 2>/dev/null || echo "(open it manually)"
fi
