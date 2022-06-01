CREATE SEQUENCE SSI_CONTEST_ACTIVITY_PK_SQ INCREMENT BY 1 START WITH 5000
/
CREATE TABLE SSI_CONTEST_ACTIVITY
(
    SSI_CONTEST_ACTIVITY_ID   NUMBER(18)    NOT NULL,
    SSI_CONTEST_ID            NUMBER(12),
    DESCRIPTION               VARCHAR2(50),
    INCREMENT_AMOUNT          NUMBER(18,4),
    PAYOUT_AMOUNT             NUMBER(18),
    MIN_QUALIFIER             NUMBER(18,4),
    PAYOUT_CAP_AMOUNT         NUMBER(18),
    GOAL_AMOUNT               NUMBER(18,4),
    SEQUENCE_NUMBER           NUMBER(18) NOT NULL,
    PAYOUT_DESCRIPTION        VARCHAR2(100),
    GOAL_PERCENTAGE            NUMBER(18,4) DEFAULT 100,
    CREATED_BY                NUMBER(18)   NOT NULL,
    DATE_CREATED              DATE         NOT NULL,
    MODIFIED_BY               NUMBER(18),
    DATE_MODIFIED             DATE,
    VERSION                   NUMBER(18)   NOT NULL
)
/
ALTER TABLE SSI_CONTEST_ACTIVITY
ADD CONSTRAINT SSI_CONTEST_ACTIVITY_PK PRIMARY KEY (SSI_CONTEST_ACTIVITY_ID)
USING INDEX
/
ALTER TABLE SSI_CONTEST_ACTIVITY ADD CONSTRAINT SSI_CONTEST_ACT_CONTEST_FK
  FOREIGN KEY (SSI_CONTEST_ID) REFERENCES SSI_CONTEST (SSI_CONTEST_ID)
/