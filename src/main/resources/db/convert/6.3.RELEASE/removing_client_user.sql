--liquibase formatted sql

--changeset cornelius:1
--comment Remove client type from roles
delete from user_type_role where user_type_code = 'clt';
--rollback not required

--changeset cornelius:2
--comment Change client users to bi users
update application_user set user_type = 'bi' where user_type = 'clt';
--rollback not required
