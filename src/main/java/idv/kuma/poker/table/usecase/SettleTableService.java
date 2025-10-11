package idv.kuma.poker.table.usecase;

import idv.kuma.poker.common.usecase.DomainEventBus;
import idv.kuma.poker.table.entity.PokerComparator;
import idv.kuma.poker.table.entity.PokerResult;
import idv.kuma.poker.table.entity.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SettleTableService {
    private final TableRepository tableRepository;
    private final DomainEventBus domainEventBus;
    private final PokerComparator pokerComparator;

    public void settle(String tableId) {
        Table table = tableRepository.findById(tableId);
        PokerResult pokerResult = pokerComparator.compare(table.getPlayerCards(), table.getBoard());
        table.settle(pokerResult);
        tableRepository.save(table);
        for (Object event : table.flushDomainEvents()) {
            domainEventBus.publish(event);
        }
    }
}
