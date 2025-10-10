package idv.kuma.poker;

public interface DomainEventHandler {
    void handle(Object event);
}
