package idv.kuma.poker;

import idv.kuma.poker.table.entity.TableSettledEvent;

import java.util.ArrayList;
import java.util.List;

public class DummyDomainEventHandler implements DomainEventHandler {
    private List<TableSettledEvent> receivedEvents = new ArrayList<>();

    @Override
    public void handle(Object event) {
        if (event instanceof TableSettledEvent) {
            receivedEvents.add((TableSettledEvent) event);
        }
    }

    public List<TableSettledEvent> getReceivedEvents() {
        return receivedEvents;
    }
}
