package idv.kuma.poker.table.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Projections;
import com.querydsl.sql.SQLQueryFactory;
import idv.kuma.poker.generated.HandDbDto;
import idv.kuma.poker.generated.QHand;
import idv.kuma.poker.table.entity.Board;
import idv.kuma.poker.table.entity.Card;
import idv.kuma.poker.table.entity.Hand;
import idv.kuma.poker.table.entity.HandStatus;
import idv.kuma.poker.table.entity.HoleCards;
import idv.kuma.poker.table.usecase.HandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HandRepositoryQueryDsl implements HandRepository {
    private final SQLQueryFactory queryFactory;
    private final ObjectMapper objectMapper;
    private static final QHand qHand = QHand.hand;

    @Override
    @Transactional(readOnly = true)
    public Hand findById(String handId) {
        HandDbDto dto = queryFactory
            .select(Projections.bean(HandDbDto.class,
                qHand.id,
                qHand.status,
                qHand.version,
                qHand.userIds,
                qHand.bet,
                qHand.holeCardsJson,
                qHand.boardJson))
            .from(qHand)
            .where(qHand.id.eq(handId))
            .fetchOne();

        return dto == null ? null : toEntity(dto);
    }

    @Override
    @Transactional
    public void save(Hand hand) {
        Long count = queryFactory
            .select(qHand.id.count())
            .from(qHand)
            .where(qHand.id.eq(hand.getId()))
            .fetchOne();

        List<List<Card>> holeCardsList = hand.getHoleCards().stream()
            .map(HoleCards::getCards)
            .toList();
        List<Card> boardCards = hand.getBoard().getCards();

        if (count == 0) {
            queryFactory.insert(qHand)
                .set(qHand.id, hand.getId())
                .set(qHand.status, hand.getStatus().name())
                .set(qHand.version, hand.getVersion())
                .set(qHand.userIds, toJson(hand.getUserIds()))
                .set(qHand.bet, hand.getBet())
                .set(qHand.holeCardsJson, toJson(holeCardsList))
                .set(qHand.boardJson, toJson(boardCards))
                .execute();
        } else {
            queryFactory.update(qHand)
                .set(qHand.status, hand.getStatus().name())
                .set(qHand.version, hand.getVersion())
                .set(qHand.userIds, toJson(hand.getUserIds()))
                .set(qHand.bet, hand.getBet())
                .set(qHand.holeCardsJson, toJson(holeCardsList))
                .set(qHand.boardJson, toJson(boardCards))
                .where(qHand.id.eq(hand.getId()))
                .execute();
        }
    }

    private Hand toEntity(HandDbDto dto) {

        List<List<Card>> holeCardsList = fromJson(dto.getHoleCardsJson(), new TypeReference<>() {});
        List<HoleCards> holeCards = holeCardsList.stream()
            .map(HoleCards::of)
            .toList();

        List<Card> boardCards = fromJson(dto.getBoardJson(), new TypeReference<>() {});
        Board board = Board.of(boardCards);

        return Hand.restore(
            dto.getId(),
            HandStatus.valueOf(dto.getStatus()),
            dto.getVersion(),
            fromJson(dto.getUserIds(), new TypeReference<>() {}),
            dto.getBet(),
            holeCards,
            board
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