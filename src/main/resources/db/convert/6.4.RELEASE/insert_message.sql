--liquibase formatted sql

--changeset rajadura:1
--comment message Recognition Advisor for New Hire Email Process
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Recognition Advisor NewHire Notification to Manager', 'message_data.message.10000492', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Recognition Advisor NewHire Notification to Manager';

--changeset ramesh:2
--comment message table entry for RA Team member message sent notification Pax
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Recognition Advisor TeamMember Notification to Manager', 'message_data.message.10000491', 'Recognition', 'Recognition', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Recognition Advisor TeamMember Notification to Manager';

--changeset ramesh:3
--comment message table entry for RA welcom email message sent notification to manager
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Recognition Advisor Welcome email Notification to Manager', 'message_data.message.10000493', 'Recognition', 'Recognition', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Recognition Advisor Welcome email Notification to Manager';

--changeset sivanand:4
--comment message entry for RA welcom email message sent notification to manager
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'RA Welcome eMail Message', 'message_data.message.64000001', 'Recognition', 'Recognition', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'RA Welcome eMail Message';

--changeset esakkimu:5
--comment message entry for InActivate Biw Users Process Notification Message
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'InActivate Biw Users Process notification ', 'message_data.message.10000501', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'InActivate Biw Users Process notification' 


