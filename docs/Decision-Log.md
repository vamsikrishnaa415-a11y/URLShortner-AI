# Architecture Decision Log

## Purpose

This log records foundational technology decisions for the URL Shortener assignment. The decisions favor a production-minded structure while preserving a low-friction local development experience.

## ADR-001: Java 17

### Decision

Use Java 17 as the application language and runtime baseline.

### Rationale

Java 17 is a long-term support release with mature tooling, strong ecosystem compatibility, and language improvements that support concise, maintainable server-side code. It is fully supported by Spring Boot 3.x and is a sensible baseline for an enterprise-oriented interview assignment.

### Consequences

- The build and runtime environments must provide JDK 17 or later.
- The project can use modern Java language and standard-library capabilities available in Java 17.
- Compatibility with older Java runtimes is not a goal.

## ADR-002: Spring Boot 3.x

### Decision

Use Spring Boot 3.x for future REST service implementation.

### Rationale

Spring Boot provides convention-driven configuration, embedded web-server support, validation integration, persistence support, and a mature testing ecosystem. Version 3.x aligns with Java 17 and Jakarta EE namespaces, giving the project a current framework baseline without requiring extensive boilerplate.

### Consequences

- Services will follow Spring Boot's configuration and dependency-management conventions.
- The project will use Jakarta-based APIs where applicable.
- Framework upgrades must account for Spring Boot's managed dependency versions.

## ADR-003: H2 Database

### Decision

Use H2 as the local development and automated-test database.

### Rationale

H2 is embedded, fast to start, and requires no external server. It enables repeatable tests and a self-contained developer experience while still supporting relational constraints needed for URL records, including unique short-code enforcement.

### Consequences

- Developers can run services locally without database provisioning.
- H2 data may not persist across local restarts, depending on configuration.
- H2 must be replaced by a durable production relational database before production deployment.
- SQL behavior and migrations require production-database validation later.

## ADR-004: Maven

### Decision

Use Maven for build automation and dependency management.

### Rationale

Maven is widely understood in Java teams, provides deterministic dependency resolution, integrates directly with Spring Boot, and supports multi-module builds suited to the planned service boundaries. Its standard lifecycle also makes builds and tests easy to run in local and continuous-integration environments.

### Consequences

- Future services will use Maven's conventional project layout and lifecycle.
- A parent Maven build can coordinate shared dependency versions and service modules.
- Dependency additions should be deliberate to keep the assignment lightweight.

## ADR-005: Microservices

### Decision

Model the target architecture as a small set of Spring Boot microservices: API Gateway, URL Service, and a future Analytics Service.

### Rationale

The separation makes domain ownership visible: URL lifecycle and redirect correctness belong to the URL Service, while analytics can evolve independently when it becomes in scope. This supports independent scaling and avoids a reporting workload affecting redirect performance.

The decision is intentionally restrained. The assignment does not require the operational machinery commonly associated with large microservice platforms.

### Consequences

- Each service owns its data and exposes a REST boundary.
- The API Gateway becomes the public routing layer.
- Local development will involve more than one service only when implementation commits introduce them.
- Cross-service calls introduce failure and latency considerations that must be handled explicitly.
- Docker, Kubernetes, Kafka, Redis, RabbitMQ, Eureka, OAuth, JWT, Grafana, and Prometheus remain out of scope.

## Alternatives Considered

| Alternative | Reason Not Chosen for This Assignment |
| --- | --- |
| Older Java LTS release | Java 17 offers a current, Spring Boot 3.x-compatible baseline. |
| Spring Boot 2.x | It does not provide the requested Spring Boot 3.x baseline or Jakarta alignment. |
| External relational database for local development | It increases setup burden without improving the interview assignment's core learning goals. |
| Gradle | Maven is specified and provides the required multi-module build support. |
| Single monolith | It obscures the requested microservice-oriented service ownership and future analytics isolation. |
| Full distributed platform | It adds operational complexity explicitly excluded from the assignment. |