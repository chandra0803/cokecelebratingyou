delete from os_propertyset 
where ENTITY_NAME 
in ('shop.internalproxyport.prod','shop.internalproxyport.preprod','shop.internalproxyport.qa','shop.internalproxyport.dev',
    'shop.internal.proxy.prod','shop.internal.proxy.preprod','shop.internal.proxy.qa','shop.internal.proxy.dev',
	'diy.tips.available','diy.resource.center.available','diy.news.stories.available','diy.banners.available')
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'webservices.url.dev','Web Services URL-DEV',5,0,0,'http://bankservicesqa.biworldwide.com/banqsharedservices/services',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'webservices.url.qa','Web Services URL-QA',5,0,0,'http://bankservicesqa.biworldwide.com/banqsharedservices/services',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'webservices.url.preprod','Web Services URL-Preprod',5,0,0,'http://bankservicespprd.biworldwide.com/banqsharedservices/services',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'webservices.url.prod','Web Services URL-Prod',5,0,0,'http://bankservices.biworldwide.com/banqsharedservices/services',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'show.participant.birth.date','Show Participant Birth Date?',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'show.participant.hire.date','Show Participant Hire Date?',1,0,0,NULL,0,0,NULL)
/
Insert into OS_PROPERTYSET
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
values
(entity_id_pk_sq.nextval,'sea.security.salt','sea.security.salt',5,0,0,'salt',0,0,null)
/
Insert into OS_PROPERTYSET
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
values
(entity_id_pk_sq.nextval,'sea.email.account','sea.email.account',5,0,0,'seaqa@biworldwide.com',0,0,null)
/
Insert into OS_PROPERTYSET
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
values
(entity_id_pk_sq.nextval,'sea.email.password','sea.email.password',5,0,0,'Password1!',0,0,null)
/
Insert into OS_PROPERTYSET
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
values
(entity_id_pk_sq.nextval,'sea.days.to.abandoned','sea.days.to.abandoned',2,0,0,null,0,3,null)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'install.ssi','SSI Installed',1,1,0,NULL,0,0,NULL)
/
insert into os_propertyset
values 
(entity_id_pk_sq.nextval,'import.pax.file.name','pax_file_name',5,0,0,'Pax_PreImport.csv', 0,0,null)
/
insert into os_propertyset
values 
(entity_id_pk_sq.nextval,'unix.movefile.scriptpath','unix_movefile_script',5,0,0,'/apps/local/scripts/', 0,0,null)
/
insert into os_propertyset
values (entity_id_pk_sq.nextval,'unix.movefile.scriptname','unix_movefile_script',5,0,0,'dev_ms_movefile.sh', 0,0,null)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'ssi.progress.upload.size.limit','SSI Progress Upload Size Limit In MB',2,0,0,NULL,0,3,NULL)
/
UPDATE OS_PROPERTYSET 
SET STRING_VAL = '172.16.*,10.20.70.*,10.20.71.*,10.20.72.*,10.20.73.*,198.246.149.241,198.246.149.242,198.246.149.243,61.12.73.*, 61.12.45.*'
WHERE ENTITY_NAME LIKE 'ips.to.allow.csv.regex'
/
UPDATE OS_PROPERTYSET 
SET STRING_VAL = '<!-- CONTENT ENDS HERE --> </td> </tr> <tr> <td id="bottomContainer" align="center" width="100%"><img class="image_fix" src="${emailPhoto}" alt="" title="" width="300" height="255" /></td> </tr> </table></td> </tr> <tr> <td height="43">&nbsp;</td> </tr> </table></td> </tr></table><!-- End of wrapper table --></body></html>'
WHERE ENTITY_NAME LIKE 'email.wrapper.footer'
/
