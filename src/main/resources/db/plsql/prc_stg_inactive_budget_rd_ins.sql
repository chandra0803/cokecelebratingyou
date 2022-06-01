CREATE OR REPLACE PROCEDURE prc_stg_inactive_budget_rd_ins (
      p_import_record         STAGE_INACTIVE_BUDGET_RD%ROWTYPE,
      p_out_return_code   OUT NUMBER)
   IS
      -- EXECUTION LOG VARIABLES
      v_process_name    execution_log.process_name%TYPE
                           := 'prc_stg_inactive_budget_rd_ins';
      v_release_level   execution_log.release_level%TYPE := '1';

   BEGIN
      INSERT INTO STAGE_INACTIVE_BUDGET_RD (import_record_id, 
                                            import_file_id, 
                                            budget_master_name, 
                                            budget_time_period, 
                                            budget_master_time_period, 
                                            org_unit_id,
                                            budget_owner_login_id, 
                                            budget_owner_name, 
                                            budget_amount, 
                                            transfer_to_owner1, 
                                            amount_owner1, 
                                            transfer_to_owner2, 
                                            amount_owner2, 
                                            transfer_to_owner3, 
                                            amount_owner3, 
                                            budget_master_id,
                                            budget_segment_id,
                                            budget_id,
                                            user_id,
                                            node_id,
                                            budget_type,
                                            date_created, 
                                            created_by, 
                                            date_modified, 
                                            modified_by, 
                                            version)
           VALUES (p_import_record.import_record_id,
                   p_import_record.import_file_id,
                   p_import_record.budget_master_name,
                   p_import_record.budget_time_period,
                   p_import_record.budget_master_time_period,
                   p_import_record.org_unit_id,
                   p_import_record.budget_owner_login_id,
                   p_import_record.budget_owner_name,
                   p_import_record.budget_amount,
                   p_import_record.transfer_to_owner1,
                   p_import_record.amount_owner1,
                   p_import_record.transfer_to_owner2,
                   p_import_record.amount_owner2,
                   p_import_record.transfer_to_owner3,
                   p_import_record.amount_owner3,
                   p_import_record.budget_master_id,
                   p_import_record.budget_segment_id,
                   p_import_record.budget_id,
                   p_import_record.user_id,
                   p_import_record.node_id,
                   p_import_record.budget_type,
                   p_import_record.date_created,
                   p_import_record.created_by,
                   p_import_record.date_modified,
                   p_import_record.modified_by,
                   p_import_record.version);

      p_out_return_code := 0;
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := 99;
         prc_execution_log_entry (
            v_process_name,
            v_release_level,
            'ERROR',
               'Import_file_id: '
            || p_import_record.import_file_id
            || 'Import_record_id: '
            || p_import_record.import_record_id
            || ' --> '
            || SQLERRM,
            NULL);
         COMMIT;
   END prc_stg_inactive_budget_rd_ins;
/
