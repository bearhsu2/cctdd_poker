package idv.kuma.poker;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Hand {
    private final List<Card> cards;
    
    public static Hand of(List<Card> cards) {
        return new Hand(cards);
    }
    
    public int compareTo(Hand other) {
        List<Card> sortedCards1 = this.cards.stream().sorted(Collections.reverseOrder()).toList();
        List<Card> sortedCards2 = other.cards.stream().sorted(Collections.reverseOrder()).toList();

        return IntStream.range(0, sortedCards1.size())
                .map(i -> sortedCards1.get(i).compareTo(sortedCards2.get(i)))
                .filter(comparison -> comparison != 0)
                .findFirst()
                .orElse(0);
    }
}