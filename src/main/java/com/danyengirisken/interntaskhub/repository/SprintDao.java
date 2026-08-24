package com.danyengirisken.interntaskhub.repository;

import com.danyengirisken.interntaskhub.entity.Tsprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintDao extends JpaRepository<Tsprint, Long> {

    // Spring'in kafası karışmasın diye SQL benzeri (JPQL) kendi sorgumuzu yazıyoruz
    @Query("SELECT s FROM Tsprint s WHERE s.project_id = :projectId")
    List<Tsprint> findByProjectId(@Param("projectId") Long projectId);
}