--liquibase formatted sql

--changeset chandrsa:1
--comment added unique constraint on columns (user_id, policy_version) to restrict duplicate records
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT CASE WHEN count(1) = 0 THEN 1 WHEN count(1) = 1 THEN 0 ELSE 1 END counting FROM (SELECT table_name FROM user_tables WHERE table_name = 'USER_COOKIES_ACCEPTANCE' UNION ALL SELECT table_name FROM user_constraints WHERE table_name = 'USER_COOKIES_ACCEPTANCE' AND constraint_name IN ('USER_COOKIES_USER_ID_UK','USER_COOKIE_USER_ID_UK'))
ALTER TABLE USER_COOKIES_ACCEPTANCE ADD CONSTRAINT USER_COOKIES_USER_ID_UK UNIQUE (USER_ID, POLICY_VERSION); 
--rollback not required

--changeset chandrsa:2
--comment added unique constraint on columns (user_id, policy_version) to restrict duplicate records
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT CASE WHEN count(1) = 0 THEN 1 WHEN count(1) = 1 THEN 0 ELSE 1 END counting FROM (SELECT table_name FROM user_tables WHERE table_name = 'USER_COOKIE_ACCEPTANCE' UNION ALL SELECT table_name FROM user_constraints WHERE table_name = 'USER_COOKIE_ACCEPTANCE' AND constraint_name IN ('USER_COOKIES_USER_ID_UK','USER_COOKIE_USER_ID_UK'))
ALTER TABLE USER_COOKIE_ACCEPTANCE ADD CONSTRAINT USER_COOKIE_USER_ID_UK UNIQUE (USER_ID, POLICY_VERSION); 
--rollback not required