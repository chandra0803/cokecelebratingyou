INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'SEA Email behavior inactive', 'message_data.message.1068033', 'SEA','SEA_Email_behavior_inactive', 'act', '0', SYSDATE, 0)
/