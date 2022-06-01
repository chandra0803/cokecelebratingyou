--liquibase formatted sql

--changeset palaniss:1
--comment added column to capture batch description, batch number and program number.
ALTER TABLE rpt_qcard_detail ADD (batch_description VARCHAR2(500),batch_number NUMBER,program_number NUMBER);
--rollback ALTER TABLE rpt_qcard_detail DROP (batch_description,batch_number,program_number);
