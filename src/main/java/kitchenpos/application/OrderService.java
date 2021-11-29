package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.request.OrderChangeStatusRequest;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderLineItemCreateRequest;
import kitchenpos.ui.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemCreateRequest> orderLineItems = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Order savedOrder = orderRepository.save(
                new Order(null, orderTable, OrderStatus.COOKING, null));

        Long orderId = savedOrder.getId();
        List<OrderLineItem> orderLineItemEntities = orderLineItems.stream()
                .map(orderLineItemCreateRequest -> new OrderLineItem(
                        null, orderId, orderLineItemCreateRequest.getMenuId(), orderLineItemCreateRequest.getQuantity()))
                .collect(Collectors.toList());
        List<OrderLineItem> savedOrderLineItems = (List<OrderLineItem>) orderLineItemRepository.saveAll(orderLineItemEntities);

        return new OrderResponse(savedOrder, savedOrderLineItems);
    }

    public List<OrderResponse> list() {
        List<OrderResponse> responses = new ArrayList<>();
        for (final Order order : orderRepository.findAll()) {
            OrderResponse orderResponse = new OrderResponse(order, orderLineItemRepository.findAllByOrderId(order.getId()));
            responses.add(orderResponse);
        }
        return responses;
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        savedOrder.setOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return new OrderResponse(savedOrder, orderLineItemRepository.findAllByOrderId(orderId));
    }
}
