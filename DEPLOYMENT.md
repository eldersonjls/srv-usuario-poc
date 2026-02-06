# Instruções de Deployment

## 🐳 Docker

### Build da Imagem

```bash
# Criar Dockerfile na raiz do projeto
docker build -t viafluvial/srv-usuario:1.0.0 .
```

### Docker Compose

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: Ejls989720601
      POSTGRES_DB: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./src/main/resources/db/schema.sql:/docker-entrypoint-initdb.d/01-schema.sql

  app:
    build: .
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/postgres
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: Ejls989720601
      JWT_SECRET: "your-secret-key-here"
    ports:
      - "8080:8080"
    depends_on:
      - postgres

volumes:
  postgres_data:
```

### Executar com Docker Compose

```bash
docker-compose up -d
```

## ☁️ Cloud Platforms

### AWS EC2

```bash
# SSH na instância
ssh -i key.pem ubuntu@seu-ip

# Instalar Java 21
sudo apt update
sudo apt install openjdk-21-jdk

# Fazer upload do JAR
scp -i key.pem target/srv-usuario-1.0.0.jar ubuntu@seu-ip:/home/ubuntu/

# Executar
java -jar srv-usuario-1.0.0.jar
```

### Heroku

```bash
# Login
heroku login

# Criar app
heroku create seu-app-name

# Deploy
git push heroku main

# Logs
heroku logs --tail
```

### Google Cloud Run

```bash
# Build com Cloud Build
gcloud builds submit --tag gcr.io/seu-projeto/srv-usuario:1.0.0

# Deploy
gcloud run deploy srv-usuario \
  --image gcr.io/seu-projeto/srv-usuario:1.0.0 \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated
```

## 📦 Kubernetes

### Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: srv-usuario
spec:
  replicas: 3
  selector:
    matchLabels:
      app: srv-usuario
  template:
    metadata:
      labels:
        app: srv-usuario
    spec:
      containers:
      - name: srv-usuario
        image: viafluvial/srv-usuario:1.0.0
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_DATASOURCE_URL
          value: jdbc:postgresql://postgres:5432/postgres
        - name: SPRING_DATASOURCE_USERNAME
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: username
        - name: SPRING_DATASOURCE_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: password
```

### Service

```yaml
apiVersion: v1
kind: Service
metadata:
  name: srv-usuario-service
spec:
  selector:
    app: srv-usuario
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: LoadBalancer
```

## 📋 Checklist de Deploy

- [ ] Testes executados com sucesso
- [ ] Build Maven sem erros
- [ ] Variáveis de ambiente configuradas
- [ ] Banco de dados migrado
- [ ] Certificados SSL/TLS configurados
- [ ] Logs centralizados
- [ ] Monitoring e alertas ativados
- [ ] Backup automático ativado
- [ ] Load balancer configurado
- [ ] Health checks implementados

## 🔍 Verificações Pós-Deploy

```bash
# Health check
curl http://localhost:8080/actuator/health

# Swagger UI
curl http://localhost:8080/api/v1/swagger-ui.html

# Status da aplicação
curl http://localhost:8080/actuator/info
```

## 🚨 Troubleshooting

### Conexão com Banco de Dados

```bash
# Testar conexão
psql -h db.ibwprzjqvegzepphznkm.supabase.co -U postgres -d postgres
```

### Logs

```bash
# Ver logs em tempo real
tail -f /var/log/srv-usuario/app.log

# Com Docker
docker logs -f container-id
```

### Reiniciar

```bash
# Parar
docker-compose down

# Iniciar
docker-compose up -d
```
