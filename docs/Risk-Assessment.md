# Risk Assessment

## Technical Risks

| Risk | Impact | Mitigation | Residual Risk |
| --- | --- | --- | --- |
| Short-code collision | Failed or conflicting URL creation. | Base62 `SecureRandom`, retry attempts, and database uniqueness constraints. | Low for assignment traffic; retry policy is finite. |
| Concurrent redirect increments | Click totals can lose updates. | Documented limitation; use atomic database updates before production scale. | Medium under concurrent traffic. |
| Non-durable analytics event | Redirect analytics can be lost between services. | Keep redirect independent; introduce durable post-commit delivery later. | High for analytics completeness. |
| H2 lifecycle | Data is transient and database behavior differs from production. | Restrict H2 to local/test execution. | High for production deployment. |
| Large analytics volume | Storage and query cost increase over time. | Indexed summary queries; defer retention and archival policy. | Medium. |

## Business Risks

| Risk | Impact | Mitigation |
| --- | --- | --- |
| Destination abuse | Short URLs can mask undesirable destinations. | Validate scheme; introduce reputation and allow/block controls later. |
| Privacy handling | IP and browser data can create compliance obligations. | Define consent, retention, anonymization, and access policies before real-user use. |
| Duplicate URL policy | Clients may expect multiple aliases for one destination. | Document the one-mapping-per-destination conflict behavior. |
| Assignment scope growth | Additional platform requirements could delay delivery. | Preserve explicit exclusions and incremental commits. |

## Failure Scenarios

| Scenario | Expected Behavior | Current Handling |
| --- | --- | --- |
| Invalid URL request | `400 Bad Request`. | DTO validation and structured error response. |
| Duplicate destination | `409 Conflict`. | Domain exception and persistence-conflict mapping. |
| Unknown short code | `404 Not Found`. | URL lookup exception handler. |
| Unexpected server exception | `500 Internal Server Error`. | Sanitized response and server-side logging. |
| Analytics summary without events | `200 OK` with zero clicks. | Count/latest repository queries. |
| Analytics receiver unavailable | Redirect still completes, but delivery is absent. | Known limitation; no durable integration exists. |

## Mitigations

- Preserve thin controllers, constructor injection, validation boundaries, and domain-specific exceptions.
- Keep repository query methods focused on required response data.
- Use isolated H2 test profiles and multiple test layers.
- Maintain code-review and validation records for follow-up work.
- Prioritize production database, durable analytics delivery, atomic counters, privacy controls, and abuse controls before production deployment.