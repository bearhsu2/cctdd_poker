package idv.kuma.poker.table;

import idv.kuma.poker.common.ComparatorUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum Category {
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
    }),
    THREE_OF_A_KIND(3, Hand::hasThreeOfAKind, (hand1, hand2) -> {
        int threeOfAKindComparison = ComparatorUtil.compareByHighest(hand1.getTripletCards(), hand2.getTripletCards());
        if (threeOfAKindComparison != 0) {
            return threeOfAKindComparison;
        }
        return ComparatorUtil.compareByHighest(hand1.getKickerCards(), hand2.getKickerCards());
    }),
    STRAIGHT(4, Hand::hasStraight, (hand1, hand2) -> Integer.compare(hand1.getStraightHighValue(), hand2.getStraightHighValue())),
    FLUSH(5, Hand::hasFlush, (hand1, hand2) -> ComparatorUtil.compareByHighest(hand1.getNumbers(), hand2.getNumbers())),
    FULL_HOUSE(6, Hand::hasFullHouse, (hand1, hand2) -> {
        int tripletComparison = hand1.getTripletNumber().compareValue(hand2.getTripletNumber());
        if (tripletComparison != 0) {
            return tripletComparison;
        }
        return hand1.getPairNumber().compareValue(hand2.getPairNumber());
    }),
    FOUR_OF_A_KIND(7, Hand::hasFourOfAKind, (hand1, hand2) -> {
        int quadrupletComparison = hand1.getQuadrupletNumber().compareValue(hand2.getQuadrupletNumber());
        if (quadrupletComparison != 0) {
            return quadrupletComparison;
        }
        return hand1.getKickerNumber().compareValue(hand2.getKickerNumber());
    }),
    STRAIGHT_FLUSH(8, Hand::hasStraightFlush, (hand1, hand2) -> Integer.compare(hand1.getStraightHighValue(), hand2.getStraightHighValue()));

    @Getter
    private final int weight;
    private final Function<Hand, Boolean> checker;
    private final BiFunction<Hand, Hand, Integer> compareStrategy;

    Category(int weight, Function<Hand, Boolean> checker, BiFunction<Hand, Hand, Integer> compareStrategy) {
        this.weight = weight;
        this.checker = checker;
        this.compareStrategy = compareStrategy;
    }

    static Category from(Hand hand) {
        return Arrays.stream(values())
                .sorted(Comparator.comparing(Category::getWeight).reversed())
                .filter(type -> type.checker.apply(hand))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unknown hand type: " + hand));
    }

    public int compare(Category other) {
        return Integer.compare(this.getWeight(), other.getWeight());
    }

    public int compareHands(Hand hand1, Hand hand2) {
        return compareStrategy.apply(hand1, hand2);
    }
}