CREATE OR REPLACE PROCEDURE prc_stg_award_record_insert(p_import_record STAGE_AWARD_LEVEL_IMPORT%rowtype,
                                                        p_out_return_code  OUT NUMBER)
 IS
   /*******************************************************************************
   -- Purpose: To insert records into STAGE_AWARD_LEVEL_IMPORT table
   --
   -- Person        Date        Comments
   -- -----------   --------    -----------------------------------------------------
   -- S Majumder    02/18/2009  Initial Creation
   -- Arun S        10/06/2010  PURL changes, added and populated column AWARD_DATE
   -- Chidamba      09/21/2012  G5 change added and populated column Comments
   -- Chidamba      10/10/2012  Comment out columns FIRST_NAME, MIDDLE_NAME, LAST_NAME 
                                COUNTRY_CODE, COUNTRY_ID  AND EMAIL_ADDRESS As AWARD 
                                for NON PARTICIPANT is removed.
  -- Chidamba      	12/12/2012  Change Award file load to include up to 3 form elements for PURL promotions only.
  -- Swati			04/18/2014	Change Award File load to include anniversary days and years.
   *******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'prc_stg_award_record_insert';
  v_release_level         execution_log.release_level%type := '1';


BEGIN

  INSERT INTO stage_award_level_import a
    (import_record_id,
     import_file_id,
     action_type,
     USER_NAME, 
     AWARD_LEVEL, 
     --FIRST_NAME,                 -- Chidamba      10/10/2012 start  
     --MIDDLE_NAME, 
     --LAST_NAME, 
     --COUNTRY_CODE, 
     --COUNTRY_ID, 
     --EMAIL_ADDRESS,              -- Chidamba      10/10/2012 end          
     created_by,
     date_created,
     award_date,      --Added on 10/06/2010
     comments,        --Added on 09/21/2012
     form_element_1,   --Added on 12/12/2012
     form_element_2,   --Added on 12/12/2012
     form_element_3,    --Added on 12/12/2012         
	 anniversary_num -- Added on 04/18/2014
     )
  VALUES
    (p_import_record.import_record_id,
     p_import_record.import_file_id,
     p_import_record.action_type,
     p_import_record.USER_NAME, 
     p_import_record.AWARD_LEVEL, 
    -- p_import_record.FIRST_NAME,      -- Commented --Chidamba  10/10/2012 start
    -- p_import_record.MIDDLE_NAME, 
    -- p_import_record.LAST_NAME, 
    -- p_import_record.COUNTRY_CODE, 
    -- p_import_record.COUNTRY_ID, 
    -- p_import_record.EMAIL_ADDRESS,    -- Commented --Chidamba  10/10/2012 end     
     p_import_record.created_by,
     p_import_record.date_created,
     p_import_record.award_date,     --Added on 10/06/2010
     p_import_record.comments,       --Added on 09/21/2012
     p_import_record.form_element_1,   --Added on 12/12/2012
     p_import_record.form_element_2,   --Added on 12/12/2012
     p_import_record.form_element_3,    --Added on 12/12/2012
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
END;
/