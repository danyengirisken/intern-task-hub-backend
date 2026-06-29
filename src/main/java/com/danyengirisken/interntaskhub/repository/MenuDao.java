package com.danyengirisken.interntaskhub.repository;

import com.danyengirisken.interntaskhub.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuDao extends JpaRepository<Menu, Long> {
}
