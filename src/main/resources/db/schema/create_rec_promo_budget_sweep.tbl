create table promo_budget_sweep
( promo_budget_sweep_id		  NUMBER(18,0) NOT NULL,
  promotion_id                NUMBER(18,0) NOT NULL,
  budget_segment_id           NUMBER(18,0) NOT NULL,
  BUDGET_SWEEP_DATE           DATE,
  IS_BUDGET_SWEEP_RUN         VARCHAR(1),
  BUDGET_SWEEP_RUN_DATE       DATE,
  STATUS                      NUMBER(1) DEFAULT 1,
  created_by number(18) NOT NULL,
  date_created     DATE NOT NULL,
  modified_by number(18),
  date_modified    DATE,
  version          NUMBER(18) NOT NULL)
/
ALTER TABLE promo_budget_sweep
ADD CONSTRAINT promo_budget_sweep_pk PRIMARY KEY (promo_budget_sweep_id)
USING INDEX
/
ALTER TABLE promo_budget_sweep ADD CONSTRAINT promo_budget_sweep_uk
  UNIQUE (promotion_id,budget_segment_id)
/
ALTER TABLE promo_budget_sweep ADD CONSTRAINT promo_budget_sweep_promo_fk
  FOREIGN KEY (promotion_id) REFERENCES promo_recognition (promotion_id) 
/
ALTER TABLE promo_budget_sweep ADD CONSTRAINT promo_budget_sweep_segment_fk
  FOREIGN KEY (budget_segment_id) REFERENCES budget_segment (budget_segment_id) 
/
COMMENT ON TABLE promo_budget_sweep  IS 'This is to capture the budget sweep date for each budget segment for a promotion'
/
COMMENT ON COLUMN promo_budget_sweep.promo_budget_sweep_id IS 'System generated PK'
/
COMMENT ON COLUMN promo_budget_sweep.PROMOTION_ID IS 'FK to promotion table'
/
COMMENT ON COLUMN promo_budget_sweep.BUDGET_SEGMENT_ID IS 'FK to budget_segment table'
/
CREATE SEQUENCE PROMO_BUDGET_SWEEP_PK_SQ INCREMENT BY 1   START WITH 5000
/