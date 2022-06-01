--liquibase formatted sql

--changeset ramesh:1
--comment  Insert system variable for Direct, SSO, Mixed.Direct for Direct Login,SSO for SSO only,Mixed for Direct and SSO. Default:Mixed
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'login.type'
INSERT INTO OS_PROPERTYSET (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,GROUP_NAME,CREATED_BY,DATE_CREATED)
VALUES(ENTITY_ID_PK_SQ.NEXTVAL,'login.type','Allowed values : Direct, SSO, Mixed',5,0,0,'CHANGE ME',0,0,NULL,'client',6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='login.type';

--changeset palaniss:2
--comment insert system variable for enabling unused budget notification
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ra.Program.NotUsed.Notification.Enabled'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'ra.Program.NotUsed.Notification.Enabled', 'Send notification if budget is not used for X days', 'Recognition Advisor', 1, 1, 0, null, 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='ra.Program.NotUsed.Notification.Enabled';

--changeset palaniss:3
--comment system variable to provide no. of days after which a budget is considered as not used
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ra.Program.NumberOfDays.NotUsed'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'ra.Program.NumberOfDays.NotUsed', 'No. of days after which a budget is considered as not used', 'Recognition Advisor', 2, 0, 0, null, 0, 30, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='ra.Program.NumberOfDays.NotUsed';

--changeset sivanand:4
--comment insert system variable for enable new service anniversary from nackle
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'new.service.anniversary.enabled'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'new.service.anniversary.enabled', 'Enable New Service Anniversary Service From Nackle', 'Nackle Engagement Products', 1, 0, 0, null, 0, 0, null, 0, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='new.service.anniversary.enabled';

--changeset sivanand:5
--comment  Insert system variable for Nackle Mesh Services Host Base Url dev
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'nackle.mesh.services.base.endpoint.dev'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'nackle.mesh.services.base.endpoint.dev', 'Nackle Mesh Services Host Base Url-DEV', 'Nackle Mesh Services', 5, 0, 0, 'https://dev.api.biw.cloud/v1', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='nackle.mesh.services.base.endpoint.dev';

--changeset sivanand:6
--comment  Insert system variable for Nackle Mesh Services Host Base Url qa
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'nackle.mesh.services.base.endpoint.qa'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'nackle.mesh.services.base.endpoint.qa', 'Nackle Mesh Services Host Base Url-QA', 'Nackle Mesh Services', 5, 0, 0, 'https://qa.api.biw.cloud/v1', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='nackle.mesh.services.base.endpoint.qa';

--changeset sivanand:7
--comment  Insert system variable for Nackle Mesh Services Host Base Url preprod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'nackle.mesh.services.base.endpoint.preprod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'nackle.mesh.services.base.endpoint.preprod', 'Nackle Mesh Services Host Base Url-PPRD', 'Nackle Mesh Services', 5, 0, 0, 'https://pprd.api.biw.cloud/v1', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='nackle.mesh.services.base.endpoint.preprod';

--changeset sivanand:8
--comment  Insert system variable for Nackle Mesh Services Host Base Url prod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'nackle.mesh.services.base.endpoint.prod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'nackle.mesh.services.base.endpoint.prod', 'Nackle Mesh Services Host Base Url-PRD', 'Nackle Mesh Services', 5, 0, 0, 'https://api.biw.cloud/v1', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='nackle.mesh.services.base.endpoint.prod';

--changeset jabeen:9
--comment  Insert system variable for Amazon Kinesis Stream Name
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.kinesis.streamname'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.kinesis.streamname', 'Amazon Kinesis Data Stream name', 'AWS Kinesis', 5, 0, 0, 'core-event-stream', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='aws.kinesis.streamname';

--changeset jabeen:10
--comment  Insert system variable for Amazon Kinesis region
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.kinesis.region'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.kinesis.region', 'Amazon Kinesis region', 'AWS Kinesis', 5, 0, 0, 'us-west-2', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='aws.kinesis.region';

--changeset jabeen:11
 --comment  Insert system variable for JWT Secret for Roster
 --preconditions onFail:MARK_RAN onError:MARK_RAN
 --precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'jwt.secret.roster.dev'
 INSERT INTO os_propertyset
 (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
 VALUES (ENTITY_ID_PK_SQ.nextval, 'jwt.secret.roster.dev', 'JWT Secret for Roster - DEV', 'JWT Web Token', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
 --rollback DELETE FROM os_propertyset WHERE entity_name='jwt.secret.roster.dev';
 
 --changeset jabeen:12
 --comment  Insert system variable for JWT Issuer for Roster
 --preconditions onFail:MARK_RAN onError:MARK_RAN
 --precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'jwt.issuer.roster.dev'
 INSERT INTO os_propertyset
 (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
 VALUES (ENTITY_ID_PK_SQ.nextval, 'jwt.issuer.roster.dev', 'JWT issuer for Roster - DEV', 'JWT Web Token', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
 --rollback DELETE FROM os_propertyset WHERE entity_name='jwt.issuer.roster.dev';
 
  --changeset jabeen:13
  --comment  Insert system variable for JWT Secret for Roster
  --preconditions onFail:MARK_RAN onError:MARK_RAN
  --precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'jwt.secret.roster.qa'
  INSERT INTO os_propertyset
  (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
  VALUES (ENTITY_ID_PK_SQ.nextval, 'jwt.secret.roster.qa', 'JWT Secret for Roster - QA', 'JWT Web Token', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
  --rollback DELETE FROM os_propertyset WHERE entity_name='jwt.secret.roster.qa';
  
  --changeset jabeen:14
  --comment  Insert system variable for JWT Issuer for Roster
  --preconditions onFail:MARK_RAN onError:MARK_RAN
  --precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'jwt.issuer.roster.qa'
  INSERT INTO os_propertyset
  (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
  VALUES (ENTITY_ID_PK_SQ.nextval, 'jwt.issuer.roster.qa', 'JWT issuer for Roster - QA', 'JWT Web Token', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
 --rollback DELETE FROM os_propertyset WHERE entity_name='jwt.issuer.roster.qa';
 
  --changeset jabeen:15
  --comment  Insert system variable for JWT Secret for Roster
  --preconditions onFail:MARK_RAN onError:MARK_RAN
  --precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'jwt.secret.roster.preprod'
  INSERT INTO os_propertyset
  (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
  VALUES (ENTITY_ID_PK_SQ.nextval, 'jwt.secret.roster.preprod', 'JWT Secret for Roster - PPRD', 'JWT Web Token', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
  --rollback DELETE FROM os_propertyset WHERE entity_name='jwt.secret.roster.preprod';
  
  --changeset jabeen:16
  --comment  Insert system variable for JWT Issuer for Roster
  --preconditions onFail:MARK_RAN onError:MARK_RAN
  --precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'jwt.issuer.roster.preprod'
  INSERT INTO os_propertyset
  (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
  VALUES (ENTITY_ID_PK_SQ.nextval, 'jwt.issuer.roster.preprod', 'JWT issuer for Roster - PPRD', 'JWT Web Token', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
 --rollback DELETE FROM os_propertyset WHERE entity_name='jwt.issuer.roster.preprod';
 
  --changeset jabeen:17
  --comment  Insert system variable for JWT Secret for Roster
  --preconditions onFail:MARK_RAN onError:MARK_RAN
  --precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'jwt.secret.roster.prod'
  INSERT INTO os_propertyset
  (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
  VALUES (ENTITY_ID_PK_SQ.nextval, 'jwt.secret.roster.prod', 'JWT Secret for Roster - PRD', 'JWT Web Token', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
  --rollback DELETE FROM os_propertyset WHERE entity_name='jwt.secret.roster.prod';
  
  --changeset jabeen:18
  --comment  Insert system variable for JWT Issuer for Roster
  --preconditions onFail:MARK_RAN onError:MARK_RAN
  --precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'jwt.issuer.roster.prod'
  INSERT INTO os_propertyset
  (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
  VALUES (ENTITY_ID_PK_SQ.nextval, 'jwt.issuer.roster.prod', 'JWT issuer for Roster - PRD', 'JWT Web Token', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
 --rollback DELETE FROM os_propertyset WHERE entity_name='jwt.issuer.roster.prod';
 
--changeset sivanand:19
--comment  Insert system variable for Nackle Mesh Micor Services Credentials Dev
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'nackle.mesh.services.client.id.dev'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'nackle.mesh.services.client.id.dev', 'Nackle Mesh Services Client ID - DEV', 'Nackle Mesh Services', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='nackle.mesh.services.client.id.dev';

--changeset sivanand:20
--comment  Insert system variable for Nackle Mesh Micor Services Credentials Dev
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'nackle.mesh.services.secret.key.dev'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'nackle.mesh.services.secret.key.dev', 'Nackle Mesh Services Secret Key - DEV', 'Nackle Mesh Services', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='nackle.mesh.services.secret.key.dev';

--changeset sivanand:21
--comment  Insert system variable for Nackle Mesh Micor Services Credentials QA
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'nackle.mesh.services.client.id.qa'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'nackle.mesh.services.client.id.qa', 'Nackle Mesh Services Client ID - QA', 'Nackle Mesh Services', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='nackle.mesh.services.client.id.qa';

--changeset sivanand:22
--comment  Insert system variable for Nackle Mesh Micor Services Credentials QA
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'nackle.mesh.services.secret.key.qa'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'nackle.mesh.services.secret.key.qa', 'Nackle Mesh Services Secret Key - QA', 'Nackle Mesh Services', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='nackle.mesh.services.secret.key.qa';

--changeset sivanand:23
--comment  Insert system variable for Nackle Mesh Micor Services Credentials PPRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'nackle.mesh.services.client.id.preprod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'nackle.mesh.services.client.id.preprod', 'Nackle Mesh Services Client ID - PPRD', 'Nackle Mesh Services', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='nackle.mesh.services.client.id.preprod';

--changeset sivanand:24
--comment  Insert system variable for Nackle Mesh Micor Services Credentials PPRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'nackle.mesh.services.secret.key.preprod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'nackle.mesh.services.secret.key.preprod', 'Nackle Mesh Services Secret Key - PPRD', 'Nackle Mesh Services', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='nackle.mesh.services.secret.key.preprod';

--changeset sivanand:25
--comment  Insert system variable for Nackle Mesh Micor Services Credentials PRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'nackle.mesh.services.client.id.prod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'nackle.mesh.services.client.id.prod', 'Nackle Mesh Services Client ID - PRD', 'Nackle Mesh Services', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='nackle.mesh.services.client.id.prod';

--changeset sivanand:26
--comment  Insert system variable for Nackle Mesh Micor Services Credentials PRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'nackle.mesh.services.secret.key.prod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'nackle.mesh.services.secret.key.prod', 'Nackle Mesh Services Secret Key - PRD', 'Nackle Mesh Services', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='nackle.mesh.services.secret.key.prod';

--changeset sivanand:27
--comment  Update Nackle Mesh Micor Services Credentials From The Existing OTS DEV
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.clientid.dev' AND STRING_VAL IS NOT NULL
UPDATE os_propertyset 
SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'ots.api.clientid.dev' ),MODIFIED_BY=65,DATE_MODIFIED=sysdate WHERE entity_name = 'nackle.mesh.services.client.id.dev';
--rollback UPDATE os_propertyset SET string_val = 'CHANGE ME!' WHERE entity_name = 'nackle.mesh.services.client.id.dev';

--changeset sivanand:28
--comment  Update Nackle Mesh Micor Services Credentials From The Existing OTS DEV
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.secretkey.dev' AND STRING_VAL IS NOT NULL
UPDATE os_propertyset 
SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'ots.api.secretkey.dev' ),MODIFIED_BY=65,DATE_MODIFIED=sysdate WHERE entity_name = 'nackle.mesh.services.secret.key.dev';
--rollback UPDATE os_propertyset SET string_val = 'CHANGE ME!' WHERE entity_name = 'nackle.mesh.services.secret.key.dev';

--changeset sivanand:29
--comment  Update Nackle Mesh Micor Services Credentials From The Existing OTS QA
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.clientid.qa' AND STRING_VAL IS NOT NULL
UPDATE os_propertyset 
SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'ots.api.clientid.qa' ),MODIFIED_BY=65,DATE_MODIFIED=sysdate WHERE entity_name = 'nackle.mesh.services.client.id.qa';
--rollback UPDATE os_propertyset SET string_val = 'CHANGE ME!' WHERE entity_name = 'nackle.mesh.services.client.id.qa';

--changeset sivanand:30
--comment  Update Nackle Mesh Micor Services Credentials From The Existing OTS QA
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.secretkey.qa' AND STRING_VAL IS NOT NULL
UPDATE os_propertyset 
SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'ots.api.secretkey.qa' ),MODIFIED_BY=65,DATE_MODIFIED=sysdate WHERE entity_name = 'nackle.mesh.services.secret.key.qa';
--rollback UPDATE os_propertyset SET string_val = 'CHANGE ME!' WHERE entity_name = 'nackle.mesh.services.secret.key.qa';

--changeset sivanand:31
--comment  Update Nackle Mesh Micor Services Credentials From The Existing OTS PPRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.clientid.preprod' AND STRING_VAL IS NOT NULL
UPDATE os_propertyset 
SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'ots.api.clientid.preprod' ),MODIFIED_BY=65,DATE_MODIFIED=sysdate WHERE entity_name = 'nackle.mesh.services.client.id.preprod';
--rollback UPDATE os_propertyset SET string_val = 'CHANGE ME!' WHERE entity_name = 'nackle.mesh.services.client.id.preprod';

--changeset sivanand:32
--comment  Update Nackle Mesh Micor Services Credentials From The Existing OTS PPRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.secretkey.preprod' AND STRING_VAL IS NOT NULL
UPDATE os_propertyset 
SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'ots.api.secretkey.preprod' ),MODIFIED_BY=65,DATE_MODIFIED=sysdate WHERE entity_name = 'nackle.mesh.services.secret.key.preprod';
--rollback UPDATE os_propertyset SET string_val = 'CHANGE ME!' WHERE entity_name = 'nackle.mesh.services.secret.key.preprod';

--changeset sivanand:33
--comment  Update Nackle Mesh Micor Services Credentials From The Existing OTS PRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.clientid.prod' AND STRING_VAL IS NOT NULL
UPDATE os_propertyset 
SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'ots.api.clientid.prod' ),MODIFIED_BY=65,DATE_MODIFIED=sysdate WHERE entity_name = 'nackle.mesh.services.client.id.prod';
--rollback UPDATE os_propertyset SET string_val = 'CHANGE ME!' WHERE entity_name = 'nackle.mesh.services.client.id.prod';

--changeset sivanand:34
--comment  Update Nackle Mesh Micor Services Credentials From The Existing OTS PRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.secretkey.prod' AND STRING_VAL IS NOT NULL
UPDATE os_propertyset 
SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'ots.api.secretkey.prod' ),MODIFIED_BY=65,DATE_MODIFIED=sysdate WHERE entity_name = 'nackle.mesh.services.secret.key.prod';
--rollback UPDATE os_propertyset SET string_val = 'CHANGE ME!' WHERE entity_name = 'nackle.mesh.services.secret.key.prod';

--changeset sivanand:35
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.clientid.dev'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'ots.api.clientid.dev';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.client.id.dev' ) WHERE entity_name = 'ots.api.clientid.dev';

--changeset sivanand:36
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.clientid.qa'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'ots.api.clientid.qa';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.client.id.qa' ) WHERE entity_name = 'ots.api.clientid.qa';

--changeset sivanand:37
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.clientid.preprod'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'ots.api.clientid.preprod';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.client.id.preprod' ) WHERE entity_name = 'ots.api.clientid.preprod';

--changeset sivanand:38
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.clientid.prod'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'ots.api.clientid.prod';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.client.id.prod' ) WHERE entity_name = 'ots.api.clientid.prod';

--changeset sivanand:39
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.secretkey.dev'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'ots.api.secretkey.dev';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.secret.key.dev' ) WHERE entity_name = 'ots.api.secretkey.dev';

--changeset sivanand:40
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.secretkey.qa'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'ots.api.secretkey.qa';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.secret.key.qa' ) WHERE entity_name = 'ots.api.secretkey.qa';

--changeset sivanand:41
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.secretkey.preprod'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'ots.api.secretkey.preprod';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.secret.key.preprod' ) WHERE entity_name = 'ots.api.secretkey.preprod';

--changeset sivanand:42
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.secretkey.prod'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'ots.api.secretkey.prod';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.secret.key.prod' ) WHERE entity_name = 'ots.api.secretkey.prod';

--changeset sivanand:43
--comment Delete OTS service end point - DEV. As We Grouped The End Points Under Nackle Mesh Services Host Url.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.endpoint.url.dev'
DELETE FROM os_propertyset WHERE entity_name='ots.endpoint.url.dev';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.endpoint.url.dev', 'OTS service host url-DEV', 'OTS Service', 5, 0, 0, 'https://dev.api.biw.cloud/v1/ots', 0, 0, null, 1, 1);

--changeset sivanand:44
--comment Delete OTS service end point - QA. As We Grouped The End Points Under Nackle Mesh Services Host Url.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.endpoint.url.qa'
DELETE FROM os_propertyset WHERE entity_name='ots.endpoint.url.qa';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.endpoint.url.qa', 'OTS service host url-QA', 'OTS Service', 5, 0, 0, 'https://qa.api.biw.cloud/v1/ots', 0, 0, null, 1, 1);

--changeset sivanand:45
--comment Delete OTS service end point - PPRD. As We Grouped The End Points Under Nackle Mesh Services Host Url.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.endpoint.url.preprod'
DELETE FROM os_propertyset WHERE entity_name='ots.endpoint.url.preprod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.endpoint.url.preprod', 'OTS service host url-PPRD', 'OTS Service', 5, 0, 0, 'https://pprd.api.biw.cloud/v1/ots', 0, 0, null, 1, 1);

--changeset sivanand:46
--comment Delete OTS service end point - PRD. As We Grouped The End Points Under Nackle Mesh Services Host Url.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.endpoint.url.prod'
DELETE FROM os_propertyset WHERE entity_name='ots.endpoint.url.prod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.endpoint.url.prod', 'OTS service host url-PRD', 'OTS Service', 5, 0, 0, 'https://api.biw.cloud/v1/ots', 0, 0, null, 1, 1);

--changeset sivanand:47
--comment Delete OTS service system variable - DEV. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.clientid.dev'
DELETE FROM os_propertyset WHERE entity_name='ots.api.clientid.dev';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.clientid.dev', 'OTS client id-DEV', 'OTS Service', 5, 0, 0,null, 0, 0, null, 1, 1);

--changeset sivanand:48
--comment Delete OTS service system variable - QA. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.clientid.qa'
DELETE FROM os_propertyset WHERE entity_name='ots.api.clientid.qa';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.clientid.qa', 'OTS client id-QA', 'OTS Service', 5, 0, 0,null, 0, 0, null, 1, 1);

--changeset sivanand:49
--comment Delete OTS service system variable - PPRD. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.clientid.preprod'
DELETE FROM os_propertyset WHERE entity_name='ots.api.clientid.preprod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.clientid.preprod', 'OTS client id-PPRD', 'OTS Service', 5, 0, 0,null, 0, 0, null, 1, 1);

--changeset sivanand:50
--comment Delete OTS service system variable - PPRD. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.clientid.prod'
DELETE FROM os_propertyset WHERE entity_name='ots.api.clientid.prod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.clientid.prod', 'OTS client id-PRD', 'OTS Service', 5, 0, 0,null, 0, 0, null, 1, 1);

--changeset sivanand:51
--comment Delete OTS service system variable - DEV. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.secretkey.dev'
DELETE FROM os_propertyset WHERE entity_name='ots.api.secretkey.dev';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.secretkey.dev', 'OTS secret key-DEV', 'OTS Service', 5, 0, 0, null, 0, 0, null, 1, 1);

--changeset sivanand:52
--comment Delete OTS service system variable - QA. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.secretkey.qa'
DELETE FROM os_propertyset WHERE entity_name='ots.api.secretkey.qa';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.secretkey.qa', 'OTS secret key-QA', 'OTS Service', 5, 0, 0, null, 0, 0, null, 1, 1);

--changeset sivanand:53
--comment Delete OTS service system variable - PPRD. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.secretkey.preprod'
DELETE FROM os_propertyset WHERE entity_name='ots.api.secretkey.preprod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.secretkey.preprod', 'OTS secret key-PPRD', 'OTS Service', 5, 0, 0, null, 0, 0, null, 1, 1);

--changeset sivanand:54
--comment Delete OTS service system variable - PRD. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'ots.api.secretkey.prod'
DELETE FROM os_propertyset WHERE entity_name='ots.api.secretkey.prod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'ots.api.secretkey.prod', 'OTS secret key-PRD', 'OTS Service', 5, 0, 0, null, 0, 0, null, 1, 1);

--changeset sivanand:55
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.clientid.dev'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'mtc.api.clientid.dev';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.client.id.dev' ) WHERE entity_name = 'mtc.api.clientid.dev';

--changeset sivanand:56
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.clientid.qa'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'mtc.api.clientid.qa';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.client.id.qa' ) WHERE entity_name = 'mtc.api.clientid.qa';

--changeset sivanand:57
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.clientid.preprod'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'mtc.api.clientid.preprod';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.client.id.preprod' ) WHERE entity_name = 'mtc.api.clientid.preprod';

--changeset sivanand:58
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.clientid.prod'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'mtc.api.clientid.prod';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.client.id.prod' ) WHERE entity_name = 'mtc.api.clientid.prod';

--changeset sivanand:59
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.secretkey.dev'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'mtc.api.secretkey.dev';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.secret.key.dev' ) WHERE entity_name = 'mtc.api.secretkey.dev';

--changeset sivanand:60
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.secretkey.qa'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'mtc.api.secretkey.qa';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.secret.key.qa' ) WHERE entity_name = 'mtc.api.secretkey.qa';

--changeset sivanand:61
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.secretkey.preprod'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'mtc.api.secretkey.preprod';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.secret.key.preprod' ) WHERE entity_name = 'mtc.api.secretkey.preprod';

--changeset sivanand:62
--comment Dummy execution for proper rollback of DB version control.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.secretkey.prod'
UPDATE os_propertyset SET modified_by = 65,DATE_MODIFIED=sysdate WHERE entity_name = 'mtc.api.secretkey.prod';
--rollback UPDATE os_propertyset SET string_val = ( SELECT string_val FROM os_propertyset WHERE entity_name = 'nackle.mesh.services.secret.key.prod' ) WHERE entity_name = 'mtc.api.secretkey.prod';

--changeset sivanand:63
--comment Delete MTC service system variable - DEV. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.endpoint.url.dev'
DELETE FROM os_propertyset WHERE entity_name='mtc.endpoint.url.dev';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.endpoint.url.dev', 'Media Transcoder service host url-DEV', 'Media Transcoder Service', 5, 0, 0, 'https://dev.api.biw.cloud/v1/mtc-digest', 0, 0, null, 1, 1);

--changeset sivanand:64
--comment Delete MTC service system variable - QA. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.endpoint.url.qa'
DELETE FROM os_propertyset WHERE entity_name='mtc.endpoint.url.qa';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.endpoint.url.qa', 'Media Transcoder service host url-QA', 'Media Transcoder Service', 5, 0, 0, 'https://qa.api.biw.cloud/v1/mtc-digest', 0, 0, null, 1, 1);

--changeset sivanand:65
--comment Delete MTC service system variable - PPRD. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.endpoint.url.preprod'
DELETE FROM os_propertyset WHERE entity_name='mtc.endpoint.url.preprod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.endpoint.url.preprod', 'Media Transcoder service host url-PPRD', 'Media Transcoder Service', 5, 0, 0, 'https://pprd.api.biw.cloud/v1/mtc-digest', 0, 0, null, 1, 1);

--changeset sivanand:66
--comment Delete MTC service system variable - PRD. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.endpoint.url.prod'
DELETE FROM os_propertyset WHERE entity_name='mtc.endpoint.url.prod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.endpoint.url.prod', 'Media Transcoder service host url-PRD', 'Media Transcoder Service', 5, 0, 0, 'https://api.biw.cloud/v1/mtc-digest', 0, 0, null, 1, 1);


--changeset sivanand:67
--comment Delete MTC service system variable - DEV. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.clientid.dev'
DELETE FROM os_propertyset WHERE entity_name='mtc.api.clientid.dev';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.clientid.dev', 'Media Transcoder client id-DEV', 'Media Transcoder Service', 5, 0, 0,null, 0, 0, null, 1, 1);


--changeset sivanand:68
--comment Delete MTC service system variable - QA. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.clientid.qa'
DELETE FROM os_propertyset WHERE entity_name='mtc.api.clientid.qa';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.clientid.qa', 'Media Transcoder client id-QA', 'Media Transcoder Service', 5, 0, 0,null, 0, 0, null, 1, 1);

--changeset sivanand:69
--comment Delete MTC service system variable - PPRD. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.clientid.preprod'
DELETE FROM os_propertyset WHERE entity_name='mtc.api.clientid.preprod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.clientid.preprod', 'Media Transcoder client id-PPRD', 'Media Transcoder Service', 5, 0, 0,null, 0, 0, null, 1, 1);

--changeset sivanand:70
--comment Delete MTC service system variable - PRD. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.clientid.prod'
DELETE FROM os_propertyset WHERE entity_name='mtc.api.clientid.prod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.clientid.prod', 'Media Transcoder client id-PRD', 'Media Transcoder Service', 5, 0, 0,null, 0, 0, null, 1, 1);

--changeset sivanand:71
--comment Delete MTC service system variable - DEV. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.secretkey.dev'
DELETE FROM os_propertyset WHERE entity_name='mtc.api.secretkey.dev';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.secretkey.dev', 'Media Transcoder secret key-DEV', 'Media Transcoder Service', 5, 0, 0, null, 0, 0, null, 1, 1);

--changeset sivanand:72
--comment Delete MTC service system variable - QA. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.secretkey.qa'
DELETE FROM os_propertyset WHERE entity_name='mtc.api.secretkey.qa';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.secretkey.qa', 'Media Transcoder secret key-QA', 'Media Transcoder Service', 5, 0, 0, null, 0, 0, null, 1, 1);

--changeset sivanand:73
--comment Delete MTC service system variable - PPRD. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.secretkey.preprod'
DELETE FROM os_propertyset WHERE entity_name='mtc.api.secretkey.preprod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.secretkey.preprod', 'Media Transcoder secret key-PPRD', 'Media Transcoder Service', 5, 0, 0, null, 0, 0, null, 1, 1);

--changeset sivanand:74
--comment Delete MTC service system variable - PRD. As We Grouped The System Variable Under Nackle Mesh Services.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.secretkey.prod'
DELETE FROM os_propertyset WHERE entity_name='mtc.api.secretkey.prod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE) VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.secretkey.prod', 'Media Transcoder secret key-PRD', 'Media Transcoder Service', 5, 0, 0, null, 0, 0, null, 1, 1);

--changeset palaniss:75
--comment Update email footer for donot reply change 
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'email.wrapper.footer'
UPDATE OS_PROPERTYSET
   SET STRING_VAL = '<!'||CHR(45)||CHR(45)||' CONTENT ENDS HERE '||CHR(45)||CHR(45)||'></td></tr></tbody></table></td></tr><tr><td height="10"></td></tr><tr><td style="background-color:#e0e0e0;"></td> <td> <table cellpadding="10" style="background-color:#e0e0e0; width:100%"> <tbody> <tr> <td> <p style="text-align:left; margin:0;padding-top:20px;padding-bottom:20px;">{0} </p> </td> </tr> </tbody> </table> </td> <td style="background-color:#e0e0e0;"></td> </tr></tbody></table><!'||CHR(45)||CHR(45)||'[if (gte mso 9)|(IE)]> </td> </tr> </table> <![endif]'||CHR(45)||CHR(45)||'></div></td></tr><tr><td height="50"></td></tr></tbody></table><!'||CHR(45)||CHR(45)||' End of wrapper table '||CHR(45)||CHR(45)||'></body></html>',MODIFIED_BY=65,DATE_MODIFIED=sysdate
WHERE ENTITY_NAME = 'email.wrapper.footer';
--rollback UPDATE OS_PROPERTYSET SET STRING_VAL = '<!'||CHR(45)||CHR(45)||' CONTENT ENDS HERE '||CHR(45)||CHR(45)||'></td></tr></tbody></table></td></tr><tr><td height="10"></td></tr></tbody></table><!'||CHR(45)||CHR(45)||'[if (gte mso 9)|(IE)]> </td> </tr> </table> <![endif]'||CHR(45)||CHR(45)||'></div></td></tr><tr><td height="50"></td></tr></tbody></table><!'||CHR(45)||CHR(45)||' End of wrapper table '||CHR(45)||CHR(45)||'></body></html>' WHERE ENTITY_NAME = 'email.wrapper.footer';
 
--changeset jabeen:76
--comment  Insert system variable for Amazon Account
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.kinesis.account.no'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.kinesis.account.no', 'AWS Kinesis account number', 'AWS Kinesis', 5, 0, 0, '408804306756', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='aws.kinesis.account.no';

--changeset sivanand:77
--comment Deleting the key, as the credentials will be taken from instance profile.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.accessKey.dev'
DELETE FROM OS_PROPERTYSET WHERE ENTITY_NAME='aws.accessKey.dev';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY) VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.accessKey.dev', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);

--changeset sivanand:78
--comment Deleting the key, as the credentials will be taken from instance profile.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.accessKey.qa'
DELETE FROM OS_PROPERTYSET WHERE ENTITY_NAME='aws.accessKey.qa';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY) VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.accessKey.qa', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);

--changeset sivanand:79
--comment Deleting the key, as the credentials will be taken from instance profile.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.accessKey.preprod'
DELETE FROM OS_PROPERTYSET WHERE ENTITY_NAME='aws.accessKey.preprod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY) VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.accessKey.preprod', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);

--changeset sivanand:80
--comment Deleting the key, as the credentials will be taken from instance profile.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.accessKey.prod'
DELETE FROM OS_PROPERTYSET WHERE ENTITY_NAME='aws.accessKey.prod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY) VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.accessKey.prod', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);

--changeset sivanand:81
--comment Deleting the key, as the credentials will be taken from instance profile.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.secretKey.dev'
DELETE FROM OS_PROPERTYSET WHERE ENTITY_NAME='aws.secretKey.dev';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY) VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.secretKey.dev', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);

--changeset sivanand:82
--comment Deleting the key, as the credentials will be taken from instance profile.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.secretKey.qa'
DELETE FROM OS_PROPERTYSET WHERE ENTITY_NAME='aws.secretKey.qa';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY) VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.secretKey.qa', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);

--changeset sivanand:83
--comment Deleting the key, as the credentials will be taken from instance profile.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.secretKey.preprod'
DELETE FROM OS_PROPERTYSET WHERE ENTITY_NAME='aws.secretKey.preprod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY) VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.secretKey.preprod', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);

--changeset sivanand:84
--comment Deleting the key, as the credentials will be taken from instance profile.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.secretKey.prod'
DELETE FROM OS_PROPERTYSET WHERE ENTITY_NAME='aws.secretKey.prod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY) VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.secretKey.prod', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate,6.4);

--changeset sivanand:85
--comment Deleting the key, as the credentials will be taken from instance profile.
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.s3.image.prefix'
DELETE FROM OS_PROPERTYSET WHERE ENTITY_NAME='aws.s3.image.prefix';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED) VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.s3.image.prefix', 'AWS S3 prefix key for file upload Mirrors cm3dam url in BIWS', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 1,sysdate);

--changeset sivanand:86
--comment  Deleting aws avatar bucketname for dev
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.avatar.bucketname.dev'
DELETE FROM OS_PROPERTYSET WHERE ENTITY_NAME='aws.avatar.bucketname.dev';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY) VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.avatar.bucketname.dev', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 						1,sysdate,6.4);

--changeset sivanand:87
--comment  Deleting aws avatar bucketname for qa
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.avatar.bucketname.qa'
DELETE FROM OS_PROPERTYSET WHERE ENTITY_NAME='aws.avatar.bucketname.qa';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY) VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.avatar.bucketname.qa', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 						1,sysdate,6.4);

--changeset sivanand:88
--comment  Deleting aws avatar bucketname for preprod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.avatar.bucketname.preprod'
DELETE FROM OS_PROPERTYSET WHERE ENTITY_NAME='aws.avatar.bucketname.preprod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY) VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.avatar.bucketname.preprod', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 						1,sysdate,6.4);

--changeset sivanand:89
--comment  Deleting aws avatar bucketname for prod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.avatar.bucketname.prod'
DELETE FROM OS_PROPERTYSET WHERE ENTITY_NAME='aws.avatar.bucketname.prod';
--rollback INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,DATE_CREATED,CREATED_BY) VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.avatar.bucketname.prod', 'AWS Access Key for the S3 instance', 'AWS', 5, 0, 0, 'CHANGE ME', 0, 0, null, 1, 						1,sysdate,6.4);

--changeset esakkimu:90
--comment  URL to PDF generation service String Value changes QA
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'pdf.service.url.qa'
UPDATE os_propertyset 
SET string_val = 'https://qa.api.biw.cloud/v1/utility/pdf',MODIFIED_BY=65,DATE_MODIFIED=sysdate WHERE entity_name = 'pdf.service.url.qa';
--rollback UPDATE os_propertyset SET string_val = 'CHANGE ME!' WHERE entity_name = 'pdf.service.url.qa';

--changeset esakkimu:91
--comment  URL to PDF generation service String Value changes preprod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'pdf.service.url.preprod'
UPDATE os_propertyset 
SET string_val = 'https://pprd.api.biw.cloud/v1/utility/pdf',MODIFIED_BY=65,DATE_MODIFIED=sysdate WHERE entity_name = 'pdf.service.url.preprod';
--rollback UPDATE os_propertyset SET string_val = 'CHANGE ME!' WHERE entity_name = 'pdf.service.url.preprod';

--changeset esakkimu:92
--comment  URL to PDF generation service String Value changes prod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'pdf.service.url.prod'
UPDATE os_propertyset 
SET string_val = 'https://api.biw.cloud/v1/utility/pdf',MODIFIED_BY=65,DATE_MODIFIED=sysdate WHERE entity_name = 'pdf.service.url.prod';
--rollback UPDATE os_propertyset SET string_val = 'CHANGE ME!' WHERE entity_name = 'pdf.service.url.prod';

--changeset esakkimu:93
--comment insert system variable for AWS clients controlled by ORACLE
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'aws.deployed'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.deployed', 'If set to true then it is deployed in AWS', 'AWS', 1, 1, 0, null, 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='aws.deployed';

--changeset sivanand:94
--comment added column Modified the column width
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='OS_PROPERTYSET' and COLUMN_NAME ='ENTITY_KEY'
ALTER TABLE OS_PROPERTYSET MODIFY ENTITY_KEY VARCHAR2(100);
--rollback not required

--changeset esakkimu:95
--comment insert system variable for Auto Budget transfer
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'budget.auto.transfer'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'budget.auto.transfer', 'When a node is moved or deleted transfer budget automatically to parent node', 'Functionality', 1, 0, 0, null, 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='budget.auto.transfer';

--changeset sivanand:96
--comment Adding audit trail columns like modified by and Modified Date Time
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:4 select COUNT(1) from OS_PROPERTYSET WHERE entity_name in ('email.wrapper.footer','pdf.service.url.qa','pdf.service.url.preprod','pdf.service.url.prod')
UPDATE os_propertyset SET modified_by = 65,date_modified = sysdate WHERE entity_name in ('email.wrapper.footer','pdf.service.url.qa','pdf.service.url.preprod','pdf.service.url.prod');
--rollback not required

--changeset esakkimu:97
--comment insert system variable for Public Info On the Preference page should be controlled
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'show.my.info.to.public'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'show.my.info.to.public', 'Show My Info To Public', 'Functionality', 1, 0, 0, null, 0, 0, null, 1, 1, 65,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='show.my.info.to.public';

--changeset elizabet:98
--comment change boolean value from true to false 
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT boolean_val FROM os_propertyset WHERE entity_name='fileload.userrole.delete'
update os_propertyset set boolean_val=0,modified_by = 65,date_modified = sysdate where entity_name='fileload.userrole.delete';
--rollback UPDATE os_propertyset SET boolean_val=1 where entity_name='fileload.userrole.delete';

--changeset esakkimu:99
--comment added column Modified the column width
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 select COUNT(1) from USER_TAB_COLUMNS where TABLE_NAME='OS_PROPERTYSET' and COLUMN_NAME ='ENTITY_KEY'
ALTER TABLE OS_PROPERTYSET MODIFY ENTITY_KEY VARCHAR2(1000);
--rollback not required

--changeset esakkimu:100
--comment  Insert system variable for CSRF excluded urls
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'exclude.csrf.urls'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'exclude.csrf.urls', 'Comma separated list of excluded url paths. Do not place a full url path, instead just place an end path like "login.do" or an interim path like "mobileapp".', 'system', 5, 0, 0, 'mobileapp,purlRecipientLoadActivity.do,seamlessLogonTest,clientSeamlessLogonTest', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='exclude.csrf.urls';