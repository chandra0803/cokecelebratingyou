--liquibase formatted sql

--changeset gorantla:1
--comment ADD column
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM USER_TAB_COLUMNS WHERE TABLE_NAME='NODE' AND COLUMN_NAME ='NODE_UUID';
 ALTER TABLE NODE ADD NODE_UUID VARCHAR2(50) ;
--rollback ALTER TABLE NODE DROP COLUMN NODE_UUID;
 
--changeset gorantla:2
--comment Add unique constraint on NODE_UUID
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM USER_CONSTRAINTS WHERE TABLE_NAME = 'NODE' AND CONSTRAINT_NAME = 'NODE_UUID_UK';
 ALTER TABLE NODE ADD CONSTRAINT NODE_UUID_UK UNIQUE (NODE_UUID);
--rollback ALTER TABLE NODE DROP CONSTRAINT NODE_UUID_UK; 