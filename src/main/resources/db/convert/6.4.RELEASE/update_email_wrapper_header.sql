--liquibase formatted sql

--changeset sivanand:1
--comment Update email wrapper_header
UPDATE OS_PROPERTYSET
   SET STRING_VAL = '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><head><meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> <meta name="viewport" content="width=device-width, initial-scale=1.0"/> <title></title> <style type="text/css"> ${css} </style> </head> <body>  <!'||CHR(45)||CHR(45)||'[if mso]>  <style type="text/css">  body, table, td {font-family: Arial, Helvetica, sans-serif !important;}  </style>  <![endif]'||CHR(45)||CHR(45)||'><table cellpadding="0" cellspacing="0" border="0" id="backgroundTable"> <tbody> <tr> <td rowspan="3" width="5%" class="shim-side">'||CHR(38)||'nbsp'||CHR(59)||'</td> <td height="50" class="shim-top">'||CHR(38)||'nbsp'||CHR(59)||'</td> <td rowspan="3" width="5%" class="shim-side">'||CHR(38)||'nbsp'||CHR(59)||'</td> </tr> <tr> <td> <div class="webkit"> <!'||CHR(45)||CHR(45)||'[if (gte mso 9)|(IE)]> <table border="0" cellspacing="0" cellpadding="0" width="720" align="center"> <tr> <td> <![endif]'||CHR(45)||CHR(45)||'> <table id="columnTable" width="100%" border="0" cellpadding="0" cellspacing="0"> <tbody> <tr> <td rowspan="3" width="5.5%">'||CHR(38)||'nbsp'||CHR(59)||'</td> <td height="25">'||CHR(38)||'nbsp'||CHR(59)||'</td> <td rowspan="3" width="5.5%">'||CHR(38)||'nbsp'||CHR(59)||'</td> </tr> <tr> <td> <table width="100%" border="0" cellpadding="0" cellspacing="0"> <tbody> <tr> <td width="100%"height="110"><table width="100%" border="0" cellspacing="0" cellpadding="0"> <tr> <td id="logoContainer"> <img id="logo" class="image_fix" src="${emailClientLogo}" alt="" title="" width="300" height="110" align="left" /> </td> </tr> </table> </td> </tr> <tr> <td id="topContainer" width="100%"><!'||CHR(45)||CHR(45)||' CONTENT STARTS HERE '||CHR(45)||CHR(45)||'>'
WHERE ENTITY_NAME = 'email.wrapper.header';
--rollback not required

--changeset sivanand:2
--comment Update email footer
UPDATE OS_PROPERTYSET
   SET STRING_VAL = '<!'||CHR(45)||CHR(45)||' CONTENT ENDS HERE '||CHR(45)||CHR(45)||'> </td> </tr></table></td> </tr> <tr> <td height="10"></td> </tr> </table></td> </tr><tr><td height="50" class="shim-top"></td></tr></table><!'||CHR(45)||CHR(45)||' End of wrapper table '||CHR(45)||CHR(45)||'></body></html>'
WHERE ENTITY_NAME = 'email.wrapper.footer';
--rollback not required

--changeset sivanand:3
--comment Update email wrapper_header for center alignment and background colour
UPDATE OS_PROPERTYSET
   SET STRING_VAL = '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><head><meta http-equiv="Content-Type" content="text/html; charset=utf-8" /><meta name="viewport" content="width=device-width, initial-scale=1.0"/><title></title><style type="text/css"> ${css} </style></head><body><!'||CHR(45)||CHR(45)||'[if mso]>  <style type="text/css">  body, table, td {font-family: Arial, Helvetica, sans-serif !important;}  </style><![endif]'||CHR(45)||CHR(45)||'><table cellpadding="0" cellspacing="0" border="0" id="backgroundTable" width="100%"><tbody><tr><td rowspan="3" width="5%" class="shim-side">'||CHR(38)||'nbsp'||CHR(59)||'</td><td height="50" class="shim-top">'||CHR(38)||'nbsp'||CHR(59)||'</td><td rowspan="3" width="5%" class="shim-side">'||CHR(38)||'nbsp'||CHR(59)||'</td></tr><tr><td><div class="webkit"><!'||CHR(45)||CHR(45)||'[if (gte mso 9)|(IE)]> <table border="0" cellspacing="0" cellpadding="0" width="720" align="center"><tr><td><![endif]'||CHR(45)||CHR(45)||'> <table id="columnTable" width="720px" border="0" cellpadding="0" cellspacing="0" style="width:720px;" align="center"><tbody><tr><td rowspan="3" width="5.5%">'||CHR(38)||'nbsp'||CHR(59)||'</td><td height="25">'||CHR(38)||'nbsp'||CHR(59)||'</td><td rowspan="3" width="5.5%">'||CHR(38)||'nbsp'||CHR(59)||'</td></tr><tr><td><table width="100%" border="0" cellpadding="0" cellspacing="0"><tbody><tr><td width="100%"height="110"><table width="100%" border="0" cellspacing="0" cellpadding="0"><tr><td id="logoContainer"> <img id="logo" class="image_fix" src="${emailClientLogo}" alt="" title="" width="300" height="110" align="left" /> </td></tr></table></td></tr><tr><td id="topContainer" width="100%"><!'||CHR(45)||CHR(45)||' CONTENT STARTS HERE '||CHR(45)||CHR(45)||'>'
WHERE ENTITY_NAME = 'email.wrapper.header';
--rollback not required

--changeset sivanand:4
--comment Update email footer for height change
UPDATE OS_PROPERTYSET
   SET STRING_VAL = '<!'||CHR(45)||CHR(45)||' CONTENT ENDS HERE '||CHR(45)||CHR(45)||'></td></tr></tbody></table></td></tr><tr><td height="10"></td></tr></tbody></table><!'||CHR(45)||CHR(45)||'[if (gte mso 9)|(IE)]> </td> </tr> </table> <![endif]'||CHR(45)||CHR(45)||'></div></td></tr><tr><td height="50" style="height:50px;">'||CHR(38)||'nbsp'||CHR(59)||'</td></tr></tbody></table><!'||CHR(45)||CHR(45)||' End of wrapper table '||CHR(45)||CHR(45)||'></body></html>'
WHERE ENTITY_NAME = 'email.wrapper.footer';
--rollback not required

--changeset sivanand:5
--comment Update email wrapper_header for mobile change
UPDATE OS_PROPERTYSET
   SET STRING_VAL = '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> <html xmlns="http://www.w3.org/1999/xhtml"> <head> <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> <meta name="viewport" content="width=device-width, initial-scale=1.0"/> <title></title> <style type="text/css"> ${css} </style> </head> <body> <!'||CHR(45)||CHR(45)||'[if mso]>  <style type="text/css">  body, table, td {font-family: Arial, Helvetica, sans-serif !important;}  </style> <![endif]'||CHR(45)||CHR(45)||'> <table cellpadding="0" cellspacing="0" border="0" id="backgroundTable"> <tbody> <tr> <td rowspan="3" width="5%" class="shim-side">&nbsp;</td> <td height="50" class="shim-top">&nbsp;</td> <td rowspan="3" width="5%" class="shim-side">&nbsp;</td> </tr> <tr> <td> <div class="webkit"> <!'||CHR(45)||CHR(45)||'[if (gte mso 9)|(IE)]> <table border="0" cellspacing="0" cellpadding="0" width="720" align="center"> <tr> <td> <![endif]'||CHR(45)||CHR(45)||'> <table id="columnTable" width="100%" border="0" cellpadding="0" cellspacing="0" align="center"> <tbody> <tr> <td rowspan="3" width="5.5%">&nbsp;</td> <td height="25">&nbsp;</td> <td rowspan="3" width="5.5%">&nbsp;</td> </tr> <tr> <td> <table width="100%" border="0" cellpadding="0" cellspacing="0"> <tbody> <tr> <td width="100%"height="110"> <table width="100%" border="0" cellspacing="0" cellpadding="0"> <tr> <td id="logoContainer"> <img id="logo" class="image_fix" src="${emailClientLogo}" alt="" title="" width="300" height="110" align="left" /> </td> </tr> </table> </td> </tr> <tr> <td id="topContainer" width="100%"> <!'||CHR(45)||CHR(45)||' CONTENT STARTS HERE '||CHR(45)||CHR(45)||'>'
WHERE ENTITY_NAME = 'email.wrapper.header';
--rollback not required

--changeset sivanand:6
--comment Update email footer for mobile change
UPDATE OS_PROPERTYSET
   SET STRING_VAL = '<!'||CHR(45)||CHR(45)||' CONTENT ENDS HERE '||CHR(45)||CHR(45)||'></td></tr></tbody></table></td></tr><tr><td height="10"></td></tr></tbody></table><!'||CHR(45)||CHR(45)||'[if (gte mso 9)|(IE)]> </td> </tr> </table> <![endif]'||CHR(45)||CHR(45)||'></div></td></tr><tr><td height="50"></td></tr></tbody></table><!'||CHR(45)||CHR(45)||' End of wrapper table '||CHR(45)||CHR(45)||'></body></html>'
WHERE ENTITY_NAME = 'email.wrapper.footer';
--rollback not required

