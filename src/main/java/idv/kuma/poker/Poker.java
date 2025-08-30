package idv.kuma.poker;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Poker {
    private final int number;
    
    public static Poker with(String card) {
        return new Poker(convertCardToNumber(card));
    }
    
    private static int convertCardToNumber(String card) {
        switch (card) {
            case "J":
                return 11;
            case "Q":
                return 12;
            case "K":
                return 13;
            case "A":
                return 14;
            default:
                try {
                    return Integer.parseInt(card);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Unknown card: " + card);
                }
        }
    }
}