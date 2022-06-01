--liquibase formatted sql

--changeset subramap:1
--comment Insert Unique constraint for userId and phoneType
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*) from user_constraints where constraint_name='USER_PHONE_TYPE_UNIQUE'
alter table user_phone
add constraint "USER_PHONE_TYPE_UNIQUE" unique (user_id, phone_type);
--rollback alter table user_phone drop constraint "USER_PHONE_TYPE_UNIQUE";
