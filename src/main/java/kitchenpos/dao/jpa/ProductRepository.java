package kitchenpos.dao.jpa;

import kitchenpos.domain.jpa.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
