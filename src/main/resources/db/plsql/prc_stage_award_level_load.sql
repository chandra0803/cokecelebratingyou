create or replace PROCEDURE prc_stage_award_level_load(p_file_name IN VARCHAR2,
                                                       p_out_returncode OUT NUMBER) IS
  /*******************************************************************************
  -- Purpose: BI Admin can load a file to issue an award level to a batch of recipients (pax or non-pax). 
  -- This applies to merchandise level promotions only. This is not applicable for merchandise item promotions. 
  -- The same file layout for the load will be used for pax (using login_id) and non-pax (using name and country)
  --
  -- Person         Date       Comments
  -- -----------    --------   -----------------------------------------------------
  -- S Majumder    	02/18/2009  Initial Creation.
  -- S Majumder    	04/03/2009  Bug Bug # 25170 
  -- Arun S        	01/07/2009  Changes to replace UC4
  -- Arun S        	10/06/2010  PURL changes, added and populated column AWARD_DATE
  -- Chidamba      	09/21/2012  G5 change added and populated column Comments  
  -- Chidamba      	10/10/2012  Comment out columns FIRST_NAME, MIDDLE_NAME, LAST_NAME 
                                COUNTRY_CODE, COUNTRY_ID  AND EMAIL_ADDRESS As AWARD 
                                for NON PARTICIPANT is removed 
  -- Chidamba      	12/12/2012 	Change Award file load to include up to 3 form elements for PURL promotions only.
  -- Ravi Dhanekula 02/05/2013 	Fixed issues with user validation and other issues with inserting data to stage table.
  -- Swati			    04/18/2014 	Change Award File load to include anniversary num.
  -- Ramkumar       11/30/2018  Bug 78218 Fix - Copied from WIP 50502 fix
  *******************************************************************************/
  
  --MISCELLANIOUS
  c_delimiter               CONSTANT  VARCHAR2(10):='|';
  v_count                   NUMBER := 0; --for commit count
  v_stage                   VARCHAR2(500);
  v_return_code             NUMBER(5);  -- return from insert call
 -- v_header_rec              BOOLEAN := FALSE ;
  v_nonpax_ind              NUMBER := 0;
  v_user_id                 application_user.user_id%TYPE;

  --UTL_FILE
  v_in_file_handler         utl_file.file_type;
  --v_path_name               os_propertyset.string_val%TYPE :=
  --                               fnc_get_system_variable('import.file.utl_path','db_utl_path');
  v_text                    varchar2(2000);
  v_file_name        VARCHAR2 (500);
  v_directory_path   VARCHAR2 (4000);
  v_directory_name   VARCHAR2 (30);

  -- RECORD TYPE DECLARATION
  rec_award_level           STAGE_AWARD_LEVEL_IMPORT%ROWTYPE;
  rec_import_record_error   import_record_error%ROWTYPE;

  --AWARD LEVEL LOAD
  v_import_record_id        STAGE_AWARD_LEVEL_IMPORT.import_record_id%TYPE;
  v_import_file_id          STAGE_AWARD_LEVEL_IMPORT.import_file_id%TYPE;
  v_action_type             STAGE_AWARD_LEVEL_IMPORT.action_type%TYPE;
  v_user_name               STAGE_AWARD_LEVEL_IMPORT.user_name%TYPE;
  v_award_level             VARCHAR2(12);
  v_valid_award_level       STAGE_AWARD_LEVEL_IMPORT.award_level%TYPE;
  --v_fname                   STAGE_AWARD_LEVEL_IMPORT.first_name%TYPE;
  --v_mname                   STAGE_AWARD_LEVEL_IMPORT.middle_name%TYPE;
  --v_lname                   STAGE_AWARD_LEVEL_IMPORT.last_name%TYPE;
  --v_email_address           STAGE_AWARD_LEVEL_IMPORT.email_address%TYPE;
  --v_country_id              STAGE_AWARD_LEVEL_IMPORT.country_id%TYPE;
  --v_country                 STAGE_AWARD_LEVEL_IMPORT.country_code%TYPE;   
  v_created_by              STAGE_AWARD_LEVEL_IMPORT.created_by%TYPE := 0;
  v_award_date              STAGE_AWARD_LEVEL_IMPORT.award_date%TYPE;  --Added on 10/06/2010
  v_str_award_date          VARCHAR2(30);                              --Added on 10/06/2010
  v_form_element_1          STAGE_AWARD_LEVEL_IMPORT.form_element_1%TYPE;
  v_form_element_2          STAGE_AWARD_LEVEL_IMPORT.form_element_2%TYPE;
  v_form_element_3          STAGE_AWARD_LEVEL_IMPORT.form_element_3%TYPE;
  v_anniversary_num  VARCHAR2(12); -- Added on 04/18/2014
  v_valid_anniversary_num	STAGE_AWARD_LEVEL_IMPORT.anniversary_num%TYPE; -- Added on 04/18/2014

    
  v_comments                STAGE_AWARD_LEVEL_IMPORT.COMMENTS%TYPE;

  -- EXECUTION LOG VARIABLES
  v_process_name            execution_log.process_name%type  := 'prc_stage_award_level_load';
  v_release_level           execution_log.release_level%type := '1';
  v_execution_log_msg       execution_log.text_line%TYPE;
  
  -- COUNTS
  v_recs_in_file_cnt        INTEGER := 0;
  v_field_cnt               INTEGER := 0;
  v_recs_loaded_cnt         INTEGER := 0;
  v_error_count             PLS_INTEGER := 0;
  v_error_tbl_count         PLS_INTEGER := 0;

  v_language_code           VARCHAR2(10) := 'en_US';
  -- EXCEPTIONS
  exit_program_exception    EXCEPTION;

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
       staged_by,
       date_staged)
    VALUES
      (v_import_file_id,
       substr(v_file_name, 1, instr(v_file_name, '.') - 1) || '_' ||v_import_file_id,
       'awardlevel',
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
    --*** move records from text file into the table stage_pax_import_record.**
    v_stage := 'Reset variables';
    v_field_cnt := 0;
    v_nonpax_ind := 0;
    v_user_name := NULL;
    v_award_level := NULL;
    --v_fname := NULL;
    --v_mname := NULL;
    --v_lname := NULL;
    --v_email_address := NULL;
    --v_country := NULL;
    v_user_id        := NULL;
    v_award_date     := NULL;
    v_comments       := NULL;
    v_form_element_1 := NULL;
    v_form_element_2 := NULL;
    v_form_element_3 := NULL;
	v_anniversary_num := NULL; -- Added on 04/18/2014
	

    BEGIN
      v_stage := 'Get record';
      utl_file.get_line(v_in_file_handler, v_text);

--      IF v_header_rec THEN
--        v_header_rec := FALSE;
--      ELSE
      
        SELECT import_record_pk_sq.NEXTVAL
          INTO v_import_record_id
          FROM dual;
        rec_import_record_error.import_record_id := v_import_record_id;
              
        v_recs_in_file_cnt := v_recs_in_file_cnt + 1;
        v_stage := 'Parsing record number: '||v_recs_in_file_cnt;

        v_field_cnt := v_field_cnt + 1;
        v_user_name         := UPPER(REPLACE(TRIM(fnc_pipe_parse(v_text, 1, c_delimiter)),CHR(13)));

        v_field_cnt := v_field_cnt + 1;
        v_award_level       := REPLACE(TRIM(fnc_pipe_parse(v_text, 2, c_delimiter)),CHR(13));

       --v_field_cnt := v_field_cnt + 1;                                                          --Commented, Chidamba 10/10/2012 start
       -- v_fname             := REPLACE(TRIM(fnc_pipe_parse(v_text, 3, c_delimiter)),CHR(13));

       --v_field_cnt := v_field_cnt + 1;
       --v_mname             := REPLACE(TRIM(fnc_pipe_parse(v_text, 4, c_delimiter)),CHR(13));

       --v_field_cnt := v_field_cnt + 1;
       --v_lname             := REPLACE(TRIM(fnc_pipe_parse(v_text, 5, c_delimiter)),CHR(13));

       --v_field_cnt := v_field_cnt + 1;
       --v_country              := REPLACE(TRIM(fnc_pipe_parse(v_text, 6, c_delimiter)),CHR(13));

       -- v_field_cnt := v_field_cnt + 1;
       -- v_email_address        := REPLACE(TRIM(fnc_pipe_parse(v_text, 7, c_delimiter)),CHR(13));  --Commented, Chidamba 10/10/2012  end

        --Populated column AWARD_DATE start --Added on 10/06/2010
        v_field_cnt := v_field_cnt + 1;
        v_str_award_date       := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text, 3, c_delimiter))),CHR(13));
        
        
        v_field_cnt := v_field_cnt + 1;  -- Added on 09/21/2012
        v_comments             := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text, 4, c_delimiter))),CHR(13));
        
        v_field_cnt := v_field_cnt + 1;  -- Added on 12/12/2012
        v_form_element_1         := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text, 5, c_delimiter))),CHR(13));
        
        v_field_cnt := v_field_cnt + 1;  -- Added on 12/12/2012
        v_form_element_2        := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text, 6, c_delimiter))),CHR(13));
        
        v_field_cnt := v_field_cnt + 1;  -- Added on 12/12/2012
        v_form_element_3        := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text, 7, c_delimiter))),CHR(13));    

		v_field_cnt := v_field_cnt + 1;	 -- Added on 04/18/2014
		v_anniversary_num	:= REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text, 8, c_delimiter))),CHR(13)); 
		
	IF v_str_award_date IS NOT NULL THEN
          BEGIN        
            v_award_date := fnc_locale_to_date_dt(v_str_award_date, v_language_code);
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
        --Populated column AWARD_DATE end

        v_stage := 'Participant Validations';
        
    -- User_name and Award Level are Required
        IF LTRIM (RTRIM (v_user_name)) IS NULL THEN
           rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
           rec_import_record_error.param1 := 'User Name';
           rec_import_record_error.created_by := v_created_by;
           prc_insert_import_record_error (rec_import_record_error);
        END IF;

        IF LTRIM (RTRIM (v_award_level)) IS NULL THEN
           rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
           rec_import_record_error.param1 := 'Award Level';
           rec_import_record_error.created_by := v_created_by;
           prc_insert_import_record_error (rec_import_record_error);
        END IF;

        -- Find out if user is a Non Pax
        IF upper(v_user_name) = 'NONPAX' THEN
           v_nonpax_ind := 1;
        END IF;

    -- If login id is non pax, First Name, Last Name, Country and Email Address are.  
      /*  IF v_nonpax_ind = 1 THEN                    --Commented. Chidamba --10/10/2012 Start 

          -- IF LTRIM (RTRIM (v_fname)) IS NULL THEN
          --    rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
          --    rec_import_record_error.param1 := 'First Name for Non Pax';
          --    rec_import_record_error.created_by := v_created_by;
          --    prc_insert_import_record_error (rec_import_record_error);
          -- END IF;
    
          -- IF LTRIM (RTRIM (v_lname)) IS NULL THEN
          --    rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
          --    rec_import_record_error.param1 := 'Last Name for Non Pax';
          --    rec_import_record_error.created_by := v_created_by;
          --    prc_insert_import_record_error (rec_import_record_error);
          -- END IF;

          -- IF LTRIM (RTRIM (v_email_address)) IS NULL THEN
          --    rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
          --    rec_import_record_error.param1 := 'Email Address for Non Pax';
          --    rec_import_record_error.created_by := v_created_by;
          --    prc_insert_import_record_error (rec_import_record_error);
          -- END IF;

          -- IF LTRIM (RTRIM (v_country)) IS NULL THEN
          --    rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
          --    rec_import_record_error.param1 := 'Country for Non Pax';
          --    rec_import_record_error.created_by := v_created_by;
          --    prc_insert_import_record_error (rec_import_record_error);
          --  END IF;

        END IF;*/       ---Commented. Chidamba --10/10/2012 End

    -- Login id for participant (not non-pax) is valid and participant is active status.
        IF ((v_nonpax_ind = 0) AND (v_user_name IS NOT NULL)) THEN
           BEGIN
             SELECT user_id
               INTO v_user_id
               FROM application_user
              WHERE upper(user_name) = upper(v_user_name)
            AND is_active = 1;
           EXCEPTION
             WHEN NO_DATA_FOUND THEN
               BEGIN
             SELECT user_id
               INTO v_user_id
               FROM application_user
              WHERE upper(user_name) = upper(v_user_name)
                AND is_active = 0;
                
                  IF v_user_id is NOT NULL THEN --02/05/2013
                         
               rec_import_record_error.item_key := 'admin.fileload.errors.INACTIVE_USER';
               rec_import_record_error.param1   := 'User Name - '||v_user_name;
               rec_import_record_error.created_by   := v_created_by;
               prc_insert_import_record_error(rec_import_record_error);
               v_user_id := NULL;                         
             
             END IF;--Is the user active
               EXCEPTION
             WHEN NO_DATA_FOUND THEN
               rec_import_record_error.item_key := 'admin.fileload.errors.USER_NOT_VALID';
               rec_import_record_error.param1   := 'User Name - '||v_user_name;
               rec_import_record_error.created_by   := v_created_by;
               prc_insert_import_record_error(rec_import_record_error);
               v_user_id := NULL;
               END;    
               
               END;            
                                    
             IF v_user_id is NOT NULL THEN --02/05/2013
             
             BEGIN
             SELECT user_id
                 INTO v_user_id
                 FROM participant                       
                WHERE user_id = v_user_id
                  --and NVL(suspension_status,'NONE') = 'NONE'; --11/30/2018
                  and NVL(suspension_status,'none') = 'none'; --11/30/2018
            EXCEPTION
             WHEN NO_DATA_FOUND THEN
               rec_import_record_error.item_key := 'admin.fileload.errors.SUSPENDED_USER';
               rec_import_record_error.param1   := 'User Name - '||v_user_name;
               rec_import_record_error.created_by   := v_created_by;
               prc_insert_import_record_error(rec_import_record_error);
               v_user_id := NULL;
               END;   
               END IF;--Is the user suspended
          -- END; --  check if user exists
        END IF;

        -- Award level is numeric
        BEGIN
           v_valid_award_level := TO_NUMBER(v_award_level);
           IF MOD(v_valid_award_level, 1) <> 0 THEN
              rec_import_record_error.item_key := 'user.characteristic.errors.INTEGER';
              rec_import_record_error.param1 := 'Award Level';
              rec_import_record_error.created_by := v_created_by;
              prc_insert_import_record_error(rec_import_record_error);
              v_valid_award_level :=v_award_level;--02/05/2013
           END IF;
        EXCEPTION
           WHEN VALUE_ERROR THEN
              rec_import_record_error.item_key := 'user.characteristic.errors.INTEGER';
              rec_import_record_error.param1 := 'Award Level';
              rec_import_record_error.created_by := v_created_by;
              prc_insert_import_record_error(rec_import_record_error);
              v_valid_award_level :=v_award_level;--02/05/2013
        END;
		
		-- Anniversary Num is numeric  -- Added on 04/18/2014
        BEGIN
           v_valid_anniversary_num := TO_NUMBER(v_anniversary_num);
           IF MOD(v_valid_anniversary_num, 1) <> 0 THEN
              rec_import_record_error.item_key := 'user.characteristic.errors.INTEGER';
              rec_import_record_error.param1 := 'Anniversary Num';
              rec_import_record_error.created_by := v_created_by;
              prc_insert_import_record_error(rec_import_record_error);
              v_valid_anniversary_num :=v_anniversary_num;--02/05/2013
           END IF;
        EXCEPTION
           WHEN VALUE_ERROR THEN
              rec_import_record_error.item_key := 'user.characteristic.errors.INTEGER';
              rec_import_record_error.param1 := 'Anniversary Num';
              rec_import_record_error.created_by := v_created_by;
              prc_insert_import_record_error(rec_import_record_error);
              v_valid_anniversary_num :=v_anniversary_num;--02/05/2013
        END;
	        
        v_stage := 'Get country_id';
       /* -- If login id is non pax, country code is valid.             --10/10/2012 Chidamba  Start  
        IF ((v_nonpax_ind = 1) AND (v_country IS NOT NULL)) THEN
           BEGIN
             -- look for 2 character country code
             SELECT country_id
               INTO v_country_id
               FROM country
              WHERE country_code = LOWER(v_country);
           EXCEPTION
              WHEN no_data_found THEN
                 BEGIN
                    -- look for 3 character country code
                    SELECT country_id, LOWER(country_code)
                      INTO v_country_id, v_country
                      FROM country
                     WHERE awardbanq_country_abbrev = UPPER(v_country);
                 EXCEPTION
                   WHEN OTHERS THEN
                     v_country_id := NULL;
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1   := 'Country';
                     rec_import_record_error.param2   := v_country;
                     rec_import_record_error.created_by   := v_created_by;
                     prc_insert_import_record_error(rec_import_record_error);
                 END;
             WHEN others THEN
                v_country_id := NULL;
           END;
        END IF;*/                                                       --10/10/2012 Chidamba  End
        
        -- Initialize the import record error values
        rec_import_record_error.item_key := NULL;
        rec_import_record_error.param1   := NULL;
        rec_import_record_error.param2   := NULL;

        v_stage := 'Initialize the record type';
        rec_award_level.import_record_id := v_import_record_id;
        rec_award_level.import_file_id   := v_import_file_id;
        rec_award_level.action_type           := 'add'; -- It is always a add record
        rec_award_level.user_name             := v_user_name;
        rec_award_level.award_level           := v_valid_award_level;
        --rec_award_level.first_name            := v_fname;         
        --rec_award_level.middle_name           := v_mname;
        --rec_award_level.last_name             := v_lname;
        --rec_award_level.email_address         := v_email_address;
        --rec_award_level.country_id            := v_country_id;
        --rec_award_level.country_code          := v_country;
        rec_award_level.created_by            := v_created_by;
        rec_award_level.date_created          := SYSDATE;
        rec_award_level.award_date            := v_award_date;
        rec_award_level.comments              := v_comments;
        rec_award_level.form_element_1      := v_form_element_1;
        rec_award_level.form_element_2      := v_form_element_2;
        rec_award_level.form_element_3      := v_form_element_3;
		rec_award_level.anniversary_num	:= v_valid_anniversary_num; -- Added on 04/18/2014
        
        

        --Insert into stage_pax_import_record
        v_stage := 'Insert into stage_pax_import_record table';
        prc_stg_award_record_insert(rec_award_level, v_return_code);
        IF v_return_code = 99 THEN
          v_error_count := v_error_count + 1;  -- need to count any insert errors
        ELSE
          v_count := v_count + 1;
          v_recs_loaded_cnt := v_recs_loaded_cnt + 1;
        END IF;
            
        IF MOD(v_count, 10) = 0 THEN
          COMMIT;
        END IF;
     -- END IF; -- v_header_rec
    
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
    
END; -- Procedure prc_stage_award_level_load
