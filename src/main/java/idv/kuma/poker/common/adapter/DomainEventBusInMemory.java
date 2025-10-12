package idv.kuma.poker.common.adapter;

import idv.kuma.poker.common.entity.DomainEvent;
import idv.kuma.poker.common.usecase.DomainEventBus;
import idv.kuma.poker.common.usecase.DomainEventHandler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DomainEventBusInMemory implements DomainEventBus {
    private final List<DomainEventHandler> handlers;

    public DomainEventBusInMemory(DomainEventHandler... handlers) {
        this.handlers = Arrays.asList(handlers);
    }

    @Override
    public void publish(DomainEvent event) {
        for (DomainEventHandler handler : handlers) {
            handler.handle(event);
        }
    }
}
