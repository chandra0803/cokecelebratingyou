DECLARE 
  p_out_returncode number;
BEGIN 
  p_out_returncode := null;
  prc_conv_script_pax_term_date(p_out_returncode);
  COMMIT;
END;
