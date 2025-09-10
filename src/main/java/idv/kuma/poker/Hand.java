package idv.kuma.poker;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Hand implements Comparable<Hand> {
    private final List<Card> cards;
    
    public static Hand of(List<Card> cards) {
        DBCUtil.require(() -> cards.size() == 5, "Hand must contain exactly 5 cards, but got " + cards.size());
        return new Hand(cards);
    }
    
    private HandType getHandType() {
        return hasPair() ? HandType.PAIR : HandType.HIGH_CARD;
    }
    
    @Override
    public int compareTo(Hand other) {
        HandType thisType = getHandType();
        HandType otherType = other.getHandType();
        
        int typeComparison = thisType.compare(otherType);
        if (typeComparison != 0) {
            return typeComparison;
        }
        
        if (thisType == HandType.PAIR) {
            return ComparatorUtil.compareByHighestCards(getPairCards(), other.getPairCards());
        }
        
        return ComparatorUtil.compareByHighestCards(this.cards, other.cards);
    }
    
    
    private Map<Number, List<Card>> groupCardsByNumber() {
        return cards.stream().collect(Collectors.groupingBy(Card::getNumber));
    }
    
    private boolean hasPair() {
        return groupCardsByNumber().values().stream()
                .anyMatch(group -> group.size() == 2);
    }
    
    private List<Card> getPairCards() {
        return groupCardsByNumber().values().stream()
                .filter(group -> group.size() == 2)
                .findFirst()
                .orElseThrow();
    }
}