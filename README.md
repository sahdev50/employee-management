# Employee Management

A Java and Spring Boot backend for managing employee and organizational data. The application exposes REST endpoints and uses PostgreSQL for persistence.

## Features

- Employee, department, and team management
- Location and organization data, including building, city, country, and location modules
- Persistence with Spring Data JPA and PostgreSQL
- Request validation
- Spring Security support

## Tech stack

- Java 21
- Spring Boot 4.1.0
- Maven (Maven Wrapper included)
- PostgreSQL
- Spring Data JPA, Spring MVC, and Bean Validation

## Requirements

- JDK 21
- PostgreSQL

You do not need to install Maven separately; use the included Maven Wrapper commands below.

## Getting started

1. Clone the repository and enter the project directory:

   ```bash
   git clone https://github.com/sahdev50/employee-management.git
   cd employee-management
   ```

2. Create a PostgreSQL database for the application. The current configuration expects a database named `employeeDB` on `localhost:5434`.

3. Configure the database connection and any security credentials in `src/main/resources/application.properties` for your environment. Do not use the example credentials in a shared or production environment, and do not commit real passwords or secrets to Git.

4. Run the application:

   **macOS/Linux**
   ```bash
   ./mvnw spring-boot:run
   ```

   **Windows**
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

The application starts with the Spring profile configured in `application.properties`. Check the application logs for the startup status and configured port.

## Build and test

Build the project:

```bash
./mvnw clean package
```

Run the tests:

```bash
./mvnw test
```

On Windows, replace `./mvnw` with `.\mvnw.cmd`.

## Project structure

```text
src/
├── main/
│   ├── java/com/employee/management/
│   │   ├── employee/
│   │   ├── department/
│   │   ├── team/
│   │   ├── building/
│   │   ├── city/
│   │   ├── country/
│   │   ├── location/
│   │   └── security/
│   └── resources/
└── test/
```

Each feature package contains the application components for that domain. Explore the controllers in the feature packages to see the available routes and request formats.

## Configuration and security

Database and application settings are in `src/main/resources/application.properties`. Set values appropriate for your local environment before running the service. In particular, replace development credentials and avoid exposing secrets in source control. The project includes Spring Security; configure authentication and access rules to suit your deployment.

## License

No license is currently specified. Unless a license is added, standard copyright restrictions apply.
