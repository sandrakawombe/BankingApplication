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
