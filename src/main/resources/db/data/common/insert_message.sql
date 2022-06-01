INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'ADDITIONAL INFORMATION REQUESTED','message_data.message.12288','idea','general','ina',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, sms_group_type, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Participant - Claim Reviewed','message_data.message.12338','prd','participant_claim_reviewed','act', 'depositNotifications',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Participant - Passed Quiz','message_data.message.12352','quiz','general','act','promotionalMessages',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Approver - New Pending Recognition','message_data.message.12356','rec','approver_recognition_submitted','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Giver - Recognition Status Update','message_data.message.12380','rec','giver_recognition_approval_decision','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Approver - New Pending Claim','message_data.message.12342','prd','approver_claim_submitted','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Comm Log Escalation','message_data.message.12540','comm','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'eStatement','message_data.message.12544','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Budget Import Process','message_data.message.101449','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Deposit Import Process','message_data.message.101450','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Hierarchy Import Process','message_data.message.101451','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'InstantPoll Notify Participant Notification Message','message_data.message.152552','svy','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Participant Import Process','message_data.message.17078','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Product Import Process','message_data.message.101387','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Deposit Notice','message_data.message.12548','general','deposit','act','depositNotifications',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Deposit Notice Process','message_data.message.15594','general','general','act',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Promo Announce','message_data.message.12552','general','general','act','promotionalMessages',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Welcome Login and Password','message_data.message.12556','wm','welcome_message','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Welcome Login','message_data.message.15567','wm','welcome_message','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Welcome Password','message_data.message.15571','wm','welcome_message','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Escalated Comm Log:Original Assigned User','message_data.message.12868','comm','general','act',NULL,0,sysdate,null,null,2)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Escalated Comm Log Email:New Assigned User','message_data.message.12874','comm','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Manager Override Process Notification','message_data.message.12892','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Proactive Notifications Process','message_data.message.13030','general','general','act',NULL,0,sysdate,null,null,2)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Budget Reminder Process','message_data.message.13040','general','general','act',NULL,0,sysdate,null,null,2)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Bank Account Enrollment Process','message_data.message.13034','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Bank Account Update Process','message_data.message.13038','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Batch Promotion Process','message_data.message.13042','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'e-Statement Process','message_data.message.13046','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Welcome Email Process','message_data.message.13050','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Delayed Approvals Process','message_data.message.13054','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Bounce Back Email Verification','message_data.message.105983','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Sweepstakes Email','message_data.message.15971','general','general','act',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Escalate Comm Logs Process','message_data.message.13058','comm','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Receiver - Recognition Received','message_data.message.13807','rec','recognition_received','act','recognitionReceived',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Giver/Budget - Inactivity Notification (Recognition)','message_data.message.14090','rec','participant_inactivity','act','promotionalMessages',NULL,0,sysdate,null,null,2)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, SMS_GROUP_TYPE,date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Product Claim - Inactivity Notification (Product Claim)','message_data.message.14095','prd','participant_inactivity','act','promotionalMessages',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE,date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Participant - Inactivity Notification (Quiz)','message_data.message.14100','quiz','participant_inactivity','act','promotionalMessages',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE,date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Participant - Inactivity Notification (Survey)','message_data.message.14101','svy','participant_inactivity','act','promotionalMessages',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE,date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Nominator - Inactivity Notification (Nomination)','message_data.message.104087','nom','participant_inactivity','act','promotionalMessages',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Participant - Promotion End Notification (Recognition)','message_data.message.14137','rec','promotion_end','act','promotionalMessages',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Participant - Promotion End Alert (Product Claim)','message_data.message.14282','prd','promotion_end','act','promotionalMessages',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Participant - Promotion End Notification (Quiz)','message_data.message.14286','quiz','promotion_end','act','promotionalMessages',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Participant - Promotion End Notification (Nomination)','message_data.message.104089','nom','promotion_end','act','promotionalMessages',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Participant - Promotion End Alert (Survey)','message_data.message.105366','svy','promotion_end','act','promotionalMessages',NULL,0,sysdate,null,null,1)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE,CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Approver Reminder (Recognition)', 'message_data.message.105370', 'rec','approver_reminder', 'act','promotionalMessages',0, SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE,CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Product Claim - Approver Reminder (Product Claim)', 'message_data.message.105371', 'prd','approver_reminder', 'act','promotionalMessages',0, SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Approver Reminder', 'message_data.message.105372', 'nom','approver_reminder', 'act', 'promotionalMessages', 0, SYSDATE, 0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Administrator - eCard Selection Needed','message_data.message.15662','rec','general','act',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'FileLoad Verify Process Message','message_data.message.17240','general','general','act',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
                     status_code, date_last_sent, created_by, date_created,
                     modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval, 'Report Refresh Process', 'message_data.message.17681', 'general','general', 'act', null, 0, sysdate, null, null, 0)
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
(message_pk_sq.nextval, 'RPM Notification Email Message For Team', 'message_data.message.10001620', 'eng','general', 'act', null, 0, sysdate, null, null, 0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
                     status_code, date_last_sent, created_by, date_created,
                     modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval, 'RPM Notification Email Message For User', 'message_data.message.10001640', 'eng','general', 'act', null, 0, sysdate, null, null, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Participant - Promotion Announcement (Recognition)', 'message_data.message.17701', 'rec','promotion_launch', 'act', 'promotionalMessages',0, SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE,CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Participant - Promotion Announcement (Product Claim)', 'message_data.message.17706', 'prd','promotion_launch', 'act','promotionalMessages',0, SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE,CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Participant - Promotion Announcement (Quiz)', 'message_data.message.17710', 'quiz','promotion_launch', 'act','promotionalMessages', 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE,CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Promotion Launch Notification', 'message_data.message.104088', 'nom','promotion_launch', 'act','promotionalMessages', 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE,CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Participant - Promotion Announcement (Survey)', 'message_data.message.105365', 'svy','promotion_launch', 'act','promotionalMessages', 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Node Approval Configuration Errors', 'message_data.message.101813', 'general','general', 'act', 0, SYSDATE, 0)
/
INSERT INTO message
    (message_id, message_name, cm_asset_code, module_code, message_type_code, status_code, date_last_sent,
    date_created, date_modified, version, created_by, modified_by)
VALUES
    (message_pk_sq.nextval, 'Stack Rank Creation', 'message_data.message.102238', 'general','general', 'act', null, SYSDATE,
    null, 0, 5662, null)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, DATE_CREATED, VERSION, CREATED_BY)
VALUES
   (message_pk_sq.nextval, 'Product Claim Import Process', 'message_data.message.102261', 'general','general', 'act', SYSDATE, 0, 100)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Nominee - Nomination Submitted', 'message_data.message.103084', 'nom','nominee_nomination_submitted', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Nominee''s Manager Nomination Submitted', 'message_data.message.103085', 'nom','nominee_mgr_nomination_submitted', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Approver - Nomination Submitted', 'message_data.message.103089', 'nom','approver_nomination_submitted', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Nominee - Winner Notification', 'message_data.message.103090', 'nom','nominee_winner', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Nominee''s Manager - Winner Notification', 'message_data.message.103091', 'nom','nominee_mgr_winner', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Nominator - Winner Notification', 'message_data.message.104099', 'nom','nominator_winner', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Nominator - Non Winner Notification', 'message_data.message.104098', 'nom','to_nominator_non_winner', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Approver Reminder - Time Period Expired', 'message_data.message.104092', 'nom','approver_reminder_tp_expired', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Approver Reminder - Approval End Date', 'message_data.message.104093', 'nom','approver_reminder_approval_end_date', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Approver - Request For More Information Received', 'message_data.message.104094', 'nom','approver_request_more_information', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Nominator - Request For More Information', 'message_data.message.104095', 'nom','nominator_request_more_information', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
    (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
    (message_pk_sq.nextval, 'Quiz Import Process', 'message_data.message.104281', 'general','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
    (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
    (message_pk_sq.nextval, 'PaxBase Import Process', 'message_data.message.107641', 'general','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
    (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
    (message_pk_sq.nextval, 'PaxGoal Import Process', 'message_data.message.107642', 'general','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
    (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
    (message_pk_sq.nextval, 'PaxProgress Import Process', 'message_data.message.107681', 'general','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
    (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
    (message_pk_sq.nextval, 'Auto Vin Import Process', 'message_data.message.107685', 'general','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
    (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
    (message_pk_sq.nextval, 'Report Extract Notification', 'message_data.message.104710', 'general','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
    (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
    (message_pk_sq.nextval, 'Mailing Attachment Cleanup Notification', 'message_data.message.105204', 'general','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
    (message_id, message_name, cm_asset_code, module_code, message_type_code, status_code, created_by, date_created, version)
VALUES
    (message_pk_sq.nextval, 'Audience Extraction Process', 'message_data.message.104061', 'general','general', 'act', '0', SYSDATE, 0)
/
-- Goal Quest Notification Messages, must be inserted in the order they appear on the jsp --
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE,CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Participant - Promotion Announcement (GoalQuest)', 'message_data.message.106841', 'gq','promotion_launch', 'act','promotionalMessages', 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Participant - Goal Selected', 'message_data.message.106842', 'gq','participant_goal_selected', 'act', 'goalReminders', 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Participant - Goal Not Selected', 'message_data.message.106843', 'gq','participant_goal_not_selected', 'act','goalReminders', 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Participant - Progress Updated', 'message_data.message.106844', 'gq','participant_progress_updated', 'act','goalReminders', 0, SYSDATE, 0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Participant - Inactivity Alert (GoalQuest)','message_data.message.106845','gq','general','act',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Participant - Promotion End Alert (GoalQuest)','message_data.message.106846','gq','promotion_end','act','promotionalMessages',NULL,0,sysdate,null,null,1)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Participant - Goal Achieved', 'message_data.message.106847', 'gq','participant_goal_achieved', 'act', 0, SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Participant - Goal Not Achieved', 'message_data.message.106848', 'gq','participant_goal_not_achieved', 'act',0, SYSDATE, 0)
/
-- END Goal Quest Notification Messages, must be inserted in the order they appear on the jsp --
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Participant - Recognition Denied Message', 'message_data.message.102457', 'rec','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Manager Override - Process Run', 'message_data.message.106361', 'prd','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'GoalQuest - Detail Extract', 'message_data.message.107141', 'gq','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
    (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
    (message_pk_sq.nextval, 'Participant - Replacement Gift Code (GoalQuest)', 'message_data.message.107861', 'gq','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
    (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
    (message_pk_sq.nextval, 'Participant - Replacement Gift Code (Recognition)', 'message_data.message.114541', 'rec','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'GoalQuest - Promotion Launch Notification', 'message_data.message.111201', 'gq','promotion_launch', 'act','promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Throwdown - Round Extract Details', 'message_data.message.10000894', 'td','throwdown_round_extract', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'GoalQuest - Welcome Email', 'message_data.message.111202', 'gq','general', 'act','promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Goalquest partner Selected', 'message_data.message.118001', 'gq','partner_goal_selected', 'act','goalReminders', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Goalquest Partner Progress Updated', 'message_data.message.118002', 'gq','partner_progress_updated', 'act','goalReminders', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Goalquest promotion Partner Welcome Email', 'message_data.message.118003', 'gq','general', 'act','promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Goalquest partner Goal Achieved', 'message_data.message.118004', 'gq','partner_goal_achieved', 'act','0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE , CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Goalquest Partner Goal Not Achieved', 'message_data.message.118005', 'gq','partner_goal_not_achieved', 'act','0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Goalquest partner Goal Achieved No Payout', 'message_data.message.118361', 'gq','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE,date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Plateau Non-redemption Reminder (Recognition)','message_data.message.110961','rec','non_redemption_reminder','act','promotionalMessages',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Budget Sweep Notification?','message_data.message.122981','rec','budget_sweep','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Budget End Notification?','message_data.message.10000833','rec','budget_end','act','budgetEndAlerts', NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Budget Reminder Notification','message_data.message.10000933','rec','budget_reminder','act','budgetReminderAlerts', NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Budget Sweep Process Complete','message_data.message.123161','rec','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Budget Sweep Process Promotion Details','message_data.message.123162','rec','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO MESSAGE 
  ( MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE,
  STATUS_CODE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES 
  ( message_pk_sq.nextval, 'Challengepoint - Detail Extract', 'message_data.message.124401', 'cp', 'general'
  , 'act', NULL, 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE 
  ( MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE,
  STATUS_CODE,DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES 
  ( message_pk_sq.nextval, 'Participant - Challengepoint Achieved', 'message_data.message.124405', 'cp'
  , 'participant_challengepoint_achieved', 'act',NULL, 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE 
  ( MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE,
  STATUS_CODE,SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES 
  ( message_pk_sq.nextval, 'Participant - Challengepoint Selected', 'message_data.message.124408', 'cp'
  , 'participant_challengepoint_selected', 'act','goalReminders', NULL, 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE 
  ( MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE,
  STATUS_CODE,SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES 
  ( message_pk_sq.nextval, 'Participant - Challengepoint Not Selected', 'message_data.message.124407'
  , 'cp', 'participant_challengepoint_not_selected', 'act','goalReminders', NULL, 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE 
  ( MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE,
  STATUS_CODE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES 
  ( message_pk_sq.nextval, 'Participant - Challengepoint Not Achieved', 'message_data.message.124406'
  , 'cp', 'participant_challengepoint_not_achieved', 'act',NULL, 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE 
  ( MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE,
  STATUS_CODE,SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES 
  ( message_pk_sq.nextval, 'Challengepoint - Promotion Launch Notification', 'message_data.message.124402'
  , 'cp', 'promotion_launch', 'act','promotionalMessages', NULL, 5662,  sysdate, NULL, NULL, 0)
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
  ( MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE,
  STATUS_CODE,SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION )
VALUES 
  ( message_pk_sq.nextval, 'Challengepoint Progress Updated', 'message_data.message.124404'
  , 'cp', 'participant_challengepoint_progress_updated', 'act','goalReminders', NULL, 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE 
  ( MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE,
  STATUS_CODE,SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES 
  ( message_pk_sq.nextval, 'Participant - Promotion End Alert (Challengepoint)', 'message_data.message.124409'
  , 'cp', 'promotion_end', 'act','promotionalMessages', NULL, 5662,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE 
  ( MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE,
  STATUS_CODE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES 
  ( message_pk_sq.nextval, 'Paticipant - Challengepoint Intermin payout processed', 'message_data.message.124410'
  , 'cp', 'participant_challengepoint_interim_payout_processed', 'act', NULL, 0, sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE 
  ( MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE,
   STATUS_CODE,SMS_GROUP_TYPE, DATE_LAST_SENT, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION ) 
VALUES 
  ( message_pk_sq.nextval, 'Challengepoint - Welcome Login and Password', 'message_data.message.124403'
   , 'cp', 'general', 'act','promotionalMessages', NULL, 0,  sysdate, NULL, NULL, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'CPoint PaxBase Import Process', 'message_data.message.124411', 'general','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'CPoint PaxLevel Import Process', 'message_data.message.124421', 'general','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
   (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'CPoint PaxProgress Import Process', 'message_data.message.124422', 'general','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Award Level Import Process', 'message_data.message.10000941', 'general','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, sms_group_type, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'PURL - Manager Notification Message', 'message_data.message.10000841', 'rec','purl_manager_notification', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, sms_group_type, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'PURL - Contributors Invitation Message', 'message_data.message.10000842', 'rec','purl_contributor_invitation', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, sms_group_type, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'PURL - Recipient Invitation Message', 'message_data.message.10000843', 'rec','purl_recipient_invitation', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, sms_group_type, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'PURL - Manager Nonresponse Message', 'message_data.message.10000844', 'rec','purl_manager_nonresponse', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, sms_group_type, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'PURL - Contributors Nonresponse Message', 'message_data.message.10000845', 'rec','purl_contributor_nonresponse', 'act', 'promotionalMessages', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'PURL Archive Process Message', 'message_data.message.10000848', 'general','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'PURL Award Process Message', 'message_data.message.10000849', 'general','general', 'act', '0', SYSDATE, 0)
/

INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'PURL Auto Send Process Message', 'message_data.message.10000825', 'general','general', 'act', '0', SYSDATE, 0)
/

INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code, status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Goalquest - Non redemption reminder','message_data.message.100821','gq','gq_non_redemption_reminder','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'GoalQuest Data Repository Process Message','message_data.message.100822','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Resend Welcome Email Count Message','message_data.message.100823','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, sms_group_type, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Budget Reallocation Message','message_data.message.10000881','general','general','act','budgetEndAlerts',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Campaign Transfer Process Message','message_data.message.10000821','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, sms_group_type, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Budget Reallocation Recipient Message','message_data.message.129001','general','general','act','budgetEndAlerts',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Global File Upload Message','message_data.message.129002','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, sms_group_type, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Badge Received','message_data.message.10000822','general','badge_received','act','promotionalMessages',null, 0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Badge Import Process','message_data.message.10000860','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Manager Alert Template','message_data.message.17711','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
      (message_pk_sq.nextval,'Leaderboard Create Update Notify Mesg','message_data.message.10000850','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
      (message_pk_sq.nextval,'Leaderboard Import process','message_data.message.10000005','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
      (message_pk_sq.nextval,'Process Failed Notification','message_data.message.10000893','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,status_code, sms_group_type, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Report Extraction Completed','message_data.message.10000872','general','general','act','promotionalMessages',null, 0,sysdate,null,null,0)
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
INSERT INTO message (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(message_pk_sq.nextval, 'Throwdown - Promotion Launch Notification With Login Details', 'message_data.message.110836', 'td','throwdown_promotion_launch_with_login_detail', 'act',NULL, '0', SYSDATE, 0)
/
INSERT INTO message (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE,SMS_GROUP_TYPE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(message_pk_sq.nextval, 'Throwdown - Manager Promotion Launch Notification With Login Details', 'message_data.message.110837', 'td','throwdown_manager_promotion_launch_with_login_detail', 'act',NULL, '0', SYSDATE, 0)
/
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
   (message_pk_sq.nextval, 'Engagement Notification Summary Message', 'message_data.message.10001720', 'eng', 'general', 'act', 5662,  sysdate, NULL, NULL, 0)
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

Insert into MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
 Values
   (MESSAGE_PK_SQ.nextVal, 'Contest Edit Notify Creator', 'message_data.message.10000191', 'ssi', 'contest_edit_notify_creator', 'act', 0, TO_DATE('11/22/2016 11:21:34 AM', 'MM/DD/YYYY HH:MI:SS AM'), 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Setup Launch Notify Creator', 'message_data.message.10000006', 'ssi','contest_setup_launch_notify_creator', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Launch Notify Mgr Do This Get That', 'message_data.message.10000017', 'ssi','contest_launch_notify_mgr_do_this_get_that', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Launch Notify Pax Do This Get That', 'message_data.message.10000018', 'ssi','contest_launch_notify_pax_do_this_get_that', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Progress Notify Pax Do This Get That', 'message_data.message.10000019', 'ssi','contest_progress_notify_pax_do_this_get_that', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest End Notify Pax Do This Get That', 'message_data.message.10000020', 'ssi','contest_end_notify_pax_do_this_get_that', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Final Results Notify Pax Do This Get That', 'message_data.message.10000021', 'ssi','contest_final_result_notify_pax_do_this_get_that', 'act', '0', SYSDATE, 0)
/

INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Launch Notify Mgr Objectives', 'message_data.message.10000027', 'ssi','contest_launch_notify_mgr_objectives', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Launch Notify Pax Objectives', 'message_data.message.10000028', 'ssi','contest_launch_notify_pax_objectives', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Progress Notify Pax Objectives', 'message_data.message.10000029', 'ssi','contest_progress_notify_pax_objectives', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest End Notify Pax Objectives', 'message_data.message.10000030', 'ssi','contest_end_notify_pax_objectives', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Final Result Notify Pax Objectives', 'message_data.message.10000031', 'ssi','contest_final_result_notify_pax_objectives', 'act', '0', SYSDATE, 0)
/


INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Launch Notify Mgr Stack Rank', 'message_data.message.10000037', 'ssi','contest_launch_notify_mgr_stack_rank', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Launch Notify Pax Stack Rank', 'message_data.message.10000038', 'ssi','contest_launch_notify_pax_stack_rank', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Progress Notify Pax Stack Rank', 'message_data.message.10000039', 'ssi','contest_progress_notify_pax_stack_rank', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest End Notify Pax Stack Rank', 'message_data.message.10000040', 'ssi','contest_end_notify_pax_stack_rank', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Final Results Notify Pax Stack Rank', 'message_data.message.10000041', 'ssi','contest_final_result_notify_pax_stack_rank', 'act', '0', SYSDATE, 0)
/

INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Launch Notify Mgr Step It Up', 'message_data.message.10000047', 'ssi','contest_launch_notify_mgr_step_it_up', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Launch Notify Pax Step It Up', 'message_data.message.10000048', 'ssi','contest_launch_notify_pax_step_it_up', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Progress Notify Pax Step It Up', 'message_data.message.10000049', 'ssi','contest_progress_notify_pax_step_it_up', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest End Notify Pax Step It Up', 'message_data.message.10000050', 'ssi','contest_end_notify_pax_step_it_up', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Final Results Pax Notify Step It Up', 'message_data.message.10000051', 'ssi','contest_final_result_notify_pax_step_it_up', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Final Result Notify Manager Do This Get That', 'message_data.message.10000062', 'ssi','contest_final_result_notify_mgr_do_this_get_that', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Final Result Notify Creator Do This Get That', 'message_data.message.10000063', 'ssi','contest_final_result_notify_creator_do_this_get_that', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Final Result Notify Creator Objectives', 'message_data.message.10000064', 'ssi','contest_final_result_notify_creator_objectives', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Final Result Notify Manager Objectives', 'message_data.message.10000065', 'ssi','contest_final_result_notify_mgr_objectives', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Final Result Notify Manager Stack Rank', 'message_data.message.10000066', 'ssi','contest_final_result_notify_mgr_stack_rank', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Final Result Notify Creator Stack Rank', 'message_data.message.10000067', 'ssi','contest_final_result_notify_creator_stack_rank', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Final Result Notify Creator Step It Up', 'message_data.message.10000068', 'ssi','contest_final_result_notify_creator_step_it_up', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Final Result Notify Manager Step It Up', 'message_data.message.10000069', 'ssi','contest_final_result_notify_mgr_step_it_up', 'act', '0', SYSDATE, 0)
/

INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Launch Notify Creator Do This Get That', 'message_data.message.10000070', 'ssi','contest_launch_notify_creator_do_this_get_that', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Launch Notify Creator Objectives', 'message_data.message.10000071', 'ssi','contest_launch_notify_creator_objectives', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Launch Notify Creator Stack Rank', 'message_data.message.10000072', 'ssi','contest_launch_notify_creator_stack_rank', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Launch Notify Creator Step It Up', 'message_data.message.10000073', 'ssi','contest_launch_notify_creator_step_it_up', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Progress Notify Creator Do This Get That', 'message_data.message.10000074', 'ssi','contest_progress_notify_creator_do_this_get_that', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Progress Notify Creator Objectives', 'message_data.message.10000075', 'ssi','contest_progress_notify_creator_objectives', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Progress Notify Creator Stack Rank', 'message_data.message.10000076', 'ssi','contest_progress_notify_creator_stack_rank', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Progress Notify Creator Step It Up', 'message_data.message.10000077', 'ssi','contest_progress_notify_creator_step_it_up', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Progress Notify Mgr Do This Get That', 'message_data.message.10000078', 'ssi','contest_progress_notify_mgr_do_this_get_that', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Progress Notify Mgr Objectives', 'message_data.message.10000079', 'ssi','contest_progress_notify_mgr_objectives', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Progress Notify Mgr Stack Rank', 'message_data.message.10000080', 'ssi','contest_progress_notify_mgr_stack_rank', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Progress Notify Mgr Step It Up', 'message_data.message.10000081', 'ssi','contest_progress_notify_mgr_step_it_up', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest End Notify Creator Do This Get That', 'message_data.message.10000082', 'ssi','contest_end_notify_creator_do_this_get_that', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest End Notify Creator Objectives', 'message_data.message.10000083', 'ssi','contest_end_notify_creator_objectives', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest End Notify Creator Stack Rank', 'message_data.message.10000084', 'ssi','contest_end_notify_creator_stack_rank', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest End Notify Creator Step It Up', 'message_data.message.10000085', 'ssi','contest_end_notify_creator_step_it_up', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest End Notify Mgr Do This Get That', 'message_data.message.10000086', 'ssi','contest_end_notify_mgr_do_this_get_that', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest End Notify Mgr Objectives', 'message_data.message.10000087', 'ssi','contest_end_notify_mgr_objectives', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest End Notify Mgr Stack Rank', 'message_data.message.10000088', 'ssi','contest_end_notify_mgr_stack_rank', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest End Notify Mgr Step It Up', 'message_data.message.10000089', 'ssi','contest_end_notify_mgr_step_it_up', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Notify Contest creator of contest approval or denial', 'message_data.message.10000090', 'ssi','contest_approval_status_notify_creator', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Final Results Reminder Notify Creator', 'message_data.message.10000091', 'ssi','contest_final_result_rem_notify_creator', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Approval Notify Approver', 'message_data.message.10000092', 'ssi','contest_approval_notify_aprvr', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Approval Reminder Notify Approver', 'message_data.message.10000093', 'ssi','contest_approval_rem_notify_aprvr', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Update After Approval Status Notify Approver', 'message_data.message.10000094', 'ssi','contest_upd_after_aprvl_stat_notify_aprvr', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Award Issuance (Award them Now) Notify Participants', 'message_data.message.10000095', 'ssi','contest_award_issuance_to_pax', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Award Issuance (Award them Now) Notify Creator', 'message_data.message.10000096', 'ssi','contest_award_issuance_to_creator', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Award Issuance (Award them Now) Notify Manager', 'message_data.message.10000097', 'ssi','contest_award_issuance_to_manager', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'SSI Contest StackRank Update Process Failure Message', 'message_data.message.10000099', 'ssi','general', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Approver Notification - Award Them Now', 'message_data.message.10000100', 'ssi','contest_notify_approver_award_them_now', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Progress Load Status Notify Creator Message', 'message_data.message.10000101', 'ssi','contest_progress_status_notify_creator', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Claim Action Notify Submitter Message', 'message_data.message.10000103', 'ssi','contest_claim_approval_notify_submitter', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Claim Submission Notify Approver Message', 'message_data.message.10000102', 'ssi','contest_claim_approval_notify_approver', 'act', '0', SYSDATE, 0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'WorkHappier Report Extract Message', 'message_data.message.10000108', 'general', 'general', 'act', '0',sysdate,0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Campaign Transfer Account Balance Transfer Summary Message', 'message_data.message.10000109', 'general', 'general', 'act', '0',sysdate,0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Update Cash Currencies Summary Message', 'message_data.message.10000110', 'general', 'general', 'act', '0',sysdate,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE,date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Approver Notification','message_data.message.10000128','nom','approver_notification','act','promotionalMessages',NULL,0,sysdate,null,null,1)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Nomination Custom Appoover Import Process','message_data.message.10000111','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Refresh Pending Nomination Approver Process','message_data.message.10000129','general','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Budget Redistribution Import Process Message','message_data.message.10000104','general','general','act',NULL,0,sysdate,null,null,0)
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
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'SSI Contest ATN Import Process','message_data.message.10000201','ssi','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'SSI Contest DTGT Import Process','message_data.message.10000202','ssi','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'SSI Contest SIU Import Process','message_data.message.10000203','ssi','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'SSI Contest SR Import Process','message_data.message.10000204','ssi','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'SSI Contest OBJ Import Process','message_data.message.10000205','ssi','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Reissue Password','message_data.message.105001','general','reissue_password','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(MESSAGE_PK_SQ.nextVal, 'Opt Out Of Awards Notify Pax', 'message_data.message.10000301', 'general', 'general', 'act', 0, sysdate, 0)
/
INSERT INTO MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
(MESSAGE_PK_SQ.nextVal, 'Opt Out Of Awards Notify Manager', 'message_data.message.10000302', 'general', 'general', 'act', 0, sysdate, 0)
/