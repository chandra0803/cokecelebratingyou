CREATE OR REPLACE FUNCTION fnc_cmx_ins() RETURNS VOID AS $$
BEGIN
  INSERT INTO "Key" 
   select id,
          name,
         (select id from "Project" where name = 'service-awards') as projectId,
          null,
         "createdAt",
         "updatedAt",
         "doNotTranslate",
         metadata
    from "DMSAPROD".stg_cmx_Key;
  INSERT INTO "String" select * from "DMSAPROD".stg_cmx_string;
END
$$ LANGUAGE 'plpgsql';