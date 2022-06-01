CREATE TABLE budget_segment
   (budget_segment_id        NUMBER(18) NOT NULL,
    budget_master_id         NUMBER(18) NOT NULL,
    name                     VARCHAR2(1000 CHAR) NOT NULL,
    status                   NUMBER(1) DEFAULT 1 NOT NULL,
    start_date               DATE NOT NULL,
    end_date                 DATE,
    is_allow_budget_reallocation        NUMBER(1) DEFAULT 0 NOT NULL,
    budget_reallocation_elig_type       VARCHAR2(40),    
    created_by               NUMBER(18) NOT NULL,
    date_created             DATE NOT NULL,
    modified_by              NUMBER(18),
    date_modified            DATE,
    version                  NUMBER(18) NOT NULL)
/
ALTER TABLE budget_segment
ADD CONSTRAINT budget_segment_id_pk PRIMARY KEY (budget_segment_id)
USING INDEX
/
ALTER TABLE budget_segment
ADD CONSTRAINT segment_budget_master_fk FOREIGN KEY (budget_master_id)
REFERENCES budget_master (budget_master_id)
/
ALTER TABLE budget_segment
ADD CONSTRAINT budget_segment_Uk UNIQUE (budget_master_id,start_date,end_date)
USING INDEX
/
CREATE INDEX BUDGET_SEGMENT_BM_FK_idx ON budget_segment
  (BUDGET_MASTER_ID)
/
COMMENT ON TABLE budget_segment IS 'The budget_segment table stores budget segment data'
/
COMMENT ON COLUMN budget_segment.budget_segment_id IS 'Primary key'
/
COMMENT ON COLUMN budget_segment.budget_master_id IS 'Database key that links the promotion to the budget'
/
COMMENT ON COLUMN budget_segment.status IS 'Status of the record'
/
COMMENT ON COLUMN budget_segment.start_date IS 'Start Date of the budget segment'
/
COMMENT ON COLUMN budget_segment.end_date IS 'End Date of the budget segment'
/
COMMENT ON COLUMN budget_segment.IS_ALLOW_BUDGET_REALLOCATION IS 'Budget Reallocation enabled '
/
COMMENT ON COLUMN budget_segment.BUDGET_REALLOCATION_ELIG_TYPE IS 'Whether budget reallocation is enabled for pax in org unit or org unit and below'
/
CREATE SEQUENCE BUDGET_SEGMENT_PK_SQ INCREMENT BY 1   START WITH 5000
/
ALTER TABLE BUDGET ADD BUDGET_SEGMENT_ID NUMBER(18)
/
CREATE INDEX BUDGET_BUDGET_SEGMENT_FK_IDX ON BUDGET
(BUDGET_SEGMENT_ID)
/
ALTER TABLE BUDGET_MASTER
ADD START_DATE               DATE DEFAULT SYSDATE NOT NULL
ADD    END_DATE                 DATE
/
