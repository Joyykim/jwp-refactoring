package kitchenpos.ui.request;

import kitchenpos.domain.jpa.MenuGroup;

public class MenuGroupCreateRequest {

    private String name;

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public MenuGroupCreateRequest() {
    }

    public MenuGroup toEntity() {
        return new MenuGroup(null, name);
    }

    public String getName() {
        return name;
    }
}
