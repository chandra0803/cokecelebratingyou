INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Work Happier Report Extract Message', 'message_data.message.10000108', 'general', 'general', 'act', '0',sysdate,0)
/
INSERT INTO message
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Campaign Transfer Account Balance Transfer Summary Message', 'message_data.message.10000109', 'general', 'general', 'act', '0',sysdate,0)
/