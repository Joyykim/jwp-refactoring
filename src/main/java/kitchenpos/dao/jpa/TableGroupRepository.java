package kitchenpos.dao.jpa;

import kitchenpos.domain.jpa.TableGroup;
import org.springframework.data.repository.CrudRepository;

public interface TableGroupRepository extends CrudRepository<TableGroup, Long> {
}
