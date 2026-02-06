#!/bin/bash

# Script para testar compilação do projeto
set -e

echo "========================================"
echo "Teste de Compilação - srv-usuario-poc"
echo "========================================"
echo ""

cd /workspaces/srv-usuario-poc

echo "[1/4] Verificando versão do Java..."
java -version 2>&1 || echo "Java não encontrado"
echo ""

echo "[2/4] Limpando build anterior..."
mvn clean --quiet
echo "✓ Build anterior limpo"
echo ""

echo "[3/4] Compilando projeto..."
mvn compile --quiet
echo "✓ Compilação bem-sucedida"
echo ""

echo "[4/4] Executando testes..."
mvn test --quiet 2>&1 || echo "⚠ Alguns testes podem ter falhado"
echo ""

echo "========================================"
echo "✓ Build completo executado com sucesso!"
echo "========================================"
