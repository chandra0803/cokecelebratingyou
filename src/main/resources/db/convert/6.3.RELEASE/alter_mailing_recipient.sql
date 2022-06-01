--liquibase formatted sql

--changeset siemback:1
--comment Added a column to identify an alternate email address other than primary
ALTER TABLE MAILING_RECIPIENT ADD ALT_EMAIL_ADDR_ID NUMBER(18);
--rollback ALTER TABLE MAILING_RECIPIENT DROP (ALT_EMAIL_ADDR_ID); 

--changeset siemback:2
--comment Added a column to identify an alternate email address other than primary
ALTER TABLE MAILING_RECIPIENT ADD ALT_PHONE_ID NUMBER(18);
--rollback ALTER TABLE MAILING_RECIPIENT DROP (ALT_PHONE_ID); 
