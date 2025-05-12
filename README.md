erp
===

### Enterprise resource planning application.

[![Continuous integration](https://github.com/jbence1994/erp/actions/workflows/build.yaml/badge.svg)](https://github.com/jbence1994/erp/actions/workflows/build.yaml)

Prerequisites
-------------

To avoid any unexpected application behavior, make sure you have installed the following tools with the proper version
numbers:

- [Eclipse Temurin JDK 21](https://adoptium.net/temurin/releases/?version=21)
- [Maven 3.9.6](https://maven.apache.org/download.cgi)

Run project locally
-----------------------

Be sure to copy `.env.example` to `.env` and update it with your local database connection parameters before running the
application.

### Build application and database schema populating it with test data with Flyway Maven plugin

```bash
mvn clean install
```

### Starting application with Spring Boot Maven plugin

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Note: The `default` Spring profile is used by GitHub Actions workflows to make sure JPA starts up without requiring
an external database, thus preventing build failures. GitHub Actions will pick up `default` automatically.**
