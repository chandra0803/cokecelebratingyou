--liquibase formatted sql

--changeset gorantla:1
--comment  Insert system variable history for Nackle Mesh Micor Services Credentials Dev  
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET where entity_name = 'nackle.mesh.services.client.id.dev'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name = 'nackle.mesh.services.client.id.dev';        
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'nackle.mesh.services.client.id.dev');

--changeset gorantla:2
--comment  Insert system variable history for Nackle Mesh Micor Services Credentials Dev   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name = 'nackle.mesh.services.secret.key.dev'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name = 'nackle.mesh.services.secret.key.dev';        
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'nackle.mesh.services.secret.key.dev');

--changeset gorantla:3
--comment  Insert system variable history for Nackle Mesh Micro Services Credentials Qa  
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name = 'nackle.mesh.services.client.id.qa'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name = 'nackle.mesh.services.client.id.qa';            
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'nackle.mesh.services.client.id.qa');

--changeset gorantla:4
--comment  Insert system variable history for Nackle Mesh Micro Services Credentials Qa 
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1)  FROM os_propertyset where entity_name = 'nackle.mesh.services.secret.key.qa'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name = 'nackle.mesh.services.secret.key.qa';        
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'nackle.mesh.services.secret.key.qa');

--changeset gorantla:5
--comment  Insert system variable history for Nackle Mesh Micro Services Credentials PPRD 
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name = 'nackle.mesh.services.client.id.preprod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name = 'nackle.mesh.services.client.id.preprod';        
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'nackle.mesh.services.client.id.preprod');

--changeset gorantla:6
--comment  Insert system variable history for Nackle Mesh Micro Services Credentials PPRD    
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name = 'nackle.mesh.services.secret.key.preprod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name = 'nackle.mesh.services.secret.key.preprod';        
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'nackle.mesh.services.secret.key.preprod');

--changeset gorantla:7
--comment  Insert system variable history for Nackle Mesh Micro Services Credentials PRD    
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name = 'nackle.mesh.services.client.id.prod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name = 'nackle.mesh.services.client.id.prod';    
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'nackle.mesh.services.client.id.prod');

--changeset gorantla:8
--comment  Insert system variable history for Nackle Mesh Micro Services Credentials PRD    
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name = 'nackle.mesh.services.secret.key.prod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name = 'nackle.mesh.services.secret.key.prod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'nackle.mesh.services.secret.key.prod');

--changeset gorantla:9
--comment  Insert system variable history for OTS service end point - DEV   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'ots.endpoint.url.dev'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'ots.endpoint.url.dev';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'ots.endpoint.url.dev');

--changeset gorantla:10
--comment  Insert system variable history for OTS service end point - QA   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'ots.endpoint.url.qa'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'ots.endpoint.url.qa';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'ots.endpoint.url.qa');

--changeset gorantla:11
--comment  Insert system variable history for OTS service end point - PPRD   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'ots.endpoint.url.preprod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'ots.endpoint.url.preprod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'ots.endpoint.url.preprod');

--changeset gorantla:12
--comment  Insert system variable history for OTS service end point - PRD   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'ots.endpoint.url.prod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'ots.endpoint.url.prod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'ots.endpoint.url.prod');

--changeset gorantla:13
--comment  Insert system variable history for OTS service system variable - DEV   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'ots.api.clientid.dev'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'ots.api.clientid.dev';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'ots.api.clientid.dev');

--changeset gorantla:14
--comment  Insert system variable history for OTS service system variable - QA   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'ots.api.clientid.qa'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'ots.api.clientid.qa';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'ots.api.clientid.qa');

--changeset gorantla:15
--comment  Insert system variable history for OTS service system variable - PPRD   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'ots.api.clientid.preprod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'ots.api.clientid.preprod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'ots.api.clientid.preprod');

--changeset gorantla:16
--comment  Insert system variable history for OTS service system variable - PRD   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'ots.api.clientid.prod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'ots.api.clientid.prod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'ots.api.clientid.prod');

--changeset gorantla:17
--comment  Insert system variable history for OTS service system variable - DEV   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'ots.api.secretkey.dev'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'ots.api.secretkey.dev';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'ots.api.secretkey.dev');

--changeset gorantla:18
--comment  Insert system variable history for OTS service system variable - QA   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'ots.api.secretkey.qa'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'ots.api.secretkey.qa';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'ots.api.secretkey.qa');

--changeset gorantla:19
--comment  Insert system variable history for OTS service system variable - PPRD   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'ots.api.secretkey.preprod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'ots.api.secretkey.preprod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'ots.api.secretkey.preprod');

--changeset gorantla:20
--comment  Insert system variable history for OTS service system variable - PRD   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'ots.api.secretkey.prod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'ots.api.secretkey.prod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'ots.api.secretkey.prod');

--changeset gorantla:21
--comment  Insert system variable history for MTC service system variable - DEV   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'mtc.endpoint.url.dev'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'mtc.endpoint.url.dev';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.endpoint.url.dev');

--changeset gorantla:22
--comment  Insert system variable history for MTC service system variable - QA   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'mtc.endpoint.url.qa'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'mtc.endpoint.url.qa';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.endpoint.url.qa');

--changeset gorantla:23
--comment  Insert system variable history for MTC service system variable - PPRD   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'mtc.endpoint.url.preprod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'mtc.endpoint.url.preprod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.endpoint.url.preprod');

--changeset gorantla:24
--comment  Insert system variable history for MTC service system variable - PRD   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'mtc.endpoint.url.prod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'mtc.endpoint.url.prod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.endpoint.url.prod');

--changeset gorantla:25
--comment  Insert system variable history for MTC service system variable - DEV   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'mtc.api.clientid.dev'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'mtc.api.clientid.dev';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.api.clientid.dev');

--changeset gorantla:26
--comment  Insert system variable history for MTC service system variable - QA   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'mtc.api.clientid.qa'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'mtc.api.clientid.qa';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.api.clientid.qa');

--changeset gorantla:27
--comment  Insert system variable history for MTC service system variable - PPRD   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'mtc.api.clientid.preprod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'mtc.api.clientid.preprod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.api.clientid.preprod');

--changeset gorantla:28
--comment  Insert system variable history for MTC service system variable - PRD   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'mtc.api.clientid.prod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'mtc.api.clientid.prod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.api.clientid.prod');

--changeset gorantla:29
--comment  Insert system variable history for MTC service system variable - DEV   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'mtc.api.secretkey.dev'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'mtc.api.secretkey.dev';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.api.secretkey.dev');

--changeset gorantla:30
--comment  Insert system variable history for MTC service system variable - QA   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'mtc.api.secretkey.qa'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'mtc.api.secretkey.qa';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.api.secretkey.qa');

--changeset gorantla:31
--comment  Insert system variable history for MTC service system variable - PPRD   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'mtc.api.secretkey.preprod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'mtc.api.secretkey.preprod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.api.secretkey.preprod');

--changeset gorantla:32
--comment  Insert system variable history for MTC service system variable - PRD   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'mtc.api.secretkey.prod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'mtc.api.secretkey.prod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'mtc.api.secretkey.prod');

--changeset gorantla:33
--comment  Insert system variable history for email footer for donot reply   
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'email.wrapper.footer'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'email.wrapper.footer';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'email.wrapper.footer');

--changeset gorantla:34
--comment  Insert system variable history for AWS Access Key for the S3 instance -DEV
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'aws.accessKey.dev'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'aws.accessKey.dev';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'aws.accessKey.dev');

--changeset gorantla:35
--comment  Insert system variable history for AWS Access Key for the S3 instance -QA
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'aws.accessKey.qa'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'aws.accessKey.qa';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'aws.accessKey.qa');

--changeset gorantla:36
--comment  Insert system variable history for AWS Access Key for the S3 instance -PPRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'aws.accessKey.preprod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'aws.accessKey.preprod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'aws.accessKey.preprod');

--changeset gorantla:37
--comment  Insert system variable history for AWS Access Key for the S3 instance -PRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'aws.accessKey.prod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'aws.accessKey.prod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'aws.accessKey.prod');

--changeset gorantla:38
--comment  Insert system variable history for AWS Secret Key for the S3 instance -DEV
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'aws.secretKey.dev'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'aws.secretKey.dev';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'aws.secretKey.dev');

--changeset gorantla:39
--comment  Insert system variable history for AWS Secret Key for the S3 instance -QA
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'aws.secretKey.qa'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'aws.secretKey.qa';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'aws.secretKey.qa');

--changeset gorantla:40
--comment  Insert system variable history for AWS Secret Key for the S3 instance -PPRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'aws.secretKey.preprod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'aws.secretKey.preprod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'aws.secretKey.preprod');

--changeset gorantla:41
--comment  Insert system variable history for AWS Secret Key for the S3 instance -PRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'aws.secretKey.prod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'aws.secretKey.prod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'aws.secretKey.prod');

--changeset gorantla:42
--comment  Insert system variable history for AWS S3 prefix key for file upload Mirrors cm3dam url in BIWS
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'aws.s3.image.prefix'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'aws.s3.image.prefix';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'aws.s3.image.prefix');

--changeset gorantla:43
--comment  Insert system variable history for aws avatar bucketname -DEV
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'aws.avatar.bucketname.dev'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'aws.avatar.bucketname.dev';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'aws.avatar.bucketname.dev');

--changeset gorantla:44
--comment  Insert system variable history for aws avatar bucketname -QA
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'aws.avatar.bucketname.qa'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'aws.avatar.bucketname.qa';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'aws.avatar.bucketname.qa');

--changeset gorantla:45
--comment  Insert system variable history for aws avatar bucketname -PPRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'aws.avatar.bucketname.preprod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'aws.avatar.bucketname.preprod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'aws.avatar.bucketname.preprod');

--changeset gorantla:46
--comment  Insert system variable history for aws avatar bucketname -PRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'aws.avatar.bucketname.prod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'aws.avatar.bucketname.prod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'aws.avatar.bucketname.prod');

--changeset gorantla:47
--comment  Insert system variable history for URL to PDF generation service String Value changes -QA
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'pdf.service.url.qa'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'pdf.service.url.qa';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'pdf.service.url.qa');

--changeset gorantla:48
--comment  Insert system variable history for URL to PDF generation service String Value changes -PPRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'pdf.service.url.preprod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'pdf.service.url.preprod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'pdf.service.url.preprod');

--changeset gorantla:49
--comment  Insert system variable history for URL to PDF generation service String Value changes -PRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'pdf.service.url.prod'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'pdf.service.url.prod';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'pdf.service.url.prod');

--changeset gorantla:50
--comment  Insert system variable history for fileload user role deletion
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM os_propertyset where entity_name =  'fileload.userrole.delete'
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name =  'fileload.userrole.delete';
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'fileload.userrole.delete');
  