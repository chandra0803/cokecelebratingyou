---
--- update the country data for bonfire
---
update country set campaign_nbr = '090020', campaign_password = '9002test', program_id = '09002', program_password = 'tsg09002',  status = 'active', SHOPPING_BANNER_URL='images\banners\Shop2.gif' where country_code = 'us'
/

update country set campaign_nbr = '090020', campaign_password = '9002test', program_id = '09002', program_password = 'tsg09002',  status = 'active' where country_code = 'ca'
/

update country set campaign_nbr = '777860', campaign_password = '77786pat', program_id = '77786', program_password = '77786pre',  status = 'active' where country_code = 'gb'
/

update country set campaign_nbr = '090110', campaign_password = 'tingtang', program_id = '09011', program_password = 'tsg09011', status = 'active' where country_code = 'fr'
/

update country set campaign_nbr = '090110', campaign_password = 'tingtang', program_id = '09011', program_password = 'tsg09011', status = 'active' where country_code = 'it'
/

update country set campaign_nbr = '090110', campaign_password = 'tingtang', program_id = '09011', program_password = 'tsg09011', status = 'active' where country_code = 'au'
/

update country set campaign_nbr = '090140', campaign_password = 'partners', program_id = '09014', program_password = 'tsg09014',  status = 'active' where country_code = 'in'
/

update country set campaign_nbr = NULL where status = 'inactive'
/

update country set require_postalcode = 0 where country_code = 'ie'
/

update country set require_postalcode = 0 where country_code = 'pa'
/