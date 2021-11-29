package kitchenpos.ui.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableGroupingRequest> orderTables;

    public TableGroupCreateRequest(List<OrderTableGroupingRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupCreateRequest() {
    }

    public List<OrderTableGroupingRequest> getOrderTables() {
        return orderTables;
    }
}
