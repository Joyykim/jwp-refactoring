package kitchenpos.domain.repository;

import kitchenpos.domain.Menu;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuRepository extends CrudRepository<Menu, Long> {

    Long countByIdIn(List<Long> ids);
}
