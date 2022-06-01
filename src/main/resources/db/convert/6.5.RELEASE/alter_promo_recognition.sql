--liquibase formatted sql

--changeset esakkimu:1
--comment ADD column
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='PROMO_RECOGNITION' and COLUMN_NAME ='IS_CONT_RES_MIGRATED'
 ALTER TABLE PROMO_RECOGNITION ADD IS_CONT_RES_MIGRATED NUMBER(1) DEFAULT 0 NOT NULL ;
--rollback ALTER TABLE PROMO_RECOGNITION DROP COLUMN IS_CONT_RES_MIGRATED;

