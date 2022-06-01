--liquibase formatted sql

--changeset chidamba:1
--comment Modify a column to increase size in cp_progress table
ALTER TABLE CHALLENGEPOINT_PROGRESS MODIFY(QUANTITY NUMBER(30,4));
--rollback ALTER TABLE CHALLENGEPOINT_PROGRESS MODIFY(QUANTITY NUMBER(18,2));
