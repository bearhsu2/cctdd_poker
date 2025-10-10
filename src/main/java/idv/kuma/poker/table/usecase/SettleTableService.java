package idv.kuma.poker.table.usecase;

import idv.kuma.poker.common.usecase.DomainEventBus;
import idv.kuma.poker.table.entity.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SettleTableService {
    private final TableRepository tableRepository;
    private final DomainEventBus domainEventBus;

    public void settle(String tableId) {
        Table table = tableRepository.findById(tableId);
        table.settle();
        tableRepository.save(table);
        for (Object event : table.flushDomainEvents()) {
            domainEventBus.publish(event);
        }
    }
}
