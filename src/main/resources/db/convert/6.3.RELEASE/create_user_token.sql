--liquibase formatted sql

--changeset mattam:1
--comment User Token sequence for password token management
CREATE SEQUENCE USER_TOKEN_PK_SQ START WITH 250 INCREMENT BY 1;
--rollback DROP SEQUENCE user_token_pk_sq;

--changeset mattam:2
--comment User Token table for password token management
CREATE TABLE USER_TOKEN( 
  USER_TOKEN_ID             NUMBER(18)          NOT NULL,
  USER_ID 					NUMBER(18) 			NOT NULL,
  TOKEN 					VARCHAR2(100 CHAR)  NOT NULL,
  STATUS                    VARCHAR2(30 CHAR)   NOT NULL,
  CREATED_BY                VARCHAR2(30 CHAR)   NOT NULL,
  DATE_CREATED              DATE                NOT NULL,
  MODIFIED_BY               VARCHAR2(30 CHAR),
  DATE_MODIFIED             DATE,
  CONSTRAINT USER_TOKEN_PK PRIMARY KEY (USER_TOKEN_ID));
ALTER TABLE USER_TOKEN ADD ( CONSTRAINT USER_TOKEN_FK  FOREIGN KEY (USER_ID)  REFERENCES PARTICIPANT (USER_ID)  ENABLE VALIDATE);
--rollback DROP TABLE user_token CASCADE CONSTRAINTS;

--changeset kancherla:3
--comment Flag to allow auto generate token for processes. Defaulted to 0
ALTER TABLE user_token ADD allow_regenerate NUMBER(1,0) DEFAULT 0 NOT NULL;
--rollback ALTER TABLE user_token DROP (allow_regenerate);

--changeset kancherla:4
--comment Adding token expiration date. This is used to identify when token expires.
ALTER TABLE user_token ADD expiration_date DATE DEFAULT sysdate NOT NULL;
--rollback ALTER TABLE user_token DROP (expiration_date);

--changeset siemback:5
--comment Adding token type. This is the hibernate discriminator value.
ALTER TABLE user_token ADD token_type VARCHAR2(30 CHAR) DEFAULT 'EMAIL' NOT NULL;
--rollback ALTER TABLE user_token DROP (token_type);

--changeset ramesh:6
--comment drop the User_toke_fk constraint
ALTER TABLE  USER_TOKEN DROP CONSTRAINT USER_TOKEN_FK;
--rollback not required

--changeset ramesh:7
--comment alter the user_token_fk constratint reference from participant table to application_user table  
 ALTER TABLE USER_TOKEN ADD CONSTRAINT USER_TOKEN_FK FOREIGN KEY (USER_ID)  REFERENCES APPLICATION_USER (USER_ID) ENABLE;
--rollback not required