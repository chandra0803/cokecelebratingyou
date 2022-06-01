CREATE SEQUENCE SSI_CONTEST_BILL_CODE_PK_SQ
  START WITH 100
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
/
CREATE TABLE SSI_CONTEST_BILL_CODE
(
  SSI_CONTEST_BILL_CODE_ID  NUMBER(18)          NOT NULL,
  SSI_CONTEST_ID            NUMBER(18)          NOT NULL,
  TRACK_BILLS_BY            VARCHAR2(15 CHAR),
  BILL_CODE                 VARCHAR2(100 CHAR),
  CUSTOM_VALUE              VARCHAR2(100 CHAR),
  SORT_ORDER                NUMBER(12),
  CREATED_BY                NUMBER(18)          NOT NULL,
  DATE_CREATED              DATE                NOT NULL,
  MODIFIED_BY               NUMBER(18),
  DATE_MODIFIED             DATE,
  VERSION                   NUMBER(18)          NOT NULL
)
/
CREATE UNIQUE INDEX SSI_CONTEST_BILL_CODE_PK ON SSI_CONTEST_BILL_CODE
(SSI_CONTEST_BILL_CODE_ID)
/
CREATE INDEX SSI_BILL_CODE_SSI_FK_IDX ON SSI_CONTEST_BILL_CODE
(SSI_CONTEST_ID)
/
ALTER TABLE SSI_CONTEST_BILL_CODE ADD (
  CONSTRAINT SSI_CONTEST_BILL_CODE_PK
  PRIMARY KEY
  (SSI_CONTEST_BILL_CODE_ID)
  USING INDEX SSI_CONTEST_BILL_CODE_PK
  ENABLE VALIDATE)
/
INSERT INTO SSI_CONTEST_BILL_CODE (SSI_CONTEST_BILL_CODE_ID,SSI_CONTEST_ID,TRACK_BILLS_BY,BILL_CODE,CUSTOM_VALUE,SORT_ORDER,DATE_CREATED,CREATED_BY,VERSION)
(SELECT SSI_CONTEST_BILL_CODE_PK_SQ.NEXTVAL,
        ssi_contest_id,
        track_bill_by,
        bill_payout_code,
        custom_value,
        sort_order,
        date_created,
        created_by,
        version FROM (
        SELECT
             ssi_contest_id,
             CASE WHEN bill_payout_code_type ='other' THEN 'participant' ELSE bill_payout_code_type END track_bill_by,
             CASE WHEN bill_payout_code_type ='other' THEN 'customValue'  ELSE 'orgUnitName' END bill_payout_code,
             CASE WHEN bill_payout_code_type ='other' THEN bill_payout_code_1 ELSE NULL END custom_value,
             0 sort_order,
             date_created,
             created_by,
             0 version
        FROM ssi_contest s
       WHERE     bill_payout_code_1 IS NOT NULL  OR bill_payout_code_type IN  ('participant','creator') 
       UNION ALL
       SELECT ssi_contest_id,
             CASE WHEN bill_payout_code_type ='other' THEN 'participant' ELSE bill_payout_code_type END track_bill_by,
             CASE WHEN bill_payout_code_type ='other' THEN 'customValue'  ELSE 'orgUnitName' END bill_payout_code,
             CASE WHEN bill_payout_code_type ='other' THEN bill_payout_code_2 ELSE NULL END custom_value,
             1 sort_order,
             date_created,
             created_by,
             0 version
        FROM ssi_contest s
       WHERE     bill_payout_code_2 IS NOT NULL OR bill_payout_code_type IN  ('participant','creator')))      
/
alter table ssi_contest
drop (BILL_PAYOUT_CODE_1,BILL_PAYOUT_CODE_2,BILL_PAYOUT_CODE_TYPE)
/
