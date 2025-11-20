# Migration Plan: In-Memory to H2 Database with QueryDSL and Flyway

## Configuration Decisions

- **Database**: H2 in-memory mode
- **H2 Console**: Disabled
- **Query Builder**: QueryDSL SQL (type-safe queries, no ORM)
- **Schema Migration**: Flyway (versioned migrations)
- **DTO Naming**: `*DbDto` (e.g., `WalletDbDto`, `HandDbDto`, `GameHistoryDbDto`)
- **Q-class Generation**: Database metadata (no JPA annotations needed)
- **Architecture**: Hexagonal - Domain entities remain pure, DTOs are pure POJOs

## Architecture Pattern

```
Domain Entity (Wallet) ←→ DB DTO (WalletDbDto) ←→ Database Table
     ↑                           ↑                      ↑
  Pure DDD               Pure POJO             Flyway creates schema
  No persistence         No annotations        QueryDSL reads metadata
  concerns              Mapping/conversion     Generates Q-classes
                        in repository adapter
```

## Phase 1: Dependencies & Configuration

### 1.1 Update pom.xml - Add Dependencies

Add the following dependencies:
- `spring-boot-starter-jdbc` - JDBC support (no JPA/Hibernate)
- `h2` - H2 database driver
- `querydsl-sql` - QueryDSL SQL support (no JPA needed)
- `querydsl-sql-spring` - Spring integration for QueryDSL SQL
- `flyway-core` - Flyway migration tool
- `jackson-databind` - JSON serialization for complex fields

### 1.2 Update pom.xml - Configure QueryDSL Maven Plugin

Add `querydsl-maven-plugin` to generate Q-classes from database metadata:
- Plugin: `com.querydsl:querydsl-maven-plugin`
- Execution phase: `generate-sources`
- Database URL: `jdbc:h2:mem:pokerdb` (in-memory for generation)
- Package name: Match your DTO package structure
- Output directory: `target/generated-sources/java`

**How it works:**
1. Plugin starts H2 database
2. Flyway runs migrations to create schema
3. Plugin reads table metadata
4. Generates Q-classes (QWallets, QHands, QGameHistories)

### 1.3 Configure application.properties

Add H2 and Flyway configuration:
```properties
# H2 In-Memory Database
spring.datasource.url=jdbc:h2:mem:pokerdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
```

**Important**: No JPA/Hibernate configuration needed - we're using pure JDBC with QueryDSL SQL

### 1.4 Create Flyway Migration Scripts

Create directory: `src/main/resources/db/migration/`

Migration files (executed in order):
- `V1__create_wallets_table.sql`
- `V2__create_hands_table.sql`
- `V3__create_game_histories_table.sql`

## Phase 2: Wallet Repository Migration

### 2.1 Create Flyway Migration - V1__create_wallets_table.sql

**Location**: `src/main/resources/db/migration/V1__create_wallets_table.sql`

```sql
CREATE TABLE wallets (
    player_id VARCHAR(255) PRIMARY KEY,
    balance BIGINT NOT NULL,
    version INT NOT NULL
);
```

### 2.2 Create WalletDbDto

**Location**: `src/main/java/idv/kuma/poker/wallet/adapter/WalletDbDto.java`

**Fields**:
- `playerId` (String) - Primary key
- `balance` (Long)
- `version` (Integer) - Optimistic locking

**Annotations**:
- Lombok only: `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`
- **No JPA annotations needed** - pure POJO

### 2.3 Create WalletRepositoryQueryDsl

**Location**: `src/main/java/idv/kuma/poker/wallet/adapter/WalletRepositoryQueryDsl.java`

**Dependencies**:
- Inject `EntityManager`
- Create `JPAQueryFactory` from EntityManager

**Methods**:
- `findByPlayerId(String playerId)` - Use QueryDSL to query by playerId
- `save(Wallet wallet)` - Convert to DTO, persist, convert back

**Mapping**:
- `toEntity(WalletDbDto dto)` - DTO → Domain Entity
- `toDto(Wallet entity)` - Domain Entity → DTO

**Implementation Details**:
```java
QWalletDbDto qWallet = QWalletDbDto.walletDbDto;
WalletDbDto dto = queryFactory
    .selectFrom(qWallet)
    .where(qWallet.playerId.eq(playerId))
    .fetchOne();
```

### 2.4 Replace WalletRepositoryInMemory

- Remove or rename `WalletRepositoryInMemory` to `WalletRepositoryInMemory.old`
- Update Spring configuration to use `WalletRepositoryQueryDsl` as the `@Repository` implementation

## Phase 3: Hand Repository Migration

### 3.1 Create Flyway Migration - V2__create_hands_table.sql

**Location**: `src/main/resources/db/migration/V2__create_hands_table.sql`

```sql
CREATE TABLE hands (
    id VARCHAR(255) PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    version INT NOT NULL,
    user_ids TEXT NOT NULL,
    bet INT NOT NULL,
    hole_cards_json TEXT NOT NULL,
    board_json TEXT NOT NULL
);

CREATE INDEX idx_hands_status ON hands(status);
```

### 3.2 Create HandDbDto

**Location**: `src/main/java/idv/kuma/poker/table/adapter/HandDbDto.java`

**Fields**:
- `id` (String) - Primary key
- `status` (String) - HandStatus enum as string
- `version` (Integer) - Optimistic locking
- `userIds` (String) - JSON array serialized (List<String>)
- `bet` (Integer)
- `holeCardsJson` (String) - JSON serialized List<HoleCards>
- `boardJson` (String) - JSON serialized Board

**Annotations**:
- `@Entity`
- `@Table(name = "hands")`
- `@Id`, `@Version`
- `@Column(name = "user_ids", columnDefinition = "TEXT")`
- `@Column(name = "hole_cards_json", columnDefinition = "TEXT")`
- `@Column(name = "board_json", columnDefinition = "TEXT")`
- Lombok annotations

**Note**: Domain events (`domainEvents`) will NOT be persisted - they are transient events

### 3.3 Create JSON Converters (if needed)

Create utility methods or use Jackson ObjectMapper to serialize/deserialize:
- `List<HoleCards>` ↔ JSON string
- `Board` ↔ JSON string
- Handle Card, Suit, Number objects

### 3.4 Create HandRepositoryQueryDsl

**Location**: `src/main/java/idv/kuma/poker/table/adapter/HandRepositoryQueryDsl.java`

**Methods**:
- `findById(String handId)` - QueryDSL lookup by ID
- `save(Hand hand)` - Convert to DTO, persist

**Mapping**:
- `toEntity(HandDbDto dto)` - Deserialize JSON fields, reconstruct domain entity
- `toDto(Hand entity)` - Serialize complex objects to JSON

**Important**:
- Domain events are NOT persisted
- When loading from DB, `domainEvents` list will be empty
- Events are only held in memory during the current transaction

### 3.5 Replace HandRepositoryInMemory

Remove or rename the in-memory implementation

## Phase 4: GameHistory Repository Migration

### 4.1 Create Flyway Migration - V3__create_game_histories_table.sql

**Location**: `src/main/resources/db/migration/V3__create_game_histories_table.sql`

```sql
CREATE TABLE game_histories (
    hand_id VARCHAR(255) PRIMARY KEY,
    hand_result_json TEXT NOT NULL,
    version INT NOT NULL
);
```

### 4.2 Create GameHistoryDbDto

**Location**: `src/main/java/idv/kuma/poker/gamehistory/adapter/GameHistoryDbDto.java`

**Fields**:
- `handId` (String) - Primary key
- `handResultJson` (String) - JSON serialized HandResult (Map<Integer, PlayerResult>)
- `version` (Integer) - Optimistic locking

**Annotations**:
- `@Entity`
- `@Table(name = "game_histories")`
- `@Id` on handId field
- `@Version` on version field
- `@Column(name = "hand_id")` on handId field
- `@Column(name = "hand_result_json", columnDefinition = "TEXT")` on handResultJson field
- Lombok: `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`

### 4.3 Create GameHistoryRepositoryQueryDsl

**Location**: `src/main/java/idv/kuma/poker/gamehistory/adapter/GameHistoryRepositoryQueryDsl.java`

**Methods**:
- `findByHandId(String handId)` - QueryDSL lookup by handId (primary key)
- `save(GameHistory gameHistory)` - Convert to DTO, persist

**Mapping**:
- `toEntity(GameHistoryDbDto dto)` - Deserialize HandResult JSON
- `toDto(GameHistory entity)` - Serialize HandResult to JSON

**JSON Structure Example**:
```json
{
  "1": {"userId": "user1", "rank": 1},
  "2": {"userId": "user2", "rank": 2}
}
```

### 4.4 Replace GameHistoryRepositoryInMemory

Remove or rename the in-memory implementation

## Phase 5: Code Generation & Testing

### 5.1 Generate QueryDSL Q-Classes

Run Maven compile to trigger annotation processing:
```bash
mvn clean compile
```

This generates:
- `QWalletDbDto`
- `QHandDbDto`
- `QGameHistoryDbDto`

Located in: `target/generated-sources/java`

### 5.2 Verify Flyway Migrations

On application startup, Flyway will:
1. Create `flyway_schema_history` table (tracks migrations)
2. Execute V1, V2, V3 migrations in order
3. Record successful migrations

Check logs for:
```
Flyway Community Edition x.x.x
Successfully validated x migrations
Migrating schema `pokerdb` to version x
```

### 5.3 Run All Tests

```bash
mvn clean test
```

Expected behavior:
- H2 database created fresh for each test run (in-memory mode)
- Flyway runs migrations on test startup
- All existing tests should pass with new DB implementations
- No changes to test code required (repositories implement same interfaces)

### 5.4 Verify Functionality

Check that:
- Wallets can be saved and retrieved by playerId
- Hands can be saved and retrieved by handId
- GameHistory can be saved and retrieved by handId
- Optimistic locking works (version field updates)
- JSON serialization/deserialization works correctly
- Domain events still fire correctly (even though not persisted)
- Flyway migrations execute successfully

## Phase 6: Documentation

### 6.1 Create ADR

**Location**: `.ADR/XXX-migrate-to-h2-querydsl-flyway.md`

Document:
- **Context**: Need persistent storage with versioned schema management for demo project
- **Decision**: Use H2 in-memory database with QueryDSL for queries and Flyway for migrations, DTO pattern to keep domain pure
- **Consequences**:
  - ✓ Domain entities remain persistence-ignorant
  - ✓ Type-safe queries with QueryDSL
  - ✓ Versioned schema migrations with Flyway
  - ✓ Simple setup (no external DB)
  - ✓ Easy to switch persistence technology
  - ✓ Schema changes tracked and reproducible
  - ✗ Additional DTO mapping code
  - ✗ Domain events not persisted (acceptable for demo)

## Implementation Order

1. Dependencies & Configuration (Phase 1)
2. Wallet Repository (Phase 2) - Simplest, no JSON serialization
3. Hand Repository (Phase 3) - Most complex, has nested collections
4. GameHistory Repository (Phase 4) - Medium complexity, Map serialization
5. Generate & Test (Phase 5)
6. Document (Phase 6)

## Rollback Plan

If issues arise:
1. Keep old `*InMemory` implementations (renamed to `*.old`)
2. Can switch back by changing `@Component` annotations
3. Flyway migrations can be reverted by dropping tables (or use Flyway undo migrations)

## Key Design Decisions

### Why Flyway?
- Versioned schema migrations (V1, V2, V3...)
- Reproducible database setup
- Migration history tracked in `flyway_schema_history` table
- Easy to add indexes, constraints, or schema changes later
- Industry standard for database migrations

### Why DTOs instead of direct entity mapping?
- Keeps domain model pure (DDD principle)
- Follows Hexagonal Architecture
- Domain entities have no persistence framework dependencies
- Easier to test domain logic in isolation
- Can change persistence technology without touching domain

### Why QueryDSL instead of Spring Data JPA?
- Type-safe queries (compile-time checking)
- More control over query construction
- Better for complex queries
- No "magic" method name parsing

### Why not persist domain events?
- Events are fire-and-forget in this demo
- Event handlers process them immediately
- Event sourcing would require different architecture
- Transient outbox pattern could be added later if needed

## Dependencies Summary

### Maven Dependencies
- spring-boot-starter-data-jpa
- h2
- querydsl-jpa (version 5.0.0)
- querydsl-apt (version 5.0.0)
- flyway-core

### Maven Plugins
- maven-compiler-plugin with querydsl-apt processor

## File Structure After Migration

```
src/main/
├── java/idv/kuma/poker/
│   ├── wallet/
│   │   ├── entity/
│   │   │   └── Wallet.java (unchanged, pure domain)
│   │   ├── adapter/
│   │   │   ├── WalletDbDto.java (NEW - JPA entity)
│   │   │   ├── WalletRepositoryQueryDsl.java (NEW - QueryDSL implementation)
│   │   │   └── WalletRepositoryInMemory.java (DELETE or rename to .old)
│   │   └── usecase/
│   │       └── WalletRepository.java (unchanged, interface)
│   ├── table/
│   │   ├── entity/
│   │   │   └── Hand.java (unchanged, pure domain)
│   │   ├── adapter/
│   │   │   ├── HandDbDto.java (NEW - JPA entity)
│   │   │   ├── HandRepositoryQueryDsl.java (NEW - QueryDSL implementation)
│   │   │   └── HandRepositoryInMemory.java (DELETE or rename to .old)
│   │   └── usecase/
│   │       └── HandRepository.java (unchanged, interface)
│   └── gamehistory/
│       ├── entity/
│       │   └── GameHistory.java (unchanged, pure domain)
│       ├── adapter/
│       │   ├── GameHistoryDbDto.java (NEW - JPA entity)
│       │   ├── GameHistoryRepositoryQueryDsl.java (NEW - QueryDSL implementation)
│       │   └── GameHistoryRepositoryInMemory.java (DELETE or rename to .old)
│       └── usecase/
│           └── GameHistoryRepository.java (unchanged, interface)
└── resources/
    ├── db/
    │   └── migration/
    │       ├── V1__create_wallets_table.sql (NEW)
    │       ├── V2__create_hands_table.sql (NEW)
    │       └── V3__create_game_histories_table.sql (NEW)
    └── application.properties (UPDATED with H2, JPA, Flyway config)
```

## Flyway Migration Naming Convention

**Format**: `V{version}__{description}.sql`

Examples:
- `V1__create_wallets_table.sql`
- `V2__create_hands_table.sql`
- `V3__create_game_histories_table.sql`
- `V4__add_index_on_user_id.sql` (future migration example)

**Rules**:
- Version must be unique and sequential
- Use double underscore `__` between version and description
- Use underscore `_` for spaces in description
- Migrations are executed once and tracked in `flyway_schema_history`

## Notes

- All domain entities remain unchanged (no JPA pollution)
- Repository interfaces remain unchanged (same contracts)
- Service layer unchanged (depends on interfaces)
- Tests unchanged (test against interfaces)
- Only adapters are affected
- Clear separation of concerns maintained
- Flyway ensures schema consistency across all environments (including tests)
