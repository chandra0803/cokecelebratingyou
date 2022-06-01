Insert into MESSAGE (MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, MESSAGE_TYPE_CODE, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
 Values
   (MESSAGE_PK_SQ.nextVal, 'Contest Edit Notify Creator', 'message_data.message.10000191', 'ssi', 'contest_edit_notify_creator', 'act', 0, TO_DATE('11/22/2016 11:21:34 AM', 'MM/DD/YYYY HH:MI:SS AM'), 0)
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