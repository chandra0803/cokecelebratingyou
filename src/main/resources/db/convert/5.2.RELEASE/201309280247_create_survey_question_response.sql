CREATE SEQUENCE survey_qus_res_pk_sq INCREMENT BY 1 START WITH 5000
/
CREATE TABLE survey_question_response 
   (survey_question_response_id		NUMBER(18) NOT NULL,
   	survey_question_id 	   	  	    	NUMBER(18) NOT NULL,
    STATUS_TYPE	   						VARCHAR2(30) NOT NULL,
   	cm_asset_name				    	VARCHAR2(255) NOT NULL,
   	sequence_num				    	NUMBER(10) NOT NULL,
    created_by                      	number(18),
    date_created   				    	DATE,
    modified_by                     	number(18),
    date_modified  				    	DATE,
    version        				    	NUMBER(18))
/
ALTER TABLE survey_question_response  ADD CONSTRAINT qqa_survey_fk
  FOREIGN KEY (survey_question_id) REFERENCES survey_question(survey_question_id) 
/
ALTER TABLE survey_question_response 
ADD CONSTRAINT survey_question_response_id_pk PRIMARY KEY (survey_question_response_id)
USING INDEX
/
CREATE INDEX sury_qstn_res_qstn_fk_idx     ON survey_question_response
  (survey_question_id)
/
COMMENT ON TABLE survey_question_response  IS 'The SURVEY_QUESTION_ANSWER table defines a specific survey question response  for a survey question.'
/
COMMENT ON COLUMN survey_question_response.survey_question_response_id IS 'System-generated key that identifies a specific survey question response .'
/
COMMENT ON COLUMN survey_question_response.survey_question_id IS 'Foreign key to survey.'
/