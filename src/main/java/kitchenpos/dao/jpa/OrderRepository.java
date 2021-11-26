package kitchenpos.dao.jpa;

import kitchenpos.domain.jpa.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
