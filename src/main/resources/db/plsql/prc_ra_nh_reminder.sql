CREATE OR REPLACE PROCEDURE PRC_RA_NH_REMINDER(p_in_user_id      IN  application_user.user_id%TYPE, 
                                               p_out_return_code OUT NUMBER,
                                               p_out_user_data   OUT SYS_REFCURSOR) 
 IS
  PRAGMA AUTONOMOUS_TRANSACTION;
    /*******************************************************************************
   -- Purpose: To generate result for RAf or new hires
   --
   -- Person        Date         Comments
   -- -----------   --------     -----------------------------------------------------
   -- Gorantla      01/06/2018   Initial creation                                                  
   *******************************************************************************/
  c_process_name          CONSTANT execution_log.process_name%TYPE:='PRC_RA_NH_REMINDER';

  v_user_node             user_node.node_id%TYPE;
  v_live_cnt_promotion    NUMBER(10);
  v_cnt_all_active_promo  NUMBER(10);
  v_level                 PLS_INTEGER;
  v_orphan_nodes          PLS_INTEGER;
  v_return_code           PLS_INTEGER;
  v_stage                 VARCHAR2(400);
  
  --System variables
  v_days_emp_reminder     os_propertyset.int_val%TYPE;
  v_days_new_to_regemp    os_propertyset.int_val%TYPE;
  v_no_of_levels          os_propertyset.int_val%TYPE;
                                        

BEGIN
  prc_execution_log_entry(c_process_name,1,'INFO','Process Started '||p_in_user_id,null);
  
  v_stage := 'Days after which a recogn reminder is shown for regular emp'; 
  SELECT int_val
    INTO v_days_emp_reminder
    FROM os_propertyset
   WHERE entity_name = 'ra.numberofdays.employee.reminder';
 
  v_stage := 'Days after which a newhire is changed to regular emp status';
  SELECT int_val
    INTO v_days_new_to_regemp
    FROM os_propertyset
   WHERE entity_name = 'ra.numberofdays.newhiretoregularemployee';
    
   v_stage := 'Populate promotions and redeivers info into gtt tables '; 
   prc_ra_promo_level(p_in_user_id,v_return_code);       
 
  v_stage := 'Check for promotions with givers as all active pax '; 
  SELECT count(1)
    INTO v_cnt_all_active_promo
    FROM gtt_ra_promotion
   WHERE promotion_status = 'live'
     AND secondary_audience_type = 'allactivepaxaudience';
     
   SELECT count(1)
    INTO v_live_cnt_promotion
    FROM gtt_ra_promotion
   WHERE promotion_status = 'live';  
      
IF v_live_cnt_promotion >= 1 THEN 
  IF v_cnt_all_active_promo >= 1 THEN
    v_stage := 'Get result for all active promotions'; 
    OPEN p_out_user_data FOR  
    WITH rec_user as 
    (SELECT au.user_id,
           au.first_name,
           au.last_name,
           pe.position_type,
           p.avatar_small,
           trunc(sysdate+1) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created))) days_since_hired,
           pe.hire_Date,
           au.enrollment_date,
           au.date_created                     
      FROM application_user au,
           participant p,
           vw_curr_pax_employer pe,
           gtt_ra_receiver gr
     WHERE au.user_id = p.user_id
       AND au.user_id = gr.user_id
       AND au.user_id = pe.user_id(+)
       AND au.is_active = 1
       AND p.is_opt_out_of_program = 0 
       AND p.status = 'active'),
-- Get all rec claim records                      
 rec_pax as
   (SELECT cr.participant_id,ci.approval_status_type,ci.date_created
      FROM claim_recipient cr,
           claim_item ci,
           recognition_claim rc,
           claim c,
           rec_user ru
     WHERE ci.claim_item_id = cr.claim_item_id 
       AND ci.claim_id = rc.claim_id
       AND rc.claim_id = c.claim_id
       AND c.submitter_id = p_in_user_id
       AND cr.participant_id = user_id
    ),
-- Get only pending records    
 rec_pend as 
    (SELECT *
      FROM (SELECT PR.*,
                   rank() over(partition by PR.participant_id order by pr.date_created desc) ranks
              FROM rec_pax pr
             WHERE approval_status_type = 'pend'
               AND NOT EXISTS (SELECT participant_id
                                 FROM rec_pax
                                WHERE participant_id = pr.participant_id
                                  AND approval_status_type <> 'pend')
            )
     WHERE ranks = 1)        
      SELECT user_id,
             first_name,
             last_name,
             position_type,
             avatar_small,
             days_since_hired
        FROM (SELECT *
                FROM rec_user 
               WHERE days_since_hired <= v_days_new_to_regemp
                 AND user_id NOT IN (SELECT user_id  --Find pax which are not recog by only recognition type promotions
                                       FROM activity a,
                                            gtt_ra_promotion p,
                                            claim c
                                      WHERE a.promotion_id = p.promotion_id
                                        AND a.claim_id = c.claim_id
                                        AND c.submitter_id = p_in_user_id
                                        AND is_submitter = 0)) rec,
             rec_pend p
       WHERE rec.user_id = p.participant_id(+)
         AND p.approval_status_type IS NULL 
       ORDER BY days_since_hired desc,
                upper(first_name),
                upper(last_name),
                user_id
         FETCH FIRST 7 ROWS ONLY;   --hard coded to restrict;
  ELSE
    v_stage := 'Get result for specific promotions'; 
    OPEN p_out_user_data FOR 
     WITH rec_user as 
 (SELECT au.user_id,
         au.first_name,
         au.last_name,
         pe.position_type,
         p.avatar_small,
         trunc(sysdate+1) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created))) days_since_hired,
         pe.hire_Date,
         au.enrollment_date,
         au.date_created                     
    FROM application_user au,
         participant p,
         vw_curr_pax_employer pe,
         gtt_ra_receiver gr
   WHERE au.user_id = p.user_id
     AND au.user_id = gr.user_id
     AND au.user_id = pe.user_id(+)
     AND EXISTS (SELECT user_id
                   FROM audience a,
                        promo_audience pa,
                        participant_audience par,
                        gtt_ra_promotion gm
                  WHERE a.audience_id = pa.audience_id
                    AND par.audience_id = pa.audience_id
                    AND pa.promotion_id = gm.promotion_id
                    AND ((orgl_secondary_audience_type = 'specifyaudience'
                       AND pa.promo_audience_type = 'SECONDARY')
                    OR (orgl_secondary_audience_type <> 'specifyaudience'
                        AND pa.promo_audience_type = 'PRIMARY'))
                    AND par.user_id = gr.user_id
                    AND gm.promotion_status = 'live'
                UNION
                SELECT distinct un.user_id
                  FROM user_node un,
                       (SELECT node_id,
                               level AS node_level
                          FROM node
                         WHERE is_deleted = 0
                    CONNECT BY PRIOR node_id = parent_node_id
                      START WITH node_id IN (SELECT node_id
                                               FROM user_node
                                              WHERE user_id = p_in_user_id)) n
                 WHERE un.node_id = n.node_id
                   AND un.user_id = gr.user_id 
                   AND 1 = ANY (SELECT 1
                                  FROM gtt_ra_promotion
                                 WHERE secondary_audience_type = 'activepaxfromprimarynodebelowaudience'
                                   AND promotion_status = 'live')
                )
        AND au.is_active = 1
        AND p.is_opt_out_of_program = 0 
        AND p.status = 'active'),
-- Get all rec claim records                      
 rec_pax as
   (SELECT cr.participant_id,ci.approval_status_type,ci.date_created
      FROM claim_recipient cr,
           claim_item ci,
           recognition_claim rc,
           claim c,
           rec_user ru
     WHERE ci.claim_item_id = cr.claim_item_id 
       AND ci.claim_id = rc.claim_id
       AND rc.claim_id = c.claim_id
       AND c.submitter_id = p_in_user_id
       AND cr.participant_id = user_id
    ),
-- Get only pending records    
 rec_pend as 
    (SELECT *
      FROM (SELECT PR.*,
                   rank() over(partition by PR.participant_id order by pr.date_created desc) ranks
              FROM rec_pax pr
             WHERE approval_status_type = 'pend'
               AND NOT EXISTS (SELECT participant_id
                                 FROM rec_pax
                                WHERE participant_id = pr.participant_id
                                  AND approval_status_type <> 'pend')
            )
     WHERE ranks = 1)           
        SELECT user_id,
               first_name,
               last_name,
               position_type,
               avatar_small,
               days_since_hired
         FROM (SELECT *
                FROM rec_user gr
         WHERE days_since_hired <= v_days_new_to_regemp
           AND gr.user_id NOT IN (SELECT user_id  --Find pax which are not recog by only recognition type promotions
                                    FROM activity a,
                                         gtt_ra_promotion p,
                                         claim c
                                   WHERE a.promotion_id = p.promotion_id
                                     AND a.claim_id = c.claim_id
                                     AND c.submitter_id = p_in_user_id
                                     AND is_submitter = 0)) rec,
             rec_pend p
       WHERE rec.user_id = p.participant_id(+)
         AND p.approval_status_type IS NULL  
         ORDER BY days_since_hired desc,
                  upper(first_name),
                  upper(last_name),
                  user_id
                 FETCH FIRST 7 ROWS ONLY;   --hard coded to restrict
    END IF;    
    ELSE 
    OPEN p_out_user_data FOR 
      SELECT NULL user_id,
             NULL first_name,
             NULL last_name,
             NULL position_type,
             NULL avatar_small,
             NULL days_since_hired
        FROM dual  ;              
END IF;
                    
   COMMIT;
  prc_execution_log_entry(c_process_name,1,'INFO','Process Completed ',null);
  p_out_return_code:=0;
  
EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    p_out_return_code :=  99 ;
    prc_execution_log_entry(c_process_name,1,'ERROR',v_stage||' - '||SQLERRM,null);
    OPEN p_out_user_data FOR SELECT NULL FROM dual  ;
END;
/
