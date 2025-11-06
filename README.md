# S4.01 — Fruit API 🍎

## 📄 Description

This project is a **REST API built with Spring Boot** for managing fruit stock in a store.  
It allows full CRUD operations (Create, Read, Update, Delete) and stores data in an **H2 in-memory database**.

The application follows the **MVC architecture**, uses **DTOs**, **Bean Validation**, **Exception Handling**, and follows **TDD principles** with unit and integration tests.

---

## 💻 Technologies Used

- **Java 21**
- **Spring Boot 3.x**
    - Spring Web
    - Spring Data JPA
    - Validation API
- **H2 Database**
- **Maven**
- **JUnit 5 / Mockito**
- **Docker (multi-stage build)**

---

## 📋 Requirements

| Tool | Version |
|------|----------|
| Java | 21 |
| Maven | 3.9+ |
| Docker | 24+ (optional) |

---

## 🛠️ Installation & Setup

### 1️⃣ Clone the repository
```bash
git clone https://github.com/yourusername/fruit-api-h2.git
cd fruit-api-h2
```

### 2️⃣ Build the project
```bash
mvn clean package
```

### 3️⃣ Run the application
```bash
java -jar target/fruit-api-h2-0.0.1-SNAPSHOT.jar
```

---

## 🌐 Available Endpoints

| Method | Endpoint | Description | Response |
|--------|-----------|--------------|-----------|
| **POST** | `/fruits` | Create a new fruit | `201 Created` |
| **GET** | `/fruits` | Retrieve all fruits | `200 OK` |
| **GET** | `/fruits/{id}` | Retrieve a fruit by ID | `200 OK` or `404 Not Found` |
| **PUT** | `/fruits/{id}` | Update an existing fruit | `200 OK`, `400 Bad Request`, or `404 Not Found` |
| **DELETE** | `/fruits/{id}` | Delete a fruit by ID | `204 No Content` |

---

## 🧪 Testing

### 🧩 Unit Tests
- **Mockito** used to isolate `FruitServiceImpl`
- Tests cover:
  - Creation with valid and invalid data
  - Update and delete operations
  - Validation logic using custom exceptions

### 🔍 Integration Tests
- **MockMvc** used to test full request/response cycle.
- Verifies HTTP status codes and JSON bodies.
- Database is reset after each test with `@DirtiesContext`.

### ✅ Test Coverage Goals
- All service and controller methods covered.
- All exceptions and branches tested (happy and error paths).
- TDD approach followed: *RED → GREEN → REFACTOR.*

---

## 🧾 Global Exception Handling

All exceptions are managed by a centralized `GlobalExceptionHandler`.

Example of JSON error response:

```json
{
  "status": 400,
  "error": "ValidationError",
  "message": "Fruit name cannot be blank"
}
```
Handled exceptions include:

- `InvalidFruitNameException`
- `InvalidFruitWeightException`
- `FruitNotFoundException`
- `MethodArgumentNotValidException` (for @Valid DTO validation)

---

## 🧩 DTO and Validation

The `FruitDTO` is used in all controller methods to prevent exposing JPA entities directly.

```java
public class FruitDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Positive(message = "Weight must be positive")
    private int weight;
}
```

This ensures data integrity and provides consistent validation across the API.

---

## 🐳 Docker Build Explanation

This project includes a **multi-stage Dockerfile** optimized for production:

```dockerfile
# ===============================
# 🏗️ Stage 1 — Build
# ===============================
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# ===============================
# 🚀 Stage 2 — Run
# ===============================
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 📘 Explanation

| Stage | Purpose | Image |
|--------|----------|--------|
| **Build** | Compiles the project and generates `.jar` | `eclipse-temurin:21-jdk` |
| **Run** | Runs only the `.jar` in a light environment | `eclipse-temurin:21-jre` |

Each stage is optimized for its purpose, reducing the final image size and improving performance in production environments.

---

## 💡 Commands

Build the image:
```bash
docker build -t fruit-api-h2 .
```

Run the container:
```bash
docker run -p 8080:8080 fruit-api-h2
```

Access the API at:
👉 http://localhost:8080/fruits