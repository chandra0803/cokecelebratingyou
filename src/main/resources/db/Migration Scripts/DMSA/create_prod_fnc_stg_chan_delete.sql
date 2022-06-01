CREATE OR REPLACE FUNCTION fnc_stg_chan_delete() RETURNS VOID AS $$
BEGIN
  DELETE from "DMSAPROD".stg_chan_channels;      
  DELETE from "DMSAPROD".stg_chan_messages;
END
$$ LANGUAGE 'plpgsql';