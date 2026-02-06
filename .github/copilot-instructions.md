# ViáFluvial User Service - AI Coding Instructions

## Project Overview

This is a **Spring Boot 3.4.1 microservice** (`srv-usuario`) for user management on the ViáFluvial platform. Key characteristics:
- **Java 17** with manual implementation (Lombok removed)
- **Layered architecture**: Domain → Application → Presentation → Infrastructure
- **Database**: PostgreSQL (Supabase)
- **Authentication**: JWT + Spring Security + BCrypt
- **API Documentation**: OpenAPI 3.0 with Swagger UI at `/api/v1/swagger-ui.html`
- **Domain Model**: User, Boatman, Passenger with specialized services

---

## Architecture Patterns

### Layered Structure (Domain-Driven Design)
```
com.viafluvial.srvusuario/
├── domain/entity/          # Core business entities (User, Boatman, Passenger, UserPreference)
├── application/
│   ├── service/           # Business logic (UserService, AuthService, BoatmanService, PassengerService)
│   └── dto/               # Data Transfer Objects (UserDTO, UserCreateDTO, AuthDTO, AuthResponseDTO)
├── presentation/
│   └── controller/        # REST endpoints (UserController, AuthController, BoatmanController)
└── infrastructure/
    ├── config/           # Spring configuration (SecurityConfig, etc.)
    └── repository/       # JPA repositories (UserRepository, BoatmanRepository, etc.)
```

**Key Rule**: DTOs (application layer) map to entities (domain layer). Services handle business logic with `@Transactional`. Controllers validate input with `@Valid`.

### Entity Design Pattern
Entities use **manual builder pattern** (no Lombok). Example pattern from [User.java](User.java):
- All-args constructor + no-args constructor
- Getters/setters for each field
- Static `builder()` method returning a Builder inner class
- Table indexes on frequently queried columns: `email`, `user_type`, `status`, `created_at`

```java
public static Builder builder() {
    return new Builder();
}

public static class Builder {
    // Build method returns User instance
}
```

### DTO Mapping Pattern
Services manually map entities to DTOs:
```java
// In UserService
private UserDTO mapToDTO(User user) {
    return UserDTO.builder()
        .id(user.getId())
        .email(user.getEmail())
        // ... other fields
        .build();
}
```

---

## Critical Developer Workflows

### Build & Compilation
```bash
# Compile only (no tests)
mvn clean compile

# Full build with tests
mvn clean install

# Build Docker image (skip tests)
mvn clean package -DskipTests
docker build -t srv-usuario:1.0.0 .
```

**Important**: Project uses **Java 17**, Maven 3.12.1+, PostgreSQL 15. Lombok has been removed—use manual constructors/builders.

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserControllerTest

# Run tests with coverage
mvn clean test jacoco:report
```

Tests use **H2 in-memory database** for unit tests, **TestContainers** for PostgreSQL integration tests.

### Local Development
1. **Database**: Ensure PostgreSQL/Supabase connection configured in `application.yml`
2. **Start Application**:
   ```bash
   mvn spring-boot:run
   ```
   API runs on `http://localhost:8080/api/v1`
3. **Swagger UI**: `http://localhost:8080/api/v1/swagger-ui.html`

---

## Important Implementation Details

### Password Security
- Uses **BCrypt** (Spring Security) configured in [SecurityConfig.java](SecurityConfig.java)
- Service method: `passwordEncoder.encode()` and `passwordEncoder.matches()`
- Never store plain passwords; always encode before persisting

### User Types & Status
Two enums in [User.java](User.java):
- **UserType**: `PASSENGER`, `BOATMAN`, `ADMIN`, `AGENCY`
- **UserStatus**: `PENDING`, `ACTIVE`, `SUSPENDED`, `INACTIVE`

Default: `status = PENDING`, `emailVerified = false`

### JWT & Authentication
[AuthService.java](AuthService.java) handles registration and authentication:
- Registration creates user + default `UserPreference` record
- Authentication updates `lastLogin` timestamp
- Validates email uniqueness and user status before operations

### Repository Query Methods
Common patterns in [UserRepository.java](UserRepository.java):
```java
findByEmail(String email)           // Optional<User>
existsByEmail(String email)         // boolean
findByUserType(UserType userType)   // List<User>
findAll()                           // List<User>
```

---

## Code Conventions

### Annotations & Validation
- Controllers use `@Valid` on DTO parameters
- Services marked with `@Service` and `@Transactional`
- Endpoints documented with `@Operation`, `@ApiResponse`, `@ApiResponses`
- All DTOs have validation constraints (`@NotBlank`, `@Email`, etc.)

### Error Handling
Services throw `IllegalArgumentException` with descriptive messages:
```java
throw new IllegalArgumentException("Email já está registrado");
throw new IllegalArgumentException("Usuário não encontrado");
```
Controllers return appropriate HTTP status codes (201 Created, 400 Bad Request, 404 Not Found, 409 Conflict).

### Response Format
All endpoints return JSON via `ResponseEntity<DTO>`:
```java
ResponseEntity.status(HttpStatus.CREATED).body(createdUser);  // POST
ResponseEntity.ok(user);                                      // GET
```

---

## Key Files Reference

| File | Purpose |
|------|---------|
| [pom.xml](pom.xml) | Dependencies (Spring Boot 3.4.1, JWT, Spring Security, MapStruct) |
| [application.yml](application.yml) | Database connection, JPA config, Swagger settings |
| [User.java](src/main/java/com/viafluvial/srvusuario/domain/entity/User.java) | Core entity with builder pattern |
| [UserService.java](src/main/java/com/viafluvial/srvusuario/application/service/UserService.java) | CRUD operations & business logic |
| [UserController.java](src/main/java/com/viafluvial/srvusuario/presentation/controller/UserController.java) | REST endpoints |
| [SecurityConfig.java](src/main/java/com/viafluvial/srvusuario/infrastructure/config/SecurityConfig.java) | BCrypt password encoder |

---

## When Adding Features

1. **New Entity**: Create in `domain/entity/`, implement builder pattern, add table indexes
2. **New Service**: Create in `application/service/`, add `@Service`, `@Transactional`
3. **New DTOs**: Create matching input (CreateDTO) and output (DTO) in `application/dto/`
4. **New Repository**: Extend `JpaRepository` in `infrastructure/repository/`
5. **New Endpoint**: Create controller in `presentation/controller/`, document with `@Operation` and `@ApiResponses`
6. **Tests**: Mirror class structure in `src/test/`, use H2 for unit tests, TestContainers for integration tests

---

## Useful References

- API Spec: [API_SPEC.yml](API_SPEC.yml)
- Build Guide: [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md)
- Database Schema: [schema.sql](src/main/resources/db/schema.sql)
- Docker Config: [Dockerfile](Dockerfile)
