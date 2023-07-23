### Hexlet tests and linter status:
[![Actions Status](https://github.com/NowUKnow1/java-project-73/workflows/hexlet-check/badge.svg)](https://github.com/NowUKnow1/java-project-73/actions)
<a href="https://codeclimate.com/github/NowUKnow1/java-project-73/maintainability"><img src="https://api.codeclimate.com/v1/badges/acc5ad764473b4c79e1c/maintainability" /></a>
<a href="https://codeclimate.com/github/NowUKnow1/java-project-73/test_coverage"><img src="https://api.codeclimate.com/v1/badges/acc5ad764473b4c79e1c/test_coverage" /></a>
[![Java CI](https://github.com/NowUKnow1/java-project-73/actions/workflows/main.yml/badge.svg)](https://github.com/NowUKnow1/java-project-73/actions/workflows/main.yml)

# Task Manager

[Task Manager](https://task-manager-7f8t.onrender.com) – система управления задачами. Она позволяет ставить задачи, назначать исполнителей и менять их статусы. Для работы с системой требуется регистрация и аутентификация.

**Технологии:**

Java 17
Spring Boot, MVC, Data JPA, Validator
Swagger, Lombok
Gradle
Liquibase, H2, PostgreSQL
Spring Security, JWT

**Разработка:**

Прежде чем Вы сможете собрать этот проект, Вы должны установить и настроить следующие зависимости на своем компьютере:

JDK 17
Gradle 7.4
Node.js 16.13.1

## Сборка проекта

```
make build
```

## Тестирование проекта

Для запуска теста приложения, запустите:

```
make test
```

## Запуск приложения с локальной базой данных
```
make start
```
