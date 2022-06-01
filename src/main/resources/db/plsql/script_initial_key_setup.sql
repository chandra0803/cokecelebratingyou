DECLARE
   v_file_name    VARCHAR2(30 char);
BEGIN

  prc_generate_seed_data('N', 'N'); -- Second parameter is set as 'N' because the OS_PROPERTYSET values are not set up, for subsequent runs this parameter should be NULL

END;
/