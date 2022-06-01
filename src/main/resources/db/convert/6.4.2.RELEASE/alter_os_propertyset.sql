--liquibase formatted sql

--changeset subramap:1
--comment  Insert system variable for Media Transcoder service end point dev
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.endpoint.url.dev'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.endpoint.url.dev', 'Media Transcoder service host url-DEV', 'Media Transcoder Service', 5, 0, 0, 'https://dev.api.biw.cloud/v1/mtc-digest', 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='mtc.endpoint.url.dev';

--changeset subramap:2
--comment  Insert system variable for Media Transcoder service end point qa
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.endpoint.url.qa'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.endpoint.url.qa', 'Media Transcoder service host url-QA', 'Media Transcoder Service', 5, 0, 0, 'https://qa.api.biw.cloud/v1/mtc-digest', 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='mtc.endpoint.url.qa';

--changeset subramap:3
--comment  Insert system variable for Media Transcoder service end point preprod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.endpoint.url.preprod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.endpoint.url.preprod', 'Media Transcoder service host url-PPRD', 'Media Transcoder Service', 5, 0, 0, 'https://pprd.api.biw.cloud/v1/mtc-digest', 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='mtc.endpoint.url.preprod';

--changeset subramap:4
--comment  Insert system variable for Media Transcoder service end point prod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.endpoint.url.prod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.endpoint.url.prod', 'Media Transcoder service host url-PRD', 'Media Transcoder Service', 5, 0, 0, 'https://api.biw.cloud/v1/mtc-digest', 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='mtc.endpoint.url.prod';

--changeset subramap:5
--comment  Insert system variable for Media Transcoder service end client id dev
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.clientid.dev'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.clientid.dev', 'Media Transcoder client id-DEV', 'Media Transcoder Service', 5, 0, 0,null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='mtc.api.clientid.dev';

--changeset subramap:6
--comment  Insert system variable for Media Transcoder service end client id qa
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.clientid.qa'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.clientid.qa', 'Media Transcoder client id-QA', 'Media Transcoder Service', 5, 0, 0, null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='mtc.api.clientid.qa';

--changeset subramap:7
--comment  Insert system variable for Media Transcoder service end client id preprod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.clientid.preprod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.clientid.preprod', 'Media Transcoder client id-PPRD', 'Media Transcoder Service', 5, 0, 0, null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='mtc.api.clientid.preprod';

--changeset subramap:8
--comment  Insert system variable for Media Transcoder service end client id prod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.clientid.prod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.clientid.prod', 'Media Transcoder client id-PRD', 'Media Transcoder Service', 5, 0, 0, null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='mtc.api.clientid.prod';

--changeset subramap:9
--comment  Insert system variable for Media Transcoder service secretkey dev
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.secretkey.dev'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.secretkey.dev', 'Media Transcoder secret key-DEV', 'Media Transcoder Service', 5, 0, 0, null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='mtc.api.secretkey.dev';

--changeset subramap:10
--comment  Insert system variable for Media Transcoder service secretkey qa
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.secretkey.qa'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.secretkey.qa', 'Media Transcoder secret key-QA', 'Media Transcoder Service', 5, 0, 0, null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='mtc.api.secretkey.qa';

--changeset subramap:11
--comment  Insert system variable for Media Transcoder service secretkey preprod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.secretkey.preprod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.secretkey.preprod', 'Media Transcoder secret key-PPRD', 'Media Transcoder Service', 5, 0, 0, null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='mtc.api.secretkey.preprod';

--changeset subramap:12
--comment  Insert system variable for Media Transcoder service secretkey prod
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'mtc.api.secretkey.prod'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
VALUES (ENTITY_ID_PK_SQ.nextval, 'mtc.api.secretkey.prod', 'Media Transcoder secret key-PRD', 'Media Transcoder Service', 5, 0, 0, null, 0, 0, null, 1, 1);
--rollback DELETE FROM os_propertyset WHERE entity_name='mtc.api.secretkey.prod';

--changeset subramap:13
--comment Updating the end point url
update os_propertyset
set STRING_VAL='https://dev.api.biw.cloud/v1/mtc-ingest'
WHERE entity_name='mtc.endpoint.url.dev';
--rollback not required

--changeset subramap:14
--comment Updating the end point url
update os_propertyset
set STRING_VAL='https://qa.api.biw.cloud/v1/mtc-ingest'
WHERE entity_name='mtc.endpoint.url.qa';
--rollback not required

--changeset subramap:15
--comment Updating the end point url
update os_propertyset
set STRING_VAL='https://pprd.api.biw.cloud/v1/mtc-ingest'
WHERE entity_name='mtc.endpoint.url.preprod';
--rollback not required

--changeset subramap:16
--comment Updating the end point url
update os_propertyset
set STRING_VAL='https://api.biw.cloud/v1/mtc-ingest'
WHERE entity_name='mtc.endpoint.url.prod';
--rollback not required

--changeset palaniss:18
--comment insert system variable for Billboard resizing
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'Billboard.Size'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY)
VALUES (ENTITY_ID_PK_SQ.nextval, 'Billboard.Size', 'Billboard Size option.Allowed values are Small,Medium,Large', 'UI Size options', 5, 0, 0, 'large', 0, 0, null, 1, 1, 6.42);
--rollback DELETE FROM os_propertyset WHERE entity_name='Billboard.Size';

--changeset kothanda:28
--comment insert system variable for ES AWS Region
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'autocomplete.es.aws.region'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY)
VALUES (ENTITY_ID_PK_SQ.nextval, 'autocomplete.es.aws.region', 'ES AWS Region', 'Elastic Search', 5, 0, 0, 'changeme', 0, 0, null, 1, 1, 6.42);
--rollback DELETE FROM os_propertyset WHERE entity_name='autocomplete.es.aws.region';

--changeset kothanda:29
--comment update ES end point variable to change me value
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'autocomplete.es.url.dev' and STRING_VAL = 'https://550fe1f624f682311a1435834aea1d96.us-east-1.aws.found.io:9243'
UPDATE os_propertyset SET string_val = 'changeme' where entity_name = 'autocomplete.es.url.dev';
--rollback UPDATE os_propertyset SET string_val = 'https://550fe1f624f682311a1435834aea1d96.us-east-1.aws.found.io:9243' where entity_name = 'autocomplete.es.url.dev';

--changeset kothanda:30
--comment update ES end point variable to change me value
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'autocomplete.es.url.qa' and STRING_VAL = 'https://550fe1f624f682311a1435834aea1d96.us-east-1.aws.found.io:9243'
UPDATE os_propertyset SET string_val = 'changeme' where entity_name = 'autocomplete.es.url.qa';
--rollback UPDATE os_propertyset SET string_val = 'https://550fe1f624f682311a1435834aea1d96.us-east-1.aws.found.io:9243' where entity_name = 'autocomplete.es.url.qa';

--changeset kothanda:31
--comment update ES end point variable to change me value
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'autocomplete.es.url.preprod' and STRING_VAL = 'https://bd04c7414a4fde7aeb29fbc38d100ba2.us-east-1.aws.found.io:9243'
UPDATE os_propertyset SET string_val = 'changeme' where entity_name = 'autocomplete.es.url.preprod';
--rollback UPDATE os_propertyset SET string_val = 'https://bd04c7414a4fde7aeb29fbc38d100ba2.us-east-1.aws.found.io:9243' where entity_name = 'autocomplete.es.url.preprod';

--changeset kothanda:32
--comment update ES end point variable to change me value
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'autocomplete.es.url.prod' and STRING_VAL = 'https://2f888c19a421941567c0e2abd642b7b8.us-east-1.aws.found.io:9243'
UPDATE os_propertyset SET string_val = 'changeme' where entity_name = 'autocomplete.es.url.prod';
--rollback UPDATE os_propertyset SET string_val = 'https://2f888c19a421941567c0e2abd642b7b8.us-east-1.aws.found.io:9243' where entity_name = 'autocomplete.es.url.prod';

--changeset kothanda:33
--comment update ES user name to change me value
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'elasticsearch.creds.password.dev' and STRING_VAL = 'biwpassword'
UPDATE os_propertyset SET string_val = 'changeme' where entity_name = 'elasticsearch.creds.password.dev';
--rollback UPDATE os_propertyset SET string_val = 'biwpassword' where entity_name = 'elasticsearch.creds.password.dev';

--changeset kothanda:34
--comment update ES user name to change me value
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'elasticsearch.creds.password.qa' and STRING_VAL = 'biwpassword'
UPDATE os_propertyset SET string_val = 'changeme' where entity_name = 'elasticsearch.creds.password.qa';
--rollback UPDATE os_propertyset SET string_val = 'biwpassword' where entity_name = 'elasticsearch.creds.password.qa';

--changeset kothanda:35
--comment update ES user name to change me value
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'elasticsearch.creds.username.dev' and STRING_VAL = 'admin'
UPDATE os_propertyset SET string_val = 'changeme' where entity_name = 'elasticsearch.creds.username.dev';
--rollback UPDATE os_propertyset SET string_val = 'admin' where entity_name = 'elasticsearch.creds.username.dev';

--changeset kothanda:36
--comment update ES user name to change me value
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'elasticsearch.creds.username.qa' and STRING_VAL = 'admin'
UPDATE os_propertyset SET string_val = 'changeme' where entity_name = 'elasticsearch.creds.username.qa';
--rollback UPDATE os_propertyset SET string_val = 'admin' where entity_name = 'elasticsearch.creds.username.qa';

--changeset kothanda:37
--comment remove proxy variables for auto complete feature from sys variables
delete from os_propertyset where entity_name like 'autocomplete.es.proxy%';
--rollback not required

--changeset subramap:38
update os_propertyset 
set created_by=6.42 where group_name='Media Transcoder Service';
--rollback not required

--changeset gorantla:39
--comment  Insert system variable history for Media Transcoder service end point dev
Insert into os_propertyset_history (OS_PROPERTYSET_HISTORY_ID, ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE,BOOLEAN_VAL, DOUBLE_VAL, STRING_VAL,LONG_VAL, INT_VAL, IS_EDITABLE,IS_VIEWABLE, GROUP_NAME, DATE_CREATED, CREATED_BY)
 Values (os_propertyset_tracking_sq.nextval, -1, 'mtc.endpoint.url.dev', 'Media Transcoder service host url-DEV', 5,0, 0, 'https://dev.api.biw.cloud/v1/mtc-digest',0, 0, 1,1, 'Media Transcoder Service', sysdate, 5662);
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.endpoint.url.dev');  

--changeset gorantla:40
--comment  Insert system variable history for Media Transcoder service end point qa   
Insert into os_propertyset_history (OS_PROPERTYSET_HISTORY_ID, ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE,BOOLEAN_VAL, DOUBLE_VAL,STRING_VAL, LONG_VAL, INT_VAL, IS_EDITABLE,IS_VIEWABLE, GROUP_NAME, DATE_CREATED, CREATED_BY)
 Values (os_propertyset_tracking_sq.nextval, -1, 'mtc.endpoint.url.qa', 'Media Transcoder service host url-QA', 5,0, 0, 'https://qa.api.biw.cloud/v1/mtc-digest',0, 0, 1,1, 'Media Transcoder Service', sysdate, 5662);  
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.endpoint.url.qa');      
    
--changeset gorantla:41
--comment  Insert system variable history for Media Transcoder service end point pre    
Insert into os_propertyset_history (OS_PROPERTYSET_HISTORY_ID, ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE,BOOLEAN_VAL, DOUBLE_VAL,STRING_VAL, LONG_VAL, INT_VAL, IS_EDITABLE,IS_VIEWABLE, GROUP_NAME, DATE_CREATED, CREATED_BY)
 Values (os_propertyset_tracking_sq.nextval, -1, 'mtc.endpoint.url.preprod', 'Media Transcoder service host url-PPRD', 5,0, 0,'https://pprd.api.biw.cloud/v1/mtc-digest', 0, 0, 1,1, 'Media Transcoder Service', sysdate, 5662);  
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.endpoint.url.preprod');    
    
--changeset gorantla:42
--comment  Insert system variable history for Media Transcoder service end point prd   
Insert into os_propertyset_history (OS_PROPERTYSET_HISTORY_ID, ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE,BOOLEAN_VAL, DOUBLE_VAL,STRING_VAL, LONG_VAL, INT_VAL, IS_EDITABLE,IS_VIEWABLE, GROUP_NAME, DATE_CREATED, CREATED_BY)
 Values (os_propertyset_tracking_sq.nextval, -1, 'mtc.endpoint.url.prod', 'Media Transcoder service host url-PRD', 5,0, 0,'https://api.biw.cloud/v1/mtc-digest', 0, 0, 1,1, 'Media Transcoder Service', sysdate, 5662); 
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.endpoint.url.prod');    

--changeset gorantla:43
--comment  Insert system variable history for ES end point dev   	
Insert into os_propertyset_history (OS_PROPERTYSET_HISTORY_ID, ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE,BOOLEAN_VAL, DOUBLE_VAL,STRING_VAL, LONG_VAL, INT_VAL, IS_EDITABLE,IS_VIEWABLE, GROUP_NAME, DATE_CREATED, CREATED_BY)
 Values (os_propertyset_tracking_sq.nextval, -1, 'autocomplete.es.url.dev', 'Elasticsearch host url-DEV', 5,0, 0,'https://550fe1f624f682311a1435834aea1d96.us-east-1.aws.found.io:9243', 0, 0, 1,1, null, sysdate, 5662);	
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'autocomplete.es.url.dev');    
	
--changeset gorantla:44
--comment  Insert system variable history for ES end point qa
Insert into os_propertyset_history (OS_PROPERTYSET_HISTORY_ID, ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE,BOOLEAN_VAL, DOUBLE_VAL,STRING_VAL, LONG_VAL, INT_VAL, IS_EDITABLE,IS_VIEWABLE, GROUP_NAME, DATE_CREATED, CREATED_BY)
 Values (os_propertyset_tracking_sq.nextval, -1, 'autocomplete.es.url.qa', 'Elasticsearch host url-QA', 5,0, 0,'https://550fe1f624f682311a1435834aea1d96.us-east-1.aws.found.io:9243', 0, 0, 1,1, null, sysdate, 5662);
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'autocomplete.es.url.qa');	
	
--changeset gorantla:45
--comment  Insert system variable history for ES end point pre
Insert into os_propertyset_history (OS_PROPERTYSET_HISTORY_ID, ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE,BOOLEAN_VAL, DOUBLE_VAL,STRING_VAL, LONG_VAL, INT_VAL, IS_EDITABLE,IS_VIEWABLE, GROUP_NAME, DATE_CREATED, CREATED_BY)
 Values (os_propertyset_tracking_sq.nextval, -1, 'autocomplete.es.url.preprod', 'Elasticsearch host url-PPRD', 5,0, 0,'https://bd04c7414a4fde7aeb29fbc38d100ba2.us-east-1.aws.found.io:9243', 0, 0, 1,1, null, sysdate, 5662);	
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'autocomplete.es.url.preprod');    
	
--changeset gorantla:46
--comment  Insert system variable history for ES end point prd
Insert into os_propertyset_history (OS_PROPERTYSET_HISTORY_ID, ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE,BOOLEAN_VAL, DOUBLE_VAL,STRING_VAL, LONG_VAL, INT_VAL, IS_EDITABLE,IS_VIEWABLE, GROUP_NAME, DATE_CREATED, CREATED_BY)
 Values (os_propertyset_tracking_sq.nextval, -1, 'autocomplete.es.url.prd', 'Elasticsearch host url-PRD', 5,0, 0,'https://2f888c19a421941567c0e2abd642b7b8.us-east-1.aws.found.io:9243', 0, 0, 1,1, null, sysdate, 5662);
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'autocomplete.es.url.prd');
    		
--changeset gorantla:47
--comment  Insert system variable history for ES biwpassword dev	
Insert into os_propertyset_history (OS_PROPERTYSET_HISTORY_ID, ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE, BOOLEAN_VAL, DOUBLE_VAL,STRING_VAL, LONG_VAL, INT_VAL, IS_EDITABLE,IS_VIEWABLE, GROUP_NAME, DATE_CREATED, CREATED_BY)
 Values (os_propertyset_tracking_sq.nextval, -1, 'elasticsearch.creds.password.dev', 'ElasticSearch Password DEV', 5, 0, 0,'biwpassword', 0, 0, 1,1, null, sysdate, 5662);
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'elasticsearch.creds.password.dev');    
	
--changeset gorantla:48
--comment  Insert system variable history for ES biwpassword qa    
Insert into os_propertyset_history (OS_PROPERTYSET_HISTORY_ID, ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE,BOOLEAN_VAL, DOUBLE_VAL,STRING_VAL, LONG_VAL, INT_VAL, IS_EDITABLE,IS_VIEWABLE, GROUP_NAME, DATE_CREATED, CREATED_BY)
 Values (os_propertyset_tracking_sq.nextval, -1, 'elasticsearch.creds.password.qa', 'ElasticSearch Password QA', 5,0, 0,'biwpassword', 0, 0, 1,1, null, sysdate, 5662);
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'elasticsearch.creds.password.qa');    

--changeset gorantla:49
--comment  Insert system variable history for ES user name dev	
Insert into os_propertyset_history (OS_PROPERTYSET_HISTORY_ID, ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE,BOOLEAN_VAL, DOUBLE_VAL,STRING_VAL, LONG_VAL, INT_VAL, IS_EDITABLE,IS_VIEWABLE, GROUP_NAME, DATE_CREATED, CREATED_BY)
 Values (os_propertyset_tracking_sq.nextval, -1, 'elasticsearch.creds.username.dev', 'ElasticSearch username DEV', 5,0, 0,'admin', 0, 0, 1,1, null, sysdate, 5662);	
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'elasticsearch.creds.username.dev');    
	
--changeset gorantla:50
--comment  Insert system variable history for ES user name qa    
Insert into os_propertyset_history (OS_PROPERTYSET_HISTORY_ID, ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE,BOOLEAN_VAL, DOUBLE_VAL,STRING_VAL, LONG_VAL, INT_VAL, IS_EDITABLE,IS_VIEWABLE, GROUP_NAME, DATE_CREATED, CREATED_BY)
 Values (os_propertyset_tracking_sq.nextval, -1, 'elasticsearch.creds.username.qa', 'ElasticSearch username QA', 5,0, 0,'admin', 0, 0, 1,1, null, sysdate, 5662);		
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'elasticsearch.creds.username.qa');