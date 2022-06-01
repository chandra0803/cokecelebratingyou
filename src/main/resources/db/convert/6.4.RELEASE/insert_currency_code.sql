--liquibase formatted sql

--changeset ramesh:1
--comment Insert currency code MOP
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000126','MOP','MOP$','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'MOP');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000126';

--changeset ramesh:2
--comment Insert currency code TZS
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000127','TZS','TSh','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'TZS');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000127';

--changeset ramesh:3
--comment Insert currency code THB
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000128','THB','à¸¿','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'THB');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000128';

--changeset ramesh:4
--comment Insert currency code AED
INSERT INTO currency  
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000129','AED','Ø¯.Ø¥','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'AED');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000129';

--changeset ramesh:5
--comment Insert currency code AED
INSERT INTO currency  VND
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000130','VND','â‚«','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'VND');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000130';