package idv.kuma.poker.table.adapter;

import idv.kuma.poker.table.entity.Table;
import idv.kuma.poker.table.usecase.TableRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
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
