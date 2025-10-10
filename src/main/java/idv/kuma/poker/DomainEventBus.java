package idv.kuma.poker;

public interface DomainEventBus {
    void register(DomainEventHandler handler);

    void publish(Object event);
}
