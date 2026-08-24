package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.dto.RoleDto;
import com.danyengirisken.interntaskhub.repository.RoleDao;
import com.danyengirisken.interntaskhub.security.Roles;
import com.danyengirisken.interntaskhub.security.UserContext;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Rol listesi is mantigi (rol atama ekranindaki secim listesi).
 *
 * Partner yoneticisine ADMIN rolu gosterilmez; zaten atayamaz
 * (bkz. {@link UserServiceImpl#assignRole}), listede de gorunmemesi gerekir.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;
    private final UserContext userContext;

    public RoleServiceImpl(RoleDao roleDao, UserContext userContext) {
        this.roleDao = roleDao;
        this.userContext = userContext;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> findAll() {
        userContext.requireUserManager();

        boolean isAdmin = userContext.isAdmin();
        return roleDao.findAll().stream()
                .filter(role -> isAdmin || !Roles.ADMIN.equals(role.getName()))
                .map(role -> new RoleDto(role.getId(), role.getName()))
                .toList();
    }
}
