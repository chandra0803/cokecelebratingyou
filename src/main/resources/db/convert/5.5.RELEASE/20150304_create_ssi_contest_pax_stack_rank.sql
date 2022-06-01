CREATE SEQUENCE ssi_contest_pax_stackrankpk_sq INCREMENT BY 1 START WITH 5000
/
CREATE TABLE ssi_contest_pax_stack_rank
(	
    ssi_contest_pax_stack_rank_id number(18,0) not null, 
	ssi_contest_id number(18,0) not null, 
	user_id number(18,0) not null,
	stack_rank_position number(18,0), 
    ssi_contest_activity_id	number(18,0), 
	created_by number(18,0) not null, 
	date_created date not null, 
	modified_by number(18,0), 
	date_modified date, 
	version number(18,0) not null,
	CONSTRAINT ssi_contest_pax_stack_rank_pk PRIMARY KEY (ssi_contest_pax_stack_rank_id)
)
/
ALTER TABLE ssi_contest_pax_stack_rank ADD CONSTRAINT ssi_pax_stack_rank_contest_fk
  FOREIGN KEY (ssi_contest_id) REFERENCES ssi_contest (ssi_contest_id)
/
ALTER TABLE ssi_contest_pax_stack_rank ADD CONSTRAINT ssi_pax_stack_rank_user_fk
  FOREIGN KEY (user_id) REFERENCES application_user (user_id)
/
ALTER TABLE ssi_contest_pax_stack_rank ADD CONSTRAINT ssi_contest_pax_activity_fk
  FOREIGN KEY (ssi_contest_activity_id) REFERENCES SSI_CONTEST_ACTIVITY (ssi_contest_activity_id)
/