# VGDB Backend: Distributed Microservices Infrastructure

The Video Game Database (VGDB) backend is an enterprise-grade distributed system designed using microservices architecture patterns. It enforces decentralization, high availability, and separation of concerns by splitting read and write responsibilities into autonomous services.

## 🏗️ Core Architectural Design Patterns

### 1. Database-Per-Service Pattern
Each microservice governs its own physical database container, entirely decoupling the data schemas.
* *Search Database (Port 5433):* Read-optimized catalog store.
* *Inventory Database (Port 5434):* Write-optimized administrative store.
* *The "Why":* Prevents a "Distributed Monolith." If the inventory write database experiences heavy locks during major transaction periods, the catalog query engine remains 100% operational on its independent channel.

### 2. Service Discovery Pattern (Netflix Eureka)
Microservices operate in environments where host IPs and ports are fluid and dynamic. Hardcoding locations is an anti-pattern.
* *Mechanism:* Every service instance registers itself with Eureka on startup and streams a continuous "heartbeat." 
* *The "Why":* The API Gateway asks Eureka for the location of an instance by name (GAME-SEARCH-SERVICE), shifting routing complexity out of the code and into the structural topology.

### 3. Reactive API Gateway Pattern (Spring Cloud Gateway)
Serves as the exclusive single entry point ("Bouncer") for external applications (like our React frontend).
* *The "Why" (Why Not Netflix Zuul?):* Traditional Netflix Zuul 1.x is *blocking/synchronous* (one thread per incoming HTTP request). If down-stream services stall, threads quickly deplete, leading to complete cluster failure. Spring Cloud Gateway operates on a non-blocking, reactive *Event-Loop* pattern powered by Eclipse Netty. It multiplexes thousands of simultaneous requests across a minimal thread pool, resulting in massive concurrent throughput with fractional memory allocation.

### 4. Specification Pattern (Domain-Driven Design Criteria)
Implemented inside the Search Service via Spring Data JPA's Specification executor.
* *The "Why":* Avoids the "Method Explosion" anti-pattern inside Spring Repositories (e.g., findByGenreAndPlatformAndReleaseYear...). Instead, it acts as a dynamic Query Builder, assembling SQL WHERE clauses matching only the criteria specified by the client at runtime.

---

## 🛠️ Microservices Ecosystem Map

| Component / Service Name | Layer Role | Network Port | DB Port | Key Design Decisions |
| :--- | :--- | :--- | :--- | :--- |
| eureka-server | Service Registry | 8761 | N/A | Standalone configuration (register-with-eureka: false) |
| api-gateway | Edge Router / Proxy | 8000 | N/A | Non-blocking Event-Loop; Load balancing via lb:// tokens |
| game-search-service | Read Specialist | 8081 | 5433 | Stateless query processing; JPA Specifications engine |
| game-inventory-service| Write Specialist | 8082 | 5434 | Strict schema mutation boundaries; Jakarta Validation (@Valid) |

---

## 🗂️ Standardized 7-Layer Service Pattern
Every business domain service implements a strict, loosely coupled 7-layer design layout:
1. *Entity:* Plain Old Java Object (POJO) mapping directly to the specific relational database schema.
2. *Repository:* Abstract layer extending JpaRepository for basic or specialized database communication.
3. *Specification:* Component to fabricate dynamic, runtime SQL query predicates (used heavily in the Search domain).
4. *Service Interface:* Abstract functional contract detailing the exact business operational capabilities.
5. *Service Implementation (Impl):* Core programmatic engine executing validation rules and transaction isolation boundaries (@Transactional).
6. *Data Transfer Object (DTO):* Data masking layers (GameRequestDTO, GameResponseDTO) isolating internal database identities (e.g., auto-increment sequences) from the client layer.
7. *Controller:* Network boundary layer exposing RESTful HTTP resources to the network.

---

## 🚀 Execution & Integration Testing Flow

### Prerequisites
Ensure your local environment has Docker and Java 17 runtime dependencies installed.

### 1. Boot the Infrastructure
Spin up the decoupled PostgreSQL storage blocks:
```bash
docker compose up -d
Boot the Java components strictly in this sequence, giving each service 15-30 seconds to settle:

eureka-server

game-search-service & game-inventory-service

api-gateway

2. Postman Test Verification Protocol
All external traffic must navigate exclusively through the Gateway on port 8000. Direct resource access to 8081 or 8082 is restricted.

Test Route Engine (Search Catalog):

GET http://localhost:8000/api/v1/search

Expected Status: 200 OK (Returns active payload or an empty collection []).

Test Data Mutator & Validator (Create Game Inventory):

POST http://localhost:8000/api/v1/inventory

Payload Header: Content-Type: application/json

Payload Body: {"title": "Elden Ring", "genre": "RPG", "platform": "PC", "price": 59.99, "releaseYear": 2022}

Expected Status: 201 Created (Returns fully persistent entity schema bounded to a globally distinct UUID).

Test Edge Validation Mechanics (Failure Injection):

POST http://localhost:8000/api/v1/inventory

Payload Body: {"title": "", "price": -10.00}

Expected Status: 400 Bad Request (Intercepted by local global exception interceptors).

Test Erasure Mechanics (Delete Game Inventory):

DELETE http://localhost:8000/api/v1/inventory/{YOUR-UUID-HERE}

Expected Status: 204 No Content (Signals resource absolute erasure with zero residual payload return).