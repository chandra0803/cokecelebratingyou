--liquibase formatted sql

--changeset sundaram:1
--comment Add CLOB column
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(*) from all_tab_columns where TABLE_NAME ='CLAIM_CFSE' and COLUMN_NAME='VALUE' AND DATA_TYPE='CLOB'
alter table CLAIM_CFSE ADD VALUE1 CLOB;
--rollback not required

--changeset sundaram:2
--comment update varchar value date to value1
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(*) from all_tab_columns where TABLE_NAME ='CLAIM_CFSE' and COLUMN_NAME='VALUE1' AND DATA_TYPE='CLOB'
update CLAIM_CFSE set VALUE1=VALUE;
--rollback not required

--changeset sundaram:3
--comment drop the value column
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(*) from all_tab_columns where TABLE_NAME ='CLAIM_CFSE' and COLUMN_NAME='VALUE1'
alter table CLAIM_CFSE DROP COLUMN VALUE;
--rollback not required

--changeset sundaram:4
--comment Rename value1 to value
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(*) from all_tab_columns where TABLE_NAME ='CLAIM_CFSE' and COLUMN_NAME='VALUE1'
alter table CLAIM_CFSE rename COLUMN VALUE1 to VALUE;
--rollback not required
