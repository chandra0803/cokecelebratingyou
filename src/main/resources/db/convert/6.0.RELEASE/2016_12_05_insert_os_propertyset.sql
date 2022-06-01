INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'elasticsearch.index.batch.size','Elasticsearch index batch size',2,0,0,NULL,0,500,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.url.dev','Elasticsearch host url-DEV',5,0,0,'https://550fe1f624f682311a1435834aea1d96.us-east-1.aws.found.io:9243',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.url.qa','Elasticsearch host url-QA',5,0,0,'https://550fe1f624f682311a1435834aea1d96.us-east-1.aws.found.io:9243',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.url.preprod','Elasticsearch host url-PPRD',5,0,0,'https://bd04c7414a4fde7aeb29fbc38d100ba2.us-east-1.aws.found.io:9243', 0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.url.prod','Elasticsearch host url-PRD',5,0,0,'https://2f888c19a421941567c0e2abd642b7b8.us-east-1.aws.found.io:9243',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.readtimeout','Elasticsearch Readtimeout',2,0,0,NULL,0,200000,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.proxy.url.dev','Elasticsearch proxy url-DEV',5,0,0,'userproxy.biperf.com',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.proxy.url.qa','Elasticsearch proxy url-QA',5,0,0,'appproxy.biperf.com',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.proxy.url.preprod','Elasticsearch proxy url-PPRD',5,0,0,'appproxy.biperf.com',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.proxy.url.prod','Elasticsearch proxy url-PRD',5,0,0,'appproxy.biperf.com',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.proxy.port.dev','Elasticsearch proxy port-DEV',2,0,0,NULL,0,8080,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.proxy.port.qa','Elasticsearch proxy port-QA',2,0,0,NULL,0,8080,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.proxy.port.preprod','Elasticsearch proxy port-PPRD',2,0,0,NULL,0,8080,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.proxy.port.prod','Elasticsearch proxy port-PRD',2,0,0,NULL,0,8080,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'elastic.search.enabled','Elasticsearch enabled',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'asset.debug','Asset Debug',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.max.result.size','Elasticsearch- maximum records size for search result',2,0,0,NULL,0,8,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.max.pagination.records','ElastiSsearch-Max # of displayed records for pagination',2,0,0,NULL,0,200,NULL)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval,'birth.date.year.rule','birth.date.year.rule (override/accept)',5,0,0,'accept',0,0,NULL)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval,'elasticsearch.creds.username.dev','ElasticSearch username DEV',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval,'elasticsearch.creds.username.qa','ElasticSearch username QA',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval,'elasticsearch.creds.username.preprod','ElasticSearch username PRE',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval,'elasticsearch.creds.username.prod','ElasticSearch username PROD',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval,'elasticsearch.creds.password.dev','ElasticSearch Password DEV',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval,'elasticsearch.creds.password.qa','ElasticSearch Password QA',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval,'elasticsearch.creds.password.preprod','ElasticSearch Password PRE',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval,'elasticsearch.creds.password.prod','ElasticSearch Password PROD',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset   
VALUES (ENTITY_ID_PK_SQ.NEXTVAL, 'report.recognition.comment.display', 'Display comment column in recognition report', 1, 1, 0, null, 0, 0, null)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.max.allowed.recognize','Elasticsearch- maximum pax allowed to recognize',2,0,0,NULL,0,300,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.delay.millis','The delay in millis for the autocomplete trigger',2,0,0,NULL,0,150,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'meplus.enabled','MEPULS Installed',1,0,0,NULL,0,0,NULL)
/
INSERT INTO OS_PROPERTYSET
(ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE, BOOLEAN_VAL, DOUBLE_VAL, LONG_VAL, INT_VAL)
VALUES
(ENTITY_ID_PK_SQ.nextval, 'public.recog.days.check.recommended', 'Public recognition (recommeneded) number of days to check', 2, 0, 0, 0, 60)
/
INSERT INTO OS_PROPERTYSET
   (ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE, BOOLEAN_VAL, DOUBLE_VAL, LONG_VAL, INT_VAL)
VALUES
(ENTITY_ID_PK_SQ.nextval,'public.recog.levels.check.team', 'Public recognition (team) number of levels to check', 2, 0, 0, 0, 2)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'instantpoll','Instant Poll',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'celebration','Celebration',1,1,0,NULL,0,0,NULL)
/