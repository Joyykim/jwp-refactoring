package kitchenpos.application;

import kitchenpos.dao.jpa.MenuGroupRepository;
import kitchenpos.domain.jpa.MenuGroup;
import kitchenpos.ui.request.MenuGroupCreateRequest;
import kitchenpos.ui.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        MenuGroup menuGroup = menuGroupRepository.save(request.toEntity());
        return new MenuGroupResponse(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroupResponse> responses = new ArrayList<>();
        for (MenuGroup menuGroup : menuGroupRepository.findAll()) {
            responses.add(new MenuGroupResponse(menuGroup));
        }
        return responses;
    }
}
