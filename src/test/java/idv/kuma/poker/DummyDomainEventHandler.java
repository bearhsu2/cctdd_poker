package idv.kuma.poker;

import idv.kuma.poker.common.entity.DomainEvent;
import idv.kuma.poker.common.usecase.DomainEventHandler;
import idv.kuma.poker.table.entity.TableSettledEvent;

import java.util.ArrayList;
import java.util.List;

public class DummyDomainEventHandler implements DomainEventHandler {
    private List<TableSettledEvent> receivedEvents = new ArrayList<>();

    @Override
    public void handle(DomainEvent event) {
        if (event instanceof TableSettledEvent) {
            receivedEvents.add((TableSettledEvent) event);
        }
    }

    public List<TableSettledEvent> getReceivedEvents() {
        return receivedEvents;
    }
}
