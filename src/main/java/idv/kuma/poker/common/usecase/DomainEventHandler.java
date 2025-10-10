package idv.kuma.poker.common.usecase;

public interface DomainEventHandler {
    void handle(Object event);
}
