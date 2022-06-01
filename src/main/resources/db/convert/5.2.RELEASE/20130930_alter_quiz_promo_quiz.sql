ALTER TABLE promo_quiz 
MODIFY (quiz_id NUMBER(18,0) NULL)
/
ALTER TABLE promo_quiz 
DROP COLUMN quiz_details
/
ALTER TABLE promo_quiz 
ADD manager_DIY NUMBER(1,0)
/