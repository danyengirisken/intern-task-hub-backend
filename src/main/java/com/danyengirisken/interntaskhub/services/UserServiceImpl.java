package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.Role;
import com.danyengirisken.interntaskhub.entity.User;
import com.danyengirisken.interntaskhub.entity.dto.AssignRoleRequest;
import com.danyengirisken.interntaskhub.entity.dto.UserListDto;
import com.danyengirisken.interntaskhub.exception.ResourceNotFoundException;
import com.danyengirisken.interntaskhub.repository.RoleDao;
import com.danyengirisken.interntaskhub.repository.UserDao;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kullanici listeleme ve rol atama is mantigi. Entity <-> DTO donusumu burada.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;

    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserListDto> findAll() {
        return userDao.findAll().stream()
                .sorted(Comparator.comparing(User::getId))
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserListDto assignRole(AssignRoleRequest request) {
        User user = userDao.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Kullanıcı bulunamadı: " + request.getUserId()));
        Role role = roleDao.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rol bulunamadı: " + request.getRoleId()));
        user.setRole(role);
        return toDto(userDao.save(user));
    }

    private UserListDto toDto(User user) {
        return new UserListDto(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getRole().getId(),
                user.getRole().getName());
    }
}
