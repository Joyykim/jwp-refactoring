package kitchenpos.ui.response;

import kitchenpos.domain.jpa.Product;

import java.math.BigDecimal;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    public ProductResponse(Product product) {
        id = product.getId();
        name = product.getName();
        price = product.getPrice().getPrice();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
