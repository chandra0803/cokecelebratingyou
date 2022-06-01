--liquibase formatted sql

--changeset ramesh:1
--comment removed REISSUE_SEND_PASSWORD data
DELETE from user_role where role_id in (select role_id from role where code='REISSUE_SEND_PASSWORD');
DELETE from user_type_role where role_id in (select role_id from role where code='REISSUE_SEND_PASSWORD');
DELETE from ROLE WHERE CODE = 'REISSUE_SEND_PASSWORD';
--rollback not required
