package idv.kuma.poker;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Poker {
    private final int number;
    
    public static Poker with(int number) {
        return new Poker(number);
    }
    
    public static Poker with(String card) {
        return new Poker(convertCardToNumber(card));
    }
    
    private static int convertCardToNumber(String card) {
        switch (card) {
            case "J":
                return 11;
            default:
                throw new IllegalArgumentException("Unknown card: " + card);
        }
    }
}