package idv.kuma.poker;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Hand implements Comparable<Hand> {
    private final List<Card> cards;
    private final HandType handType;
    
    public static Hand of(List<Card> cards) {
        DBCUtil.require(() -> cards.size() == 5, "Hand must contain exactly 5 cards, but got " + cards.size());
        HandType handType = calculateHandType(cards);
        return new Hand(cards, handType);
    }
    
    private static HandType calculateHandType(List<Card> cards) {
        return hasPair(cards) ? HandType.PAIR : HandType.HIGH_CARD;
    }
    
    private static boolean hasPair(List<Card> cards) {
        return groupCardsByNumber(cards).values().stream()
                .anyMatch(group -> group.size() == 2);
    }
    
    private static Map<Number, List<Card>> groupCardsByNumber(List<Card> cards) {
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
        
        if (thisType == HandType.PAIR) {
            return ComparatorUtil.compareByHighest(getPairCards(), other.getPairCards());
        }
        
        return ComparatorUtil.compareByHighest(this.cards, other.cards);
    }
    
    
    private List<Card> getPairCards() {
        return groupCardsByNumber(cards).values().stream()
                .filter(group -> group.size() == 2)
                .findFirst()
                .orElseThrow();
    }
}