package idv.kuma.poker.table.entity;

import idv.kuma.poker.common.entity.DomainEvent;

public record TableSettledEvent(String tableId, PokerResult pokerResult) implements DomainEvent {
}