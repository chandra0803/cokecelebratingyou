CREATE SEQUENCE promo_bill_code_pk_sq START WITH 250 INCREMENT BY 1
/
CREATE TABLE promo_bill_code
(
 promo_bill_code_id        		NUMBER(18,0) NOT NULL,
 promotion_id                   NUMBER(18,0) NOT NULL,
 sweeps_billcode				NUMBER(1) DEFAULT 0 NOT NULL,
 track_bills_by					VARCHAR2(10),
 bill_code						VARCHAR2(100),
 custom_value					VARCHAR2(100),
 sort_order						NUMBER(12),
 created_by 					number(18) NOT NULL,
 date_created                   DATE NOT NULL,
 modified_by 					number(18),
 date_modified    				DATE,
 version                        NUMBER(18,0) NOT NULL
)
/
ALTER TABLE promo_bill_code
ADD CONSTRAINT promo_bill_code_pk PRIMARY KEY (promo_bill_code_id)
	USING INDEX
/
ALTER TABLE promo_bill_code ADD CONSTRAINT promo_bill_code_fk
  FOREIGN KEY (promotion_id) REFERENCES promotion (promotion_id)
/
CREATE INDEX PROMO_BILL_CODE_PROMO_FK_idx ON promo_bill_code
  (PROMOTION_ID)
/