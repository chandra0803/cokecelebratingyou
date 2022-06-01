CREATE OR REPLACE FUNCTION fnc_get_dir_name
( i_directory_path   IN all_directories.directory_path%TYPE
) RETURN VARCHAR2
IS
--------------------------------------------------------------------------------
-- Purpose: Retrieves the database directory name based upon the input directory path.
--
-- MODIFICATION HISTORY
--    Person      Date        Comments
--    ---------   ------      --------------------------------------------------
--    J.Flees     06/22/2011  Initial version.
--------------------------------------------------------------------------------
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('fnc_get_dir_name');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;

   v_msg                execution_log.text_line%TYPE;
   v_directory_name     all_directories.directory_name%TYPE;

   CURSOR cur_dir_name
   ( cv_dir_path  VARCHAR2
   ) IS
   SELECT ad.directory_name
     FROM all_directories ad
     WHERE ad.directory_path = cv_dir_path
      ;
BEGIN
   v_msg := 'OPEN cur_dir_name: i_directory_path >' || i_directory_path || '<';
   OPEN cur_dir_name(i_directory_path);
   v_msg := 'FETCH cur_dir_name: i_directory_path >' || i_directory_path || '<';
   FETCH cur_dir_name INTO v_directory_name;
   CLOSE cur_dir_name;

   RETURN v_directory_name;

EXCEPTION   
  WHEN OTHERS THEN
    RAISE_APPLICATION_ERROR (-20015,'FAILURE: (' || v_msg || ') ' || SQLCODE || ' - ' || SQLERRM);

END fnc_get_dir_name;
/