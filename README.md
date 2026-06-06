# SmartDisaster API

API REST corporativa para gestão inteligente de emergências — coordena abrigos, vítimas, voluntários, doações, necessidades e leituras de sensores com o objetivo de reduzir o caos logístico em situações de desastre.

---

## Links Importantes

| Recurso | Link |
|---|---|
| **Deploy (API pública)** | `https://SEU-LINK-DE-DEPLOY.com` |
| **Swagger / Docs da API** | `https://SEU-LINK-DE-DEPLOY.com/swagger-ui.html` |
| **Vídeo de Apresentação** | `https://www.youtube.com/watch?v=SEU-VIDEO` |
| **Repositório GitHub** | `https://github.com/SEU-USUARIO/smartdisaster` |

> ⚠️ Substitua os placeholders acima pelos links reais antes da entrega.

---

## Arquitetura

```
br.com.fiap.smartdisaster
├── config          → SecurityConfig, OpenApiConfig
├── controller      → AuthController, AbrigoController, VitimaController,
│                     DoacaoController, NecessidadeController,
│                     SensorController, MatchingController
├── dto
│   ├── request     → Records de entrada com validação Bean Validation
│   └── response    → Records de saída
├── entity          → JPA entities (herança SINGLE_TABLE para Usuario)
├── enums           → Role, StatusAbrigo, StatusDoacao, StatusNecessidade
├── exception       → GlobalExceptionHandler, ErrorResponse, Exceptions
├── mapper          → Conversão manual entity ↔ DTO
├── repository      → JpaRepository para cada entidade
├── security        → JwtTokenProvider, JwtAuthenticationFilter,
│                     UserDetailsServiceImpl
└── service         → Toda a lógica de negócio
```

Princípios: SOLID, Clean Code, arquitetura em camadas, sem lógica nos controllers.

---

## Tecnologias

| Tecnologia              | Versão   |
|-------------------------|----------|
| Java                    | 17       |
| Spring Boot             | 3.2.4    |
| Spring Security + JWT   | 6.x      |
| Spring Data JPA         | 3.x      |
| Spring HATEOAS          | 2.x      |
| springdoc-openapi       | 2.3.0    |
| JJWT                    | 0.11.5   |
| Lombok                  | —        |
| H2 Database             | Runtime  |
| Oracle JDBC (ojdbc11)   | Runtime  |

---

## Diagrama de Entidades

```
Usuario (SINGLE_TABLE)
  ├── Admin         (ROLE = ADMIN)
  └── Voluntario    (ROLE = VOLUNTARIO, +telefone)

Abrigo
  ├── @Embedded Endereco (rua, numero, bairro, cidade, estado, cep)
  ├── @OneToMany → Vitima
  ├── @OneToMany → Necessidade
  └── @OneToMany → SensorLeitura

Vitima          @ManyToOne → Abrigo
Doacao          @ManyToOne → Voluntario
Necessidade     @ManyToOne → Abrigo
SensorLeitura   @ManyToOne → Abrigo

MatchingDoacaoNecessidade
  @EmbeddedId MatchingId (doacaoId, necessidadeId)
  @ManyToOne → Doacao
  @ManyToOne → Necessidade
```

---

## Endpoints

### Auth
| Método | Endpoint          | Acesso  | Descrição               |
|--------|-------------------|---------|-------------------------|
| POST   | `/auth/login`     | Público | Retorna token JWT       |
| POST   | `/auth/register`  | Público | Cria Admin ou Voluntário|

### Abrigos
| Método | Endpoint          | Acesso    | Descrição                        |
|--------|-------------------|-----------|----------------------------------|
| GET    | `/abrigos`        | Ambos     | Lista paginada com HATEOAS       |
| GET    | `/abrigos/{id}`   | Ambos     | Busca por ID                     |
| POST   | `/abrigos`        | ADMIN     | Cria abrigo                      |
| PUT    | `/abrigos/{id}`   | ADMIN     | Atualiza abrigo                  |
| DELETE | `/abrigos/{id}`   | ADMIN     | Soft delete → status = INATIVO   |

### Vítimas
| Método | Endpoint                     | Acesso    | Descrição                    |
|--------|------------------------------|-----------|------------------------------|
| GET    | `/vitimas`                   | Ambos     | Lista; `?abrigoId=1` filtra  |
| GET    | `/vitimas/{id}`              | Ambos     | Busca por ID                 |
| POST   | `/vitimas`                   | Ambos     | Cadastra vítima              |
| PUT    | `/vitimas/{id}`              | ADMIN     | Atualiza vítima              |
| DELETE | `/vitimas/{id}`              | ADMIN     | Remove vítima                |

### Doações
| Método | Endpoint          | Acesso    | Descrição          |
|--------|-------------------|-----------|--------------------|
| GET    | `/doacoes`        | Ambos     | Lista paginada     |
| GET    | `/doacoes/{id}`   | Ambos     | Busca por ID       |
| POST   | `/doacoes`        | Ambos     | Registra doação    |
| PUT    | `/doacoes/{id}`   | ADMIN     | Atualiza doação    |
| DELETE | `/doacoes/{id}`   | ADMIN     | Remove doação      |

### Necessidades
| Método | Endpoint               | Acesso    | Descrição              |
|--------|------------------------|-----------|------------------------|
| GET    | `/necessidades`        | Ambos     | Lista paginada         |
| GET    | `/necessidades/{id}`   | Ambos     | Busca por ID           |
| POST   | `/necessidades`        | ADMIN     | Registra necessidade   |
| PUT    | `/necessidades/{id}`   | ADMIN     | Atualiza necessidade   |
| DELETE | `/necessidades/{id}`   | ADMIN     | Remove necessidade     |

### Sensor
| Método | Endpoint                       | Acesso    | Descrição                            |
|--------|--------------------------------|-----------|--------------------------------------|
| POST   | `/sensor/leitura`              | ADMIN     | Registra leitura; ativa regra lotação|
| GET    | `/sensor/leitura`              | Ambos     | Lista todas as leituras              |
| GET    | `/sensor/leitura/abrigo/{id}`  | Ambos     | Leituras de um abrigo específico     |

### Matching
| Método | Endpoint             | Acesso    | Descrição                           |
|--------|----------------------|-----------|-------------------------------------|
| POST   | `/matching/executar` | ADMIN     | Executa engine de matching          |
| GET    | `/matching`          | Ambos     | Histórico de matchings realizados   |

---

## Regras de Negócio

### Regra 1 — Lotação Automática
Ao registrar uma `SensorLeitura`:
> Se `ocupacaoAtual >= capacidadeMaxima` → `Abrigo.status = LOTADO`

### Regra 2 — Engine de Matching
`POST /matching/executar`:
1. Localiza doações com `status = DISPONIVEL`
2. Para cada doação, localiza necessidades com `status = PENDENTE` e mesmo `tipo`
3. Para cada correspondência: persiste `MatchingDoacaoNecessidade`, atualiza `Doacao → ENTREGUE`, `Necessidade → ATENDIDA`

### Regra 3 — Soft Delete de Abrigo
`DELETE /abrigos/{id}`:
> Não remove fisicamente — apenas altera `Abrigo.status = INATIVO`

---

## Autenticação JWT

1. Fazer login em `POST /auth/login` com `email` e `senha`
2. Copiar o campo `token` da resposta
3. Nos demais endpoints, incluir o header:
   ```
   Authorization: Bearer <token>
   ```
4. Token válido por **24 horas** (configurável em `jwt.expiration`)

---

## Swagger / OpenAPI

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui.html
```

Para autenticar no Swagger:
1. Clique em **Authorize** (cadeado)
2. No campo `bearerAuth`, cole apenas o token (sem "Bearer ")
3. Clique em **Authorize**

---

## Execução — Perfil H2 (Desenvolvimento)

### Pré-requisitos
- Java 17+
- Maven 3.8+

### Comandos

```bash
# Clonar / entrar no projeto
cd API_smart

# Compilar e executar
./mvnw spring-boot:run

# Ou com Maven instalado
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`.  
Console H2 disponível em `http://localhost:8080/h2-console`  
(JDBC URL: `jdbc:h2:mem:smartdisaster`, user: `sa`, sem senha)

---

## Execução — Perfil Oracle (Produção)

Configure as variáveis de ambiente e ative o perfil:

```bash
export ORACLE_HOST=seu-host
export ORACLE_PORT=1521
export ORACLE_SERVICE=XEPDB1
export ORACLE_USER=smartdisaster
export ORACLE_PASSWORD=suasenha
export JWT_SECRET=seuSecretBase64

mvn spring-boot:run -Dspring.profiles.active=oracle
```

Ou via `application-oracle.properties` diretamente.

---

## Exemplos de Requisições JSON

### POST /auth/register
```json
{
  "nome": "Ana Admin",
  "email": "ana@smartdisaster.com",
  "senha": "admin123",
  "role": "ADMIN"
}
```

### POST /auth/register (Voluntário)
```json
{
  "nome": "Carlos Voluntário",
  "email": "carlos@smartdisaster.com",
  "senha": "volunt123",
  "role": "VOLUNTARIO",
  "telefone": "11999990000"
}
```

### POST /auth/login
```json
{
  "email": "ana@smartdisaster.com",
  "senha": "admin123"
}
```

### POST /abrigos
```json
{
  "nome": "Abrigo Central SP",
  "capacidadeMaxima": 200,
  "latitude": -23.5505,
  "longitude": -46.6333,
  "status": "ATIVO",
  "endereco": {
    "rua": "Av. Paulista",
    "numero": "1000",
    "bairro": "Bela Vista",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01310-100"
  }
}
```

### POST /vitimas
```json
{
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "dataNascimento": "1990-05-15",
  "condicaoSaude": "Fratura no braço esquerdo",
  "abrigoId": 1
}
```

### POST /doacoes
```json
{
  "tipo": "alimento",
  "descricao": "Caixas de macarrão e arroz",
  "quantidade": 50,
  "dataDoacao": "2024-06-01",
  "status": "DISPONIVEL",
  "voluntarioId": 2
}
```

### POST /necessidades
```json
{
  "tipo": "alimento",
  "descricao": "Necessidade urgente de alimentos não perecíveis",
  "quantidadeNecessaria": 100,
  "status": "PENDENTE",
  "abrigoId": 1
}
```

### POST /sensor/leitura
```json
{
  "ocupacaoAtual": 205,
  "temperatura": 28.5,
  "abrigoId": 1
}
```

### POST /matching/executar
```json
{}
```
*(corpo vazio — o engine faz tudo automaticamente)*

---

## Permissões por Role

| Operação          | ADMIN | VOLUNTARIO |
|-------------------|:-----:|:----------:|
| GET (qualquer)    | ✅    | ✅         |
| POST /vitimas     | ✅    | ✅         |
| POST /doacoes     | ✅    | ✅         |
| POST /abrigos     | ✅    | ❌         |
| POST /necessidades| ✅    | ❌         |
| POST /sensor      | ✅    | ❌         |
| POST /matching    | ✅    | ❌         |
| PUT (qualquer)    | ✅    | ❌         |
| DELETE (qualquer) | ✅    | ❌         |

---

## Autores

- **Pedro Vaz** — pedrovazferreira10@gmail.com  
- FIAP — Challenge SmartDisaster 2024
