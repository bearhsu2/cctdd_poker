package idv.kuma.poker.table.entity;

import idv.kuma.poker.common.entity.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableSettledEvent implements DomainEvent {
    private String tableId;
    private PokerResult pokerResult;
}
