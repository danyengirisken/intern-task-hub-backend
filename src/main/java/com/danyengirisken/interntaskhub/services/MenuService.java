package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.dto.MenuDto;
import java.util.List;

/**
 * Menu modulu is mantigi (carbon: interface + Impl konvansiyonu).
 */
public interface MenuService {

    /** Verilen rolun gorebilecegi menuler (duz liste; agaci frontend kurar). */
    List<MenuDto> findByRoleId(Long roleId);

    /** Oturumdaki kullanicinin menuleri. */
    List<MenuDto> findForCurrentUser();
}
