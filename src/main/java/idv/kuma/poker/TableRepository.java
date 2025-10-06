package idv.kuma.poker;

public interface TableRepository {
    void save(Table table);
    Table findById(String tableId);
}
