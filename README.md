# CineTrack

Sistema web para organizar filmes assistidos e filmes que o usuário deseja assistir.

O CineTrack permite cadastrar, editar, excluir e filtrar filmes. A aplicação também consulta a API do TMDB para localizar automaticamente as capas dos filmes cadastrados.

## Autor

Desenvolvido por **Alexandre Milton Alves** como projeto do curso Jovem Programador.

## Funcionalidades

- Cadastro de gêneros;
- Edição e exclusão de gêneros;
- Cadastro de filmes;
- Edição e exclusão de filmes;
- Classificação entre “Quero assistir” e “Assistido”;
- Avaliação de filmes assistidos com nota de 0 a 10;
- Comentários pessoais sobre os filmes;
- Filtro por gênero;
- Filtro por status;
- Filtro por nota mínima;
- Exibição responsiva em cards;
- Busca automática de capas no TMDB;
- Cadastro disponível mesmo sem internet;
- Atualização automática de capas pendentes;
- Layout responsivo para computadores, tablets e celulares.

## Tecnologias utilizadas

### Back-end

- Java 21;
- Spring Boot 4.1.1;
- Spring MVC;
- Spring Data JPA;
- Jakarta Bean Validation;
- Hibernate;
- Maven.

### Front-end

- HTML5;
- CSS3;
- JavaScript;
- Thymeleaf.

### Banco de dados e integração

- MySQL;
- API do The Movie Database (TMDB).

## Regras de negócio

- Todo filme deve possuir título, ano, gênero e status;
- O ano deve estar entre 1888 e 2100;
- A nota deve estar entre 0 e 10;
- Filmes com status “Assistido” devem possuir nota;
- Filmes com status “Quero assistir” não mantêm nota;
- Não é permitido cadastrar gêneros duplicados;
- Um gênero associado a filmes não pode ser excluído;
- Falhas na API do TMDB não impedem o cadastro;
- Filmes sem capa são atualizados automaticamente quando a integração volta a ficar disponível.

## Pré-requisitos

Antes de executar o CineTrack, instale:

- Java Development Kit 21;
- MySQL Server;
- Git;
- Visual Studio Code ou outra IDE compatível com Java.

Não é necessário instalar o Maven separadamente, pois o projeto utiliza Maven Wrapper.

## Configuração do banco de dados

Acesse o MySQL e crie o banco:

```sql
CREATE DATABASE cinetrack
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

As tabelas são criadas e atualizadas automaticamente pelo Hibernate durante a inicialização da aplicação.

Por padrão, o projeto utiliza:

```text
Banco: cinetrack
Usuário: root
Senha: root
Porta: 3306
```

Esses valores podem ser substituídos por variáveis de ambiente:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/cinetrack"
$env:DB_USERNAME="seu_usuario"
$env:DB_PASSWORD="sua_senha"
```

## Configuração do TMDB

A integração com o TMDB é opcional. Sem o token, o sistema continua permitindo o cadastro de filmes, porém as capas ficam pendentes.

Crie uma conta no TMDB, solicite acesso à API e copie o valor chamado **API Read Access Token**.

No PowerShell, configure:

```powershell
$env:TMDB_API_TOKEN="seu_token"
```

O token nunca deve ser escrito diretamente no `application.properties` nem enviado ao GitHub.

Quando a integração estiver disponível, o sistema:

- pesquisa a capa pelo título e ano;
- salva o identificador do TMDB;
- armazena a URL da capa;
- tenta atualizar automaticamente filmes cadastrados sem capa.

## Como executar

Clone o repositório:

```powershell
git clone https://github.com/alexandredevsc/CineTrack.git
```

Entre na pasta:

```powershell
cd CineTrack
```

Execute os testes:

```powershell
.\mvnw.cmd test
```

Inicie a aplicação:

```powershell
.\mvnw.cmd spring-boot:run
```

Depois, acesse:

```text
http://localhost:8080/filmes
```

## Como interromper a aplicação

No terminal em que o sistema está executando, pressione:

```text
Ctrl + C
```

## Estrutura do projeto

```text
src
├── main
│   ├── java/com/alexandre/cinetrack
│   │   ├── controller
│   │   ├── entity
│   │   ├── integration/tmdb
│   │   ├── repository
│   │   └── service
│   └── resources
│       ├── static
│       │   ├── css
│       │   └── js
│       ├── templates
│       │   ├── filmes
│       │   └── generos
│       └── application.properties
└── test
    └── java/com/alexandre/cinetrack
```

### Responsabilidade das camadas

- `controller`: recebe as requisições e direciona as páginas;
- `entity`: representa as tabelas e os dados do sistema;
- `repository`: realiza operações no banco de dados;
- `service`: concentra validações e regras de negócio;
- `integration/tmdb`: comunica-se com a API externa;
- `templates`: contém as páginas processadas pelo Thymeleaf;
- `static`: contém os arquivos CSS e JavaScript.

## Fluxo de utilização

1. Cadastre os gêneros que serão utilizados;
2. Acesse a página de filmes;
3. Cadastre um filme informando título, ano, gênero e status;
4. Informe uma nota se o filme estiver marcado como “Assistido”;
5. O sistema tentará localizar automaticamente a capa no TMDB;
6. Caso a integração esteja indisponível, o filme será salvo sem capa;
7. Quando a integração voltar, as capas pendentes serão atualizadas;
8. Use os filtros para organizar a coleção;
9. Edite ou exclua os registros quando necessário.

## Principais rotas

| Método | Rota | Função |
|---|---|---|
| GET | `/filmes` | Lista os filmes |
| GET | `/filmes/novo` | Abre o cadastro de filme |
| POST | `/filmes` | Cadastra um filme |
| GET | `/filmes/{id}/editar` | Abre a edição de filme |
| POST | `/filmes/{id}` | Atualiza um filme |
| POST | `/filmes/{id}/excluir` | Exclui um filme |
| GET | `/generos` | Lista os gêneros |
| GET | `/generos/novo` | Abre o cadastro de gênero |
| POST | `/generos` | Cadastra um gênero |
| GET | `/generos/{id}/editar` | Abre a edição de gênero |
| POST | `/generos/{id}` | Atualiza um gênero |
| POST | `/generos/{id}/excluir` | Exclui um gênero |

## Estratégia de branches

O projeto utiliza uma branch estável, uma branch de integração e branches específicas para cada funcionalidade:

```text
main
└── develop
    └── feature/nome-da-funcionalidade
```

Cada funcionalidade é desenvolvida em sua própria branch. Depois de testada, ela é integrada à `develop` por meio de um Pull Request.

A branch `main` recebe somente versões estáveis e prontas para apresentação ou entrega.

## Créditos

Desenvolvido pelo desenvolvedor full-stack **Alexandre Milton Alves**.

Este produto usa a API do TMDB, mas não é endossado nem certificado pelo TMDB.

Os dados e as imagens dos filmes são fornecidos pelo [The Movie Database](https://www.themoviedb.org/).

## Licença

Projeto desenvolvido para fins educacionais.
