package kitchenpos.application;

import kitchenpos.ui.request.ProductCreateRequest;
import kitchenpos.ui.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ProductServiceTest extends IntegrationTest {

    @DisplayName("상품을 생성한다")
    @Test
    void create() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("product", 1000L);

        // when
        ProductResponse response = productService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("product");
        assertThat(response.getPrice().longValue()).isEqualTo(1000L);
    }

    @DisplayName("상품의 이름은 null일 수 없다")
    @Test
    void create_fail_productNameCannotBeNull() {
        // given
        ProductCreateRequest request = new ProductCreateRequest(null, 1000L);

        // when, then
        assertThatCode(() -> productService.create(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("상품의 가격은 null일 수 없다")
    @Test
    void create_fail_productPriceCannotBeNull() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("product", null);

        // when, then
        assertThatCode(() -> productService.create(request))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격은 음수일 수 없다")
    @Test
    void create_fail_productPriceCannotBeNegative() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("product", -1L);

        // when, then
        assertThatCode(() -> productService.create(request))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격은 0 이상이어야 한다")
    @ParameterizedTest
    @ValueSource(longs = {0L, 1L})
    void create_success_productPriceIsZeroOrPositive(Long price) {
        // given
        ProductCreateRequest request = new ProductCreateRequest("product", price);

        // when, then
        assertThatCode(() -> productService.create(request))
                .doesNotThrowAnyException();
    }

    @DisplayName("상품 목록을 조회한다")
    @Test
    void list() {
        List<ProductResponse> responses = productService.list();

        assertThat(responses).hasSize(6);
    }
}