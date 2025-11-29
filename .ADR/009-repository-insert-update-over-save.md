# ADR 009: Repository Method Design - Save vs Insert/Update

## Status

Accepted

## Context

In our poker application, repositories need methods for persisting aggregates/entities. There are two common design patterns:

1. **Generic save()**: A single method that handles both inserting new entities and updating existing ones (common in JPA and similar frameworks)
2. **Explicit insert()/update()**: Separate methods with clear semantics about whether the operation is creating or modifying an entity

The question arose: should we mandate one approach for all repositories, or allow developers to choose based on their specific needs?

## Decision

Developers may choose between the `save()` design and the `insert()/update()` design based on what makes the most sense for their specific repository and use case.

Both approaches are acceptable:
- Use `save()` when the caller doesn't need to distinguish between insert and update operations
- Use separate `insert()` and `update()` when explicit operation semantics are important

## Rationale

### When to Use Insert/Update Design

Use separate `insert()` and `update()` methods when:

1. **Explicit Intent Matters**
   - The caller's intent should be immediately clear from the method name
   - Creating an entity is conceptually different from modifying one in your domain
   - You want fail-fast behavior (insert fails if exists, update fails if doesn't exist)

2. **Domain Alignment**
   - Factory methods like `Aggregate.create()` naturally align with repository `insert()`
   - Aggregate state-changing methods naturally align with repository `update()`
   - The separation reflects important domain semantics

3. **Simpler Implementation**
   - Each method has a single, well-defined responsibility
   - No conditional logic needed to determine whether to insert or update
   - Clearer error handling for each operation type

4. **Better Testability**
   - Tests clearly express their intent (testing insert vs. testing update)
   - Mock/stub setup is more straightforward
   - Test names are more meaningful

### When to Use Save Design

Use a generic `save()` method when:

1. **Operation Doesn't Matter to Caller**
   - The calling code doesn't care whether it's inserting or updating
   - The repository can handle the distinction internally based on entity state
   - Reduces decision-making burden on the caller

2. **Consistency with Framework Patterns**
   - Working with frameworks that use the save() pattern (e.g., Spring Data JPA)
   - Maintaining consistency with existing codebase patterns
   - Reduces cognitive overhead when switching between framework code and your code

3. **Convenience Over Explicitness**
   - The use case genuinely doesn't distinguish between create and update
   - The entity itself knows whether it's new or existing (e.g., has an ID or not)
   - Simpler API surface for the caller

## Consequences

### Positive

- Developers have flexibility to choose the right approach for their use case
- Can optimize for explicitness (insert/update) or convenience (save) as needed
- Different repositories can use different patterns based on their domain needs
- Allows alignment with both domain semantics and framework conventions

### Negative

- Lack of consistency across repositories may cause confusion
- Developers must understand the tradeoffs to make informed choices
- Code reviews must ensure the chosen approach is appropriate for the use case

### Neutral

- Each approach has its own tradeoffs that developers must consider
- The codebase may contain a mix of both patterns

## Examples

### Option 1: Generic Save

```java
public interface TableRepository {
    void save(Table table);
}

// Caller code - convenient when operation doesn't matter
tableRepository.save(table);
```

### Option 2: Explicit Insert/Update

```java
public interface WalletRepository {
    void insert(Wallet wallet);
    void update(Wallet wallet);
}

// Caller code - explicit and fail-fast
walletRepository.insert(newWallet);
walletRepository.update(existingWallet);
```

## Notes

The WalletRepository implementation uses the insert/update design (see commit "Refactor: Split WalletRepository save method into insert and update"), demonstrating one valid approach.

Other repositories in the codebase may use the save() design if that better serves their specific needs.

When choosing between the two approaches, consider:
- How important is explicit intent in your domain?
- Do you want fail-fast behavior or convenience?
- Does your use case naturally distinguish between create and update?