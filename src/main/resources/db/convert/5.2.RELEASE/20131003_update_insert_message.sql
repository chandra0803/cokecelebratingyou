UPDATE MESSAGE SET MESSAGE_NAME='Giver/Budget - Inactivity Notification (Recognition)', SMS_GROUP_TYPE='promotionalMessages' 
	WHERE cm_asset_code='message_data.message.14090'
/
UPDATE MESSAGE SET MESSAGE_NAME='Product Claim - Inactivity Notification (Product Claim)', SMS_GROUP_TYPE='promotionalMessages' 
	WHERE cm_asset_code='message_data.message.14095'
/
UPDATE MESSAGE SET MESSAGE_NAME='Participant - Inactivity Notification (Quiz)', SMS_GROUP_TYPE='promotionalMessages' 
	WHERE cm_asset_code='message_data.message.14100'
/
UPDATE MESSAGE SET MESSAGE_NAME='Nominator - Inactivity Notification (Nomination)', SMS_GROUP_TYPE='promotionalMessages' 
	WHERE cm_asset_code='message_data.message.104087'
/
UPDATE MESSAGE SET MESSAGE_NAME='Approver Reminder (Recognition)', SMS_GROUP_TYPE='promotionalMessages' 
	WHERE cm_asset_code='message_data.message.105370'
/
UPDATE MESSAGE SET MESSAGE_NAME='Product Claim - Approver Reminder (Product Claim)', SMS_GROUP_TYPE='promotionalMessages' 
	WHERE cm_asset_code='message_data.message.105371'
/
UPDATE MESSAGE SET MESSAGE_NAME='Approver Reminder (Nomination)', SMS_GROUP_TYPE='promotionalMessages' 
	WHERE cm_asset_code='message_data.message.105372'
/
UPDATE MESSAGE SET MESSAGE_NAME='Plateau Non-redemption Reminder (Recognition)', SMS_GROUP_TYPE='promotionalMessages' 
	WHERE cm_asset_code='message_data.message.110961'
/
INSERT INTO MESSAGE 
  ( MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE,
  STATUS_CODE,SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION )
VALUES 
   (message_pk_sq.nextval, 'Challengepoint partner Goal Achieved', 'message_data.message.128004', 
   'cp','cp_partner_goal_achieved', 'act', NULL, NULL, 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE,
   STATUS_CODE ,SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES
   (message_pk_sq.nextval, 'Challengepoint Partner Goal Not Achieved', 'message_data.message.128005',
   'cp','cp_partner_goal_not_achieved', 'act', NULL, NULL, 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, 
   STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES
   (message_pk_sq.nextval, 'Challengepoint Partner Progress Updated', 'message_data.message.128002', 
   'cp','cp_partner_progress_updated', 'act','goalReminders', NULL, 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, 
   STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES
   (message_pk_sq.nextval, 'Challengepoint partner Selected', 'message_data.message.128001', 
   'cp', 'cp_partner_goal_selected', 'act','goalReminders', NULL, 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Plateau Awards Reminder Message', 'message_data.message.10000823', 'gq','general', 'act','promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, 
   STATUS_CODE, SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES
   (message_pk_sq.nextval, 'ChallengePoint - Non redemption reminder', 'message_data.message.110821', 
   'cp','cp_non_redemption_reminder', 'act', NULL, NULL, 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Award File Extract Message', 'message_data.message.10000861', 'general', 'general', 
    'act', 5662,  sysdate, NULL, NULL, 0)
/