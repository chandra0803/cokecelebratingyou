--liquibase formatted sql

--changeset Siemback:1
--comment participant_identifier sequence
CREATE SEQUENCE participant_identifier_seq INCREMENT BY 1 START WITH 5001;
--rollback DROP SEQUENCE participant_identifier_seq;

--changeset Siemback:2
--comment Participant Identifier table for Activations
CREATE TABLE participant_identifier
(	
   	participant_identifier_id  				NUMBER(18,0) NOT NULL,
   	field_type			VARCHAR2(40 CHAR),
   	cm_asset_code		VARCHAR2(100 CHAR),
   	type_id				NUMBER(18,0),
	created_by			NUMBER(18,0) NOT NULL,
	date_created		TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	modified_by			NUMBER(18,0),
	date_modified		TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	version				NUMBER(18,0) DEFAULT 1,
	CONSTRAINT pax_identifier_pk  PRIMARY KEY (participant_identifier_id),
	CONSTRAINT TYPE_ID_FK  FOREIGN KEY (TYPE_ID)  REFERENCES CHARACTERISTIC (CHARACTERISTIC_ID)
);
   
COMMENT ON COLUMN participant_identifier.participant_identifier_id is 'primary key of Participant Identifier table';
COMMENT ON COLUMN participant_identifier.field_type is 'Indicates the type';
COMMENT ON COLUMN participant_identifier.type_id is 'Indicates the id of the Characteristic type ';
COMMENT ON COLUMN participant_identifier.cm_asset_code is 'CMS Code';
COMMENT ON COLUMN participant_identifier.created_by IS 'Audit Column for new record, userID of the person';
COMMENT ON COLUMN participant_identifier.date_created IS 'Audit column for new record';
COMMENT ON COLUMN participant_identifier.modified_by IS 'Audit Column for tracking updates, userID of the person';
COMMENT ON COLUMN participant_identifier.date_modified IS 'Audit Column for tracking updates';
COMMENT ON COLUMN participant_identifier.version IS 'Used for Java optimistic locking';
--rollback DROP TABLE participant_identifier CASCADE CONSTRAINTS;

--changeset Siemback:3
--comment Insert Default Values
INSERT INTO participant_identifier VALUES ( participant_identifier_seq.nextval, 'city', 'participant_identifier.city',null, 0,sysdate,NULL,NULL,1 );
INSERT INTO participant_identifier VALUES ( participant_identifier_seq.nextval, 'country', 'participant_identifier.country',null, 0,sysdate,NULL,NULL,1 );
INSERT INTO participant_identifier VALUES ( participant_identifier_seq.nextval, 'dob', 'participant_identifier.dob',null, 0,sysdate,NULL,NULL,1 );
INSERT INTO participant_identifier VALUES ( participant_identifier_seq.nextval, 'department', 'participant_identifier.department',null, 0,sysdate,NULL,NULL,1 );
INSERT INTO participant_identifier VALUES ( participant_identifier_seq.nextval, 'email', 'participant_identifier.email',null, 0,sysdate,NULL,NULL,1 );
INSERT INTO participant_identifier VALUES ( participant_identifier_seq.nextval, 'hiredate', 'participant_identifier.hiredate',null, 0,sysdate,NULL,NULL,1 );
INSERT INTO participant_identifier VALUES ( participant_identifier_seq.nextval, 'jobtitle', 'participant_identifier.jobtitle',null, 0,sysdate,NULL,NULL,1 );
INSERT INTO participant_identifier VALUES ( participant_identifier_seq.nextval, 'loginid', 'participant_identifier.loginid',null, 0,sysdate,NULL,NULL,1 );
INSERT INTO participant_identifier VALUES ( participant_identifier_seq.nextval, 'postalcode', 'participant_identifier.postalcode',null, 0,sysdate,NULL,NULL,1 );
INSERT INTO participant_identifier VALUES ( participant_identifier_seq.nextval, 'state', 'participant_identifier.state',null, 0,sysdate,NULL,NULL,1 );
--rollback DELETE FROM participant_identifier WHERE field_type IN ( 'city', 'country', 'dob', 'department','email','hiredate','jobtitle','loginid','postalcode','state');

--changeset Siemback:4
--comment Alter table for is_selected attrbiteu
ALTER TABLE participant_identifier ADD IS_SELECTED NUMBER(1,0) DEFAULT 0 NOT NULL;
--rollback ALTER TABLE participant_identifier DROP (IS_SELECTED);

--changeset Siemback:5
--comment Alter table for is_selected attrbiteu
UPDATE participant_identifier SET IS_SELECTED = 1 WHERE field_type='email';
--rollback UPDATE participant_identifier SET IS_SELECTED = 0 WHERE field_type='email';

--changeset Siemback:6
--comment alter table characteristic to only exists once in this table
ALTER TABLE participant_identifier add CONSTRAINT pax_id_char_unq UNIQUE (TYPE_ID);
--rollback ALTER TABLE participant_identifier DROP CONSTRAINT pax_id_char_unq ;

