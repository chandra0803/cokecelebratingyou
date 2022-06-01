--liquibase formatted sql

--changeset gorantla:1
--comment UPDATE column roster_user_id
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'APPLICATION_USER' AND COLUMN_NAME = 'ROSTER_USER_ID'
UPDATE APPLICATION_USER 
     SET ROSTER_USER_ID = FNC_RANDOMUUID, DATE_MODIFIED = SYSDATE, MODIFIED_BY = 652, VERSION = VERSION+1 
 WHERE ROSTER_USER_ID IS NULL;
--rollback not required

--changeset gorantla:2
--comment UPDATE column roster_hierarchy_id
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'HIERARCHY' AND COLUMN_NAME = 'ROSTER_HIERARCHY_ID'
UPDATE HIERARCHY 
     SET ROSTER_HIERARCHY_ID = FNC_RANDOMUUID, DATE_MODIFIED = SYSDATE, MODIFIED_BY = 652, VERSION = VERSION+1 
 WHERE ROSTER_HIERARCHY_ID IS NULL;
--rollback not required   

--changeset gorantla:3
--comment UPDATE column roster_node_id
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'NODE' AND COLUMN_NAME = 'ROSTER_NODE_ID'      
UPDATE NODE 
     SET ROSTER_NODE_ID = FNC_RANDOMUUID, DATE_MODIFIED = SYSDATE, MODIFIED_BY = 652, VERSION = VERSION+1 
 WHERE ROSTER_NODE_ID IS NULL;
--rollback not required   

--changeset gorantla:4
--comment UPDATE column roster_audience_id
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'AUDIENCE' AND COLUMN_NAME = 'ROSTER_AUDIENCE_ID'
UPDATE AUDIENCE 
     SET ROSTER_AUDIENCE_ID = FNC_RANDOMUUID, DATE_MODIFIED = SYSDATE, MODIFIED_BY = 652, VERSION = VERSION+1 
 WHERE ROSTER_AUDIENCE_ID IS NULL;
--rollback not required  

--changeset gorantla:5
--comment UPDATE column roster_phone_id
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'USER_PHONE' AND COLUMN_NAME = 'ROSTER_PHONE_ID'
UPDATE USER_PHONE 
     SET ROSTER_PHONE_ID = FNC_RANDOMUUID, DATE_MODIFIED = SYSDATE, MODIFIED_BY = 652, VERSION = VERSION+1 
 WHERE ROSTER_PHONE_ID IS NULL;
--rollback not required    

--changeset gorantla:6
--comment UPDATE  column roster_email_id
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'USER_EMAIL_ADDRESS' AND COLUMN_NAME = 'ROSTER_EMAIL_ID'
UPDATE USER_EMAIL_ADDRESS 
     SET ROSTER_EMAIL_ID = FNC_RANDOMUUID, DATE_MODIFIED = SYSDATE, MODIFIED_BY = 652, VERSION = VERSION+1 
 WHERE ROSTER_EMAIL_ID IS NULL;
--rollback not required    

--changeset gorantla:7
--comment UPDATE  column roster_address_id
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'USER_ADDRESS' AND COLUMN_NAME = 'ROSTER_ADDRESS_ID'
UPDATE USER_ADDRESS 
     SET ROSTER_ADDRESS_ID = FNC_RANDOMUUID, DATE_MODIFIED = SYSDATE, MODIFIED_BY = 652, VERSION = VERSION+1 
 WHERE ROSTER_ADDRESS_ID IS NULL;
--rollback not required    

--changeset gorantla:8
--comment UPDATE  column roster_characteristic_id
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'CHARACTERISTIC' AND COLUMN_NAME = 'ROSTER_CHARACTERISTIC_ID'
UPDATE CHARACTERISTIC 
     SET ROSTER_CHARACTERISTIC_ID = FNC_RANDOMUUID, DATE_MODIFIED = SYSDATE, MODIFIED_BY = 652, VERSION = VERSION+1 
 WHERE ROSTER_CHARACTERISTIC_ID IS NULL;
--rollback not required    

--changeset gorantla:9
--comment UPDATE column roster_device_id
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'USER_DEVICE' AND COLUMN_NAME = 'ROSTER_DEVICE_ID'
UPDATE USER_DEVICE 
   SET ROSTER_DEVICE_ID = FNC_RANDOMUUID, DATE_MODIFIED = SYSDATE, MODIFIED_BY = 652, VERSION = VERSION+1 
 WHERE ROSTER_DEVICE_ID IS NULL;
--rollback not required     
           
--changeset gorantla:10
--comment UPDATE column roster_user_char_id
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'USER_CHARACTERISTIC' AND COLUMN_NAME = 'ROSTER_USER_CHAR_ID'
UPDATE USER_CHARACTERISTIC 
   SET ROSTER_USER_CHAR_ID = FNC_RANDOMUUID, DATE_MODIFIED = SYSDATE, MODIFIED_BY = 652, VERSION = VERSION+1 
 WHERE ROSTER_USER_CHAR_ID IS NULL;
--rollback not required

--changeset gorantla:11
--comment UPDATE column roster_node_char_id
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'NODE_CHARACTERISTIC' AND COLUMN_NAME = 'ROSTER_NODE_CHAR_ID'
UPDATE NODE_CHARACTERISTIC 
   SET ROSTER_NODE_CHAR_ID = FNC_RANDOMUUID, DATE_MODIFIED = SYSDATE, MODIFIED_BY = 652, VERSION = VERSION+1 
 WHERE ROSTER_NODE_CHAR_ID IS NULL;
--rollback not required	 
