CREATE OR REPLACE PROCEDURE prc_report_promo_node_activity
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2)
IS

/*-----------------------------------------------------------------------------

  Purpose:  Populates claim count for a claim submission date in promotion,node 
            and giver_recvr_type combination in rpt_promo_node_activity table
   
  Person          Date        Comments
  -----------    -----------  -------------------------------------------------
  Arun S          12/17/2012  Initial creation
  Chidamba        01/21/2013  Defect # 2103 - Fix to restrict Inactive participant.
                              . For recognition given, a recognition should only be counted if the giver is active.   
                              . For recognition received, a recognition should only be counted if the recipient is active.
  Chidamba        02/05/2012  Defect 2516 Commented user_node.is_primary = 1 to allow all nodes for a user.
  Chidamba        07/31/2013  Added Product Claim promotion details to have active user in rpt_promo_node_activity  
  nagarajs       07/29/2015   New nomination redesign changes    
-----------------------------------------------------------------------------*/

  c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_REPORT_PROMO_NODE_ACTIVITY';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by         CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;

  --Procedure variables
  v_stage              VARCHAR2(500);


    
BEGIN

  prc_execution_log_entry(c_process_name, 
                          c_release_level, 
                          'INFO', 
                          'Procedure Started', 
                          NULL);

  v_stage := 'Insert rpt_promo_node_activity for Recognition Given';
  MERGE INTO rpt_promo_node_activity rpt
  USING (SELECT c.promotion_id, 
                un.node_id, 
                TRUNC(c.submission_date)    submission_date,
                'giver'                     giver_recvr_type,
                COUNT(c.claim_id)           activity_count
           FROM promotion p,
                promo_recognition pr,
                claim c,
                claim_item ci,
                user_node un,
                application_user au
          WHERE p.promotion_id          = pr.promotion_id
            AND pr.promotion_id         = c.promotion_id
            AND c.claim_id              = ci.claim_id
            AND c.submitter_id          = un.user_id
            AND c.submitter_id          = au.user_id
            AND ci.approval_status_type = 'approv'
            AND un.is_primary           = 1                   --02/05/2012
            AND au.is_active            = 1                     --01/21/2013
            AND (c.submission_date       BETWEEN p_start_date AND p_end_date  
                OR au.date_modified       BETWEEN p_start_date AND p_end_date) --01/21/2013 added au.date_modified    
          GROUP BY c.promotion_id, un.node_id, TRUNC(c.submission_date)
         ) dtl
        ON (    rpt.promotion_id     = dtl.promotion_id
            AND rpt.node_id          = dtl.node_id
            AND rpt.submission_date  = dtl.submission_date
            AND rpt.giver_recvr_type = dtl.giver_recvr_type)
      WHEN MATCHED THEN
        UPDATE SET rpt.activity_count = rpt.activity_count+dtl.activity_count,
                   rpt.modified_by    = p_user_id, 
                   rpt.date_modified  = SYSDATE
      WHEN NOT MATCHED THEN
        INSERT (rpt_promo_node_activity_id, 
                promotion_id, 
                node_id, 
                submission_date, 
                giver_recvr_type, 
                activity_count, 
                created_by, 
                date_created, 
                modified_by, 
                date_modified
                )
        VALUES (rpt_promo_node_activity_id_sq.nextval,
                dtl.promotion_id, 
                dtl.node_id, 
                dtl.submission_date, 
                dtl.giver_recvr_type, 
                dtl.activity_count,
                p_user_id,
                SYSDATE,
                NULL,
                NULL
                );


  v_stage := 'Insert rpt_promo_node_activity for Recognition Received';
  MERGE INTO rpt_promo_node_activity rpt
  USING (SELECT c.promotion_id, 
                un.node_id, 
                TRUNC(c.submission_date)    submission_date,
                'receiver'                  giver_recvr_type,
                COUNT(c.claim_id)           activity_count
           FROM promotion p,
                promo_recognition pr,
                claim c,
                claim_item ci,
                claim_recipient cr,
                user_node un,
                application_user au
          WHERE p.promotion_id          = pr.promotion_id
            AND pr.promotion_id         = c.promotion_id
            AND c.claim_id              = ci.claim_id
            AND ci.claim_item_id        = cr.claim_item_id
            AND cr.participant_id       = un.user_id
            AND cr.participant_id       = au.user_id
            AND ci.approval_status_type = 'approv'
            AND un.is_primary           = 1                 --02/05/2012
            AND au.is_active            = 1                 --01/21/2013
            AND (c.submission_date       BETWEEN p_start_date AND p_end_date
                 OR au.date_modified       BETWEEN p_start_date AND p_end_date) --01/21/2013 added au.date_modified
          GROUP BY c.promotion_id, un.node_id, TRUNC(c.submission_date)
         ) dtl
        ON (    rpt.promotion_id     = dtl.promotion_id
            AND rpt.node_id          = dtl.node_id
            AND rpt.submission_date  = dtl.submission_date
            AND rpt.giver_recvr_type = dtl.giver_recvr_type)
      WHEN MATCHED THEN
        UPDATE SET rpt.activity_count = rpt.activity_count+dtl.activity_count,
                   rpt.modified_by    = p_user_id, 
                   rpt.date_modified  = SYSDATE
      WHEN NOT MATCHED THEN
        INSERT (rpt_promo_node_activity_id, 
                promotion_id, 
                node_id, 
                submission_date, 
                giver_recvr_type, 
                activity_count, 
                created_by, 
                date_created, 
                modified_by, 
                date_modified
                )
        VALUES (rpt_promo_node_activity_id_sq.nextval,
                dtl.promotion_id, 
                dtl.node_id, 
                dtl.submission_date, 
                dtl.giver_recvr_type, 
                dtl.activity_count,
                p_user_id,
                SYSDATE,
                NULL,
                NULL
                );  

  --07/29/2016 New  nomination  redesign changes
  v_stage := 'Insert rpt_promo_node_activity for Nomination Given';
  MERGE INTO rpt_promo_node_activity rpt
  USING (SELECT c.promotion_id,
                un.node_id, 
                TRUNC(c.submission_date) submission_date,
                'giver'                  giver_recvr_type,
                COUNT(c.claim_id)        activity_count 
           FROM promotion p,
                promo_nomination pn,
                claim c,
                user_node un,
                application_user au
          WHERE p.promotion_id  = pn.promotion_id
            AND pn.promotion_id = c.promotion_id
            AND c.submitter_id  = un.user_id
            AND c.submitter_id  = au.user_id
            AND un.is_primary   = 1                      
            AND au.is_active    = 1
            AND EXISTS ( SELECT 1
                           FROM claim_item ci
                          WHERE ci.claim_id = c.claim_id
                            AND ci.approval_status_type = 'pend' 
                          UNION 
                         SELECT 1
                           FROM claim_item ci,
                                claim_item_approver cia
                          WHERE ci.claim_id = c.claim_id
                            AND ci.claim_item_id = cia.claim_item_id
                            AND cia.approval_status_type = 'winner'
                        )
            AND (c.submission_date       BETWEEN p_start_date AND p_end_date
                 OR au.date_modified       BETWEEN p_start_date AND p_end_date)  
          GROUP BY c.promotion_id,
                   un.node_id, 
                   TRUNC(c.submission_date)
         ) dtl
        ON (    rpt.promotion_id     = dtl.promotion_id
            AND rpt.node_id          = dtl.node_id
            AND rpt.submission_date  = dtl.submission_date
            AND rpt.giver_recvr_type = dtl.giver_recvr_type)
      WHEN MATCHED THEN
        UPDATE SET rpt.activity_count = rpt.activity_count+dtl.activity_count,
                   rpt.modified_by    = p_user_id, 
                   rpt.date_modified  = SYSDATE
      WHEN NOT MATCHED THEN
        INSERT (rpt_promo_node_activity_id, 
                promotion_id, 
                node_id, 
                submission_date, 
                giver_recvr_type, 
                activity_count, 
                created_by, 
                date_created, 
                modified_by, 
                date_modified
                )
        VALUES (rpt_promo_node_activity_id_sq.nextval,
                dtl.promotion_id, 
                dtl.node_id, 
                dtl.submission_date, 
                dtl.giver_recvr_type, 
                dtl.activity_count,
                p_user_id,
                SYSDATE,
                NULL,
                NULL
                );


  v_stage := 'Insert rpt_promo_node_activity for Nomination Received';
  MERGE INTO rpt_promo_node_activity rpt
  USING (SELECT c.promotion_id,
                un.node_id, 
                TRUNC(c.submission_date) submission_date,
                'receiver'               giver_recvr_type,
                COUNT(c.claim_id)        activity_count 
           FROM promotion p,
                promo_nomination pn,
                claim c,
                claim_item ci,
                claim_recipient cr,
                user_node un,
                application_user au
          WHERE p.promotion_id    = pn.promotion_id
            AND pn.promotion_id   = c.promotion_id
            AND c.claim_id        = ci.claim_id
            AND ci.claim_item_id  = cr.claim_item_id
            AND cr.participant_id = un.user_id
            AND cr.participant_id = au.user_id
            AND un.is_primary     = 1                       --02/05/2012
            AND au.is_active      = 1                       --01/21/2013
            AND EXISTS ( SELECT 1
                           FROM claim_item ci
                          WHERE ci.claim_id = c.claim_id
                            AND ci.approval_status_type = 'pend' 
                          UNION 
                         SELECT 1
                           FROM claim_item ci,
                                claim_item_approver cia
                          WHERE ci.claim_id = c.claim_id
                            AND ci.claim_item_id = cia.claim_item_id
                            AND cia.approval_status_type = 'winner'
                        )
            AND (c.submission_date     BETWEEN p_start_date AND p_end_date
                 OR au.date_modified   BETWEEN p_start_date AND p_end_date)  --01/21/2013 added au.date_modified
          GROUP BY c.promotion_id,
                   un.node_id, 
                   TRUNC(c.submission_date)
         ) dtl
        ON (    rpt.promotion_id     = dtl.promotion_id
            AND rpt.node_id          = dtl.node_id
            AND rpt.submission_date  = dtl.submission_date
            AND rpt.giver_recvr_type = dtl.giver_recvr_type)
      WHEN MATCHED THEN
        UPDATE SET rpt.activity_count = rpt.activity_count+dtl.activity_count,
                   rpt.modified_by    = p_user_id, 
                   rpt.date_modified  = SYSDATE
      WHEN NOT MATCHED THEN
        INSERT (rpt_promo_node_activity_id, 
                promotion_id, 
                node_id, 
                submission_date, 
                giver_recvr_type, 
                activity_count, 
                created_by, 
                date_created, 
                modified_by, 
                date_modified
                )
        VALUES (rpt_promo_node_activity_id_sq.nextval,
                dtl.promotion_id, 
                dtl.node_id, 
                dtl.submission_date, 
                dtl.giver_recvr_type, 
                dtl.activity_count,
                p_user_id,
                SYSDATE,
                NULL,
                NULL
                );
   --07/29/2016 New nomination design End
                
   v_stage := 'Insert rpt_promo_node_activity for Product_Claim';     --07/31/2013 start
   MERGE INTO rpt_promo_node_activity rpt
   USING (SELECT c.promotion_id, 
                un.node_id, 
                TRUNC(c.submission_date)    submission_date,
                decode(c.is_open,1,'open',0,'closed')  claim_status,
                COUNT(c.claim_id)           activity_count
           FROM promotion p,
                promo_product_claim pr,
                claim c,
                claim_item ci,
                user_node un,
                application_user au
          WHERE p.promotion_id          = pr.promotion_id
            AND pr.promotion_id         = c.promotion_id
            AND c.claim_id              = ci.claim_id
            AND c.submitter_id          = un.user_id
            AND c.submitter_id          = au.user_id            
            AND un.is_primary           = 1                  
            AND au.is_active            = 1                  
            AND (c.submission_date   BETWEEN p_start_date AND p_end_date  
                OR au.date_modified  BETWEEN p_start_date AND p_end_date)    
          GROUP BY c.promotion_id, 
                   un.node_id, 
                   TRUNC(c.submission_date),
                   c.is_open
         ) dtl
        ON (    rpt.promotion_id     = dtl.promotion_id
            AND rpt.node_id          = dtl.node_id
            AND rpt.submission_date  = dtl.submission_date
            AND rpt.giver_recvr_type = dtl.claim_status)
      WHEN MATCHED THEN
        UPDATE SET rpt.activity_count = rpt.activity_count+dtl.activity_count,
                   rpt.modified_by    = p_user_id, 
                   rpt.date_modified  = SYSDATE
      WHEN NOT MATCHED THEN
        INSERT (rpt_promo_node_activity_id, 
                promotion_id, 
                node_id, 
                submission_date, 
                giver_recvr_type, 
                activity_count, 
                created_by, 
                date_created, 
                modified_by, 
                date_modified
                )
        VALUES (rpt_promo_node_activity_id_sq.nextval,
                dtl.promotion_id, 
                dtl.node_id, 
                dtl.submission_date, 
                dtl.claim_status, 
                dtl.activity_count,
                p_user_id,
                SYSDATE,
                NULL,
                NULL
                );                                        --07/31/2013 end
                   
                
  --Update count for Active and Inactive participant appart from the date range
  
  --Call to exclude Inactive participant count
  prc_rpt_promo_node_inactive(p_user_id,
                              p_start_date, 
                              p_end_date,
                              p_return_code,
                              p_error_message);
  
  IF p_return_code <> 0 THEN

   prc_execution_log_entry('prc_rpt_promo_node_inactive', 
                           c_release_level, 
                           'ERROR', 
                           p_error_message, 
                           NULL);
    
    p_return_code := 99; 
  ELSE
   
   prc_execution_log_entry(c_process_name, 
                           c_release_level, 
                           'INFO', 
                           'Procedure Completed', 
                           NULL);
   p_return_code := 0;
  END IF ;         
EXCEPTION
  WHEN OTHERS THEN
    p_return_code   := 99;
    p_error_message := 'Error at Stage: '||v_stage||', ' || SQLERRM;
    prc_execution_log_entry(c_process_name, 
                            c_release_level, 
                            'ERROR', 
                            'Stage: '||v_stage||', '||SQLERRM,
                            NULL);                        

END prc_report_promo_node_activity;
/
