--liquibase formatted sql

--changeset siemback:1
--comment add visibility column
alter table characteristic
add VISIBILITY VARCHAR2(30 CHAR) DEFAULT 'visible' not null;
--rollback alter table characteristic drop column VISIBILITY;
