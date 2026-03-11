# 📚 Literatura Gutendex 

Aplicação Java com Spring Boot para catálogo de livros consumindo a API Gutendex (Project Gutenberg), persistindo dados em PostgreSQL com análise de estatísticas.

## 🎯 Funcionalidades

- 🔍 Buscar livro por título na API Gutendex
- 📖 Listar todos os livros registrados
- 👤 Listar todos os autores registrados
- 📅 Listar autores vivos em determinado ano
- 🌐 Listar livros por idioma
- 📊 Estatísticas de livros por idioma (Streams + Derived Queries)

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 3.2**
- **Spring Data JPA**
- **PostgreSQL**
- **Jackson 2.16** (JSON parsing)
- **HttpClient** (Java 11+)
- **Maven**

## 📋 Pré-requisitos

- Java 17 ou superior
- PostgreSQL 12+
- IntelliJ IDEA (recomendado)
- Maven 3.8+

## 🚀 Configuração do Banco de Dados

1. Crie o banco de dados no PostgreSQL:
```sql
CREATE DATABASE literatura_db;
spring.datasource.url=jdbc:postgresql://localhost:5432/literatura_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

## ⚙️ Como Executar
Opção 1: IntelliJ IDEA
Importe como projeto Maven
Aguarde o download das dependências
Execute a classe LiteraturaApplication
Opção 2: Terminal

# Compilar
mvn clean install

# Executar
mvn spring-boot:run
