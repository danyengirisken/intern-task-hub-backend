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
 * Hiyerarsik uygulama menu ogesi (carbon S_MENU yapisi referans alindi).
 *
 *  - parentId     : ust menu (grup); null ise en ust seviye menudur
 *  - page         : yonlendirilecek route; grup menulerde null
 *  - menuOrder    : ayni seviyedeki siralama
 *  - active       : menunun gosterilip gosterilmeyecegi
 *  - permissionId : menuyu goren yetki (S_PERMISSION); null ise herkese acik
 *
 * Yeni bir ekran eklemek icin S_MENU'ya yeni bir satir insert etmek yeterlidir
 * (bkz. 14_POSTGRESQL_INITDATA_S_PERMISSION.sql icindeki ekleme sablonu).
 */
@Entity
@Table(name = "S_MENU")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Menu extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_S_MENU")
    @SequenceGenerator(name = "SEQ_S_MENU", sequenceName = "SEQ_S_MENU", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "page", length = 200)
    private String page;

    @Column(name = "icon", length = 100)
    private String icon;

    @Column(name = "menu_order")
    private Integer menuOrder;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "permission_id")
    private Long permissionId;
}
