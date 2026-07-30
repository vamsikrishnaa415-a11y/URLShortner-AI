# System Overview

The URL Shortener creates compact links, redirects visitors to original destinations, and provides basic click analytics. It is planned as a small REST-based system using Java 17, Spring Boot 3.x, Maven, and H2 for local development.

# Architecture Goals

- Keep URL creation, redirection, and analytics responsibilities explicit.
- Keep the redirect path lightweight and independent from analytics retrieval.
- Support local development without external infrastructure.
- Preserve clear boundaries for future scaling while avoiding unnecessary enterprise components.

# Why Microservices

Microservices provide explicit ownership for URL lifecycle and analytics workloads, which can evolve and scale differently. This assignment limits the design to a small set of synchronous REST services and excludes service discovery, brokers, distributed cache, and orchestration.

# High Level Architecture

The API Gateway is the public entry point. It routes URL creation and redirect requests to the URL Service and analytics requests to the Analytics Service. The URL Service owns URL lifecycle and redirect resolution, while the Analytics Service owns click metrics.

Services communicate synchronously over HTTP. Each service owns its data store. For local development, H2 supplies simple embedded relational persistence without external infrastructure.

```mermaid
flowchart LR
    Client[Client] -->|REST| Gateway[API Gateway]
    Gateway -->|URL creation and redirects| UrlService[URL Service]
    Gateway -->|Analytics requests| AnalyticsService[Analytics Service]
    UrlService --> UrlDatabase[(URL H2 Database)]
    UrlService -->|Successful redirect event| AnalyticsService
    AnalyticsService --> AnalyticsDatabase[(Analytics H2 Database)]
```

# Components

| Component | Responsibilities | Owns |
| --- | --- | --- |
| API Gateway | Public request entry, routing, cross-cutting request handling, and consistent external error boundaries. | Route configuration and gateway-level policies. |
| URL Service | Short-code generation, destination validation, URL lifecycle management, metadata lookup, and redirect eligibility/resolution. | URL records and business rules. |
| Analytics Service | Capture successful redirect events and retrieve basic click metrics. | Analytics events and derived aggregates. |

## API Gateway

- Provide a single public base URL for client requests.
- Route management requests, such as `/api/v1/urls`, to the URL Service.
- Route short-code resolution requests, such as `/{shortCode}`, to the URL Service.
- Apply request correlation identifiers and forward them to downstream services.
- Enforce gateway-level request size limits and return consistent responses for routing failures.
- Keep business validation and URL persistence out of the gateway.

Authentication, authorization, rate limiting, and TLS termination are not implemented in the initial assignment. Their future introduction must remain gateway concerns rather than URL-domain concerns where appropriate.

## URL Service

- Validate destination URLs as absolute HTTP or HTTPS addresses.
- Generate URL-safe short codes and prevent collisions through persistence-level uniqueness.
- Create, retrieve, update, activate, and deactivate shortened URL records.
- Resolve an active short code to its destination URL.
- Return domain-appropriate outcomes for unknown, malformed, or inactive short codes.
- Persist URL data in its own H2 database during local development.
- Keep controller, service, domain, and persistence responsibilities separate within the service.

## Analytics Service

The Analytics Service records successful redirect events without changing URL Service ownership of link data. It will:
- Store event attributes such as short code, timestamp, and non-sensitive request context.
- Produce aggregate usage metrics for a short code.
- Apply retention and privacy policies before exposing analytics data.

The current constraints prohibit Kafka, RabbitMQ, and Redis. The initial design uses a simple REST interaction; more complex delivery is deferred until justified.

## H2 Database

H2 is used only for local development and automated testing. It keeps the assignment self-contained, supports relational constraints such as a unique short code, and allows repeatable test data setup.

The URL Service owns an H2 database containing URL records. The future Analytics Service would own a separate H2 database for its event data. No service reads another service's tables directly. Database access is isolated behind repository and service layers so H2 can later be replaced with a production relational database without changing REST contracts.

H2 is not a production durability or scaling strategy: data may be reset locally, and database behavior must be verified against a production database before a production release.

# Request Flow

### Create a Short URL

1. A client sends `POST /api/v1/urls` to the API Gateway.
2. The gateway assigns or forwards a correlation identifier and routes the request to the URL Service.
3. The URL Service validates the destination URL, generates a candidate short code, and saves the record.
4. A uniqueness conflict causes the service to generate another candidate short code.
5. The URL Service returns the created resource representation through the gateway.

### Resolve a Short URL

1. A client sends `GET /{shortCode}` to the API Gateway.
2. The gateway routes the request to the URL Service.
3. The URL Service retrieves the URL record and verifies that it is active.
4. For an active record, the service returns an HTTP redirect with the destination URL.
5. For an unknown or inactive record, the service returns a documented non-redirect response.
6. The Analytics Service records the successful redirect without preventing a valid redirect response.

# Request Flow Diagram

```mermaid
sequenceDiagram
    participant Client
    participant Gateway as API Gateway
    participant URL as URL Service
    participant Database as URL H2 Database
    participant Analytics as Analytics Service

    Client->>Gateway: GET /{shortCode}
    Gateway->>URL: Forward resolution request
    URL->>Database: Find URL record by short code
    Database-->>URL: URL record
    alt Record exists and is active
        URL-->>Gateway: HTTP redirect and destination Location
        Gateway-->>Client: HTTP redirect
        URL-->>Analytics: Record successful redirect event
    else Record is missing or inactive
        URL-->>Gateway: Documented error response
        Gateway-->>Client: Error response
    end
```

# Folder Structure

The current repository contains documentation only. The planned structure is:

```text
.
|-- docs/
|   |-- Architecture.md
|   |-- BRD.md
|   `-- Decision-Log.md
|-- api-gateway/                 # Future Spring Boot gateway module
|-- url-service/                 # Future Spring Boot URL lifecycle module
|-- analytics-service/           # Future Spring Boot analytics module
|-- pom.xml                      # Future Maven parent build
`-- README.md
```

Each future service module will keep application code, tests, and configuration within its own boundary. The listed modules are architectural intent only and are not created by this documentation commit.

# Service Responsibilities

- API Gateway owns public routing and route-level request handling.
- URL Service owns validation, short-code generation, URL mapping persistence, and redirects.
- Analytics Service owns click counts and latest successful redirect timestamps.

# API Contracts

| Method and Path | Service | Purpose |
| --- | --- | --- |
| `POST /api/v1/urls` | URL Service | Create a shortened URL. |
| `GET /{shortCode}` | URL Service | Redirect an active short code. |
| `GET /api/v1/urls/{shortCode}/analytics` | Analytics Service | Retrieve click count and latest redirect time. |

Management endpoints return JSON. Redirect responses use an HTTP redirect and a `Location` header. Exact schemas and status codes will be added with implementation.

# Design Principles

- Use REST for synchronous, easily inspectable service communication.
- Make the API Gateway the only public routing boundary.
- Assign URL lifecycle ownership exclusively to the URL Service.
- Keep analytics isolated and deferred because it is not required for core URL shortening.
- Give each service independent data ownership; cross-service database access is prohibited.
- Use H2 to make local execution and tests self-contained.
- Keep all management endpoints versioned under `/api/v1` while preserving a concise redirect route.

# Scalability Considerations

- Redirect traffic is read-heavy; URL resolution can scale independently from management operations.
- Short-code records require a unique indexed lookup.
- Analytics may grow faster than URL creation and remains independently scalable.
- H2 supports local development only; production requires durable relational storage.

# Failure Scenarios

| Scenario | Expected Behavior | Mitigation |
| --- | --- | --- |
| Unknown or inactive short code | Return a documented non-redirect response. | Verify existence and active status before redirecting. |
| Invalid destination URL | Return a validation error. | Accept only absolute HTTP or HTTPS URLs. |
| Short-code collision | Retry code generation. | Enforce database uniqueness. |
| Analytics failure | Preserve a valid redirect response. | Keep analytics off the redirect success path. |
| H2 unavailable | Return a controlled server error. | Use repeatable local setup; replace H2 before production. |

# Tradeoffs

| Decision | Benefit | Cost |
| --- | --- | --- |
| Microservice boundaries | Clear ownership and independent future scaling. | More modules and HTTP hops than a modular monolith. |
| API Gateway | Single public edge and centralized routing. | Adds another runtime component. |
| H2 for local persistence | Zero external setup and fast tests. | Does not represent production durability or database behavior fully. |
| Synchronous REST | Simple debugging and familiar failure model. | Downstream availability can affect request latency. |
| Deferred analytics | Keeps the initial release focused. | No click visibility until the capability is implemented. |

## Risks

| Risk | Impact | Mitigation |
| --- | --- | --- |
| Short-code collision | Failed creation or incorrect mapping if uniqueness is weak. | Enforce a database uniqueness constraint and retry generation. |
| Gateway outage | Public API requests cannot reach services. | Keep gateway configuration minimal and test route behavior. |
| Redirect latency | User-facing redirects slow under load. | Keep resolution path limited to a single indexed lookup. |
| Analytics coupling | Event capture could delay redirects. | Treat analytics as non-blocking and isolate it from redirect success. |
| H2 differences | Production behavior may differ from local tests. | Add production database compatibility testing before production use. |
| Open redirect abuse | Short links can conceal undesirable destinations. | Restrict schemes to HTTP/HTTPS and consider reputation controls later. |
