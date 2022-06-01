--liquibase formatted sql

--changeset palaniss:1
--comment Create temporary table for badge tuning
CREATE GLOBAL TEMPORARY TABLE GTT_RPT_BADGE_COUNT
(
USER_ID           NUMBER(18),
PROMO_TYPE        VARCHAR2(100 CHAR),
DATE_CREATED      DATE,
BADGE_RULE_COUNT  NUMBER(18)
)
ON COMMIT PRESERVE ROWS
NOCACHE
--rollback DROP TABLE GTT_RPT_BADGE_COUNT;
