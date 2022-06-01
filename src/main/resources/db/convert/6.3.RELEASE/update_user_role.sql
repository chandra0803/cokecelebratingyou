--liquibase formatted sql

--changeset subramap:1
--comment Updating Role IDs of the users those who have Reissue password role to Account recovery info
update user_role
set role_id = (select role_id from role where code='MODIFY_RECOVERY_CONTACTS')
where role_id in (select role_id from role where code='REISSUE_PASSWORD');
--rollback not required

--changeset ramesh:2
--comment delete the REISSUE_SEND_PASSWORD role 
DELETE FROM USER_ROLE WHERE role_id = (SELECT role_id FROM role WHERE code = 'REISSUE_SEND_PASSWORD')
--rollback not required

