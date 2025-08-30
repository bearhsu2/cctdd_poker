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
}