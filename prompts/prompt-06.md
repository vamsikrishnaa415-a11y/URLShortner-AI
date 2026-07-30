# Prompt 06

## Objective

Implement short-code lookup and redirect behavior.

## Expected Output

`GET /{shortCode}`, `302` redirect, click increment, and redirect event publication.

## AI Reasoning

Keep the redirect path in URL Service, expose a standard location header, and isolate analytics notification.

## Acceptance Criteria

Known codes redirect, unknown codes return not found, and redirect tests verify click/event behavior.