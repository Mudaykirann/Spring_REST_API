# Spring Boot User CRUD API

A clean, layered Spring Boot REST API demonstrating full CRUD operations on a `User` entity backed by MySQL.

---

## Project Structure

```
src/main/java/com/example/usercrud/
├── UserCrudApplication.java
├── controller/
│   └── UserController.java
├── service/
│   ├── UserService.java              ← interface
│   └── impl/
│       └── UserServiceImpl.java      ← implementation
├── repository/
│   └── UserRepository.java
├── entity/
│   └── User.java
├── dto/
│   ├── UserRequestDTO.java
│   └── UserResponseDTO.java
└── exception/
    ├── ResourceNotFoundException.java
    ├── DuplicateEmailException.java
    ├── ErrorResponse.java
    └── GlobalExceptionHandler.java
```

---

## Prerequisites

| Tool        | Version  |
|-------------|----------|
| Java        | 17+      |
| Maven       | 3.8+     |
| MySQL       | 8.0+     |

Create the database before running:

```sql
CREATE DATABASE userdb;
```

Update `src/main/resources/application.properties` with your MySQL credentials.

---

## Running the App

```bash
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080`

---

## API Endpoints

| Method | Endpoint           | Description       | Status Codes       |
|--------|--------------------|-------------------|--------------------|
| POST   | `/api/users`       | Create a user     | 201, 400, 409      |
| GET    | `/api/users`       | Get all users     | 200                |
| GET    | `/api/users/{id}`  | Get user by ID    | 200, 404           |
| PUT    | `/api/users/{id}`  | Update a user     | 200, 400, 404, 409 |
| DELETE | `/api/users/{id}`  | Delete a user     | 204, 404           |

---

## Sample Requests & Responses

### POST /api/users — Create User

**Request:**
```json
POST /api/users
Content-Type: application/json

{
  "name": "Alice Johnson",
  "email": "alice@example.com"
}
```

**Response `201 Created`:**
```json
{
  "id": 1,
  "name": "Alice Johnson",
  "email": "alice@example.com"
}
```

---

### GET /api/users — Get All Users

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "name": "Alice Johnson",
    "email": "alice@example.com"
  },
  {
    "id": 2,
    "name": "Bob Smith",
    "email": "bob@example.com"
  }
]
```

---

### GET /api/users/{id} — Get User by ID

**Response `200 OK`:**
```json
{
  "id": 1,
  "name": "Alice Johnson",
  "email": "alice@example.com"
}
```

**Response `404 Not Found`:**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "User not found with ID: 99",
  "timestamp": "2024-03-15 10:30:00",
  "details": null
}
```

---

### PUT /api/users/{id} — Update User

**Request:**
```json
PUT /api/users/1
Content-Type: application/json

{
  "name": "Alice M. Johnson",
  "email": "alice.m@example.com"
}
```

**Response `200 OK`:**
```json
{
  "id": 1,
  "name": "Alice M. Johnson",
  "email": "alice.m@example.com"
}
```

---

### DELETE /api/users/{id} — Delete User

**Response `204 No Content`** *(empty body)*

---

## Error Responses

### 400 Bad Request (Validation)
```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation error",
  "timestamp": "2024-03-15 10:30:00",
  "details": [
    "name: Name must not be blank",
    "email: Email must be a valid email address"
  ]
}
```

### 409 Conflict (Duplicate Email)
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Email already in use: alice@example.com",
  "timestamp": "2024-03-15 10:30:00",
  "details": null
}
```
