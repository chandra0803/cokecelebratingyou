DELETE FROM MESSAGE WHERE cm_asset_code = 'message_data.message.114861'
/
INSERT INTO MESSAGE (MESSAGE_ID,MESSAGE_NAME,CM_ASSET_CODE,MODULE_CODE,MESSAGE_TYPE_CODE,STATUS_CODE,SMS_GROUP_TYPE,DATE_LAST_SENT,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES (message_pk_sq.nextval,'SEA To Save this Promotion','message_data.message.1068032','SEA','general','act',null,null,5662,SYSDATE,null,null,0)
/
INSERT INTO MESSAGE
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Claim Action Notify Submitter Message', 'message_data.message.10000103', 'ssi','contest_claim_approval_notify_submitter', 'act', '0', SYSDATE, 0)
/
INSERT INTO MESSAGE
(MESSAGE_ID, MESSAGE_NAME, CM_ASSET_CODE, MODULE_CODE, message_type_code, STATUS_CODE, CREATED_BY, DATE_CREATED, VERSION)
VALUES
   (message_pk_sq.nextval, 'Contest Claim Submission Notify Approver Message', 'message_data.message.10000102', 'ssi','contest_claim_approval_notify_approver', 'act', '0', SYSDATE, 0)
/