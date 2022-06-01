--liquibase formatted sql

--changeset subramap:1
--comment security_pwd_request sequence
CREATE SEQUENCE security_pwd_request_pk_sq INCREMENT BY 1 START WITH 5000;
--rollback DROP SEQUENCE security_pwd_request_pk_sq;

--changeset subramap:2
--comment Security Password Request For Audit Purpose in Security Patch 3
CREATE TABLE security_pwd_request 
(security_pwd_request_id NUMBER(18) NOT NULL,
 user_id                 NUMBER(18) NOT NULL,   
 guid                    VARCHAR(80 CHAR) NOT NULL,
 created_by              NUMBER(18) NOT NULL,
 date_created            DATE NOT NULL,
 modified_by             NUMBER(18),
 date_modified           DATE,
 version                 NUMBER(18) NOT NULL);

   
COMMENT ON COLUMN security_pwd_request.user_id IS 'User Id of the pax for which the pwd was requested.';
COMMENT ON COLUMN security_pwd_request.created_by IS 'User Id of who requested and received the users pwd.';
--rollback DROP TABLE security_pwd_request CASCADE CONSTRAINTS;

--changeset subramap:3
--comment Alter table for adding Constraint
ALTER TABLE security_pwd_request
ADD CONSTRAINT security_pwd_request_id_pk PRIMARY KEY (security_pwd_request_id)
USING INDEX;
--rollback ALTER TABLE security_pwd_request DROP CONSTRAINT security_pwd_request_id_pk ;

