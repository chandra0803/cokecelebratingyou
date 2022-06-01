CREATE OR REPLACE PROCEDURE prc_rpt_promo_node_inactive
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2)IS
/*-----------------------------------------------------------------------------

  Purpose:  To deduct claim count for inactive pax with respect to date_modified in application_user, claim promotion_id, 
            node and giver_recvr_type combination in rpt_promo_node_activity table.            
  Person          Date        Comments
  -----------    -----------  -------------------------------------------------
  Chidamba        01/28/2013  Initial creation to fix Defect # 2103 - Fix to restrict Inactive participant.
  Chidamba        02/05/2012  Defect 2516 Commented user_node.is_primary = 1 to allow all nodes for a user.
  Chidamba        07/31/2013  Added Product Claim promotion details to have inactive user in rpt_promo_node_activity  
-----------------------------------------------------------------------------*/

  c_process_name       CONSTANT execution_log.process_name%TYPE := 'prc_rpt_promo_node_inactive';
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

  v_stage := 'Update rpt_promo_node_activity for Recognition Given';
FOR rec_main IN (SELECT r.rpt_promo_node_activity_id,(r.activity_count-p.difference) difference 
                    FROM rpt_promo_node_activity r,
                         (SELECT c.promotion_id, 
                                un.node_id, 
                                TRUNC(c.submission_date)    submission_date,
                                'giver'                     giver_recvr_type,
                                COUNT(c.claim_id)           difference
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
                            --AND un.is_primary           = 1  --02/05/2012
                            AND au.is_active            = 0                    
                            AND au.date_modified        BETWEEN p_start_date AND p_end_date
                          GROUP BY c.promotion_id, un.node_id, TRUNC(c.submission_date)) p
                         WHERE p.promotion_id = r.promotion_id
                           AND p.node_id      = r.node_id  
                           AND p.submission_date = r.submission_date 
                           AND p.giver_recvr_type = r.giver_recvr_type) LOOP
                           
   UPDATE rpt_promo_node_activity
      SET activity_count = rec_main.difference,
          modified_by = p_user_id,
          date_modified = SYSDATE
    WHERE rpt_promo_node_activity_id = rec_main.rpt_promo_node_activity_id;

 END LOOP; 


  v_stage := 'UPDATE rpt_promo_node_activity for Recognition Received';
  FOR rec_main IN (SELECT r.rpt_promo_node_activity_id,(r.activity_count-p.difference) difference 
                    FROM rpt_promo_node_activity r,
                         (SELECT c.promotion_id, 
                                un.node_id, 
                                TRUNC(c.submission_date)    submission_date,
                                'receiver'                  giver_recvr_type,
                                COUNT(c.claim_id)           difference
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
                            --AND un.is_primary           = 1   --02/05/2012
                            AND au.is_active            = 0                 
                            AND au.date_modified        BETWEEN p_start_date AND p_end_date
                          GROUP BY c.promotion_id, un.node_id, TRUNC(c.submission_date)
                          ) p
                         WHERE p.promotion_id = r.promotion_id
                           AND p.node_id      = r.node_id  
                           AND p.submission_date = r.submission_date 
                           AND p.giver_recvr_type = r.giver_recvr_type) LOOP
                           
   UPDATE rpt_promo_node_activity
      SET activity_count = rec_main.difference,
          modified_by = p_user_id,
          date_modified = SYSDATE
    WHERE rpt_promo_node_activity_id = rec_main.rpt_promo_node_activity_id;
     
 END LOOP;
  

  v_stage := 'UPDATE rpt_promo_node_activity for Nomination Given';
 FOR rec_main IN (SELECT r.rpt_promo_node_activity_id,(r.activity_count-p.difference) difference 
                    FROM rpt_promo_node_activity r,
                         (SELECT c.promotion_id,
                                un.node_id, 
                                TRUNC(c.submission_date) submission_date,
                                'giver'                  giver_recvr_type,
                                COUNT(c.claim_id)        difference 
                           FROM promotion p,
                                promo_nomination pn,
                                claim c,
                                claim_item ci,
                                user_node un,
                                application_user au
                          WHERE p.promotion_id  = pn.promotion_id
                            AND pn.promotion_id = c.promotion_id
                            AND c.claim_id      = ci.claim_id
                            AND c.submitter_id  = un.user_id
                            AND c.submitter_id  = au.user_id
                            --AND un.is_primary   = 1   --02/05/2012
                            AND au.is_active    = 0                      
                            AND ci.approval_status_type IN ('pend','winner')
                            AND au.date_modified     BETWEEN p_start_date AND p_end_date
                          GROUP BY c.promotion_id,
                                   un.node_id, 
                                   TRUNC(c.submission_date)
                          ) p
                         WHERE p.promotion_id = r.promotion_id
                           AND p.node_id      = r.node_id  
                           AND p.submission_date = r.submission_date 
                           AND p.giver_recvr_type = r.giver_recvr_type) LOOP
                           
   UPDATE rpt_promo_node_activity
      SET activity_count = rec_main.difference,
          modified_by = p_user_id,
          date_modified = SYSDATE
    WHERE rpt_promo_node_activity_id = rec_main.rpt_promo_node_activity_id;
     
 END LOOP;
   

  v_stage := 'Update rpt_promo_node_activity for Nomination Received';
 FOR rec_main IN (SELECT r.rpt_promo_node_activity_id,(r.activity_count-p.difference) difference 
                    FROM rpt_promo_node_activity r,
                        (SELECT c.promotion_id,
                                un.node_id, 
                                TRUNC(c.submission_date) submission_date,
                                'receiver'               giver_recvr_type,
                                COUNT(c.claim_id)        difference 
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
                            --AND un.is_primary     = 1   --02/05/2012
                            AND au.is_active      = 0                      
                            AND ci.approval_status_type IN ('pend','winner')
                            AND au.date_modified        BETWEEN p_start_date AND p_end_date
                          GROUP BY c.promotion_id,
                                   un.node_id, 
                                   TRUNC(c.submission_date)
                          UNION ALL
                         SELECT c.promotion_id,
                                un.node_id, 
                                TRUNC(c.submission_date) submission_date,
                                'receiver'               giver_recvr_type,
                                COUNT(c.claim_id)        difference 
                           FROM promotion p,
                                promo_nomination pn,
                                claim c,
                                claim_item ci,
                                claim_participant cp,
                                user_node un,
                                application_user au
                          WHERE p.promotion_id    = pn.promotion_id
                            AND pn.promotion_id   = c.promotion_id
                            AND c.claim_id        = ci.claim_id
                            AND c.claim_id        = cp.claim_id
                            AND cp.participant_id = un.user_id
                            AND cp.participant_id = au.user_id
                            --AND un.is_primary     = 1   --02/05/2012
                            AND au.is_active      = 0                       
                            AND ci.approval_status_type IN ('pend','winner')
                            AND au.date_modified        BETWEEN p_start_date AND p_end_date
                          GROUP BY c.promotion_id, un.node_id,TRUNC(c.submission_date)
                          ) p
                         WHERE p.promotion_id = r.promotion_id
                           AND p.node_id      = r.node_id  
                           AND p.submission_date = r.submission_date 
                           AND p.giver_recvr_type = r.giver_recvr_type) LOOP
                           
   UPDATE rpt_promo_node_activity
      SET activity_count = rec_main.difference,
          modified_by = p_user_id,
          date_modified = SYSDATE
    WHERE rpt_promo_node_activity_id = rec_main.rpt_promo_node_activity_id;
         
 END LOOP;  
 
 
  v_stage := 'Update rpt_promo_node_activity for Recognition Given';
FOR rec_main IN (SELECT r.rpt_promo_node_activity_id,(r.activity_count-p.difference) difference 
                    FROM rpt_promo_node_activity r,
                        (SELECT c.promotion_id, 
                                un.node_id, 
                                TRUNC(c.submission_date)    submission_date,
                                decode(c.is_open,1,'open',0,'closed')  claim_status,
                                COUNT(c.claim_id)           difference
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
                            --AND un.is_primary           = 1                  
                            AND au.is_active            = 0                  
                            AND (c.submission_date   BETWEEN p_start_date AND p_end_date  
                                OR au.date_modified  BETWEEN p_start_date AND p_end_date)    
                          GROUP BY c.promotion_id, 
                                   un.node_id, 
                                   TRUNC(c.submission_date),
                                   c.is_open) p
                         WHERE p.promotion_id = r.promotion_id
                           AND p.node_id      = r.node_id  
                           AND p.submission_date = r.submission_date 
                           AND p.claim_status    = r.giver_recvr_type) LOOP
                           
   UPDATE rpt_promo_node_activity
      SET activity_count = rec_main.difference,
          modified_by = p_user_id,
          date_modified = SYSDATE
    WHERE rpt_promo_node_activity_id = rec_main.rpt_promo_node_activity_id;

 END LOOP;
                       
 prc_execution_log_entry(c_process_name, 
                          c_release_level, 
                          'INFO', 
                          'Procedure Completed', 
                          NULL);
  p_return_code := 0;
           
EXCEPTION
  WHEN OTHERS THEN
    p_return_code   := 99;
    p_error_message := 'Error at Stage: '||v_stage||', ' || SQLERRM;
    prc_execution_log_entry(c_process_name, 
                            c_release_level, 
                            'ERROR', 
                            'Stage: '||v_stage||', '||SQLERRM,
                            NULL);                        

END;
/
