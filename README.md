# MOOC IFPR - Plataforma de Cursos Online

Sistema de gerenciamento de cursos online massivos (MOOC) desenvolvido para o IFPR Campus Foz do Iguaçu. A plataforma permite que administradores criem e gerenciem cursos, enquanto alunos podem se inscrever, acompanhar seu progresso e solicitar certificados de conclusão.

## 🚀 Tecnologias

- **Backend**: Java 21 + Spring Boot 3.5.5
- **Banco de Dados**: PostgreSQL
- **Autenticação**: JWT (JSON Web Tokens)
- **Migrations**: Flyway

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

### 3. Compilar e Executar

No diretório raiz do projeto, execute:
```bash
# Compilar o projeto
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080/mooc`

### 4. Importe Arquivo Insomnia

Após iniciar a aplicação, importe a collection do insomina que esta no seguinte arquivo:
```
mooc-insomnia.yaml
```

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
- Inscrições e Progresso
- Certificados e Solicitações
- Campus e Áreas de Conhecimento

Dados de exemplo (cursos, campus, áreas de conhecimento) são populados automaticamente.

## 🔐 Autenticação

Para acessar endpoints protegidos:

1. Faça login via `POST /mooc/auth/login`
2. Use o token JWT retornado no header `Authorization: Bearer {token}`

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