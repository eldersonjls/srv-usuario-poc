# srv-usuario — ViáFluvial

Microserviço de gerenciamento de usuários da plataforma ViáFluvial (Spring Boot).

**Versão do artefato:** `1.0.0` (ver `pom.xml`)  
**Context path da API:** `/api/v1` (ver `application.yml`)

## Principais links (local)

- Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/v1/v3/api-docs`
- Health: `http://localhost:8080/api/v1/actuator/health`
- Prometheus: `http://localhost:8080/api/v1/actuator/prometheus`

## Stack

- Java **21**
- Spring Boot **3.4.1**
- Maven
- PostgreSQL 15
- Flyway + JPA (Hibernate)
- Spring Security (Resource Server JWT — via `jwk-set-uri`)
- OpenAPI/Swagger UI (springdoc)
- Testes: JUnit 5, H2, Testcontainers

## Arquitetura e estrutura

O projeto segue uma organização por camadas + **Ports & Adapters** (Hexagonal), com foco em separar domínio e regras de negócio da infraestrutura/web.

Estrutura principal (resumo):

```
src/main/java/com/viafluvial/srvusuario/
	adapters/
		in/web/           # Controllers, handlers e contratos web
		out/              # Adapters de saída (quando aplicável)
	application/        # Serviços de aplicação, DTOs e casos de uso
	domain/             # Entidades e regras de negócio
	infrastructure/     # Configurações e integrações (ex.: Spring Security)
	config/             # Configurações adicionais
	common/             # Utilitários e convenções
```

## Contrato da API (OpenAPI)

A especificação usada no build fica em:

- `src/main/resources/openapi/openapi.yaml`

> Observação: existe também o arquivo `API_SPEC.yml` na raiz, usado como referência/documentação do repositório.

## Como executar

### 1) Com Docker Compose (recomendado)

Suba banco + aplicação:

```bash
docker compose up --build
# (ou) docker-compose up --build
```

O `docker-compose.yml` inicializa o Postgres e aplica o schema inicial via `src/main/resources/db/schema.sql`.

### 2) Sem Docker (Maven)

Pré-requisitos:

- Java 21
- Um PostgreSQL acessível (local ou remoto)

Exemplo de execução apontando para um Postgres local (ajuste conforme seu ambiente):

```bash
export SPRING_DATASOURCE_URL='jdbc:postgresql://localhost:5432/postgres'
export SPRING_DATASOURCE_USERNAME='postgres'
export SPRING_DATASOURCE_PASSWORD='postgres'

mvn spring-boot:run
```

## Build e testes

```bash
# Compila (sem testes)
mvn clean compile

# Build completo (com testes)
mvn clean install

# Somente testes
mvn test
```

Scripts auxiliares (opcionais):

- `./build.sh`
- `./test-build.sh`
- `./validate-build.sh`
- `./diagnose-build.sh`

## Segurança (JWT Resource Server)

Por padrão, o profile **dev** desabilita segurança (ver `application-dev.yml`).

Para habilitar segurança em execução local, use:

- `APP_SECURITY_ENABLED=true`
- `APP_SECURITY_JWT_JWK_SET_URI=<url do seu JWKS>`
- (opcional) `APP_SECURITY_JWT_ROLES_CLAIM=roles`

Exemplo:

```bash
export APP_SECURITY_ENABLED=true
export APP_SECURITY_JWT_JWK_SET_URI='https://<seu-dominio>/.well-known/jwks.json'
mvn spring-boot:run
```

## Lombok

O projeto **não usa Lombok** (implementações manuais de construtores/builders/getters/setters).

Documento: `LOMBOK_REMOVAL_COMPLETE.md`

## Documentação adicional

- Guia rápido: `QUICKSTART.md`
- Testes: `TESTING.md`
- Deploy: `DEPLOYMENT.md`
- Visão técnica: `TECHNICAL_SUMMARY.md`

## Autor

Elderson Jammer — Arquitetura & Engenharia de Plataforma
