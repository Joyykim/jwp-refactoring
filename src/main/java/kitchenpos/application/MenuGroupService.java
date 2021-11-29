package kitchenpos.application;

import kitchenpos.dao.jpa.MenuGroupRepository;
import kitchenpos.domain.jpa.MenuGroup;
import kitchenpos.ui.request.MenuGroupCreateRequest;
import kitchenpos.ui.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        return ((List<MenuGroup>) menuGroupRepository.findAll()).stream()
                .map(MenuGroupResponse::new)
                .collect(Collectors.toList());
    }
}
