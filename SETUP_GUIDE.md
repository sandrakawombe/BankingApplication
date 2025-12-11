# Banking Application - Microservices Setup Guide

## 📋 Table of Contents
1. [Repository Structure](#repository-structure)
2. [Initial Setup Steps](#initial-setup-steps)
3. [Git Workflow & Branching Strategy](#git-workflow--branching-strategy)
4. [Technology Stack](#technology-stack)
5. [Development Workflow](#development-workflow)
6. [Best Practices](#best-practices)

---

## 🏗️ Repository Structure

We'll use a **monorepo** approach with separate services. This is ideal for your team size and ensures:
- Shared dependencies and configurations
- Easier code sharing and refactoring
- Consistent versioning
- Simplified CI/CD

### Recommended Structure

```
BankingApplication/
├── .github/
│   └── workflows/              # CI/CD pipelines
│       ├── account-service.yml
│       ├── transaction-service.yml
│       └── auth-service.yml
├── services/
│   ├── account-service/
│   │   ├── src/
│   │   ├── pom.xml
│   │   ├── Dockerfile
│   │   └── README.md
│   ├── transaction-service/
│   │   ├── src/
│   │   ├── pom.xml
│   │   ├── Dockerfile
│   │   └── README.md
│   └── auth-service/
│       ├── src/
│       ├── pom.xml
│       ├── Dockerfile
│       └── README.md
├── shared/
│   └── common-lib/             # Shared DTOs, utilities, exceptions
│       ├── src/
│       └── pom.xml
├── infrastructure/
│   ├── docker-compose.yml      # Local development
│   ├── kubernetes/             # K8s manifests (optional)
│   └── config/                 # Config files
│       ├── application-dev.yml
│       ├── application-prod.yml
│       └── application-test.yml
├── docs/
│   ├── API_DOCUMENTATION.md
│   ├── ARCHITECTURE.md
│   └── SETUP_GUIDE.md
├── scripts/
│   ├── setup.sh
│   ├── build-all.sh
│   └── run-local.sh
├── .gitignore
├── pom.xml                      # Parent POM
└── README.md
```

---

## 🚀 Initial Setup Steps

### Step 1: Clean Up Your Repository

```bash
# Clone your repository
git clone https://github.com/sandrakawombe/BankingApplication.git
cd BankingApplication

# Remove the test file
git rm httpsclaude.aishare00c7a87e-2d42-41.txt
git commit -m "Clean up repository"
git push origin main
```

### Step 2: Create Branch Protection Rules

**On GitHub:**
1. Go to: Settings → Branches → Add rule
2. Branch name pattern: `main`
3. Enable:
   - ✅ Require a pull request before merging
   - ✅ Require approvals (at least 1)
   - ✅ Require status checks to pass before merging
   - ✅ Require branches to be up to date before merging
   - ✅ Do not allow bypassing the above settings

### Step 3: Set Up Issue Templates

Create `.github/ISSUE_TEMPLATE/` folder with:
- `bug_report.md`
- `feature_request.md`

### Step 4: Create Initial Structure

```bash
# Create main directories
mkdir -p services/{account-service,transaction-service,auth-service}/src/main/{java,resources}
mkdir -p services/{account-service,transaction-service,auth-service}/src/test/java
mkdir -p shared/common-lib/src/main/{java,resources}
mkdir -p infrastructure/{config,kubernetes}
mkdir -p docs scripts .github/workflows

# Create README files for each service
touch services/account-service/README.md
touch services/transaction-service/README.md
touch services/auth-service/README.md
```

---

## 🌿 Git Workflow & Branching Strategy

### Branching Model: GitFlow (Simplified)

```
main                    # Production-ready code only
  ├── develop           # Integration branch
  │   ├── feature/account-service-create-account
  │   ├── feature/transaction-service-transfer
  │   ├── feature/auth-service-jwt-implementation
  │   └── bugfix/account-service-validation-fix
```

### Branch Naming Convention

- `feature/{service-name}-{feature-description}`
  - Example: `feature/account-service-create-account`
  
- `bugfix/{service-name}-{bug-description}`
  - Example: `bugfix/transaction-service-null-pointer`
  
- `hotfix/{service-name}-{critical-fix}`
  - Example: `hotfix/auth-service-token-expiry`

### Workflow Steps

#### For You (Account Service)

```bash
# 1. Create and switch to develop branch
git checkout -b develop
git push -u origin develop

# 2. Create feature branch
git checkout -b feature/account-service-initial-setup

# 3. Work on your feature
# ... make changes ...

# 4. Commit with meaningful messages
git add .
git commit -m "feat(account-service): Add account creation endpoint

- Implemented AccountController with POST /accounts
- Added AccountService with business logic
- Created AccountRepository with JPA
- Added validation for account creation
- Unit tests for AccountService"

# 5. Push to remote
git push -u origin feature/account-service-initial-setup

# 6. Create Pull Request on GitHub
# - Base: develop
# - Compare: feature/account-service-initial-setup
# - Request review from colleague
```

#### For Your Colleague (Transaction Service)

```bash
# 1. Clone and switch to develop
git clone https://github.com/sandrakawombe/BankingApplication.git
cd BankingApplication
git checkout develop

# 2. Create feature branch
git checkout -b feature/transaction-service-initial-setup

# 3. Follow same workflow as above
```

### Commit Message Convention (Conventional Commits)

```
<type>(<scope>): <short summary>

<detailed description>

<footer>
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting)
- `refactor`: Code refactoring
- `test`: Adding tests
- `chore`: Build process or auxiliary tool changes

**Examples:**
```
feat(account-service): Add account creation endpoint
fix(transaction-service): Fix null pointer in transfer logic
docs(readme): Update setup instructions
test(auth-service): Add integration tests for JWT
```

---

## 🛠️ Technology Stack

### Recommended Technologies

#### Core Framework
- **Spring Boot 3.2.x** (Latest stable)
- **Java 17 or 21** (LTS versions)
- **Maven** (Dependency management)

#### Database
- **PostgreSQL** (Per service, if needed)
- **H2** (In-memory for testing)

#### Communication
- **REST APIs** (HTTP/JSON)
- **Spring Cloud OpenFeign** (Inter-service communication)
- Optional: **Apache Kafka** or **RabbitMQ** (Event-driven)

#### Security
- **Spring Security**
- **JWT (JSON Web Tokens)** for authentication
- **BCrypt** for password hashing

#### Service Discovery & Configuration
- **Spring Cloud Config** (Centralized configuration)
- **Eureka** or **Consul** (Service discovery)
- Optional: **API Gateway** (Spring Cloud Gateway)

#### Monitoring & Logging
- **SLF4J + Logback**
- **Spring Boot Actuator**
- **Micrometer** (Metrics)
- Optional: **ELK Stack** (Elasticsearch, Logback, Kibana)

#### Testing
- **JUnit 5**
- **Mockito**
- **Spring Boot Test**
- **TestContainers** (Integration testing with real DB)

#### Containerization
- **Docker**
- **Docker Compose** (Local development)
- Optional: **Kubernetes** (Production orchestration)

---

## 👥 Development Workflow

### Daily Workflow

1. **Start Your Day**
   ```bash
   git checkout develop
   git pull origin develop
   git checkout feature/your-feature-branch
   git rebase develop  # Keep your branch updated
   ```

2. **Work on Your Feature**
   - Write code
   - Write tests (TDD approach recommended)
   - Run tests locally
   - Commit frequently with meaningful messages

3. **Before Pushing**
   ```bash
   # Run all tests
   mvn clean test
   
   # Build the project
   mvn clean install
   
   # If all passes, push
   git push origin feature/your-feature-branch
   ```

4. **Create Pull Request**
   - Provide clear description
   - Link related issues
   - Request review from team member
   - Ensure CI/CD passes

5. **Code Review**
   - Reviewer checks code quality, tests, and functionality
   - Address feedback
   - Once approved, merge to develop

### Conflict Resolution

```bash
# If conflicts occur during rebase
git checkout develop
git pull origin develop
git checkout feature/your-feature-branch
git rebase develop

# Resolve conflicts in your IDE
git add .
git rebase --continue

# Force push (only to your feature branch!)
git push -f origin feature/your-feature-branch
```

---

## ✅ Best Practices

### 1. Code Organization

**Package Structure per Service:**
```
com.banking.accountservice/
├── controller/         # REST controllers
├── service/           # Business logic
├── repository/        # Data access
├── model/            # Domain entities
│   ├── entity/
│   └── dto/
├── exception/        # Custom exceptions
├── config/           # Configuration classes
└── util/            # Utility classes
```

### 2. Configuration Management

- **Use profiles:** `application-dev.yml`, `application-prod.yml`
- **Never commit secrets:** Use environment variables or secret managers
- **Externalize configuration:** Spring Cloud Config

### 3. API Design

- **RESTful principles:** Proper HTTP verbs and status codes
- **Versioning:** `/api/v1/accounts`
- **Consistent response format:**
  ```json
  {
    "timestamp": "2024-12-10T10:30:00",
    "status": 200,
    "data": { },
    "message": "Success"
  }
  ```

### 4. Error Handling

- **Global exception handler:** `@ControllerAdvice`
- **Custom exceptions:** `AccountNotFoundException`, `InsufficientFundsException`
- **Proper HTTP status codes:** 400, 404, 500, etc.

### 5. Security

- **Input validation:** `@Valid`, `@NotNull`, etc.
- **SQL injection prevention:** Use parameterized queries
- **Rate limiting:** Prevent abuse
- **CORS configuration:** Control cross-origin requests

### 6. Testing

- **Unit tests:** Test service layer (70% coverage minimum)
- **Integration tests:** Test controllers with MockMvc
- **Contract tests:** Test inter-service communication
- **Test coverage:** Use JaCoCo plugin

### 7. Documentation

- **API Documentation:** Swagger/OpenAPI (SpringDoc)
- **README per service:** Setup, endpoints, examples
- **Code comments:** Only for complex logic
- **Architecture diagrams:** Use Mermaid or draw.io

### 8. Database Practices

- **One database per service** (Microservice principle)
- **Database migrations:** Use Flyway or Liquibase
- **Connection pooling:** HikariCP (default in Spring Boot)
- **Transactions:** Use `@Transactional` wisely

### 9. Logging

- **Structured logging:** Include correlation IDs
- **Log levels:** DEBUG, INFO, WARN, ERROR
- **Don't log sensitive data:** Passwords, tokens, PII

### 10. Continuous Integration

- **Automated builds:** GitHub Actions
- **Run tests on PR:** Prevent broken code from merging
- **Code quality checks:** SonarQube, Checkstyle
- **Dependency scanning:** Dependabot

---

## 🚨 Common Pitfalls to Avoid

1. **❌ Committing directly to main**
   - Always use feature branches and PRs

2. **❌ Large, infrequent commits**
   - Commit small, logical changes frequently

3. **❌ Ignoring merge conflicts**
   - Address conflicts immediately and carefully

4. **❌ Not writing tests**
   - Write tests as you develop (TDD)

5. **❌ Hardcoding values**
   - Use configuration files and environment variables

6. **❌ Not reviewing code**
   - Always review each other's PRs thoroughly

7. **❌ Tight coupling between services**
   - Services should be independent and loosely coupled

8. **❌ Sharing databases between services**
   - Each service should have its own database

9. **❌ Not handling failures**
   - Implement circuit breakers, retries, and fallbacks

10. **❌ Ignoring security**
    - Security is not an afterthought

---

## 📚 Next Steps

1. **Week 1:** Set up repository structure and CI/CD
2. **Week 2:** Implement Account Service (you)
3. **Week 2:** Implement Transaction Service (colleague)
4. **Week 3:** Integrate services and test
5. **Week 4:** Implement Auth Service together
6. **Week 5:** Add monitoring, logging, and documentation
7. **Week 6:** Testing, bug fixes, and deployment preparation

---

## 🆘 Getting Help

- **Daily standups:** Sync with your colleague
- **Code reviews:** Learn from each other
- **Documentation:** Refer to Spring Boot docs, Stack Overflow
- **Version control:** Git documentation

---

**Remember:** Good practices at the start save hours of debugging later!

Happy coding! 🚀
