package idv.kuma.poker.common.exception;

public class PokerGameException extends Exception {
    public PokerGameException(String message) {
        super(message);
    }

    public PokerGameException(String message, Throwable cause) {
        super(message, cause);
    }
}