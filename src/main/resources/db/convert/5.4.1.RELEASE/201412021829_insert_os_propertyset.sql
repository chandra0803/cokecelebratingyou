update os_propertyset
set string_val = '<!-- CONTENT ENDS HERE --> </td> </tr> <tr> <td id="bottomContainer" align="center" width="100%"><img class="image_fix" src="${emailPhoto}" alt="" title="" width="300" height="255" /></td> </tr> </table></td> </tr> <tr> <td height="43">&nbsp;</td> </tr> </table></td> </tr><tr><td height="50">&nbsp;</td></tr></table><!-- End of wrapper table --></body></html>'
where entity_name like 'email.wrapper.footer'
/
DELETE FROM OS_PROPERTYSET WHERE ENTITY_NAME IN 
('wellness.url.dev','wellness.url.qa','wellness.url.preprod','wellness.url.prod',
 'import.pax.file.name','unix.movefile.scriptpath','unix.movefile.scriptname')
/
INSERT INTO os_propertyset
(ENTITY_ID,ENTITY_NAME,ENTITY_KEY,KEY_TYPE,BOOLEAN_VAL,DOUBLE_VAL,STRING_VAL,LONG_VAL,INT_VAL,DATE_VAL)
VALUES
(entity_id_pk_sq.nextval,'show.participant.birth.hire.date','Show Participant Birth and Hire Date?',1,1,0,NULL,0,0,NULL)
/