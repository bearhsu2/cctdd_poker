# Optimistic Locking Design

## Overview

This project uses **optimistic locking** with version-based concurrency control to handle concurrent updates to entities. This approach assumes conflicts are rare and detects them when they occur, rather than preventing them with pessimistic locks.

## Approach

### 1. Version Management

**Version increment location**: Entity methods (domain layer)

- The `version` field is managed by the domain entity itself
- Entity methods that modify state also increment the version
- Example: `Wallet.addBalance()` increments `version` after updating `balance`

**Rationale**:
- Version is part of the entity's state and lifecycle
- Domain logic controls when version changes occur
- Clear coupling between state changes and version increments

### 2. Repository Save Strategy

**SQL Pattern**:
```sql
UPDATE wallet
SET balance=?, version=?
WHERE player_id=? AND version=?-1
```

**Key Points**:
- The `WHERE` clause includes the **old version** (version - 1)
- The `SET` clause updates to the **new version** (already incremented by entity)
- If the version in DB doesn't match the old version, no rows are updated

**Example Flow**:
```
1. Load wallet: { playerId: "user-1", version: 5, balance: 1000 }
2. Modify in domain: wallet.addBalance(100)
   → { playerId: "user-1", version: 6, balance: 1100 }
3. Save with SQL: UPDATE wallet SET balance=1100, version=6
                  WHERE player_id='user-1' AND version=5
4. If another transaction already updated to version 6:
   → SQL returns 0 rows updated
   → OptimisticLockException is thrown
```

### 3. Conflict Detection

**Detection mechanism**: Check update row count

```java
long rowsUpdated = queryFactory.update(qWallet)
    .set(qWallet.balance, wallet.getBalance())
    .set(qWallet.version, wallet.getVersion())
    .where(qWallet.playerId.eq(wallet.getPlayerId())
        .and(qWallet.version.eq(wallet.getVersion() - 1)))
    .execute();

if (rowsUpdated == 0) {
    throw new OptimisticLockException("Wallet has been modified by another transaction");
}
```

**Conflict handling**: Throw exception

- When version mismatch is detected (0 rows updated), throw `OptimisticLockException`
- Caller must decide how to handle: retry, reload, or fail
- Exception provides clear signal that concurrent modification occurred

## Design Decisions

### Why version in entity methods, not repository?

**Option A (chosen)**: Entity increments version
```java
// Domain layer
public void addBalance(long amount) {
    this.balance += amount;
    this.version++;  // Entity manages version
}
```

**Option B (rejected)**: Repository increments version
```java
// Repository layer
UPDATE wallet SET balance=?, version=version+1 WHERE player_id=? AND version=?
```

**Rationale for Option A**:
- ✅ Version is part of the entity's state and lifecycle
- ✅ Domain layer has full control over when version changes
- ✅ Testable in isolation (no DB needed to test version increment)
- ✅ Explicit in code when version changes occur
- ❌ Requires entity to know about versioning concept

### Why throw exception instead of returning result?

**Chosen approach**: Throw `OptimisticLockException`

**Alternative**: Return `SaveResult` enum (SUCCESS, CONFLICT, etc.)

**Rationale**:
- Concurrency conflicts are exceptional conditions, not normal flow
- Exceptions propagate clearly through call stack
- Forces caller to handle the conflict explicitly
- Consistent with Spring/JPA optimistic locking behavior

### Why check `version=?-1` instead of `version=?`?

Because the entity has already incremented its version before save:

```java
// Before modification
Wallet wallet = { version: 5, balance: 1000 }

// After domain operation
wallet.addBalance(100)
// → wallet = { version: 6, balance: 1100 }

// Repository needs to check OLD version (5) in WHERE clause
// But entity already has NEW version (6)
UPDATE ... WHERE version = wallet.getVersion() - 1  // version = 5
```

If we used `version=?` without `-1`:
```sql
UPDATE ... WHERE version=6  -- Wrong! We need to check version 5
```

## Implementation Pattern

### Entity

```java
public class Wallet {
    private final String playerId;
    private int version;
    private long balance;

    public void addBalance(long amount) {
        this.balance += amount;
        this.version++;  // Increment on state change
    }
}
```

### Repository

```java
@Override
@Transactional
public void save(Wallet wallet) {
    WalletDbDto existing = findDto(wallet.getPlayerId());

    if (existing == null) {
        // Insert new wallet
        queryFactory.insert(qWallet)
            .set(qWallet.playerId, wallet.getPlayerId())
            .set(qWallet.balance, wallet.getBalance())
            .set(qWallet.version, wallet.getVersion())
            .execute();
    } else {
        // Update with optimistic lock check
        long rowsUpdated = queryFactory.update(qWallet)
            .set(qWallet.balance, wallet.getBalance())
            .set(qWallet.version, wallet.getVersion())
            .where(qWallet.playerId.eq(wallet.getPlayerId())
                .and(qWallet.version.eq(wallet.getVersion() - 1)))
            .execute();

        if (rowsUpdated == 0) {
            throw new OptimisticLockException(
                "Wallet " + wallet.getPlayerId() + " has been modified by another transaction"
            );
        }
    }
}
```

### Exception

```java
public class OptimisticLockException extends RuntimeException {
    public OptimisticLockException(String message) {
        super(message);
    }
}
```

## Testing Strategy

### Unit Test (Domain Layer)

Test that entity methods increment version:

```java
@Test
void addBalance_should_increment_version() {
    Wallet wallet = Wallet.create("user-1", 1000);
    int initialVersion = wallet.getVersion();

    wallet.addBalance(100);

    assertThat(wallet.getVersion()).isEqualTo(initialVersion + 1);
}
```

### Integration Test (Repository Layer)

Test concurrent modification detection:

```java
@Test
void save_should_throw_exception_when_version_conflict_occurs() {
    // Given: wallet in DB with version 1
    Wallet wallet1 = Wallet.create("user-1", 1000);
    walletRepository.save(wallet1);

    // Load same wallet twice (simulating two transactions)
    Wallet walletTx1 = walletRepository.findByPlayerId("user-1");
    Wallet walletTx2 = walletRepository.findByPlayerId("user-1");

    // Tx1 modifies and saves (version becomes 2)
    walletTx1.addBalance(100);
    walletRepository.save(walletTx1);

    // Tx2 tries to save (still has version 1 → 2, but DB is already at 2)
    walletTx2.addBalance(200);

    // Then: Should throw OptimisticLockException
    assertThrows(OptimisticLockException.class, () -> {
        walletRepository.save(walletTx2);
    });
}
```

## Applicability

This optimistic locking pattern applies to:
- `Wallet` entity (player balance updates)
- `Hand` entity (game state transitions)
- `GameHistory` entity (if updated after creation)

All entities with a `version` field should follow this pattern.

## Trade-offs

### Advantages
- ✅ No database locks held during business logic execution
- ✅ Better concurrency (optimistic vs pessimistic)
- ✅ Simple to understand and implement
- ✅ Works well when conflicts are rare
- ✅ Clear error signaling via exceptions

### Disadvantages
- ❌ Transaction must be retried on conflict
- ❌ Wasted work if conflict occurs
- ❌ Not suitable for high-conflict scenarios
- ❌ Manual implementation (no framework magic)

### When NOT to use
- High-contention resources (use pessimistic locking)
- Long-running transactions (higher conflict probability)
- Operations that MUST succeed on first attempt