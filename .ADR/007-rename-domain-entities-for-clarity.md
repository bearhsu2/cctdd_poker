# ADR 007: Rename Domain Entities for Clarity

## Status

Accepted

## Context

The codebase initially used the name `Hand` for a 5-card poker hand and `Table` for the aggregate representing one poker game round. This naming caused confusion:

1. In poker terminology, "hand" commonly refers to both:
   - A player's 5 cards (specific hand ranking like "pair of aces")
   - A complete round of play from deal to showdown

2. "Table" is not accurate for a poker round - a table is where players sit, not a single game

3. Having both meanings for "hand" in the same codebase creates ambiguity

## Decision

We decided to rename the domain entities as follows:

1. **`Hand` → `PokerHand`**: The 5-card poker hand with its ranking
   - Represents a specific card combination (e.g., flush, straight, pair)
   - Used for comparing poker hand rankings
   - More explicit and avoids confusion

2. **`Table` → `Hand`**: The aggregate root for one poker round
   - Represents one complete round of poker from start to settlement
   - In poker terminology, a "hand" often means one round of play
   - Related entities renamed accordingly:
     - `TableStatus` → `HandStatus`
     - `TableSettledEvent` → `HandSettledEvent` (including field `tableId` → `handId`)
     - `TableRepository` → `HandRepository`
     - `SettleTableService` → `SettleHandService`
   - Related references in other bounded contexts:
     - `GameHistory.tableId` → `GameHistory.handId`
     - `GameHistoryRepository.findByTableId()` → `findByHandId()`

## Consequences

### Positive

- **Clearer domain model**: `PokerHand` explicitly indicates a 5-card hand ranking
- **Better alignment with poker terminology**: "hand" for a game round is standard poker terminology
- **Reduced ambiguity**: No confusion between the two different concepts
- **More maintainable**: Developers can immediately understand what each entity represents

### Negative

- **Breaking change**: Requires updating all references throughout the codebase
- **Git history**: Historical references to `Hand` and `Table` may be less clear
- **Initial learning curve**: Developers familiar with the old names need to adjust

### Neutral

- All tests continue to pass after the refactoring
- No functional changes were made, only naming improvements