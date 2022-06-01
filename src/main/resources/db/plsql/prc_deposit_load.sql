CREATE OR REPLACE PROCEDURE prc_deposit_load(p_file_name IN VARCHAR2,p_out_returncode OUT NUMBER) is
  /*******************************************************************************
  -- Purpose: To process the Deposit records from the file and populate the
  --          STAGE_DEPOSIT_IMPORT_RECORD with transactional data. Basically resolve
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
  -- Rachel R.        01/17/2006 Changed all references of p_insert_stg_deposit
  --                                 to prc_insert_stg_deposit
  -- Rachel R.        01/17/2006 Changed all references of unix_call
  --                                 to prc_unix_call
  -- Arun S        01/20/2006 Added the output parameter so that the procedure can be called from Script generator
  -- Percy M       01/26/2006 Changed from csv to pipe delimited
  -- Percy M       01/31/2006 Commented out call to movefile script
  -- D Murray      04/28/2006 Updated import_file table to stg_fail if file load fails (Bug 12020).
  --                          Removed all move file code - 9i can use prc_move_load_file_9i procedure.
  --                          Moved recs_in_file_cnt before parsing, changed call to prc_insert_stg_product
  --                          to have a return code, only counting recs_loaded_cnt if insert successful,
  --                          added v_error_count to count any records that fail during parsing or during
  --                          insert to stage table, changed dbms_outputs to execution_log entries. (Bug 12148)
  --                          Changed v_stage to have better messaging and added v_field_cnt for parsing errors.
  -- Arun S        01/07/2009 Changes to replace UC4
  -- Arun S        10/06/2010 PURL changes, added and populated column AWARD_DATE   
  -- Arun S        05/26/2011 Bug 37246 - Fixed the change on 10/6/2010 to move the validation 
  --                          after rec_import_record_error is initialized.
  -- Chidamba      09/21/2012 G5 change added and populated column Comments  
  -- Chidamba      01/10/2013 G5 change added three from elements column to stage table.
  -- Swati         04/18/2014 G5 change added anniversary num.
  -- Suresh J      01/23/2015 Bug Fix 59045 - error while inserting record into import_record_error table      
  -- Suresh J      02/10/2015 Added validation& error check for comment column length    
  -- Swati         04/01/2015 Bug 60952 - Deposit file load does not display an error when Comments exceed max character count (500)
  -- Chidamba      03/23/2018 Modifying fix did on 02/10/2015; since clinet needs more than 500 Char in comments via file load.    
      		              Higlited as Issue in G6-3953 Recognition via File load character limit does not match Online Recognition character limit
  *******************************************************************************/
  --MISCELLANIOUS
  c_delimiter        CONSTANT  VARCHAR2(10):='|';
  v_count            NUMBER := 0; --for commit count
  v_stage            VARCHAR2(500);
  v_return_code      NUMBER(5);  -- return from insert call
  v_header_rec       BOOLEAN := FALSE ;
  v_max_col_length   BOOLEAN := FALSE ;    --02/10/2015

  --UTL_FILE
  v_in_file_handler  utl_file.file_type;
  --v_path_name        os_propertyset.string_val%TYPE :=
  --                        fnc_get_system_variable('import.file.utl_path','db_utl_path');
  v_text             VARCHAR2(32767);   --01/23/2015 03/23/2018
  v_file_name        VARCHAR2 (500);
  v_directory_path   VARCHAR2 (4000);
  v_directory_name   VARCHAR2 (30);

  -- RECORD TYPE DECLARATION
  rec_stg_deposit          stage_deposit_import_record%ROWTYPE;
  rec_import_file          import_file%ROWTYPE;
  rec_import_record_error  import_record_error%ROWTYPE;

  --DEPOSIT LOAD
  v_import_record_id       stage_deposit_import_record.import_record_id%TYPE;
  v_import_file_id         stage_deposit_import_record.import_file_id%TYPE;
  v_action_type            stage_deposit_import_record.action_type%TYPE;
  v_import_record_error_id import_record_error.import_record_error_id%type;

  v_user_name              stage_deposit_import_record.user_name%TYPE;
  v_award_amount           stage_deposit_import_record.award_amount%TYPE;
  v_str_award_amount       VARCHAR2(20);

  v_created_by             stage_pax_import_record.created_by%TYPE := 0;
  v_date_created           stage_pax_import_record.date_created%TYPE := sysdate;

  v_award_date             stage_deposit_import_record.award_date%TYPE;  --Added on 10/06/2010
  v_str_award_date         VARCHAR2(30);                                 --Added on 10/06/2010
  v_comments               stage_deposit_import_record.comments%TYPE;    --Added on 09/21/2012
  v_form_element_1         stage_deposit_import_record.form_element_1%TYPE;  -- Added on 01/10/2013 
  v_form_element_2         stage_deposit_import_record.form_element_2%TYPE;  -- Added on 01/10/2013
  v_form_element_3         stage_deposit_import_record.form_element_3%TYPE;  -- Added on 01/10/2013
    
  v_str_anniversary_num    stage_deposit_import_record.anniversary_num%TYPE; -- Added on 04/18/2014
  v_anniversary_num        stage_deposit_import_record.anniversary_num%TYPE; -- Added on 04/18/2014

  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'PRC_DEPOSIT_LOAD' ;
  v_release_level         execution_log.release_level%type := '1';
  v_execution_log_msg     execution_log.text_line%TYPE;
  
  -- COUNTS
  v_recs_in_file_cnt       INTEGER := 0;
  v_field_cnt              INTEGER := 0;
  v_recs_loaded_cnt        INTEGER := 0;
  v_error_count            PLS_INTEGER := 0;
  v_error_tbl_count        PLS_INTEGER := 0;

  -- EXCEPTIONS
  exit_program_exception   EXCEPTION;


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
       'dep',
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
  v_in_file_handler := utl_file.fopen(v_directory_name, v_file_name, 'r',32767);   --03/23/2018 added 32767

  LOOP
    --*** move records from text file into the table stage_deposit_import_record.**
    v_stage := 'Reset variables';
    v_field_cnt := 0;
    v_award_date := NULL;
    v_comments   := NULL;

    BEGIN
      v_stage := 'Get record';
      utl_file.get_line(v_in_file_handler, v_text);
      
      IF v_header_rec THEN
        v_header_rec := FALSE;
      ELSE
        v_recs_in_file_cnt := v_recs_in_file_cnt + 1;
        v_stage := 'Parsing record number: '||v_recs_in_file_cnt;

        v_field_cnt := v_field_cnt + 1;
        v_user_name :=
             replace(ltrim(rtrim(fnc_pipe_parse(v_text, 1, c_delimiter))),chr(13));

        v_field_cnt := v_field_cnt + 1;
        v_str_award_amount :=
             replace(ltrim(rtrim(fnc_pipe_parse(v_text, 2, c_delimiter))),chr(13));
        
        --Populated column AWARD_DATE start --Added on 10/06/2010
        v_field_cnt := v_field_cnt + 1;
        v_str_award_date :=
             replace(ltrim(rtrim(fnc_pipe_parse(v_text, 3, c_delimiter))),chr(13));
             
        --Populated column Comments start --Added on 09/21/2012     
        v_field_cnt := v_field_cnt + 1;        
        /************************************* 03/23/2018 Start *****************************************/
                IF LENGTH(NVL(replace(ltrim(rtrim(fnc_pipe_parse(v_text, 4, c_delimiter))),chr(13)),'x')) > 2000 THEN   --02/10/2015 
                     v_comments       :=  NULL;
                     v_max_col_length :=  TRUE;
                ELSE
                     v_comments       := 
                     replace(ltrim(rtrim(fnc_pipe_parse(v_text, 4, c_delimiter))),chr(13));
                     v_max_col_length :=  FALSE;
                END IF;
        /************************************* 03/23/2018 End  *****************************************/
        
        v_field_cnt := v_field_cnt + 1;  -- Added on 01/10/2013
        v_form_element_1 := 
             replace(ltrim(rtrim(fnc_pipe_parse(v_text, 5, c_delimiter))),CHR(13));
        
        v_field_cnt := v_field_cnt + 1;  -- Added on 01/10/2013
        v_form_element_2 := 
             replace(ltrim(rtrim(fnc_pipe_parse(v_text, 6, c_delimiter))),CHR(13));
        
        v_field_cnt := v_field_cnt + 1;  -- Added on 01/10/2013
        v_form_element_3 := 
             replace(ltrim(rtrim(fnc_pipe_parse(v_text, 7, c_delimiter))),CHR(13));
        
        v_field_cnt := v_field_cnt + 1;  -- Added on 04/18/2014
        v_str_anniversary_num := 
             replace(ltrim(rtrim(fnc_pipe_parse(v_text, 8, c_delimiter))),CHR(13));        
                
        /* -- 05/26/2011 move below code after the rec_import_record_error is initialized below
        IF v_str_award_date IS NOT NULL THEN
          BEGIN        
            v_award_date := TO_DATE(v_str_award_date, 'MM/DD/YYYY');
          EXCEPTION
            WHEN OTHERS THEN
              rec_import_record_error.item_key   := 'system.errors.DATE';
              rec_import_record_error.param1     := v_str_award_date;
              rec_import_record_error.param2     := SUBSTR(SQLERRM,1,250);
              rec_import_record_error.created_by := v_created_by;
              prc_insert_import_record_error(rec_import_record_error);
              v_award_date := NULL;
          END;                         
        END IF;
        */
        --Populated column AWARD_DATE end
        
        v_stage := 'Validations';
        SELECT import_record_pk_sq.NEXTVAL
          INTO v_import_record_id
          FROM dual;
      

        v_stage := 'Check for required columns';
        rec_import_record_error.date_created     := sysdate;
        rec_import_record_error.created_by       := v_created_by;
        rec_import_record_error.import_record_id := v_import_record_id;
        rec_import_record_error.import_file_id   := v_import_file_id;

        IF ltrim(rtrim(v_user_name)) IS NULL THEN
            rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
            rec_import_record_error.param1   := 'User Name';
            rec_import_record_error.created_by   := v_created_by ;
            prc_insert_import_record_error(rec_import_record_error);
        ELSIF ltrim(rtrim(v_str_award_amount)) IS NULL THEN
          rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
          rec_import_record_error.param1   := 'Award Amount';
          rec_import_record_error.created_by   := v_created_by ;
          prc_insert_import_record_error(rec_import_record_error);
          v_str_award_amount := NULL;
        END IF; -- v_user_name is null


        v_stage := 'Check for numerics';
        BEGIN
          v_award_amount := TO_NUMBER(v_str_award_amount);
        EXCEPTION
          WHEN OTHERS THEN
            rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
            rec_import_record_error.param1   := 'Award Amount';
            rec_import_record_error.param2   := v_str_award_amount;
            rec_import_record_error.created_by   := v_created_by ;
            prc_insert_import_record_error(rec_import_record_error);
            v_award_amount := NULL;
        END;
        
        BEGIN -- Added on 04/18/2014
          v_anniversary_num := TO_NUMBER(v_str_anniversary_num);
        EXCEPTION
          WHEN OTHERS THEN
            rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
            rec_import_record_error.param1   := 'Anniversary Days';
            rec_import_record_error.param2   := v_str_anniversary_num;
            rec_import_record_error.created_by   := v_created_by ;
            prc_insert_import_record_error(rec_import_record_error);
            v_anniversary_num := NULL;
        END;
            
       -- 05/26/2011  
       -- for populated column AWARD_DATE
        IF v_str_award_date IS NOT NULL THEN
          BEGIN        
            v_award_date := TO_DATE(v_str_award_date, 'MM/DD/YYYY');
          EXCEPTION
            WHEN OTHERS THEN
              rec_import_record_error.item_key   := 'system.errors.DATE';
              rec_import_record_error.param1     := v_str_award_date;
              rec_import_record_error.param2     := SUBSTR(SQLERRM,1,250);
              rec_import_record_error.created_by := v_created_by;
              prc_insert_import_record_error(rec_import_record_error);
              v_award_date := NULL;
          END;                         
        END IF;

        IF v_max_col_length THEN    --02/10/2015
            rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
            rec_import_record_error.param1   := 'Comments';
            rec_import_record_error.param2   := 'Length of text exceeds 2000';  -- 03/23/2018 replaced 500 with 2000
            rec_import_record_error.created_by   := v_created_by ;
            prc_insert_import_record_error(rec_import_record_error);
        END IF;
		--04/01/2015 Bug 60952 Starts 
		IF LENGTH(v_form_element_1) > 500 THEN
			v_form_element_1 := NULL ;
			rec_import_record_error.item_key := 'admin.fileload.errors.VALUE_TOO_LARGE';
            rec_import_record_error.param1   := 'Comments';
            rec_import_record_error.param2   := 'Length of text exceeds 500';
            rec_import_record_error.created_by   := v_created_by ;
            prc_insert_import_record_error(rec_import_record_error);
		END IF;
		
		IF LENGTH(v_form_element_2) > 500 THEN
			v_form_element_2 := NULL ;
			rec_import_record_error.item_key := 'admin.fileload.errors.VALUE_TOO_LARGE';
            rec_import_record_error.param1   := 'Comments';
            rec_import_record_error.param2   := 'Length of text exceeds 500';
            rec_import_record_error.created_by   := v_created_by ;
            prc_insert_import_record_error(rec_import_record_error);
		END IF;
		
		IF LENGTH(v_form_element_3) > 500 THEN
			v_form_element_3 := NULL ;
			rec_import_record_error.item_key := 'admin.fileload.errors.VALUE_TOO_LARGE';
            rec_import_record_error.param1   := 'Comments';
            rec_import_record_error.param2   := 'Length of text exceeds 500';
            rec_import_record_error.created_by   := v_created_by ;
            prc_insert_import_record_error(rec_import_record_error);
		END IF;
		--04/01/2015 Bug 60952 Ends

        v_action_type := 'add';
        v_stage := 'Initialize the record type';
        rec_stg_deposit.import_record_id := v_import_record_id;
        rec_stg_deposit.import_file_id   := v_import_file_id;
        rec_stg_deposit.action_type      := v_action_type;
        rec_stg_deposit.user_name        := v_user_name;
        rec_stg_deposit.award_amount     := v_award_amount;
        rec_stg_deposit.created_by       := v_created_by;
        rec_stg_deposit.date_created     := v_date_created;
        rec_stg_deposit.award_date       := v_award_date;         --Added on 10/06/2010
        rec_stg_deposit.comments         := v_comments;           --Added on 09/21/2012
        rec_stg_deposit.form_element_1   := v_form_element_1;     --Added on 01/10/2013
        rec_stg_deposit.form_element_2   := v_form_element_2;     --Added on 01/10/2013
        rec_stg_deposit.form_element_3   := v_form_element_3;     --Added on 01/10/2013 
        rec_stg_deposit.anniversary_num    := v_anniversary_num;      -- Added on 04/18/2014
        
        --Insert into stage_deposit_import_record
        v_stage := 'Insert into stage_deposit_import_record table';
        prc_insert_stg_deposit(rec_stg_deposit, v_return_code);
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
    
END  prc_deposit_load;
/
