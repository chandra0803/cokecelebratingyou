UPDATE os_propertyset 
SET string_val = 'proxyuser.biperf.com'
WHERE entity_name = 'autocomplete.es.proxy.url.dev'
/
DELETE from os_propertyset 
WHERE entity_name = 'system.debug'
/
UPDATE os_propertyset 
SET string_val = 'search,followed,team,global,recommended,country,department'
WHERE entity_name ='public.recog.active.tabs'
/
UPDATE os_propertyset 
SET string_val = 'search,followed,team,global,recommended,country,department'
WHERE entity_name = 'purl.celeb.active.tabs'
/