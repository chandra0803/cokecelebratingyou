CREATE TABLE RPT_PAX_PROMO_ELIG_STAGE
(
  KEY_FIELD_HASH    RAW(16)                     NOT NULL,
  PROMOTION_ID      NUMBER(18)                  NOT NULL,
  PARTICIPANT_ID    NUMBER(18)                  NOT NULL,
  NODE_ID           NUMBER(18)                  NOT NULL,
  AUDIENCE_ID       NUMBER(18)                  NOT NULL,
  GIVER_RECVR_TYPE  VARCHAR2(10 CHAR)           NOT NULL
)
/
COMMENT ON TABLE RPT_PAX_PROMO_ELIG_STAGE IS 'Staging table used with refresh process.'
/
COMMENT ON COLUMN RPT_PAX_PROMO_ELIG_STAGE.KEY_FIELD_HASH IS 'An MD5 hash of key fields PROMOTION_ID, PARTICIPANT_ID, NODE_ID, AUDIENCE_ID, GIVER_RECVR_TYPE used to speed index efficiency.'
/
CREATE UNIQUE INDEX RPT_PAX_PROMO_ELIG_STAGE_PK ON RPT_PAX_PROMO_ELIG_STAGE
(KEY_FIELD_HASH)
/
ALTER TABLE RPT_PAX_PROMO_ELIG_STAGE ADD (
  CONSTRAINT RPT_PAX_PROMO_ELIG_STAGE_PK
  PRIMARY KEY
  (KEY_FIELD_HASH)
  USING INDEX RPT_PAX_PROMO_ELIG_STAGE_PK)
/
