# AI Usage

## Where AI Was Used

AI assistance was used as an engineering collaborator for requirements structuring, architecture documentation, Maven skeleton generation, service-layer implementation, test generation, error-handling design, production-readiness review, and documentation drafting. Each change was made in small, reviewable increments and validated against the workspace.

## Where AI Suggestions Were Rejected

- Distributed infrastructure such as Kafka, Redis, RabbitMQ, Eureka, Docker, Kubernetes, OAuth, JWT, Flyway, and Liquibase was not introduced because it conflicts with the assignment constraints.
- Testcontainers were not added because H2 is the required local test database and the task explicitly excluded them.
- A full production platform was not simulated through containers or managed services.
- The in-process redirect event was not represented as durable cross-service delivery. It is documented as a limitation rather than overstated as a completed integration.

## Engineering Decisions Taken by the Developer

- Keep the architecture to three bounded components: gateway, URL Service, and Analytics Service.
- Use Base62 generation backed by `SecureRandom` for short codes.
- Treat duplicate destination URLs as conflicts to preserve a single mapping per destination.
- Use explicit path-variable names so endpoint binding does not depend on compiler parameter metadata.
- Use repository count and latest-event queries for analytics summaries rather than materializing complete event lists.
- Disable the H2 console and JPA open-session-in-view in default service configuration.

## Manual Improvements

- Reviewed generated code for endpoint drift and aligned gateway routes with versioned APIs.
- Added repository indexes for redirect and analytics query paths.
- Added SLF4J logs that record short codes and aggregate counts without logging destinations, IP addresses, or browser data.
- Added structured validation and error responses.
- Added JavaDoc to public production contracts and a code-review record for constraints that remain unresolved.

## Traceability

The `prompts/` directory records the thirteen development phases as concise prompt summaries. Existing documentation provides complementary traceability:

- `docs/BRD.md` defines business and functional requirements.
- `docs/Architecture.md` defines service boundaries and request flow.
- `docs/Decision-Log.md` explains technology and scope choices.
- `docs/Testing.md` documents test execution and coverage intent.
- `docs/Code-Review.md` documents readiness findings and limitations.

## Validation

Generated changes were checked through focused Maven module builds and tests. The hardened URL and Analytics services passed their respective test suites, and the API Gateway compiled successfully. Automated validation covered unit, MVC, JPA repository, and H2-backed integration layers.