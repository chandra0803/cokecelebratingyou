ALTER TABLE participant_survey ADD CONSTRAINT promotion_id_fk
FOREIGN KEY (promotion_id) REFERENCES promotion(promotion_id)
/