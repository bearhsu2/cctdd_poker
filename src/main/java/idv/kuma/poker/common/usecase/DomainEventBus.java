package idv.kuma.poker.common.usecase;

public interface DomainEventBus {
    void register(DomainEventHandler handler);

    void publish(Object event);
}
