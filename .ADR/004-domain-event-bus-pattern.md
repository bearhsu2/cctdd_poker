# ADR 004: Domain Event Bus Pattern

## Status

Accepted

## Context

As our application grows, we need a way to handle side effects and cross-cutting concerns when domain events occur. For example, when a table is settled, we may need to:
- Notify players of the results
- Update leaderboards
- Record statistics
- Trigger reward calculations

Without a proper event mechanism, this logic would be embedded directly in use case services, leading to:
- Tight coupling between core domain logic and side effects
- Violation of Single Responsibility Principle
- Difficulty in adding new features without modifying existing code
- Poor testability

The question arose: How should we handle domain events in a way that maintains clean architecture boundaries and allows for extensibility?

## Decision

We will implement a **Domain Event Bus Pattern** with the following characteristics:

1. **Aggregates create their own domain events**
   - When aggregate state changes, the aggregate creates appropriate domain events
   - Events are stored in the aggregate until flushed
   - Events contain all necessary data for handlers to process them

2. **Event Bus for publishing and subscription**
   - `DomainEventBus` interface defines `register()` and `publish()` operations
   - `DomainEventBusInMemory` provides in-memory implementation
   - `DomainEventHandler` interface allows multiple handlers to subscribe

3. **Use case services orchestrate event publishing**
   - Services flush events from aggregates after state changes
   - Services publish events through the event bus
   - Event bus notifies all registered handlers

4. **Atomic flush operation**
   - `flushDomainEvents()` retrieves and clears events in single operation
   - Prevents forgetting to clear events after publishing
   - Cleaner API with fewer method calls

## Rationale

### 1. Domain-Driven Design Principles
- Aggregates are responsible for their own domain events
- Events are first-class domain concepts
- Separates "what happened" (events) from "what to do about it" (handlers)

### 2. Separation of Concerns
- Core domain logic (aggregate state changes) separated from side effects (event handlers)
- Use case services focus on orchestration, not implementation of side effects
- New features can be added by registering new handlers without modifying existing code

### 3. Testability
- Aggregates can be tested independently by verifying they create correct events
- Use case services can be tested with dummy event handlers
- Event handlers can be tested in isolation
- No need to mock multiple dependencies in use case service tests

### 4. Extensibility
- New handlers can be added without modifying existing code (Open/Closed Principle)
- Event-driven architecture allows for future features like:
  - Event sourcing
  - Audit logging
  - Real-time notifications
  - Analytics and reporting

### 5. Clean Architecture
- Domain layer (aggregates, events) has no dependencies on infrastructure
- Event bus interface allows for different implementations (in-memory, message queue, etc.)
- Handlers can be in application or infrastructure layers

## Consequences

### Positive
- Clean separation between domain logic and side effects
- Easy to add new features by adding new event handlers
- Improved testability at all layers
- Domain events serve as documentation of what happens in the system
- Foundation for future event sourcing or CQRS if needed
- No coupling between core domain and specific handlers

### Negative
- Additional complexity with event bus infrastructure
- More classes and interfaces to manage
- Potential for event handlers to have hidden dependencies
- Need to ensure events are flushed and published correctly
- Debugging can be harder due to indirect handler invocation

### Neutral
- Developers must understand when to use events vs. direct calls
- Need to design events with appropriate data
- Event ordering may matter in some cases

## Implementation Example

```java
// Aggregate creates events
public class Table {
    private List<Object> domainEvents = new ArrayList<>();

    public void settle() {
        this.status = TableStatus.SETTLED;
        this.version++;
        this.domainEvents.add(new TableSettledEvent(id));
    }

    public List<Object> flushDomainEvents() {
        List<Object> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }
}

// Use case service publishes events
public class SettleTableService {
    public void settle(String tableId) {
        Table table = tableRepository.findById(tableId);
        table.settle();
        tableRepository.save(table);
        for (Object event : table.flushDomainEvents()) {
            domainEventBus.publish(event);
        }
    }
}

// Handler reacts to events
public class NotificationHandler implements DomainEventHandler {
    public void handle(Object event) {
        if (event instanceof TableSettledEvent) {
            // Send notifications
        }
    }
}
```

## Alternatives Considered

### 1. Direct method calls in use case service
- Rejected: Creates tight coupling and violates SRP
- Hard to extend without modifying existing code

### 2. Observer pattern on aggregates
- Rejected: Aggregates shouldn't know about infrastructure concerns
- Violates Clean Architecture boundaries

### 3. Spring Events (@EventListener)
- Rejected: Creates dependency on Spring framework in domain layer
- Makes testing more complex
- Less control over event ordering and error handling

## Future Considerations

- May evolve to support async event handling
- Could be extended to support event sourcing
- May add event store for audit trail
- Could integrate with external message queues (Kafka, RabbitMQ)

## Notes

This decision aligns with:
- DDD principles of aggregate responsibility
- Clean Architecture separation of concerns
- Project's existing pattern of interface-based abstractions