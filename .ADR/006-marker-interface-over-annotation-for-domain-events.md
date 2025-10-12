# 006: Marker Interface over Annotation for Domain Events

## Status

Accepted

## Context

The `DomainEvent` interface in our codebase is currently an empty marker interface with no methods or fields. This raised the question of whether we should use an annotation (`@DomainEvent`) instead of a marker interface.

Both approaches can identify domain events in the system, but they have different trade-offs:

**Marker Interface Approach:**
- Provides compile-time type safety in `DomainEventBus.publish(DomainEvent event)`
- Enables polymorphic usage and `instanceof` checks
- Uses the single interface inheritance slot
- Clear type identity: events "are" domain events

**Annotation Approach:**
- More flexible - doesn't use inheritance slot
- Can carry metadata (e.g., `@DomainEvent(async = true)`)
- Better for cross-cutting concerns
- Loses compile-time type safety - would require `publish(Object event)`
- Requires runtime validation instead of compiler enforcement

## Decision

We will use a marker interface for `DomainEvent` rather than an annotation.

The primary reason is that `DomainEvent` represents type identity, not metadata or behavior configuration. Domain events are not just classes that need special treatment - they are a fundamental type in our domain model.

The compile-time type safety provided by the interface is valuable:
- `DomainEventBus.publish(DomainEvent event)` prevents accidental publishing of arbitrary objects
- IDEs can provide better autocomplete and refactoring support
- The type system enforces that only domain events can be published

## Consequences

### Positive

- Compile-time type safety prevents runtime errors from publishing non-events
- Clear type hierarchy makes it easy to find all domain events in the codebase
- Better IDE support for navigation and refactoring
- Explicit opt-in through `implements DomainEvent` makes architectural intent clear
- Prevents accidental misuse of the event bus

### Negative

- Domain events cannot extend other classes (single inheritance limitation in Java)
- Cannot add metadata to the marker without modifying the interface
- Less flexible than annotations for cross-cutting concerns

### Notes

- This decision does not preclude using annotations alongside the marker interface if we need to add metadata in the future (e.g., `@EventMetadata`)
- The current use of `instanceof` checks in handlers (e.g., `AddGameHistoryEventHandler`) is a separate concern related to handler filtering, not event identification