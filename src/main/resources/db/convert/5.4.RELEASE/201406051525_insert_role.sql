ALTER TABLE role ADD IS_PAX_SPECIFIC NUMBER(1) DEFAULT 1
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
update role set IS_PAX_SPECIFIC = 1 where role_id in ('1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19')
/