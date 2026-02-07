# Guia Completo de Padrões Arquiteturais e de Engenharia de Software
## Microserviço Spring Boot - ViáFluvial User Service

Este documento consolida todos os padrões arquiteturais, de engenharia de software e abordagens de código adotados no projeto `srv-usuario`. Use como referência e prompt para criação de novos microserviços com as mesmas práticas.

---

## 📋 Stack Tecnológica Base

### Core Framework
- **Java 17** (versão LTS com recursos modernos)
- **Spring Boot 3.4.1** (última versão estável)
- **Maven 3.12.1+** (gerenciamento de dependências)
- **PostgreSQL 15+** (banco de dados relacional)

### Dependências Principais
- **Spring Boot Starter Web** - API REST
- **Spring Boot Starter Data JPA** - persistência e ORM
- **Spring Boot Starter Validation** - validação de dados
- **Spring Boot Starter Actuator** - monitoramento e health checks
- **Micrometer Prometheus** - métricas para observabilidade
- **Flyway** - versionamento de schema de banco de dados
- **MapStruct 1.6.0** - mapeamento entre DTOs e entidades
- **Springdoc OpenAPI 2.7.0** - documentação automática de APIs
- **Caffeine Cache** - cache em memória de alto desempenho
- **H2 Database** - testes unitários com banco em memória
- **JUnit 5 + Mockito** - framework de testes

---

## 🏗️ Padrões Arquiteturais

### 1. **Arquitetura em Camadas (Layered Architecture)**

Estrutura clara de separação por responsabilidade seguindo princípios de Domain-Driven Design (DDD):

```
com.viafluvial.srvusuario/
├── domain/                      # Camada de Domínio
│   ├── entity/                  # Entidades de negócio (User, Boatman, Passenger)
│   │   └── converter/           # Conversores JPA customizados (Enum converters)
│   └── exception/               # Exceções de domínio
│
├── application/                 # Camada de Aplicação
│   ├── service/                 # Lógica de negócio e casos de uso
│   ├── dto/                     # Data Transfer Objects (entrada/saída)
│   └── mapper/                  # Interfaces MapStruct para conversão
│
├── presentation/                # Camada de Apresentação
│   └── controller/              # Controllers REST (endpoints HTTP)
│
└── infrastructure/              # Camada de Infraestrutura
    ├── config/                  # Configurações Spring (Cache, Security, etc.)
    ├── repository/              # Interfaces JPA e Specifications
    │   └── spec/                # Specifications para consultas dinâmicas
    ├── exception/               # Exception handlers globais
    └── util/                    # Utilitários de infraestrutura
```

**Regras de Dependência:**
- Domain não depende de nenhuma outra camada (núcleo isolado)
- Application depende apenas de Domain
- Presentation depende de Application
- Infrastructure pode ser usada por todas as camadas (crosscutting)

### 2. **Repository Pattern**

Todos os repositórios estendem interfaces Spring Data JPA:

```java
@Repository
public interface UserRepository extends JpaRepository<User, UUID>, 
                                         JpaSpecificationExecutor<User> {
    
    @QueryHints(@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true"))
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType")
    List<User> findByUserType(User.UserType userType);
}
```

**Características:**
- Uso de `JpaRepository` para operações CRUD básicas
- `JpaSpecificationExecutor` para consultas dinâmicas complexas
- Query methods derivados do nome (e.g., `findByEmail`)
- `@Query` para JPQL customizado quando necessário
- `@QueryHints` para otimizações de cache de segundo nível

### 3. **Specification Pattern**

Para construção de consultas dinâmicas reutilizáveis:

```java
public final class UserSpecifications {
    
    private UserSpecifications() {} // Utility class
    
    public static Specification<User> emailContainsIgnoreCase(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isBlank()) {
                return cb.conjunction(); // WHERE 1=1 (neutro)
            }
            return cb.like(cb.lower(root.get("email")), 
                          "%" + email.trim().toLowerCase() + "%");
        };
    }
    
    public static Specification<User> hasUserType(User.UserType userType) {
        return (root, query, cb) -> userType == null 
            ? cb.conjunction() 
            : cb.equal(root.get("userType"), userType);
    }
}
```

**Vantagens:**
- Composição de filtros usando `.and()` e `.or()`
- Type-safe queries (sem strings SQL)
- Reutilização de lógica de consulta
- Consultas dinâmicas baseadas em parâmetros opcionais

**Uso nos Serviços:**
```java
Specification<User> spec = Specification.where(emailContainsIgnoreCase(email))
    .and(hasUserType(userType))
    .and(hasStatus(status));
    
Page<User> users = userRepository.findAll(spec, pageable);
```

### 4. **Service Layer Pattern**

Serviços contêm toda a lógica de negócio e orquestração:

```java
@Service
@Transactional
public class UserService {
    
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    
    @Caching(evict = {
        @CacheEvict(value = USERS_CACHE, allEntries = true),
        @CacheEvict(value = USER_BY_EMAIL_CACHE, key = "#dto.email")
    })
    public UserDTO createUser(UserCreateDTO dto) {
        log.info("Criando usuário com email: {}", dto.getEmail());
        
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException(dto.getEmail());
        }
        
        User user = userMapper.toEntity(dto);
        User saved = userRepository.save(user);
        
        return userMapper.toDTO(saved);
    }
}
```

**Características Obrigatórias:**
- Anotação `@Service` para componente Spring
- `@Transactional` no nível de classe (read-write por padrão)
- `@Transactional(readOnly = true)` em métodos de leitura
- Injeção de dependências via construtor (não `@Autowired` em campos)
- Logger SLF4J para rastreabilidade
- Validações de negócio com exceções customizadas
- Uso de cache annotations (`@Cacheable`, `@CacheEvict`)
- Conversão entre entidades e DTOs via mappers

### 5. **DTO Pattern (Data Transfer Object)**

DTOs separados para entrada e saída:

```java
// DTO de entrada (criação)
@Schema(description = "Dados para criação de usuário")
public class UserCreateDTO {
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    private String password;
    
    @NotBlank(message = "Nome completo é obrigatório")
    private String fullName;
    
    // Getters, setters, builder
}

// DTO de saída (resposta)
@Schema(description = "Dados completos de usuário")
public class UserDTO {
    
    @Schema(description = "ID único", example = "550e8400-...")
    private UUID id;
    
    @Schema(description = "Email", example = "user@example.com")
    private String email;
    
    // Sem senha! Nunca expor credenciais
    
    private String fullName;
    private LocalDateTime createdAt;
    
    // Getters, setters, builder
}
```

**Princípios:**
- DTOs de entrada (`*CreateDTO`, `*UpdateDTO`) com validações Bean Validation
- DTOs de saída sem campos sensíveis (senhas, tokens internos)
- Documentação OpenAPI com `@Schema`
- Builder pattern para construção fluente
- Validações declarativas: `@NotBlank`, `@Email`, `@Size`, `@Pattern`

### 6. **Mapper Pattern com MapStruct**

Conversão automática entre entidades e DTOs:

```java
@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
    
    @Mapping(target = "password", ignore = true)
    UserDTO toDTO(User user);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    User toEntity(UserCreateDTO dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    void updateEntity(@MappingTarget User user, UserDTO dto);
}
```

**Configurações:**
- `componentModel = SPRING` - MapStruct gera beans Spring
- `unmappedTargetPolicy = IGNORE` - ignora campos não mapeados
- `nullValuePropertyMappingStrategy = IGNORE` - não sobrescreve com nulls
- `@Mapping` para customizações (valores padrão, expressões, ignore)
- `@MappingTarget` para atualização parcial de entidades

---

## 🎯 Padrões de Código Java

### 1. **Builder Pattern Manual (sem Lombok)**

Todas as entidades e DTOs implementam builder pattern manual:

```java
public class User {
    
    private UUID id;
    private String email;
    private String passwordHash;
    // ... outros campos
    
    // Construtor sem argumentos (JPA)
    public User() {}
    
    // Construtor com todos os argumentos
    public User(UUID id, String email, String passwordHash, ...) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        // ...
    }
    
    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    // ...
    
    // Builder estático
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private UUID id;
        private String email;
        private String passwordHash;
        // ... outros campos
        
        public Builder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        // ... outros métodos
        
        public User build() {
            return new User(id, email, passwordHash, ...);
        }
    }
}
```

**Por que não Lombok?**
- Controle total sobre geração de código
- Evita problemas de compatibilidade com annotation processors
- Facilita debugging e navegação no código
- Mais transparente para ferramentas de análise estática

### 2. **Enums com Converters Customizados**

Enums case-insensitive para robustez de API:

```java
// Enum interno à entidade
public class User {
    
    public enum UserType {
        PASSENGER, BOATMAN, AGENCY, ADMIN
    }
    
    public enum UserStatus {
        PENDING, APPROVED, ACTIVE, BLOCKED
    }
}

// Converter abstrato genérico
public abstract class AbstractCaseInsensitiveEnumConverter<E extends Enum<E>> 
    implements AttributeConverter<E, String> {
    
    private final Class<E> enumClass;
    
    protected AbstractCaseInsensitiveEnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }
    
    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute == null ? null : attribute.name().toUpperCase();
    }
    
    @Override
    public E convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        return Enum.valueOf(enumClass, dbData.trim().toUpperCase());
    }
}

// Converter específico
@Converter
public class UserTypeConverter extends AbstractCaseInsensitiveEnumConverter<User.UserType> {
    public UserTypeConverter() {
        super(User.UserType.class);
    }
}
```

**Uso na Entidade:**
```java
@Column(nullable = false, length = 20)
@Convert(converter = UserTypeConverter.class)
private UserType userType;
```

### 3. **Indexação de Banco de Dados**

Índices estratégicos para otimização de consultas:

```java
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_email", columnList = "email"),
    @Index(name = "idx_users_user_type", columnList = "user_type"),
    @Index(name = "idx_users_status", columnList = "status"),
    @Index(name = "idx_users_created_at", columnList = "created_at")
})
public class User {
    // ...
}
```

**Critérios para Índices:**
- Campos de busca frequente (email, CPF, CNPJ)
- Campos de filtro (status, tipo de usuário)
- Campos de ordenação (created_at, updated_at)
- Colunas únicas já têm índice automático

### 4. **UUID como Identificador**

Uso de UUIDs para IDs distribuídos e seguros:

```java
@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;
```

**Vantagens:**
- Evita sequências centralizadas (escalabilidade)
- Dificulta enumeração de recursos (segurança)
- Suporta geração client-side
- Facilita merge de bancos de dados

### 5. **Timestamps Automáticos**

Auditoria com timestamps de criação e atualização:

```java
@Column(name = "created_at", nullable = false, updatable = false)
private LocalDateTime createdAt = LocalDateTime.now();

@Column(name = "updated_at")
private LocalDateTime updatedAt = LocalDateTime.now();
```

**Alternativa com JPA Callbacks:**
```java
@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
}

@PreUpdate
protected void onUpdate() {
    updatedAt = LocalDateTime.now();
}
```

---

## 🎛️ Padrões de Infraestrutura

### 1. **Cache em Memória com Caffeine**

Configuração centralizada de cache:

```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    public static final String USERS_CACHE = "users";
    public static final String USER_BY_EMAIL_CACHE = "usersByEmail";
    public static final String BOATMEN_CACHE = "boatmen";
    public static final String PASSENGERS_CACHE = "passengers";
    
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(
            USERS_CACHE, USER_BY_EMAIL_CACHE, BOATMEN_CACHE, PASSENGERS_CACHE
        );
        manager.setCaffeine(caffeineCacheBuilder());
        return manager;
    }
    
    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
            .initialCapacity(100)
            .maximumSize(500)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .recordStats();
    }
}
```

**Uso nos Serviços:**
```java
@Cacheable(value = CacheConfig.USERS_CACHE, key = "#id")
@Transactional(readOnly = true)
public UserDTO getUserById(UUID id) {
    // ...
}

@CacheEvict(value = CacheConfig.USERS_CACHE, allEntries = true)
public UserDTO updateUser(UUID id, UserDTO dto) {
    // ...
}
```

### 2. **Trace ID para Correlação de Logs**

Filtro para injetar trace ID em todas as requisições:

```java
@Component
public class TraceIdFilter extends OncePerRequestFilter {
    
    public static final String TRACE_ID_HEADER = "X-Trace-Id";
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) 
        throws ServletException, IOException {
        
        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        
        MDC.put("traceId", traceId);
        response.setHeader(TRACE_ID_HEADER, traceId);
        
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("traceId");
        }
    }
}
```

**Configuração de Logs (logback-spring.xml):**
```xml
<pattern>%d{yyyy-MM-dd HH:mm:ss} [%X{traceId}] %-5level %logger{36} - %msg%n</pattern>
```

### 3. **Exception Handling Global**

Handler centralizado para tratamento de exceções:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error("User Not Found")
            .message(ex.getMessage())
            .traceId(MDC.get("traceId"))
            .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException ex) {
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.CONFLICT.value())
            .error("Duplicate Email")
            .message(ex.getMessage())
            .traceId(MDC.get("traceId"))
            .build();
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField, 
                FieldError::getDefaultMessage
            ));
        
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Validation Failed")
            .message("Campos inválidos")
            .errors(errors)
            .traceId(MDC.get("traceId"))
            .build();
        
        return ResponseEntity.badRequest().body(error);
    }
}
```

### 4. **Flyway para Versionamento de Schema**

Migrações SQL versionadas:

```
src/main/resources/db/migration/
├── V1__init_schema.sql           # Schema inicial
├── V2__seed_fictitious_data.sql  # Dados de teste
└── V3__seed_passenger_users.sql  # Dados específicos
```

**Convenção de Nomenclatura:**
- `V{version}__{description}.sql` - Migrações versionadas
- `R__{description}.sql` - Scripts repetíveis
- Usar snake_case para descrições

**Configuração (application.yml):**
```yaml
spring:
  flyway:
    baseline-on-migrate: true
    baseline-version: 0
    locations: classpath:db/migration
```

### 5. **Paginação e Ordenação**

Uso de Spring Data Pageable:

```java
@GetMapping
public ResponseEntity<PagedResponse<UserDTO>> listUsers(
    @RequestParam(required = false) String email,
    @RequestParam(required = false) User.UserType userType,
    @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) 
    Pageable pageable) {
    
    PagedResponse<UserDTO> response = userService.listUsers(email, userType, pageable);
    return ResponseEntity.ok(response);
}
```

**Configuração (application.yml):**
```yaml
spring:
  data:
    web:
      pageable:
        one-indexed-parameters: true  # Páginas começam em 1
        default-page-size: 20
        max-page-size: 200
```

**DTO de Resposta Paginada:**
```java
public class PagedResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    // Getters, setters, builder
}
```

---

## 📡 Padrões de API REST

### 1. **Estrutura de Endpoints**

Convenções RESTful consistentes:

```
POST   /users              - Criar usuário
GET    /users              - Listar usuários (paginado)
GET    /users/{id}         - Obter usuário por ID
PUT    /users/{id}         - Atualizar usuário completo
PATCH  /users/{id}         - Atualização parcial
DELETE /users/{id}         - Remover usuário
GET    /users/me           - Obter usuário corrente
GET    /users/email/{email} - Buscar por email
POST   /users/{id}/verify  - Ação específica
```

**Regras:**
- Substantivos no plural (users, boatmen, passengers)
- Recursos aninhados quando apropriado (`/users/{id}/preferences`)
- Ações não-CRUD como sub-recursos (`/verify`, `/approve`)
- Query parameters para filtros e paginação

### 2. **Códigos de Status HTTP**

Uso semântico e consistente:

| Código | Cenário | Uso |
|--------|---------|-----|
| **200 OK** | Leitura bem-sucedida | GET, PUT, PATCH |
| **201 Created** | Recurso criado | POST |
| **204 No Content** | Exclusão bem-sucedida | DELETE |
| **400 Bad Request** | Validação falhou | Campos inválidos |
| **401 Unauthorized** | Autenticação necessária | Token ausente/inválido |
| **403 Forbidden** | Sem permissão | Autorização negada |
| **404 Not Found** | Recurso não existe | ID inválido |
| **409 Conflict** | Conflito de estado | Email duplicado |
| **500 Internal Error** | Erro não tratado | Exceção inesperada |

### 3. **Documentação OpenAPI Completa**

Anotações Swagger em todos os endpoints:

```java
@RestController
@RequestMapping("/users")
@Tag(
    name = "Usuários",
    description = "APIs do contexto de Identidade (IAM) para cadastro, consulta " +
                  "e manutenção de usuários. Inclui listagem com paginação/filtros " +
                  "e consulta do usuário corrente (/me)."
)
public class UserController {
    
    @PostMapping
    @Operation(
        summary = "Criar usuário",
        description = "O que faz: cria um usuário base no IAM.\n" +
            "Quando usar: no cadastro inicial do usuário (antes de criar perfis " +
            "como Passageiro/Barqueiro/Agência/Admin).\n" +
            "Observações: o e-mail deve ser único; valores padrão (ex.: status inicial) " +
            "são definidos pelo serviço; em erro retorna ErrorResponse com traceId " +
            "para correlação."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Sucesso: usuário criado (retorna o usuário criado)",
            content = @Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = UserDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Erro: requisição inválida (validação de campos/formatos)"
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Erro: conflito (já existe usuário com o e-mail informado)"
        )
    })
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserCreateDTO dto) {
        UserDTO created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
```

**Configuração Swagger (application.yml):**
```yaml
springdoc:
  api-docs:
    path: /api/v1/api-docs
  swagger-ui:
    path: /api/v1/swagger-ui.html
    operations-sorter: alpha
    tags-sorter: alpha
  show-actuator: true
```

### 4. **Validação com Bean Validation**

Validações declarativas em DTOs:

```java
public class UserCreateDTO {
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, max = 100, message = "Senha deve ter entre 8 e 100 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
        message = "Senha deve conter letras maiúsculas, minúsculas e números"
    )
    private String password;
    
    @NotBlank(message = "Nome completo é obrigatório")
    @Size(max = 255, message = "Nome não pode exceder 255 caracteres")
    private String fullName;
    
    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "^\\(\\d{2}\\) \\d{4,5}-\\d{4}$", 
             message = "Telefone deve estar no formato (XX) XXXXX-XXXX")
    private String phone;
    
    @NotNull(message = "Tipo de usuário é obrigatório")
    private User.UserType userType;
}
```

**Annotations Disponíveis:**
- `@NotNull`, `@NotBlank`, `@NotEmpty`
- `@Size(min=, max=)`, `@Min`, `@Max`
- `@Email`, `@Pattern(regexp=)`
- `@Past`, `@Future`, `@PastOrPresent`
- `@Positive`, `@PositiveOrZero`

---

## 🧪 Padrões de Testes

### 1. **Estrutura de Testes**

Espelhamento da estrutura de produção:

```
src/test/java/com/viafluvial/srvusuario/
├── SrvUsuarioApplicationTest.java       # Teste de contexto
├── application/
│   └── service/
│       ├── UserServiceTest.java         # Testes unitários
│       └── PassengerServiceTest.java
├── domain/
│   └── entity/
│       └── converter/
│           └── UserStatusConverterTest.java
└── presentation/
    └── controller/
        ├── UserControllerTest.java      # Testes de integração
        └── BoatmanControllerTest.java
```

### 2. **Testes Unitários de Serviço**

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do UserService")
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserMapper userMapper;
    
    @InjectMocks
    private UserService userService;
    
    private UserCreateDTO userCreateDTO;
    private User user;
    
    @BeforeEach
    void setup() {
        userCreateDTO = UserCreateDTO.builder()
            .email("test@example.com")
            .password("password123")
            .fullName("Test User")
            .phone("(92) 98765-4321")
            .userType(User.UserType.PASSENGER)
            .build();
        
        user = User.builder()
            .id(UUID.randomUUID())
            .email("test@example.com")
            .passwordHash("hashedPassword")
            .fullName("Test User")
            .userType(User.UserType.PASSENGER)
            .status(User.UserStatus.PENDING)
            .build();
    }
    
    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void testCreateUserSuccess() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userMapper.toEntity(any())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any())).thenReturn(new UserDTO());
        
        // Act
        UserDTO result = userService.createUser(userCreateDTO);
        
        // Assert
        assertThat(result).isNotNull();
        verify(userRepository).existsByEmail(userCreateDTO.getEmail());
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao criar usuário com email duplicado")
    void testCreateUserWithDuplicateEmail() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        
        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(userCreateDTO))
            .isInstanceOf(DuplicateEmailException.class)
            .hasMessageContaining("já está registrado");
        
        verify(userRepository, never()).save(any());
    }
}
```

### 3. **Testes de Integração de Controller**

```java
@WebMvcTest(UserController.class)
@DisplayName("Testes de integração do UserController")
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @DisplayName("POST /users deve criar usuário e retornar 201")
    void testCreateUser() throws Exception {
        // Arrange
        UserCreateDTO createDTO = UserCreateDTO.builder()
            .email("test@example.com")
            .password("password123")
            .fullName("Test User")
            .phone("(92) 98765-4321")
            .userType(User.UserType.PASSENGER)
            .build();
        
        UserDTO responseDTO = UserDTO.builder()
            .id(UUID.randomUUID())
            .email("test@example.com")
            .fullName("Test User")
            .status(User.UserStatus.PENDING)
            .build();
        
        when(userService.createUser(any())).thenReturn(responseDTO);
        
        // Act & Assert
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.status").value("PENDING"));
    }
    
    @Test
    @DisplayName("POST /users com dados inválidos deve retornar 400")
    void testCreateUserValidationFailure() throws Exception {
        // Arrange - DTO inválido (sem email)
        UserCreateDTO invalid = UserCreateDTO.builder()
            .password("password123")
            .fullName("Test User")
            .build();
        
        // Act & Assert
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
            .andExpect(status().isBadRequest());
    }
}
```

### 4. **Configuração de Banco de Dados de Testes**

**application-test.yml:**
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  
  flyway:
    enabled: false  # Desabilitar em testes unitários
```

### 5. **Padrão AAA (Arrange-Act-Assert)**

Todos os testes seguem estrutura clara:

```java
@Test
void testExample() {
    // Arrange - Preparação
    UserCreateDTO input = UserCreateDTO.builder()
        .email("test@example.com")
        .build();
    
    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    
    // Act - Execução
    UserDTO result = userService.createUser(input);
    
    // Assert - Verificação
    assertThat(result).isNotNull();
    assertThat(result.getEmail()).isEqualTo("test@example.com");
    verify(userRepository).save(any());
}
```

---

## 🐳 Padrões de Containerização

### 1. **Multi-stage Dockerfile**

Build otimizado para produção:

```dockerfile
# Estágio 1: Build
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /build

COPY pom.xml .
COPY src src

RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests

# Estágio 2: Runtime
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=builder /build/target/srv-usuario-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD java -cp /app/app.jar org.springframework.boot.loader.JarLauncher || exit 1
```

**Vantagens:**
- Imagem final menor (sem Maven e dependências de build)
- Camadas separadas para cache eficiente
- Healthcheck integrado ao Docker
- JDK otimizado da Eclipse Temurin

### 2. **Docker Compose para Desenvolvimento**

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: viafluvial
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5
  
  srv-usuario:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/viafluvial
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
    depends_on:
      postgres:
        condition: service_healthy

volumes:
  postgres_data:
```

---

## 📊 Padrões de Observabilidade

### 1. **Actuator para Health Checks**

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
      base-path: /actuator
  
  endpoint:
    health:
      show-details: when-authorized
      probes:
        enabled: true
  
  metrics:
    export:
      prometheus:
        enabled: true
```

**Endpoints Expostos:**
- `/actuator/health` - Status da aplicação
- `/actuator/health/liveness` - Liveness probe (K8s)
- `/actuator/health/readiness` - Readiness probe (K8s)
- `/actuator/metrics` - Métricas detalhadas
- `/actuator/prometheus` - Métricas formato Prometheus

### 2. **Logging Estruturado**

**Padrão de Logs:**
```java
@Service
public class UserService {
    
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    
    public UserDTO createUser(UserCreateDTO dto) {
        log.info("Criando usuário com email: {}", dto.getEmail());
        
        try {
            // lógica
            log.info("Usuário criado com sucesso: id={}, email={}", 
                    saved.getId(), saved.getEmail());
            return result;
        } catch (Exception e) {
            log.error("Erro ao criar usuário: email={}", dto.getEmail(), e);
            throw e;
        }
    }
}
```

**Níveis de Log:**
- `ERROR` - Erros críticos que exigem ação
- `WARN` - Situações anormais não críticas
- `INFO` - Eventos importantes do fluxo (criação, atualização)
- `DEBUG` - Informações detalhadas para troubleshooting

---

## 🔒 Padrões de Segurança

### 1. **Tratamento de Senhas**

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // 12 rounds = bom equilíbrio
    }
}

// Uso no serviço
@Service
public class AuthService {
    
    private final PasswordEncoder passwordEncoder;
    
    public UserDTO register(UserCreateDTO dto) {
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        
        User user = User.builder()
            .email(dto.getEmail())
            .passwordHash(hashedPassword)
            .build();
        
        // salvar
    }
    
    public boolean authenticate(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
```

### 2. **Exceções Customizadas**

```java
// Exceção base
public abstract class DomainException extends RuntimeException {
    protected DomainException(String message) {
        super(message);
    }
}

// Exceções específicas
public class UserNotFoundException extends DomainException {
    public UserNotFoundException(UUID id) {
        super(String.format("Usuário não encontrado: id=%s", id));
    }
}

public class DuplicateEmailException extends DomainException {
    private final String email;
    
    public DuplicateEmailException(String email) {
        super(String.format("Email '%s' já está registrado", email));
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
}
```

### 3. **Sanitização de Inputs**

```java
public class UserService {
    
    public UserDTO createUser(UserCreateDTO dto) {
        // Normalização de email
        String normalizedEmail = dto.getEmail().trim().toLowerCase();
        
        // Validação adicional
        if (!isValidEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        // Sanitização de telefone
        String phone = dto.getPhone().replaceAll("[^0-9]", "");
        
        // ... continuar processamento
    }
}
```

---

## 📝 Padrões de Configuração

### 1. **Profiles Spring**

**application.yml (base):**
```yaml
spring:
  application:
    name: srv-usuario
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}
```

**application-dev.yml:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/viafluvial
  
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: validate
  
  flyway:
    clean-on-validation-error: true

logging:
  level:
    com.viafluvial: DEBUG
```

**application-prod.yml:**
```yaml
spring:
  datasource:
    url: ${DATABASE_URL}
    hikari:
      maximum-pool-size: 20
  
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate

logging:
  level:
    com.viafluvial: INFO
```

### 2. **Externalização de Configurações Sensíveis**

Nunca commitar credenciais:

```yaml
spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}

jwt:
  secret: ${JWT_SECRET}
  expiration: ${JWT_EXPIRATION:86400}
```

**Variáveis de Ambiente (.env para desenvolvimento):**
```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/viafluvial
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
JWT_SECRET=your-secret-key-here
```

---

## 🎓 Princípios de Design Aplicados

### 1. **SOLID**

- **Single Responsibility**: Cada classe tem uma responsabilidade única
  - Controllers apenas recebem requisições e delegam para serviços
  - Services contêm lógica de negócio
  - Repositories fazem acesso a dados
  
- **Open/Closed**: Aberto para extensão, fechado para modificação
  - Specifications compostas
  - Mappers extensíveis via MapStruct
  
- **Liskov Substitution**: Interfaces e abstrações
  - JpaRepository implementações intercambiáveis
  - Converters genéricos
  
- **Interface Segregation**: Interfaces coesas
  - Mappers específicos por entidade
  - Repositories com métodos focados
  
- **Dependency Inversion**: Dependência de abstrações
  - Injeção via construtores
  - Uso de interfaces (Repository, Mapper)

### 2. **DRY (Don't Repeat Yourself)**

- Specifications reutilizáveis
- Mappers centralizados
- Exception handlers globais
- Configurações centralizadas
- Utility classes para lógica comum

### 3. **KISS (Keep It Simple, Stupid)**

- Código direto sem over-engineering
- Builder pattern simples
- Validações declarativas
- Nomenclatura clara e óbvia

### 4. **YAGNI (You Aren't Gonna Need It)**

- Implementar apenas o necessário
- Evitar abstrações prematuras
- Features sob demanda

---

## 📦 Estrutura de Pacotes Completa

```
com.viafluvial.srvusuario/
├── SrvUsuarioApplication.java              # Classe principal Spring Boot
│
├── domain/                                  # Camada de Domínio
│   ├── entity/                             # Entidades JPA
│   │   ├── User.java                       # Entidade base de usuário
│   │   ├── Boatman.java                    # Entidade de barqueiro
│   │   ├── Passenger.java                  # Entidade de passageiro
│   │   ├── Agency.java                     # Entidade de agência
│   │   ├── Admin.java                      # Entidade de administrador
│   │   ├── UserPreference.java             # Preferências do usuário
│   │   ├── Approval.java                   # Aprovações de cadastro
│   │   └── converter/                      # Conversores JPA
│   │       ├── AbstractCaseInsensitiveEnumConverter.java
│   │       ├── UserTypeConverter.java
│   │       └── UserStatusConverter.java
│   │
│   └── exception/                          # Exceções de domínio
│       ├── DomainException.java            # Exceção base
│       ├── UserNotFoundException.java
│       ├── DuplicateEmailException.java
│       └── InvalidStatusTransitionException.java
│
├── application/                            # Camada de Aplicação
│   ├── service/                            # Serviços de negócio
│   │   ├── UserService.java               # CRUD de usuários
│   │   ├── AuthService.java               # Autenticação/registro
│   │   ├── BoatmanService.java            # Lógica de barqueiros
│   │   ├── PassengerService.java          # Lógica de passageiros
│   │   ├── AgencyService.java             # Lógica de agências
│   │   └── AdminService.java              # Lógica de administradores
│   │
│   ├── dto/                                # Data Transfer Objects
│   │   ├── UserDTO.java                   # DTO de saída
│   │   ├── UserCreateDTO.java             # DTO de criação
│   │   ├── UserUpdateDTO.java             # DTO de atualização
│   │   ├── BoatmanDTO.java
│   │   ├── PassengerDTO.java
│   │   ├── PagedResponse.java             # Wrapper de paginação
│   │   ├── ErrorResponse.java             # Resposta de erro
│   │   └── AuthResponseDTO.java           # Resposta de autenticação
│   │
│   └── mapper/                             # Mappers MapStruct
│       ├── UserMapper.java
│       ├── BoatmanMapper.java
│       └── PassengerMapper.java
│
├── presentation/                           # Camada de Apresentação
│   └── controller/                         # Controllers REST
│       ├── UserController.java            # /users
│       ├── AuthController.java            # /auth
│       ├── BoatmanController.java         # /boatmen
│       ├── PassengerController.java       # /passengers
│       └── AgencyController.java          # /agencies
│
└── infrastructure/                         # Camada de Infraestrutura
    ├── config/                             # Configurações Spring
    │   ├── SecurityConfig.java            # Configuração de segurança
    │   ├── CacheConfig.java               # Configuração de cache
    │   ├── OpenApiConfig.java             # Configuração Swagger
    │   ├── TraceIdFilter.java             # Filtro de trace ID
    │   └── JpaConfig.java                 # Configuração JPA
    │
    ├── repository/                         # Repositórios JPA
    │   ├── UserRepository.java
    │   ├── BoatmanRepository.java
    │   ├── PassengerRepository.java
    │   ├── AgencyRepository.java
    │   └── spec/                           # Specifications
    │       ├── UserSpecifications.java
    │       ├── BoatmanSpecifications.java
    │       └── PassengerSpecifications.java
    │
    ├── exception/                          # Exception Handlers
    │   └── GlobalExceptionHandler.java    # @RestControllerAdvice
    │
    └── util/                               # Utilitários
        ├── UnsafeJwtClaimsExtractor.java  # Extração de claims JWT
        └── ValidationUtils.java            # Utilidades de validação
```

---

## ✅ Checklist para Novos Projetos

Ao criar um novo microserviço seguindo estes padrões:

### Setup Inicial
- [ ] Criar projeto Spring Boot 3.4.1+ com Java 17
- [ ] Configurar Maven com parent `spring-boot-starter-parent`
- [ ] Adicionar dependências: web, data-jpa, validation, actuator, flyway
- [ ] Adicionar MapStruct 1.6.0 e configurar annotation processor
- [ ] Adicionar Springdoc OpenAPI 2.7.0
- [ ] Adicionar Caffeine Cache
- [ ] Criar estrutura de pacotes (domain/application/presentation/infrastructure)

### Database
- [ ] Configurar datasource no `application.yml`
- [ ] Configurar Flyway baseline
- [ ] Criar migrações SQL em `src/main/resources/db/migration/`
- [ ] Definir índices em tabelas principais

### Domain Layer
- [ ] Criar entidades com builder pattern manual
- [ ] Adicionar anotações JPA (`@Entity`, `@Table`, `@Index`)
- [ ] Usar UUID para IDs principais
- [ ] Adicionar timestamps (`createdAt`, `updatedAt`)
- [ ] Criar converters para enums customizados
- [ ] Definir exceções de domínio customizadas

### Application Layer
- [ ] Criar DTOs de entrada (`*CreateDTO`, `*UpdateDTO`) com validações
- [ ] Criar DTOs de saída (sem campos sensíveis)
- [ ] Implementar builders em DTOs
- [ ] Criar interfaces MapStruct para mapeamento
- [ ] Implementar serviços com `@Service` e `@Transactional`
- [ ] Adicionar logging SLF4J em operações importantes
- [ ] Configurar cache com `@Cacheable`/`@CacheEvict`

### Infrastructure Layer
- [ ] Criar repositórios estendendo `JpaRepository` e `JpaSpecificationExecutor`
- [ ] Implementar Specifications para consultas dinâmicas
- [ ] Configurar `CacheConfig` com Caffeine
- [ ] Implementar `TraceIdFilter` para correlação de logs
- [ ] Criar `GlobalExceptionHandler` com `@RestControllerAdvice`
- [ ] Configurar profiles (dev, test, prod)

### Presentation Layer
- [ ] Criar controllers REST com `@RestController`
- [ ] Adicionar tags Swagger (`@Tag`, `@Operation`, `@ApiResponses`)
- [ ] Implementar validação com `@Valid`
- [ ] Suportar paginação com `Pageable`
- [ ] Retornar status HTTP corretos
- [ ] Documentar todos os endpoints completamente

### Testing
- [ ] Criar testes unitários de serviços com Mockito
- [ ] Criar testes de integração de controllers com `@WebMvcTest`
- [ ] Configurar `application-test.yml` com H2
- [ ] Usar padrão AAA (Arrange-Act-Assert)
- [ ] Adicionar `@DisplayName` descritivos

### Containerization
- [ ] Criar Dockerfile multi-stage
- [ ] Adicionar healthcheck no Docker
- [ ] Criar `docker-compose.yml` para desenvolvimento
- [ ] Configurar variáveis de ambiente

### Observability
- [ ] Expor endpoints Actuator
- [ ] Configurar métricas Prometheus
- [ ] Implementar structured logging
- [ ] Adicionar MDC trace ID

### Documentation
- [ ] Criar README.md do projeto
- [ ] Documentar variáveis de ambiente
- [ ] Criar guia de build e execução
- [ ] Documentar estrutura de APIs

---

## 🚀 Comandos Rápidos de Referência

```bash
# Build completo
mvn clean install

# Build sem testes
mvn clean package -DskipTests

# Executar aplicação
mvn spring-boot:run

# Executar com profile específico
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Rodar testes
mvn test

# Rodar teste específico
mvn test -Dtest=UserServiceTest

# Build Docker
docker build -t srv-usuario:1.0.0 .

# Subir com Docker Compose
docker-compose up -d

# Ver logs
docker-compose logs -f srv-usuario

# Parar containers
docker-compose down
```

---

## 📚 Referências e Best Practices

### Spring Boot
- [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/reference/)

### API Design
- [RESTful API Guidelines](https://restfulapi.net/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [HTTP Status Codes](https://httpstatuses.com/)

### Testing
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [AssertJ Documentation](https://assertj.github.io/doc/)

### Architecture
- [Domain-Driven Design](https://martinfowler.com/tags/domain%20driven%20design.html)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)

---

## 📄 Licença e Contribuição

Este guia foi criado baseado no projeto **srv-usuario** da plataforma **ViáFluvial**.

**Uso recomendado:**
- Adapte para suas necessidades específicas
- Mantenha consistência entre microserviços
- Evolua os padrões conforme aprendizados do time
- Documente desvios e justificativas

**Contribuições:**
- Sugira melhorias baseadas em experiência prática
- Compartilhe novos padrões descobertos
- Atualize com novas versões do Spring Boot
- Adicione exemplos de casos de uso específicos

---

**Última atualização:** Fevereiro 2026  
**Versão Spring Boot:** 3.4.1  
**Versão Java:** 17  
**Autor:** Documentação consolidada do projeto srv-usuario-poc
