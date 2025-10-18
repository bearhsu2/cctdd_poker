package idv.kuma.poker.wallet.adapter;

import idv.kuma.poker.common.entity.DomainEvent;
import idv.kuma.poker.common.usecase.DomainEventHandler;
import idv.kuma.poker.table.entity.HandSettledEvent;
import idv.kuma.poker.table.entity.PlayerResult;
import idv.kuma.poker.wallet.usecase.AddBalanceService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateWalletEventHandler implements DomainEventHandler {
    private final AddBalanceService addBalanceService;

    @Override
    public void handle(DomainEvent event) {
        if (event instanceof HandSettledEvent handSettledEvent) {
            PlayerResult rank1Winner = handSettledEvent.handResult().getPositionToResult().values().stream()
                    .filter(result -> result.getRank() == 1)
                    .findFirst()
                    .orElse(null);

            if (rank1Winner != null) {
                addBalanceService.addBalance(rank1Winner.getUserId(), handSettledEvent.bet());
            }
        }
    }
}