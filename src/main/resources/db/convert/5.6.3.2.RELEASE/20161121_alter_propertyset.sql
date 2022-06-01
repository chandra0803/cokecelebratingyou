INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'translation.service.url.dev', 'Dev Translation Service URL', 5, 0, 0, 'https://translatorqa.biworldwide.com/translationservices/services/v1.0/', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'translation.service.url.qa', 'QA Translation Service URL', 5, 0, 0, 'https://translatorqa.biworldwide.com/translationservices/services/v1.0/', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'translation.service.url.preprod', 'PPRD Translation Service URL', 5, 0, 0, 'https://translatorpprd.biworldwide.com/translationservices/services/v1.0/', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'translation.service.url.prod', 'Prod Translation Service URL', 5, 0, 0, 'https://translator.biworldwide.com/translationservices/services/v1.0/', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'translation.bill.code', 'Translation Bill Code', 5, 0, 0, 'ChangeMePID', 0, 0 , null )
/
DELETE FROM os_propertyset
WHERE entity_name='public.rec.wall.kong.header.value'
/