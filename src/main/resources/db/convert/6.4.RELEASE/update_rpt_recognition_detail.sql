--liquibase formatted sql

--changeset chidamba:1
--comment update receiver login id column
 UPDATE RPT_RECOGNITION_DETAIL rpt set rpt.recvr_login_id = (SELECT user_name FROM application_user where user_id = rpt.recvr_user_id) WHERE exists (SELECT 1 FROM application_user where user_id = rpt.recvr_user_id ) and rpt.claim_id is null;
--rollback not required
