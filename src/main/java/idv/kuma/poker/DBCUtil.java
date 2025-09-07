package idv.kuma.poker;

import java.util.List;
import java.util.function.BooleanSupplier;

public class DBCUtil {
    
    public static void require(BooleanSupplier condition, String errorMessage) {
        if (!condition.getAsBoolean()) {
            throw new RuntimeException(errorMessage);
        }
    }
}