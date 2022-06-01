create or replace function fnc_get_user_name    ( p_in_user_id   IN NUMBER)  RETURN VARCHAR2 IS
/*******************************************************************************
   Purpose:  Get the fullname of the user that is passed in.

   Person          Date        Comments
-----------     ----------  -----------------------------------------------------
   Raju N       01/23/2007   Cloned from code originally created by D Murray 
	                           on 5/10/2006 and made it a standalone function 
   Chidamba     01/22/2014   Bugfix# 50860 - fixed to return null if the input parm is null.
*******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name         execution_log.process_name%type  := 'fnc_get_user_name';
  v_release_level        execution_log.release_level%type := '1';

  v_fullname             VARCHAR2(120);

BEGIN
  IF p_in_user_id IS NOT NULL THEN
    SELECT initcap(last_name)||', '||initcap(first_name)
           || decode(ltrim(rtrim(middle_name)),null,null,(' '||ltrim(rtrim(middle_name))))
           || decode(ltrim(rtrim(suffix)),null,null,(' '||ltrim(rtrim(suffix))))
     INTO v_fullname
     FROM application_user
     WHERE user_id = p_in_user_id;
  ELSE 
   v_fullname := NULL;
  END IF;

   RETURN v_fullname;
EXCEPTION
   WHEN OTHERS THEN
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                            'User_id: '||p_in_user_id||' --> '||SQLERRM,null);

     v_fullname := NULL;
     RETURN v_fullname;

END; -- FNC_GET_USER_NAME