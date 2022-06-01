UPDATE MESSAGE SET MESSAGE_NAME = 'Nominee - Nomination Submitted', SMS_GROUP_TYPE = 'promotionalMessages' WHERE CM_ASSET_CODE ='message_data.message.103084'
/
UPDATE MESSAGE SET MESSAGE_NAME = 'Nominee''s Manager Nomination Submitted' WHERE CM_ASSET_CODE ='message_data.message.103085'
/
UPDATE MESSAGE SET MESSAGE_NAME = 'Approver - Nomination Submitted' WHERE CM_ASSET_CODE ='message_data.message.103089'
/
UPDATE MESSAGE SET MESSAGE_NAME = 'Nominee - Winner Notification' WHERE CM_ASSET_CODE ='message_data.message.103090'
/
UPDATE MESSAGE SET MESSAGE_NAME = 'Nominee''s Manager - Winner Notification' WHERE CM_ASSET_CODE ='message_data.message.103091'
/
UPDATE MESSAGE SET MESSAGE_NAME = 'Promotion Launch Notification' WHERE CM_ASSET_CODE ='message_data.message.104088'
/
UPDATE MESSAGE SET MESSAGE_NAME = 'Nominator - Winner Notification', SMS_GROUP_TYPE = 'promotionalMessages' WHERE CM_ASSET_CODE ='message_data.message.104099'
/
UPDATE MESSAGE SET MESSAGE_NAME = 'Approver Reminder' WHERE CM_ASSET_CODE ='message_data.message.105372'
/
INSERT INTO MESSAGE
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(message_pk_sq.nextval, 'Budget Redistribution Import Process Message', 'message_data.message.10000104', 'general', 'general', 'act', NULL, NULL, 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(message_pk_sq.nextval, 'Update Cash Currencies Summary Message', 'message_data.message.10000110', 'general', 'general', 'act', NULL, NULL, 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(message_pk_sq.nextval, 'Nomination Custom Appoover Import Process', 'message_data.message.10000111', 'general', 'general', 'act', NULL, NULL, 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(message_pk_sq.nextval, 'Approver Notification', 'message_data.message.10000128', 'nom', 'approver_notification', 'act', 'promotionalMessages', NULL, 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(message_pk_sq.nextval, 'Refresh Pending Nomination Approver Process', 'message_data.message.10000129', 'general', 'general', 'act', NULL, NULL, 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(message_pk_sq.nextval, 'Approver Reminder - Time Period Expired', 'message_data.message.104092', 'nom', 'approver_reminder_tp_expired', 'act', 'promotionalMessages', NULL, 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(message_pk_sq.nextval, 'Approver Reminder - Approval End Date', 'message_data.message.104093', 'nom', 'approver_reminder_approval_end_date', 'act', 'promotionalMessages', NULL, 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(message_pk_sq.nextval, 'Approver - Request For More Information Received', 'message_data.message.104094', 'nom', 'approver_request_more_information', 'act', 'promotionalMessages', NULL, 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(message_pk_sq.nextval, 'Nominator - Request For More Information', 'message_data.message.104095', 'nom', 'nominator_request_more_information', 'act', 'promotionalMessages', NULL, 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(message_pk_sq.nextval, 'Nominator - Non Winner Notification', 'message_data.message.104098', 'nom', 'to_nominator_non_winner', 'act', 'promotionalMessages', NULL, 0, SYSDATE, 0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Post Process Retry Mailing','message_data.message.20000101','general','general','act',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Mailing Retry Process','message_data.message.20000100','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Request For More Budget','message_data.message.104062','nom','nomination_request_more_budget','act',NULL,0,sysdate,null,null,0)
/