--liquibase formatted sql

--changeset dhanekul:1
--comment add index on participant.
CREATE INDEX PAX_TERM_DATE_IDX ON PARTICIPANT
(USER_ID,TERMINATION_DATE);
--rollback DROP INDEX PAX_TERM_DATE_IDX;

--changeset dhanekul:2
--comment add index on participant.
CREATE INDEX USER_EMAIL_ADDRESS_IDX ON USER_EMAIL_ADDRESS
(USER_ID,EMAIL_ADDR,EMAIL_TYPE);
--rollback DROP INDEX USER_EMAIL_ADDRESS_IDX;
