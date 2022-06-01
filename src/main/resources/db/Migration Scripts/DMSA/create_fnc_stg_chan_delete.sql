CREATE OR REPLACE FUNCTION fnc_stg_chan_delete() RETURNS VOID AS $$
BEGIN
  DELETE from "DMSADBNP".stg_chan_channels;      
  DELETE from "DMSADBNP".stg_chan_messages;
END
$$ LANGUAGE 'plpgsql';