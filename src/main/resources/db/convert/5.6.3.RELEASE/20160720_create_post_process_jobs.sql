CREATE SEQUENCE post_process_job_id_pk_sq INCREMENT BY 1 START WITH 1000
/
CREATE TABLE POST_PROCESS_JOBS(
  post_process_jobs_id        NUMBER(18),
  process_bean_name           VARCHAR2(100),
  promotion_type			  VARCHAR2(80),
  claim_id                    NUMBER(12),
  journal_id                  NUMBER(12),
  trigger_name                VARCHAR2(80),
  is_fired                    NUMBER(1),
  fired_date                  DATE,
  retry_count                 NUMBER(10),
  retry_count_change_date     DATE,
  date_created                DATE,
  created_by				  NUMBER(18),
  date_modified				  DATE,
  modified_by				  NUMBER(18),
  version					  NUMBER(18) NOT NULL
  )
/
CREATE UNIQUE INDEX POST_PROCESS_JOBS_ID_PK ON POST_PROCESS_JOBS
(POST_PROCESS_JOBS_ID)
/

CREATE UNIQUE INDEX POST_PROCESS_JOBS_UK2 ON POST_PROCESS_JOBS
(JOURNAL_ID)
/
ALTER TABLE POST_PROCESS_JOBS ADD (
  CONSTRAINT POST_PROCESS_JOBS_ID_PK
  PRIMARY KEY (POST_PROCESS_JOBS_ID)
  USING INDEX POST_PROCESS_JOBS_ID_PK
  ENABLE VALIDATE,
  CONSTRAINT POST_PROCESS_JOBS_UK2
  UNIQUE (JOURNAL_ID)
  USING INDEX POST_PROCESS_JOBS_UK2
  ENABLE VALIDATE)
/
