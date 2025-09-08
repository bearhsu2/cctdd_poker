package idv.kuma.poker;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
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
            int numberComparison = thisPair.get(0).getNumber().compareTo(otherPair.get(0).getNumber());
            if (numberComparison != 0) {
                return numberComparison;
            }
            List<Card> thisSortedPair = thisPair.stream().sorted(Collections.reverseOrder()).toList();
            List<Card> otherSortedPair = otherPair.stream().sorted(Collections.reverseOrder()).toList();
            return thisSortedPair.get(0).compareTo(otherSortedPair.get(0));
        }
        
        List<Card> sortedCards1 = this.cards.stream().sorted(Collections.reverseOrder()).toList();
        List<Card> sortedCards2 = other.cards.stream().sorted(Collections.reverseOrder()).toList();

        return IntStream.range(0, sortedCards1.size())
                .map(i -> sortedCards1.get(i).compareTo(sortedCards2.get(i)))
                .filter(comparison -> comparison != 0)
                .findFirst()
                .orElse(0);
    }
    
    private boolean hasPair() {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getNumber, Collectors.counting()))
                .values()
                .stream()
                .anyMatch(count -> count == 2);
    }
    
    private List<Card> getPairCards() {
        return cards.stream()
                .collect(Collectors.groupingBy(Card::getNumber))
                .values()
                .stream()
                .filter(group -> group.size() == 2)
                .findFirst()
                .orElseThrow();
    }
}