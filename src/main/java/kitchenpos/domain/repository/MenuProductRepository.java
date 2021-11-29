package kitchenpos.domain.repository;

import kitchenpos.domain.MenuProduct;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuProductRepository extends CrudRepository<MenuProduct, Long> {

    List<MenuProduct> findByMenuId(Long menuId);
}
