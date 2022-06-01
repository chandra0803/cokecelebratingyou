CREATE TABLE diy_quiz
  (
    quiz_id  NUMBER(18,0) NOT NULL,
    badge_rule_id  NUMBER(18,0),
    certificate_id NUMBER(18,0),
    owner_id       NUMBER(18,0) NOT NULL,
    promotion_id   NUMBER(18,0) NOT NULL,
    start_date DATE,
    end_date DATE,
    allow_unlimited_attempts NUMBER(1,0),
    maximum_attempts         NUMBER(4,0),
    notification_text        VARCHAR2(4000),
    introduction_text        VARCHAR2(4000)
  )
/
ALTER TABLE diy_quiz ADD CONSTRAINT diy_quiz_id_pk 
  PRIMARY KEY (quiz_id) USING INDEX  
/
ALTER TABLE diy_quiz ADD CONSTRAINT diy_quiz_badge_rule_fk
  FOREIGN KEY (badge_rule_id) REFERENCES badge_rule (badge_rule_id) 
/
ALTER TABLE diy_quiz ADD CONSTRAINT diy_quiz_promo_certificate_fk
  FOREIGN KEY (certificate_id) REFERENCES promo_certificate (promo_certificate_id) 
/
ALTER TABLE diy_quiz ADD CONSTRAINT diy_quiz_owner_fk
  FOREIGN KEY (owner_id) REFERENCES APPLICATION_USER (user_id) 
/
ALTER TABLE diy_quiz ADD CONSTRAINT diy_quiz_promotion_fk
  FOREIGN KEY (promotion_id) REFERENCES promotion (promotion_id)
/