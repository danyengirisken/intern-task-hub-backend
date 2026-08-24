package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.User;
import com.danyengirisken.interntaskhub.entity.dto.AssignRoleRequest;
import com.danyengirisken.interntaskhub.entity.dto.UserListDto;
import com.danyengirisken.interntaskhub.entity.dto.UserRequest;
import java.util.List;

/**
 * Kullanici modulu is mantigi (carbon: interface + Impl konvansiyonu).
 */
public interface UserService {

    List<UserListDto> findAll();

    /** Kullanici olusturur veya gunceller. */
    UserListDto save(UserRequest request);

    void delete(Long id);

    /** Bir kullaniciya rol atar ve guncel kullaniciyi doner. */
    UserListDto assignRole(AssignRoleRequest request);

    /** Kullanıcı adına göre sistemden kullanıcıyı getirir (Güvenlik kontrolleri için). */
    User findByUsername(String username);
}
