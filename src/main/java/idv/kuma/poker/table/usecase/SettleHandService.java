package idv.kuma.poker.table.usecase;

import idv.kuma.poker.common.entity.DomainEvent;
import idv.kuma.poker.common.usecase.DomainEventBus;
import idv.kuma.poker.table.entity.PokerComparator;
import idv.kuma.poker.table.entity.HandResult;
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
        HandResult handResult = pokerComparator.compare(hand.getUserIds(), hand.getHoleCards(), hand.getBoard());
        hand.settle(handResult);
        handRepository.save(hand);
        for (DomainEvent event : hand.flushDomainEvents()) {
            domainEventBus.publish(event);
        }
    }
}
