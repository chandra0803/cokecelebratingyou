ALTER TABLE PARTICIPANT_SURVEY
DROP CONSTRAINT PROMOTION_ID_FK
/
ALTER TABLE PARTICIPANT_SURVEY
ADD FOREIGN KEY (promotion_id) REFERENCES promotion(promotion_id)
/