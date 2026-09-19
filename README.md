# 🛒 E-Commerce Microservices Platform

> A production-ready **E-Commerce Microservices Platform** built with **Spring Boot**, **Spring Cloud Gateway**, **JWT authentication**, and **MySQL**. Demonstrates industry-standard microservice architecture with complete authentication, authorization, and product management capabilities.

## 📌 Architecture Overview

This project implements a **distributed microservices architecture** with 3 independent services:

```
┌─────────────────────────────────────────────────────┐
│         Client (Postman / Mobile App)               │
└───────────────┬─────────────────────────────────────┘
                │
                ▼
┌─────────────────────────────────────────────────────┐
│         API Gateway (Port 8080)                      │
│    • Request Routing                                │
│    • JWT Validation                                 │
│    • Central Security Layer                         │
└────────────┬───────────────────────┬────────────────┘
             │                       │
             ▼                       ▼
┌──────────────────────┐  ┌──────────────────────┐
│  Auth Service        │  │ Product Service      │
│  (Port 8081)         │  │ (Port 8082)          │
│ • User Registration  │  │ • CRUD Operations    │
│ • User Login         │  │ • Pagination         │
│ • JWT Generation     │  │ • Role-Based Access  │
│ • Role Management    │  │                      │
└──────────────────────┘  └──────────────────────┘
         │                         │
         ▼                         ▼
    ┌─────────────┐            ┌─────────────┐
    │  MySQL      │            │  MySQL      │
    │  auth_db    │            │  product_db │
    └─────────────┘            └─────────────┘
```

Each service runs independently and communicates via **HTTP REST APIs**, following the **microservices** and **single responsibility principle**.

## 🧩 Microservices

### 🔐 Auth Service (Port 8081)
Manages user authentication and JWT token generation.

**Features:**
- ✅ User registration with password hashing
- ✅ User login with JWT token generation
- ✅ Role-based support (`USER`, `ADMIN`)
- ✅ Secure password storage in MySQL
- ✅ Error handling for duplicate users and invalid credentials

**Endpoints:**
```http
POST   /auth/register      - Create new user account
POST   /auth/login         - Authenticate and receive JWT token
```

---

### 📦 Product Service (Port 8082)
Manages product catalog with role-based access control.

**Features:**
- ✅ Create products (ADMIN only)
- ✅ View all products with pagination (USER & ADMIN)
- ✅ Update products (ADMIN only)
- ✅ Delete products (ADMIN only)
- ✅ Role-based authorization via JWT

**Endpoints:**
```http
GET    /products           - List all products (paginated)
POST   /products           - Create product (ADMIN only)
PUT    /products/{id}      - Update product (ADMIN only)
DELETE /products/{id}      - Delete product (ADMIN only)
```

---

### 🌐 API Gateway (Port 8080)
Central routing and security layer for all microservices.

**Features:**
- ✅ Request routing to Auth and Product services
- ✅ JWT validation on protected routes
- ✅ Token extraction from `Authorization: Bearer` header
- ✅ Public routes for authentication (`/auth/**`)
- ✅ Protected routes for product management (`/products/**`)
- ✅ Centralized security configuration

**Routes:**
```
/auth/**       → Auth Service (8081)     [PUBLIC]
/products/**   → Product Service (8082)  [PROTECTED]
```

---

## 🔐 Security Architecture

### Authentication Flow
```
1. User registers via /auth/register
2. User logs in via /auth/login
3. Auth Service returns JWT token
4. Client includes token in Authorization header: Bearer {token}
5. API Gateway validates JWT
6. Token passed to Product Service for authorization
7. Role-based access enforced based on token claims
```

### JWT Token Structure
```json
{
  "username": "john_doe",
  "role": "USER",
  "iat": 1694000000,
  "exp": 1694086400
}
```

### Security Features
- 🔒 **JWT Authentication** - Stateless, token-based security
- 🎯 **Role-Based Access Control (RBAC)** - USER and ADMIN roles
- 🛡️ **Spring Security** - Industry-standard security framework
- 🔐 **Password Hashing** - BCrypt password encoding
- 📍 **Centralized Validation** - Gateway-level JWT validation
- 🚫 **No Sessions** - Stateless architecture for scalability

## 🛠 Tech Stack

| Component | Version | Purpose |
|-----------|---------|---------|
| **Java** | 17 | Runtime language |
| **Spring Boot** | 3.5.9 | Framework |
| **Spring Cloud** | 2025.0.1 | Microservices support |
| **Spring Cloud Gateway** | Latest | API routing & WebFlux |
| **Spring Security** | Latest | Authentication & Authorization |
| **Spring Web** | Latest | REST API development |
| **Spring Data JPA** | Latest | Database ORM |
| **JWT (JJWT)** | 0.11.5 | Token generation & validation |
| **MySQL** | 8.0+ | Relational database |
| **Maven** | 3.9+ | Build automation |

---

## � Project Structure

```
ecommerce-microservice/
│
├── api-gateway/
│   ├── src/main/java/com/api_gateway/
│   │   └── api_gateway/
│   │       ├── ApiGatewayApplication.java
│   │       └── configs/
│   │           ├── JwtConfig.java              # JWT configuration
│   │           ├── JwtHeaderFilter.java        # JWT validation filter
│   │           └── SecurityConfig.java         # Security setup
│   ├── pom.xml
│   └── mvnw.cmd
│
├── auth-service/
│   ├── src/main/java/com/auth/auth_service/
│   │   └── auth_service/
│   │       ├── AuthServiceApplication.java
│   │       ├── config/
│   │       │   └── SecurityConfig.java
│   │       ├── controller/
│   │       │   └── AuthController.java        # Auth endpoints
│   │       ├── dto/
│   │       │   ├── LoginRequest.java
│   │       │   ├── RegisterRequest.java
│   │       │   └── AuthResponse.java
│   │       ├── entity/
│   │       │   ├── UserEntity.java
│   │       │   └── Role.java
│   │       ├── exception/
│   │       │   ├── GlobalExceptionHandler.java
│   │       │   ├── InvalidCredentialsException.java
│   │       │   ├── UserAlreadyExistsException.java
│   │       │   └── errorResponseDto/
│   │       │       └── ApiErrorResponse.java
│   │       ├── repository/
│   │       │   └── UserRepository.java
│   │       └── service/
│   │           └── AuthService.java            # Business logic
│   ├── pom.xml
│   └── mvnw.cmd
│
├── product-service/
│   ├── src/main/java/com/product/product_service/
│   │   └── product_service/
│   │       ├── ProductServiceApplication.java
│   │       ├── config/
│   │       ├── controller/
│   │       │   └── ProductController.java     # Product endpoints
│   │       ├── dto/
│   │       ├── entity/
│   │       │   └── ProductEntity.java
│   │       ├── exception/
│   │       │   └── GlobalExceptionHandler.java
│   │       ├── repository/
│   │       │   └── ProductRepository.java
│   │       └── service/
│   │           └── ProductService.java        # Business logic
│   ├── pom.xml
│   └── mvnw.cmd
│
├── README.md                                   # This file
└── .gitignore
```

---

## 🚀 Getting Started

### Prerequisites
- **Java 17+** installed
- **Maven 3.9+** for build automation
- **MySQL 8.0+** running locally
- **Postman** or **cURL** for API testing (optional)

### Installation & Setup

#### 1️⃣ Clone the Repository
```bash
git clone https://github.com/yourusername/ecommerce-microservices.git
cd ecommerce-microservices
```

#### 2️⃣ Create MySQL Databases
```sql
CREATE DATABASE auth_db;
CREATE DATABASE product_db;
```

#### 3️⃣ Configure Each Service
Update `application.properties` in each service:

**Auth Service** (`auth-service/src/main/resources/application.properties`):
```properties
server.port=8081
spring.datasource.url=jdbc:mysql://localhost:3306/auth_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

jwt.secret=my-super-secret-key-change-this-in-production
jwt.expiration=86400000
```

**Product Service** (`product-service/src/main/resources/application.properties`):
```properties
server.port=8082
spring.datasource.url=jdbc:mysql://localhost:3306/product_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

**API Gateway** (`api-gateway/src/main/resources/application.properties`):
```properties
server.port=8080
spring.cloud.gateway.routes[0].id=auth-service
spring.cloud.gateway.routes[0].uri=http://localhost:8081
spring.cloud.gateway.routes[0].predicates[0]=Path=/auth/**

spring.cloud.gateway.routes[1].id=product-service
spring.cloud.gateway.routes[1].uri=http://localhost:8082
spring.cloud.gateway.routes[1].predicates[0]=Path=/products/**

jwt.secret=my-super-secret-key-change-this-in-production
```

#### 4️⃣ Start the Services
Open three terminal windows and run each service:

**Terminal 1 - Auth Service:**
```bash
cd auth-service
mvn spring-boot:run
```

**Terminal 2 - Product Service:**
```bash
cd product-service
mvn spring-boot:run
```

**Terminal 3 - API Gateway:**
```bash
cd api-gateway
mvn spring-boot:run
```

✅ All services should be running on their respective ports.

---

## � API Documentation

### Testing with Postman

#### 1️⃣ Register a New User
```http
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123",
  "role": "USER"
}
```

**Response:**
```json
{
  "message": "User registered successfully",
  "userId": 1
}
```

#### 2️⃣ Login to Get JWT Token
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

**Response:**
```json
{
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_doe",
  "role": "USER"
}
```

📌 **Copy the token for next steps.**

#### 3️⃣ Create a Product (ADMIN Only)
```http
POST http://localhost:8080/products
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "quantity": 50
}
```

#### 4️⃣ Get All Products (Paginated)
```http
GET http://localhost:8080/products?page=0&size=10
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Laptop",
      "description": "High-performance laptop",
      "price": 999.99,
      "quantity": 50,
      "createdAt": "2024-01-15T10:30:00"
    }
  ],
  "totalPages": 1,
  "totalElements": 1,
  "currentPage": 0
}
```

#### 5️⃣ Update a Product (ADMIN Only)
```http
PUT http://localhost:8080/products/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "name": "Gaming Laptop",
  "price": 1299.99,
  "quantity": 30
}
```

#### 6️⃣ Delete a Product (ADMIN Only)
```http
DELETE http://localhost:8080/products/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 🔍 Common Issues & Troubleshooting

### Issue: "Connection refused" on port 8080/8081/8082
**Solution:** Ensure all three services are running. Check that no other application is using these ports.

### Issue: "Access denied" when accessing products
**Solution:** Make sure you're sending a valid JWT token in the `Authorization: Bearer` header.

### Issue: "User already exists"
**Solution:** Register with a different username or login if the user already exists.

### Issue: MySQL connection failed
**Solution:** 
1. Verify MySQL is running: `mysql -u root -p`
2. Check credentials in `application.properties`
3. Ensure databases exist: `SHOW DATABASES;`

---

## � Testing

### Unit Tests
```bash
# Run tests for Auth Service
cd auth-service
mvn test

# Run tests for Product Service
cd product-service
mvn test
```

### Integration Tests with Postman
Import the Postman collection (if available) or manually test the API endpoints listed above.

---


## 🔮 Future Enhancements

- ✨ **Service Discovery** with Eureka
- ✨ **Distributed Tracing** with Spring Cloud Sleuth
- ✨ **Centralized Logging** with ELK Stack
- ✨ **Cache Layer** with Redis
- ✨ **Message Queue** with RabbitMQ
- ✨ **Docker & Docker Compose** support
- ✨ **Refresh Tokens** for enhanced security
- ✨ **Mobile App** integration (Android/iOS)
- ✨ **CI/CD Pipeline** with GitHub Actions
- ✨ **API Documentation** with Swagger/OpenAPI
- ✨ **Rate Limiting** and API throttling
- ✨ **Health Checks** and Monitoring

---


## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

**Built with ❤️ using Spring Boot & Microservices Architecture**
