package idv.kuma.poker;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Card {
    private final Suit suit;
    private final CardNumber cardNumber;
}