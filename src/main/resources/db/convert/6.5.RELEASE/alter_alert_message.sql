--liquibase formatted sql

--changeset esakkimu:1
--comment Adding foreign key constraint
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT count(1) FROM user_constraints WHERE table_name = 'ALERT_MESSAGE' AND constraint_name = 'ALERT_MESSAGE_CONTESTID_FK'
ALTER TABLE ALERT_MESSAGE 
ADD ( CONSTRAINT ALERT_MESSAGE_CONTESTID_FK FOREIGN KEY (SSI_CONTEST_ID)  REFERENCES SSI_CONTEST (SSI_CONTEST_ID) );
--rollback ALTER TABLE ALERT_MESSAGE DROP CONSTRAINT ALERT_MESSAGE_CONTESTID_FK;

