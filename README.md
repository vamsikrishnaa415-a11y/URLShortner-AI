# AI-Assisted Software Engineering System - URL Shortener

## Project Overview

This repository contains an interview assignment for an AI-assisted URL shortener. The system will provide a reliable REST API for creating compact URLs, resolving short codes to their original destinations, and managing link lifecycle data. Development is intentionally incremental: each Git commit introduces a small, reviewable slice of the system.

Commit 1 establishes the business requirements and repository conventions only. No application code or deployable services are included yet.

## Architecture Overview

The target design is a small set of independently deployable Spring Boot microservices communicating through REST APIs. The initial implementation will keep service boundaries intentionally simple:

- A URL management service will create, retrieve, update, and deactivate shortened URLs.
- A redirect service will resolve a short code and issue the appropriate HTTP redirect.
- Each service will own its H2-backed data access concerns during local development.
- REST APIs will be versioned and documented as they are introduced.

The architecture avoids distributed infrastructure in the assignment's initial scope. There is no message broker, distributed cache, service discovery platform, container orchestration, or external identity provider.

## Technology Stack

- Java 17
- Spring Boot 3.x
- Maven
- Spring Web REST APIs
- H2 Database
- JUnit 5
- Mockito

## Folder Structure

```text
.
|-- docs/
|   `-- BRD.md                 # Business requirements document
|-- README.md                  # Project and development overview
`-- .gitignore                 # Local build and IDE exclusions
```

Future commits will introduce Maven modules, Spring Boot services, tests, and supporting documentation while keeping their ownership boundaries explicit.

## How to Run

Commit 1 contains documentation only and has no executable application.

Once Maven-based Spring Boot services are introduced, each service will be started from its module directory with:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell, use:

```powershell
.\mvnw.cmd spring-boot:run
```

The exact service ports, profiles, and database configuration will be documented with the implementation commits that add them.

## API Overview

The planned REST API surface includes:

- `POST /api/v1/urls` to create a short URL for a valid destination URL.
- `GET /{shortCode}` to resolve a short code and redirect to its destination.
- `GET /api/v1/urls/{shortCode}` to retrieve URL metadata.
- `PATCH /api/v1/urls/{shortCode}` to update supported metadata or activation state.

Endpoint contracts, status codes, validation rules, and examples will be added alongside their implementation.

## Git Commit Strategy

Commits are small, focused, and independently reviewable. The intended sequence is:

1. Requirements and repository documentation.
2. Maven and Spring Boot project foundations.
3. URL domain model and persistence layer.
4. URL creation and management REST API.
5. Redirect resolution API.
6. Automated tests, validation hardening, and operational documentation.

Each commit should include only the files necessary for its stated purpose and should keep the build and tests passing once application code exists.
