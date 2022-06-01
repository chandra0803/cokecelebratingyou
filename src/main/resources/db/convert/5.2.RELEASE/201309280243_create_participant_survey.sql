CREATE SEQUENCE participant_survey_pk_sq START WITH 250 INCREMENT BY 1
/
CREATE TABLE participant_survey
(participant_survey_id				NUMBER(18) NOT NULL,
user_id						        NUMBER(18) NOT NULL,
promotion_survey_id				   	NUMBER(18) NOT NULL,
survey_date                 		DATE,
date_created                 		DATE ,
created_by                   		NUMBER(18),
version                      		NUMBER(18,0)
)
/
ALTER TABLE participant_survey
ADD CONSTRAINT participant_survey_pk PRIMARY KEY (participant_survey_id)
USING INDEX
/
ALTER TABLE participant_survey ADD CONSTRAINT user_id_fk
FOREIGN KEY (user_id) REFERENCES application_user(user_id)
/
ALTER TABLE participant_survey ADD CONSTRAINT promotion_survey_id_fk
FOREIGN KEY (promotion_survey_id) REFERENCES promo_goalquest_survey(promotion_survey_id)
/
COMMENT ON COLUMN participant_survey.participant_survey_id IS 'System-generated key .'
/
COMMENT ON COLUMN participant_survey.user_id IS 'Id of the participant.'
/
COMMENT ON COLUMN participant_survey.promotion_survey_id IS 'Id of the goalquest promotion survey id.'
/