# TaskManagerAPI - Task 1

This repository contains Task 1 for the TaskManagerAPI project. The files below provide a clear description of the task, how to compile and run the application, how to call the API endpoints (with examples), and where to find sample input/output screenshots.

> NOTE: This README and task-level README are written in Markdown (.md) so they render properly on GitHub. If any command or file path below does not match your project layout, update the paths to reflect the actual repository structure.

## Repository structure

- README.md (this file)
- task1/README.md (detailed instructions for Task 1)
- screenshots/
  - input.png (placeholder screenshot of input)
  - output.png (placeholder screenshot of output)
- (other project source files)

## Quick summary

Task 1 implements a simple Task Manager API. The API exposes endpoints to create, read, update and delete tasks (typical CRUD operations). The exact implementation language and framework are part of the repository source code. The instructions below describe how to build and run the application for the most common setups and show example API usage with curl.

---

## How to build and run (guides for common stacks)

Choose the section that matches your project. If your project uses a different stack (for example Python Flask, Ruby, Go), adapt the commands accordingly.

### .NET 6 / .NET Core (C# Web API)

Prerequisites:
- .NET SDK 6.0 or later installed (dotnet command available)

Build & run:

```bash
# from repository root
cd <project-folder-if-any> # optional: change to folder that contains the .csproj
dotnet restore
dotnet build -c Release
# to run locally
dotnet run --project <YourProject>.csproj --urls "http://localhost:5000"
```

Then open http://localhost:5000/swagger (if Swagger is enabled) or call the API as shown in the examples below.

### Node.js / Express (JavaScript / TypeScript)

Prerequisites:
- Node 14+ and npm (or pnpm/yarn)

Build & run:

```bash
cd <project-folder-if-any>
npm install
# if TypeScript project: npm run build
npm start
# or in development
npm run dev
```

The API will run on the port configured by the project (often 3000 or 4000). Check package.json scripts.

### Java / Spring Boot

Prerequisites:
- JDK 11+
- Maven or Gradle

Build & run (Maven example):

```bash
cd <project-folder-if-any>
mvn clean package
java -jar target/<your-artifact>.jar
```

Access the API on the port the application binds to (default 8080 for Spring Boot) and use the examples below.

---

## How to use the API (curl examples)

Replace `http://localhost:PORT` with the actual base URL after you run the server.

Assumed endpoints (update them to match the real routes in your project):
- GET /api/tasks          -> list tasks
- GET /api/tasks/{id}     -> get a task
- POST /api/tasks         -> create a task
- PUT /api/tasks/{id}     -> update a task
- DELETE /api/tasks/{id}  -> delete a task

Examples:

List tasks:
```bash
curl -X GET "http://localhost:5000/api/tasks" -H "Accept: application/json"
```

Create a new task:
```bash
curl -X POST "http://localhost:5000/api/tasks" -H "Content-Type: application/json" 
  -d '{"title":"My task","description":"Example task","dueDate":"2025-12-31","done":false}'
```

Get a task by id:
```bash
curl -X GET "http://localhost:5000/api/tasks/1" -H "Accept: application/json"
```

Update a task:
```bash
curl -X PUT "http://localhost:5000/api/tasks/1" -H "Content-Type: application/json" 
  -d '{"title":"My task (updated)","description":"Updated","done":true}'
```

Delete a task:
```bash
curl -X DELETE "http://localhost:5000/api/tasks/1"
```

## Screenshots

Screenshots are stored in the `screenshots/` folder. Two placeholder PNG images were added: `input.png` and `output.png`. Replace these placeholders with real screenshots showing the UI or the HTTP request/response examples (for example, Postman or curl output) for the task.

Example Markdown insertion in a README (already included in the task README):

```md
![Input screenshot](screenshots/input.png)
![Output screenshot](screenshots/output.png)
```

---

## What I added

- A repository-level README.md with build/run instructions and API examples.
- A `task1/README.md` with detailed Task 1 instructions and sample usage (created in the next step of this commit).
- A `screenshots/` folder with two placeholder PNGs (input.png, output.png). Replace them with real images if you have better screenshots.

If you would like, I can now:
- Adjust the README examples to exactly match the routes and port used by this repository if you point me to the specific project entrypoint (for example the .csproj file, package.json, or pom.xml).
- Replace placeholder screenshots with your supplied screenshots.
