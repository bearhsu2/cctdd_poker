package idv.kuma.poker.gamehistory.adapter;

import idv.kuma.poker.common.entity.DomainEvent;
import idv.kuma.poker.common.usecase.DomainEventHandler;
import idv.kuma.poker.gamehistory.usecase.AddGameHistoryService;
import idv.kuma.poker.table.entity.HandSettledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AddGameHistoryEventHandler implements DomainEventHandler {
    private final AddGameHistoryService addGameHistoryService;

    @Override
    public void handle(DomainEvent event) {
        if (event instanceof HandSettledEvent handSettledEvent) {
            addGameHistoryService.execute(
                    handSettledEvent.handId(),
                    handSettledEvent.handResult()
            );
        }
    }
}