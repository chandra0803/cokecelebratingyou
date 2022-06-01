--liquibase formatted sql

--changeset rameshj:1
--comment Add column to determine whetehr the recogntion comes from RA flow or normal recogniton flow
ALTER TABLE scheduled_recognition ADD (SOURCE VARCHAR2(200 CHAR));
--rollback ALTER TABLE scheduled_recognition DROP (SOURCE);