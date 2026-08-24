package com.danyengirisken.interntaskhub.repository;

import com.danyengirisken.interntaskhub.entity.Partner;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerDao extends JpaRepository<Partner, Long> {

    Optional<Partner> findByCode(String code);

    boolean existsByCode(String code);

    List<Partner> findAllByOrderByNameAsc();
}
