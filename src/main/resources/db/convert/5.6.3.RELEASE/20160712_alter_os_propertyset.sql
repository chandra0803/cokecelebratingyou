INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'show.sendall.estatement.process','Show "Send to all question" in e-statement process?',1,1,0,NULL,0,0,NULL)
/
INSERT INTO OS_PROPERTYSET(ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE, BOOLEAN_VAL, DOUBLE_VAL, STRING_VAL, LONG_VAL, INT_VAL,DATE_VAL)
 VALUES
 (entity_id_pk_sq.nextval, 'estatement.starting.user.id', 'Estatement Starting User ID', 3, 0, 0, NULL, 0, 0, NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'smtp.host.username','Smtp host username',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'smtp.host.password','Smtp host password',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'public.rec.wall.feed.page.count','Public Recognition Wall API Service Page Count',2,0,0,null,0,50,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'public.rec.wall.feed.enabled','Public Recognition Wall API Service Enabled',1,0,0,null,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'public.rec.wall.kong.header.value','Public Recognition Wall API KONG HEADER CONTENT',5,0,0,'bCcGOBsHhlFTkI3mYQkmzMZhAzvvwd146pBX6oAk8ts=',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'mailing.post.process.retry.attempts','Retries For Post Processes',2,0,0,'',0,10,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'sso.detailed.logging.on','SSO detailed logging on',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'sso.logout.redirect.url','SSO Logout Redirect URL (login, static, url)',5,0,0,'static',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'logout.timeout.like.sso','Logout Timeout Like SSO',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'noauth.timeout.redirect.url','Not Auth/Timeout-Redirect URL (login, static, url)',5,0,0,'login',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'noauth.timeout.internal.redirect.url','Not Auth/Timeout-Internal Redirect URL (login, static, url)',5,0,0,'login',0,0,NULL)
/
INSERT INTO OS_PROPERTYSET
(ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE, BOOLEAN_VAL, DOUBLE_VAL, LONG_VAL, INT_VAL)
VALUES (ENTITY_ID_PK_SQ.NEXTVAL, 'public.recog.days.check', 'Public recognition number of days to check', 2, 0, 0, 0, 30)
/
DELETE os_propertyset WHERE entity_name = 'sso.logout'
/
UPDATE os_propertyset set ENTITY_NAME = 'bankenrollment.characteristic1', ENTITY_KEY = 'Bank Enrollment Characteristic 1' WHERE ENTITY_NAME = 'enrollment.characteristic1'
/
UPDATE os_propertyset set ENTITY_NAME = 'bankenrollment.characteristic2', ENTITY_KEY = 'Bank Enrollment Characteristic 2' WHERE ENTITY_NAME = 'enrollment.characteristic2'
/
UPDATE os_propertyset set INT_VAL = 30 WHERE ENTITY_NAME = 'purl.video.upload.size.limit'
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.accessKey.dev', 'AWS Access Key for the S3 instance', 5, 0, 0, 'AKIAJ2GGY76J3IRXD2OA', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.secretKey.dev', 'AWS Secret Key for the S3 instance', 5, 0, 0, 'vVtnp7Gdr0wGjqb2sGKPgLzorDlh/LDYYBnyAmOV', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.accessKey.prod', 'AWS Access Key for the S3 instance', 5, 0, 0, 'CHANGE ME!', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.secretKey.prod', 'AWS Secret Key for the S3 instance', 5, 0, 0, 'CHANGE ME!', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.avatar.bucketname.dev', 'AWS Bucket Name for the S3 instance.', 5, 0, 0, 'biw-avatars-dev', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.avatar.bucketname.prod', 'AWS Bucket Name for the S3 instance.', 5, 0, 0, 'CHANGE ME!', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.s3.image.prefix', 'AWS S3 prefix key for file upload Mirrors cm3dam url in BIWS', 5, 0, 0, 'https://biw-avatars-prod.s3.amazonaws.com/bravo', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.global.fileprocessing.webdav.usernam', 'AWS username for the webdav instance/FTP', 5, 0, 0, 'BravoFTP', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.global.fileprocessing.webdav.passwor', 'AWS password for the webdav instance/FTP', 5, 0, 0, '1Z2x3c4v5b6n', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.fileload.transer.cgi', 'AWS File load transfer CGI location', 5, 0, 0, 'CHANGE ME!', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.fileload.transer.execution.cmd', 'AWS File load transfer execution cmd', 5, 0, 0, 'perl', 0, 0, null)
/