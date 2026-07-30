# Prompt 05

## Objective

Harden URL creation with Base62 codes and duplicate handling.

## Expected Output

Secure random generator, repository lookup, validation, duplicate behavior, and focused tests.

## AI Reasoning

Use `SecureRandom` and persistence uniqueness instead of predictable identifiers; make duplicate behavior explicit.

## Acceptance Criteria

New mappings receive Base62 codes, duplicate destinations return a defined result, and service tests pass.