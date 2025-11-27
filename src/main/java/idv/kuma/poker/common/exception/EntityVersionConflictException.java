package idv.kuma.poker.common.exception;

public class EntityVersionConflictException extends PokerGameException {
    public EntityVersionConflictException(String message) {
        super(message);
    }
}
