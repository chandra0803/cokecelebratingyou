INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Participant - Public Recognition Add Points Notification','message_data.message.138807','rec','public_recognition_received','act',NULL,NULL,0,sysdate,null,null,0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Participant - RPM Metric Update Message', 'message_data.message.10022001', 'eng', 'participant_kpm_metric_update', 'act', 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Managers - RPM Metric Update Message', 'message_data.message.10022002', 'eng', 'manager_kpm_metric_update', 'act', 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Engagement Notification Summary Message', 'message_data.message.20001720', 'eng', 'general', 'act', 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
      (message_pk_sq.nextval,'Leaderboard Import process','message_data.message.20000005','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
                     status_code, date_last_sent, created_by, date_created,
                     modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval, 'Engagement Refresh Process Message', 'message_data.message.156489', 'eng','general', 'act', null, 0, sysdate, null, null, 0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
                     status_code, date_last_sent, created_by, date_created,
                     modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval, 'RPM Notification Email Message For Team', 'message_data.message.20001620', 'eng','general', 'act', null, 0, sysdate, null, null, 0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
                     status_code, date_last_sent, created_by, date_created,
                     modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval, 'RPM Notification Email Message For User', 'message_data.message.20001640', 'eng','general', 'act', null, 0, sysdate, null, null, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, sms_group_type, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Celebration - Manager Notification Message', 'message_data.message.10021841', 'rec','celebration_manager_notification', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, sms_group_type, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Celebration - Manager Nonresponse Message', 'message_data.message.10021842', 'rec','celebration_manager_nonresponse', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, sms_group_type, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Celebration - Recognition Received Message', 'message_data.message.10021843', 'rec','celebration_recognition_received', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, sms_group_type, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Celebration - Purl Recipient Invitation Message', 'message_data.message.10021844', 'rec','celebration_purl_recipient_invitation', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/