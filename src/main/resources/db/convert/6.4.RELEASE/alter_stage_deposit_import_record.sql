--liquibase formatted sql

--changeset chidamba:1
--comment Increasing column size from 500 to 2000
ALTER TABLE STAGE_DEPOSIT_IMPORT_RECORD MODIFY COMMENTS VARCHAR2(2000 CHAR);
--rollback not required
