# Sistema de Gerenciamento de Promissórias

Este projeto é um sistema de gerenciamento de promissórias desenvolvido em JavaFX e PostgreSQL. O sistema permite o cadastro, visualização e gerenciamento de promissórias, incluindo funcionalidades de adicionar compras, registrar adiantamentos, e calcular automaticamente o valor total e o valor a pagar.

## Funcionalidades Principais

### 1. **Tela de Login**
   - O usuário deve inserir as credenciais corretas para acessar o sistema.
   - Caminho para a imagem da tela: `Telas/TelaLogin.png`

### 2. **Tela Home**
   - Após o login bem-sucedido, o usuário será redirecionado para a tela principal (Home).
   - Opções disponíveis:
     - **Cadastrar Promissória**: permite registrar uma nova promissória com as informações do cliente e da compra.
     - **Visualizar Promissórias**: permite listar todas as promissórias cadastradas, com uma opção de pesquisa por nome do cliente.
     - **Sair**: encerra a sessão e retorna à tela de login.
   - Caminho para a imagem da tela: `Telas/TelaHome.png`

### 3. **Tela de Cadastro de Promissória**
   - Nesta tela, o usuário pode cadastrar uma nova promissória.
   - Informações necessárias:
     - Nome do cliente
     - CPF ou RG
     - Valor da compra inicial
     - Data da compra
   - Caminho para a imagem da tela: `Telas/TelaCadastroPromissoria.png`

### 4. **Tela de Visualização de Promissórias**
   - Exibe uma lista de todas as promissórias registradas no sistema.
   - Inclui um campo de pesquisa para facilitar a localização de uma promissória específica por nome do cliente.
   - Caminho para a imagem da tela: `Telas/TelaVisualizar-Pesquisar.png`

### 5. **Tela de Detalhes da Promissória**
   - Ao clicar em uma promissória na tela de visualização, o usuário é redirecionado para a tela de detalhes, que mostra todas as informações associadas àquela promissória.
   - Funcionalidades:
     - **Adicionar Compra**: permite adicionar uma nova compra à promissória existente.
     - **Adicionar Adiantamento**: disponível somente se houver compras registradas. Permite registrar um adiantamento para abatimento no valor total.
     - **Apagar Compra**: remove uma compra específica da promissória.
     - **Limpar Histórico de Adiantamentos**: apaga todos os adiantamentos registrados, voltando o valor a pagar ao total das compras.
     - **Apagar Promissória**: remove completamente a promissória do sistema.
   - O **valor total** é calculado pela soma de todas as compras associadas à promissória.
   - O **valor a pagar** é calculado pela subtração dos adiantamentos feitos do valor total.
   - Caminho para a imagem da tela: `Telas/TelaDetalhes.png`

## Configuração do Banco de Dados

O sistema utiliza PostgreSQL como banco de dados relacional. Certifique-se de que o PostgreSQL esteja instalado e configurado corretamente antes de executar o sistema.

### Requisitos:
- PostgreSQL 17 ou superior
- Java 17 ou superior
- JavaFX jdk
- Maven (para gerenciamento de dependências)

### Configuração do Banco:
1. Crie um banco de dados chamado `promissoriasdb`.
2. Tabelas `promissoria`, `compras` e `adiantamento` sendos essas duas relacionadas a promissoria
3. Atualize o arquivo `application.properties` com as credenciais corretas do banco de dados.

## Tecnologias Utilizadas

- **JavaFX**: Interface gráfica para construção das telas.
- **PostgreSQL**: Banco de dados relacional.
- **Maven**: Gerenciador de dependências e construção do projeto.
- **Spring Boot**: Framework utilizado para a camada de serviço e integração com o banco de dados.

