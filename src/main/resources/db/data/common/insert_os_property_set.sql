delete os_propertyset
/
---
--- SYSTEM VARIABLES 
---
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'install.productclaims','Product Claims Installed',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'install.recognition','Recognition Installed',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'install.quizzes','Quizzes Installed',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'install.nominations','Nominations Installed',1,1,0,NULL,0,0,NULL)
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
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'install.survey','Survey Installed',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'install.wellness','Wellness Installed',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'install.goalquest','GoalQuest Installed',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'install.challengepoint','ChallengePoint Installed',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'install.ssi','SSI Installed',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'boolean.include.balance','user info',1,1,0,'',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'contact.us.email','Contact Us Email',5,0,0,'cheng@biworldwide.com',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'lockout.failure.count','Auth Failure Count For Lockout',2,0,0,NULL,0,4,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'password.pattern','Pattern to generate default password',5,0,0,'A###A###',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'password.min.length','Password Min Length',2,0,0,NULL,0,8,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'password.should.use.regex','Password Use Regex',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'password.not.match.regex','Password Not Match Regex',5,0,0,'[^a-z]+|[^A-Z]+|[^0-9]+|\p{Punct}+',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'password.can.reuse','Can Reuse Password',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'password.expired.period','Password Expired Period (days)',3,0,0,NULL,6307200000,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'password.force.reset', 'Force Password Reset For Pax', 1, 1, 0, null, 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'password.use.initial', 'Use Initial Password', 1, 0, 0, null, 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'password.initial','Initial Password',5,0,0,null,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'claim.processing.allow.batch','Allow Claim Batch Processing',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'client.launch.date','Client Launch Date',7,0,0,'',0,0,to_date('01-01-2005', 'MM-DD-YYYY'))
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'awardbanq.source.system', 'AwardBanQ Source System', 5, 0, 0, 'EXTERNAL', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'awardbanq.realtime.userid', 'AwardBanQ Realtime User Id', 5, 0, 0, 'ORACLEID', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'awardbanq.organization.number', 'AwardBanQ Org Number', 5, 0, 0, '01001', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'awardbanq.mode', 'AwardBanQ Mode (awardbanq, mock, none)', 5, 0, 0, 'mock', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'awardbanq.convertcert.used', 'Is Convert Certificate used?', 1, 0, 0, null, 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'should.restrict.by.ip','Restrict Access based on IP',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'ips.to.allow.csv.regex','Only allow these IPs',5,0,0,'172.16.*,10.20.70.*,10.20.71.*,10.20.72.*,10.20.73.*,198.246.149.241,198.246.149.242,198.246.149.243,61.12.73.*, 61.12.45.*',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'shop.internalremoteurl.dev','Shop Internal Remote URL-DEV',5,0,0,'https://biwdispatcherpprd.performnet.com/biw-dispatcher/catalog/1/login.action',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'shop.internalremoteurl.qa','Shop Internal Remote URL-QA',5,0,0,'https://biwdispatcherpprd.performnet.com/biw-dispatcher/catalog/1/login.action',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'shop.internalremoteurl.preprod','Shop InternalRemoteURL-Preprod',5,0,0,'https://biwdispatcherpprd.performnet.com/biw-dispatcher/catalog/1/login.action',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'shop.internalremoteurl.prod','Shop Internal Remote URL-Prod',5,0,0,'https://biwdispatcher.performnet.com/biw-dispatcher/catalog/1/login.action',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'goalquest.merchlinqdestenv.prod','GoalQuestMerchLinqDestEnv-Prod',5,0,0,'prod',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'goalquest.merchlinqdestenv.dev','GoalQuestMerchLinqDestEnv-Dev',5,0,0,'qa',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'goalquest.merchlinqdestenv.qa','GoalQuestMerchLinqDestEnv-QA',5,0,0,'qa',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'goalquest.merchlinqdestenv.preprod','GoalQuestMerchLinqDestEnv-Preprod',5,0,0,'preprod',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'client.name','Client Name',5,0,0,'G4',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'client.url','Client URL',5,0,0,'/login.do',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'client.contact.url','Client Contact Us URL',5,0,0,'/contactUsPublic.do',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'participant.allow.contacts','Allow Contacts?',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'participant.allow.estatements','Allow estatements?',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'allow.find.others','Allow Find Others',1,1,0,NULL,0,0,NULL)
/
insert into os_propertyset
values (entity_id_pk_sq.nextval,'import.client.prefix','client_prefix',5,0,0,
        'aaqd', 0,0,null)
/
insert into os_propertyset
values (entity_id_pk_sq.nextval,'import.file.utl_path','db_utl_path',5,0,0,
        '/work/wip/qa/aawm', 0,0,null)
/
insert into os_propertyset
values (entity_id_pk_sq.nextval,'all.users.proxy','proxy',1,1,0,
         null, 0,0,null)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'escalation.hierarchy.id','Escalation Hierarchy Name',3,0,0,5001,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'email.address.system','System Notification Email Address',5,0,0,'system@biperf.com',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'email.address.system.addl','System Notification Additional Email Address',5,0,0,'',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'email.subject.prefix','Subject Prefix',5,0,0,'[LOCALHOST]',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'email.subject.prefix.display','Display Subject Prefix',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'email.address.system.incentive','Incentive System E-mail Address',5,0,0,'incentive@biperf.com',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'email.address.system.personaldisplay','System Personal Name Display',5,0,0,'System Generated Message',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'email.address.incentive.personaldisplay','Incentive Personal Name Display',5,0,0,'G3 Rewards',0,0,NULL)
/
INSERT INTO OS_PROPERTYSET
   (ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE, BOOLEAN_VAL, DOUBLE_VAL, STRING_VAL, LONG_VAL, INT_VAL)
VALUES
(entity_id_pk_sq.nextval, 'email.notification.senderaddr', 'Email Notification Address', 5, 0, 0, 'notification@biworldwide.com', 0, 0)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'email.welcome.loginAndPassword.separate','send Login ID and Password separately',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'email.bounceback.verified','bounce back email verified?',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'discretionary.award.min', 'Discretionary Award Minimum Amount', 3, 0, 0, NULL, 1, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'discretionary.award.max', 'Discretionary Award Maximum Amount', 3, 0, 0, NULL, 1000, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'site.url.dev', 'Dev Site URL', 5, 0, 0, 'http://localhost:8001/beacon', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'site.url.qa', 'QA Site URL', 5, 0, 0, 'http://pentagqa.performnet.com/pentag', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'site.url.preprod', 'PPRD Site URL', 5, 0, 0, 'http://pentagpprd.performnet.com/pentag', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'site.url.prod', 'Prod Site URL', 5, 0, 0, 'http://pentag.performnet.com/pentag', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'email.wrapper.header','Email Wrapper Header',5,0,0,'<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> <html xmlns="http://www.w3.org/1999/xhtml"> <head>  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>  <title></title> <style type="text/css"> ${css} </style> </head> <body>  <table cellpadding="0" cellspacing="0" border="0" id="backgroundTable"> <tr> <td rowspan="3" width="5%" class="shim-side">&nbsp;</td><td height="50" class="shim-top">&nbsp;</td><td rowspan="3" width="5%" class="shim-side">&nbsp;</td></tr><tr><td><table id="columnTable" width="100%" border="0" cellpadding="0" cellspacing="0"> <tr> <td rowspan="3" width="5.5%">&nbsp;</td> <td height="25">&nbsp;</td> <td rowspan="3" width="5.5%">&nbsp;</td> </tr> <tr> <td><table width="100%" border="0" cellpadding="0" cellspacing="0"> <tr> <td width="100%" height="110"><table width="100%" border="0" cellspacing="0" cellpadding="0"> <tr> <td id="logoContainer"><img id="logo" class="image_fix" src="${emailClientLogo}" alt="" title="" width="300" height="110" align="left" /></td> </tr> </table></td> </tr> <tr> <td id="topContainer" width="100%"> <!-- CONTENT STARTS HERE -->',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'email.wrapper.footer','Email Wrapper Footer',5,0,0,'<!-- CONTENT ENDS HERE --> </td> </tr></table></td> </tr> <tr> <td height="10">&nbsp;</td> </tr> </table></td> </tr><tr><td height="50" class="shim-top">&nbsp;</td></tr></table><!-- End of wrapper table --></body></html>',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'termsAndConditions.used','Are Terms & Conditions used?',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'termsAndConditions.usercanaccept','Can an user accept T&C for the pax?',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'termsAndConditionsView.display','Display T&Cs link on Login?',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'project.id','project id',5,0,0,'beacon',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'goalquest.merchlinqsso.url.dev','GoalQuestMerchLinqURL-Dev',5,0,0,'http://www1qa.awardslinq.com/merchlinq/signon.action',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'goalquest.merchlinqsso.url.qa','GoalQuestMerchLinqURL-QA',5,0,0,'http://www1qa.awardslinq.com/merchlinq/signon.action',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'goalquest.merchlinqsso.url.preprod','GoalQuestMerchLinqURL-PreProd',5,0,0,'http://www1pprd.awardslinq.com/merchlinq/signon.action',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'goalquest.merchlinqsso.url.prod','GoalQuestMerchLinqURL-Prod',5,0,0,'http://www1.awardslinq.com/merchlinq/signon.action',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'login.welcome.message','Login page Welcome Message Display',5,0,0,'G4 Rewards',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'cap.provider.url.dev', 'DEV Order History Provider URL', 5, 0, 0, 'http://wwwpprd.my-order-status.com/apps/orderstatus/OrderInformationServlet?param1=', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'cap.provider.url.qa', 'QA Order History Provider URL', 5, 0, 0, 'http://wwwpprd.my-order-status.com/apps/orderstatus/OrderInformationServlet?param1=', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'cap.provider.url.pprd', 'PREPROD Order History Provider URL', 5, 0, 0, 'http://wwwpprd.my-order-status.com/apps/orderstatus/OrderInformationServlet?param1=', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'cap.provider.url.prod', 'PROD Order History Provider URL', 5, 0, 0, 'http://www.my-order-status.com/apps/orderstatus/OrderInformationServlet?param1=', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'awardbanq.deposit.retry.count', 'Max retries of failed AwardBanQ deposit', 2, 0, 0, null, 0, 3, null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'awardbanq.deposit.retry.delay', 'Secs in btween AwardBanQ deposit retries', 3, 0, 0, null, 600, 0, null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'tip.day.rotate.seconds', 'Secs delay to rotate the Tip of the Day', 2, 0, 0, null, 0, 10, null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'merchlinq.url.dev','Merchlinq ordering url',5,0,0,'http://www1qa.awardslinq.com/merchlinq/secure/welcome.do',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'merchlinq.url.qa','Merchlinq ordering url',5,0,0,'http://www1qa.awardslinq.com/merchlinq/secure/welcome.do',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'merchlinq.url.preprod','Merchlinq ordering url',5,0,0,'http://www1pprd.awardslinq.com/merchlinq/secure/welcome.do',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'merchlinq.url.prod','Merchlinq ordering url',5,0,0,'http://www1.awardslinq.com/merchlinq/secure/welcome.do',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'merchorder.phone','Merchlinq ordering phone#',5,0,0,'(555) 555-1212',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'self.enrollment.enabled','Self Enrollment Enabled?',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'display.table.max.per.single.page','Max single-page table row count',3,0,0,NULL,250,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'display.table.max.per.multi.page','Max multi-page table row count',3,0,0,NULL,100,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'import.page.size','Import Page Size',2,0,0,NULL,0,100,NULL)
/

INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'merchandise.billing.code.char', 'Merchandise billing code char', 5, 0, 0, 'changeme', 0, 0 , NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'fileload.userrole.delete', 'Allow Delete User Role Through File Load', 1, 1, 0, NULL, 0, 0 , NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'awardbanq.pax.update.process.batch.size','Awardbanq Pax Update Process Batch Size',2,0,0,NULL,0,10,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'messenger.app.code','Messenger Application Code',5,0,0,'FIX ME',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'secondary.logo.enable','Secondary Logo Enable',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
( entity_id_pk_sq.nextval, 'google.analytics.account', 'Google Analytics Account - "none" to turn off', 5, 0, 0, 'none', 0, 0 , null )
/
insert into os_propertyset
values (entity_id_pk_sq.nextval,'seeddatafile.temp','Temporary Seed Data File',5,0,0,
        'external_file_1.txt', 0,0,null)
/
insert into os_propertyset
values (entity_id_pk_sq.nextval,'seeddatafile.orig','Original Seed Data File',5,0,0,
        'external_file_1.txt', 0,0,null)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'fileload.token.dev','Fileload Token - Dev',5,0,0,'G3Redesign-dev',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'fileload.token.qa','Fileload Token - QA',5,0,0,'G3Redesign-qa',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'fileload.token.preprod','Fileload Token - PreProd',5,0,0,'G3Redesign-pprd',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'fileload.token.prod','Fileload Token - Prod',5,0,0,'G3Redesign-prod',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'purl.allow.facebook','PURL Allow Facebook',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'purl.allow.twitter','PURL Allow Twitter',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'purl.allow.linkedin','PURL Allow LinkedIn',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'purl.days.to.expiration', 'PURL Days to Expiration', 2, 0, 0, null, 0, 60, null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'purl.available','PURL Available',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'system.image.upload.size.limit','Image Upload Size Limit In MB',2,0,0,NULL,0,3,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'purl.url.dev','PURL URL - Dev',5,0,0,'http://beacon2qa.recognitionpurl.com/beacon2',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'purl.url.qa','PURL URL - QA',5,0,0,'http://pentagqa.recognitionpurl.com/pentag',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'purl.url.preprod','PURL URL - PreProd',5,0,0,'http://pentagpprd.recognitionpurl.com/pentag',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'purl.url.prod','PURL URL - Prod',5,0,0,'http://pentag.recognitionpurl.com/pentag',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'facebook.api.key','Facebook API key',5,0,0,'dfbaaff5a4a727baffce33604e5e2431',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'facebook.api.secret','Facebook API secret',5,0,0,'244b798622224f283ef4a547b1a9c038',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'facebook.api.id','Facebook API id',5,0,0,'109119199167104',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'twitter.consumer.key','Twitter consumer key',5,0,0,'b32Mew5VCFr9t6zzGJ2HXA',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'twitter.consumer.secret','Twitter consumer secret',5,0,0,'C8uJ2NaNRZrOqPv8H7JPnxREikHNW9c4NAFq9jfnm8',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'email.batch.enable','Email Batch Enabled',1,0,0,'',0,0,NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'roster.management.available','Roster Management Available',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'default.language','Default Language',5,0,0,'en_US',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'default.country','Default Country',5,0,0,'us',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'run.campaign.transfer.process','Run Campaign Transfer Porcess',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'texteditor.dictionaries','Texteditor Dictionaries',5,0,0,'English_United States=en_us,English_British=en_gb,Spanish=es_mx,German=de_de,French_Canadian=fr_ca',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'boolean.enable.global.standalone','Global Standalone',1,0,0,'',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'global.fileprocessing.webdav.dev','Webdav URL - Dev',5,0,0,'https://workqa.bius.bi.corp:8443/qa',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'global.fileprocessing.webdav.qa','Webdav URL - QA',5,0,0,'https://workqa.bius.bi.corp/qa',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'global.fileprocessing.webdav.preprod','Webdav URL - PreProd',5,0,0,'https://workpre.bius.bi.corp/pre',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'global.fileprocessing.webdav.prod','Webdav URL - Prod',5,0,0,'https://work.bius.bi.corp/prd',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'global.fileprocessing.workwip.dev','WorkWIP - DEV',5,0,0,'/work/wip/qa',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'global.fileprocessing.workwip.qa','WorkWIP - QA',5,0,0,'/work/wip/qa',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'global.fileprocessing.workwip.preprod','WorkWIP - PreProd',5,0,0,'/work/wip/pre',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'global.fileprocessing.workwip.prod','WorkWIP - Prod',5,0,0,'/work/wip/prd',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'global.fileprocessing.prefix','Global File Processing PREFIX',5,0,0,'prefix-changeme',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'global.fileprocessing.subfolder','Global File Processing SUBFOLDER',5,0,0,'subfolder-changeme',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'deposit.process.send.pax.deposit.email', 'Deposit Process can send PAX deposit email?',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'email.servers', 'Comma separated list of Email Server(s)',5,0,0,'strongmail2.biperf.com,strongmail3.biperf.com',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'reward.offerings.endpoint.dev','Reward Offerings Service REST endpoint - DEV',5,0,0,'https://biwdispatcherpprd.performnet.com/biw-dispatcher/rewardOfferings/1/',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'reward.offerings.endpoint.qa','Reward Offerings Service REST endpoint - QA',5,0,0,'https://biwdispatcherpprd.performnet.com/biw-dispatcher/rewardOfferings/1/',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'reward.offerings.endpoint.preprod','Reward Offerings Service REST endpoint - PreProd',5,0,0,'https://biwdispatcherpprd.performnet.com/biw-dispatcher/rewardOfferings/1/',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'reward.offerings.endpoint.prod','Reward Offerings Service REST endpoint - Prod',5,0,0,'https://biwdispatcher.performnet.com/biw-dispatcher/rewardOfferings/1/',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'install.leaderboard', 'Is Leaderboard to display ?', 1, 1, 0, null, 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'system.video.upload.size.limit','Video Upload Size Limit In MB',2,0,0,NULL,0,30,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'system.pdf.upload.size.limit','Pdf Upload Size Limit In MB',2,0,0,NULL,0,3,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'public.recog.default.tab.name','allowed values:mine,followed,team,global,recommended',5,0,0,'recommended',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'public.recog.active.tabs','Comma separated list of active tabs',5,0,0,'mine,followed,team,global,recommended,country,department',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'purl.celeb.default.tab.name','allowed values:search,followed,team,global,recommended',5,0,0,'recommended',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'purl.celeb.active.tabs','Comma separated list of active tabs',5,0,0,'search,followed,team,global,recommended,country,department',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'smack.talk.default.tab.name','allowed values:mine,team,global',5,0,0,'global',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'smack.talk.active.tabs','Comma separated list of active tabs',5,0,0,'mine,team,global',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'client.large.audience','Client Large Audience',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'manager.send.alrt','Manager Send Alert',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'pax.search.limit','Pax search result max rows',2,0,100,NULL,100,100,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'banner.ad.limit','Banner ad max results',2,0,0,NULL,0,10,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'homepage.filters.strategy.threaded','Threading model(single or multi) 4 building homepage filters',1,1,0,null, 0,0,null)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.url.dev','URL shortner URL-DEV',5,0,0,'http://recog.co/yourls-api.php',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.url.qa','URL shortner URL-QA',5,0,0,'http://recog.co/yourls-api.php',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.url.preprod','URL shortner URL-Preprod',5,0,0,'http://recog.co/yourls-api.php',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.url.prod','URL shortner URL-Prod',5,0,0,'http://recog.co/yourls-api.php',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.signature.dev','URL shortner signature URL-DEV',5,0,0,'0b9f20a71a',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.signature.qa','URL shortner signature URL-QA',5,0,0,'0b9f20a71a',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.signature.preprod','URL shortner signature URL-Preprod',5,0,0,'0b9f20a71a',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'urlshortner.signature.prod','URL shortner signature URL-Prod',5,0,0,'0b9f20a71a',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'firstTimeLogin.required.email','First Time Login Page',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'large.audience.report.generation.enabled','Use Large Audience Asynchronous Report Generateion',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'import.file.location','Import File Location',5,0,0,'/customs/dev/process/nds/BEACON/valid',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
( entity_id_pk_sq.nextval, 'sso.unique.id','Unique ID for SSO login verification(SSO ID, Login ID, None)', 5, 0, 0, 'None', 0, 0, NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'sso.timelag.allowed','Time lag allowed on SSO date stamp',3,0,0,null,60000,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
( entity_id_pk_sq.nextval, 'sso.date.format','SSO Date Format', 5, 0, 0, 'yyyy-MM-dd HH:mm:ss', 0, 0, NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
( entity_id_pk_sq.nextval, 'sso.sender.time.zone','SSO Sender Time Zone', 5, 0, 0, 'GMT', 0, 0, NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
( entity_id_pk_sq.nextval, 'sso.secret.key','SSO Secret Key', 5, 0, 0, '5aDC7edrAvuchekacR6xevAN3D6eZbspeT5thSswUz7kuwReb73u9ruDE7AjuduQE', 0, 0, NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
( entity_id_pk_sq.nextval, 'sso.aes256.key','SSO AES256 Key', 5, 0, 0, '-114,104,-97,75,-105,40,-51,-32,-126,104,34,41,38,-45,120,-116,110,-13,22,6,-127,-59,81,-50,71,48,-11,99,69,-122,31,95', 0, 0, NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES 
( entity_id_pk_sq.nextval, 'sso.init.vector','SSO Initialization Vector', 5, 0, 0, '-45,120,-116,-13,75,-51,-32,34,6,-127,-59,81,-50,-122,31,-105', 0, 0, NULL )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES ( entity_id_pk_sq.nextval, 'default.dashboard.charts', 'Default Dashbaord Charts', 5, 0, 0, '3001,3002,3004', 0, 0 , null )
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'public.recog.allow.facebook','Public Recognition Allow Facebook',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'public.recog.allow.twitter','Public Recognition Allow Twitter',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'public.recog.allow.linkedin','Public Recognition Allow LinkedIn',1,0,0,NULL,0,0,NULL)
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
(entity_id_pk_sq.nextval,'server.instance.suffix','Server instance',5,0,0,'.biperf.com:51204',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'budget.transfer.show','Show budget transfer',1,1,0,null,0,0,NULL)
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
(entity_id_pk_sq.nextval,'email.use.strongmail','Use Strong Mail for Welcome Email',1,0,0,NULL,0,0,NULL)
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
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'install.throwdown','Throwdown Installed',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'report.startDate.rollback.days','Number of days back to default start date',2,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'chatter.consumer.key.dev','Chatter Consumer Key Dev',5,0,0,'change me',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'chatter.consumer.secret.dev','Chatter Consumer Secret Dev',5,0,0,'change me',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'chatter.consumer.key.qa','Chatter Consumer Key QA',5,0,0,'change me',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'chatter.consumer.secret.qa','Chatter Consumer Secret QA',5,0,0,'change me',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'chatter.consumer.key.preprod','Chatter Consumer Key PPRD',5,0,0,'change me',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'chatter.consumer.secret.preprod','Chatter Consumer Secret PPRD',5,0,0,'change me',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'chatter.consumer.key.prod','Chatter Consumer Key Prod',5,0,0,'change me',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'chatter.consumer.secret.prod','Chatter Consumer Secret Prod',5,0,0,'change me',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'chatter.callback.url','Chatter call back url path',5,0,0,'/participant/chatterAuthorizationSubmit.do?method=chatterAuthorizationCallback',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'purl.allow.chatter','PURL Allow Chatter',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'public.recog.allow.chatter','Public Recognition Allow Chatter',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES (entity_id_pk_sq.nextval,'survey.report.response.count','Minimum Survey Response Count',2,0,0,null,0,3,null)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES (entity_id_pk_sq.nextval,'nomination.report.comment.available','Nomination Extract Comment Available',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'install.engagement','Engagement Installed',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'allow.password.field.auto.complete','Allow Password Field Auto Complete',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'machine.language.allow.translation','Machine Language Translation Enabled',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'google.cloud.messaging.server.id','Google Cloud Messaging Server ID',5,0,0,'AIzaSyDEijaWALEr8AosgySN7PoCnZD9z0wFMwQ',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
( entity_id_pk_sq.nextval, 'diy.audience.export.limit', 'DIY Audience Export Limit', 3, 0, 0, NULL, 50, 0 , null )
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
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'ssi.progress.upload.size.limit','SSI Progress Upload Size Limit In MB',2,0,0,NULL,0,3,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'webservices.url.dev','Web Services URL-DEV',5,0,0,'https://bankservicesqa.biworldwide.com/banqsharedservices/services',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'webservices.url.qa','Web Services URL-QA',5,0,0,'https://bankservicesqa.biworldwide.com/banqsharedservices/services',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'webservices.url.preprod','Web Services URL-Preprod',5,0,0,'https://bankservicespprd.biworldwide.com/banqsharedservices/services',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'webservices.url.prod','Web Services URL-Prod',5,0,0,'https://bankservices.biworldwide.com/banqsharedservices/services',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'shared.services.security.key.qa','Shared Services Security Key',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'shared.services.security.key.preprod','Shared Services Security Key',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'shared.services.security.key.prod','Shared Services Security Key',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'shared.services.security.key.dev','Shared Services Security Key',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'plateau.platform.only','Platau Platform Only',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'bankenrollment.characteristic1','Bank Enrollment Characteristic 1',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'bankenrollment.characteristic2','Bank Enrollment Characteristic 2',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'work.happier','WorkHappier',1,0,0,NULL,0,0,NULL)
/
INSERT INTO OS_PROPERTYSET(ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE, BOOLEAN_VAL, DOUBLE_VAL, STRING_VAL, LONG_VAL, INT_VAL)
 VALUES
 (entity_id_pk_sq.nextval, 'external.survey.aes256.key', 'External Survey (Quantum) URL', 5, 0, 0, 'W9aUUrZGBzTCCgzwq8f7XdQcPYyxPuYS', 0, 0)
/
INSERT INTO OS_PROPERTYSET(ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE, BOOLEAN_VAL, DOUBLE_VAL, STRING_VAL, LONG_VAL, INT_VAL)
 VALUES
 (entity_id_pk_sq.nextval, 'external.survey.endpoint', 'External Survey (Quantum) key', 5, 0, 0, 'http://survey.dev.quantumworkplace.com/app/gw/biw', 0, 0)
/
INSERT INTO OS_PROPERTYSET(ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE, BOOLEAN_VAL, DOUBLE_VAL, STRING_VAL, LONG_VAL, INT_VAL)
 VALUES
 (entity_id_pk_sq.nextval, 'external.survey.init.vector', 'External Survey (Quantum) init', 5, 0, 0, '2NaRZrOqPv8H7JPn', 0, 0)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'show.sendall.estatement.process','Show "Send to all question" in e-statement process?',1,1,0,NULL,0,0,NULL)
/
INSERT INTO OS_PROPERTYSET(ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE, BOOLEAN_VAL, DOUBLE_VAL, STRING_VAL, LONG_VAL, INT_VAL,DATE_VAL)
 VALUES
 (entity_id_pk_sq.nextval, 'estatement.starting.user.id', 'Estatement Starting User ID', 3, 0, 0, NULL, 0, 0, NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'smtp.host.username','Smtp host username',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'smtp.host.password','Smtp host password',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'public.rec.wall.feed.page.count','Public Recognition Wall API Service Page Count',2,0,0,null,0,50,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'public.rec.wall.feed.enabled','Public Recognition Wall API Service Enabled',1,0,0,null,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'mailing.post.process.retry.attempts','Retries For Post Processes',2,0,0,'',0,10,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'sso.detailed.logging.on','SSO detailed logging on',1,1,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'elasticsearch.index.batch.size','Elasticsearch index batch size',2,0,0,NULL,0,500,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'sso.logout.redirect.url','SSO Logout Redirect URL (login, static, url)',5,0,0,'static',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'logout.timeout.like.sso','Logout Timeout Like SSO',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'noauth.timeout.redirect.url','Not Auth/Timeout-Redirect URL (login, static, url)',5,0,0,'login',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'noauth.timeout.internal.redirect.url','Not Auth/Timeout-Internal Redirect URL (login, static, url)',5,0,0,'login',0,0,NULL)
/
INSERT INTO OS_PROPERTYSET
(ENTITY_ID, ENTITY_NAME, ENTITY_KEY, KEY_TYPE, BOOLEAN_VAL, DOUBLE_VAL, LONG_VAL, INT_VAL)
VALUES (ENTITY_ID_PK_SQ.NEXTVAL, 'public.recog.days.check', 'Public recognition number of days to check', 2, 0, 0, 0, 30)
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
(entity_id_pk_sq.nextval,'autocomplete.es.proxy.url.dev','Elasticsearch proxy url-DEV',5,0,0,'proxyuser.biperf.com',0,0,NULL)
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
(entity_id_pk_sq.nextval,'password.history.count','User password history check count',2, 0, 0, null, 0, 5, NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'password.otp.expiry.days','User one time password expiration days',3, 0, 0, null, 86400000, 0, NULL)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.accessKey.dev', 'AWS Access Key for the S3 instance', 5, 0, 0, 'AKIAJ2GGY76J3IRXD2OA', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.secretKey.dev', 'AWS Secret Key for the S3 instance', 5, 0, 0, 'vVtnp7Gdr0wGjqb2sGKPgLzorDlh/LDYYBnyAmOV', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.accessKey.prod', 'AWS Access Key for the S3 instance', 5, 0, 0, 'CHANGE ME!', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.secretKey.prod', 'AWS Secret Key for the S3 instance', 5, 0, 0, 'CHANGE ME!', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.avatar.bucketname.dev', 'AWS Bucket Name for the S3 instance.', 5, 0, 0, 'biw-avatars-dev', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.avatar.bucketname.prod', 'AWS Bucket Name for the S3 instance.', 5, 0, 0, 'CHANGE ME!', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.s3.image.prefix', 'AWS S3 prefix key for file upload Mirrors cm3dam url in BIWS', 5, 0, 0, 'https://biw-avatars-prod.s3.amazonaws.com/bravo', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.global.fileprocessing.webdav.usernam', 'AWS username for the webdav instance/FTP', 5, 0, 0, 'BravoFTP', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.global.fileprocessing.webdav.passwor', 'AWS password for the webdav instance/FTP', 5, 0, 0, '1Z2x3c4v5b6n', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.fileload.transer.cgi', 'AWS File load transfer CGI location', 5, 0, 0, 'CHANGE ME!', 0, 0, null)
/
INSERT INTO os_propertyset 
VALUES (ENTITY_ID_PK_SQ.nextval, 'aws.fileload.transer.execution.cmd', 'AWS File load transfer execution cmd', 5, 0, 0, 'perl', 0, 0, null)
/
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
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.es.max.allowed.recognize','Elasticsearch- maximum pax allowed to recognize',2,0,0,NULL,0,300,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'meplus.enabled','MEPULS Installed',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'recognition-only.enabled','DayMaker',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'salesmaker.enabled','Is SalesMaker Setup?',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'autocomplete.delay.millis','The delay in millis for the autocomplete trigger',2,0,0,NULL,0,150,NULL)
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
(entity_id_pk_sq.nextval,'purl.comment.size.limit','PURL Max Comment length',2,0,0,NULL,0,500,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'admin.ip.restrictions','IPs with access to admin',5,0,0,'127.0.0.1,0:0:0:0:0:0:0:1,172.16.*,10.20.70.*,10.20.71.*,10.20.72.*,10.20.73.*,192.168.246.*,192.168.50.*,192.168.51.*,192.168.52.*,192.168.60.*,192.168.4.*,192.168.5.*,192.168.6.*,192.168.7.*,59.100.149.145,59.100.149.146,59.100.149.147,59.100.149.148,59.100.149.149,59.100.149.150,185.90.32.*,185.90.33.*,185.90.34.*,185.90.35.*,85.116.171.*,58.246.193.221,58.246.193.222,210.22.70.3,101.100.185.156,202.83.19.238,180.151.51.74',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES (ENTITY_ID_PK_SQ.nextval, 'bi.uaws.endpoint.url.preprod', 'Under Armour Micro Service REST endpoint', 5, 0, 0, 'http://uamicroservices-pprd.wzdcgsifm9.us-west-2.elasticbeanstalk.com/uamicroservices/rest', 0, 0, null)
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
(entity_id_pk_sq.nextval,'enable.opt.out.awards','Enable - Opt Out of receiving awards?',1,0,0,NULL,0,0,NULL)
/
INSERT INTO os_propertyset
 (ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
 VALUES
 (entity_id_pk_sq.nextval,'enable.opt.out.program','Enable - Opt Out of Program?',1,0,0,NULL,0,0,NULL)
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
VALUES (ENTITY_ID_PK_SQ.nextval, 'bi.uaws.endpoint.url.prod', 'Under Armour Micro Service REST endpoint', 5, 0, 0, 'http://uamicroservices-prod.wzdcgsifm9.us-west-2.elasticbeanstalk.com/uamicroservices/rest', 0, 0, null)
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
VALUES
(entity_id_pk_sq.nextval,'pdf.service.url.dev','URL to PDF generation service',5,0,0,'https://apipprd.biworldwide.com/v1/utility/pdf',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'pdf.service.url.qa','URL to PDF generation service',5,0,0,'https://apipprd.biworldwide.com/v1/utility/pdf',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'pdf.service.url.preprod','URL to PDF generation service',5,0,0,'https://apipprd.biworldwide.com/v1/utility/pdf',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'pdf.service.url.prod','URL to PDF generation service',5,0,0,'https://api.biworldwide.com/v1/utility/pdf',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES (ENTITY_ID_PK_SQ.nextval, 'bi.uaws.endpoint.url.dev', 'Under Armour Micro Service REST endpoint-DEV', 5, 0, 0, 'http://sample-env-2.j3zexxfnfb.ap-south-1.elasticbeanstalk.com/uamicroservices/rest', 0, 0, null)
/
