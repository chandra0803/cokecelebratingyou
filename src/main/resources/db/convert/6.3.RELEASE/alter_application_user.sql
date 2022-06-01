--liquibase formatted sql

--changeset subramap:1
--comment Add column to capture reissued password expiry date for Security patch 3
ALTER TABLE APPLICATION_USER
ADD (reissued_password_expire_date DATE);
--rollback ALTER TABLE APPLICATION_USER DROP (reissued_password_expire_date);

--changeset siemback:2
--comment Add column to determine when the lock timeout expires
ALTER TABLE APPLICATION_USER
ADD (LOCK_TIMEOUT_EXPIRE DATE);
--rollback ALTER TABLE APPLICATION_USER DROP (LOCK_TIMEOUT_EXPIRE);

--changeset siemback:3
--comment Add column to flag the user has locked (hard lock)
ALTER TABLE APPLICATION_USER
add IS_ACCOUNT_LOCKED NUMBER(1) DEFAULT 0 NOT NULL;
--rollback ALTER TABLE APPLICATION_USER DROP (IS_ACCOUNT_LOCKED);

--changeset ramesh:4
--comment Drop reissued_password_expire_date column from APPLICATION_USER
ALTER TABLE APPLICATION_USER DROP column reissued_password_expire_date;
-- rollback ALTER TABLE APPLICATION_USER ADD (reissued_password_expire_date DATE);
