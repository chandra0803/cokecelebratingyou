CREATE OR REPLACE PROCEDURE prc_insert_stg_hierarchy(p_import_record stage_hierarchy_import_record%rowtype,
              p_out_return_code  OUT NUMBER)
 IS
   /*******************************************************************************
   -- Purpose: To insert records into STAGE_HIERARCHY_IMPORT_RECORD table
   --
   -- Person        Date       Comments
   -- -----------   --------   -----------------------------------------------------
   -- D Murray      05/01/2006 Changed to have a return code so that calling procedure
   --                          will know if insert was successful or not, changed
   --                          dbms_outputs to execution_log entries. (Bug 12148)
   -- Chidamba      11/28/2012 Changes to add 5 more characteristic
   *******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'PRC_INSERT_STG_HIERARCHY';
  v_release_level         execution_log.release_level%type := '1';

BEGIN
  INSERT INTO stage_hierarchy_import_record a
    (import_record_id,
     import_file_id,
     action_type,
     name,
     old_name,
     move_to_node_name,
     description,
     node_type_id,
     characteristic_id1,
     characteristic_id2,
     characteristic_id3,
     characteristic_id4,
     characteristic_id5,
     characteristic_id6,
     characteristic_id7,
     characteristic_id8,
     characteristic_id9,
     characteristic_id10,
     characteristic_name1,
     characteristic_name2,
     characteristic_name3,
     characteristic_name4,
     characteristic_name5,
     characteristic_name6,
     characteristic_name7,
     characteristic_name8,
     characteristic_name9,
     characteristic_name10,
     characteristic_value1,
     characteristic_value2,
     characteristic_value3,
     characteristic_value4,
     characteristic_value5,
     characteristic_value6,
     characteristic_value7,
     characteristic_value8,
     characteristic_value9,
     characteristic_value10,
     created_by,
     date_created,
     parent_node_name,
     old_parent_node_name,
     node_type_name)
  VALUES
    (p_import_record.import_record_id,
     p_import_record.import_file_id,
     p_import_record.action_type,
     p_import_record.name,
     p_import_record.old_name,
     p_import_record.move_to_node_name,
     p_import_record.description,
     p_import_record.node_type_id,
     p_import_record.characteristic_id1,
     p_import_record.characteristic_id2,
     p_import_record.characteristic_id3,
     p_import_record.characteristic_id4,
     p_import_record.characteristic_id5,
     p_import_record.characteristic_id6,
     p_import_record.characteristic_id7,
     p_import_record.characteristic_id8,
     p_import_record.characteristic_id9,
     p_import_record.characteristic_id10,
     p_import_record.characteristic_name1,
     p_import_record.characteristic_name2,
     p_import_record.characteristic_name3,
     p_import_record.characteristic_name4,
     p_import_record.characteristic_name5,
     p_import_record.characteristic_name6,
     p_import_record.characteristic_name7,
     p_import_record.characteristic_name8,
     p_import_record.characteristic_name9,
     p_import_record.characteristic_name10,
     p_import_record.characteristic_value1,
     p_import_record.characteristic_value2,
     p_import_record.characteristic_value3,
     p_import_record.characteristic_value4,
     p_import_record.characteristic_value5,
     p_import_record.characteristic_value6,
     p_import_record.characteristic_value7,
     p_import_record.characteristic_value8,
     p_import_record.characteristic_value9,
     p_import_record.characteristic_value10,
     p_import_record.created_by,
     p_import_record.date_created,
     p_import_record.parent_node_name,
     p_import_record.old_parent_node_name,
     p_import_record.node_type_name);

  p_out_return_code := 0;
EXCEPTION
   WHEN OTHERS THEN
     p_out_return_code := 99;
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                             'Import_file_id: '||p_import_record.import_file_id||
                             'Import_record_id: '||p_import_record.import_record_id||
                             ' --> '||SQLERRM,null);
     COMMIT;
END  prc_insert_stg_hierarchy ;
/