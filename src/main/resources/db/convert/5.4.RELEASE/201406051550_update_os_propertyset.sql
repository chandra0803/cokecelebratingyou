update os_propertyset set STRING_VAL='172.16.*,10.20.70.*,10.20.71.*,10.20.72.*,10.20.73.*,198.246.149.241,198.246.149.242,198.246.149.243' where ENTITY_NAME='ips.to.allow.csv.regex'
/
update os_propertyset set STRING_VAL='<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> <html xmlns="http://www.w3.org/1999/xhtml"> <head>  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>  <title></title> <style type="text/css"> ${css} </style> </head> <body>  <table cellpadding="0" cellspacing="0" border="0" id="backgroundTable"> <tr> <td rowspan="3" width="5%">&nbsp;</td><td height="50">&nbsp;</td><td rowspan="3" width="5%">&nbsp;</td></tr><tr><td><table id="columnTable" width="100%" border="0" cellpadding="0" cellspacing="0"> <tr> <td rowspan="3" width="5%">&nbsp;</td> <td height="25">&nbsp;</td> <td rowspan="3" width="5%">&nbsp;</td> </tr> <tr> <td><table width="100%" border="0" cellpadding="0" cellspacing="0"> <tr> <td width="100%" height="110"><table width="100%" border="0" cellspacing="0" cellpadding="0"> <tr> <td id="logoContainer"><img id="logo" class="image_fix" src="${emailClientLogo}" alt="" title="" width="300" height="110" align="left" /></td> </tr> </table></td> </tr> <tr> <td id="topContainer" width="100%"> <!-- CONTENT STARTS HERE -->' where ENTITY_NAME='email.wrapper.header'
/
update os_propertyset set STRING_VAL='http://webservicesqa.awardslinq.com/ecommerce/rest' where ENTITY_NAME='reward.offerings.endpoint.dev'
/
update os_propertyset set STRING_VAL='http://webservicesqa.awardslinq.com/ecommerce/rest' where ENTITY_NAME='reward.offerings.endpoint.qa'
/
update os_propertyset set STRING_VAL='http://webservicespprd.awardslinq.com/ecommerce/rest' where ENTITY_NAME='reward.offerings.endpoint.preprod'
/
update os_propertyset set STRING_VAL='http://webservices.awardslinq.com/ecommerce/rest' where ENTITY_NAME='reward.offerings.endpoint.prod'
/
update os_propertyset set ENTITY_KEY='Use Strong Mail for Welcome Email',BOOLEAN_VAL= '0' where ENTITY_NAME='email.use.strongmail'
/
DELETE FROM os_propertyset where ENTITY_NAME='chatter.consumer.key'
/
DELETE FROM os_propertyset where ENTITY_NAME='chatter.consumer.secret'
/
DELETE FROM os_propertyset where ENTITY_NAME='force.https.dev'
/
DELETE FROM os_propertyset where ENTITY_NAME='force.https.qa'
/
DELETE FROM os_propertyset where ENTITY_NAME='force.https.preprod'
/
DELETE FROM os_propertyset where ENTITY_NAME='force.https.prod'
/