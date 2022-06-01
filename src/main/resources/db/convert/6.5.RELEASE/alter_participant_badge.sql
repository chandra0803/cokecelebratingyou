--liquibase formatted sql

--changeset esakkimu:1
--comment Adding foreign key constraint
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT count(1) FROM user_constraints WHERE table_name = 'PARTICIPANT_BADGE' AND constraint_name = 'PARTICIPANT_BADGE_CONTESTID_FK'
ALTER TABLE PARTICIPANT_BADGE 
ADD ( CONSTRAINT PARTICIPANT_BADGE_CONTESTID_FK  FOREIGN KEY (SSI_CONTEST_ID)  REFERENCES SSI_CONTEST (SSI_CONTEST_ID) );
--rollback ALTER TABLE PARTICIPANT_BADGE DROP CONSTRAINT PARTICIPANT_BADGE_CONTESTID_FK;

