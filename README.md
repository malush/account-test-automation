# Automated integration testing project for account transactional system

This project contains integration tests for the following coding challenge project:
https://bitbucket.digipay.com.mt/projects/COD/repos/radu-solution/browse

## About the project under test
It is a financial institution's account transactional system developed as
a Spring Boot Application with REST APIs secured with JSON Web Token (JWT)
with accompanying PosgreSQL database

Features:
* Sign up an user
* Login the user
* Create new account
* Get an account
* Load an account
* Transfer between accounts

## About this project
The requirement was to build the project in Java together with the adequate
testing framework around it.

The build tool of choice is Grade.
The project has following dependencies:

* JUnit
* Cucumber
* PicoContainer
* SnakeYAML
* REST Assured
* PostgreSQL JDBC Driver

## Running the integration tests on your local environment

#### 1. Get the Postgres docker image
```
docker pull postgres
mkdir -p ~/docker/volumes/postgres
docker run --rm  --name pg-docker -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 -v ~/docker/volumes/postgres:/var/lib/postgresql/data  postgres
psql -h localhost -U postgres -d postgres
```
#### 2. Once the container is up and running you can connect to it
```
psql -h localhost -U postgres -d postgres
```
#### 3. Clone the radu-solution project
```
git clone https://bitbucket.digipay.com.mt/scm/cod/radu-solution.git
```
#### 4. Verify that database credentials, connection url and server port are set properly
```
 radu-solution/src/main/resources/application.yml

 e.g.
 server.port: 8095
 spring.datasource:
   url: jdbc:postgresql://localhost:5432/postgres
   username: postgres
   password: postgres
```

#### 5. Run the Spring Boot application
Either from your favorite IDE (e.g. Intellij), or as a packaged application,
or with Maven or Gradle.
#### 6. Clone this project:
```
git clone git@github.com:malush/account-test-automation.git

```
#### 7.Verify that database credentials, connection url and server port are set properly
```
account-test-automation/integration/src/test/resources/settings.yaml

e.g.

databaseConfig:
  connectionUrl: jdbc:postgresql://localhost:5432/postgres
  username: postgres
  password: postgres

webConfig:
  baseUri: http://localhost
  port: 8095
```

#### 8. Run the tests
Either from your favorite IDE (e.g. Intellij) (Run 'TestRunner') Ctrl+Shift+F10

Or use Gradle. In the root of the project run the test task with Gradle Wrapper
```
./gradlew test --rerun-tasks
```