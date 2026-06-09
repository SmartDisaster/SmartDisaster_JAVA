# SmartDisaster API

API REST para gestão inteligente de emergências — coordena abrigos, vítimas, voluntários, doações, necessidades e leituras de sensores com o objetivo de reduzir o caos logístico em situações de desastre.

---

## Links Importantes

| Recurso | Link |
|---|---|
| **Deploy da API** | https://smartdisasterjava-production.up.railway.app |
| **Swagger / Documentação** | https://smartdisasterjava-production.up.railway.app/swagger-ui.html |
| **Vídeo de Apresentação (até 10 min)** | https://youtu.be/Kg750vQBo48 |
| **Video Pitch (até 3 min)** | https://youtu.be/ItPEbWxzNkw |
| **Testes Mobile** | https://youtu.be/pk97vHmkX98 |

---

## Integrantes

| Nome | RM |
|---|---|
| Pedro Vaz | RM566551 |
| João Victor Luiz Oliveira Resende | RM565139 |

**Turma:** Java Advanced — FIAP Global Solution 2025

---

## Proposta da Solução

O **SmartDisaster** resolve o problema de coordenação logística em situações de desastre natural. Em cenários de emergência, a falta de informação centralizada sobre disponibilidade de abrigos, necessidades das vítimas e doações disponíveis gera desperdício de recursos e aumento do sofrimento humano.

A API oferece:
- Gestão em tempo real de abrigos e sua capacidade de ocupação
- Cadastro e acompanhamento de vítimas por abrigo
- Registro de doações por voluntários
- Engine automática de matching entre doações disponíveis e necessidades pendentes
- Ingestão de leituras de sensores IoT com atualização automática do status de lotação

---

## Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.2.4 | Framework base |
| Spring Security | 6.x | Autenticação e autorização |
| Spring Data JPA | 3.x | ORM e persistência |
| Spring HATEOAS | 2.x | Hipermídia nos responses |
| Spring Validation | 3.x | Validação de entrada |
| Spring Boot DevTools | 3.x | Produtividade em desenvolvimento |
| JJWT | 0.11.5 | Geração e validação de tokens JWT |
| Lombok | — | Redução de boilerplate |
| springdoc-openapi | 2.3.0 | Documentação Swagger/OpenAPI |
| H2 Database | runtime | Banco em memória (dev/local) |
| Oracle JDBC (ojdbc11) | runtime | Banco de produção (Railway) |

---

## Arquitetura do Projeto

```
br.com.fiap.smartdisaster
├── config          → SecurityConfig, CorsConfig, OpenApiConfig, DataLoader
├── controller      → AuthController, AbrigoController, VitimaController,
│                     DoacaoController, NecessidadeController,
│                     SensorController, MatchingController
├── dto
│   ├── request     → Java Records de entrada com Bean Validation
│   └── response    → Java Records de saída
├── entity          → Entidades JPA (herança SINGLE_TABLE em Usuario)
├── enums           → Role, StatusAbrigo, StatusDoacao, StatusNecessidade
├── exception       → GlobalExceptionHandler, ErrorResponse, exceções customizadas
├── mapper          → Conversão entity ↔ DTO
├── repository      → JpaRepository para cada entidade
├── security        → JwtTokenProvider, JwtAuthenticationFilter, UserDetailsServiceImpl
└── service         → Lógica de negócio isolada dos controllers
```

Princípios aplicados: SOLID, Clean Code, separação de responsabilidades, sem lógica nos controllers.

---

## Modelagem de Dados

```
Usuario (TB_USUARIOS — SINGLE_TABLE com discriminator ROLE)
  ├── Admin      (ROLE = ADMIN)
  └── Voluntario (ROLE = VOLUNTARIO, +telefone)

Abrigo
  ├── @Embedded Endereco (rua, numero, bairro, cidade, estado, cep)
  ├── @OneToMany → Vitima
  ├── @OneToMany → Necessidade
  └── @OneToMany → SensorLeitura

Vitima          @ManyToOne → Abrigo
Doacao          @ManyToOne → Voluntario
Necessidade     @ManyToOne → Abrigo
SensorLeitura   @ManyToOne → Abrigo

MatchingDoacaoNecessidade  (TB_MATCHING_DOACAO_NECESSIDADE)
  @EmbeddedId MatchingId (doacaoId + necessidadeId — chave composta)
  @ManyToOne → Doacao
  @ManyToOne → Necessidade
```

**Recursos de modelagem avançada:**
- **Herança:** `Usuario` com `@Inheritance(SINGLE_TABLE)` e `@DiscriminatorColumn`
- **Chave composta:** `MatchingId` com `@Embeddable` + `@EmbeddedId`
- **Embedded:** `Endereco` com `@Embeddable` incorporado em `Abrigo`
- **Múltiplas tabelas:** TB_USUARIOS, TB_MATCHING_DOACAO_NECESSIDADE, Abrigo, Vitima, Doacao, Necessidade, SensorLeitura

---

## Segurança

A API utiliza **Spring Security 6 + JWT (stateless)**:

- Todos os endpoints (exceto `/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`) exigem autenticação
- Autorização por roles: `ADMIN` e `VOLUNTARIO`
- Senhas armazenadas com **BCrypt**
- Token JWT com expiração de 24h

### Permissões por Role

| Operação | ADMIN | VOLUNTARIO |
|---|:---:|:---:|
| GET (qualquer endpoint) | ✅ | ✅ |
| POST /vitimas | ✅ | ✅ |
| POST /doacoes | ✅ | ✅ |
| POST /abrigos | ✅ | ❌ |
| POST /necessidades | ✅ | ❌ |
| POST /sensor/leitura | ✅ | ❌ |
| POST /matching/executar | ✅ | ❌ |
| PUT (qualquer) | ✅ | ❌ |
| DELETE (qualquer) | ✅ | ❌ |

---

## Endpoints da API

### Autenticação (público)
| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/auth/register` | Cadastra Admin ou Voluntário, retorna JWT |
| POST | `/auth/login` | Autentica e retorna JWT |

### Abrigos
| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| GET | `/abrigos` | ADMIN, VOLUNTARIO | Lista paginada com HATEOAS |
| GET | `/abrigos/{id}` | ADMIN, VOLUNTARIO | Busca por ID |
| POST | `/abrigos` | ADMIN | Cria abrigo |
| PUT | `/abrigos/{id}` | ADMIN | Atualiza abrigo |
| DELETE | `/abrigos/{id}` | ADMIN | Soft delete → status INATIVO |

### Vítimas
| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| GET | `/vitimas` | ADMIN, VOLUNTARIO | Lista; `?abrigoId=1` filtra por abrigo |
| GET | `/vitimas/{id}` | ADMIN, VOLUNTARIO | Busca por ID |
| POST | `/vitimas` | ADMIN, VOLUNTARIO | Cadastra vítima |
| PUT | `/vitimas/{id}` | ADMIN | Atualiza vítima |
| DELETE | `/vitimas/{id}` | ADMIN | Remove vítima |

### Doações
| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| GET | `/doacoes` | ADMIN, VOLUNTARIO | Lista paginada |
| GET | `/doacoes/{id}` | ADMIN, VOLUNTARIO | Busca por ID |
| POST | `/doacoes` | ADMIN, VOLUNTARIO | Registra doação |
| PUT | `/doacoes/{id}` | ADMIN | Atualiza doação |
| DELETE | `/doacoes/{id}` | ADMIN | Remove doação |

### Necessidades
| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| GET | `/necessidades` | ADMIN, VOLUNTARIO | Lista paginada |
| GET | `/necessidades/{id}` | ADMIN, VOLUNTARIO | Busca por ID |
| POST | `/necessidades` | ADMIN | Registra necessidade |
| PUT | `/necessidades/{id}` | ADMIN | Atualiza necessidade |
| DELETE | `/necessidades/{id}` | ADMIN | Remove necessidade |

### Sensor
| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| POST | `/sensor/leitura` | ADMIN | Registra leitura; aplica regra de lotação |
| GET | `/sensor/leitura` | ADMIN, VOLUNTARIO | Lista todas as leituras |
| GET | `/sensor/leitura/abrigo/{id}` | ADMIN, VOLUNTARIO | Leituras de um abrigo |

### Matching
| Método | Endpoint | Role | Descrição |
|---|---|---|---|
| POST | `/matching/executar` | ADMIN | Executa engine de matching automático |
| GET | `/matching` | ADMIN, VOLUNTARIO | Histórico de matchings |

---

## Regras de Negócio

**Regra 1 — Lotação automática por sensor:**
Ao registrar `POST /sensor/leitura`, se `ocupacaoAtual >= capacidadeMaxima` → `Abrigo.status = LOTADO`

**Regra 2 — Engine de Matching:**
`POST /matching/executar` localiza doações com `status = DISPONIVEL`, cruza com necessidades `PENDENTE` do mesmo tipo e persiste o `MatchingDoacaoNecessidade`, atualizando os status automaticamente.

**Regra 3 — Soft delete de Abrigo:**
`DELETE /abrigos/{id}` não remove fisicamente — altera `status = INATIVO`.

---

## Como Executar Localmente

### Pré-requisitos
- Java 17+
- Maven 3.8+

### Perfil H2 (desenvolvimento — sem banco externo)

```bash
# Clonar o repositório
git clone https://github.com/pedrovaz100/smartdisaster.git
cd smartdisaster

# Executar
./mvnw spring-boot:run
# ou
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`

Console H2: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:smartdisaster`
- User: `sa` | Senha: *(vazio)*

### Perfil Oracle (produção)

```bash
mvn spring-boot:run -Dspring.profiles.active=oracle
```

Configure as credenciais Oracle em `src/main/resources/application-oracle.properties`.

---

## Autenticação JWT — Passo a Passo

1. `POST /auth/register` — crie um usuário ADMIN
2. `POST /auth/login` — copie o campo `token` da resposta
3. Em todos os demais endpoints, adicione o header:
   ```
   Authorization: Bearer <token>
   ```
4. No Swagger: clique em **Authorize** (cadeado) e cole apenas o token (sem "Bearer ")

---

## Exemplos de Requisições

### POST /auth/register
```json
{
  "nome": "Ana Admin",
  "email": "ana@smartdisaster.com",
  "senha": "admin123",
  "role": "ADMIN"
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
  "dataDoacao": "2025-06-01",
  "status": "DISPONIVEL",
  "voluntarioId": 2
}
```

### POST /necessidades
```json
{
  "tipo": "alimento",
  "descricao": "Alimentos não perecíveis urgente",
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

---

## Critérios Atendidos

| Critério | Implementação |
|---|---|
| API REST com Java + Spring Boot | Spring Boot 3.2.4, organização em camadas |
| Verbos HTTP + Status Codes + HATEOAS | GET/POST/PUT/DELETE + 200/201/204/400/401/403/404/422 + EntityModel/PagedModel |
| Injeção de dependência + Lombok + DevTools | `@RequiredArgsConstructor`, Lombok annotations, spring-boot-devtools |
| Spring Data JPA + JpaRepository + CRUD completo | 7 repositories, CRUD em todos os módulos |
| DTOs como Java Records + Spring Validation | Todos os `*Request` e `*Response` são records com `@NotBlank`, `@NotNull`, `@Valid` |
| Tratamento de exceções padronizado | `GlobalExceptionHandler` com `@RestControllerAdvice` |
| Herança | `Usuario` com `@Inheritance(SINGLE_TABLE)` + discriminator |
| Chave composta | `MatchingId` com `@EmbeddedId` |
| Embedded | `Endereco` com `@Embeddable` |
| Spring Security + JWT | Filtro JWT stateless, roles ADMIN/VOLUNTARIO, BCrypt |
| Swagger/OpenAPI | springdoc 2.3.0, `@Tag`, `@Operation`, `@SecurityScheme` |
| CORS | `CorsConfig` com `UrlBasedCorsConfigurationSource` |
| Deploy público | Railway — https://smartdisasterjava-production.up.railway.app |
