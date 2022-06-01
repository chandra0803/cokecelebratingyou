--liquibase formatted sql

--changeset elizabet:1
--comment ADD column
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='PURL_CONTRIBUTOR' and COLUMN_NAME ='DEFAULT_INVITEE'
 ALTER TABLE PURL_CONTRIBUTOR ADD DEFAULT_INVITEE NUMBER(1) DEFAULT 1 NOT NULL ;
--rollback ALTER TABLE PURL_CONTRIBUTOR DROP COLUMN DEFAULT_INVITEE;