CREATE OR REPLACE PROCEDURE prc_remove_all_reporting_data
  (p_out_returncode OUT NUMBER)
IS
  
  CURSOR cur_tbl IS
    SELECT DISTINCT table_name
      FROM user_tables
     WHERE table_name LIKE 'RPT%';

  --execution log variables
  C_process_name          CONSTANT execution_log.process_name%type  := 'PRC_REMOVE_ALL_REPORTING_DATA';
  C_release_level         CONSTANT execution_log.release_level%type := '1';
  C_severity_i            CONSTANT execution_log.severity%TYPE := 'INFO';  
  C_severity_e            CONSTANT execution_log.severity%TYPE := 'ERROR';

  v_stage  VARCHAR2(100);

BEGIN

  prc_execution_log_entry(C_process_name, C_release_level, C_severity_i,
                          'Process Started',
                          NULL);
  COMMIT;

  FOR rec_tbl IN cur_tbl LOOP

    v_stage := 'Truncate table '||rec_tbl.table_name;
    EXECUTE IMMEDIATE 'TRUNCATE TABLE '||rec_tbl.table_name; 
  
  END LOOP;

  prc_execution_log_entry(C_process_name, C_release_level, C_severity_i,
                          'Process Completed',
                          NULL);
  COMMIT;
  p_out_returncode := 0;
  
EXCEPTION
  WHEN OTHERS THEN
    prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                            'Failed at Stage: '||v_stage||' --> '||SQLERRM,
                            NULL);
    COMMIT;
    p_out_returncode := 99;
END;
/