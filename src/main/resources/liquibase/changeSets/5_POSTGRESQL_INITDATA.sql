--liquibase formatted sql

-- ====================================================================
--  Baslangic verileri: roller ve kullanicilar.
--  Yetkiler ve menuler 14_POSTGRESQL_S_MENU_ADD_PERMISSION.xml'de.
--  Sifreler BCrypt hash olarak tutulur (duz metin: 123456)
-- ====================================================================

--changeset danyengirisken:INITDATA_S_ROLE
-- ADMIN          : sistemin sahibi, tum partnerler, her ekran
-- CUSTOMER_ADMIN : partner (musteri firma) yoneticisi, yalnizca kendi partneri
-- CUSTOMER       : son kullanici, yalnizca Calisma Alani ekranlari
-- Rollerin hangi yetkilere sahip oldugu: 14_POSTGRESQL_S_MENU_ADD_PERMISSION.xml
insert into s_role (id, name, created_date) values (nextval('SEQ_S_ROLE'), 'ADMIN', now());
insert into s_role (id, name, created_date) values (nextval('SEQ_S_ROLE'), 'CUSTOMER_ADMIN', now());
insert into s_role (id, name, created_date) values (nextval('SEQ_S_ROLE'), 'CUSTOMER', now());

--changeset danyengirisken:INITDATA_S_USER
-- Kullanicinin bagli oldugu partner S_USER.partner_id ile tutulur; kolon
-- 17_POSTGRESQL_ALTER_S_USER_ADD_PARTNER.xml'de eklenip varsayilan partnere baglanir.
insert into s_user (id, full_name, username, password, role_id, created_date)
values (nextval('SEQ_S_USER'), 'Admin Kullanıcı', 'admin',
        '$2b$10$8idFxVvtqrwr/tIfHWJWKeo3vRAh.kAZ9oMeJourhN26x4Y.q3bq2',
        (select id from s_role where name = 'ADMIN'), now());
insert into s_user (id, full_name, username, password, role_id, created_date)
values (nextval('SEQ_S_USER'), 'Partner Yöneticisi', 'customeradmin',
        '$2b$10$5vDqc9u341O8A1iLU8zWL.IamgYzBX5xsDyQKpcahygotCyUfwAcC',
        (select id from s_role where name = 'CUSTOMER_ADMIN'), now());
insert into s_user (id, full_name, username, password, role_id, created_date)
values (nextval('SEQ_S_USER'), 'Intern Kullanıcı', 'intern',
        '$2b$10$5vDqc9u341O8A1iLU8zWL.IamgYzBX5xsDyQKpcahygotCyUfwAcC',
        (select id from s_role where name = 'CUSTOMER'), now());
