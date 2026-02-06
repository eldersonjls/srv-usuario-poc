#!/bin/bash

# Script para diagnóstico de compilação - Java 21 + Lombok 1.18.30
set -e

echo "========================================"
echo "Diagnóstico de Compilação Maven"
echo "========================================"
echo ""

cd /workspaces/srv-usuario-poc

# Verificar configurações
echo "[1/5] Verificando configurações..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Java: $(java -version 2>&1 | head -1)"
echo "Maven: $(mvn -version | head -1)"
echo ""

# Verificar pom.xml
echo "[2/5] Verificando pom.xml..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
grep -A 2 "java.version" pom.xml | grep -v "^--"
grep "lombok.*version" pom.xml | head -2
grep "maven-compiler-plugin" pom.xml -A 1 | head -2
echo ""

# Verificar .mvn config
echo "[3/5] Verificando .mvn/jvm.config..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
cat .mvn/jvm.config || echo "Arquivo não encontrado"
echo ""

# Limpar cache
echo "[4/5] Limpando cache Maven..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
mvn clean --quiet 2>&1 | tail -5 || true
echo "✓ Cache limpo"
echo ""

# Compilar
echo "[5/5] Compilando projeto..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
mvn compile 2>&1 | tail -20

echo ""
echo "========================================"
echo "✓ Diagnóstico completo!"
echo "========================================"
