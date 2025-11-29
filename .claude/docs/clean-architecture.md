# PROJECT ARCHITECTURE PATTERNS

This document outlines the architecture patterns used in this project, including controller, service, and
aggregate/entity. This project basically follows a Clean Architecture but taking some Spring Boot annotations for
convenience.

## Controller Pattern

### Responsibility

- The controller should be thin. It only deals with the input/output transforming job, delegating business logic to a
  service class.

### Input/Output

- The response should be a ResponseEntity<ApiResponse<SomeType>> object. The ApiResponse class is a generic wrapper for
  all API responses.

### Spring Annotations

- Use @RestController to define the controller.
- Use @RequiredArgsConstructor to automatically generate a constructor for final fields (dependency injection).
- Use @PostMapping, @GetMapping, etc. to map HTTP methods and paths.
- Use @PathVariable to extract variables from the URL path.
- Use @RequestBody to bind the request body to a Java object.
- Use @Valid to enable validation on the request body.

### package

- Place controllers in the `/{aggreagte name}/adapter` package.

### Sample

- Below is an example of a controller that follows this pattern.

```java

@RestController
@RequiredArgsConstructor
public class CreateByDayTeamController {

    private final CreateByDayTeamService createByDayTeamService;

    @PostMapping("/{sport}/by-day/{targetId}/team")
    @UserAllowedToPlay
    public ResponseEntity<ApiResponse<Void>> createByDayTeam(
            GameServerToken token,
            @PathVariable Sport sport,
            @PathVariable String targetId,
            @Valid @RequestBody CreateByDayTeamRequest request) throws FantasySystemException {

        createByDayTeamService.execute(request.toInput(token, targetId, sport));
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
```

## Service Pattern

### Responsibility

- The service class takes care of flows that involve multiple steps and operates Aggregates, which are fetched via
  repositories.

### Input/Output

- The input should be simple VOs or primitive types. Avoid using complex objects like HttpServletRequest or
  ResponseEntity as input parameters.
- The output can be a simple VO, primitive type, or void.
- The service class should be stateless. Avoid using instance variables to store state between method calls.

### Spring Annotations

- Use @Component to define the service class.
- Use @RequiredArgsConstructor to automatically generate a constructor for final fields (dependency injection).

### package

- Place services in the `/{aggreagte name}/service` package.

### Sample

```java

@Component
@RequiredArgsConstructor
public class CreateOtpService {
    private final MobileOtpRepository mobileOtpRepository;
    private final SmsSender smsSender;

    public void execute(MobileNumber mobileNumber, User user) throws FantasySystemException {
        if (mobileOtpRepository.findOpt(user.getId()).isEmpty) {
            smsSender.sendOtp(mobileNumber);
        }
    }
}
```

## Aggregate/Entity Pattern

### Responsibility

- The aggregate/entity class encapsulates business logic and state related to a specific domain concept.
- It should enforce domain invariance, pre-condition, and post-condition.
- It should NOT depend on any framework-specific annotations or classes (e.g., Spring annotations).
- It should NOT perform any I/O operations (e.g., database access, network calls).
- It contains a ```version``` field, which has initial value ```1``` for optimistic locking.
- Every command that changes the state of the aggregate/entity should be done via a method call on the aggregate/entity
  itself.
    - Once the state is changed, the ```version``` field should be incremented by ```1```.

### Factory Methods

- Use static factory methods (e.g., ```create()```, ```restore()```) to instantiate the aggregate/entity.
    - create() is used for creating a new instance.
    - restore() is used for rehydrating an existing instance from the database.
    - The constructor should be private or protected to enforce the use of factory methods, using Lombok's
      @AllArgsConstructor(access = AccessLevel.PRIVATE).

### package

- Place aggregates/entities in the `/{aggreagte name}/entity` package.

### Sample

```java

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Tenant {
    private long id;
    private String code;
    private Long creator;
    private long createTime;
    private String secretKey;
    private String publicKey;
    private TenantType tenantType;
    private int version;

    public static Tenant create(String tenantCode, long creatorId, long createTime, TenantType tenantType, String secretKey, String publicKey) {
        Tenant tenant = new Tenant();
        tenant.setCode(tenantCode);
        tenant.setCreator(creatorId);
        tenant.setCreateTime(createTime);
        tenant.setSecretKey(secretKey);
        tenant.setPublicKey(publicKey);
        tenant.setTenantType(tenantType);
        return tenant;
    }

    public static Tenant restore(long id, String code, Long creator, long createTime, TenantType tenantType, String secretKey, String publicKey) {
        Tenant tenant = new Tenant();
        tenant.setId(id);
        tenant.setCode(code);
        tenant.setCreator(creator);
        tenant.setCreateTime(createTime);
        tenant.setSecretKey(secretKey);
        tenant.setPublicKey(publicKey);
        tenant.setTenantType(tenantType);
        return tenant;
    }

    public boolean isAllowApLevel(ActivityPointsLevel minApLevel) {
        return getAllowApLevels().contains(minApLevel);
    }

    public List<ActivityPointsLevel> getAllowApLevels() {
        if (tenantType.equals(TenantType.B2C)) {
            return getB2CAllowApLevels();
        }

        return getB2BAllowApLevels();
    }

    private static List<ActivityPointsLevel> getB2CAllowApLevels() {
        return ActivityPointsLevel.findLevelsExcludeDummy();
    }

    private static List<ActivityPointsLevel> getB2BAllowApLevels() {
        return List.of(ActivityPointsLevel.STEEL);
    }

    public TenantInfo getInfo() {
        return TenantInfo.from(this);
    }
}
```

## Repository Pattern

### Responsibility

- The repository interface defines methods for persisting and retrieving aggregates/entities.
- The repository implementation can use any persistence mechanism (e.g., in-memory, database, file system).
- The repository implementation should NOT contain business logic.

### Persistence Method Design

Developers may choose between two approaches for persistence methods (see ADR 009):

1. **Generic save()**: Use when the caller doesn't need to distinguish between insert and update operations
2. **Explicit insert()/update()**: Use when explicit operation semantics, fail-fast behavior, or domain alignment is important

Consider the tradeoffs:
- `save()` provides convenience and framework consistency
- `insert()/update()` provides explicit intent, better testability, and clearer error handling

### Spring Annotations

- Use @Component to define the repository implementation class.
- Use @RequiredArgsConstructor to automatically generate a constructor for final fields (dependency injection).

### package

- Place repository interfaces in the `/{aggreagte name}/service` package.
- Place repository implements in the `/{aggreagte name}/adapter` package.

### Samples

#### Option 1: Using save()

```java

@RequiredArgsConstructor
@Component
public class TableRepositoryInMemory implements TableRepository {
    private final Map<String, Table> tables = new HashMap<>();

    @Override
    public void save(Table table) {
        tables.put(table.getId(), table);
    }

    @Override
    public Table findById(String tableId) {
        return tables.get(tableId);
    }
}
```

#### Option 2: Using insert()/update()

```java

@RequiredArgsConstructor
@Component
public class WalletRepositoryInMemory implements WalletRepository {
    private final Map<Long, Wallet> wallets = new HashMap<>();

    @Override
    public void insert(Wallet wallet) {
        if (wallets.containsKey(wallet.getPlayerId())) {
            throw new PersistenceException("Wallet already exists for player: " + wallet.getPlayerId());
        }
        wallets.put(wallet.getPlayerId(), wallet);
    }

    @Override
    public void update(Wallet wallet) {
        if (!wallets.containsKey(wallet.getPlayerId())) {
            throw new PersistenceException("Wallet not found for player: " + wallet.getPlayerId());
        }
        wallets.put(wallet.getPlayerId(), wallet);
    }

    @Override
    public Wallet findByPlayerId(Long playerId) {
        return wallets.get(playerId);
    }
}
```


## Domain Service Pattern

### Responsibility
- A domain service encapsulates domain logic that doesn't naturally fit within an aggregate/entity.
- It should be stateless and operate on aggregates/entities passed as parameters.

### Input/Output
- The input should be aggregates/entities or simple VOs/primitive types.
- The output can be a simple VO, primitive type, or void.

### Spring Annotations
- Use @Component to define the domain service class.
- Use @RequiredArgsConstructor to automatically generate a constructor for final fields (dependency injection).
- Avoid using @Service annotation to distinguish from use case services.
- Domain services should not depend on repositories directly. Instead, they should receive aggregates/entities as parameters.

### package
- Place domain services in the `/{aggregate name}/entity` package.

### Sample
```java
public class PokerComparator {

    public PokerResult compare(List<PlayerCards> playerCards, Board board) {
        List<Map.Entry<Integer, Hand>> sortedEntries = IntStream.range(0, playerCards.size())
                .mapToObj(i -> Map.entry(i, playerCards.get(i).findBestHand(board)))
                .sorted(Map.Entry.<Integer, Hand>comparingByValue().reversed())
                .toList();

        Map<Integer, Integer> positionToRank = new HashMap<>();
        int currentRank = 1;
        for (int i = 0; i < sortedEntries.size(); i++) {
            if (i > 0 && sortedEntries.get(i).getValue().compareTo(sortedEntries.get(i - 1).getValue()) != 0) {
                currentRank = i + 1;
            }
            positionToRank.put(sortedEntries.get(i).getKey(), currentRank);
        }

        return new PokerResult(positionToRank);
    }
}
```