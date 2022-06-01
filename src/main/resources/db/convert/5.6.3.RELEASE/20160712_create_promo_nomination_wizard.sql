CREATE SEQUENCE PROMOTION_WIZARD_ID_PK_SQ START WITH 250 INCREMENT BY 1
/
CREATE TABLE PROMO_NOMINATION_WIZARD (
	PROMOTION_WIZARD_ID			NUMBER(18,0) NOT NULL,
    PROMOTION_ID				NUMBER(18) NOT NULL,
    WIZARD_ORDER_NAME   		VARCHAR2(20), 
	WIZARD_ORDER 				VARCHAR2(10),
	created_by 					number(18) NOT NULL,
 	date_created                DATE NOT NULL,
 	modified_by 				number(18),
 	date_modified    			DATE,
 	version                     NUMBER(18,0) NOT NULL
  )
/
ALTER TABLE PROMO_NOMINATION_WIZARD
ADD CONSTRAINT promo_nomination_wizard_pk PRIMARY KEY (PROMOTION_WIZARD_ID)
USING INDEX
/
ALTER TABLE PROMO_NOMINATION_WIZARD
ADD CONSTRAINT promo_nomination_wizard_fk FOREIGN KEY (promotion_id)
REFERENCES promotion (promotion_id)
/
CREATE INDEX promo_nomination_wizard_fk_idx ON PROMO_NOMINATION_WIZARD
  (PROMOTION_ID)
/