# Task Manager API (Task1)

A lightweight, production-ready RESTful Task Management API built with Java, Spring Boot, Maven and MongoDB. This service provides endpoints to create, read, update and delete tasks, and is designed for easy local development with Postman for manual testing.

Quick summary
- Language: Java
- Build: Maven
- Framework: Spring Boot
- Database: MongoDB
- Manual testing / API exploration: Postman
- Repo: MokshagnaBhuvan/TaskManagerAPI_Task1

Table of contents
- Features
- Tech stack
- Prerequisites
- Installation & local run
- Configuration
- Running with Docker (MongoDB)
- API usage (endpoints, examples)
- Postman collection
- Data model
- Error handling & validation
- Tests
- Contributing
- Troubleshooting
- License & contact

Features
- CRUD for tasks (Create / Read / Update / Delete)
- Validation on input payloads
- Timestamping (createdAt / updatedAt)
- Pagination & filtering (basic) — extendable
- Clean layered architecture: controller -> service -> repository
- MongoDB persistence with Spring Data MongoDB

Tech stack
- Java (LTS recommended: Java 17)
- Maven (build & dependency management)
- Spring Boot (web, validation, data-mongodb)
- Spring Data MongoDB (data access)
- MongoDB (document database)
- Postman (API exploration & collection import)

Prerequisites
- Java 17 (or Java 11 if project configured for 11) installed and JAVA_HOME set
- Maven (3.6+)
- MongoDB running locally or accessible remotely
- Postman (optional, for manual testing)

Installation & local run (fast)
1. Clone the repository
   git clone https://github.com/MokshagnaBhuvan/TaskManagerAPI_Task1.git
   cd TaskManagerAPI_Task1

2. Configure MongoDB connection (see Configuration below)

3. Build the project
   mvn clean package

4. Run the application
   mvn spring-boot:run
   OR
   java -jar target/<artifact-name>.jar

By default the app runs on port 8080 (configurable).

Configuration
Spring Boot configuration is typically in src/main/resources/application.properties or application.yml. Example application.properties to connect to local MongoDB:

spring.data.mongodb.uri=mongodb://localhost:27017/taskmanagerdb
spring.profiles.active=local
server.port=8080
# (Optional) logging.level.root=INFO

If your repository uses environment variables, set them before launching:
- MONGODB_URI (example): mongodb://username:password@host:27017/taskmanagerdb
- SERVER_PORT (optional): 8080

Running MongoDB locally with Docker (recommended for quick setup)
Create a quick docker-compose.yml to run MongoDB:

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

Then:
docker compose up -d
And point spring.data.mongodb.uri to mongodb://localhost:27017/taskmanagerdb

API Usage
Below are the common endpoints and examples. Adjust paths if your project uses different base paths or versions (e.g., /api/v1).

Base URL (default):
http://localhost:8080

Common endpoints
- Create a task
  POST /api/tasks
  Request JSON:
  {
    "title": "Finish README",
    "description": "Write the README for TaskManagerAPI",
    "dueDate": "2025-11-01T12:00:00Z",
    "priority": "MEDIUM",
    "completed": false
  }

  Success response (201 Created):
  {
    "id": "653a1e3b...",
    "title": "Finish README",
    "description": "...",
    "dueDate": "2025-11-01T12:00:00Z",
    "priority": "MEDIUM",
    "completed": false,
    "createdAt": "2025-10-20T18:00:00Z",
    "updatedAt": "2025-10-20T18:00:00Z"
  }

- Get a list of tasks (with optional pagination/filter)
  GET /api/tasks?page=0&size=20&completed=false

  Success response (200 OK):
  [
    { "id": "...", "title": "...", ... },
    ...
  ]

- Get a single task by id
  GET /api/tasks/{id}

- Update a task (partial or full)
  PUT /api/tasks/{id}
  PATCH /api/tasks/{id}

- Delete a task
  DELETE /api/tasks/{id}

Example curl (create)
curl -X POST "http://localhost:8080/api/tasks" \
  -H "Content-Type: application/json" \
  -d '{"title":"Finish README","description":"Write README","priority":"HIGH"}'

Authentication & Authorization
- If this repository includes authentication (JWT/basic), follow the README in the project or check application.properties for security configuration.
- If there is no auth configured, the API is public on the exposed endpoints — protect it before deploying to production.

Postman collection
- Import the provided Postman collection (if included in repo as `postman_collection.json`) into Postman.
- If a collection is not present, create a new collection with the endpoints above.
- Use environment variables for baseUrl (http://localhost:8080) and token (if auth exists).

Data model (example Task document)
A typical Task document persisted in MongoDB:

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

Repository & package layout (recommended / common)
- src/main/java
  - com.example.taskmanager
    - controller  (REST controllers)
    - service     (business logic)
    - repository  (Spring Data MongoDB interfaces)
    - model       (domain / DTOs)
    - dto         (request/response models)
    - config      (Mongo config, Swagger, etc.)
- src/main/resources
  - application.properties / application.yml

Validation & error handling
- Controllers should validate incoming payloads using javax.validation annotations (@Valid, @NotNull, @Size, etc.)
- Errors should return consistent error response shape:
  {
    "timestamp": "2025-10-20T18:22:10Z",
    "status": 400,
    "error": "Bad Request",
    "message": "title must not be blank",
    "path": "/api/tasks"
  }

Tests
- Unit tests: Use JUnit 5 + Mockito for service and controller unit tests
- Integration tests: Use @SpringBootTest and an embedded MongoDB (or Testcontainers) to test repository and controllers with real data
- Run tests:
  mvn test

Dockerizing the application (optional)
- Build the jar:
  mvn clean package
- Example Dockerfile:
  FROM eclipse-temurin:17-jre
  ARG JAR_FILE=target/*.jar
  COPY ${JAR_FILE} app.jar
  ENTRYPOINT ["java","-jar","/app.jar"]

- Build & run:
  docker build -t taskmanager-api .
  docker run -e SPRING_DATA_MONGODB_URI='mongodb://host:27017/taskmanagerdb' -p 8080:8080 taskmanager-api

CI / CD suggestions
- Use GitHub Actions to:
  - Run mvn -B -DskipTests=false test
  - Build artifact
  - Optionally build Docker image and push to registry
- Protect main branch and require PR reviews

Contributing
- Fork the repo, create a feature branch: git checkout -b feat/your-feature
- Run tests and linters locally
- Open a PR with a clear description and test coverage for the change
- Follow Java code style (formatting & linting) used in project (e.g., Google Java Style or project-specific)

Troubleshooting
- If the app fails to connect to MongoDB:
  - Ensure MongoDB is running and accessible
  - Check spring.data.mongodb.uri in application properties or env vars
  - For authentication-enabled MongoDB clusters, include credentials in the URI
- If port 8080 is already in use, change server.port in application.properties or run with -Dserver.port=9090

Security notes before production
- Add authentication (JWT or OAuth2)
- Add input sanitization and rate limiting
- Secure MongoDB (authentication, network access rules)
- Use HTTPS via reverse proxy / load balancer

License
Specify your license (for example MIT). If none present, add one to the repo.

Contact / Author
- Author: MokshagnaBhuvan
- Repo: https://github.com/MokshagnaBhuvan/TaskManagerAPI_Task1

Appendix — Quick checklist for maintainers
- [ ] Confirm Java version in pom.xml (update README accordingly)
- [ ] Add Postman collection or OpenAPI/Swagger spec to repo
- [ ] Add sample .env.example or instructions for secrets/URIs
- [ ] Add Dockerfile & docker-compose for app + MongoDB (if desired)
- [ ] Add CI config (GitHub Actions) for tests & build

If you want, I can:
- generate a ready-to-commit README.md that exactly matches the project's code (I can inspect the repo for accurate package names, port, endpoints and example responses) — tell me and I'll fetch files and produce a final version matched to the code.
- create Postman collection or OpenAPI spec based on actual controllers in the repository.
