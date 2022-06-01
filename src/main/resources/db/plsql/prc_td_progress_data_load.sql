CREATE OR REPLACE PROCEDURE prc_td_progress_data_load(p_file_name IN VARCHAR2, p_out_returncode OUT NUMBER) IS

  /*******************************************************************************
  -- Purpose: To load the progress data from file to stage table for Throwdown
  --
  -- Person        Date       Comments
  -- -----------   --------   -----------------------------------------------------
  -- Chidamba         10/11/2013  Initial Creation
  -- Ravi Dhanekula   11/04/2013  Fixed defect # 49722  
  -- Chidamba         11/12/2013  Defect# 49577 
  *******************************************************************************/
  
  c_delimiter                   CONSTANT  VARCHAR2(10):='|';
  c_created_by                  CONSTANT  stage_td_progress_data_import.created_by%TYPE := 0;
  c_date_created                CONSTANT  stage_td_progress_data_import.date_created%TYPE := sysdate;
  
  --UTL_FILE
  v_in_file_handler             utl_file.file_type;  
  v_text                        VARCHAR2(500);
  v_file_name                   VARCHAR2(500); 
  v_directory_path              VARCHAR2(4000);
  v_directory_name              VARCHAR2(30);

  -- RECORD TYPE DECLARATION
  rec_stg_td_progress_data       stage_td_progress_data_import%ROWTYPE;
  rec_import_file                import_file%ROWTYPE;
  rec_import_record_error        import_record_error%ROWTYPE;

  -- LOAD Variables
  v_import_record_id            stage_td_progress_data_import.import_record_id%TYPE;
  v_import_file_id              stage_td_progress_data_import.import_file_id%TYPE;
  v_import_record_error_id      import_record_error.import_record_error_id%type;

  v_login_id                    stage_td_progress_data_import.login_id%TYPE;
  v_user_id                     stage_td_progress_data_import.user_id%TYPE;
  v_first_name                  stage_td_progress_data_import.first_name%TYPE;
  v_last_name                   stage_td_progress_data_import.last_name%TYPE;
  v_email_addr                  stage_td_progress_data_import.email_address%TYPE;
  v_total_perf_to_date          stage_td_progress_data_import.total_performance%TYPE;
  v_str_total_perf_to_date      VARCHAR2(100);

  -- EXECUTION LOG VARIABLES
  v_process_name                execution_log.process_name%type  := 'PRC_TD_PROGRESS_DATA_LOAD' ;
  v_release_level               execution_log.release_level%type := '1';
  v_execution_log_msg           execution_log.text_line%TYPE;
  v_count                       NUMBER                := 0;
  v_stage                       VARCHAR2(500);
  v_return_code                 NUMBER(5);        
  v_header_rec                  BOOLEAN               := FALSE;
  
  -- COUNTS
  v_recs_in_file_cnt            INTEGER := 0;
  v_field_cnt                   INTEGER := 0;
  v_recs_loaded_cnt             INTEGER := 0;
  v_error_count                 PLS_INTEGER := 0;
  v_error_tbl_count             PLS_INTEGER := 0;

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
       SUBSTR(v_file_name, 1, instr(v_file_name, '.') - 1) || '_' || v_import_file_id, 
       'tdprog',
       0,
       0,
       'stg_in_process',
       SYSDATE,
       c_created_by ,
       1);
       
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
    --*** move records from text file into the table stage_td_progress_data_import_rec.**
    v_stage := 'Reset variables';
    v_field_cnt := 0;
    rec_stg_td_progress_data := NULL;  
    BEGIN
      v_stage := 'Get record';
      utl_file.get_line(v_in_file_handler, v_text);
      
      IF v_header_rec THEN
        v_header_rec := FALSE;
      ELSE
        v_recs_in_file_cnt := v_recs_in_file_cnt + 1;
        v_stage := 'Parsing record number: '||v_recs_in_file_cnt;

        v_field_cnt := v_field_cnt + 1;
        rec_stg_td_progress_data.login_id :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 1, c_delimiter)),chr(13));

        v_field_cnt := v_field_cnt + 1;
        rec_stg_td_progress_data.first_name :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 2, c_delimiter)),chr(13));
      
        v_field_cnt := v_field_cnt + 1;
        rec_stg_td_progress_data.last_name :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 3, c_delimiter)),chr(13));

        v_field_cnt := v_field_cnt + 1;
        rec_stg_td_progress_data.email_address :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 4, c_delimiter)),chr(13));

        v_field_cnt := v_field_cnt + 1;
        rec_stg_td_progress_data.total_performance :=
             REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 5, c_delimiter)),chr(13));
        
        v_stage := 'Validations';
        SELECT import_record_pk_sq.NEXTVAL
          INTO rec_stg_td_progress_data.import_record_id
          FROM dual;      

        v_stage := 'Check for required columns';
        rec_import_record_error.date_created     := sysdate;
        rec_import_record_error.created_by       := c_created_by;
        rec_import_record_error.import_record_id := rec_stg_td_progress_data.import_record_id;
        rec_import_record_error.import_file_id   := v_import_file_id;
        rec_stg_td_progress_data.import_file_id  := v_import_file_id;

        IF rtrim(rec_stg_td_progress_data.login_id) IS NULL THEN
            rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
            rec_import_record_error.param1   := 'User Name';
            rec_import_record_error.param2   := rec_stg_td_progress_data.login_id;              
            prc_insert_import_record_error(rec_import_record_error);
        ELSE 
            BEGIN
              SELECT user_id
                INTO rec_stg_td_progress_data.user_id
                FROM application_user 
               WHERE lower(user_name) = lower(rec_stg_td_progress_data.login_id);
            EXCEPTION when others then
              rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
              rec_import_record_error.param1   := 'User Name';
              rec_import_record_error.param2   := rec_stg_td_progress_data.login_id;   --11/12/2013 --bug fix 49577 v_login_id               
              prc_insert_import_record_error(rec_import_record_error);                
            
            END;
        END IF; -- v_user_name is null

        IF NVL(LENGTH(rec_stg_td_progress_data.total_performance), 0) = 0 THEN
            rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
            rec_import_record_error.param1   := 'progress level';
            rec_import_record_error.param2   :=  NULL;            
            prc_insert_import_record_error(rec_import_record_error);
        ELSE
            BEGIN
                rec_stg_td_progress_data.total_performance := TO_NUMBER(TRIM(rec_stg_td_progress_data.total_performance));
            EXCEPTION when others then
              rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
              rec_import_record_error.param1   := 'progress level';
              rec_import_record_error.param2   := rec_stg_td_progress_data.total_performance;            
              prc_insert_import_record_error(rec_import_record_error);            
            END;
        END IF;

        --Insert into stage_td_progress_data_import_rec
        v_stage := 'Insert into stage_td_progress_data_import table';
        INSERT INTO stage_td_progress_data_import(import_record_id,
                                                  import_file_id,
                                                  login_id,
                                                  user_id,
                                                  first_name,
                                                  last_name,
                                                  email_address,
                                                  total_performance,
                                                  created_by,
                                                  date_created)
                                           VALUES(rec_stg_td_progress_data.import_record_id,
                                                  rec_stg_td_progress_data.import_file_id,
                                                  rec_stg_td_progress_data.login_id,
                                                  rec_stg_td_progress_data.user_id,
                                                  rec_stg_td_progress_data.first_name,
                                                  rec_stg_td_progress_data.last_name,
                                                  rec_stg_td_progress_data.email_address,
                                                  rec_stg_td_progress_data.total_performance,
                                                  c_created_by,
                                                  SYSDATE);
        
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
  END LOOP;
  COMMIT;

  v_stage := 'Count distinct records that have an error';
  SELECT COUNT(DISTINCT(import_record_id))
    INTO v_error_tbl_count
    FROM import_record_error
   WHERE import_file_id = v_import_file_id;

  v_stage := 'Total error count';
  v_error_count := v_error_count + v_error_tbl_count;
   
  v_stage := 'Update import_file table with record counts';
  UPDATE import_file f
     SET version = 1,
         status  = 'stg',
         import_record_count = v_recs_in_file_cnt,
         import_record_error_count = v_error_count
   WHERE import_file_id      = v_import_file_id;

  utl_file.fclose(v_in_file_handler);
  
  v_stage := 'Write counts and ending to execution_log table';
  prc_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure completed for file '||p_file_name||CHR(10)||
                          ' Count of records on file: '||v_recs_in_file_cnt||CHR(10)||
                          ' Count of records in error: '||v_error_count,null);
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