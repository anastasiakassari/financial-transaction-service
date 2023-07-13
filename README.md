# Financial Transaction Service

The Financial Transaction Service is a microservice built with Spring Boot and Gradle (Kotlin script) that handles bank
accounts and creates simple financial transactions between them. The available currencies of the accounts are defined as
an enum:

```java
public enum Currency {
    EUR, USD, GBP
}
```

Transactions can be performed by setting a source and target account with the same currency.

## Features

- Exposes a RESTful API for creating new accounts, financial transactions and retrieving them from the database
- Covers multiple scenarios, including successful money transfer between two accounts, validation of request
  parameters
  (e.g. invalid transfer amount), avoid transfer between the same account, etc.

## Requirements

- Java 17 or higher
- Postgres
- Docker (optional)

## Getting Started

These instructions will help you set up the project and run it locally.

### Prerequisites

- Java 17 or later installed on your system.
- A PostgreSQL database set up and running. You can install PostgreSQL from the official website or use a PostgreSQL
  Docker container. Make sure to note down the database credentials (username and password) and the connection details (
  host, port, and database name) for configuration.

- The application requires the following environment variables to be set:
    - `DB_USERNAME`: The username for the PostgreSQL database. (e.g. `postgres`)
    - `DB_PASSWORD`: The password for the PostgreSQL database. (e.g. `password`)
    - `DB_NAME`: The name of the PostgreSQL database. (e.g. `fts`)
    - `DB_HOST`: The hostname or IP address of the PostgreSQL database server. (e.g. `localhost`)
    - `DB_PORT`: The port number of the PostgreSQL database server. (e.g. `5432`)
    - `PROFILE`: The active Spring profile for the application. (e.g. `test`)

You can either set these environment variables directly on your local development environment or provide them when
deploying the application to a server or container.

Please ensure that these environment variables are properly configured before running the application.

### Installing

1. Clone this repository:

```shell
git clone https://github.com/anastasiakassari/financial-transaction-service.git
```

2. Navigate to the project directory:

```shell
cd financial-transaction-service
```

3. Build the project with Gradle:

```shell
./gradlew bootJar
```

### Running the Application

1. Run the application with Gradle:

```shell
./gradlew bootRun
```

2. The application will start running on `http://localhost:8080`.

### Running Tests

Run the unit tests with Gradle:

```shell
./gradlew test
```

## Deployment with Docker

To deploy the application using Docker follow these steps:

### Prerequisites

Make sure you have Docker and Docker Compose installed on your system.

### Configuration

Before deploying the application, you need to provide the necessary configuration in the `docker-compose.yml` file:

1. Open the `docker-compose.yml` file.
2. Set the desired environment variables under the `environment` section of the `app` service:

- `DB_USERNAME`: The username for the PostgreSQL database.
- `DB_PASSWORD`: The password for the PostgreSQL database.
- `DB_NAME`: The name of the PostgreSQL database.

### Deployment

1. Open a terminal or command prompt.
2. Navigate to the root directory of your project where the `docker-compose.yml` file is located.3.
3. Run the following command to build the Docker image:

```shell
docker-compose build
```

4. Run the following command to start the Docker containers:

```shell
docker-compose up -d
```

5. Docker Compose will build the application and start the containers defined in the `docker-compose.yml` file.
6. Wait for the containers to start up. You can check the logs for any issues using the command:

```shell
docker-compose logs -f
```

7. Once the containers are running, the application will be accessible at `http://localhost:8080`.

### Stopping the Deployment

To stop the application and shut down the Docker containers, run the following command in the same directory as
the `docker-compose.yml` file:

```shell
docker-compose down
```

This will stop and remove the containers created by Docker Compose.

### Cleaning Up

If you want to remove the built Docker images as well, run the following command:

```shell
docker-compose down --rmi all
```

This will stop the containers and remove both the containers and their associated images.

## API Documentation

The application provides API documentation using the OpenAPI specification. The documentation describes all the
available endpoints, request/response structures, and other important details.

To access the API documentation go to `http://localhost:8080/swagger-ui.html`.


