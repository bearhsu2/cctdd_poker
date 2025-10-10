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

- Place controllers in the `/{aggreagte name}/controller` package.

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

@RequiredArgsConstructor
@Component
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

- Place aggregates/entities in the `/{aggreagte name}/domain` package.

### Sample

```java

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
