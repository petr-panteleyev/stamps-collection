# Каталог коллекции марок

Подробности см. на [сайте проекта](https://www.panteleyev.ru/projects/stamps-collection/).

## Сборка

Окружение:
- JDK 27+
- JavaFX 27
- Maven 3.9.16

```shell
export JAVA_HOME=/path/to/jdk-27
mvn clean install
```

## Запуск

Сервер:

```shell
mvn -pl backend spring-boot:run -Dspring-boot.run.profiles=prod
```

Приложение:

```shell
mvn -pl desktop exec:exec@run
```
