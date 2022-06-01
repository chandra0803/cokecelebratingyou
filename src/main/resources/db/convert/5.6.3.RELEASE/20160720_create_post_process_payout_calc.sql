CREATE SEQUENCE post_process_calc_id_pk_sq INCREMENT BY 1 START WITH 1000
/
CREATE TABLE POST_PROCESS_PAYOUT_CALC(
  post_process_payout_calc_id   NUMBER(18),   
  post_process_jobs_id          NUMBER(18) NOT NULL,
  journal_id                    NUMBER(12),
  promotion_Payout_Group_Id		NUMBER(12),
  minimum_qualifier_status_id	NUMBER(12),
  payout_calculation_audit_id	NUMBER(12),
  promo_merch_program_level_id	NUMBER(12),
  calculated_payout				NUMBER(18),
  calculated_cash_payout        NUMBER(18,4),
  date_created                	DATE,
  created_by				  	NUMBER(18),
  date_modified				  	DATE,
  modified_by				  	NUMBER(18),
  version					  	NUMBER(18) NOT NULL
)
/
CREATE UNIQUE INDEX POST_PROCESS_CALC_ID_PK ON POST_PROCESS_PAYOUT_CALC
  (POST_PROCESS_PAYOUT_CALC_ID)
/
ALTER TABLE POST_PROCESS_PAYOUT_CALC ADD (
  CONSTRAINT POST_PROCESS_CALC_ID_PK
  PRIMARY KEY (POST_PROCESS_PAYOUT_CALC_ID)
  USING INDEX POST_PROCESS_CALC_ID_PK
  ENABLE VALIDATE,
  CONSTRAINT POST_PROCESS_JOBS_ID_FK
  FOREIGN KEY (POST_PROCESS_JOBS_ID)
  REFERENCES POST_PROCESS_JOBS (POST_PROCESS_JOBS_ID)
)