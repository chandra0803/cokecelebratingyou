-- FILE IMPORT VARIABLES
--insert into os_propertyset
--values (4000,'import.pax.file.location','datacenter_location',5,0,0,
--        '/customs/dev/process/ms/aaqi/incoming', 0,0,null)
--/
--insert into os_propertyset
--values (4001,'import.pax.file.name','pax_file_name',5,0,0,
--        'Pax_PreImport.csv', 0,0,null)
--/
--insert into os_propertyset
--values (4002,'import.file.utl_path','db_utl_path',5,0,0,
--        '/work/wip/qa/aawm', 0,0,null)
--/
--insert into os_propertyset
--values (4003,'unix.movefile.scriptpath','unix_movefile_script',5,0,0,
--        '/apps/local/scripts/', 0,0,null)
--/
--insert into os_propertyset
--values (4004,'unix.movefile.scriptname','unix_movefile_script',5,0,0,
--         'dev_ms_movefile.sh', 0,0,null)
--/
UPDATE os_propertyset
   SET string_val = '/customs/dev/process/ms/aaqi/incoming'
 WHERE entity_name = 'import.file.location'
   AND entity_key ='datacenter_location'
/
UPDATE os_propertyset
   SET string_val = 'aaqi'
 WHERE entity_name = 'import.client.prefix'
   AND entity_key ='client_prefix'
/
UPDATE os_propertyset
   SET STRING_VAL = 'awardbanq'
 WHERE ENTITY_NAME = 'awardbanq.mode'
/
update os_propertyset 
   set string_val = 'bicore_admin@biworldwide.com' 
 where entity_name = 'email.address.system'
/
update os_propertyset 
   set string_val = 'bicore_admin@biworldwide.com' 
 where entity_name = 'contact.us.email'
/
update os_propertyset 
   set string_val = 'bicore_admin@biworldwide.com' 
 where entity_name = 'email.address.system.incentive'
/
update os_propertyset 
   set string_val = 'bicore_admin@biworldwide.com' 
 where entity_name = 'email.notification.senderaddr'
/
commit
/