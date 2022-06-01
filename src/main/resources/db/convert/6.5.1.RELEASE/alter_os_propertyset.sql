--liquibase formatted sql

--changeset gorantla:1
--comment update domain changes for DEV
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'global.fileprocessing.webdav.dev';
UPDATE os_propertyset 
   SET string_val = 'https://workqa.corp.biworldwide.com:8443/qa', modified_by = 65, date_modified = sysdate WHERE entity_name = 'global.fileprocessing.webdav.dev';
--rollback UPDATE os_propertyset SET string_val = 'https://workqa.bius.bi.corp:8443/qa' WHERE entity_name = 'global.fileprocessing.webdav.dev';

--changeset gorantla:2
--comment update domain changes for QA
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'global.fileprocessing.webdav.qa';
UPDATE os_propertyset 
   SET string_val = 'https://workqa.corp.biworldwide.com/qa', modified_by = 65, date_modified = sysdate WHERE entity_name = 'global.fileprocessing.webdav.qa';
--rollback UPDATE os_propertyset SET string_val = 'https://workqa.bius.bi.corp/qa' WHERE entity_name = 'global.fileprocessing.webdav.qa';

--changeset gorantla:3
--comment update domain changes for PRE
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'global.fileprocessing.webdav.preprod';
UPDATE os_propertyset 
   SET string_val = 'https://workpre.corp.biworldwide.com/pre', modified_by = 65, date_modified = sysdate WHERE entity_name = 'global.fileprocessing.webdav.preprod';
--rollback UPDATE os_propertyset SET string_val = 'https://workpre.bius.bi.corp/pre' WHERE entity_name = 'global.fileprocessing.webdav.preprod';

--changeset gorantla:4
--comment update domain changes for PRD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'global.fileprocessing.webdav.prod';
UPDATE os_propertyset 
   SET string_val = 'https://work.corp.biworldwide.com/prd', modified_by = 65, date_modified = sysdate WHERE entity_name = 'global.fileprocessing.webdav.prod';
--rollback UPDATE os_propertyset SET string_val = 'https://work.bius.bi.corp/prd' WHERE entity_name = 'global.fileprocessing.webdav.prod';

--changeset gorantla:5
--comment  Add dm contextname  for datatransporter
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM OS_PROPERTYSET WHERE ENTITY_NAME = 'dm.context.name'
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,GROUP_NAME,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL,IS_EDITABLE,IS_VIEWABLE,CREATED_BY,DATE_CREATED)
VALUES (ENTITY_ID_PK_SQ.nextval, 'dm.context.name', 'DM Context Name', 'DM Context Name', 5, 0, 0, 'CHANGE ME!', 0, 0, null, 1, 1, 6.5,sysdate);
--rollback DELETE FROM os_propertyset WHERE entity_name='dm.context.name';
