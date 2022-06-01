create or replace function fnc_get_final_appr_name    ( p_in_user_id   IN NUMBER)  RETURN VARCHAR2 IS
/*******************************************************************************
   Purpose:  Get the fullname of the user that is passed in.

   Person          Date        Comments
-----------     ----------  -----------------------------------------------------
   John Ernste	04/16/2007  Gets the final approver name with user id passed in
   							New function because extracts dont handle commas
*******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name         execution_log.process_name%type  := 'fnc_get_final_appr_name';
  v_release_level        execution_log.release_level%type := '1';

  v_fullname             VARCHAR2(120);

BEGIN
    SELECT initcap(first_name)
           || decode(ltrim(rtrim(middle_name)),null,null,' '||(ltrim(rtrim(middle_name))))
           ||' '|| initcap(last_name)
           || decode(ltrim(rtrim(suffix)),null,null,(' '||ltrim(rtrim(suffix))))
     INTO v_fullname
     FROM application_user
     WHERE user_id = p_in_user_id;

   RETURN v_fullname;
EXCEPTION
   WHEN OTHERS THEN
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                            'User_id: '||p_in_user_id||' --> '||SQLERRM,null);

     v_fullname := NULL;
     RETURN v_fullname;

END; -- fnc_get_final_appr_name
/