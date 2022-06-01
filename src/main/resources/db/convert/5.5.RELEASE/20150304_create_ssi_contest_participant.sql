CREATE SEQUENCE ssi_contest_participant_pk_sq INCREMENT BY 1 START WITH 5000
/
CREATE TABLE ssi_contest_participant
(
  ssi_contest_participant_id 	NUMBER(18) NOT NULL,
  ssi_contest_id	    		NUMBER(18) NOT NULL,
  award_issuance_number		    NUMBER(4),
  user_id               		NUMBER(18) NOT NULL,
  activity_description			VARCHAR2(100),
  objective_amount				NUMBER(18,4),
  siu_baseline_amount			NUMBER(18,4),
  objective_payout				NUMBER(18),
  objective_payout_description	VARCHAR2(50),
  objective_bonus_increment		NUMBER(18,4),
  objective_bonus_payout		NUMBER(18),
  objective_bonus_cap			NUMBER(18),
  created_by            		NUMBER(18) NOT NULL,  
  date_created          		DATE NOT NULL,
  modified_by           		NUMBER(18),
  date_modified         		DATE,  
  version               		NUMBER(18) NOT NULL,
  CONSTRAINT ssi_contest_participant_pk PRIMARY KEY (ssi_contest_participant_id)
)
/
ALTER TABLE ssi_contest_participant ADD CONSTRAINT ssi_contest_pax_contest_fk
  FOREIGN KEY (ssi_contest_id) REFERENCES ssi_contest (ssi_contest_id)
/
ALTER TABLE ssi_contest_participant ADD CONSTRAINT ssi_contest_pax_user_fk
  FOREIGN KEY (user_id) REFERENCES application_user (user_id)
/