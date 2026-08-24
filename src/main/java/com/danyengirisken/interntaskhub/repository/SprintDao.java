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

    /**
     * Bir partnerin sprintleri. Sprint'in partner'i, bagli oldugu projeden gelir
     * (T_SPRINT.project_id -> T_PROJECT.partner_id).
     */
    @Query("""
            SELECT s FROM Tsprint s
             WHERE s.project_id IN (SELECT p.id FROM Tproject p WHERE p.partner_id = :partnerId)
             ORDER BY s.id DESC
            """)
    List<Tsprint> findAllByPartnerId(@Param("partnerId") Long partnerId);
}
