--liquibase formatted sql

--changeset ramesh:3
--comment inserted serbia country code and supplier name in country_suppliers table
INSERT INTO country_suppliers (country_id,supplier_id,is_primary,created_by,date_created) VALUES ((SELECT country_id FROM country WHERE country_code = 'rs'),(SELECT supplier_id FROM supplier WHERE supplier_name = 'BII'),1,0,SYSDATE);
--rollback delete from COUNTRY_SUPPLIERS where COUNTRY_ID in ( SELECT  COUNTRY.COUNTRY_ID FROM COUNTRY WHERE COUNTRY_CODE = 'rs');