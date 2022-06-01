ALTER TABLE comm_log
ADD message_type 	VARCHAR2(8) DEFAULT 'email'
/
ALTER TABLE comm_log
MODIFY message_type NOT NULL
/