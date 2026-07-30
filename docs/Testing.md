# Testing

## Test Profiles

Both services use the `test` Spring profile with isolated in-memory H2 databases. The test profiles are defined in each module under `src/test/resources/application-test.yml`.

Mockito unit tests use strict stubbing through `@MockitoSettings(strictness = Strictness.STRICT_STUBS)` to detect unused or incorrectly configured mocks.

## Commands

Run the complete suite and create JaCoCo reports:

```powershell
mvn clean verify
```

Run one service suite:

```powershell
mvn -pl url-service clean test
mvn -pl analytics-service clean test
```

Open the HTML coverage report for either service after `verify`:

```text
url-service/target/site/jacoco/index.html
analytics-service/target/site/jacoco/index.html
```

## Coverage Target

Maintain at least 90% line coverage for URL creation, redirect, analytics aggregation, validation, and exception-handling paths. Review JaCoCo reports before merging changes and add tests for any newly introduced branches.