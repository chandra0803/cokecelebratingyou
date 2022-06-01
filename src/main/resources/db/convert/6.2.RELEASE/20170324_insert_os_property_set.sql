INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES (ENTITY_ID_PK_SQ.nextval, 'bi.uaws.endpoint.url.preprod', 'Under Armour Micro Service REST endpoint', 5, 0, 0, 'http://uamicroservices-pprd.wzdcgsifm9.us-west-2.elasticbeanstalk.com/uamicroservices/rest', 0, 0, null)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES (ENTITY_ID_PK_SQ.nextval, 'bi.uaws.endpoint.url.prod', 'Under Armour Micro Service REST endpoint', 5, 0, 0, 'http://uamicroservices-prod.wzdcgsifm9.us-west-2.elasticbeanstalk.com/uamicroservices/rest', 0, 0, null)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'bi.uaws.authorize.url','Under Armour authorize url path',5,0,0,'https://www.mapmyfitness.com/v7.1/oauth2/uacf/authorize/?',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'bi.ua.microservice.enabled','Under Armour Micro Service Enabled?',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'bi.ua.microservice.code','Under Armour Code',5,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'bi.ua.microservice.client.id','Under Armour client id',5,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'bi.ua.microservice.encryption.salt','Under Armour encryption salt',5,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'bi.ua.microservice.repo','Under Armour Micro Service repo (mock, non-mock)',5,0,0,'non-mock',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES (ENTITY_ID_PK_SQ.nextval, 'bi.uaws.endpoint.url.qa', 'Under Armour Micro Service REST endpoint', 5, 0, 0, 'http://sample-env-2.j3zexxfnfb.ap-south-1.elasticbeanstalk.com/uamicroservices/rest', 0, 0, null)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES (ENTITY_ID_PK_SQ.nextval, 'bi.uaws.endpoint.url.dev', 'Under Armour Micro Service REST endpoint-DEV', 5, 0, 0, 'http://sample-env-2.j3zexxfnfb.ap-south-1.elasticbeanstalk.com/uamicroservices/rest', 0, 0, null)
/
