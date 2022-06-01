CREATE OR REPLACE PROCEDURE prc_inactivate_biw_users
  (
  p_in_user_id     IN NUMBER,
  p_out_count      OUT NUMBER,
  p_out_returncode OUT NUMBER) 
IS
PRAGMA AUTONOMOUS_TRANSACTION;
/*******************************************************************************
  -- Purpose: Inactivate BIW users who have not logged in in the past X days
  
  --
  -- Person         Date                Comments
  -- -----------   --------        ------------------------------------------------
  -- Gorantla      05/07/2018       Initial Version
  -- Gorantla      06/25/2018       GitLab#1121/Bug 76857 - The new process to inactivate BIW Users does not 
                                    inactivate users who never have logged into the site.
  *******************************************************************************/
    
  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%TYPE  := 'PRC_INACTIVATE_BIW_USERS' ;
  v_release_level         execution_log.release_level%TYPE := '1';
  
  --MISCELLANIOUS
  c_delimiter             CONSTANT  VARCHAR2(10):='|';
  v_stage                 VARCHAR2(500);

  v_text                  VARCHAR2(500);

  v_user_id               application_user.user_id%TYPE;
  v_error_message         VARCHAR2(1000);
  v_days_expire           os_propertyset.int_val%TYPE;

  -- COUNTS
  v_recs_insert_cnt       INTEGER := 0;
  v_recs_update_cnt       INTEGER := 0;
  v_error_cnt             INTEGER := 0;
  v_user_cnt              INTEGER := 0;
  v_update_cnt            INTEGER := 0;    
  v_user_last_login       NUMBER;
  v_upd_cnt               INTEGER := 0;  -- 06/25/2018

  
-- Cursor of the bi users
CURSOR cur_user IS
SELECT au.user_id
  FROM application_user au
 WHERE au.is_active      = 1 
   AND au.user_type = 'bi';
   
                         
BEGIN
  v_stage := 'Write start to execution_log table';
  p_out_returncode := 0;
  prc_execution_log_entry(v_process_name,v_release_level,'INFO',
                          'Procedure Started ',NULL);

   v_stage := 'Get days to expire';
    BEGIN
      SELECT int_val
      INTO   v_days_expire
      FROM   os_propertyset
      WHERE  entity_name = LOWER('biw.user.days.to.expire'); -- required, let error fall
    EXCEPTION
      WHEN OTHERS THEN
        v_days_expire := NULL;
    END;

  v_stage := 'user loop';
  FOR rec_user IN cur_user LOOP
    v_user_last_login := 0;
    v_user_cnt := v_user_cnt + 1;
    
    v_stage := 'Get last login_activity';
    BEGIN 
    SELECT TRUNC(SYSDATE) - TRUNC(MAX(login_date_time))
      INTO v_user_last_login
      FROM login_activity
     WHERE user_id =  rec_user.user_id;
    EXCEPTION WHEN OTHERS THEN 
      v_user_last_login := 0;
    END; 
    
    v_stage := 'Update is active status';
    IF v_user_last_login > v_days_expire AND v_days_expire IS NOT NULL THEN
    BEGIN
      --dbms_output.put_line(rec_user.user_id||' '||v_user_last_login|| ' '||v_days_expire);   
        UPDATE application_user
           SET is_active = 0,
               modified_by = p_in_user_id,
               date_modified = sysdate,
               version = version + 1
         WHERE user_id =  rec_user.user_id ;
         
         v_update_cnt := v_update_cnt + 1;    
    END;
    END IF;
  END LOOP;
  
  -- 06/25/2018 start
  v_stage := 'Inactivate pax who did not logged-in within '||v_days_expire;
  IF v_days_expire IS NOT NULL THEN
    UPDATE application_user au
       SET is_active = 0,
           modified_by = 5662,
           date_modified = sysdate,
           version = version + 1
     WHERE au.is_active      = 1 
       AND au.user_type = 'bi'
       AND trunc(au.date_created) < trunc(sysdate) - v_days_expire
       AND NOT EXISTS (SELECT * 
                         FROM login_activity la 
                        WHERE la.user_id = au.user_id);
                        
    v_upd_cnt := sql%rowcount;                    
                    
  END IF;
  
  v_update_cnt := v_update_cnt + v_upd_cnt;
  -- 06/25/2018 end
   
  v_stage := 'Write counts and ending to execution_log table';
  p_out_count := v_update_cnt;
  prc_execution_log_entry(v_process_name,v_release_level,'INFO','User count: '||v_user_cnt||
                          ' User update count: '||v_update_cnt
                          ,NULL);
  prc_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure completed for file ',NULL);
  COMMIT;
EXCEPTION
  WHEN OTHERS THEN    
    p_out_returncode := 99;
    ROLLBACK;
    prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                            'failed at Stage: '||v_stage||
                            ' --> '||SQLERRM,NULL);
END  ;
/