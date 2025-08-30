package idv.kuma.poker;

import lombok.Getter;

@Getter
public class Card {
    private final Suit suit;
    private final CardNumber cardNumber;
    
    private Card(Suit suit, CardNumber cardNumber) {
        this.suit = suit;
        this.cardNumber = cardNumber;
    }
    
    public static Card of(Suit suit, CardNumber cardNumber) {
        return new Card(suit, cardNumber);
    }
}