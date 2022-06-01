--liquibase formatted sql

--changeset sivanand:1
--comment  Insert system variable for OTS service end point dev
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.endpoint.url.dev', 'OTS service host url-DEV', 'OTS Service', 5, 0, 0, 'https://dev.api.biw.cloud/v1/ots', 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='ots.endpoint.url.dev';

--changeset sivanand:2
--comment  Insert system variable for OTS service end point qa
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.endpoint.url.qa', 'OTS service host url-QA', 'OTS Service', 5, 0, 0, 'https://qa.api.biw.cloud/v1/ots', 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='ots.endpoint.url.qa';

--changeset sivanand:3
--comment  Insert system variable for OTS service end point preprod
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.endpoint.url.preprod', 'OTS service host url-PPRD', 'OTS Service', 5, 0, 0, 'https://pprd.api.biw.cloud/v1/ots', 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='ots.endpoint.url.preprod';

--changeset sivanand:4
--comment  Insert system variable for OTS service end point prod
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.endpoint.url.prod', 'OTS service host url-PRD', 'OTS Service', 5, 0, 0, 'https://api.biw.cloud/v1/ots', 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='ots.endpoint.url.prod';

--changeset sivanand:5
--comment  Insert system variable for OTS service end client id dev
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.clientid.dev', 'OTS client id-DEV', 'OTS Service', 5, 0, 0,null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='ots.api.clientid.dev';

--changeset sivanand:6
--comment  Insert system variable for OTS service end client id qa
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.clientid.qa', 'OTS client id-QA', 'OTS Service', 5, 0, 0, null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='ots.api.clientid.qa';

--changeset sivanand:7
--comment  Insert system variable for OTS service end client id preprod
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.clientid.preprod', 'OTS client id-PPRD', 'OTS Service', 5, 0, 0, null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='ots.api.clientid.preprod';

--changeset sivanand:8
--comment  Insert system variable for OTS service end client id prod
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.clientid.prod', 'OTS client id-PRD', 'OTS Service', 5, 0, 0, null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='ots.api.clientid.prod';

--changeset sivanand:9
--comment  Insert system variable for OTS service secretkey dev
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.secretkey.dev', 'OTS secret key-DEV', 'OTS Service', 5, 0, 0, null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='ots.api.secretkey.dev';

--changeset sivanand:10
--comment  Insert system variable for OTS service secretkey qa
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.secretkey.qa', 'OTS secret key-QA', 'OTS Service', 5, 0, 0, null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='ots.api.secretkey.qa';

--changeset sivanand:11
--comment  Insert system variable for OTS service secretkey preprod
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.secretkey.preprod', 'OTS secret key-PPRD', 'OTS Service', 5, 0, 0, null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='ots.api.secretkey.preprod';

--changeset sivanand:12
--comment  Insert system variable for OTS service secretkey prod
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.secretkey.prod', 'OTS secret key-PRD', 'OTS Service', 5, 0, 0, null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='ots.api.secretkey.prod';

--changeset palaniss:13
--comment System variable for MerchlinQ for prod, updating to new value
UPDATE os_propertyset SET STRING_VAL = 'https://biwdispatcher.performnet.com/biw-dispatcher/plateau/1/login.action' WHERE entity_name = 'goalquest.merchlinqsso.url.prod';
--rollback UPDATE os_propertyset SET STRING_VAL = 'http://www1.awardslinq.com/merchlinq/signon.action' WHERE entity_name = 'goalquest.merchlinqsso.url.prod';

--changeset palaniss:14
--comment System variable for MerchlinQ for pprd, updating to new value
UPDATE os_propertyset SET STRING_VAL = 'https://biwdispatcherpprd.performnet.com/biw-dispatcher/plateau/1/login.action' WHERE entity_name = 'goalquest.merchlinqsso.url.preprod';
--rollback UPDATE os_propertyset SET STRING_VAL = 'http://www1pprd.awardslinq.com/merchlinq/signon.action' WHERE entity_name = 'goalquest.merchlinqsso.url.preprod';

--changeset palaniss:15
--comment System variable for MerchlinQ for dev, updating to new value
UPDATE os_propertyset SET STRING_VAL = 'https://biwdispatcherpprd.performnet.com/biw-dispatcher/plateau/1/login.action' WHERE entity_name = 'goalquest.merchlinqsso.url.dev';
--rollback UPDATE os_propertyset SET STRING_VAL = 'http://www1qa.awardslinq.com/merchlinq/signon.action' WHERE entity_name = 'goalquest.merchlinqsso.url.dev';

--changeset palaniss:16
--comment System variable for MerchlinQ for dev, updating to new value
UPDATE os_propertyset SET STRING_VAL = 'https://biwdispatcherqa.performnet.com/biw-dispatcher/plateau/1/login.action' WHERE entity_name = 'goalquest.merchlinqsso.url.qa';
--rollback UPDATE os_propertyset SET STRING_VAL = 'http://www1qa.awardslinq.com/merchlinq/signon.action' WHERE entity_name = 'goalquest.merchlinqsso.url.qa';

--changeset ramesh:17
--comment  Insert system variable for Recognition Advisor Manager level check
INSERT INTO OS_PROPERTYSET (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,GROUP_NAME)
VALUES(ENTITY_ID_PK_SQ.NEXTVAL,'ra.recog.levels.check','Number of levels to check under a manager',2,0,0,NULL,0,1,NULL,'Recognition Advisor');
--rollback DELETE FROM os_propertyset WHERE entity_name='ra.recog.levels.check';

--changeset ramesh:18
--comment  Insert system variable for Recognition Advisor, Number of days after which a recognition reminder will be shown for a new hire
INSERT INTO OS_PROPERTYSET (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,GROUP_NAME)
VALUES(ENTITY_ID_PK_SQ.NEXTVAL,'ra.numberofdays.newhire.firstreminder','Days after which a recog reminder will be shown for newhire',2,0,0,NULL,0,15,NULL,'Recognition Advisor');
--rollback DELETE FROM os_propertyset WHERE entity_name='ra.numberofdays.newhire.firstreminder';

--changeset ramesh:19
--comment  Insert system variable for Recognition Advisor, Number of days after which a new hire will be changed to a regular employee status
INSERT INTO OS_PROPERTYSET (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,GROUP_NAME)
VALUES(ENTITY_ID_PK_SQ.NEXTVAL,'ra.numberofdays.newhiretoregularemployee','Days after which a newhire is changed to regular emp status',2,0,0,NULL,0,30,NULL,'Recognition Advisor');
--rollback DELETE FROM os_propertyset WHERE entity_name='ra.numberofdays.newhiretoregularemployee';

--changeset ramesh:20
--comment  Insert system variable for Recognition Advisor,Number of days after which a recognition reminder will be shown for a regular employee
INSERT INTO OS_PROPERTYSET (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,GROUP_NAME)
VALUES(ENTITY_ID_PK_SQ.NEXTVAL,'ra.numberofdays.employee.reminder','Days after which a recogn reminder is shown for regular emp',2,0,0,NULL,0,90,NULL,'Recognition Advisor');
--rollback DELETE FROM os_propertyset WHERE entity_name='ra.numberofdays.employee.reminder';

--changeset sivanand:21
--comment  Insert system variable for enabling Recognition advisor
INSERT INTO OS_PROPERTYSET (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,GROUP_NAME)
VALUES(ENTITY_ID_PK_SQ.NEXTVAL,'ra.enabled','Recognition advisor enabled',1,0,0,NULL,0,0,NULL,'Recognition Advisor');
--rollback DELETE FROM os_propertyset WHERE entity_name='ra.enabled';

--changeset sivanand:22
--comment Removed system variable as the no of days for new hire not required 
DELETE os_propertyset WHERE entity_name='ra.numberofdays.newhire.firstreminder';
--rollback not required

--changeset ramesh:23
--comment  Insert system variable for enabling new hire RA reminder mail
INSERT INTO OS_PROPERTYSET (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,GROUP_NAME)
VALUES(ENTITY_ID_PK_SQ.NEXTVAL,'ra.sendreminder.torecognize.newhire','Enable new hire RA reminder mail',1,1,0,NULL,0,0,NULL,'Recognition Advisor');
--rollback DELETE FROM os_propertyset WHERE entity_name='ra.sendreminder.torecognize.newhire';

--changeset ramesh:24
--comment  Insert system variable for enabling team member RA reminder mail
INSERT INTO OS_PROPERTYSET (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,GROUP_NAME)
VALUES(ENTITY_ID_PK_SQ.NEXTVAL,'ra.sendreminder.torecognize.teammember','Enable team member RA reminder mail',1,1,0,NULL,0,0,NULL,'Recognition Advisor');
--rollback DELETE FROM os_propertyset WHERE entity_name='ra.sendreminder.torecognize.teammember';

--changeset subramap:25
--comment Column for tracking the system variable changes
ALTER TABLE OS_PROPERTYSET
ADD TRACKING_ID NUMBER(18);
--rollback ALTER TABLE OS_PROPERTYSET DROP COLUMN TRACKING_ID;

--changeset subramap:26
--comment Audit Column for tracking modified by
ALTER TABLE OS_PROPERTYSET
ADD MODIFIED_BY NUMBER(18);
--rollback ALTER TABLE OS_PROPERTYSET DROP COLUMN MODIFIED_BY;

--changeset subramap:27
--comment Column for tracking the modified date
ALTER TABLE OS_PROPERTYSET
ADD DATE_MODIFIED DATE;
--rollback ALTER TABLE OS_PROPERTYSET DROP COLUMN DATE_MODIFIED;

--changeset subramap:28
--comment Audit Column for tracking created by
ALTER TABLE OS_PROPERTYSET
ADD CREATED_BY NUMBER(18);
--rollback ALTER TABLE OS_PROPERTYSET DROP COLUMN CREATED_BY;

--changeset subramap:29
--comment Column for tracking the created date
ALTER TABLE OS_PROPERTYSET
ADD DATE_CREATED DATE;
--rollback ALTER TABLE OS_PROPERTYSET DROP COLUMN DATE_CREATED;

--changeset subramap:30
--comment Adding foreign key constraint
ALTER TABLE OS_PROPERTYSET 
ADD CONSTRAINT OS_PROPERTYSET_TRACKING_FK  FOREIGN KEY (TRACKING_ID) REFERENCES OS_PROPERTYSET_TRACKING(TRACKING_ID);
--rollback ALTER TABLE OS_PROPERTYSET DROP CONSTRAINT OS_PROPERTYSET_TRACKING_FK;

--changeset ramesh:31
--comment Update the roles.for.biw.only system variable for disabling the Pax User type to ContentAdministrator,PROCESS_TEAM roles
UPDATE os_propertyset SET STRING_VAL='BI_ADMIN,PROJ_MGR,PLATEAU_REDEMPTION,ContentAdministrator,PROCESS_TEAM'  WHERE entity_name='roles.for.biw.only';
--rollback UPDATE os_propertyset SET entity_name='roles.for.biw.only' WHERE STRING_VAL='BI_ADMIN,PROJ_MGR,PLATEAU_REDEMPTION';

--changeset palaniss:32
--comment Update the value of Under Armour Micro Service Past Days to 7 instead of 1 as default
UPDATE os_propertyset set INT_VAL=7 where ENTITY_NAME='bi.ua.microservice.past.days';
--rollback update os_propertyset set INT_VAL=1 where ENTITY_NAME='bi.ua.microservice.past.days';

--changeset sivanand:33
--comment Reverting the above changes
UPDATE os_propertyset set INT_VAL=1 where ENTITY_NAME='bi.ua.microservice.past.days';
--rollback update os_propertyset set INT_VAL=7 where ENTITY_NAME='bi.ua.microservice.past.days';

--changeset ramesh:34
--comment  Insert asystem variable for Termed Users
INSERT INTO OS_PROPERTYSET (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,GROUP_NAME)
VALUES(ENTITY_ID_PK_SQ.NEXTVAL,'termed.user.allow.redeem','Allow termed users to shop',1,1,0,NULL,0,0,NULL,'Termed Users');
--rollback DELETE FROM os_propertyset WHERE entity_name='termed.user.allow.redeem';

--changeset sivanand:35
--comment  Insert system variable for Pure SSO client
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'sso.login.url.dev', 'SSO Client Url-DEV', 'client', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='sso.login.url.dev';

--changeset sivanand:36
--comment  Insert system variable for Pure SSO client
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'sso.login.url.qa', 'SSO Client Url-QA', 'client', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='sso.login.url.qa';

--changeset sivanand:37
--comment  Insert system variable for Pure SSO client
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'sso.login.url.preprod', 'SSO Client Url-PREPROD', 'client', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='sso.login.url.preprod';

--changeset sivanand:38
--comment  Insert system variable for Pure SSO client
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'sso.login.url.prod', 'SSO Client Url-PROD', 'client', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='sso.login.url.prod';

--changeset sivanand:39
--comment  Insert system variable for Pure SSO client
INSERT INTO OS_PROPERTYSET (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,GROUP_NAME)
VALUES(ENTITY_ID_PK_SQ.NEXTVAL,'sso.only','If set to yes, indicates a SSO only client',1,0,0,NULL,0,0,NULL,'client');
--rollback DELETE FROM os_propertyset WHERE entity_name='sso.only';

--changeset esakkimu:40
--comment  Insert system variable for InActivate Biw Users Process 
INSERT INTO OS_PROPERTYSET (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,GROUP_NAME)
VALUES(ENTITY_ID_PK_SQ.NEXTVAL,'biw.user.days.to.expire','BIW user days to expire',2,0,0,NULL,0,90,NULL,'unknown');
--rollback DELETE FROM os_propertyset WHERE entity_name='biw.user.days.to.expire';


--changeset subramap:41
--comment Updating Date Created and Created By for all the newly introduced system variables
UPDATE OS_PROPERTYSET
set DATE_CREATED=sysdate,CREATED_BY=6.4
where ENTITY_NAME in ('ots.endpoint.url.dev','ots.endpoint.url.qa','ots.endpoint.url.preprod','ots.endpoint.url.prod','ots.api.clientid.dev','ots.api.clientid.qa','ots.api.clientid.preprod','ots.api.clientid.prod');
--rollback not required


--changeset subramap:42
--comment Updating Date Created and Created By for all the newly introduced system variables
UPDATE OS_PROPERTYSET
set DATE_CREATED=sysdate,CREATED_BY=6.4
where ENTITY_NAME in ('ots.api.secretkey.dev','ots.api.secretkey.qa','ots.api.secretkey.preprod','ots.api.secretkey.prod','ra.recog.levels.check','ra.numberofdays.newhiretoregularemployee');
--rollback not required

--changeset subramap:43
--comment Updating Date Created and Created By for all the newly introduced system variables
UPDATE OS_PROPERTYSET
set DATE_CREATED=sysdate,CREATED_BY=6.4
where ENTITY_NAME in ('ra.numberofdays.employee.reminder','ra.enabled','ra.sendreminder.torecognize.newhire','ra.sendreminder.torecognize.teammember','sso.login.url.dev','sso.login.url.qa','sso.login.url.preprod','sso.login.url.prod','sso.only','biw.user.days.to.expire');
--rollback not required


--changeset subramap:44
--comment adding the precision
alter table os_propertyset
modify  created_by number(19,1);
--rollback not required

--changeset subramap:45
--comment Updating Date Created and Created By for all the newly introduced system variables
UPDATE OS_PROPERTYSET
set DATE_CREATED=sysdate,CREATED_BY=6.4
where ENTITY_NAME in ('ots.endpoint.url.dev','ots.endpoint.url.qa','ots.endpoint.url.preprod','ots.endpoint.url.prod','ots.api.clientid.dev','ots.api.clientid.qa','ots.api.clientid.preprod','ots.api.clientid.prod');
--rollback not required


--changeset subramap:46
--comment Updating Date Created and Created By for all the newly introduced system variables
UPDATE OS_PROPERTYSET
set DATE_CREATED=sysdate,CREATED_BY=6.4
where ENTITY_NAME in ('ots.api.secretkey.dev','ots.api.secretkey.qa','ots.api.secretkey.preprod','ots.api.secretkey.prod','ra.recog.levels.check','ra.numberofdays.newhiretoregularemployee');
--rollback not required

--changeset subramap:47
--comment Updating Date Created and Created By for all the newly introduced system variables
UPDATE OS_PROPERTYSET
set DATE_CREATED=sysdate,CREATED_BY=6.4
where ENTITY_NAME in ('ra.numberofdays.employee.reminder','ra.enabled','ra.sendreminder.torecognize.newhire','ra.sendreminder.torecognize.teammember','sso.login.url.dev','sso.login.url.qa','sso.login.url.preprod','sso.login.url.prod','sso.only','biw.user.days.to.expire');
--rollback not required

--changeset sivanand:48
--comment  Insert missing aws accessKey for qa
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.accessKey.qa'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.accessKey.qa', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);
--rollback not required

--changeset sivanand:49
--comment  Insert missing aws accessKey for preprod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.accessKey.preprod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.accessKey.preprod', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);
--rollback not required


--changeset sivanand:50
--comment  Insert missing aws secretKey for qa
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.secretKey.qa'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.secretKey.qa', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);
--rollback not required


--changeset sivanand:51
--comment  Insert missing aws secretKey for preprod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.secretKey.preprod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.secretKey.preprod', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);
--rollback not required

--changeset sivanand:52
--comment  Insert missing aws avatar bucketname for qa
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.avatar.bucketname.qa'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.avatar.bucketname.qa', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);
--rollback not required

--changeset sivanand:53
--comment  Insert missing aws avatar bucketname for preprod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.avatar.bucketname.preprod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.avatar.bucketname.preprod', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);
--rollback not required


--changeset sivanand:54
--comment correct entity name
UPDATE os_propertyset SET ENTITY_NAME = 'aws.global.fileproc.webdav.username',MODIFIED_BY='64',DATE_MODIFIED=sysdate WHERE ENTITY_NAME = 'aws.global.fileprocessing.webdav.usernam';
--rollback not required


--changeset sivanand:55
--comment correct entity name
UPDATE os_propertyset SET ENTITY_NAME = 'aws.global.fileproc.webdav.password',MODIFIED_BY='64',DATE_MODIFIED=sysdate WHERE ENTITY_NAME = 'aws.global.fileprocessing.webdav.passwor';
--rollback not required

--changeset sivanand:56
--comment AWS attribute values has to change accordingly for every client 
UPDATE os_propertyset SET STRING_VAL = 'CHANGE ME!',MODIFIED_BY='64',DATE_MODIFIED=sysdate WHERE GROUP_NAME = 'AWS';
--rollback not required

--changeset subramap:57
--comment Increasing the size of entity name column
ALTER TABLE OS_PROPERTYSET 
MODIFY ENTITY_NAME VARCHAR2(100 CHAR);
--rollback not required

--changeset subramap:58
--comment  Insert missing aws username for qa
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.global.fileproc.webdav.username.qa'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.global.fileproc.webdav.username.qa', 'AWS username for the webdav instance/FTP', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);
--rollback not required

--changeset subramap:59
--comment  Insert missing aws username for pprd
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.global.fileproc.webdav.username.preprod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.global.fileproc.webdav.username.preprod', 'AWS username for the webdav instance/FTP', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);
--rollback not required


--changeset subramap:60
--comment  Insert missing aws username for prod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.global.fileproc.webdav.username.prod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.global.fileproc.webdav.username.prod', 'AWS username for the webdav instance/FTP', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);
--rollback not required


--changeset subramap:61
--comment  Insert missing aws password for prod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.global.fileproc.webdav.password.prod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.global.fileproc.webdav.password.prod', 'AWS password for the webdav instance/FTP', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);
--rollback not required


--changeset subramap:62
--comment  Insert missing aws password for preprod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.global.fileproc.webdav.password.preprod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.global.fileproc.webdav.password.preprod', 'AWS password for the webdav instance/FTP', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);
--rollback not required

--changeset subramap:63
--comment  Insert missing aws password for qa
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.global.fileproc.webdav.password.qa'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.global.fileproc.webdav.password.qa', 'AWS password for the webdav instance/FTP', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);
--rollback not required