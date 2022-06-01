INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'InstantPoll Notify Participant Notification Message','message_data.message.152552','svy','general','act',NULL,0,sysdate,null,null,0)
/
INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code,SMS_GROUP_TYPE,date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Participant - Inactivity Notification (Survey)','message_data.message.14101','svy','participant_inactivity','act','promotionalMessages',NULL,0,sysdate,null,null,1)
/