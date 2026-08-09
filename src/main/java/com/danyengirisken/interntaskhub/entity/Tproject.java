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
@Table(name = "T_PROJECT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tproject extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PROJECT")
    @SequenceGenerator(name = "SEQ_T_PROJECT", sequenceName = "SEQ_T_PROJECT", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "partner_id", nullable = false, length = 200)
    private Long partner_id;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "active")
    private String active;

    @Column(name = "start_date")
    private LocalDate start_date;

    @Column(name = "end_date")
    private LocalDate end_date;


}
