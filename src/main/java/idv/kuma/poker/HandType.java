package idv.kuma.poker;

public enum HandType {
    HIGH_CARD(0),
    PAIR(1);
    
    private final int rank;
    
    HandType(int rank) {
        this.rank = rank;
    }
    
    public int getRank() {
        return rank;
    }
}