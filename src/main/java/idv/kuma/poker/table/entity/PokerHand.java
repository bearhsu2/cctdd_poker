package idv.kuma.poker.table.entity;

import idv.kuma.poker.common.ComparatorUtil;
import idv.kuma.poker.common.DBCUtil;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class PokerHand implements Comparable<PokerHand> {
    public static final int HAND_SIZE = 5;

    @Getter
    private final List<Card> cards;
    private final Category category;

    private PokerHand(List<Card> cards) {
        this.cards = cards;
        this.category = Category.from(this);
    }

    public static PokerHand of(List<Card> cards) {
        DBCUtil.require(() -> cards.size() == HAND_SIZE, "Hand must contain exactly " + HAND_SIZE + " cards, but got " + cards.size());
        return new PokerHand(cards);
    }

    @Override
    public int compareTo(PokerHand other) {
        int typeComparison = category.compare(other.getCategory());
        if (typeComparison != 0) {
            return typeComparison;
        }

        return category.compareHands(this, other);
    }

    private Category getCategory() {
        return category;
    }

    public Number getKickerNumber() {
        return getKickerCards().stream()
                .map(Card::number)
                .findFirst()
                .orElseThrow();
    }

    public List<Card> getKickerCards() {
        return groupCardsByNumber().values().stream()
                .filter(group -> group.size() == 1)
                .flatMap(List::stream)
                .toList();
    }

    private Map<Number, List<Card>> groupCardsByNumber() {
        return cards.stream().collect(Collectors.groupingBy(Card::number));
    }

    boolean hasTwoPair() {
        return getAllPairs().size() == 2;
    }

    private List<List<Card>> getAllPairs() {
        return groupCardsByNumber().values().stream()
                .filter(group -> group.size() == 2)
                .toList();
    }

    public List<Card> getHighPairCards() {
        return getAllPairs().stream()
                .max(ComparatorUtil::compareByHighest)
                .orElseThrow();
    }

    public List<Card> getLowPairCards() {
        return getAllPairs().stream()
                .min(ComparatorUtil::compareByHighest)
                .orElseThrow();
    }

    public int getStraightHighValue() {
        DBCUtil.require(this::hasStraight, "getStraightHighValue can only be called on a straight hand");

        List<Integer> sortedNumbers = getSortedNumbers();

        if (isAceLowStraight(sortedNumbers)) {
            return 5;
        }

        return sortedNumbers.get(HAND_SIZE - 1);
    }

    boolean hasStraight() {
        List<Integer> sortedNumbers = getSortedNumbers();

        if (isAceLowStraight(sortedNumbers)) {
            return true;
        }

        return sortedNumbers.size() == HAND_SIZE &&
                sortedNumbers.get(HAND_SIZE - 1) - sortedNumbers.get(0) == HAND_SIZE - 1;
    }

    private List<Integer> getSortedNumbers() {
        return cards.stream()
                .map(card -> card.number().getNumber())
                .distinct()
                .sorted()
                .toList();
    }

    private boolean isAceLowStraight(List<Integer> sortedNumbers) {
        return sortedNumbers.equals(List.of(2, 3, 4, 5, 14));
    }

    boolean hasFlush() {
        return cards.stream()
                .map(Card::suit)
                .distinct()
                .count() == 1;
    }

    public List<Number> getNumbers() {
        return cards.stream().map(Card::number).toList();
    }

    boolean hasFullHouse() {
        return hasThreeOfAKind() && hasOnePair();
    }

    boolean hasStraightFlush() {
        return hasStraight() && hasFlush();
    }

    boolean hasThreeOfAKind() {
        return tryFindTripletCards().isPresent();
    }

    boolean hasOnePair() {
        return getAllPairs().size() == 1;
    }

    private Optional<List<Card>> tryFindTripletCards() {
        return groupCardsByNumber().values().stream()
                .filter(group -> group.size() == 3)
                .findFirst();
    }

    public Number getTripletNumber() {
        return getTripletCards().get(0).number();
    }

    public List<Card> getTripletCards() {
        return tryFindTripletCards().orElseThrow();
    }

    public Number getPairNumber() {
        return getPairCards().get(0).number();
    }

    public List<Card> getPairCards() {
        return getAllPairs().stream()
                .findFirst()
                .orElseThrow();
    }

    boolean hasFourOfAKind() {
        return tryFindQuadrupletCards().isPresent();
    }

    private Optional<List<Card>> tryFindQuadrupletCards() {
        return groupCardsByNumber().values().stream()
                .filter(group -> group.size() == 4)
                .findFirst();
    }

    public Number getQuadrupletNumber() {
        return getQuadrupletCards().get(0).number();
    }

    public List<Card> getQuadrupletCards() {
        return tryFindQuadrupletCards().orElseThrow();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PokerHand hand = (PokerHand) obj;
        return new HashSet<>(cards).equals(new HashSet<>(hand.cards));
    }

    @Override
    public int hashCode() {
        return Objects.hash(cards);
    }
}