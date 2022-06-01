CREATE OR REPLACE PROCEDURE prc_stage_nom_approver_load(p_file_name IN VARCHAR2,
                                                        p_out_returncode OUT NUMBER) IS
  /*******************************************************************************
   Purpose: BI Admin can load a file to load an approver for nomination promotions via file load

   Person         Date       Comments
   -----------    --------   -----------------------------------------------------
   nagarajs     04/15/2016   Initial Creation.
  *******************************************************************************/
  
  --CONSTANTS
  C_delimiter               CONSTANT  VARCHAR2(10):='|';
  C_process_name            CONSTANT  execution_log.process_name%type  := 'PRC_STAGE_NOM_APPROVER_LOAD';
  C_release_level           CONSTANT  execution_log.release_level%type := '1';

  --UTL_FILE
  v_in_file_handler         utl_file.file_type;
  v_text                    VARCHAR2(2000);
  v_file_name               VARCHAR2 (500);
  v_directory_path          VARCHAR2 (4000);
  v_directory_name          VARCHAR2 (30);

  --RECORD TYPE DECLARATION
  rec_nom_approvers         stage_nom_approvers_import%ROWTYPE;
  rec_import_record_error   import_record_error%ROWTYPE;

  --STAGE VARIBALES
  v_execution_log_msg       execution_log.text_line%TYPE;
  v_stage                   VARCHAR2(500);
  v_return_code             NUMBER(5); 
  v_import_record_id        stage_nom_approvers_import.import_record_id%TYPE;
  v_import_file_id          stage_nom_approvers_import.import_file_id%TYPE;
  v_created_by              stage_nom_approvers_import.created_by%TYPE := 0;
  v_approval_round          stage_nom_approvers_import.approval_round%TYPE;
  v_user_id                 application_user.user_id%TYPE;
  v_is_active               application_user.is_active%TYPE;
  
  --COUNTS
  v_recs_in_file_cnt        INTEGER := 0;
  v_field_cnt               INTEGER := 0;
  v_recs_loaded_cnt         INTEGER := 0;
  v_error_count             PLS_INTEGER := 0;
  v_error_tbl_count         PLS_INTEGER := 0;  
  v_count                   NUMBER := 0; 

  -- EXCEPTIONS
  exit_program_exception    EXCEPTION;

BEGIN
  v_stage := 'Write start to execution_log table';
  p_out_returncode := 0;
  prc_execution_log_entry(C_process_name,C_release_level,'INFO','Procedure Started for file '
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
       'nomapprover',
       0,
       0,
       'stg_in_process',
       v_created_by,
       SYSDATE);
    COMMIT;
    rec_import_record_error.import_file_id := v_import_file_id;
  
  EXCEPTION
    WHEN OTHERS THEN
       prc_execution_log_entry(C_process_name,C_release_level,'ERROR',
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
    v_field_cnt     := 0;
    v_user_id       := NULL;
    
    BEGIN
        v_stage := 'Get record';
        utl_file.get_line(v_in_file_handler, v_text);

        SELECT import_record_pk_sq.NEXTVAL
          INTO v_import_record_id
          FROM dual;
        rec_import_record_error.import_record_id := v_import_record_id;
              
        v_recs_in_file_cnt := v_recs_in_file_cnt + 1;
        v_stage := 'Parsing record number: '||v_recs_in_file_cnt;
       
        v_field_cnt := v_field_cnt + 1;
        rec_nom_approvers.approval_type   := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text, 1, c_delimiter))),CHR(13));

        v_field_cnt := v_field_cnt + 1;
        rec_nom_approvers.min_value    := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text, 2, c_delimiter))),CHR(13));
        
        v_field_cnt := v_field_cnt + 1;
        rec_nom_approvers.max_value    := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text, 3, c_delimiter))),CHR(13));
        
        v_field_cnt := v_field_cnt + 1;
        rec_nom_approvers.user_name       := UPPER(REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text, 4, c_delimiter))),CHR(13)));
        
        v_field_cnt := v_field_cnt + 1;
        rec_nom_approvers.approval_round  := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text, 5, c_delimiter))),CHR(13));

        IF rec_nom_approvers.user_name IS NULL THEN
           rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
           rec_import_record_error.param1 := 'User Name';
           rec_import_record_error.created_by := v_created_by;
           prc_insert_import_record_error (rec_import_record_error);
        END IF;

        IF rec_nom_approvers.approval_type IS NULL THEN
           rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
           rec_import_record_error.param1 := 'Approval Type';
           rec_import_record_error.created_by := v_created_by;
           prc_insert_import_record_error (rec_import_record_error);
        END IF;

        IF rec_nom_approvers.min_value IS NULL THEN
           rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
           rec_import_record_error.param1 := 'Min Value';
           rec_import_record_error.created_by := v_created_by;
           prc_insert_import_record_error (rec_import_record_error);
        END IF;
        
--        IF rec_nom_approvers.max_value IS NULL THEN
--           rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
--           rec_import_record_error.param1 := 'Max Value';
--           rec_import_record_error.created_by := v_created_by;
--           prc_insert_import_record_error (rec_import_record_error);
--        END IF;
        
        IF rec_nom_approvers.approval_round IS NULL THEN
           rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
           rec_import_record_error.param1 := 'Approval Round';
           rec_import_record_error.created_by := v_created_by;
           prc_insert_import_record_error (rec_import_record_error);
        END IF;
        
        IF rec_nom_approvers.user_name IS NOT NULL THEN
        
           BEGIN
           
             SELECT user_id, is_active
               INTO v_user_id, v_is_active
               FROM application_user
              WHERE UPPER(user_name) = rec_nom_approvers.user_name;
              
              IF v_is_active = 0 THEN  
                rec_import_record_error.item_key := 'admin.fileload.errors.INACTIVE_USER';
                rec_import_record_error.param1   := 'User Name - '||rec_nom_approvers.user_name;
                rec_import_record_error.created_by   := v_created_by;
                prc_insert_import_record_error(rec_import_record_error);
              END IF;
              
           EXCEPTION
             WHEN NO_DATA_FOUND THEN
               rec_import_record_error.item_key := 'admin.fileload.errors.USER_NOT_VALID';
               rec_import_record_error.param1   := 'User Name - '||rec_nom_approvers.user_name;
               rec_import_record_error.created_by   := v_created_by;
               prc_insert_import_record_error(rec_import_record_error);
               v_user_id := NULL;
           END;    

        END IF;

        -- Approval Round is numeric
        BEGIN
           v_approval_round := TO_NUMBER(rec_nom_approvers.approval_round);
        EXCEPTION
           WHEN VALUE_ERROR THEN
              rec_import_record_error.item_key := 'user.characteristic.errors.INTEGER';
              rec_import_record_error.param1 := 'Approval Round - '||rec_nom_approvers.approval_round;
              rec_import_record_error.created_by := v_created_by;
              prc_insert_import_record_error(rec_import_record_error);
        END;
        
        v_stage := 'Initialize the record type';
        -- Initialize the import record error values
        rec_import_record_error.item_key := NULL;
        rec_import_record_error.param1   := NULL;
        rec_import_record_error.param2   := NULL;

        v_stage := 'Assign the values';      
        rec_nom_approvers.user_id          := v_user_id;
        rec_nom_approvers.import_record_id := v_import_record_id;
        rec_nom_approvers.import_file_id   := v_import_file_id;
        rec_nom_approvers.created_by       := v_created_by;
        rec_nom_approvers.date_created     := SYSDATE;
        
        --Insert into stage_nom_approvers_import
        v_stage := 'Insert into stage_nom_approvers_import table';
        prc_stg_nom_approver_insert(rec_nom_approvers, v_return_code);
        IF v_return_code = 99 THEN
          v_error_count := v_error_count + 1;  
        ELSE
          v_count := v_count + 1;
          v_recs_loaded_cnt := v_recs_loaded_cnt + 1;
        END IF;
            
        IF MOD(v_count, 10) = 0 THEN
          COMMIT;
        END IF;
    
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        utl_file.fclose(v_in_file_handler);
        EXIT;
      WHEN OTHERS THEN
        IF v_stage LIKE 'Parsing%' THEN
          v_stage := v_stage||' field number: '||v_field_cnt;
          
          rec_import_record_error.item_key := 'admin.fileload.errors.PARSING';
          rec_import_record_error.param1   := v_recs_in_file_cnt;
          rec_import_record_error.param2   := v_field_cnt;
          rec_import_record_error.created_by   := v_created_by;
          prc_insert_import_record_error(rec_import_record_error);
        ELSE
          prc_execution_log_entry(C_process_name,C_release_level,'ERROR',
                                'Stage: '||v_stage||' --> '||SQLERRM,null);
        END IF;

        COMMIT;

        v_error_count := v_error_count + 1;  
    END;
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
     SET version                   = 1,
         status                    = 'stg',
         import_record_count       = v_recs_in_file_cnt,
         import_record_error_count = v_error_count
   WHERE import_file_id = v_import_file_id;

  utl_file.fclose(v_in_file_handler);

  v_stage := 'Write counts and ending to execution_log table';
  prc_execution_log_entry(C_process_name,C_release_level,'INFO','Count of records on file: '||v_recs_in_file_cnt||
                          '  Count of records in error: '||v_error_count,null);
  prc_execution_log_entry(C_process_name,C_release_level,'INFO','Procedure completed for file '
                          ||p_file_name,null);
  COMMIT;
EXCEPTION
    WHEN exit_program_exception THEN
      p_out_returncode := 99;

      UPDATE import_file 
         SET version        = 1,
             status         = 'stg_fail'
       WHERE import_file_id = v_import_file_id;

      prc_execution_log_entry(C_process_name,C_release_level,'ERROR',
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

    prc_execution_log_entry(C_process_name,C_release_level,'ERROR',
                            'File: '||p_file_name||' failed at Stage: '||v_stage||
                            ' --> '||SQLERRM,null);
    COMMIT;
    p_out_returncode := 99;
    
END;
/
