package idv.kuma.poker.table;

import java.util.function.BooleanSupplier;

public class DBCUtil {

    public static void require(BooleanSupplier condition, String errorMessage) {
        if (!condition.getAsBoolean()) {
            throw new RuntimeException(errorMessage);
        }
    }
}