# ADR 007: Rename Domain Entities for Clarity

## Status

Accepted

## Context

The codebase initially used generic or ambiguous names for poker domain entities, causing confusion:

1. **`Hand`** was used for a 5-card poker hand, but in poker terminology, "hand" commonly refers to both:
   - A player's 5 cards (specific hand ranking like "pair of aces")
   - A complete round of play from deal to showdown

2. **`Table`** was used for the aggregate representing one poker game round
   - "Table" is not accurate for a poker round - a table is where players sit, not a single game

3. **`PlayerCards`** was a generic name for the two private cards dealt to each player
   - In Texas Hold'em, these are specifically called "hole cards"

These naming issues created ambiguity and reduced clarity in the domain model

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

3. **`PlayerCards` → `HoleCards`**: The two private cards dealt to each player
   - In Texas Hold'em poker, these are specifically called "hole cards"
   - More precise and aligned with standard poker terminology
   - Updated in Hand aggregate field: `playerCards` → `holeCards`

## Consequences

### Positive

- **Clearer domain model**: Each entity name explicitly indicates what it represents
  - `PokerHand` clearly means a 5-card hand ranking
  - `HoleCards` clearly means the player's two private cards
- **Better alignment with poker terminology**: All names match standard poker vocabulary
  - "hand" for a game round is standard poker terminology
  - "hole cards" is the correct Texas Hold'em term
- **Reduced ambiguity**: No confusion between different concepts
- **More maintainable**: Developers familiar with poker can immediately understand what each entity represents

### Negative

- **Breaking change**: Requires updating all references throughout the codebase
- **Git history**: Historical references to `Hand` and `Table` may be less clear
- **Initial learning curve**: Developers familiar with the old names need to adjust

### Neutral

- All tests continue to pass after the refactoring
- No functional changes were made, only naming improvements