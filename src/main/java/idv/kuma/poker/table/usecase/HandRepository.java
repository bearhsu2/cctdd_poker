package idv.kuma.poker.table.usecase;

import idv.kuma.poker.table.entity.Hand;

public interface HandRepository {
    void save(Hand hand);
    Hand findById(String handId);
}
