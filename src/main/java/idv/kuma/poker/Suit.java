package idv.kuma.poker;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Suit {
    SPADE(4),
    HEART(3),
    CLUB(2),
    DIAMOND(1);
    
    private final int weight;
}