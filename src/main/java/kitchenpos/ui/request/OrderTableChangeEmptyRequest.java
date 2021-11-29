package kitchenpos.ui.request;

public class OrderTableChangeEmptyRequest {

    private boolean empty;

    public OrderTableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public OrderTableChangeEmptyRequest() {
    }

    public boolean isEmpty() {
        return empty;
    }
}
