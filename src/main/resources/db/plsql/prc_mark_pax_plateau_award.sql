CREATE OR REPLACE PROCEDURE prc_mark_pax_plateau_award 
  (p_out_returncode OUT VARCHAR2) IS
  

/*******************************************************************************
-- Purpose: An overnight process that marks the selected audience as 
--          "Plateau Award Only" in the participant properties. 
--          Upadates participant.giftcode_only to 1 based on 
--          audience.giftcode_only 

-- Person        Date        Comments
-- -----------   ----------  ---------------------------------------------------
-- Arun S        12/06/2011  Initial creation

*******************************************************************************/ 

  CURSOR cur_audience IS
    SELECT audience_id
      FROM audience
     WHERE giftcode_only = 1;  

  CURSOR cur_pax (v_audience_id audience.audience_id%TYPE) IS
    SELECT user_id
      FROM participant_audience
     WHERE audience_id = v_audience_id;
  
  --Execution log variables
  v_process_name            execution_log.process_name%type  := 'prc_mark_pax_plateau_award';
  v_release_level           execution_log.release_level%type := '1';
  v_log_msg                 execution_log.text_line%TYPE;
   
  --Counts
  v_recs_cnt                INTEGER := 0;
  v_recs_upd_true_cnt       INTEGER := 0;
  v_recs_upd_false_cnt      INTEGER := 0;
  v_recs_err_cnt            INTEGER := 0;

  --Procedure variables
  v_stage                   VARCHAR2(500);

BEGIN

  v_stage := 'Write start to execution_log table';
  prc_execution_log_entry(v_process_name,
                          v_release_level,
                          'INFO',
                          'Procedure Started', 
                          NULL);

  p_out_returncode := '00';

  BEGIN
    v_stage := 'Update giftcode_only flag to false for the Pax whose flag is true';
    UPDATE participant
       SET giftcode_only = 0
     WHERE giftcode_only = 1;
    
    v_recs_upd_false_cnt := SQL%ROWCOUNT;
  END;
  
  FOR rec_audience IN cur_audience LOOP
    
    FOR rec_pax IN cur_pax (rec_audience.audience_id) LOOP

      v_recs_cnt := v_recs_cnt + 1;
      
      BEGIN
        v_stage := 'Update participant for User id: '||rec_pax.user_id;
        UPDATE participant
           SET giftcode_only = 1
         WHERE user_id = rec_pax.user_id;
      EXCEPTION
        WHEN OTHERS THEN
          v_recs_err_cnt := v_recs_err_cnt + 1;
          prc_execution_log_entry(v_process_name,
                                  v_release_level,
                                  'ERROR',
                                  'When Others at Stage: '||v_stage||
                                  ' Message: '||SQLERRM, 
                                  NULL);
             
      END; 
      v_recs_upd_true_cnt := v_recs_upd_true_cnt + 1;
      
      --IF MOD(v_recs_upd_true_cnt, 2) = 0 THEN
        --COMMIT;
      --END IF;
             
    END LOOP;
    
  END LOOP;

  --COMMIT;
  
  v_log_msg := 'Procedure Completed'||CHR(13)||
               'Initial Giftcode only flag set to false count: '||v_recs_upd_false_cnt||CHR(13)||
               'Giftcode only flag set to true count: '||v_recs_upd_true_cnt||CHR(13)||
               'Error record count: '||v_recs_err_cnt; 
  
  prc_execution_log_entry(v_process_name,
                          v_release_level,
                          'INFO',
                          v_log_msg, 
                          NULL);

EXCEPTION
  WHEN OTHERS THEN
    p_out_returncode := '99';
    prc_execution_log_entry(v_process_name,
                            v_release_level,
                            'ERROR',
                            'When Others at Stage: '||v_stage||
                            ' Message: '||SQLERRM, 
                            NULL);

END;
/
