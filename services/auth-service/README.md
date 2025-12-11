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
