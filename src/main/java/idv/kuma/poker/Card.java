package idv.kuma.poker;

public record Card(Suit suit, Number number) implements Comparable<Card> {

    public static Card of(Suit suit, Number number) {
        return new Card(suit, number);
    }

    @Override
    public int compareTo(Card other) {
        int numberComparison = this.number.compareValue(other.number);
        if (numberComparison != 0) {
            return numberComparison;
        }

        // When card numbers are equal, compare suits by weight: higher weight wins
        return this.suit.compareValue(other.suit);
    }
}