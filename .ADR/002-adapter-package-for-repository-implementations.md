# ADR 002: Adapter Package for Repository Implementations

## Status

Accepted

## Context

As the project adopts Clean Architecture principles, we needed to determine the proper package location for repository implementations. The codebase has the following structure:

- `/{aggregate name}/` - Contains domain entities
- `/{aggregate name}/usecase/` - Contains services and repository interfaces
- Repository implementations (e.g., `TableRepositoryInMemory`) were initially placed directly in the aggregate package

The question arose: Where should repository implementations be placed to best reflect Clean Architecture's separation of concerns?

## Decision

We will use the **`/{aggregate name}/adapter`** package for all repository implementations and controllers.

This aligns with Clean Architecture's Adapter pattern, where:
- Repository implementations are adapters to external systems (databases, in-memory stores, etc.)
- Controllers are adapters from external input (HTTP, CLI, etc.)
- Both belong in the same architectural layer

## Rationale

### 1. Clean Architecture Principles
- Infrastructure concerns should be separated from domain and use case layers
- Repository implementations are interface adapters in the Clean Architecture model
- They translate between use case interfaces and external systems

### 2. Clarity and Consistency
- Package names clearly indicate the role and layer of contained classes
- All adapters (controllers, repository implementations) follow the same pattern
- Developers can easily locate infrastructure code

### 3. Maintainability
- Easy to add alternative implementations (e.g., `TableRepositoryJpa`, `TableRepositoryMongo`)
- Swap implementations without touching domain or use case code
- Clear boundaries prevent leaking infrastructure concerns into business logic

### 4. Standard Terminology
- "Adapter" is the standard term in Clean Architecture literature
- More widely recognized than alternatives like "infrastructure" or "persistence"

## Options Considered

### Option 1: `/{aggregate name}/infrastructure`
- Pros: Explicitly indicates infrastructure layer
- Cons: Less commonly used term in Clean Architecture

### Option 2: Move to test directory (`src/test/java`)
- Pros: Co-locates test-only implementations with tests
- Cons: Cannot be reused in main code if needed; mixes concerns

### Option 3: `/{aggregate name}/adapter` (Chosen)
- Pros: Standard Clean Architecture terminology; consistent with controller placement
- Cons: None identified

## Consequences

### Positive
- Clear separation of concerns between domain entities, use cases, and adapters
- Improved testability with easy implementation swapping
- Consistency across all adapter types (controllers, repositories)
- Scalability for multiple implementations
- Prevents domain/use case pollution with infrastructure details

### Negative
- Migration effort for existing repository implementations (minimal impact)
- One additional level in package hierarchy

### Neutral
- Developers must understand the adapter pattern and proper placement
- Need to maintain consistency in this approach across the codebase

## Package Structure

```
/{aggregate name}/
  ├── entity/          # Domain entities (e.g., Table, Card)
  ├── usecase/         # Use case services and repository interfaces
  └── adapter/         # Repository implementations and controllers
```

## Implementation Notes

- Repository interfaces remain in `/{aggregate name}/usecase/` package
- Repository implementations go in `/{aggregate name}/adapter/` package
- Controllers also belong in `/{aggregate name}/adapter/` package
- Import statements must be updated when moving implementations

## Related Decisions

- Package naming conventions documented in `.claude/CLAUDE.md`
- Domain service placement (ADR 001)
