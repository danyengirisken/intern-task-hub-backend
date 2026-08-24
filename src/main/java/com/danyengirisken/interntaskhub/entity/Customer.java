package com.danyengirisken.interntaskhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Uygulamaya giris yapan kullanici. Sifre BCrypt hash olarak saklanir.
 */
@Entity
@Table(name = "S_CUSTOMER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Customer extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_S_CUSTOMER")
    @SequenceGenerator(name = "SEQ_S_CUSTOMER", sequenceName = "SEQ_S_CUSTOMER", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "partner_id" , nullable = false)
    private Long partnerId;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "city_code")
    private String cityCode;

    @Column(name = "town_code")
    private String townCode;

    @Column(name = "active")
    private String active;

    @Column(name = "description")
    private String description;
}
