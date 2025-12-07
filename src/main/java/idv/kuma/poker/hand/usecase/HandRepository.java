package idv.kuma.poker.hand.usecase;

import idv.kuma.poker.hand.entity.Hand;

public interface HandRepository {
    void save(Hand hand);
    Hand findById(String handId);
}
