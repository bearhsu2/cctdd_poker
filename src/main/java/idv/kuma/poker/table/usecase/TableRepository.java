package idv.kuma.poker.table.usecase;

import idv.kuma.poker.table.Table;

public interface TableRepository {
    void save(Table table);
    Table findById(String tableId);
}
