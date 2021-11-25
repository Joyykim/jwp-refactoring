package kitchenpos.ui.request;

import kitchenpos.domain.OrderTable;

public class OrderTableChangeEmptyRequest {

    private boolean empty;

    public OrderTableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public OrderTableChangeEmptyRequest() {
    }

    public OrderTable toEntity() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
