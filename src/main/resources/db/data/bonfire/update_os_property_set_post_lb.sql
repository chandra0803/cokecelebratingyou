---
--- SYSTEM VARIABLES 
---
UPDATE os_propertyset SET string_val = 'ap-south-1' where entity_name = 'autocomplete.es.aws.region'
/
UPDATE os_propertyset SET string_val = 'https://search-gts-inspirecloud-qa-7ttvenrjxnps6nip7bywy5cxuq.ap-south-1.es.amazonaws.com/' 
where entity_name like 'autocomplete.es.url.%'
/
UPDATE os_propertyset SET string_val = 'AKIAIGEEZTTL2TE6C2RA' where entity_name like 'elasticsearch.creds.username.%'
/
UPDATE os_propertyset SET string_val = '6uqPuCHLgI9RYJ2icYS5OXwWE+CJKahgnYi1KUoH' where entity_name like 'elasticsearch.creds.password.%'
/