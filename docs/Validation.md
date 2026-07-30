# Validation

## Functional Validation

- A valid HTTP or HTTPS destination creates a short URL with a Base62 short code.
- Duplicate destination creation returns a conflict response.
- A known short code resolves to a `302 Found` response and destination `Location` header.
- An unknown short code returns a structured not-found response.
- Redirect resolution increments the URL click counter and publishes an in-process redirect event.
- Analytics events persist short code, original URL, timestamp, IP address, and browser fields.
- Analytics summaries return total clicks and the latest redirect timestamp; no-event summaries return zero clicks and a null latest timestamp.

## API Validation

| Endpoint | Expected Validation |
| --- | --- |
| `POST /api/v1/urls` | Requires a nonblank absolute HTTP or HTTPS URL within the configured length. |
| `GET /{shortCode}` | Resolves an existing Base62 short code or returns `404`. |
| `POST /analytics/events` | Requires valid short code, destination, timestamp, IP address, and browser values. |
| `GET /api/v1/analytics/{code}` | Requires an eight-character Base62 code. |

## Testing Evidence

- URL Service: 24 tests passed in the latest focused module validation, including unit, MVC, JPA, and H2 integration coverage.
- Analytics Service: 16 tests passed in the latest focused module validation, including unit, MVC, JPA, and H2 integration coverage.
- API Gateway: Maven compilation and test lifecycle completed successfully; it currently has no test sources.
- Maven and JaCoCo commands are documented in `docs/Testing.md`.

## Security Review

- Destination URL validation allows only HTTP and HTTPS schemes.
- H2 console is disabled by default.
- Error handlers avoid returning unexpected exception details.
- Logs exclude full destination URLs, IP addresses, and browser values.
- Authentication, authorization, rate limiting, and destination reputation checks remain out of scope and are not represented as completed controls.

## Performance Review

- Short-code and original-URL lookup paths are indexed.
- Analytics uses count and latest-event queries rather than loading all event records for a summary.
- JPA open-session-in-view is disabled.
- Redirect counter mutation is suitable for local assignment load but is not an atomic high-concurrency implementation.

## Known Issues

- Redirect analytics events are in-process and are not durably delivered to Analytics Service.
- H2 with `create-drop` is not suitable for persistent production data.
- Raw IP and browser storage requires privacy policy, retention policy, and access control before production use.
- The project has no authentication, rate limiting, or abuse-prevention layer by design.