package idv.kuma.poker;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Card {
    private final Suit suit;
    private final Number cardNumber;
    
    public static Card of(Suit suit, Number cardNumber) {
        return new Card(suit, cardNumber);
    }
    
    public int compareTo(Card other) {
        int cardNumberComparison = Integer.compare(this.cardNumber.getNumber(), other.cardNumber.getNumber());
        if (cardNumberComparison != 0) {
            return cardNumberComparison;
        }
        
        // When card numbers are equal, compare suits by weight: higher weight wins
        return Integer.compare(this.suit.getWeight(), other.suit.getWeight());
    }
}