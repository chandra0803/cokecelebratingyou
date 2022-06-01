INSERT INTO message (message_id, message_name, cm_asset_code, module_code, message_type_code,
       status_code, date_last_sent, created_by, date_created,
       modified_by, date_modified, version)
VALUES
(message_pk_sq.nextval,'Reissue Password','message_data.message.105001','general','reissue_password','act',NULL,0,sysdate,null,null,0)
/