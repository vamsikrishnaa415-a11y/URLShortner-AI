# Engineering Summary

## Project Overview

AI URL Shortener is a Java 17, Spring Boot microservice assignment that creates unique short URLs, resolves redirects, and records basic click analytics. The repository is organized as Maven modules for an API Gateway, URL Service, and Analytics Service. H2 supports local execution and automated tests.

## Requirement Understanding

The core requirements are URL creation, deterministic duplicate handling, redirect resolution, click recording, analytics retrieval, validation, and consistent failure responses. The assignment also requires incremental Git-oriented delivery, maintainability, testability, and a deliberately constrained platform without container orchestration, messaging brokers, distributed caches, service discovery, or an authentication layer.

## Task Decomposition

1. Define business requirements, architecture, and engineering decisions.
2. Establish a parent Maven build and independently runnable service modules.
3. Implement URL persistence, Base62 code generation, creation, validation, and duplicate detection.
4. Implement redirect resolution, click counting, and an in-process redirect event.
5. Implement analytics event storage and short-code summaries.
6. Add structured error handling, test profiles, unit tests, MockMvc tests, repository tests, and H2 integration tests.
7. Harden configuration, query paths, logging, documentation, and engineering traceability.

## Architecture Decisions

- Java 17 and Spring Boot 3.5.x provide a current LTS-compatible Java baseline.
- Maven manages the multi-module build and dependency versions.
- Spring Cloud Gateway provides the public routing boundary.
- URL Service owns URL mappings and redirect behavior; Analytics Service owns analytics records.
- H2 is limited to local development and test execution.
- REST is used for visible service boundaries; no broker or external platform is required.
- Short codes use `SecureRandom` Base62 generation and persistence-level uniqueness.

## Implementation Summary

- URL creation is exposed through `POST /api/v1/urls`.
- Redirect resolution is exposed through `GET /{shortCode}` and responds with `302 Found` plus a `Location` header.
- Analytics event ingestion is exposed through `POST /analytics/events`.
- Analytics summaries are exposed through `GET /api/v1/analytics/{code}`.
- DTO validation constrains URLs, short codes, event fields, and field sizes.
- Error responses distinguish validation, not-found, conflict, and unexpected failures.
- Analytics summaries use count and latest-event queries instead of loading all matching rows.

## Validation Strategy

Validation is applied at the API boundary using Jakarta Bean Validation. URL requests require absolute HTTP or HTTPS values. Short codes are constrained to the eight-character Base62 format. Analytics events validate short code, destination, timestamp, IP address length, and browser length. Global exception handlers translate validation and domain failures into structured HTTP responses.

## Testing Strategy

The suite combines strict Mockito unit tests, MockMvc controller tests, `@DataJpaTest` repository tests, and `@SpringBootTest` plus `@AutoConfigureMockMvc` integration tests. Each service uses an isolated H2 `test` profile. JaCoCo reporting is configured through Maven and the target is at least 90% coverage for the core URL, redirect, analytics, validation, and exception paths.

## Engineering Tradeoffs

- H2 and `create-drop` maximize local simplicity but provide no durable production data strategy.
- Destination URLs are unique, so a duplicate creation request returns `409 Conflict` rather than creating multiple short codes for one URL.
- Analytics aggregation prioritizes lightweight summary queries over exposing raw event histories.
- Logs omit destination URLs, IP addresses, and browser values to reduce sensitive-data exposure.

## Risks

- In-process analytics events are not durable across service boundaries.
- Entity-based click increments may lose updates under concurrent redirect traffic.
- Raw analytics metadata requires privacy, retention, and access-control policy before real-user deployment.
- H2 behavior and index definitions must be verified against the eventual production database.

## Assumptions

- The project is evaluated primarily in a local development environment.
- Public HTTP and HTTPS destinations are acceptable inputs.
- Basic analytics means click count and latest redirect time.
- Authentication, authorization, rate limiting, and advanced analytics are intentionally outside the assignment scope.

## Limitations

- No durable analytics delivery exists between URL Service and Analytics Service.
- No production database or schema migration mechanism is included.
- No API contract publication, authentication, rate limiting, or destination reputation checks are implemented.
- Gateway routes use local defaults and environment-variable overrides; service discovery is intentionally absent.

## Future Improvements

- Introduce durable post-commit analytics delivery compatible with the platform constraints.
- Replace H2 with a production relational database and controlled schema evolution.
- Use atomic database updates for redirect counters.
- Add OpenAPI contracts, API compatibility checks, privacy controls, and operational runbooks.