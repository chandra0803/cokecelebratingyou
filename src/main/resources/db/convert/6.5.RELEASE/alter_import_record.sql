--liquibase formatted sql

--changeset subramap:1
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='IMPORT_RECORD_ERROR_FK'
alter table IMPORT_RECORD_ERROR
drop constraint IMPORT_RECORD_ERROR_FK;
--rollback not required

--changeset subramap:2
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='IMPORT_RECORD_ERROR_FK'
alter table IMPORT_RECORD_ERROR
ADD CONSTRAINT "IMPORT_RECORD_ERROR_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required
	  
	  
--changeset subramap:3
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_AWARD_LEVEL_IMPORT_FK'
alter table STAGE_AWARD_LEVEL_IMPORT
drop constraint STAGE_AWARD_LEVEL_IMPORT_FK;
--rollback not required

--changeset subramap:4
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_AWARD_LEVEL_IMPORT_FK'
alter table STAGE_AWARD_LEVEL_IMPORT
ADD CONSTRAINT "STAGE_AWARD_LEVEL_IMPORT_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required
	  
	  
--changeset subramap:5
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_BUD_IMPORT_RECORD_FK'	  
alter table STAGE_BUDGET_IMPORT_RECORD
drop constraint STAGE_BUD_IMPORT_RECORD_FK;
--rollback not required

--changeset subramap:6
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_BUD_IMPORT_RECORD_FK'
alter table STAGE_BUDGET_IMPORT_RECORD
ADD CONSTRAINT "STAGE_BUD_IMPORT_RECORD_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required	  
	  

--changeset subramap:7
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_CP_BASE_DATA_IMPORT_FK'	  
alter table STAGE_CP_BASE_DATA_IMPORT
drop constraint STAGE_CP_BASE_DATA_IMPORT_FK;
--rollback not required

--changeset subramap:8
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_CP_BASE_DATA_IMPORT_FK'
alter table STAGE_CP_BASE_DATA_IMPORT
ADD CONSTRAINT "STAGE_CP_BASE_DATA_IMPORT_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required	  
	  
	  
--changeset subramap:9
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_CP_DATA_IMPORT_FK'
alter table STAGE_CP_DATA_IMPORT
drop constraint STAGE_CP_DATA_IMPORT_FK;
--rollback not required

--changeset subramap:10
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_CP_DATA_IMPORT_FK'
alter table STAGE_CP_DATA_IMPORT
ADD CONSTRAINT "STAGE_CP_DATA_IMPORT_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required	  
	  
	  
--changeset subramap:11
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_CP_PROG_DATA_IMPORT_FK'
alter table STAGE_CP_PROGRESS_DATA_IMPORT
drop constraint STAGE_CP_PROG_DATA_IMPORT_FK;
--rollback not required

--changeset subramap:12
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_CP_PROG_DATA_IMPORT_FK'
alter table STAGE_CP_PROGRESS_DATA_IMPORT
ADD CONSTRAINT "STAGE_CP_PROG_DATA_IMPORT_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required	  
	  
	  
--changeset subramap:13
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_DEP_IMPORT_RECORD_FK'
alter table STAGE_DEPOSIT_IMPORT_RECORD
drop constraint STAGE_DEP_IMPORT_RECORD_FK;
--rollback not required

--changeset subramap:14
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_DEP_IMPORT_RECORD_FK'
alter table STAGE_DEPOSIT_IMPORT_RECORD
ADD CONSTRAINT "STAGE_DEP_IMPORT_RECORD_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required	  
	  
	  

--changeset subramap:15
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_GQ_BASE_DATA_IMPORT_FK'
alter table STAGE_GQ_BASE_DATA_IMPORT
drop constraint STAGE_GQ_BASE_DATA_IMPORT_FK;
--rollback not required

--changeset subramap:16
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_GQ_BASE_DATA_IMPORT_FK'
alter table STAGE_GQ_BASE_DATA_IMPORT
ADD CONSTRAINT "STAGE_GQ_BASE_DATA_IMPORT_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required	  



--changeset subramap:17
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_GQ_GOAL_DATA_IMPORT_FK'
alter table STAGE_GQ_GOAL_DATA_IMPORT
drop constraint STAGE_GQ_GOAL_DATA_IMPORT_FK;
--rollback not required

--changeset subramap:18
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_GQ_GOAL_DATA_IMPORT_FK'
alter table STAGE_GQ_GOAL_DATA_IMPORT
ADD CONSTRAINT "STAGE_GQ_GOAL_DATA_IMPORT_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required	  



--changeset subramap:19
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_GQ_PROG_DATA_IMPORT_FK'
alter table STAGE_GQ_PROGRESS_DATA_IMPORT
drop constraint STAGE_GQ_PROG_DATA_IMPORT_FK;
--rollback not required

--changeset subramap:20
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_GQ_PROG_DATA_IMPORT_FK'
alter table STAGE_GQ_PROGRESS_DATA_IMPORT
ADD CONSTRAINT "STAGE_GQ_PROG_DATA_IMPORT_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required

--changeset subramap:21
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_GQ_VIN_NBR_IMPORT_FK'
alter table STAGE_GQ_VIN_NBR_IMPORT
drop constraint STAGE_GQ_VIN_NBR_IMPORT_FK;
--rollback not required

--changeset subramap:22
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_GQ_VIN_NBR_IMPORT_FK'
alter table STAGE_GQ_VIN_NBR_IMPORT
ADD CONSTRAINT "STAGE_GQ_VIN_NBR_IMPORT_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required	  


--changeset subramap:23
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_HIER_IMPORT_RECORD_FK'
alter table STAGE_HIERARCHY_IMPORT_RECORD
drop constraint STAGE_HIER_IMPORT_RECORD_FK;
--rollback not required

--changeset subramap:24
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_HIER_IMPORT_RECORD_FK'
alter table STAGE_HIERARCHY_IMPORT_RECORD
ADD CONSTRAINT "STAGE_HIER_IMPORT_RECORD_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID");
--rollback not required	  


--changeset subramap:25
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_NOM_APPROVERS_IMPORT_FK'
alter table STAGE_NOM_APPROVERS_IMPORT
drop constraint STAGE_NOM_APPROVERS_IMPORT_FK;
--rollback not required

--changeset subramap:26
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_NOM_APPROVERS_IMPORT_FK'
alter table STAGE_NOM_APPROVERS_IMPORT
ADD CONSTRAINT "STAGE_NOM_APPROVERS_IMPORT_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required	  


--changeset subramap:27
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_PAX_IMPORT_RECORD_FK'
alter table STAGE_PAX_IMPORT_RECORD
drop constraint STAGE_PAX_IMPORT_RECORD_FK;
--rollback not required

--changeset subramap:28
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_PAX_IMPORT_RECORD_FK'
alter table STAGE_PAX_IMPORT_RECORD
ADD CONSTRAINT "STAGE_PAX_IMPORT_RECORD_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
	  --rollback not required


--changeset subramap:29
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_PROD_IMPORT_RECORD_FK'
alter table STAGE_PRODUCT_IMPORT_RECORD
drop constraint STAGE_PROD_IMPORT_RECORD_FK;
--rollback not required

--changeset subramap:30
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_PROD_IMPORT_RECORD_FK'
alter table STAGE_PRODUCT_IMPORT_RECORD
ADD CONSTRAINT "STAGE_PROD_IMPORT_RECORD_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required


--changeset subramap:31
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_QUIZ_IMPORT_RECORD_FK'
alter table STAGE_QUIZ_IMPORT_RECORD
drop constraint STAGE_QUIZ_IMPORT_RECORD_FK;
--rollback not required

--changeset subramap:32
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_QUIZ_IMPORT_RECORD_FK'
alter table STAGE_QUIZ_IMPORT_RECORD
ADD CONSTRAINT "STAGE_QUIZ_IMPORT_RECORD_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required	  	  	  


--changeset subramap:33
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_HIER_IMPORT_RECORD_FK'
alter table STAGE_HIERARCHY_IMPORT_RECORD
drop constraint STAGE_HIER_IMPORT_RECORD_FK;
--rollback not required

--changeset subramap:34
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_HIER_IMPORT_RECORD_FK'
alter table STAGE_HIERARCHY_IMPORT_RECORD
ADD CONSTRAINT "STAGE_HIER_IMPORT_RECORD_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required	  

--changeset subramap:35
--comment Drop Foriegn constraint For adding on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_constraints where constraint_name='STAGE_BADGE_LOAD_FK'
alter table STAGE_BADGE_LOAD
drop constraint STAGE_BADGE_LOAD_FK;
--rollback not required

--changeset subramap:36
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='STAGE_BADGE_LOAD_FK'
alter table STAGE_BADGE_LOAD
ADD CONSTRAINT "STAGE_BADGE_LOAD_FK" FOREIGN KEY ("IMPORT_FILE_ID")
REFERENCES "IMPORT_FILE" ("IMPORT_FILE_ID") ON DELETE CASCADE;
--rollback not required	  

