CREATE TABLE PROMO_SURVEY
    (promotion_id                   	NUMBER(18,0) NOT NULL,
     IS_CORP_AND_MNGR     		  NUMBER(1,0) DEFAULT 0,
     survey_id						NUMBER(18, 0)
     )
/
ALTER TABLE PROMO_SURVEY
ADD CONSTRAINT promo_survey_pk PRIMARY KEY (promotion_id)
USING INDEX
/
ALTER TABLE PROMO_SURVEY ADD CONSTRAINT promo_survey_promotion_fk
  FOREIGN KEY (promotion_id) REFERENCES promotion (promotion_id)
/
ALTER TABLE PROMO_SURVEY ADD CONSTRAINT promo_survey_survey_fk
  FOREIGN KEY (survey_id) REFERENCES survey (survey_id)
/
CREATE INDEX PROMO_SURVEY_SURVEY_FK_idx ON PROMO_SURVEY
  (SURVEY_ID)
/
COMMENT ON COLUMN PROMO_SURVEY.PROMOTION_ID IS 'System generated PK.'
/
COMMENT ON COLUMN PROMO_SURVEY.IS_CORP_AND_MNGR IS 'reporting to Corporate and Managers or to Corporate Only'
/