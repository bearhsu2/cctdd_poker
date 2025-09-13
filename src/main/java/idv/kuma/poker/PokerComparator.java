package idv.kuma.poker;

public class PokerComparator {

    // https://zh.wikipedia.org/zh-tw/%E6%92%B2%E5%85%8B%E7%89%8C%E5%9E%8B
    public int compare(Hand hand1, Hand hand2) {
        return hand1.compareTo(hand2);
    }
}