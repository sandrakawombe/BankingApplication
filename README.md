# 🏦 Banking Application - Microservices Architecture

A modern, production-ready banking application built with microservices architecture using Spring Boot and Java.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Services](#services)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [Development](#development)
- [Testing](#testing)
- [Deployment](#deployment)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)

## 🎯 Overview

This project demonstrates a complete microservices-based banking system with the following capabilities:

- Account management (create, view, update accounts)
- Transaction processing (deposits, withdrawals, transfers)
- Secure authentication and authorization
- RESTful APIs
- Database per service pattern
- Inter-service communication
- Comprehensive testing

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                         API Gateway                          │
│                  (Spring Cloud Gateway)                      │
└────────────┬────────────────────────────────┬────────────────┘
             │                                │
             │                                │
    ┌────────▼─────────┐          ┌──────────▼──────────┐
    │  Auth Service    │          │  Account Service    │
    │  Port: 8081      │          │  Port: 8082         │
    │  ┌─────────────┐ │          │  ┌────────────────┐ │
    │  │ PostgreSQL  │ │          │  │  PostgreSQL    │ │
    │  └─────────────┘ │          │  └────────────────┘ │
    └──────────────────┘          └─────────────────────┘
             │                                │
             │                                │
             │         ┌──────────▼──────────────┐
             │         │ Transaction Service     │
             └────────►│ Port: 8083              │
                       │  ┌────────────────┐     │
                       │  │  PostgreSQL    │     │
                       │  └────────────────┘     │
                       └─────────────────────────┘
```

### Service Responsibilities

| Service | Responsibility | Port |
|---------|---------------|------|
| **Auth Service** | User authentication, JWT token generation, authorization | 8081 |
| **Account Service** | Account creation, account management, balance inquiries | 8082 |
| **Transaction Service** | Deposits, withdrawals, transfers, transaction history | 8083 |

## 🔧 Services

### 1. Auth Service
Handles user authentication and authorization.

**Features:**
- User registration
- Login with JWT token generation
- Token validation
- Role-based access control (RBAC)

**Endpoints:**
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/validate` - Validate token
- `POST /api/v1/auth/refresh` - Refresh token

### 2. Account Service
Manages bank accounts.

**Features:**
- Create bank accounts
- View account details
- Update account information
- Get account balance
- List user accounts

**Endpoints:**
- `POST /api/v1/accounts` - Create account
- `GET /api/v1/accounts/{id}` - Get account details
- `GET /api/v1/accounts/user/{userId}` - Get user accounts
- `PUT /api/v1/accounts/{id}` - Update account
- `DELETE /api/v1/accounts/{id}` - Close account

### 3. Transaction Service
Processes financial transactions.

**Features:**
- Deposit money
- Withdraw money
- Transfer between accounts
- View transaction history
- Transaction status tracking

**Endpoints:**
- `POST /api/v1/transactions/deposit` - Deposit money
- `POST /api/v1/transactions/withdraw` - Withdraw money
- `POST /api/v1/transactions/transfer` - Transfer between accounts
- `GET /api/v1/transactions/account/{accountId}` - Get account transactions
- `GET /api/v1/transactions/{id}` - Get transaction details

## 🛠️ Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.2.x** - Application framework
- **Spring Cloud** - Microservices infrastructure
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Data persistence
- **PostgreSQL** - Primary database
- **H2** - In-memory database for testing

### Build & Dependencies
- **Maven** - Dependency management
- **Lombok** - Reduce boilerplate code
- **MapStruct** - Object mapping

### Documentation
- **SpringDoc OpenAPI 3** - API documentation (Swagger)

### Testing
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework
- **TestContainers** - Integration testing
- **REST Assured** - API testing

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Local orchestration
- **GitHub Actions** - CI/CD

## 🚀 Getting Started

### Prerequisites

- Java 17 or later
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL (or use Docker)
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/sandrakawombe/BankingApplication.git
   cd BankingApplication
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run with Docker Compose (Recommended)**
   ```bash
   cd infrastructure
   docker-compose up -d
   ```

4. **Or run services individually**
   
   Terminal 1 - Auth Service:
   ```bash
   cd services/auth-service
   mvn spring-boot:run
   ```
   
   Terminal 2 - Account Service:
   ```bash
   cd services/account-service
   mvn spring-boot:run
   ```
   
   Terminal 3 - Transaction Service:
   ```bash
   cd services/transaction-service
   mvn spring-boot:run
   ```

### Configuration

Each service has its own `application.yml` in `src/main/resources/`. You can override properties using environment variables or profiles.

**Example for Account Service:**
```yaml
server:
  port: 8082

spring:
  application:
    name: account-service
  datasource:
    url: jdbc:postgresql://localhost:5432/account_db
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

## 💻 Development

### Setting Up Development Environment

1. **IDE Setup**
   - IntelliJ IDEA (Recommended) or Eclipse
   - Install Lombok plugin
   - Enable annotation processing

2. **Database Setup**
   ```bash
   # Using Docker
   docker run --name postgres-dev \
     -e POSTGRES_PASSWORD=postgres \
     -p 5432:5432 \
     -d postgres:15
   ```

3. **Create Databases**
   ```sql
   CREATE DATABASE auth_db;
   CREATE DATABASE account_db;
   CREATE DATABASE transaction_db;
   ```

### Git Workflow

1. **Create feature branch**
   ```bash
   git checkout -b feature/service-name-feature-description
   ```

2. **Make changes and commit**
   ```bash
   git add .
   git commit -m "feat(service-name): Add feature description"
   ```

3. **Push and create PR**
   ```bash
   git push origin feature/service-name-feature-description
   ```

See [SETUP_GUIDE.md](./docs/SETUP_GUIDE.md) for detailed workflow instructions.

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Tests for Specific Service
```bash
cd services/account-service
mvn test
```

### Run Integration Tests
```bash
mvn verify -P integration-tests
```

### Code Coverage Report
```bash
mvn clean test jacoco:report
# Report will be in target/site/jacoco/index.html
```

### Test Coverage Goals
- Unit Test Coverage: Minimum 70%
- Integration Test Coverage: Minimum 60%
- Overall Coverage: Minimum 70%

## 📦 Deployment

### Docker Build

Build individual service:
```bash
cd services/account-service
docker build -t banking/account-service:latest .
```

Build all services:
```bash
./scripts/build-all.sh
```

### Deploy with Docker Compose

```bash
cd infrastructure
docker-compose up -d
```

Services will be available at:
- Auth Service: http://localhost:8081
- Account Service: http://localhost:8082
- Transaction Service: http://localhost:8083

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_HOST` | Database host | localhost |
| `DB_PORT` | Database port | 5432 |
| `DB_USERNAME` | Database username | postgres |
| `DB_PASSWORD` | Database password | postgres |
| `JWT_SECRET` | JWT signing key | (required) |
| `JWT_EXPIRATION` | JWT expiration time | 86400000 |

## 📚 API Documentation

Once services are running, access Swagger UI:

- Auth Service: http://localhost:8081/swagger-ui.html
- Account Service: http://localhost:8082/swagger-ui.html
- Transaction Service: http://localhost:8083/swagger-ui.html

### Sample API Requests

**Register User:**
```bash
curl -X POST http://localhost:8081/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "email": "john@example.com",
    "password": "SecurePass123!"
  }'
```

**Create Account:**
```bash
curl -X POST http://localhost:8082/api/v1/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>" \
  -d '{
    "accountType": "SAVINGS",
    "currency": "USD",
    "initialBalance": 1000.00
  }'
```

## 👥 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'feat: Add AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style Guidelines

- Follow Java naming conventions
- Write meaningful commit messages (Conventional Commits)
- Add unit tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

## 📝 Project Status

- [x] Repository setup
- [ ] Auth Service - In Progress
- [ ] Account Service - In Progress
- [ ] Transaction Service - Planned
- [ ] API Gateway - Planned
- [ ] Service Discovery - Planned
- [ ] Monitoring & Logging - Planned

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Team

- **Sandra Kawombe** - Account Service Developer
- **[Colleague Name]** - Transaction Service Developer

## 📞 Support

For questions or issues:
- Open an issue on GitHub
- Contact the development team

---

**Happy Banking! 💰**
