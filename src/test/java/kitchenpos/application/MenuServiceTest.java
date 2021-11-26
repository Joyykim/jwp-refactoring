package kitchenpos.application;

import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductRequest;
import kitchenpos.ui.request.ProductCreateRequest;
import kitchenpos.ui.response.MenuResponse;
import kitchenpos.ui.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class MenuServiceTest extends IntegrationTest {

    @DisplayName("메뉴를 생성한다")
    @Test
    void create() {
        // given
        MenuCreateRequest request = new MenuCreateRequest("메뉴", BigDecimal.valueOf(5000L), 1L, menuProducts());

        // when
        MenuResponse response = menuService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getPrice().longValue()).isEqualTo(request.getPrice().longValue());
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getMenuGroupId()).isEqualTo(request.getMenuGroupId());
        assertThat(response.getMenuProducts()).hasSize(1);
    }

    @DisplayName("메뉴의 가격은 null일 수 없다")
    @Test
    void create_fail_menuPriceCannotBeNull() {
        // given
        MenuCreateRequest request = new MenuCreateRequest("메뉴", null, 1L, menuProducts());

        // when, then
        assertThatCode(() -> menuService.create(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("메뉴의 가격은 음수일 수 없다")
    @Test
    void create_fail_menuPriceCannotBeNegative() {
        // given
        MenuCreateRequest request = new MenuCreateRequest("메뉴", BigDecimal.valueOf(-1L), 1L, menuProducts());

        // when, then
        assertThatCode(() -> menuService.create(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("메뉴의 메뉴그룹id에 해당하는 메뉴그룹이 존재해야 한다")
    @Test
    void create_fail_menuGroupShouldExistsWhereInMenusMenuGroupId() {
        // given
        MenuCreateRequest request = new MenuCreateRequest("메뉴", BigDecimal.valueOf(1000L), -1L, menuProducts());

        // when, then
        assertThatCode(() -> menuService.create(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("메뉴의 메뉴프로덕츠는 null일 수 없다")
    @Test
    void create_fail_menuMenuProductsCannotBeNull() {
        // given
        MenuCreateRequest request = new MenuCreateRequest("메뉴", BigDecimal.valueOf(1000L), 1L, null);

        // when, then
        assertThatCode(() -> menuService.create(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("메뉴의 가격이 상품들의 가격의 총합보다 같거나 작아야 한다")
    @Test
    void create_fail_menuPriceShouldLowerThanTotalProductsPrice() {
        // given
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("product", BigDecimal.valueOf(1000L));
        ProductResponse productResponse = productService.create(productCreateRequest);

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴", BigDecimal.valueOf(1001L), 1L,
                Collections.singletonList(new MenuProductRequest(1L, productResponse.getId(), 1)));

        // when, then
        assertThatCode(() -> menuService.create(menuCreateRequest))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("메뉴의 가격이 상품들의 가격의 총합보다 같거나 작아야 한다")
    @ParameterizedTest
    @ValueSource(longs = {1000L, 999L})
    void create_success_menuPriceIsLowerThanTotalProductsPrice(Long menuPrice) {
        // given
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("product", BigDecimal.valueOf(1000L));
        ProductResponse productResponse = productService.create(productCreateRequest);

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("메뉴", BigDecimal.valueOf(menuPrice), 1L,
                Collections.singletonList(new MenuProductRequest(1L, productResponse.getId(), 1)));

        // when, then
        assertThatCode(() -> menuService.create(menuCreateRequest))
                .doesNotThrowAnyException();
    }

    @DisplayName("메뉴 목록을 조회 한다")
    @Test
    void list() {
        List<MenuResponse> responses = menuService.list();

        assertThat(responses).hasSize(6);
    }

    private List<MenuProductRequest> menuProducts() {
        MenuProductRequest request = new MenuProductRequest(1L, 1L, 1);
        return Collections.singletonList(request);
    }
}