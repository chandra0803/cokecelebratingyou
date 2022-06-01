CREATE SEQUENCE c_manager_message_pk_sq INCREMENT BY 1 START WITH 5000
/

CREATE TABLE celebration_manager_message
   (
    celebration_manager_message_id 	   NUMBER(18) NOT NULL,
   	recipient_id      	  			   NUMBER(18) NOT NULL,
   	promotion_id	       			   NUMBER(18) NOT NULL,
	anniversary_num_years              NUMBER(5),
	anniversary_num_days               NUMBER(5),
	message_collect_expire_date        DATE,
	manager_id						   NUMBER(18),
	manager_message  				   VARCHAR2(4000 Char),
	manager_above_id 				   NUMBER(18),
	manager_above_message 			   VARCHAR2(4000 Char),
    created_by     					   NUMBER(18) ,
    date_created   					   DATE ,
    modified_by    					   NUMBER(18),
    date_modified  					   DATE,
    version        					   NUMBER(18) )
/

ALTER TABLE celebration_manager_message
ADD CONSTRAINT c_manager_message_pk_sq PRIMARY KEY (celebration_manager_message_id)
USING INDEX
/
COMMENT ON TABLE celebration_manager_message IS 'The celebration_manager_message table defines a specific Celebration Manager Message.'
/
COMMENT ON COLUMN celebration_manager_message.celebration_manager_message_id IS 'System-generated key that identifies a specific Celebration Manager Message.'
/




