CREATE SEQUENCE DIY_QUIZ_PARTICIPANT_PK_SQ START WITH 5000 INCREMENT BY 1 
/
CREATE TABLE diy_quiz_participant
  (
    diy_quiz_participant_id NUMBER(18) NOT NULL,
    quiz_id                 NUMBER(18) NOT NULL,
    participant_id          NUMBER(18) NOT NULL,
    status_type             VARCHAR2(30) NOT NULL,
    is_notification_sent    NUMBER(1,0) NOT NULL
  )
/
ALTER TABLE diy_quiz_participant
  ADD CONSTRAINT diy_quiz_participant_pk PRIMARY KEY (diy_quiz_participant_id)
  USING INDEX  
/
ALTER TABLE diy_quiz_participant ADD CONSTRAINT diy_quiz_quiz_fk
  FOREIGN KEY (quiz_id) REFERENCES quiz (quiz_id) 
/
ALTER TABLE diy_quiz_participant ADD CONSTRAINT diy_quiz_participant_fk
  FOREIGN KEY (participant_id) REFERENCES participant (user_id) 
/
