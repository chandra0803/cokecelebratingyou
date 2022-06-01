ALTER TABLE participant_survey_response 
ADD (
		OPEN_END_RESPONSE    				varchar2(4000),
		DATE_MODIFIED                 		DATE ,
		MODIFIED_BY                   		NUMBER(18)
    )
/
ALTER TABLE participant_survey DROP COLUMN promotion_survey_id
/
ALTER TABLE participant_survey 
ADD (
		promotion_id				   		NUMBER(18),
		survey_id				   			NUMBER(18) NOT NULL,
		IS_COMPLETED						NUMBER(1) DEFAULT 0 NOT NULL,
		NODE_ID								NUMBER(18)
    )
/
COMMENT ON COLUMN participant_survey.survey_id IS 'Id of the survey.'
/
COMMENT ON COLUMN participant_survey.promotion_id IS 'Id of the promotion.'
/
COMMENT ON COLUMN participant_survey.node_id IS 'Id of the organization for the user taking the survey.'
/
ALTER TABLE promo_goalquest_survey DROP CONSTRAINT survey_id_fk
/
ALTER TABLE promo_goalquest_survey ADD CONSTRAINT pgq_survey_id_fk
FOREIGN KEY (survey_id) REFERENCES survey(survey_id)
/
ALTER TABLE participant_survey ADD CONSTRAINT survey_id_fk
FOREIGN KEY (survey_id) REFERENCES survey(survey_id)
/