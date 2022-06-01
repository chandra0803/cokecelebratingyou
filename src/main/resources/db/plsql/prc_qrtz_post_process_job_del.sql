CREATE OR REPLACE PROCEDURE prc_qrtz_post_process_job_del (p_out_return_code  OUT NUMBER)
AS
 /*******************************************************************************
  -- Purpose: To delete the QRTZ related tables and POST_PROCESS_JOBS table record where trigger_state = ERROR
  --
  -- Person                  Date       Comments
  -- -----------            --------   -----------------------------------------------------
  -- nagarajs              07/14/2016   Initial Creation                               
  *******************************************************************************/
   PRAGMA    AUTONOMOUS_TRANSACTION;
   
   v_stage   VARCHAR2 (300);

BEGIN

   p_out_return_code:=0;
   prc_execution_log_entry ('PRC_QRTZ_POST_PROCESS_JOB_DEL',1,'INFO','Process Started ',NULL);
   
  DELETE FROM QRTZ_BLOB_TRIGGERS qbt
   WHERE EXISTS (SELECT 1
                   FROM qrtz_triggers tr
                  WHERE tr.trigger_state = 'ERROR'
                    AND tr.trigger_name = qbt.trigger_name
                    AND EXISTS ( SELECT 1
                                   FROM post_process_jobs ppj 
                                  WHERE ppj.trigger_name = tr.trigger_name
                               )
                );
                
   DELETE FROM QRTZ_CRON_TRIGGERS qct
   WHERE EXISTS (SELECT 1
                   FROM qrtz_triggers tr
                  WHERE tr.trigger_state = 'ERROR'
                    AND tr.trigger_name = qct.trigger_name
                    AND EXISTS ( SELECT 1
                                   FROM post_process_jobs ppj 
                                  WHERE ppj.trigger_name = tr.trigger_name
                               )
                );
              
   DELETE FROM QRTZ_SIMPROP_TRIGGERS qst
   WHERE EXISTS (SELECT 1
                   FROM qrtz_triggers tr
                  WHERE tr.trigger_state = 'ERROR'
                    AND tr.trigger_name = qst.trigger_name
                    AND EXISTS ( SELECT 1
                                   FROM post_process_jobs ppj 
                                  WHERE ppj.trigger_name = tr.trigger_name
                               )
                );
                
   DELETE FROM QRTZ_SIMPLE_TRIGGERS qst
   WHERE EXISTS (SELECT 1
                   FROM qrtz_triggers tr
                  WHERE tr.trigger_state = 'ERROR'
                    AND tr.trigger_name = qst.trigger_name
                    AND EXISTS ( SELECT 1
                                   FROM post_process_jobs ppj 
                                  WHERE ppj.trigger_name = tr.trigger_name
                               )
                );
                              
   DELETE FROM qrtz_triggers tr
    WHERE tr.trigger_state = 'ERROR'
      AND EXISTS ( SELECT 1
                     FROM post_process_jobs ppj 
                    WHERE ppj.trigger_name = tr.trigger_name
                 );
                 
   DELETE FROM post_process_payout_calc pppc
    WHERE EXISTS ( SELECT 1
                     FROM post_process_jobs ppj 
                    WHERE TRUNC(ppj.date_created) <=  ADD_MONTHS(TRUNC(SYSDATE),-6)
                      AND ppj.post_process_jobs_id = pppc.post_process_jobs_id
                 );
   
   DELETE FROM post_process_jobs ppj
    WHERE TRUNC(ppj.date_created) <=  ADD_MONTHS(TRUNC(SYSDATE),-6);
   
   COMMIT;
EXCEPTION 
  WHEN OTHERS THEN
    p_out_return_code:=99;
    ROLLBACK;
    prc_execution_log_entry ('PRC_QRTZ_POST_PROCESS_JOB_DEL',1,'ERROR','stage : '||v_stage||' '||SQLERRM,NULL);
END;
/
