INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.url.dev','URL shortner URL-DEV',5,0,0,'http://recog.co/yourls-api.php',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.url.qa','URL shortner URL-QA',5,0,0,'http://recog.co/yourls-api.php',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.url.preprod','URL shortner URL-Preprod',5,0,0,'http://recog.co/yourls-api.php',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.url.prod','URL shortner URL-Prod',5,0,0,'http://recog.co/yourls-api.php',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.signature.dev','URL shortner signature URL-DEV',5,0,0,'0b9f20a71a',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.signature.qa','URL shortner signature URL-QA',5,0,0,'0b9f20a71a',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.signature.preprod','URL shortner signature URL-Preprod',5,0,0,'0b9f20a71a',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.signature.prod','URL shortner signature URL-Prod',5,0,0,'0b9f20a71a',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'firstTimeLogin.required.email','First Time Login Page',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'large.audience.report.generation.enabled','Use Large Audience Asynchronous Report Generateion',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'import.file.location','Import File Location',5,0,0,'/customs/dev/process/nds/BEACON/valid',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
( entity_id_pk_sq.nextval, 'sso.unique.id','Unique ID for SSO login verification(SSO ID, Login ID, None)', 5, 0, 0, 'None', 0, 0, NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'sso.timelag.allowed','Time lag allowed on SSO date stamp',3,0,0,null,60000,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
( entity_id_pk_sq.nextval, 'sso.date.format','SSO Date Format', 5, 0, 0, 'YYYY-MM-DD HH:mm:ss', 0, 0, NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
( entity_id_pk_sq.nextval, 'sso.sender.time.zone','SSO Sender Time Zone', 5, 0, 0, 'GMT', 0, 0, NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
( entity_id_pk_sq.nextval, 'sso.secret.key','SSO Secret Key', 5, 0, 0, '5aDC7edrAvuchekacR6xevAN3D6eZbspeT5thSswUz7kuwReb73u9ruDE7AjuduQE', 0, 0, NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
( entity_id_pk_sq.nextval, 'sso.aes256.key','SSO AES256 Key', 5, 0, 0, '-114,104,-97,75,-105,40,-51,-32,-126,104,34,41,38,-45,120,-116,110,-13,22,6,-127,-59,81,-50,71,48,-11,99,69,-122,31,95', 0, 0, NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
( entity_id_pk_sq.nextval, 'sso.init.vector','SSO Initialization Vector', 5, 0, 0, '-45,120,-116,-13,75,-51,-32,34,6,-127,-59,81,-50,-122,31,-105', 0, 0, NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
( entity_id_pk_sq.nextval, 'sso.logout','Target URL to go upon logout from SSO', 5, 0, 0, 'None', 0, 0, NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'default.dashboard.charts', 'Default Dashbaord Charts', 5, 0, 0, '5001,5002,5004', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'public.recog.allow.facebook','Public Recognition Allow Facebook',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'public.recog.allow.twitter','Public Recognition Allow Twitter',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'public.recog.allow.linkedin','Public Recognition Allow LinkedIn',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'server.instance.suffix','Server instance',5,0,0,'.biperf.com:51204',0,0,NULL)
/