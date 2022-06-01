--liquibase formatted sql

--changeset subramap:1
--comment mtc video table to store transcoded video urls
CREATE TABLE MTC_VIDEO
            (MTC_VIDEO_ID NUMBER(18),
            
             REQUEST_ID       VARCHAR2(40 CHAR) NOT NULL,
             MP4_URL 		VARCHAR2(800 CHAR) NOT NULL,
             WEBM_URL 		VARCHAR2(800 CHAR) NOT NULL,
             THUMBNAIL_IMAGE_URL VARCHAR2(800 CHAR) NOT NULL,
             CREATED_BY           NUMBER(18)   NOT NULL,
             DATE_CREATED         DATE         NOT NULL,
			 MODIFIED_BY          NUMBER(18)   ,
			 DATE_MODIFIED        DATE         ,
             VERSION              NUMBER(18)   NOT NULL,
             CONSTRAINT MTC_VIDEO_PK PRIMARY KEY (MTC_VIDEO_ID));

COMMENT ON TABLE MTC_VIDEO IS 'The MTC_VIDEO table is used to store the transcoded video details';


--rollback DROP TABLE MTC_VIDEO CASCADE CONSTRAINTS;


--changeset subramap:2
--comment MTC_VIDEO_SQ sequence
CREATE SEQUENCE MTC_VIDEO_SQ START WITH 100 INCREMENT BY 1;
--rollback DROP SEQUENCE MTC_VIDEO_SQ;

--changeset subramap:3
--comment Adding Orignal Format Column
Alter table MTC_VIDEO
add ORIGINAL_FORMAT VARCHAR2(800 CHAR);
--rollback ALTER TABLE MTC_VIDEO DROP COLUMN ORIGINAL_FORMAT;

--changeset subramap:4
--comment Adding Expiry date column
Alter table MTC_Video
add EXPIRY_DATE timestamp;
--rollback alter table MTC_VIDEO drop column expiry_date; 