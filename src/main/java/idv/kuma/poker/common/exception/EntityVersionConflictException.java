package idv.kuma.poker.common.exception;

public class EntityVersionConflictException extends RuntimeException {
    public EntityVersionConflictException(String message) {
        super(message);
    }
}
