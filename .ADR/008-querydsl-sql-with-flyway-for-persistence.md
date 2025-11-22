# ADR 008: QueryDSL SQL with Flyway for Persistence

## Status

Accepted

## Context

The poker application needed to migrate from in-memory repository implementations to a persistent database solution. Several technical decisions needed to be made:

1. **Database choice**: Which database to use for this demo project?
2. **Query approach**: JPA/ORM vs. type-safe SQL builders vs. plain JDBC?
3. **Schema management**: How to version and manage database schema changes?
4. **Domain purity**: How to keep domain entities free from persistence concerns (DDD principle)?
5. **Code generation**: How to generate type-safe query code?

Traditional approaches like Spring Data JPA would introduce ORM complexity and require annotating domain entities, which violates clean architecture principles.

## Decision

We chose a **database-first approach** using:

1. **H2 in-memory database** for storage
2. **QueryDSL SQL** (not QueryDSL JPA) for type-safe queries
3. **Flyway** for versioned schema migrations
4. **DTO pattern** to separate persistence from domain
5. **Pure POJO DTOs** with Lombok only (no JPA annotations)
6. **Metadata-driven code generation**: Flyway creates schema → QueryDSL reads metadata → Generates Q-classes

### Architecture Pattern

```
Domain Entity (Wallet) ←→ DB DTO (WalletDbDto) ←→ Database Table
     ↑                           ↑                      ↑
  Pure DDD               Pure POJO             Flyway creates schema
  No persistence         Lombok only           QueryDSL reads metadata
  concerns              Manual mapping         Generates Q-classes
```

### Technology Stack

- **Database**: H2 in-memory mode (`jdbc:h2:mem:pokerdb`)
- **Query Builder**: QueryDSL SQL (`querydsl-sql`, `querydsl-sql-spring`)
- **Connection**: Spring JDBC (`spring-boot-starter-jdbc`) - No JPA/Hibernate
- **Schema Migration**: Flyway (`flyway-core`)
- **Serialization**: Jackson (`jackson-databind`) for complex fields
- **QueryFactory**: `SQLQueryFactory` (not `JPAQueryFactory`)

### Code Generation Workflow

1. Developer writes Flyway migration (e.g., `V00000000001__create_wallet_table.sql`)
2. Maven plugin executes: `mvn generate-sources`
3. QueryDSL plugin starts H2, runs Flyway migrations, reads table metadata
4. Q-classes are generated (e.g., `QWallet`, `QHand`, `QGameHistory`)
5. Developer creates matching DTO POJOs manually (with Lombok annotations only)
6. Repository uses `SQLQueryFactory` + Q-classes for type-safe queries

## Rationale

### 1. Domain Purity (Hexagonal Architecture)
- Domain entities remain pure POJOs with no persistence annotations
- DTOs live in the adapter layer, domain entities in the entity layer
- Easy to test domain logic in isolation
- Can swap persistence technology without touching domain code

### 2. Type Safety
- QueryDSL provides compile-time query checking
- Refactoring support (rename columns, change types)
- IDE autocomplete for queries
- Catches SQL errors at compile time

### 3. Database-First Design
- Schema is the source of truth (defined by Flyway migrations)
- Code is generated from schema (not vice versa)
- Clear schema versioning and history
- Reproducible database setup across environments

### 4. No ORM Complexity
- No lazy loading issues
- No N+1 query problems
- No hidden SQL queries
- Explicit control over all database operations
- Lighter weight than Hibernate

### 5. Migration Management
- Flyway tracks all schema changes in `flyway_schema_history`
- Versioned migrations (V1, V2, V3...)
- Easy to add indexes, constraints, or schema changes
- Reproducible across development, test, and production

### 6. Simplicity for Demo Project
- H2 in-memory requires no external database setup
- Fast test execution (fresh DB for each test run)
- Self-contained project (easy to run on any machine)

## Consequences

### Positive
- ✅ Domain entities remain persistence-ignorant (DDD principle)
- ✅ Type-safe queries with compile-time checking
- ✅ Versioned schema migrations tracked by Flyway
- ✅ Simple setup - no external database required
- ✅ Easy to switch persistence technology (just change adapter layer)
- ✅ Schema changes are tracked and reproducible
- ✅ No ORM magic - explicit and understandable SQL operations
- ✅ Clear separation of concerns (Hexagonal Architecture)
- ✅ Lighter weight than JPA/Hibernate

### Negative
- ❌ Additional DTO mapping code (manual `toEntity`/`toDto` methods)
- ❌ Manual CRUD operations (explicit insert/update/select logic)
- ❌ Manual optimistic locking (no automatic `@Version` handling)
- ❌ Q-class generation requires database during build
- ❌ Domain events not persisted (transient, in-memory only - acceptable for demo)

### Neutral
- Repository implementations are more verbose than Spring Data JPA
- Requires understanding of both QueryDSL SQL and Flyway
- Need to maintain both Flyway migrations and DTO classes manually

## Implementation Details

### Migration Naming Convention
- Format: `V{version}__{description}.sql`
- Example: `V00000000001__create_wallet_table.sql`
- Migrations execute once and are tracked in `flyway_schema_history`

### DTO Naming Convention
- Format: `*DbDto` (e.g., `WalletDbDto`, `HandDbDto`, `GameHistoryDbDto`)
- Pure POJOs with Lombok only: `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`
- No JPA annotations (`@Entity`, `@Table`, `@Id`, `@Column` are NOT used)

### Q-Class Naming
- Generated by QueryDSL from table metadata
- Format: `Q{TableName}` (e.g., `QWallet`, `QHand`, `QGameHistory`)
- Located in: `target/generated-sources/java`

### Repository Pattern
- Implement domain repository interface (`WalletRepository`)
- Use `SQLQueryFactory` for all database operations
- Explicit insert/update logic with version checking
- Manual DTO ↔ Entity conversion

### JSON Serialization
- Complex domain objects stored as JSON strings (e.g., `Board`, `HoleCards`)
- Jackson `ObjectMapper` handles serialization/deserialization
- Allows persisting rich domain objects without flattening

## Alternatives Considered

### Spring Data JPA
- **Rejected**: Requires `@Entity` annotations on domain entities (violates DDD)
- ORM complexity (lazy loading, proxy objects, Hibernate sessions)
- More heavyweight than needed for this project

### Plain JDBC with SQL Strings
- **Rejected**: No type safety (errors only at runtime)
- No refactoring support
- More error-prone
- No compile-time query validation

### QueryDSL JPA
- **Rejected**: Still requires JPA entity annotations
- Still brings in Hibernate ORM
- More complex than QueryDSL SQL for our use case

### jOOQ
- **Considered**: Similar to QueryDSL SQL
- **Rejected**: Team already familiar with QueryDSL
- QueryDSL has better Spring integration

## Migration Phases

The migration was executed in phases:
1. **Phase 1**: Dependencies & Configuration
2. **Phase 2**: Wallet Repository (simplest, no JSON)
3. **Phase 3**: Hand Repository (most complex, nested collections)
4. **Phase 4**: GameHistory Repository (medium complexity, Map serialization)
5. **Phase 5**: Testing & Verification

Each phase followed: Flyway migration → Generate Q-classes → Create DTO → Implement repository → Test

## References

- Implementation details: `plan.md`
- Code examples: `querydsl-sql-approach.md`
- QueryDSL SQL documentation: https://querydsl.com/static/querydsl/latest/reference/html/ch02s03.html
- Flyway documentation: https://flywaydb.org/documentation/