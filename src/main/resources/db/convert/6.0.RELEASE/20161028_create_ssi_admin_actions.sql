CREATE SEQUENCE SSI_ADMIN_ACTIONS_PK_SQ START WITH 250 INCREMENT BY 1
/
CREATE TABLE SSI_ADMIN_ACTIONS
(
  SSI_ADMIN_ACTIONS_ID  	NUMBER(18)          NOT NULL,
  USER_ID                   NUMBER(18)          NOT NULL,
  SSI_CONTEST_ID         	NUMBER(18)          NOT NULL,
  ACTION                    VARCHAR2(40 CHAR)   NOT NULL,
  DESCRPTION                VARCHAR2(100 CHAR)  NOT NULL,
  CREATED_BY                VARCHAR2(30 CHAR)   NOT NULL,
  DATE_CREATED              DATE                NOT NULL
)
/
ALTER TABLE SSI_ADMIN_ACTIONS ADD (
  CONSTRAINT SI_ADMIN_ACTIONS_USER_FK 
  FOREIGN KEY (USER_ID) 
  REFERENCES APPLICATION_USER (USER_ID)
  ENABLE VALIDATE)
/
ALTER TABLE SSI_ADMIN_ACTIONS ADD (
  CONSTRAINT SI_ADMIN_ACTIONS_SSI_FK 
  FOREIGN KEY (SSI_CONTEST_ID) 
  REFERENCES SSI_CONTEST (SSI_CONTEST_ID)
  ENABLE VALIDATE)
/