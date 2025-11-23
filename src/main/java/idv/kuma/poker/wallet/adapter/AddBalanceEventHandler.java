package idv.kuma.poker.wallet.adapter;

import idv.kuma.poker.common.entity.DomainEvent;
import idv.kuma.poker.common.exception.EntityExistsException;
import idv.kuma.poker.common.usecase.DomainEventHandler;
import idv.kuma.poker.table.entity.HandSettledEvent;
import idv.kuma.poker.table.entity.PlayerResult;
import idv.kuma.poker.wallet.usecase.AddBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddBalanceEventHandler implements DomainEventHandler {
    private final AddBalanceService addBalanceService;

    @Override
    public void handle(DomainEvent event) {
        if (event instanceof HandSettledEvent handSettledEvent) {
            PlayerResult rank1Winner = handSettledEvent.handResult().getRank1Winner();
            try {
                addBalanceService.addBalance(rank1Winner.userId(), handSettledEvent.bet());
            } catch (EntityExistsException e) {
                throw new RuntimeException("Failed to add balance", e);
            }
        }
    }
}