CREATE OR REPLACE FUNCTION fnc_gc_ins() RETURNS VOID AS $$
BEGIN
    INSERT INTO countries SELECT *FROM "DMSAPROD".stg_gc_countries; 
    INSERT INTO awards SELECT *FROM "DMSAPROD".stg_gc_awards;
    INSERT INTO levels SELECT *FROM "DMSAPROD".stg_gc_levels;
    INSERT INTO giftcodes SELECT *FROM "DMSAPROD".stg_gc_giftcodes;
END
$$ LANGUAGE 'plpgsql';