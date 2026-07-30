# Prompt 09

## Objective

Standardize URL Service validation and error handling.

## Expected Output

Error response DTO, custom exceptions, global advice, and `400`, `404`, `409`, and `500` mappings.

## AI Reasoning

Centralize error mapping to keep controllers thin and avoid leaking implementation details.

## Acceptance Criteria

Validation, missing mappings, duplicates, and unexpected failures produce structured responses.