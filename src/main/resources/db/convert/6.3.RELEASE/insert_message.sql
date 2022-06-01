--liquibase formatted sql

--changeset mattam:1
--comment message table entry for Forgot Login ID Notify Pax
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Forgot Login ID Notify Pax', 'message_data.message.10000303', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Forgot Login ID Notify Pax';

--changeset mattam:2
--comment message table entry for Forgot Password Notify Pax
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Forgot Password Notify Pax', 'message_data.message.10000304', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Forgot Password Notify Pax';

--changeset mattam:3
--comment message table entry for Password Change Notify Pax
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Password Change Notify Pax', 'message_data.message.10000305', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Password Change Notify Pax';

--changeset siemback:4
--comment message table entry for Participant Activation Notification
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Participant Activation Notification', 'message_data.message.10000306', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Participant Activation Notification';

--changeset sivanand:5
--comment message table entry for Award Error Policy Notification
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Award Error Policy', 'message_data.message.10000307', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Award Error Policy';

--changeset subramap:6
--comment Message table entry for sending employee password for security patch 3
INSERT INTO MESSAGE(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE,STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES (message_pk_sq.nextval, 'Reissue Employee Password', 'message_data.message.105020', 'general', 'general','act', 0, sysdate, 0);
--rollback DELETE message where message_name ='Reissue Employee Password';

--changeset siemback:7
--comment message table entry for Forgot Login ID message sent notification Pax
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Forgot Login ID Message Sent Notification', 'message_data.message.10000310', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Forgot Login ID Message Sent Notification';

--changeset bethke:8
--comment message table entry for Merch Order Gift Code Retry Process
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Merch Order Gift Code Retry Process', 'message_data.message.10000320', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Merch Order Gift Code Retry Process';

--changeset siemback:9
--comment message table entry for Account Activation Notification message sent notification Pax
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Account Activation Notification', 'message_data.message.10000311', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Account Activation Notification';

--changeset subramap:10
--comment removing reissue password related message
delete from message where cm_asset_code='message_data.message.105001';
--rollback not required

--changeset siemback:11
--comment remove Welcome Message Audience
DELETE FROM WELCOME_MESSAGE_AUDIENCE WHERE WELCOME_MESSAGE_ID IN ( SELECT WELCOME_MESSAGE_ID FROM WELCOME_MESSAGE WHERE MESSAGE_ID IN ( SELECT MESSAGE_ID from MESSAGE WHERE cm_asset_code='message_data.message.15571' AND message_type_code='welcome_message') );
--rollback  not required

--changeset siemback:12
--comment remove Welcome Message 
DELETE FROM WELCOME_MESSAGE WHERE MESSAGE_ID IN ( SELECT MESSAGE_ID from MESSAGE WHERE cm_asset_code='message_data.message.15571' AND message_type_code='welcome_message') ;
--rollback  not required

--changeset siemback:13
--comment remove Welcome Message Audience
DELETE FROM WELCOME_MESSAGE_AUDIENCE WHERE WELCOME_MESSAGE_ID IN ( SELECT WELCOME_MESSAGE_ID FROM WELCOME_MESSAGE WHERE MESSAGE_ID IN ( SELECT MESSAGE_ID from MESSAGE WHERE cm_asset_code='message_data.message.12556' AND message_type_code='welcome_message') );
--rollback  not required

--changeset siemback:14
--comment remove Welcome Message 
DELETE FROM WELCOME_MESSAGE WHERE MESSAGE_ID IN ( SELECT MESSAGE_ID from MESSAGE WHERE cm_asset_code='message_data.message.12556' AND message_type_code='welcome_message') ;
--rollback INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, date_last_sent, created_by, date_created, modified_by, date_modified, version) VALUES (message_pk_sq.nextval,'Welcome Password','message_data.message.15571','wm','welcome_message','act',NULL,0,sysdate,null,null,0);

--changeset siemback:15
--comment remove Welcome email messages with either password or username
DELETE FROM MESSAGE WHERE cm_asset_code='message_data.message.15571' AND message_type_code='welcome_message';
--rollback  not required

--changeset siemback:16
--comment remove Welcome email messages with either password or username
DELETE FROM MESSAGE WHERE cm_asset_code='message_data.message.12556' AND message_type_code='welcome_message';
--rollback  not required

--changeset siemback:17
--comment message table entry for Recovery Method Verification
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Account Recovery Verification Message', 'message_data.message.155660000', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Account Recovery Verification Message';

--changeset cornelius:18
--comment message table entry for Recovery Change Notification
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Account Recovery Change Message', 'message_data.message.155660001', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Account Recovery Change Message';
