# Event Bus Design Rules

## Overview

The event bus is a mechanism for decoupling components through asynchronous event-driven communication. It allows
aggregates to publish domain events and event handlers to react to them without direct dependencies.

## Core Principles

- Events are facts about things that have happened in the domain
    - Keep events small and focused
- Event publishers should not know about event handlers
- Event handlers should not modify the state of the publishing aggregate
- Events flow in one direction: from aggregates to handlers
- Use events to maintain eventual consistency between aggregates

## DomainEvent

### Responsibility

- Represent significant occurrences in the domain
- Carry data relevant to the event
- Be immutable
- Be serializable (if needed for persistence or messaging)

### Naming Convention

- All domain events must implement the `DomainEvent` interface
- Events should be simple data carriers with no behavior
- Event names should be in past tense (e.g., `TableSettled`, `GameHistoryAdded`)

### Example

```java
public record TableSettled(String tableId, List<PlayerResult> results) implements DomainEvent {
}
```

## DomainEventBus

### Responsibility

- Register event handlers for specific event types
- Publish events to all registered handlers
- Ensure handlers are invoked asynchronously (if needed)

### Interface

- Define a `DomainEventBus` interface with methods to register handlers and publish events
- In the `/common/usecase` package

### Implementation

- Provide at least one implementation of the `DomainEventBus` interface
- In the `/common/adapter` package
    - For example, an in-memory implementation (`DomainEventBusInMemory`) provides simple feature.
- Use constructor injection to register handlers
- Multiple handlers can subscribe to the same event type

### Example

```java
public interface DomainEventBus {
    void publish(DomainEvent event);
}

@Component
public class DomainEventBusInMemory implements DomainEventBus {
    private List<DomainEventHandler> handlers;

    public DomainEventBusInMemory(DomainEventHandler... handlers) {
        this.handlers = Arrays.asList(handlers);
    }

    @Override
    public void publish(DomainEvent event) {
        for (DomainEventHandler handler : handlers) {
            handler.handle(event);
        }
    }
}
```

## Event Handlers

### Responsibility

- React to domain events
- Coordinate application-level workflows
- Should not contain domain logic (delegate to domain services or aggregates)

### Naming Convention

- Name handlers after their primary action: `{Action}{EventName}Handler`
- Example: `AddGameHistoryEventHandler` (handles `TableSettled` event to add game history)

### Package Location

- Place event handlers in the `/{aggregate name}/adapter` package
- Event handlers are infrastructure concerns, not domain logic
- Domain logic should reside in domain services or aggregates

## Example

```java

@Component
@RequiredArgsConstructor
public class AddGameHistoryEventHandler implements DomainEventHandler {
    private final GameHistoryRepository gameHistoryRepository;
    private final AddGameHistoryService addGameHistoryService;

    public void handle(TableSettled event) {
        addGameHistoryService.execute(event.tableId(), event.results());
    }
}
```