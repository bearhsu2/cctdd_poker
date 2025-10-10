package idv.kuma.poker;

import idv.kuma.poker.common.adapter.DomainEventBusInMemory;
import idv.kuma.poker.common.usecase.DomainEventBus;
import idv.kuma.poker.common.usecase.DomainEventHandler;
import idv.kuma.poker.table.adapter.TableRepositoryInMemory;
import idv.kuma.poker.table.entity.Table;
import idv.kuma.poker.table.entity.TableStatus;
import idv.kuma.poker.table.usecase.SettleTableService;
import idv.kuma.poker.table.usecase.TableRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettleTableServiceTest {
    private final TableRepository tableRepository = new TableRepositoryInMemory();
    private final DomainEventBus domainEventBus = new DomainEventBusInMemory();
    private final DomainEventHandler dummyDomainEventHandler = new DummyDomainEventHandler();
    private final SettleTableService settleTableService = new SettleTableService(tableRepository, domainEventBus);

    {
        domainEventBus.register(dummyDomainEventHandler);
    }

    @Test
    void should_retrieve_table_settle_it_and_save_back() {
        given_table("table-1");

        when_settle("table-1");

        then_table_status_should_be("table-1", TableStatus.SETTLED, 2);
        then_table_settled_event_should_be_sent("table-1");
    }

    private void given_table(String tableId) {
        Table table = Table.create(tableId);
        tableRepository.save(table);
    }

    private void when_settle(String tableId) {
        settleTableService.settle(tableId);
    }

    private void then_table_status_should_be(String tableId, TableStatus expectedStatus, int expectedVersion) {
        Table table = tableRepository.findById(tableId);
        assertThat(table.getStatus()).isEqualTo(expectedStatus);
        assertThat(table.getVersion()).isEqualTo(expectedVersion);
    }

    private void then_table_settled_event_should_be_sent(String tableId) {
        DummyDomainEventHandler handler = (DummyDomainEventHandler) dummyDomainEventHandler;
        assertThat(handler.getReceivedEvents()).hasSize(1);
        assertThat(handler.getReceivedEvents().get(0).getTableId()).isEqualTo(tableId);
    }
}
