--liquibase formatted sql

--changeset ramesh:1
--comment removed the north country from countries_suppliers table it is not used
DELETE from COUNTRY_SUPPLIERS where COUNTRY_ID in ( SELECT  COUNTRY.COUNTRY_ID FROM COUNTRY WHERE COUNTRY_CODE = 'kp');
--rollback not required

--changeset ramesh:2
--comment Removed the North Korea country from country list because it is not used
DELETE FROM COUNTRY WHERE COUNTRY_CODE = 'kp';
--rollback not required

--changeset ramesh:3
--comment Inserted Serbia country entry in country table
INSERT INTO country(country_id,country_code,cm_asset_code,name_cm_key,awardbanq_country_abbrev,address_method,status,date_status,allow_sms,display_travel_award,created_by,date_created,version,phone_country_code,timezone_id,currency_code,require_postalcode)
     VALUES (country_pk_sq.NEXTVAL,'rs','country_data.country.rs','COUNTRY_NAME','SRB','international','inactive',sysdate,0,0,0,sysdate,1,'2','ACT','RSD',0)
--rollback delete from country where country_code='rs';
