INSERT INTO FILTER_MODULE_APP
   (FILTER_MODULE_APP_ID,MODULE_APP_ID,FILTER_SETUP_TYPE,PRIORITY,ORDER_NUMBER,CREATED_BY, DATE_CREATED,VERSION)
VALUES
   (FILTER_MODULE_APP_PK_SQ.nextval, 3017, 'home', 1, 2, 5662, sysdate, 1)
/
INSERT INTO FILTER_MODULE_APP
   (FILTER_MODULE_APP_ID,MODULE_APP_ID,FILTER_SETUP_TYPE,PRIORITY,ORDER_NUMBER,CREATED_BY, DATE_CREATED,VERSION)
VALUES
   (FILTER_MODULE_APP_PK_SQ.nextval, 3031, 'home', 1, 3, 5662, sysdate, 1)
/
UPDATE FILTER_MODULE_APP SET ORDER_NUMBER=4 WHERE MODULE_APP_ID=3006 AND FILTER_SETUP_TYPE='home' AND ORDER_NUMBER=2
/
UPDATE FILTER_MODULE_APP SET ORDER_NUMBER=5 WHERE MODULE_APP_ID=3008 AND FILTER_SETUP_TYPE='home' AND ORDER_NUMBER=3
/
UPDATE FILTER_MODULE_APP SET ORDER_NUMBER=6 WHERE MODULE_APP_ID=3000 AND FILTER_SETUP_TYPE='home' AND ORDER_NUMBER=4
/
UPDATE FILTER_MODULE_APP SET ORDER_NUMBER=7 WHERE MODULE_APP_ID=3010 AND FILTER_SETUP_TYPE='home' AND ORDER_NUMBER=5
/
UPDATE FILTER_MODULE_APP SET ORDER_NUMBER=8 WHERE MODULE_APP_ID=3014 AND FILTER_SETUP_TYPE='home' AND ORDER_NUMBER=6
/
UPDATE FILTER_MODULE_APP SET ORDER_NUMBER=9 WHERE MODULE_APP_ID=3007 AND FILTER_SETUP_TYPE='home' AND ORDER_NUMBER=7
/
UPDATE FILTER_MODULE_APP SET ORDER_NUMBER=10 WHERE MODULE_APP_ID=3018 AND FILTER_SETUP_TYPE='home' AND ORDER_NUMBER=8
/