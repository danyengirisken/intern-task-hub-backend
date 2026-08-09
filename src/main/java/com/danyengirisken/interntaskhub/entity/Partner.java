package com.danyengirisken.interntaskhub.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Uygulamaya giris yapan kullanici. Sifre BCrypt hash olarak saklanir.
 */
@Entity
@Table(name = "S_PARTNER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Partner extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_S_PARTNER")
    @SequenceGenerator(name = "SEQ_S_PARTNER", sequenceName = "SEQ_S_PARTNER", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false, length = 12)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "using_language", nullable = false, length = 100)
    private String using_language;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "active", nullable = false ,length = 2)
    private String avtive;

}
