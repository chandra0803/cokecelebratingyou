CREATE OR REPLACE PROCEDURE prc_insert_stg_quiz
   ( p_in_rec_stage_quiz IN stage_quiz_import_record%ROWTYPE ,
     p_out_return_code OUT VARCHAR2)
   IS

/***************************************************************************
--
--
-- Purpose: 
--
-- MODIFICATION HISTORY
-- Person      Date          Comments
-- ---------   ----------    -------------------------------------------
-- Percy M     08/28/2006    Initial Creation
****************************************************************************/
  v_process_name          execution_log.process_name%type  := 'PRC_INSERT_STG_QUIZ';
  v_release_level         execution_log.release_level%type := '1';

BEGIN

     INSERT INTO stage_quiz_import_record
        (import_record_id,
         import_file_id   ,
         action_type      ,
         record_type      ,
         quiz_name        ,
         quiz_description  ,
         quiz_number_asked ,
         quiz_passing_score,
         quiz_type         ,
         quiz_status_type  ,
         question_status_type ,
         question_is_required ,
         question_text        ,
         answer_is_correct    ,
         answer_choice_text   ,
         answer_explanation_text ,
         created_by              ,
         date_created
         )
        VALUES
        (p_in_rec_stage_quiz.import_record_id,
         p_in_rec_stage_quiz.import_file_id   ,
         p_in_rec_stage_quiz.action_type      ,
         p_in_rec_stage_quiz.record_type      ,
         p_in_rec_stage_quiz.quiz_name        ,
         p_in_rec_stage_quiz.quiz_description  ,
         p_in_rec_stage_quiz.quiz_number_asked ,
         p_in_rec_stage_quiz.quiz_passing_score,
         p_in_rec_stage_quiz.quiz_type         ,
         p_in_rec_stage_quiz.quiz_status_type  ,
         p_in_rec_stage_quiz.question_status_type ,
         p_in_rec_stage_quiz.question_is_required ,
         p_in_rec_stage_quiz.question_text        ,
         p_in_rec_stage_quiz.answer_is_correct    ,
         p_in_rec_stage_quiz.answer_choice_text   ,
         p_in_rec_stage_quiz.answer_explanation_text ,
         p_in_rec_stage_quiz.created_by              ,
         p_in_rec_stage_quiz.date_created
        );
    

        p_out_return_code := 0;

EXCEPTION
    WHEN others THEN
      p_out_return_code := 99;
      prc_execution_log_entry(v_process_name,
                              v_release_level,
                              'ERROR',
                             'Import_file_id: '||p_in_rec_stage_quiz.import_file_id||
                             'Import_record_id: '||p_in_rec_stage_quiz.import_record_id||
                             ' --> '||SQLERRM,
                             null);
     COMMIT;
END;
/

CREATE OR REPLACE PROCEDURE prc_stage_quiz_load(p_file_name IN VARCHAR2,
                              p_out_returncode OUT NUMBER)
IS
  /*******************************************************************************
  -- Purpose: Imports quiz file extracted from Opinio.
  --
  -- Person        Date       Comments
  -- -----------   --------   -----------------------------------------------------
  -- Percy M       08/28/2006 Initial Creation
  -- Eikos         01/25/2007 Added the following enhancements to procedure. 
  --                          1. Strip off path from file name if present.
  --                          2. Verify required system variables are populated.
  --                          3. Enter a record into the import_record_error table for each column
  --                             within the text record that contains invalid data.
  --                             All text records are loaded into the stage_quiz_import_record
  --                             table even if it contains errors, the errors are captured
  --                             in the import_record_error table.
  -- Arun S        01/19/2009 Changes to replace UC4     
  *******************************************************************************/
  --CONSTANTS--
  c_delimiter        CONSTANT VARCHAR2(10) := '|';
  --c_path_name        CONSTANT os_propertyset.string_val%TYPE :=
  --                            fnc_get_system_variable('import.file.utl_path','db_utl_path');
  
  --EXCEPTIONS--
  termination_error  EXCEPTION;

  --PROGRAM VARIABLES
  v_stage            VARCHAR2(500);
  v_return_code      NUMBER(5);  -- return from insert call
  v_column_name      VARCHAR2(30); 
  v_column_value     VARCHAR2(2000);

  --UTL_FILE
  v_file_name        VARCHAR2(500); 
  v_in_file_handler  utl_file.file_type;
  v_text             VARCHAR2(500);
  v_directory_path   VARCHAR2(4000);
  v_directory_name   VARCHAR2(30);

  -- RECORD TYPE DECLARATION
  rec_stg_quiz            stage_quiz_import_record%ROWTYPE;
  rec_import_record_error import_record_error%ROWTYPE;

  --QUIZ LOAD
  v_import_record_id      stage_quiz_import_record.import_record_id%TYPE;
  v_import_file_id        stage_quiz_import_record.import_file_id%TYPE;
  
  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'PRC_STAGE_QUIZ_LOAD' ;
  v_release_level         execution_log.release_level%type := '1';
   
  -- COUNTS
  v_recs_in_file_cnt      INTEGER := 0;
  v_field_cnt             INTEGER := 0;
  v_field_err_cnt         INTEGER := 0;  
  v_recs_loaded_cnt       INTEGER := 0;
  v_error_count           PLS_INTEGER := 0;

BEGIN
  -- remove file path from file name if present --
  /*SELECT substr(p_file_name ,instr(p_file_name,'/',-1,1) + 1)
  INTO v_file_name
  FROM DUAL;
  */
  -- write execution log for procedure start --
  -- (uses file_name so remove path from name first) --   
  v_stage := 'Write start to execution_log table';
  p_out_returncode := 0;
  prc_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure Started for file '
                          ||p_file_name,null);
  COMMIT;

  --Changes to replace UC4, added on 01/19/2009
  v_stage := 'Get Directory path and File name from p_file_name'; 
  prc_get_directory_file_name ( p_file_name, v_directory_path, v_file_name );

  IF v_file_name is null THEN
     v_stage := ' File name cannot be NULL';
     RAISE termination_error;
  END IF;

  -- Verify all constants have been populated
  -- that require a system variable record.
  IF v_directory_path is null THEN
        v_stage := ' Required System Variable for path_name Missing';
        RAISE termination_error;
  END IF;

  --Insert record into import_file table for this file_name -- 
  BEGIN
    v_stage := 'Retrieve next sequence value for import_file_id ';
    SELECT IMPORT_FILE_PK_SQ.NEXTVAL
    INTO v_import_file_id
    FROM dual;
    
    v_stage := 'Insert into import_file table';
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
       'quiz',
       0,
       0,
       'stg_in_process',
       SYSDATE,
       0,
       1);
  EXCEPTION
    WHEN OTHERS THEN
       RAISE termination_error;
  END;   

  --Changes to replace UC4, added on 01/19/2009
  BEGIN
    v_stage := 'Get Directory name';                                 
    SELECT directory_name
      INTO v_directory_name
      FROM all_directories
     WHERE directory_path = v_directory_path
       AND ROWNUM = 1;   
  EXCEPTION
    WHEN OTHERS THEN
      RAISE termination_error;  
  END;
  
  v_stage := 'Open file for read: '||v_file_name;
  v_in_file_handler := utl_file.fopen(v_directory_name, v_file_name, 'r');

  LOOP
    --*** move records from text file into the table stage_quiz_import_record.**
    BEGIN
      v_stage := 'Get text file record';
      utl_file.get_line(v_in_file_handler, v_text);
    
      v_recs_in_file_cnt := v_recs_in_file_cnt + 1;
      v_stage            := 'Parsing record number: '||v_recs_in_file_cnt;

      v_stage := 'Retrieve next sequence value for import_record_id ';
      SELECT import_record_pk_sq.NEXTVAL
      INTO v_import_record_id
      FROM dual;
          
      v_field_cnt     := 1;
      v_field_err_cnt := 0;
      rec_stg_quiz    := null;

      -- Fields populated for all Record Types --
      rec_stg_quiz.record_type := replace(ltrim(rtrim(fnc_pipe_parse(v_text, 1, c_delimiter))),chr(13));
      rec_stg_quiz.import_record_id := v_import_record_id;
      rec_stg_quiz.import_file_id   := v_import_file_id;      
      rec_stg_quiz.action_type      := 'add';
      rec_stg_quiz.created_by       := 0;
      rec_stg_quiz.date_created     := sysdate;
      
      IF UPPER(rec_stg_quiz.record_type) = 'H' THEN --Header record
          BEGIN
            v_field_cnt            := v_field_cnt + 1;
            v_column_name          := 'quiz_name';
            v_column_value         := replace(ltrim(rtrim(fnc_pipe_parse(v_text, 2, c_delimiter))),chr(13));
            rec_stg_quiz.quiz_name := v_column_value;
          EXCEPTION
            WHEN OTHERS THEN
              rec_stg_quiz.quiz_name := null; -- Set to null in stage table, error rec holds original value
              v_field_err_cnt                          := v_field_err_cnt + 1;
              rec_import_record_error                  := NULL;
              rec_import_record_error.import_record_id := v_import_record_id;
              rec_import_record_error.import_file_id   := v_import_file_id;
              rec_import_record_error.item_key         := 'INVALID_VALUE '||sqlerrm;
              rec_import_record_error.param1           := v_column_name;
              rec_import_record_error.param2           := v_column_value;
              rec_import_record_error.date_created     := sysdate;
              rec_import_record_error.created_by       := 0;
              prc_insert_import_record_error(rec_import_record_error);
          END;

          BEGIN    
            v_field_cnt                   := v_field_cnt + 1;
            v_column_name                 := 'quiz_description';
            v_column_value                := replace(ltrim(rtrim(fnc_pipe_parse(v_text, 3, c_delimiter))),chr(13));
            rec_stg_quiz.quiz_description := v_column_value;
          EXCEPTION
            WHEN OTHERS THEN
              rec_stg_quiz.quiz_description := null; -- Set to null in stage table, error rec holds original value
              v_field_err_cnt                          := v_field_err_cnt + 1;
              rec_import_record_error                  := NULL;
              rec_import_record_error.import_record_id := v_import_record_id;
              rec_import_record_error.import_file_id   := v_import_file_id;
              rec_import_record_error.item_key         := 'INVALID_VALUE '||sqlerrm;
              rec_import_record_error.param1           := v_column_name;
              rec_import_record_error.param2           := v_column_value;
              rec_import_record_error.date_created     := sysdate;
              rec_import_record_error.created_by       := 0;
              prc_insert_import_record_error(rec_import_record_error);
          END;

          BEGIN    
            v_field_cnt                   := v_field_cnt + 1;
            v_column_name                 := 'quiz_type';
            v_column_value                := replace(ltrim(rtrim(fnc_pipe_parse(v_text, 4, c_delimiter))),chr(13));
            rec_stg_quiz.quiz_type        := v_column_value;
          EXCEPTION
            WHEN OTHERS THEN
              rec_stg_quiz.quiz_type := null; -- Set to null in stage table, error rec holds original value
              v_field_err_cnt                          := v_field_err_cnt + 1;
              rec_import_record_error                  := NULL;
              rec_import_record_error.import_record_id := v_import_record_id;
              rec_import_record_error.import_file_id   := v_import_file_id;
              rec_import_record_error.item_key         := 'INVALID_VALUE '||sqlerrm;
              rec_import_record_error.param1           := v_column_name;
              rec_import_record_error.param2           := v_column_value;
              rec_import_record_error.date_created     := sysdate;
              rec_import_record_error.created_by       := 0;
              prc_insert_import_record_error(rec_import_record_error);
          END;

          BEGIN    
            v_field_cnt                     := v_field_cnt + 1;
            v_column_name                   := 'quiz_passing_score';
            v_column_value                  := replace(ltrim(rtrim(fnc_pipe_parse(v_text, 5, c_delimiter))),chr(13));
            rec_stg_quiz.quiz_passing_score := to_number(v_column_value);
          EXCEPTION
            WHEN OTHERS THEN
              rec_stg_quiz.quiz_passing_score := null; -- Set to null in stage table, error rec holds original value
              v_field_err_cnt                          := v_field_err_cnt + 1;
              rec_import_record_error                  := NULL;
              rec_import_record_error.import_record_id := v_import_record_id;
              rec_import_record_error.import_file_id   := v_import_file_id;
              rec_import_record_error.item_key         := 'INVALID_VALUE '||sqlerrm;
              rec_import_record_error.param1           := v_column_name;
              rec_import_record_error.param2           := v_column_value;
              rec_import_record_error.date_created     := sysdate;
              rec_import_record_error.created_by       := 0;
              prc_insert_import_record_error(rec_import_record_error);
          END;

          BEGIN    
            v_field_cnt                    := v_field_cnt + 1;
            v_column_name                  := 'quiz_number_asked';
            v_column_value                 := replace(ltrim(rtrim(fnc_pipe_parse(v_text, 6, c_delimiter))),chr(13));
            rec_stg_quiz.quiz_number_asked := to_number(v_column_value);
          EXCEPTION
            WHEN OTHERS THEN
              rec_stg_quiz.quiz_number_asked := null; -- Set to null in stage table, error rec holds original value
              v_field_err_cnt                          := v_field_err_cnt + 1;
              rec_import_record_error                  := NULL;
              rec_import_record_error.import_record_id := v_import_record_id;
              rec_import_record_error.import_file_id   := v_import_file_id;
              rec_import_record_error.item_key         := 'INVALID_VALUE '||sqlerrm;
              rec_import_record_error.param1           := v_column_name;
              rec_import_record_error.param2           := v_column_value;
              rec_import_record_error.date_created     := sysdate;
              rec_import_record_error.created_by       := 0;
              prc_insert_import_record_error(rec_import_record_error);
          END;
     
      ELSIF UPPER(rec_stg_quiz.record_type) = 'Q' THEN --Question record
          BEGIN    
            v_field_cnt                    := v_field_cnt + 1;
            v_column_name                  := 'question_text';
            v_column_value                 := replace(ltrim(rtrim(fnc_pipe_parse(v_text, 2, c_delimiter))),chr(13));
            rec_stg_quiz.question_text     := v_column_value;
          EXCEPTION
            WHEN OTHERS THEN
              rec_stg_quiz.question_text := null; -- Set to null in stage table, error rec holds original value
              v_field_err_cnt                          := v_field_err_cnt + 1;
              rec_import_record_error                  := NULL;
              rec_import_record_error.import_record_id := v_import_record_id;
              rec_import_record_error.import_file_id   := v_import_file_id;
              rec_import_record_error.item_key         := 'INVALID_VALUE '||sqlerrm;
              rec_import_record_error.param1           := v_column_name;
              rec_import_record_error.param2           := v_column_value;
              rec_import_record_error.date_created     := sysdate;
              rec_import_record_error.created_by       := 0;
              prc_insert_import_record_error(rec_import_record_error);
          END;

          BEGIN
            v_field_cnt                       := v_field_cnt + 1;
            v_column_name                     := 'question_is_required';
            v_column_value                    := replace(ltrim(rtrim(fnc_pipe_parse(v_text, 3, c_delimiter))),chr(13));
            rec_stg_quiz.question_is_required := to_number(v_column_value);
          EXCEPTION
            WHEN OTHERS THEN
              rec_stg_quiz.question_is_required := null; -- Set to null in stage table, error rec holds original value
              v_field_err_cnt                          := v_field_err_cnt + 1;
              rec_import_record_error                  := NULL;
              rec_import_record_error.import_record_id := v_import_record_id;
              rec_import_record_error.import_file_id   := v_import_file_id;
              rec_import_record_error.item_key         := 'INVALID_VALUE '||sqlerrm;
              rec_import_record_error.param1           := v_column_name;
              rec_import_record_error.param2           := v_column_value;
              rec_import_record_error.date_created     := sysdate;
              rec_import_record_error.created_by       := 0;
              prc_insert_import_record_error(rec_import_record_error);
          END;
 
          BEGIN
            v_field_cnt                       := v_field_cnt + 1;
            v_column_name                     := 'question_status_type';
            v_column_value                    := replace(ltrim(rtrim(fnc_pipe_parse(v_text, 4, c_delimiter))),chr(13));
            rec_stg_quiz.question_status_type := v_column_value;
          EXCEPTION
            WHEN OTHERS THEN
              rec_stg_quiz.question_status_type := null; -- Set to null in stage table, error rec holds original value
              v_field_err_cnt                          := v_field_err_cnt + 1;
              rec_import_record_error                  := NULL;
              rec_import_record_error.import_record_id := v_import_record_id;
              rec_import_record_error.import_file_id   := v_import_file_id;
              rec_import_record_error.item_key         := 'INVALID_VALUE '||sqlerrm;
              rec_import_record_error.param1           := v_column_name;
              rec_import_record_error.param2           := v_column_value;
              rec_import_record_error.date_created     := sysdate;
              rec_import_record_error.created_by       := 0;
              prc_insert_import_record_error(rec_import_record_error);
          END;
            
          
      ELSIF UPPER(rec_stg_quiz.record_type) = 'A' THEN --Answer record
          BEGIN
            v_field_cnt                     := v_field_cnt + 1;
            v_column_name                   := 'question_status_type';
            v_column_value                  := replace(ltrim(rtrim(fnc_pipe_parse(v_text, 2, c_delimiter))),chr(13));
            rec_stg_quiz.answer_choice_text := v_column_value;
          EXCEPTION
            WHEN OTHERS THEN
              rec_stg_quiz.answer_choice_text := null; -- Set to null in stage table, error rec holds original value
              v_field_err_cnt                          := v_field_err_cnt + 1;
              rec_import_record_error                  := NULL;
              rec_import_record_error.import_record_id := v_import_record_id;
              rec_import_record_error.import_file_id   := v_import_file_id;
              rec_import_record_error.item_key         := 'INVALID_VALUE '||sqlerrm;
              rec_import_record_error.param1           := v_column_name;
              rec_import_record_error.param2           := v_column_value;
              rec_import_record_error.date_created     := sysdate;
              rec_import_record_error.created_by       := 0;
              prc_insert_import_record_error(rec_import_record_error);
          END;
     
          BEGIN
            v_field_cnt                     := v_field_cnt + 1;
            v_column_name                   := 'question_status_type';
            v_column_value                  := replace(ltrim(rtrim(fnc_pipe_parse(v_text, 3, c_delimiter))),chr(13));
            rec_stg_quiz.answer_is_correct := to_number(v_column_value);
          EXCEPTION
            WHEN OTHERS THEN
              rec_stg_quiz.answer_is_correct := null; -- Set to null in stage table, error rec holds original value
              v_field_err_cnt                          := v_field_err_cnt + 1;
              rec_import_record_error                  := NULL;
              rec_import_record_error.import_record_id := v_import_record_id;
              rec_import_record_error.import_file_id   := v_import_file_id;
              rec_import_record_error.item_key         := 'INVALID_VALUE '||sqlerrm;
              rec_import_record_error.param1           := v_column_name;
              rec_import_record_error.param2           := v_column_value;
              rec_import_record_error.date_created     := sysdate;
              rec_import_record_error.created_by       := 0;
              prc_insert_import_record_error(rec_import_record_error);
          END;

          BEGIN
            v_field_cnt                          := v_field_cnt + 1;
            v_column_name                        := 'question_status_type';
            v_column_value                       := replace(ltrim(rtrim(fnc_pipe_parse(v_text, 4, c_delimiter))),chr(13));
            rec_stg_quiz.answer_explanation_text := v_column_value;
          EXCEPTION
            WHEN OTHERS THEN
              rec_stg_quiz.answer_explanation_text := null; -- Set to null in stage table, error rec holds original value
              v_field_err_cnt                          := v_field_err_cnt + 1;
              rec_import_record_error                  := NULL;
              rec_import_record_error.import_record_id := v_import_record_id;
              rec_import_record_error.import_file_id   := v_import_file_id;
              rec_import_record_error.item_key         := 'INVALID_VALUE '||sqlerrm;
              rec_import_record_error.param1           := v_column_name;
              rec_import_record_error.param2           := v_column_value;
              rec_import_record_error.date_created     := sysdate;
              rec_import_record_error.created_by       := 0;
              prc_insert_import_record_error(rec_import_record_error);
          END;
          
      ELSE -- Unknown record_type, unable to parce text file fields
         v_field_err_cnt                          := v_field_err_cnt + 1;
         rec_import_record_error                  := NULL;
         rec_import_record_error.import_record_id := v_import_record_id;
         rec_import_record_error.import_file_id   := v_import_file_id;
         rec_import_record_error.item_key         := 'INVALID_RECORD! Record type must be H, Q or A. Unable to parse fields. ';
         rec_import_record_error.param1           := 'Record Type';
         rec_import_record_error.param2           := rec_stg_quiz.record_type;
         rec_import_record_error.date_created     := sysdate;
         rec_import_record_error.created_by       := 0;
         prc_insert_import_record_error(rec_import_record_error);
    
      END IF;
                   
      --Insert into stage_quiz_import_record (Add record even if errors found)
      v_stage := 'Insert into stage_quiz_import_record table';
      prc_insert_stg_quiz(rec_stg_quiz, v_return_code);

      IF v_return_code = 99 THEN
        v_field_err_cnt := v_field_err_cnt + 1; 
        rec_import_record_error                  := NULL;
        rec_import_record_error.import_record_id := v_import_record_id;
        rec_import_record_error.import_file_id   := v_import_file_id;
        rec_import_record_error.item_key         := v_stage;
        rec_import_record_error.param1           := 'Check Execution_log';
        rec_import_record_error.param2           := null;
        rec_import_record_error.date_created     := sysdate;
        rec_import_record_error.created_by       := 0;
        prc_insert_import_record_error(rec_import_record_error);
      END IF;

      IF v_field_err_cnt > 0 THEN
         v_error_count := v_error_count + 1;
      ELSE
         v_recs_loaded_cnt := v_recs_loaded_cnt + 1;
      END IF;

      IF MOD(v_recs_loaded_cnt, 1000) = 0 THEN
         COMMIT;
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

    END; -- Get record
    
  END LOOP; -- *** end of loop to move data to stage_quiz_import_record table. ***
  
  COMMIT;

  v_stage := 'Update import_file table with record counts';
  UPDATE import_file f
     SET version = 1,
         status = 'stg',
         import_record_count       = v_recs_in_file_cnt,
         import_record_error_count = v_error_count
   WHERE import_file_id = v_import_file_id;

  utl_file.fclose(v_in_file_handler);

  v_stage := 'Write counts and ending to execution_log table';
  prc_execution_log_entry(v_process_name,v_release_level,'INFO','Count of records on file: '||v_recs_in_file_cnt||
                          '  Count of records in error: '||v_error_count,null);
  prc_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure completed for file '
                          ||p_file_name,null);
  COMMIT;
  
EXCEPTION
  WHEN termination_error THEN
    p_out_returncode := 99;
    UPDATE import_file f
    SET version                   = 1,
        status                    = 'stg_fail'
    WHERE import_file_id = v_import_file_id;

    prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                              'File: '||p_file_name||' failed at Stage: '||v_stage||
                              ' --> '||SQLERRM,null);
    COMMIT;

  WHEN OTHERS THEN
    p_out_returncode := 99;
    UTL_FILE.Fclose(v_in_file_handler);
 
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
END ;
/