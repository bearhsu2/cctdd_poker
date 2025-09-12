package idv.kuma.poker;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Hand implements Comparable<Hand> {
    @Getter
    private final List<Card> cards;
    private final HandType handType;
    
    private Hand(List<Card> cards) {
        this.cards = cards;
        this.handType = calculateHandType();
    }
    
    public static Hand of(List<Card> cards) {
        DBCUtil.require(() -> cards.size() == 5, "Hand must contain exactly 5 cards, but got " + cards.size());
        return new Hand(cards);
    }
    
    private HandType calculateHandType() {
        return hasPair() ? HandType.PAIR : HandType.HIGH_CARD;
    }
    
    private boolean hasPair() {
        return groupCardsByNumber().values().stream()
                .anyMatch(group -> group.size() == 2);
    }
    
    private Map<Number, List<Card>> groupCardsByNumber() {
        return cards.stream().collect(Collectors.groupingBy(Card::getNumber));
    }
    
    private HandType getHandType() {
        return handType;
    }
    
    @Override
    public int compareTo(Hand other) {
        HandType thisType = getHandType();
        HandType otherType = other.getHandType();
        
        int typeComparison = thisType.compare(otherType);
        if (typeComparison != 0) {
            return typeComparison;
        }
        
        return thisType.compareHands(this, other);
    }
    
    
    
    public List<Card> getPairCards() {
        return groupCardsByNumber().values().stream()
                .filter(group -> group.size() == 2)
                .findFirst()
                .orElseThrow();
    }
    
    public List<Card> getKickerCards() {
        return groupCardsByNumber().values().stream()
                .filter(group -> group.size() == 1)
                .flatMap(List::stream)
                .toList();
    }
}