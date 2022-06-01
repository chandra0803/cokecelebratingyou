--liquibase formatted sql

--changeset kancherla:1
--comment removed SEA messages
DELETE message WHERE cm_asset_code in ('message_data.message.1068001','message_data.message.1068002','message_data.message.1068003','message_data.message.1068005','message_data.message.1068006','message_data.message.1068007','message_data.message.1068008','message_data.message.1068009','message_data.message.1068010','message_data.message.1068011','message_data.message.1068012','message_data.message.1068013','message_data.message.1068014','message_data.message.1068015','message_data.message.1068016','message_data.message.1068018','message_data.message.1068019','message_data.message.1068022','message_data.message.1068023','message_data.message.1068024','message_data.message.1068025','message_data.message.1068026','message_data.message.1068027','message_data.message.1068028','message_data.message.1068029','message_data.message.1068030','message_data.message.1068031','message_data.message.1068032','message_data.message.1068033','message_data.message.1068034','message_data.message.1068035','message_data.message.1068036','message_data.message.1068037','message_data.message.1068038','message_data.message.1068039','message_data.message.1068040','message_data.message.1068042','message_data.message.1068043','message_data.message.1068044');
--rollback not required

--changeset ramesh:2
--comment removing reissue send password related message
delete from message where cm_asset_code='message_data.message.105020';
--rollback not required