package idv.kuma.poker;

import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum HandType {
    HIGH_CARD(0, hand -> true, (hand1, hand2) -> ComparatorUtil.compareByHighest(hand1.getCards(), hand2.getCards())),
    ONE_PAIR(1, Hand::hasOnePair, (hand1, hand2) -> {
        int pairComparison = ComparatorUtil.compareByHighest(hand1.getPairCards(), hand2.getPairCards());
        if (pairComparison != 0) {
            return pairComparison;
        }
        return ComparatorUtil.compareByHighest(hand1.getKickerCards(), hand2.getKickerCards());
    }),
    TWO_PAIR(2, Hand::hasTwoPair, (hand1, hand2) -> {
        int highPairComparison = ComparatorUtil.compareByHighest(hand1.getHighPairCards(), hand2.getHighPairCards());
        if (highPairComparison != 0) {
            return highPairComparison;
        }
        int lowPairComparison = ComparatorUtil.compareByHighest(hand1.getLowPairCards(), hand2.getLowPairCards());
        if (lowPairComparison != 0) {
            return lowPairComparison;
        }
        return ComparatorUtil.compareByHighest(hand1.getKickerCards(), hand2.getKickerCards());
    });

    @Getter
    private final int weight;
    private final Function<Hand, Boolean> checker;
    private final BiFunction<Hand, Hand, Integer> compareStrategy;

    HandType(int weight, Function<Hand, Boolean> checker, BiFunction<Hand, Hand, Integer> compareStrategy) {
        this.weight = weight;
        this.checker = checker;
        this.compareStrategy = compareStrategy;
    }

    static HandType from(Hand hand) {
        return Arrays.stream(values())
                .sorted(Comparator.comparing(HandType::getWeight).reversed())
                .filter(type -> type.checker.apply(hand))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unknown hand type: " + hand));
    }

    public int compare(HandType other) {
        return Integer.compare(this.getWeight(), other.getWeight());
    }

    public int compareHands(Hand hand1, Hand hand2) {
        return compareStrategy.apply(hand1, hand2);
    }
}