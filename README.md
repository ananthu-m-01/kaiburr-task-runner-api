#  Kaiburr Task Runner API – Spring Boot REST API

This project is developed as part of the Kaibur company technical assignment.  
It is a Spring Boot–based RESTful API designed to manage **tasks** — supporting operations like creating, retrieving, updating, deleting, and executing tasks.

---

##  Project Overview

The **Kaibur Task Runner API** allows users to create and manage tasks stored in a **MongoDB database**.  
Each task contains details like name, description, and command.  
The API also provides an endpoint to **execute a given task command** and returns the output dynamically.

---

## Features

- Create a new task
- View all existing tasks
- Retrieve a task by ID or name
- Update task details
- Delete a task
- Execute a task (run the command and return the output)

---

## Architecture

The application follows a **layered architecture** pattern for clean separation of concerns:


### Layers:
- **Controller:** Handles HTTP requests and responses.
- **Service:** Contains business logic for task management.
- **Repository:** Interfaces with MongoDB using Spring Data.
- **Model:** Defines the structure of the Task entity.

---

##  Tech Stack

| Layer | Technology Used |
|-------|------------------|
| Backend Framework | Spring Boot 3.x |
| Database | MongoDB |
| Build Tool | Maven |
| Language | Java 17+ |
| Testing | JUnit 5 |
| IDE | IntelliJ IDEA / Eclipse |
| API Testing | Postman / cURL |

> All Task objects, including their TaskExecution history, are stored in a MongoDB database.

---

## Project Structure

```
kaiburr-task-runner-api/
│
├── src/
│ ├── main/
│ │ ├── java/com/ananthu/kaiburr_task_runner_api/
│ │ │
│ │ │ ├── controller/
│ │ │ │ └── TaskController.java
│ │ │
│ │ │ ├── dto/
│ │ │ │ ├── task/
│ │ │ │ │ ├── CreateTaskDTO.java
│ │ │ │ │ ├── TaskResponseDTO.java
│ │ │ │ │ └── UpdateTaskDTO.java
│ │ │ │ └── task_execution/
│ │ │ │ └── TaskExecutionDTO.java
│ │ │
│ │ │ ├── exceptions/
│ │ │ │ ├── task/
│ │ │ │ │ ├── InvalidCommandException.java
│ │ │ │ │ ├── TaskInvalidCredentialException.java
│ │ │ │ │ └── TaskNotFoundException.java
│ │ │ │ ├── task_execution/
│ │ │ │ │ └── TaskExecutionNotFoundException.java
│ │ │ │ └── GlobalExceptionHandler.java
│ │ │
│ │ │ ├── model/
│ │ │ │ ├── Task.java
│ │ │ │ ├── TaskExecution.java
│ │ │ │ └── TaskStatus.java
│ │ │
│ │ │ ├── repository/
│ │ │ │ └── TaskRepository.java
│ │ │
│ │ │ ├── service/
│ │ │ │ ├── ITaskService.java
│ │ │ │ └── TaskService.java
│ │ │
│ │ │ ├── util/
│ │ │ │ └── Validation.java
│ │ │
│ │ │ └── KaiburrTaskRunnerApiApplication.java
│ │
│ ├── resources/
│ │ ├── application.properties
│ │ └── static/
│ │
│ └── test/java/com/ananthu/kaiburr_task_runner_api/
│ └── KaiburrTaskRunnerApiApplicationTests.java
│
├── pom.xml
├── .env
└── README.md
```

---

##  API Endpoints

| Method     | Endpoint                     | Description |
|------------|------------------------------|-------------|
| **POST**   | `/api/tasks`                 | Create a new task |
| **GET**    | `/api/tasks`                 | Get all tasks |
| **GET**    | `/api/tasks/{id}`            | Get task by ID |
| **GET**    | `/api/tasks/name/{taskName}` | Get task by name |
| **PUT**    | `/api/tasks/{id}`            | Update a task |
| **PUT**    | `/api/tasks/{id}/run`        | Execute task command |
| **DELETE** | `/api/tasks/{id}`            | Delete a task |

---

## Sample API Usage

### 1️. Create a Task
**POST:** `http://localhost:8080/api/tasks`

**Request Body:**
```json
  {
    "name": "Show Date",
    "owner": "Ananthu M",
    "command": "echo %DATE%"
  }
```
**Response**
```json
    {
      "id": "68f4cde42985ab90021e4270",
      "name": "Show Date",
      "owner": "Ananthu M",
      "command": "echo %DATE%",
      "taskExecutions": []
    }
```

### 2. Find all tasks
**GET:** `http://localhost:8080/api/tasks`
**Response**    
```json
  [
  {
    "id": "68f4cde42985ab90021e4270",
    "name": "Show Date",
    "owner": "Ananthu M",
    "command": "echo %DATE%",
    "taskExecutions": [
      {
        "startTime": "2025-10-19T11:39:22.916Z",
        "endTime": "2025-10-19T11:39:22.991Z",
        "output": "19-10-2025",
        "status": "SUCCESS"
      },
      {
        "startTime": "2025-10-19T11:40:07.388Z",
        "endTime": "2025-10-19T11:40:07.452Z",
        "output": "19-10-2025",
        "status": "SUCCESS"
      }
    ]
  },
  {
    "id": "68f4c9eddf31467a7c342795",
    "name": "Print Hello",
    "owner": "Ananthu M",
    "command": "echo Hello World",
    "taskExecutions": [
      {
        "startTime": "2025-10-19T11:34:58.902Z",
        "endTime": "2025-10-19T11:34:58.962Z",
        "output": "Hello World",
        "status": "SUCCESS"
      }
    ]
  }
]


```

### 3. Find task by ID
**GET:** `http://localhost:8080/api/tasks/68f4cde42985ab90021e4270`
**Response**
```json
    {
      "id": "68f4c66132a6fc8ad3e564cb",
      "name": "Hello world",
      "owner": "Ananthu",
      "command": "hello world",
      "taskExecutions": []
    }
```

### 4. Find task by Name
**GET:** `http://localhost:8080/api/tasks/date`
**Response**
```json
    {
      "id": "68f4cde42985ab90021e4270",
      "name": "Show Date",
      "owner": "Ananthu M",
      "command": "echo %DATE%",
      "taskExecutions": []
    }
```

### 5. Update a Task
**PUT:** `http://localhost:8080/api/tasks/68f4cde42985ab90021e4270`
**Request Body**
```json
    {
        "name": "Show Date",
        "owner": "Ananthu M [SpringBoot Developer]",
        "command": "echo %DATE%"
    }
```

**Response**
```json
{
  "id": "68f4cde42985ab90021e4270",
  "name": "Show Date",
  "owner": "Ananthu M [SpringBoot Developer]",
  "command": "echo %DATE%",
  "taskExecutions": []
}
```

### 6. Execute the command
**PUT:** `http://localhost:8080/api/tasks/68f4cde42985ab90021e4270/run`
**Request Body**
```json
  {
      "startTime": "2025-10-19T12:06:45.954285300Z",
      "endTime": "2025-10-19T12:06:46.019356900Z",
      "output": "19-10-2025",
      "status": "SUCCESS"
  }
```

### 7. Delete a Task
**DELETE:** `http://localhost:8080/api/tasks/68f4c66132a6fc8ad3e564cb`
**Response**
```
task with id 68f4c66132a6fc8ad3e564cb deleted successfully
```

### 8. Validation / Unsafe Command Check

**POST:** `http://localhost:8080/api/tasks`

**Response**
```json
    {
      "name": "Shut Down",
      "owner": "Ananthu M",
      "command": "shutdown"
    }
```
> The command field is validated. Unsafe or invalid commands are rejected.

**Response**
```json
    {
      "error": "invalid command",
      "message": "Unsafe or potentially malicious command detected!",
      "timestamp": "2025-10-19T18:13:11.0592954",
      "status": 400
    }
```
>Commands are validated during task creation and update. Invalid or unsafe commands are rejected with a 400 BAD REQUEST response.

## Setup Instructions

### Prerequisites
Before running the project, ensure the following are installed on your system:

- **Java 17** or above
- **Maven**
- **MongoDB** (either locally or using MongoDB Atlas)

### Clone the Repository
1. Clone this repository to your local machine:

    ```bash
    git clone https://github.com/ananthu-m-01/kaiburr-task-runner-api.git
    ```
2. Navigate to the project directory:

    ```bash
    cd kaiburr-task-runner-api
    ```
3. Configure MongoDB
   - Create a .env file in the root directory of the project:
    ```bash
      touch .env
    ```
   - Add your MongoDB connection URI in the .env file.
     Replace <username> , <databasename> and <password> with your actual credentials:
    ```bash
      MONGODB_URI=mongodb+srv://<username>:<password>@cluster0.g9ecy.mongodb.net/<database>?retryWrites=true&w=majority&authSource=admin
    ```
4. Build the Application
    ```bash
      mvn clean install
    ```
5. Run the Application
    ```bash
      mvn spring-boot:run
    ```
   Or run from your IDE using KaiburrTaskRunnerApiApplication.java

6. Access API
    ```bash
      http://localhost:8080/api/tasks
    ```

## Key Dependencies

| Dependency | Version | Scope | Purpose |
|------------|---------|-------|---------|
| `spring-boot-starter-web` | 3.5.6 | - | Provides all necessary components to create REST APIs and web applications using Spring Boot. |
| `spring-boot-starter-data-mongodb` | 3.5.6 | - | Integrates MongoDB with Spring Boot and provides repositories and template classes. |
| `dotenv-java` | 3.2.0 | - | Allows loading environment variables from a `.env` file. |
| `lombok` | 1.18.30 | provided | Reduces boilerplate code by generating getters, setters, constructors, etc. |
| `spring-boot-starter-validation` | 3.5.6 | - | Provides support for Java Bean validation using annotations. |

> **Note:** Versions for Spring Boot dependencies inherit from the parent `spring-boot-starter-parent` unless explicitly specified.


## Exception Handling

The project uses a global exception handler to manage and standardize API error responses. Below are the key exceptions handled:

| Exception Class | HTTP Status | Error Description | Purpose |
|-----------------|------------|-----------------|---------|
| `TaskNotFoundException` | 404 NOT FOUND | "task not found" | Thrown when a requested task ID does not exist in the database. |
| `InvalidCommandException` | 400 BAD REQUEST | "invalid command" | Thrown when a command provided for a task is unsafe or invalid. |
| `TaskExecutionException` | 500 INTERNAL SERVER ERROR | "task execution error" | Thrown when an error occurs during the execution of a task. |
| `MethodArgumentNotValidException` | 400 BAD REQUEST | Field-specific validation errors | Handles validation failures for request payloads, returning field-specific error messages. |
| `Exception` (Generic) | 500 INTERNAL SERVER ERROR | "Internal Server Error" | Catches all other unhandled exceptions to prevent exposing internal errors. |

> Each exception returns a JSON response with the following structure:
> - `timestamp`: The date and time of the error
> - `status`: HTTP status code
> - `error`: Brief description of the error
> - `message`: Detailed error message
> - (For validation errors) Field-specific error messages

## Screenshots

### Project Setup – IntelliJ IDEA Community Edition

The frontend application was developed using **IntelliJ IDEA Community Edition**, which provides robust TypeScript and React support with integrated terminal and version control.

You can download IntelliJ IDEA Community Edition here:  
🔗 [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)

![Project setup](https://github.com/ananthu-m-01/kaiburr-task-runner-api/blob/main/screenshots/project-setup.png?raw=true)

### 1. Create a Task
![Create Task Screenshot](https://github.com/ananthu-m-01/kaiburr-task-runner-api/blob/main/screenshots/create-task.png?raw=true)

### 2. Get All Tasks
![Get All Tasks Screenshot](https://github.com/ananthu-m-01/kaiburr-task-runner-api/blob/main/screenshots/get-all-tasks.png?raw=true)

### 3. Get Task by ID
![Get Task by ID Screenshot](https://github.com/ananthu-m-01/kaiburr-task-runner-api/blob/main/screenshots/get-task.png?raw=true)

### 4. Update a Task
![Update Task Screenshot](https://github.com/ananthu-m-01/kaiburr-task-runner-api/blob/main/screenshots/update.png?raw=true)

### 5. Execute a Task Command
![Run Task Screenshot](https://github.com/ananthu-m-01/kaiburr-task-runner-api/blob/main/screenshots/run-task.png?raw=true)

### 6. Delete a Task
![Delete Task Screenshot](https://github.com/ananthu-m-01/kaiburr-task-runner-api/blob/main/screenshots/delete-task.png?raw=true)

### 7. Validation Error
![Validation Error Screenshot](https://github.com/ananthu-m-01/kaiburr-task-runner-api/blob/main/screenshots/validation-error.png?raw=true)


  
### Front End

The front-end for this project can be found at:  
[Kaiburr Task Runner UI](https://github.com/ananthu-m-01/kaiburr-task-runner-ui)

### Author

**Name:** Ananthu M  
**Email:** [ananthu.m.utr@gmail.com](mailto:ananthu.m.utr@gmail.com)  
**GitHub:** [https://github.com/ananthu-m-01](https://github.com/ananthu-m-01)

> No code is copied from external sources. AI tools were used only for guidance; I fully understand and implements the functionality.

### License

This project is developed solely for educational and assignment purposes for the Kaiburr company task.