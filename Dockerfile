FROM eclipse-temurin:17-jdk AS builder

WORKDIR /build

COPY pom.xml .
COPY src src

RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=builder /build/target/srv-usuario-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD java -cp /app/app.jar org.springframework.boot.loader.JarLauncher || exit 1
