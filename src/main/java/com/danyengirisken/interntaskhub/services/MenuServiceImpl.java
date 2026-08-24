package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.Menu;
import com.danyengirisken.interntaskhub.entity.User;
import com.danyengirisken.interntaskhub.entity.dto.MenuDto;
import com.danyengirisken.interntaskhub.repository.MenuDao;
import com.danyengirisken.interntaskhub.security.UserContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Rolun yetkilerine gore menu agacini uretir.
 *
 * Iki temizlik adimi vardir:
 *  1) Ustu yetkisiz olan alt menuler dusurulur (parent gorunmuyorsa cocuk da gorunmez).
 *  2) Icinde hic ekran kalmayan grup menuleri (page'i olmayan basliklar) atilir;
 *     boylece "Sistem Ayarlari" gibi bir baslik bos olarak asilı kalmaz.
 */
@Service
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    private final MenuDao menuDao;
    private final UserContext userContext;

    public MenuServiceImpl(MenuDao menuDao, UserContext userContext) {
        this.menuDao = menuDao;
        this.userContext = userContext;
    }

    @Override
    public List<MenuDto> findByRoleId(Long roleId) {
        if (roleId == null) {
            return List.of();
        }

        List<Menu> allowed = menuDao.findAllowedByRoleId(roleId);
        List<Menu> reachable = dropOrphans(allowed);
        List<Menu> visible = dropEmptyGroups(reachable);

        return visible.stream().map(this::toDto).toList();
    }

    @Override
    public List<MenuDto> findForCurrentUser() {
        User user = userContext.getCurrentUser();
        return user.getRole() != null ? findByRoleId(user.getRole().getId()) : List.of();
    }

    /** Ust menusu yetki listesinde olmayan menuleri dusurur. */
    private List<Menu> dropOrphans(List<Menu> menus) {
        Set<Long> ids = new HashSet<>(menus.stream().map(Menu::getId).toList());
        return menus.stream()
                .filter(m -> m.getParentId() == null || ids.contains(m.getParentId()))
                .toList();
    }

    /**
     * Altinda hic ekran kalmayan grup menulerini atar.
     * Ic ice gruplar icin liste degismeyene kadar tekrarlanir.
     */
    private List<Menu> dropEmptyGroups(List<Menu> menus) {
        List<Menu> current = new ArrayList<>(menus);

        boolean removed = true;
        while (removed) {
            Set<Long> parentIds = new HashSet<>(current.stream()
                    .map(Menu::getParentId)
                    .filter(java.util.Objects::nonNull)
                    .toList());

            List<Menu> next = current.stream()
                    .filter(m -> hasPage(m) || parentIds.contains(m.getId()))
                    .toList();

            removed = next.size() != current.size();
            current = new ArrayList<>(next);
        }
        return current;
    }

    private boolean hasPage(Menu menu) {
        return menu.getPage() != null && !menu.getPage().isBlank();
    }

    private MenuDto toDto(Menu menu) {
        return new MenuDto(
                menu.getId(),
                menu.getParentId(),
                menu.getTitle(),
                menu.getPage(),
                menu.getIcon(),
                menu.getMenuOrder());
    }
}
