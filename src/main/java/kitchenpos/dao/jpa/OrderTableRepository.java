package kitchenpos.dao.jpa;

import kitchenpos.domain.jpa.OrderTable;
import org.springframework.data.repository.CrudRepository;

public interface OrderTableRepository extends CrudRepository<OrderTable, Long> {
}
