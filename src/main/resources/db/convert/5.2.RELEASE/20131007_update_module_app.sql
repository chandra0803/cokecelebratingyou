--disable Travel Great Deal
update module_app set APP_AUDIENCE_TYPE = 'disabled' where module_app_id = 3028
/

--disable Travel Envoy
update module_app set APP_AUDIENCE_TYPE = 'disabled' where module_app_id = 3026
/
