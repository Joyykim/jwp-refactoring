package kitchenpos.ui.response;

import kitchenpos.domain.jpa.Menu;
import kitchenpos.domain.jpa.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse(Menu menu, List<MenuProduct> menuProducts) {
        id = menu.getId();
        name = menu.getName();
        price = menu.getPrice().getPrice();
        menuGroupId = menu.getMenuGroupId();
        this.menuProducts = menuProducts.stream()
                .map(menuProduct -> new MenuProductResponse())
                .collect(Collectors.toList());
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
