package kitchenpos.ui.request;

public class OrderChangeStatusRequest {

    private String orderStatus;

    public OrderChangeStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderChangeStatusRequest() {
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
