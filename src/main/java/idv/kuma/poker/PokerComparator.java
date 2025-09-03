package idv.kuma.poker;

import java.util.ArrayList;
import java.util.List;

public class PokerComparator {
    public int compare(List<Card> pokers1, List<Card> pokers2) {
        Card maxCard1 = pokers1.stream().max(Card::compareTo).get();
        Card maxCard2 = pokers2.stream().max(Card::compareTo).get();
        int maxComparison = maxCard1.compareTo(maxCard2);
        
        if (maxComparison != 0) {
            return maxComparison;
        }
        
        List<Card> remainingCards1 = new ArrayList<>(pokers1);
        remainingCards1.remove(maxCard1);
        Card secondMaxCard1 = remainingCards1.stream().max(Card::compareTo).get();
        
        List<Card> remainingCards2 = new ArrayList<>(pokers2);
        remainingCards2.remove(maxCard2);
        Card secondMaxCard2 = remainingCards2.stream().max(Card::compareTo).get();
        int secondMaxComparison = secondMaxCard1.compareTo(secondMaxCard2);
        
        if (secondMaxComparison != 0) {
            return secondMaxComparison;
        }
        
        remainingCards1.remove(secondMaxCard1);
        Card thirdMaxCard1 = remainingCards1.stream().max(Card::compareTo).get();
        
        remainingCards2.remove(secondMaxCard2);
        Card thirdMaxCard2 = remainingCards2.stream().max(Card::compareTo).get();
        return thirdMaxCard1.compareTo(thirdMaxCard2);
    }
}