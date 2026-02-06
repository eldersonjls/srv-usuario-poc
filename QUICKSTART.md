# 🚀 Quick Start Guide

## Primeira Execução

### 1. Clonar o Repositório

```bash
git clone https://github.com/eldersonjls/srv-usuario-poc.git
cd srv-usuario-poc
```

### 2. Compilar o Projeto

```bash
mvn clean install
```

### 3. Executar com Docker Compose (Recomendado)

```bash
docker-compose up -d
```

Aguarde 30-40 segundos para a aplicação inicializar.

### 4. Acessar a Aplicação

- **Swagger UI**: http://localhost:8080/api/v1/swagger-ui.html
- **API Base**: http://localhost:8080/api/v1
- **Health Check**: http://localhost:8080/actuator/health

## Uso Rápido

### Criar Usuário

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@example.com",
    "password": "Senha123!",
    "fullName": "João Silva",
    "phone": "(92) 98765-4321",
    "userType": "PASSENGER"
  }'
```

Resposta:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "teste@example.com",
  "fullName": "João Silva",
  "phone": "(92) 98765-4321",
  "userType": "PASSENGER",
  "status": "PENDING",
  "emailVerified": false
}
```

### Obter Usuário

```bash
curl http://localhost:8080/api/v1/users/550e8400-e29b-41d4-a716-446655440000
```

### Criar Passageiro

```bash
curl -X POST http://localhost:8080/api/v1/passengers \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "cpf": "123.456.789-00",
    "birthDate": "1990-05-15",
    "city": "Manaus",
    "state": "AM"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@example.com",
    "password": "Senha123!"
  }'
```

## Executar Testes

```bash
# Todos os testes
mvn test

# Com cobertura
mvn test jacoco:report

# Um teste específico
mvn test -Dtest=UserServiceTest
```

## Estrutura de Diretórios

```
srv-usuario-poc/
├── src/
│   ├── main/
│   │   ├── java/com/viafluvial/srvusuario/
│   │   │   ├── application/  (DTOs, Services)
│   │   │   ├── domain/       (Entities)
│   │   │   ├── infrastructure/ (Config, Repos)
│   │   │   └── presentation/ (Controllers)
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/schema.sql
│   └── test/
│       ├── java/com/viafluvial/srvusuario/
│       └── resources/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── README.md
├── DEPLOYMENT.md
└── TECHNICAL_SUMMARY.md
```

## Troubleshooting

### Porta 8080 já em uso

```bash
# Parar container anterior
docker-compose down

# Ou mudar porta em docker-compose.yml
```

### Erro de conexão com banco

```bash
# Verificar se container do postgres está rodando
docker ps

# Ver logs
docker-compose logs postgres
```

### Limpar tudo

```bash
docker-compose down -v
rm -rf target/
```

## Variáveis de Ambiente

Criar arquivo `.env` na raiz:

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=Ejls989720601
JWT_SECRET=sua-chave-secreta
SPRING_PROFILES_ACTIVE=prod
```

## Próximos Passos

1. ✅ Aplicação rodando
2. 📖 Explorar Swagger em http://localhost:8080/api/v1/swagger-ui.html
3. 🧪 Executar testes: `mvn test`
4. 🔧 Customizar configurações em `application.yml`
5. 📝 Ler documentação completa em README.md

## Suporte

- 📧 Email: support@viafluvial.com.br
- 📚 Wiki: [Wiki do Projeto](https://github.com/eldersonjls/srv-usuario-poc/wiki)
- 🐛 Issues: [Issues do GitHub](https://github.com/eldersonjls/srv-usuario-poc/issues)

---

**Pronto! 🎉 A aplicação está funcionando!**
