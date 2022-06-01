CREATE OR REPLACE PROCEDURE prc_budget_load(p_file_name IN VARCHAR2,p_out_returncode OUT NUMBER) is
  /*******************************************************************************
  -- Purpose: To process the Budget records from the file and populate the
  --          STAGE_BUDGET_IMPORT_RECORD with transactional data. Basically resolve 
  --          or validate the relational dependencies and mark the records
  --          for Insert
  --
  -- Person        Date       Comments
  -- -----------   --------   -----------------------------------------------------
  -- Raju N        09/23/2005 Creation
  -- Raju N        01/01/2006 Exectuion log entry changes
  -- Rachel R.        01/17/2006 Changed all references of execution_log_entry to
  --                                 prc_execution_log_entry
  -- Rachel R.        01/17/2006 Changed all references of p_insert_import_record_error
  --                                 to prc_insert_import_record_error
  -- Rachel R.        01/17/2006 Changed all references of p_insert_stg_budget
  --                                 to prc_insert_stg_budget
  -- Rachel R.        01/17/2006 Changed all references of unix_call
  --                                 to prc_unix_call
  -- Arun S        01/20/2006 Added the output parameter so that the procedure can be called from Script generator
  -- Percy M       01/26/2006 Changed from csv to pipe delimited
  -- Percy M       01/31/2006 Commented out call to movefile script
  -- D Murray      05/01/2006 Updated import_file table to stg_fail if file load fails (Bug 12020).
  --                          Removed all move file code - 9i can use prc_move_load_file_9i procedure.
  --                          Moved recs_in_file_cnt before parsing, changed call to prc_insert_stg_product
  --                          to have a return code, only counting recs_loaded_cnt if insert successful,
  --                          added v_error_count to count any records that fail during parsing or during
  --                          insert to stage table, changed dbms_outputs to execution_log entries. (Bug 12148)
  --                          Changed v_stage to have better messaging and added v_field_cnt for parsing errors.
  -- Arun S        01/07/2009 Changes to replace UC4  
  --Ravi Dhanekula 11/06/2014 Bug # 51706  Budget Load assigning node/user info before the process knows which budget master it belongs to
  --Ravi Dhanekula 11/19/2014 Bug # 51706 As a part of re-writing budget file load process in Oracle, Node/user assignment has been moved to Verify Step.
  *******************************************************************************/
  --MISCELLANIOUS
  c_delimiter        CONSTANT  VARCHAR2(10):='|';
  v_count            NUMBER := 0; --for commit count
  v_stage            VARCHAR2(500);
  v_return_code      NUMBER(5);  -- return from insert call
  v_header_rec       BOOLEAN := FALSE ;

  --UTL_FILE
  v_in_file_handler       utl_file.file_type;
  --v_path_name        os_propertyset.string_val%TYPE :=
  --                        fnc_get_system_variable('import.file.utl_path','db_utl_path');
  v_text                  VARCHAR2(500);
  v_file_name             VARCHAR2 (500);
  v_directory_path        VARCHAR2 (4000);
  v_directory_name        VARCHAR2 (30);

  -- RECORD TYPE DECLARATION
  rec_stg_budget          stage_budget_import_record%ROWTYPE;
  rec_import_record_error import_record_error%ROWTYPE;

  --BUDGET LOAD
  v_import_record_id      stage_budget_import_record.import_record_id%TYPE;
  v_import_file_id        stage_budget_import_record.import_file_id%TYPE;
  v_action_type           stage_budget_import_record.action_type%TYPE;

  v_budget_owner          stage_budget_import_record.budget_owner%TYPE;
  v_user_id               stage_budget_import_record.user_id%TYPE;
  v_node_id               stage_budget_import_record.node_id%TYPE;
  v_str_user_id           VARCHAR2(20); --  stage_budget_import_record.budget_owner%TYPE;
  v_str_node_id           VARCHAR2(20); --  stage_budget_import_record.budget_owner%TYPE;
  v_budget_amount         stage_budget_import_record.budget_amount%TYPE;
  v_str_budget_amt        VARCHAR2(20);
  v_budget_type        budget_master.budget_type%TYPE;--11/06/2014
  v_str_budget_type  VARCHAR2(20);--11/06/2014

  v_created_by            stage_pax_import_record.created_by%TYPE := 0;
  v_date_created          stage_pax_import_record.date_created%TYPE := sysdate;

  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'PRC_BUDGET_LOAD' ;
  v_release_level         execution_log.release_level%type := '1';
  v_execution_log_msg     execution_log.text_line%TYPE;
   
  -- COUNTS
  v_recs_in_file_cnt      INTEGER := 0;
  v_field_cnt             INTEGER := 0;
  v_recs_loaded_cnt       INTEGER := 0;
  v_error_count           PLS_INTEGER := 0;
  v_error_tbl_count       PLS_INTEGER := 0;

  -- EXCEPTIONS
  exit_program_exception  EXCEPTION;


--  -- forward declaration of functions --11/19/2014 *****************
--  FUNCTION F_GET_USER_ID (p_in_user_name IN VARCHAR2 ) return VARCHAR2  IS
--    v_user_nbr VARCHAR2(18) := 0 ;
--   BEGIN
--      SELECT user_id
--        INTO v_user_nbr
--        FROM application_user
--       WHERE upper(user_name) = ltrim(rtrim(p_in_user_name)) ;
--       return v_user_nbr ;
--   EXCEPTION
--      WHEN no_data_found THEN
--         v_user_nbr := NULL ;
--         return v_user_nbr ;
--      WHEN others THEN
--         v_user_nbr := 'X' ;
--         return v_user_nbr ;
--   END  ;
--
--  FUNCTION F_GET_node_ID (p_in_node_name IN VARCHAR2 ) return VARCHAR2  IS
--    v_node_nbr VARCHAR2(18) := 0 ;
--   BEGIN
--      SELECT node_id
--        INTO v_node_nbr
--        FROM node
--       WHERE upper(name) = ltrim(rtrim(p_in_node_name))
--         AND hierarchy_id = (SELECT hierarchy_id FROM HIERARCHY WHERE is_primary = 1) ;
--       return v_node_nbr ;
--   EXCEPTION
--      WHEN no_data_found THEN
--         v_node_nbr := NULL ;
--         return v_node_nbr ;
--      WHEN others THEN
--         v_node_nbr := 'X' ;
--         return v_node_nbr ;
--   END  ;


BEGIN
  v_stage := 'Write start to execution_log table';
  p_out_returncode := 0;
  prc_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure Started for file '
                          ||p_file_name,null);
  COMMIT;

  v_stage := 'Get Directory path and File name from p_file_name';               --01/07/2009 Changes to replace UC4 
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
       'bud',
       0,
       0,
       'stg_in_process',
       SYSDATE,
       v_created_by,
       1);
  EXCEPTION
    WHEN OTHERS THEN
       prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                               'Stage: '||v_stage||' -- '||SQLERRM,null);
  END;

  BEGIN
    v_stage := 'Get Directory name';                                            --01/07/2009 Changes to replace UC4
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
    --*** move records from text file into the table stage_budget_import_record.**
    v_stage := 'Reset variables';
    v_field_cnt := 0;
    v_user_id := NULL;
    v_node_id := NULL;
    
    BEGIN
      v_stage := 'Get record';
      utl_file.get_line(v_in_file_handler, v_text);

      IF v_header_rec THEN
        v_header_rec := FALSE;
      ELSE
        v_recs_in_file_cnt := v_recs_in_file_cnt + 1;
        v_stage := 'Parsing record number: '||v_recs_in_file_cnt;

        v_field_cnt := v_field_cnt + 1;
        v_budget_owner :=
               replace(ltrim(rtrim(fnc_pipe_parse(v_text,1,c_delimiter))),chr(13));

        v_field_cnt := v_field_cnt + 1;
        v_str_budget_amt :=
               replace(ltrim(rtrim(fnc_pipe_parse(v_text,2,c_delimiter))),chr(13));
               
         v_field_cnt := v_field_cnt + 1; --11/06/2014
        v_str_budget_type :=
               replace(ltrim(rtrim(fnc_pipe_parse(v_text,3,c_delimiter))),chr(13));

      

        v_stage := 'Validations';
        SELECT import_record_pk_sq.NEXTVAL
          INTO v_import_record_id
          FROM dual;

      
        v_stage := 'Check for required columns';
        rec_import_record_error.date_created     := sysdate;
        rec_import_record_error.created_by       := v_created_by;
        rec_import_record_error.import_record_id := v_import_record_id;
        rec_import_record_error.import_file_id   := v_import_file_id;

        IF ltrim(rtrim(v_budget_owner)) IS NULL THEN
            rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
            rec_import_record_error.param1   := 'User Name';
            rec_import_record_error.created_by   := v_created_by ;
            prc_insert_import_record_error(rec_import_record_error);
        ELSIF ltrim(rtrim(v_str_budget_amt)) IS NULL THEN
          rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
          rec_import_record_error.param1   := 'Budget Amount';
          rec_import_record_error.created_by   := v_created_by ;
          prc_insert_import_record_error(rec_import_record_error);
          v_budget_amount := NULL;       
        END IF; -- v_budget_owner is null

      v_stage := 'Check for numerics';
        BEGIN
          v_budget_amount := TO_NUMBER(v_str_budget_amt);
        EXCEPTION
          WHEN OTHERS THEN
            rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
            rec_import_record_error.param1   := 'Budget Amount';
            rec_import_record_error.param2   := v_str_budget_amt;
            rec_import_record_error.created_by   := v_created_by ;
            prc_insert_import_record_error(rec_import_record_error);
            v_budget_amount := NULL;
        END;

        
        v_action_type := 'add';
        v_stage := 'Initialize the record type';
        rec_stg_budget.import_record_id := v_import_record_id;
        rec_stg_budget.import_file_id   := v_import_file_id;
        rec_stg_budget.action_type      := v_action_type;
        rec_stg_budget.node_id          := v_node_id;
        rec_stg_budget.user_id          := v_user_id;
        rec_stg_budget.budget_owner     := v_budget_owner;
        rec_stg_budget.budget_amount    := v_budget_amount;
        rec_stg_budget.created_by       := v_created_by;
        rec_stg_budget.date_created     := v_date_created;


        --Insert into stage_budget_import_record
        v_stage := 'Insert into stage_budget_import_record table';
        prc_insert_stg_budget(rec_stg_budget, v_return_code);
        IF v_return_code = 99 THEN
          v_error_count := v_error_count + 1;  -- need to count any insert errors
        ELSE
          v_count := v_count + 1;
          v_recs_loaded_cnt := v_recs_loaded_cnt + 1;
        END IF;

        IF MOD(v_count, 10) = 0 THEN
          COMMIT;
        END IF;
      END IF;
    EXCEPTION
      WHEN no_data_found THEN
        -- end of file
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
  END LOOP; -- *** end of loop to move data to stage_budget_import_record table. ***
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
      
END  prc_budget_load;
/