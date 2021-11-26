package kitchenpos.dao.jpa;

import kitchenpos.domain.jpa.MenuGroup;
import org.springframework.data.repository.CrudRepository;

public interface MenuGroupRepository extends CrudRepository<MenuGroup, Long> {
}
