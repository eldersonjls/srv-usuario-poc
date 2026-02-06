# 🚀 Microsserviço – Plataforma ViaFluvial  
**Versão:** 0.1.0 – Implementação Integral  

Microsserviço backend desenvolvido em Spring Boot, seguindo arquitetura moderna e padrões avançados de engenharia de software.

Principais conceitos aplicados:

- Domain-Driven Design (DDD)  
- Clean Architecture  
- Hexagonal Architecture (Ports & Adapters)  
- API-First com OpenAPI  
- Código orientado a testes  
- Containerização com Docker  

Este projeto serve como template oficial para os próximos microsserviços da plataforma.

---

## 📐 Arquitetura

Estrutura lógica do projeto:

Domain  
 ├── Entities  
 ├── Value Objects  
 ├── Aggregates  
 ├── Domain Services  
 └── Ports (Interfaces)  

Application  
 ├── Use Cases  
 └── DTOs  

Infrastructure  
 ├── Controllers (REST)  
 ├── Persistence (JPA)  
 ├── External Adapters  
 └── Config  

Bootstrap  
 └── Spring Boot Application  

Princípios adotados:

- Domínio isolado de frameworks  
- Dependências sempre apontam para dentro  
- Comunicação via Ports & Adapters  
- Casos de uso explícitos  
- Contrato da API definido antes da implementação  

---

## 📄 Contrato da API

Arquivo principal:

API_SPEC.yml  

Formato OpenAPI 3 (fonte da verdade da API).

---

## 🧱 Estrutura e Documentação

Arquivos de apoio:

ESTRUTURA_VISUAL.txt  
FILE_INDEX.md  
TECHNICAL_SUMMARY.md  

---

## 🛠️ Stack Tecnológica

- Java 21  
- Spring Boot 3  
- Maven  
- PostgreSQL  
- Docker  
- Docker Compose  
- OpenAPI  
- JUnit  
- Testcontainers  

---

## ▶️ Executando Localmente

Pré-requisitos:

- Java 21  
- Docker  
- Docker Compose  

Execução com Docker:

docker-compose up --build  

Execução sem Docker:

./mvnw spring-boot:run  

---

## ✅ Build e Validação

Scripts disponíveis:

./build.sh  
./test-build.sh  
./validate-build.sh  
./diagnose-build.sh  

Arquivos de controle:

BUILD_CHECKLIST.md  
BUILD_VERIFICATION.md  
VALIDATION_REPORT.md  

---

## 🧪 Testes

Documentação:

TESTING.md  

Executar testes:

./mvnw test  

---

## 📦 Deployment

Guia completo:

DEPLOYMENT.md  

Inclui build da imagem, variáveis de ambiente e Docker Compose.

---

## 📊 Relatórios do Projeto

Arquivos incluídos:

EXECUTIVE_SUMMARY.md  
PROJECT_COMPLETION_SUMMARY.md  
COMPLETION_REPORT.md  
FINAL_FIX_SUMMARY.md  
SESSION_SUMMARY.md  

Esses documentos registram:

- Estado final do código  
- Correções aplicadas  
- Validações  
- Compilação  
- Padronização  

---

## 🧹 Lombok

Projeto totalmente sem Lombok para maior transparência, debug facilitado e compatibilidade futura.

Arquivo:

LOMBOK_REMOVAL_COMPLETE.md  

---

## 📁 Docker

Arquivos principais:

Dockerfile  
docker-compose.yml  

---

## 🎯 Objetivo

Servir como base padrão para todos os microsserviços, garantindo:

- Consistência arquitetural  
- Qualidade de código  
- Facilidade de manutenção  
- Escalabilidade  
- Integração com API Gateway  
- Preparação para CI/CD  

---

## 📌 Padrão para Novos Microsserviços

Todo novo serviço deve seguir:

- API-First  
- DDD  
- Clean Architecture  
- Ports & Adapters  
- Testes automatizados  
- Docker  

---

## 👤 Autor

Elderson Jammer  
Arquitetura & Engenharia de Plataforma
