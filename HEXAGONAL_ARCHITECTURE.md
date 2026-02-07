# Microserviço de Usuários - Arquitetura Hexagonal

## 📋 Visão Geral

Microserviço de gerenciamento de usuários implementado com **Arquitetura Hexagonal (Ports and Adapters)**, seguindo princípios de **DDD**, **Clean Architecture** e **SOLID**, desenvolvido com metodologia **TDD**.

### Stack Tecnológica

- **Java 21** ☕
- **Spring Boot 3.4.1** 🍃
- **PostgreSQL 15** 🐘
- **Maven** 📦
- **MapStruct 1.6.0** (mapeamento de objetos)
- **Testcontainers** (testes de integração)
- **Flyway** (migração de banco de dados)
- **RFC 7807** (Problem Details for HTTP APIs)
- **OpenAPI 3.0 / Swagger** (documentação de API)

---

## 🏗️ Arquitetura

### Estrutura Hexagonal

```
com.viafluvial.srvusuario/
├── domain/                          # NÚCLEO - Regras de Negócio Puras
│   ├── model/                       # Entidades de domínio (POJOs puros)
│   │   ├── User.java               # Entidade User com invariantes
│   │   ├── UserType.java           # Value Object
│   │   └── UserStatus.java         # Value Object com regras de transição
│   ├── service/                     # Serviços de domínio (regras complexas)
│   └── event/                       # Eventos de domínio
│
├── application/                     # CASOS DE USO
│   ├── port/
│   │   ├── in/                     # Portas de ENTRADA (interfaces de use cases)
│   │   │   ├── CreateUserUseCase.java
│   │   │   └── GetUserUseCase.java
│   │   └── out/                    # Portas de SAÍDA (interfaces para infra)
│   │       └── UserRepositoryPort.java
│   ├── usecase/                    # Implementação dos use cases
│   │   ├── CreateUserUseCaseImpl.java
│   │   └── GetUserUseCaseImpl.java
│   ├── dto/                        # DTOs internos (Command/Response)
│   │   ├── UserCommand.java
│   │   ├── UserResponse.java
│   │   └── UserQuery.java
│   └── mapper/                     # Mappers application ↔ domain
│       └── UserApplicationMapper.java
│
├── adapters/                       # ADAPTADORES
│   ├── in/                         # Adaptadores de ENTRADA
│   │   └── web/                    # Adapter REST
│   │       ├── controller/         # Controllers REST
│   │       │   └── UserController.java
│   │       ├── dto/                # DTOs da API
│   │       │   ├── CreateUserRequest.java
│   │       │   └── UserApiResponse.java
│   │       ├── mapper/             # Mapper API ↔ Application
│   │       │   └── UserWebMapper.java
│   │       └── exception/          # Exception Handler
│   │           └── GlobalExceptionHandler.java (RFC 7807)
│   │
│   └── out/                        # Adaptadores de SAÍDA
│       └── persistence/            # Adapter de Persistência
│           ├── UserRepositoryAdapter.java (implementa Port OUT)
│           ├── entity/             # Entidades JPA
│           │   └── UserJpaEntity.java
│           ├── repository/         # Spring Data JPA
│           │   └── UserJpaRepository.java
│           └── mapper/             # Mapper Domain ↔ JPA
│               └── UserPersistenceMapper.java
│
├── common/                         # CROSSCUTTING CONCERNS
│   ├── error/                      # Exceções e códigos de erro
│   │   ├── DomainException.java
│   │   ├── ErrorCode.java
│   │   ├── ResourceNotFoundException.java
│   │   └── UniqueConstraintViolationException.java
│   ├── logging/                    # Logging e correlação
│   │   └── CorrelationIdFilter.java
│   └── id/                         # Geração de IDs
│       └── CorrelationIdGenerator.java
│
└── config/                         # Configurações Spring
```

---

## 🎯 Princípios Arquiteturais

### 1. Hexagonal Architecture (Ports and Adapters)

- **Domínio no centro**: Lógica de negócio isolada, sem dependências de frameworks
- **Portas**: Interfaces que definem contratos
  - **IN**: O que a aplicação oferece (use cases)
  - **OUT**: O que a aplicação precisa (repositories, gateways)
- **Adapters**: Implementações concretas das portas
  - **IN**: REST controllers, mensageria
  - **OUT**: JPA repositories, APIs externas

### 2. Domain-Driven Design (DDD)

- **Entidades de domínio**: `User` com regras de negócio e invariantes
- **Value Objects**: `UserType`, `UserStatus` com lógica encapsulada
- **Agregados**: User é um agregado raiz
- **Domain Services**: Para lógica que não pertence a uma entidade específica
- **Domain Events**: Para comunicação entre agregados (preparado para expansão)

### 3. Clean Architecture

- **Independência de frameworks**: Domínio não conhece Spring, JPA, etc.
- **Testabilidade**: Domínio e use cases testáveis sem infraestrutura
- **Regra de dependência**: Dependências apontam sempre para dentro
  - Domain não depende de ninguém
  - Application depende de Domain
  - Adapters dependem de Application e Domain

### 4. SOLID

- **Single Responsibility**: Cada classe tem uma única razão para mudar
- **Open/Closed**: Extensível via novos adapters sem modificar o core
- **Liskov Substitution**: Implementações de ports são intercambiáveis
- **Interface Segregation**: Ports específicos e coesos
- **Dependency Inversion**: Aplicação depende de abstrações (ports), não de implementações

---

## 🔄 Fluxo de Requisição

```
HTTP Request (JSON)
    ↓
[UserController] ← Adapter IN (REST)
    ↓ valida e mapeia (DTO API → Command)
[CreateUserUseCase] ← Port IN (interface)
    ↓ implementado por
[CreateUserUseCaseImpl] ← Use Case
    ↓ valida regras de negócio
[User (domain model)] ← Domínio Puro
    ↓ salva via
[UserRepositoryPort] ← Port OUT (interface)
    ↓ implementado por
[UserRepositoryAdapter] ← Adapter OUT (Persistence)
    ↓ converte (Domain → JPA Entity)
[UserJpaRepository] ← Spring Data
    ↓
PostgreSQL Database
```

---

## 🧪 Testes (TDD)

### 1. Testes de Domínio (Unit)
**Arquivo**: `domain/model/UserTest.java`

```java
@Test
@DisplayName("Deve impedir transição de status inválida")
void shouldPreventInvalidStatusTransition() {
    User user = User.builder()
        .email("test@example.com")
        .status(UserStatus.PENDING)
        .build();
    
    assertThatThrownBy(() -> user.changeStatus(UserStatus.ACTIVE))
        .isInstanceOf(InvalidStatusTransitionException.class);
}
```

### 2. Testes de Use Case (Unit com Mocks)
**Arquivo**: `application/usecase/CreateUserUseCaseImplTest.java`

```java
@Test
@DisplayName("Deve criar usuário com sucesso")
void shouldCreateUserSuccessfully() {
    when(userRepository.existsByEmail(any())).thenReturn(false);
    when(userRepository.save(any())).thenReturn(user);
    
    UserResponse result = createUserUseCase.create(command);
    
    assertThat(result).isNotNull();
    verify(userRepository).save(any());
}
```

### 3. Testes de Adapter REST (WebMvcTest)
**Arquivo**: `adapters/in/web/controller/UserControllerTest.java`

```java
@Test
@DisplayName("POST /users - Deve retornar 409 quando email duplicado")
void shouldReturn409WhenEmailAlreadyExists() throws Exception {
    when(createUserUseCase.create(any()))
        .thenThrow(new UniqueConstraintViolationException("Email", "test@example.com"));
    
    mockMvc.perform(post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isConflict())
        .andExpect(content().contentType("application/problem+json"));
}
```

### 4. Testes de Integração (Testcontainers)
**Arquivo**: `adapters/out/persistence/UserRepositoryAdapterIntegrationTest.java`

```java
@Test
@DisplayName("Deve salvar e recuperar usuário")
void shouldSaveAndRetrieveUser() {
    User user = User.builder()
        .email("test@example.com")
        .build();
    
    User saved = repositoryAdapter.save(user);
    Optional<User> retrieved = repositoryAdapter.findById(saved.getId());
    
    assertThat(retrieved).isPresent();
}
```

---

## 🚀 Como Rodar

### Pré-requisitos

- **Java 21** instalado
- **Maven 3.9+** instalado
- **Docker** instalado e rodando (para Testcontainers)
- **PostgreSQL 15** (ou usar Docker Compose fornecido)

### 1. Clonar o repositório

```bash
git clone https://github.com/eldersonjls/srv-usuario-poc.git
cd srv-usuario-poc
```

### 2. Subir banco de dados (Docker Compose)

```bash
docker-compose up -d
```

Isso iniciará PostgreSQL na porta `5432`.

### 3. Compilar o projeto

```bash
mvnw clean compile
```

### 4. Rodar testes

```bash
# Todos os testes (incluindo Testcontainers)
mvnw test

# Teste específico
mvnw test -Dtest=UserTest

# Teste de integração
mvnw test -Dtest=UserRepositoryAdapterIntegrationTest
```

### 5. Rodar a aplicação

```bash
mvnw spring-boot:run
```

A aplicação estará disponível em: **http://localhost:8080**

### 6. Acessar documentação da API

```bash
# Swagger UI
http://localhost:8080/api/v1/swagger-ui.html

# OpenAPI JSON
http://localhost:8080/api/v1/api-docs
```

### 7. Testar endpoints

```bash
# Criar usuário
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "SenhaForte123",
    "fullName": "João Silva",
    "phone": "(92) 98765-4321",
    "userType": "PASSENGER"
  }'

# Buscar usuário por ID
curl http://localhost:8080/api/v1/users/{id}

# Buscar por email
curl http://localhost:8080/api/v1/users/email/joao@example.com
```

---

## 📄 RFC 7807 - Problem Details

Todos os erros retornam formato padronizado:

```json
{
  "type": "https://api.viafluvial.com/problems/email_already_exists",
  "title": "Email já cadastrado",
  "status": 409,
  "detail": "Email 'test@example.com' já está em uso",
  "errorCode": "EMAIL_ALREADY_EXISTS",
  "timestamp": "2024-01-20T15:30:00Z",
  "correlationId": "a1b2c3d4e5f6"
}
```

### Códigos de Status HTTP

| Status | Cenário |
|--------|---------|
| **201 Created** | Recurso criado com sucesso |
| **200 OK** | Consulta bem-sucedida |
| **400 Bad Request** | Validação falhou |
| **404 Not Found** | Recurso não encontrado |
| **409 Conflict** | Email duplicado |
| **500 Internal Error** | Erro inesperado |

---

## 🔍 Observabilidade

### Correlation ID

Todas as requisições recebem um `X-Correlation-Id`:

```bash
curl -H "X-Correlation-Id: custom-trace-123" \
  http://localhost:8080/api/v1/users/{id}
```

Se não fornecido, um ID é gerado automaticamente e retornado no header.

### Health Checks

```bash
# Liveness probe
curl http://localhost:8080/actuator/health/liveness

# Readiness probe
curl http://localhost:8080/actuator/health/readiness

# Métricas Prometheus
curl http://localhost:8080/actuator/prometheus
```

---

## 📊 Decisões Arquiteturais

### Por que Hexagonal Architecture?

1. **Testabilidade**: Domínio e use cases testáveis sem infraestrutura
2. **Flexibilidade**: Fácil trocar adapters (REST → GraphQL, JPA → MongoDB)
3. **Manutenibilidade**: Mudanças em frameworks não afetam regras de negócio
4. **Escalabilidade**: Use cases podem ser distribuídos em microserviços

### Separação de Modelos

- **Domain Model** (`User`): POJO puro com regras de negócio
- **JPA Entity** (`UserJpaEntity`): Anotações JPA, otimizações de persistência
- **API DTOs** (`CreateUserRequest`, `UserApiResponse`): Contratos de API
- **Application DTOs** (`UserCommand`, `UserResponse`): DTOs internos

**Vantagem**: Cada camada evolui independentemente sem impactar outras.

### MapStruct para Mapeamento

Evita código boilerplate manual e gera código otimizado em compile-time:

- `UserWebMapper`: API ↔ Application
- `UserApplicationMapper`: Application ↔ Domain
- `UserPersistenceMapper`: Domain ↔ JPA

### Testcontainers vs H2

Usamos **Testcontainers** para testes de integração porque:
- Testa contra PostgreSQL real (mesmo dialeto SQL)
- Detecta incompatibilidades de schema
- Valida índices e constraints

---

## 📚 Referências

- [Alistair Cockburn - Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Robert C. Martin - Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Eric Evans - Domain-Driven Design](https://www.domainlanguage.com/ddd/)
- [RFC 7807 - Problem Details](https://datatracker.ietf.org/doc/html/rfc7807)
- [Spring Boot Best Practices](https://docs.spring.io/spring-boot/docs/current/reference/html/)

---

## 👥 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

**Importante**: Mantenha a arquitetura hexagonal e sempre escreva testes!

---

## 📝 Licença

Este projeto é parte do ecossistema **ViáFluvial** e está sob licença proprietária.

---

## 📞 Contato

**Elderson Silva** - [@eldersonjls](https://github.com/eldersonjls)

**Projeto**: [srv-usuario-poc](https://github.com/eldersonjls/srv-usuario-poc)
