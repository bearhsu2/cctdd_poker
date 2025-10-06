# ADR 001: Domain Service Placement in Clean Architecture

## Status

Accepted

## Context

In our poker application, we have a `PokerComparator` that contains the logic for comparing poker hands. The question arose: should this logic be placed as a private method in `SettleTableService` (use case/application service) or kept as a separate domain service?

The discussion centered around:
- Whether aggregates should call domain services directly
- Whether use case services should call domain services
- Whether domain logic should be embedded in use case services as private methods

## Decision

We will keep `PokerComparator` as a separate domain service and have it called by use case services (like `SettleTableService`), not by aggregates.

Domain services should be:
- Called by use case/application services
- NOT called directly by aggregates
- NOT embedded as private methods within use case services

## Rationale

### 1. Single Responsibility Principle
- Use case services should focus on orchestrating workflows and coordinating domain objects
- Domain services should encapsulate specific domain logic
- Mixing poker comparison logic into `SettleTableService` would blur these responsibilities

### 2. Reusability
- Poker hand comparison logic may be needed in multiple contexts:
  - Settling tables after a game
  - Displaying hand rankings during play
  - Tie-breaking in tournaments
  - Showing hand strength indicators to players
- A separate domain service can be injected wherever needed

### 3. Testability
- Domain services can be tested independently with focused unit tests
- Embedding logic as private methods forces testing through the use case service's integration tests
- Isolated testing leads to better test coverage and clearer failure messages

### 4. Domain Clarity
- Poker hand comparison is core domain knowledge
- Making it explicit as a separate service improves code readability
- Domain concepts should not be hidden as implementation details

### 5. Clean Architecture Principles
- Aggregates maintain their own invariants and state
- Use cases orchestrate operations
- Domain services handle domain logic that spans multiple aggregates or doesn't naturally fit within one
- This separation maintains proper architectural boundaries

## Consequences

### Positive
- Clear separation of concerns between orchestration and domain logic
- Improved testability and maintainability
- Domain logic is discoverable and reusable
- Easier to modify comparison logic without affecting workflow logic

### Negative
- Slightly more files and classes to manage
- Requires dependency injection setup

### Neutral
- Developers must understand when to create domain services vs. embedding logic in use cases
- Need to maintain consistency in this approach across the codebase

## Example Flow

```
Controller → Use Case Service → Domain Service + Aggregates
              (SettleTableService)  (PokerComparator)
```

## Notes

This decision aligns with the project's Clean Architecture principles as documented in `.claude/CLAUDE.md`, where:
- Services handle flows and operate on aggregates
- Aggregates encapsulate business logic related to their own state
- Domain services handle cross-cutting domain concerns