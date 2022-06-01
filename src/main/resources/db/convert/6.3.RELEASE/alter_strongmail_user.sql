--liquibase formatted sql

--changeset subramap:1
--comment add userToeknUrl column for Welcome Email content
alter table STRONGMAIL_USER
add USER_TOKEN_URL VARCHAR2(100 CHAR);
--rollback alter table STRONGMAIL_USER drop column USER_TOKEN_URL;

--changeset subramap:2
--comment STRONGMAIL_USER sequence
CREATE SEQUENCE STRONGMAIL_USER_PK_SQ INCREMENT BY 1 START WITH 5000;
--rollback DROP SEQUENCE STRONGMAIL_USER_PK_SQ;

--changeset subramap:3
--comment Adding Id column for hbm mapping
alter table STRONGMAIL_USER
add STRONGMAIL_USER_ID NUMBER(18);
--rollback alter table STRONGMAIL_USER drop column STRONGMAIL_USER_ID;

--changeset subramap:4
--comment Set strongmail user ID to sequence value for existing rows
UPDATE STRONGMAIL_USER
SET STRONGMAIL_USER_ID = STRONGMAIL_USER_PK_SQ.nextval;
--rollback not required

--changeset subramap:5
--comment Make ID column not null now that it has been set for existing rows
alter table STRONGMAIL_USER modify (strongmail_user_id not null);
--rollback alter table STRONGMAIL_USER modify (strongmail_user_id null);

--changeset subramap:6
--comment Alter table for adding Constraint
ALTER TABLE STRONGMAIL_USER
ADD CONSTRAINT STRONGMAIL_USER_PK_SQ PRIMARY KEY (STRONGMAIL_USER_ID)
USING INDEX;
--rollback ALTER TABLE STRONGMAIL_USER DROP CONSTRAINT STRONGMAIL_USER_PK_SQ ;

--changeset subramap:7
--comment Alter table for adding Constraint
ALTER TABLE STRONGMAIL_USER
MODIFY   USER_TOKEN_URL VARCHAR2(200 CHAR);
--rollback not required

