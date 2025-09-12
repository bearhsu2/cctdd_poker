package idv.kuma.poker;

import lombok.Getter;

import java.util.function.BiFunction;

public enum HandType {
    HIGH_CARD(0, (hand1, hand2) -> ComparatorUtil.compareByHighest(hand1.getCards(), hand2.getCards())),
    PAIR(1, (hand1, hand2) -> {
        int pairComparison = ComparatorUtil.compareByHighest(hand1.getPairCards(), hand2.getPairCards());
        if (pairComparison != 0) {
            return pairComparison;
        }
        return ComparatorUtil.compareByHighest(hand1.getKickerCards(), hand2.getKickerCards());
    });
    
    @Getter
    private final int weight;
    private final BiFunction<Hand, Hand, Integer> compareStrategy;
    
    HandType(int weight, BiFunction<Hand, Hand, Integer> compareStrategy) {
        this.weight = weight;
        this.compareStrategy = compareStrategy;
    }
    
    
    public int compare(HandType other) {
        return Integer.compare(this.getWeight(), other.getWeight());
    }
    
    public int compareHands(Hand hand1, Hand hand2) {
        return compareStrategy.apply(hand1, hand2);
    }
}