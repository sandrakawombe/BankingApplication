#!/bin/bash

# Banking Application - Project Setup Script
# This script initializes the complete project structure

set -e

echo "🏦 Banking Application - Project Setup"
echo "======================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

# Check if we're in the right directory
if [ ! -f "pom.xml" ]; then
    print_error "Please run this script from the project root directory"
    exit 1
fi

print_info "Creating directory structure..."

# Create main directories
mkdir -p services/{account-service,transaction-service,auth-service}
mkdir -p shared/common-lib
mkdir -p infrastructure/{config,kubernetes}
mkdir -p docs
mkdir -p scripts
mkdir -p .github/workflows

print_success "Main directories created"

# Create service directories
print_info "Creating service structures..."

for service in account-service transaction-service auth-service; do
    mkdir -p services/$service/src/main/java/com/banking/${service//-/}/controller
    mkdir -p services/$service/src/main/java/com/banking/${service//-/}/service
    mkdir -p services/$service/src/main/java/com/banking/${service//-/}/repository
    mkdir -p services/$service/src/main/java/com/banking/${service//-/}/model/entity
    mkdir -p services/$service/src/main/java/com/banking/${service//-/}/model/dto
    mkdir -p services/$service/src/main/java/com/banking/${service//-/}/exception
    mkdir -p services/$service/src/main/java/com/banking/${service//-/}/config
    mkdir -p services/$service/src/main/java/com/banking/${service//-/}/util
    mkdir -p services/$service/src/main/resources
    mkdir -p services/$service/src/test/java/com/banking/${service//-/}
    print_success "Created structure for $service"
done

# Create common library structure
print_info "Creating common library structure..."
mkdir -p shared/common-lib/src/main/java/com/banking/common/{dto,exception,util}
mkdir -p shared/common-lib/src/main/resources
print_success "Common library structure created"

# Create POM files for each service
print_info "Creating POM files..."

# Account Service POM
cat > services/account-service/pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.banking</groupId>
        <artifactId>banking-application</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../../pom.xml</relativePath>
    </parent>

    <artifactId>account-service</artifactId>
    <name>Account Service</name>
    <description>Account Management Service</description>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- Database -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>

        <!-- Utilities -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
        </dependency>

        <!-- Documentation -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        </dependency>

        <!-- Common Library -->
        <dependency>
            <groupId>com.banking</groupId>
            <artifactId>common-lib</artifactId>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
EOF

print_success "Account Service POM created"

# Transaction Service POM
cat > services/transaction-service/pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.banking</groupId>
        <artifactId>banking-application</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../../pom.xml</relativePath>
    </parent>

    <artifactId>transaction-service</artifactId>
    <name>Transaction Service</name>
    <description>Transaction Processing Service</description>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- Database -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>

        <!-- Utilities -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
        </dependency>

        <!-- Documentation -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        </dependency>

        <!-- Common Library -->
        <dependency>
            <groupId>com.banking</groupId>
            <artifactId>common-lib</artifactId>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
EOF

print_success "Transaction Service POM created"

# Auth Service POM
cat > services/auth-service/pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.banking</groupId>
        <artifactId>banking-application</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../../pom.xml</relativePath>
    </parent>

    <artifactId>auth-service</artifactId>
    <name>Auth Service</name>
    <description>Authentication and Authorization Service</description>

    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
        </dependency>

        <!-- Database -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>

        <!-- Utilities -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
        </dependency>

        <!-- Documentation -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        </dependency>

        <!-- Common Library -->
        <dependency>
            <groupId>com.banking</groupId>
            <artifactId>common-lib</artifactId>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
EOF

print_success "Auth Service POM created"

# Common Library POM
cat > shared/common-lib/pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.banking</groupId>
        <artifactId>banking-application</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../../pom.xml</relativePath>
    </parent>

    <artifactId>common-lib</artifactId>
    <name>Common Library</name>
    <description>Shared utilities, DTOs, and exceptions</description>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </dependency>
    </dependencies>
</project>
EOF

print_success "Common Library POM created"

# Create application.yml files
print_info "Creating application configuration files..."

# Account Service configuration
cat > services/account-service/src/main/resources/application.yml << 'EOF'
server:
  port: 8082

spring:
  application:
    name: account-service
  
  datasource:
    url: jdbc:postgresql://localhost:5432/account_db
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  
  jackson:
    serialization:
      write-dates-as-timestamps: false

logging:
  level:
    com.banking.accountservice: DEBUG
    org.hibernate.SQL: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
EOF

# Transaction Service configuration
cat > services/transaction-service/src/main/resources/application.yml << 'EOF'
server:
  port: 8083

spring:
  application:
    name: transaction-service
  
  datasource:
    url: jdbc:postgresql://localhost:5432/transaction_db
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  
  jackson:
    serialization:
      write-dates-as-timestamps: false

logging:
  level:
    com.banking.transactionservice: DEBUG
    org.hibernate.SQL: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
EOF

# Auth Service configuration
cat > services/auth-service/src/main/resources/application.yml << 'EOF'
server:
  port: 8081

spring:
  application:
    name: auth-service
  
  datasource:
    url: jdbc:postgresql://localhost:5432/auth_db
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  
  jackson:
    serialization:
      write-dates-as-timestamps: false

jwt:
  secret: ${JWT_SECRET:yourSecretKeyForJWTTokenGeneration123456789}
  expiration: 86400000 # 24 hours in milliseconds

logging:
  level:
    com.banking.authservice: DEBUG
    org.hibernate.SQL: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
EOF

print_success "Configuration files created"

# Create README files for each service
cat > services/account-service/README.md << 'EOF'
# Account Service

Manages bank accounts for the banking application.

## Responsibilities
- Create new accounts
- View account details
- Update account information
- Get account balance
- List user accounts

## Endpoints
- `POST /api/v1/accounts` - Create account
- `GET /api/v1/accounts/{id}` - Get account details
- `GET /api/v1/accounts/user/{userId}` - Get user accounts
- `PUT /api/v1/accounts/{id}` - Update account
- `DELETE /api/v1/accounts/{id}` - Close account

## Running Locally
```bash
mvn spring-boot:run
```

Access Swagger UI: http://localhost:8082/swagger-ui.html
EOF

cat > services/transaction-service/README.md << 'EOF'
# Transaction Service

Processes financial transactions for the banking application.

## Responsibilities
- Deposit money
- Withdraw money
- Transfer between accounts
- View transaction history
- Transaction status tracking

## Endpoints
- `POST /api/v1/transactions/deposit` - Deposit money
- `POST /api/v1/transactions/withdraw` - Withdraw money
- `POST /api/v1/transactions/transfer` - Transfer between accounts
- `GET /api/v1/transactions/account/{accountId}` - Get account transactions
- `GET /api/v1/transactions/{id}` - Get transaction details

## Running Locally
```bash
mvn spring-boot:run
```

Access Swagger UI: http://localhost:8083/swagger-ui.html
EOF

cat > services/auth-service/README.md << 'EOF'
# Auth Service

Handles authentication and authorization for the banking application.

## Responsibilities
- User registration
- User login
- JWT token generation
- Token validation
- Role-based access control

## Endpoints
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/validate` - Validate token
- `POST /api/v1/auth/refresh` - Refresh token

## Running Locally
```bash
mvn spring-boot:run
```

Access Swagger UI: http://localhost:8081/swagger-ui.html
EOF

print_success "Service README files created"

echo ""
echo "======================================"
print_success "Project structure setup complete!"
echo "======================================"
echo ""
print_info "Next steps:"
echo "1. Review the generated structure"
echo "2. Commit initial structure to git:"
echo "   git add ."
echo "   git commit -m 'chore: Initialize project structure'"
echo "   git push origin main"
echo ""
echo "3. Create develop branch:"
echo "   git checkout -b develop"
echo "   git push -u origin develop"
echo ""
echo "4. Start working on your feature branches!"
echo ""
print_success "Happy coding! 🚀"
