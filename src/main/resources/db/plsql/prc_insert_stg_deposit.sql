CREATE OR REPLACE PROCEDURE prc_insert_stg_deposit(p_import_record stage_deposit_import_record%rowtype,
              p_out_return_code  OUT NUMBER)
 IS
   /*******************************************************************************
   -- Purpose: To insert records into STAGE_DEPOSIT_IMPORT_RECORD table
   --
   -- Person        Date       Comments
   -- -----------   --------   -----------------------------------------------------
   -- D Murray      05/01/2006  Changed to have a return code so that calling procedure
   --                           will know if insert was successful or not, changed
   --                           dbms_outputs to execution_log entries. (Bug 12148)
   -- Arun S        10/06/2010  PURL changes, added and populated column AWARD_DATE
   -- Chidamba      09/21/2012  G5 change added and populated column Comments
   -- Chidamba      01/10/2013  G5 change added 3 from element   
   -- Swati			04/18/2014	G5 change added anniversary num.
   *******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'PRC_INSERT_STG_DEPOSIT';
  v_release_level         execution_log.release_level%type := '1';

BEGIN

  INSERT INTO stage_deposit_import_record a
    (import_record_id,
     import_file_id,
     action_type,
     user_name,
     award_amount,
     created_by,
     date_created,
     award_date,     --Added on 10/06/2010
     comments,       --Added on 09/21/2012
     form_element_1, --Added on 01/10/2013
     form_element_2, --Added on 01/10/2013
     form_element_3,  --Added on 01/10/2013   
	 anniversary_num -- Added on 04/18/2014
     )
  VALUES
    (p_import_record.import_record_id,
     p_import_record.import_file_id,
     p_import_record.action_type,
     p_import_record.user_name,
     p_import_record.award_amount,
     p_import_record.created_by,
     p_import_record.date_created,
     p_import_record.award_date,     --Added on 10/06/2010
     p_import_record.comments,       --Added on 09/21/2012
     p_import_record.form_element_1, --Added on 01/10/2013
     p_import_record.form_element_2, --Added on 01/10/2013
     p_import_record.form_element_3, --Added on 01/10/2013
	 p_import_record.anniversary_num -- Added on 04/18/2014
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
END  prc_insert_stg_deposit ;
/