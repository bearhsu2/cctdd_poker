package idv.kuma.poker.common.usecase;

import idv.kuma.poker.common.entity.DomainEvent;

public interface DomainEventHandler<T extends DomainEvent> {
    void handle(T event);

}
