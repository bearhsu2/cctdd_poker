package idv.kuma.poker.table.entity;

import idv.kuma.poker.common.entity.DomainEvent;

public record HandSettledEvent(String handId, int bet, HandResult handResult) implements DomainEvent {
}