# Project Overview

AI-Assisted Software Engineering System - URL Shortener

This repository contains an interview assignment for an AI-assisted URL Shortener. The target system will create compact links, redirect visitors to original destinations, and provide basic click analytics. Development is incremental: each Git commit introduces a small, reviewable slice of the system.

Commit 1 establishes the business requirements and repository conventions only. No application code or deployable services are included yet.

# Business Problem

Long URLs are cumbersome to share and difficult to measure. Users need short, unique links that redirect reliably and offer a basic view of how often each link is used.

# Solution Overview

The planned solution is a simple REST-based URL Shortener. It will validate destination URLs, generate short codes, redirect active links, and retain basic click metrics. The design favors clean ownership boundaries and avoids unnecessary enterprise components.

# Technology Stack

- Java 17
- Spring Boot 3.x
- Maven
- Microservices
- REST APIs
- H2 Database
- JUnit 5
- Mockito

# High Level Architecture

The target design is a small set of independently deployable Spring Boot microservices communicating through REST APIs. The initial implementation will keep service boundaries intentionally simple:

- An API Gateway will provide a single public entry point and route requests.
- A URL Service will create shortened URLs and resolve redirects.
- An Analytics Service will collect and expose basic click metrics.
- Each service will own its H2-backed data access concerns during local development.
- REST APIs will be versioned and documented as they are introduced.

The architecture avoids distributed infrastructure in the assignment's initial scope. There is no message broker, distributed cache, service discovery platform, container orchestration, or external identity provider.

# Repository Structure

```text
.
|-- docs/
|   |-- Architecture.md        # Target architecture documentation
|   |-- BRD.md                 # Business requirements document
|   `-- Decision-Log.md        # Technology decision records
|-- README.md                  # Project overview
`-- .gitignore                 # Local build and IDE exclusions
```

Future commits will introduce Maven modules, Spring Boot services, tests, and supporting documentation while keeping their ownership boundaries explicit.

# Planned Git Commit Strategy

1. Requirements and repository documentation.
2. Architecture and technology decisions.
3. Maven and Spring Boot project foundations.
4. URL domain model and persistence layer.
5. URL creation and redirect REST APIs.
6. Click analytics API and integration.
7. Automated tests, validation hardening, and operational documentation.

# How To Run (placeholder)

Commit 1 contains documentation only and has no executable application.

Future implementation commits will document Maven commands, service ports, profiles, and H2 configuration.

# API Summary (placeholder)

The planned REST API surface includes:

- `POST /api/v1/urls` to create a short URL for a valid destination URL.
- `GET /{shortCode}` to resolve a short code and redirect to its destination.
- `GET /api/v1/urls/{shortCode}/analytics` to retrieve basic click analytics.

Endpoint contracts, status codes, validation rules, and examples will be added alongside their implementation.

# Development Roadmap

1. Establish requirements, architecture, and decision records.
2. Create the future Maven and Spring Boot foundations.
3. Implement URL creation, validation, persistence, and redirect behavior.
4. Add basic analytics collection and retrieval.
5. Expand test coverage and refine error handling.
6. Document operational choices required for a production deployment.
