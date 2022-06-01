CREATE OR REPLACE VIEW vue_characteristic_name (
   characteristic_id,
   characteristic_type,
   domain_id,
   description,
   pl_name,
   cm_name )
AS
select c.characteristic_id,c.characteristic_type,c.domain_id,c.description,
       c.pl_name, substr(upper(FNC_CMS_ASSET_CODE_VALUE(c.cm_asset_code)),1,100) cm_name
  from characteristic c
/
