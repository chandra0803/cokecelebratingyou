CREATE OR REPLACE PACKAGE pkg_ssi_atn_load IS

  /*******************************************************************************
  -- Purpose: To stage,verify and load the AWARD THEM NOW contest for pax/manager/superviewer
  --
  -- Person        Date          Comments
  -- -----------   --------      -----------------------------------------------------
  -- Suresh J     11/11/2016     Initial Creation 
  *******************************************************************************/

  PROCEDURE p_stage_ssi_atn 
    (p_file_name        IN VARCHAR2, 
     p_out_returncode   OUT NUMBER);

  PROCEDURE p_ssi_atn_verify_import
    ( p_import_file_id   IN  import_file.import_file_id%TYPE,
      p_load_type        IN  VARCHAR2,
      p_ssi_contest_id   IN  ssi_contest.ssi_contest_id%TYPE,
      p_total_error_rec     OUT NUMBER,
      p_out_returncode      OUT NUMBER
    ); 
    
 PROCEDURE p_ssi_atn_stg_verify_import
    ( p_file_name        IN  import_file.file_name%TYPE,
      p_ssi_contest_id   IN  ssi_contest.ssi_contest_id%TYPE,
      p_in_user_id       IN NUMBER,
      p_total_error_rec     OUT NUMBER,
      p_out_import_file_id  OUT NUMBER,
      p_out_returncode      OUT NUMBER
    ); 

END pkg_ssi_atn_load; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY pkg_ssi_atn_load
IS
  /*******************************************************************************
  -- Purpose: To stage,verify and load the AWARD THEM NOW contest for participant
  --
  -- Person        Date          Comments
  -- -----------   --------      -----------------------------------------------------
  -- Suresh J     11/11/2016     Initial Creation 
  *******************************************************************************/

-- global package variables
g_created_by               import_record_error.created_by%TYPE := 0;
g_timestamp                import_record_error.date_created%TYPE := SYSDATE;
g_load_type                VARCHAR2(1);
g_user_name                application_user.user_name%TYPE;

PROCEDURE p_stage_ssi_atn
             (p_file_name IN VARCHAR2, 
              p_out_returncode OUT NUMBER
             ) IS

  /*******************************************************************************
  -- Purpose: To load the AWARD_THEM_NOW contest for participant
  --
  -- Person        Date          Comments
  -- -----------   --------      -----------------------------------------------------
  -- nagarajs     11/01/2016     Initial Creation 
  -- nagarajs     03/03/2017    G6-1844 - Added new error message for inactive user validation
  *******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name                execution_log.process_name%type  := 'p_stage_ssi_atn' ;
  v_release_level               execution_log.release_level%type := '1';
  v_execution_log_msg           execution_log.text_line%TYPE;
  
  c_delimiter                   CONSTANT  VARCHAR2(10):='|';
  c_amount_allow                CONSTANT  NUMBER := 10000000000;
  c_description_allow           CONSTANT  NUMBER := 50;
  v_count                       NUMBER := 0; 
  v_stage                       VARCHAR2(500);
  v_return_code                 NUMBER(5);  -- return from insert call

  --UTL_FILE
  v_in_file_handler             utl_file.file_type;
  v_text                        VARCHAR2(500);
  v_file_name                   VARCHAR2(500); 
  v_directory_path              VARCHAR2(4000);
  v_directory_name              VARCHAR2(30);

  -- RECORD TYPE DECLARATION
  rec_import_file               import_file%ROWTYPE;
  rec_import_record_error       import_record_error%ROWTYPE;
  rec_ssi_atn                   stage_ssi_atn_import%ROWTYPE;

  --award them now LOAD
  v_import_record_id            stage_ssi_atn_import.import_record_id%TYPE;
  v_import_file_id              stage_ssi_atn_import.import_file_id%TYPE;
  v_import_record_error_id      import_record_error.import_record_error_id%type;

  v_created_by                  stage_ssi_atn_import.created_by%TYPE := 0;
  v_date_created                stage_ssi_atn_import.date_created%TYPE := SYSDATE;

  v_activity_amount             stage_ssi_atn_import.activity_amount%TYPE;
  v_payout_amount               stage_ssi_atn_import.payout_amount%TYPE;
  v_award_issue_value           stage_ssi_atn_import.award_issue_value%TYPE;
  v_is_active                   application_user.is_active%TYPE; --03/03/2017
  
  -- COUNTS
  v_recs_in_file_cnt            INTEGER := 0;
  v_field_cnt                   INTEGER := 0;
  v_recs_loaded_cnt             INTEGER := 0;
  v_error_count                 PLS_INTEGER := 0;
  v_error_tbl_count             PLS_INTEGER := 0;

  -- EXCEPTIONS
  exit_program_exception        EXCEPTION;

  PROCEDURE prc_insert_stg_ssi_atn (p_import_record     IN  stage_ssi_atn_import%ROWTYPE,
                                         p_out_return_code   OUT NUMBER)
   IS
      -- EXECUTION LOG VARIABLES
      v_process_name    execution_log.process_name%TYPE := 'PRC_INSERT_STG_SSI_ATN';
      v_release_level   execution_log.release_level%TYPE := '1';
   BEGIN
      INSERT INTO stage_ssi_atn_import 
           (import_record_id,
            import_file_id,
            user_name,
            user_id,
            first_name,
            last_name,
            role,
            activity_description,
            activity_amount,
            payout_amount,
            payout_description,
            award_issue_value,
            date_created,
            created_by)
     VALUES (p_import_record.import_record_id,
             p_import_record.import_file_id,
             p_import_record.user_name,
             p_import_record.user_id,
             p_import_record.first_name,
             p_import_record.last_name,
             p_import_record.role,
             p_import_record.activity_description,
             p_import_record.activity_amount,
             p_import_record.payout_amount,
             p_import_record.payout_description,
             p_import_record.award_issue_value,
             p_import_record.date_created,
             p_import_record.created_by);

      p_out_return_code := 0;
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := 99;
         prc_execution_log_entry ( v_process_name,v_release_level,'ERROR','Import_file_id: '|| p_import_record.import_file_id|| 'Import_record_id: '|| 
            p_import_record.import_record_id|| ' --> '|| SQLERRM,NULL);
         COMMIT;
   END prc_insert_stg_ssi_atn;
   
BEGIN
  v_stage := 'Write start to execution_log table';
  p_out_returncode := 0;
  prc_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure Started for file '
                          ||p_file_name, null);
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
       date_staged,
       staged_by,
       version)
    VALUES
      (v_import_file_id,
       substr(v_file_name, 1, instr(v_file_name, '.') - 1) || '_' || v_import_file_id,
       'ssicontestatn',
       0,
       0,
       'stg_in_process',
       SYSDATE,
       v_created_by ,
       1);
  EXCEPTION
    WHEN OTHERS THEN
       v_execution_log_msg := SQLERRM; 
      RAISE exit_program_exception; 
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
    --*** move records from text file into the table stage_ssi_atn_import**
    v_stage := 'Reset variables';
    v_field_cnt := 0;

    BEGIN
        v_stage := 'Get record';
        utl_file.get_line(v_in_file_handler, v_text);

        v_recs_in_file_cnt := v_recs_in_file_cnt + 1;
        v_stage := 'Parsing record number: '||v_recs_in_file_cnt;

        v_field_cnt := v_field_cnt + 1;
        rec_ssi_atn.user_name  := REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 1, c_delimiter)),CHR(13));

        v_field_cnt  := v_field_cnt + 1;
        rec_ssi_atn.first_name := REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 2, c_delimiter)),CHR(13));
      
        v_field_cnt := v_field_cnt + 1;
        rec_ssi_atn.last_name := REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 3, c_delimiter)),CHR(13));

        v_field_cnt  := v_field_cnt + 1;
        rec_ssi_atn.role := REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 4, c_delimiter)),CHR(13));

        v_field_cnt  := v_field_cnt + 1;
        rec_ssi_atn.activity_description := REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 5, c_delimiter)),CHR(13));
        
        v_field_cnt  := v_field_cnt + 1;
        rec_ssi_atn.activity_amount := REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 6, c_delimiter)),CHR(13));
      
        v_field_cnt := v_field_cnt + 1;
        rec_ssi_atn.payout_amount := REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 7, c_delimiter)),CHR(13));

        v_field_cnt  := v_field_cnt + 1;
        rec_ssi_atn.payout_description := REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 8, c_delimiter)),CHR(13));

        v_field_cnt  := v_field_cnt + 1;
        rec_ssi_atn.award_issue_value := REPLACE(TRIM(FNC_PIPE_PARSE(v_text, 9, c_delimiter)),CHR(13));


        v_stage := 'Validations';
        SELECT import_record_pk_sq.NEXTVAL
          INTO v_import_record_id
          FROM dual;
      

        v_stage := 'Check for required columns';
        rec_import_record_error.date_created     := sysdate;
        rec_import_record_error.created_by       := v_created_by;
        rec_import_record_error.import_record_id := v_import_record_id;
        rec_import_record_error.import_file_id   := v_import_file_id;

        IF rtrim(rec_ssi_atn.user_name) IS NULL THEN
            rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
            rec_import_record_error.param1   := 'User Name';
            rec_import_record_error.param2   := rec_ssi_atn.user_name;              
            prc_insert_import_record_error(rec_import_record_error);
        ELSE 
          BEGIN
            SELECT user_id,
                   is_active  --03/03/2017
              INTO rec_ssi_atn.user_id,
                   v_is_active --03/03/2017
              FROM application_user 
             WHERE lower(user_name) = lower(rec_ssi_atn.user_name);
               --AND is_active = 1; --03/03/2017
            IF v_is_active = 0 THEN
              rec_import_record_error.item_key := 'admin.fileload.errors.INACTIVE_PAX';  --03/03/2017
              rec_import_record_error.param1   := 'User Name';
              rec_import_record_error.param2   := rec_ssi_atn.user_name;              
              prc_insert_import_record_error(rec_import_record_error); 
            END IF;
          EXCEPTION 
            WHEN OTHERS THEN
              rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
              rec_import_record_error.param1   := 'User Name';
              rec_import_record_error.param2   := rec_ssi_atn.user_name;              
              prc_insert_import_record_error(rec_import_record_error);                
            
          END;
        END IF; -- v_user_name is null
        
        IF trim(rec_ssi_atn.role) IS NULL THEN
            rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
            rec_import_record_error.param1   := 'Role';
            rec_import_record_error.param2   := rec_ssi_atn.role;              
            prc_insert_import_record_error(rec_import_record_error);

        ELSE
        
            IF lower(trim(rec_ssi_atn.role)) NOT IN ('participant','manager','superviewer')  THEN
            
              rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
              rec_import_record_error.param1   := 'Role';
              rec_import_record_error.param2   := rec_ssi_atn.role;              
              prc_insert_import_record_error(rec_import_record_error);                
            
            END IF;

        END IF;
        
        IF LENGTH(rec_ssi_atn.activity_description) > c_description_allow THEN
          rec_import_record_error.item_key := 'admin.fileload.errors.TOO_LONG_CHAR';
          rec_import_record_error.param1   := 'Activity Description';
          rec_import_record_error.param2   := rec_ssi_atn.activity_description;              
          prc_insert_import_record_error(rec_import_record_error);
        END IF;
        
        IF rec_ssi_atn.activity_amount IS NOT NULL THEN
        
          BEGIN
            v_activity_amount := TO_NUMBER(rec_ssi_atn.activity_amount);
          EXCEPTION
            WHEN OTHERS THEN
              rec_import_record_error.item_key := 'admin.fileload.errors.MUST_BE_NUMBER_ERROR';
              rec_import_record_error.param1   := 'Activity Amount';
              rec_import_record_error.param2   := rec_ssi_atn.activity_amount;              
              prc_insert_import_record_error(rec_import_record_error);
          END;
          
          IF rec_ssi_atn.activity_amount > c_amount_allow THEN   
            rec_import_record_error.item_key := 'admin.fileload.errors.TOO_LONG_NUMBER';
            rec_import_record_error.param1   := 'Activity Amount';
            rec_import_record_error.param2   := rec_ssi_atn.activity_amount;              
            prc_insert_import_record_error(rec_import_record_error);
          END IF;
          
          IF INSTR (TO_CHAR (rec_ssi_atn.activity_amount), '.') > 0  AND
             REGEXP_COUNT ( 
                         TO_CHAR (rec_ssi_atn.activity_amount),
                         '[0-9]',
                         INSTR (TO_CHAR (rec_ssi_atn.activity_amount), '.') + 1) NOT BETWEEN 2 AND 4  THEN 
            rec_import_record_error.item_key := 'admin.fileload.errors.TWO_DECIMAL_VALIDATION';
            rec_import_record_error.param1   := 'Activity Amount';
            rec_import_record_error.param2   := rec_ssi_atn.activity_amount;              
            prc_insert_import_record_error(rec_import_record_error);
          END IF;
          
        END IF;
        
        IF LENGTH(rec_ssi_atn.payout_description) > c_description_allow THEN
          rec_import_record_error.item_key := 'admin.fileload.errors.TOO_LONG_CHAR';
          rec_import_record_error.param1   := 'Payout Description';
          rec_import_record_error.param2   := rec_ssi_atn.payout_description;              
          prc_insert_import_record_error(rec_import_record_error);
        END IF;
        
        IF rec_ssi_atn.payout_amount IS NOT NULL THEN
        
          BEGIN
            v_payout_amount := TO_NUMBER(rec_ssi_atn.payout_amount);
          EXCEPTION
            WHEN OTHERS THEN
              rec_import_record_error.item_key := 'admin.fileload.errors.MUST_BE_NUMBER_ERROR';
              rec_import_record_error.param1   := 'Payout Amount';
              rec_import_record_error.param2   := rec_ssi_atn.payout_amount;              
              prc_insert_import_record_error(rec_import_record_error);
          END;
          
          IF rec_ssi_atn.payout_amount > c_amount_allow THEN   
            rec_import_record_error.item_key := 'admin.fileload.errors.TOO_LONG_NUMBER';
            rec_import_record_error.param1   := 'Payout Amount';
            rec_import_record_error.param2   := rec_ssi_atn.payout_amount;              
            prc_insert_import_record_error(rec_import_record_error);
          END IF;          
                  
          IF FLOOR(rec_ssi_atn.payout_amount*100)!= rec_ssi_atn.payout_amount*100   THEN

            rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_NUMBER_ERROR';
            rec_import_record_error.param1   := 'Payout Amount';
            rec_import_record_error.param2   := rec_ssi_atn.payout_amount;              
            prc_insert_import_record_error(rec_import_record_error);
          END IF;
          
        END IF;
        
        IF rec_ssi_atn.award_issue_value IS NOT NULL THEN
        
          BEGIN
            v_award_issue_value := TO_NUMBER(rec_ssi_atn.award_issue_value);
          EXCEPTION
            WHEN OTHERS THEN
              rec_import_record_error.item_key := 'admin.fileload.errors.MUST_BE_NUMBER_ERROR';
              rec_import_record_error.param1   := 'Value';
              rec_import_record_error.param2   := rec_ssi_atn.award_issue_value;              
              prc_insert_import_record_error(rec_import_record_error);
          END;
          
          IF rec_ssi_atn.award_issue_value > c_amount_allow THEN   
            rec_import_record_error.item_key := 'admin.fileload.errors.TOO_LONG_NUMBER';
            rec_import_record_error.param1   := 'Value';
            rec_import_record_error.param2   := rec_ssi_atn.award_issue_value;              
            prc_insert_import_record_error(rec_import_record_error);
          END IF;         
          
          
          IF FLOOR(rec_ssi_atn.award_issue_value*1)!= rec_ssi_atn.award_issue_value*1   THEN
            rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_NUMBER_ERROR';
            rec_import_record_error.param1   := 'Value';
            rec_import_record_error.param2   := rec_ssi_atn.award_issue_value;              
            prc_insert_import_record_error(rec_import_record_error);
          END IF;
          
        END IF;

        v_stage := 'Initialize the record type';
        rec_ssi_atn.import_record_id := v_import_record_id;
        rec_ssi_atn.import_file_id   := v_import_file_id;
        rec_ssi_atn.created_by       := v_created_by;
        rec_ssi_atn.date_created     := v_date_created;


        --Insert into stage_ssi_atn_import
        v_stage := 'Insert into stage_ssi_atn_import table';
        prc_insert_stg_ssi_atn(rec_ssi_atn, v_return_code);
        
        IF v_return_code = 99 THEN
          v_error_count := v_error_count + 1;  -- need to count any insert errors
        ELSE
          v_count := v_count + 1;
          v_recs_loaded_cnt := v_recs_loaded_cnt + 1;
        END IF;

        IF MOD(v_count, 10) = 0 THEN
          COMMIT;
        END IF;
        
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        -- end of file
        --Program continues on to compare rec cnt.  Then final
        -- file_load_audit entry is done.
        utl_file.fclose(v_in_file_handler);
        EXIT;
      WHEN OTHERS THEN
        IF v_stage LIKE 'Parsing%' THEN
          v_stage := v_stage||' field number: '||v_field_cnt;
        END IF;
        prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                                'Stage: '||v_stage||' --> '||SQLERRM,null);

        v_error_count := v_error_count + 1;  -- need to count any parsing errors
    END; -- Get record
  END LOOP; -- *** end of loop to move data to stage_ssi_atn_import table. ***
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
         status = 'stg',
         import_record_count = v_recs_in_file_cnt,
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
        
END p_stage_ssi_atn ;

FUNCTION f_get_error_record_count
( p_import_file_id  IN  NUMBER
) RETURN INTEGER
IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('f_get_error_record_count');
   c_release_level      CONSTANT execution_log.release_level%type := '1';

   v_msg                execution_log.text_line%TYPE;
   v_error_record_count INTEGER;

   CURSOR cur_import_error_rec_count
   ( cv_import_file_id  import_record_error.import_file_id%TYPE
   ) IS
   SELECT COUNT(DISTINCT ire.import_record_id) AS error_cnt
     FROM import_record_error ire
    WHERE ire.import_file_id = cv_import_file_id
      ;

BEGIN
   -- get count of any existing errors
   v_msg := 'OPEN cur_import_error_rec_count';
   OPEN cur_import_error_rec_count(p_import_file_id);
   v_msg := 'FETCH cur_import_error_rec_count';
   FETCH cur_import_error_rec_count INTO v_error_record_count;
   CLOSE cur_import_error_rec_count;

   RETURN v_error_record_count;
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END f_get_error_record_count;

---------------------

-- Updates the import file parent record
PROCEDURE p_upd_import_file
( p_in_import_file_id      IN import_file.import_file_id%TYPE,
  p_in_status              IN import_file.status%TYPE,
  p_in_import_record_count IN import_file.import_record_count%TYPE,
  p_in_import_error_count  OUT import_file.import_record_error_count%TYPE
) IS
   c_proc_name         CONSTANT execution_log.process_name%TYPE := 'P_UPD_IMPORT_FILE';
   c_release_level     CONSTANT execution_log.release_level%TYPE := 1;
   l_proc_step          execution_log.text_line%TYPE;
   l_parm_list          execution_log.text_line%TYPE;
BEGIN
   -- initialize variables
   l_proc_step := 'initialize variables';
   l_parm_list := 'Parms'
      ||  ': p_in_import_file_id >' || p_in_import_file_id
      || '<, p_in_status >' || p_in_status
      || '<, p_in_import_record_count >' || p_in_import_record_count
      || '<';

   -- get error count
   l_proc_step := 'SELECT error count';
   SELECT COUNT(DISTINCT(e.import_record_id))
     INTO p_in_import_error_count
     FROM import_record_error e
    WHERE e.import_file_id = p_in_import_file_id;

   l_proc_step := 'UPDATE import_file';
   UPDATE import_file f
      SET version                   = NVL(f.version, 0) + 1,
          status                    = p_in_status,
          import_record_count       = NVL(p_in_import_record_count, f.import_record_count),
          import_record_error_count = p_in_import_error_count,
          verified_by               = DECODE(g_load_type,'V',g_user_name),
          date_verified             = DECODE(g_load_type,'V',SYSDATE),
          imported_by               = DECODE(g_load_type,'L',g_user_name),
          date_imported             = DECODE(g_load_type,'L',SYSDATE)
    WHERE f.import_file_id = p_in_import_file_id;

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_proc_name, c_release_level, 'ERROR', l_proc_step || '~' || l_parm_list || '~' || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;

END p_upd_import_file;

-- Reports any errors in the staged file records
PROCEDURE p_verify_ssi_atn
    ( p_in_import_file_id   IN  import_file.import_file_id%TYPE,
      p_in_ssi_contest_id   IN  ssi_contest.ssi_contest_id%TYPE,
      p_total_err_count     IN OUT NUMBER,
      p_out_return_code     OUT NUMBER
    ) 
  /*******************************************************************************
  -- Purpose: To verify the AWARD THEM NOW contest for participant
  --
  -- Person        Date          Comments
  -- -----------   --------      -----------------------------------------------------
  -- Suresh J     11/11/2016     Initial Creation 
  *******************************************************************************/
    IS

  -- EXECUTION LOG VARIABLES
   c_process_name                CONSTANT execution_log.process_name%TYPE := UPPER('p_verify_ssi_atn');
   c_release_level               execution_log.release_level%type := '1';
   c_execution_log_msg           execution_log.text_line%TYPE;
   v_stage                       VARCHAR2(500);
   v_msg                         execution_log.text_line%TYPE;
   v_rec_cnt                    INTEGER;
   gc_sys_err_not_elig_rcvr     CONSTANT import_record_error.item_key%TYPE := 'system.errors.PAX_NOT_ELIGIBLE_RECEIVER';
   gc_sys_err_repeated          CONSTANT import_record_error.item_key%TYPE := 'system.errors.REPEATED'; 
   gc_sys_err_invalid_property  CONSTANT import_record_error.item_key%TYPE := 'admin.fileload.errors.INVALID_PROPERTY';
   gc_sys_err_missing_property  CONSTANT import_record_error.item_key%TYPE := 'admin.fileload.errors.MISSING_PROPERTY'; 

   v_count_rec_other            INTEGER;
   v_count_rec_point            INTEGER;
   v_count_rec_all              INTEGER;
   
              
BEGIN

    p_out_return_code := 0;

/*
      --Check if file is having points or other payout type 
      v_stage := 'Checking file for payout type validation';
      v_count_rec_other := 0;
      v_count_rec_point := 0;
      v_count_rec_all   := 0;
      
      SELECT SUM( CASE WHEN (s.payout_description IS NOT NULL OR s.award_issue_value IS NOT NULL )
                            AND lower(s.role) = 'participant'
                  THEN 1
                       ELSE 0
                   END       
                 ) AS count_rec_other,
            SUM( CASE WHEN (s.payout_description IS NULL AND s.award_issue_value IS NULL ) 
                           AND lower(s.role) = 'participant'
                 THEN 1
                       ELSE 0
                   END       
                 ) AS count_rec_point,
            SUM( CASE 
                        WHEN lower(s.role) = 'participant'  THEN 1 
                        ELSE 0 
                 END )  AS count_rec_all      
             INTO v_count_rec_other,
                  v_count_rec_point,
                  v_count_rec_all           
      FROM stage_ssi_atn_import s
      WHERE s.import_file_id = p_in_import_file_id AND
            s.import_record_id NOT IN
             ( SELECT e.import_record_id
                 FROM import_record_error e
                WHERE e.import_file_id = p_in_import_file_id
                );

*/
    
    v_msg := 'INSERT import_record_error';
       INSERT INTO import_record_error
       ( import_record_error_id,
         import_file_id,
         import_record_id,
         item_key,
         param1,
         param2,
         param3,
         param4,
         created_by,
         date_created
       )
       (  SELECT import_record_error_pk_sq.nextval AS import_record_error_id,
                 e.import_file_id,
                 e.import_record_id,
                 e.item_key,
                 e.field_name  AS param1,
                 e.field_value AS param2,
                 e.ref_value   AS param3,
                 NULL          AS param4,
                 0             AS created_by,
                 SYSDATE       AS date_created
            FROM ( -- get errant records
                   WITH stg_rec AS
                   (  SELECT s.*
                             ,p_in_ssi_contest_id AS p_program_id
                             ,COUNT(1) OVER (PARTITION BY s.user_name,s.role) AS user_name_count
                        FROM stage_ssi_atn_import s
                       WHERE s.import_file_id = p_in_import_file_id
                         AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_in_import_file_id
                         )
                   )
                   -- duplicate user name
                   SELECT sr.import_file_id,
                          sr.import_record_id,
                          gc_sys_err_repeated AS item_key,
                          'USER_NAME'   AS field_name,
                          sr.user_name  AS field_value,
                          NULL          AS ref_value
                     FROM stg_rec sr
                    WHERE sr.user_name IS NOT NULL
                      AND sr.user_name_count > 1
                    UNION ALL
                   -- participant not eligible receiver
                   SELECT sr.import_file_id,
                          sr.import_record_id,
                          gc_sys_err_not_elig_rcvr   AS item_key,
                          'USER_ID'           AS field_name,
                          TO_CHAR(sr.user_id) AS field_value,
                          NULL                       AS ref_value
                     FROM stg_rec sr,
                          promotion pg,
                          ssi_contest s
                    WHERE sr.user_id IS NOT NULL
                      AND s.promotion_id = pg.promotion_id
                      AND s.ssi_contest_id = p_in_ssi_contest_id
                      AND lower(sr.role) = 'participant'
                      AND NOT EXISTS
                           -- get eligible receiver audience
                                    (SELECT pax_aud.user_id
                                       FROM participant_audience pax_aud,
                                            promo_audience promo_aud,
                                            promotion p
                                  WHERE       pax_aud.user_id          = sr.user_id  and
                                              p.promotion_id           = s.promotion_id and 
                                              pax_aud.audience_id      = promo_aud.audience_id and
                                              promo_audience_type ='SECONDARY' AND
                                              promo_aud.promotion_id   = p.promotion_id 
                                               UNION ALL
                                              SELECT pax.user_id 
                                                                FROM participant pax,
                                                                     promotion p 
                                                                WHERE p.promotion_id           = s.promotion_id and 
                                                                      p.secondary_audience_type = 'allactivepaxaudience' and
                                                                      pax.status = 'active' and 
                                                                      pax.user_id = sr.user_id)
                     UNION ALL
                        -- payout type is other and required columns are empty
                     SELECT sr.import_file_id,
                               sr.import_record_id,
                               gc_sys_err_missing_property                  AS item_key,
                               'PAYOUT_DESCRIPTION/AWARD_ISSUE_VALUE'       AS field_name,
                                NULL                                        AS field_value,
                               'Payout Type is Other'                       AS ref_value
                         FROM stg_rec sr,
                              ssi_contest s  
                        WHERE 
                              s.ssi_contest_id      = p_in_ssi_contest_id
                          AND ((sr.payout_description IS NULL OR sr.award_issue_value IS NULL )    
                                    AND s.payout_type = 'other' 
                                    AND lower(sr.role) = 'participant'
                               )
                     UNION ALL
                        -- payout type is point but other columns have value
                     SELECT sr.import_file_id,
                               sr.import_record_id,
                               gc_sys_err_invalid_property                       AS item_key,
                               'PAYOUT_DESCRIPTION/AWARD_ISSUE_VALUE'            AS field_name,
                                sr.payout_description||'/'||sr.award_issue_value AS field_value,
                               'Payout Type is Points'                           AS ref_value
                         FROM stg_rec sr,
                              ssi_contest s  
                        WHERE 
                              s.ssi_contest_id      = p_in_ssi_contest_id
                          AND ((sr.payout_description IS NOT NULL AND sr.award_issue_value IS NOT NULL )    
                                    AND s.payout_type = 'points' 
                                    AND lower(sr.role) = 'participant'
                               )
                                      ) e);

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      p_out_return_code := 99;
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;       
END p_verify_ssi_atn;

PROCEDURE p_import_ssi_atn
    ( p_in_import_file_id   IN  import_file.import_file_id%TYPE,
      p_in_ssi_contest_id   IN  promotion.promotion_id%TYPE,
      p_out_returncode      OUT NUMBER
    ) 
    IS
  /*******************************************************************************
  -- Purpose: To load the AWARD THEM NOW contest for participant
  --
  -- Person        Date          Comments
  -- -----------   --------      -----------------------------------------------------
  -- Suresh J     11/11/2016     Initial Creation 
  *******************************************************************************/
  -- EXECUTION LOG VARIABLES
   c_process_name           CONSTANT execution_log.process_name%TYPE := UPPER('p_import_ssi_atn');
   v_msg                    execution_log.text_line%TYPE;
   v_rec_cnt                INTEGER;
   c_release_level          execution_log.release_level%type := '1';
   v_execution_log_msg      execution_log.text_line%TYPE;
   v_stage                  VARCHAR2(500);
   l_rec_cnt                INTEGER;
       
BEGIN

     v_stage := 'MERGE ssi_contest_participant :';
     MERGE INTO ssi_contest_participant d
               USING ( -- get staged data
                        SELECT  p_in_ssi_contest_id         as ssi_contest_id, 
                                s.user_id                   as user_id, 
                                s.activity_amount           as activity_amount,
                                s.payout_amount             as payout_amount,
                                CASE 
                                WHEN NVL(sc.payout_type,'x') = 'points' THEN
                                     s.payout_amount
                                ELSE
                                     s.award_issue_value
                                END                         as objective_payout, 
                                s.award_issue_value         as award_issue_value, 
                                s.activity_description      as activity_description, 
                                s.payout_description        as payout_description, 
                                sc.payout_type              as payout_type,
                                atn.issuance_number         as issuance_number
                        FROM stage_ssi_atn_import s,
                             ssi_contest sc,
                             ssi_contest_atn atn
                        WHERE     s.import_file_id  = p_in_import_file_id  AND
                                  lower(s.role)            = 'participant' AND
                                  sc.ssi_contest_id = p_in_ssi_contest_id  AND
                                  sc.ssi_contest_id = atn.ssi_contest_id (+)
                             -- skip records marked as erred
                                 AND s.import_record_id NOT IN (SELECT e.import_record_id
                                                                  FROM import_record_error e
                                                                 WHERE e.import_file_id =
                                                                          p_in_import_file_id))
                     s
                  ON (    d.ssi_contest_id  = s.ssi_contest_id AND
                          d.user_id         = s.user_id
                          
                          )
          WHEN MATCHED
          THEN
             UPDATE -- staged data over lays database values
             SET
                d.objective_amount              = s.activity_amount,
                d.objective_payout              = s.objective_payout,
                d.activity_description          = s.activity_description,
                d.award_issuance_number         = s.issuance_number,
                -- track update
                d.modified_by                   = g_created_by,
                d.date_modified                 = SYSDATE,
                d.version                       = d.version + 1
                     -- only update if data differs (decode handles null values)
                     WHERE NOT (    DECODE (d.objective_amount,                 s.activity_amount ,             1,0) = 1
                                AND DECODE (d.objective_payout,                 s.objective_payout,             1,0) = 1
                                AND DECODE (d.activity_description,             s.activity_description,         1,0) = 1
                                )
          WHEN NOT MATCHED THEN   
          INSERT
          (
           ssi_contest_participant_id, 
           ssi_contest_id, 
           user_id, 
           award_issuance_number,
           objective_amount,
           objective_payout,
           activity_description,
           created_by, 
           date_created, 
           modified_by, 
           date_modified, 
           version       
          )
          VALUES 
          (
           ssi_contest_participant_pk_sq.nextval, 
           s.ssi_contest_id, 
           s.user_id, 
           s.issuance_number,
           s.activity_amount,
           s.objective_payout,
           s.activity_description,
           g_created_by, 
           SYSDATE, 
           NULL, 
           NULL, 
           0       
          );
       l_rec_cnt := SQL%ROWCOUNT;
       prc_execution_log_entry(c_process_name,c_release_level,'INFO',v_stage||l_rec_cnt||' record(s) processed'
                               ||' p_in_import_file_id: '||p_in_import_file_id, null);
          

    v_stage := 'MERGE ssi_contest_manager :';
    MERGE INTO ssi_contest_manager d
               USING (                                          -- get staged data
                        SELECT  p_in_ssi_contest_id ssi_contest_id, 
                                s.user_id
                        FROM stage_ssi_atn_import s
                        WHERE     s.import_file_id = p_in_import_file_id AND
                                  lower(s.role)           = 'manager'
                             -- skip records marked as erred
                                 AND s.import_record_id NOT IN (SELECT e.import_record_id
                                                                  FROM import_record_error e
                                                                 WHERE e.import_file_id =
                                                                          p_in_import_file_id))
                     s
                  ON (    d.ssi_contest_id  = s.ssi_contest_id AND
                          d.user_id         = s.user_id  
                            
                          )
          WHEN NOT MATCHED THEN   
          INSERT
          (
           ssi_contest_manager_id, 
           ssi_contest_id, 
           user_id, 
           created_by, 
           date_created, 
           version       
          )
          VALUES 
          (
           ssi_contest_manager_pk_sq.nextval, 
           s.ssi_contest_id, 
           s.user_id, 
           g_created_by, 
           SYSDATE, 
           0       
          );
       l_rec_cnt := SQL%ROWCOUNT;
       prc_execution_log_entry(c_process_name,c_release_level,'INFO',v_stage||l_rec_cnt||' record(s) processed'
                               ||' p_in_import_file_id: '||p_in_import_file_id, null);


    v_stage := 'MERGE ssi_contest_superviewer :';
    MERGE INTO ssi_contest_superviewer d
               USING (                                          -- get staged data
                        SELECT  p_in_ssi_contest_id ssi_contest_id, 
                                s.user_id
                        FROM stage_ssi_atn_import s
                        WHERE     s.import_file_id = p_in_import_file_id AND
                                  lower(s.role)           = 'superviewer'
                             -- skip records marked as erred
                                 AND s.import_record_id NOT IN (SELECT e.import_record_id
                                                                  FROM import_record_error e
                                                                 WHERE e.import_file_id =
                                                                          p_in_import_file_id))
                     s
                  ON (    d.ssi_contest_id  = s.ssi_contest_id AND
                          d.user_id         = s.user_id  
                            
                          )
          WHEN NOT MATCHED THEN   
          INSERT
          (
           ssi_contest_superviewer_id, 
           ssi_contest_id, 
           user_id, 
           created_by, 
           date_created, 
           version       
          )
          VALUES 
          (
           ssi_contest_superviewer_pk_sq.nextval, 
           s.ssi_contest_id, 
           s.user_id, 
           g_created_by, 
           SYSDATE, 
           0       
          );
       l_rec_cnt := SQL%ROWCOUNT;
       prc_execution_log_entry(c_process_name,c_release_level,'INFO',v_stage||l_rec_cnt||' record(s) processed'
                               ||' p_in_import_file_id: '||p_in_import_file_id, null);
       p_out_returncode := 0;
EXCEPTION
   WHEN OTHERS THEN
      p_out_returncode := 99;
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ', ' || SQLERRM, NULL);
      RAISE;
END p_import_ssi_atn;

PROCEDURE p_ssi_atn_verify_import
    ( p_import_file_id     IN  import_file.import_file_id%TYPE,
      p_load_type          IN  VARCHAR2,
      p_ssi_contest_id     IN  ssi_contest.ssi_contest_id%TYPE,
      p_total_error_rec     OUT NUMBER,
      p_out_returncode      OUT NUMBER
) IS

/*******************************************************************************
  -- Purpose: To load the AWARD_THEM_NOW contest for participant from bi-admin screen
  --
  -- Person        Date          Comments
  -- -----------   --------      -----------------------------------------------------
  -- nagarajs     11/17/2016     Initial Creation 
  *******************************************************************************/
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name          CONSTANT execution_log.process_name%type := 'PRC_SSI_ATN_VERIFY_LOAD';
   c_release_level         CONSTANT execution_log.release_level%type := '1';

   v_msg                    execution_log.text_line%TYPE;
   v_total_err_count        INTEGER; -- count of errors
   v_import_record_count    import_file.import_record_count%TYPE;   
   v_out_returncode         NUMBER :=0; 
   v_valid_ssi_contest_id   BOOLEAN := FALSE;
   v_ssi_contest_id         NUMBER;
   
   e_pgm_exit               EXCEPTION;
   
   CURSOR cur_import_file
   ( cv_import_file_id  import_record_error.import_file_id%TYPE
   ) IS
   SELECT f.import_record_count
     FROM import_file f
    WHERE f.import_file_id = cv_import_file_id
      ;

BEGIN
   v_msg := 'Start'
      || ': p_import_file_id >' || p_import_file_id
      || '<, p_load_type >'     || p_load_type      
      || '<, p_ssi_contest_id >'|| p_ssi_contest_id
      || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);
    
   -- initialize variables
   p_out_returncode := 0;
   v_total_err_count := 0;
   
   v_msg := 'OPEN cur_import_file';
   OPEN cur_import_file(p_import_file_id);
   v_msg := 'FETCH cur_import_file';
   FETCH cur_import_file INTO v_import_record_count;
   CLOSE cur_import_file;

   -- get count of any existing errors
   p_total_error_rec := f_get_error_record_count(p_import_file_id);
   v_msg := 'Previous error count >' || p_total_error_rec || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);
   
   BEGIN
     SELECT ssi_contest_id 
       INTO v_ssi_contest_id
       FROM ssi_contest
      WHERE ssi_contest_id = p_ssi_contest_id
        AND status IN ('live','pending','draft','contest_over');
   EXCEPTION
      WHEN OTHERS THEN
        v_valid_ssi_contest_id := TRUE;
   END;
             
   IF v_valid_ssi_contest_id THEN
     INSERT INTO import_record_error 
       ( import_record_error_id,
         import_file_id,
         import_record_id,
         item_key,
         param1,
         param2,
         param3,
         param4,
         created_by,
         date_created
       )
     SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
           sr.import_file_id,
           sr.import_record_id,
           'system.errors.INVALID_SSI_CONTEST' item_key,
           p_ssi_contest_id AS param1,
           NULL AS param2,
           NULL AS param3,
           NULL AS param4,
           g_created_by AS created_by,
           g_timestamp AS date_created
      FROM stage_ssi_atn_import sr
     WHERE import_file_id = p_import_file_id;
     
     RAISE e_pgm_exit;  
   END IF;
   -- report validation errors
   v_msg := 'Call p_verify_ssi_atn';
   p_verify_ssi_atn(p_import_file_id, p_ssi_contest_id, v_total_err_count,v_out_returncode);

   -- get count of any existing errors
   p_total_error_rec := f_get_error_record_count(p_import_file_id);

   IF( p_total_error_rec > 0) THEN
      -- import file has errors
      p_out_returncode := 1;
   END IF;

   -- load file records without errors
   IF (p_load_type = 'L') THEN
      v_msg := 'Call p_import_ssi_atn';
      p_import_ssi_atn(p_import_file_id,p_ssi_contest_id,v_out_returncode);     
      COMMIT; 
      p_out_returncode := NVL(v_out_returncode,0);
   END IF; -- load file

   v_msg := 'Success'
      || ': p_import_file_id >'   || p_import_file_id
      || '<, p_out_returncode >'  || p_out_returncode
      || '<, v_total_err_count >' || v_total_err_count
      || '<, p_total_error_rec >' || p_total_error_rec
      || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

   COMMIT;

EXCEPTION
   WHEN e_pgm_exit THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Invalid contest selected while at verify/load step', NULL);
      p_total_error_rec := v_import_record_count;
      p_out_returncode := 98; 
      COMMIT;
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      ROLLBACK;
      p_total_error_rec := v_import_record_count;
      p_out_returncode := 99;
END p_ssi_atn_verify_import;

PROCEDURE p_ssi_atn_stg_verify_import
    ( p_file_name        IN  import_file.file_name%TYPE,
      p_ssi_contest_id   IN  ssi_contest.ssi_contest_id%TYPE,
      p_in_user_id       IN NUMBER,
      p_total_error_rec     OUT NUMBER,
      p_out_import_file_id  OUT NUMBER,      
      p_out_returncode      OUT NUMBER
) IS


  /*******************************************************************************
  -- Purpose: To load the AWARD_THEM_NOW contest for participant from SSI Contest screen
  --
  -- Person        Date          Comments
  -- -----------   --------      -----------------------------------------------------
  -- nagarajs     11/17/2016     Initial Creation 
  *******************************************************************************/
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name          CONSTANT execution_log.process_name%type := 'P_SSI_ATN_STG_VERIFY_IMPORT';
   c_release_level         CONSTANT execution_log.release_level%type := '1';

   v_msg                    execution_log.text_line%TYPE;
   v_total_err_count        INTEGER; -- count of errors
   v_import_record_count    import_file.import_record_count%TYPE;   
   v_out_returncode         NUMBER :=0; 
   v_error_count            NUMBER := 0;
   v_valid_ssi_contest_id   BOOLEAN := FALSE;
   v_ssi_contest_id         NUMBER;
   v_import_file_id         import_file.import_file_id%TYPE;  
   v_load_type              VARCHAR2(15); 
   
   e_pgm_exit               EXCEPTION;
   
   CURSOR cur_import_file
   ( cv_import_file_id  import_record_error.import_file_id%TYPE
   ) IS
   SELECT f.import_record_count
     FROM import_file f
    WHERE f.import_file_id = cv_import_file_id
      ;

BEGIN
   v_msg := 'Start'
      || ': p_file_name >' || p_file_name
      || '<, p_ssi_contest_id >'     || p_ssi_contest_id      
      || '<, p_in_user_id >'|| p_in_user_id
      ||'<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);
    
   -- initialize variables
   p_out_returncode := 0;
   v_total_err_count := 0;
   g_created_by  := p_in_user_id;
   
   BEGIN
     SELECT user_name
       INTO g_user_name
       FROM application_user
      WHERE user_id = p_in_user_id;
   EXCEPTION
     WHEN OTHERS THEN
       g_user_name := p_in_user_id;
   END;
   
   v_msg := 'Call p_stage_ssi_atn';
   p_stage_ssi_atn(p_file_name, v_out_returncode);
   
   IF v_out_returncode = 0 THEN
   
       SELECT MAX(import_file_id)
         INTO v_import_file_id
         FROM import_file
        WHERE file_type = 'ssicontestatn';

       p_out_import_file_id := v_import_file_id;
        
       v_msg := 'OPEN cur_import_file';
       OPEN cur_import_file(v_import_file_id);
       v_msg := 'FETCH cur_import_file';
       FETCH cur_import_file INTO v_import_record_count;
       CLOSE cur_import_file;

       -- get count of any existing errors
       p_total_error_rec := f_get_error_record_count(v_import_file_id);
       v_msg := 'Previous error count >' || p_total_error_rec || '<';
       prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);
       
       BEGIN
         SELECT ssi_contest_id 
           INTO v_ssi_contest_id
           FROM ssi_contest
          WHERE ssi_contest_id = p_ssi_contest_id
            AND status IN ('live','pending','draft','contest_over');
       EXCEPTION
          WHEN OTHERS THEN
            v_valid_ssi_contest_id := TRUE;
       END;
                 
       IF v_valid_ssi_contest_id THEN
         INSERT INTO import_record_error 
           ( import_record_error_id,
             import_file_id,
             import_record_id,
             item_key,
             param1,
             param2,
             param3,
             param4,
             created_by,
             date_created
           )
         SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
               sr.import_file_id,
               sr.import_record_id,
               'system.errors.INVALID_SSI_CONTEST' item_key,
               p_ssi_contest_id AS param1,
               NULL AS param2,
               NULL AS param3,
               NULL AS param4,
               g_created_by AS created_by,
               g_timestamp AS date_created
          FROM stage_ssi_atn_import sr
         WHERE import_file_id = v_import_file_id;
         
         RAISE e_pgm_exit;  
       END IF;
       
       g_load_type := 'V';
       
       -- report validation errors
       v_msg := 'Call p_verify_ssi_atn';
       p_verify_ssi_atn(v_import_file_id, p_ssi_contest_id, v_total_err_count,v_out_returncode);

       -- get count of any existing errors
       p_total_error_rec := f_get_error_record_count(v_import_file_id);


       -- load file records without errors
       IF (v_out_returncode = 0) THEN
       
          p_upd_import_file
               ( v_import_file_id,
                 'ver',
                 NULL,
                 v_error_count
               );
               
          g_load_type := 'L';
          v_msg := 'Call p_import_ssi_atn';
          p_import_ssi_atn(v_import_file_id,p_ssi_contest_id,v_out_returncode);     
          COMMIT; 
          IF NVL(v_out_returncode,0) = 0 THEN
            p_out_returncode := NVL(v_out_returncode,0);
            p_upd_import_file
               ( v_import_file_id,
                 'imp',
                 NULL,
                 v_error_count
               );
               
              v_msg := 'Load Success'
              || ': v_import_file_id >'   || v_import_file_id
              || '<, p_out_returncode >'  || p_out_returncode
              || '<, v_total_err_count >' || v_total_err_count
              || '<, p_total_error_rec >' || p_total_error_rec
              || '<';
             prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);
          ELSE
            p_out_returncode := NVL(v_out_returncode,0);
            p_upd_import_file
               ( v_import_file_id,
                 'imp_fail',
                 NULL,
                 v_error_count
               );
               
              v_msg := 'Load Failed'
              || ': v_import_file_id >'   || v_import_file_id
              || '<, p_out_returncode >'  || p_out_returncode
              || '<, v_total_err_count >' || v_total_err_count
              || '<, p_total_error_rec >' || p_total_error_rec
              || '<';
             prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);
          END IF;
       ELSE
          p_upd_import_file
           ( v_import_file_id,
             'ver_fail',
             NULL,
             v_error_count
           );
           
          v_msg := 'Verify Failed'
          || ': v_import_file_id >'   || v_import_file_id
          || '<, p_out_returncode >'  || p_out_returncode
          || '<, v_total_err_count >' || v_total_err_count
          || '<, p_total_error_rec >' || p_total_error_rec
          || '<';
         prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);
       END IF; -- load file

   ELSE
     SELECT import_record_error_count
       INTO p_total_error_rec
       FROM import_file
      WHERE import_file_id = v_import_file_id;
     p_out_returncode := 99; 
     prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Error at stage call', NULL);
   END IF;
   COMMIT;

EXCEPTION
   WHEN e_pgm_exit THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Invalid contest selected while at verify/load step', NULL);
      p_total_error_rec := v_import_record_count;
      p_out_returncode := 99; 
       p_upd_import_file
       ( v_import_file_id,
         'ver_fail',
         NULL,
         v_error_count
       );
      COMMIT;
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      ROLLBACK;
      p_total_error_rec := v_import_record_count;
      p_out_returncode := 99;
      SELECT DECODE(g_load_type, 'V','ver_fail','L','imp_fail')
        INTO v_load_type
        FROM DUAL;
      p_upd_import_file
       ( v_import_file_id,
         v_load_type,
         NULL,
         v_error_count
       );
END p_ssi_atn_stg_verify_import;

END pkg_ssi_atn_load; -- End of Package
/
