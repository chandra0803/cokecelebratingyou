--liquibase formatted sql

--changeset Chidamba:1
--comment temporary table for tuning.
CREATE GLOBAL TEMPORARY TABLE gtt_promo_list
(
  PROMOTION_ID             NUMBER(18)           NOT NULL,
  PROMOTION_TYPE           VARCHAR2(80 CHAR)    NOT NULL,
  PRIMARY_AUDIENCE_TYPE    VARCHAR2(40 CHAR),
  SECONDARY_AUDIENCE_TYPE  VARCHAR2(40 CHAR)
);

--rollback DROP TABLE gtt_promo_list CASCADE CONSTRAINTS;
