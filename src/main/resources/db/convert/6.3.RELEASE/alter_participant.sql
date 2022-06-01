--liquibase formatted sql

--changeset cornelius:1
--comment honeycomb user id column
alter table participant
add (HONEYCOMB_USER_ID NUMBER(18));
--rollback alter table participant drop column honeycomb_user_id;
