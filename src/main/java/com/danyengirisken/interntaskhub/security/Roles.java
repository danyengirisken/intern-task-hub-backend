package com.danyengirisken.interntaskhub.security;

/**
 * Sistemdeki rol adlari (S_ROLE.name) tek yerde.
 *
 *  ADMIN          : sistemin sahibi. Tum partnerlerin verisini gorur,
 *                   Partner ekrani dahil her ekrana erisir.
 *  CUSTOMER_ADMIN : partner (musteri firma) yoneticisi. Yalnizca kendi
 *                   partnerinin verisini gorur; kendi partnerindeki
 *                   kullanicilara rol atayabilir, Partner ekranini goremez.
 *  CUSTOMER       : son kullanici. Yalnizca Calisma Alani ekranlari.
 *
 * Ornek: Kilic Holding bir S_PARTNER'dir; Tahir o partnerin CUSTOMER_ADMIN'i,
 * Mirac ve Ahmet ise CUSTOMER rolundeki kullanicilaridir. Kullanicinin hangi
 * partnere ait oldugu S_USER.partner_id ile tutulur — ayri bir "customer"
 * tablosu yoktur, customer kullanicinin kendisidir.
 */
public final class Roles {

    public static final String ADMIN = "ADMIN";
    public static final String CUSTOMER_ADMIN = "CUSTOMER_ADMIN";
    public static final String CUSTOMER = "CUSTOMER";

    private Roles() {
    }
}
