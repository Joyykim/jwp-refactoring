package kitchenpos.application;

import kitchenpos.ui.request.MenuGroupCreateRequest;
import kitchenpos.ui.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class MenuGroupServiceTest extends IntegrationTest {

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void create() {
        // given
        MenuGroupCreateRequest request = new MenuGroupCreateRequest("MenuGroup");

        // when
        MenuGroupResponse response = menuGroupService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("MenuGroup");
    }

    @DisplayName("메뉴 그룹의 이름은 null일 수 없다")
    @Test
    void create_fail_menuGroupNameCannotBeNull() {
        // given
        MenuGroupCreateRequest request = new MenuGroupCreateRequest(null);

        // when, then
        assertThatCode(() -> menuGroupService.create(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void list() {
        List<MenuGroupResponse> responses = menuGroupService.list();

        assertThat(responses).hasSize(4);
    }
}