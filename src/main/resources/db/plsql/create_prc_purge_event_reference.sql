CREATE OR REPLACE PROCEDURE PRC_PURGE_EVENT_REFERENCE (p_out_return_code  OUT VARCHAR2)
 IS
   /****************************************************************************************
   -- Purpose: To delete records from EVENT_REFERENCE table that are older than 7 days   
   ------------This procedure is being called by a scheduler job 
   -- Person                   Date                  Comments
   -- -----------              --------          ----------------------------------
   -- Suresh J                 04/30/2019          Initial version
   ******************************************************************************************/
    v_process_name          execution_log.process_name%type  := 'PRC_PURGE_EVENT_REFERENCE';
    v_release_level         execution_log.release_level%type := '1';
    v_msg                   execution_log.text_line%TYPE;
    v_live_promo_count      NUMBER := 0;
 BEGIN
    v_msg := 'Deleting EVENT_REFERENCE Table data.....';
    DELETE
    FROM EVENT_REFERENCE er
    WHERE NOT EXISTS
               (SELECT ers.id
                  FROM EVENT_REFERENCE ers
                 WHERE     ers.id = er.id
                       AND TRUNC (ers.date_created) BETWEEN TRUNC (SYSDATE) - 7
                                                        AND SYSDATE);
    COMMIT;
    p_out_return_code := '00';
    EXCEPTION
       WHEN OTHERS THEN
         p_out_return_code := SQLERRM;
         prc_execution_log_entry(v_process_name,v_release_level,'ERROR',v_msg ||'--->'||SQLERRM,null);
         COMMIT;
    END  PRC_PURGE_EVENT_REFERENCE ;
/

BEGIN
  SYS.DBMS_SCHEDULER.CREATE_JOB
    (
       job_name        => 'DBMS_PRC_PURGE_EVENT_REFERENCE'
      ,start_date      => TO_TIMESTAMP_TZ('2019/04/30 21:30:00.000000 -05:00','yyyy/mm/dd hh24:mi:ss.ff tzr')
      ,repeat_interval => 'FREQ=WEEKLY; BYDAY=SAT;  BYHOUR=01; BYMINUTE=01'
      ,end_date        => NULL
      ,job_class       => 'DEFAULT_JOB_CLASS'
      ,job_type        => 'PLSQL_BLOCK'
      ,job_action      => 'DECLARE  PO_RETURN_CODE VARCHAR2; BEGIN PRC_PURGE_EVENT_REFERENCE(PO_RETURN_CODE); END;'
      ,comments        => 'Run on every saturday at 01.00 Hrs'
    );
  SYS.DBMS_SCHEDULER.SET_ATTRIBUTE
    ( name      => 'DBMS_PRC_PURGE_EVENT_REFERENCE'
     ,attribute => 'RESTARTABLE'
     ,value     => FALSE);
  SYS.DBMS_SCHEDULER.SET_ATTRIBUTE
    ( name      => 'DBMS_PRC_PURGE_EVENT_REFERENCE'
     ,attribute => 'LOGGING_LEVEL'
     ,value     => SYS.DBMS_SCHEDULER.LOGGING_OFF);
  SYS.DBMS_SCHEDULER.SET_ATTRIBUTE_NULL
    ( name      => 'DBMS_PRC_PURGE_EVENT_REFERENCE'
     ,attribute => 'MAX_FAILURES');
  SYS.DBMS_SCHEDULER.SET_ATTRIBUTE_NULL
    ( name      => 'DBMS_PRC_PURGE_EVENT_REFERENCE'
     ,attribute => 'MAX_RUNS');
  SYS.DBMS_SCHEDULER.SET_ATTRIBUTE
    ( name      => 'DBMS_PRC_PURGE_EVENT_REFERENCE'
     ,attribute => 'STOP_ON_WINDOW_CLOSE'
     ,value     => FALSE);
  SYS.DBMS_SCHEDULER.SET_ATTRIBUTE
    ( name      => 'DBMS_PRC_PURGE_EVENT_REFERENCE'
     ,attribute => 'JOB_PRIORITY'
     ,value     => 3);
  SYS.DBMS_SCHEDULER.SET_ATTRIBUTE_NULL
    ( name      => 'DBMS_PRC_PURGE_EVENT_REFERENCE'
     ,attribute => 'SCHEDULE_LIMIT');
  SYS.DBMS_SCHEDULER.SET_ATTRIBUTE
    ( name      => 'DBMS_PRC_PURGE_EVENT_REFERENCE'
     ,attribute => 'AUTO_DROP'
     ,value     => TRUE);
  SYS.DBMS_SCHEDULER.SET_ATTRIBUTE
    ( name      => 'DBMS_PRC_PURGE_EVENT_REFERENCE'
     ,attribute => 'RESTART_ON_RECOVERY'
     ,value     => FALSE);
  SYS.DBMS_SCHEDULER.SET_ATTRIBUTE
    ( name      => 'DBMS_PRC_PURGE_EVENT_REFERENCE'
     ,attribute => 'RESTART_ON_FAILURE'
     ,value     => FALSE);
  SYS.DBMS_SCHEDULER.SET_ATTRIBUTE
    ( name      => 'DBMS_PRC_PURGE_EVENT_REFERENCE'
     ,attribute => 'STORE_OUTPUT'
     ,value     => TRUE);

  SYS.DBMS_SCHEDULER.ENABLE
    (name                  => 'DBMS_PRC_PURGE_EVENT_REFERENCE');
END;
/
