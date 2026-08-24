package com.danyengirisken.interntaskhub.repository;

import com.danyengirisken.interntaskhub.entity.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuDao extends JpaRepository<Menu, Long> {

    /**
     * Verilen rolun gorebilecegi aktif menuler.
     * Erisim modeli: S_MENU.permission_id -> S_PERMISSION -> S_ROLE_PERMISSION -> S_ROLE.
     * permission_id null ise menu herkese aciktir.
     */
    @Query("""
            select m from Menu m
             where m.active = true
               and (m.permissionId is null
                    or m.permissionId in (
                        select p.id from Role r join r.permissions p where r.id = :roleId))
             order by m.menuOrder asc, m.id asc
            """)
    List<Menu> findAllowedByRoleId(@Param("roleId") Long roleId);
}
