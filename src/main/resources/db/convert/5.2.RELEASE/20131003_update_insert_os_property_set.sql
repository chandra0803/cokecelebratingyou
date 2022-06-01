DELETE from os_propertyset WHERE ENTITY_NAME='proxy.pax.search.node.restriction'
/
DELETE from os_propertyset WHERE ENTITY_NAME='goalquest.participant.partners.allowed'
/
DELETE from os_propertyset WHERE ENTITY_NAME='goalquest.max.partners.allowed'
/
DELETE from os_propertyset WHERE ENTITY_NAME='goalquest.self.enrollment.enabled'
/
UPDATE os_propertyset SET STRING_VAL='<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> <html xmlns="http://www.w3.org/1999/xhtml"> <head>  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>  <title></title> <style type="text/css"> ${css} </style> </head> <body>  <table cellpadding="0" cellspacing="0" border="0" id="backgroundTable"> <tr> <td><table id="columnTable" width="90%" border="0" cellpadding="0" cellspacing="0"> <tr> <td rowspan="3" width="5%">&nbsp;</td> <td height="25">&nbsp;</td> <td rowspan="3" width="5%">&nbsp;</td> </tr> <tr> <td><table width="100%" border="0" cellpadding="0" cellspacing="0"> <tr> <td width="100%" height="110"><table width="100%" border="0" cellspacing="0" cellpadding="0"> <tr> <td id="logoContainer"><img id="logo" class="image_fix" src="${emailClientLogo}" alt="" title="" width="300" height="110" align="left" /></td> </tr> </table></td> </tr> <tr> <td id="topContainer" width="100%"> <!-- CONTENT STARTS HERE -->'
	WHERE ENTITY_NAME='email.wrapper.header'
/
UPDATE os_propertyset SET STRING_VAL='<!-- CONTENT ENDS HERE --> </td> </tr> <tr> <td id="bottomContainer" align="center" width="100%"><img class="image_fix" src="${emailPhoto}" alt="" title="" width="300" height="255" /></td> </tr> </table></td> </tr> <tr> <td height="43">&nbsp;</td> </tr> </table></td> </tr></table><!-- End of wrapper table --></body></html>'
	WHERE ENTITY_NAME='email.wrapper.footer'
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'self.enrollment.enabled','Self Enrollment Enabled?',1,0,0,NULL,0,0,NULL)
/
UPDATE os_propertyset SET DOUBLE_VAL=100, LONG_VAL=100
	WHERE ENTITY_NAME='pax.search.limit'
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'install.badges', 'Install Badges ?', 1, 1, 0, null, 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'profile.followlist.tab.show', 'Allow FollowList in Profile tab', 1, 1, 0, null, 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'allow.delegate', 'Allow Delegate ?', 1, 1, 0, null, 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'plateau.awards.reminder','Plateau Awards Reminder Enabled',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'plateau.awards.reminder.days','Plateau Awards Reminder Days',2,0,0,NULL,0,10,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'budget.transfer.show','Show budget transfer',1,1,0,null,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'install.throwdown', 'Is throwdown to display ?', 1, 0, 0, null, 0, 0 , null )
/
INSERT
INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
( entity_id_pk_sq.nextval,'password.variations.notallowed','Password Variations Not Allowed',5,0,0,'password,pa$$word,pa55word,p@ssword,passw0rd',0,0,NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'email.use.strongmail','Use Strong Mail',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset 
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
(entity_id_pk_sq.nextval,'strongmail.organization.name','StrongMail Organization Name',5,0,0,'admin',0,0,NULL)
/
INSERT INTO os_propertyset 
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
(entity_id_pk_sq.nextval,'strongmail.user.name','StrongMail User Name',5,0,0,'g5@strongmail.com',0,0,NULL)
/
INSERT INTO os_propertyset 
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
(entity_id_pk_sq.nextval,'strongmail.password.name','StrongMail Password',5,0,0,'bi.g5',0,0,NULL)
/
INSERT INTO os_propertyset 
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
(entity_id_pk_sq.nextval,'strongmail.sub.organization.id','StrongMail Sub Organization Id',5,0,0,'12800',0,0,NULL)
/

UPDATE os_propertyset SET STRING_VAL='3001,3002,3004'
	WHERE ENTITY_NAME='default.dashboard.charts'
/

UPDATE os_propertyset SET STRING_VAL='yyyy-MM-dd HH:mm:ss'
	WHERE ENTITY_NAME='sso.date.format'
/

