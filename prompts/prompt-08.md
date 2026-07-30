# Prompt 08

## Objective

Provide analytics retrieval by short code.

## Expected Output

`GET /api/v1/analytics/{code}` and a summary response with count and latest redirect time.

## AI Reasoning

Return a compact aggregate model and distinguish an empty analytics history from an invalid request.

## Acceptance Criteria

Valid codes return summary data; empty histories return zero clicks; invalid codes are rejected.