package idv.kuma.poker;

import java.util.List;
import java.util.function.BooleanSupplier;

public class DBCUtil {
    
    public static void requireSize(List<?> list, int expectedSize, BooleanSupplier condition) {
        if (!condition.getAsBoolean()) {
            throw new RuntimeException("List must contain exactly " + expectedSize + " elements, but got " + list.size());
        }
    }
}