--liquibase formatted sql

--changeset chidamba:1
--comment Increase column size to 4000
ALTER TABLE rpt_budget_usage_detail MODIFY budget_owner_name VARCHAR2(4000);
--rollback not required
