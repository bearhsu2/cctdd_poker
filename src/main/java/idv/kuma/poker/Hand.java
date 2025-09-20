package idv.kuma.poker;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Hand implements Comparable<Hand> {
    @Getter
    private final List<Card> cards;
    private final HandType handType;
    
    private Hand(List<Card> cards) {
        this.cards = cards;
        this.handType = HandType.from(this);
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

    private HandType getHandType() {
        return handType;
    }

    @Override
    public int compareTo(Hand other) {
        int typeComparison = handType.compare(other.getHandType());
        if (typeComparison != 0) {
            return typeComparison;
        }

        return handType.compareHands(this, other);
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

    boolean hasStraight() {
        List<Integer> sortedNumbers = cards.stream()
                .map(card -> card.getNumber().getNumber())
                .distinct()
                .sorted()
                .toList();

        if (sortedNumbers.equals(List.of(2, 3, 4, 5, 14))) {
            return true;
        }

        return sortedNumbers.size() == 5 &&
               sortedNumbers.get(4) - sortedNumbers.get(0) == 4;
    }
}