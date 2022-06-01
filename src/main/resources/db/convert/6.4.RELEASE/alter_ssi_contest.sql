--liquibase formatted sql

--changeset palaniss:1
--comment added column to capture whether launch notification send to a participant
ALTER TABLE SSI_CONTEST_PARTICIPANT
ADD  IS_LAUNCH_NOTIFICATION_SENT NUMBER(1) DEFAULT 0;  
--rollback ALTER TABLE SSI_CONTEST_PARTICIPANT DROP (IS_LAUNCH_NOTIFICATION_SENT);

--changeset palaniss:2
--comment added column to capture whether launch notification send to a manager
ALTER TABLE SSI_CONTEST_MANAGER
ADD  IS_LAUNCH_NOTIFICATION_SENT NUMBER(1) DEFAULT 0;  
--rollback ALTER TABLE SSI_CONTEST_MANAGER DROP (IS_LAUNCH_NOTIFICATION_SENT);
