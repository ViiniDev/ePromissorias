# ePromissórias

Sistema desktop desenvolvido em JavaFX para gerenciamento de promissórias. A aplicação permite cadastrar clientes, registrar compras, controlar adiantamentos e acompanhar o valor total e o saldo a pagar de cada promissória.

## Funcionalidades

- Login de acesso ao sistema.
- Cadastro de promissórias.
- Listagem e pesquisa por cliente.
- Visualização detalhada de cada promissória.
- Registro de novas compras.
- Registro e limpeza de adiantamentos.
- Remoção de compras e promissórias.
- Cálculo automático do valor total e do valor pendente.

## Telas

As imagens de referência estão na pasta `Telas`:

- `TelaLogin.png`
- `TelaHome.png`
- `CadastroPromissoria.png`
- `TelaLista-Pesquisa.png`
- `TelaDetalhes.png`

## Tecnologias

- Java 17
- JavaFX
- Maven
- PostgreSQL
- JDBC
- Spring Data JPA

## Estrutura do Projeto

```text
src/main/java/com/demo/epromissorias
├── connection
│   └── DatabaseConnection.java
├── controllers
├── dao
├── entities
└── MainApp.java
```

## Configuração do Banco

Crie um banco PostgreSQL chamado `ePromissorias`:

```sql
CREATE DATABASE "ePromissorias";
```

Configure as credenciais por variáveis de ambiente:

```bash
EPROMISSORIAS_DB_URL=jdbc:postgresql://localhost:5432/ePromissorias
EPROMISSORIAS_DB_USER=postgres
EPROMISSORIAS_DB_PASSWORD=sua_senha
```

O projeto também possui valores padrão para ambiente local, mas a senha deve ser informada por variável de ambiente.

## Como Executar

```bash
./mvnw clean package
java -jar target/ePromissorias-1.0-SNAPSHOT.jar
```

No Windows:

```bash
mvnw.cmd clean package
java -jar target/ePromissorias-1.0-SNAPSHOT.jar
```

## Observações

- O projeto combina interface JavaFX com persistência em PostgreSQL.
- A autenticação atual é simples e voltada para estudo.
- Arquivos gerados por IDE e builds locais foram removidos do versionamento para manter o repositório mais limpo.

## Melhorias Futuras

- Criar tela administrativa para usuários.
- Substituir login fixo por autenticação persistida no banco.
- Adicionar scripts SQL de criação das tabelas.
- Adicionar testes para regras de cálculo.
- Criar empacotamento distribuível da aplicação desktop.
