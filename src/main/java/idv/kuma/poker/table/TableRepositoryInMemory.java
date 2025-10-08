package idv.kuma.poker.table;

import idv.kuma.poker.table.usecase.TableRepository;

import java.util.HashMap;
import java.util.Map;

public class TableRepositoryInMemory implements TableRepository {
    private final Map<String, Table> tables = new HashMap<>();

    @Override
    public void save(Table table) {
        tables.put(table.getId(), table);
    }

    @Override
    public Table findById(String tableId) {
        return tables.get(tableId);
    }
}
