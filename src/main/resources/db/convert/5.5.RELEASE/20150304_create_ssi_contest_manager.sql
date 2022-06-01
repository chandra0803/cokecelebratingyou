CREATE SEQUENCE ssi_contest_manager_pk_sq INCREMENT BY 1 START WITH 5000
/
CREATE TABLE ssi_contest_manager
(
  ssi_contest_manager_id	    NUMBER(18) NOT NULL,
  ssi_contest_id	    		NUMBER(18) NOT NULL,
  user_id               		NUMBER(18) NOT NULL,
  created_by            		NUMBER(18) NOT NULL,  
  date_created          		DATE NOT NULL,
  version               		NUMBER(18) NOT NULL,
  CONSTRAINT ssi_contest_manager_pk PRIMARY KEY (ssi_contest_manager_id)
)
/
ALTER TABLE ssi_contest_manager ADD CONSTRAINT ssi_contest_mgr_contest_fk
  FOREIGN KEY (ssi_contest_id) REFERENCES ssi_contest (ssi_contest_id)
/
ALTER TABLE ssi_contest_manager ADD CONSTRAINT ssi_contest_manager_user_fk
  FOREIGN KEY (user_id) REFERENCES application_user (user_id)
/