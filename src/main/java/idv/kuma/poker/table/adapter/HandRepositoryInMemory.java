package idv.kuma.poker.table.adapter;

import idv.kuma.poker.table.entity.Hand;
import idv.kuma.poker.table.usecase.HandRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HandRepositoryInMemory implements HandRepository {
    private final Map<String, Hand> hands = new HashMap<>();

    @Override
    public void save(Hand hand) {
        hands.put(hand.getId(), hand);
    }

    @Override
    public Hand findById(String handId) {
        return hands.get(handId);
    }
}
