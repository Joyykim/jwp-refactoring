package kitchenpos.dao.jpa;

import kitchenpos.domain.jpa.OrderTable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderTableRepository extends CrudRepository<OrderTable, Long> {

    List<OrderTable> findAllByTableGroupId(Long TableGroupId);
}
