package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.jpa.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.request.*;
import kitchenpos.ui.response.OrderResponse;
import kitchenpos.ui.response.OrderTableResponse;
import kitchenpos.ui.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class TableGroupServiceTest extends IntegrationTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("단체 지정을 생성한다")
    @Test
    void create() {
        // given
        List<OrderTableResponse> orderTableResponses = Arrays.asList(
                createOrderTable(true), createOrderTable(true));

        TableGroupCreateRequest request = new TableGroupCreateRequest(convert(orderTableResponses));

        // when
        TableGroupResponse response = tableGroupService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("테이블의 수는 2 이상이어야 한다")
    @Test
    void create_fail_orderTablesSizeShouldGreaterThanOrEqualTo2() {
        // given
        List<OrderTableResponse> orderTableResponses = Collections.singletonList(createOrderTable(true));
        TableGroupCreateRequest request = new TableGroupCreateRequest(convert(orderTableResponses));

        // when,then
        assertThatCode(() -> tableGroupService.create(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("요청한 주문 테이블들이 저장되어있어야 한다")
    @Test
    void create_fail_requestedOrderTablesShouldExists() {
        // given
        List<OrderTableGroupingRequest> orderTableGroupingRequests = convert(Arrays.asList(
                createOrderTable(true), createOrderTable(true)));
        orderTableGroupingRequests.add(new OrderTableGroupingRequest(-1L));

        TableGroupCreateRequest request = new TableGroupCreateRequest(orderTableGroupingRequests);

        // when,then
        assertThatCode(() -> tableGroupService.create(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("주문 테이블이 모두 비어있어야 한다")
    @Test
    void create_fail_allTablesShouldBeEmpty() {
        // given
        List<OrderTableResponse> orderTableResponses = Arrays.asList(
                createOrderTable(false), createOrderTable(false));

        TableGroupCreateRequest request = new TableGroupCreateRequest(convert(orderTableResponses));

        // when,then
        assertThatCode(() -> tableGroupService.create(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("주문 테이블이 테이블그룹에 연결되어있지 않아야 한다")
    @Test
    void create_fail_allTablesShouldHaveTableGroupId() {
        // given
        OrderTableResponse orderTableResponse1 = createOrderTable(true);
        OrderTableResponse orderTableResponse2 = createOrderTable(true);
        List<OrderTableResponse> orderTableResponses = Arrays.asList(
                orderTableResponse1, orderTableResponse2);
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(convert(orderTableResponses));
        tableGroupService.create(tableGroupCreateRequest);

        // when,then
        TableGroupCreateRequest request = new TableGroupCreateRequest(convert(orderTableResponses));
        assertThatCode(() -> tableGroupService.create(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("단체 지정을 해체한다")
    @Test
    void ungroup() {
        // given
        List<OrderTableResponse> orderTableResponses = Arrays.asList(
                createOrderTable(true), createOrderTable(true));
        TableGroupCreateRequest request = new TableGroupCreateRequest(convert(orderTableResponses));
        TableGroupResponse tableGroupResponse = tableGroupService.create(request);

        // when
        tableGroupService.ungroup(tableGroupResponse.getId());

        // then
        assertThat(orderTableRepository.findAllByTableGroupId(tableGroupResponse.getId()))
                .allMatch(orderTable -> orderTable.getTableGroupId() == null)
                .allMatch(orderTable -> !orderTable.isEmpty());
    }

    @DisplayName("연결된 주문들의 주문상태는 `계산 완료`여야 한다")
    @Test
    void ungroup_fail_relatedOrdersOrderStatusShouldBeCompletion() {
        // 주문,테이블 생성
        OrderTableResponse orderTableResponse = createOrderTable(false);
        long tableId = orderTableResponse.getId();

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(tableId,
                Collections.singletonList(new OrderLineItemCreateRequest(1L, 1)));
        OrderResponse orderResponse = orderService.create(orderCreateRequest);

        // 주문 상태 바꾸기 - 테이블 비우기 시 필요
        orderService.changeOrderStatus(orderResponse.getId(),
                new OrderChangeStatusRequest(OrderStatus.COMPLETION.name()));

        // 테이블 비우기 - tableGroup 생성 시, 주문상태 변경 시 필요
        tableService.changeEmpty(tableId, new OrderTableChangeEmptyRequest(true));

        // 주문 상태 조리 중으로 변경
        Order order = orderDao.findById(orderResponse.getId()).get();
        order.setOrderStatus(OrderStatus.COOKING.name());
        orderDao.save(order);

        // 단체 지정
        OrderTableResponse normalOrderTable = createOrderTable(true);
        TableGroupResponse tableGroupResponse = tableGroupService.create(
                new TableGroupCreateRequest(Arrays.asList(
                        new OrderTableGroupingRequest(tableId),
                        new OrderTableGroupingRequest(normalOrderTable.getId()))));

        // when,then
        assertThatCode(() -> tableGroupService.ungroup(tableGroupResponse.getId()))
                .isInstanceOf(RuntimeException.class);
    }

    private OrderTableResponse createOrderTable(boolean empty) {
        return tableService.create(new OrderTableCreateRequest(0, empty));
    }

    private List<OrderTableGroupingRequest> convert(List<OrderTableResponse> orderTableResponses) {
        return orderTableResponses.stream()
                .map(orderTableResponse -> new OrderTableGroupingRequest(orderTableResponse.getId()))
                .collect(Collectors.toList());
    }
}