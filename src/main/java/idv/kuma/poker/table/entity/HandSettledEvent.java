package idv.kuma.poker.table.entity;

import idv.kuma.poker.common.entity.DomainEvent;

import java.util.List;

public record HandSettledEvent(String handId, List<String> userIds, int bet, PokerResult pokerResult) implements DomainEvent {
}