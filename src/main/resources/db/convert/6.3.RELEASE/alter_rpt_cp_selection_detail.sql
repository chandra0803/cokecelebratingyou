--liquibase formatted sql

--changeset chidamba:1
--comment Modify a column to increase size in rpt_cp_selection_detail table
ALTER TABLE rpt_cp_selection_detail MODIFY (PROGRESS_CHALLENGEPOINT NUMBER(30,4));
--rollback not required

