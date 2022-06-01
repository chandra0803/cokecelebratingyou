ALTER TABLE rpt_pax_promo_eligibility 
ADD KEY_FIELD_HASH RAW(16) NOT NULL
/
COMMENT ON COLUMN RPT_PAX_PROMO_ELIGIBILITY.KEY_FIELD_HASH IS 'An MD5 hash of key fields PROMOTION_ID, PARTICIPANT_ID, NODE_ID, AUDIENCE_ID, GIVER_RECVR_TYPE used to speed index efficiency.'
/
CREATE INDEX RPT_ELIGI_PAX_PROMO_NODE_IDX2 ON RPT_PAX_PROMO_ELIGIBILITY(PROMOTION_ID, KEY_FIELD_HASH)
/
CREATE UNIQUE INDEX RPT_ELIGI_PAX_PROMO_NODE_PK ON RPT_PAX_PROMO_ELIGIBILITY(KEY_FIELD_HASH)
/
ALTER TABLE RPT_PAX_PROMO_ELIGIBILITY 
ADD (
  CONSTRAINT RPT_ELIGI_PAX_PROMO_NODE_PK
  PRIMARY KEY
  (KEY_FIELD_HASH)
  USING INDEX RPT_ELIGI_PAX_PROMO_NODE_PK)
/