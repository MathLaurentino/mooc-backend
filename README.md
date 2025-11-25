# MOOC IFPR - Plataforma de Cursos Online

Sistema de gerenciamento de cursos online massivos (MOOC) desenvolvido para o IFPR Campus Foz do Iguaçu. A plataforma permite que administradores criem e gerenciem cursos, enquanto alunos podem se inscrever, acompanhar seu progresso e solicitar certificados de conclusão.

## 🚀 Tecnologias

- **Backend**: Java 21 + Spring Boot 3.5.5
- **Banco de Dados**: PostgreSQL
- **Autenticação**: JWT (JSON Web Tokens)
- **Migrations**: Flyway

## 🌐 URLs de Stage

O projeto está atualmente em stage nos seguintes endereços:

- **Backend**: http://200.17.101.2:8000/mooc
- **Frontend**: http://200.17.101.2:3000 (em breve)

## 📋 Pré-requisitos

- Java 21 ou superior
- PostgreSQL 12 ou superior
- Maven 3.8 ou superior

## 🔧 Configuração e Instalação

### 1. Configurar o Banco de Dados

Crie um banco de dados PostgreSQL:
```sql
CREATE DATABASE mooc;
```

### 2. Configurar Credenciais

Edite o arquivo `src/main/resources/application.properties` com suas credenciais do PostgreSQL:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mooc
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 3. Configurar URLs (Ambiente Local)

**IMPORTANTE**: Se você for executar o projeto **localmente**, é necessário alterar as seguintes propriedades no arquivo `application.properties`:
```properties
# Para ambiente local, altere de:
server.base-url=http://200.17.101.2:8000
frontend.base-url=http://200.17.101.2:3000

# Para:
server.base-url=http://localhost:8080
frontend.base-url=http://localhost:3000
```

> **Nota**: As URLs configuradas por padrão (`200.17.101.2`) são para o ambiente de stage. Para desenvolvimento local, utilize `localhost`.

### 4. Compilar e Executar

No diretório raiz do projeto, execute:
```bash
# Compilar o projeto
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080/mooc`

### 5. Importe Arquivo Insomnia

Após iniciar a aplicação, importe a collection do Insomnia que está no seguinte arquivo:
```
mooc-insomnia.yaml
```

A collection possui dois ambientes configurados:
- **localhost**: Para desenvolvimento local (http://localhost:8080)
- **stage**: Para ambiente de stage (http://200.17.101.2:8000)

Você pode alternar entre os ambientes clicando no seletor no **canto superior esquerdo** do Insomnia.

## 👤 Usuários Padrão

O sistema cria automaticamente dois usuários para testes:

**Administrador:**
- Email: `admin@mooc.ifpr.edu.br`
- Senha: `senha`

**Aluno:**
- Email: `estudante@mooc.ifpr.edu.br`
- Senha: `senha`

## 📁 Estrutura de Dados

O Flyway criará automaticamente todas as tabelas necessárias:
- Usuários (alunos e administradores)
- Cursos e Aulas
- Inscrições e Progresso de Aula
- Certificados e Solicitação de Certificado
- Campus e Áreas de Conhecimento

Dados de exemplo (cursos, campus, áreas de conhecimento) são populados automaticamente.

## 🔐 Autenticação

Para acessar endpoints protegidos:

1. Faça login via `POST /mooc/auth/login`
2. Use o token JWT retornado no header `Authorization: Bearer {token}`

> **💡 Dica para usuários do Insomnia**: A collection está configurada para capturar automaticamente o token JWT da resposta de login e armazená-lo na variável global `jwt_token`. Isso significa que você não precisa copiar e colar o token manualmente em cada requisição - todas as requisições protegidas já estão configuradas para usar essa variável automaticamente!

## 📦 Principais Funcionalidades

- Gerenciamento de cursos e aulas
- Sistema de inscrições
- Acompanhamento de progresso
- Solicitação e emissão de certificados digitais
- Upload de thumbnails para cursos
- Gestão de campus e áreas de conhecimento

## 🛠️ Troubleshooting

**Erro de conexão com o banco:**
- Verifique se o PostgreSQL está rodando
- Confirme as credenciais no `application.properties`
- Certifique-se de que o banco `mooc` foi criado

**Erro de compilação:**
- Verifique se está usando Java 21
- Execute `mvn clean install -U` para atualizar dependências

**Problemas com URLs/CORS:**
- Verifique se as propriedades `server.base-url` e `frontend.base-url` estão configuradas corretamente para seu ambiente (local ou stage)
- Para desenvolvimento local, use `localhost` ao invés dos IPs de stage
