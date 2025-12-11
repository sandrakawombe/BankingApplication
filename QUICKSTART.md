# 🚀 Quick Start Guide

This guide will help you get your Banking Application microservices repository up and running in under 30 minutes.

## ⚡ Immediate Setup (Do This First!)

### 1. Clean Your Repository

First, remove the test file and add your new files:

```bash
# Navigate to your repository
cd BankingApplication

# Remove the test file
git rm httpsclaude.aishare00c7a87e-2d42-41.txt

# Add all the new files (README, .gitignore, pom.xml, etc.)
git add .

# Commit
git commit -m "chore: Initialize project structure with configuration files"

# Push to main
git push origin main
```

### 2. Run the Setup Script

```bash
# Make the script executable
chmod +x setup.sh

# Run it
./setup.sh
```

This will create:
- Complete directory structure for all services
- POM files for each service
- Application configuration files
- README files for each service

### 3. Commit the Structure

```bash
# Add everything
git add .

# Commit
git commit -m "chore: Initialize complete microservices structure"

# Push to main
git push origin main
```

### 4. Create and Set Up Branch Protection

**On GitHub:**
1. Go to: `Settings` → `Branches` → `Add rule`
2. Branch name pattern: `main`
3. Enable:
   - ☑️ Require a pull request before merging
   - ☑️ Require approvals (1)
   - ☑️ Require status checks to pass
4. Save changes

### 5. Create Develop Branch

```bash
# Create and switch to develop branch
git checkout -b develop

# Push develop branch
git push -u origin develop

# Develop is now your integration branch
```

## 👨‍💻 Your First Development Task (Account Service)

### Step 1: Create Your Feature Branch

```bash
# Make sure you're on develop
git checkout develop
git pull origin develop

# Create your feature branch
git checkout -b feature/account-service-initial-setup
```

### Step 2: Create Your First Application Class

Create `services/account-service/src/main/java/com/banking/accountservice/AccountServiceApplication.java`:

```java
package com.banking.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }
}
```

### Step 3: Test Your Service Runs

```bash
cd services/account-service
mvn spring-boot:run
```

You should see:
```
Started AccountServiceApplication in X.XXX seconds
```

Access:
- Application: http://localhost:8082
- Swagger UI: http://localhost:8082/swagger-ui.html
- Actuator: http://localhost:8082/actuator/health

Press `Ctrl+C` to stop.

### Step 4: Commit and Push

```bash
# Go back to root
cd ../..

# Add your changes
git add .

# Commit with proper message
git commit -m "feat(account-service): Add initial Spring Boot application

- Created AccountServiceApplication main class
- Configured application.yml
- Verified service starts successfully on port 8082"

# Push to your feature branch
git push -u origin feature/account-service-initial-setup
```

### Step 5: Create Pull Request

1. Go to GitHub
2. Click "Compare & pull request"
3. Base: `develop` ← Compare: `feature/account-service-initial-setup`
4. Title: `feat(account-service): Add initial Spring Boot application`
5. Description:
   ```markdown
   ## Changes
   - Created AccountServiceApplication main class
   - Configured application.yml for port 8082
   - Verified service starts successfully
   
   ## Testing
   - [x] Service starts without errors
   - [x] Actuator health endpoint responds
   - [x] Swagger UI accessible
   
   ## Screenshots
   (Optional: Add screenshots)
   ```
6. Request review from your colleague
7. Create pull request

## 👥 Your Colleague's First Task (Transaction Service)

Your colleague should follow the same pattern:

```bash
# Clone the repository
git clone https://github.com/sandrakawombe/BankingApplication.git
cd BankingApplication

# Switch to develop
git checkout develop

# Create feature branch
git checkout -b feature/transaction-service-initial-setup

# Create TransactionServiceApplication.java in appropriate location
# Test it runs
cd services/transaction-service
mvn spring-boot:run

# Commit and create PR
```

## 🔄 Daily Workflow

### Starting Your Day

```bash
# Update develop branch
git checkout develop
git pull origin develop

# Update your feature branch
git checkout feature/your-feature-name
git rebase develop

# Start working
```

### During Development

```bash
# Commit frequently (every logical change)
git add .
git commit -m "feat(service): What you did"

# Push regularly
git push origin feature/your-feature-name
```

### Before Creating PR

```bash
# Make sure all tests pass
mvn clean test

# Make sure it builds
mvn clean package

# Update from develop
git checkout develop
git pull origin develop
git checkout feature/your-feature-name
git rebase develop

# Push
git push -f origin feature/your-feature-name
```

## 🗄️ Database Setup (Optional for Local Development)

### Option 1: Using Docker (Recommended)

```bash
cd infrastructure
docker-compose up -d auth-db account-db transaction-db

# Databases will be available at:
# auth_db: localhost:5433
# account_db: localhost:5434
# transaction_db: localhost:5435
```

### Option 2: Local PostgreSQL

Install PostgreSQL and create databases:

```sql
CREATE DATABASE auth_db;
CREATE DATABASE account_db;
CREATE DATABASE transaction_db;
```

Update `application.yml` in each service if needed.

## 📋 Next Features to Implement

### Week 1-2: Account Service (You)

1. ✅ Initial setup
2. ⬜ Create Account entity
3. ⬜ Create AccountRepository
4. ⬜ Create AccountService with business logic
5. ⬜ Create AccountController with REST endpoints
6. ⬜ Add validation
7. ⬜ Write unit tests
8. ⬜ Write integration tests

### Week 1-2: Transaction Service (Colleague)

1. ⬜ Initial setup
2. ⬜ Create Transaction entity
3. ⬜ Create TransactionRepository
4. ⬜ Create TransactionService
5. ⬜ Create TransactionController
6. ⬜ Add validation
7. ⬜ Write tests

### Week 3: Integration

1. ⬜ Connect Account and Transaction services
2. ⬜ Implement inter-service communication
3. ⬜ Test end-to-end flows

### Week 4: Auth Service (Together)

1. ⬜ User entity and repository
2. ⬜ Registration endpoint
3. ⬜ Login endpoint
4. ⬜ JWT token generation
5. ⬜ Token validation
6. ⬜ Integrate with other services

## 🔧 Useful Commands

### Maven Commands

```bash
# Build everything
mvn clean install

# Build single service
cd services/account-service
mvn clean package

# Run tests
mvn test

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Skip tests
mvn clean install -DskipTests
```

### Git Commands

```bash
# See status
git status

# See what changed
git diff

# See commit history
git log --oneline --graph

# Undo last commit (keep changes)
git reset --soft HEAD~1

# Discard local changes
git checkout -- <file>

# Update branch from develop
git rebase develop
```

### Docker Commands

```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f account-service

# Rebuild service
docker-compose up -d --build account-service
```

## 🐛 Troubleshooting

### Port Already in Use

```bash
# Find process using port 8082
lsof -i :8082

# Kill it
kill -9 <PID>
```

### Maven Build Fails

```bash
# Clean everything
mvn clean

# Delete target directories
find . -name "target" -type d -exec rm -rf {} +

# Try again
mvn clean install
```

### Git Conflicts

```bash
# During rebase
git rebase develop

# If conflicts occur:
# 1. Fix conflicts in your IDE
# 2. Mark as resolved
git add .
git rebase --continue

# If you want to abort
git rebase --abort
```

## 📚 Learning Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Git Documentation](https://git-scm.com/doc)
- [Docker Documentation](https://docs.docker.com/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

## ✅ Checklist for First Week

- [ ] Repository structure created
- [ ] Branch protection rules set up
- [ ] Develop branch created
- [ ] Your service runs locally
- [ ] Colleague's service runs locally
- [ ] First PR created and merged
- [ ] Communication workflow established
- [ ] Daily standups scheduled

## 🎉 Success Criteria

You're on track when:
- ✅ Both services start without errors
- ✅ You can create and merge PRs
- ✅ Tests are passing
- ✅ No conflicts when merging
- ✅ You understand the workflow

## 🆘 Need Help?

1. Check the main [SETUP_GUIDE.md](SETUP_GUIDE.md)
2. Review service README files
3. Check GitHub Issues
4. Ask your colleague
5. Review Spring Boot documentation

---

**Remember:** Start small, commit often, test thoroughly, and communicate constantly!

Good luck! 🚀
