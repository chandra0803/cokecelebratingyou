CREATE OR REPLACE PROCEDURE prc_stg_leaderboard_insert(p_import_record stage_leaderboard_load%rowtype,
                                                        p_out_return_code  OUT NUMBER)
 IS
   /*******************************************************************************
   -- Purpose: To insert records into stage_leaderboard_load table
   --
   -- Person        Date       Comments
   -- -----------   --------   -----------------------------------------------------
   -- Ravi Dhanekula  07/05/2012   Initial Creation
   *******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'prc_stg_leaderboard_insert';
  v_release_level         execution_log.release_level%type := '1';


BEGIN

  INSERT INTO stage_leaderboard_load
    (import_record_id,
     import_file_id,
     USER_NAME, 
     USER_ID,
     SCORE ,
     leaderboard_name,
    as_of_date,
    add_or_replace,
     is_deleted,
     created_by,
     date_created,
     modified_by,
     date_modified,     
     version
     )
  VALUES
    (p_import_record.import_record_id,
     p_import_record.import_file_id,
     p_import_record.USER_NAME, 
     p_import_record.USER_ID, 
     p_import_record.SCORE, 
     p_import_record.leaderboard_name, 
     p_import_record.as_of_date, 
     p_import_record.add_or_replace, 
     p_import_record.is_deleted, 
     p_import_record.created_by,
     p_import_record.date_created,
     p_import_record.modified_by,
     p_import_record.date_modified,
     p_import_record.version     
     );
     
  p_out_return_code := 0;
EXCEPTION
   WHEN OTHERS THEN
     p_out_return_code := 99;
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                             'Import_file_id: '||p_import_record.import_file_id||
                             'Import_record_id: '||p_import_record.import_record_id||
                             ' --> '||SQLERRM,null);
     COMMIT;
END  prc_stg_leaderboard_insert ;
/
