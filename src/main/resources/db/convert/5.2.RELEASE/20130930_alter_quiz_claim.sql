ALTER TABLE quiz_claim add quiz_id NUMBER(12)
/
ALTER TABLE quiz_claim ADD CONSTRAINT quiz_id_fk
  FOREIGN KEY (quiz_id) REFERENCES quiz (quiz_id)
/