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
}