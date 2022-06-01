CREATE OR REPLACE PROCEDURE prc_refresh_pend_nom_approver
         ( p_in_user_id             IN  NUMBER,
           p_out_returncode         OUT NUMBER)
AS
 /*******************************************************************************
  -- Purpose: update the appprover to default approver if pending nominations 
  --          approver became inactive
  -- Person             Date         Comments
  -- -----------        --------    -----------------------------------------------------
  -- nagarajs          05/11/2016    Initial Creation                      
  *******************************************************************************/
   c_process_name    CONSTANT execution_log.process_name%type :='PRC_REFRESH_PEND_NOM_APPROVER';
   c_release_level   CONSTANT execution_log.release_level%type := '1.0';
   
   v_stage           VARCHAR2(100);
   v_rec_cnt         NUMBER(10);
BEGIN

   
  p_out_returncode := 0;
  
  v_stage := 'MERGE INTO claim_approver_snapshot';
  MERGE INTO claim_approver_snapshot d
     USING (SELECT cas.claim_id,
                   un_df.user_id default_user_id,
                   un_df.node_id default_node_id
              FROM claim c,
                   claim_approver_snapshot cas,
                   user_node un,
                   application_user au,
                   promo_nomination pn,
                   user_node un_df
             WHERE c.claim_id = cas.claim_id
               AND cas.approver_user_id = un.user_id
               AND cas.source_node_id = un.node_id
               AND un.user_id = au.user_id
               AND au.is_active = 0
               AND c.promotion_id = pn.promotion_id
               AND pn.default_approver = un_df.user_id
               AND un.is_primary = 1) s
        ON (d.claim_id = s.claim_id)
      WHEN MATCHED THEN
    UPDATE SET approver_user_id = s.default_user_id,
               source_node_id   = s.default_node_id,
               date_modified    = sysdate,
               modified_by      = p_in_user_id;
  
  v_rec_cnt := SQL%ROWCOUNT;
  prc_execution_log_entry(c_process_name, 1, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);     
                  
  v_stage := 'MERGE INTO approver';
  MERGE INTO approver d
     USING (SELECT ap.approver_criteria_id,
                   pn.default_approver default_user_id
              FROM approver ap,
                   approver_criteria ac,
                   approver_option ao,
                   application_user au,
                   promo_nomination pn
             WHERE ap.user_id = au.user_id
               AND au.is_active = 0
               and ap.approver_criteria_id = ac.approver_criteria_id
               AND ac.approver_option_id = ao.approver_option_id
               AND ao.promotion_id = pn.promotion_id
             ) s
        ON (d.approver_criteria_id = s.approver_criteria_id)
      WHEN MATCHED THEN
    UPDATE SET user_id          = s.default_user_id,
               date_modified    = sysdate,
               modified_by      = p_in_user_id;
  
  v_rec_cnt := SQL%ROWCOUNT;
  prc_execution_log_entry(c_process_name, 1, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL); 
EXCEPTION 
  WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Parms : p_in_user_id: '||p_in_user_id||
      ' Error at stage: '||v_stage||' -->'||SQLERRM, NULL);
      p_out_returncode := 99;
END prc_refresh_pend_nom_approver;
/
