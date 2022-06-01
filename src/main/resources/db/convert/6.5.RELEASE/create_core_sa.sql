--liquibase formatted sql

--changeset palaniss:1
--comment SA_INVITATION_INFO_PK_SQ sequence
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_SEQUENCES where SEQUENCE_NAME='COMPANY_PK_SQ'
CREATE SEQUENCE COMPANY_PK_SQ START WITH 100 INCREMENT BY 1;
--rollback DROP SEQUENCE COMPANY_PK_SQ;

--changeset palaniss:2
--comment create event reference table to store even details
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TABLES where TABLE_NAME='COMPANY'
CREATE TABLE COMPANY
   (ID                            		  NUMBER(18,0),
   	COMPANY_ID                            VARCHAR2(100) NOT NULL UNIQUE,
   	COMPANY_IDENTIFIER                    VARCHAR2(100) NOT NULL UNIQUE,
   	COMPANY_NAME                          VARCHAR2(100),
   	COMPANY_EMAIL                         VARCHAR2(100),
    CREATED_BY                            NUMBER(18) NOT NULL,
  	DATE_CREATED                          DATE NOT NULL,
  	MODIFIED_BY                           NUMBER(18),
  	DATE_MODIFIED                         DATE,
	VERSION				                  NUMBER(18,0) DEFAULT 1,
	CONSTRAINT COMPANY_PK PRIMARY KEY (ID)
	);
--rollback DROP TABLE COMPANY CASCADE CONSTRAINTS;

--changeset palaniss:3
--comment SA_INVITATION_INFO_PK_SQ sequence
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_SEQUENCES where SEQUENCE_NAME='EVENT_REFERENCE_PK_SQ'
CREATE SEQUENCE EVENT_REFERENCE_PK_SQ START WITH 100 INCREMENT BY 1;
--rollback DROP SEQUENCE EVENT_REFERENCE_PK_SQ;

--changeset palaniss:4
--comment create event reference table to store even details
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TABLES where TABLE_NAME='EVENT_REFERENCE'
CREATE TABLE EVENT_REFERENCE
   (ID                            		  NUMBER(18,0),
    COMPANY_ID                            VARCHAR2(100) NOT NULL,
    APPLICATION_NAME                      VARCHAR2(100),
    SCHEMA_NAME                           VARCHAR2(100),
    EVENT_NAME                            VARCHAR2(100),
    STATE                                 VARCHAR2(64),
    RECIPIENT_ID                          NUMBER(18,0),
    LOG                                   CLOB,
    DATA                                  CLOB,
    MESSAGE							 	  VARCHAR2(1024),
    COMMENTS							  VARCHAR2(1024),
    CHECKSUM                              VARCHAR2(1024) unique,
    CREATED_BY                            NUMBER(18) NOT NULL,
  	DATE_CREATED                          DATE NOT NULL,
  	MODIFIED_BY                           NUMBER(18),
  	DATE_MODIFIED                         DATE,
	VERSION				                  NUMBER(18,0) DEFAULT 1,
	CONSTRAINT EVENT_REFERENCE_PK PRIMARY KEY (ID)
	);
	ALTER TABLE EVENT_REFERENCE ADD ( CONSTRAINT EVENT_REFERENCE_COMPANY_FK  FOREIGN KEY (COMPANY_ID)  REFERENCES COMPANY (COMPANY_ID)  ENABLE VALIDATE);
--rollback DROP TABLE EVENT_REFERENCE CASCADE CONSTRAINTS;

--changeset palaniss:5
--comment program table to store program details retrived from SA events
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TABLES where TABLE_NAME='PROGRAM'
CREATE TABLE PROGRAM
   (PROGRAM_ID                     		  NUMBER(18,0),
    EXTERNAL_PROGRAM_ID                   VARCHAR2(100) NOT NULL,
    COMPANY_ID                            VARCHAR2(100) NOT NULL,
	PROGRAM_NAME_CMX_ASSET_CODE           VARCHAR2(2000),
	PROGRAM_NAME                          VARCHAR2(1000) NOT NULL,
	PROGRAM_TYPE                          VARCHAR2(80) NOT NULL,
	PROGRAM_STATUS                        VARCHAR2(30) NOT NULL,
	PROGRAM_START_DATE                    DATE NOT NULL,
	PROGRAM_END_DATE                      DATE,
	AWARD_TYPE                            VARCHAR2(40),
	PROGRAM_CREATE_DATE                   DATE NOT NULL,
	ALLOW_CONTRIBUTION                    NUMBER(1,0) DEFAULT 0,
	PROGRAM_HEADER						  VARCHAR2(1000),
	PROGRAM_HEADER_CMX_ASSET_CODE		  VARCHAR2(1000),
	PRIMARY_COLOR                         VARCHAR2(50),
	SECONDARY_COLOR                       VARCHAR2(50),
	CREATED_BY                            NUMBER(18) NOT NULL,
  	DATE_CREATED                          DATE NOT NULL,
  	MODIFIED_BY                           NUMBER(18),
  	DATE_MODIFIED                         DATE,
	VERSION				                  NUMBER(18,0) DEFAULT 1,
	CONSTRAINT PROGRAM_PK PRIMARY KEY (PROGRAM_ID)
	);
	ALTER TABLE PROGRAM ADD ( CONSTRAINT PROGRAM_COMPANY_FK  FOREIGN KEY (COMPANY_ID)  REFERENCES COMPANY (COMPANY_ID)  ENABLE VALIDATE);
	ALTER TABLE PROGRAM ADD CONSTRAINT PROGRAM_TYPE_UK UNIQUE (EXTERNAL_PROGRAM_ID, PROGRAM_TYPE);
--rollback DROP TABLE PROGRAM CASCADE CONSTRAINTS;

--changeset palaniss:6
--comment SA_INVITATION_INFO_PK_SQ sequence
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_SEQUENCES where SEQUENCE_NAME='SA_CELEBRATION_INFO_PK_SQ'
CREATE SEQUENCE SA_CELEBRATION_INFO_PK_SQ START WITH 100 INCREMENT BY 1;
--rollback DROP SEQUENCE SA_CELEBRATION_INFO_PK_SQ;

--changeset palaniss:7
--comment create sa_invitation_info table to store invitation details retrived from SA events
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TABLES where TABLE_NAME='SA_CELEBRATION_INFO'
CREATE TABLE SA_CELEBRATION_INFO
   (ID                            		  NUMBER(18,0),
    PROGRAM_ID            		          NUMBER(18,0) NOT NULL,
    CELEBRATION_ID                        VARCHAR2(100) NOT NULL UNIQUE,
    CLAIM_ID							  NUMBER(18),	
    COMPANY_ID                            VARCHAR2(100) NOT NULL,
    RECIPIENT_ID                          NUMBER(18,0) NOT NULL,
    AWARD_LEVEL                           VARCHAR2(30),
    COUNTRY	          			  		  VARCHAR2(3),
    AWARD_STATUS				      	  VARCHAR2(20),
    GIFTCODE_STATUS	  			          VARCHAR2(20),
    AWARD_POINTS						  NUMBER(12,2),
    IS_TAXABLE							  NUMBER(1,0) DEFAULT 0,
    POINTS_STATUS				      	  VARCHAR2(20),
    AWARD_DATE                            DATE,
    TEAM_ID								  NUMBER(12),
    IS_GIFTCODE                           NUMBER(1,0) DEFAULT 0,
    IS_POINTS                             NUMBER(1,0) DEFAULT 0,
    IS_CELEBRATION_SITE                   NUMBER(1,0) DEFAULT 0,
    IS_DAYMAKER 						  NUMBER(1,0) DEFAULT 0,
    CREATED_BY                            NUMBER(18) NOT NULL,
  	DATE_CREATED                          DATE NOT NULL,
  	MODIFIED_BY                           NUMBER(18),
  	DATE_MODIFIED                         DATE,
	VERSION				                  NUMBER(18,0) DEFAULT 1,
	CONSTRAINT SA_CELEBRATION_INFO_PK PRIMARY KEY (ID)
	);
	ALTER TABLE SA_CELEBRATION_INFO ADD ( CONSTRAINT SA_CELEBRATION_INFO_FK  FOREIGN KEY (PROGRAM_ID)  REFERENCES PROGRAM (PROGRAM_ID)  ENABLE VALIDATE);
	ALTER TABLE SA_CELEBRATION_INFO ADD ( CONSTRAINT SA_CELEBRATION_INFO_USERID_FK  FOREIGN KEY (RECIPIENT_ID)  REFERENCES PARTICIPANT (USER_ID)  ENABLE VALIDATE);
	ALTER TABLE SA_CELEBRATION_INFO ADD ( CONSTRAINT SA_CELEBRATION_INFO_COMPANY_FK  FOREIGN KEY (COMPANY_ID)  REFERENCES COMPANY (COMPANY_ID)  ENABLE VALIDATE);
--rollback DROP TABLE SA_CELEBRATION_INFO CASCADE CONSTRAINTS;

--changeset palaniss:8
--comment SA_INVITEANDCONTRIBUTE_INFO_PK_SQ sequence
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_SEQUENCES where SEQUENCE_NAME='SA_INVITEANDCONTRIB_INFO_PK_SQ'
CREATE SEQUENCE SA_INVITEANDCONTRIB_INFO_PK_SQ START WITH 100 INCREMENT BY 1;
--rollback DROP SEQUENCE SA_INVITEANDCONTRIB_INFO_PK_SQ;

--changeset palaniss:9
--comment create SA_InviteAndContribute_Info table to store contributor details
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TABLES where TABLE_NAME='SA_INVITEANDCONTRIBUTE_INFO'
CREATE TABLE SA_INVITEANDCONTRIBUTE_INFO
   (ID                            		  NUMBER(18,0),
    CELEBRATION_ID                        VARCHAR2(100) NOT NULL,
    PURL_CONTRIBUTOR_ID					  NUMBER(18),	
   	CONTRIBUTOR_PERSON_ID                 NUMBER(18,0),
	CONTRIBUTOR_FIRST_NAME                VARCHAR2(40),
    CONTRIBUTOR_LAST_NAME                 VARCHAR2(40),
  	CONTRIBUTOR_EMAIL_ADDR                VARCHAR2(75),
  	INVITE_SEND_DATE                      DATE,
  	INVITEE_PERSON_ID                     NUMBER(18,0),
	CONTRIBUTION_STATE				      VARCHAR2(20),
	IS_EXTERNAL_OR_INTERNAL               NUMBER(1,0) DEFAULT 0,
	IS_INVITED                            NUMBER(1,0) DEFAULT 0,
	CONTRIBUTED_DATE                      DATE,
	CREATED_BY                            NUMBER(18) NOT NULL,
  	DATE_CREATED                          DATE NOT NULL,
  	MODIFIED_BY                           NUMBER(18),
  	DATE_MODIFIED                         DATE,
	VERSION				                  NUMBER(18,0) DEFAULT 1,
	CONSTRAINT SA_INVITEANDCONTRIBUTE_INFO_PK PRIMARY KEY (ID)
	);
COMMENT ON COLUMN SA_INVITEANDCONTRIBUTE_INFO.CELEBRATION_ID IS 'Recipient identifier';
COMMENT ON COLUMN SA_INVITEANDCONTRIBUTE_INFO.CONTRIBUTOR_PERSON_ID IS 'Invited Person Id';
COMMENT ON COLUMN SA_INVITEANDCONTRIBUTE_INFO.CONTRIBUTOR_FIRST_NAME IS 'Invited Person First Name';
COMMENT ON COLUMN SA_INVITEANDCONTRIBUTE_INFO.CONTRIBUTOR_LAST_NAME IS 'Invited Person Last Name';
COMMENT ON COLUMN SA_INVITEANDCONTRIBUTE_INFO.CONTRIBUTOR_EMAIL_ADDR IS 'The invitation sent to the mail id';
COMMENT ON COLUMN SA_INVITEANDCONTRIBUTE_INFO.INVITE_SEND_DATE IS 'Invite Sent Date';
COMMENT ON COLUMN SA_INVITEANDCONTRIBUTE_INFO.INVITEE_PERSON_ID IS 'User id of the person who invited him';
COMMENT ON COLUMN SA_INVITEANDCONTRIBUTE_INFO.CONTRIBUTION_STATE IS 'Contribution state either invitation or contribution or complete or expired';
COMMENT ON COLUMN SA_INVITEANDCONTRIBUTE_INFO.IS_EXTERNAL_OR_INTERNAL IS 'True if the participant belongs to same organization or else false, as he/she from external world but invited.';
COMMENT ON COLUMN SA_INVITEANDCONTRIBUTE_INFO.IS_INVITED IS 'Whether he/she invited or not. Since some people can contribute without invite.';
COMMENT ON COLUMN SA_INVITEANDCONTRIBUTE_INFO.CONTRIBUTED_DATE IS 'Contributed date';
--rollback DROP TABLE SA_INVITEANDCONTRIBUTE_INFO CASCADE CONSTRAINTS;

--changeset ramm:10
--comment added column ROSTERPERSONID in APPLICATION_USER table to store roster person id
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='APPLICATION_USER' and COLUMN_NAME ='ROSTERPERSONID'
ALTER TABLE APPLICATION_USER ADD ROSTERPERSONID VARCHAR2(50);
--rollback ALTER TABLE APPLICATION_USER DROP (ROSTERPERSONID);

--changeset sivanand:11
--comment added column Modified the column width
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='PARTICIPANT' and COLUMN_NAME ='AVATAR_ORIGINAL'
ALTER TABLE PARTICIPANT MODIFY AVATAR_ORIGINAL VARCHAR2(300);
--rollback not required

--changeset sivanand:12
--comment added column Modified the column width
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='PARTICIPANT' and COLUMN_NAME ='AVATAR_SMALL'
ALTER TABLE PARTICIPANT MODIFY AVATAR_SMALL VARCHAR2(300);
--rollback not required

--changeset jabeen:13
--comment PROGRAM_AWARD_LEVEL_PK_SQ sequence
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_SEQUENCES where SEQUENCE_NAME='PROGRAM_AWARD_LEVEL_PK_SQ'
CREATE SEQUENCE PROGRAM_AWARD_LEVEL_PK_SQ START WITH 100 INCREMENT BY 1;
--rollback DROP SEQUENCE PROGRAM_AWARD_LEVEL_PK_SQ;

--changeset jabeen:14
--comment create program_award_levels table to store award level details
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select COUNT(1) from USER_TABLES where TABLE_NAME='PROGRAM_AWARD_LEVEL'
CREATE TABLE PROGRAM_AWARD_LEVEL
   (PROGRAM_AWARD_LEVEL_ID				  NUMBER(18,0),
    PROGRAM_ID                            NUMBER(18,0),
   	AWARD_LEVEL                           VARCHAR2(30),
   	COUNTRY	          			  		  VARCHAR2(3),
	CELEB_LABEL							  VARCHAR2(1000),
    CELEB_LABEL_CMX_ASSET_CODE			  VARCHAR2(1000),
	CELEB_MSG							  VARCHAR2(1000),
    CELEB_MSG_CMX_ASSET_CODE              VARCHAR2(1000),
	CELEB_IMG_URL                         VARCHAR2(1000),
	CELEB_IMG_DESC						  VARCHAR2(1000),
	CELEB_IMG_DESC_CMX_ASSET_CODE         VARCHAR2(1000),
    CREATED_BY                            NUMBER(18) NOT NULL,
  	DATE_CREATED                          DATE NOT NULL,
  	MODIFIED_BY                           NUMBER(18),
  	DATE_MODIFIED                         DATE,
	VERSION				                  NUMBER(18,0) DEFAULT 1,
	CONSTRAINT PROGRAM_AWARD_LEVEL_PK PRIMARY KEY (PROGRAM_AWARD_LEVEL_ID)
	);
ALTER TABLE PROGRAM_AWARD_LEVEL ADD ( CONSTRAINT PROGRAM_AWARD_LEVEL_FK  FOREIGN KEY (PROGRAM_ID)  REFERENCES PROGRAM (PROGRAM_ID)  ENABLE VALIDATE);
--rollback DROP TABLE PROGRAM_AWARD_LEVEL CASCADE CONSTRAINTS;

--changeset gorantla:15
--comment modifying the program_name column to null
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT DECODE(nullable,'N',0,1) FROM user_tab_columns WHERE table_name = 'PROGRAM' AND column_name = 'PROGRAM_NAME'
ALTER TABLE program MODIFY program_name NULL;
--rollback ALTER TABLE program MODIFY program_name NOT NULL;

--changeset gorantla:16
--comment added column Modified the column width
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM USER_TAB_COLUMNS WHERE TABLE_NAME='TMP_WINNER_NOMINATION_SUMMARY' AND COLUMN_NAME ='NOMINEE_AVATAR_URL'
ALTER TABLE TMP_WINNER_NOMINATION_SUMMARY MODIFY NOMINEE_AVATAR_URL VARCHAR2(300);
--rollback not required

