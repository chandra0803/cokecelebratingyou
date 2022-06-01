CREATE OR REPLACE PROCEDURE prc_get_directory_file_name 
  (p_in_string          IN  VARCHAR2,
   p_out_directory_path OUT VARCHAR2,
   p_out_file_name      OUT VARCHAR2)
  IS

/*******************************************************************************
  Process   :  PRC_GET_DIRECTORY_FILE_NAME
  Purpose   :  To get directory path and file name from the input parameter
  
  Person         Date           Comments
  -----------   --------    ----------------------------------------------------
  Arun S        01/07/2009  Initial creation  
      
*******************************************************************************/

  v_string      VARCHAR2(4000);
  v_delim_loc   NUMBER(10) := 0;
  v_file_name   VARCHAR2(4000);
  v_directory   VARCHAR2(4000);
  
BEGIN
  
  v_string    := p_in_string;
  
  v_delim_loc := INSTR(v_string, '/', -1);
  
  v_directory := SUBSTR(v_string, 1, v_delim_loc - 1);
  
  v_file_name := SUBSTR(v_string, v_delim_loc + 1);
  
  p_out_directory_path := v_directory; 
  p_out_file_name      := v_file_name;

EXCEPTION
  WHEN OTHERS THEN
    NULL;  
END;
/