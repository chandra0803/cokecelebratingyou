--liquibase formatted sql

--changeset sundaram:1
--comment MODIFY  column
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(*) from all_tab_columns where TABLE_NAME ='ACTIVITY' and COLUMN_NAME='CASH_AWARD_QTY'
alter table activity modify CASH_AWARD_QTY number(32,16);
--rollback alter table activity modify CASH_AWARD_QTY number(18,4);

--changeset sundaram:2
--comment MODIFY  column
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(*) from all_tab_columns where TABLE_NAME ='CLAIM_RECIPIENT' and COLUMN_NAME='CASH_AWARD_QTY'
alter table CLAIM_RECIPIENT modify CASH_AWARD_QTY number(32,16);
--rollback alter table CLAIM_RECIPIENT modify CASH_AWARD_QTY number(18,4);


--changeset sundaram:3
--comment MODIFY  column
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(*) from all_tab_columns where TABLE_NAME ='TMP_APPROVER_NOMI_DETAIL' and COLUMN_NAME='AWARD_AMOUNT'
alter table tmp_approver_nomi_detail modify AWARD_AMOUNT number(32,16);
--rollback alter table tmp_approver_nomi_detail modify AWARD_AMOUNT number(18,2);


--changeset sundaram:4
--comment MODIFY  column
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(*) from all_tab_columns where TABLE_NAME ='TMP_NOMINATION_WINDOW_MODAL_DL' and COLUMN_NAME='CASH_WON'
alter table tmp_nomination_window_modal_dl modify CASH_WON number(32,16);
--rollback alter table tmp_nomination_window_modal_dl modify CASH_WON number(18,2);