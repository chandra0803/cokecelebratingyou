--liquibase formatted sql

--changeset ramesh:1
--comment Insert Plateau Redemption USER_TYPE_ROLE.
INSERT INTO USER_TYPE_ROLE (ROLE_ID, USER_TYPE_CODE) 
SELECT (SELECT ROLE_ID from ROLE where code='PLATEAU_REDEMPTION'),'bi' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM USER_TYPE_ROLE WHERE ROLE_id = (SELECT ROLE_ID from ROLE where code='PLATEAU_REDEMPTION') and USER_TYPE_CODE = 'bi') ;
--rollback DELETE FROM USER_TYPE_ROLE WHERE role_id = (SELECT ROLE_ID from ROLE where code='PLATEAU_REDEMPTION') and USER_TYPE_code = 'bi';

--changeset subramap:2
--comment Adding new user type role for issuing employee password Security patch 3 
INSERT INTO USER_TYPE_ROLE (ROLE_ID, USER_TYPE_CODE) 
SELECT (SELECT ROLE_ID from ROLE where code='REISSUE_SEND_PASSWORD'),'pax' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM USER_TYPE_ROLE WHERE ROLE_id = (SELECT ROLE_ID from ROLE where code='REISSUE_SEND_PASSWORD') and USER_TYPE_CODE = 'pax') ;
--rollback DELETE FROM user_type_role where role_id =(SELECT role_id FROM role WHERE code = 'REISSUE_SEND_PASSWORD') and user_type_code = 'pax';

--changeset subramap:3
--comment Adding new user type role for modifying recovery contacts for Client users
INSERT INTO USER_TYPE_ROLE (ROLE_ID, USER_TYPE_CODE) 
SELECT (SELECT ROLE_ID from ROLE where code='MODIFY_RECOVERY_CONTACTS'),'clt' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM USER_TYPE_ROLE WHERE ROLE_id = (SELECT ROLE_ID from ROLE where code='MODIFY_RECOVERY_CONTACTS') and USER_TYPE_CODE = 'clt') ;
--rollback DELETE FROM user_type_role where role_id =(SELECT role_id FROM role WHERE code = 'MODIFY_RECOVERY_CONTACTS') and user_type_code = 'clt';

--changeset subramap:4
--comment Adding new user type role for modifying recovery contacts for BI Users
INSERT INTO USER_TYPE_ROLE (ROLE_ID, USER_TYPE_CODE) 
SELECT (SELECT ROLE_ID from ROLE where code='MODIFY_RECOVERY_CONTACTS'),'bi' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM USER_TYPE_ROLE WHERE ROLE_id = (SELECT ROLE_ID from ROLE where code='MODIFY_RECOVERY_CONTACTS') and USER_TYPE_CODE = 'bi') ;
--rollback DELETE FROM user_type_role where role_id =(SELECT role_id FROM role WHERE code = 'MODIFY_RECOVERY_CONTACTS') and user_type_code = 'bi';

--changeset subramap:5
--comment removing reissue password related roles
delete from user_type_role where role_id = (select role_id from role where code='REISSUE_PASSWORD');
--rollback not required
