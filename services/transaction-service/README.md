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
