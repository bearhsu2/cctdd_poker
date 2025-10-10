package idv.kuma.poker.common.adapter;

import idv.kuma.poker.common.usecase.DomainEventBus;
import idv.kuma.poker.common.usecase.DomainEventHandler;

import java.util.ArrayList;
import java.util.List;

public class DomainEventBusInMemory implements DomainEventBus {
    private List<DomainEventHandler> handlers = new ArrayList<>();

    @Override
    public void register(DomainEventHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void publish(Object event) {
        for (DomainEventHandler handler : handlers) {
            handler.handle(event);
        }
    }
}
