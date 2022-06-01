CREATE OR REPLACE PACKAGE pkg_engagement IS

/******************************************************************************
  NAME:       pkg_engagement
  PURPOSE:    List out the data for the public recognition wall.

  Date          Author           Description
  ----------    ---------------  ------------------------------------------------
  06/18/2014     Ravi Dhanekula    Initial code.               
******************************************************************************/

  FUNCTION fnc_check_public_recog_elig
 (p_user_id IN NUMBER,
 p_promotion_id IN NUMBER)
  RETURN VARCHAR2;
  
 FUNCTION fnc_get_budget_conv_ratio
 (p_team_id IN NUMBER)
  RETURN FLOAT;

  PROCEDURE prc_eng_user_dashboard
   ( p_in_userid  IN NUMBER,
     p_in_Listtype     IN VARCHAR2,
     p_in_node_id VARCHAR2,
     p_in_start_date VARCHAR2,
     p_in_end_date  VARCHAR2,
     p_rownum_start IN NUMBER,
     p_rownum_end IN NUMBER,   
     p_out_return_code  OUT NUMBER,
     p_out_result_set   OUT sys_refcursor,
     p_out_result_set_recipients   OUT sys_refcursor,
     p_out_result_set_pub_likes   OUT sys_refcursor,
     p_out_result_set_pub_comments       OUT SYS_REFCURSOR);
     
     PROCEDURE prc_eng_login_chart
( p_in_node_id VARCHAR2,
  p_in_start_date VARCHAR2,
  p_in_end_date  VARCHAR2,
  p_in_languageCode VARCHAR2,
  p_in_time_frame    IN VARCHAR2,     --11/18/2014  
  p_out_return_code       OUT NUMBER,
  p_out_data                  OUT SYS_REFCURSOR);
 PROCEDURE prc_engagement_user_details(
p_in_mode            IN    VARCHAR2,
p_in_user_id         IN    NUMBER,
p_in_locale         IN    VARCHAR2, --04/06/2016
P_in_time_frame    IN VARCHAR2,
p_in_end_month         IN    NUMBER,
p_in_end_year        IN       NUMBER,
p_out_result_set     OUT   sys_refcursor);
PROCEDURE prc_engagement_manager_score(
p_in_user_id        IN    NUMBER,
p_in_mode           IN   VARCHAR2,
p_in_time_frame    IN VARCHAR2,
p_in_node_id        IN    VARCHAR2,
p_in_locale         IN    VARCHAR2,
p_in_end_month     IN    NUMBER,
p_in_end_year       IN    NUMBER,
p_in_rownum_start   IN    NUMBER,
p_in_rownum_end     IN    NUMBER,
p_in_sort_mem_col  IN   VARCHAR2,
p_in_sort_mem_order IN VARCHAR2,
p_in_sort_team_col IN VARCHAR2,
p_in_sort_team_order IN VARCHAR2,
p_in_start_date     IN    VARCHAR2,
p_in_end_date       IN    VARCHAR2,
p_out_result_set1   OUT sys_refcursor,
p_out_result_set2   OUT sys_refcursor,
p_out_result_set3   OUT sys_refcursor,
p_out_result_set4   OUT sys_refcursor,
p_out_result_set5   OUT sys_refcursor,
p_out_result_set6   OUT sys_refcursor,
p_out_result_set7   OUT sys_refcursor,
p_out_result_set8   OUT sys_refcursor,
p_out_result_set9   OUT sys_refcursor,
p_out_user_name   OUT VARCHAR2,
p_out_return_code OUT NUMBER);

PROCEDURE prc_eng_childnodes_sort(
p_in_user_id        IN    NUMBER,
p_in_mode           IN   VARCHAR2,
p_in_time_frame    IN VARCHAR2,
p_in_node_id        IN    VARCHAR2,
p_in_locale         IN    VARCHAR2,
p_in_end_month     IN    NUMBER,
p_in_end_year       IN    NUMBER,
p_in_rownum_start   IN    NUMBER,
p_in_rownum_end     IN    NUMBER,
p_in_sort_mem_col  IN   VARCHAR2,
p_in_sort_mem_order IN VARCHAR2,
p_in_sort_team_col IN VARCHAR2,
p_in_sort_team_order IN VARCHAR2,
p_in_start_date     IN    VARCHAR2,
p_in_end_date       IN    VARCHAR2,
p_out_result_set1   OUT sys_refcursor);

PROCEDURE prc_eng_teammember_sort(
p_in_user_id        IN    NUMBER,
p_in_mode           IN   VARCHAR2,
p_in_time_frame    IN VARCHAR2,
p_in_node_id        IN    VARCHAR2,
p_in_locale         IN    VARCHAR2,
p_in_end_month     IN    NUMBER,
p_in_end_year       IN    NUMBER,
p_in_rownum_start   IN    NUMBER,
p_in_rownum_end     IN    NUMBER,
p_in_sort_mem_col  IN   VARCHAR2,
p_in_sort_mem_order IN VARCHAR2,
p_in_sort_team_col IN VARCHAR2,
p_in_sort_team_order IN VARCHAR2,
p_in_start_date     IN    VARCHAR2,
p_in_end_date       IN    VARCHAR2,
p_out_result_set1   OUT sys_refcursor);

PROCEDURE prc_eng_recog_byorg_chart
( p_in_node_id VARCHAR2,
  p_in_start_date VARCHAR2,
  p_in_end_date  VARCHAR2,
  p_in_giver_recvr VARCHAR2,
  p_in_time_frame    IN VARCHAR2,
  p_in_end_month     IN    NUMBER,
  p_in_end_year       IN    NUMBER,
  p_out_return_code       OUT NUMBER,
  p_out_data                  OUT SYS_REFCURSOR);
  
  PROCEDURE prc_eng_recog_bypromo_chart
( p_in_node_id VARCHAR2,
  p_in_user_id        IN    NUMBER,
  p_in_mode           IN   VARCHAR2,
  p_in_start_date VARCHAR2,
  p_in_end_date  VARCHAR2,
  p_in_giver_recvr VARCHAR2,
  p_in_time_frame    IN VARCHAR2,
  p_in_end_month     IN    NUMBER,
  p_in_end_year       IN    NUMBER,
  p_in_locale         IN    VARCHAR2,
  p_out_return_code       OUT NUMBER,
  p_out_data                  OUT SYS_REFCURSOR);
  
  PROCEDURE prc_eng_recog_byuser
( p_in_node_id VARCHAR2,
  p_in_user_id        IN    NUMBER,
  p_in_mode           IN   VARCHAR2,
  p_in_start_date VARCHAR2,
  p_in_end_date  VARCHAR2,
  p_in_giver_recvr VARCHAR2,
  p_in_time_frame    IN VARCHAR2,
  p_in_end_month     IN    NUMBER,
  p_in_end_year       IN    NUMBER,
  p_in_locale         IN    VARCHAR2,  
  p_out_viewer_first_name OUT VARCHAR2,
  p_out_viewer_avatar_url OUT VARCHAR2,
  p_out_return_code       OUT NUMBER,
  p_out_node_names OUT SYS_REFCURSOR,
  p_out_data                  OUT SYS_REFCURSOR);
  
  PROCEDURE prc_engagement_notification(
p_in_user_id        IN    NUMBER,
p_in_mode           IN   VARCHAR2,
p_in_end_month     IN    NUMBER,
p_in_end_year       IN    NUMBER,
p_out_result_set1   OUT sys_refcursor,
p_out_return_code OUT NUMBER);

PROCEDURE prc_participation_extract (
p_file_name          IN     VARCHAR2,
p_header             IN     VARCHAR2, 
p_in_time_frame      IN     VARCHAR2,
p_in_user_id         IN     NUMBER,
p_in_node_id         IN     NUMBER,    
p_in_end_month       IN     NUMBER,
p_in_end_year        IN     NUMBER,
p_out_return_code    OUT    VARCHAR2,
p_out_result_set     OUT    SYS_REFCURSOR  
);
END;


/
CREATE OR REPLACE PACKAGE BODY      pkg_engagement
IS
   /******************************************************************************
     NAME:       pkg_engagement
     PURPOSE:    List out the data for the Engagement dashboard

     Date                   Author           Description
     ----------           ---------------  ------------------------------------------------
     06/18/2014 Ravi Dhanekula    Initial code.

   ******************************************************************************/

   FUNCTION fnc_check_public_recog_elig (p_user_id        IN NUMBER,
                                         p_promotion_id   IN NUMBER)
      RETURN VARCHAR2
   IS
  /******************************************************************************
     NAME:       fnc_check_public_recog_elig
     PURPOSE:    Check the public recog eligibility for engagement dashboard

     Date                   Author           Description
     ----------           ---------------  ------------------------------------------------
     06/18/2014 Ravi Dhanekula    Initial code.

   ******************************************************************************/
      v_public_recog_budget_masterid   NUMBER;
      v_return                         VARCHAR2 (4000)
                                          := 'N' || ','|| ' ' || ',' || ' ' || ', '||' ';
      v_budget_type                    VARCHAR2 (10);
   BEGIN
      SELECT public_rec_budget_master_id
        INTO v_public_recog_budget_masterid
        FROM promo_recognition pr
       WHERE promotion_id = p_promotion_id;


      IF v_public_recog_budget_masterid IS NULL
      THEN
         v_return := 'Y' || ','|| ' ' || ',' || ' ' || ', '||' ';
      ELSE
         BEGIN
            SELECT budget_type
              INTO v_budget_type
              FROM budget_master bm
             WHERE bm.budget_master_id = v_public_recog_budget_masterid;
         END;

         IF v_budget_type = 'pax'
         THEN
            SELECT    'Y'
                   || ','
                   || b.budget_id
                   || ','
                   || b.current_value
                   || ','
                   || bm.cm_asset_code
              INTO v_return
              FROM budget_master bm,
                   promo_recognition pr,
                   budget_segment bms,
                   budget b
             WHERE     pr.promotion_id = p_promotion_id
                   AND pr.public_rec_budget_master_id = bm.budget_master_id
                   AND bm.budget_master_id = bms.budget_segment_id
                   AND bms.budget_segment_id = b.budget_segment_id
                   AND b.user_id = p_user_id
                   AND NVL (public_rec_award_amount_fixed,
                            public_rec_award_amount_max) <= b.current_value;
         ELSIF v_budget_type = 'node'
         THEN
            SELECT    'Y'
                   || ','
                   || b.budget_id
                   || ','
                   || b.current_value
                   || ','
                   || bm.cm_asset_code
              INTO v_return
              FROM budget_master bm,
                   promo_recognition pr,
                   budget_segment bms,
                   budget b
             WHERE     pr.promotion_id = p_promotion_id
                   AND pr.public_rec_budget_master_id = bm.budget_master_id
                   AND bm.budget_master_id = bms.budget_segment_id
                   AND bms.budget_segment_id = b.budget_segment_id
                   AND EXISTS
                          (SELECT *
                             FROM user_node
                            WHERE     user_id = p_user_id
                                  AND node_id = b.node_id
                                  AND role IN ('own', 'mgr'))
                   AND NVL (public_rec_award_amount_fixed,
                            public_rec_award_amount_max) <= b.current_value;
         ELSIF v_budget_type = 'central'
         THEN
            SELECT    'Y'
                   || ','
                   || b.budget_id
                   || ','
                   || b.current_value
                   || ','
                   || bm.cm_asset_code
              INTO v_return
              FROM budget_master bm,
                   promo_recognition pr,
                   budget_segment bms,
                   budget b
             WHERE     pr.promotion_id = p_promotion_id
                   AND pr.public_rec_budget_master_id = bm.budget_master_id
                   AND bm.budget_master_id = bms.budget_segment_id
                   AND bms.budget_segment_id = b.budget_segment_id
                   AND EXISTS
                          (SELECT *
                             FROM user_node
                            WHERE user_id = p_user_id AND role IN ('own', 'mgr'))
                   AND NVL (public_rec_award_amount_fixed,
                            public_rec_award_amount_max) <= b.current_value;
         END IF;
      END IF;

      RETURN v_return;
   EXCEPTION
      WHEN OTHERS
      THEN
         prc_execution_log_entry (
            'FNC_CHECK_PUBLIC_RECOG_ELIG',
            1,
            'ERROR',
            'user: ' || p_user_id || ' promo: ' || p_promotion_id || SQLERRM,
            NULL);
         v_return := 'N' || ','|| ' ' || ',' || ' ' || ', '||' ';
         RETURN v_return;
   END;

FUNCTION fnc_get_budget_conv_ratio
 (p_team_id IN NUMBER)
      RETURN FLOAT
   IS
    /******************************************************************************
     NAME:       fnc_get_budget_conv_ratio
     PURPOSE:    Check the public recog eligibility for engagement dashboard

     Date                   Author           Description
     ----------           ---------------  ------------------------------------------------
     06/18/2014 Ravi Dhanekula    Initial code.

   ******************************************************************************/  
  c_process_name       CONSTANT execution_log.process_name%TYPE := 'FNC_GET_BUDGET_CONV_RATIO';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by         CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_stage              VARCHAR2(500);
  v_return FLOAT;
   BEGIN
   
   SELECT NVL(c1.budget_media_value,1)/NVL(c2.budget_media_value,1) INTO v_return FROM activity a1,
                            activity a2,
                            recognition_claim rc,
                            user_address ua1,
                            user_address ua2,
                            country c1,
                            country c2 WHERE 
                            rc.team_id = p_team_id
                            AND rc.claim_id = a1.claim_id                            
                            AND a1.is_submitter =1
                            AND a1.user_id = ua1.user_id
                            AND ua1.is_primary = 1
                            AND ua1.country_id = c1.country_id
                            AND rc.claim_id = a2.claim_id
                            AND a2.is_submitter =0
                            AND a2.user_id = ua2.user_id
                            AND ua2.is_primary = 1
                            AND ua2.country_id = c2.country_id;

      RETURN v_return;
   EXCEPTION
      WHEN OTHERS
      THEN
         prc_execution_log_entry (
            'FNC_GET_BUDGET_CONV_RATIO',
            1,
            'ERROR',
            'team_id: ' ||p_team_id || SQLERRM,
            NULL);
--         v_return := 1;
         RETURN v_return;
   END;
   
   PROCEDURE prc_eng_user_dashboard (
      p_in_userid         IN     NUMBER,
      p_in_Listtype     IN VARCHAR2,
      p_in_node_id VARCHAR2,
      p_in_start_date VARCHAR2,
     p_in_end_date  VARCHAR2,
      p_rownum_start      IN     NUMBER,
      p_rownum_end        IN     NUMBER,
      p_out_return_code      OUT NUMBER,
     p_out_result_set   OUT SYS_REFCURSOR,
     p_out_result_set_recipients   OUT SYS_REFCURSOR,          
      p_out_result_set_pub_likes   OUT SYS_REFCURSOR,
      p_out_result_set_pub_comments       OUT SYS_REFCURSOR)
   IS
   
   /******************************************************************************
     PURPOSE:    Used to create a data set results for recognition wall.

     Date                   Author           Description
     ----------           ---------------  ------------------------------------------------
     06/18/2014 Ravi Dhanekula    Initial code.
    --09/11/2014  Suresh J    -  Bug Fix 55475 added public recognition comment ID to the result set 
    09/19/2014  Ravi Dhanekula    Bug # 56709
    09/29/2014  Ravi Dhanekula    Bug # 56961 restrict recognitions to match rpm refresh date
    04/14/2017  Ravi Dhanekula    G6-2177 Add is_opt_out_of_awards flag.
    04/01/2019  Suresh J          SA Integeration with DayMaker. Excludes/Includes PURL/Celebration promotions based on boolean value of new.service.anniversary.enabled              
    04/29/2019
   ******************************************************************************/ 
      -- constants
      c_delimiter    CONSTANT VARCHAR2 (1) := '|';
      c2_delimiter   CONSTANT VARCHAR2 (1) := '"';
      v_team_ids              VARCHAR2 (4000);    
   
  c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_ENG_USER_DASHBOARD';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by         CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_stage              VARCHAR2(500);
  v_refresh_date DATE;
  p_start_date DATE;
  p_end_date DATE;
  v_sa_enabled         os_propertyset.boolean_val%type;     --04/01/2019

  BEGIN
   
   p_start_date := TO_DATE(P_IN_START_DATE,'MM/DD/YYYY');
   p_end_date := TO_DATE(P_IN_END_DATE,'MM/DD/YYYY');
 
    BEGIN
      SELECT boolean_val                            --04/01/2019
        INTO v_sa_enabled
        FROM os_propertyset
       WHERE entity_name = 'new.service.anniversary.enabled';
     EXCEPTION
         WHEN OTHERS THEN
         v_sa_enabled := 0;
     END;
     
v_stage := 'Get RPM refresh_date from promo_engagement';
 
 BEGIN
      SELECT as_of_date INTO v_refresh_date FROM promo_engagement pe WHERE EXISTS 
      (SELECT * FROM promotion WHERE promotion_status = 'live' AND promotion_id = pe.promotion_id);
      EXCEPTION
      WHEN OTHERS THEN
       prc_execution_log_entry(c_process_name,1,'INFO',SQLERRM,null);  --sql error fix
      END;  
   
 v_stage := 'get v_team_ids';  
     
 IF p_in_Listtype = 'received' THEN
 SELECT LISTAGG (TEAM_ID, ', ') WITHIN GROUP (ORDER BY 1)
           INTO v_team_ids
           FROM ( SELECT ROWNUM rn, A.* FROM (  SELECT DISTINCT TEAM_ID AS team_Id
                     FROM (SELECT recognition_claim.team_id
          FROM claim 
          INNER JOIN (SELECT * FROM promotion WHERE promotion_id IN ( --09/19/2014
          SELECT eligible_promotion_id from (SELECT * FROM (SELECT * FROM (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) WHERE promotion_id IN (SELECT promotion_id FROM promotion where promotion_status = 'live'))) p ON p.promotion_id = claim.promotion_id  --04/01/2019
          INNER JOIN claim_item ON claim_item.claim_id = claim.claim_id
          INNER JOIN claim_recipient ON claim_recipient.claim_item_id = claim_item.claim_item_id
          INNER JOIN recognition_claim ON recognition_claim.claim_id=claim.claim_id
          INNER JOIN promo_recognition ON promo_recognition.promotion_id = p.promotion_id
--          INNER JOIN participant recognition_participant ON recognition_participant.user_id = claim_recipient.participant_id         
          WHERE p.promotion_status = 'live'
          AND claim.is_add_points_claim = 0          
          AND ( claim_item.approval_status_type = 'winner' OR claim_item.approval_status_type = 'approv' )         
          AND claim_item.date_approved <= v_refresh_date --Bug # 56961 09/29/2014
          AND TRUNC(claim_item.date_approved) BETWEEN p_start_date AND p_end_date
          AND claim_recipient.participant_id = p_in_userid 
          AND (p_in_node_id IS NULL OR claim_recipient.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )))         
          )
                 ORDER BY TEAM_ID DESC) A )
          WHERE RN BETWEEN p_rownum_start AND p_rownum_end;
       ELSIF p_in_Listtype = 'sent' THEN
         SELECT LISTAGG (TEAM_ID, ', ') WITHIN GROUP (ORDER BY 1)
           INTO v_team_ids
           FROM ( SELECT ROWNUM rn, A.* FROM (  SELECT DISTINCT TEAM_ID AS team_Id
                     FROM (SELECT recognition_claim.team_id
          FROM claim 
          INNER JOIN (SELECT * FROM promotion WHERE promotion_id IN ( --09/19/2014
          SELECT eligible_promotion_id from (SELECT * FROM (SELECT * FROM (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) WHERE promotion_id IN (SELECT promotion_id FROM promotion where promotion_status = 'live'))) p ON p.promotion_id = claim.promotion_id  --04/01/2019
          INNER JOIN claim_item ON claim_item.claim_id = claim.claim_id
          INNER JOIN activity ON activity.claim_id = claim.claim_id
          INNER JOIN recognition_claim ON recognition_claim.claim_id=claim.claim_id
          INNER JOIN promo_recognition ON promo_recognition.promotion_id = p.promotion_id
          WHERE p.promotion_status = 'live'
          AND claim.is_add_points_claim = 0                   
          AND ( claim_item.approval_status_type = 'winner' OR claim_item.approval_status_type = 'approv' )                   
          AND claim_item.date_approved <= v_refresh_date --Bug # 56961 09/29/2014
          AND TRUNC(claim_item.date_approved) BETWEEN p_start_date AND p_end_date
          AND activity.is_submitter = 1
          AND activity.user_id = p_in_userid   
          AND (p_in_node_id IS NULL OR activity.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )))                 
          )
                 ORDER BY TEAM_ID DESC) A )
          WHERE RN BETWEEN p_rownum_start AND p_rownum_end;
      END IF;

--prc_execution_log_entry(c_process_name,1,'INFO', 'V_TEAM_IDS-->' ||v_team_ids,null);  --04/29/2019
v_stage := 'Fetch  results for p_out_result_set for user '||p_in_userid||' And for list-type '|| p_in_Listtype;
      OPEN p_out_result_set FOR          
SELECT team_id,team_name,date_approved                        
                        ,giver_first_name
                        ,giver_last_name
                        ,giver_avatar
                        ,submitter_comments
                        ,submitter_comments_lang_id                        
                        ,receiver_count
                        ,giver_user_id                       
                        ,add_points_elig
                        ,fnc_check_public_recog_elig (p_in_userid,
                                                        promotion_id) AS budget_data,
                         CASE WHEN receiver_count = 1 AND add_points_elig ='Y' THEN
                         fnc_get_budget_conv_ratio (team_id) 
                         ELSE NULL
                         END budget_conversion_ratio
                        ,likes_count
                        ,comments_count,
                        promotion_id,
         promotion_name,
         promotion_type,
         is_include_purl,
         purl_recipient_id,
         allow_public_recog_points,
         public_rec_award_type_fixed,
         public_rec_award_amount_min,
         public_rec_award_amount_max,
         public_rec_award_amount_fixed,
         card_active,
         card_id,
         card_image_name,
         hide_public_recognition     
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
         (SELECT COUNT(1) FROM recognition_claim WHERE team_id = rc.team_id) receiver_count,
         (SELECT COUNT(1) FROM public_recognition_like WHERE team_id = rc.team_id) likes_count,
         (SELECT COUNT(1) FROM public_recognition_comment WHERE team_id = rc.team_id) comments_count,
         CASE
            WHEN pr.public_rec_audience_type = 'allactivepaxaudience' THEN 'Y'
            ELSE 'N'
         END
            add_points_elig,
         p.promotion_id,
         p.promotion_name,
         p.promotion_type,
         pr.allow_public_recog_points,
         pr.public_rec_award_type_fixed,
         pr.public_rec_award_amount_min,
         pr.public_rec_award_amount_max,
         pr.public_rec_award_amount_fixed,
         pr.card_active,
         rc.card_id,
         NVL(card.large_image_name,rc.own_card_name) card_image_name,
         rc.hide_public_recognition 
    FROM claim_item ci,
         claim c,         
          ((SELECT
                                                            r.*
                                                        FROM ( 
                                                              SELECT RANK ()
                                                                     OVER (
                                                                        PARTITION BY pe.team_id
                                                                        ORDER BY
                                                                           claim_id)
                                                                        AS rec_rank,
                                                                     pe.*
                                                                FROM recognition_claim pe
                                                               WHERE team_id IN (SELECT * FROM TABLE (get_array_varchar (v_team_ids))) 
                                                                         ) r                                                       
                                                       WHERE r.rec_rank = 1)) rc,
         promo_recognition pr,
         promotion p,
         (SELECT 'Y' AS elig, pa.promotion_id
            FROM promo_audience pa, participant_audience paa
           WHERE     promo_audience_type = 'PUBLIC_RECOGNITION'
                 AND pa.audience_id = paa.audience_id
                 AND user_id = p_in_userid) pa,
         application_user au_g,
         participant p2,        
         card,
        (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep       --04/29/2019         
   WHERE     ci.claim_id = c.claim_id         
         AND (   ci.approval_status_type = 'winner'
              OR ci.approval_status_type = 'approv')         
         AND c.claim_id = rc.claim_id
--         AND rc.hide_public_recognition = 0
         AND c.submitter_id = au_g.user_id
         AND c.promotion_id = pr.promotion_id
--         AND pr.allow_public_recognition = 1
         AND pr.promotion_id = p.promotion_id
         AND p.promotion_status = 'live'
         AND p.promotion_type = 'recognition'
         AND p.promotion_id = pep.eligible_promotion_id     --04/29/2019         
         AND pr.promotion_id = pa.promotion_id(+)         
         AND c.submitter_id = p2.user_id         
         AND rc.card_id = card.card_id(+)
GROUP BY rc.team_id,rc.claim_id,
         au_g.user_id,
         au_g.Last_name,
         au_g.first_name,
         p2.avatar_small,
         rc.submitter_comments,
         rc.submitter_comments_lang_id,
         pr.public_rec_audience_type,
         pa.elig,
         p.promotion_id,
         p.promotion_name,
         p.promotion_type,
         pr.allow_public_recog_points,
         pr.public_rec_award_type_fixed,
         pr.public_rec_award_amount_min,
         pr.public_rec_award_amount_max,
         pr.public_rec_award_amount_fixed,
         pr.card_active,
         rc.card_id,
         NVL(card.large_image_name,rc.own_card_name),
         rc.hide_public_recognition,
         ci.date_approved,is_include_purl        )         
         UNION ALL                  
         SELECT team_id,team_name,date_approved                       
                        ,giver_first_name
                        ,giver_last_name
                        ,avatar_small giver_avatar
                        ,submitter_comments
                        ,submitter_comments_lang_id                      
                        ,receiver_count
                        ,giver_user_id                       
                        ,add_points_elig
                        ,NULL AS budget_data
                        ,NULL AS budget_conversion_ratio
                        ,likes_count
                        ,comments_count,
                        promotion_id,
         promotion_name,
         promotion_type,
         0 is_include_purl,
         NULL purl_recipient_id,
         allow_public_recog_points,
         public_rec_award_type_fixed,
         public_rec_award_amount_min,
         public_rec_award_amount_max,
         public_rec_award_amount_fixed,
         card_active,
         card_id,
         card_image_name,
         hide_public_recognition     
                        FROM (
         SELECT rc.team_id,rc.team_name,
         date_approved,
         au_g.user_id giver_user_id,
         au_g.First_name giver_first_name,
         au_g.last_name giver_last_name,
         p2.avatar_small,
         rc.submitter_comments,
         rc.submitter_comments_lang_id,
          (SELECT COUNT(1) FROM nomination_claim WHERE team_id = rc.team_id) receiver_count,
        (SELECT COUNT(1) FROM public_recognition_like WHERE team_id = rc.team_id) likes_count,
        (SELECT COUNT(1) FROM public_recognition_comment WHERE team_id = rc.team_id) comments_count,
        'N' AS   add_points_elig,
         p.promotion_id,
         p.promotion_name,
         p.promotion_type,
         NULL AS allow_public_recog_points,
         NULL AS public_rec_award_type_fixed,
         NULL AS public_rec_award_amount_min,
         NULL AS public_rec_award_amount_max,
         NULL AS public_rec_award_amount_fixed,
         pr.card_active,
         rc.card_id,
         NVL(card.large_image_name,rc.own_card_name) card_image_name,
         rc.hide_public_recognition 
    FROM claim_item ci,
         claim c,         
           ((SELECT
                                                            r.*
                                                        FROM ( 
                                                              SELECT RANK ()
                                                                     OVER (
                                                                        PARTITION BY pe.team_id
                                                                        ORDER BY
                                                                           claim_id)
                                                                        AS rec_rank,
                                                                     pe.*
                                                                FROM nomination_claim pe
                                                               WHERE team_id IN (SELECT * FROM TABLE (get_array_varchar (v_team_ids))) 
                                                                         ) r                                                       
                                                       WHERE r.rec_rank = 1)) rc,
         promo_nomination pr,
         promotion p,       
         application_user au_g,
         participant p2,        
         card,
         (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep	--04/29/2019
   WHERE     ci.claim_id = c.claim_id         
         AND (   ci.approval_status_type = 'winner'
              OR ci.approval_status_type = 'approv')         
         AND c.claim_id = rc.claim_id
         AND c.submitter_id = au_g.user_id
         AND c.promotion_id = pr.promotion_id        
         AND pr.promotion_id = p.promotion_id
         AND p.promotion_status = 'live'
         AND p.promotion_type = 'nomination'    
         AND p.promotion_id = pep.eligible_promotion_id     --04/29/2019                     
         AND c.submitter_id = p2.user_id         
         AND rc.card_id = card.card_id(+)
GROUP BY rc.team_id,rc.team_name,
         au_g.user_id,
         au_g.Last_name,
         au_g.first_name,
         p2.avatar_small,
         rc.submitter_comments,
         rc.submitter_comments_lang_id,         
         p.promotion_id,
         p.promotion_name,
         p.promotion_type,         
         pr.card_active,
         rc.card_id,
         NVL(card.large_image_name,rc.own_card_name),
         rc.hide_public_recognition,
         ci.date_approved) ORDER BY DATE_APPROVED DESC;

v_stage := 'Fetch  results for p_out_result_set_recipients for user '||p_in_userid||' And for list-type '|| p_in_Listtype||' And for team_ids '||v_team_ids;

OPEN p_out_result_set_recipients FOR
                 SELECT rc.team_id,rc.claim_id,au.user_id recvr_user_id,au.first_name recvr_first_name,au.last_name recvr_last_name,p.avatar_small recvr_avatar,p.is_opt_out_of_awards FROM--04/14/2017 
           recognition_claim rc,claim_item ci,claim_recipient cr,
           application_user au, participant p  WHERE team_id IN (SELECT *
                                                                FROM TABLE (
                                                                        get_array_varchar (
                                                                           v_team_ids)))
    AND rc.claim_id = ci.claim_id    
    AND ci.claim_item_id = cr.claim_item_id
    AND cr.participant_id = au.user_id
    AND au.user_id = p.user_id
    UNION ALL
    SELECT rc.team_id,rc.claim_id,au.user_id recvr_user_id,au.first_name recvr_first_name,au.last_name recvr_last_name,p.avatar_small recvr_avatar,p.is_opt_out_of_awards FROM--04/14/2017 
           nomination_claim rc,claim_item ci,claim_recipient cr,
           application_user au, participant p  WHERE team_id IN (SELECT *
                                                                FROM TABLE (
                                                                        get_array_varchar (
                                                                           v_team_ids)))
    AND rc.claim_id = ci.claim_id    
    AND ci.claim_item_id = cr.claim_item_id
    AND cr.participant_id = au.user_id
    AND au.user_id = p.user_id
    ORDER BY team_id DESC;

v_stage := 'Fetch  results for p_out_result_set_pub_likes for user '||p_in_userid||' And for list-type '|| p_in_Listtype||' And for team_ids '||v_team_ids;

OPEN p_out_result_set_pub_likes FOR
                 SELECT rc.team_id,prc.claim_id,au.user_id liker_user_id,au.first_name liker_first_name,au.last_name liker_last_name                                       
                                      FROM (-- rank records by termination date and employer index in reverse order
         select RANK() OVER (PARTITION BY pe.team_id ORDER BY claim_id) as rec_rank,
                pe.*
           FROM recognition_claim pe WHERE pe.team_id IN (SELECT *
                                                                FROM TABLE (
                                                                        get_array_varchar (
                                                                           v_team_ids)))
       ) rc , public_recognition_like prc,application_user au
WHERE rc.rec_rank = 1 AND rc.claim_id = prc.claim_id
AND prc.user_id = au.user_id
UNION ALL
SELECT rc.team_id,prc.claim_id,au.user_id liker_user_id,au.first_name liker_first_name,au.last_name liker_last_name                                       
                                      FROM (-- rank records by termination date and employer index in reverse order
         select RANK() OVER (PARTITION BY pe.team_id ORDER BY claim_id) as rec_rank,
                pe.*
           FROM nomination_claim pe WHERE pe.team_id IN (SELECT *
                                                                FROM TABLE (
                                                                        get_array_varchar (
                                                                           v_team_ids)))
       ) rc , public_recognition_like prc,application_user au
WHERE rc.rec_rank = 1 AND rc.claim_id = prc.claim_id
AND prc.user_id = au.user_id
ORDER BY team_id DESC;

v_stage := 'Fetch  results for p_out_result_set_pub_comments for user '||p_in_userid||' And for list-type '|| p_in_Listtype||' And for team_ids '||v_team_ids;

OPEN p_out_result_set_pub_comments FOR
                 SELECT rc.team_id,prc.claim_id,prc.comments public_comments,prc.comments_lang_id public_comments_language,au.user_id commenter_user_id,au.first_name commenter_first_name,au.last_name commenter_last_name                                       
                 ,prc.public_recognition_comment_id public_recognition_comment_id   --09/11/2014
                                      FROM (-- rank records by termination date and employer index in reverse order
         select RANK() OVER (PARTITION BY pe.team_id ORDER BY claim_id) as rec_rank,
                pe.*
           FROM recognition_claim pe WHERE pe.team_id IN (SELECT *
                                                                FROM TABLE (
                                                                        get_array_varchar (
                                                                           v_team_ids)))
       ) rc , public_recognition_comment prc,application_user au
WHERE rc.rec_rank = 1 AND rc.claim_id = prc.claim_id    
AND prc.user_id = au.user_id
UNION ALL
SELECT rc.team_id,prc.claim_id,prc.comments public_comments,prc.comments_lang_id public_comments_language,au.user_id commenter_user_id,au.first_name commenter_first_name,au.last_name  commenter_last_name                                      
       ,prc.public_recognition_comment_id public_recognition_comment_id   --09/11/2014
                                      FROM (-- rank records by termination date and employer index in reverse order
         select RANK() OVER (PARTITION BY pe.team_id ORDER BY claim_id) as rec_rank,
                pe.*
           FROM nomination_claim pe WHERE pe.team_id IN (SELECT *
                                                                FROM TABLE (
                                                                        get_array_varchar (
                                                                           v_team_ids)))
       ) rc , public_recognition_comment prc,application_user au
WHERE rc.rec_rank = 1 AND rc.claim_id = prc.claim_id
AND prc.user_id = au.user_id
ORDER BY team_id DESC;

      p_out_return_code := '00';

   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_result_set FOR SELECT NULL FROM DUAL;
   END;
   
 PROCEDURE prc_eng_login_chart
( p_in_node_id VARCHAR2,
  p_in_start_date VARCHAR2,
  p_in_end_date  VARCHAR2,
  p_in_languageCode VARCHAR2,
  p_in_time_frame    IN VARCHAR2,     --11/18/2014  
  p_out_return_code       OUT NUMBER,
  p_out_data                  OUT SYS_REFCURSOR)
  IS
  /******************************************************************************
     PURPOSE:    Used to create a data set results for login counts chart for manager dashboard.

     Date                   Author           Description
     ----------           ---------------  ------------------------------------------------
     06/18/2014 Ravi Dhanekula    Initial code.
     09/30/2014 Ravi Dhanekula    Bug # 57047.
     11/12/2014     Swati              Bug 57851 - In the Site Visits trend chart, display 0 activity as well
    11/18/2014   Suresh J        Bug 57850 - Site Visits Chart X Axis should change based on time period selected 
   ******************************************************************************/ 
  p_start_date DATE;
  p_end_date DATE;
  c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_ENG_LOGIN_CHART';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by         CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_stage              VARCHAR2(500);
  
  BEGIN
   prc_execution_log_entry(c_process_name,1,'INFO','START',null);  
    
      p_start_date := TO_DATE(P_IN_START_DATE,'MM/DD/YYYY');
      p_end_date := TO_DATE(P_IN_END_DATE,'MM/DD/YYYY');
      
    /*  OPEN p_out_data FOR  -- 11/12/2014 Bug 57851
    SELECT d.login_activity_count,date_created
    FROM engagement_quart_login_cnt d
   WHERE d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )) 
    AND    date_created BETWEEN p_start_date AND p_end_date
    ORDER BY date_created; --Bug # 57047 */

        IF p_in_time_frame = 'month' THEN    --11/18/2014
            OPEN p_out_data FOR -- 11/12/2014 Bug 57851
                SELECT NVL(login_activity_count,0) login_activity_count, date_within_range.date_created
                FROM 
                    (SELECT d.login_activity_count,date_created
                        FROM engagement_quart_login_cnt d
                       WHERE d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )) 
                        AND    date_created BETWEEN p_start_date AND p_end_date) e,
                    (SELECT TO_DATE(P_IN_START_DATE,'MM/DD/YYYY') + ROWNUM -1 date_created
                          from all_objects
                         WHERE ROWNUM <= TO_DATE(P_IN_END_DATE,'MM/DD/YYYY') - 
                         TO_DATE(P_IN_START_DATE,'MM/DD/YYYY')+1) date_within_range     
                WHERE  date_within_range.date_created = e.date_created(+)
                ORDER BY date_within_range.date_created;
         END IF;  

        IF p_in_time_frame = 'quarter' THEN    --11/18/2014
                OPEN p_out_data FOR  --11/18/2014
                SELECT NVL(login_activity_count,0) login_activity_count,date_within_range.week_start_date as Date_Created  
                FROM 
                    (SELECT Week_Number,Year_Month,sum(login_activity_count) as login_activity_count
                        FROM
                        (SELECT to_char(date_created ,'YYYYMM') year_month
                               ,to_char(date_created , 'W' ) as Week_Number
                               ,d.login_activity_count
                               ,date_created
                                        FROM engagement_quart_login_cnt d
                                       WHERE d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )) 
                                        AND    date_created BETWEEN p_start_date AND p_end_date )
                        GROUP BY Year_Month,Week_Number) e,
                    (SELECT Week_Number,year_month,min(date_created) as Week_Start_Date, max(date_created) as Week_End_Date, count(*)
                        FROM 
                        (SELECT  TO_DATE(P_IN_START_DATE,'MM/DD/YYYY') + ROWNUM -1 date_created
                               ,to_char(TO_DATE(P_IN_START_DATE,'MM/DD/YYYY') + ROWNUM -1,'YYYYMM') year_month
                               ,to_char( (TO_DATE(P_IN_START_DATE,'MM/DD/YYYY') + ROWNUM -1), 'W' ) as Week_Number
                                          from all_objects
                                         WHERE ROWNUM <= TO_DATE(P_IN_END_DATE,'MM/DD/YYYY') - 
                                         TO_DATE(P_IN_START_DATE,'MM/DD/YYYY')+1) quarter_chart
                        group by  year_month,Week_Number        
                    ) date_within_range     
                WHERE  date_within_range.Week_Number = e.Week_Number(+)
                       AND date_within_range.year_month = e.year_month(+)
                ORDER BY date_within_range.Year_Month,date_within_range.Week_Number;
        END IF;

        IF p_in_time_frame = 'year' THEN    --11/18/2014
            OPEN p_out_data FOR  --11/18/2014
                SELECT NVL(login_activity_count,0) login_activity_count,date_within_range.Month_Start_Date as Date_Created
                FROM 
                (SELECT Year_Month,sum(login_activity_count) as login_activity_count
                FROM
                (SELECT to_char(date_created ,'YYYYMM') year_month
                       ,d.login_activity_count
                       ,date_created
                                FROM engagement_quart_login_cnt d
                               WHERE d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )) 
                                AND    date_created BETWEEN p_start_date AND p_end_date )
                GROUP BY Year_Month
                ) e,
                (SELECT year_month,min(date_created) as Month_Start_Date, max(date_created) as Month_End_Date, count(*)
                FROM 
                    (SELECT  TO_DATE(P_IN_START_DATE,'MM/DD/YYYY') + ROWNUM -1 date_created
                           ,to_char(TO_DATE(P_IN_START_DATE,'MM/DD/YYYY') + ROWNUM -1,'YYYYMM') year_month
                                      from all_objects
                                     WHERE ROWNUM <= TO_DATE(P_IN_END_DATE,'MM/DD/YYYY') - 
                                     TO_DATE(P_IN_START_DATE,'MM/DD/YYYY')+1) quarter_char
                group by  year_month) date_within_range     
                WHERE  date_within_range.year_month = e.year_month(+)
                ORDER BY date_within_range.Year_Month;
        END IF;

  EXCEPTION
      WHEN OTHERS
      THEN
         prc_execution_log_entry (
             c_process_name,
            1,
            'ERROR',
            'p_in_node_id: ' ||p_in_node_id || SQLERRM,
            NULL);
  END;

 PROCEDURE prc_engagement_user_details(
p_in_mode            IN    VARCHAR2,
p_in_user_id         IN    NUMBER,
p_in_locale         IN    VARCHAR2, --04/06/2016
P_in_time_frame    IN VARCHAR2,
p_in_end_month         IN    NUMBER,
p_in_end_year        IN       NUMBER,
p_out_result_set     OUT   sys_refcursor) IS

/******************************************************************************
     PURPOSE:    Used to create a data set results for engagement user dashboard.

     Date                   Author           Description
     ----------           ---------------  ------------------------------------------------
     06/18/2014 Ravi Dhanekula    Initial code.
     09/09/2014 Ravi Dhanekula    Bug # 56183.
     11/11/2014 Ravi Dhanekula    Bug # 57896.
     12/15/2014                            Updated connected_to AND connected_from averages to show 1 if it is 0
    02/25/2015  Suresh J      Bug #59693 Added TIMEZONE_ID to p_out_result_set for time zoning..     
    03/16/2015  Ravi Dhanekula Bug # 60376 Alter Score for participant that is associated to multiple org units
    04/06/2016  nagarajs       Bug 66299 - My Dashboard Tiie Date Format Displayed Incorrectly
    07/31/2017  Ravi Dhanekula G6-2761 RPM date/time shown in GMT instead of CST.
    01/29/2018  Chidamba       G6-3787 - Modifying fix did for G6-2761 "replace hardcode value 'America/Chicago' with variable to have common timezone.
    05/07/2018  Gorantla       G6- 4054/Bug# 66799 - RPM data incorrect- the login_activity_count for users with more than one node(2 nodes either due to change of node in a month or
                                  user belonging to more than one node) is being updated in uneven ways. 
   ******************************************************************************/ 

  c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_ENGAGEMENT_USER_DETAILS';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by         CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_stage              VARCHAR2(500);
v_user_code         user_node.node_id%type;
v_user_node_list    varchar2(200);
v_field_sep         varchar2(1) := ',';

v_as_of_date DATE;
v_company_goal NUMBER;
v_score_is_active NUMBER;
v_display_target NUMBER;
v_selected_benchmarks NUMBER;
v_timezone_id country.timezone_id%TYPE;    --02/25/2015
v_cnt NUMBER; --03/16/2015 Bug # 60376
v_pattern   VARCHAR2(40);       --04/06/2016

CURSOR cur_node_for_user (v_user_id NUMBER)
IS
SELECT node_id FROM user_node
WHERE user_id = v_user_id
    AND role IN ('own','mgr');

BEGIN
--    prc_execution_log_entry(c_process_name,1,'INFO','START'||  
--    '    p_in_user_id    '||    p_in_user_id    ||
--'    p_in_mode    '||    p_in_mode    ||
--'    p_in_time_frame    '||    p_in_time_frame    ||
--'    p_in_locale    '||    p_in_locale    ||
--'    p_in_end_month    '||    p_in_end_month    ||
--'    p_in_end_year    '||    p_in_end_year     ,NULL);

    
    BEGIN       --04/06/2016
    SELECT pattern INTO v_pattern FROM locale_date_pattern WHERE locale = p_in_locale;
    EXCEPTION WHEN no_data_found THEN 
    v_pattern :='MM/DD/YYYY';
    END;
      
      BEGIN
      SELECT --CAST((FROM_TZ(CAST(as_of_date AS TIMESTAMP), dbtimezone) AT TIME ZONE 'America/Chicago') AS DATE)--01/29/2018 since DB had CST time revoking this change
          as_of_date,company_goal,is_score_active,selected_benchmarks,display_target 
      INTO v_as_of_date,v_company_goal,v_score_is_active,v_selected_benchmarks,v_display_target  FROM promo_engagement pe WHERE EXISTS--07/31/2017 
      (SELECT * FROM promotion WHERE promotion_status = 'live' AND promotion_id = pe.promotion_id  );
      END;

     SELECT c.timezone_id      --02/25/2015
       into v_timezone_id 
     FROM country c,
          user_address ua 
     WHERE ua.user_id = p_in_user_id and 
           c.country_id = ua.country_id and 
           ua.is_primary = 1;
    

    IF upper(p_in_mode) = upper('user') THEN
    
    SELECT  COUNT(1) INTO v_cnt --03/16/2015 Bug # 60376
                          FROM engagement_score_detail d 
                          WHERE d.user_id = p_in_user_id
                               AND d.time_frame = p_in_time_frame   
                               AND trans_month = p_in_end_month
                               AND trans_year = p_in_end_year;
                               
                 IF v_cnt < 2 THEN
                                
                      OPEN p_out_result_set FOR --09/09/2014
                                SELECT  v_as_of_date as_of_date,v_company_goal company_goal,v_score_is_active is_score_active,v_selected_benchmarks selected_benchmarks,v_display_target display_target,SUM(d.score) score,                                
                                  SUM(d.received_count) received_count,SUM(d.sent_count) sent_count,SUM(d.connected_to_count) connected_to_count,
                                 SUM(d.connected_from_count) connected_from_count,SUM(d.login_activity_count) login_activity_count,
                                 SUM(d.received_target) received_target,SUM(d.sent_target) sent_target,SUM(d.connected_target) connected_target,
                                 SUM(d.connected_from_target) connected_from_target,SUM(d.login_activity_target) login_activity_target
                                --,TO_CHAR (v_as_of_date, 'MON-DD-YYYY') as_of_date_str,   --02/25/2015 --04/06/2016  
                                ,TO_CHAR (v_as_of_date, v_pattern) as_of_date_str,   --04/06/2016
                                 TO_CHAR (v_as_of_date, 'HH:MI AM') as_of_time_str,   --02/25/2015
                                 NVL(v_timezone_id,'CST') TIMEZONE_ID   --02/25/2015
                          FROM engagement_score_detail d 
                          WHERE d.user_id = p_in_user_id
                               AND d.time_frame = p_in_time_frame   
                               AND trans_month = p_in_end_month
                               AND trans_year = p_in_end_year;
--                               AND ROWNUM =1;

ELSE --User is part of more than one node. We need to calculate score.
            
             OPEN p_out_result_set FOR --03/16/2015 Bug # 60376
             SELECT  v_as_of_date as_of_date,v_company_goal company_goal,v_score_is_active is_score_active,v_selected_benchmarks selected_benchmarks,v_display_target display_target,
(CASE WHEN SUM(d.received_count) >= received_target THEN received_weight
           WHEN SUM(d.received_count) < received_target THEN received_weight*SUM(d.received_count)/(received_target) 
  ELSE 0 END +
  CASE WHEN SUM(d.sent_count) >= sent_target THEN sent_weight
           WHEN SUM(d.sent_count) < sent_target THEN sent_weight*SUM(d.sent_count)/(sent_target) 
  ELSE 0 END +
   CASE WHEN connected_to >= connected_target THEN connected_weight
           WHEN connected_to < connected_target THEN connected_weight*connected_to/(connected_target) 
  ELSE 0 END +
   CASE WHEN connected_from >= connected_from_target THEN connected_from_weight
           WHEN connected_from < connected_from_target THEN connected_from_weight*connected_from/(connected_from_target) 
  ELSE 0 END +
    -- CASE WHEN d.login_activity_count >= login_activity_target THEN login_activity_weight --05/07/2018
      --     WHEN d.login_activity_count < login_activity_target THEN login_activity_weight*d.login_activity_count/(login_activity_target)  --05/07/2018 
    CASE WHEN MAX(d.login_activity_count) >= login_activity_target THEN login_activity_weight  --05/07/2018
           WHEN MAX(d.login_activity_count) < login_activity_target THEN login_activity_weight*MAX(d.login_activity_count)/(login_activity_target) --05/07/2018
  ELSE 0 END) score,                                  
                                  SUM(d.received_count) received_count,SUM(d.sent_count) sent_count,SUM(d.connected_to_count) connected_to_count,
                                 SUM(d.connected_from_count) connected_from_count,--d.login_activity_count, --05/07/2018
                                 MAX(d.login_activity_count) login_activity_count, --05/07/2018
                                 received_target,sent_target,connected_target,connected_from_target,login_activity_target                                
                                --,TO_CHAR (v_as_of_date, 'MON-DD-YYYY') as_of_date_str,   --02/25/2015   --04/06/2016
                                ,TO_CHAR (v_as_of_date, v_pattern) as_of_date_str,   --04/06/2016
                                 TO_CHAR (v_as_of_date, 'HH:MI AM') as_of_time_str,   --02/25/2015
                                 NVL(v_timezone_id,'CST') TIMEZONE_ID   --02/25/2015
                          FROM engagement_score_detail d,eng_user_connected_to_from eu
                          WHERE d.user_id = p_in_user_id
                               AND d.time_frame = p_in_time_frame   
                               AND d.trans_month = p_in_end_month
                               AND d.trans_year = p_in_end_year
                               AND d.user_id = eu.user_id
                               AND eu.time_frame = p_in_time_frame
                               AND eu.trans_month = p_in_end_month
                               AND eu.trans_year = p_in_end_year
                          GROUP BY received_weight,sent_weight,connected_weight,connected_from_weight,login_activity_weight,received_target,sent_target,connected_target,connected_from_target,login_activity_target,--login_activity_count,--05/07/2018
                          eu.connected_to,eu.connected_from;
                          
END IF;
        
        
    ELSIF upper(p_in_mode) = upper('team') THEN
        OPEN cur_node_for_user(p_in_user_id);
        LOOP
            FETCH cur_node_for_user INTO v_user_code ;
            EXIT WHEN cur_node_for_user%NOTFOUND;
            v_user_node_list := v_user_node_list || v_user_code || v_field_sep;
        END LOOP;
            v_user_node_list := substr(v_user_node_list,1,length(v_user_node_list)-length(v_field_sep)) ;
        
        OPEN p_out_result_set FOR        
        SELECT v_as_of_date as_of_date,v_company_goal company_goal,v_score_is_active is_score_active,v_selected_benchmarks selected_benchmarks,v_display_target display_target,
        score,
        received_count,
        sent_count,
        CASE WHEN connected_to_count= 0 AND sent_count !=0 THEN 1--12/15/2014
                 ELSE connected_to_count END connected_to_count,
         CASE WHEN connected_from_count= 0 AND received_count !=0 THEN 1
                 ELSE connected_from_count END connected_from_count,
                 login_activity_count,
                 received_target,
                 sent_target,
                 connected_target,
                 connected_from_target,
                 login_activity_target 
                --,TO_CHAR (v_as_of_date, 'MON-DD-YYYY') as_of_date_str,   --02/25/2015   --04/06/2016
                ,TO_CHAR (v_as_of_date, v_pattern) as_of_date_str,   --04/06/2016
                 TO_CHAR (v_as_of_date, 'HH:MI AM') as_of_time_str,   --02/25/2015
                 NVL(v_timezone_id,'CST') TIMEZONE_ID   --02/25/2015
                 FROM (SELECT 
        SUM (d.score * d.total_participant_count) / SUM(d.total_participant_count) score ,sum(d.received_count) received_count,sum(d.sent_count) sent_count,
--        sum(d.connected_to_count) connected_to_count,
        ROUND(SUM(connected_avg * total_participant_count)/ SUM(total_participant_count)) connected_to_count, --11/11/2014
--        sum(d.connected_from_count) connected_from_count,
        ROUND(SUM(connected_from_avg * total_participant_count)/ SUM(total_participant_count)) connected_from_count,--11/11/2014
                                 sum(d.login_activity_count) login_activity_count,
                                 sum(d.received_target) received_target,sum(d.sent_target) sent_target,
--        sum(d.connected_target) connected_target,
        ROUND(SUM(connected_avg_target * total_participant_count)/ SUM(total_participant_count)) connected_target,--11/11/2014
--        sum(d.connected_from_target) connected_from_target,
        ROUND(SUM(connected_from_avg_target * total_participant_count)/ SUM(total_participant_count)) connected_from_target,--11/11/2014
                        sum(d.login_activity_target) login_activity_target 
                          FROM engagement_score_summary d 
                          WHERE d.time_frame = p_in_time_frame   AND   d.record_type ='nodesum' AND d.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))
                                AND trans_month = p_in_end_month
                               AND trans_year = p_in_end_year);                                
        
    END IF;
    prc_execution_log_entry(c_process_name,1,'INFO','START',null);
    
EXCEPTION
WHEN OTHERS THEN
    prc_execution_log_entry(c_process_name,1,'INFO',SQLERRM,null);  

END prc_engagement_user_details;


PROCEDURE prc_engagement_manager_score(
p_in_user_id        IN    NUMBER,
p_in_mode           IN   VARCHAR2,
p_in_time_frame    IN VARCHAR2,
p_in_node_id        IN    VARCHAR2,
p_in_locale         IN    VARCHAR2,
p_in_end_month     IN    NUMBER,
p_in_end_year       IN    NUMBER,
p_in_rownum_start   IN    NUMBER,
p_in_rownum_end     IN    NUMBER,
p_in_sort_mem_col  IN   VARCHAR2,
p_in_sort_mem_order IN VARCHAR2,
p_in_sort_team_col IN VARCHAR2,
p_in_sort_team_order IN VARCHAR2,
p_in_start_date     IN    VARCHAR2,
p_in_end_date       IN    VARCHAR2,
p_out_result_set1   OUT sys_refcursor, 
p_out_result_set2   OUT sys_refcursor,
p_out_result_set3   OUT sys_refcursor,
p_out_result_set4   OUT sys_refcursor,
p_out_result_set5   OUT sys_refcursor,
p_out_result_set6   OUT sys_refcursor,
p_out_result_set7   OUT sys_refcursor,
p_out_result_set8   OUT sys_refcursor,
p_out_result_set9   OUT sys_refcursor,
p_out_user_name   OUT VARCHAR2,
p_out_return_code OUT NUMBER
)
IS
/******************************************************************************
     PURPOSE:    Used to create a data set results for engagement manager dashboard.
      
     p_out_result_set1: Result set for Nodesum data.
     p_out_result_set2 : Result set for teammembers.
     p_out_result_set3 : Result set for child nodes.
     
     p_out_result_set9 : Node information for the dropdown on the screen.

     Date         Author           Description
     ----------           ---------------  ------------------------------------------------
     06/18/2014 Ravi Dhanekula  Initial code.
     08/21/2014 Ravi Dhanekula  Bug # 55916
     08/25/2014 Ravi Dhanekula  Bug # 55839
     09/02/2014 Ravi Dhanekula  Bug # 56186
     09/03/2014 Ravi Dhanekula  Bug # 56246,56294
     09/29/2014 Ravi Dhanekula  Bug # 56999 Added avatar_small to the result set2.
     10/17/2014 Swati           Bug 57351 - RPM-Barbara Reagan My dashboard --> I recognized show extra one count than that of Activity History
     11/11/2014 Ravi Dhanekula  Bug # 57896 RPM Unique People Issue..New CR
     11/12/2014 Suresh J        Bug# 57824 - All Team Dashboard participant tables Sort Order
     11/12/2014 Ravi Dhanekula  Bug # 57776 Remove duplicates in teammeber list.
     11/26/2014 Ravi Dhanekula  Bug 57843 - No Behavior Badge On RPM Secondary Page
     12/08/2014 Ravi Dhanekula  Bug # 57847 Data is not coming up for count for paxRecBy and paxRecTo because of that we 
                                are unable to populate the link to go to tree chart view..Added 2 new variables for team dashboard.
     12/12/2014 Ravi Dhanekula  Bug # 57896.if the calculated average is equal to 0, display 1
     02/25/2015 Suresh J        Bug #59693 Added TIMEZONE_ID to p_out_result_set1 and p_out_result_set8    
     03/16/2015 Ravi Dhanekula  Bug # 60376 Alter Score for participant that is associated to multiple org units 
     05/21/2015 Swati           Bug 62397 - RPM Team Dashboard shows incorrect Pax count and inactive Pax as team memeber
     08/11/2015 Ravi Dhanekula Bug # 63626 - Added node_id to ref cursor 2.
     08/24/2015 Ravi Dhanekula Bug # 63563 - No Behavior count is not working in Team Dashboard page for Recognitions Received dashboard chart 
     08/25/2015 Ravi Dhanekula Bug # 63676 - Login as owner who is an owner of mutiple org units.Check the details it keep on spinning
     09/01/2015 Ravi Dhanekula Bug # 63719 - Translation: Space appears in view dashboard promotion. Should be left aligned.Also, No behaviour is not getting translated.
     09/01/2015 Ravi Dhanekula Bug # 63612 - Team dashboard page 'People Team Recognized' & 'People Recognizing Team' section are not displaying the values in the columns that are from below org unit
     09/04/2015 Mythilim       Bug # 63923 - If CM data does not exist for incoming locale then default to en_US. replaced p_in_locale with v_locale
     10/21/2015 Ravi Dhanekula Bug # 64360 - pkg_engagement_extract incorrect data with node change
     12/28/2016 Sherif Basha   Bug 70638 - Owner belonging to multiple org unit having issue to fetch records in People Recognizing Me page under Team Dashboard
                                           --replaced connected_to of eng_user_connected_to_from as it is not node based value  
     05/02/2017 Ravi Dhanekula Fixed issue reported on Bravo site...Recognitions sent ccounts showing 0.
     07/31/2017 Ravi Dhanekula G6-2761
     11/28/2017 Chidamba       Commenting as this doesn't give more info to developers and also execution log table getting more entries every day.
     01/29/2018 Chidamba       G6-3787 - Modifying fix did for G6-2761 "replace hardcode value 'CST' and 'America/Chicago' with variable to have common timezone.
     02/22/2018 Chidamba       G6-3907 - Java requested to handle dateformat since they were not able to handle in front end.
     05/04/2018 Gorantla       G6-4066/Bug 72318 - RPM Scores do not calculate correctly
     05/07/2018  Gorantla      G6- 4054/Bug# 66799 - RPM data incorrect- the login_activity_count for users with more than one node(2 nodes either due to change of node in a month or
                                  user belonging to more than one node) is being updated in uneven ways.
     04/01/2019  Suresh J          SA Integeration with DayMaker. Excludes/Includes PURL/Celebration promotions based on boolean value of new.service.anniversary.enabled                                   
   ******************************************************************************/ 
  c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_ENGAGEMENT_MANAGER_SCORE';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by         CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_stage              VARCHAR2(500);
  
e_no_data     EXCEPTION;
v_user_node_list    VARCHAR2(4000); --09/02/2014
v_child_node_list    VARCHAR2(4000); --09/02/2014
v_child_node_count NUMBER;
v_team_mem_count NUMBER;

v_sort_teammber            VARCHAR2(200);
v_sort_childnodes            VARCHAR2(200);
l_query_teammember VARCHAR2(32767);
l_query_childnodes VARCHAR2(32767);

p_start_date DATE;
p_end_date DATE;
v_as_of_date DATE;
v_as_of_date_orig DATE;--07/31/2017
v_company_goal NUMBER;
v_is_score_active NUMBER;
v_display_target NUMBER;
v_selected_benchmarks NUMBER;

v_connected_to_pax_count NUMBER;
v_connected_from_pax_count NUMBER;
v_rec_sent_company_avg NUMBER;
v_rec_recv_company_avg NUMBER;
v_conn_to_company_avg NUMBER;
v_conn_from_company_avg NUMBER;
v_login_company_avg NUMBER;
v_score_company_avg NUMBER;
v_timezone_id country.timezone_id%TYPE;    --02/25/2015
v_cnt NUMBER ;--03/16/2015 Bug # 60376
v_cms_no_behavior VARCHAR2(100);--Bug # 63719
v_locale  cms_content.locale%type; -- 09/04/2015        
v_locale_date_format   LOCALE_DATE_PATTERN.PATTERN%TYPE;  --02/22/2018
v_sa_enabled         os_propertyset.boolean_val%type;       --04/01/2019
BEGIN

   /* prc_execution_log_entry(c_process_name,1,'INFO','START'||
    '    p_in_user_id    '||    p_in_user_id    ||
'    p_in_mode    '||    p_in_mode    ||
'    p_in_time_frame    '||    p_in_time_frame    ||
'    p_in_node_id    '||    p_in_node_id    ||
'    p_in_locale    '||    p_in_locale    ||
'    p_in_end_month    '||    p_in_end_month    ||
'    p_in_end_year    '||    p_in_end_year    ||
'    p_in_rownum_start    '||    p_in_rownum_start    ||
'    p_in_rownum_end    '||    p_in_rownum_end    ||
'    p_in_sort_mem_col    '||    p_in_sort_mem_col    ||
'    p_in_sort_mem_order    '||    p_in_sort_mem_order    ||
'    p_in_sort_team_col    '||    p_in_sort_team_col    ||
'    p_in_sort_team_order    '||    p_in_sort_team_order    ||
'    p_in_start_date    '||    p_in_start_date    ||
'    p_in_end_date    '||    p_in_end_date,null);*/

    BEGIN
      SELECT boolean_val                            --04/01/2019
        INTO v_sa_enabled
        FROM os_propertyset
       WHERE entity_name = 'new.service.anniversary.enabled';
     EXCEPTION
         WHEN OTHERS THEN
         v_sa_enabled := 0;
     END;

      p_start_date := TO_DATE(P_IN_START_DATE,'MM/DD/YYYY');
      p_end_date := TO_DATE(P_IN_END_DATE,'MM/DD/YYYY');
  
  BEGIN    
  SELECT cms_value INTO v_cms_no_behavior --Bug # 63719
    FROM vw_cms_asset_value
   WHERE asset_code = 'engagement.participant' 
    AND key = 'NO_BEHAVIOR'
    AND locale = p_in_locale;
    
    v_locale := p_in_locale;
    
  EXCEPTION
  WHEN NO_DATA_FOUND THEN 
   SELECT cms_value INTO v_cms_no_behavior --Bug # 63719
     FROM vw_cms_asset_value
    WHERE asset_code = 'engagement.participant' 
    AND key = 'NO_BEHAVIOR'
    AND locale = 'en_US';
    
   v_locale := 'en_US'; 
   END;
    
   BEGIN
    SELECT PATTERN 
      INTO v_locale_date_format 
      FROM locale_date_pattern 
     WHERE locale = p_in_locale;
   EXCEPTION
   WHEN NO_DATA_FOUND THEN 
     SELECT PATTERN 
       INTO v_locale_date_format 
       FROM LOCALE_DATE_PATTERN 
      WHERE locale = 'en_US';     
   END;
      
      DELETE temp_table_session;

INSERT INTO temp_table_session
SELECT cav.asset_code,
          MAX (DECODE (cav.key, 'NAME', cav.cms_value, NULL)) AS cms_name,
         MAX (DECODE (cav.key, 'EARNED_IMAGE_SMALL', cav.cms_value, NULL)) AS cms_code            
       FROM vw_cms_asset_value cav --11/12/2014
       WHERE asset_code='promotion.badge'
       AND locale = v_locale --p_in_locale -- 09/04/2015
   GROUP BY cav.asset_code,
            cav.locale,
            cav.asset_id,
            cav.content_key_id,
            cav.content_id;
            
 SELECT cms_value INTO v_cms_no_behavior --Bug # 63719
  FROM vw_cms_asset_value
 WHERE asset_code = 'engagement.participant' 
    AND key = 'NO_BEHAVIOR'
    AND locale = v_locale; --p_in_locale -- 09/04/2015

     SELECT c.timezone_id      --02/25/2015
       into v_timezone_id 
     FROM country c,
          user_address ua 
     WHERE ua.user_id = p_in_user_id and 
           c.country_id = ua.country_id and 
           ua.is_primary = 1;
      
      BEGIN
      SELECT --CAST((FROM_TZ(CAST(as_of_date AS TIMESTAMP), dbtimezone) AT TIME ZONE 'America/Chicago') AS DATE) --01/29/2018 since DB had CST time revoking this change
                as_of_date,as_of_date,company_goal,is_score_active,selected_benchmarks,display_target INTO v_as_of_date,v_as_of_date_orig,v_company_goal,v_is_score_active,v_selected_benchmarks,v_display_target FROM promo_engagement pe WHERE EXISTS--07/31/2017 
      (SELECT * FROM promotion WHERE promotion_status = 'live' AND promotion_id = pe.promotion_id  );
      EXCEPTION
      WHEN OTHERS THEN
       prc_execution_log_entry(c_process_name,1,'INFO',SQLERRM,null);  --sql error fix
      END;
      
      BEGIN     
      SELECT received_count/total_participant_count,
                sent_count/total_participant_count,
--                connected_from_count/total_participant_count,
                connected_from_avg, --11/11/2014
--                connected_to_count/total_participant_count,
                connected_avg, --11/11/2014
                login_activity_count/total_participant_count,score  INTO v_rec_recv_company_avg,v_rec_sent_company_avg,v_conn_from_company_avg,v_conn_to_company_avg,v_login_company_avg,v_score_company_avg  from engagement_score_summary d
      WHERE parent_node_id IS NULL
           AND d.time_frame = p_in_time_frame AND d.record_type ='nodesum'
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;
      EXCEPTION
      WHEN OTHERS THEN 
      p_out_return_code := '1';
       prc_execution_log_entry(c_process_name,1,'ERROR','LINE 825'||SQLERRM,null);  --sql error fix 
       RAISE e_no_data;
      END;
  
      v_sort_teammber := p_in_sort_mem_col || ' ' || p_in_sort_mem_order;    
    v_sort_childnodes := p_in_sort_team_col || ' ' || p_in_sort_team_order;
    l_query_teammember := 'SELECT * FROM ( ';
    l_query_childnodes := 'SELECT * FROM ( ';
     
    IF upper(p_in_mode) = upper('team') THEN
  
    IF p_in_node_id IS NULL THEN
    
       prc_execution_log_entry(c_process_name,1,'INFO','line 869',null);  --sql error fix   
           --get first child node list        
        SELECT LISTAGG(node_id,',') WITHIN GROUP(ORDER BY node_id),COUNT(node_id) --08/21/2014 Bug # 55916
            INTO v_child_node_list,v_child_node_count
        FROM
        (SELECT n.node_id FROM node n,user_node un WHERE
        un.user_id = p_in_user_id
         AND un.role IN ('own','mgr')
         AND parent_node_id = un.node_id ); 
         
           

        SELECT LISTAGG(node_id,',') WITHIN GROUP(ORDER BY node_id)
            INTO v_user_node_list
        FROM user_node
        WHERE user_id = p_in_user_id
            AND role IN ('own','mgr');  
                       
                
            SELECT COUNT(1) --08/21/2014 Bug # 55916
            INTO v_team_mem_count
        FROM engagement_elig_user --09/03/2014 
        WHERE node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)));      
        
        SELECT SUM(d.sent_count) INTO v_connected_from_pax_count
    FROM engagement_recog_received d
   WHERE d.node_id IN (SELECT * FROM TABLE(get_array_varchar( v_user_node_list  ) )) 
    AND    d.time_frame = p_in_time_frame
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;
                   
      SELECT SUM(d.received_count) INTO v_connected_to_pax_count
    FROM engagement_recog_sent d
   WHERE d.node_id IN (SELECT * FROM TABLE(get_array_varchar( v_user_node_list  ) )) 
    AND    d.time_frame = p_in_time_frame
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;
          
 OPEN p_out_result_set1 FOR
        SELECT  v_as_of_date as_of_date,v_company_goal company_goal,v_is_score_active is_score_active,v_selected_benchmarks selected_benchmarks,v_child_node_count child_node_count,v_team_mem_count team_mem_count,d.score ,d.received_count,d.sent_count,
        v_connected_to_pax_count connected_to_pax_count,--12/08/2014
        v_connected_from_pax_count connected_from_pax_count, --12/08/2014
        CASE WHEN connected_to_count= 0 AND sent_count !=0 THEN 1--12/15/2014  
                 ELSE connected_to_count END connected_to_count,
         CASE WHEN connected_from_count= 0 AND received_count !=0 THEN 1       
                 ELSE connected_from_count END connected_from_count,
                                 d.login_activity_count,
                                 d.received_target,d.sent_target,d.connected_target,
                                 d.connected_from_target,d.login_activity_target,
                                 d.score_achieved_count,d.sent_achieved_count,
                                 d.recv_achieved_count,d.conn_to_achieved_count,
                                 d.conn_from_achieved_count,d.login_achieved_count,
                                 d.total_participant_count
                                ,TO_CHAR (v_as_of_date,v_locale_date_format) as_of_date_str,   --02/25/2015   --02/22/2018 --TO_CHAR (v_as_of_date,'MON-DD-YYYY') as_of_date_str
                                 TO_CHAR (v_as_of_date, 'HH:MI PM') as_of_time_str,   --02/25/2015 
                                 NVL(v_timezone_id,'CST') TIMEZONE_ID   --02/25/2015
                                  FROM (
        SELECT  SUM (d.score * d.total_participant_count) / SUM(d.total_participant_count) score ,sum(d.received_count) received_count,sum(d.sent_count) sent_count,
                                 sum(d.connected_to_count) connected_to_pax_count,--12/08/2014
                                 ROUND(SUM(connected_avg * total_participant_count)/ SUM(total_participant_count)) connected_to_count, --11/11/2014
                                 sum(d.connected_from_count) connected_from_pax_count,--12/08/2014
                                 ROUND(SUM(connected_from_avg * total_participant_count)/ SUM(total_participant_count)) connected_from_count,--11/11/2014
                                 sum(d.login_activity_count) login_activity_count,
                                 sum(d.received_target) received_target,sum(d.sent_target) sent_target,                                 
--                                 sum(d.connected_target) connected_target,
                                 ROUND(SUM(connected_avg_target * total_participant_count)/ SUM(total_participant_count)) connected_target,--11/11/2014
--                                 sum(d.connected_from_target) connected_from_target,
                                 ROUND(SUM(connected_from_avg_target * total_participant_count)/ SUM(total_participant_count)) connected_from_target,--11/11/2014                                 
                                 sum(d.login_activity_target) login_activity_target,
                                 sum(d.score_achieved_count) score_achieved_count,
                                 sum(d.sent_achieved_count) sent_achieved_count,
                                 sum(d.recv_achieved_count) recv_achieved_count,
                                 sum(d.conn_to_achieved_count) conn_to_achieved_count,
                                 sum(d.conn_from_achieved_count) conn_from_achieved_count,
                                 sum(d.login_achieved_count) login_achieved_count,
                                 sum(d.total_participant_count) total_participant_count
                          FROM engagement_score_summary d
                          WHERE d.time_frame = p_in_time_frame AND d.record_type ='nodesum' AND d.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))
                                AND trans_month = p_in_end_month AND trans_year = p_in_end_year
                                 )d;
                               
           OPEN p_out_result_set6 FOR
        SELECT node_id,name node_name FROM node n
                          WHERE n.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)));
            
        l_query_teammember := l_query_teammember ||'  '||'
        SELECT ROWNUM RN,company_goal,node_id,NULL node_name,user_id,first_name||CASE WHEN node_count >1 THEN   '' ( '' || node_name || '' ) ''   ELSE NULL END first_name,last_name,score,--Bug # 60377
        received_target,sent_target,connected_target,connected_from_target,login_activity_target,
        received_count,sent_count,connected_to_count,connected_from_count,login_activity_count,avatar_url FROM (
        SELECT DISTINCT '''||v_company_goal||''''||' company_goal'||',un.node_id,name node_name,a.user_id,a.first_name,a.last_name,ecd.score, --11/21/2014 Bug # 57776
        received_target,sent_target,connected_target,connected_from_target,login_activity_target,
        received_count,sent_count,connected_to_count,connected_from_count,login_activity_count,avatar_small avatar_url,
        (SELECT COUNT(1) FROM user_node WHERE node_id IN (SELECT * FROM TABLE(get_array ('''||v_user_node_list||'''))) AND user_id = ecd.user_id) node_count
        FROM user_node un,application_user a,engagement_score_detail ecd,node n,participant p
        WHERE un.node_id IN (SELECT * FROM TABLE(get_array ('''||v_user_node_list||''')))
            AND un.user_id = a.user_id
            AND a.user_id = ecd.user_id
            AND un.node_id = ecd.node_id
            AND un.node_id = n.node_id
            AND un.user_id = p.user_id
            AND a.is_active = 1 --05/21/2015 Bug 62397
            AND ecd.time_frame = '''||p_in_time_frame||'''
            AND trans_month = '''||p_in_end_month||'''
            AND trans_year = '''||p_in_end_year||'''
            ORDER BY '|| v_sort_teammber ||' ) RS ) WHERE  RN > ' ||p_in_rownum_start||' AND RN   < '|| p_in_rownum_end;
               
        OPEN p_out_result_set2 FOR
        l_query_teammember;
        
        l_query_childnodes := l_query_childnodes ||'  '||'
        SELECT ROWNUM RN,RS.* FROM ( 
        SELECT '''||v_company_goal||''''||' company_goal'||',s.node_id,rh.name node_name,rh.parent_node_id,score,
        received_target,sent_target,connected_target,connected_from_target,login_activity_target,--Bug # 63612
        received_count,sent_count,connected_to_count,connected_from_count,login_activity_count,--Bug # 63612
--         (SELECT LISTAGG (au.first_name||'' ''||au.last_name, '', '') WITHIN GROUP (ORDER BY 1) AS name FROM user_node un,application_user au WHERE node_id=rh.node_id and role =''mgr''   --11/12/2014
           (SELECT LISTAGG (au.last_name||'', ''||au.first_name, ''  '') WITHIN GROUP (ORDER BY 1) AS name FROM user_node un,application_user au WHERE node_id=rh.node_id and role =''mgr''  --11/12/2014
              AND un.user_id = au.user_id) AS manager_name     
        FROM engagement_score_summary s,node rh
        WHERE s.record_type =''nodesum'' 
            AND time_frame = '''||p_in_time_frame||'''
            AND s.node_id IN (SELECT * FROM TABLE(get_array('''||v_child_node_list||''')))
            AND s.node_id = rh.node_id
            AND trans_month = '''||p_in_end_month||'''
            AND trans_year = '''||p_in_end_year||'''            
            ORDER BY '||v_sort_childnodes||' )  RS ) WHERE RN > ' ||p_in_rownum_start||' AND RN   < '|| p_in_rownum_end;
            
            OPEN p_out_result_set3 FOR
        l_query_childnodes;
        
            OPEN p_out_result_set4 FOR
        SELECT v_company_goal company_goal,
        SUM (d.score * d.total_participant_count) / SUM(d.total_participant_count) score ,sum(d.received_count) received_count,sum(d.sent_count) sent_count,
--        sum(d.connected_to_count) connected_to_count,
--ROUND(SUM(d.connected_avg * d.total_participant_count)/ SUM(d.total_participant_count)) 
NULL connected_to_count, --11/11/2014
--                                 sum(d.connected_from_count) connected_from_count,
--ROUND(SUM(d.connected_from_avg * d.total_participant_count)/ SUM(d.total_participant_count)) 
NULL connected_from_count,--11/11/2014                                 
                                 sum(d.login_activity_count) login_activity_count,
                                 sum(d.received_target) received_target,sum(d.sent_target) sent_target,                                 
--                                 sum(d.connected_target) connected_target,
--ROUND(SUM(connected_avg_target * total_participant_count)/ SUM(total_participant_count)) 
NULL connected_target,--11/11/2014
--                                 sum(d.connected_from_target) connected_from_target,
--ROUND(SUM(connected_from_avg_target * total_participant_count)/ SUM(total_participant_count)) 
NULL connected_from_target,--11/11/2014                                 
                                 sum(d.login_activity_target) login_activity_target
                          FROM engagement_score_summary d, node n 
                          WHERE d.time_frame = p_in_time_frame AND d.record_type ='teamsum' AND d.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))
                                AND trans_month = p_in_end_month
                                AND trans_year = p_in_end_year 
                                AND d.node_id = n.node_id;
      
    OPEN p_out_result_set5 FOR    
    SELECT NVL(cms.cms_name,d.behavior) behavior,sent_count,received_count,cms_badge.cms_code behavior_badge FROM (    
    SELECT behavior,SUM (sent_count) sent_count,
         SUM (received_count) received_count FROM (
      SELECT behavior,
         sent_count sent_count,
         received_count received_count
    FROM engagement_behavior_summary d
   WHERE d.header_node_id IN (SELECT *
                                FROM TABLE (get_array (v_user_node_list)))
       AND d.time_frame = p_in_time_frame 
       AND trans_month = p_in_end_month
       AND trans_year = p_in_end_year
UNION ALL
SELECT behavior_type behavior, 0 sent_count, 0 received_count
  FROM promo_behavior
 WHERE promotion_id IN (SELECT eligible_promotion_id from (SELECT * FROM (SELECT * FROM (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) WHERE promotion_id IN (SELECT promotion_id FROM promotion where promotion_status = 'live'))) --04/01/2019
GROUP BY behavior               
UNION ALL --11/26/2014
SELECT v_cms_no_behavior,(a.total_sent_count-NVL(b.sent_count,0)) sent_count,(a.total_received_count-NVL(b.received_count,0)) received_count FROM (   --Bug # 63563 --Bug # 63719     
       SELECT SUM (sent_count) total_sent_count,
         SUM (received_count) total_received_count from engagement_score_summary d WHERE time_frame = p_in_time_frame 
       AND d.record_type ='nodesum' AND d.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))         
       AND trans_month = p_in_end_month
       AND trans_year = p_in_end_year) a,      
      (SELECT SUM(sent_count) sent_count,
         SUM(received_count) received_count
    FROM engagement_behavior_summary d
   WHERE time_frame = p_in_time_frame 
       AND d.header_node_id IN (SELECT * FROM TABLE (get_array (v_user_node_list)))
       AND trans_month = p_in_end_month
       AND trans_year = p_in_end_year) b                   
                          ) d,
(SELECT cms_name,cms_code
     FROM vw_cms_code_value E
    WHERE asset_code in ( 'picklist.promo.recognition.behavior.items') and e.locale=v_locale) cms,  --p_in_locale -- 09/04/2015
    temp_table_session cms_badge,
    (select * from (SELECT RANK ()
                                                                     OVER (
                                                                        PARTITION BY pe.behavior_name
                                                                        ORDER BY
                                                                           rownum)
                                                                        AS rec_rank,
                                                                     pe.*
                                                                FROM (SELECT  br.behavior_name,br.cm_asset_key from badge_promotion bp, badge_rule br WHERE eligible_promotion_id in (select eligible_promotion_id from (SELECT * FROM (SELECT * FROM (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) pep,promotion p WHERE pep.promotion_id = p.promotion_id    --04/01/2019
AND promotion_status = 'live') AND bp.promotion_id = br.promotion_id
AND br.behavior_name is NOT NULL) pe ) WHERE rec_rank =1) br
WHERE cms.cms_code(+) = d.behavior --11/26/2014
AND d.behavior = br.behavior_name(+)
AND cms_badge.cms_name(+) = br.cm_asset_key;

     OPEN p_out_result_set7 FOR--For team avg and Company average
    SELECT SUM(received_count)/SUM(total_participant_count) AS v_rec_recv_team_avg,
                SUM(sent_count)/SUM(total_participant_count) AS v_rec_sent_team_avg,
--                SUM(connected_from_count)/SUM(total_participant_count) AS v_conn_from_team_avg,
                ROUND(SUM(connected_from_avg * total_participant_count)/ SUM(total_participant_count)) AS v_conn_from_team_avg,--11/11/2014
--                SUM(connected_to_count)/SUM(total_participant_count) AS v_conn_to_team_avg,
               ROUND(SUM(connected_avg * total_participant_count)/ SUM(total_participant_count)) AS v_conn_to_team_avg, --11/11/2014                
                SUM(login_activity_count)/SUM(total_participant_count) AS v_login_team_avg,
                v_rec_sent_company_avg AS v_rec_sent_company_avg,v_rec_recv_company_avg AS v_rec_recv_company_avg,
                v_conn_to_company_avg AS v_conn_to_company_avg,
                v_conn_from_company_avg AS v_conn_from_company_avg,                
                v_login_company_avg AS v_login_company_avg,v_score_company_avg AS v_score_company_avg FROM engagement_score_summary d
    WHERE d.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))        
                   AND d.time_frame = p_in_time_frame AND d.record_type ='teamsum'
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;    
     OPEN p_out_result_set8 FOR
      SELECT NULL FROM DUAL;
      
         
    OPEN p_out_result_set9 FOR
        SELECT node_id,name node_name FROM node n
                          WHERE n.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)));
    ELSE
      
    --team for individual node_id
        
--    --get first child node list
         
           SELECT LISTAGG(node_id,',') WITHIN GROUP(ORDER BY node_id),COUNT(node_id)
            INTO v_child_node_list,v_child_node_count
        FROM
        (SELECT n.node_id FROM node n WHERE
        n.parent_node_id IN (SELECT * FROM TABLE(get_array(p_in_node_id))));
        
        SELECT COUNT(1) --08/21/2014 Bug # 55916
            INTO v_team_mem_count
        FROM engagement_elig_user --09/03/2014 
        WHERE node_id IN (SELECT * FROM TABLE(get_array(p_in_node_id)));
            
        SELECT SUM(d.sent_count) INTO v_connected_from_pax_count
    FROM engagement_recog_received d
   WHERE d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )) 
    AND    d.time_frame = p_in_time_frame
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;
                   
      SELECT SUM(d.received_count) INTO v_connected_to_pax_count
    FROM engagement_recog_sent d
   WHERE d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )) 
    AND    d.time_frame = p_in_time_frame
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;
         
        OPEN p_out_result_set1 FOR
        SELECT  v_as_of_date as_of_date,v_company_goal company_goal,v_is_score_active is_score_active,v_selected_benchmarks selected_benchmarks,v_child_node_count child_node_count,v_team_mem_count team_mem_count,d.score ,d.received_count,d.sent_count,
        v_connected_to_pax_count connected_to_pax_count,--12/08/2014
        v_connected_from_pax_count connected_from_pax_count,--12/08/2014
        CASE WHEN d.connected_to_count= 0 AND sent_count !=0 THEN 1 --12/12/2014  --12/28/2016 Bug 70638 only calculated average should be made 1
                 ELSE d.connected_to_count END connected_to_count,
         CASE WHEN d.connected_from_count= 0 AND received_count !=0 THEN 1  -- 12/28/2016  Bug 70638 only calculated average should be made 1
                 ELSE d.connected_from_count END connected_from_count,d.login_activity_count,
                                 d.received_target,d.sent_target,d.connected_target,
                                 d.connected_from_target,d.login_activity_target,
                                 d.score_achieved_count,d.sent_achieved_count,
                                 d.recv_achieved_count,d.conn_to_achieved_count,
                                 d.conn_from_achieved_count,d.login_achieved_count,
                                 d.total_participant_count                                 
                                ,TO_CHAR (v_as_of_date,v_locale_date_format) as_of_date_str,   --02/25/2015   --02/22/2018 --TO_CHAR (v_as_of_date,'MON-DD-YYYY') as_of_date_str   
                                 TO_CHAR (v_as_of_date, 'HH:MI PM') as_of_time_str,   --02/25/2015 
                                 NVL(v_timezone_id,'CST') TIMEZONE_ID   --02/25/2015
                                 FROM (                                 
                                 SELECT SUM (d.score * d.total_participant_count) / SUM(d.total_participant_count) score ,sum(d.received_count) received_count,sum(d.sent_count) sent_count,                                 
SUM(d.connected_to_count) connected_to_pax_count,--12/08/2014
                                 ROUND(SUM(connected_avg * total_participant_count)/ SUM(total_participant_count)) connected_to_count, --11/11/2014
                                 sum(d.connected_from_count) connected_from_pax_count,--12/08/2014
ROUND(SUM(connected_from_avg * total_participant_count)/ SUM(total_participant_count)) connected_from_count,--11/11/2014                                 
                                 sum(d.login_activity_count) login_activity_count,
                                 sum(d.received_target) received_target,sum(d.sent_target) sent_target,                                 
--                                 sum(d.connected_target) connected_target,
ROUND(SUM(connected_avg_target * total_participant_count)/ SUM(total_participant_count)) connected_target,--11/11/2014
--                                 sum(d.connected_from_target) connected_from_target,
ROUND(SUM(connected_from_avg_target * total_participant_count)/ SUM(total_participant_count)) connected_from_target,--11/11/2014                                 
                                 sum(d.login_activity_target) login_activity_target,
                                 sum(d.score_achieved_count) score_achieved_count,
                                 sum(d.sent_achieved_count) sent_achieved_count,
                                 sum(d.recv_achieved_count) recv_achieved_count,
                                 sum(d.conn_to_achieved_count) conn_to_achieved_count,
                                 sum(d.conn_from_achieved_count) conn_from_achieved_count,
                                 sum(d.login_achieved_count) login_achieved_count,
                                 sum(d.total_participant_count) total_participant_count
                          FROM engagement_score_summary d 
                          WHERE time_frame = p_in_time_frame AND d.record_type ='nodesum' AND d.node_id IN (SELECT * FROM TABLE(get_array(p_in_node_id)))--= p_in_node_id
                                AND trans_month = p_in_end_month
                                AND trans_year = p_in_end_year) d;
         
        l_query_teammember := l_query_teammember ||'  '||'
        SELECT ROWNUM RN,RS.* FROM (            
        SELECT DISTINCT '''||v_company_goal||''''||' company_goal'||',un.node_id,NULL node_name,a.user_id,a.first_name,a.last_name,ecd.score,  --11/21/2014 Bug # 57776
        received_target,sent_target,connected_target,connected_from_target,login_activity_target,
        received_count,sent_count,connected_to_count,connected_from_count,login_activity_count,p.avatar_small avatar_url      
        FROM user_node un,application_user a,engagement_score_detail ecd, node n,participant p
        WHERE un.node_id IN (SELECT * FROM TABLE(get_array('''||p_in_node_id||''')))
            AND un.user_id = a.user_id
            AND a.user_id = ecd.user_id
            AND un.node_id = ecd.node_id
            AND un.node_id = n.node_id
            AND un.user_id = p.user_id
            AND a.is_active = 1 --05/21/2015 Bug 62397
            AND time_frame = '''||p_in_time_frame||'''
            AND trans_month = '''||p_in_end_month||'''
            AND trans_year = '''||p_in_end_year||'''        
            ORDER BY '|| v_sort_teammber ||' ) RS ) WHERE  RN > ' ||p_in_rownum_start||' AND RN   < '|| p_in_rownum_end;
       
        OPEN p_out_result_set2 FOR
        l_query_teammember;
        
      
        l_query_childnodes := l_query_childnodes ||'  '||'
        SELECT ROWNUM RN,RS.* FROM (         
        SELECT '''||v_company_goal||''''||' company_goal'||', s.node_id, rh.name node_name, rh.parent_node_id parent_node_id, s.score,
        received_target,sent_target,connected_target,connected_from_target,login_activity_target,--Bug # 63612
        received_count,sent_count,connected_to_count,connected_from_count,login_activity_count,--Bug # 63612
--        (SELECT LISTAGG (au.first_name||'' ''||au.last_name, '', '') WITHIN GROUP (ORDER BY 1) AS name FROM user_node un,application_user au WHERE node_id=rh.node_id and role =''mgr''  --11/12/2014
          (SELECT LISTAGG (au.last_name||'', ''||au.first_name, ''  '') WITHIN GROUP (ORDER BY 1) AS name FROM user_node un,application_user au WHERE node_id=rh.node_id and role =''mgr''  --11/12/2014
              AND un.user_id = au.user_id) AS manager_name         
        FROM engagement_score_summary s,node rh
        WHERE s.record_type =''nodesum'' 
            AND s.node_id IN (SELECT * FROM TABLE(get_array('''||v_child_node_list||''')))
            AND s.node_id = rh.node_id
            AND time_frame = '''||p_in_time_frame||'''
            AND trans_month = '''||p_in_end_month||'''
            AND trans_year = '''||p_in_end_year||'''            
            ORDER BY  '||v_sort_childnodes||' )  RS ) WHERE RN > ' ||p_in_rownum_start||' AND RN   < '|| p_in_rownum_end;
       
         OPEN p_out_result_set3 FOR   
         l_query_childnodes;
        
        OPEN p_out_result_set4 FOR
        SELECT v_company_goal company_goal, SUM (d.score * d.total_participant_count) / SUM(d.total_participant_count) score ,sum(d.received_count) received_count,sum(d.sent_count) sent_count,        
        --                                 sum(d.connected_to_count) connected_to_count,
--ROUND(SUM(connected_avg * total_participant_count)/ SUM(total_participant_count)) 
NULL connected_to_count, --11/11/2014
--                                 sum(d.connected_from_count) connected_from_count,
--ROUND(SUM(connected_from_avg * total_participant_count)/ SUM(total_participant_count)) 
NULL connected_from_count,--11/11/2014                                 
                                 sum(d.login_activity_count) login_activity_count,
                                 sum(d.received_target) received_target,sum(d.sent_target) sent_target,                                 
--                                 sum(d.connected_target) connected_target,
--ROUND(SUM(connected_avg_target * total_participant_count)/ SUM(total_participant_count)) 
NULL connected_target,--11/11/2014
--                                 sum(d.connected_from_target) connected_from_target,
--ROUND(SUM(connected_from_avg_target * total_participant_count)/ SUM(total_participant_count)) 
NULL connected_from_target,--11/11/2014   
                                 sum(d.login_activity_target) login_activity_target
                          FROM engagement_score_summary d, node n 
                          WHERE d.time_frame = p_in_time_frame AND d.record_type ='teamsum' AND d.node_id IN (SELECT * FROM TABLE(get_array(p_in_node_id)))--= p_in_node_id
                                AND trans_month = p_in_end_month
                                AND trans_year = p_in_end_year 
                                AND d.node_id = n.node_id;
                                
     OPEN p_out_result_set5 FOR     
     SELECT NVL(cms.cms_name,d.behavior) behavior,sent_count,received_count,cms_badge.cms_code behavior_badge FROM (    
    SELECT behavior,SUM (sent_count) sent_count,
         SUM (received_count) received_count FROM (
      SELECT behavior,
         sent_count sent_count,
         received_count received_count
    FROM engagement_behavior_summary d
   WHERE d.header_node_id IN (SELECT *
                                FROM TABLE (get_array (p_in_node_id)))
       AND d.time_frame = p_in_time_frame 
       AND trans_month = p_in_end_month
       AND trans_year = p_in_end_year
UNION ALL
SELECT behavior_type behavior, 0 sent_count, 0 received_count
  FROM promo_behavior
 WHERE promotion_id IN (SELECT eligible_promotion_id from (SELECT * FROM (SELECT * FROM (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) WHERE promotion_id IN (SELECT promotion_id FROM promotion where promotion_status = 'live'))) --04/01/2019
GROUP BY behavior
UNION ALL --11/26/2014
SELECT v_cms_no_behavior,(a.total_sent_count-NVL(b.sent_count,0)) sent_count,(a.total_received_count-NVL(b.received_count,0)) received_count FROM (    --Bug # 63563 --Bug # 63719
       SELECT SUM (sent_count) total_sent_count,
         SUM (received_count) total_received_count from engagement_score_summary d WHERE time_frame = p_in_time_frame 
       AND d.record_type ='nodesum' AND d.node_id IN (SELECT * FROM TABLE(get_array(p_in_node_id)))         
       AND trans_month = p_in_end_month
       AND trans_year = p_in_end_year) a,      
      (SELECT SUM(sent_count) sent_count,
         SUM(received_count) received_count
    FROM engagement_behavior_summary d
   WHERE time_frame = p_in_time_frame 
       AND d.header_node_id IN (SELECT * FROM TABLE (get_array (p_in_node_id)))
       AND trans_month = p_in_end_month
       AND trans_year = p_in_end_year) b                          
                          ) d,
(SELECT cms_name,cms_code
     FROM vw_cms_code_value E
    WHERE asset_code in ( 'picklist.promo.recognition.behavior.items') and e.locale=v_locale) cms,  --p_in_locale -- 09/04/2015
    temp_table_session cms_badge,
    (select * from (SELECT RANK ()
                                                                     OVER (
                                                                        PARTITION BY pe.behavior_name
                                                                        ORDER BY
                                                                           rownum)
                                                                        AS rec_rank,
                                                                     pe.*
                                                                FROM (SELECT  br.behavior_name,br.cm_asset_key from badge_promotion bp, badge_rule br WHERE eligible_promotion_id in (select eligible_promotion_id from (SELECT * FROM (SELECT * FROM (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) pep,promotion p WHERE pep.promotion_id = p.promotion_id    --04/01/2019
AND promotion_status = 'live') AND bp.promotion_id = br.promotion_id
AND br.behavior_name is NOT NULL) pe ) WHERE rec_rank =1) br
WHERE cms.cms_code(+) = d.behavior --11/26/2014
AND d.behavior = br.behavior_name(+)
AND cms_badge.cms_name(+) = br.cm_asset_key;
    
   
OPEN p_out_result_set6 FOR
        SELECT node_id,name node_name FROM node n
                          WHERE n.node_id IN (SELECT * FROM TABLE(get_array(p_in_node_id)));
                          
                          OPEN p_out_result_set7 FOR--For team avg and Company average
    SELECT SUM(received_count)/SUM(total_participant_count) AS v_rec_recv_team_avg,
                SUM(sent_count)/SUM(total_participant_count) AS v_rec_sent_team_avg,
--                SUM(connected_from_count)/SUM(total_participant_count) AS v_conn_from_team_avg,
                ROUND(SUM(connected_from_avg * total_participant_count)/ SUM(total_participant_count)) AS v_conn_from_team_avg,--11/11/2014
--                SUM(connected_to_count)/SUM(total_participant_count) AS v_conn_to_team_avg,
               ROUND(SUM(connected_avg * total_participant_count)/ SUM(total_participant_count)) AS v_conn_to_team_avg, --11/11/2014
               SUM(login_activity_count)/SUM(total_participant_count) AS v_login_team_avg,
               v_rec_sent_company_avg AS v_rec_sent_company_avg,v_rec_recv_company_avg AS v_rec_recv_company_avg,v_conn_to_company_avg AS v_conn_to_company_avg,
                v_conn_from_company_avg AS v_conn_from_company_avg,v_login_company_avg AS v_login_company_avg,v_score_company_avg AS v_score_company_avg FROM engagement_score_summary d
    WHERE d.node_id  IN (SELECT * FROM TABLE(get_array(p_in_node_id)))--= p_in_node_id 
                   AND d.time_frame = p_in_time_frame AND d.record_type ='teamsum'
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;
     OPEN p_out_result_set8 FOR
    SELECT NULL FROM DUAL;    
    
    OPEN p_out_result_set9 FOR --Bug # 56923
    SELECT 0 sort_order, un.node_id,name node_name
        FROM user_node un, node n
        WHERE user_id = p_in_user_id
        AND un.is_primary = 1
        AND un.node_id = n.node_id
        AND role IN ('own','mgr')
    UNION ALL
    SELECT 1 sort_order, un.node_id,name node_name
        FROM user_node un, node n
        WHERE user_id = p_in_user_id
        AND un.is_primary = 0
        AND un.node_id = n.node_id
        AND role IN ('own','mgr')
    ORDER BY sort_order,node_name;  
    
    END IF;    
    
    ELSE  
    
    -- user records with or without node inputs
       
    OPEN p_out_result_set7 FOR--For team avg and Company average
    SELECT received_count/total_participant_count AS v_rec_recv_team_avg,
                sent_count/total_participant_count AS v_rec_sent_team_avg,
--                connected_from_count/total_participant_count AS v_conn_from_team_avg,
                connected_from_avg AS v_conn_from_team_avg,
--                connected_to_count/total_participant_count AS v_conn_to_team_avg,
                connected_avg AS v_conn_to_team_avg,            
                login_activity_count/total_participant_count AS v_login_team_avg,
                v_rec_sent_company_avg AS v_rec_sent_company_avg,v_rec_recv_company_avg AS v_rec_recv_company_avg,v_conn_to_company_avg AS v_conn_to_company_avg,
                v_conn_from_company_avg AS v_conn_from_company_avg,v_login_company_avg AS v_login_company_avg,v_score_company_avg AS v_score_company_avg FROM engagement_score_summary d,user_node un
    WHERE d.node_id  = un.node_id
                AND un.user_id = p_in_user_id
                AND un.is_primary = 1
           AND d.time_frame = p_in_time_frame AND d.record_type ='teamsum'
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;   
                   
                   --manager score user records  
                   
                   SELECT  COUNT(1) INTO v_cnt -- Start 05/04/2018 
                          FROM engagement_score_detail d 
                          WHERE d.user_id = p_in_user_id
                               AND d.time_frame = p_in_time_frame   
                               AND trans_month = p_in_end_month
                               AND trans_year = p_in_end_year;
                               
                 IF v_cnt < 2 THEN 
                                
                      OPEN p_out_result_set1 FOR
                                SELECT  v_as_of_date as_of_date,v_company_goal company_goal,v_is_score_active is_score_active,v_selected_benchmarks selected_benchmarks,v_display_target display_target,SUM(d.score) score,                                
                                  SUM(d.received_count) received_count,SUM(d.sent_count) sent_count,SUM(d.connected_to_count) connected_to_count,
                                 SUM(d.connected_from_count) connected_from_count,SUM(d.login_activity_count) login_activity_count,
                                 SUM(d.received_target) received_target,SUM(d.sent_target) sent_target,SUM(d.connected_target) connected_target,
                                 SUM(d.connected_from_target) connected_from_target,SUM(d.login_activity_target) login_activity_target 
                                ,TO_CHAR (v_as_of_date,v_locale_date_format) as_of_date_str,   --02/25/2015   --02/22/2018 --,TO_CHAR (v_as_of_date, 'MON-DD-YYYY') as_of_date_str,
                                 TO_CHAR (v_as_of_date, 'HH:MI PM') as_of_time_str,   --02/25/2015--G6-2761  
                                 NVL(v_timezone_id,'CST') TIMEZONE_ID   --02/25/2015 --01/29/2018 replaced NVL('CST','CST') 
                          FROM engagement_score_detail d 
                          WHERE d.user_id = p_in_user_id
                               AND d.time_frame = p_in_time_frame   
                               AND trans_month = p_in_end_month
                               AND trans_year = p_in_end_year;

               ELSE --User is part of more than one node. We need to calculate score.
                    
                     OPEN p_out_result_set1 FOR  
                     SELECT  v_as_of_date as_of_date,v_company_goal company_goal,v_is_score_active is_score_active,v_selected_benchmarks selected_benchmarks,v_display_target display_target,
                            (CASE WHEN SUM(d.received_count) >= received_target THEN received_weight
                                       WHEN SUM(d.received_count) < received_target THEN received_weight*SUM(d.received_count)/(received_target) 
                              ELSE 0 END +
                              CASE WHEN SUM(d.sent_count) >= sent_target THEN sent_weight
                                       WHEN SUM(d.sent_count) < sent_target THEN sent_weight*SUM(d.sent_count)/(sent_target) 
                              ELSE 0 END +
                               CASE WHEN connected_to >= connected_target THEN connected_weight
                                       WHEN connected_to < connected_target THEN connected_weight*connected_to/(connected_target) 
                              ELSE 0 END +
                               CASE WHEN connected_from >= connected_from_target THEN connected_from_weight
                                       WHEN connected_from < connected_from_target THEN connected_from_weight*connected_from/(connected_from_target) 
                              ELSE 0 END +
                          --    CASE WHEN d.login_activity_count >= login_activity_target THEN login_activity_weight --05/07/2018
                          --             WHEN d.login_activity_count < login_activity_target THEN login_activity_weight*d.login_activity_count/(login_activity_target)  --05/07/2018
                              CASE WHEN MAX(d.login_activity_count) >= login_activity_target THEN login_activity_weight --05/07/2018
                                       WHEN MAX(d.login_activity_count) < login_activity_target THEN login_activity_weight*MAX(d.login_activity_count)/(login_activity_target)  --05/07/2018
                              ELSE 0 END) score,                                  
                              SUM(d.received_count) received_count,SUM(d.sent_count) sent_count,SUM(d.connected_to_count) connected_to_count,
                             SUM(d.connected_from_count) connected_from_count,--d.login_activity_count,--05/07/2018
                             MAX(d.login_activity_count) login_activity_count,--05/07/2018
                             received_target,sent_target,connected_target,connected_from_target,login_activity_target
                            ,TO_CHAR (v_as_of_date,v_locale_date_format) as_of_date_str,   --02/25/2015   --02/22/2018 --,TO_CHAR (v_as_of_date, 'MON-DD-YYYY') as_of_date_str,
                             TO_CHAR (v_as_of_date, 'HH:MI PM') as_of_time_str,   --02/25/2015--G6-2761  
                             NVL(v_timezone_id,'CST') TIMEZONE_ID   --02/25/2015 --01/29/2018 replaced NVL('CST','CST')   
                      FROM engagement_score_detail d,eng_user_connected_to_from eu
                      WHERE d.user_id = p_in_user_id
                           AND d.time_frame = p_in_time_frame   
                           AND d.trans_month = p_in_end_month
                           AND d.trans_year = p_in_end_year
                           AND d.user_id = eu.user_id
                           AND eu.time_frame = p_in_time_frame
                           AND eu.trans_month = p_in_end_month
                           AND eu.trans_year = p_in_end_year
                      GROUP BY received_weight,sent_weight,connected_weight,connected_from_weight,login_activity_weight,received_target,sent_target,
                      connected_target,connected_from_target,login_activity_target,--login_activity_count,---05/07/2018
                      eu.connected_to,eu.connected_from;
               END IF;  -- End 05/04/2018         
                   
                   /* OPEN p_out_result_set1 FOR --10/21/2015 Bug # 64360     --05/04/2018   
        SELECT  v_as_of_date as_of_date,v_company_goal company_goal,v_is_score_active is_score_active,v_selected_benchmarks selected_benchmarks,v_display_target display_target,
(CASE WHEN SUM(d.received_count) >= received_target THEN received_weight
           WHEN SUM(d.received_count) < received_target THEN received_weight*SUM(d.received_count)/(received_target) 
  ELSE 0 END +
  CASE WHEN SUM(d.sent_count) >= sent_target THEN sent_weight
           WHEN SUM(d.sent_count) < sent_target THEN sent_weight*SUM(d.sent_count)/(sent_target) 
  ELSE 0 END +
   CASE WHEN connected_to >= connected_target THEN connected_weight                               --12/28/2016 Bug 70638--05/02/2017
           WHEN connected_to < connected_target THEN connected_weight*connected_to/(connected_target)  --12/28/2016 Bug 70638--05/02/2017
  ELSE 0 END +
   CASE WHEN connected_from >= connected_from_target THEN connected_from_weight --12/28/2016 Bug 70638
           WHEN connected_from < connected_from_target THEN connected_from_weight*connected_from/(connected_from_target)  --12/28/2016 Bug 70638
  ELSE 0 END +
    CASE WHEN d.login_activity_count >= login_activity_target THEN login_activity_weight
           WHEN d.login_activity_count < login_activity_target THEN login_activity_weight*d.login_activity_count/(login_activity_target) 
  ELSE 0 END) score,                                  
                                  SUM(d.received_count) received_count,SUM(d.sent_count) sent_count,connected_to connected_to_count,   --12/28/2016 Bug 70638--05/02/2017
                                 connected_from connected_from_count,d.login_activity_count,                                          --12/28/2016 Bug 70638--05/02/2017
                                 received_target,sent_target,connected_target,connected_from_target,login_activity_target                                
                                 ,TO_CHAR (v_as_of_date,v_locale_date_format) as_of_date_str,   --02/25/2015   --02/22/2018 --,TO_CHAR (v_as_of_date, 'MON-DD-YYYY') as_of_date_str,   --02/25/2015--G6-2761
                                 TO_CHAR (v_as_of_date, 'HH:MI PM') as_of_time_str,   --02/25/2015--G6-2761  
                                 NVL(v_timezone_id,'CST') TIMEZONE_ID   --02/25/2015 --01/29/2018 replaced NVL('CST','CST')
                          FROM engagement_score_detail d,
                               eng_user_connected_to_from eu
                          WHERE d.user_id = p_in_user_id
                               AND d.time_frame = p_in_time_frame   
                               AND d.trans_month = p_in_end_month
                               AND d.trans_year = p_in_end_year
                               AND d.user_id = eu.user_id
                               AND eu.time_frame = p_in_time_frame
                               AND eu.trans_month = p_in_end_month
                               AND eu.trans_year = p_in_end_year
                               AND d.node_id = NVL(p_in_node_id,d.node_id) 
                          GROUP BY received_weight,sent_weight,connected_weight,connected_from_weight,login_activity_weight,received_target,sent_target,connected_target,connected_from_target,login_activity_target,login_activity_count,
                          eu.connected_to,eu.connected_from; */ --12/28/2016 Bug 70638--05/02/2017 -- End 05/04/2018
          
          
          /*                
                   
                   SELECT  COUNT(1) INTO v_cnt --03/16/2015 Bug # 60376
                          FROM engagement_score_detail d 
                          WHERE d.user_id = p_in_user_id
                               AND d.time_frame = p_in_time_frame   
                               AND trans_month = p_in_end_month
                               AND trans_year = p_in_end_year
                               AND d.node_id = NVL(p_in_node_id,d.node_id);
                               
                               IF v_cnt <2 THEN
                          
    OPEN p_out_result_set1 FOR --10/17/2014 Bug 57351 
    SELECT     v_as_of_date as_of_date,v_company_goal company_goal,v_is_score_active is_score_active,v_selected_benchmarks selected_benchmarks,v_display_target display_target,
            d.score,d.received_count,d.sent_count,CASE WHEN p_in_node_id IS NULL THEN eu.connected_to ELSE d2.connected_to_count END connected_to_count,CASE WHEN p_in_node_id IS NULL THEN eu.connected_from ELSE d2.connected_from_count END connected_from_count,--08/25/2015
            d2.login_activity_count,d2.received_target,d2.sent_target,d2.connected_target,d2.connected_from_target,
                                  d2.login_activity_target
                                ,TO_CHAR (v_as_of_date, 'MON-DD-YYYY') as_of_date_str,   --02/25/2015   
                                 TO_CHAR (v_as_of_date, 'HH:MI AM') as_of_time_str,   --02/25/2015
                                 NVL(v_timezone_id,'CST') TIMEZONE_ID   --02/25/2015                                 
    FROM (
        SELECT  user_id,SUM(d.score) score,SUM(d.received_count) received_count,SUM(d.sent_count) sent_count                     
        FROM engagement_score_detail d 
        WHERE time_frame = p_in_time_frame AND d.user_id = p_in_user_id  AND d.node_id = NVL(p_in_node_id,d.node_id)                              
        AND trans_month = p_in_end_month
        AND trans_year = p_in_end_year
        GROUP BY user_id) d,
        engagement_score_detail d2,
        eng_user_connected_to_from eu
    WHERE d2.user_id = d.user_id
        AND d2.user_id = eu.user_id
        AND eu.time_frame = p_in_time_frame
        AND eu.trans_month = p_in_end_month
        AND eu. trans_year = p_in_end_year
        AND d2.time_frame = p_in_time_frame                              
        AND d2.trans_month = p_in_end_month
        AND d2.trans_year = p_in_end_year 
        AND d2.node_id = NVL(p_in_node_id,d2.node_id)     ; --08/25/2015
        
        ELSE         
        
        OPEN p_out_result_set1 FOR --03/16/2015 Bug # 60376        
        SELECT  v_as_of_date as_of_date,v_company_goal company_goal,v_is_score_active is_score_active,v_selected_benchmarks selected_benchmarks,v_display_target display_target,
(CASE WHEN SUM(d.received_count) >= received_target THEN received_weight
           WHEN SUM(d.received_count) < received_target THEN received_weight*SUM(d.received_count)/(received_target) 
  ELSE 0 END +
  CASE WHEN SUM(d.sent_count) >= sent_target THEN sent_weight
           WHEN SUM(d.sent_count) < sent_target THEN sent_weight*SUM(d.sent_count)/(sent_target) 
  ELSE 0 END +
   CASE WHEN connected_to >= connected_target THEN connected_weight
           WHEN connected_to < connected_target THEN connected_weight*connected_to/(connected_target) 
  ELSE 0 END +
   CASE WHEN connected_from >= connected_from_target THEN connected_from_weight
           WHEN connected_from < connected_from_target THEN connected_from_weight*connected_from/(connected_from_target) 
  ELSE 0 END +
    CASE WHEN d.login_activity_count >= login_activity_target THEN login_activity_weight
           WHEN d.login_activity_count < login_activity_target THEN login_activity_weight*d.login_activity_count/(login_activity_target) 
  ELSE 0 END) score,                                  
                                  SUM(d.received_count) received_count,SUM(d.sent_count) sent_count,connected_to connected_to_count,
                                 connected_from connected_from_count,d.login_activity_count,
                                 received_target,sent_target,connected_target,connected_from_target,login_activity_target                                
                                ,TO_CHAR (SYSDATE, 'MON-DD-YYYY') as_of_date_str,   --02/25/2015   
                                 TO_CHAR (SYSDATE, 'HH:MI AM') as_of_time_str,   --02/25/2015
                                 NVL('CST','CST') TIMEZONE_ID   --02/25/2015
                          FROM engagement_score_detail d,eng_user_connected_to_from eu
                          WHERE d.user_id = p_in_user_id
                               AND d.time_frame = p_in_time_frame   
                               AND d.trans_month = p_in_end_month
                               AND d.trans_year = p_in_end_year
                               AND d.user_id = eu.user_id
                               AND eu.time_frame = p_in_time_frame
                               AND eu.trans_month = p_in_end_month
                               AND eu.trans_year = p_in_end_year
                          GROUP BY received_weight,sent_weight,connected_weight,connected_from_weight,login_activity_weight,received_target,sent_target,connected_target,connected_from_target,login_activity_target,login_activity_count,eu.connected_to,eu.connected_from;
                          
        END IF;--03/16/2015 Bug # 60376
        */
                       
    OPEN p_out_result_set2 FOR
    SELECT NULL FROM DUAL;
    
    OPEN p_out_result_set3 FOR
    SELECT NULL FROM DUAL;
    
     OPEN p_out_result_set4 FOR
    SELECT NULL FROM DUAL;
    
    OPEN p_out_result_set5 FOR
    SELECT NVL(cms.cms_name,d.behavior) behavior,sent_count,received_count,cms_badge.cms_code behavior_badge FROM (    
    SELECT behavior,SUM (sent_count) sent_count,
         SUM (received_count) received_count FROM (
      SELECT behavior,
         sent_count sent_count,
         received_count received_count
    FROM engagement_behavior_user_mode d
   WHERE time_frame = p_in_time_frame 
       AND d.user_id = p_in_user_id    AND (p_in_node_id IS NULL OR d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )))       --09/29/2014  Bug # 56995.           
       AND trans_month = p_in_end_month
       AND trans_year = p_in_end_year
UNION ALL
SELECT behavior_type behavior, 0 sent_count, 0 received_count
  FROM promo_behavior
 WHERE promotion_id IN (SELECT eligible_promotion_id from (SELECT * FROM (SELECT * FROM (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) WHERE promotion_id IN (SELECT promotion_id FROM promotion where promotion_status = 'live'))  --04/01/2019
UNION ALL
SELECT v_cms_no_behavior,(a.total_sent_count-NVL(b.sent_count,0)) sent_count,(a.total_received_count-NVL(b.received_count,0)) received_count FROM (   --Bug # 63563--Bug # 63719
       SELECT SUM (sent_count) total_sent_count,
         SUM (received_count) total_received_count from engagement_score_detail d WHERE time_frame = p_in_time_frame 
       AND d.user_id = p_in_user_id    AND (p_in_node_id IS NULL OR d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )))       --09/29/2014  Bug # 56995.           
       AND trans_month = p_in_end_month
       AND trans_year = p_in_end_year) a,      
      (SELECT SUM(sent_count) sent_count,
         SUM(received_count) received_count
    FROM engagement_behavior_user_mode d
   WHERE time_frame = p_in_time_frame 
       AND d.user_id = p_in_user_id    AND (p_in_node_id IS NULL OR d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )))     
       AND trans_month = p_in_end_month
       AND trans_year = p_in_end_year) b)
GROUP BY behavior                          
                          ) d,
(SELECT cms_name,cms_code
     FROM vw_cms_code_value E
    WHERE asset_code in ( 'picklist.promo.recognition.behavior.items') and e.locale=v_locale) cms,  --p_in_locale -- 09/04/2015
    temp_table_session cms_badge,
    (select * from (SELECT RANK ()
                                                                     OVER (
                                                                        PARTITION BY pe.behavior_name
                                                                        ORDER BY
                                                                           rownum)
                                                                        AS rec_rank,
                                                                     pe.*
                                                                FROM (SELECT  br.behavior_name,br.cm_asset_key from badge_promotion bp, badge_rule br WHERE eligible_promotion_id in (select eligible_promotion_id from (SELECT * FROM (SELECT * FROM (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) pep,promotion p WHERE pep.promotion_id = p.promotion_id    --04/01/2019
AND promotion_status = 'live') AND bp.promotion_id = br.promotion_id
AND br.behavior_name is NOT NULL) pe ) WHERE rec_rank =1) br
WHERE cms.cms_code(+) = d.behavior --11/26/2014
AND d.behavior = br.behavior_name(+)
AND cms_badge.cms_name(+) = br.cm_asset_key;

    OPEN p_out_result_set6 FOR
    SELECT NULL FROM DUAL;
    
    OPEN p_out_result_set8 FOR
    SELECT TO_CHAR (la.login_date_time, 'MON-DD-YYYY') login_date,
             TO_CHAR (la.login_date_time, 'HH:MI AM') login_time,
             c.timezone_id TIMEZONE_ID   --02/25/2015
        FROM login_activity la      
             ,country c    --02/25/2015
             ,user_address ua   --02/25/2015
       WHERE     la.user_id = p_in_user_id
             AND la.user_id = ua.user_id   
             AND ua.country_id = c.country_id   
             AND ua.is_primary = 1     
             AND la.login_date_time BETWEEN p_start_date AND p_end_date
             AND la.login_date_time <= v_as_of_date_orig--07/31/2017
    ORDER BY la.login_date_time;         -- Bug # 55839 Bug # 55833

    SELECT first_name||' '||last_name INTO p_out_user_name FROM application_user
             WHERE user_id = p_in_user_id;
             
             OPEN p_out_result_set9 FOR
    SELECT NULL FROM DUAL;    
    END IF;
   -- prc_execution_log_entry(c_process_name,1,'INFO','END',null); --11/28/2017
    
    p_out_return_code := '00';
EXCEPTION
WHEN e_no_data THEN
    prc_execution_log_entry(c_process_name,1,'INFO',SQLERRM,null);    
    OPEN p_out_result_set1 FOR SELECT * FROM DUAL WHERE 1=2;
    OPEN p_out_result_set2 FOR SELECT * FROM DUAL WHERE 1=2;
    OPEN p_out_result_set3 FOR SELECT * FROM DUAL WHERE 1=2;
    OPEN p_out_result_set4 FOR SELECT * FROM DUAL WHERE 1=2;
    OPEN p_out_result_set5 FOR SELECT * FROM DUAL WHERE 1=2;
    OPEN p_out_result_set6 FOR SELECT * FROM DUAL WHERE 1=2;
    OPEN p_out_result_set7 FOR SELECT * FROM DUAL WHERE 1=2;
    OPEN p_out_result_set8 FOR SELECT * FROM DUAL WHERE 1=2;
    OPEN p_out_result_set9 FOR SELECT * FROM DUAL WHERE 1=2;
    p_out_user_name :=NULL;
    p_out_return_code := '1';    
WHEN OTHERS THEN 
p_out_return_code := '99';
    prc_execution_log_entry(c_process_name,1,'INFO',SQLERRM,null);  

END prc_engagement_manager_score;

PROCEDURE prc_eng_childnodes_sort(
p_in_user_id        IN    NUMBER,
p_in_mode           IN   VARCHAR2,
p_in_time_frame    IN VARCHAR2,
p_in_node_id        IN    VARCHAR2,
p_in_locale         IN    VARCHAR2,
p_in_end_month     IN    NUMBER,
p_in_end_year       IN    NUMBER,
p_in_rownum_start   IN    NUMBER,
p_in_rownum_end     IN    NUMBER,
p_in_sort_mem_col  IN   VARCHAR2,
p_in_sort_mem_order IN VARCHAR2,
p_in_sort_team_col IN VARCHAR2,
p_in_sort_team_order IN VARCHAR2,
p_in_start_date     IN    VARCHAR2,
p_in_end_date       IN    VARCHAR2,
p_out_result_set1   OUT sys_refcursor)
IS

 c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_ENG_CHILDNODES_SORT';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by         CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_child_node_list    VARCHAR2(1000);
  
  v_as_of_date DATE;
v_company_goal NUMBER;
v_is_score_active NUMBER;

v_rec_sent_company_avg NUMBER;
v_rec_recv_company_avg NUMBER;
v_conn_to_company_avg NUMBER;
v_conn_from_company_avg NUMBER;
v_login_company_avg NUMBER;
v_score_company_avg NUMBER;

v_sortCol             VARCHAR2(200);
l_query VARCHAR2(32767);
  
BEGIN

      
      BEGIN
      SELECT as_of_date,company_goal,is_score_active INTO v_as_of_date,v_company_goal,v_is_score_active FROM promo_engagement pe WHERE EXISTS 
      (SELECT * FROM promotion WHERE promotion_status = 'live' AND promotion_id = pe.promotion_id  );
      END;
      
   
      SELECT received_count/total_participant_count,
                sent_count/total_participant_count,
                connected_from_count/total_participant_count,
                connected_to_count/total_participant_count,
                login_activity_count/total_participant_count,score  INTO v_rec_recv_company_avg,v_rec_sent_company_avg,v_conn_from_company_avg,v_conn_to_company_avg,v_login_company_avg,v_score_company_avg  from engagement_score_summary d
    WHERE parent_node_id IS NULL
           AND d.time_frame = p_in_time_frame AND d.record_type ='nodesum'
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;
                         
         
    --get first child node list        
        SELECT LISTAGG(node_id,',') WITHIN GROUP(ORDER BY node_id)
            INTO v_child_node_list
        FROM
        (SELECT n.node_id FROM node n WHERE --Bug # 56244
        n.parent_node_id IN (SELECT * FROM TABLE(get_array(p_in_node_id)))); --Bug # 56246
         
                  
         l_query  := 'SELECT * FROM ( ';
    
    v_sortCol := p_in_sort_team_col || ' ' || p_in_sort_team_order;
        l_query := l_query ||'  '||'               ----Bug # 56527
        SELECT ROWNUM RN,RS.* FROM ( SELECT * FROM (SELECT '''||v_company_goal||''''||' score_target,n.node_id,n.name member,
--        (SELECT LISTAGG (au.first_name||'' ''||au.last_name, '', '') WITHIN GROUP (ORDER BY 1) AS name FROM user_node un,application_user au WHERE node_id=n.node_id and role =''mgr''   --11/12/2014
          (SELECT LISTAGG (au.last_name||'', ''||au.first_name, ''  '') WITHIN GROUP (ORDER BY 1) AS name FROM user_node un,application_user au WHERE node_id=n.node_id and role =''mgr''   --11/12/2014
              AND un.user_id = au.user_id) AS manager_name ,ecd.score score_actual,
        received_target recRecv_target,sent_target recSent_target,
        connected_target paxRecTo_target,
--        NULL paxRecTo_target,
       connected_from_target paxRecBy_target,
--        NULL paxRecBy_target,
        login_activity_target visits_target,
        received_count recRecv_actual,sent_count recSent_actual,connected_to_count paxRecTo_actual,connected_from_count paxRecBy_actual,login_activity_count visits_actual,
        (received_count-received_target) recRecv_diff,
        (sent_count-sent_target) recSent_diff,
        (connected_to_count-connected_target) paxRecTo_diff,--Bug # 63612
--          NULL paxRecTo_diff,
        (connected_from_count-connected_from_target) paxRecBy_diff,--Bug # 63612
--        NULL paxRecBy_diff,
        (login_activity_count-login_activity_target) visits_diff     
        FROM engagement_score_summary ecd, node n
        WHERE ecd.node_id IN (SELECT * FROM TABLE(get_array('''||v_child_node_list||''')))
            AND ecd.record_type =''nodesum''            
            AND ecd.node_id = n.node_id
            AND time_frame = '''||p_in_time_frame||'''
            AND trans_month = '''||p_in_end_month||'''
            AND trans_year = '''||p_in_end_year||''')         
             ORDER BY '|| v_sortCol ||'  )RS ) WHERE RN > ' ||p_in_rownum_start||' AND RN   < '|| p_in_rownum_end;  
       
         OPEN p_out_result_set1 FOR
   l_query;
   
    prc_execution_log_entry(c_process_name,1,'INFO','END',null); 
EXCEPTION
WHEN OTHERS THEN 
    prc_execution_log_entry(c_process_name,1,'INFO',SQLERRM,null);  

END;

PROCEDURE prc_eng_teammember_sort(
p_in_user_id        IN    NUMBER,
p_in_mode           IN   VARCHAR2,
p_in_time_frame    IN VARCHAR2,
p_in_node_id        IN    VARCHAR2,
p_in_locale         IN    VARCHAR2,
p_in_end_month     IN    NUMBER,
p_in_end_year       IN    NUMBER,
p_in_rownum_start   IN    NUMBER,
p_in_rownum_end     IN    NUMBER,
p_in_sort_mem_col  IN   VARCHAR2,
p_in_sort_mem_order IN VARCHAR2,
p_in_sort_team_col IN VARCHAR2,
p_in_sort_team_order IN VARCHAR2,
p_in_start_date     IN    VARCHAR2,
p_in_end_date       IN    VARCHAR2,
p_out_result_set1   OUT sys_refcursor)
IS
 /******************************************************************************
     NAME:       fnc_check_public_recog_elig
     PURPOSE:    Check the public recog eligibility for engagement dashboard

     Date                   Author           Description
     ----------           ---------------  ------------------------------------------------
     08/18/2014 Ravi Dhanekula    Initial code.
     09/02/2014 Ravi Dhanekula    Bug # 56010..Added condition AND ecd.node_id = un.node_id to the query to avoid duplicates.
     09/05/2014 Ravi Dhanekula   Bug # 56244
     09/29/2014 Ravi Dhanekula   Bug # 56999 Added avatar_small to the result set.
     11/11/2014 Ravi Dhanekula Bug # 57896 RPM Unique People Issue..New CR
     11/12/2014 Ravi Dhanekula Bug # 57776 Remove duplicates from team member list.
     04/10/2015 Suresh J       Bug # 61270 RPM Sorting issue   
     08/11/2015 Ravi Dhanekula Bug # 63626 - Added node_id.
     08/31/2015 Ravi Dhanekula Bug # 63557  Sort by column are not working in Team Dashboard page
     11/13/2015 nagarajs       Bug 64599 - Inactive pax is showing up in Manager's team dash board under RPM   
   ******************************************************************************/
  c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_ENG_TEAMMEMBER_SORT';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by         CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_user_node_list    VARCHAR2(32767);
  
  v_as_of_date DATE;
v_company_goal NUMBER;
v_is_score_active NUMBER;

v_rec_sent_company_avg NUMBER;
v_rec_recv_company_avg NUMBER;
v_conn_to_company_avg NUMBER;
v_conn_from_company_avg NUMBER;
v_login_company_avg NUMBER;
v_score_company_avg NUMBER;

v_sortCol             VARCHAR2(200);
l_query VARCHAR2(32767);
  
BEGIN

  l_query  := 'SELECT * FROM ( ';
    
--  v_sortCol := p_in_sort_mem_col  || ' ' || p_in_sort_mem_order ||','||'member' || ' ' ||p_in_sort_mem_order;   --04/10/2015
--    v_sortCol := 'last_name'  || ' ' || p_in_sort_mem_order ||','||p_in_sort_mem_col|| ' ' ||p_in_sort_mem_order;   --04/10/2015--08/31/2015
    v_sortCol := p_in_sort_mem_col|| ' ' ||p_in_sort_mem_order||','||'last_name'  || ' ' || p_in_sort_mem_order;--08/31/2015

prc_execution_log_entry(c_process_name,1,'INFO','START',null);  
    
      
      BEGIN
      SELECT as_of_date,company_goal,is_score_active INTO v_as_of_date,v_company_goal,v_is_score_active FROM promo_engagement pe WHERE EXISTS 
      (SELECT * FROM promotion WHERE promotion_status = 'live' AND promotion_id = pe.promotion_id  );
      END;
      
   SELECT received_count/total_participant_count,
                sent_count/total_participant_count,
--                connected_from_count/total_participant_count,
                connected_from_avg, --11/11/2014
--                connected_to_count/total_participant_count,
                connected_avg, --11/11/2014
                login_activity_count/total_participant_count,score  INTO v_rec_recv_company_avg,v_rec_sent_company_avg,v_conn_from_company_avg,v_conn_to_company_avg,v_login_company_avg,v_score_company_avg  from engagement_score_summary d
    WHERE parent_node_id IS NULL
           AND d.time_frame = p_in_time_frame AND d.record_type ='nodesum'
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;
       
        --Bug # 56999 09/29/2014
    l_query := l_query ||'  '||'    
        SELECT ROWNUM RN,RS.* FROM ( SELECT score_target,node_id,NULL node_name,user_id,member||CASE WHEN node_count >1 THEN   '' ( '' || node_name || '' ) ''   ELSE NULL END member,last_name,score_actual,--Bug # 60377
        recRecv_target,recSent_target,paxRecTo_target,paxRecBy_target,visits_target,recRecv_actual,recSent_actual,paxRecTo_actual,paxRecBy_actual,visits_actual,
        recRecv_diff,recSent_diff,paxRecTo_diff,paxRecBy_diff,visits_diff,avatar_url        
         FROM (SELECT DISTINCT '''||v_company_goal||''''||' score_target'||',un.node_id,name node_name,a.user_id,a.first_name member,a.last_name,ecd.score score_actual,     --11/21/2014 Bug # 57776
        received_target recRecv_target,sent_target recSent_target,connected_target paxRecTo_target,connected_from_target paxRecBy_target,login_activity_target visits_target,
         received_count recRecv_actual,sent_count recSent_actual,connected_to_count paxRecTo_actual,connected_from_count paxRecBy_actual,login_activity_count visits_actual,
        (received_count-received_target) recRecv_diff,
        (sent_count-sent_target) recSent_diff,
        (connected_to_count-connected_target) paxRecTo_diff,
        (connected_from_count-connected_from_target) paxRecBy_diff,
        (login_activity_count-login_activity_target) visits_diff,avatar_small avatar_url,
         (SELECT COUNT(1) FROM user_node WHERE node_id IN (SELECT * FROM TABLE(get_array ('''||p_in_node_id||'''))) AND user_id = ecd.user_id) node_count        
        FROM user_node un,application_user a,engagement_score_detail ecd,node n,participant p
        WHERE un.node_id IN (SELECT * FROM TABLE(get_array('''||p_in_node_id||'''))) 
            AND un.user_id = a.user_id
            AND a.is_active = 1  --11/13/2015
            AND a.user_id = ecd.user_id
            AND un.node_id = n.node_id
            AND ecd.node_id = un.node_id
            AND un.user_id = p.user_id 
            AND ecd.time_frame = '''||p_in_time_frame||'''
            AND trans_month = '''||p_in_end_month||'''
            AND trans_year = '''||p_in_end_year||''' )
            ORDER BY '|| v_sortCol ||'  ) RS ) WHERE RN > ' ||p_in_rownum_start||' AND RN   < '|| p_in_rownum_end;--RN BETWEEN ' ||p_in_rownum_start||' AND '|| p_in_rownum_end;

            OPEN p_out_result_set1 FOR
   l_query;

EXCEPTION
WHEN OTHERS THEN 
    prc_execution_log_entry(c_process_name,1,'INFO',SQLERRM,null);  
END;
PROCEDURE prc_eng_recog_byorg_chart
( p_in_node_id VARCHAR2,
  p_in_start_date VARCHAR2,  
  p_in_end_date  VARCHAR2,
  p_in_giver_recvr VARCHAR2,
  p_in_time_frame    IN VARCHAR2,
  p_in_end_month     IN    NUMBER,
  p_in_end_year       IN    NUMBER,
  p_out_return_code       OUT NUMBER,
  p_out_data                  OUT SYS_REFCURSOR) IS
  /******************************************************************************
     PURPOSE:    Used to create a data set results for engagement manager dashboard..Recognition counts by Org chart.

     Date                   Author           Description
     ----------           ---------------  ------------------------------------------------
     06/18/2014 Ravi Dhanekula    Initial code.
     09/26/2014 Ravi Dhanekula  Bug # 56816 
     12/12/2014 Ravi Dhanekula    Bug # 57847 Sorting added.
   ******************************************************************************/ 
  c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_ENG_RECOG_BYORG_CHART';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by         CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_stage              VARCHAR2(500);
  
  
  BEGIN
  
  IF p_in_giver_recvr = 'giver' THEN
  
  OPEN p_out_data FOR
    SELECT * FROM (SELECT SUM(d.received_count) recognition_count,receiver_node_id node_id,n.name node_name
    FROM engagement_recog_sent d,
              node n
   WHERE d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )) 
    AND    d.time_frame = p_in_time_frame
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year
                   AND d.receiver_node_id = n.node_id                             
    GROUP BY receiver_node_id,n.name
    ORDER BY recognition_count DESC,node_name ASC ) WHERE ROWNUM<=10;--09/26/2014--12/12/2014
  ELSE
  OPEN p_out_data FOR
    SELECT * FROM (SELECT SUM(d.sent_count) recognition_count,sender_node_id node_id,n.name node_name
    FROM engagement_recog_received d,
              node n
   WHERE d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )) 
    AND    d.time_frame = p_in_time_frame
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year
                   AND d.sender_node_id = n.node_id                   
     GROUP BY sender_node_id,n.name
     ORDER BY recognition_count DESC,node_name ASC ) WHERE ROWNUM<=10; --09/26/2014--12/12/2014
    END IF;
    
    EXCEPTION
WHEN OTHERS THEN 
    prc_execution_log_entry(c_process_name,1,'INFO',SQLERRM,null);  
    p_out_return_code :='99';
  END prc_eng_recog_byorg_chart;
  
  PROCEDURE prc_eng_recog_bypromo_chart
( p_in_node_id VARCHAR2,
  p_in_user_id        IN    NUMBER,
  p_in_mode           IN   VARCHAR2,
  p_in_start_date VARCHAR2,  
  p_in_end_date  VARCHAR2,
  p_in_giver_recvr VARCHAR2,
  p_in_time_frame    IN VARCHAR2,
  p_in_end_month     IN    NUMBER,
  p_in_end_year       IN    NUMBER,
  p_in_locale         IN    VARCHAR2,
  p_out_return_code       OUT NUMBER,
  p_out_data                  OUT SYS_REFCURSOR) IS
  /******************************************************************************
     PURPOSE:    Used to create a data set results for engagement manager dashboard..Recognition counts by Org chart.

     Date                   Author           Description
     ----------           ---------------  ------------------------------------------------
     06/18/2014 Ravi Dhanekula    Initial code.
     09/29/2014 Ravi Dhanekula    Bug # 57004. Will show all eligible promotions.
    04/01/2019  Suresh J          SA Integeration with DayMaker. Excludes/Includes PURL/Celebration promotions based on boolean value of new.service.anniversary.enabled
   ******************************************************************************/ 
  c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_ENG_RECOG_BYPROMO_CHART';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by         CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_stage              VARCHAR2(500);
  v_user_node_list    VARCHAR2(32767);
  v_sa_enabled         os_propertyset.boolean_val%type; --04/01/2019
  
  BEGIN
    BEGIN
      SELECT boolean_val                            --04/01/2019
        INTO v_sa_enabled
        FROM os_propertyset
       WHERE entity_name = 'new.service.anniversary.enabled';
     EXCEPTION
         WHEN OTHERS THEN
         v_sa_enabled := 0;
     END;

  IF p_in_mode = 'team' THEN
  IF p_in_giver_recvr = 'giver' THEN
  
  OPEN p_out_data FOR
  SELECT fnc_cms_asset_code_val_extr(promo_name_asset_code,'PROMOTION_NAME_',p_in_locale) promotion_name,recognition_count FROM (
    SELECT SUM(recognition_count) recognition_count,promo_name_asset_code FROM (
      SELECT d.sent_count recognition_count,p.promo_name_asset_code
    FROM eng_recog_sent_by_promo d,
              promotion p
    WHERE d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )) 
    AND    d.time_frame = p_in_time_frame
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year
                   AND d.promotion_id = p.promotion_id
                   UNION ALL --09/29/2014 
SELECT 0 recognition_count,p.promo_name_asset_code
    FROM (SELECT * FROM (SELECT * FROM (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) pmp,  --04/01/2019
              promotion p2,
              promotion p
    WHERE p2.promotion_type = 'engagement'
      AND p2.promotion_status = 'live'
      ANd p2.promotion_id = pmp.promotion_id
      AND pmp.eligible_promotion_id = p.promotion_id )
      GROUP BY promo_name_asset_code) ;
  ELSE
  OPEN p_out_data FOR
  SELECT fnc_cms_asset_code_val_extr(promo_name_asset_code,'PROMOTION_NAME_',p_in_locale) promotion_name ,recognition_count FROM (
    SELECT SUM(recognition_count) recognition_count,promo_name_asset_code FROM (
      SELECT d.received_count recognition_count,promo_name_asset_code
    FROM eng_recog_recvd_by_promo d,
              promotion p
   WHERE d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )) 
    AND    d.time_frame = p_in_time_frame
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year
                   AND d.promotion_id = p.promotion_id     
                   UNION ALL --09/29/2014 
SELECT 0 recognition_count,p.promo_name_asset_code
    FROM (SELECT * FROM (SELECT * FROM (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) pmp,  --04/01/2019
              promotion p2,
              promotion p
    WHERE p2.promotion_type = 'engagement'
      AND p2.promotion_status = 'live'
      ANd p2.promotion_id = pmp.promotion_id
      AND pmp.eligible_promotion_id = p.promotion_id )
      GROUP BY promo_name_asset_code) ;
    END IF;
    
    ELSE
    IF p_in_giver_recvr = 'giver' THEN
  
  OPEN p_out_data FOR
  SELECT fnc_cms_asset_code_val_extr(promo_name_asset_code,'PROMOTION_NAME_',p_in_locale) promotion_name,recognition_count FROM (    
    SELECT SUM(recognition_count) recognition_count,promo_name_asset_code FROM (
      SELECT d.sent_count recognition_count,p.promo_name_asset_code
    FROM eng_recog_sent_by_promo_user d,
              promotion p
    WHERE d.user_id = p_in_user_id AND (p_in_node_id IS NULL OR d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )))
    AND    d.time_frame = p_in_time_frame
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year
                   AND d.promotion_id = p.promotion_id
                   UNION ALL --09/29/2014 
SELECT 0 recognition_count,p.promo_name_asset_code
    FROM (SELECT * FROM (SELECT * FROM (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) pmp,  --04/01/2019
              promotion p2,
              promotion p
    WHERE p2.promotion_type = 'engagement'
      AND p2.promotion_status = 'live'
      ANd p2.promotion_id = pmp.promotion_id
      AND pmp.eligible_promotion_id = p.promotion_id )
      GROUP BY promo_name_asset_code);
  ELSE
  OPEN p_out_data FOR
  SELECT fnc_cms_asset_code_val_extr(promo_name_asset_code,'PROMOTION_NAME_',p_in_locale) promotion_name ,recognition_count FROM (    
     SELECT SUM(recognition_count) recognition_count,promo_name_asset_code FROM (
      SELECT d.received_count recognition_count,promo_name_asset_code
    FROM eng_recog_recvd_by_promo_user d,
              promotion p
   WHERE d.user_id = p_in_user_id AND (p_in_node_id IS NULL OR d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )))
    AND    d.time_frame = p_in_time_frame
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year
                   AND d.promotion_id = p.promotion_id     
                   UNION ALL --09/29/2014 
SELECT 0 recognition_count,p.promo_name_asset_code
    FROM (SELECT * FROM (SELECT * FROM (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) p WHERE NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) pmp,  --04/01/2019
              promotion p2,
              promotion p
    WHERE p2.promotion_type = 'engagement'
      AND p2.promotion_status = 'live'
      ANd p2.promotion_id = pmp.promotion_id
      AND pmp.eligible_promotion_id = p.promotion_id )
      GROUP BY promo_name_asset_code) ;
    END IF;
    
    END IF;
    
    EXCEPTION
WHEN OTHERS THEN 
    prc_execution_log_entry(c_process_name,1,'INFO',SQLERRM,null);  
    p_out_return_code :='99';
    
  END prc_eng_recog_bypromo_chart;
  
  PROCEDURE prc_eng_recog_byuser
( p_in_node_id VARCHAR2,
  p_in_user_id        IN    NUMBER,
  p_in_mode           IN   VARCHAR2,
  p_in_start_date VARCHAR2,
  p_in_end_date  VARCHAR2,
  p_in_giver_recvr VARCHAR2,
  p_in_time_frame    IN VARCHAR2,
  p_in_end_month     IN    NUMBER,
  p_in_end_year       IN    NUMBER,
  p_in_locale         IN    VARCHAR2, 
  p_out_viewer_first_name OUT VARCHAR2,
  p_out_viewer_avatar_url OUT VARCHAR2,
  p_out_return_code       OUT NUMBER,
  p_out_node_names OUT SYS_REFCURSOR,
  p_out_data                  OUT SYS_REFCURSOR) IS
  /******************************************************************************
     PURPOSE:  Used to create a data set results for engagement manager dashboard..Recognition counts by user data.

     Date                   Author           Description
     ----------           ---------------  ------------------------------------------------
     06/18/2014 Ravi Dhanekula    Initial code.
     08/28/2014 Hanumanth Kandhi  Bug # 56138 Added p_out_node_names cursor for user mode.
     12/12/2014 Ravi Dhanekula    Bug # 57847 Sorting added.

   ******************************************************************************/ 
  c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_ENG_RECOG_BYUSER';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by         CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_stage              VARCHAR2(500);
  BEGIN
  
  IF p_in_mode = 'team' THEN
  
  OPEN p_out_node_names FOR
  SELECT node_id,name node_name
  FROM node WHERE 
  node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )) ;
  
  
  IF p_in_giver_recvr = 'giver' THEN
  
  OPEN p_out_data FOR
  SELECT SUM(d.received_count) recognition_count,n.node_id,n.name node_name,au.first_name,au.user_id,p.avatar_small avatar
    FROM engagement_recog_sent_by_user d,
              application_user au,
              user_node un,
              node n,
              participant p
    WHERE d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )) 
           AND  d.time_frame = p_in_time_frame
           AND trans_month = p_in_end_month
           AND trans_year = p_in_end_year
           AND d.receiver_user_id = au.user_id
           AND au.user_id = un.user_id
           AND un.is_primary = 1
           AND un.node_id = n.node_id
           AND au.user_id = p.user_id
    GROUP BY n.node_id,n.name,au.first_name,au.user_id,p.avatar_small ORDER BY recognition_count DESC,node_name ASC,au.first_name ASC ; --12/12/2014
  ELSE
  OPEN p_out_data FOR
  SELECT SUM(d.sent_count) recognition_count,n.node_id,n.name node_name,au.first_name,au.user_id,p.avatar_small avatar
    FROM engagement_recog_recvd_by_user d,
              application_user au,
              user_node un,
              node n,
              participant p
    WHERE d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )) 
           AND  d.time_frame = p_in_time_frame
           AND trans_month = p_in_end_month
           AND trans_year = p_in_end_year
           AND d.sender_user_id = au.user_id
           AND au.user_id = un.user_id
           AND un.is_primary = 1
           AND un.node_id = n.node_id
           AND au.user_id = p.user_id
    GROUP BY n.node_id,n.name,au.first_name,au.user_id,p.avatar_small ORDER BY recognition_count DESC,node_name ASC,au.first_name ASC; --12/12/2014
    
  END IF;
    
  ELSE
  
      OPEN p_out_node_names FOR SELECT * FROM DUAL WHERE 1=2;
      
   SELECT first_name, avatar_small INTO p_out_viewer_first_name,p_out_viewer_avatar_url
  FROM application_user au,participant p
  WHERE au.user_id = p_in_user_id
  AND au.user_id = p.user_id;
  
   IF p_in_giver_recvr = 'giver' THEN
   OPEN p_out_data FOR
    
    SELECT d.recognition_count,n.node_id,n.name node_name,au.first_name,au.user_id,p.avatar_small avatar FROM
(SELECT SUM(d.received_count) recognition_count,receiver_user_id
    FROM eng_recog_sent_user_mode d
    WHERE d.user_id = p_in_user_id AND (p_in_node_id IS NULL OR d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )))       --09/29/2014  Bug # 56995.           
           AND  d.time_frame = p_in_time_frame
           AND trans_month = p_in_end_month
           AND trans_year = p_in_end_year
           GROUP BY d.receiver_user_id) d,
           application_user au,
             user_node un,
             node n,
             participant p
           WHERE            
           d.receiver_user_id = au.user_id
           AND au.user_id = un.user_id
           AND un.is_primary = 1
           AND un.node_id = n.node_id
           AND au.user_id = p.user_id ORDER BY recognition_count DESC,node_name ASC;--12/12/2014
  ELSE
  OPEN p_out_data FOR
    SELECT d.recognition_count,n.node_id,n.name node_name,au.first_name,au.user_id,p.avatar_small avatar FROM
(SELECT SUM(d.sent_count) recognition_count,sender_user_id
    FROM eng_recog_recvd_user_mode d
    WHERE d.user_id = p_in_user_id  AND (p_in_node_id IS NULL OR d.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_node_id  ) )))       --09/29/2014  Bug # 56995.           
           AND  d.time_frame = p_in_time_frame
           AND trans_month = p_in_end_month
           AND trans_year = p_in_end_year
           GROUP BY d.sender_user_id) d,
           application_user au,
             user_node un,
             node n,
             participant p
           WHERE            
           d.sender_user_id = au.user_id
           AND au.user_id = un.user_id
           AND un.is_primary = 1
           AND un.node_id = n.node_id
           AND au.user_id = p.user_id ORDER BY recognition_count DESC,node_name ASC;--12/12/2014
           
           
    END IF;
    
    END IF;
  
  EXCEPTION
WHEN OTHERS THEN 
    prc_execution_log_entry(c_process_name,1,'INFO',SQLERRM,null);  
    p_out_return_code :='99';
    END;
    
PROCEDURE prc_engagement_notification(
p_in_user_id        IN    NUMBER,
p_in_mode           IN   VARCHAR2,
p_in_end_month     IN    NUMBER,
p_in_end_year       IN    NUMBER,
p_out_result_set1   OUT sys_refcursor,
p_out_return_code OUT NUMBER
)
IS
/******************************************************************************
     PURPOSE:    Used for RPM notification emails.
      
     p_out_result_set1: Result set for Nodesum data.    

     Date                   Author           Description
     ----------           ---------------  ------------------------------------------------
     06/18/2014          Ravi Dhanekula    Initial code.    
     02/10/2015          Suresh J          Bug 59563 -  Added Locale to the result set 
   ******************************************************************************/ 
  c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_ENGAGEMENT_NOTIFICATION';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by         CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_stage              VARCHAR2(500);

v_company_goal NUMBER;
v_is_score_active NUMBER;
v_user_name application_user.first_name%TYPE;
v_email_address user_email_address.email_addr%TYPE;
v_node_name VARCHAR2(32767);
v_user_node_list    VARCHAR2(32767);
v_score_company_avg NUMBER;
v_rec_recv_company_avg NUMBER;
v_rec_sent_company_avg NUMBER;
v_conn_from_company_avg NUMBER;
v_conn_to_company_avg NUMBER;
v_login_company_avg NUMBER;

v_rec_recv_team_avg NUMBER;
v_rec_sent_team_avg NUMBER;
v_conn_from_team_avg NUMBER;
v_conn_to_team_avg NUMBER;
v_login_team_avg NUMBER;
v_language_id application_user.language_id%TYPE;    --02/10/2015



BEGIN
    prc_execution_log_entry(c_process_name,1,'INFO','START',null);  
          
      BEGIN
      SELECT company_goal,is_score_active INTO v_company_goal,v_is_score_active FROM promo_engagement pe WHERE EXISTS 
      (SELECT * FROM promotion WHERE promotion_status = 'live' AND promotion_id = pe.promotion_id  );
      EXCEPTION
      WHEN OTHERS THEN 
       prc_execution_log_entry(c_process_name,1,'INFO',SQLERRM,null);  --sql error fix
      END;
      
      BEGIN     
      
       SELECT received_count/total_participant_count,
                sent_count/total_participant_count,                
--                connected_from_count/total_participant_count,
                connected_from_avg, --11/11/2014
--                connected_to_count/total_participant_count,
                connected_avg, --11/11/2014
                login_activity_count/total_participant_count,score  INTO v_rec_recv_company_avg,v_rec_sent_company_avg,v_conn_from_company_avg,v_conn_to_company_avg,v_login_company_avg,v_score_company_avg  from engagement_score_summary d
      WHERE parent_node_id IS NULL
           AND d.time_frame = 'month' AND d.record_type ='nodesum'
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;
      
      EXCEPTION
      WHEN OTHERS THEN 
      v_score_company_avg :=0;
      p_out_return_code := '1';
       prc_execution_log_entry(c_process_name,1,'ERROR',SQLERRM,null);  --sql error fix 
       END;
       SELECT first_name,uea.email_addr,au.language_id INTO v_user_name,v_email_address,v_language_id FROM application_user au , user_email_address uea   --02/10/2015       
             WHERE au.user_id = p_in_user_id
             AND au.user_id = uea.user_id(+)
             AND uea.is_primary(+) = 1;     
      
    IF upper(p_in_mode) = upper('team') THEN
  
                SELECT LISTAGG(n.node_id,',') WITHIN GROUP(ORDER BY n.node_id),LISTAGG(n.name,'|') WITHIN GROUP(ORDER BY n.node_id)
            INTO v_user_node_list,v_node_name
        FROM user_node un, node n
        WHERE un.user_id = p_in_user_id
            AND role IN ('own','mgr')
            AND un.node_id = n.node_id;        
            
            SELECT SUM(received_count)/SUM(total_participant_count),
                SUM(sent_count)/SUM(total_participant_count),
--                SUM(connected_from_count)/SUM(total_participant_count),
ROUND(SUM(connected_from_avg * total_participant_count)/ SUM(total_participant_count)),
--                SUM(connected_to_count)/SUM(total_participant_count),
ROUND(SUM(connected_avg * total_participant_count)/ SUM(total_participant_count)),
                SUM(login_activity_count)/SUM(total_participant_count)  INTO v_rec_recv_team_avg,v_rec_sent_team_avg,v_conn_from_team_avg,v_conn_to_team_avg,v_login_team_avg  from engagement_score_summary d
      WHERE node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))
           AND d.time_frame = 'month' AND d.record_type ='teamsum'
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;      
         
          
 OPEN p_out_result_set1 FOR
        SELECT v_company_goal company_goal,v_is_score_active is_score_active,
        v_rec_recv_company_avg recv_company_avg,v_rec_sent_company_avg sent_company_avg,v_conn_from_company_avg conn_from_company_avg,v_conn_to_company_avg conn_to_company_avg,
        v_login_company_avg login_company_avg,
        v_rec_recv_team_avg recv_team_avg,v_rec_sent_team_avg sent_team_avg,v_conn_from_team_avg conn_from_team_avg,v_conn_to_team_avg conn_to_team_avg,
        v_login_team_avg login_team_avg,
        v_score_company_avg company_avg,v_user_name user_name,v_email_address email_address,v_language_id user_locale,v_node_name node_name, d.score ,d.received_count,d.sent_count,d.connected_to_count,  --02/10/2015
                                 d.connected_from_count,d.login_activity_count,
                                 d.received_target,d.sent_target,d.connected_target,
                                 d.connected_from_target,d.login_activity_target,
                                 d.score_achieved_count,d.total_participant_count
                                  FROM (
        SELECT  SUM (d.score * d.total_participant_count) / SUM(d.total_participant_count) score ,sum(d.received_count) received_count,sum(d.sent_count) sent_count,        
--        sum(d.connected_to_count) connected_to_count,
ROUND(SUM(connected_avg * total_participant_count)/ SUM(total_participant_count)) connected_to_count, --11/11/2014
--                                 sum(d.connected_from_count) connected_from_count,
ROUND(SUM(connected_from_avg * total_participant_count)/ SUM(total_participant_count)) connected_from_count,--11/11/2014
                                 sum(d.login_activity_count) login_activity_count,
                                 sum(d.received_target) received_target,sum(d.sent_target) sent_target,                                 
--                                 sum(d.connected_target) connected_target,
ROUND(SUM(connected_avg_target * total_participant_count)/ SUM(total_participant_count)) connected_target,                                 
--                                 sum(d.connected_from_target) connected_from_target,
ROUND(SUM(connected_from_avg_target * total_participant_count)/ SUM(total_participant_count)) connected_from_target,                                 
                                 sum(d.login_activity_target) login_activity_target,
                                 sum(d.score_achieved_count) score_achieved_count,                                
                                 sum(d.total_participant_count) total_participant_count
                          FROM engagement_score_summary d
                          WHERE d.time_frame = 'month' AND d.record_type ='nodesum' AND d.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))
                                AND trans_month = p_in_end_month AND trans_year = p_in_end_year
                                 )d;           
    
    ELSE  
    
     SELECT LISTAGG(n.node_id,',') WITHIN GROUP(ORDER BY n.node_id),LISTAGG(n.name,'|') WITHIN GROUP(ORDER BY n.node_id)
            INTO v_user_node_list,v_node_name
        FROM user_node un, node n
        WHERE un.user_id = p_in_user_id            
            AND un.node_id = n.node_id;     
            
            SELECT SUM(received_count)/SUM(total_participant_count),
                SUM(sent_count)/SUM(total_participant_count),
--                SUM(connected_from_count)/SUM(total_participant_count),
                ROUND(SUM(connected_from_avg * total_participant_count)/ SUM(total_participant_count)),
--                SUM(connected_to_count)/SUM(total_participant_count),
                ROUND(SUM(connected_avg * total_participant_count)/ SUM(total_participant_count)),
                SUM(login_activity_count)/SUM(total_participant_count) INTO v_rec_recv_team_avg,v_rec_sent_team_avg,v_conn_from_team_avg,v_conn_to_team_avg,v_login_team_avg  from engagement_score_summary d
      WHERE node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))
           AND d.time_frame = 'month' AND d.record_type ='teamsum'
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;  
                         
    OPEN p_out_result_set1 FOR
                                SELECT  v_company_goal company_goal,v_is_score_active is_score_active,
                                v_rec_recv_company_avg recv_company_avg,v_rec_sent_company_avg sent_company_avg,v_conn_from_company_avg conn_from_company_avg,v_conn_to_company_avg conn_to_company_avg,
        v_login_company_avg login_company_avg,
        v_rec_recv_team_avg recv_team_avg,v_rec_sent_team_avg sent_team_avg,v_conn_from_team_avg conn_from_team_avg,v_conn_to_team_avg conn_to_team_avg,
        v_login_team_avg login_team_avg,v_score_company_avg company_avg,v_user_name user_name,v_email_address email_address,v_language_id user_locale,v_node_name node_name,   --02/10/2015
        SUM(d.score) score,SUM(d.received_count) received_count,SUM(d.sent_count) sent_count,SUM(d.connected_to_count) connected_to_count ,SUM(d.connected_from_count) connected_from_count,
        d.login_activity_count,d.received_target,d.sent_target,d.connected_target,d.connected_from_target,
                                  d.login_activity_target,
                                   0 score_achieved_count,0 total_participant_count                              
                          FROM engagement_score_detail d 
                          WHERE time_frame = 'month' AND d.user_id = p_in_user_id                                
                               AND trans_month = p_in_end_month
                               AND trans_year = p_in_end_year
                               GROUP BY user_id,d.login_activity_count,d.received_target,d.sent_target,d.connected_target,d.connected_from_target,
                                  d.login_activity_target;    
            
            
    END IF;
    prc_execution_log_entry(c_process_name,1,'INFO','END',null); 
    
    p_out_return_code := '00';
EXCEPTION
WHEN OTHERS THEN 
p_out_return_code := '99';
    prc_execution_log_entry(c_process_name,1,'INFO',SQLERRM,null);  

END prc_engagement_notification;

PROCEDURE PRC_PARTICIPATION_EXTRACT (
p_file_name          IN     VARCHAR2,
p_header             IN     VARCHAR2, 
p_in_time_frame      IN     VARCHAR2,
p_in_user_id         IN     NUMBER,
p_in_node_id         IN     NUMBER,    
p_in_end_month       IN     NUMBER,
p_in_end_year        IN     NUMBER,
p_out_return_code    OUT    VARCHAR2,
p_out_result_set     OUT    SYS_REFCURSOR  
)
   IS
/*******************************************************************************
-- Purpose: 
-- This procedure returns the result set which will be used for 'participation Score Extract'
-- Person              Date        Comments
-- ---------           ----------  -----------------------------------------------------
-- KrishnaDeepika     08/26/2013    Initial Version         
-- Swati              09/18/2014    Bug 56692 - RPM - Team dashboard - Participation Score - Report Detail Extract - Department and Job Title is not printing in extract.
-- Ravi Dhanekula  09/18/2014    Bug 56693 - RPM - Team dashboard - Participation Score - Report Detail Extract - Country not printing in extract.  
-- KrishnaDeepika  09/29/2014    Bug 57002 - Report extract appears to be pulling all of the data for the entire organization
-- Ravi Dhanekula  09/30/2014    Bug # 57002 - Changed the extract to get the nodes from user_id in case if the node_id is NULL. And also changed the extract to pull data for 'Team and Below'.
--                          10/03/2014     Bug # 57146.
*******************************************************************************/
   c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_ENGAGEMENT_MANAGER_SCORE';

   -- constants
   c_delimiter  CONSTANT VARCHAR2(1) := ',' ;
   c2_delimiter CONSTANT VARCHAR2(1) := '"' ;
   -- local variables
   file_handler     UTL_FILE.file_type;
   v_extract        varchar2(4000);
   v_file_location  VARCHAR2(1000);
   directory_error  exception;  
   l_cursor         SYS_REFCURSOR;
   
   v_company_goal NUMBER;
   v_department_type varchar2(100char);
   v_position_type  varchar2(100char);
   v_country        number;
   v_user_node_list VARCHAR2(1000);

   BEGIN 
   
    prc_execution_log_entry('PRC_PARTICIPATION_EXTRACT',1,'INFO',
        p_file_name||': '||'p_file_name'||p_header||': '||'p_header'||p_in_time_frame ||': '||'p_in_time_frame '||p_in_user_id||': '||'p_in_user_id'||
        p_in_node_id||': '||'p_in_node_id'||p_in_end_month||': '||'p_in_end_month'||p_in_end_year||': '||'p_in_end_year'||CHR(10)||SQLERRM,null);  
    DELETE temp_table_session; --09/18/2014 Bug 56692

    INSERT INTO temp_table_session --09/18/2014 Bug 56692
     SELECT asset_code,cms_name,cms_code
         FROM vw_cms_code_value E
        WHERE asset_code in ('picklist.department.type.items','picklist.positiontype.items') 
    and e.locale = 'en_US';
    IF p_in_node_id IS NULL THEN
    SELECT LISTAGG(node_id,',') WITHIN GROUP(ORDER BY node_id)
            INTO v_user_node_list
        FROM user_node
        WHERE user_id = p_in_user_id
            AND role IN ('own','mgr');  
    END IF;
    BEGIN
      SELECT company_goal INTO v_company_goal FROM promo_engagement pe WHERE EXISTS 
      (SELECT * FROM promotion WHERE promotion_status = 'live' AND promotion_id = pe.promotion_id  );
      EXCEPTION
      WHEN OTHERS THEN 
       prc_execution_log_entry(c_process_name,1,'INFO',SQLERRM,null);  
      END;
          
     

   IF p_file_name IS NULL THEN 
          
 OPEN p_out_result_set FOR 
                             
     SELECT textline FROM (
        SELECT 1,p_header 
         Textline
        FROM dual
      UNION
           SELECT (ROWNUM+1),
                  c2_delimiter||Participant_first_Name     ||c2_delimiter||c_delimiter||
                  c2_delimiter||Participant_last_name      ||c2_delimiter||c_delimiter||
                  c2_delimiter||Org_Name                   ||c2_delimiter||c_delimiter||
                  c2_delimiter||Department_Name            ||c2_delimiter||c_delimiter||
                  c2_delimiter||Job_Title                  ||c2_delimiter||c_delimiter||
                  c2_delimiter||country                    ||c2_delimiter||c_delimiter||                     
                  c2_delimiter||Recognition_Sent           ||c2_delimiter||c_delimiter||
                  c2_delimiter||Recognition_Sent_Target    ||c2_delimiter||c_delimiter||
                  c2_delimiter||Recognition_Received       ||c2_delimiter||c_delimiter||
                  c2_delimiter||Recognition_Received_Target||c2_delimiter||c_delimiter||
                  c2_delimiter||Recognized                 ||c2_delimiter||c_delimiter||
                  c2_delimiter||Recognized_Target          ||c2_delimiter||c_delimiter||
                  c2_delimiter||Recognized_By              ||c2_delimiter||c_delimiter||
                  c2_delimiter||Recognized_By_Target       ||c2_delimiter||c_delimiter||
                  c2_delimiter||Login_Activity             ||c2_delimiter||c_delimiter||
                  c2_delimiter||Login_Activity_Target      ||c2_delimiter||c_delimiter||
                  c2_delimiter||Patcipation_Score          ||c2_delimiter||c_delimiter||
                  c2_delimiter||Patcipation_Score_Target   ||c2_delimiter
                         FROM  
                         (SELECT au.first_name  Participant_first_Name,
                                 au.last_name  Participant_last_Name,       
                            n.name AS Org_Name,           
                            (select cms_name from temp_table_session where asset_code='picklist.department.type.items' and cms_code=pe.department_type) AS department_name,
                            (select cms_name from temp_table_session where asset_code='picklist.positiontype.items' and cms_code=pe.position_type)  AS Job_Title,
                            UPPER(c.country_code)  AS country, --56693
                            esd.sent_count AS Recognition_Sent,
                            esd.sent_target AS Recognition_Sent_Target,
                            esd.received_count AS Recognition_Received,
                            esd.received_target AS Recognition_Received_Target,
                            esd.connected_to_count AS Recognized,
                            esd.connected_target AS Recognized_Target,
                            esd.connected_from_count AS Recognized_By,
                            esd.connected_from_target AS Recognized_By_Target,
                            esd.Login_Activity_count AS Login_Activity,
                            esd.login_activity_target,
                            esd.score AS Patcipation_Score ,
                            v_company_goal AS Patcipation_Score_Target
                   FROM application_user au,
                   engagement_score_detail esd,
                   node n,
                   user_node un,
                   user_address ua,
                   country c,
                   vw_curr_pax_employer pe 
          WHERE 
(
 (p_in_node_id IS NULL AND un.node_id IN ( SELECT node_id
FROM node CONNECT BY PRIOR node_id = parent_node_id START WITH node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))))
OR
(p_in_node_id IS NOT NULL AND un.node_id IN ( SELECT node_id
FROM node CONNECT BY PRIOR node_id = parent_node_id START WITH node_id IN (SELECT * FROM TABLE(get_array(p_in_node_id))))))
            AND un.user_id = au.user_id
            AND au.user_id = esd.user_id
            AND ua.user_id=au.user_id
            AND ua.country_id=c.country_id
            AND c.status='active'
            AND ua.is_primary = 1
            AND un.node_id = esd.node_id
            AND un.node_id = n.node_id
            AND au.user_id = pe.user_id(+)
            AND esd.time_frame = p_in_time_frame
            AND trans_month = p_in_end_month
            AND trans_year = p_in_end_year        
             )                             
   );    
p_out_return_code :=  '00' ; 

ELSE

p_out_return_code :=  '00' ;
OPEN p_out_result_set FOR SELECT NULL FROM dual  ;

OPEN l_cursor FOR
  SELECT textline FROM (
        SELECT 1,p_header 
         Textline
        FROM dual
      UNION
           SELECT (ROWNUM+1),
                  c2_delimiter||Participant_first_Name     ||c2_delimiter||c_delimiter||
                  c2_delimiter||Participant_last_Name      ||c2_delimiter||c_delimiter||
                  c2_delimiter||Org_Name                   ||c2_delimiter||c_delimiter||
                  c2_delimiter||Department_Name            ||c2_delimiter||c_delimiter||
                  c2_delimiter||Job_Title                  ||c2_delimiter||c_delimiter||
                  c2_delimiter||country                    ||c2_delimiter||c_delimiter||                     
                  c2_delimiter||Recognition_Sent           ||c2_delimiter||c_delimiter||
                  c2_delimiter||Recognition_Sent_Target    ||c2_delimiter||c_delimiter||
                  c2_delimiter||Recognition_Received       ||c2_delimiter||c_delimiter||
                  c2_delimiter||Recognition_Received_Target||c2_delimiter||c_delimiter||
                  c2_delimiter||Recognized                 ||c2_delimiter||c_delimiter||
                  c2_delimiter||Recognized_Target          ||c2_delimiter||c_delimiter||
                  c2_delimiter||Recognized_By              ||c2_delimiter||c_delimiter||
                  c2_delimiter||Recognized_By_Target       ||c2_delimiter||c_delimiter||
                  c2_delimiter||Login_Activity             ||c2_delimiter||c_delimiter||
                  c2_delimiter||Login_Activity_Target      ||c2_delimiter||c_delimiter||
                  c2_delimiter||Patcipation_Score          ||c2_delimiter||c_delimiter||
                  c2_delimiter||Patcipation_Score_Target   ||c2_delimiter
                         FROM  
                         (SELECT au.first_name  Participant_first_Name,
                                 au.last_name  Participant_last_Name,       
                            n.name AS Org_Name,           
                            (select cms_name from temp_table_session where asset_code='picklist.department.type.items' and cms_code=pe.department_type) AS department_name,
                            (select cms_name from temp_table_session where asset_code='picklist.positiontype.items' and cms_code=pe.position_type)  AS Job_Title,
                            UPPER(c.country_code) AS country, --56693
                            esd.sent_count AS Recognition_Sent,
                            esd.sent_target AS Recognition_Sent_Target,
                            esd.received_count AS Recognition_Received,
                            esd.received_target AS Recognition_Received_Target,
                            esd.connected_to_count AS Recognized,
                            esd.connected_target AS Recognized_Target,
                            esd.connected_from_count AS Recognized_By,
                            esd.connected_from_target AS Recognized_By_Target,
                            esd.Login_Activity_count AS Login_Activity,
                            esd.login_activity_target,
                            esd.score AS Patcipation_Score ,
                            v_company_goal AS Patcipation_Score_Target 
                   FROM application_user au,
                   engagement_score_detail esd,
                   node n,
                   user_node un,
                   user_address ua,
                   country c,
                   vw_curr_pax_employer pe  
                   WHERE
(
 (p_in_node_id IS NULL AND un.node_id IN ( SELECT node_id
FROM node CONNECT BY PRIOR node_id = parent_node_id START WITH node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))))
OR
(p_in_node_id IS NOT NULL AND un.node_id IN ( SELECT node_id
FROM node CONNECT BY PRIOR node_id = parent_node_id START WITH node_id IN (SELECT * FROM TABLE(get_array(p_in_node_id))))))
            AND un.user_id = au.user_id
            AND au.user_id = esd.user_id
            AND ua.user_id=au.user_id
            AND ua.country_id=c.country_id
            AND c.status='active'
            AND ua.is_primary = 1
            AND un.node_id = esd.node_id
            AND un.node_id = n.node_id
            AND au.user_id = pe.user_id(+)
            AND esd.time_frame = p_in_time_frame
            AND trans_month = p_in_end_month
            AND trans_year = p_in_end_year
                              )
    );    
BEGIN
       SELECT directory_name
         INTO v_file_location        
         FROM all_directories
        WHERE owner = 'SYS'
          AND directory_path LIKE '%/work/wip/%reports'
          AND ROWNUM = 1;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE directory_error;
    END;    
 file_handler := UTL_FILE.fopen(v_file_location, p_file_name, 'w',4096);

LOOP  
     fetch l_cursor into  v_extract;
     exit when l_cursor%notfound;
           UTL_FILE.put_line(file_handler, v_extract);           
      
END LOOP;

    UTL_FILE.fclose(file_handler);
       
 END IF;
EXCEPTION
    WHEN OTHERS  THEN
        IF utl_file.is_open(file_handler) THEN 
          UTL_FILE.fclose(file_handler);
        END IF;
        p_out_return_code :=  '99' ;
        prc_execution_log_entry('PRC_PARTICIPATION_EXTRACT',1,'ERROR',
        p_file_name||': '||'p_file_name'||p_header||': '||'p_header'||p_in_time_frame ||': '||'p_in_time_frame '||p_in_user_id||': '||'p_in_user_id'||
        p_in_node_id||': '||'p_in_node_id'||p_in_end_month||': '||'p_in_end_month'||p_in_end_year||': '||'p_in_end_year'||CHR(10)||SQLERRM,null);  
        OPEN p_out_result_set FOR SELECT NULL FROM dual  ;
END PRC_PARTICIPATION_EXTRACT;

END;
/
