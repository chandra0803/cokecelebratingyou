--liquibase formatted sql

--changeset esakkimu:1
--comment added column to capture progress update notification send to a participant , manager or not(yes or no)
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='SSI_CONTEST' and COLUMN_NAME ='SAVE_AND_SEND_PROGRESS_UPDATE'
ALTER TABLE SSI_CONTEST
ADD  SAVE_AND_SEND_PROGRESS_UPDATE VARCHAR2(10 CHAR);  
--rollback ALTER TABLE SSI_CONTEST DROP (SAVE_AND_SEND_PROGRESS_UPDATE);

