# 📚 Biblioteca Digital — GraphQL Server + Cliente + Conversão JSON/XML

Sistema de gestão de uma **biblioteca digital** que expõe uma API **GraphQL** completa para
administração de autores, livros e empréstimos, acompanhada de um **cliente GraphQL HTTP** e
de um módulo de **exportação/conversão de dados em JSON e XML**.

O projeto foi construído sobre **Java 21** e **Spring Boot 3.5**, seguindo princípios de
**Domain-Driven Design (DDD)** e **Arquitetura Hexagonal (Ports & Adapters)**.

> **Contexto acadêmico:** desenvolvido como entrega da *Prática 7* — (Parte 1) servidor GraphQL,
> (Parte 2) aplicação cliente consumindo o servidor e (Parte 3) serialização de dados em JSON/XML.

---

## 🎯 O que o sistema atende

O domínio modela as operações reais de uma biblioteca:

| Agregado    | Responsabilidades de negócio |
|-------------|------------------------------|
| **Author**  | Cadastro de autores, nacionalidade, biografia e contato. |
| **Book**    | Catálogo de obras com ISBN-13 validado, gênero, ano de publicação, sinopse e controle de disponibilidade. |
| **Loan**    | Ciclo de vida de empréstimos: criação, devolução, cancelamento e detecção de atraso (*overdue*). |

Regras de negócio relevantes implementadas no domínio:

- Um livro só pode ser emprestado quando está **disponível** (`available = true`);
- Devolução de um empréstimo libera novamente o livro;
- Empréstimos vencidos são marcados automaticamente como **`OVERDUE`**;
- **ISBN** é tratado como *Value Object* imutável e auto-validável (ISBN-13);
- Entidades protegem seus invariantes via *factory methods* (`Book.create(...)`) em vez de setters públicos.

---

## 🏛️ Arquitetura

O código está organizado em camadas conforme a **Arquitetura Hexagonal**, isolando o domínio
de frameworks e detalhes de infraestrutura:

```
com.biblioteca.digital
├── domain                # Núcleo de negócio — sem dependência de frameworks
│   ├── model             # Entidades (Author, Book, Loan) e Value Objects (Isbn, Genre, LoanStatus)
│   ├── repository        # Portas (interfaces) de persistência
│   ├── service           # Domain Services (ex.: regras de atraso de empréstimo)
│   └── exception         # Exceções de domínio
│
├── application           # Casos de uso / orquestração
│   ├── usecase           # AuthorUseCase, BookUseCase, LoanUseCase, DataExportUseCase
│   ├── dto               # DTOs e Inputs (Java records, imutáveis)
│   └── mapper            # Conversão entre entidades e DTOs
│
├── infrastructure        # Adaptadores (implementações das portas)
│   ├── persistence       # Repositórios JPA + adapters
│   ├── client            # GraphQLClientService (cliente HTTP — Parte 2)
│   ├── converter         # JsonDataConverter / XmlDataConverter (Parte 3)
│   └── graphql           # Scalar Date e configurações GraphQL
│
└── api                   # Camada de entrada (driving adapters)
    ├── graphql           # Controllers GraphQL (@QueryMapping / @MutationMapping)
    ├── rest              # REST Controller de exportação e cliente
    └── exception         # GlobalExceptionHandler
```

### Decisões de design profissionais

- **Domínio independente de framework:** entidades e VOs não conhecem GraphQL, REST nem Jackson;
  a tradução acontece nos *mappers* e adapters.
- **Ports & Adapters:** `domain.repository.*` define as *portas*; `infrastructure.persistence.*Adapter`
  implementa essas portas sobre Spring Data JPA — permitindo trocar a tecnologia de persistência
  sem tocar no domínio.
- **Records para DTOs/Inputs:** payloads imutáveis e concisos; **Lombok** é restrito às entidades JPA.
- **Value Object `Isbn`:** garante que *toda* instância em memória representa um ISBN válido
  (fail-fast no construtor) — eliminando validações dispersas pelo código.
- **Separação CQRS-leve:** *queries* e *mutations* GraphQL mapeadas em controllers distintos por agregado.
- **Tratamento centralizado de erros** via `GlobalExceptionHandler`, traduzindo exceções de
  domínio em respostas apropriadas.

---

## 🔌 Interfaces expostas

### 1. API GraphQL (Parte 1)

Endpoint: **`POST /graphql`** · UI interativa: **`GET /graphiql`**

O schema ([`schema.graphqls`](src/main/resources/graphql/schema.graphqls)) oferece:

- **Queries:** listagem e busca de autores (por id/nacionalidade), livros
  (por id, ISBN, gênero, autor, disponíveis) e empréstimos (por membro, status, em atraso).
- **Mutations:** CRUD de autores e livros, criação de empréstimo, devolução (`returnBook`),
  cancelamento (`cancelLoan`) e exclusão.
- **Scalar `Date`** customizado para datas (`LocalDate`).

Exemplo de mutation:

```graphql
mutation {
  createLoan(input: {
    bookId: "1"
    memberName: "Maria Silva"
    memberEmail: "maria@email.com"
    expectedReturnDate: "2026-07-01"
  }) {
    id status bookTitle expectedReturnDate
  }
}
```

### 2. Cliente GraphQL HTTP (Parte 2)

[`GraphQLClientService`](src/main/java/com/biblioteca/digital/infrastructure/client/GraphQLClientService.java)
usa `RestClient` para consumir o próprio servidor GraphQL, simulando uma **aplicação cliente externa**.
Exposto via REST:

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/client/query`   | Executa query GraphQL arbitrária (com variáveis). |
| `GET`  | `/api/client/books`   | Busca todos os livros via cliente GraphQL. |
| `GET`  | `/api/client/authors` | Busca todos os autores via cliente GraphQL. |
| `GET`  | `/api/client/loans`   | Busca todos os empréstimos via cliente GraphQL. |

### 3. Exportação / Conversão JSON ↔ XML (Parte 3)

Conversores dedicados ([`JsonDataConverter`](src/main/java/com/biblioteca/digital/infrastructure/converter/JsonDataConverter.java)
e [`XmlDataConverter`](src/main/java/com/biblioteca/digital/infrastructure/converter/XmlDataConverter.java))
serializam o estado completo da biblioteca:

| Método | Rota | Resultado |
|--------|------|-----------|
| `GET` | `/api/export`      | Gera JSON **e** XML e salva em `exports/dados.json` e `exports/dados.xml`. |
| `GET` | `/api/export/json` | Retorna o dataset em `application/json`. |
| `GET` | `/api/export/xml`  | Retorna o dataset em `application/xml`. |

---

## 🛠️ Stack técnica

- **Java 21** (records, *pattern matching*, *text blocks*)
- **Spring Boot 3.5** — `starter-graphql`, `starter-web`, `starter-data-jpa`, `starter-validation`
- **Spring for GraphQL** (schema-first)
- **PostgreSQL** (runtime) · **H2** (testes)
- **Jackson** + `jackson-dataformat-xml` para serialização JSON/XML
- **Lombok** (apenas entidades)
- **Maven** (com wrapper `mvnw`)
- **Testes:** `spring-boot-starter-test` + `spring-graphql-test`

---

## ▶️ Como executar

### Pré-requisitos
- JDK 21
- PostgreSQL acessível em `localhost:5433` (ou ajuste via variáveis de ambiente)

### Configuração do banco

A conexão é parametrizável por variáveis de ambiente (veja [`application.yml`](src/main/resources/application.yml)):

| Variável      | Padrão |
|---------------|--------|
| `DB_URL`      | `jdbc:postgresql://localhost:5433/biblioteca_digital` |
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | `postgres` |

> O schema é recriado a cada início (`ddl-auto: create-drop`) e populado por
> [`data.sql`](src/main/resources/data.sql) — ideal para demonstração.

### Rodando

```bash
# via Maven wrapper
./mvnw spring-boot:run

# ou, no Windows, usando o script de conveniência
run.cmd
```

A aplicação sobe na porta **8088**:

- GraphiQL: <http://localhost:8088/graphiql>
- Endpoint GraphQL: <http://localhost:8088/graphql>
- Exportação: <http://localhost:8088/api/export>

### Testes

```bash
./mvnw test
```

---

## 📦 Coleção Postman

O arquivo [`biblioteca-digital.postman_collection.json`](biblioteca-digital.postman_collection.json)
contém requisições prontas para exercitar as queries, mutations e endpoints REST.

---

## 🧭 Destaques para avaliação profissional

Este projeto vai além de um CRUD acadêmico ao demonstrar práticas de engenharia de software de mercado:

1. **Modelagem rica de domínio (DDD):** regras de negócio residem nas entidades, não em *services* anêmicos.
2. **Arquitetura Hexagonal real:** dependências apontam para o domínio; infraestrutura é plugável.
3. **Imutabilidade e *fail-fast*:** *Value Objects* e *records* reduzem estados inválidos.
4. **Integração GraphQL ponta a ponta:** servidor schema-first + cliente HTTP + tratamento de erros.
5. **Interoperabilidade de dados:** serialização configurável em JSON e XML para integração entre sistemas.
6. **Observabilidade:** logging estruturado e SQL/GraphQL em modo DEBUG para diagnóstico.
7. **Configuração externalizada:** credenciais e endpoints via variáveis de ambiente (12-factor).
