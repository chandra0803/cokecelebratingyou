--liquibase formatted sql

--changeset kancherla:1
--comment Delete sea enabled flag from recognition promotion
ALTER TABLE promo_recognition DROP (is_email_enabled);
--rollback ALTER TABLE promo_recognition ADD is_email_enabled NUMBER(1) DEFAULT 0;

--changeset kancherla:2
--comment Delete sea confirm recognition enabled flag from recognition promotion
ALTER TABLE promo_recognition DROP (email_confirm_enabled);
--rollback ALTER TABLE promo_recognition ADD email_confirm_enabled NUMBER(1) DEFAULT 1;

--changeset chidamba:3
--comment Add constraint to promo_recognition on column PUBLIC_REC_BUDGET_MASTER_ID with reference to BUDGET_MASTER_ID in budget_master table.
ALTER TABLE promo_recognition ADD CONSTRAINT PUBLIC_REC_BUDGET_MASTER_ID_FK FOREIGN KEY (PUBLIC_REC_BUDGET_MASTER_ID) REFERENCES budget_master(BUDGET_MASTER_ID);
--rollback ALTER TABLE promo_recognition DROP CONSTRAINT PUBLIC_REC_BUDGET_MASTER_ID_FK;
