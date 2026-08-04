--liquibase formatted sql

-- ====================================================================
--  Baslangic verileri: roller ve kullanicilar.
--  Menuler ve yetkiler 14_POSTGRESQL_INITDATA_PERMISSION_MENU.xml'de.
--  Sifreler BCrypt hash olarak tutulur (duz metin: 123456)
-- ====================================================================

--changeset danyengirisken:INITDATA_S_ROLE
insert into s_role (id, name, created_date) values (nextval('SEQ_S_ROLE'), 'ADMIN', now());
insert into s_role (id, name, created_date) values (nextval('SEQ_S_ROLE'), 'INTERN', now());

--changeset danyengirisken:INITDATA_S_USER
insert into s_user (id, full_name, username, password, role_id, created_date)
values (nextval('SEQ_S_USER'), 'Admin Kullanıcı', 'admin',
        '$2b$10$8idFxVvtqrwr/tIfHWJWKeo3vRAh.kAZ9oMeJourhN26x4Y.q3bq2',
        (select id from s_role where name = 'ADMIN'), now());
insert into s_user (id, full_name, username, password, role_id, created_date)
values (nextval('SEQ_S_USER'), 'Intern Kullanıcı', 'intern',
        '$2b$10$5vDqc9u341O8A1iLU8zWL.IamgYzBX5xsDyQKpcahygotCyUfwAcC',
        (select id from s_role where name = 'INTERN'), now());
