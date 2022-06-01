CREATE OR REPLACE PROCEDURE prc_purge_old_log_data (
    p_out_return_code   OUT NUMBER)
IS
    /********************************
    Purpose : To purge the old data from execution_log table and comm_log and mailing tables.

    Author           Date         Comments
    -------          ----        ----------
    Ravi Dhanekula 07/13/2017     Initial
    Loganathan     06/09/2019     Bug 79054 - clean up LOB space
    ********************************/
    v_process_name   CONSTANT execution_log.process_name%TYPE
                                  := 'PRC_PURGE_OLD_LOG_DATA' ;
    v_stage                   execution_log.text_line%TYPE;
    v_error_msg               execution_log.text_line%TYPE;
    v_purge_comm_log          NUMBER;
    v_purge_execution_log     NUMBER;
    v_max_mailing_id          mailing.mailing_id%TYPE;
    v_max_batch_id            mailing_batch.batch_id%TYPE;
    v_sql                     VARCHAR2(500); -- 06/09/2019 Bug#79054

    v_delete_el               INT;
    v_delete_mrd              INT;
    v_delete_mr               INT;
    v_delete_mml              INT;
    v_delete_mai              INT;
    v_delete_clc              INT;
    v_delete_cl               INT;
    v_delete_m                INT;
    v_delete_mb               INT;

    e_stop                    EXCEPTION;
BEGIN
    prc_execution_log_entry (v_process_name,
                             1,
                             'INFO',
                             'Process Started',
                             NULL);
    v_stage := 'Get system variables';

    BEGIN
        SELECT INT_VAL
          INTO v_purge_comm_log
          FROM os_propertyset
         WHERE entity_name = 'comm.log.data.purge.days';
    EXCEPTION
        WHEN OTHERS
        THEN
            v_error_msg :=
                'Process stopped. Value for comm.log.data.purge.days not defined';
            RAISE e_stop;
    END;

    BEGIN
        SELECT INT_VAL
          INTO v_purge_execution_log
          FROM os_propertyset
         WHERE entity_name = 'execution.log.data.purge.days';
    EXCEPTION
        WHEN OTHERS
        THEN
            v_error_msg :=
                'Process stopped. Value for execution.log.data.purge.days not defined';
            RAISE e_stop;
    END;

    --------------------------------------------------------------------------------

    v_stage := 'get max batch_id';

    SELECT NVL (MAX (batch_id), 0)
      INTO v_max_batch_id
      FROM mailing_batch
     WHERE TRUNC (date_created) < TRUNC (SYSDATE) - v_purge_comm_log;

    v_stage := 'get max mailing_id';

    SELECT MAX (mailing_id)
      INTO v_max_mailing_id
      FROM (SELECT NVL (MAX (mailing_id), 0) mailing_id
              FROM mailing
             WHERE TRUNC (date_created) < TRUNC (SYSDATE) - v_purge_comm_log
            UNION ALL
            SELECT NVL (MAX (mailing_id), 0) mailing_id
              FROM mailing
             WHERE batch_id <= v_max_batch_id);

    IF v_max_mailing_id = 0
    THEN
        v_error_msg := 'Process stopped. No elig recs in MAILING.';
        RAISE e_stop;
    END IF;

    --------------------------------------------------------------------------------
    v_stage := 'DELETE mailing_recipient_data';

    DELETE FROM execution_log
          WHERE date_created < SYSDATE - v_purge_execution_log;

    v_delete_el := SQL%ROWCOUNT;

    v_stage := 'DELETE mailing_recipient_data';

    DELETE mailing_recipient_data
     WHERE mailing_recipient_id IN (SELECT mailing_recipient_id
                                      FROM mailing_recipient
                                     WHERE mailing_id <= v_max_mailing_id);

    v_delete_mrd := SQL%ROWCOUNT;

    v_stage := 'DELETE mailing_recipient';

    DELETE mailing_recipient
     WHERE mailing_id <= v_max_mailing_id;

    v_delete_mr := SQL%ROWCOUNT;

    v_stage := 'DELETE mailing_message_locale';

    DELETE mailing_message_locale
     WHERE mailing_id <= v_max_mailing_id;

    v_delete_mml := SQL%ROWCOUNT;

    v_stage := 'DELETE mailing_attachment_info';

    DELETE mailing_attachment_info
     WHERE mailing_id <= v_max_mailing_id;

    v_delete_mai := SQL%ROWCOUNT;

    v_stage := 'DELETE comm_log_comment';

    DELETE comm_log_comment
     WHERE comm_log_id IN (SELECT comm_log_id
                             FROM comm_log
                            WHERE mailing_id <= v_max_mailing_id);

    v_delete_clc := SQL%ROWCOUNT;

    v_stage := 'DELETE comm_log';

    DELETE comm_log
     WHERE mailing_id <= v_max_mailing_id;

    v_delete_cl := SQL%ROWCOUNT;

    v_stage := 'DELETE mailing';

    DELETE mailing
     WHERE mailing_id <= v_max_mailing_id;

    v_delete_m := SQL%ROWCOUNT;

    v_stage := 'DELETE mailing_batch';

    DELETE mailing_batch
     WHERE batch_id <= v_max_batch_id;

    v_delete_mb := SQL%ROWCOUNT;

    COMMIT;

    --------------------------------------------------------------------------------

    v_stage := 'gather stats';

    FOR rec
        IN (SELECT table_name
              FROM user_tables
             WHERE    table_name LIKE 'MAILING%'
                   OR table_name LIKE 'COMM_LOG%'
                   OR table_name LIKE 'EXECUTION_LOG%')
    LOOP
        BEGIN
            SYS.DBMS_STATS.GATHER_TABLE_STATS (
                OWNNAME            => USER,
                TABNAME            => rec.table_name,
                ESTIMATE_PERCENT   => DBMS_STATS.AUTO_SAMPLE_SIZE,
                CASCADE            => TRUE,
                METHOD_OPT         => 'for all columns size auto');
        END;
    END LOOP;

    -- 06/09/2019 start Bug#79054
    v_stage := 'clear out LOBs';    
		FOR rec IN (SELECT table_name, column_name
								FROM   user_lobs
								WHERE  table_name = 'COMM_LOG'
								OR     table_name LIKE 'MAIL%') LOOP

			v_sql := 'ALTER TABLE '||rec.table_name||' MODIFY LOB('||rec.column_name||') (SHRINK SPACE CASCADE)';
			EXECUTE IMMEDIATE v_sql;
			
			BEGIN
				SYS.DBMS_STATS.GATHER_TABLE_STATS (
						OWNNAME            => USER,
						TABNAME            => rec.table_name,
						ESTIMATE_PERCENT   => DBMS_STATS.AUTO_SAMPLE_SIZE,
						CASCADE            => TRUE,
						METHOD_OPT         => 'for all columns size auto');
			END;

		END LOOP; 
		-- 06/09/2019 end Bug#79054
    --------------------------------------------------------------------------------

    v_error_msg :=
           'PROCEDURE Successful.'
        || CHR (10)
        || 'Recs deleted from mailing_batch: '
        || v_delete_mb
        || CHR (10)
        || 'Recs deleted from mailing: '
        || v_delete_m
        || CHR (10)
        || 'Recs deleted from comm_log: '
        || v_delete_cl
        || CHR (10)
        || 'Recs deleted from comm_log_comment: '
        || v_delete_clc
        || CHR (10)
        || 'Recs deleted from mailing_attachment_info: '
        || v_delete_mai
        || CHR (10)
        || 'Recs deleted from mailing_message_locale: '
        || v_delete_mml
        || CHR (10)
        || 'Recs deleted from mailing_recipient: '
        || v_delete_mr
        || CHR (10)
        || 'Recs deleted from mailing_recipient_data: '
        || v_delete_mrd;
    prc_execution_log_entry (v_process_name,
                             1,
                             'INFO',
                             v_error_msg,
                             NULL);
EXCEPTION
    WHEN e_stop
    THEN
        ROLLBACK;
        prc_execution_log_entry (v_process_name,
                                 1,
                                 'INFO',
                                 v_error_msg,
                                 NULL);
    WHEN OTHERS
    THEN
        ROLLBACK;
        p_out_return_code := 99;
        v_error_msg := 'OTHER ERROR: ' || SQLERRM || '-->Stage: ' || v_stage;
        prc_execution_log_entry (v_process_name,
                                 1,
                                 'ERROR',
                                 v_error_msg,
                                 NULL);
END;
/
BEGIN
  SYS.DBMS_SCHEDULER.CREATE_JOB
    (
       job_name        => 'DBMS_PRC_PURGE_OLD_LOG_DATA'
      ,start_date      => TO_TIMESTAMP_TZ('2017/06/27 21:30:00.000000 -05:00','yyyy/mm/dd hh24:mi:ss.ff tzr')
      ,repeat_interval => 'FREQ=MONTHLY; BYDAY=-1SAT; BYHOUR=21; BYMINUTE=30;'
      ,end_date        => NULL
      ,job_class       => 'DEFAULT_JOB_CLASS'
      ,job_type        => 'PLSQL_BLOCK'
      ,job_action      => 'DECLARE  PO_RETURN_CODE NUMBER; BEGIN PRC_PURGE_OLD_LOG_DATA(PO_RETURN_CODE); END;'
      ,comments        => 'Run on last saturday of every month at 21.30 Hrs'
      ,enabled         => FALSE
    );
END;
/
