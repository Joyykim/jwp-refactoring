package kitchenpos.ui.request;

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
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
