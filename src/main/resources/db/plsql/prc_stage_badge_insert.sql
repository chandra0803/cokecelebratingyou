CREATE OR REPLACE PROCEDURE prc_stg_badge_insert(p_import_record stage_badge_load%rowtype,
                                                        p_out_return_code  OUT NUMBER)
 IS
   /*******************************************************************************
   -- Purpose: To insert records into stage_badge_load table
   --
   -- Person        Date       Comments
   -- -----------   --------   -----------------------------------------------------
   -- Ravi Dhanekula  10/01/2012   Initial Creation
   *******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'prc_stg_badge_insert';
  v_release_level         execution_log.release_level%type := '1';


BEGIN

  INSERT INTO stage_badge_load
    (import_record_id,
     import_file_id,
     USER_NAME, 
     USER_ID,
     badge_name,
     insert_or_update,
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
     p_import_record.badge_name, 
     p_import_record.insert_or_update, 
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
END  prc_stg_badge_insert ;
/
