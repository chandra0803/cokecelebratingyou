CREATE OR REPLACE FUNCTION fnc_stg_gc_delete() RETURNS VOID AS $$
BEGIN
  DELETE from "DMSADBNP".stg_gc_countries;      
  DELETE from "DMSADBNP".stg_gc_awards;
  DELETE from "DMSADBNP".stg_gc_levels;
  DELETE from "DMSADBNP".stg_gc_giftcodes;
END
$$ LANGUAGE 'plpgsql';