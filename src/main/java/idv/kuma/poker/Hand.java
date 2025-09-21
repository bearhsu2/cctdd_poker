package idv.kuma.poker;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Hand implements Comparable<Hand> {
    @Getter
    private final List<Card> cards;
    private final Category category;

    private Hand(List<Card> cards) {
        this.cards = cards;
        this.category = Category.from(this);
    }

    public static Hand of(List<Card> cards) {
        DBCUtil.require(() -> cards.size() == 5, "Hand must contain exactly 5 cards, but got " + cards.size());
        return new Hand(cards);
    }


    boolean hasOnePair() {
        return getAllPairs().size() == 1;
    }

    private List<List<Card>> getAllPairs() {
        return groupCardsByNumber().values().stream()
                .filter(group -> group.size() == 2)
                .toList();
    }

    private Map<Number, List<Card>> groupCardsByNumber() {
        return cards.stream().collect(Collectors.groupingBy(Card::getNumber));
    }

    @Override
    public int compareTo(Hand other) {
        int typeComparison = category.compare(other.getCategory());
        if (typeComparison != 0) {
            return typeComparison;
        }

        return category.compareHands(this, other);
    }

    private Category getCategory() {
        return category;
    }

    public List<Card> getPairCards() {
        return getAllPairs().stream()
                .findFirst()
                .orElseThrow();
    }

    public List<Card> getKickerCards() {
        return groupCardsByNumber().values().stream()
                .filter(group -> group.size() == 1)
                .flatMap(List::stream)
                .toList();
    }

    boolean hasTwoPair() {
        return getAllPairs().size() == 2;
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

    boolean hasThreeOfAKind() {
        return findThreeOfAKindCards().isPresent();
    }

    private Optional<List<Card>> findThreeOfAKindCards() {
        return groupCardsByNumber().values().stream()
                .filter(group -> group.size() == 3)
                .findFirst();
    }

    public List<Card> getThreeOfAKindCards() {
        return findThreeOfAKindCards().orElseThrow();
    }

    public int getStraightHighValue() {
        DBCUtil.require(this::hasStraight, "getStraightHighValue can only be called on a straight hand");

        List<Integer> sortedNumbers = getSortedNumbers();

        if (isAceLowStraight(sortedNumbers)) {
            return 5;
        }

        return sortedNumbers.get(4);
    }

    boolean hasStraight() {
        List<Integer> sortedNumbers = getSortedNumbers();

        if (isAceLowStraight(sortedNumbers)) {
            return true;
        }

        return sortedNumbers.size() == 5 &&
                sortedNumbers.get(4) - sortedNumbers.get(0) == 4;
    }

    private List<Integer> getSortedNumbers() {
        return cards.stream()
                .map(card -> card.getNumber().getNumber())
                .distinct()
                .sorted()
                .toList();
    }

    private boolean isAceLowStraight(List<Integer> sortedNumbers) {
        return sortedNumbers.equals(List.of(2, 3, 4, 5, 14));
    }

    boolean hasFlush() {
        return cards.stream()
                .map(Card::getSuit)
                .distinct()
                .count() == 1;
    }

    public List<Number> getNumbers() {
        return cards.stream().map(Card::getNumber).toList();
    }

    boolean hasFullHouse() {
        return hasThreeOfAKind() && hasOnePair();
    }

    public Number getTripletNumber() {
        return getThreeOfAKindCards().get(0).getNumber();
    }
}