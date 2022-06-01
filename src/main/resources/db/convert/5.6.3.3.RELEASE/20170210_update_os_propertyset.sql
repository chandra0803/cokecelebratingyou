update os_propertyset
set string_val = 'https://workpre.bius.bi.corp/pre'
where entity_name = 'global.fileprocessing.webdav.preprod'
/
update os_propertyset
set string_val = 'https://work.bius.bi.corp/prd'
where entity_name = 'global.fileprocessing.webdav.prod'
/
update os_propertyset
set string_val = 'https://workqa.bius.bi.corp/qa'
where entity_name = 'global.fileprocessing.webdav.qa'
/
update os_propertyset
set string_val = 'https://workqa.bius.bi.corp:8443/qa'
where entity_name = 'global.fileprocessing.webdav.dev'
/