package idv.kuma.poker.table.entity;

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
    private List<Object> domainEvents;

    public static Table create(String id) {
        return new Table(id, TableStatus.CREATED, 1, new ArrayList<>());
    }

    public static Table restore(String id, TableStatus status, int version) {
        return new Table(id, status, version, new ArrayList<>());
    }

    public void settle() {
        this.status = TableStatus.SETTLED;
        this.version++;
        this.domainEvents.add(new TableSettledEvent(id));
    }

    public List<Object> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}
