package com.danyengirisken.interntaskhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Görev (task) modülü ana varligi. Carbon domain entity konvansiyonu
 * (prefiks + sequence ID + Auditable) referans alinmistir.
 *
 *  status   : TODO | IN_PROGRESS | DONE
 *  priority : LOW | MEDIUM | HIGH
 */
@Entity
@Table(name = "T_SPRINT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tsprint extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_SPRINT")
    @SequenceGenerator(name = "SEQ_T_SPRINT", sequenceName = "SEQ_T_SPRINT", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "project_id", nullable = false)
    private Long project_id;

    @Column(name = "start_date")
    private LocalDate start_date;

    @Column(name = "end_date")
    private LocalDate end_date;

    /** Aktiflik durumu: '1' aktif, '0' pasif (S_PARTNER / T_PROJECT ile ayni). */
    @Column(name = "active", length = 2)
    private String active;


}
