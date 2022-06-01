--liquibase formatted sql

--changeset cornelius:1
--comment Add verification status type column to user_phone
ALTER TABLE USER_PHONE
ADD (verification_status VARCHAR2(30) DEFAULT 'unverified' NOT NULL);
--rollback ALTER TABLE USER_PHONE DROP (verification_status);

--changeset cornelius:2
--comment Add verification status type column to user_email_address
ALTER TABLE USER_EMAIL_ADDRESS
ADD (verification_status VARCHAR2(30) DEFAULT 'unverified' NOT NULL);
--rollback ALTER TABLE USER_EMAIL_ADDRESS DROP (verification_status);