package idv.kuma.poker.common.exception;

public class EntityExistsException extends RuntimeException {
    public EntityExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}