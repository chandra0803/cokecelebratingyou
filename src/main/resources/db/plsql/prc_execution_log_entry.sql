CREATE OR REPLACE 
PROCEDURE prc_execution_log_entry (
   i_process_name  execution_log.process_name%type
  ,i_release_level execution_log.release_level%type
  ,i_severity  in  execution_log.severity%type
  ,i_text_line in  execution_log.text_line%type
  ,i_dbms_job_nbr in execution_log.dbms_job_nbr%TYPE
) is
-- --------------------------------------------------------------
-- This procedure is used to create an entry into execution_log table to faciltate
-- the audit trail of any process thats executed on the DB.
-- --------------------------------------------------------------

/* --------------------------------------------------------------
   REVISIONS:
   Date        Author           Description
   ----------  ---------------  ------------------------------------
   08/01/2011  J. Flees         Added autonomous transaction processing.
-- --------------------------------------------------------------*/
  PRAGMA AUTONOMOUS_TRANSACTION;

begin
  -- i_severity should be one of the following:
  --    info   = informational message

  --    warn   = warning message
  --    error  = error message that should be checked now
  --    fatal  = fatal error message that should be checked now
  --    detail = detailed message for debugging

    insert into execution_log
      (execution_log_id
      ,session_id
      ,process_name
      ,severity
      ,text_line
      ,release_level
      ,dbms_job_nbr
      ,created_by
      ,date_created)
    values
      (execution_log_pk_sq.nextval
      ,userenv('sessionid')
      ,i_process_name
      ,i_severity
      ,i_text_line
      ,i_release_level
      ,i_dbms_job_nbr
      ,0
      ,sysdate);

   COMMIT;
end;
/