CREATE GLOBAL TEMPORARY TABLE GTT_SSI_DTGT
(
  SSI_CONTEST_ID           NUMBER(18)           NOT NULL,
  SSI_CONTEST_ACTIVITY_ID  NUMBER(18)           NOT NULL,
  DESCRIPTION          VARCHAR2(100)            NOT NULL,
  USER_ID                  NUMBER(18)           NOT NULL,
  ACTIVITY_AMT             NUMBER,
  PAYOUT_QUANTITY          NUMBER,
  PAYOUT_VALUE             NUMBER
)
ON COMMIT PRESERVE ROWS
/