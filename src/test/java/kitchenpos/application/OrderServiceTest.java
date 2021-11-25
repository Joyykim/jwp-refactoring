package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.request.*;
import kitchenpos.ui.response.OrderResponse;
import kitchenpos.ui.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class OrderServiceTest extends IntegrationTest {

    @DisplayName("주문을 생성한다")
    @Test
    void create() {
        // given
        OrderTableResponse savedOrderTable = createOrderTable(false);
        OrderLineItemCreateRequest orderLineItem = new OrderLineItemCreateRequest(1L, 1);
        OrderCreateRequest request = new OrderCreateRequest(savedOrderTable.getId(), Arrays.asList(orderLineItem));

        // when
        OrderResponse response = orderService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("연결된 주문 항목은 비어있으면 안된다")
    @Test
    void create_fail_orderLineItemsCannotBeEmpty() {
        // given
        OrderTableResponse savedOrderTable = createOrderTable(false);
        OrderCreateRequest request = new OrderCreateRequest(savedOrderTable.getId(), Collections.emptyList());

        // when,then
        assertThatCode(() -> orderService.create(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("연결된 주문 항목의 메뉴들은 저장되어있어야 한다")
    @Test
    void create_fail_orderLineItemsMenusShouldExists() {
        // given
        OrderTableResponse savedOrderTable = createOrderTable(false);
        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(-1L, 1);
        OrderCreateRequest request = new OrderCreateRequest(savedOrderTable.getId(),
                Arrays.asList(orderLineItemCreateRequest));

        // when,then
        assertThatCode(() -> orderService.create(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("연결된 주문 테이블이 저장되어있어야 한다")
    @Test
    void create_fail_orderTableShouldExists() {
        // given
        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(1L, 1);
        OrderCreateRequest request = new OrderCreateRequest(-1L, Arrays.asList(orderLineItemCreateRequest));

        // when,then
        assertThatCode(() -> orderService.create(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("연결된 주문 테이블은 비어있을 수 없다")
    @Test
    void create_fail_orderTableCannotBeEmpty() {
        // given
        OrderTableResponse savedOrderTable = createOrderTable(true);
        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(1L, 1);
        OrderCreateRequest request = new OrderCreateRequest(savedOrderTable.getId(), Arrays.asList(orderLineItemCreateRequest));

        // when,then
        assertThatCode(() -> orderService.create(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() {
        // given
        createOrder();

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders).hasSize(1);
    }

    @DisplayName("주문의 주문상태를 변경한다")
    @Test
    void changeOrderStatus() {
        // given
        OrderResponse orderResponse = createOrder();
        OrderChangeStatusRequest request = new OrderChangeStatusRequest(OrderStatus.COOKING.name());

        // when
        OrderResponse response = orderService.changeOrderStatus(orderResponse.getId(), request);

        // then
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("이미 `계산 완료` 상태라면 변경할 수 없다")
    @Test
    void changeOrderStatus_fail() {
        // given
        OrderResponse createdOrder = createOrder();
        OrderChangeStatusRequest request = new OrderChangeStatusRequest(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(createdOrder.getId(), request);

        // when
        assertThatCode(() -> orderService.changeOrderStatus(createdOrder.getId(), request))
                .isInstanceOf(RuntimeException.class);
    }

    private OrderResponse createOrder() {
        OrderTableResponse orderTableResponse = createOrderTable(false);
        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(1L, 1);
        OrderCreateRequest request = new OrderCreateRequest(orderTableResponse.getId(),
                Collections.singletonList(orderLineItemCreateRequest));
        return orderService.create(request);
    }

    private OrderTableResponse createOrderTable(boolean empty) {
        OrderTableCreateRequest request = new OrderTableCreateRequest(0, empty);
        return tableService.create(request);
    }
}