# ADR 003: Domain Classes in Entity Package

## Status

Accepted

## Context

After establishing the adapter package for infrastructure concerns (ADR 002), we needed to decide how to organize domain layer classes. The domain layer contained various types of classes:

- **Aggregate roots**: Table
- **Value objects**: Card, Hand, Board, PlayerCards, Suit, Number, PokerResult
- **Enums**: Category, TableStatus
- **Domain services**: PokerComparator

The question arose: Should we separate these into distinct packages (entity, value object, service) or group them together?

## Decision

We will place **all domain layer classes** in the `/{aggregate name}/entity` package, regardless of whether they are:
- Aggregate roots and entities
- Value objects
- Domain services
- Enums

This provides a single, unified domain package per aggregate.

## Rationale

### 1. Simplicity
- Single location for all domain logic
- No need to decide between entity/vo/service packages
- Easier navigation - developers know where all domain code lives

### 2. Cohesion
- Domain concepts naturally belong together
- All classes in the package share the same level of abstraction
- Reinforces that these are all "domain layer" concerns

### 3. Consistency with Clean Architecture
- Clean Architecture groups by layer, not by pattern
- The distinction between entities, value objects, and domain services is less important than their shared "domain layer" membership
- External layers (use cases, adapters) don't need to know internal domain organization

### 4. Reduced Package Complexity
- Avoids deep package hierarchies
- Fewer import paths to remember
- Less ceremony when creating new domain classes

## Options Considered

### Option 1: Single Entity Package (Chosen)
- `table/entity/` - All domain objects
- Pros: Simple, cohesive, easy to navigate
- Cons: Mixes different domain patterns in one package

### Option 2: Separate Packages by Domain Concept
- `table/entity/` - Aggregate roots only
- `table/vo/` - Value objects
- `table/service/` - Domain services (risk of confusion with usecase services)
- Pros: Clear separation of domain patterns
- Cons: More complex, harder to navigate, potential confusion

### Option 3: Mixed Approach
- `table/` - Value objects and enums at root
- `table/entity/` - Aggregate roots
- `table/service/` - Domain services
- Pros: Some organization without full separation
- Cons: Inconsistent, arbitrary decisions about placement

## Consequences

### Positive
- Clear, simple package structure
- All domain logic in one discoverable location
- No ambiguity about where to place new domain classes
- Aligns with Clean Architecture's layer-based organization
- Reduces cognitive load for developers

### Negative
- Large number of classes in a single package as domain grows
- Less explicit distinction between entities, value objects, and domain services
- May not follow traditional DDD package organization patterns

### Neutral
- Developers must understand that "entity package" means "domain layer," not just aggregate roots
- Package name could be misleading to those expecting only entities
- Could consider renaming to `table/domain` in the future if clarity becomes an issue

## Package Structure

```
table/
  ├── entity/          # All domain classes
  │   ├── Table.java              (aggregate root)
  │   ├── Card.java               (value object)
  │   ├── Hand.java               (value object)
  │   ├── Board.java              (value object)
  │   ├── PlayerCards.java        (value object)
  │   ├── PokerComparator.java    (domain service)
  │   ├── Category.java           (enum)
  │   ├── TableStatus.java        (enum)
  │   ├── Suit.java               (enum)
  │   ├── Number.java             (enum)
  │   └── PokerResult.java        (value object)
  ├── usecase/         # Use case services and repository interfaces
  │   ├── SettleTableService.java
  │   └── TableRepository.java
  └── adapter/         # Controllers and repository implementations
      └── TableRepositoryInMemory.java
```

## Related Decisions

- ADR 001: Domain service placement (establishes that domain services exist)
- ADR 002: Adapter package for repository implementations
- Package naming conventions documented in `.claude/CLAUDE.md`

## Notes

If the entity package becomes too large (>20 classes), we may revisit this decision and consider sub-packages within entity/ such as:
- `entity/core/` - Aggregate roots
- `entity/vo/` - Value objects
- `entity/service/` - Domain services

However, such reorganization should only occur when complexity justifies it, not prematurely.