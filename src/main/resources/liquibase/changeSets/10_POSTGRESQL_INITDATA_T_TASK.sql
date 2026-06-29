--liquibase formatted sql

-- ====================================================================
--  Örnek görevler (demo verisi)
-- ====================================================================

--changeset danyengirisken:INITDATA_T_TASK
insert into t_task (id, title, description, status, priority, due_date, created_date)
values (nextval('SEQ_T_TASK'), 'Projeyi kur', 'Backend ve frontend iskeletini ayağa kaldır',
        'DONE', 'HIGH', current_date, now());
insert into t_task (id, title, description, status, priority, due_date, created_date)
values (nextval('SEQ_T_TASK'), 'Login ekranını tamamla', 'JWT ile giriş akışı',
        'DONE', 'MEDIUM', current_date, now());
insert into t_task (id, title, description, status, priority, due_date, created_date)
values (nextval('SEQ_T_TASK'), 'Görev modülü CRUD', 'Görevler ekranını CRUD olarak geliştir',
        'IN_PROGRESS', 'HIGH', current_date + 7, now());
