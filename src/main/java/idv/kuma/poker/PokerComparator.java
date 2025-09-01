package idv.kuma.poker;

public class PokerComparator {
    public int compare(java.util.List<Card> poker1, java.util.List<Card> poker2) {
        return poker1.get(0).compareTo(poker2.get(0));
    }
}