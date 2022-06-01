--liquibase formatted sql

--changeset sundaram:1
--comment Incresing the size of the column
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(*) from all_tab_columns where table_name ='USER_ACL' AND COLUMN_NAME='TARGET' AND CHAR_LENGTH<>255
  ALTER TABLE USER_ACL MODIFY TARGET VARCHAR2(255);
--rollback not required