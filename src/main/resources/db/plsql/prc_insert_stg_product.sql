CREATE OR REPLACE 
PROCEDURE prc_insert_stg_product
             (p_import_record    IN  stage_product_import_record%ROWTYPE,
              p_out_return_code  OUT NUMBER)
 IS
   /*******************************************************************************
   -- Purpose: To insert records into STAGE_PRODUCT_IMPORT_RECORD table
   --
   -- Person        Date       Comments
   -- -----------   --------   -----------------------------------------------------
   -- D Murray      04/28/2006 Changed to have a return code so that calling procedure
   --                          will know if insert was successful or not, changed 
   --                          dbms_outputs to execution_log entries. (Bug 12148)
   *******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'PRC_INSERT_STG_PRODUCT';
  v_release_level         execution_log.release_level%type := '1';

BEGIN
  INSERT INTO stage_product_import_record a
    (import_record_id,
     import_file_id,
     action_type,
     category_id,
     category_name,
     category_desc,
     subcategory_id,
     subcategory_name,
     subcategory_desc,
     product_id,
     product_name,
     product_desc,
     sku_code,
     characteristic_id1,
     characteristic_id2,
     characteristic_id3,
     characteristic_id4,
     characteristic_id5,
     characteristic_name1,
     characteristic_name2,
     characteristic_name3,
     characteristic_name4,
     characteristic_name5,
     created_by,
     date_created
)
  VALUES
    (p_import_record.import_record_id,
     p_import_record.import_file_id,
     p_import_record.action_type,
     p_import_record.category_id,
     p_import_record.category_name,
     p_import_record.category_desc,
     p_import_record.subcategory_id,
     p_import_record.subcategory_name,
     p_import_record.subcategory_desc,
     p_import_record.product_id,
     p_import_record.product_name,
     p_import_record.product_desc,     
     p_import_record.sku_code,
     p_import_record.characteristic_id1,
     p_import_record.characteristic_id2,
     p_import_record.characteristic_id3,
     p_import_record.characteristic_id4,
     p_import_record.characteristic_id5,
     p_import_record.characteristic_name1,
     p_import_record.characteristic_name2,
     p_import_record.characteristic_name3,
     p_import_record.characteristic_name4,
     p_import_record.characteristic_name5,
     p_import_record.created_by,
     p_import_record.date_created
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
END  prc_insert_stg_product;
/

