package com.danyengirisken.interntaskhub.repository;

import com.danyengirisken.interntaskhub.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
