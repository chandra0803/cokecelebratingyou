CREATE OR REPLACE FUNCTION fnc_stg_gc_delete() RETURNS VOID AS $$
BEGIN
  DELETE from "DMSAPROD".stg_gc_countries;      
  DELETE from "DMSAPROD".stg_gc_awards;
  DELETE from "DMSAPROD".stg_gc_levels;
  DELETE from "DMSAPROD".stg_gc_giftcodes;
END
$$ LANGUAGE 'plpgsql';