--liquibase formatted sql

--changeset gorantla:1
--comment Add unique constraint on HIERARCHY_UUID
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM USER_CONSTRAINTS WHERE TABLE_NAME = 'HIERARCHY' AND CONSTRAINT_NAME = 'HIERARCHY_UUID_UK';
 ALTER TABLE HIERARCHY ADD CONSTRAINT HIERARCHY_UUID_UK UNIQUE (HIERARCHY_UUID);
--rollback ALTER TABLE HIERARCHY DROP CONSTRAINT HIERARCHY_UUID_UK;