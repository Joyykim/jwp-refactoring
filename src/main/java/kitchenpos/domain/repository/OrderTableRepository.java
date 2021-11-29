package kitchenpos.domain.repository;

import kitchenpos.domain.OrderTable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderTableRepository extends CrudRepository<OrderTable, Long> {

    List<OrderTable> findAllByTableGroupId(Long TableGroupId);
}
