CREATE OR REPLACE PROCEDURE prc_clear_lob_space
(p_out_returncode OUT NUMBER)
IS

   v_lob_size            NUMBER (12) := 0;

   -- EXECUTION LOG VARIABLES
   v_process_name          execution_log.process_name%type  := 'prc_clear_lob_space' ;
   v_release_level         execution_log.release_level%type := '1';
   
BEGIN

SELECT ROUND (a.bytes / (1024 * 1024), 0) INTO v_lob_size
  FROM sys.user_segments a, user_lobs b
 WHERE     A.SEGMENT_NAME = B.SEGMENT_NAME
       AND a.segment_type LIKE '%LOB%'
       AND b.table_name = 'QRTZ_TRIGGERS';
       
       IF v_lob_size > 100 THEN
       
       EXECUTE IMMEDIATE ('alter table qrtz_triggers modify lob(job_data) (shrink space cascade)');
       
       END IF;


p_out_returncode := 0;

prc_execution_log_entry(v_process_name,v_release_level,'INFO','lob_size : '||v_lob_size,null);

EXCEPTION WHEN OTHERS THEN
p_out_returncode := 99;
prc_execution_log_entry(v_process_name,v_release_level,'ERROR',SQLERRM,null);

END;
/