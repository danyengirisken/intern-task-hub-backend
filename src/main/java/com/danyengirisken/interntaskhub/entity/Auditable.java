package com.danyengirisken.interntaskhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Tum kayit tablolari icin ortak denetim (audit) kolonlari.
 * Carbon'daki Auditable taban sinifinin sade karsiligidir (Envers yok).
 */
@MappedSuperclass
@Getter
@Setter
public abstract class Auditable {

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}
