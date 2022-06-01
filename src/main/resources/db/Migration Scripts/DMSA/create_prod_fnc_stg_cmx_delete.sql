CREATE OR REPLACE FUNCTION fnc_stg_cmx_delete() RETURNS VOID AS $$
BEGIN
  DELETE from "DMSAPROD".stg_cmx_string;
  DELETE from "DMSAPROD".stg_cmx_key;
END
$$ LANGUAGE 'plpgsql';