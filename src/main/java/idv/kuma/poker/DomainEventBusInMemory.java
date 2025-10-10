package idv.kuma.poker;

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
