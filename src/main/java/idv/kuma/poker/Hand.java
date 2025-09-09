package idv.kuma.poker;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Hand implements Comparable<Hand> {
    private final List<Card> cards;
    
    public static Hand of(List<Card> cards) {
        DBCUtil.require(() -> cards.size() == 5, "Hand must contain exactly 5 cards, but got " + cards.size());
        return new Hand(cards);
    }
    
    @Override
    public int compareTo(Hand other) {
        if (hasPair() && !other.hasPair()) {
            return 1;
        }
        if (!hasPair() && other.hasPair()) {
            return -1;
        }
        if (hasPair() && other.hasPair()) {
            List<Card> thisPair = getPairCards();
            List<Card> otherPair = other.getPairCards();
            return compareByHighestCards(thisPair, otherPair);
        }
        
        return compareByHighestCards(this.cards, other.cards);
    }
    
    private int compareByHighestCards(List<Card> cards1, List<Card> cards2) {
        List<Card> sortedCards1 = cards1.stream().sorted(Collections.reverseOrder()).toList();
        List<Card> sortedCards2 = cards2.stream().sorted(Collections.reverseOrder()).toList();
        
        return IntStream.range(0, sortedCards1.size())
                .map(i -> sortedCards1.get(i).compareTo(sortedCards2.get(i)))
                .filter(comparison -> comparison != 0)
                .findFirst()
                .orElse(0);
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