package idv.kuma.poker;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Table {
    private final String id;
    private TableStatus status;
    private int version;

    public static Table create(String id) {
        return new Table(id, TableStatus.CREATED, 1);
    }

    public static Table restore(String id, TableStatus status, int version) {
        return new Table(id, status, version);
    }

    public void settle() {
        this.status = TableStatus.SETTLED;
        this.version++;
    }
}
