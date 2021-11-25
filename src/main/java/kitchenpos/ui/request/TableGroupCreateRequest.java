package kitchenpos.ui.request;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private List<OrderTableGroupingRequest> orderTables;

    public TableGroupCreateRequest(List<OrderTableGroupingRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupCreateRequest() {
    }

    public TableGroup toEntity() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables.stream()
                .map(orderTableGroupingRequest -> {
                    OrderTable orderTable = new OrderTable();
                    orderTable.setId(orderTableGroupingRequest.getId());
                    return orderTable;
                })
                .collect(Collectors.toList()));
        return tableGroup;
    }
}
