package kitchenpos.dao.jpa;

import kitchenpos.domain.jpa.Order;
import kitchenpos.domain.jpa.OrderStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    Boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatus);

    Boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatus);
}
