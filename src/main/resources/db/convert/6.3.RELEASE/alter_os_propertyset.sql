--liquibase formatted sql

--changeset dudyala:1
--comment Added a column to identify editable system variables
ALTER TABLE os_propertyset ADD is_editable NUMBER(1) DEFAULT 1 NOT NULL;
--rollback ALTER TABLE os_propertyset DROP (is_editable);

--changeset dudyala:2
--comment Added a column to identify viewable system variables
ALTER TABLE os_propertyset ADD is_viewable NUMBER(1) DEFAULT 1 NOT NULL;
--rollback ALTER TABLE os_propertyset DROP (is_viewable);

--changeset cornelius:3
--comment change value of run campaign transfer process from false to true
UPDATE os_propertyset SET entity_key='Run Campaign Transfer Process', boolean_val=1 WHERE entity_name='run.campaign.transfer.process';
--rollback not required

--changeset mattam:4
--comment system variable passwordreset.token.timelag.allowed for password reset token timelag allowed
INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL) 
SELECT entity_id_pk_sq.nextval,'passwordreset.token.timelag.allowed','Password Reset Token timelag allowed [Min]',2,0,0,NULL,0,240,NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='passwordreset.token.timelag.allowed');
-- rollback DELETE os_propertyset WHERE entity_name = 'passwordreset.token.timelag.allowed';

--changeset dhanekula:6
--comment system variable execution.log.data.purge.days for dbms scheduler job to purge comm_log and execution_log tables. 
INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL) 
SELECT entity_id_pk_sq.nextval,'execution.log.data.purge.days','Number of days of data to be kept in execution_log table',2,0,0,NULL,0,180,NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='execution.log.data.purge.days');
--rollback DELETE FROM os_propertyset WHERE entity_name = 'execution.log.data.purge.days';

--changeset dhanekula:7
--comment system variable comm.log.data.purge.days for dbms scheduler job to purge comm_log and execution_log tables. 
INSERT INTO os_propertyset (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL) 
SELECT entity_id_pk_sq.nextval,'comm.log.data.purge.days','Number of days of data to be kept in comm_log table',2,0,0,NULL,0,390,NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='comm.log.data.purge.days');
--rollback DELETE FROM os_propertyset WHERE entity_name = 'comm.log.data.purge.days';

--changeset cornelius:8
--comment Insert system variable for Honeycomb Config app URL DEV
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'honeycomb.config.url.dev', 'Honeycomb Config URL Dev', 5, 0, 0, 'http://localhost:8001/hcconfig/', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='honeycomb.config.url.dev');
--rollback DELETE FROM os_propertyset WHERE entity_name='honeycomb.config.url.dev';

--changeset cornelius:9
--comment Insert system variable for Honeycomb Config app URL QA
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'honeycomb.config.url.qa', 'Honeycomb Config URL QA', 5, 0, 0, 'https://configqa.honeycombsvc.com/hcconfig/', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='honeycomb.config.url.qa');
--rollback DELETE FROM os_propertyset WHERE entity_name='honeycomb.config.url.qa';

--changeset cornelius:10
--comment Insert system variable for Honeycomb Config app URL PRE
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'honeycomb.config.url.preprod', 'Honeycomb Config URL PRE', 5, 0, 0, 'https://configpprd.honeycombsvc.com/hcconfig/', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='honeycomb.config.url.preprod');
--rollback DELETE FROM os_propertyset WHERE entity_name='honeycomb.config.url.preprod';

--changeset cornelius:11
--comment Insert system variable for Honeycomb Config app URL PROD
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'honeycomb.config.url.prod', 'Honeycomb Config URL PROD', 5, 0, 0, 'https://config.honeycombsvc.com/hcconfig/', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='honeycomb.config.url.prod');
--rollback DELETE FROM os_propertyset WHERE entity_name='honeycomb.config.url.prod';

--changeset palaniss:12
--comment Insert system variable to allow Security option in sidebar for participant
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
SELECT entity_id_pk_sq.nextval, 'sidebar.allow.security', 'Allow security in sidebar', 1, 1, 0, null, 0, 0 , null  FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='sidebar.allow.security');
--rollback DELETE FROM os_propertyset WHERE entity_name='sidebar.allow.security';

--changeset jaligama:13
--comment copying the value from plateau.platform.only to drive.enabled
UPDATE os_propertyset SET boolean_val = (SELECT boolean_val FROM os_propertyset WHERE entity_name = 'plateau.platform.only') WHERE entity_name = 'drive.enabled';
--rollback not required

--changeset palaniss:15
--comment Insert system variable to allow terms option in participant login
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
SELECT entity_id_pk_sq.nextval, 'sidebar.allow.terms', 'Allow terms in sidebar', 1, 1, 0, null, 0, 0 , null  FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='sidebar.allow.terms');
--rollback DELETE FROM os_propertyset WHERE entity_name='sidebar.allow.terms';

--changeset palaniss:16
--comment Insert system variable to allow Personal info option in participant login
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
SELECT entity_id_pk_sq.nextval, 'sidebar.allow.personalinfo', 'Allow personalinfo in sidebar', 1, 1, 0, null, 0, 0 , null  FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='sidebar.allow.personalinfo');
--rollback DELETE FROM os_propertyset WHERE entity_name='sidebar.allow.personalinfo';

--changeset palaniss:17
--comment Insert system variable to allow faqs option in participant login
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
SELECT entity_id_pk_sq.nextval, 'sidebar.allow.faqs', 'Allow faqs in sidebar', 1, 1, 0, null, 0, 0 , null FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='sidebar.allow.faqs');
--rollback DELETE FROM os_propertyset WHERE entity_name='sidebar.allow.faqs';

--changeset palaniss:18
--comment Insert system variable to allow Activity History option in participant login
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
SELECT entity_id_pk_sq.nextval, 'sidebar.allow.activityhistory', 'Allow activityhistory in sidebar', 1, 1, 0, null, 0, 0 , null FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='sidebar.allow.activityhistory');
--rollback DELETE FROM os_propertyset WHERE entity_name='sidebar.allow.activityhistory';

--changeset palaniss:19
--comment Insert system variable to allow statement option in participant login
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
SELECT entity_id_pk_sq.nextval, 'sidebar.allow.statement', 'Allow statement in sidebar', 1, 1, 0, null, 0, 0 , null FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='sidebar.allow.statement');
--rollback DELETE FROM os_propertyset WHERE entity_name='sidebar.allow.statement';

--changeset palaniss:20
--comment Insert system variable to allow preferences option in participant login
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
SELECT entity_id_pk_sq.nextval, 'sidebar.allow.preferences', 'Allow preferences in sidebar', 1, 1, 0, null, 0, 0 , null FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='sidebar.allow.preferences');
--rollback DELETE FROM os_propertyset WHERE entity_name='sidebar.allow.preferences';

--changeset palaniss:21
--comment Insert system variable to allow delegate option in participant login
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
SELECT entity_id_pk_sq.nextval, 'sidebar.allow.delegate', 'Allow delegare in sidebar', 1, 1, 0, null, 0, 0 , null FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='sidebar.allow.delegate');
--rollback DELETE FROM os_propertyset WHERE entity_name='sidebar.allow.delegate';

--changeset palaniss:22
--comment copying the value from allow.delegate to sidebar.allow.delegate
UPDATE OS_PROPERTYSET SET BOOLEAN_VAL = (SELECT BOOLEAN_VAL from  OS_PROPERTYSET WHERE ENTITY_NAME='allow.delegate') WHERE  ENTITY_NAME='sidebar.allow.delegate';
--rollback not required

--changeset kancherla:28
--comment Insert system variable for batch size of account synchronization to honeycomb
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'honeycomb.sync.batch.size', 'Honeycomb Account Synchronization batch size', 2, 0, 0, null, 0, 100, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='honeycomb.sync.batch.size');
--rollback DELETE FROM os_propertyset WHERE entity_name='honeycomb.sync.batch.size';

--changeset kancherla:29
--comment Insert column for grouping system variables and sort them according to groups
ALTER TABLE os_propertyset ADD group_name VARCHAR2(40 CHAR);
--rollback ALTER TABLE os_propertyset DROP (group_name);

--changeset cornelius:30
--comment Insert system variable for Honeycomb Config app URL DEV
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'honeycomb.gateway.url.dev', 'Honeycomb Gateway URL Dev', 5, 0, 0, 'http://localhost:8001/hcgateway/', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='honeycomb.gateway.url.dev');
--rollback DELETE FROM os_propertyset WHERE entity_name='honeycomb.gateway.url.dev';

--changeset cornelius:31
--comment Insert system variable for Honeycomb Config app URL QA
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'honeycomb.gateway.url.qa', 'Honeycomb Gateway URL QA', 5, 0, 0, 'https://gatewayqa.${farm}.honeycombsvc.com/hcgateway/', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='honeycomb.gateway.url.qa');
--rollback DELETE FROM os_propertyset WHERE entity_name='honeycomb.gateway.url.qa';

--changeset cornelius:32
--comment Insert system variable for Honeycomb Config app URL PRE
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'honeycomb.gateway.url.preprod', 'Honeycomb Gateway URL PRE', 5, 0, 0, 'https://gatewaypprd.${farm}.honeycombsvc.com/hcgateway/', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='honeycomb.gateway.url.preprod');
--rollback DELETE FROM os_propertyset WHERE entity_name='honeycomb.gateway.url.preprod';

--changeset cornelius:33
--comment Insert system variable for Honeycomb Config app URL PROD
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'honeycomb.gateway.url.prod', 'Honeycomb Gateway URL PROD', 5, 0, 0, 'https://gateway.${farm}.honeycombsvc.com/hcgateway/', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='honeycomb.gateway.url.prod');
--rollback DELETE FROM os_propertyset WHERE entity_name='honeycomb.gateway.url.prod';

--changeset cornelius:34
--comment Insert system variable for Account Sync honeycomb gateway endpoint
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'honeycomb.endpoint.account.sync', 'Honeycomb Account Sync Endpoint', 5, 0, 0, 'services/v1/gaccountsync', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='honeycomb.endpoint.account.sync');
--rollback DELETE FROM os_propertyset WHERE entity_name='honeycomb.endpoint.account.sync';

--changeset kancherla:35
--comment Grouping AWS system variables
UPDATE os_propertyset SET group_name = 'AWS' where entity_name in ('aws.accessKey.dev', 'aws.accessKey.prod', 'aws.avatar.bucketname.dev', 'aws.avatar.bucketname.prod', 'aws.fileload.transer.cgi', 'aws.fileload.transer.execution.cmd', 'aws.global.fileprocessing.webdav.passwor', 'aws.global.fileprocessing.webdav.usernam', 'aws.s3.image.prefix', 'aws.secretKey.dev', 'aws.secretKey.prod', 'pdf.service.url.dev', 'pdf.service.url.preprod', 'pdf.service.url.prod', 'pdf.service.url.qa');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('aws.accessKey.dev', 'aws.accessKey.prod', 'aws.avatar.bucketname.dev', 'aws.avatar.bucketname.prod', 'aws.fileload.transer.cgi', 'aws.fileload.transer.execution.cmd', 'aws.global.fileprocessing.webdav.passwor', 'aws.global.fileprocessing.webdav.usernam', 'aws.s3.image.prefix', 'aws.secretKey.dev', 'aws.secretKey.prod', 'pdf.service.url.dev', 'pdf.service.url.preprod', 'pdf.service.url.prod', 'pdf.service.url.qa');

--changeset kancherla:36
--comment Grouping Bank system variables
UPDATE os_propertyset SET group_name = 'Bank' where entity_name in ('awardbanq.deposit.retry.count', 'awardbanq.deposit.retry.delay', 'awardbanq.mode', 'awardbanq.organization.number', 'awardbanq.pax.update.process.batch.size', 'awardbanq.realtime.userid', 'awardbanq.source.system', 'webservices.url.dev', 'webservices.url.preprod', 'webservices.url.prod', 'webservices.url.qa', 'bankenrollment.characteristic1', 'bankenrollment.characteristic2');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('awardbanq.deposit.retry.count', 'awardbanq.deposit.retry.delay', 'awardbanq.mode', 'awardbanq.organization.number', 'awardbanq.pax.update.process.batch.size', 'awardbanq.realtime.userid', 'awardbanq.source.system', 'webservices.url.dev', 'webservices.url.preprod', 'webservices.url.prod', 'webservices.url.qa', 'bankenrollment.characteristic1', 'bankenrollment.characteristic2');

--changeset kancherla:37
--comment Grouping Catalog system variables
UPDATE os_propertyset SET group_name = 'Catalog' where entity_name in ('merchlinq.url.dev', 'merchlinq.url.preprod', 'merchlinq.url.prod', 'merchlinq.url.qa', 'merchorder.phone', 'reward.offerings.endpoint.dev', 'reward.offerings.endpoint.preprod', 'reward.offerings.endpoint.prod', 'reward.offerings.endpoint.qa', 'shop.internalremoteurl.dev', 'shop.internalremoteurl.preprod', 'shop.internalremoteurl.prod', 'shop.internalremoteurl.qa');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('merchlinq.url.dev', 'merchlinq.url.preprod', 'merchlinq.url.prod', 'merchlinq.url.qa', 'merchorder.phone', 'reward.offerings.endpoint.dev', 'reward.offerings.endpoint.preprod', 'reward.offerings.endpoint.prod', 'reward.offerings.endpoint.qa', 'shop.internalremoteurl.dev', 'shop.internalremoteurl.preprod', 'shop.internalremoteurl.prod', 'shop.internalremoteurl.qa');

--changeset kancherla:38
--comment Grouping Elastic search system variables
UPDATE os_propertyset SET group_name = 'Elastic Search' where entity_name in ('autocomplete.delay.millis', 'autocomplete.es.max.allowed.recognize', 'autocomplete.es.max.pagination.records', 'autocomplete.es.max.result.size', 'autocomplete.es.proxy.port.dev', 'autocomplete.es.proxy.port.preprod', 'autocomplete.es.proxy.port.prod', 'autocomplete.es.proxy.port.qa', 'autocomplete.es.proxy.url.dev', 'autocomplete.es.proxy.url.preprod', 'autocomplete.es.proxy.url.prod', 'autocomplete.es.proxy.url.qa', 'autocomplete.es.readtimeout', 'autocomplete.es.url.dev', 'autocomplete.es.url.preprod', 'autocomplete.es.url.prod', 'autocomplete.es.url.qa', 'elastic.search.enabled', 'elasticsearch.creds.password.dev', 'elasticsearch.creds.password.preprod', 'elasticsearch.creds.password.prod', 'elasticsearch.creds.password.qa', 'elasticsearch.creds.username.dev', 'elasticsearch.creds.username.preprod', 'elasticsearch.creds.username.prod', 'elasticsearch.creds.username.qa', 'elasticsearch.index.batch.size');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('autocomplete.delay.millis', 'autocomplete.es.max.allowed.recognize', 'autocomplete.es.max.pagination.records', 'autocomplete.es.max.result.size', 'autocomplete.es.proxy.port.dev', 'autocomplete.es.proxy.port.preprod', 'autocomplete.es.proxy.port.prod', 'autocomplete.es.proxy.port.qa', 'autocomplete.es.proxy.url.dev', 'autocomplete.es.proxy.url.preprod', 'autocomplete.es.proxy.url.prod', 'autocomplete.es.proxy.url.qa', 'autocomplete.es.readtimeout', 'autocomplete.es.url.dev', 'autocomplete.es.url.preprod', 'autocomplete.es.url.prod', 'autocomplete.es.url.qa', 'elastic.search.enabled', 'elasticsearch.creds.password.dev', 'elasticsearch.creds.password.preprod', 'elasticsearch.creds.password.prod', 'elasticsearch.creds.password.qa', 'elasticsearch.creds.username.dev', 'elasticsearch.creds.username.preprod', 'elasticsearch.creds.username.prod', 'elasticsearch.creds.username.qa', 'elasticsearch.index.batch.size');

--changeset kancherla:39
--comment Grouping Email system variables
UPDATE os_propertyset SET group_name = 'Email' where entity_name in ('email.address.incentive.personaldisplay', 'email.address.system', 'email.address.system.addl', 'email.address.system.incentive', 'email.address.system.personaldisplay', 'email.batch.enable', 'email.bounceback.verified', 'email.notification.senderaddr', 'email.servers', 'email.subject.prefix', 'email.subject.prefix.display', 'email.use.strongmail', 'email.wrapper.footer', 'email.wrapper.header', 'mailing.post.process.retry.attempts');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('email.address.incentive.personaldisplay', 'email.address.system', 'email.address.system.addl', 'email.address.system.incentive', 'email.address.system.personaldisplay', 'email.batch.enable', 'email.bounceback.verified', 'email.notification.senderaddr', 'email.servers', 'email.subject.prefix', 'email.subject.prefix.display', 'email.use.strongmail', 'email.wrapper.footer', 'email.wrapper.header', 'mailing.post.process.retry.attempts');

--changeset kancherla:40
--comment Grouping e-statement system variables
UPDATE os_propertyset SET group_name = 'e-statement' where entity_name in ('estatement.starting.user.id', 'participant.allow.estatements', 'show.sendall.estatement.process');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('estatement.starting.user.id', 'participant.allow.estatements', 'show.sendall.estatement.process');

--changeset kancherla:41
--comment Grouping File Load system variables
UPDATE os_propertyset SET group_name = 'File Load' where entity_name in ('fileload.token.dev', 'fileload.token.preprod', 'fileload.token.prod', 'fileload.token.qa', 'fileload.userrole.delete', 'global.fileprocessing.prefix', 'global.fileprocessing.subfolder', 'global.fileprocessing.webdav.dev', 'global.fileprocessing.webdav.preprod', 'global.fileprocessing.webdav.prod', 'global.fileprocessing.webdav.qa', 'global.fileprocessing.workwip.dev', 'global.fileprocessing.workwip.preprod', 'global.fileprocessing.workwip.prod', 'global.fileprocessing.workwip.qa', 'import.file.location', 'import.file.utl_path', 'boolean.enable.global.standalone', 'import.page.size', 'ssi.progress.upload.size.limit');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('fileload.token.dev', 'fileload.token.preprod', 'fileload.token.prod', 'fileload.token.qa', 'fileload.userrole.delete', 'global.fileprocessing.prefix', 'global.fileprocessing.subfolder', 'global.fileprocessing.webdav.dev', 'global.fileprocessing.webdav.preprod', 'global.fileprocessing.webdav.prod', 'global.fileprocessing.webdav.qa', 'global.fileprocessing.workwip.dev', 'global.fileprocessing.workwip.preprod', 'global.fileprocessing.workwip.prod', 'global.fileprocessing.workwip.qa', 'import.file.location', 'import.file.utl_path', 'boolean.enable.global.standalone', 'import.page.size', 'ssi.progress.upload.size.limit');

--changeset kancherla:42
--comment Grouping GoalQuest system variables
UPDATE os_propertyset SET group_name = 'GoalQuest' where entity_name in ('goalquest.merchlinqdestenv.dev', 'goalquest.merchlinqdestenv.preprod', 'goalquest.merchlinqdestenv.prod', 'goalquest.merchlinqdestenv.qa', 'goalquest.merchlinqsso.url.dev', 'goalquest.merchlinqsso.url.preprod', 'goalquest.merchlinqsso.url.prod', 'goalquest.merchlinqsso.url.qa');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('goalquest.merchlinqdestenv.dev', 'goalquest.merchlinqdestenv.preprod', 'goalquest.merchlinqdestenv.prod', 'goalquest.merchlinqdestenv.qa', 'goalquest.merchlinqsso.url.dev', 'goalquest.merchlinqsso.url.preprod', 'goalquest.merchlinqsso.url.prod', 'goalquest.merchlinqsso.url.qa');

--changeset kancherla:43
--comment Grouping Honeycomb system variables
UPDATE os_propertyset SET group_name = 'Honeycomb' where entity_name in ('honeycomb.config.url.dev', 'honeycomb.config.url.preprod', 'honeycomb.config.url.prod', 'honeycomb.config.url.qa', 'honeycomb.endpoint.account.sync', 'honeycomb.sync.batch.size', 'honeycomb.gateway.url.dev', 'honeycomb.gateway.url.qa', 'honeycomb.gateway.url.preprod', 'honeycomb.gateway.url.prod');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('honeycomb.config.url.dev', 'honeycomb.config.url.preprod', 'honeycomb.config.url.prod', 'honeycomb.config.url.qa', 'honeycomb.endpoint.account.sync', 'honeycomb.sync.batch.size', 'honeycomb.gateway.url.dev', 'honeycomb.gateway.url.qa', 'honeycomb.gateway.url.preprod', 'honeycomb.gateway.url.prod');

--changeset kancherla:44
--comment Grouping Languages system variables
UPDATE os_propertyset SET group_name = 'Languages' where entity_name in ('texteditor.dictionaries', 'translation.service.url.dev', 'translation.service.url.preprod', 'translation.service.url.prod', 'translation.service.url.qa', 'machine.language.allow.translation');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('texteditor.dictionaries', 'translation.service.url.dev', 'translation.service.url.preprod', 'translation.service.url.prod', 'translation.service.url.qa', 'machine.language.allow.translation');

--changeset kancherla:45
--comment Grouping Security system variables
UPDATE os_propertyset SET group_name = 'Security' where entity_name in ('admin.ip.restrictions', 'allow.password.field.auto.complete', 'password.can.reuse', 'password.expired.period', 'password.force.reset', 'password.history.count', 'password.min.length', 'password.not.match.regex', 'password.otp.expiry.days', 'password.pattern', 'password.reset.token.length', 'password.should.use.regex', 'password.variations.notallowed', 'passwordreset.token.timelag.allowed', 'ips.to.allow.csv.regex', 'lockout.failure.count', 'seeddatafile.orig', 'seeddatafile.temp', 'should.restrict.by.ip');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('admin.ip.restrictions', 'allow.password.field.auto.complete', 'password.can.reuse', 'password.expired.period', 'password.force.reset', 'password.history.count', 'password.min.length', 'password.not.match.regex', 'password.otp.expiry.days', 'password.pattern', 'password.reset.token.length', 'password.should.use.regex', 'password.variations.notallowed', 'passwordreset.token.timelag.allowed', 'ips.to.allow.csv.regex', 'lockout.failure.count', 'seeddatafile.orig', 'seeddatafile.temp', 'should.restrict.by.ip');

--changeset kancherla:46
--comment Grouping Purl system variables
UPDATE os_propertyset SET group_name = 'Purl' where entity_name in ('purl.available', 'purl.celeb.active.tabs', 'purl.celeb.default.tab.name', 'purl.comment.size.limit', 'purl.days.to.expiration', 'purl.url.dev', 'purl.url.preprod', 'purl.url.prod', 'purl.url.qa');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('purl.available', 'purl.celeb.active.tabs', 'purl.celeb.default.tab.name', 'purl.comment.size.limit', 'purl.days.to.expiration', 'purl.url.dev', 'purl.url.preprod', 'purl.url.prod', 'purl.url.qa');

--changeset kancherla:47
--comment Grouping Recognition Wall system variables
UPDATE os_propertyset SET group_name = 'Recognition Wall' where entity_name in ('public.recog.active.tabs', 'public.recog.days.check', 'public.recog.days.check.recommended', 'public.recog.default.tab.name', 'public.recog.levels.check.team', 'profile.followlist.tab.show');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('public.recog.active.tabs', 'public.recog.days.check', 'public.recog.days.check.recommended', 'public.recog.default.tab.name', 'public.recog.levels.check.team', 'profile.followlist.tab.show');

--changeset kancherla:48
--comment Grouping Shared Services system variables
UPDATE os_propertyset SET group_name = 'Shared Services' where entity_name in ('shared.services.security.key.dev', 'shared.services.security.key.preprod', 'shared.services.security.key.prod', 'shared.services.security.key.qa');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('shared.services.security.key.dev', 'shared.services.security.key.preprod', 'shared.services.security.key.prod', 'shared.services.security.key.qa');

--changeset kancherla:49
--comment Grouping SMS system variables
UPDATE os_propertyset SET group_name = 'SMS' where entity_name in ('urlshortner.signature.dev', 'urlshortner.signature.preprod', 'urlshortner.signature.prod', 'urlshortner.signature.qa', 'urlshortner.url.dev', 'urlshortner.url.preprod', 'urlshortner.url.prod', 'urlshortner.url.qa');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('urlshortner.signature.dev', 'urlshortner.signature.preprod', 'urlshortner.signature.prod', 'urlshortner.signature.qa', 'urlshortner.url.dev', 'urlshortner.url.preprod', 'urlshortner.url.prod', 'urlshortner.url.qa');

--changeset kancherla:50
--comment Grouping Social Media system variables
UPDATE os_propertyset SET group_name = 'Social Media' where entity_name in ('chatter.callback.url', 'chatter.consumer.key.dev', 'chatter.consumer.key.preprod', 'chatter.consumer.key.prod', 'chatter.consumer.key.qa', 'chatter.consumer.secret.dev', 'chatter.consumer.secret.preprod', 'chatter.consumer.secret.prod', 'chatter.consumer.secret.qa', 'facebook.api.id', 'facebook.api.key', 'facebook.api.secret', 'purl.allow.chatter', 'purl.allow.facebook', 'purl.allow.linkedin', 'purl.allow.twitter', 'public.recog.allow.chatter', 'public.recog.allow.facebook', 'public.recog.allow.linkedin', 'public.recog.allow.twitter', 'twitter.consumer.key', 'twitter.consumer.secret');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('chatter.callback.url', 'chatter.consumer.key.dev', 'chatter.consumer.key.preprod', 'chatter.consumer.key.prod', 'chatter.consumer.key.qa', 'chatter.consumer.secret.dev', 'chatter.consumer.secret.preprod', 'chatter.consumer.secret.prod', 'chatter.consumer.secret.qa', 'facebook.api.id', 'facebook.api.key', 'facebook.api.secret', 'purl.allow.chatter', 'purl.allow.facebook', 'purl.allow.linkedin', 'purl.allow.twitter', 'public.recog.allow.chatter', 'public.recog.allow.facebook', 'public.recog.allow.linkedin', 'public.recog.allow.twitter', 'twitter.consumer.key', 'twitter.consumer.secret');

--changeset kancherla:51
--comment Grouping SSO system variables
UPDATE os_propertyset SET group_name = 'SSO' where entity_name in ('sso.aes256.key', 'sso.date.format', 'sso.detailed.logging.on', 'sso.init.vector', 'sso.logout.redirect.url', 'sso.secret.key', 'sso.sender.time.zone', 'sso.timelag.allowed', 'sso.unique.id', 'logout.timeout.like.sso');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('sso.aes256.key', 'sso.date.format', 'sso.detailed.logging.on', 'sso.init.vector', 'sso.logout.redirect.url', 'sso.secret.key', 'sso.sender.time.zone', 'sso.timelag.allowed', 'sso.unique.id', 'logout.timeout.like.sso');

--changeset kancherla:52
--comment Grouping Strongmail system variables
UPDATE os_propertyset SET group_name = 'Strongmail' where entity_name in ('strongmail.organization.name', 'strongmail.password.name', 'strongmail.sub.organization.id', 'strongmail.user.name', 'smtp.host.password', 'smtp.host.username');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('strongmail.organization.name', 'strongmail.password.name', 'strongmail.sub.organization.id', 'strongmail.user.name', 'smtp.host.password', 'smtp.host.username');

--changeset kancherla:53
--comment Grouping Under Armour system variables
UPDATE os_propertyset SET group_name = 'Under Armour' where entity_name in ('bi.ua.microservice.client.id', 'bi.ua.microservice.code', 'bi.ua.microservice.enabled', 'bi.ua.microservice.encryption.salt', 'bi.ua.microservice.repo', 'bi.uaws.authorize.url', 'bi.uaws.endpoint.url.dev', 'bi.uaws.endpoint.url.preprod', 'bi.uaws.endpoint.url.prod', 'bi.uaws.endpoint.url.qa');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('bi.ua.microservice.client.id', 'bi.ua.microservice.code', 'bi.ua.microservice.enabled', 'bi.ua.microservice.encryption.salt', 'bi.ua.microservice.repo', 'bi.uaws.authorize.url', 'bi.uaws.endpoint.url.dev', 'bi.uaws.endpoint.url.preprod', 'bi.uaws.endpoint.url.prod', 'bi.uaws.endpoint.url.qa');

--changeset kancherla:54
--comment Grouping Work Happier system variables
UPDATE os_propertyset SET group_name = 'Work Happier' where entity_name in ('external.survey.aes256.key', 'external.survey.endpoint', 'external.survey.init.vector');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('external.survey.aes256.key', 'external.survey.endpoint', 'external.survey.init.vector');

--changeset kancherla:59
--comment System variable from install wizard was adding key_type 5. Changing it to key_type 1
UPDATE os_propertyset SET key_type = 1 WHERE entity_name = 'drive.enabled';
--rollback UPDATE os_propertyset SET key_type = 5 WHERE entity_name = 'drive.enabled';

--changeset kancherla:60
--comment Grouping Admin system variables
UPDATE os_propertyset SET group_name = 'Admin' where entity_name in ('execution.log.data.purge.days', 'comm.log.data.purge.days', 'default.country', 'deposit.process.send.pax.deposit.email', 'display.table.max.per.multi.page', 'display.table.max.per.single.page', 'escalation.hierarchy.id', 'homepage.filters.strategy.threaded', 'participant.allow.contacts');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('execution.log.data.purge.days', 'comm.log.data.purge.days', 'default.country', 'deposit.process.send.pax.deposit.email', 'display.table.max.per.multi.page', 'display.table.max.per.single.page', 'escalation.hierarchy.id', 'homepage.filters.strategy.threaded', 'participant.allow.contacts');

--changeset kancherla:61
--comment Grouping Billing system variables
UPDATE os_propertyset SET group_name = 'Billing' where entity_name in ('translation.bill.code', 'merchandise.billing.code.char', 'messenger.app.code');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('translation.bill.code', 'merchandise.billing.code.char', 'messenger.app.code');

--changeset kancherla:62
--comment Grouping Client system variables
UPDATE os_propertyset SET group_name = 'Client' where entity_name in ('banner.ad.limit', 'birth.date.year.rule', 'client.large.audience', 'client.launch.date', 'client.name', 'client.url', 'default.dashboard.charts', 'default.language', 'discretionary.award.max', 'discretionary.award.min', 'diy.audience.export.limit', 'import.client.prefix', 'login.welcome.message', 'manager.send.alrt', 'enable.opt.out.awards', 'enable.opt.out.program', 'firstTimeLogin.required.email', 'large.audience.report.generation.enabled', 'project.id', 'run.campaign.transfer.process', 'secondary.logo.enable', 'server.instance.suffix', 'show.participant.birth.date', 'show.participant.hire.date', 'site.url.dev', 'site.url.preprod', 'site.url.prod', 'site.url.qa', 'boolean.include.balance', 'celebration', 'noauth.timeout.internal.redirect.url', 'noauth.timeout.redirect.url', 'pax.search.limit', 'plateau.awards.reminder', 'plateau.awards.reminder.days', 'tip.day.rotate.seconds', 'client.contact.url', 'contact.us.email');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('banner.ad.limit', 'birth.date.year.rule', 'client.large.audience', 'client.launch.date', 'client.name', 'client.url', 'default.dashboard.charts', 'default.language', 'discretionary.award.max', 'discretionary.award.min', 'diy.audience.export.limit', 'import.client.prefix', 'login.welcome.message', 'manager.send.alrt', 'enable.opt.out.awards', 'enable.opt.out.program', 'firstTimeLogin.required.email', 'large.audience.report.generation.enabled', 'project.id', 'run.campaign.transfer.process', 'secondary.logo.enable', 'server.instance.suffix', 'show.participant.birth.date', 'show.participant.hire.date', 'site.url.dev', 'site.url.preprod', 'site.url.prod', 'site.url.qa', 'boolean.include.balance', 'celebration', 'noauth.timeout.internal.redirect.url', 'noauth.timeout.redirect.url', 'pax.search.limit', 'plateau.awards.reminder', 'plateau.awards.reminder.days', 'tip.day.rotate.seconds', 'client.contact.url', 'contact.us.email');

--changeset kancherla:63
--comment Grouping EZ Thanks system variables
UPDATE os_propertyset SET group_name = 'EZ Thanks' where entity_name in ('google.cloud.messaging.server.id');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('google.cloud.messaging.server.id');

--changeset kancherla:64
--comment Grouping Functionality system variables
UPDATE os_propertyset SET group_name = 'Functionality' where entity_name in ('all.users.proxy', 'allow.delegate', 'install.badges', 'install.challengepoint', 'install.engagement', 'install.goalquest', 'install.leaderboard', 'install.nominations', 'install.productclaims', 'install.quizzes', 'install.recognition', 'install.ssi', 'install.survey', 'install.throwdown', 'install.wellness', 'instantpoll', 'awardbanq.convertcert.used', 'sidebar.allow.activityhistory', 'sidebar.allow.alertsandmessages', 'sidebar.allow.faqs', 'sidebar.allow.groups', 'sidebar.allow.personalinfo', 'sidebar.allow.preferences', 'sidebar.allow.security', 'sidebar.allow.statement', 'sidebar.allow.terms', 'sidebar.allow.delegate', 'allow.find.others', 'roster.management.available', 'self.enrollment.enabled', 'work.happier', 'asset.debug', 'budget.transfer.show', 'claim.processing.allow.batch');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('all.users.proxy', 'allow.delegate', 'install.badges', 'install.challengepoint', 'install.engagement', 'install.goalquest', 'install.leaderboard', 'install.nominations', 'install.productclaims', 'install.quizzes', 'install.recognition', 'install.ssi', 'install.survey', 'install.throwdown', 'install.wellness', 'instantpoll', 'awardbanq.convertcert.used', 'sidebar.allow.activityhistory', 'sidebar.allow.alertsandmessages', 'sidebar.allow.faqs', 'sidebar.allow.groups', 'sidebar.allow.personalinfo', 'sidebar.allow.preferences', 'sidebar.allow.security', 'sidebar.allow.statement', 'sidebar.allow.terms', 'sidebar.allow.delegate', 'allow.find.others', 'roster.management.available', 'self.enrollment.enabled', 'work.happier', 'asset.debug', 'budget.transfer.show', 'claim.processing.allow.batch');

--changeset kancherla:65
--comment Grouping Google Analytics system variables
UPDATE os_propertyset SET group_name = 'Google Analytics' where entity_name in ('google.analytics.account');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('google.analytics.account');

--changeset kancherla:66
--comment Grouping Media Upload system variables
UPDATE os_propertyset SET group_name = 'Media Upload' where entity_name in ('system.image.upload.size.limit', 'system.pdf.upload.size.limit', 'system.video.upload.size.limit');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('system.image.upload.size.limit', 'system.pdf.upload.size.limit', 'system.video.upload.size.limit');

--changeset kancherla:67
--comment Grouping Platform Type system variables
UPDATE os_propertyset SET group_name = 'Platform Type' where entity_name in ('recognition-only.enabled', 'drive.enabled', 'meplus.enabled', 'salesmaker.enabled');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('recognition-only.enabled', 'drive.enabled', 'meplus.enabled', 'salesmaker.enabled');

--changeset kancherla:68
--comment Grouping Recognition Wall API system variables
UPDATE os_propertyset SET group_name = 'Recognition Wall API' where entity_name in ('public.rec.wall.feed.enabled', 'public.rec.wall.feed.page.count');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('public.rec.wall.feed.enabled', 'public.rec.wall.feed.page.count');

--changeset kancherla:69
--comment Grouping Reports system variables
UPDATE os_propertyset SET group_name = 'Reports' where entity_name in ('nomination.report.comment.available', 'report.recognition.comment.display', 'report.startDate.rollback.days', 'survey.report.response.count');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('nomination.report.comment.available', 'report.recognition.comment.display', 'report.startDate.rollback.days', 'survey.report.response.count');

--changeset kancherla:70
--comment Grouping Under Armour system variables
UPDATE os_propertyset SET group_name = 'Under Armour' where entity_name in ('bi.ua.microservice.enabled', 'bi.ua.microservice.code', 'bi.ua.microservice.client.id', 'bi.ua.microservice.encryption.salt');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('bi.ua.microservice.enabled', 'bi.ua.microservice.code', 'bi.ua.microservice.client.id', 'bi.ua.microservice.encryption.salt');

--changeset kancherla:71
--comment Grouping Terms and Conditions system variables
UPDATE os_propertyset SET group_name = 'Terms and Conditions' where entity_name in ('termsAndConditions.used', 'termsAndConditions.usercanaccept', 'termsAndConditionsView.display');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('termsAndConditions.used', 'termsAndConditions.usercanaccept', 'termsAndConditionsView.display');

--changeset kancherla:72
--comment Grouping Throwdown system variables
UPDATE os_propertyset SET group_name = 'Throwdown' where entity_name in ('smack.talk.active.tabs', 'smack.talk.default.tab.name');
--rollback UPDATE os_propertyset SET group_name = '' where entity_name in ('smack.talk.active.tabs', 'smack.talk.default.tab.name');

--changeset kancherla:73
--comment Delete system variable sea.days.to.abandoned
DELETE FROM os_propertyset WHERE entity_name = 'sea.days.to.abandoned';
--rollback not required
 
--changeset kancherla:74
--comment Delete system variable sea.email.account
DELETE FROM os_propertyset WHERE entity_name = 'sea.email.account';
--rollback not required
 
--changeset kancherla:75
--comment Delete system variable sea.security.salt
DELETE FROM os_propertyset WHERE entity_name = 'sea.security.salt';
--rollback not required
 
--changeset kancherla:76
--comment Delete system variable sea.email.password
DELETE FROM os_propertyset WHERE entity_name = 'sea.email.password';
--rollback not required

--changeset cornelius:76.5
--comment Make any stray system variables have unknown group name
UPDATE os_propertyset SET group_name='unknown' where group_name is null;
--rollback not required

--changeset kancherla:77
--comment Making group name column non nullable for system properties
ALTER TABLE os_propertyset MODIFY group_name DEFAULT 'unknown' not null;
--rollback ALTER TABLE os_propertyset MODIFY group_name NULL;

--changeset ramesh:78
--comment Insert system variable for Plateau Redemtion role
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,GROUP_NAME)
SELECT entity_id_pk_sq.nextval, 'roles.for.biw.only', 'Roles for BIW use only', 5, 0, 0, 'BI_ADMIN,PROJ_MGR,PLATEAU_REDEMPTION', 0, 0 , null,'Admin' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='roles.for.biw.only');
--rollback DELETE FROM os_propertyset WHERE entity_name='roles.for.biw.only';

--changeset cornelius:79
--comment Insert system variable for Goalquest Details honeycomb gateway endpoint
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'honeycomb.endpoint.goalquest.details', 'Honeycomb Goalquest Details Endpoint', 'Honeycomb', 5, 0, 0, 'services/v1/goalquest/programDetails/${participantId}', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='honeycomb.endpoint.goalquest.details');
--rollback DELETE FROM os_propertyset WHERE entity_name='honeycomb.endpoint.goalquest.details';

--changeset cornelius:80
--comment Insert system variable for Goalquest Details honeycomb gateway endpoint
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'honeycomb.endpoint.sso.parameters', 'Honeycomb Get SSO Parameters Endpoint', 'Honeycomb', 5, 0, 0, 'services/v1/goalquest/sso/${userName}', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='honeycomb.endpoint.sso.parameters');
--rollback DELETE FROM os_propertyset WHERE entity_name='honeycomb.endpoint.sso.parameters';

--changeset cornelius:81
--comment Insert system variable for Get Client ID honeycomb gateway endpoint
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'honeycomb.endpoint.client.id', 'Honeycomb Get Client ID Endpoint', 'Honeycomb', 5, 0, 0, 'services/v1/client/getIdByCode/${clientCode}', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='honeycomb.endpoint.client.id');
--rollback DELETE FROM os_propertyset WHERE entity_name='honeycomb.endpoint.client.id';

--changeset palaniss:82
--comment Insert system variable for install wizard Environment URL
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'install.wizard.environment', 'Functionality', 'Environment URL', 5, 0, 0, 'http://beacon.biworldwide.com/g3installwizard/exportProject.do', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='install.wizard.environment');
--rollback DELETE FROM os_propertyset WHERE entity_name='install.wizard.environment';

--changeset siemback:83
--comment change value of regex from [^a-z]+|[^A-Z]+|[^0-9]+|\p{Punct}+ to (?=.*?\p{Lower})(?=.*?\p{Upper})(?=.*?\p{Digit})(?=.*?\p{Punct})
UPDATE os_propertyset SET STRING_VAL='(?=.*?\p{Lower})(?=.*?\p{Upper})(?=.*?\p{Digit})(?=.*?\p{Punct})' WHERE entity_name='password.not.match.regex';
--rollback UPDATE os_propertyset SET STRING_VAL='[^a-z]+|[^A-Z]+|[^0-9]+|\p{Punct}+' WHERE entity_name='password.match.regex';

--changeset siemback:84
--comment change value of regex  name from password.not.match.regex to password.match.regex
UPDATE os_propertyset SET entity_name='password.match.regex'  WHERE entity_name='password.not.match.regex';
--rollback UPDATE os_propertyset SET entity_name='password.not.match.regex' WHERE entity_name='password.match.regex';

--changeset siemback:85
--comment change value of  regex key name from Password Not Match Regex to Password Match Regex
UPDATE os_propertyset SET entity_key='Password Match Regex'  WHERE entity_name='password.match.regex';
--rollback UPDATE os_propertyset SET entity_key='Password Not Match Regex' WHERE entity_name='password.match.regex';

--changeset siemback:86
--comment Insert system variable for user token length welcome email
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'user.token.length.welcome.email', 'Welcome Email User Token Length', 'User Tokens', 2, 0, 0, null, 0, 40, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='user.token.length.welcome.email');
--rollback DELETE FROM os_propertyset WHERE entity_name='user.token.length.welcome.email';

--changeset siemback:87
--comment Insert system variable for user token length email
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'user.token.length.email', 'Email User Token Length', 'User Tokens', 2, 0, 0, null, 0, 40, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='user.token.length.email');
--rollback DELETE FROM os_propertyset WHERE entity_name='user.token.length.email';

--changeset siemback:88
--comment Insert system variable for user token length phone
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'user.token.length.phone', 'Phone User Token Length', 'User Tokens', 2, 0, 0, null, 0, 6, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='user.token.length.phone');
--rollback DELETE FROM os_propertyset WHERE entity_name='user.token.length.phone';

--changeset siemback:89
--comment Insert system variable for user token welcome email exipiration
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'user.token.expire.days.welcome.email', 'Welcome Email Token Expiration (days)', 'User Tokens', 3, 0, 0, null, 30, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='user.token.expire.days.welcome.email');
--rollback DELETE FROM os_propertyset WHERE entity_name='user.token.expire.days.welcome.email';

--changeset siemback:90
--comment Insert system variable for user token email exipiration
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'user.token.expire.hours.email', 'Email Token Expiration (hours)', 'User Tokens', 3, 0, 0, null, 4, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='user.token.expire.hours.email');
--rollback DELETE FROM os_propertyset WHERE entity_name='user.token.expire.hours.email';

--changeset siemback:91
--comment Insert system variable for user token Phone exipiration
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'user.token.expire.minutes.phone', 'Phone Token Expiration (minutes)', 'User Tokens', 3, 0, 0, null, 10, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='user.token.expire.minutes.phone');
--rollback DELETE FROM os_propertyset WHERE entity_name='user.token.expire.minutes.phone';

--changeset rajadura:92
--comment Insert system variable to fetch Past step data from UA microservice
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT entity_id_pk_sq.nextval,'bi.ua.microservice.past.days','Under Armour Micro Service Past Days','Under Armour',2,0,0,NULL,0,1,NULL,1,1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='bi.ua.microservice.past.days');
--rollback DELETE FROM os_propertyset WHERE entity_name='bi.ua.microservice.past.days';

--changeset panneers:93
--comment Insert system variable to deactive exist UA user information through browser call
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT entity_id_pk_sq.nextval,'bi.ua.session.logout.url','Under Armour session logout URL','Under Armour',5,0,0,'https://www.mapmyfitness.com/auth/logout',0,0,NULL,1,1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='bi.ua.session.logout.url');
--rollback DELETE FROM os_propertyset WHERE entity_name='bi.ua.session.logout.url';

--changeset siemback:94
--comment Insert system variable to for the password failure lock timout
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'password.lockout.timeout.minutes', 'Password Lockout Expiration Length (minutes)', 'Security', 3, 0, 0, null, 30, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='password.lockout.timeout.minutes');
--rollback DELETE FROM os_propertyset WHERE entity_name='password.lockout.timeout.minutes';

--changeset rajadura:95
--comment Insert system variable for PS V2 Consolidated Customer ID
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'partnersrvc.consolidated.customer.id', 'PS V2 Consolidated Customer ID', 'PS V2 Catalog SSO', 5, 0, 0, 'Change Me', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='partnersrvc.consolidated.customer.id');
--rollback DELETE FROM os_propertyset WHERE entity_name='partnersrvc.consolidated.customer.id';

--changeset rajadura:96
--comment Insert system variable for PS V2 Catalog Type
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'partnersrvc.catalogsso.catalog.type', 'PS V2 Catalog Type (MERCH or DIY)', 'PS V2 Catalog SSO', 5, 0, 0, 'MERCH', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='partnersrvc.catalogsso.catalog.type');
--rollback DELETE FROM os_propertyset WHERE entity_name='partnersrvc.catalogsso.catalog.type';

--changeset rajadura:97
--comment Insert system variable for PS V2 Catalog SSO Endpoint - Dev
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'partnersrvc.catalogsso.wsdl.url.dev', 'PS V2 Catalog SSO Endpoint - Dev', 'PS V2 Catalog SSO', 5, 0, 0, 'https://rewardservicesqa.biworldwide.com/partnerservices/services/v2/CatalogSSOWebService.biws?wsdl', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='partnersrvc.catalogsso.wsdl.url.dev');
--rollback DELETE FROM os_propertyset WHERE entity_name='partnersrvc.catalogsso.wsdl.url.dev';

--changeset rajadura:98
--comment Insert system variable for PS V2 Catalog SSO Endpoint - QA
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'partnersrvc.catalogsso.wsdl.url.qa', 'PS V2 Catalog SSO Endpoint - QA', 'PS V2 Catalog SSO', 5, 0, 0, 'https://rewardservicesqa.biworldwide.com/partnerservices/services/v2/CatalogSSOWebService.biws?wsdl', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='partnersrvc.catalogsso.wsdl.url.qa');
--rollback DELETE FROM os_propertyset WHERE entity_name='partnersrvc.catalogsso.wsdl.url.qa';

--changeset rajadura:99
--comment Insert system variable for PS V2 Catalog SSO Endpoint - Pre
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'partnersrvc.catalogsso.wsdl.url.preprod', 'PS V2 Catalog SSO Endpoint - Pre', 'PS V2 Catalog SSO', 5, 0, 0, 'https://rewardservicespprd.biworldwide.com/partnerservices/services/v2/CatalogSSOWebService.biws?wsdl', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='partnersrvc.catalogsso.wsdl.url.preprod');
--rollback DELETE FROM os_propertyset WHERE entity_name='partnersrvc.catalogsso.wsdl.url.preprod';

--changeset rajadura:100
--comment Insert system variable for PS V2 Catalog SSO Endpoint - Prod
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'partnersrvc.catalogsso.wsdl.url.prod', 'PS V2 Catalog SSO Endpoint - Prod', 'PS V2 Catalog SSO', 5, 0, 0, 'https://rewardservices.biworldwide.com/partnerservices/services/v2/CatalogSSOWebService.biws?wsdl', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='partnersrvc.catalogsso.wsdl.url.prod');
--rollback DELETE FROM os_propertyset WHERE entity_name='partnersrvc.catalogsso.wsdl.url.prod';

--changeset rajadura:101
--comment Insert system variable for PS V2 Client Non Prod Key Password
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'partnersrvc.client.key.password.nonprd', 'PS V2 Client Non Prod Key Password', 'PS V2 Catalog SSO', 5, 0, 0, 'Change Me', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='partnersrvc.client.key.password.nonprd');
--rollback DELETE FROM os_propertyset WHERE entity_name='partnersrvc.client.key.password.nonprd';

--changeset rajadura:102
--comment Insert system variable for PS V2 Client Prod Key Password
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'partnersrvc.client.key.password.prod', 'PS V2 Client Prod Key Password', 'PS V2 Catalog SSO', 5, 0, 0, 'Change Me', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='partnersrvc.client.key.password.prod');
--rollback DELETE FROM os_propertyset WHERE entity_name='partnersrvc.client.key.password.prod';

--changeset rajadura:103
--comment Insert system variable for PS V2 Client Non Prod Keystore Password
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'partnersrvc.client.store.password.nonprd', 'PS V2 Client Non Prod Keystore Password', 'PS V2 Catalog SSO', 5, 0, 0, 'Change Me', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='partnersrvc.client.store.password.nonprd');
--rollback DELETE FROM os_propertyset WHERE entity_name='partnersrvc.client.store.password.nonprd';

--changeset rajadura:104
--comment Insert system variable for PS V2 Client Prod Keystore Password
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'partnersrvc.client.store.password.prod', 'PS V2 Client Prod Keystore Password', 'PS V2 Catalog SSO', 5, 0, 0, 'Change Me', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='partnersrvc.client.store.password.prod');
--rollback DELETE FROM os_propertyset WHERE entity_name='partnersrvc.client.store.password.prod';

--changeset rajadura:105
--comment Insert system variable for PS V2 Server Keystore Password Non Prod
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'partnersrvc.server.store.password.nonprd', 'PS V2 Server Keystore Password Non prod', 'PS V2 Catalog SSO', 5, 0, 0, 'Change Me', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='partnersrvc.server.store.password.nonprd');
--rollback DELETE FROM os_propertyset WHERE entity_name='partnersrvc.server.store.password.nonprd';

--changeset rajadura:106
--comment Insert system variable for PS V2 Server Keystore Password Prod
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'partnersrvc.server.store.password.prod', 'PS V2 Server Keystore Password Prod', 'PS V2 Catalog SSO', 5, 0, 0, 'Change Me', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='partnersrvc.server.store.password.prod');
--rollback DELETE FROM os_propertyset WHERE entity_name='partnersrvc.server.store.password.prod';

--changeset rajadura:107
--comment Insert system variable for PS V2 AES 256 Key
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'partnersrvc.aes256.key', 'PS V2 AES 256 Key', 'PS V2 Catalog SSO', 5, 0, 0, 'Change Me', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='partnersrvc.aes256.key');
--rollback DELETE FROM os_propertyset WHERE entity_name='partnersrvc.aes256.key';

--changeset rajadura:108
--comment Insert system variable for PS V2 Init Vector
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'partnersrvc.init.vector', 'PS V2 Init Vector', 'PS V2 Catalog SSO', 5, 0, 0, 'Change Me', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='partnersrvc.init.vector');
--rollback DELETE FROM os_propertyset WHERE entity_name='partnersrvc.init.vector';

--changeset ramesh:109
--comment alter system variable table to drop primary key constraint
ALTER TABLE os_propertyset DROP CONSTRAINT os_propertyset_pk;
--rollback ALTER TABLE os_propertyset ADD CONSTRAINT os_propertyset_pk PRIMARY KEY (entity_id) USING INDEX;

--changeset ramesh:110
--comment alter system variable table to drop primary key constraint
ALTER TABLE os_propertyset ADD CONSTRAINT os_propertyset_pk PRIMARY KEY (entity_name) USING INDEX;
--rollback ALTER TABLE os_propertyset DROP CONSTRAINT os_propertyset_pk;

--changeset cornelius:111
--comment Insert system variable for Goalquest Manager Programs honeycomb gateway endpoint
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'honeycomb.endpoint.gq.managerprograms', 'Honeycomb Goalquest Manager Programs Endpoint', 'Honeycomb', 5, 0, 0, 'services/v1/goalquest/managerPrograms/${participantId}', 0, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='honeycomb.endpoint.gq.managerprograms');
--rollback DELETE FROM os_propertyset WHERE entity_name='honeycomb.endpoint.gq.managerprograms';

--changeset kancherla:112
--comment Insert system variable to allow My Groups option in sidebar for participant
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,GROUP_NAME)
SELECT entity_id_pk_sq.nextval, 'sidebar.allow.mygroups', 'Allow My Groups in sidebar', 1, 1, 0, null, 0, 0 , null, 'Functionality'  FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='sidebar.allow.mygroups');
--rollback DELETE FROM os_propertyset WHERE entity_name='sidebar.allow.mygroups';

--changeset ramesh:113
update os_propertyset set LONG_VAL=30 where ENTITY_NAME='password.otp.expiry.days'
--rollback update os_propertyset set LONG_VAL=86400000 where ENTITY_NAME='password.otp.expiry.days'

--changeset ramesh:114
update os_propertyset set LONG_VAL=365 where ENTITY_NAME='password.expired.period'
-- rollback update os_propertyset set LONG_VAL=100 where ENTITY_NAME='password.expired.period'

--changeset ramesh:115
update os_propertyset set INT_VAL=10 where ENTITY_NAME='password.history.count'
--rollback update os_propertyset set INT_VAL=5 where ENTITY_NAME='password.history.count'

--changeset cornelius:116
--comment This variable is stored in milliseconds, but displayed in days
update os_propertyset set LONG_VAL=31536000000 where ENTITY_NAME='password.expired.period'
-- rollback update os_propertyset set LONG_VAL=365 where ENTITY_NAME='password.expired.period'

--changeset cornelius:117
--comment Updating default password length to 14
update os_propertyset set INT_VAL=14 where ENTITY_NAME='password.min.length'
-- rollback update os_propertyset set INT_VAL=8 where ENTITY_NAME='password.min.length'

--changeset siemback:118
--comment This variable is used to determine the number of distinct character types for a password, or to use a custom regex if theh value is not between 1 and 4
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE, GROUP_NAME)
VALUES (ENTITY_ID_PK_SQ.nextval, 'password.num.of.char.types.required', '# of Character Types needed for password', 2, 0, 0, null, 0, 3, null, 1, 1, 'Security');
--rollback DELETE FROM os_propertyset WHERE entity_name='password.num.of.char.types.required';

--changeset cornelius:119
--comment This variable is used to determine which character types are available [as a requirement] for a password
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE, GROUP_NAME)
VALUES (ENTITY_ID_PK_SQ.nextval, 'password.character.types.available', 'Character types. upper,lower,number,special', 5, 0, 0, 'upper,lower,number,special', 0, 0, null, 1, 1, 'Security');
--rollback DELETE FROM os_propertyset WHERE entity_name='password.character.types.available';

--changeset cornelius:120
--comment Update description for Maximum clarity
UPDATE os_propertyset SET entity_key='# of Character Types needed. 1-4 is types, else regex.' where ENTITY_NAME='password.num.of.char.types.required';
--rollback UPDATE os_propertyset SET entity_key='# of Character Types needed for password' where ENTITY_NAME='password.num.of.char.types.required';

--changeset kothanda:121
--comment Update UA end point for dev environment
UPDATE os_propertyset SET STRING_VAL='https://qa.api.biw.cloud/v1/activity/under-armour/uamicroservices/rest' where ENTITY_NAME='bi.uaws.endpoint.url.dev';
--rollback UPDATE os_propertyset SET STRING_VAL='http://sample-env-2.j3zexxfnfb.ap-south-1.elasticbeanstalk.com/uamicroservices/rest' where ENTITY_NAME='bi.uaws.endpoint.url.dev';

--changeset kothanda:122
--comment Update UA end point for qa environment
UPDATE os_propertyset SET STRING_VAL='https://qa.api.biw.cloud/v1/activity/under-armour/uamicroservices/rest' where ENTITY_NAME='bi.uaws.endpoint.url.qa';
--rollback UPDATE os_propertyset SET STRING_VAL='http://sample-env-2.j3zexxfnfb.ap-south-1.elasticbeanstalk.com/uamicroservices/rest' where ENTITY_NAME='bi.uaws.endpoint.url.qa';

--changeset kothanda:123
--comment Update UA end point for pprd environment
UPDATE os_propertyset SET STRING_VAL='https://pprd.api.biw.cloud/v1/activity/under-armour/uamicroservices/rest' where ENTITY_NAME='bi.uaws.endpoint.url.preprod';
--rollback UPDATE os_propertyset SET STRING_VAL='http://uamicroservices-pprd.wzdcgsifm9.us-west-2.elasticbeanstalk.com/uamicroservices/rest' where ENTITY_NAME='bi.uaws.endpoint.url.preprod';

--changeset kothanda:124
--comment Update UA end point for prod environment
UPDATE os_propertyset SET STRING_VAL='https://api.biw.cloud/v1/activity/under-armour/uamicroservices/rest' where ENTITY_NAME='bi.uaws.endpoint.url.prod';
--rollback UPDATE os_propertyset SET STRING_VAL='http://uamicroservices-prod.wzdcgsifm9.us-west-2.elasticbeanstalk.com/uamicroservices/rest' where ENTITY_NAME='bi.uaws.endpoint.url.prod';

--changeset siemback:125
--comment Insert system variable for user token Email verification expiration
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'user.token.expire.hours.email.verify', 'Email Verification Expiration (Hours)', 'User Tokens', 3, 0, 0, null, 4, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='user.token.expire.hours.email.verify');
--rollback DELETE FROM os_propertyset WHERE entity_name='user.token.expire.hours.email.verify';

--changeset siemback:126
--comment Insert system variable for user token length email verification
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'user.token.length.email.verify', 'Email Verification User Token Length', 'User Tokens', 2, 0, 0, null, 0, 8, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='user.token.length.email.verify');
--rollback DELETE FROM os_propertyset WHERE entity_name='user.token.length.email.verify';

--changeset siemback:127
--comment update system variable for user token Email verification expiration
UPDATE os_propertyset SET LONG_VAL=24 where ENTITY_NAME='user.token.expire.hours.email.verify';
--rollback UPDATE os_propertyset SET LONG_VAL=4 where ENTITY_NAME='user.token.expire.hours.email.verify';

--changeset siemback:128
--comment Insert system variable for user token Phone verification expiration
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'user.token.expire.hours.phone.verify', 'Phone Verification Expiration (Minutes)', 'User Tokens', 3, 0, 0, null, 1440, 0, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='user.token.expire.hours.phone.verify');
--rollback DELETE FROM os_propertyset WHERE entity_name='user.token.expire.hours.phone.verify';

--changeset siemback:129
--comment Insert system variable for user token phone verify length
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE)
SELECT ENTITY_ID_PK_SQ.nextval, 'user.token.length.phone.verify', 'Phone Verify User Token Length', 'User Tokens', 2, 0, 0, null, 0, 6, null, 1, 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM os_propertyset WHERE entity_name='user.token.length.phone.verify');
--rollback DELETE FROM os_propertyset WHERE entity_name='user.token.length.phone.verify';

--changeset siemback:130
--comment update DEV Elasticsearch username
UPDATE os_propertyset SET STRING_VAL='admin' where ENTITY_NAME='elasticsearch.creds.username.dev';
--rollback not required

--changeset siemback:131
--comment update DEV Elasticsearch password
UPDATE os_propertyset SET STRING_VAL='biwpassword' where ENTITY_NAME='elasticsearch.creds.password.dev';
--rollback not required

--changeset siemback:132
--comment update QA Elasticsearch username
UPDATE os_propertyset SET STRING_VAL='admin' where ENTITY_NAME='elasticsearch.creds.username.qa';
--rollback not required

--changeset siemback:133
--comment update QA Elasticsearch password
UPDATE os_propertyset SET STRING_VAL='biwpassword' where ENTITY_NAME='elasticsearch.creds.password.qa';
--rollback not required