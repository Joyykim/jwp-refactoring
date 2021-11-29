package kitchenpos.ui.request;

public class OrderTableChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    public OrderTableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTableChangeNumberOfGuestsRequest() {
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
