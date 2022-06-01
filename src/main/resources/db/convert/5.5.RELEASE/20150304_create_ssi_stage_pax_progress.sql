CREATE TABLE STAGE_SSI_PAX_PROGRESS_LOAD
(
  IMPORT_RECORD_ID         NUMBER(18),
  IMPORT_FILE_ID           NUMBER(18),
  USER_NAME                VARCHAR2(40 BYTE),
  FIRST_NAME               VARCHAR2(40 CHAR),
  LAST_NAME                VARCHAR2(40 CHAR),
  NODE_NAME                VARCHAR2(40 CHAR),
  PROGRESS                 NUMBER(18,4),
  EMAIL_ADDRESS            VARCHAR2(75 CHAR),
  ACTIVITY_DESCRIPTION     VARCHAR2(50 CHAR),
  SSI_CONTEST_ID           NUMBER(18),
  ACTIVITY_DATE            DATE,
  USER_ID                  NUMBER(18),
  SSI_CONTEST_ACTIVITY_ID  NUMBER(18),
  DATE_CREATED             DATE,
  CREATED_BY               NUMBER(18),
  DATE_MODIFIED            DATE,
  MODIFIED_BY              NUMBER(18),
  VERSION                  NUMBER(18)
)
/
CREATE INDEX STAGE_SSI_PROGRESS_LOAD_FK_IDX ON STAGE_SSI_PAX_PROGRESS_LOAD (IMPORT_FILE_ID)
/
CREATE UNIQUE INDEX STAGE_SSI_PAX_PROGRESS_LOAD_PK ON STAGE_SSI_PAX_PROGRESS_LOAD (IMPORT_RECORD_ID)
/
ALTER TABLE STAGE_SSI_PAX_PROGRESS_LOAD
ADD (
	CONSTRAINT STAGE_SSI_PAX_PROGRESS_LOAD_FK 
	FOREIGN KEY
	(IMPORT_FILE_ID) 
	REFERENCES IMPORT_FILE (IMPORT_FILE_ID) ENABLE VALIDATE)
/
ALTER TABLE STAGE_SSI_PAX_PROGRESS_LOAD 
ADD (
	CONSTRAINT STAGE_SSI_PAX_PROGRESS_LOAD_PK
	PRIMARY KEY
	(IMPORT_RECORD_ID) 
	USING INDEX STAGE_SSI_PAX_PROGRESS_LOAD_PK ENABLE VALIDATE)
/