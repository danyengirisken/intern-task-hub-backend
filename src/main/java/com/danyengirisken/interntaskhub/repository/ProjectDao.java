package com.danyengirisken.interntaskhub.repository;

import com.danyengirisken.interntaskhub.entity.Tproject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectDao extends JpaRepository<Tproject, Long> {

    @Query("SELECT t FROM Tproject t WHERE t.partner_id = :partnerId")
    List<Tproject> findByPartner_id(@Param("partnerId") Long partnerId);

}