UPDATE cms_asset
   SET pax_or_admin = 'P',
       date_modified = SYSDATE
 WHERE id IN (SELECT ca.id
                FROM cms_asset ca,
                     cms_section cs,
                     cms_application capp
               WHERE ca.section_id = cs.id
                 AND cs.application_id = capp.id
                 AND capp.code = 'beacon'
                 AND ca.code IN 
(
'message_data.message.128004',
'message_data.message.128005',
'message_data.message.128002',
'message_data.message.128001',
'message_data.message.110821')
)
/
