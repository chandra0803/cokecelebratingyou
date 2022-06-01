CREATE OR REPLACE FUNCTION fnc_format_user_name
( i_last_name     IN VARCHAR2,
  i_first_name    IN VARCHAR2,
  i_middle_name   IN VARCHAR2 DEFAULT NULL,
  i_suffix        IN VARCHAR2 DEFAULT NULL
) RETURN VARCHAR2 IS
/*******************************************************************************
 Purpose:  Places the user name into a standard format

 Person        Date        Comments
 -----------   ----------  -----------------------------------------------------
 J Flees       09/22/2011  Initial version.
*******************************************************************************/
  c_process_name         CONSTANT execution_log.process_name%type  := 'fnc_format_user_name';
  c_release_level        CONSTANT execution_log.release_level%type := '1';

BEGIN
   RETURN INITCAP(TRIM(i_last_name))
            || CASE WHEN (TRIM(i_last_name) IS NULL)   THEN NULL ELSE ', ' || INITCAP(TRIM(i_first_name)) END
            || CASE WHEN (TRIM(i_middle_name) IS NULL) THEN NULL ELSE ' '  || TRIM(i_middle_name) END
            || CASE WHEN (TRIM(i_suffix) IS NULL)      THEN NULL ELSE ' '  || TRIM(i_suffix) END
            ;

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR',
                'i_last_name >'  || i_last_name
         || '<, i_first_name >'  || i_first_name
         || '<, i_middle_name >' || i_middle_name
         || '<, i_suffix >'      || i_suffix
         || '<: ' || SQLCODE || ', ' || SQLERRM,
         NULL);

     RETURN NULL;

END fnc_format_user_name;
/
