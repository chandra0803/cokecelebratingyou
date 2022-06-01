CREATE OR REPLACE FUNCTION fnc_stg_sa_delete() RETURNS VOID AS $$
BEGIN
  DELETE from "DMSADBNP".stg_sa_celebrationspersons;    
  DELETE from "DMSADBNP".stg_sa_recipients;
  DELETE from "DMSADBNP".stg_sa_milestones;
  DELETE from "DMSADBNP".stg_sa_programs;
  DELETE from "DMSADBNP".stg_sa_persons;
END
$$ LANGUAGE 'plpgsql';