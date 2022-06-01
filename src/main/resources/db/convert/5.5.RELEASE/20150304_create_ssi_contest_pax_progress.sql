CREATE SEQUENCE ssi_contest_pax_progress_pk_sq INCREMENT BY 1 START WITH 5000
/
CREATE TABLE ssi_contest_pax_progress
(
  ssi_contest_pax_progress_id   NUMBER(18),
  ssi_contest_id	    		NUMBER(18),
  user_id               		NUMBER(18),
  activity_amt					NUMBER(18,4),
  activity_date          		DATE NOT NULL,
  ssi_contest_activity_id       NUMBER(18),
  created_by            		NUMBER(18) NOT NULL,  
  date_created          		DATE NOT NULL,
  modified_by					NUMBER(18),
  date_modified					DATE, 
  version               		NUMBER(18) NOT NULL,
  CONSTRAINT ssi_contest_pax_progress_pk PRIMARY KEY (ssi_contest_pax_progress_id),
  CONSTRAINT fk_contest_progress FOREIGN KEY (ssi_contest_activity_id) REFERENCES ssi_contest_activity(ssi_contest_activity_id)
)
/
ALTER TABLE ssi_contest_pax_progress ADD CONSTRAINT ssi_contest_pax_progress_unq
  UNIQUE (ssi_contest_id, user_id, ssi_contest_activity_id)
/