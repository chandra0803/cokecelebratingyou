CREATE OR REPLACE FUNCTION fnc_stg_sa_delete() RETURNS VOID AS $$
BEGIN
  DELETE from "DMSAPROD".stg_sa_celebrationspersons;    
  DELETE from "DMSAPROD".stg_sa_recipients;
  DELETE from "DMSAPROD".stg_sa_milestones;
  DELETE from "DMSAPROD".stg_sa_programs;
  DELETE from "DMSAPROD".stg_sa_persons;
END
$$ LANGUAGE 'plpgsql';