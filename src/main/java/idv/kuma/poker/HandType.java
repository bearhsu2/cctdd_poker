package idv.kuma.poker;

public enum HandType {
    HIGH_CARD(0) {
        @Override
        public int compareHands(Hand thisHand, Hand otherHand) {
            return Hand.compareByHighestCards(thisHand.getCards(), otherHand.getCards());
        }
    },
    PAIR(1) {
        @Override
        public int compareHands(Hand thisHand, Hand otherHand) {
            return Hand.compareByHighestCards(thisHand.getPairCards(), otherHand.getPairCards());
        }
    };
    
    private final int weight;
    
    HandType(int weight) {
        this.weight = weight;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public int compare(HandType other) {
        return Integer.compare(this.getWeight(), other.getWeight());
    }
    
    public abstract int compareHands(Hand thisHand, Hand otherHand);
}