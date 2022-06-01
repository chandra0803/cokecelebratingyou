INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, 
   STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES
   (message_pk_sq.nextval, 'Throwdown - Promotion Launch Notification', 'message_data.message.110822', 
   'td','promotion_launch', 'act', 'newPromotionStart', NULL, 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, 
   STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES
   (message_pk_sq.nextval, 'Participant - Promotion End Notification (Throwdown)', 'message_data.message.110823', 
   'td','promotion_end', 'act', 'promotionAboutToExpire', NULL, 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Throwdown Progress Updated Notification', 'message_data.message.110824', 'td','throwdown_progress_updated', 'act','goalReminders', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Throwdown Next Round Notification', 'message_data.message.110825', 'td','next_round', 'act', NULL, '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Throwdown Match Outcome Notification', 'message_data.message.110826', 'td','match_outcome', 'act', NULL, '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Throwdown Progress Updated Notification - Tie', 'message_data.message.110827', 'td','throwdown_progress_tie', 'act','goalReminders', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Throwdown Progress Updated Notification - Trail', 'message_data.message.110828', 'td','throwdown_progress_trail', 'act','goalReminders', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Throwdown PaxProgress Import Process', 'message_data.message.107684', 'general','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Throwdown Match Outcome Notification - Tie', 'message_data.message.110830', 'td','match_outcome_tie', 'act',NULL, '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Throwdown Match Outcome Notification - Trail', 'message_data.message.110831', 'td','match_outcome_trail', 'act',NULL, '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Throwdown Points Deposited Update', 'message_data.message.110829', 'td','throwdown_points_deposited', 'act',NULL, '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Throwdown - Manager Promotion Launch Notification', 'message_data.message.110832', 'td','throwdown_manager_promotion_launch', 'act',NULL, '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Throwdown - Manager Match Announcement Email', 'message_data.message.110833', 'td','throwdown_manager_match_announcement', 'act',NULL, '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Throwdown - Manager Match Progress Email', 'message_data.message.110834', 'td','throwdown_manager_progress_updated', 'act',NULL, '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Throwdown - Manager End of Round Email', 'message_data.message.110835', 'td','throwdown_manager_round_end', 'act',NULL, '0', SYSDATE, 0)   
/
INSERT INTO message (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(message_pk_sq.nextval, 'Throwdown - Promotion Launch Notification With Login Details', 'message_data.message.110836', 'td','throwdown_promotion_launch_with_login_detail', 'act',NULL, '0', SYSDATE, 0)
/
INSERT INTO message (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(message_pk_sq.nextval, 'Throwdown - Manager Promotion Launch Notification With Login Details', 'message_data.message.110837', 'td','throwdown_manager_promotion_launch_with_login_detail', 'act',NULL, '0', SYSDATE, 0)
/