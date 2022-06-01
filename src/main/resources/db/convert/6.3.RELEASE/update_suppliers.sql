--liquibase formatted sql

--changeset kothanda:1
--comment updating UK supplier to have PS v2 SSO enabled
UPDATE supplier SET allow_partner_sso = 1 WHERE supplier_name = 'BII';
--rollback UPDATE supplier SET allow_partner_sso = 0 WHERE supplier_name = 'BII';