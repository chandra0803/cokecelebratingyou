ALTER TABLE PROMO_NOMINATION MODIFY AWARD_ACTIVE NUMBER(1,0)
/
ALTER TABLE PROMO_NOMINATION MODIFY BEHAVIOR_ACTIVE NUMBER(1,0)
/
ALTER TABLE PROMO_NOMINATION MODIFY CARD_ACTIVE NUMBER(1,0)
/
ALTER TABLE PROMO_NOMINATION
ADD CERTIFICATE_ACTIVE NUMBER(1,0) DEFAULT 0 NOT NULL
/
ALTER TABLE PROMO_NOMINATION
ADD ONE_CERT_PER_PROMOTION NUMBER(1,0) DEFAULT 0 NOT NULL
/
ALTER TABLE PROMO_NOMINATION MODIFY CARD_CLIENT_SETUP_DONE NUMBER(1,0)
/
ALTER TABLE PROMO_NOMINATION MODIFY INCLUDE_CERTIFICATE NUMBER(1,0)
/
ALTER TABLE PROMO_NOMINATION MODIFY SELF_NOMINATION NUMBER(1,0)
/
ALTER TABLE PROMO_NOMINATION MODIFY ALLOW_PUBLIC_RECOGNITION NUMBER(1,0)
/
ALTER TABLE PROMO_NOMINATION MODIFY PUBLICATION_DATE_ACTIVE NUMBER(1,0)
/
ALTER TABLE PROMO_NOMINATION
ADD REQUEST_MORE_BUDGET NUMBER(1,0) DEFAULT 0 NOT NULL
/
ALTER TABLE PROMO_NOMINATION
ADD BUDGET_APPROVER NUMBER(18,0)
/
ALTER TABLE PROMO_NOMINATION
ADD PAYOUT_LEVEL_TYPE VARCHAR2(40 ) DEFAULT NULL
/
ALTER TABLE PROMO_NOMINATION
ADD AWARD_GROUP_SIZE  VARCHAR2(20)
/
ALTER TABLE PROMO_NOMINATION
ADD DEFAULT_APPROVER NUMBER(18,0)
/
ALTER TABLE PROMO_NOMINATION
ADD TIME_PERIOD_ACTIVE NUMBER DEFAULT 0
/
ALTER TABLE PROMO_NOMINATION
ADD PAX_DISPLAY_ORDER VARCHAR2(40)
/
ALTER TABLE PROMO_NOMINATION
ADD ALLOW_PUBLIC_RECOG_POINTS NUMBER(1,0) DEFAULT 0 NOT NULL
/
ALTER TABLE PROMO_NOMINATION
ADD PUBLIC_REC_AWARD_AMOUNT_MIN NUMBER(18,0)
/
ALTER TABLE PROMO_NOMINATION
ADD PUBLIC_REC_AWARD_AMOUNT_MAX NUMBER(18,0)
/
ALTER TABLE PROMO_NOMINATION
ADD PUBLIC_REC_BUDGET_MASTER_ID NUMBER(18)
/
ALTER TABLE PROMO_NOMINATION
ADD PUBLIC_REC_AUDIENCE_TYPE VARCHAR2(40)
/
ALTER TABLE PROMO_NOMINATION
ADD PUBLIC_REC_AWARD_TYPE_FIXED NUMBER(1,0) DEFAULT 0 NOT NULL
/
ALTER TABLE PROMO_NOMINATION
ADD PUBLIC_REC_AWARD_AMOUNT_FIXED NUMBER(18,0)
/
ALTER TABLE PROMO_NOMINATION
ADD IS_WHY NUMBER(1,0) DEFAULT 1 NOT NULL
/
ALTER TABLE PROMO_NOMINATION
ADD RECOMMENDED_AWARD NUMBER(1,0) DEFAULT 0 NOT NULL
/
ALTER TABLE PROMO_NOMINATION
ADD VIEW_PAST_WINNERS NUMBER(1,0) DEFAULT 0 NOT NULL
/
ALTER TABLE PROMO_NOMINATION
ADD LAST_PNT_BUD_REQ_DATE DATE
/
ALTER TABLE PROMO_NOMINATION
ADD LAST_CASH_BUD_REQ_DATE DATE
/