--liquibase formatted sql

--changeset elizabet:1
--comment added column FROM_BUDGET_ID in BUDGET_HISTORY table to store from_budget id
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='BUDGET_HISTORY' and COLUMN_NAME ='FROM_BUDGET_ID'
ALTER TABLE BUDGET_HISTORY 
ADD FROM_BUDGET_ID NUMBER(18,0);
--rollback ALTER TABLE BUDGET_HISTORY DROP (FROM_BUDGET_ID);

--changeset elizabet:2
--comment added column ALLOW_ADDL_TRANS in BUDGET_MASTER table to store allow_addl transferrees
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='BUDGET_MASTER' and COLUMN_NAME ='ALLOW_ADDL_TRANS'
ALTER TABLE BUDGET_MASTER 
ADD ALLOW_ADDL_TRANS NUMBER(1,0) DEFAULT 0;
--rollback ALTER TABLE BUDGET_MASTER DROP (ALLOW_ADDL_TRANS);

