package idv.kuma.poker.table.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableSettledEvent {
    private String tableId;
    private PokerResult pokerResult;
}
