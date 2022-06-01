--liquibase formatted sql

--changeset subramap:1
--comment ots batch table to store ots batch details
CREATE TABLE ots_batch
(
    ots_batch_id         NUMBER (18),
    ots_batch_nbr        NUMBER (18),
    ots_program_id       NUMBER (18),
    ots_cms_asset_code   VARCHAR2 (100 CHAR),
    date_created         DATE,
    created_by           NUMBER (18),
    CONSTRAINT ots_batch_fk FOREIGN KEY (ots_program_id)
    REFERENCES ots_program
);
--rollback DROP TABLE ots_batch CASCADE CONSTRAINTS;



--changeset subramap:2
--comment OTS_BATCH_SQ sequence
CREATE SEQUENCE OTS_BATCH_SQ START WITH 100 INCREMENT BY 1;
--rollback DROP SEQUENCE OTS_BATCH_SQ;