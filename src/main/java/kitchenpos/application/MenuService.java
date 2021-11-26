package kitchenpos.application;

import kitchenpos.dao.jpa.MenuGroupRepository;
import kitchenpos.dao.jpa.MenuProductRepository;
import kitchenpos.dao.jpa.MenuRepository;
import kitchenpos.dao.jpa.ProductRepository;
import kitchenpos.domain.jpa.Menu;
import kitchenpos.domain.jpa.MenuProduct;
import kitchenpos.domain.jpa.Price;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductRequest;
import kitchenpos.ui.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            MenuProductRepository menuProductRepository,
            ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        Menu menu = request.toEntity();
        final Price price = menu.getPrice();

        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<MenuProductRequest> menuProductsRequests = request.getMenuProducts();
        Map<Long, Long> productIdQuantityMap = menuProductsRequests.stream()
                .collect(Collectors.toMap(
                        MenuProductRequest::getProductId,
                        MenuProductRequest::getQuantity
                ));

        BigDecimal sum = BigDecimal.ZERO;
        for (kitchenpos.domain.jpa.Product product : productRepository.findAllById(productIdQuantityMap.keySet())) {
            Long quantity = productIdQuantityMap.get(product.getId());
            sum = sum.add(product.getPrice().getPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        if (price.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuRepository.save(menu);
        final Long menuId = savedMenu.getId();

        List<MenuProduct> menuProducts = menuProductsRequests.stream()
                .map(menuProductRequest -> new MenuProduct(null, menuId,
                        menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .collect(Collectors.toList());

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        menuProductRepository.saveAll(menuProducts)
                .forEach(savedMenuProducts::add);

        return new MenuResponse(menu, savedMenuProducts);
    }

    public List<MenuResponse> list() {

        List<MenuResponse> menuResponses = new ArrayList<>();
        for (final Menu menu : menuRepository.findAll()) {
            menuResponses.add(
                    new MenuResponse(
                            menu,
                            menuProductRepository.findByMenuId(menu.getId())));
        }

        return menuResponses;
    }
}
