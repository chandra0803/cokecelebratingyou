ALTER TABLE RPT_AWARD_ITEM_SELECTION 
ADD ( 
  	INV_ITEM_ID VARCHAR2(54),
  	NODE_ID NUMBER(18),
  	AWARD_DATE DATE,
  	CREATED_BY NUMBER(18),
  	DATE_CREATED DATE,
  	MODIFIED_BY NUMBER(18),
  	DATE_MODIFIED DATE
    )
/