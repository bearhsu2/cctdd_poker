# ADR 005: Common Package Structure for Cross-Cutting Concerns

## Status

Accepted

## Context

As the application grows, we need infrastructure components that serve multiple aggregates rather than belonging to a specific domain concept. Examples include:
- Domain Event Bus - used by all aggregates to publish domain events
- Shared value objects - used across multiple bounded contexts
- Common utilities - cross-cutting technical concerns

The question arose: Where should these cross-cutting components be placed in our package structure?

Options considered:
1. Root package (`idv.kuma.poker`) - Simple but doesn't scale, becomes cluttered
2. `infrastructure` package - Doesn't distinguish between interfaces (domain) and implementations (adapter)
3. `shared` or `common` package - Dedicated space for cross-cutting concerns
4. Duplicate in each aggregate package - Creates unnecessary duplication

## Decision

We will use a `common` package structure that mirrors the same layering pattern as aggregate-specific packages:

```
idv.kuma.poker.common/
  ├── usecase/         (interfaces - application/domain layer)
  │   ├── DomainEventBus.java
  │   └── DomainEventHandler.java
  └── adapter/         (implementations - infrastructure layer)
      └── DomainEventBusInMemory.java
```

This structure:
- Uses `common` as the package name (preferred over `shared`)
- Maintains the same `usecase` and `adapter` separation as aggregate packages
- Follows Clean Architecture layering principles
- Keeps interfaces in inner layers, implementations in outer layers

## Rationale

### 1. Consistency with Existing Architecture
- Aggregate packages follow pattern: `{aggregate}/usecase` and `{aggregate}/adapter`
- Common package follows same pattern: `common/usecase` and `common/adapter`
- Developers immediately understand where to find interfaces vs implementations
- No special rules to remember for cross-cutting concerns

### 2. Clean Architecture Compliance
- Interfaces (`DomainEventBus`, `DomainEventHandler`) in `usecase` layer
- Implementations (`DomainEventBusInMemory`) in `adapter` layer
- Domain layer has no dependency on infrastructure
- Dependency rule is preserved: inner layers don't depend on outer layers

### 3. Scalability
- Clear place to put future cross-cutting concerns
- Won't clutter root package as project grows
- Easy to find all shared infrastructure in one place
- Can add entity, service subdirectories if needed later

### 4. Testability
- Test code can depend on interfaces from `common.usecase`
- Test implementations (like `DummyDomainEventHandler`) clearly separate
- Easy to swap implementations for testing vs production

### 5. Package Naming
- "common" is more conventional than "shared" in Java projects
- Clearly indicates cross-cutting nature
- Doesn't conflict with Spring's "commons" or other libraries

## Consequences

### Positive
- Consistent package structure across entire codebase
- Clear separation between interfaces and implementations
- Easy to find cross-cutting infrastructure
- Clean Architecture principles maintained
- New developers can navigate by pattern recognition
- Future cross-cutting concerns have clear home

### Negative
- Slightly deeper package nesting
- Could be tempting to overuse for things that should be in aggregates
- Need to decide when something is "common" vs aggregate-specific

### Neutral
- Developers must understand when to use `common` vs aggregate packages
- May need additional subdirectories (entity, service) as common layer grows

## Examples of What Belongs in Common

### Should go in common:
- Domain event infrastructure (bus, handlers)
- Cross-aggregate value objects (e.g., Money, Email)
- Technical utilities used by multiple aggregates
- Shared interfaces for external systems

### Should NOT go in common:
- Aggregate-specific domain logic
- Value objects used by single aggregate
- Aggregate-specific repositories
- Aggregate-specific domain events (e.g., `TableSettledEvent` stays in `table.entity`)

## Implementation Pattern

When adding new cross-cutting infrastructure:

1. **Interface/Contract** → `common.usecase`
   - Defines behavior
   - No dependencies on infrastructure
   - Can be used by domain/application layer

2. **Implementation** → `common.adapter`
   - Implements interface from `common.usecase`
   - May depend on external libraries/frameworks
   - Swappable without affecting domain layer

3. **Tests** → Test package mirrors structure
   - Mock implementations for testing
   - Integration tests for adapter implementations

## Future Considerations

- May add `common.entity` for truly cross-cutting value objects
- Could add `common.service` for cross-aggregate domain services
- Might evolve into multiple "common" contexts if system grows large
- Consider bounded context patterns if common package grows too large

## Related ADRs

- ADR 002: Adapter Package for Repository Implementations - Established adapter pattern
- ADR 004: Domain Event Bus Pattern - Primary inhabitant of common package

## Notes

This decision reinforces our commitment to Clean Architecture and consistent package organization. The `common` package is not a catch-all for unrelated code, but a structured space for genuinely cross-cutting infrastructure following the same architectural principles as the rest of the codebase.