CREATE OR REPLACE FUNCTION fnc_stg_cmx_delete() RETURNS VOID AS $$
BEGIN
  DELETE from "DMSADBNP".stg_cmx_string;
  DELETE from "DMSADBNP".stg_cmx_key;
END
$$ LANGUAGE 'plpgsql';