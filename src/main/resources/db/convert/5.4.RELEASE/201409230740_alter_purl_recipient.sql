ALTER TABLE purl_recipient ADD ( 
		    anniversary_num_days  NUMBER(5),
		    anniversary_num_years NUMBER(5),
		    celebration_manager_message_id NUMBER(18)
    )
/
COMMENT ON COLUMN purl_recipient.anniversary_num_days IS 'Celebration anniversary days if celebration turned on'
/
COMMENT ON COLUMN purl_recipient.anniversary_num_years IS 'Celebration anniversary years if celebration turned on'
/
COMMENT ON COLUMN purl_recipient.celebration_manager_message_id IS 'Celebration Manager Message Id reference to celebration manage messages'
/