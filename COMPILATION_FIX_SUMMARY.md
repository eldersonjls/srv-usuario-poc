# Resumo das Correções de Build - 2026-02-01

## 🔴 Erro Original
```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.11.0:compile
[ERROR] Fatal error compiling: java.lang.ExceptionInInitializerError: 
com.sun.tools.javac.code.TypeTag :: UNKNOWN
```

## ✅ Solução Implementada

### Mudança Principal: Java 23 → Java 21 LTS

Java 23 possui uma incompatibilidade conhecida com o Maven Compiler Plugin 3.11.0 quando compilando certos tipos genéricos. Java 21 (LTS) é a versão recomendada para Spring Boot 3.4.1.

### Arquivos Modificados

#### 1. **pom.xml** (3 mudanças)
```xml
<!-- Antes -->
<java.version>23</java.version>
<maven.compiler.source>23</maven.compiler.source>
<maven.compiler.target>23</maven.compiler.target>
<!-- Plugin maven-compiler também: source/target em 23 -->

<!-- Depois -->
<java.version>21</java.version>
<maven.compiler.source>21</maven.compiler.source>
<maven.compiler.target>21</maven.compiler.target>
<!-- Plugin maven-compiler também: source/target em 21 -->
```

#### 2. **Dockerfile** (2 mudanças)
```dockerfile
# Antes
FROM eclipse-temurin:23-jdk AS builder
# ...
FROM eclipse-temurin:23-jdk

# Depois
FROM eclipse-temurin:21-jdk AS builder
# ...
FROM eclipse-temurin:21-jdk
```

#### 3. **.mvn/jvm.config** (Criado)
```
-XX:+IgnoreUnrecognizedVMOptions --illegal-access=warn
```

#### 4. **.mvn/maven.config** (Criado)
```
-Dmaven.compiler.source=21
-Dmaven.compiler.target=21
```

#### 5. **.java-version** (Criado)
```
21
```

#### 6. **BUILD_FIX.md** (Documentação)

#### 7. **test-build.sh** (Script de teste)

## 📝 Mudanças Anteriores (Validação de DTOs)

Também foram aplicadas as seguintes correções no mesmo período:

### DTOs Refatorados
- **UserCreateDTO.java**: Novo DTO com validações estritas para criação
- **UserDTO.java**: Relaxado para operações de atualização
- Campos opcionais: password, email, fullName, phone

### Camada de Serviço
- **UserService.createUser()**: Agora aceita `UserCreateDTO`
- **AuthService.register()**: Agora aceita `UserCreateDTO`

### Camada de Apresentação
- **UserController.createUser()**: POST agora usa `UserCreateDTO`
- **AuthController.register()**: POST agora usa `UserCreateDTO`

### Testes
- **UserControllerTest**: Atualizado para usar `UserCreateDTO` em testes POST

## 🚀 Próximas Ações

1. **Executar build completo:**
   ```bash
   mvn clean install
   ```

2. **Se ainda houver erro, debugar com:**
   ```bash
   mvn clean compile -e  # Stack trace completo
   mvn clean compile -X  # Verbose debug
   ```

3. **Compilar sem testes (mais rápido):**
   ```bash
   mvn clean install -DskipTests
   ```

4. **Executar testes separadamente:**
   ```bash
   mvn test
   ```

5. **Build Docker:**
   ```bash
   docker build -t srv-usuario:1.0.0 .
   ```

## 📊 Tabela de Compatibilidade

| Componente | Antes | Depois | Status |
|-----------|-------|--------|--------|
| Java | 23 | 21 LTS | ✅ Mais estável |
| Spring Boot | 3.4.1 | 3.4.1 | ✅ Compatível |
| Maven | 3.8+ | 3.8+ | ✅ Compatível |
| Eclipse Temurin | 23-jdk | 21-jdk | ✅ Disponível |
| Maven Compiler Plugin | 3.11.0 | 3.11.0 | ✅ Compatível |

## ℹ️ Informações do Projeto

- **Nome**: srv-usuario
- **Versão**: 1.0.0
- **Descrição**: Microserviço de Gerenciamento de Usuários - ViáFluvial
- **Grupo**: com.viafluvial
- **Artifact**: srv-usuario

## 🔗 Referências Úteis

- [Java 21 LTS Release](https://www.oracle.com/java/technologies/java21/)
- [Spring Boot 3.4.1 Documentation](https://spring.io/projects/spring-boot)
- [Maven Compiler Plugin](https://maven.apache.org/plugins/maven-compiler-plugin/)
- [Eclipse Temurin Docker Images](https://hub.docker.com/_/eclipse-temurin)

## ✨ Resultado Esperado

Após aplicar essas mudanças:
- ✅ Build Maven compila sem erros
- ✅ Todos os 33+ testes passam
- ✅ Docker build bem-sucedido
- ✅ API Swagger disponível em `/api/v1/swagger-ui.html`
- ✅ Aplicação inicia corretamente na porta 8080

---
**Data**: 2026-02-01  
**Status**: Pronto para testes
