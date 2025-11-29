package idv.kuma.poker.gamehistory.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Projections;
import com.querydsl.sql.SQLQueryFactory;
import idv.kuma.poker.gamehistory.entity.GameHistory;
import idv.kuma.poker.gamehistory.usecase.GameHistoryRepository;
import idv.kuma.poker.generated.GameHistoryDbDto;
import idv.kuma.poker.generated.QGameHistory;
import idv.kuma.poker.table.entity.HandResult;
import idv.kuma.poker.table.entity.PlayerResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GameHistoryRepositoryQueryDsl implements GameHistoryRepository {
    private final SQLQueryFactory queryFactory;
    private final ObjectMapper objectMapper;
    private static final QGameHistory qGameHistory = QGameHistory.gameHistory;

    @Override
    @Transactional(readOnly = true)
    public GameHistory findByHandId(String handId) {
        GameHistoryDbDto dto = queryFactory
            .select(Projections.bean(GameHistoryDbDto.class,
                qGameHistory.handId,
                qGameHistory.handResultJson))
            .from(qGameHistory)
            .where(qGameHistory.handId.eq(handId))
            .fetchOne();

        return dto == null ? null : toEntity(dto);
    }

    @Override
    @Transactional
    public void save(GameHistory gameHistory) {
        queryFactory.insert(qGameHistory)
            .set(qGameHistory.handId, gameHistory.getHandId())
            .set(qGameHistory.handResultJson, toJson(gameHistory.getHandResult().getPositionToResult()))
            .execute();
    }

    private GameHistory toEntity(GameHistoryDbDto dto) {
        Map<Integer, PlayerResult> positionToResult = fromJson(dto.getHandResultJson(), new TypeReference<>() {});
        HandResult handResult = HandResult.of(positionToResult);

        return GameHistory.restore(
            dto.getHandId(),
            handResult
        );
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize to JSON", e);
        }
    }

    private <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize from JSON", e);
        }
    }
}