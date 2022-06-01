CREATE SEQUENCE pax_survey_res_pk_sq START WITH 250 INCREMENT BY 1
/
CREATE TABLE participant_survey_response
(participant_survey_response_id     NUMBER(18) NOT NULL,
participant_survey_id				NUMBER(18) NOT NULL,
survey_question_id			        NUMBER(18) NOT NULL,
survey_question_response_id 	   	NUMBER(18) NOT NULL,
sequence_num		                NUMBER(10),
date_created                 		DATE ,
created_by                   		NUMBER(18),
version                      		NUMBER(18,0)
)
/
ALTER TABLE participant_survey_response
ADD CONSTRAINT participant_survey_response_pk PRIMARY KEY (participant_survey_response_id)
USING INDEX
/
ALTER TABLE participant_survey_response ADD CONSTRAINT participant_survey_id_fk
FOREIGN KEY (participant_survey_id) REFERENCES participant_survey(participant_survey_id)
/
ALTER TABLE participant_survey_response ADD CONSTRAINT survey_question_id_fk
FOREIGN KEY (survey_question_id) REFERENCES survey_question(survey_question_id)
/
ALTER TABLE participant_survey_response ADD CONSTRAINT survey_question_response_id_fk
FOREIGN KEY (survey_question_response_id) REFERENCES survey_question_response(survey_question_response_id)
/
COMMENT ON COLUMN participant_survey_response.participant_survey_response_id IS 'System-generated key .'
/
COMMENT ON COLUMN participant_survey_response.participant_survey_id IS 'Id of the participant survey.'
/
COMMENT ON COLUMN participant_survey_response.survey_question_id IS 'Id of the survey question .'
/
COMMENT ON COLUMN participant_survey_response.survey_question_response_id IS 'Id of the survey question response .'
/