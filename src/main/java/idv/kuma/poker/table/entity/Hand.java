package idv.kuma.poker.table.entity;

import idv.kuma.poker.common.entity.DomainEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Hand {
    private final String id;
    private HandStatus status;
    private int version;
    private List<DomainEvent> domainEvents;
    private List<HoleCards> holeCards;
    private Board board;

    public static Hand create(String id) {
        return new Hand(id, HandStatus.CREATED, 1, new ArrayList<>(), new ArrayList<>(), null);
    }

    public static Hand restore(String id, HandStatus status, int version, List<HoleCards> holeCards, Board board) {
        return new Hand(id, status, version, new ArrayList<>(), holeCards, board);
    }

    public void settle(PokerResult pokerResult) {
        this.status = HandStatus.SETTLED;
        this.version++;
        this.domainEvents.add(new HandSettledEvent(id, pokerResult));
    }

    public List<DomainEvent> flushDomainEvents() {
        List<DomainEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }
}
