--liquibase formatted sql

--changeset elizabet:1
--comment message table entry for Budget Transfer Giver message sent notification Pax
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM MESSAGE WHERE CM_ASSET_CODE = 'message_data.message.20000102'
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Budget Transfer Giver Message', 'message_data.message.20000102', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Budget Transfer Giver Message';

--changeset elizabet:2
--comment message table entry for Budget Transfer Receiver Message sent notification Pax
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM MESSAGE WHERE CM_ASSET_CODE = 'message_data.message.20000103'
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Budget Transfer Receiver Message', 'message_data.message.20000103', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Budget Transfer Receiver Message';

--changeset elizabet:3
--comment message table entry for Budget Transfer Process Message sent notification Pax
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM MESSAGE WHERE CM_ASSET_CODE = 'message_data.message.20000104'
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION) 
VALUES (MESSAGE_PK_SQ.nextVal, 'Budget Transfer Process Message', 'message_data.message.20000104', 'general', 'general', 'act', 0, sysdate, 0);
--rollback DELETE message WHERE message_name = 'Budget Transfer Process Message';

