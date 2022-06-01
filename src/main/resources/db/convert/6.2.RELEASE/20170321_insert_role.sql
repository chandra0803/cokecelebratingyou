INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT, CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(ROLE_PK_SQ.nextval, 'View Reports', 'VIEW REPORTS HELP TEXT', 'VIEW_REPORTS', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='VIEW_REPORTS'), 'bi')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='VIEW_REPORTS'), 'pax')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='VIEW_REPORTS'), 'clt')
/

INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT, CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(ROLE_PK_SQ.nextval, 'View Participants', 'VIEW PARTICIPANTS HELP TEXT', 'VIEW_PARTICIPANTS', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='VIEW_PARTICIPANTS'), 'bi')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='VIEW_PARTICIPANTS'), 'pax')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='VIEW_PARTICIPANTS'), 'clt')
/

INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT, CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(ROLE_PK_SQ.nextval, 'Launch as Pax', 'LAUNCH AS PAX HELP TEXT', 'LAUNCH_AS_PAX', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='LAUNCH_AS_PAX'), 'bi')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='LAUNCH_AS_PAX'), 'pax')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='LAUNCH_AS_PAX'), 'clt')
/

INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT, CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(ROLE_PK_SQ.nextval, 'Unlock Login', 'UNLOCK LOGIN HELP TEXT', 'UNLOCK_LOGIN', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='UNLOCK_LOGIN'), 'bi')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='UNLOCK_LOGIN'), 'pax')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='UNLOCK_LOGIN'), 'clt')
/

INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT, CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(ROLE_PK_SQ.nextval, 'Modify Proxies', 'MODIFY PROXIES HELP TEXT', 'MODIFY_PROXIES', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='MODIFY_PROXIES'), 'bi')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='MODIFY_PROXIES'), 'pax')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='MODIFY_PROXIES'), 'clt')
/

INSERT INTO role
(ROLE_ID, NAME, HELP_TEXT, CODE, IS_ACTIVE,IS_PAX_SPECIFIC,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(ROLE_PK_SQ.nextval, 'Reissue Password', 'REISSUE PASSWORD HELP TEXT', 'REISSUE_PASSWORD', 1,1,0,sysdate,NULL,NULL,1)
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='REISSUE_PASSWORD'), 'bi')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='REISSUE_PASSWORD'), 'pax')
/
INSERT INTO user_type_role
(ROLE_ID, USER_TYPE_CODE)
VALUES
((select role_id from role where code='REISSUE_PASSWORD'), 'clt')
/