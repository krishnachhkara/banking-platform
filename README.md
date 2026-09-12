# 🏦 Banking Platform

> A production-oriented banking platform built progressively with Java and Spring Boot, evolving from a secure monolithic backend into an event-driven, distributed system.

This project is being developed as a **single evolving banking system**, with each version introducing new engineering challenges and technologies only when they solve a real problem in the system.

The core philosophy is:

> **Every version exists because the previous version creates a real engineering problem that the next version needs to solve.**

---

## 🚀 Project Evolution

```text
V1
Foundation + Authentication
        ↓
V2
Security + Account Management
        ↓
V3
Money Movement + Transactions
        ↓
V4
Performance + Caching
        ↓
V5
Event-Driven Architecture
        ↓
V6
Microservices + Distributed Transactions
        ↓
V7
Fraud + Notifications + External Payments
        ↓
V8
Production Deployment + Observability + CI/CD
```

The project is intentionally developed **incrementally** rather than introducing every technology from the beginning.

---

# 📌 Current Status

### V1 — Foundation + Authentication

**Status: 🚧 In Progress**

The current version focuses on building a clean Spring Boot backend with proper authentication, validation, persistence, exception handling, and security fundamentals.

### Implemented

* User registration
* DTO validation
* Email normalization
* Duplicate email validation
* Password hashing with Argon2
* JPA auditing
* Custom exception handling
* `UserDetails`
* `UserDetailsService`
* `DaoAuthenticationProvider`
* `AuthenticationManager`
* Email/password authentication

### Current Work

* JWT access token
* Refresh token
* JWT authentication filter
* Security context
* Protected endpoints
* Authorization and roles

---

# 🏗 Architecture

The initial backend follows a layered monolithic architecture:

```text
                     Client
                       │
                       ▼
                  HTTP / REST
                       │
                       ▼
              Security Filter Chain
                       │
                       ▼
                  Controller
                       │
                       ▼
                    Service
                       │
                       ▼
                  Repository
                       │
                       ▼
                  PostgresSQL
```

The architecture will evolve as the system develops and new engineering requirements emerge.

---

# 🛠 Technology Stack

### Current

| Technology      | Purpose                                |
| --------------- | -------------------------------------- |
| Java 21         | Backend language                       |
| Spring Boot     | Application framework                  |
| Spring MVC      | REST APIs                              |
| Spring Security | Authentication & authorization         |
| Spring Data JPA | Data access                            |
| Hibernate       | ORM                                    |
| PostgreSQL      | Relational database                    |
| Bean Validation | Request validation                     |
| Maven           | Build & dependency management          |
| Git             | Version control                        |
| GitHub          | Source control & project documentation |

### Planned

The following technologies will be introduced progressively when the project creates a genuine need for them:

* Redis
* Kafka
* Microservices
* Saga Pattern
* Docker
* CI/CD
* AWS
* Observability tools

---

# 🔐 Authentication & Security

The authentication architecture uses Spring Security with the following flow:

```text
Login
  │
  ▼
AuthController
  │
  ▼
AuthService
  │
  ▼
AuthenticationManager
  │
  ▼
DaoAuthenticationProvider
  │
  ├── UserDetailsService
  │       │
  │       ▼
  │   UserRepository
  │       │
  │       ▼
  │   PostgresSQL
  │
  └── PasswordEncoder
          │
          ▼
        Argon2
```

The authentication system is being extended with:

```text
Access JWT
    ↓
Refresh Token
    ↓
JWT Authentication Filter
    ↓
SecurityContext
    ↓
Protected Endpoints
    ↓
Authorization / Roles
```

---

# 👤 User Domain

The user domain currently contains concepts such as:

```text
User
├── id
├── name
├── email
├── passwordHash
├── role
├── status
├── createdAt
└── updatedAt
```

Registration follows:

```text
Request
   ↓
DTO Validation
   ↓
Email Normalization
   ↓
Duplicate Check
   ↓
Password Hashing
   ↓
Create User
   ↓
Save to Database
   ↓
Response DTO
```

---

# 🏦 Development Roadmap

## V1 — Foundation + Authentication

**Status: 🚧 In Progress**

Focus:

* Spring Boot foundation
* User management
* Authentication
* Password hashing
* JWT
* Refresh tokens
* Authorization
* Validation
* Exception handling
* Testing

---

## V2 — Real Banking Domain

**Status: ⏳ Planned**

Introduce the actual banking domain:

```text
Customer
   │
   ├── Account
   │
   └── Transactions
```

Planned account concepts:

* Account
* Account number
* Account type
* Balance
* Currency
* Status
* Account ownership

Example APIs:

```text
POST   /api/v1/accounts
GET    /api/v1/accounts
GET    /api/v1/accounts/{id}
PATCH  /api/v1/accounts/{id}
```

Authorization will distinguish between customer-owned resources and administrative operations.

---

## V3 — Money Movement & Transactions

**Status: ⏳ Planned**

This is where the project introduces real financial transaction problems.

Planned functionality:

* Deposits
* Withdrawals
* Fund transfers
* Transaction entity
* Transaction history
* Debit/credit concepts
* Atomic operations
* Database transactions
* Concurrency
* Race conditions
* Optimistic locking
* Pessimistic locking
* Idempotency
* Transaction states
* Rollback
* Double-spending prevention
* Proper monetary representation

Example transfer flow:

```text
Request
   ↓
Validate Accounts
   ↓
Check Ownership
   ↓
Check Balance
   ↓
Debit Sender
   ↓
Credit Receiver
   ↓
Create Transaction
   ↓
Commit
```

A major focus of this version will be safely handling concurrent financial operations.

---

## V4 — Performance & Caching

**Status: ⏳ Planned**

Redis will be introduced when repeated database reads create a meaningful performance problem.

Potential use cases:

* Account-related reads
* User profile data
* Frequently accessed data
* Rate limiting
* Session-related use cases

Concepts:

* Redis fundamentals
* Cache-aside pattern
* TTL
* Cache invalidation
* Serialization
* Distributed caching

The goal is not to cache everything, but to understand **when caching is actually useful**.

---

## V5 — Event-Driven Architecture

**Status: ⏳ Planned**

As the banking system grows, operations such as transactions, notifications, auditing, fraud analysis and analytics can become tightly coupled.

The architecture will evolve toward:

```text
Transaction Completed
        │
        ▼
      Kafka
        │
   ┌────┼────────────┐
   ▼    ▼            ▼
 Fraud  Notification Audit
```

Topics of study:

* Kafka
* Topics
* Partitions
* Producers
* Consumers
* Consumer groups
* Offsets
* Delivery semantics
* Eventual consistency

Kafka will be introduced because the system needs event-driven communication, rather than simply being added as a resume technology.

---

## V6 — Microservices & Distributed Transactions

**Status: ⏳ Planned**

Only after the monolithic backend is genuinely understood will the system evolve toward microservices.

Potential services:

```text
                 API Gateway
                     │
          ┌──────────┼──────────┐
          ▼          ▼          ▼
     User Service  Account   Transaction
                   Service     Service
                                  │
                         ┌────────┼────────┐
                         ▼        ▼        ▼
                       Fraud  Notification Payment
```

Potential concepts:

* Service boundaries
* Inter-service communication
* Distributed transactions
* Saga pattern
* Compensating transactions
* Eventual consistency
* Outbox pattern
* Distributed failures

---

## V7 — Advanced Banking Features

**Status: ⏳ Planned**

Potential capabilities:

### Fraud Detection

```text
Transaction
    ↓
Kafka
    ↓
Fraud Service
    ↓
Rules / ML
    ↓
Risk Score
    ↓
ALLOW / REVIEW / BLOCK
```

### OTP

```text
Sensitive Operation
       ↓
   OTP Service
       ↓
 Notification
       ↓
    User
       ↓
 Verify OTP
```

### Notifications

```text
Event
  ↓
Kafka
  ↓
Notification Service
  ├── Email
  ├── SMS
  └── Push
```

### External Payments

```text
Banking System
      ↓
Payment Provider
      ↓
Webhook
      ↓
Our Backend
```

This stage will introduce concepts such as webhooks, signature verification, retries, idempotency and failure recovery.

---

## V8 — Production Engineering

**Status: ⏳ Planned**

The final stage focuses on making the system deployable and observable.

### Docker

```text
Docker
├── Banking API
├── PostgresSQL
├── Redis
└── Kafka
```

### CI/CD

```text
GitHub
   ↓
GitHub Actions
   ↓
Tests
   ↓
Build
   ↓
Docker Image
   ↓
Deployment
```

### Cloud

Potential AWS components:

* Compute
* PostgreSQL
* Redis
* Object storage
* Load balancing
* Monitoring

### Observability

Potential areas:

* Application logs
* Metrics
* Distributed traces
* Health checks
* Monitoring

Potential technologies include:

* Spring Boot Actuator
* Prometheus
* Grafana
* OpenTelemetry

---

# 🧪 Testing Strategy

Testing will be introduced progressively alongside the features that require it.

Important scenarios include:

```text
Registration
    ↓
Validation
    ↓
Duplicate Email
    ↓
Successful Login
    ↓
Wrong Password
    ↓
Inactive User
    ↓
JWT Generation
    ↓
JWT Expiration
    ↓
Protected Endpoint
    ↓
Unauthorized Request
```

Later versions will add tests for critical banking scenarios such as:

* Successful transfers
* Insufficient balance
* Invalid accounts
* Concurrent withdrawals
* Transaction rollback
* Idempotent requests
* Authorization failures

---

# 🧠 Engineering Philosophy

This project is not being built by learning technologies in isolation.

The development process is:

```text
Problem
   ↓
Architecture
   ↓
Design Decision
   ↓
Learn Required Concepts
   ↓
Implementation
   ↓
Testing
   ↓
Refactoring
   ↓
Next Problem
```

For example:

```text
Transfers have race conditions
        ↓
How do we solve concurrency?
        ↓
Learn transactions and locking
        ↓
Implement
        ↓
Test concurrent transfers
```

Another example:

```text
Repeated database reads
        ↓
Can caching help?
        ↓
Learn Redis
        ↓
Implement
        ↓
Measure and test
```

This approach keeps every technology connected to an actual engineering problem.

---

# 📈 Project Goals

Through this project, the goal is to progressively develop practical understanding of:

* Backend architecture
* Spring Boot
* Spring Security
* JPA/Hibernate
* PostgreSQL
* Database transactions
* Concurrency
* Distributed systems
* Caching
* Event-driven architecture
* Microservices
* Fault tolerance
* Security
* Testing
* CI/CD
* Cloud deployment
* Observability

The final goal is not simply to collect technologies, but to understand **why, when and how they are used in real backend systems**.

---

# 📌 Current Development Checkpoint

```text
Project: Banking Platform

Current Version:
V1 — Foundation + Authentication

Completed:
✓ Registration
✓ DTO validation
✓ Email normalization
✓ Password hashing / Argon2
✓ JPA auditing
✓ Exception handling
✓ UserDetails
✓ UserDetailsService
✓ DaoAuthenticationProvider
✓ AuthenticationManager
✓ Email/password authentication

Current:
🚧 JWT Access Token

Next:
→ Refresh Token
→ JWT Filter
→ SecurityContext
→ Protected Endpoint
→ Authorization
```

---

## ⚠️ Development Principle

Technologies such as Redis, Kafka, microservices, Saga, Docker and cloud infrastructure will **not** be introduced simply because they are popular.

Each technology will be introduced when the banking system creates a genuine engineering problem that the technology can help solve.

> **Build → Encounter a problem → Understand the problem → Design a solution → Learn the required technology → Implement → Test → Refactor.**

---

## ⭐ Project Status

**Actively developed**

This repository represents the progressive development of the Banking Platform and will evolve as new versions are completed.
