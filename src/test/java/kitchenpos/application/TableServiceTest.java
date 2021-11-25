package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.request.*;
import kitchenpos.ui.response.OrderResponse;
import kitchenpos.ui.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class TableServiceTest extends IntegrationTest {

    @DisplayName("주문 테이블을 생성한다")
    @Test
    void create() {
        // given
        OrderTableCreateRequest request = new OrderTableCreateRequest(false);

        // when
        OrderTableResponse response = tableService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getNumberOfGuests()).isEqualTo(0);
        assertThat(response.isEmpty()).isFalse();
        assertThat(response.getTableGroupId()).isNull();
    }

    @DisplayName("주문 테이블 목록을 조회한다")
    @Test
    void list() {
        List<OrderTableResponse> responses = tableService.list();

        assertThat(responses).hasSize(8);
    }

    @DisplayName("주문 테이블을 비우거나 채운다")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmpty(boolean empty) {
        // given
        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(empty);

        // when
        OrderTableResponse response = tableService.changeEmpty(1L, request);

        // then
        assertThat(response.isEmpty()).isEqualTo(empty);
    }

    @DisplayName("주문 테이블이 저장되어있어야 한다")
    @Test
    void changeEmpty_fail_orderTableShouldExists() {
        // given
        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(true);

        // when, then
        assertThatCode(() -> tableService.changeEmpty(-1L, request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("주문 테이블이 테이블그룹에 연결되어있지 않아야 한다")
    @Test
    void changeEmpty_fail_orderTableShouldNotRelatedAnyTableGroup() {
        // given
        OrderTableResponse orderTable1 = tableService.create(new OrderTableCreateRequest(true));
        OrderTableResponse orderTable2 = tableService.create(new OrderTableCreateRequest(true));
        Long tableId = orderTable1.getId();
        TableGroupCreateRequest tableGroupRequest = new TableGroupCreateRequest(
                Arrays.asList(
                        new OrderTableGroupingRequest(tableId),
                        new OrderTableGroupingRequest(orderTable2.getId())
                ));
        tableGroupService.create(tableGroupRequest);

        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(true);

        // when, then
        assertThatCode(() -> tableService.changeEmpty(tableId, request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("연결된 주문들의 주문상태는 `계산 완료`여야 한다")
    @Test
    void changeEmpty_fail_relatedOrdersOrderStatusShouldBeCompletion() {
        // given
        OrderTableResponse orderTableResponse = tableService.create(new OrderTableCreateRequest(false));
        Long tableId = orderTableResponse.getId();

        OrderCreateRequest orderRequest = new OrderCreateRequest(tableId,
                Collections.singletonList(new OrderLineItemCreateRequest(1L, 1)));
        OrderResponse createResponse = orderService.create(orderRequest);

        OrderChangeStatusRequest statusRequest = new OrderChangeStatusRequest(OrderStatus.COOKING.name());
        orderService.changeOrderStatus(createResponse.getId(), statusRequest);

        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);

        // when, then
        assertThatCode(() -> tableService.changeEmpty(tableId, request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("주문 테이블의 인원을 수정한다")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTableCreateRequest tableRequest = new OrderTableCreateRequest(1, false);
        OrderTableResponse tableResponse = tableService.create(tableRequest);

        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(2);

        // when
        OrderTableResponse response = tableService.changeNumberOfGuests(tableResponse.getId(), request);

        // then
        assertThat(response.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("주문 테이블은 비어있을 수 없다")
    @Test
    void changeNumberOfGuests_fail_tableCannotBeEmpty() {
        // given
        OrderTableCreateRequest tableRequest = new OrderTableCreateRequest(true);
        OrderTableResponse tableResponse = tableService.create(tableRequest);

        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(2);

        // when, then
        assertThatCode(() -> tableService.changeNumberOfGuests(tableResponse.getId(), request))
                .isInstanceOf(RuntimeException.class);
    }
}