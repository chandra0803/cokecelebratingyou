--liquibase formatted sql

--changeset Gorantla:1
--comment program audience table to store the list of audience which are associated with the given OTS program.
CREATE TABLE PROGRAM_AUDIENCE
            (PROGRAM_AUDIENCE_ID   NUMBER(18),    
             OTS_PROGRAM_ID       NUMBER(18)    NOT NULL,
             AUDIENCE_ID           NUMBER(18)    NOT NULL,
             CREATED_BY            NUMBER(18)    NOT NULL,
             DATE_CREATED          DATE          NOT NULL,
             VERSION               NUMBER(18)    NOT NULL,
             CONSTRAINT PROGRAM_AUDIENCE_PK PRIMARY KEY (PROGRAM_AUDIENCE_ID),
             CONSTRAINT PROGRAM_AUDIENCE_FK1 FOREIGN KEY (OTS_PROGRAM_ID) REFERENCES OTS_PROGRAM(OTS_PROGRAM_ID),
             CONSTRAINT PROGRAM_AUDIENCE_FK2 FOREIGN KEY (AUDIENCE_ID) REFERENCES AUDIENCE(AUDIENCE_ID));

COMMENT ON TABLE PROGRAM_AUDIENCE IS 'The program_audience table is used to get the list of audience which are associated with the given OTS program';
--rollback DROP TABLE PROGRAM_AUDIENCE CASCADE CONSTRAINTS;