--liquibase formatted sql

--changeset subramap:1
--comment added column OWN_CARD_NAME in SCHEDULED_RECOGNITION table to store Own card name
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='SCHEDULED_RECOGNITION' and COLUMN_NAME ='OWN_CARD_NAME'
ALTER TABLE SCHEDULED_RECOGNITION 
ADD OWN_CARD_NAME VARCHAR2(800 CHAR);
--rollback ALTER TABLE SCHEDULED_RECOGNITION DROP (OWN_CARD_NAME);

--changeset sivanand:2
--comment added column to place hierarchy uuid
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='HIERARCHY' and COLUMN_NAME ='HIERARCHY_UUID'
ALTER TABLE HIERARCHY ADD HIERARCHY_UUID VARCHAR2(100 CHAR);  
--rollback ALTER TABLE HIERARCHY DROP (HIERARCHY_UUID);