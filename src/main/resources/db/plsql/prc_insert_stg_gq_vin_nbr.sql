CREATE OR REPLACE 
PROCEDURE prc_insert_stg_gq_vin_nbr
   ( p_in_import_record IN stage_gq_vin_nbr_import%ROWTYPE, 
     p_out_return_code OUT number)
   IS
/*******************************************************************************
--
-- Purpose:
--
-- MODIFICATION HISTORY
-- Person      Date    Comments
-- ---------   ------  -------------------------------------------       
-- Percy M.   12/11/06  Initial Creation
*******************************************************************************/

  v_process_name          execution_log.process_name%type  := 'PRC_INSERT_STG_GQ_VIN_NBR';
  v_release_level         execution_log.release_level%type := '1';


BEGIN
  INSERT INTO stage_GQ_vin_nbr_import
    (import_record_id,
     import_file_id,
     action_type,
     login_id,
     user_id,
     first_name,
     last_name,
     email_address,
     vin_nbr,
     model,
     trans_type,
     sales_date,
     delivery_date,
     dealer_code,
     dealer_name,
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
     p_in_import_record.vin_nbr,
     p_in_import_record.model,
     p_in_import_record.trans_type,
     p_in_import_record.sales_date,
     p_in_import_record.delivery_date,
     p_in_import_record.dealer_code,
     p_in_import_record.dealer_name,
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