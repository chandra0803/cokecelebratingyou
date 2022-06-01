--liquibase formatted sql

--changeset sundaram:1
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='CLAIM_PROMOTION_ID_FK'
alter table CLAIM
ADD CONSTRAINT "CLAIM_PROMOTION_ID_FK" FOREIGN KEY ("PROMOTION_ID")
REFERENCES "PROMOTION" ("PROMOTION_ID");
--rollback not required
	  

--changeset sundaram:2
--comment Insert Index 
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_indexes where INDEX_NAME='CLAIM_IDX'
CREATE INDEX CLAIM_IDX ON CLAIM(submitter_id, promotion_id,node_id);
--rollback not required



--changeset sundaram:3
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='SUBMITTER_ID_FK'
ALTER TABLE CLAIM
ADD CONSTRAINT "SUBMITTER_ID_FK" FOREIGN KEY ("SUBMITTER_ID")
REFERENCES "APPLICATION_USER" ("USER_ID");
--rollback not required

--changeset sundaram:4
--comment Insert Foriegn constraint with on delete cascade
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='NODE_ID_FK'
ALTER TABLE CLAIM
ADD CONSTRAINT "NODE_ID_FK" FOREIGN KEY ("NODE_ID")
REFERENCES "NODE" ("NODE_ID");
--rollback not required 

--changeset sundaram:5
--comment Drop existing combo Index 
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select count(*) from user_indexes where INDEX_NAME='CLAIM_IDX'
DROP INDEX CLAIM_IDX;
--rollback not required

--changeset sundaram:6
--comment Insert Index 
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_indexes where INDEX_NAME='CLAIM_SUBMITTER_ID_IDX'
CREATE INDEX CLAIM_SUBMITTER_ID_IDX ON CLAIM(SUBMITTER_ID);
--rollback not required

--changeset sundaram:7
--comment Insert Index 
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_indexes where INDEX_NAME='CLAIM_PROMOTION_ID_IDX'
CREATE INDEX CLAIM_PROMOTION_ID_IDX ON CLAIM(PROMOTION_ID);
--rollback not required

--changeset sundaram:8
--comment Insert Index 
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_indexes where INDEX_NAME='CLAIM_NODE_ID_IDX'
CREATE INDEX CLAIM_NODE_ID_IDX ON CLAIM(NODE_ID);
--rollback not required