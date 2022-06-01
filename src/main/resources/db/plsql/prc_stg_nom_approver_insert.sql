CREATE OR REPLACE PROCEDURE prc_stg_nom_approver_insert(p_import_record    stage_nom_approvers_import%rowtype,
                                                        p_out_return_code  OUT NUMBER)
 IS
   /*******************************************************************************
    Purpose: To insert records into STAGE_AWARD_LEVEL_IMPORT table
   
    Person        Date        Comments
   -----------   --------    -----------------------------------------------------
    nagarajs     04/15/2016   Initial Creation.
   *******************************************************************************/
   
  --CONSTANTS
  C_process_name            CONSTANT  execution_log.process_name%type  := 'PRC_STG_NOM_APPROVER_INSERT';
  C_release_level           CONSTANT  execution_log.release_level%type := '1';

BEGIN

  INSERT INTO stage_nom_approvers_import a
    (import_record_id,
     import_file_id,
     user_id,
     user_name, 
     approval_type, 
     min_value,
     max_value,
     approval_round,      
     created_by,
     date_created
     )
  VALUES
    (p_import_record.import_record_id,
     p_import_record.import_file_id,
     p_import_record.user_id,
     p_import_record.user_name, 
     p_import_record.approval_type, 
     p_import_record.min_value, 
     p_import_record.max_value, 
     p_import_record.approval_round, 
     p_import_record.created_by,
     p_import_record.date_created
     );
     
  p_out_return_code := 0;
EXCEPTION
   WHEN OTHERS THEN
     p_out_return_code := 99;
     prc_execution_log_entry(C_process_name,C_release_level,'ERROR',
                             'Import_file_id: '||p_import_record.import_file_id||
                             'Import_record_id: '||p_import_record.import_record_id||
                             ' --> '||SQLERRM,null);
     COMMIT;
END;
/
