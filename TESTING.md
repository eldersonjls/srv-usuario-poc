# Configuração de Testes com Supabase

## Overview
Este projeto está configurado para rodar testes contra uma instância real do Supabase PostgreSQL, permitindo testes de integração com o banco de dados.

## Configuração

### Profile de Teste
- **Profile**: `test`
- **Arquivo de Configuração**: `src/test/resources/application-test.yml`
- **Ativar**: Adicione `@ActiveProfiles("test")` nas classes de teste

### Credenciais do Supabase
```
Host: db.ibwprzjqvegzepphznkm.supabase.co
Port: 5432
Database: postgres_test (para testes)
Username: postgres
Password: Ejls989720601
```

## Rodando os Testes

### Todos os testes
```bash
mvn clean test
```

### Apenas testes de integração com Supabase
```bash
mvn clean test -Dgroups=integration
```

### Com modo verbose
```bash
mvn clean test -X
```

### Apenas um teste específico
```bash
mvn clean test -Dtest=UserControllerTest
```

## Configuração dos Testes

### Para usar Supabase nos testes, adicione ao test:
```java
@SpringBootTest
@ActiveProfiles("test")
public class MyIntegrationTest {
    // Test code here
}
```

### ConnectionPool para testes:
- `maximum-pool-size: 5` (reduzido para testes)
- `minimum-idle: 1`
- `connection-timeout: 30000ms`
- `auto-commit: false`

## Variáveis de Ambiente (Opcional)

Para não expor credenciais, pode-se usar variáveis de ambiente:

```bash
export SUPABASE_HOST=db.ibwprzjqvegzepphznkm.supabase.co
export SUPABASE_PORT=5432
export SUPABASE_DB=postgres_test
export SUPABASE_USER=postgres
export SUPABASE_PASSWORD=Ejls989720601
```

## Hibernate DDL Strategy

- **Ambiente Dev**: `ddl-auto: validate` (apenas valida schema)
- **Testes**: `ddl-auto: create-drop` (cria e apaga tabelas a cada teste)

## Logging em Testes

```yaml
logging:
  level:
    com.viafluvial: DEBUG          # Application logs
    org.hibernate.sql: DEBUG        # SQL queries
    org.springframework.test: WARN  # Spring test framework
```

## Boas Práticas

1. **Isolamento**: Use `create-drop` para garantir que cada teste tenha um banco limpo
2. **Dados Iniciais**: Use `@Sql` para popular dados de teste
3. **Transações**: Use `@Transactional` para reverter mudanças após testes
4. **Performance**: Use pool pequeno para não sobrecarregar o Supabase

## Exemplo de Teste

```java
@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByEmail() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        userRepository.save(user);

        // When
        Optional<User> found = userRepository.findByEmail("test@example.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }
}
```

## Troubleshooting

### "Connection refused"
- Verifique se as credenciais do Supabase estão corretas
- Verifique a conexão de rede
- Confirme que o Supabase está acessível

### "Hibernate DDL error"
- Certifique-se que `ddl-auto: create-drop` está configurado em testes
- Verifique as constraints das entidades

### "Port already in use"
- O `server.port: 0` usa uma porta aleatória para cada teste
- Se ainda houver conflito, aumentar o timeout da pool

## CI/CD Integration

Para GitHub Actions, adicione as secrets:
```yaml
env:
  SUPABASE_PASSWORD: ${{ secrets.SUPABASE_PASSWORD }}
```

## Performance Tips

1. Use `batch_size: 20` para operações em massa
2. Use `fetch_size: 100` para consultas
3. Mantenha `maximum-pool-size` pequeno em testes
4. Use índices nas colunas de busca frequente
