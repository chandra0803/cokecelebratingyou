CREATE SEQUENCE merch_order_bill_pk_sq INCREMENT BY 1 START WITH 5000
/
CREATE TABLE merch_order_bill_code
( merch_order_bill_code_id NUMBER(12) NOT NULL,
merch_order_id NUMBER(12) NOT NULL,
billing_code1 VARCHAR2(25),
billing_code2 VARCHAR2(25),
billing_code3 VARCHAR2(25),
billing_code4 VARCHAR2(25),
billing_code5 VARCHAR2(25),
billing_code6 VARCHAR2(25),
billing_code7 VARCHAR2(25),
billing_code8 VARCHAR2(25),
billing_code9 VARCHAR2(25),
billing_code10 VARCHAR2(25),
created_by NUMBER(18) NOT NULL,
date_created DATE NOT NULL,
modified_by NUMBER(18),
date_modified DATE,
version NUMBER(18,0) NOT NULL
)
/
ALTER TABLE merch_order_bill_code
ADD CONSTRAINT merchorderbill_pk PRIMARY KEY (merch_order_bill_code_id)
USING INDEX
/
ALTER TABLE merch_order_bill_code ADD CONSTRAINT merchorderbill_merch_order_fk
FOREIGN KEY (merch_order_id) REFERENCES merch_order(merch_order_id)
/