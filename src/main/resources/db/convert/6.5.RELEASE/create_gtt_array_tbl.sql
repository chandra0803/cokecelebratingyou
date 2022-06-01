--liquibase formatted sql

--changeset gorantla:1
--comment Create new temp table for handle pipeline function
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_tables where table_name='GTT_ARRAY_TBL'
  CREATE GLOBAL TEMPORARY TABLE GTT_ARRAY_TBL(COLUMN_DESC VARCHAR2(100))
  ON COMMIT DELETE ROWS
NOCACHE;
--rollback not required