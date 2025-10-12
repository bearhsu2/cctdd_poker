package idv.kuma.poker;

import idv.kuma.poker.common.usecase.DomainEventHandler;
import idv.kuma.poker.table.entity.TableSettledEvent;

import java.util.ArrayList;
import java.util.List;

public class DummyDomainEventHandler implements DomainEventHandler<TableSettledEvent> {
    private List<TableSettledEvent> receivedEvents = new ArrayList<>();

    @Override
    public void handle(TableSettledEvent event) {
        receivedEvents.add(event);
    }

    public List<TableSettledEvent> getReceivedEvents() {
        return receivedEvents;
    }
}
