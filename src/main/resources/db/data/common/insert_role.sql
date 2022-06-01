delete role
/
--
-- ROLE data
--
-- NOTE: There is alot of code that depends on role code data.  Do not change codes without
--       full knowledge of the ramifications.
--
-- Beacon Roles
--


-- 
-- BI User Roles
--

INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT,CODE,IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(2,'Project Manager / Technical Analyst', 'PM/TA HELP TEXT','PROJ_MGR', 1,1,0, sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(2, 'bi')
/

INSERT INTO role
(ROLE_ID,NAME, HELP_TEXT,CODE,IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(3,'Process Team', 'PROCESS TEAM HELP TEXT', 'PROCESS_TEAM', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(3, 'bi')
/

INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT,CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(6,'BI Administrator', 'BI ADMIN HELP TEXT', 'BI_ADMIN', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(6, 'bi')
/

--
-- Content Manager Roles
-- TODO turn into all uppercase
--

INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT, CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(9, 'Content Administrator', 'CM ADMIN HELP TEXT', 'ContentAdministrator', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(9, 'bi')
/

INSERT INTO ROLE
(ROLE_ID, NAME, CODE, HELP_TEXT, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION)
VALUES
(100, 'DIY_BANNER_ADMIN', 'DIY_BANNER_ADMIN', 'DIY_BANNER_ADMIN',1,0,0,sysdate,NULL, NULL, 1)
/
INSERT INTO ROLE
(ROLE_ID, NAME, CODE, HELP_TEXT, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION)
VALUES
(101, 'DIY_NEWS_ADMIN', 'DIY_NEWS_ADMIN', 'DIY_NEWS_ADMIN',1,0,0,sysdate,NULL, NULL, 1)
/
INSERT INTO ROLE
(ROLE_ID, NAME, CODE, HELP_TEXT, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION)
VALUES
(102, 'DIY_RESOURCE_ADMIN', 'DIY_RESOURCE_ADMIN', 'DIY_RESOURCE_ADMIN',1,0,0,sysdate,NULL, NULL, 1)
/
INSERT INTO ROLE
(ROLE_ID, NAME, CODE, HELP_TEXT, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION)
VALUES
(103, 'DIY_TIPS_ADMIN', 'DIY_TIPS_ADMIN', 'DIY_TIPS_ADMIN',1,0,0,sysdate,NULL, NULL, 1)
/
-- 
-- Functional roles
--
INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT, CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(ROLE_PK_SQ.nextval, 'View Reports', 'Grants user ability to view admin reports', 'VIEW_REPORTS', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'bi')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'pax')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'clt')
/
INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT, CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(ROLE_PK_SQ.nextval, 'View Participants', 'Grants user ability to view access to Participant Info', 'VIEW_PARTICIPANTS', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'bi')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'pax')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'clt')
/
INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT, CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(ROLE_PK_SQ.nextval, 'Launch as Pax', 'Grants user ability to Launch As', 'LAUNCH_AS_PAX', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'bi')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'pax')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'clt')
/
INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT, CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(ROLE_PK_SQ.nextval, 'Unlock Login', 'Grants user ability to unlock accounts', 'UNLOCK_LOGIN', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'bi')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'pax')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'clt')
/
INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT, CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(ROLE_PK_SQ.nextval, 'Modify Proxies', 'Grants user ability to modify use proxies', 'MODIFY_PROXIES', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'bi')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'pax')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'clt')
/
INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT, CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(ROLE_PK_SQ.nextval, 'Re-Issue Password', 'Grants user ability to re-issue a pax password', 'REISSUE_PASSWORD', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'bi')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'pax')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
(ROLE_PK_SQ.currval, 'clt')
