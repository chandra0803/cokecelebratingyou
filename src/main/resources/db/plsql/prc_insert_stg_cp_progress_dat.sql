CREATE OR REPLACE PROCEDURE prc_insert_stg_cp_progress_dat
   ( p_in_import_record IN stage_cp_progress_data_import%ROWTYPE, 
     p_out_return_code OUT number)
   IS
/*******************************************************************************
--
-- Purpose:
--
-- MODIFICATION HISTORY
-- Person      Date    Comments
-- ---------   ------  -------------------------------------------       
-- M Lindvig  08/06/08  Copied from Goalquest
*******************************************************************************/

  v_process_name          execution_log.process_name%type  := 'PRC_INSERT_STG_CP_PROGRESS_DAT';
  v_release_level         execution_log.release_level%type := '1';


BEGIN
  INSERT INTO stage_cp_progress_data_import
    (import_record_id,
     import_file_id,
     action_type,
     login_id,
     user_id,
     first_name,
     last_name,
     email_address,
     total_perf_to_date,
     created_by,
     date_created)
  VALUES
    (p_in_import_record.import_record_id,
     p_in_import_record.import_file_id,
     p_in_import_record.action_type,
     p_in_import_record.login_id,
     p_in_import_record.user_id,
     p_in_import_record.first_name,
     p_in_import_record.last_name,
     p_in_import_record.email_address,
     p_in_import_record.total_perf_to_date,
     p_in_import_record.created_by,
     p_in_import_record.date_created);

  p_out_return_code := 0 ;
      
EXCEPTION
    WHEN others THEN
      p_out_return_code := 99 ;
      prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                             'Import_file_id: '||p_in_import_record.import_file_id||
                             'Import_record_id: '||p_in_import_record.import_record_id||
                             ' --> '||SQLERRM,null);
     COMMIT;
     
END;
/