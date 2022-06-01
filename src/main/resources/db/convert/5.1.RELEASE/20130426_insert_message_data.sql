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