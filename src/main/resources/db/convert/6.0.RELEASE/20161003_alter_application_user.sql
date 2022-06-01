ALTER TABLE application_user ADD ( 
	is_otp	NUMBER(1) DEFAULT 0,
	otp_date	DATE	)
/
alter table participant_badge add ( claim_id  NUMBER(12) )
/
