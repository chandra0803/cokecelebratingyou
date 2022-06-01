INSERT INTO OS_PROPERTYSET
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'plateau.platform.only','Platau Platform Only',1,0,0,NULL,0,0,NULL)
/
UPDATE OS_PROPERTYSET SET STRING_VAL = 'appproxy.biperf.com' WHERE ENTITY_NAME like 'shop.internal.proxy.%'
/
UPDATE OS_PROPERTYSET SET STRING_VAL = 'appproxy.biperf.com' WHERE ENTITY_NAME like 'certificate.proxy.%'
/