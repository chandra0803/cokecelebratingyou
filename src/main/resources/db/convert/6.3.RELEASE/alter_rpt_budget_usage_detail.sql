--liquibase formatted sql

--changeset dhanekul:1
--comment alter budget owner name column size to 300 for RPT_CASH_BUDGET_USAGE_DETAIL
ALTER TABLE RPT_CASH_BUDGET_USAGE_DETAIL
MODIFY  BUDGET_OWNER_NAME VARCHAR2(300);
--rollback not required

--changeset dhanekul:2
--comment alter budget owner name column size to 300 for RPT_BUDGET_USAGE_DETAIL
ALTER TABLE RPT_BUDGET_USAGE_DETAIL
MODIFY  BUDGET_OWNER_NAME VARCHAR2(300);
--rollback not required
