--liquibase formatted sql

--changeset esakkimu:1
--comment Insert currency code AMD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM CURRENCY WHERE CM_ASSET_NAME='currency.currencyName.10000131'
INSERT INTO currency  AMD
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000131','AMD','դր.','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'AMD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000131';

--changeset esakkimu:2
--comment Insert currency code KWD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM CURRENCY WHERE CM_ASSET_NAME='currency.currencyName.10000132'
INSERT INTO currency  KWD
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000132','KWD','ك.د','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'KWD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000132';

--changeset esakkimu:3
--comment Insert currency code OMR
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM CURRENCY WHERE CM_ASSET_NAME='currency.currencyName.10000133'
INSERT INTO currency  OMR
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000133','OMR','ر.ع.','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'OMR');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000133';

--changeset esakkimu:4
--comment Insert currency code JOD
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM CURRENCY WHERE CM_ASSET_NAME='currency.currencyName.10000134'
INSERT INTO currency  JOD
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000134','JOD','JOD','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'JOD');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000134';

--changeset esakkimu:5
--comment Insert currency code LBP
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM CURRENCY WHERE CM_ASSET_NAME='currency.currencyName.10000135'
INSERT INTO currency  LBP
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000135','LBP','ل.ل.‎','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'LBP');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000135';

--changeset esakkimu:6
--comment Insert currency code SAR
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM CURRENCY WHERE CM_ASSET_NAME='currency.currencyName.10000136'
INSERT INTO currency  SAR
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000136','SAR','ر.س','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'SAR');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000136';

--changeset esakkimu:7
--comment Insert currency code TND
--preconditions onFail:MARK_RAN onError:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM CURRENCY WHERE CM_ASSET_NAME='currency.currencyName.10000137'
INSERT INTO currency  TND
(currency_id, cm_asset_name, currency_code, currency_symbol, status, created_by, date_created, modified_by, date_modified, version)
SELECT currency_pk_sq.nextval,'currency.currencyName.10000137','TND','ر.س','active',0,sysdate,null,null,0 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM currency WHERE currency_code = 'TND');
--rollback DELETE FROM currency WHERE cm_asset_name='currency.currencyName.10000137';