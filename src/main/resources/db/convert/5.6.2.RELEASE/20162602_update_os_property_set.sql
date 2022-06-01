delete from os_propertyset where ENTITY_NAME in ('diy.banners.available', 'diy.news.stories.available', 'diy.resource.center.available','diy.tips.available')
/
UPDATE os_propertyset
SET STRING_VAL   ='<!-- CONTENT ENDS HERE --> </td> </tr></table></td> </tr> <tr> <td height="43">&nbsp;</td> </tr> </table></td> </tr><tr><td height="50">&nbsp;</td></tr></table><!-- End of wrapper table --></body></html>'
WHERE ENTITY_NAME='email.wrapper.footer'
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'enrollment.characteristic1','Enrollment Characteristic 1',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'enrollment.characteristic2','Enrollment Characteristic 2',5,0,0,'changeme',0,0,NULL)
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'work.happier','Work Happier',1,0,0,NULL,0,0,NULL)
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
