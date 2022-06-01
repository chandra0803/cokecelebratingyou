--liquibase formatted sql

--changeset gorantla:1
--comment  Insert system variable history for webdav Dev  
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET where entity_name = 'global.fileprocessing.webdav.dev';
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name = 'global.fileprocessing.webdav.dev';      
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'global.fileprocessing.webdav.dev');

--changeset gorantla:2
--comment  Insert system variable history for webdav QA  
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET where entity_name = 'global.fileprocessing.webdav.qa';
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name = 'global.fileprocessing.webdav.qa';      
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'global.fileprocessing.webdav.qa');

--changeset gorantla:3
--comment  Insert system variable history for webdav PREPROD  
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET where entity_name = 'global.fileprocessing.webdav.preprod';
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name = 'global.fileprocessing.webdav.preprod';      
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'global.fileprocessing.webdav.preprod');

--changeset gorantla:4
--comment  Insert system variable history for webdav PROD  
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET where entity_name = 'global.fileprocessing.webdav.prod';
INSERT INTO os_propertyset_history
SELECT os_propertyset_tracking_sq.nextval,entity_id,entity_name,entity_key,key_type,boolean_val,double_val,string_val, long_val, int_val,null, is_editable,is_viewable, group_name, sysdate, 65
  FROM os_propertyset where entity_name = 'global.fileprocessing.webdav.prod';      
--rollback DELETE FROM os_propertyset_history WHERE os_propertyset_history_id = (SELECT max(os_propertyset_history_id) FROM os_propertyset_history WHERE ENTITY_NAME = 'global.fileprocessing.webdav.prod');