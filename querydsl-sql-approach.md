# QueryDSL SQL Approach (No JPA Annotations)

## Key Difference from JPA Approach

**Instead of:** Using JPA annotations on DTOs and generating Q-classes from annotated entities
**We use:** Pure POJOs for DTOs and generate Q-classes directly from database metadata

## Architecture

```
Flyway Migration → H2 Database → QueryDSL MetaDataExporter → Q-classes (QWallets, QHands, QGameHistories)
                      ↓
                  Pure POJO DTOs (WalletDbDto, HandDbDto, GameHistoryDbDto)
                      ↓
                  Repository with SQLQueryFactory
```

## Dependencies (pom.xml)

### Add Dependencies

```xml
<!-- JDBC (no JPA/Hibernate) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<!-- H2 Database -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- QueryDSL SQL (not querydsl-jpa) -->
<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-sql</artifactId>
    <version>5.0.0</version>
</dependency>

<dependency>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-sql-spring</artifactId>
    <version>5.0.0</version>
</dependency>

<!-- Flyway -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>

<!-- Jackson for JSON serialization -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

### Configure QueryDSL Maven Plugin

```xml
<plugin>
    <groupId>com.querydsl</groupId>
    <artifactId>querydsl-maven-plugin</artifactId>
    <version>5.0.0</version>
    <executions>
        <execution>
            <id>generate-querydsl-sql</id>
            <phase>generate-sources</phase>
            <goals>
                <goal>export</goal>
            </goals>
            <configuration>
                <jdbcDriver>org.h2.Driver</jdbcDriver>
                <jdbcUrl>jdbc:h2:mem:pokerdb;MODE=MySQL;INIT=RUNSCRIPT FROM 'classpath:db/migration/V1__create_wallets_table.sql'\;RUNSCRIPT FROM 'classpath:db/migration/V2__create_hands_table.sql'\;RUNSCRIPT FROM 'classpath:db/migration/V3__create_game_histories_table.sql'</jdbcUrl>
                <jdbcUser>sa</jdbcUser>
                <jdbcPassword></jdbcPassword>
                <packageName>idv.kuma.poker.generated</packageName>
                <targetFolder>${project.basedir}/target/generated-sources/java</targetFolder>
                <exportBeans>true</exportBeans>
                <namePrefix>Q</namePrefix>
            </configuration>
        </execution>
    </executions>
    <dependencies>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.2.224</version>
        </dependency>
    </dependencies>
</plugin>
```

**How it works:**
1. Plugin starts H2 database
2. Executes all Flyway migration scripts
3. Reads table metadata from H2
4. Generates Q-classes: `QWallets`, `QHands`, `QGameHistories`

## DTOs - Pure POJOs (No Annotations)

### WalletDbDto

```java
package idv.kuma.poker.wallet.adapter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDbDto {
    private String playerId;  // Maps to player_id column
    private Long balance;
    private Integer version;
}
```

**No JPA annotations needed!** Just pure POJOs with Lombok.

### HandDbDto

```java
package idv.kuma.poker.table.adapter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandDbDto {
    private String id;
    private String status;
    private Integer version;
    private String userIds;           // JSON array
    private Integer bet;
    private String holeCardsJson;     // JSON
    private String boardJson;         // JSON
}
```

### GameHistoryDbDto

```java
package idv.kuma.poker.gamehistory.adapter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameHistoryDbDto {
    private String handId;           // Maps to hand_id column
    private String handResultJson;   // Maps to hand_result_json column
    private Integer version;
}
```

## Repository Implementation with QueryDSL SQL

### Spring Configuration for SQLQueryFactory

```java
package idv.kuma.poker.common.config;

import com.querydsl.sql.H2Templates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.spring.SpringConnectionProvider;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class QueryDslConfig {

    @Bean
    public com.querydsl.sql.Configuration querydslConfiguration() {
        com.querydsl.sql.Configuration configuration =
            new com.querydsl.sql.Configuration(new H2Templates());
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
        return configuration;
    }

    @Bean
    public SQLQueryFactory queryFactory(DataSource dataSource,
                                        com.querydsl.sql.Configuration configuration) {
        return new SQLQueryFactory(configuration, dataSource);
    }
}
```

### WalletRepositoryQueryDsl

```java
package idv.kuma.poker.wallet.adapter;

import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import idv.kuma.poker.generated.QWallets;
import idv.kuma.poker.wallet.entity.Wallet;
import idv.kuma.poker.wallet.usecase.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class WalletRepositoryQueryDsl implements WalletRepository {
    private final SQLQueryFactory queryFactory;
    private final QWallets qWallets = QWallets.wallets;

    @Override
    public Wallet findByPlayerId(String playerId) {
        WalletDbDto dto = queryFactory
            .select(qWallets.playerId, qWallets.balance, qWallets.version)
            .from(qWallets)
            .where(qWallets.playerId.eq(playerId))
            .fetchOne();

        return dto == null ? null : toEntity(dto);
    }

    @Override
    @Transactional
    public void save(Wallet wallet) {
        WalletDbDto existing = queryFactory
            .select(qWallets.playerId, qWallets.balance, qWallets.version)
            .from(qWallets)
            .where(qWallets.playerId.eq(wallet.getPlayerId()))
            .fetchOne();

        if (existing == null) {
            // Insert
            queryFactory.insert(qWallets)
                .set(qWallets.playerId, wallet.getPlayerId())
                .set(qWallets.balance, wallet.getBalance())
                .set(qWallets.version, wallet.getVersion())
                .execute();
        } else {
            // Update
            queryFactory.update(qWallets)
                .set(qWallets.balance, wallet.getBalance())
                .set(qWallets.version, wallet.getVersion())
                .where(qWallets.playerId.eq(wallet.getPlayerId()))
                .execute();
        }
    }

    private Wallet toEntity(WalletDbDto dto) {
        return Wallet.restore(dto.getPlayerId(), dto.getVersion(), dto.getBalance());
    }
}
```

### HandRepositoryQueryDsl (with JSON serialization)

```java
package idv.kuma.poker.table.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.sql.SQLQueryFactory;
import idv.kuma.poker.generated.QHands;
import idv.kuma.poker.table.entity.*;
import idv.kuma.poker.table.usecase.HandRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HandRepositoryQueryDsl implements HandRepository {
    private final SQLQueryFactory queryFactory;
    private final ObjectMapper objectMapper;
    private final QHands qHands = QHands.hands;

    @Override
    public Hand findById(String handId) {
        HandDbDto dto = queryFactory
            .select(qHands.id, qHands.status, qHands.version,
                    qHands.userIds, qHands.bet, qHands.holeCardsJson, qHands.boardJson)
            .from(qHands)
            .where(qHands.id.eq(handId))
            .fetchOne();

        return dto == null ? null : toEntity(dto);
    }

    @Override
    @Transactional
    public void save(Hand hand) {
        HandDbDto dto = toDto(hand);

        HandDbDto existing = queryFactory
            .select(qHands.id)
            .from(qHands)
            .where(qHands.id.eq(hand.getId()))
            .fetchOne();

        if (existing == null) {
            queryFactory.insert(qHands)
                .set(qHands.id, dto.getId())
                .set(qHands.status, dto.getStatus())
                .set(qHands.version, dto.getVersion())
                .set(qHands.userIds, dto.getUserIds())
                .set(qHands.bet, dto.getBet())
                .set(qHands.holeCardsJson, dto.getHoleCardsJson())
                .set(qHands.boardJson, dto.getBoardJson())
                .execute();
        } else {
            queryFactory.update(qHands)
                .set(qHands.status, dto.getStatus())
                .set(qHands.version, dto.getVersion())
                .set(qHands.userIds, dto.getUserIds())
                .set(qHands.bet, dto.getBet())
                .set(qHands.holeCardsJson, dto.getHoleCardsJson())
                .set(qHands.boardJson, dto.getBoardJson())
                .where(qHands.id.eq(hand.getId()))
                .execute();
        }
    }

    @SneakyThrows
    private Hand toEntity(HandDbDto dto) {
        List<String> userIds = objectMapper.readValue(dto.getUserIds(), List.class);
        List<HoleCards> holeCards = objectMapper.readValue(
            dto.getHoleCardsJson(),
            objectMapper.getTypeFactory().constructCollectionType(List.class, HoleCards.class)
        );
        Board board = objectMapper.readValue(dto.getBoardJson(), Board.class);

        return Hand.restore(
            dto.getId(),
            HandStatus.valueOf(dto.getStatus()),
            dto.getVersion(),
            userIds,
            dto.getBet(),
            holeCards,
            board
        );
    }

    @SneakyThrows
    private HandDbDto toDto(Hand hand) {
        return new HandDbDto(
            hand.getId(),
            hand.getStatus().name(),
            hand.getVersion(),
            objectMapper.writeValueAsString(hand.getUserIds()),
            hand.getBet(),
            objectMapper.writeValueAsString(hand.getHoleCards()),
            objectMapper.writeValueAsString(hand.getBoard())
        );
    }
}
```

## Generated Q-Classes

After running `mvn generate-sources`, you'll get:

### QWallets

```java
package idv.kuma.poker.generated;

// Generated by QueryDSL MetaDataExporter
public class QWallets extends RelationalPathBase<QWallets> {
    public static final QWallets wallets = new QWallets("wallets");

    public final StringPath playerId = createString("player_id");
    public final NumberPath<Long> balance = createNumber("balance", Long.class);
    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    // ... QueryDSL generated code
}
```

### QHands

```java
package idv.kuma.poker.generated;

public class QHands extends RelationalPathBase<QHands> {
    public static final QHands hands = new QHands("hands");

    public final StringPath id = createString("id");
    public final StringPath status = createString("status");
    public final NumberPath<Integer> version = createNumber("version", Integer.class);
    public final StringPath userIds = createString("user_ids");
    public final NumberPath<Integer> bet = createNumber("bet", Integer.class);
    public final StringPath holeCardsJson = createString("hole_cards_json");
    public final StringPath boardJson = createString("board_json");

    // ... QueryDSL generated code
}
```

### QGameHistories

```java
package idv.kuma.poker.generated;

public class QGameHistories extends RelationalPathBase<QGameHistories> {
    public static final QGameHistories gameHistories = new QGameHistories("game_histories");

    public final StringPath handId = createString("hand_id");
    public final StringPath handResultJson = createString("hand_result_json");
    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    // ... QueryDSL generated code
}
```

## Key Differences Summary

| Aspect | JPA Approach | QueryDSL SQL Approach |
|--------|-------------|---------------------|
| **Dependencies** | `querydsl-jpa`, `querydsl-apt`, `spring-boot-starter-data-jpa` | `querydsl-sql`, `querydsl-sql-spring`, `spring-boot-starter-jdbc` |
| **DTO Annotations** | `@Entity`, `@Table`, `@Id`, `@Column`, `@Version` | Lombok only (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`) |
| **Q-class Generation** | From JPA annotations via APT processor | From database metadata via Maven plugin |
| **Q-class Names** | `QWalletDbDto`, `QHandDbDto` | `QWallets`, `QHands`, `QGameHistories` |
| **QueryFactory** | `JPAQueryFactory` (needs `EntityManager`) | `SQLQueryFactory` (needs `DataSource`) |
| **Insert/Update** | `EntityManager.persist()`, `EntityManager.merge()` | Explicit `queryFactory.insert()`, `queryFactory.update()` |
| **ORM** | Hibernate manages entities | No ORM - manual DTO mapping |
| **Optimistic Locking** | Automatic via `@Version` | Manual version checking in queries |

## Advantages of QueryDSL SQL

1. ✅ **No JPA pollution** - DTOs are pure POJOs
2. ✅ **Database-first** - Schema defined by Flyway, code generated from it
3. ✅ **No Hibernate** - Lighter weight, no lazy loading issues
4. ✅ **Explicit SQL control** - See exactly what SQL is executed
5. ✅ **No magic** - Manual mapping makes everything clear
6. ✅ **Type-safe** - Still get compile-time query checking

## Disadvantages

1. ❌ **Manual CRUD** - Need explicit insert/update logic
2. ❌ **No automatic optimistic locking** - Must handle version checking manually
3. ❌ **More mapping code** - DTO ↔ Entity conversion is manual
4. ❌ **Q-class generation** - Requires database to be running during build

## Build Command

```bash
# Generate Q-classes and compile
mvn clean generate-sources compile

# Run tests (H2 will be created fresh, Flyway migrations run, then tests execute)
mvn clean test
```

## application.properties

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

No JPA/Hibernate configuration needed!

## Next Steps

1. Add dependencies to `pom.xml`
2. Configure QueryDSL Maven plugin
3. Create Flyway migrations (V1, V2, V3)
4. Run `mvn generate-sources` to generate Q-classes
5. Create pure POJO DTOs (no annotations except Lombok)
6. Create QueryDslConfig with SQLQueryFactory
7. Implement repositories using SQLQueryFactory
8. Run tests to verify everything works
