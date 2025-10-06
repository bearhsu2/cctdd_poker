package idv.kuma.poker;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettleTableServiceTest {

    @Test
    void shouldRetrieveTableSettleItAndSaveBack() {
        TableRepository tableRepository = new TableRepositoryInMemory();
        SettleTableService settleTableService = new SettleTableService(tableRepository);

        Table table = Table.create("table-1");
        tableRepository.save(table);

        settleTableService.settle("table-1");

        Table settledTable = tableRepository.findById("table-1");
        assertThat(settledTable.getStatus()).isEqualTo(TableStatus.SETTLED);
    }
}
