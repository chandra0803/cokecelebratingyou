CREATE SEQUENCE AWARDGEN_AWARD_PK_SQ INCREMENT BY 1 START WITH 5000 
/
CREATE TABLE AWARDGEN_AWARD
( AWARDGEN_AWARD_ID NUMBER(18) NOT NULL,
  AWARDGEN_ID NUMBER(18) NOT NULL,
  YEARS NUMBER(18) NOT NULL,
  AWARD_AMOUNT NUMBER(18),
  LEVEL_ID NUMBER(18),
  CREATED_BY NUMBER(18),
  DATE_CREATED DATE NOT NULL,
  MODIFIED_BY NUMBER(18),
  DATE_MODIFIED DATE,
  VERSION NUMBER(18) NOT NULL)
/
ALTER TABLE AWARDGEN_AWARD ADD
  CONSTRAINT AWARDGEN_AWARD_ID_PK PRIMARY KEY (AWARDGEN_AWARD_ID)
/
ALTER TABLE AWARDGEN_AWARD ADD CONSTRAINT AWARDGEN_AWARD_AWARDGEN_FK
  FOREIGN KEY (AWARDGEN_ID) REFERENCES AWARDGENERATOR (AWARDGEN_ID)
/