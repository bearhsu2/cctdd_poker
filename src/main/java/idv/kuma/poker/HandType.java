package idv.kuma.poker;

public enum HandType {
    HIGH_CARD(0),
    PAIR(1);
    
    private final int weight;
    
    HandType(int weight) {
        this.weight = weight;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public static int compare(HandType thisType, HandType otherType) {
        return Integer.compare(thisType.getWeight(), otherType.getWeight());
    }
}