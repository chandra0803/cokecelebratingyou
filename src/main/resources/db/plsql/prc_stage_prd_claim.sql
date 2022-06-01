CREATE OR REPLACE PROCEDURE prc_stage_prd_claim(
                              p_file_name      IN VARCHAR2,
                              p_out_returncode OUT NUMBER
                              )
   IS
/*******************************************************************************
--
--
-- Purpose: To upload product claim data from file.
            This is a two step load. First step loads data into the stage_prd_claim_import_record table.
            Step 2 validates and loads data into the 3 tables - .
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------  ----------  -------------------------------------------
-- Percy M.   04/19/06    Initial Creation
-- D Murray   04/28/2006  Update import file table to stg_fail if file load fails (Bug 12020).
--                        Removed all move file code - 9i can use prc_move_load_file_9i procedure.
--                        Moved recs_in_file_cnt before parsing, changed call to prc_insert_stg_prd_claim
--                        to have a return code, only counting claims_loaded_cnt if insert successful on 
--                        header records, added v_error_count to count any records that fail during parsing 
--                        or during insert to stage table on header record, changed dbms_outputs to 
--                        execution_log entries. (Bug 12148)  Changed v_stage to have better messaging and
--                        added v_field_cnt for parsing errors.  Removed v_error_found logic, all records
--                        need to be loaded to the stage tables - errors will be added to the 
--                        import_record_error table and will not get loaded to the final tables.
-- Percy M.   06/20/06    Fixed import_record_id mismatch.
-- Raju N     07/25/06    Removed the invalid property check for the form elelment
--                        name BUG# 13055. Fixed the code to populate the parm2 column
--                        with the actual value from the file in case of an 
--                        invalid_property error
-- Raju N     07/26/06    Changed the code to follow the file format for the
--                         product record type.
--R K Goyal      05/15/2007 File name having path also, modified code to remove
-- Arun S     01/07/2009  Changes to replace UC4
*******************************************************************************/


  c_delimiter                  CONSTANT  VARCHAR2(10):='|';

  v_count                      NUMBER := 0; --for commit count
  v_stage                      VARCHAR2(100);
  v_return_code                NUMBER(5);  -- return from insert call
  v_header_rec                 BOOLEAN := FALSE ;

  --UTL_FILE
  v_in_file_handler            UTL_FILE.file_type;
  --v_path_name                  os_propertyset.string_val%TYPE:=
  --                                   fnc_get_system_variable('import.file.utl_path','db_utl_path');
  v_text                       VARCHAR2(32767);
  v_file_name                  VARCHAR2(500); 
  v_directory_path             VARCHAR2 (4000);
  v_directory_name             VARCHAR2 (30);
  
  -- record type declaration
  rec_stg_prd_claim            stage_prd_claim_import_record%ROWTYPE;
  rec_import_file              import_file%ROWTYPE;
  rec_import_record_error      import_record_error%ROWTYPE;

  --prd_claim LOAD
  v_import_record_id           stage_prd_claim_import_record.import_record_id%TYPE;
  v_hdr_rec_id                 stage_prd_claim_import_record.import_record_id%TYPE;
  v_node_name                  stage_prd_claim_import_record.node_name%TYPE;
  v_import_file_id             stage_prd_claim_import_record.import_file_id%TYPE;
  v_record_type                stage_prd_claim_import_record.record_type%TYPE;
  v_form_element_id             STAGE_PRD_CLAIM_IMP_FLD_RECORD.cfse_id%TYPE;
  v_element_name               stage_prd_claim_import_record.form_element_name%TYPE;
  v_element_value              stage_prd_claim_import_record.form_element_value%TYPE;
  v_product_name               stage_prd_claim_import_record.product_name%TYPE;
  v_product_id                 stage_prd_claim_imp_prd_record.product_id%TYPE;
  v_header_record_id           NUMBER;
  
 -- v_characteristic_name1        stage_prd_claim_import_record.characteristic_name1%TYPE;
  --v_characteristic_value1       stage_prd_claim_import_record.characteristic_value1%TYPE;
  
  v_characteristic_id1         VARCHAR2(40);
  v_characteristic_name1       stage_prd_claim_imp_prd_record.product_characteristic_name1%TYPE;
  v_characteristic_value1      stage_prd_claim_imp_prd_record.product_characteristic_value1%TYPE;

  v_characteristic_id2         VARCHAR2(40);
  v_characteristic_name2       stage_prd_claim_imp_prd_record.product_characteristic_name2%TYPE;
  v_characteristic_value2      stage_prd_claim_imp_prd_record.product_characteristic_value2%TYPE;

  v_characteristic_id3         VARCHAR2(40);
  v_characteristic_name3       stage_prd_claim_imp_prd_record.product_characteristic_name3%TYPE;
  v_characteristic_value3      stage_prd_claim_imp_prd_record.product_characteristic_value3%TYPE;

  v_characteristic_id4         VARCHAR2(40);
  v_characteristic_name4       stage_prd_claim_imp_prd_record.product_characteristic_name4%TYPE;
  v_characteristic_value4      stage_prd_claim_imp_prd_record.product_characteristic_value4%TYPE;
  
  v_characteristic_id5         VARCHAR2(40);
  v_characteristic_name5       stage_prd_claim_imp_prd_record.product_characteristic_name5%TYPE;
  v_characteristic_value5      stage_prd_claim_imp_prd_record.product_characteristic_value5%TYPE;
  v_quantity                   stage_prd_claim_import_record.quantity%TYPE ;
  
  v_import_record_error_id     import_record_error.import_record_error_id%type;

  v_user_name                  stage_prd_claim_import_record.user_name%TYPE;
  v_user_id                    STAGE_PRD_CLAIM_IMP_RECORD.submitter_user_id%TYPE ;
  v_node_id                    STAGE_PRD_CLAIM_IMP_RECORD.track_to_node_id%TYPE ;

  v_characteristic_ctr         NUMBER := 0 ;

  -- Execution Log variables
  v_process_name               execution_log.process_name%type  := 'PRC_STAGE_PRD_CLAIM' ;
  v_release_level              execution_log.release_level%type := '1';
  v_execution_log_msg          execution_log.text_line%TYPE;
   
 -- Counts
  v_recs_in_file_cnt           NUMBER(18) := 0;
  v_claim_rec_cnt              INTEGER := 0;
  v_field_cnt                  INTEGER := 0;
  v_error_count                PLS_INTEGER := 0;
  v_error_tbl_count            INTEGER := 0;

  -- EXCEPTIONS
  exit_program_exception       EXCEPTION;


  CURSOR cur_prd_claim_import_record( p_in_import_file_id IN stage_prd_claim_import_record.import_file_id%TYPE )
  IS
   SELECT *
     FROM stage_prd_claim_import_record
    WHERE import_file_id = p_in_import_file_id
      AND import_record_id NOT IN (
                               SELECT IMPORT_RECORD_ID
                               FROM stage_prd_claim_import_record
                               WHERE import_file_id= p_in_import_file_id
                               GROUP BY IMPORT_RECORD_ID HAVING COUNT(DISTINCT RECORD_TYPE) < 3
                               )
    ORDER BY import_record_id, DECODE(UPPER(record_type),'H',1, 'F',2, 'P',3);
 --!!! Do not remove ORDER BY above !!! Important for process logic to work.


BEGIN

-- remove file path from file name if present --
  /*SELECT substr(p_file_name ,instr(p_file_name,'/',-1,1) + 1)
  INTO v_file_name
  FROM DUAL;
  */
  v_stage := 'Write start to execution_log table';
  p_out_returncode := 0;

  prc_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure Started for file '
                          ||p_file_name, NULL );
  COMMIT;

  v_stage := 'Get Directory path and File name from p_file_name';            --01/07/2009 Changes to replace UC4 
  prc_get_directory_file_name ( p_file_name, v_directory_path, v_file_name );

  BEGIN
    v_stage := 'Insert into import_file table';
    SELECT IMPORT_FILE_PK_SQ.NEXTVAL
      INTO v_import_file_id
    FROM dual;
    
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
       substr(v_file_name, 1, instr(v_file_name, '.') - 1) || '_' ||v_import_file_id,
       'product_claim',
       0,
       0,
       'stg_in_process',
       SYSDATE,
       v_process_name,
       1);
    COMMIT;
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
  v_in_file_handler := UTL_FILE.fopen(v_directory_name, v_file_name, 'r');

  LOOP
    --*** move records from text file into the table.**
    v_stage := 'Reset variables';
    v_field_cnt := 0;

    BEGIN
      v_stage := 'Get record';
      UTL_FILE.get_line(v_in_file_handler, v_text);

      IF v_header_rec THEN
        v_header_rec := FALSE;
      ELSE
        v_recs_in_file_cnt := v_recs_in_file_cnt + 1;
        v_stage := 'Parsing record number: '||v_recs_in_file_cnt;

        -- get record type (H, F or P)
        v_field_cnt := v_field_cnt + 1;
        v_record_type := REPLACE(TRIM(FNC_pipe_parse(v_text,1,c_delimiter)),CHR(13));

        IF upper(v_record_type) = 'H' THEN
          v_claim_rec_cnt := v_claim_rec_cnt + 1;
          v_field_cnt := v_field_cnt + 1;
          v_hdr_rec_id  := REPLACE(TRIM(FNC_pipe_parse(v_text,2,c_delimiter)),CHR(13));

          v_field_cnt := v_field_cnt + 1;
          v_user_name   := REPLACE(TRIM(FNC_pipe_parse(v_text,3,c_delimiter)),CHR(13));

          v_field_cnt := v_field_cnt + 1;
          v_node_name   := REPLACE(TRIM(FNC_pipe_parse(v_text,4,c_delimiter)),CHR(13));
          
          v_stage := 'Initialize the record type';
          rec_stg_prd_claim:=NULL;
          rec_stg_prd_claim.import_record_id := v_hdr_rec_id;
          rec_stg_prd_claim.import_file_id   := v_import_file_id;
          rec_stg_prd_claim.record_type      := v_record_type;
          rec_stg_prd_claim.user_name        := v_user_name;
          rec_stg_prd_claim.node_name        := v_node_name;

          --Insert into stage_prd_claim_import_record
          v_stage := 'Insert into stage_prd_claim_import_record table';
          prc_insert_stg_prd_claim(rec_stg_prd_claim, v_return_code);
          IF v_return_code = 99 THEN
            -- only count if error during insert of header record.
            v_error_count := v_error_count + 1;  -- need to count any insert errors
          END IF;
        
        ELSIF upper(v_record_type) = 'F' THEN

          v_field_cnt := v_field_cnt + 1;
          v_hdr_rec_id  := REPLACE(TRIM(FNC_pipe_parse(v_text,2,c_delimiter)),CHR(13));

          v_field_cnt := v_field_cnt + 1;
          v_element_name:= REPLACE(TRIM(FNC_pipe_parse(v_text,3,c_delimiter)),CHR(13));

          v_field_cnt := v_field_cnt + 1;
          v_element_value:= REPLACE(TRIM(FNC_pipe_parse(v_text,4,c_delimiter)),CHR(13));

          v_stage := 'Initialize the record type';
          rec_stg_prd_claim:=NULL;
          rec_stg_prd_claim.import_record_id := v_hdr_rec_id;
          rec_stg_prd_claim.import_file_id   := v_import_file_id;
          rec_stg_prd_claim.record_type      := v_record_type;
          rec_stg_prd_claim.form_element_name:= v_element_name;
          rec_stg_prd_claim.form_element_value:= v_element_value;

          --Insert into stage_prd_claim_import_record
          v_stage := 'Insert into stage_prd_claim_import_record table';
          prc_insert_stg_prd_claim(rec_stg_prd_claim, v_return_code);
        ELSIF upper(v_record_type) = 'P' THEN
          v_field_cnt := v_field_cnt + 1;
          v_hdr_rec_id  := REPLACE(TRIM(FNC_pipe_parse(v_text,2,c_delimiter)),CHR(13));

          v_field_cnt := v_field_cnt + 1;
          v_product_name:= REPLACE(TRIM(FNC_pipe_parse(v_text,3,c_delimiter)),CHR(13));

          v_field_cnt := v_field_cnt + 1;
          v_quantity    := REPLACE(TRIM(FNC_pipe_parse(v_text,4,c_delimiter)),CHR(13));

          v_field_cnt := v_field_cnt + 1;
          v_characteristic_name1:= REPLACE(TRIM(FNC_pipe_parse(v_text,5,c_delimiter)),CHR(13));

          v_field_cnt := v_field_cnt + 1;
          v_characteristic_value1:= REPLACE(TRIM(FNC_pipe_parse(v_text,6,c_delimiter)),CHR(13));

          v_field_cnt := v_field_cnt + 1;
          v_characteristic_name2:= REPLACE(TRIM(FNC_pipe_parse(v_text,7,c_delimiter)),CHR(13));

          v_field_cnt := v_field_cnt + 1;
          v_characteristic_value2:= REPLACE(TRIM(FNC_pipe_parse(v_text,8,c_delimiter)),CHR(13));


          v_field_cnt := v_field_cnt + 1;
          v_characteristic_name3:= REPLACE(TRIM(FNC_pipe_parse(v_text,9,c_delimiter)),CHR(13));

          v_field_cnt := v_field_cnt + 1;
          v_characteristic_value3:= REPLACE(TRIM(FNC_pipe_parse(v_text,10,c_delimiter)),CHR(13));


          v_field_cnt := v_field_cnt + 1;
          v_characteristic_name4:= REPLACE(TRIM(FNC_pipe_parse(v_text,11,c_delimiter)),CHR(13));

          v_field_cnt := v_field_cnt + 1;
          v_characteristic_value4:= REPLACE(TRIM(FNC_pipe_parse(v_text,12,c_delimiter)),CHR(13));


          v_field_cnt := v_field_cnt + 1;
          v_characteristic_name5:= REPLACE(TRIM(FNC_pipe_parse(v_text,13,c_delimiter)),CHR(13));

          v_field_cnt := v_field_cnt + 1;
          v_characteristic_value5:= REPLACE(TRIM(FNC_pipe_parse(v_text,14,c_delimiter)),CHR(13));

          v_stage := 'Initialize the record type';
          rec_stg_prd_claim:=NULL;
          rec_stg_prd_claim.import_record_id      := v_hdr_rec_id;
          rec_stg_prd_claim.import_file_id        := v_import_file_id;
          rec_stg_prd_claim.record_type           := v_record_type;
          rec_stg_prd_claim.product_name          := v_product_name;
          rec_stg_prd_claim.quantity              := v_quantity;
          rec_stg_prd_claim.characteristic_name1  := v_characteristic_name1;
          rec_stg_prd_claim.characteristic_value1 := v_characteristic_value1;
          rec_stg_prd_claim.characteristic_name2  := v_characteristic_name2;
          rec_stg_prd_claim.characteristic_value2 := v_characteristic_value2;
          rec_stg_prd_claim.characteristic_name3  := v_characteristic_name3;
          rec_stg_prd_claim.characteristic_value3 := v_characteristic_value3;
          rec_stg_prd_claim.characteristic_name4  := v_characteristic_name4;
          rec_stg_prd_claim.characteristic_value4 := v_characteristic_value4;
          rec_stg_prd_claim.characteristic_name5  := v_characteristic_name5;
          rec_stg_prd_claim.characteristic_value5 := v_characteristic_value5;

          --Insert into stage_prd_claim_import_record
          v_stage := 'Insert into stage_prd_claim_import_record table';
          prc_insert_stg_prd_claim(rec_stg_prd_claim, v_return_code);
        END IF; -- upper(v_record_type) = 'H'
        
        v_count := v_count + 1;
        IF MOD(v_count, 10) = 0 THEN
          COMMIT;
        END IF;
        
      END IF; -- v_header_rec

    EXCEPTION
      WHEN no_data_found THEN
        -- end of file
        UTL_FILE.fclose(v_in_file_handler);
        EXIT;

      WHEN OTHERS THEN
--        ROLLBACK;
        IF v_stage LIKE 'Parsing%' THEN
          v_stage := v_stage||' field number: '||v_field_cnt;
        END IF;
        prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                                'Stage: '||v_stage||' --> '||SQLERRM,null);
        COMMIT;
    END;
  END LOOP;  -- Main file loop

  prc_execution_log_entry(v_process_name,v_release_level,'INFO','First phase complete (STAGE_PRD_CLAIM_IMPORT_RECORD). Records Inserted: '||v_count,null);
  COMMIT;
  
  v_count := 0;
  
  -- altleast one 'H', 'F', and 'P' should exist, else error.
  INSERT INTO import_record_error (
         IMPORT_RECORD_ERROR_ID,
         IMPORT_FILE_ID,
         IMPORT_RECORD_ID,
         ITEM_KEY,
         PARAM1    ,
--         PARAM2    
         CREATED_BY,
         DATE_CREATED)
         
         SELECT IMPORT_record_error_pk_SQ.NEXTVAL,
                v_import_file_id,
                import_record_id,
                'admin.fileload.errors.MISSING_RECORD',
                'Record Type',
                0,
                SYSDATE
         FROM (
             SELECT IMPORT_RECORD_ID
             FROM stage_prd_claim_import_record
             WHERE import_file_id= v_import_file_id
             GROUP BY IMPORT_RECORD_ID HAVING COUNT(DISTINCT RECORD_TYPE) < 3
             );

  COMMIT;



  FOR v IN cur_prd_claim_import_record(v_import_file_id) LOOP

    IF upper(v.record_type) = 'H' THEN
      v_user_id:=NULL;
      v_node_id:=NULL;

      IF v_characteristic_ctr > 0 THEN
--v_stage:=v_import_record_id || ','||v_product_id ||','||v_quantity||','||v_characteristic_id1||','||v_characteristic_id2;


        v_characteristic_ctr := 0;
      END IF; -- v_characteristic_ctr > 0
      
      -- get record_id that will tie all H, F and P records.
      -- this will also be used to tie to the import_record_error record.
      SELECT import_record_pk_sq.NEXTVAL
        into v_import_record_id
      FROM dual;

      v_stage := 'Check required header field - user_name';
      IF ltrim(rtrim(v.user_name)) IS NULL THEN
        rec_import_record_error := NULL;
        rec_import_record_error.import_Record_id:= v_import_record_id;
        rec_import_record_error.import_file_id  := v_import_file_id ;
        rec_import_record_error.item_key   := 'admin.fileload.errors.MISSING_PROPERTY';
        rec_import_record_error.param1     := 'User Name';
        rec_import_record_error.created_by := 0;

        v_stage := 'Write header error record';
        prc_insert_import_record_error(rec_import_record_error);

      ELSE
        v_stage := 'Check header relational key - user_name';
        BEGIN
          SELECT user_id
            INTO v_user_id
            FROM application_user
           WHERE user_name = v.user_name;
          
        EXCEPTION WHEN OTHERS THEN
          rec_import_record_error := NULL;
          rec_import_record_error.import_Record_id:= v_import_record_id;
          rec_import_record_error.import_file_id  := v_import_file_id ;
          rec_import_record_error.item_key   := 'admin.fileload.errors.INVALID_PROPERTY';
          rec_import_record_error.param1     := 'User Name';
          rec_import_record_error.param2     :=  v.user_name ;
          rec_import_record_error.created_by := 0;

          v_stage := 'Write header error record';
          prc_insert_import_record_error(rec_import_record_error);

        END;
      END IF; -- ltrim(rtrim(v.user_name)) IS NULL

      IF v.node_name IS NOT NULL THEN
        v_stage := 'Check header relational key - node_name';
        BEGIN
           SELECT node_id
             INTO v_node_id
             FROM node
            WHERE name = v.node_name;

        EXCEPTION WHEN OTHERS THEN
           rec_import_record_error := NULL;
           rec_import_record_error.import_Record_id:= v_import_record_id;
           rec_import_record_error.import_file_id  := v_import_file_id ;
           rec_import_record_error.item_key   := 'admin.fileload.errors.INVALID_PROPERTY';
           rec_import_record_error.param1     := 'Node Name';
           rec_import_record_error.param2     :=  v.node_name ;
           rec_import_record_error.created_by := 0;

           v_stage := 'Write header error record';
           prc_insert_import_record_error(rec_import_record_error);

        END;
      END IF; -- v.node_name IS NOT NULL
      

      v_stage := 'Insert header into stage_prd_claim_imp_record table';
      BEGIN
        INSERT INTO STAGE_PRD_CLAIM_IMP_RECORD
                (import_record_id,
                 import_file_id,
                 action_type,
                 submitter_user_id,
                 submitter_user_name,
                 track_to_node_id,
                 track_to_node_name,
                 created_by,
                 date_created)
        VALUES    (v_import_record_id,
                 v_import_file_id,
                 'add',
                 v_user_id,
                 v.user_name,
                 v_node_id,
                 v.node_name,
                 0,
                 SYSDATE);
        EXCEPTION WHEN OTHERS THEN
          rec_import_record_error := NULL;
          rec_import_record_error.import_Record_id:= v_import_record_id;
          rec_import_record_error.import_file_id  := v_import_file_id;
          rec_import_record_error.item_key   := 'admin.fileload.errors.INSERT_FAILED';
          rec_import_record_error.param1     := 'Record Insert failed';
          rec_import_record_error.created_by := 0;

          prc_insert_import_record_error(rec_import_record_error);

          prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                              'File: '||v_file_name||' failed at Stage: '||v_stage||
                              ' --> '||SQLERRM, NULL );
          COMMIT;
      END;


 -- perform validations for 'F' type records
    ELSIF upper(v.record_type) = 'F' THEN
      v_form_element_id := NULL;
      v_stage := 'Check required form field - element_name';
      IF ltrim(rtrim(v_element_name)) IS NULL THEN
        rec_import_record_error := NULL;
        rec_import_record_error.import_Record_id:= v_import_record_id;
        rec_import_record_error.import_file_id  := v_import_file_id ;
        rec_import_record_error.item_key   := 'admin.fileload.errors.MISSING_PROPERTY';
        rec_import_record_error.param1     := 'Form element name';
        rec_import_record_error.created_by := 0;

        v_stage := 'Write form field error record';
        prc_insert_import_record_error(rec_import_record_error);
      END IF; -- ltrim(rtrim(v_element_name)) IS NULL


      v_stage := 'Insert form field into stage_prd_claim_imp_fld_record table';
      BEGIN
      INSERT into STAGE_PRD_CLAIM_IMP_FLD_RECORD
            (import_field_record_id,
             import_record_id,
             cfse_id,
             cfse_name,
             cfse_value,
             created_by,
             date_created)
      VALUES
            (PRD_CLAIM_IMP_FLD_RECORD_PK_SQ.NEXTVAL,
             v_import_record_id,
             v_form_element_id,
             v.form_element_name,
             v.form_element_value,
             0,
             SYSDATE);
        EXCEPTION WHEN OTHERS THEN
          rec_import_record_error := NULL;
          rec_import_record_error.import_Record_id:= v_import_record_id;
          rec_import_record_error.import_file_id  := v_import_file_id;
          rec_import_record_error.item_key   := 'admin.fileload.errors.INSERT_FAILED';
          rec_import_record_error.param1     := 'Record Insert failed';
          rec_import_record_error.created_by := 0;

          prc_insert_import_record_error(rec_import_record_error);

          prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                              'File: '||v_file_name||' failed at Stage: '||v_stage||
                              ' --> '||SQLERRM, NULL );
          COMMIT;
      END;

-- perform validations for 'P' type records
    ELSIF upper(v.record_type) = 'P' THEN

      v_characteristic_ctr := v_characteristic_ctr + 1;
      v_product_id := NULL;
      
      v_stage := 'Check required product field - product_name';
      IF ltrim(rtrim(v.product_name)) IS NULL THEN
        rec_import_record_error := NULL;
        rec_import_record_error.import_Record_id:= v_import_record_id;
        rec_import_record_error.import_file_id  := v_import_file_id ;
        rec_import_record_error.item_key   := 'admin.fileload.errors.MISSING_PROPERTY';
        rec_import_record_error.param1     := 'Product Name';
        rec_import_record_error.created_by := 0;

        v_stage := 'Write product error record';
        prc_insert_import_record_error(rec_import_record_error);
        
      ELSE
        v_stage := 'Check product relational key - product_name';
        BEGIN
          SELECT product_id
            INTO v_product_id
            FROM product
           WHERE lower(product_name) = lower(v.product_name);

        EXCEPTION WHEN OTHERS THEN
          rec_import_record_error := NULL;
          rec_import_record_error.import_Record_id:= v_import_record_id;
          rec_import_record_error.import_file_id  := v_import_file_id ;
          rec_import_record_error.item_key   := 'admin.fileload.errors.INVALID_PROPERTY';
          rec_import_record_error.param1     := 'Product Name';
          rec_import_record_error.param2     := lower(v.product_name);
          rec_import_record_error.created_by := 0;

          v_stage := 'Write product error record';
          prc_insert_import_record_error(rec_import_record_error);

        END;
      END IF; -- ltrim(rtrim(v.product_name)) IS NULL
      
      v_product_name := v.product_name;
      v_quantity     := v.quantity;
      v_header_record_id := v.header_record_id;
      
      v_stage := 'Reset fields';
      v_characteristic_id1   := NULL;
      v_characteristic_name1 := NULL;
      v_characteristic_value1:= NULL;
      v_characteristic_id2   := NULL;
      v_characteristic_name2 := NULL;
      v_characteristic_value2:= NULL;
      v_characteristic_id3   := NULL;
      v_characteristic_name3 := NULL;
      v_characteristic_value3:= NULL;
      v_characteristic_id4   := NULL;
      v_characteristic_name4 := NULL;
      v_characteristic_value4:= NULL;
      v_characteristic_id5   := NULL;
      v_characteristic_name5 := NULL;
      v_characteristic_value5:= NULL;
    
          v_stage := 'Check product relational key - Characteristic 1';
          IF ltrim(rtrim(v.characteristic_name1)) IS NOT NULL THEN  -- null check
            v_characteristic_id1   := FNC_GET_PRODUCTCHAR_ID(v.characteristic_name1,v_product_id,'PRODUCT');
            IF v_characteristic_id1 = 'X' THEN
              v_characteristic_id1 := NULL;
              rec_import_record_error := NULL;
              rec_import_record_error.import_Record_id:= v_import_record_id;
              rec_import_record_error.import_file_id  := v_import_file_id ;
              rec_import_record_error.item_key   := 'admin.fileload.errors.INVALID_PROPERTY';
              rec_import_record_error.param1     := 'Characteristic1';
              rec_import_record_error.param2     := v.characteristic_name1 ;
              rec_import_record_error.created_by := 0;

              v_stage := 'Write product error record';
              prc_insert_import_record_error(rec_import_record_error);

            END IF; -- v_characteristic_id1 = 'X'
            v_characteristic_name1 := v.characteristic_name1;
            v_characteristic_value1:= v.characteristic_value1;
          END IF ; -- null check
          v_stage := 'Check product relational key - Characteristic 2';
          IF ltrim(rtrim(v.characteristic_name2)) IS NOT NULL THEN  -- null check
            v_characteristic_id2   := FNC_GET_PRODUCTCHAR_ID(v.characteristic_name2,v_product_id,'PRODUCT');
            IF v_characteristic_id2 = 'X' THEN
              v_characteristic_id2 := NULL;
              rec_import_record_error := NULL;
              rec_import_record_error.import_Record_id:= v_import_record_id;
              rec_import_record_error.import_file_id  := v_import_file_id ;
              rec_import_record_error.item_key   := 'admin.fileload.errors.INVALID_PROPERTY';
              rec_import_record_error.param1     := 'Characteristic2';
              rec_import_record_error.param2     := v.characteristic_name2 ;
              rec_import_record_error.created_by := 0;

              v_stage := 'Write product error record';
              prc_insert_import_record_error(rec_import_record_error);

            END IF; -- v_characteristic_id2 = 'X'
            v_characteristic_name2 := v.characteristic_name2;
            v_characteristic_value2:= v.characteristic_value2;
          END IF ; -- null check
          v_stage := 'Check product relational key - Characteristic 3';
          IF ltrim(rtrim(v.characteristic_name3)) IS NOT NULL THEN  -- null check
            v_characteristic_id3   := FNC_GET_PRODUCTCHAR_ID(v.characteristic_name3,v_product_id,'PRODUCT');
            IF v_characteristic_id3 = 'X' THEN
              v_characteristic_id3 := NULL;
              rec_import_record_error := NULL;
              rec_import_record_error.import_Record_id:= v_import_record_id;
              rec_import_record_error.import_file_id  := v_import_file_id ;
              rec_import_record_error.item_key   := 'admin.fileload.errors.INVALID_PROPERTY';
              rec_import_record_error.param1     := 'Characteristic3';
              rec_import_record_error.param2     := v.characteristic_name3 ;
              rec_import_record_error.created_by := 0;

              v_stage := 'Write product error record';
              prc_insert_import_record_error(rec_import_record_error);

            END IF; -- v_characteristic_ctr = 3
            v_characteristic_name3 := v.characteristic_name3;
            v_characteristic_value3:= v.characteristic_value3;
          END IF ; -- null check
          v_stage := 'Check product relational key - Characteristic 4';
          IF ltrim(rtrim(v.characteristic_name4)) IS NOT NULL THEN  -- null check
            v_characteristic_id4   := FNC_GET_PRODUCTCHAR_ID(v.characteristic_name4,v_product_id,'PRODUCT');
            IF v_characteristic_id4 = 'X' THEN
              v_characteristic_id4 := NULL;
              rec_import_record_error := NULL;
              rec_import_record_error.import_Record_id:= v_import_record_id;
              rec_import_record_error.import_file_id  := v_import_file_id ;
              rec_import_record_error.item_key   := 'admin.fileload.errors.INVALID_PROPERTY';
              rec_import_record_error.param1     := 'Characteristic4';
              rec_import_record_error.param2     := v.characteristic_name4 ;
              rec_import_record_error.created_by := 0;

              v_stage := 'Write product error record';
              prc_insert_import_record_error(rec_import_record_error);

            END IF; -- v_characteristic_ctr = 4
            v_characteristic_name4 := v.characteristic_name4;
            v_characteristic_value4:= v.characteristic_value4;
          END IF ; -- null check
          v_stage := 'Check product relational key - Characteristic 5';
          IF ltrim(rtrim(v.characteristic_name5)) IS NOT NULL THEN  -- null check
            v_characteristic_id5   := FNC_GET_PRODUCTCHAR_ID(v.characteristic_name5,v_product_id,'PRODUCT');
            IF v_characteristic_id5 = 'X' THEN
              v_characteristic_id5 := NULL;
              rec_import_record_error := NULL;
              rec_import_record_error.import_Record_id:= v_import_record_id;
              rec_import_record_error.import_file_id  := v_import_file_id ;
              rec_import_record_error.item_key   := 'admin.fileload.errors.INVALID_PROPERTY';
              rec_import_record_error.param1     := 'Characteristic5';
              rec_import_record_error.param2     := v.characteristic_name5 ;
              rec_import_record_error.created_by := 0;

              v_stage := 'Write product error record';
              prc_insert_import_record_error(rec_import_record_error);

            END IF; -- v_characteristic_ctr = 5
            v_characteristic_name5 := v.characteristic_name5;
            v_characteristic_value5:= v.characteristic_value5;
          END IF ; -- null check

        -- insert into product detail table here
        v_stage := 'Insert product into stage_prd_claim_imp_prd_record table';
        BEGIN
        INSERT INTO STAGE_PRD_CLAIM_IMP_PRD_RECORD
                (import_product_record_id,
                 import_record_id,
                 product_id,
                 product_name,    
                 sold_quantity,    
                 product_characteristic_id1,
                 product_characteristic_name1,    
                 product_characteristic_value1,    
                 product_characteristic_id2,
                 product_characteristic_name2,    
                 product_characteristic_value2,    
                 product_characteristic_id3,
                 product_characteristic_name3,    
                 product_characteristic_value3,    
                 product_characteristic_id4,
                 product_characteristic_name4,    
                 product_characteristic_value4,    
                 product_characteristic_id5,
                 product_characteristic_name5,    
                 product_characteristic_value5,    
                 created_by,
                 date_created)
        VALUES
                (PRD_CLAIM_IMP_FLD_RECORD_PK_SQ.NEXTVAL,
--                import_record_pk_sq.NEXTVAL,
                 v_import_record_id,
                 v_product_id,
                 v_product_name,    
                 v_quantity,    
                 v_characteristic_id1,
                 v_characteristic_name1,    
                 v_characteristic_value1,    
                 v_characteristic_id2,
                 v_characteristic_name2,    
                 v_characteristic_value2,    
                 v_characteristic_id3,
                 v_characteristic_name3,    
                 v_characteristic_value3,    
                 v_characteristic_id4,
                 v_characteristic_name4,    
                 v_characteristic_value4,    
                 v_characteristic_id5,
                 v_characteristic_name5,    
                 v_characteristic_value5,    
                 0,
                 SYSDATE);
        EXCEPTION WHEN OTHERS THEN
          rec_import_record_error := NULL;
          rec_import_record_error.import_Record_id:= v_import_record_id;
          rec_import_record_error.import_file_id  := v_import_file_id;
          rec_import_record_error.item_key   := 'admin.fileload.errors.INSERT_FAILED';
          rec_import_record_error.param1     := 'Record Insert failed';
          rec_import_record_error.created_by := 0;

          prc_insert_import_record_error(rec_import_record_error);

          prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                              'File: '||v_file_name||' failed at Stage: '||v_stage||
                              ' --> '||SQLERRM, NULL );
          COMMIT;
      END;


    END IF; -- upper(v_record_type) = 'H'

    v_count           := v_count + 1;
  END LOOP;  -- cur_prd_claim_import_record
  
  v_stage := 'Count distinct records that have an error';
  -- Want to only count the claim record once since only counting by claims
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
         import_record_count = v_claim_rec_cnt,
         import_record_error_count = v_error_count
   where import_file_id = v_import_file_id;

  utl_file.fclose(v_in_file_handler);

  v_stage := 'Write counts and ending to execution_log table';
  prc_execution_log_entry(v_process_name,v_release_level,'INFO','Count of records on file: '||v_claim_rec_cnt||
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
            import_record_count       = v_claim_rec_cnt,
            import_record_error_count = v_error_count
      WHERE import_file_id = v_import_file_id;

      prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                              'File: '||p_file_name||' failed at Stage: '||v_stage||
                              ' --> '||SQLERRM,null);
      COMMIT;
      p_out_returncode := 99;
      
END;
/