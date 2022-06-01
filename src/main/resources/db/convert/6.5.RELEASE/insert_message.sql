--liquibase formatted sql

--changeset ramesh:1
--comment message table entry for Account unlock Notification message sent notification Pax
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM MESSAGE WHERE CM_ASSET_CODE = 'message_data.message.64000002'
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Account unlock Notification', 'message_data.message.64000002', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Account unlock Notification';


