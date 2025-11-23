package idv.kuma.poker.common.exception;

public class EntityExistsException extends Exception {
    public EntityExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}