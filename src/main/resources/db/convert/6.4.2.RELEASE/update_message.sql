--liquibase formatted sql

--changeset esakkimu:1
--comment updating the MESSAGE_NAME value as Activation Link Notification
update MESSAGE set MESSAGE_NAME = 'Activation Link Notification' where CM_ASSET_CODE = 'message_data.message.10000306'
--rollback update MESSAGE set MESSAGE_NAME = 'Participant Activation Notification' where CM_ASSET_CODE = 'message_data.message.10000306';