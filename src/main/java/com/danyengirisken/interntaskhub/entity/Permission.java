package com.danyengirisken.interntaskhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Yetki (permission) — PTT S_PERMISSION referansi.
 * Bir menu bir yetkiye baglanir; yetki S_ROLE_PERMISSION ile rollere verilir.
 *
 *  - name  : benzersiz teknik ad (orn. TaskController, UserController)
 *  - title : kullaniciya gorunen aciklama
 */
@Entity
@Table(name = "S_PERMISSION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Permission extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_S_PERMISSION")
    @SequenceGenerator(name = "SEQ_S_PERMISSION", sequenceName = "SEQ_S_PERMISSION", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "title", length = 150)
    private String title;
}
