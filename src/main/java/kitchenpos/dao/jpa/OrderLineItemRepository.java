package kitchenpos.dao.jpa;

import kitchenpos.domain.jpa.OrderLineItem;
import org.springframework.data.repository.CrudRepository;

public interface OrderLineItemRepository extends CrudRepository<OrderLineItem, Long> {
}
