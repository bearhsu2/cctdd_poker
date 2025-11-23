package idv.kuma.poker.wallet.entity.exception;

public class EntityVersionConflictException extends RuntimeException {
    public EntityVersionConflictException(String message) {
        super(message);
    }
}