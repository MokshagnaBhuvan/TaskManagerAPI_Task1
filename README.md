```markdown
# Task Manager API (Task1)

A lightweight, production-ready RESTful Task Management API built with Java, Spring Boot, Maven and MongoDB.  
This README is a clear, practical guide for developers who want to run, test and extend the service locally or in containers.

Repository
- GitHub: https://github.com/MokshagnaBhuvan/TaskManagerAPI_Task1

Quick summary
- Language: Java (recommended: Java 17)
- Build: Maven
- Framework: Spring Boot
- Database: MongoDB
- Local testing: Postman / curl
- Structure: controller -> service -> repository -> model

Table of contents
- [Features](#features)
- [Tech stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Quick start — run locally](#quick-start---run-locally)
- [Configuration](#configuration)
- [Run with Docker (MongoDB)](#run-with-docker-mongodb)
- [API reference (examples)](#api-reference-examples)
- [Data model](#data-model)
- [Validation & Error responses](#validation--error-responses)
- [Testing](#testing)
- [Dockerizing the app](#dockerizing-the-app)
- [CI / CD suggestions](#ci--cd-suggestions)
- [Postman & OpenAPI](#postman--openapi)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License & Contact](#license--contact)

---

## Features
- Full CRUD for tasks
- Input validation using Bean Validation (javax.validation)
- createdAt / updatedAt timestamps
- Basic pagination & filtering support
- Clear layered architecture (Controller → Service → Repository)
- MongoDB persistence with Spring Data MongoDB
- Ready for adding authentication (JWT/OAuth) and rate-limiting

## Tech stack
- Java 17 (recommended; project may be compatible with Java 11)
- Spring Boot (Web, Validation, Data MongoDB)
- Maven
- MongoDB (local, hosted, or containerized)
- JUnit 5 + Mockito for tests
- Optional: Testcontainers for integration tests with MongoDB

## Prerequisites
- Java 17+ and JAVA_HOME set
- Maven 3.6+
- MongoDB (local, remote or Docker)
- Git
- (Optional) Docker & Docker Compose
- (Optional) Postman for manual API testing

---

## Quick start — run locally

1. Clone repository
```bash
git clone https://github.com/MokshagnaBhuvan/TaskManagerAPI_Task1.git
cd TaskManagerAPI_Task1
```

2. Configure MongoDB (see [Configuration](#configuration)). For quick testing you can run MongoDB in Docker (see below).

3. Build the project
```bash
mvn clean package -DskipTests=false
```

4. Run the application
```bash
# from Maven
mvn spring-boot:run

# or via JAR
java -jar target/<artifact-name>.jar
```

Default server port: `8080` (configurable)

---

## Configuration

Primary configuration locations:
- `src/main/resources/application.properties` or
- `src/main/resources/application.yml`

Recommended properties (example `application.properties`):
```
spring.data.mongodb.uri=mongodb://localhost:27017/taskmanagerdb
spring.profiles.active=local
server.port=8080
```

Environment variable alternatives:
- `SPRING_DATA_MONGODB_URI` — override MongoDB URI
- `SERVER_PORT` — override port

Production Mongo example:
```
spring.data.mongodb.uri=mongodb://username:password@host:27017/taskmanagerdb
```

---

## Run with Docker (MongoDB)

Use a small `docker-compose.yml` to run MongoDB locally for development:

```yaml
version: "3.8"
services:
  mongo:
    image: mongo:6
    container_name: taskmanager-mongo
    restart: unless-stopped
    ports:
      - "27017:27017"
    environment:
      MONGO_INITDB_DATABASE: taskmanagerdb
    volumes:
      - mongo-data:/data/db

volumes:
  mongo-data:
```

Start:
```bash
docker compose up -d
```

Set `spring.data.mongodb.uri=mongodb://localhost:27017/taskmanagerdb` and start the app.

---

## API reference (examples)

Base URL (default): `http://localhost:8080`  
Base path used: `/api/tasks` (update if your controllers use a different base path)

### Create a Task
- Method: `POST`
- URL: `/api/tasks`
- Request JSON:
```json
{
  "title": "Finish README",
  "description": "Write the README for TaskManagerAPI",
  "dueDate": "2025-11-01T12:00:00Z",
  "priority": "MEDIUM",
  "completed": false
}
```
- Success: `201 Created`
```json
{
  "id": "653a1e3b...",
  "title": "Finish README",
  "description": "Write the README for TaskManagerAPI",
  "dueDate": "2025-11-01T12:00:00Z",
  "priority": "MEDIUM",
  "completed": false,
  "createdAt": "2025-10-20T18:00:00Z",
  "updatedAt": "2025-10-20T18:00:00Z"
}
```

### List Tasks (pagination & filter)
- Method: `GET`
- URL: `/api/tasks?page=0&size=20&completed=false`
- Success: `200 OK` — returns an array or page object depending on your implementation

### Get Task by ID
- Method: `GET`
- URL: `/api/tasks/{id}`
- Success: `200 OK` or `404 Not Found`

### Update Task (full)
- Method: `PUT`
- URL: `/api/tasks/{id}`
- Body: full task payload (same as create)
- Success: `200 OK`

### Patch Task (partial)
- Method: `PATCH`
- URL: `/api/tasks/{id}`
- Body: partial JSON with fields to update
- Success: `200 OK`

### Delete Task
- Method: `DELETE`
- URL: `/api/tasks/{id}`
- Success: `204 No Content`

#### Example curl (create)
```bash
curl -s -X POST "http://localhost:8080/api/tasks" \
  -H "Content-Type: application/json" \
  -d '{"title":"Finish README","description":"Write README","priority":"HIGH"}'
```

---

## Data model (example)

Task document stored in MongoDB:
```json
{
  "id": "ObjectId",
  "title": "string",
  "description": "string",
  "priority": "LOW|MEDIUM|HIGH",
  "dueDate": "ISO-8601 datetime",
  "completed": "boolean",
  "createdAt": "ISO-8601 datetime",
  "updatedAt": "ISO-8601 datetime"
}
```

Suggested DTOs / fields:
- `TaskRequest` — title, description, dueDate, priority, completed
- `TaskResponse` — id, all fields + timestamps

---

## Validation & Error responses

Use `javax.validation` annotations (e.g., `@NotBlank`, `@Size`, `@Future`).

Standard error response format (example):
```json
{
  "timestamp": "2025-10-20T18:22:10Z",
  "status": 400,
  "error": "Bad Request",
  "message": "title must not be blank",
  "path": "/api/tasks"
}
```

Implement a global `@ControllerAdvice` to map exceptions (ValidationException, ConstraintViolationException, EntityNotFoundException, etc.) to consistent HTTP responses.

---

## Testing

- Unit tests: JUnit 5 + Mockito (services, controllers)
- Integration tests: `@SpringBootTest` + Testcontainers (MongoDB) or embedded Mongo
- Run tests:
```bash
mvn test
```
Tip: use `mvn -DskipTests=false test` in CI to ensure tests run.

---

## Dockerizing the application

Example `Dockerfile`:

```dockerfile
FROM eclipse-temurin:17-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build & run (example):
```bash
mvn clean package
docker build -t taskmanager-api .
docker run -e SPRING_DATA_MONGODB_URI='mongodb://mongo:27017/taskmanagerdb' -p 8080:8080 taskmanager-api
```

Compose (app + Mongo):
```yaml
version: "3.8"
services:
  mongo:
    image: mongo:6
    ports: ["27017:27017"]
  app:
    image: taskmanager-api:latest
    build: .
    depends_on:
      - mongo
    environment:
      SPRING_DATA_MONGODB_URI: mongodb://mongo:27017/taskmanagerdb
    ports:
      - "8080:8080"
```

---

## CI / CD suggestions
A simple GitHub Actions workflow:
- `on: [push, pull_request]`
- Steps:
  - Checkout
  - Set up JDK 17
  - Cache Maven
  - mvn -B -DskipTests=false test
  - mvn -B package (optional publish artifact)
  - (optional) Build & push Docker image to registry

Protect `main` with required PR reviews and passing checks.

---

## Postman & OpenAPI
- Provide a Postman collection (`postman_collection.json`) for manual testing — store it in the repo.
- Better: expose OpenAPI/Swagger via `springdoc-openapi` to generate an interactive API doc at `/swagger-ui.html` or `/swagger-ui/index.html`.
- Use environment variables in Postman: `baseUrl = http://localhost:8080`.

---

## Troubleshooting

- App fails to connect to MongoDB:
  - Verify `spring.data.mongodb.uri`
  - Confirm Mongo is running on the configured host/port
  - Inspect logs for connection exceptions

- Port 8080 in use:
  - Use `-Dserver.port=9090` or set `server.port` in `application.properties`

- Unexpected validation errors:
  - Check DTO validation annotations
  - Check global exception handler mapping

---

## Contributing
1. Fork the repo
2. Create a branch:
```bash
git checkout -b feat/your-feature
```
3. Implement changes and tests
4. Run `mvn test` locally
5. Open a Pull Request with an explanation and test coverage

Guidelines:
- Keep controllers thin; put business logic in services
- Add unit/integration tests for behavior
- Follow project's code style (use an automatic formatter)

---

## License & Contact
- License: Add a LICENSE file (MIT recommended if you want permissive license)
- Author / Maintainer: MokshagnaBhuvan
- Repo: https://github.com/MokshagnaBhuvan/TaskManagerAPI_Task1

---

## Appendix — Checklist for maintainers
- [ ] Confirm Java version in `pom.xml`
- [ ] Add `postman_collection.json` or OpenAPI spec
- [ ] Provide example `.env.example` with `SPRING_DATA_MONGODB_URI`
- [ ] Add GitHub Actions workflow for CI
- [ ] Add Docker Compose for app + Mongo for easier local environment

---

If you'd like, I can:
- Inspect the repository and produce a README that matches package names, exact endpoints and sample responses taken from your code.
- Generate a Postman collection or an OpenAPI spec from your controllers automatically.
- Create a ready-to-commit `README.md` and open a PR with it.
Which would you prefer next?
```
