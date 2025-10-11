package idv.kuma.poker.table.entity;

import idv.kuma.poker.common.entity.DomainEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Table {
    private final String id;
    private TableStatus status;
    private int version;
    private List<DomainEvent> domainEvents;
    private List<PlayerCards> playerCards;
    private Board board;

    public static Table create(String id) {
        return new Table(id, TableStatus.CREATED, 1, new ArrayList<>(), new ArrayList<>(), null);
    }

    public static Table restore(String id, TableStatus status, int version, List<PlayerCards> playerCards, Board board) {
        return new Table(id, status, version, new ArrayList<>(), playerCards, board);
    }

    public void settle(PokerResult pokerResult) {
        this.status = TableStatus.SETTLED;
        this.version++;
        this.domainEvents.add(new TableSettledEvent(id, pokerResult));
    }

    public List<DomainEvent> flushDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }
}
