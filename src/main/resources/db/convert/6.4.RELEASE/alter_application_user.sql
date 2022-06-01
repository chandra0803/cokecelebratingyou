--liquibase formatted sql

--changeset ramesh:1
--comment Add column to check rawelcomeEmail has sent 
ALTER TABLE APPLICATION_USER
add IS_RA_WELCOME_EMAIL_SENT NUMBER(1) DEFAULT 0 NOT NULL;
--rollback ALTER TABLE APPLICATION_USER DROP (IS_RA_WELCOME_EMAIL_SENT);


