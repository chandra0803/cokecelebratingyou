INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'password.history.count','User password history check count',2, 0, 0, null, 0, 5, NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'password.otp.expiry.days','User one time password expiration days',3, 0, 0, null, 86400000, 0, NULL)
/
update os_propertyset set ENTITY_KEY='allowed values:mine,followed,team,global,recommended', STRING_VAL='recommended' where ENTITY_NAME='public.recog.default.tab.name'
/
update os_propertyset set STRING_VAL='mine,followed,team,global,recommended' where ENTITY_NAME='public.recog.active.tabs'
/
update os_propertyset set ENTITY_KEY='allowed values:search,followed,team,global,recommended', STRING_VAL='recommended' where ENTITY_NAME='purl.celeb.default.tab.name'
/
update os_propertyset set STRING_VAL='search,followed,team,global,recommended' where ENTITY_NAME='purl.celeb.active.tabs'
/
update os_propertyset set STRING_VAL='https://biwdispatcher.performnet.com/biw-dispatcher/catalog/1/login.action' where ENTITY_NAME='shop.internalremoteurl.prod'
/
update os_propertyset set STRING_VAL='https://biwdispatcher.performnet.com/biw-dispatcher/rewardOfferings/1/' where ENTITY_NAME='reward.offerings.endpoint.prod'
/