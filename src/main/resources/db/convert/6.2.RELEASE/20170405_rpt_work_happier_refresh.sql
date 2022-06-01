CREATE OR REPLACE PACKAGE pkg_report_work_happier
  IS
/*******************************************************************************
--
-- Purpose: Procedures and functions used to assist the online workhappier report
--
-- MODIFICATION HISTORY
-- Person      Date           Comments
-- ---------   ------        ------------------------------------------
-- nagarajs   12/14/2015     Initial Creation
*******************************************************************************/
 PROCEDURE prc_work_happier_detail(p_user_id IN number,
 p_start_date   IN DATE, 
 p_end_date     IN DATE, 
 p_return_code   OUT NUMBER,
 p_error_message OUT VARCHAR2) ;
    
END; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY pkg_report_work_happier
  IS
/*******************************************************************************
--
-- Purpose: Proocedures and functions used to assist the online workhappier report
--
-- MODIFICATION HISTORY
-- Person      Date           Comments
-- ---------   ------        ------------------------------------------
-- nagarajs   12/14/2015     Initial Creation
--Suresh J    04/04/2017     G6-2082 - Modify summary report (org unit report) to calculate average wh score for a pax rather than just last score 
*******************************************************************************/
PROCEDURE prc_work_happier_detail(p_user_id IN number,
 p_start_date   IN DATE, 
 p_end_date     IN DATE, 
 p_return_code   OUT NUMBER,
 p_error_message OUT VARCHAR2) 
IS

  c_created_by          CONSTANT rpt_enrollment_detail.created_by%TYPE:= p_user_id;
  v_stage               VARCHAR2(250);
  v_commit_cnt          INTEGER;

  v_rec_cnt             INTEGER;
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'PRC_WORK_HAPPIER_DETAIL';
  c_release_level       CONSTANT execution_log.release_level%TYPE := 2.0;  

BEGIN
  v_stage := 'Write start to execution_log table';
  prc_execution_log_entry(C_process_name, C_release_level, 'INFO','Procedure Started '||p_start_date||'-'||p_end_date,NULL);

  v_stage := 'MERGE INTO rpt_work_happier_detail';
  MERGE INTO rpt_work_happier_detail d    
     USING (SELECT wh.user_id,
                   n.node_id,
                   n.name node_name,
                   wh.score,
                   wh.month_created
              FROM (
--                     SELECT  node_id,     --04/04/2017
--                             user_id,
--                             score ,
--                             date_created month_created
--                                     
--                      FROM ( SELECT ws.user_id,ws.node_id, score, TRUNC(ws.date_created,'MM') date_created,
--                                      row_number() OVER(PARTITION BY ws.user_id,TRUNC(ws.date_created,'MM') ORDER BY ws.date_created DESC) rn 
--                                 FROM workhappier_score ws
--                                WHERE ws.date_created BETWEEN p_start_date AND p_end_date  
--                             )
--                      WHERE rn = 1 
                     SELECT  node_id,       --04/04/2017
                             user_id,
                             ROUND(AVG(score)) as score ,
                             date_created month_created
                      FROM ( SELECT ws.user_id,ws.node_id, 
                                    score, 
                                    TRUNC(ws.date_created,'MM') date_created
                             FROM workhappier_score ws
                             WHERE ws.date_created BETWEEN p_start_date AND p_end_date  
                             )
                      GROUP BY node_id,
                               user_id,
                               date_created
                      UNION ALL
                     SELECT un.node_id,
                            un.user_id,
                            null score,
                            month_created
                       FROM user_node un,
                            application_user au,
                            (SELECT month_created
                               FROM (SELECT TRUNC(to_date(TO_CHAR(ADD_MONTHS(p_start_date,COLUMN_VALUE - 1),'MM/DD/YYYY'),'MM/DD/YYYY'),'MM') month_created
                                       FROM dual,
                                        TABLE(CAST(MULTISET(SELECT LEVEL
                                                            FROM dual
                                                          CONNECT BY ADD_MONTHS(TRUNC(p_start_date,'MM'),LEVEL - 1) <= p_end_date
                                                          )AS sys.OdciNumberList))
                                     ) m,
                                     (select trunc(ADD_MONTHS(p_end_date,-2),'MM') la_three from dual) la
                               WHERE month_created >= la_three                         
                            ) mon
                      WHERE un.is_primary = 1
                        AND un.user_id = au.user_id
                        AND au.is_active = 1
                        AND NOT EXISTS (SELECT 1
                                          FROM workhappier_score whs
                                         WHERE whs.user_id = un.user_id
                                           AND whs.node_id = un.node_id
                                           AND TRUNC(whs.date_created,'MM') = month_created
                                        ) 
                    ) wh,
                    node n
              WHERE n.node_id = wh.node_id 
                AND n.is_deleted = 0
           ) s
        ON (d.user_id = s.user_id
            AND d.node_id = s.node_id
            AND d.month_created = s.month_created)
     WHEN MATCHED THEN
   UPDATE SET
     score          = s.score,
     modified_by    = c_created_by,
     date_modified  = SYSDATE
    WHERE NOT ( DECODE(d.score,  s.score, 1, 0) = 1)
   WHEN NOT MATCHED THEN
     INSERT (rp_work_happier_dtl_id,
             user_id,
             node_id,
             node_name,
             score,
             month_created,
             date_created,
             created_by,
             date_modified,
             modified_by
             )
     VALUES (rp_work_happier_dtl_id_pk_sq.NEXTVAL,
             s.user_id,
             s.node_id,
             s.node_name,
             s.score,
             s.month_created,
             SYSDATE,
             c_created_by,
             NULL,
             NULL
            );
                         
  v_rec_cnt := SQL%ROWCOUNT;
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
  
  v_stage := 'DELETE FROM rpt_work_happier_detail';            
  DELETE FROM rpt_work_happier_detail rpt
    WHERE EXISTS(SELECT 1
                   FROM application_user au
                  WHERE au.user_id = rpt.user_id
                    AND au.is_active = 0)
      AND score IS NULL;
                    
  v_rec_cnt := SQL%ROWCOUNT;
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' incative records deleted)', NULL);
   
  p_return_code := 0;
EXCEPTION
  WHEN OTHERS THEN
    p_return_code    := 99; 
    p_error_message  := v_stage||' '||SQLERRM; 
    prc_execution_log_entry(C_process_name, C_release_level,'ERROR',v_stage||' '||SQLERRM,null);
END;
    
END; -- Package spec
/
DECLARE
    -- Declarations
    var_P_USER_ID         NUMBER;
    var_P_START_DATE      DATE;
    var_P_END_DATE        DATE;
    var_P_RETURN_CODE     NUMBER;
    var_P_ERROR_MESSAGE   VARCHAR2 (32767);

    BEGIN
    -- Initialization
    var_P_USER_ID := 0;
    var_P_START_DATE := TO_DATE('01/01/2015','MM/DD/YYYY');
    var_P_END_DATE := SYSDATE;

    -- Call
    PKG_REPORT_WORK_HAPPIER.PRC_WORK_HAPPIER_DETAIL (
        P_USER_ID         => var_P_USER_ID,
        P_START_DATE      => var_P_START_DATE,
        P_END_DATE        => var_P_END_DATE,
        P_RETURN_CODE     => var_P_RETURN_CODE,
        P_ERROR_MESSAGE   => var_P_ERROR_MESSAGE);

     -- Transaction Control
    COMMIT;

   END;
/
