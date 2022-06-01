CREATE OR REPLACE PROCEDURE prc_gq_vin_nbr_import
   (p_file_name IN VARCHAR2, p_out_returncode OUT NUMBER)
   IS
  /*******************************************************************************
  -- Purpose: To load the Auto VIN# data for GoalQuest
  --
  -- Person        Date       Comments
  -- -----------   --------   -----------------------------------------------------
  -- iyer, Satish  03/08/2007 Initial Creation
  -- iyer, Satish  04/11/2007 Stripped path from filename for import_file.
  -- Arun S        01/20/2009 Changes to replace UC4
  --Ravi Dhanekula 08/21/2012 Fixed the bug# 42243
  
  *******************************************************************************/
  
  c_delimiter                   CONSTANT  VARCHAR2(10):='|';
  v_count                       NUMBER := 0; --for commit count
  v_stage                       VARCHAR2(500);
  v_return_code                 NUMBER(5);  -- return from insert call
  v_header_rec                  BOOLEAN := FALSE;--Changed to FALSE, as the header is already taken care off in ADC

  --UTL_FILE
  v_in_file_handler             utl_file.file_type;
  --v_path_name                   os_propertyset.string_val%TYPE :=
  --                                      fnc_get_system_variable('import.file.utl_path','db_utl_path');
  v_text                        VARCHAR2(500);
  v_file_name                   VARCHAR2(500); 
  v_directory_path              VARCHAR2(4000);
  v_directory_name              VARCHAR2(30);

  -- RECORD TYPE DECLARATION
  rec_stage_gq_vin_nbr_import   stage_gq_vin_nbr_import%ROWTYPE;
  rec_import_file               import_file%ROWTYPE;
  rec_import_record_error       import_record_error%ROWTYPE;

  --gq_progress_data LOAD
  v_import_record_id            stage_gq_vin_nbr_import.import_record_id%TYPE;
  v_import_file_id              stage_gq_vin_nbr_import.import_file_id%TYPE;
  v_action_type                 stage_gq_vin_nbr_import.action_type%TYPE;
  v_import_record_error_id      import_record_error.import_record_error_id%type;

  v_login_id                   stage_gq_vin_nbr_import.login_id%TYPE;
  v_user_id                    stage_gq_vin_nbr_import.user_id%TYPE;
  v_first_name                 stage_gq_vin_nbr_import.first_name%TYPE;
  v_last_name                  stage_gq_vin_nbr_import.last_name%TYPE;
  v_email_addr                 stage_gq_vin_nbr_import.email_address%TYPE;
  v_vin_nbr                    stage_gq_vin_nbr_import.vin_nbr%TYPE;
  v_model                      stage_gq_vin_nbr_import.model%TYPE;
  v_trans_type                 stage_gq_vin_nbr_import.trans_type%TYPE;
  v_sales_date                 stage_gq_vin_nbr_import.sales_date%TYPE;
  v_delivery_date              stage_gq_vin_nbr_import.delivery_date%TYPE;
  v_dealer_code                stage_gq_vin_nbr_import.dealer_code%TYPE;
  v_dealer_name                stage_gq_vin_nbr_import.dealer_name%TYPE;
  v_str_sales_date             VARCHAR2(100); 
  v_str_delivery_date          VARCHAR2(100); 

  v_created_by                  stage_pax_import_record.created_by%TYPE := 0;
  v_date_created                stage_pax_import_record.date_created%TYPE := sysdate;

  -- EXECUTION LOG VARIABLES
  v_process_name                execution_log.process_name%type  := 'PRC_GQ_VIN_NBR_IMPORT' ;
  v_release_level               execution_log.release_level%type := '1';
  v_execution_log_msg           execution_log.text_line%TYPE;
  
  -- COUNTS
  v_recs_in_file_cnt            INTEGER := 0;
  v_field_cnt                   INTEGER := 0;
  v_recs_loaded_cnt             INTEGER := 0;
  v_error_count                 PLS_INTEGER := 0;
  v_error_tbl_count             PLS_INTEGER := 0;
  v_language_code               VARCHAR2(10) := 'en_US';

  -- EXCEPTIONS
  exit_program_exception        EXCEPTION;


BEGIN
  v_stage := 'Write start to execution_log table';
  p_out_returncode := 0;
  prc_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure Started for file '
                          ||p_file_name, null);
  COMMIT;

  --Changes to replace UC4, added on 01/20/2009
  v_stage := 'Get Directory path and File name from p_file_name'; 
  prc_get_directory_file_name ( p_file_name, v_directory_path, v_file_name );

  BEGIN
    v_stage := 'Insert into import_file table';
    SELECT IMPORT_FILE_PK_SQ.NEXTVAL INTO v_import_file_id FROM dual;
    INSERT INTO import_file
      (import_file_id,
       file_name,
       file_type,
       import_record_count,
       import_record_error_count,
       status,
       date_staged,
       staged_by,
       version)
    VALUES
      (v_import_file_id,
       substr(v_file_name, 1, instr(v_file_name, '.') - 1) || '_' || v_import_file_id,
       'vin_nbr',
       0,
       0,
       'stg_in_process',
       SYSDATE,
       v_created_by ,
       1);
  EXCEPTION
    WHEN OTHERS THEN
       prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                               'Stage: '||v_stage||' -- '||SQLERRM,null);
  END;

  --Changes to replace UC4, added on 01/20/2009
  BEGIN
    v_stage := 'Get Directory name';                                 
    SELECT directory_name
      INTO v_directory_name
      FROM all_directories
     WHERE directory_path = v_directory_path
       AND ROWNUM = 1;   
  EXCEPTION
    WHEN OTHERS THEN
      v_execution_log_msg := SQLERRM; 
      RAISE exit_program_exception;  
  END;

  v_stage := 'Open file for read: '||v_file_name;
  v_in_file_handler := utl_file.fopen(v_directory_name, v_file_name, 'r');

  LOOP
    --*** move records from text file into the table stg_gq_progress_data_import_rec.**
    v_stage := 'Reset variables';
    v_field_cnt := 0;

    BEGIN
      v_stage := 'Get record';
      utl_file.get_line(v_in_file_handler, v_text);
      
      IF v_header_rec THEN
        v_header_rec := FALSE;
      ELSE
        v_recs_in_file_cnt := v_recs_in_file_cnt + 1;
        v_stage := 'Parsing record number: '||v_recs_in_file_cnt;

        v_field_cnt := v_field_cnt + 1;
        v_login_id :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 1, c_delimiter)),chr(13));

        v_field_cnt := v_field_cnt + 1;
        v_first_name :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 2, c_delimiter)),chr(13));
      
        v_field_cnt := v_field_cnt + 1;
        v_last_name :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 3, c_delimiter)),chr(13));

        v_field_cnt := v_field_cnt + 1;
        v_email_addr :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 4, c_delimiter)),chr(13));

        v_field_cnt := v_field_cnt + 1;
        v_vin_nbr :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 5, c_delimiter)),chr(13));

        v_field_cnt := v_field_cnt + 1;
        v_model :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 6, c_delimiter)),chr(13));

        v_field_cnt := v_field_cnt + 1;
        v_trans_type :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 7, c_delimiter)),chr(13));

        v_field_cnt := v_field_cnt + 1;
        v_str_sales_date :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 8, c_delimiter)),chr(13));
             
        v_field_cnt := v_field_cnt + 1;
        v_str_delivery_date :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 9, c_delimiter)),chr(13));

        v_field_cnt := v_field_cnt + 1;
        v_dealer_code :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 10, c_delimiter)),chr(13));

        v_field_cnt := v_field_cnt + 1;
        v_dealer_name :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 11, c_delimiter)),chr(13));


        v_stage := 'Validations';
        SELECT import_record_pk_sq.NEXTVAL
          INTO v_import_record_id
          FROM dual;
      

        v_stage := 'Check for required columns';
        rec_import_record_error.date_created     := sysdate;
        rec_import_record_error.created_by       := v_created_by;
        rec_import_record_error.import_record_id := v_import_record_id;
        rec_import_record_error.import_file_id   := v_import_file_id;
        
        v_sales_date := NULL;
        v_delivery_date := NULL;

        IF rtrim(v_login_id) IS NULL THEN
            rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
            rec_import_record_error.param1   := 'User Name';
            rec_import_record_error.param2   := v_login_id;
            prc_insert_import_record_error(rec_import_record_error);
        ELSE 
            BEGIN
            SELECT user_id
                into v_user_id
            FROM application_user 
            WHERE lower(user_name) = lower(v_login_id);
            
            EXCEPTION when others then
              rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
              rec_import_record_error.param1   := 'User Name';
              rec_import_record_error.param2   := v_login_id;              
              prc_insert_import_record_error(rec_import_record_error);                
            
            END;
        END IF; -- v_user_name is null

        IF trim(v_vin_nbr) IS NULL THEN
            rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
            rec_import_record_error.param1   := 'VIN #';
            rec_import_record_error.param2   := v_vin_nbr;            
            prc_insert_import_record_error(rec_import_record_error);
        END IF;

        IF trim(v_model) IS NULL THEN
            rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
            rec_import_record_error.param1   := 'Model';
            rec_import_record_error.param2   := v_model;            
            prc_insert_import_record_error(rec_import_record_error);
        END IF;

        IF trim(v_trans_type) IS NULL THEN
            rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
            rec_import_record_error.param1   := 'Transaction Type';
            rec_import_record_error.param2   := v_trans_type;            
            prc_insert_import_record_error(rec_import_record_error);
        ELSE     
            IF v_trans_type NOT IN ('S', 'R') THEN 
              rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
              rec_import_record_error.param1   := 'Transaction Type';
              rec_import_record_error.param2   := v_trans_type;              
              prc_insert_import_record_error(rec_import_record_error);                
            END IF;
        END IF;
        
        IF TRIM(v_str_sales_date) IS NOT NULL THEN
            BEGIN 
                v_sales_date := fnc_locale_to_date_dt(v_str_sales_date, v_language_code);
            EXCEPTION when others then
              rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
              rec_import_record_error.param1   := 'Sales Date';
              rec_import_record_error.param2   := v_str_sales_date;              
              prc_insert_import_record_error(rec_import_record_error);                
            END;
        END IF;

        IF TRIM(v_str_delivery_date) IS NOT NULL THEN
            BEGIN 
                v_delivery_date := fnc_locale_to_date_dt(v_str_delivery_date, v_language_code);
            EXCEPTION when others then
              rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
              rec_import_record_error.param1   := 'Delivery Date';
              rec_import_record_error.param2   := v_str_delivery_date;              
              prc_insert_import_record_error(rec_import_record_error);                
            END;
        END IF;

        v_action_type := 'add';
        v_stage := 'Initialize the record type';
        rec_stage_gq_vin_nbr_import.import_record_id := v_import_record_id;
        rec_stage_gq_vin_nbr_import.import_file_id   := v_import_file_id;
        rec_stage_gq_vin_nbr_import.action_type      := v_action_type;
        rec_stage_gq_vin_nbr_import.login_id         := v_login_id;
        rec_stage_gq_vin_nbr_import.USER_id          := v_user_id;
        rec_stage_gq_vin_nbr_import.first_name       := v_first_name;
        rec_stage_gq_vin_nbr_import.last_name        := v_last_name;
        rec_stage_gq_vin_nbr_import.email_address    := v_email_addr;
        rec_stage_gq_vin_nbr_import.vin_nbr          := v_vin_nbr;
        rec_stage_gq_vin_nbr_import.model            := v_model;
        rec_stage_gq_vin_nbr_import.trans_type       := v_trans_type;
        rec_stage_gq_vin_nbr_import.sales_date       := v_sales_date;
        rec_stage_gq_vin_nbr_import.delivery_date    := v_delivery_date;
        rec_stage_gq_vin_nbr_import.dealer_code      := v_dealer_code;
        rec_stage_gq_vin_nbr_import.dealer_name      := v_dealer_name;
        rec_stage_gq_vin_nbr_import.created_by       := v_created_by;
        rec_stage_gq_vin_nbr_import.date_created     := v_date_created;


        --Insert into stg_gq_progress_data_import_rec
        v_stage := 'Insert into stage_gq_vin_nbr_import table';
        prc_insert_stg_gq_vin_nbr(rec_stage_gq_vin_nbr_import, v_return_code);
        
        IF v_return_code = 99 THEN
          v_error_count := v_error_count + 1;  -- need to count any insert errors
        ELSE
          v_count := v_count + 1;
          v_recs_loaded_cnt := v_recs_loaded_cnt + 1;
        END IF;

        IF MOD(v_count, 10) = 0 THEN
          COMMIT;
        END IF;
      END IF; -- v_header_rec
    EXCEPTION
      WHEN no_data_found THEN
        -- end of file
        --Program continues on to compare rec cnt.  Then final
        -- file_load_audit entry is done.
        utl_file.fclose(v_in_file_handler);
        exit;
      WHEN OTHERS THEN
        IF v_stage LIKE 'Parsing%' THEN
          v_stage := v_stage||' field number: '||v_field_cnt;
        END IF;
        prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                                'Stage: '||v_stage||' --> '||SQLERRM,null);

        v_error_count := v_error_count + 1;  -- need to count any parsing errors
    END; -- Get record
  END LOOP; -- *** end of loop to move data to stage_pax_import_record table. ***
  COMMIT;


  v_stage := 'Count distinct records that have an error';
  SELECT COUNT(DISTINCT(import_record_id))
    INTO v_error_tbl_count
    FROM import_record_error
   WHERE import_file_id = v_import_file_id;

  v_stage := 'Total error count';
  v_error_count := v_error_count + v_error_tbl_count;
   
  v_stage := 'Update import_file table with record counts';
  update import_file f
     set version = 1,
         status = 'stg',
         import_record_count = v_recs_in_file_cnt,
         import_record_error_count = v_error_count
   where import_file_id = v_import_file_id;

  utl_file.fclose(v_in_file_handler);
  
  v_stage := 'Write counts and ending to execution_log table';
  prc_execution_log_entry(v_process_name,v_release_level,'INFO','Count of records on file: '||v_recs_in_file_cnt||
                          '  Count of records in error: '||v_error_count,null);
  prc_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure completed for file '
                          ||p_file_name,null);
  COMMIT;
  
  
EXCEPTION
  WHEN exit_program_exception THEN
    p_out_returncode := 99;
    
    UPDATE import_file 
       SET version        = 1,
           status         = 'stg_fail'
     WHERE import_file_id = v_import_file_id;

    prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                            'File: '||p_file_name||' failed at Stage: '||v_stage||
                            ' --> '||v_execution_log_msg,null);
    COMMIT;

  WHEN OTHERS THEN
    UTL_FILE.Fclose(v_in_file_handler);

    SELECT COUNT(DISTINCT(import_record_id))
      INTO v_error_tbl_count
      FROM import_record_error
     WHERE import_file_id = v_import_file_id;

    v_error_count := v_error_count + v_error_tbl_count;

    UPDATE import_file f
        SET version                   = 1,
            status                    = 'stg_fail',
            import_record_count       = v_recs_in_file_cnt,
            import_record_error_count = v_error_count
      WHERE import_file_id = v_import_file_id;

    prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                            'File: '||p_file_name||' failed at Stage: '||v_stage||
                            ' --> '||SQLERRM,null);
    COMMIT;
    p_out_returncode := 99;
        
END  ;
/