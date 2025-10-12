package idv.kuma.poker.common.adapter;

import idv.kuma.poker.common.usecase.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGeneratorInMemory implements IdGenerator {
    @Override
    public String generate() {
        // 36 characters (32 hex + 4 hyphens)
        return UUID.randomUUID().toString();
    }
}