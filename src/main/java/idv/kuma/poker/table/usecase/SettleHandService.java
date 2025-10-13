package idv.kuma.poker.table.usecase;

import idv.kuma.poker.common.entity.DomainEvent;
import idv.kuma.poker.common.usecase.DomainEventBus;
import idv.kuma.poker.table.entity.PokerComparator;
import idv.kuma.poker.table.entity.PokerResult;
import idv.kuma.poker.table.entity.Hand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SettleHandService {
    private final HandRepository handRepository;
    private final DomainEventBus domainEventBus;
    private final PokerComparator pokerComparator;

    public void settle(String handId) {
        Hand hand = handRepository.findById(handId);
        PokerResult pokerResult = pokerComparator.compare(hand.getPlayerCards(), hand.getBoard());
        hand.settle(pokerResult);
        handRepository.save(hand);
        for (DomainEvent event : hand.flushDomainEvents()) {
            domainEventBus.publish(event);
        }
    }
}
