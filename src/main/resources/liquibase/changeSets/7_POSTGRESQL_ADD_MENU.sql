--liquibase formatted sql

-- ====================================================================
--  Menu tanimlari (carbon add_carbon_menu_postgre.sql insert yapisi referans)
--
--  Kolonlar : id, parent_id, title, page, icon, menu_order, active
--    - parent_id : ust menu id'si (grup); en ust seviye icin null
--    - page      : Angular route'u; grup (parent) menulerde null
--    - icon      : Material Icons adi (orn. dashboard, group, task_alt)
--    - menu_order: ayni seviyede siralama
--
--  YENI EKRAN EKLEMEK ICIN: asagidaki sablonu kopyalayip yeni bir
--  --changeset altina ekleyin (uygulanmis changeset'leri DEGISTIRMEYIN):
--
--    --changeset <yazar>:ADD_MENU_<EKRAN>
--    insert into s_menu (id, parent_id, title, page, icon, menu_order, active)
--    values (nextval('SEQ_S_MENU'),
--            (select id from s_menu where title = '<UST_MENU>'),  -- veya null
--            '<Baslik>', '/<route>', '<icon>', <sira>, true);
-- ====================================================================

--changeset danyengirisken:ADD_MENU_TANIMLAMALAR
insert into s_menu (id, parent_id, title, page, icon, menu_order, active)
values (nextval('SEQ_S_MENU'), null, 'Tanımlamalar', null, 'settings', 10, true);

--changeset danyengirisken:ADD_MENU_GOREVLER
insert into s_menu (id, parent_id, title, page, icon, menu_order, active)
values (nextval('SEQ_S_MENU'),
        (select id from s_menu where title = 'Tanımlamalar'),
        'Görevler', '/gorevler', 'task_alt', 5, true);

--changeset danyengirisken:ADD_MENU_KULLANICILAR
insert into s_menu (id, parent_id, title, page, icon, menu_order, active)
values (nextval('SEQ_S_MENU'),
        (select id from s_menu where title = 'Tanımlamalar'),
        'Kullanıcılar', '/kullanicilar', 'group', 10, true);
