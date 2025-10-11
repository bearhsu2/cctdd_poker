package idv.kuma.poker.common.usecase;

import idv.kuma.poker.common.entity.DomainEvent;

public interface DomainEventBus {
    void publish(DomainEvent event);
}
