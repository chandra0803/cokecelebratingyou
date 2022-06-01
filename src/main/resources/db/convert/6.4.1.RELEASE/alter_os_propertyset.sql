--liquibase formatted sql

--changeset palaniss:1
--comment Updating the Elastic search proxy value in dev
UPDATE os_propertyset SET STRING_VAL = 'corpuser.corp.biworldwide.com' WHERE entity_name = 'autocomplete.es.proxy.url.dev';
--rollback not required

--changeset palaniss:2
--comment Updating the Elastic search proxy value in qa
UPDATE os_propertyset SET STRING_VAL = 'corpapp.corp.biworldwide.com' WHERE entity_name = 'autocomplete.es.proxy.url.qa';
--rollback not required

--changeset palaniss:3
--comment Updating the Elastic search proxy value in pprd
UPDATE os_propertyset SET STRING_VAL = 'corpapp.corp.biworldwide.com' WHERE entity_name = 'autocomplete.es.proxy.url.preprod';
--rollback not required

--changeset palaniss:4
--comment Updating the Elastic search proxy value in prd
UPDATE os_propertyset SET STRING_VAL = 'corpapp.corp.biworldwide.com' WHERE entity_name = 'autocomplete.es.proxy.url.prod';
--rollback not required