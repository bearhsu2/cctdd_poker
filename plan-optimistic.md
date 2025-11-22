# Optimistic Locking Implementation Plan

## Approach
1. Version increment: Keep in entity methods (current approach)
2. SQL strategy: `UPDATE wallet SET balance=?, version=? WHERE player_id=? AND version=?-1`
3. Conflict handling: Throw exception on version mismatch

## Implementation Steps

### 1. Write Test for Optimistic Lock Behavior
- Create a test that simulates concurrent updates to the same wallet
- Load wallet, modify it (e.g., add balance), save it
- Load the same wallet again (without refreshing from DB), modify it, try to save
- Expect the second save to throw an exception due to version mismatch

### 2. Create OptimisticLockException
- Create a custom exception class (e.g., `OptimisticLockException`)
- This will be thrown when a version conflict is detected

### 3. Update WalletRepositoryQueryDsl
- Modify the `save()` method to use optimistic locking
- Use SQL: `UPDATE wallet SET balance=?, version=? WHERE player_id=? AND version=?-1`
- Check if update affected 0 rows (indicating version mismatch)
- Throw `OptimisticLockException` if no rows were updated

### 4. Run Tests
- Verify the optimistic locking behavior works as expected
- Ensure existing tests still pass