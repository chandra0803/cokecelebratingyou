CREATE OR REPLACE 
PROCEDURE prc_insert_stg_budget(p_import_record stage_budget_import_record%rowtype,
              p_out_return_code  OUT NUMBER)
 IS
   /*******************************************************************************
   -- Purpose: To insert records into STAGE_BUDGET_IMPORT_RECORD table
   --
   -- Person        Date       Comments
   -- -----------   --------   -----------------------------------------------------
   -- D Murray      05/01/2006 Changed to have a return code so that calling procedure
   --                          will know if insert was successful or not, changed
   --                          dbms_outputs to execution_log entries. (Bug 12148)
   *******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'PRC_INSERT_STG_BUDGET';
  v_release_level         execution_log.release_level%type := '1';


BEGIN

  INSERT INTO stage_budget_import_record a
    (import_record_id,
     import_file_id,
     action_type,
     budget_owner,
     node_id,
     user_id,
     budget_amount,
     created_by,
     date_created)
  VALUES
    (p_import_record.import_record_id,
     p_import_record.import_file_id,
     p_import_record.action_type,
     p_import_record.budget_owner,
     p_import_record.node_id,
     p_import_record.user_id,
     p_import_record.budget_amount,
     p_import_record.created_by,
     p_import_record.date_created);
     
  p_out_return_code := 0;
EXCEPTION
   WHEN OTHERS THEN
     p_out_return_code := 99;
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                             'Import_file_id: '||p_import_record.import_file_id||
                             'Import_record_id: '||p_import_record.import_record_id||
                             ' --> '||SQLERRM,null);
     COMMIT;
END  prc_insert_stg_budget ;
/