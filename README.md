# CBE Digital Banking Platform — Backend API

> RESTful API service for the Commercial Bank of Ethiopia (CBE) Digital Banking Experience Platform.

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

## Overview

A secure, production-ready backend service powering the CBE Digital Banking platform. Built with Spring Boot 3.4 and Java 21, it provides JWT-based authentication, real-time queue management via WebSockets, and comprehensive banking operations.

## Tech Stack

| Technology | Purpose |
|---|---|
| **Spring Boot 3.4** | Application framework |
| **Java 21** | Runtime |
| **Spring Security + JWT** | Authentication & authorization |
| **Spring Data JPA** | Data persistence |
| **PostgreSQL 16** | Primary database |
| **Redis 7** | Caching & rate limiting |
| **WebSocket (STOMP)** | Real-time communication |
| **SpringDoc OpenAPI** | API documentation |
| **Lombok** | Boilerplate reduction |
| **Docker** | Containerization |

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/auth/register` | Register a new user |
| `POST` | `/api/v1/auth/login` | Authenticate and receive JWT |

### Banking Operations
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/banking/accounts` | Get user accounts |
| `POST` | `/api/v1/banking/transfer` | Transfer between accounts |
| `POST` | `/api/v1/banking/deposit` | Deposit to account |
| `POST` | `/api/v1/banking/withdraw` | Withdraw from account |
| `GET` | `/api/v1/banking/transactions` | Get transaction history |

### Queue Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/queue/book` | Book a queue token |
| `GET` | `/api/v1/queue/status` | Get queue status |

### User Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/users/profile` | Get user profile |
| `PUT` | `/api/v1/users/profile` | Update user profile |

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- Docker & Docker Compose (for database infrastructure)

### 1. Start Infrastructure

```bash
docker-compose up -d
```

### 2. Configure Environment

```bash
cp .env.example .env
# Edit .env with your configuration
```

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Project Structure

```
src/main/java/com/cbe/banking/
├── BankingApplication.java          # Application entry point
├── config/                          # Configuration classes
│   ├── ApplicationConfig.java       # Bean definitions
│   ├── DataSeeder.java             # Database seeding
│   └── WebSocketConfig.java        # WebSocket/STOMP setup
├── controller/                      # REST API controllers
│   ├── AuthenticationController.java
│   ├── BankingController.java
│   ├── QueueController.java
│   └── UserController.java
├── dto/                             # Data Transfer Objects
│   ├── AuthenticationRequest.java
│   ├── AuthenticationResponse.java
│   ├── RegisterRequest.java
│   └── TransactionRequest.java
├── model/                           # JPA Entity models
│   ├── Account.java
│   ├── Permission.java
│   ├── QueueToken.java
│   ├── Token.java
│   ├── Transaction.java
│   └── User.java
├── repository/                      # Spring Data JPA repositories
├── security/                        # Security & JWT
│   ├── JwtAuthenticationFilter.java
│   ├── JwtService.java
│   ├── LogoutService.java
│   └── SecurityConfig.java
└── service/                         # Business logic
    ├── AuthenticationService.java
    ├── BankingService.java
    └── QueueService.java
```

## Security

- **JWT Authentication** with configurable expiration
- **BCrypt** password hashing
- **CORS** configuration for frontend integration
- **Stateless sessions** — no server-side session storage
- **Role-based access control** with fine-grained permissions

## Related Repositories

- **Frontend**: [Banking-frontend](https://github.com/Etsubdinkdesalegn/Banking-frontend)

## License

This project is licensed under the MIT License.
