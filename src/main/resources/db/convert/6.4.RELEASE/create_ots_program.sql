--liquibase formatted sql

--changeset Gorantla:1
--comment ots program table to store ots program details
CREATE TABLE OTS_PROGRAM
            (OTS_PROGRAM_ID NUMBER(18),
            OTS_PROGRAM_NBR      NUMBER(18)   ,
             PROGRAM_STATUS       VARCHAR2(30 CHAR) NOT NULL,
             AUDIENCE_TYPE VARCHAR2(100 CHAR) ,
			 CLIENT_NAME          VARCHAR2(100 CHAR),
			 DESCRIPTION          VARCHAR2(400 CHAR),
             CREATED_BY           NUMBER(18)   NOT NULL,
             DATE_CREATED         DATE         NOT NULL,
			 MODIFIED_BY          NUMBER(18)   ,
			 DATE_MODIFIED        DATE         ,
             VERSION              NUMBER(18)   NOT NULL,
             CONSTRAINT OTS_PROGRAM_PK PRIMARY KEY (OTS_PROGRAM_ID));

COMMENT ON TABLE OTS_PROGRAM IS 'The ots_program table is used to store the OTS programs information';

COMMENT ON COLUMN OTS_PROGRAM.PROGRAM_STATUS IS 'Status of the OTS Program. Status can be either complete or incomplete';
--rollback DROP TABLE OTS_PROGRAM CASCADE CONSTRAINTS;


--changeset Gorantla:2
--comment PROGRAM_AUDIENCE_SQ sequence
CREATE SEQUENCE PROGRAM_AUDIENCE_SQ START WITH 100 INCREMENT BY 1;
--rollback DROP SEQUENCE PROGRAM_AUDIENCE_SQ;


--changeset Gorantla:3
--comment OTS_PROGRAM_SQ sequence
CREATE SEQUENCE OTS_PROGRAM_SQ START WITH 100 INCREMENT BY 1;
--rollback DROP SEQUENCE OTS_PROGRAM_SQ;