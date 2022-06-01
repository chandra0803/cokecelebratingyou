CREATE OR REPLACE PROCEDURE prc_stage_leaderboard_load(p_file_name IN VARCHAR2,p_out_returncode OUT NUMBER) IS
  /*******************************************************************************
  -- Purpose: BI Admin can load a file to stage and verify list of users and their leaderboard scores

  -- 
  -- Person        Date       Comments 
  -- -----------   --------   -----------------------------------------------------
  -- Dhanekul    07/05/2012  Initial Creation.
  -- Dhanekul    09/04/2012  Fixed the defect # 56

  *******************************************************************************/
  
  --MISCELLANIOUS
  c_delimiter               CONSTANT  VARCHAR2(10):='|';
  v_count                   NUMBER := 0; --for commit count
  v_stage                   VARCHAR2(500);
  v_return_code             NUMBER(5);  -- return from insert call
  v_header_rec              BOOLEAN := FALSE ;
  v_nonpax_ind              NUMBER := 0;
  v_user_id                 application_user.user_id%TYPE;


  --UTL_FILE
  v_in_file_handler         utl_file.file_type;

  v_text                    varchar2(2000);
  v_file_name        VARCHAR2 (500);
  v_directory_path   VARCHAR2 (4000);
  v_directory_name   VARCHAR2 (30);

  -- RECORD TYPE DECLARATION
  rec_leaderboard_score           stage_leaderboard_load%ROWTYPE;
  rec_import_record_error   import_record_error%ROWTYPE;

 --LEADERBOARD SCORE LOAD
   v_user_name               stage_leaderboard_load.user_name%TYPE;
  v_import_record_id        stage_leaderboard_load.import_record_id%TYPE;
  v_import_file_id          stage_leaderboard_load.import_file_id%TYPE;
  v_score             number(18);
    v_valid_score             number(18);
  v_created_by              stage_leaderboard_load.created_by%TYPE := 0;
  

  -- EXECUTION LOG VARIABLES
  v_process_name            execution_log.process_name%type  := 'prc_stage_leaderboard_load';
  v_release_level           execution_log.release_level%type := '1';
  v_execution_log_msg     execution_log.text_line%TYPE;
  
  -- COUNTS
  v_recs_in_file_cnt        INTEGER := 0;
  v_field_cnt               INTEGER := 0;
  v_recs_loaded_cnt         INTEGER := 0;
  v_error_count             PLS_INTEGER := 0;
  v_error_tbl_count         PLS_INTEGER := 0;

   -- EXCEPTIONS
  exit_program_exception   EXCEPTION;

BEGIN
  v_stage := 'Write start to execution_log table';
  p_out_returncode := 0;
  prc_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure Started for file '
                          ||p_file_name,null);
  COMMIT;

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
       staged_by,
       date_staged)
    VALUES
      (v_import_file_id,
       substr(v_file_name, 1, instr(v_file_name, '.') - 1) || '_' ||v_import_file_id,
       'leaderboard',
       0,
       0,
       'stg_in_process',
       v_created_by,
       SYSDATE);
    commit;
    rec_import_record_error.import_file_id := v_import_file_id;
  
  EXCEPTION
    WHEN OTHERS THEN
       prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                               'Stage: '||v_stage||' -- '||SQLERRM,null);
  END;

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
    --*** move records from text file into the table stage_pax_import_record.**
    v_stage := 'Reset variables';
    v_field_cnt := 0;
    v_user_name := NULL;
    v_score := NULL;
   

    BEGIN
      v_stage := 'Get record';
      utl_file.get_line(v_in_file_handler, v_text);

      IF v_header_rec THEN
        v_header_rec := FALSE;
      ELSE
      
        SELECT import_record_pk_sq.NEXTVAL
          INTO v_import_record_id
          FROM dual;
        rec_import_record_error.import_record_id := v_import_record_id;
              
        v_recs_in_file_cnt := v_recs_in_file_cnt + 1;
        v_stage := 'Parsing record number: '||v_recs_in_file_cnt;

        v_field_cnt := v_field_cnt + 1;
        v_user_name         := UPPER(REPLACE(TRIM(fnc_pipe_parse(v_text, 1, c_delimiter)),CHR(13)));

        v_field_cnt := v_field_cnt + 1;
        v_score       := REPLACE(TRIM(fnc_pipe_parse(v_text, 2, c_delimiter)),CHR(13));

        v_stage := 'Participant Validations';
        
    -- User_name and Score are Required
        IF LTRIM (RTRIM (v_user_name)) IS NULL THEN
           rec_import_record_error.item_key := 'admin.fileload.errors.USER_NOT_VALID';
           rec_import_record_error.param1 := 'User Name';
           rec_import_record_error.created_by := v_created_by;
           prc_insert_import_record_error (rec_import_record_error);
        END IF;

        IF LTRIM (RTRIM (v_score)) IS NULL THEN
           rec_import_record_error.item_key := 'admin.fileload.errors.SCORE_IS_INVALID';
           rec_import_record_error.param1 := 'Leaderboard score';
           rec_import_record_error.created_by := v_created_by;
           prc_insert_import_record_error (rec_import_record_error);
        END IF;
 
    -- Login id for participant (not non-pax) is valid and participant is active status.
        IF (v_user_name IS NOT NULL) THEN
           BEGIN
             SELECT user_id
               INTO v_user_id
               FROM application_user
              WHERE upper(user_name) = upper(v_user_name);            
           EXCEPTION
             WHEN NO_DATA_FOUND THEN
               rec_import_record_error.item_key := 'admin.fileload.errors.USER_NOT_VALID';
               rec_import_record_error.param1   := 'User Name - '||v_user_name;
               rec_import_record_error.created_by   := v_created_by;
               prc_insert_import_record_error(rec_import_record_error);
               v_user_id := NULL;
           END; --  check if user exists
        END IF;
        
         IF (v_user_id IS NOT NULL) THEN
           BEGIN
             SELECT user_id
               INTO v_user_id
               FROM application_user
              WHERE user_id = v_user_id
              AND  is_active = 1;            
           EXCEPTION
             WHEN NO_DATA_FOUND THEN
               rec_import_record_error.item_key := 'admin.fileload.errors.USER_NOT_ACTIVE';
               rec_import_record_error.param1   := 'User Name - '||v_user_name;
               rec_import_record_error.created_by   := v_created_by;
               prc_insert_import_record_error(rec_import_record_error);
               v_user_id := NULL;
           END; --  check if user is Active
        END IF;

        -- Score is numeric
        BEGIN
           v_valid_score := TO_NUMBER(v_score);
           IF MOD(v_valid_score, 1) <> 0 THEN
              rec_import_record_error.item_key := 'user.characteristic.errors.INTEGER';
              rec_import_record_error.param1 := 'Score';
              rec_import_record_error.created_by := v_created_by;
              prc_insert_import_record_error(rec_import_record_error);
           END IF;
        EXCEPTION
           WHEN VALUE_ERROR THEN
              rec_import_record_error.item_key := 'user.characteristic.errors.INTEGER';
              rec_import_record_error.param1 := 'Score';
              rec_import_record_error.created_by := v_created_by;
              prc_insert_import_record_error(rec_import_record_error);
        END;
        
             
        -- Initialize the import record error values
    rec_import_record_error.item_key := NULL;
        rec_import_record_error.param1   := NULL;
        rec_import_record_error.param2   := NULL;

        v_stage := 'Initialize the record type';
        rec_leaderboard_score.import_record_id := v_import_record_id;
        rec_leaderboard_score.import_file_id   := v_import_file_id;
        rec_leaderboard_score.user_name             := v_user_name;
        rec_leaderboard_score.user_id             := v_user_id;
        rec_leaderboard_score.score           := v_valid_score;
        rec_leaderboard_score.leaderboard_name           := null;
        rec_leaderboard_score.as_of_date           := null;
        rec_leaderboard_score.add_or_replace           := null;
        rec_leaderboard_score.is_deleted           := null;
        rec_leaderboard_score.date_modified           := null;
        rec_leaderboard_score.modified_by           := null;
        rec_leaderboard_score.created_by            := v_created_by;
        rec_leaderboard_score.date_created          := SYSDATE;
        rec_leaderboard_score.version            := 1;
          
               
        --Insert into stage_leaderboard_load
        v_stage := 'Insert into stage_leaderboard_load table';
        prc_stg_leaderboard_insert(rec_leaderboard_score, v_return_code);
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
          
          rec_import_record_error.item_key := 'admin.fileload.errors.PARSING';
          rec_import_record_error.param1   := v_recs_in_file_cnt;
          rec_import_record_error.param2   := v_field_cnt;
          rec_import_record_error.created_by   := v_created_by;
          prc_insert_import_record_error(rec_import_record_error);
        ELSE
          prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                                'Stage: '||v_stage||' --> '||SQLERRM,null);
        END IF;

        COMMIT;

        v_error_count := v_error_count + 1;  -- need to count any parsing errors
    END;
  END LOOP; -- *** end of loop to move data to stage_leaderboard_load table. ***
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
     set version                   = 1,
         status                    = 'stg',
         import_record_count       = v_recs_in_file_cnt,
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
    
END; -- Procedure prc_stage_leaderboard_load
/