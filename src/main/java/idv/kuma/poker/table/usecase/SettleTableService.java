package idv.kuma.poker.table.usecase;

import idv.kuma.poker.table.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SettleTableService {
    private final TableRepository tableRepository;

    public void settle(String tableId) {
        Table table = tableRepository.findById(tableId);
        table.settle();
        tableRepository.save(table);
    }
}
