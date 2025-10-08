package idv.kuma.poker.usecase;

import idv.kuma.poker.Table;

public interface TableRepository {
    void save(Table table);
    Table findById(String tableId);
}
