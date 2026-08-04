package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.dto.RoleDto;
import com.danyengirisken.interntaskhub.repository.RoleDao;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Rol listesi is mantigi.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> findAll() {
        return roleDao.findAll().stream()
                .map(role -> new RoleDto(role.getId(), role.getName()))
                .toList();
    }
}
