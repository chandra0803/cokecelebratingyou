CREATE OR REPLACE PROCEDURE prc_ccs_recognition_wall
   ( p_number_of_records IN NUMBER,
     p_out_return_code  OUT NUMBER,
     p_out_result_set   OUT sys_refcursor,
     p_out_result_set_recipients   OUT sys_refcursor) 
     
     IS
     /*********************************************************
  -- Purpose: 
  -- This procedure returns the result set for recognition wall.
  -- Person      Date        Comments
  -- ---------   ----------  -----------------------------------------------------
  -- Ravi Dhanekula 04/28/2016 Initial Version 
  -- Gorantla           08/19/2019 GitLab #2227 Public Recognition wall API is returning error 
  *********************************************************/
      v_team_ids              VARCHAR2 (4000);

      --Added for testing
    
  c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_CCS_RECOGNITION_WALL';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by         CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_stage              VARCHAR2(500);
   BEGIN



         SELECT LISTAGG (TEAM_ID, ', ') WITHIN GROUP (ORDER BY 1)
           INTO v_team_ids
           FROM (SELECT rownum rn, a.*
                   FROM (  SELECT *
                             FROM (SELECT *
                                     FROM (  SELECT rc.team_id,
                                                    ci.date_approved,
                                                    'recognition' trans_type
                                               FROM claim_item ci,
                                                    claim c,
                                                    ((SELECT -- get most recent participant employer record per user
                                                            r.claim_id, team_id
                                                        FROM (
                                                             SELECT RANK ()
                                                                     OVER (
                                                                        PARTITION BY pe.team_id
                                                                        ORDER BY
                                                                           ci.date_approved desc,pe.claim_id desc)
                                                                        AS rec_rank,
                                                                     pe.*
                                                                FROM recognition_claim pe,
                                                                          claim_item ci,
                                                                          claim_recipient cr,
                                                                          participant p
                                                               WHERE hide_public_recognition =
                                                                        0  AND pe.claim_id = ci.claim_id
                                                                            AND ci.approval_status_type = 'approv'
                                                                            AND ci.claim_item_id = cr.claim_item_id
                                                                            AND cr.participant_id = p.user_id
                                                                            AND p.allow_public_recognition = 1
                                                                            AND NVL(cr.notification_date,SYSDATE - 1) < SYSDATE
                                                                         ) r
                                                       -- the current employment record has the lowest ranking
                                                       WHERE r.rec_rank = 1)) rc,
                                                    promo_recognition pr,
                                                    promotion P
                                              WHERE     ci.claim_id = c.claim_id                                                    
                                                    AND c.claim_id = rc.claim_id
                                                    AND c.promotion_id =
                                                           pr.promotion_id
                                                    AND pr.allow_public_recognition =
                                                           1
                                                    AND pr.promotion_id =
                                                           P.promotion_id
                                                    AND P.promotion_status =
                                                           'live'
                                                    AND P.promotion_type =
                                                           'recognition'                                                    
                                           ORDER BY date_approved DESC)
                                    WHERE ROWNUM < 200
                                   UNION ALL
                                   SELECT *
                                     FROM (  SELECT rc.team_id,
                                                    NVL(cg.date_approved,ci.date_approved) date_approved,
                                                    'nomination' trans_type
                                               FROM claim_item ci,
                                                    claim c,
                                                    ((SELECT 
                                                            r.claim_id, team_id
                                                        FROM ( 
                                                              SELECT RANK ()
                                                                     OVER (
                                                                        PARTITION BY pe.team_id
                                                                        ORDER BY
                                                                           NVL(cg.date_approved,ci.date_approved) desc,pe.claim_id desc,ROWNUM desc)
                                                                           AS rec_rank,
                                                                     pe.*
                                                                FROM nomination_claim pe,
                                                                          claim_item ci,
                                                                          claim_participant cp,
                                                                          claim_recipient cr,
                                                                          participant p,
                                                                          claim c,
                                                                          claim_group cg
                                                               WHERE pe.hide_public_recognition = 0
                                                                          AND ci.claim_id = pe.claim_id
                                                                          AND ci.claim_item_id = cr.claim_item_id
                                                                          AND ci.approval_status_type = 'winner'
                                                                          AND pe.claim_id = cp.claim_id(+)
                                                                          AND NVL (cr.participant_id,cp.participant_id) = p.user_id
                                                                          AND ci.claim_id = c.claim_id
                                                                          AND c.claim_group_id = cg.claim_group_id(+)
                                                    AND p.allow_public_recognition = 1
                                                   AND NVL(NVL(cr.notification_date,cg.notification_date),SYSDATE-1) < SYSDATE                                             
                                                   AND NOT EXISTS (SELECT * FROM nomination_claim WHERE team_id = pe.team_id AND hide_public_recognition = 1)
                                                               ) r                                                     
                                                       WHERE r.rec_rank = 1)) rc,
                                                    promo_nomination pr,
                                                    promotion p,
                                                    claim_group cg
                                              WHERE     ci.claim_id = c.claim_id                                                   
                                                    AND c.claim_id = rc.claim_id
                                                    AND c.claim_group_id = cg.claim_group_id(+)
                                                    AND c.promotion_id =
                                                           pr.promotion_id
                                                    AND pr.allow_public_recognition =
                                                           1
                                                    AND pr.promotion_id =
                                                           P.promotion_id
                                                    AND P.promotion_status =
                                                           'live'
                                                    AND P.promotion_type =
                                                           'nomination'    
                                           ORDER BY date_approved DESC)
                                    WHERE ROWNUM < 200)
                         ORDER BY DATE_APPROVED DESC) a)
                  WHERE RN < p_number_of_records+1;


      OPEN p_out_result_set FOR          
SELECT team_id,team_name,date_approved                        
                        ,giver_first_name
                        ,giver_last_name
                        ,giver_avatar
                        ,submitter_comments
                        ,submitter_comments_lang_id                        
                        ,receiver_count
                        ,giver_user_id
                        ,promotion_id,  -- 08/19/2019 added comma
         promotion_name,
         CASE WHEN purl_recipient_id IS NOT NULL THEN 'PURL' ELSE promotion_type END promotion_type,
         is_include_purl,
         purl_recipient_id,
         card_active,
         card_id,
         card_image_name,
         hide_public_recognition,
         is_cumulative,
         giver_country,
         giver_org_unit,
         giver_department,
         giver_job_title,
         behavior
                        FROM (
  SELECT rc.team_id,NULL AS team_name,
         date_approved,
         pr.is_include_purl,
         (select purl_recipient_id FROM purL_recipient pr WHERE claim_id = rc.claim_id) purl_recipient_id,
         CASE WHEN pr.is_include_purl = 1 THEN NULL
         ELSE
         au_g.user_id
         END giver_user_id,
         au_g.First_name giver_first_name,
         au_g.last_name giver_last_name,
         p2.avatar_small giver_avatar,
         rc.submitter_comments,
         rc.submitter_comments_lang_id,
         pkg_list_recognition_wall.fnc_get_recog_recvr_cnt('recognition',rc.team_id) AS  receiver_count,
         p.promotion_id,
         p.promotion_name,
         p.promotion_type,
         pr.card_active,
         rc.card_id,
         NVL(card.large_image_name,rc.own_card_name) card_image_name,
         rc.hide_public_recognition,
         0 is_cumulative,
         cntry.country_code giver_country,
         n.name giver_org_unit,
         pep.department_type giver_department,
         pep.position_type giver_job_title,
         case when  rc.behavior = 'none'    --9/29/2014 bug fix 55111
                                                then null
                                                else  rc.behavior
                                                end as behavior        
    FROM claim_item ci,
         claim c,         
          ((SELECT
                                                            r.*
                                                        FROM ( 
                                                                SELECT RANK ()
                                                                     OVER (
                                                                        PARTITION BY pe.team_id
                                                                        ORDER BY
                                                                           ci.date_approved desc,ci.claim_id desc)
                                                                        AS rec_rank,
                                                                     pe.*
                                                                FROM recognition_claim pe,
                                                                          claim_item ci
                                                               WHERE hide_public_recognition =
                                                                        0  AND pe.claim_id = ci.claim_id
                                                                            AND ci.approval_status_type = 'approv'
                                                                  AND team_id IN (SELECT * FROM TABLE (get_array_varchar (v_team_ids)))                                                                        
                                                                         ) r                                                       
                                                       WHERE r.rec_rank = 1)) rc,
         promo_recognition pr,
         promotion p,
         application_user au_g,
         user_address ua,
         user_node un,
         node n,
         country cntry,
         participant p2,        
         card,
         participant_employer pep
   WHERE     ci.claim_id = c.claim_id    
         AND c.claim_id = rc.claim_id
         AND c.submitter_id = au_g.user_id
         AND c.promotion_id = pr.promotion_id
         AND au_g.user_id = un.user_id
         AND un.is_primary = 1
         AND un.node_id = n.node_id
         AND pr.allow_public_recognition = 1
         AND pr.promotion_id = p.promotion_id
         AND p.promotion_status = 'live'
         AND p.promotion_type = 'recognition'       
         AND c.submitter_id = p2.user_id         
         AND rc.card_id = card.card_id(+)
         AND au_g.user_id = ua.user_id 
         AND ua.is_primary = 1
         AND ua.country_id = cntry.country_id
         AND pep.user_id(+) = ua.user_id
         AND pep.termination_date IS NULL)         
         UNION ALL                  
         SELECT team_id,team_name,date_approved                       
                        ,giver_first_name
                        ,giver_last_name
                        ,avatar_small giver_avatar
                        ,submitter_comments
                        ,submitter_comments_lang_id                      
                        ,receiver_count
                        ,giver_user_id
                        ,promotion_id,  -- 08/19/2019 added comma
         promotion_name,
         promotion_type,
         0 is_include_purl,
         NULL purl_recipient_id,
         card_active,
         card_id,
         card_image_name,
         hide_public_recognition,
         is_cumulative,
         giver_country,
         giver_org_unit,
         giver_department,
         giver_job_title,
         behavior  
                        FROM (
         SELECT rc.team_id,rc.team_name,
         nvl(ci.date_approved,cg.date_approved) as date_approved,
         au_g.user_id giver_user_id,
         au_g.First_name giver_first_name,
         au_g.last_name giver_last_name,
         p2.avatar_small,
          CASE WHEN C.claim_group_id IS NOT NULL THEN NULL
          ELSE rc.submitter_comments
          END AS  submitter_comments,
         rc.submitter_comments_lang_id,
          CASE WHEN c.claim_group_id IS NOT NULL THEN 1
          ELSE pkg_list_recognition_wall.fnc_get_recog_recvr_cnt('nomination',rc.team_id) 
          END AS  receiver_count,
         p.promotion_id,
         p.promotion_name,
         p.promotion_type,
         pr.card_active,
         rc.card_id,
         NVL(card.large_image_name,rc.own_card_name) card_image_name,
         rc.hide_public_recognition,
          CASE WHEN c.claim_group_id IS NOT NULL THEN 1
          ELSE 0
          END AS  is_cumulative,
         cntry.country_code giver_country,
         n.name giver_org_unit,
         pep.department_type giver_department,
         pep.position_type giver_job_title,
--          case when  rc.behavior = 'none'    --9/29/2014 bug fix 55111
--                                                then null
--                                                else  rc.behavior
--                                                end as behavior     
--          CASE WHEN ncb.claim_id IS NULL THEN 'none'
--          ELSE LISTAGG (ncb.behavior  , ', ') WITHIN GROUP (ORDER BY 1) END as behavior
ncb.behavior
    FROM claim_item ci,
         claim c,         
           ((SELECT
                                                            r.*
                                                        FROM ( 
                                                              SELECT RANK ()
                                                                     OVER (
                                                                        PARTITION BY pe.team_id
                                                                        ORDER BY
                                                                           pe.claim_id)
                                                                        AS rec_rank,
                                                                     pe.*
                                                                FROM nomination_claim pe,
                                                                claim_item ci
                                                               WHERE team_id IN (SELECT * FROM TABLE (get_array_varchar (v_team_ids))) AND hide_public_recognition = 0
                                                               AND pe.claim_id = ci.claim_id
                                                               AND (   ci.approval_status_type = 'winner' OR ci.approval_status_type = 'approv')                                                                                                  
                                                                         ) r                                                       
                                                       WHERE r.rec_rank = 1)) rc,
         promo_nomination pr,
         promotion p,       
         application_user au_g,
         user_node un,
         node n,
         user_address ua,
         country cntry,
         participant p2,        
         card,
         claim_group cg,
         participant_employer pep,
         nomination_claim_behaviors ncb
   WHERE     ci.claim_id = c.claim_id         
         AND (   ci.approval_status_type = 'winner'
              OR ci.approval_status_type = 'approv')         
         AND c.claim_id = rc.claim_id
         AND c.claim_group_id = cg.claim_group_id (+)
         AND rc.hide_public_recognition = 0
         AND c.submitter_id = au_g.user_id
         AND c.promotion_id = pr.promotion_id
         AND pr.allow_public_recognition = 1
         AND pr.promotion_id = p.promotion_id
         AND p.promotion_status = 'live'
         AND p.promotion_type = 'nomination'                
         AND c.submitter_id = p2.user_id         
         AND rc.card_id = card.card_id(+)
         AND au_g.user_id = un.user_id
         AND un.is_primary = 1
         AND un.node_id = n.node_id
         AND au_g.user_id = ua.user_id 
         AND ua.is_primary = 1
         AND ua.country_id = cntry.country_id
         AND pep.user_id(+) = ua.user_id
         AND pep.termination_date IS NULL
         AND rc.claim_id = ncb.claim_id(+)
--         GROUP BY c.claim_id
         ) ORDER BY DATE_APPROVED DESC;


OPEN p_out_result_set_recipients FOR
                 SELECT rc.team_id,rc.claim_id,au.user_id recvr_user_id,au.first_name recvr_first_name,au.last_name recvr_last_name,p.avatar_small recvr_avatar,
                 pep.position_type receiver_job_title,pep.department_type receiver_department,cntry.country_code receiver_country,n.name receiver_org_unit FROM 
           recognition_claim rc,claim_item ci,claim_recipient cr,node n,user_address ua, country cntry,participant_employer pep,
           application_user au, participant p  WHERE team_id IN (SELECT *
                                                                FROM TABLE (
                                                                        get_array_varchar (
                                                                           v_team_ids)))
    AND rc.claim_id = ci.claim_id
    AND ci.claim_item_id = cr.claim_item_id
    AND cr.participant_id = au.user_id
    AND ci.approval_status_type = 'approv'
    AND au.user_id = p.user_id
    AND p.allow_public_recognition = 1
    AND cr.node_id = n.node_id
    AND cr.participant_id = ua.user_id
    AND ua.is_primary = 1
    AND ua.country_id = cntry.country_id
    AND p.user_id = pep.user_id (+)
    ANd pep.termination_date IS NULL
    UNION ALL
    SELECT rc.team_id,rc.claim_id,au.user_id recvr_user_id,au.first_name recvr_first_name,au.last_name recvr_last_name,p.avatar_small recvr_avatar,
                  pep.position_type receiver_job_title,pep.department_type receiver_department,cntry.country_code receiver_country,n.name receiver_org_unit FROM 
           nomination_claim rc,claim_item ci,claim_recipient cr,node n,user_address ua, country cntry,participant_employer pep,
           application_user au, participant p  WHERE team_id IN (SELECT *
                                                                FROM TABLE (
                                                                        get_array_varchar (
                                                                          v_team_ids)))
    AND rc.claim_id = ci.claim_id
    AND ci.approval_status_type = 'winner'
    AND ci.claim_item_id = cr.claim_item_id
    AND cr.participant_id = au.user_id
    AND au.user_id = p.user_id
    AND p.allow_public_recognition = 1    
    AND NVL(cr.notification_date,SYSDATE - 1) < SYSDATE
    AND cr.node_id = n.node_id
    AND cr.participant_id = ua.user_id
    AND ua.is_primary = 1
    AND ua.country_id = cntry.country_id
    AND p.user_id = pep.user_id (+)
    ANd pep.termination_date IS NULL
    ORDER BY team_id DESC;

p_out_return_code:=0;

END;
/