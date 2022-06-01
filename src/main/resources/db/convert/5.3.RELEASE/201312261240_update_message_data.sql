DELETE FROM message WHERE cm_asset_code = 'message_data.message.103981'
/
DELETE FROM message WHERE cm_asset_code = 'message_data.message.103829'
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Budget Reminder Process','message_data.message.13040','general','general','act',NULL,0,sysdate,null,null,2)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Budget Reminder Notification','message_data.message.10000933','rec','budget_reminder','act','budgetReminderAlerts', NULL,0,sysdate,null,null,0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE,CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'DIY Quiz Notify Participant Message', 'message_data.message.10000826', 'quiz','promotion_launch', 'act','promotionalMessages', 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Award File Launch Manager Message', 'message_data.message.100862', 'general', 'general', 'act', 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Award File Launch Admin Message', 'message_data.message.100863', 'general', 'general', 'act', 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
                     status_code, date_last_sent, created_by, date_created,
                     modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval, 'Sweepstakes Award Process', 'message_data.message.28585', 'general','general', 'act', null, 0, sysdate, null, null, 0)
/