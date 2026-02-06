# Resolução de Erros de Compilação - Java 23 → Java 21

## Problema
Erro de compilação ao executar `mvn clean install`:
```
[ERROR] Fatal error compiling: java.lang.ExceptionInInitializerError: 
com.sun.tools.javac.code.TypeTag :: UNKNOWN
```

## Causa
- Java 23 tem incompatibilidade conhecida com Maven Compiler Plugin 3.11.0
- O erro `TypeTag :: UNKNOWN` é uma issue específica do Java 23 com a compilação
- Java 21 (LTS) é mais estável e totalmente suportado pelo Spring Boot 3.4.1

## Solução Aplicada

### 1. Atualizado `pom.xml`
- **Antes**: `<java.version>23</java.version>`
- **Depois**: `<java.version>21</java.version>`
- Atualizadas também as propriedades `maven.compiler.source` e `maven.compiler.target` para 21
- Plugin maven-compiler-plugin: versões de source/target de 23 → 21

### 2. Atualizado `Dockerfile`
- **Antes**: `FROM eclipse-temurin:23-jdk`
- **Depois**: `FROM eclipse-temurin:21-jdk`
- Aplicado em ambos os stages (builder e runtime)

### 3. Criado `.mvn/jvm.config`
```
-XX:+IgnoreUnrecognizedVMOptions --illegal-access=warn
```
Configura opções JVM para melhor compatibilidade

### 4. Criado `.mvn/maven.config`
```
-Dmaven.compiler.source=21
-Dmaven.compiler.target=21
```
Força compilador Maven para Java 21

### 5. Criado `.java-version`
```
21
```
Para gerenciadores de versão Java (jenv, sdkman, etc.)

## Arquivos Modificados

| Arquivo | Mudança |
|---------|---------|
| `pom.xml` | Java 23 → 21 (3 locais) |
| `Dockerfile` | Java 23 → 21 (2 stages) |
| `.mvn/jvm.config` | Criado |
| `.mvn/maven.config` | Criado |
| `.java-version` | Criado |

## Próximos Passos

Execute o build novamente:
```bash
mvn clean install
```

Se ainda houver problemas, tente:
```bash
mvn clean compile -e  # Para ver stack trace completo
mvn clean compile -X  # Para debug verbose
```

## Compatibilidade

- **Java 21**: LTS (Long-Term Support), estável
- **Spring Boot 3.4.1**: Suporta Java 21 nativamente
- **Maven 3.8+**: Totalmente compatível
- **Linux Ubuntu 24.04**: Suporta Java 21 via eclipse-temurin

## Versão do Projeto

- **Versão atual**: 1.0.0
- **Versão Java alvo**: 21 (foi 23)
- **Spring Boot**: 3.4.1
- **Data da mudança**: 2026-02-01
