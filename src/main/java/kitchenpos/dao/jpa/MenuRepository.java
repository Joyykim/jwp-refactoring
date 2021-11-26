package kitchenpos.dao.jpa;

import kitchenpos.domain.jpa.Menu;
import org.springframework.data.repository.CrudRepository;

public interface MenuRepository extends CrudRepository<Menu, Long> {
}
