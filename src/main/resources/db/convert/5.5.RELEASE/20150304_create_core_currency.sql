CREATE TABLE CURRENCY
    (CURRENCY_ID            NUMBER(18,0) NOT NULL,
     CM_ASSET_NAME          VARCHAR2(50) NOT NULL,
     CURRENCY_CODE          VARCHAR2(3)  NOT NULL,
     CURRENCY_SYMBOL        VARCHAR2(5),
     STATUS                 VARCHAR2(8) NOT NULL,    
     CREATED_BY             number(18)  NOT NULL,
     DATE_CREATED           DATE NOT NULL,
     MODIFIED_BY            number(18),
     DATE_MODIFIED          DATE,
     VERSION                NUMBER(18,0) NOT NULL)
/
ALTER TABLE CURRENCY
ADD CONSTRAINT CURRENCY_ID_PK PRIMARY KEY (CURRENCY_ID)
USING INDEX
/
ALTER TABLE CURRENCY
ADD CONSTRAINT CURRENCY_UN UNIQUE (CM_ASSET_NAME)
using index
/
ALTER TABLE CURRENCY
ADD CONSTRAINT CURRENCY_UCC UNIQUE (CURRENCY_CODE)
using index
/
CREATE SEQUENCE CURRENCY_PK_SQ
  INCREMENT BY 1
  START WITH 5001
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  NOCYCLE
  NOORDER
  CACHE 20
/