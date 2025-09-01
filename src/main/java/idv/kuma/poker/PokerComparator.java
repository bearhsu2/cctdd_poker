package idv.kuma.poker;

import java.util.List;

public class PokerComparator {
    public int compare(List<Card> pokers1, List<Card> pokers2) {
        Card maxCard1 = pokers1.stream().max(Card::compareTo).get();
        Card maxCard2 = pokers2.stream().max(Card::compareTo).get();
        return maxCard1.compareTo(maxCard2);
    }
}