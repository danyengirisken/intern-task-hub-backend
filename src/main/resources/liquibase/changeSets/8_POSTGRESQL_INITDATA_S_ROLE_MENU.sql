--liquibase formatted sql

-- ====================================================================
--  Rollere menu atama (S_ROLE_MENU). Dashboard her iki role daha once
--  (5_POSTGRESQL_INITDATA.sql) atanmisti; burada yeni menuler eklenir.
--
--  Not: Bir alt menunun gorunmesi icin kullanicinin rolune hem alt menu
--  hem de ust (parent) menu atanmalidir.
-- ====================================================================

--changeset danyengirisken:ADD_ROLE_MENU_ADMIN
insert into s_role_menu (role_id, menu_id)
select (select id from s_role where name = 'ADMIN'), m.id
from s_menu m
where m.title in ('Tanımlamalar', 'Görevler', 'Kullanıcılar');

--changeset danyengirisken:ADD_ROLE_MENU_INTERN
insert into s_role_menu (role_id, menu_id)
select (select id from s_role where name = 'INTERN'), m.id
from s_menu m
where m.title in ('Tanımlamalar', 'Görevler');
