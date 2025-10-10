package idv.kuma.poker.table.usecase;

import idv.kuma.poker.table.entity.Table;

public interface TableRepository {
    void save(Table table);
    Table findById(String tableId);
}
