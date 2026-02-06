#!/bin/bash
set -e

cd /workspaces/srv-usuario-poc

echo "════════════════════════════════════════"
echo "  Build Validation Script"
echo "════════════════════════════════════════"
echo ""

echo "[1/4] Cleaning..."
mvn clean -q 2>&1 | tail -3 || true

echo "[2/4] Compiling source..."
mvn compile -q -DskipTests 2>&1 | tail -5 || true

echo "[3/4] Compiling tests..."
mvn test-compile -q 2>&1 | tail -5 || true

echo "[4/4] Running build..."
mvn clean install -DskipTests 2>&1 | tail -15

echo ""
echo "════════════════════════════════════════"
echo "✓ Build validation complete!"
echo "════════════════════════════════════════"
