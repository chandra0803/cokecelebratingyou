CREATE OR REPLACE PROCEDURE prc_alter_tbl_column_char_used
 (p_out_return_code OUT NUMBER)
IS

/*-----------------------------------------------------------------------------------------

  Purpose: Script to Alter columns with char_used = 'B' to char_used = 'C'
           Alter script will convert colmn data_type from varchar2(x byte) to varchar2(x char)

  Modification History

  Person         Date         Comments
  ------------   ----------  ---------------------------------------------------------------
  Arun S         01/23/2017   Initial version.

------------------------------------------------------------------------------------------*/

  CURSOR cur_cols IS
    SELECT table_name,
           column_name, 
           data_type||CASE
                      WHEN data_precision IS NOT NULL AND NVL(data_scale,0) > 0 THEN '('||data_precision||','||data_scale||')'
                      WHEN data_precision IS NOT NULL AND NVL(data_scale,0) = 0 THEN '('||data_precision||')'
                      WHEN data_precision IS NULL AND data_scale IS NOT NULL THEN '(*,'||data_scale||')'
                      WHEN char_length > 0 THEN '('||char_length|| CASE char_used 
                                                                     WHEN 'B' THEN ' BYTE'
                                                                     WHEN 'C' THEN ' CHAR'
                                                                     ELSE NULL 
                                                                   END||')'
                       END||DECODE(nullable, 'N', ' NOT NULL') AS data_type_current,
           data_type||CASE
                      WHEN data_precision IS NOT NULL AND NVL(data_scale,0) > 0 THEN '('||data_precision||','||data_scale||')'
                      WHEN data_precision IS NOT NULL AND NVL(data_scale,0) = 0 THEN '('||data_precision||')'
                      WHEN data_precision IS NULL AND data_scale IS NOT NULL THEN '(*,'||data_scale||')'
                      WHEN char_length > 0 THEN '('||char_length|| ' CHAR'||')'
                       END||DECODE(nullable, 'N', ' NOT NULL') AS  data_type_new       
           ,char_used
      FROM user_tab_columns
     WHERE char_used = 'B'
       AND data_type LIKE '%CHAR%'
       --AND table_name = 'TEST_COL_DTYPE2'
       ;

  --Constants
  v_release_level           CONSTANT execution_log.release_level%type := 1.0;
  v_proc_name               CONSTANT execution_log.process_name%TYPE := UPPER('prc_alter_tbl_column_char_used');
  v_return_code_success     CONSTANT NUMBER := pkg_const.gc_return_code_success;
  v_return_code_failure     CONSTANT NUMBER := pkg_const.gc_return_code_failure;
  v_error                   CONSTANT execution_log.severity%TYPE := pkg_const.gc_error;
  v_debug                   CONSTANT execution_log.severity%TYPE := pkg_const.gc_debug;
  v_warn                    CONSTANT execution_log.severity%TYPE := pkg_const.gc_warn;
  v_info                    CONSTANT execution_log.severity%TYPE := pkg_const.gc_info;

  v_stage                   execution_log.text_line%TYPE; 
  v_sql                     execution_log.text_line%TYPE;
  v_perfix                  VARCHAR2(10);
  v_counter                 NUMBER := 0;

BEGIN

  prc_execution_log_entry(v_proc_name, v_release_level, v_info, 'Procedre Started', NULL);

  FOR rec_cols IN cur_cols LOOP

    v_stage := 'Build alter script';
    v_sql := 'ALTER TABLE '||rec_cols.table_name||' MODIFY '||rec_cols.column_name||' '||rec_cols.data_type_new;

    --dbms_output.put_line('v_sql: '||v_sql);
    
    v_stage := 'Execute alter script';
    EXECUTE IMMEDIATE v_sql;

    v_counter := v_counter + 1;
    
  END LOOP;

  BEGIN
  
    SELECT UPPER(USER) 
      INTO v_perfix
      FROM dual;
  
  EXCEPTION
    WHEN OTHERS THEN
      v_perfix := NULL;
  END;

  IF v_counter > 0 AND v_perfix IS NOT NULL THEN

    BEGIN
       v_stage := 'Compile invalid objects for the schema: '||v_perfix;
      DBMS_UTILITY.COMPILE_SCHEMA(SCHEMA => v_perfix, compile_all => FALSE);
    END;
  
  END IF;

  prc_execution_log_entry(v_proc_name, v_release_level, v_info, 'Procedre Completed', NULL);
  p_out_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code := 99;
    prc_execution_log_entry(v_proc_name, v_release_level, v_error, 'Stage: '||v_stage||'~Script:'||v_sql||'~'||'Error:' || SQLERRM, NULL);
END;
/
DECLARE 
  P_OUT_RETURN_CODE NUMBER;
BEGIN 
  p_out_return_code := null;
  prc_alter_tbl_column_char_used ( p_out_return_code );
  COMMIT; 
  dbms_output.put_line('p_out_return_code:'||p_out_return_code);
END;
/
