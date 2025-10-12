package idv.kuma.poker.gamehistory.adapter;

import idv.kuma.poker.common.usecase.DomainEventHandler;
import idv.kuma.poker.gamehistory.usecase.AddGameHistoryService;
import idv.kuma.poker.table.entity.TableSettledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AddGameHistoryEventHandler implements DomainEventHandler<TableSettledEvent> {
    private final AddGameHistoryService addGameHistoryService;

    @Override
    public void handle(TableSettledEvent event) {
        addGameHistoryService.execute(
                event.getTableId(),
                event.getPokerResult()
        );
    }
}