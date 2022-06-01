CREATE OR REPLACE 
PROCEDURE prc_insert_stg_prd_claim
   ( p_in_stg_prd_claim_rec STAGE_PRD_CLAIM_IMPORT_RECORD%ROWTYPE,
     p_out_return_code  OUT NUMBER )
   IS
   
/*******************************************************************************
--
-- Purpose: Inserts into STAGE_PRD_CLAIM_IMPORT_RECORD table
--
-- MODIFICATION HISTORY
-- Person     Date        Comments
-- ---------  ----------  -------------------------------------------
-- Percy M.   04/19/06    Initial Creation
-- D Murray   05/15/2006  Changed to have a return code so that calling procedure
--                        will know if insert was successful or not, changed
--                        dbms_outputs to execution_log entries. (Bug 12148)
-- Raju N     07/26/2006   New columns to accomodate for the five characteristics
*******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'PRC_INSERT_STG_PRD_CLAIM';
  v_release_level         execution_log.release_level%type := '1';


BEGIN

     INSERT INTO STAGE_PRD_CLAIM_IMPORT_RECORD
            (import_record_id,
             import_file_id,
             record_type,
             header_record_id,
             user_name,
             node_name,
             form_element_name,
             form_element_value,
             product_name,
             quantity,
             characteristic_name1,
             characteristic_value1,
             characteristic_name2,
             characteristic_value2,
             characteristic_name3,
             characteristic_value3,
             characteristic_name4,
             characteristic_value4,
             characteristic_name5,
             characteristic_value5,
             created_by,
             date_created)
     VALUES
           (p_in_stg_prd_claim_rec.import_record_id,
           p_in_stg_prd_claim_rec.import_file_id,
           p_in_stg_prd_claim_rec.record_type,
           p_in_stg_prd_claim_rec.header_record_id,
           p_in_stg_prd_claim_rec.user_name,
           p_in_stg_prd_claim_rec.node_name,
           p_in_stg_prd_claim_rec.form_element_name,
           p_in_stg_prd_claim_rec.form_element_value,
           p_in_stg_prd_claim_rec.product_name,
           p_in_stg_prd_claim_rec.quantity,
           p_in_stg_prd_claim_rec.characteristic_name1,
           p_in_stg_prd_claim_rec.characteristic_value1,
           p_in_stg_prd_claim_rec.characteristic_name2,
           p_in_stg_prd_claim_rec.characteristic_value2,
           p_in_stg_prd_claim_rec.characteristic_name3,
           p_in_stg_prd_claim_rec.characteristic_value3,
           p_in_stg_prd_claim_rec.characteristic_name4,
           p_in_stg_prd_claim_rec.characteristic_value4,
           p_in_stg_prd_claim_rec.characteristic_name5,
           p_in_stg_prd_claim_rec.characteristic_value5,
           0,
           SYSDATE
     );

  p_out_return_code := 0;
EXCEPTION
   WHEN OTHERS THEN
     p_out_return_code := 99;
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                             'Import_file_id: '||p_in_stg_prd_claim_rec.import_file_id||
                             'Import_record_id: '||p_in_stg_prd_claim_rec.import_record_id||
                             ' --> '||SQLERRM,null);
     COMMIT;
END; -- Procedure
/
