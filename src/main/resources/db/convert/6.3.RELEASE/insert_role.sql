--liquibase formatted sql

--changeset ramesh:1
--comment Insert Plateau Redemption Role.
INSERT INTO ROLE (ROLE_ID, NAME, HELP_TEXT, CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
SELECT ROLE_PK_SQ.nextval, 'PLATEAU_REDEMPTION', 'Grants user ability to redeem plateau awards for participants', 'PLATEAU_REDEMPTION', 1,0,0,sysdate,NULL,NULL,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM ROLE WHERE name='PLATEAU_REDEMPTION');
--rollback DELETE FROM ROLE WHERE CODE='PLATEAU_REDEMPTION';

--changeset subramap:2
--comment Adding new role for issuing employee password Security patch 3 
INSERT INTO ROLE (ROLE_ID, NAME, CODE, HELP_TEXT, IS_ACTIVE,CREATED_BY, DATE_CREATED, VERSION, IS_PAX_SPECIFIC)
SELECT ROLE_PK_SQ.NEXTVAL, 'Re-Issue Send Password', 'REISSUE_SEND_PASSWORD', 'Grants user ability to re-issue a pax password AND send to the users email address on record',1, 0, SYSDATE, 0, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM ROLE WHERE name='Re-Issue Send Password');
--rollback DELETE from role WHERE code = 'REISSUE_SEND_PASSWORD';

--changeset subramap:3
--comment Adding new role for adding/modifying employee's recovery information
INSERT INTO ROLE (ROLE_ID, NAME, CODE, HELP_TEXT, IS_ACTIVE,CREATED_BY, DATE_CREATED, VERSION, IS_PAX_SPECIFIC)
SELECT ROLE_PK_SQ.NEXTVAL, 'Update Account Recovery Info', 'MODIFY_RECOVERY_CONTACTS', 'Grants user ability to add or modify the recovery contacts',1, 0, SYSDATE, 0, 0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM ROLE WHERE name='Update Account Recovery Info');
--rollback DELETE from role WHERE code = 'MODIFY_RECOVERY_CONTACTS'; 


--changeset subramap:4
--comment removing reissue password related role
DELETE from user_role where role_id in (select role_id from role where code='REISSUE_PASSWORD');
DELETE from user_type_role where role_id = (select role_id from role where code='REISSUE_PASSWORD');
DELETE from role where code='REISSUE_PASSWORD';
--rollback not required
