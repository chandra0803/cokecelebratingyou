CREATE SEQUENCE survey_pk_sq INCREMENT BY 1   START WITH 5000
/

CREATE TABLE survey
   (survey_id 	   NUMBER(18) NOT NULL,
   	name      	   VARCHAR2(200) NOT NULL,
   	description	   VARCHAR2(400),
    survey_type	   VARCHAR2(30) NOT NULL,
    status_type	   VARCHAR2(30) NOT NULL,
    created_by     number(18) ,
    date_created   DATE ,
    modified_by    number(18),
    date_modified  DATE,
    version        NUMBER(18) )
/

ALTER TABLE survey
ADD CONSTRAINT survey_id_pk PRIMARY KEY (survey_id)
USING INDEX
/

COMMENT ON TABLE survey IS 'The SURVEY table defines a specific survey.'
/

COMMENT ON COLUMN survey.survey_id IS 'System-generated key that identifies a specific survey.'
/

CREATE UNIQUE INDEX survey_name_udk ON survey
(upper(name) ASC  )
/
