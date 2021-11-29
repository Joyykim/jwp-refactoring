package kitchenpos.ui.request;

import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    public ProductCreateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public ProductCreateRequest(String name, long price) {
        this(name, BigDecimal.valueOf(price));
    }

    public ProductCreateRequest() {
    }

    public Product toEntity() {
        return new Product(null, name, new Price(price));
    }
}
