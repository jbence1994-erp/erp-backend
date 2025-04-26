erp
===

### Enterprise resource planning application.

[![Continuous integration](https://github.com/jbence1994/erp/actions/workflows/build.yml/badge.svg)](https://github.com/jbence1994/erp/actions/workflows/build.yml)

Prerequisites
-------------

To avoid any unexpected application behavior, make sure you have installed the following tools with the proper version numbers:

- [Eclipse Temurin JDK 21](https://adoptium.net/temurin/releases/?version=21)
- [Maven 3.9.6](https://maven.apache.org/download.cgi)

Run project locally
-----------------------

### Build application

```bash
mvn clean install
```

### Starting application with Spring Boot Maven plugin

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=default
```
