ALTER TABLE BUDGET_HISTORY ADD (claim_id NUMBER(12))
/
COMMENT ON COLUMN budget_history.claim_id IS 'The Claim_id is added to this table as a reference only.No functionality changes done.'
/
