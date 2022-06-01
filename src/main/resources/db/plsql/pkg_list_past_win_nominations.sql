CREATE OR REPLACE PACKAGE pkg_list_past_win_nominations IS

/******************************************************************************
  NAME:       pkg_list_past_win_nominations
  PURPOSE:    List out the data of past won nomination for the approver

   Person             Date               Comments
  ----------         -------------      ------------------------------------------------
  nagarajs           05/31/2016         Initial creation                    
******************************************************************************/

PROCEDURE prc_past_winner_enable_tile 
         ( p_in_logged_in_user_id    IN  NUMBER,
           p_out_returncode          OUT NUMBER,  
           p_out_all_elig_winner_flg OUT NUMBER,
           p_out_my_elig_winner_flg  OUT NUMBER);
           
PROCEDURE prc_past_winner_elig_promolist
         ( p_in_logged_in_user_id    IN  NUMBER,
           p_out_returncode          OUT NUMBER,  
           p_out_promo_list          OUT SYS_REFCURSOR);

PROCEDURE prc_past_winner_summary
         ( p_in_logged_in_user_id   IN  NUMBER,
           p_in_promotion_id        IN  NUMBER,
           p_in_lastname            IN  VARCHAR2,
           p_in_firstname           IN  VARCHAR2,
           p_in_country             IN  VARCHAR2,
           p_in_department          IN  VARCHAR2,
           p_in_teamname            IN  VARCHAR2,
           p_in_startdate           IN  DATE,
           p_in_enddate             IN  DATE,
           p_in_locale              IN  VARCHAR2,
           p_in_rowNumStart         IN  NUMBER,
           p_in_rowNumEnd           IN  NUMBER,
           p_in_sortedBy            IN  VARCHAR2,
           p_in_sortedOn            IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,
           p_out_nominee_dtl_res    OUT SYS_REFCURSOR,
           p_out_team_memb_res      OUT SYS_REFCURSOR,
           p_out_nominator_res      OUT SYS_REFCURSOR,
           p_out_custom_dtl_res     OUT SYS_REFCURSOR,
           p_out_activity_list     OUT SYS_REFCURSOR);

PROCEDURE prc_past_winner_detail
         ( p_in_winner_user_id      IN  NUMBER,         
           p_in_team_id             IN  NUMBER,
           p_in_logged_in_user_id   IN  NUMBER,
           p_in_activity_id         IN  NUMBER,
           p_in_time_period_id      IN  NUMBER,
           p_in_locale              IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,
           p_out_nominee_dtl_res    OUT SYS_REFCURSOR,
           p_out_team_memb_dtl_res  OUT SYS_REFCURSOR,
           p_out_behavior_dtl_res   OUT SYS_REFCURSOR,
           p_out_custom_dtl_res     OUT SYS_REFCURSOR,
           p_out_nominator_dtl_res      OUT SYS_REFCURSOR);           
END;
/
CREATE OR REPLACE PACKAGE BODY pkg_list_past_win_nominations IS

/******************************************************************************
  NAME:       pkg_list_past_win_nominations
  PURPOSE:    List out the data of past won nomination for the approver

   Person             Date               Comments
  ----------         -------------      ------------------------------------------------
  nagarajs           05/31/2016         Initial creation                    
******************************************************************************/

PROCEDURE prc_past_winner_enable_tile 
         ( p_in_logged_in_user_id    IN  NUMBER,
           p_out_returncode          OUT NUMBER,  
           p_out_all_elig_winner_flg OUT NUMBER,
           p_out_my_elig_winner_flg  OUT NUMBER)
AS
/*******************************************************************************
Purpose: To return the flags to decide the past winner tile

  Person             Date         Comments
  -----------       --------      -----------------------------------------------------
  nagarajs          06/02/2016    Initial Creation   
  Ravi Dhanekula    12/12/2016   G6-1120 This procedure is used to check the winner only count for 'my' flag.  
  nagarajs          02/02/2017   Bug 71183 - Nomination winner tile is displaying if PAX won the recognition   
  chidamba          03/07/2018   G6-3904 - lookup v_all_eligible_winner consider only when view_past_winners flage is TRUE
  Gorantla          07/10/2018   GitLab 1210/Bug 76558 - Nomination Past Winners do not display if promotion has end date
*******************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_PAST_WINNER_ENABLE_TILE';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';
   
   v_stage                  VARCHAR2(500);
   v_all_eligible_winner    NUMBER(10);
   v_my_eligible_winner     NUMBER(10);
BEGIN

  p_out_returncode := 0;
  
  v_stage := 'Check winners in eligible promotions';
  SELECT COUNT(1)
    INTO v_all_eligible_winner
    FROM claim c,
         claim_item ci,
         claim_item_approver cia
   WHERE EXISTS ( SELECT 1
                      FROM promotion p,
                           promo_nomination pn
                     WHERE p.promotion_type = 'nomination'
                       AND (p.primary_audience_type = 'allactivepaxaudience' OR p.secondary_audience_type = 'allactivepaxaudience')
                       AND p.promotion_id = c.promotion_id
                       AND p.promotion_id = pn.promotion_id
                       --AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE --07/10/2018
                       AND p.promotion_status = 'live'  --07/10/2018
                       AND pn.view_past_winners = 1
                     UNION
                    SELECT 1
                      FROM participant_audience pa,
                           promo_audience a,
                           promotion p,
                           promo_nomination pn
                     WHERE pa.user_id = p_in_logged_in_user_id
                       AND pa.audience_id = a.audience_id
                       AND a.promotion_id = p.promotion_id
                       AND p.promotion_type = 'nomination'
                       AND p.promotion_id = c.promotion_id
                       AND p.promotion_id = pn.promotion_id
                       AND pn.view_past_winners = 1
                       --AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE --07/10/2018
                       AND p.promotion_status = 'live'  --07/10/2018
                  )
    AND c.claim_id = ci.claim_id
    AND (ci.claim_item_id = cia.claim_item_id OR c.claim_group_id = cia.claim_group_id)
    AND NVL(cia.notification_date,TRUNC(SYSDATE)) <= TRUNC(SYSDATE) 
    AND cia.approval_status_type IN ('approv','winner');
  
  IF v_all_eligible_winner > 0 THEN
    p_out_all_elig_winner_flg := 1;
  ELSE
    p_out_all_elig_winner_flg := 0;
  END IF;
  
  
  v_stage := 'Check winners for the logged in user';
  SELECT COUNT(1)
    INTO v_my_eligible_winner
    FROM claim c,
         claim_item ci,
         claim_item_approver cia,
         claim_recipient cr,
         promotion p,
         promo_nomination pn       --03/07/2018
   WHERE c.claim_id = ci.claim_id
     AND (ci.claim_item_id = cia.claim_item_id OR c.claim_group_id = cia.claim_group_id)
     AND cia.approval_status_type IN ('winner')
     AND NVL(cia.notification_date,TRUNC(SYSDATE)) <= TRUNC(SYSDATE) 
     AND ci.claim_item_id = cr.claim_item_id
     AND cr.participant_id = p_in_logged_in_user_id
     AND c.promotion_id = p.promotion_id
     AND p.promotion_type = 'nomination'  --02/02/2017
     --AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE --07/10/2018
     AND p.promotion_status = 'live' --07/10/2018
     AND p.promotion_id = pn.promotion_id  --03/07/2018
     AND pn.view_past_winners = 1;         --03/07/2018
  
  IF v_my_eligible_winner > 0 THEN
    p_out_my_elig_winner_flg := 1;
  ELSE
    p_out_my_elig_winner_flg := 0;
  END IF;
  
  
EXCEPTION 
  WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Parms : p_in_logged_in_user_id: '||p_in_logged_in_user_id
      ||' ~ Error at stage :'||v_stage||'-->'||SQLERRM, NULL);
      p_out_returncode := 99;
      p_out_all_elig_winner_flg := 0;
      p_out_my_elig_winner_flg := 0;
END prc_past_winner_enable_tile;

PROCEDURE prc_past_winner_elig_promolist
         ( p_in_logged_in_user_id    IN  NUMBER,
           p_out_returncode          OUT NUMBER,  
           p_out_promo_list          OUT SYS_REFCURSOR)
AS
/*******************************************************************************
Purpose: To return the all the promotions if the logged in user is audience of that promotion

  Person             Date         Comments
  -----------       --------      -----------------------------------------------------
  nagarajs          06/02/2016    Initial Creation                              
  Gorantla          07/10/2018    GitLab 1210/Bug 76558 - Nomination Past Winners do not display if promotion has end date
*******************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_PAST_WINNER_ELIG_PROMOLIST';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';
   
   v_stage                  VARCHAR2(500);
BEGIN

  p_out_returncode := 0;
  
  OPEN p_out_promo_list FOR
    SELECT p.promotion_id, p.promotion_name 
      FROM promotion p
     WHERE p.promotion_type = 'nomination'
       --AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE --07/10/2018
       AND p.promotion_status = 'live' --07/10/2018
       AND (p.primary_audience_type = 'allactivepaxaudience' OR p.secondary_audience_type = 'allactivepaxaudience')
       AND EXISTS (SELECT 1
                     FROM claim c,
                          claim_item ci,
                          claim_item_approver cia
                    WHERE c.claim_id = ci.claim_id
                      AND (ci.claim_item_id = cia.claim_item_id OR c.claim_group_id = cia.claim_group_id)
                      AND cia.approval_status_type IN ('approv','winner')
                      AND NVL(cia.notification_date,TRUNC(SYSDATE)) <= TRUNC(SYSDATE) 
                      AND c.promotion_id = p.promotion_id
                   )
     UNION
    SELECT p.promotion_id, p.promotion_name 
      FROM participant_audience pa,
           promo_audience a,
           promotion p
     WHERE pa.user_id = p_in_logged_in_user_id
       AND pa.audience_id = a.audience_id
       AND a.promotion_id = p.promotion_id
       AND p.promotion_type = 'nomination'
       --AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE --07/10/2018
       AND p.promotion_status = 'live'  --07/10/2018
       AND EXISTS (SELECT 1
                     FROM claim c,
                          claim_item ci,
                          claim_item_approver cia
                    WHERE c.claim_id = ci.claim_id
                      AND (ci.claim_item_id = cia.claim_item_id OR c.claim_group_id = cia.claim_group_id)
                      AND cia.approval_status_type IN ('approv','winner')
                      AND NVL(cia.notification_date,TRUNC(SYSDATE)) <= TRUNC(SYSDATE) 
                      AND c.promotion_id = p.promotion_id
                   );
    
  
EXCEPTION 
  WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Parms : p_in_logged_in_user_id: '||p_in_logged_in_user_id
      ||' ~ Error at stage :'||v_stage||'-->'||SQLERRM, NULL);
      p_out_returncode := 99;
      OPEN p_out_promo_list FOR SELECT NULL FROM DUAL;
END prc_past_winner_elig_promolist;

PROCEDURE prc_past_winner_summary
         ( p_in_logged_in_user_id   IN  NUMBER,
           p_in_promotion_id        IN  NUMBER,
           p_in_lastname            IN  VARCHAR2,
           p_in_firstname           IN  VARCHAR2,
           p_in_country             IN  VARCHAR2,
           p_in_department          IN  VARCHAR2,
           p_in_teamname            IN  VARCHAR2,
           p_in_startdate           IN  DATE,
           p_in_enddate             IN  DATE,
           p_in_locale              IN  VARCHAR2,
           p_in_rowNumStart         IN  NUMBER,
           p_in_rowNumEnd           IN  NUMBER,
           p_in_sortedBy            IN  VARCHAR2,
           p_in_sortedOn            IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,
           p_out_nominee_dtl_res    OUT SYS_REFCURSOR,
           p_out_team_memb_res      OUT SYS_REFCURSOR,
           p_out_nominator_res      OUT SYS_REFCURSOR,
           p_out_custom_dtl_res     OUT SYS_REFCURSOR,
           p_out_activity_list     OUT SYS_REFCURSOR)
AS
/*******************************************************************************
  -- Purpose: To return the winner nominations summary result in all eligible promotions of the approvers
  --
  -- Person             Date         Comments
  -- -----------        --------    -----------------------------------------------------
  -- nagarajs          05/31/2016    Initial Creation
  --Sherif Basha      12/27/2016    Bug 70595 - Same team name pax is getting displayed different line in winner details page.
                                  (Requirement to group the Team nomination list of records into one)                     
  --Gorantla          07/10/2018    GitLab 1210/Bug 76558 - Nomination Past Winners do not display if promotion has end date
  --Gorantla          06/26/2019    Bug 77739 - Nomination with the comments of 4000 characters length is not loading on nomination approval page. 
  *******************************************************************************/
   c_process_name       CONSTANT execution_log.process_name%type :='PRC_PAST_WINNER_SUMMARY';
   c_release_level      CONSTANT execution_log.release_level%type := '1.0';
   
   v_stage              VARCHAR2(500);
   v_text               VARCHAR2(4000);
   v_default_locale     os_propertyset.string_val%TYPE;

BEGIN
   
  p_out_returncode := 0;
  
  BEGIN
    SELECT string_val 
      INTO v_default_locale
      FROM os_propertyset
     WHERE entity_name = 'default.language';
  EXCEPTION
    WHEN OTHERS THEN
      v_default_locale := 'en_US';
  END;
  
  DELETE temp_table_session;
  
  v_stage:= 'Insert temp_table_session';
  INSERT INTO temp_table_session
    SELECT asset_code,cms_value,key
      FROM ( SELECT asset_code,cms_value,key ,  RANK() OVER(PARTITION BY asset_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key IN ('COUNTRY_NAME','PROMOTION_LEVEL_LABEL_NAME_' , 'PROMOTION_TIME_PERIOD_NAME_')
                AND locale IN (v_default_locale, p_in_locale)
              UNION ALL
             SELECT asset_code,cms_value,key ,  RANK() OVER(PARTITION BY asset_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value
              WHERE asset_code IN (SELECT promo_name_asset_code FROM promotion WHERE promotion_type = 'nomination')
                AND locale IN (v_default_locale, p_in_locale)
              UNION ALL
             SELECT asset_code,cms_name,cms_code,RANK() OVER(PARTITION BY asset_code, cms_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn 
               FROM vw_cms_code_value 
              WHERE asset_code IN ('picklist.positiontype.items','picklist.department.type.items')
                AND locale IN (v_default_locale, p_in_locale)
                AND cms_status = 'true'
              UNION ALL
              SELECT asset_code,cms_value,key ,RANK() OVER(PARTITION BY key ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key IN (SELECT 'ELEMENTLABEL_'||cm_key_fragment from claim_form_step_element )
                AND locale IN (v_default_locale, p_in_locale)
              UNION ALL
             SELECT cf.cm_asset_code,cms_name, cms_code||'_'||cfse.cm_key_fragment,RANK() OVER(PARTITION BY cf.cm_asset_code,cms_code||'_'||cfse.cm_key_fragment ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_code_value  vw, claim_form_step_element cfse, 
                    claim_form_step cfs, claim_form cf, promotion p
              WHERE asset_code =  cfse.selection_pick_list_name 
                AND cfse.claim_form_step_id = cfs.claim_form_step_id 
                AND cfs.claim_form_id = cf.claim_form_id
                and cf.claim_form_id = p.claim_form_id
                AND p.promotion_id = p_in_promotion_id
                AND locale IN (v_default_locale, p_in_locale)
                AND cms_status = 'true' 
                AND claim_form_element_type_code <> 'copy'
              UNION ALL
             SELECT asset_code,cms_value,key ,RANK() OVER(PARTITION BY key,cms_value ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key IN (SELECT 'LABELTRUE_'||cm_key_fragment from claim_form_step_element 
                            UNION SELECT 'LABELFALSE_'||cm_key_fragment from claim_form_step_element )
                AND locale IN (v_default_locale, p_in_locale)
            ) WHERE rn = 1;

  DELETE TMP_WINNER_NOMINATION_SUMMARY;
      
  v_stage:= 'Insert TMP_WINNER_NOMINATION_SUMMARY';
  INSERT INTO TMP_WINNER_NOMINATION_SUMMARY
    (activity_id,            
    claim_id,               
    nominee_first_name,     
    nominee_last_name,      
    nominee_user_id,        
    time_period_id,         
    time_period_name,       
    nominee_hierarchy_id,   
    nominee_org_name,       
    nominee_job_position,   
    nominee_country_code,   
    nominee_country_name,   
    nominee_avatar_url,     
    nominator_first_name,   
    nominator_last_name,    
    nominator_user_id,     
    nominator_hierarchy_id, 
    nominator_org_name,     
    nominator_job_position, 
    nominator_country_code, 
    nominator_country_name, 
    submitter_comments,     
    promotion_name,         
    team_id,               
    team_name,              
    level_id,               
    level_name,             
    promotion_id,           
    date_approved,          
    nominator_department,   
    nominee_department,
    claim_group_id     
    )
    SELECT activity_id,
           claim_id,
           nominee_first_name,
           nominee_last_name,
           nominee_user_id,
           pnt.nomination_time_period_id,
           (SELECT cms_name FROM temp_table_session WHERE asset_code = pnt.time_period_name_asset_code) time_period_name,
           nominee_hierarchy_id,
           nominee_org_name,
           nominee_job_position,
           nominee_country_code,
           nominee_country_name,
           nominee_avatar_url,
           nominator_first_name,
           nominator_last_name,
           nominator_user_id,
           nominator_hierarchy_id,
           nominator_org_name,
           nominator_job_position,
           nominator_country_code,
           nominator_country_name,
           submitter_comments,
           promotion_name,
           team_id,
           team_name,
           level_id,
           level_name,
           wi.promotion_id,
           date_approved,
           nominator_department,
           nominee_department,
           claim_group_id
      FROM (SELECT a.activity_id,
                   c.claim_id,
                   au_rec.first_name nominee_first_name,
                   au_rec.last_name nominee_last_name,
                   cr.participant_id nominee_user_id,
                   cia.time_period_id,
                   n_rec.hierarchy_id nominee_hierarchy_id,
                   n_rec.name nominee_org_name,
                   (select cms_name from temp_table_session where asset_code ='picklist.positiontype.items' and cms_code = vw_rec.position_type) nominee_job_position,
                   cu_rec.country_code nominee_country_code,
                   (select cms_name from temp_table_session where asset_code = cu_rec.cm_asset_code ) nominee_country_name,
                   pa_rec.avatar_small nominee_avatar_url,
                   au_giv.first_name nominator_first_name,
                   au_giv.last_name nominator_last_name,
                   c.submitter_id nominator_user_id,
                   n_giv.hierarchy_id nominator_hierarchy_id,
                   n_giv.name nominator_org_name,
                   (select cms_name from temp_table_session where asset_code='picklist.positiontype.items' and cms_code = vw_giv.position_type)  nominator_job_position,
                   cu_giv.country_code nominator_country_code,
                   (select cms_name from temp_table_session where asset_code = cu_giv.cm_asset_code ) nominator_country_name,
                   nc.submitter_comments,
                   (select cms_name from temp_table_session where asset_code = p.promo_name_asset_code) as promotion_name,
                   CASE WHEN team_name is NULL THEN NULL ELSE nc.team_id END team_id,
                   nc.team_name,
                   a.approval_round level_id,
                   (SELECT cms_name FROM temp_table_session WHERE asset_code = pnl.level_label_asset_code) level_name,
                   p.promotion_id,
                   NVL(cia.notification_date,cia.date_approved)  date_approved,
                   (select cms_name from temp_table_session where asset_code='picklist.department.type.items' and cms_code = vw_giv.department_type)  nominator_department,
                   (select cms_name from temp_table_session where asset_code='picklist.department.type.items' and cms_code = vw_rec.department_type)  nominee_department,
                   c.claim_group_id
                    FROM claim c,
                  claim_item ci,
                  claim_recipient cr,
                  claim_item_approver cia,
                  nomination_claim nc,
                  activity a,
                  promo_nomination_level pnl,
                  promo_nomination pn,
                  promotion p,
                  node n_rec,
                  application_user au_rec,
                  participant pa_rec,
                  user_address ua_rec,
                  country cu_rec,
                  vw_curr_pax_employer vw_rec,
                  node n_giv,
                  application_user au_giv,
                  participant pa_giv,
                  user_address ua_giv,
                  country cu_giv,
                  vw_curr_pax_employer vw_giv
            WHERE c.promotion_id = NVL(p_in_promotion_id,c.promotion_id)
              AND c.claim_id = ci.claim_id
              AND (ci.claim_item_id = cia.claim_item_id OR c.claim_group_id = cia.claim_group_id)
              AND cia.approval_status_type IN ('approv','winner')
              AND ci.claim_item_id = cr.claim_item_id
              AND cr.node_id = n_rec.node_id
              AND c.node_id = n_giv.node_id
              AND c.claim_id = nc.claim_id
              AND (c.claim_id = a.claim_id OR c.claim_group_id = a.claim_group_id)
              AND a.is_submitter = 0
              AND a.promotion_id = pnl.promotion_id(+)
              AND a.approval_round = cia.approval_round
              AND a.user_id = cr.participant_id
              AND pn.promotion_id = a.promotion_id
              AND ((a.approval_round = pnl.level_index  AND pn.payout_level_type = 'eachLevel')
                  OR (pn.payout_level_type = 'finalLevel' ) )
              AND c.promotion_id = p.promotion_id
              --AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE --07/10/2018
              AND p.promotion_status = 'live'  --07/10/2018
              AND NVL(cia.notification_date,TRUNC(SYSDATE)) <= TRUNC(SYSDATE) 
              AND TRUNC(NVL(cia.notification_date,cia.date_approved)) BETWEEN NVL(p_in_startdate,TRUNC(SYSDATE-30)) AND NVL(p_in_enddate,TRUNC(SYSDATE))
              AND EXISTS ( SELECT 1
                              FROM promotion p
                             WHERE p.promotion_id = NVL(p_in_promotion_id,p.promotion_id)
                               AND p.promotion_type = 'nomination'
                               AND (p.primary_audience_type = 'allactivepaxaudience' OR p.secondary_audience_type = 'allactivepaxaudience')
                               AND p.promotion_id = c.promotion_id
                             UNION
                            SELECT 1
                              FROM participant_audience pa,
                                   promo_audience a,
                                   promotion p
                             WHERE pa.user_id = p_in_logged_in_user_id
                               AND pa.audience_id = a.audience_id
                               AND a.promotion_id = p.promotion_id
                               AND p.promotion_id = NVL(p_in_promotion_id,p.promotion_id)
                               AND p.promotion_type = 'nomination'
                               AND p.promotion_id = c.promotion_id
                          )
              AND cr.participant_id = au_rec.user_id 
              AND au_rec.user_id = pa_rec.user_id
              AND au_rec.user_id = ua_rec.user_id (+)
              AND ua_rec.is_primary (+) = 1
              AND ua_rec.country_id = cu_rec.country_id (+)
              AND au_rec.user_id = vw_rec.user_id (+)
              AND c.submitter_id = au_giv.user_id 
              AND au_giv.user_id = pa_giv.user_id
              AND au_giv.user_id = ua_giv.user_id (+)
              AND ua_giv.is_primary (+) = 1
              AND ua_giv.country_id = cu_giv.country_id (+)
              AND au_giv.user_id = vw_giv.user_id (+)
              AND (  (au_rec.last_name = p_in_lastname OR au_rec.first_name = p_in_firstname OR vw_rec.department_type = p_in_department 
                      OR cu_rec.country_code = p_in_country OR nc.team_name = p_in_teamname) 
                   OR (p_in_lastname IS NULL AND p_in_firstname IS NULL AND p_in_department IS NULL AND p_in_country IS NULL AND p_in_teamname IS NULL))
                   ) wi,
            promo_nomination_time_period pnt
      WHERE wi.promotion_id = pnt.promotion_id(+)
        AND wi.time_period_id = pnt.nomination_time_period_id(+);
      
  v_stage := 'Return nominee detail';
  OPEN p_out_nominee_dtl_res FOR
    SELECT DECODE(team_id, NULL,activity_id) activity_id, --12/27/2016    Bug 70595
           claim_id,
           DECODE(team_id, NULL,0,1) team_nomination,
           DECODE(team_id, NULL,nominee_first_name) nominee_first_name,
           DECODE(team_id, NULL,nominee_last_name) nominee_last_name,
           DECODE(team_id, NULL,nominee_user_id) nominee_user_id,
           time_period_id,
           time_period_name,
           DECODE(team_id, NULL,nominee_hierarchy_id) nominee_hierarchy_id ,
           DECODE(team_id, NULL,nominee_org_name) nominee_hierarchy_name,
           DECODE(team_id, NULL,nominee_job_position) nominee_position,
           DECODE(team_id, NULL,nominee_country_code) nominee_country_code,
           DECODE(team_id, NULL,nominee_country_name) nominee_country_name,
           DECODE(team_id, NULL,nominee_avatar_url) nominee_avatar,
           promotion_name,
           team_id,
           team_name,
           level_name,
           level_id,
           promotion_id,
           nominee_department,
           claim_group_id
      FROM (SELECT s.*
              FROM (SELECT ROW_NUMBER() OVER 
                           (ORDER BY
                             -- sort on field ascending
                             (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'first_name')      THEN nominee_first_name||' '||nominee_last_name   END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'last_name')       THEN nominee_last_name||' '||nominee_first_name   END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'date_approved')    THEN date_approved           END) ASC
                             -- sort on field descending
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'first_name')      THEN nominee_first_name||' '||nominee_last_name  END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'last_name')       THEN nominee_last_name||' '||nominee_first_name  END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'date_approved')    THEN date_approved          END) DESC
                           ) AS rn, 
                           res.*
                      FROM (SELECT * FROM (select res.*,ROW_NUMBER ()       --12/27/2016    Bug 70595 replaced rank() to restrict to one record only for a team nomi
                                                                     OVER (
                                                                        PARTITION BY NVL(to_char(claim_group_id),NVL(team_name,to_char(activity_id)))
                                                                        ORDER BY claim_id desc)
                                                                        AS rec_rank from tmp_winner_nomination_summary res ) WHERE rec_rank = 1) res
                  ) s -- sort number result set
            WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
              AND s.rn < TO_NUMBER(p_in_rowNumEnd)
           );
  
  v_stage := 'Return team members detail';    
  OPEN p_out_team_memb_res FOR
    SELECT activity_id,
           nominee_user_id,
           nominee_first_name,
           nominee_last_name,
           team_id,         --12/27/2016    Bug 70595
           team_name        --12/27/2016    Bug 70595
      FROM (SELECT s.*
              FROM (SELECT ROW_NUMBER() OVER 
                           (ORDER BY
                             -- sort on field ascending
                             (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_firstname IS NOT NULL)      THEN nominee_first_name||' '||nominee_last_name   END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_lastname IS NOT NULL)       THEN nominee_last_name||' '||nominee_first_name   END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND (p_in_firstname IS NULL OR p_in_lastname IS NULL))    THEN date_approved           END) ASC
                             -- sort on field descending
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_firstname IS NOT NULL)      THEN nominee_first_name||' '||nominee_last_name  END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_lastname IS NOT NULL)       THEN nominee_last_name||' '||nominee_first_name  END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND (p_in_firstname IS NULL OR p_in_lastname IS NULL))    THEN date_approved          END) DESC
                           ) AS rn, 
                           res.*
                      FROM tmp_winner_nomination_summary res
                    
                  ) s -- sort number result set
            WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
              AND s.rn < TO_NUMBER(p_in_rowNumEnd)
           )
     WHERE team_id IS NOT NULL;
     
  v_stage := 'Return nominator detail';
  OPEN p_out_nominator_res FOR
  SELECT DECODE(team_name, NULL,activity_id) activity_id,       --12/27/2016    Bug 70595
           nominator_first_name,
           nominator_last_name,
           nominator_user_id,
           nominator_hierarchy_id ,
           nominator_org_name,
           nominator_job_position,
           nominator_country_code,
           nominator_country_name,
           submitter_comments AS comments,
           nominator_department,
           time_period_id,
           time_period_name,
           claim_group_id,
           team_name,  --12/27/2016    Bug 70595
           team_id    --12/27/2016    Bug 70595
            FROM tmp_winner_nomination_summary WHERE activity_id in (
      SELECT s.activity_id
              FROM (SELECT ROW_NUMBER() OVER 
                           (ORDER BY
                             -- sort on field ascending
                             (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'first_name')      THEN nominee_first_name||' '||nominee_last_name   END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'last_name')       THEN nominee_last_name||' '||nominee_first_name   END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'date_approved')    THEN date_approved           END) ASC
                             -- sort on field descending
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'first_name')      THEN nominee_first_name||' '||nominee_last_name  END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'last_name')       THEN nominee_last_name||' '||nominee_first_name  END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'date_approved')    THEN date_approved          END) DESC
                           ) AS rn, 
                           res.*
                      FROM (SELECT * FROM (select res.*,ROW_NUMBER()            --12/27/2016    Bug 70595 replaced rank() to restrict to one record only for a team nomi
                                                                     OVER (
                                                                        PARTITION BY NVL(to_char(claim_group_id),NVL(team_name,to_char(activity_id)))
                                                                        ORDER BY
                                                                           claim_id desc)
                                                                        AS rec_rank from tmp_winner_nomination_summary res ) WHERE rec_rank = 1) res
                  ) s -- sort number result set
            WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
              AND s.rn < TO_NUMBER(p_in_rowNumEnd));
           
  v_stage := 'Return Custom detail for approver';
  OPEN p_out_custom_dtl_res FOR
    SELECT cc.claim_id,
           ta.nominee_user_id,
           claim_form_step_element_id,
           claim_form_step_element_name, 
           cms_value claim_form_step_element_desc
      FROM (
      SELECT *FROM
      (SELECT claim_id,
                   claim_cfse_id, 
                   sequence_num,
                   claim_form_step_element_id,
                   claim_form_step_element_name,
                   -- LISTAGG(cms_value,', ') WITHIN GROUP(ORDER BY cms_value) cms_value  -- Commented out 06/26/2019
                   cms_value,  -- 06/26/2019
                   ROW_NUMBER() OVER (PARTITION BY claim_id,   -- 06/26/2019
                                                   claim_cfse_id, 
                                                   sequence_num,
                                                   claim_form_step_element_id 
                                          ORDER BY claim_id ) rownumber
              FROM ( SELECT claim_id,
                            claim_cfse_id,
                            claim_form_step_element_id,
                            sequence_num,
                            cm_asset_code,
                            (SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND INSTR(cms_code,'ELEMENTLABEL_'||cm_key_fragment,1) > 0) claim_form_step_element_name,
                            -- NVL((SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND (lower(cms_code) = lower(value||'_'||cm_key_fragment) or lower(cms_code) = lower('label'||value||'_'||cm_key_fragment))), value)   cms_value  -- Commented out 06/26/2019
                            NVL((SELECT to_clob(cms_name) from temp_table_session 
                                where asset_code = cm_asset_code 
                                AND (dbms_lob.compare(lower(cms_code), to_clob(lower(value||'_'||cm_key_fragment))) = 0
                                 or dbms_lob.compare(lower(cms_code), to_clob(lower('label'||value||'_'||cm_key_fragment))) = 0)),value) cms_value -- 06/26/2019 
                       FROM ( SELECT cc.claim_id,
                                     -- column_value as value,  -- Commented out 06/26/2019
                                     cc.value,   -- 06/26/2019
                                     cc.claim_cfse_id,
                                     cf.cm_asset_code, 
                                     cfse.claim_form_step_element_id,
                                     cfse.sequence_num,
                                     cfse.cm_key_fragment,
                                     selection_pick_list_name
                                FROM promotion p,
                                     claim_form cf, 
                                     claim_form_step cfs, 
                                     claim_form_step_element cfse,
                                     claim_cfse cc,
                                     tmp_winner_nomination_summary ta
                                   /*  TABLE( CAST( MULTISET(                   -- Commented out 06/26/2019
                                      SELECT SUBSTR(','||cc.value||',',
                                                   INSTR(','||cc.value||',', ',', 1, LEVEL)+1, 
                                                   INSTR(','||cc.value||',', ',', 1, LEVEL+1) - INSTR(','||cc.value||',', ',', 1, LEVEL)-1 
                                                   )
                                                                     
                                        FROM dual
                                     CONNECT BY LEVEL <= REGEXP_COUNT(','||cc.value, ',') 
                                     ) AS sys.odcivarchar2list ) ) p    */                               
                               WHERE p.promotion_id = p_in_promotion_id
                                 AND p.claim_form_id = cf.claim_form_id
                                 AND cf.claim_form_id = cfs.claim_form_id
                                 AND cfs.claim_form_step_id = cfse.claim_form_step_id
                                 AND cfse.claim_form_step_element_id = cc.claim_form_step_element_id 
                                 AND cc.value IS NOT NULL
                                 AND claim_form_element_type_code <> 'copy'
                                 and cc.claim_id = ta.claim_id
                            ))) WHERE rownumber = 1  -- 06/26/2019
                  -- GROUP BY claim_id,claim_cfse_id, sequence_num,claim_form_step_element_id,claim_form_step_element_name  -- Commented out 06/26/2019
           ) cc,
           (SELECT s.*
              FROM (SELECT ROW_NUMBER() OVER 
                           (ORDER BY
                             -- sort on field ascending
                             (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_firstname IS NOT NULL)      THEN nominee_first_name||' '||nominee_last_name   END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_lastname IS NOT NULL)       THEN nominee_last_name||' '||nominee_first_name   END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND (p_in_firstname IS NULL OR p_in_lastname IS NULL))    THEN date_approved           END) ASC
                             -- sort on field descending
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_firstname IS NOT NULL)      THEN nominee_first_name||' '||nominee_last_name  END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_lastname IS NOT NULL)       THEN nominee_last_name||' '||nominee_first_name  END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND (p_in_firstname IS NULL OR p_in_lastname IS NULL))    THEN date_approved          END) DESC
                           ) AS rn, 
                           res.*
                      FROM tmp_winner_nomination_summary res   
                  ) s -- sort number result set
            WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
              AND s.rn < TO_NUMBER(p_in_rowNumEnd)
           )ta
     WHERE cc.claim_id = ta.claim_id
     ORDER BY CC.SEQUENCE_NUM;   
     
     Open P_Out_Activity_List For
     SELECT DECODE(team_name, NULL,activity_id) activity_id,  --12/27/2016    Bug 70595 replaced rank() to restrict to one record only for a team nomi
      time_period_id,claim_group_id,
      team_name,team_id  --12/27/2016  Bug 70595
       FROM (SELECT activity_id, time_period_id,claim_group_id,RANK ()
                                                                     OVER (
                                                                        PARTITION BY NVL(claim_group_id,activity_id)
                                                                        ORDER BY
                                                                           activity_id desc,ROWNUM DESC)
                                                                        AS rec_rank,team_name,team_id   --12/27/2016    Bug 70595
      FROM tmp_winner_nomination_summary WHERE activity_id in ((      
      SELECT s.activity_id
              FROM (SELECT ROW_NUMBER() OVER 
                           (ORDER BY
                             -- sort on field ascending
                           -- sort on field ascending
                             (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_firstname IS NOT NULL)      THEN nominee_first_name||' '||nominee_last_name   END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_lastname IS NOT NULL)       THEN nominee_last_name||' '||nominee_first_name   END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND (p_in_firstname IS NULL OR p_in_lastname IS NULL))    THEN date_approved           END) ASC
                             -- sort on field descending
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_firstname IS NOT NULL)      THEN nominee_first_name||' '||nominee_last_name  END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_lastname IS NOT NULL)       THEN nominee_last_name||' '||nominee_first_name  END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND (p_in_firstname IS NULL OR p_in_lastname IS NULL))    THEN date_approved          END) DESC
                           ) AS rn, 
                           res.*
                      FROM (SELECT * FROM (select res.*,ROW_NUMBER ()  --12/27/2016    Bug 70595 replaced rank() to restrict to one record only for a team nomi 
                                                                     OVER (
                                                                        PARTITION BY NVL(to_char(claim_group_id),NVL(team_name,to_char(activity_id)))
                                                                        ORDER BY
                                                                           claim_id desc)
                                                                        AS rec_rank from tmp_winner_nomination_summary res ) WHERE rec_rank = 1) res
                  ) s -- sort number result set
            WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
              AND s.rn < TO_NUMBER(p_in_rowNumEnd)
           ))) WHERE REC_RANK = 1;   
  
EXCEPTION 
  WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Parms : p_in_promotion_id: '||p_in_promotion_id||
      ' ~ p_in_logged_in_user_id: '||p_in_logged_in_user_id||' ~ p_in_lastname: '||p_in_lastname||' ~ p_in_firstname: '||p_in_firstname||
      ' ~ p_in_country: '||p_in_country||' ~ p_in_department: '||p_in_department||' ~ p_in_teamname: '||p_in_teamname||
      ' ~ p_in_startdate: '||p_in_startdate||' ~ p_in_enddate: '||p_in_enddate||' ~ Error at stage :'||v_stage||'-->'||SQLERRM, NULL);
      p_out_returncode := 99;

      OPEN p_out_nominee_dtl_res FOR SELECT NULL FROM DUAL;
      OPEN p_out_team_memb_res FOR SELECT NULL FROM DUAL;
      OPEN p_out_nominator_res FOR SELECT NULL FROM DUAL;
      OPEN p_out_custom_dtl_res FOR SELECT NULL FROM DUAL;
      OPEN p_out_activity_list FOR SELECT NULL FROM DUAL;

END prc_past_winner_summary;

PROCEDURE prc_past_winner_detail
         ( p_in_winner_user_id      IN  NUMBER,         
           p_in_team_id             IN  NUMBER,
           p_in_logged_in_user_id   IN  NUMBER,
           p_in_activity_id         IN  NUMBER,
           p_in_time_period_id      IN  NUMBER,
           p_in_locale              IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,
           p_out_nominee_dtl_res    OUT SYS_REFCURSOR,
           p_out_team_memb_dtl_res  OUT SYS_REFCURSOR,
           p_out_behavior_dtl_res   OUT SYS_REFCURSOR,
           p_out_custom_dtl_res     OUT SYS_REFCURSOR,
           p_out_nominator_dtl_res      OUT SYS_REFCURSOR)
AS
/*******************************************************************************
Purpose: To return the winner nominations detail result in all eligible promotions of the approvers  
 
  Person             Date         Comments
  -----------       --------      -----------------------------------------------------
  nagarajs          06/08/2016    Initial creation                         
  Sherif Basha     11/30/2016     Bug 70276 - include SUBMITTER_COMMENTS_LANG_ID for p_out_nominee_dtl_res
  Sherif Basha     12/27/2016    Bug 70595 - Same team name pax is getting displayed different line in winner details page.
                                  (Requirement to group the Team nomination list of records into one)
  Gorantla          04/05/2018     G6-3963/Bug 75860 - TABLE( CAST( MULTISET is causing comments to flip around based on commas within the text
                                     fix is stop using TABLE( CAST( MULTISET and just use claim_cfse.value
  Loganathan        04/24/2019      Bug 78755 - Duplicate information is showing on view nomination page
  Loganathan        06/20/2019      Bug 75186 - Application is displaying incorrect value for Non-US recipient 
  Loganathan        07/02/2019      Bug 78682 - Budget - We are facing round of issues which is removing some points from the budget                                   
  Gorantla          06/26/2019      Bug 77739 - Nomination with the comments of 4000 characters length is not loading on nomination approval page.                                  
*******************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_PAST_WINNER_DETAIL';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';
   
   v_stage                  VARCHAR2(500);
   v_currency_value         cash_currency_current.bpom_entered_rate%TYPE;
   v_default_locale         os_propertyset.string_val%TYPE;
BEGIN

  p_out_returncode := 0;
  
   v_stage := 'Get currency value';
  BEGIN
   SELECT ccc.bpom_entered_rate
     INTO v_currency_value
     FROM user_address ua,
          country c,
          cash_currency_current ccc 
    WHERE ua.user_id = p_in_logged_in_user_id
      AND is_primary = 1
      AND ua.country_id = c.country_id
      --AND c.country_code = ccc.to_cur     --06/20/2019 Bug#75186
      AND c.CURRENCY_CODE = ccc.to_cur ;    --06/20/2019 Bug#75186
  EXCEPTION
    WHEN OTHERS THEN
      v_currency_value := 1;
  END;   
   
  
  BEGIN
    SELECT string_val 
      INTO v_default_locale
      FROM os_propertyset
     WHERE entity_name = 'default.language';
  EXCEPTION
    WHEN OTHERS THEN
      v_default_locale := 'en_US';
  END;
  
  DELETE temp_table_session;
  
  v_stage:= 'Insert temp_table_session';
  INSERT INTO temp_table_session
    SELECT asset_code, cms_value, key
      FROM ( SELECT asset_code, cms_value, key, RANK() OVER(PARTITION BY asset_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key IN ('COUNTRY_NAME', 'PROMOTION_TIME_PERIOD_NAME_','PROMOTION_LEVEL_LABEL_NAME_','PAYOUT_DESCRIPTION_')
                AND locale IN (v_default_locale, p_in_locale)
              UNION ALL
             SELECT asset_code, cms_value, key, RANK() OVER(PARTITION BY asset_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value
              WHERE asset_code IN (SELECT promo_name_asset_code FROM promotion WHERE promotion_type = 'nomination')
                AND locale IN (v_default_locale, p_in_locale)
              UNION ALL
             SELECT asset_code,cms_name,cms_code,  RANK() OVER(PARTITION BY asset_code, cms_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn 
               FROM vw_cms_code_value 
              WHERE asset_code IN ( 'picklist.positiontype.items','picklist.department.type.items' , 'picklist.promo.nomination.behavior.items')
                AND locale IN (v_default_locale, p_in_locale)
                AND cms_status = 'true'
              UNION ALL
             SELECT asset_code,cms_value,key ,RANK() OVER(PARTITION BY key ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key IN (SELECT 'ELEMENTLABEL_'||cm_key_fragment from claim_form_step_element )
                AND locale IN (v_default_locale, p_in_locale)
              UNION ALL
             SELECT cf.cm_asset_code,cms_name, cms_code||'_'||cfse.cm_key_fragment,RANK() OVER(PARTITION BY cf.cm_asset_code,cms_code||'_'||cfse.cm_key_fragment ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_code_value  vw, claim_form_step_element cfse, 
                    claim_form_step cfs, claim_form cf, promotion p, activity a
              WHERE asset_code =  cfse.selection_pick_list_name 
                AND cfse.claim_form_step_id = cfs.claim_form_step_id 
                AND cfs.claim_form_id = cf.claim_form_id
                and cf.claim_form_id = p.claim_form_id
                AND p.promotion_id = a.promotion_id
                AND a.activity_id = p_in_activity_id
                AND locale IN (v_default_locale, p_in_locale)
                AND cms_status = 'true' 
                AND claim_form_element_type_code <> 'copy'
              UNION ALL
             SELECT asset_code,cms_value,key ,RANK() OVER(PARTITION BY key,cms_value ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key IN (SELECT 'LABELTRUE_'||cm_key_fragment from claim_form_step_element 
                            UNION SELECT 'LABELFALSE_'||cm_key_fragment from claim_form_step_element )
                AND locale IN (v_default_locale, p_in_locale)
             ) WHERE rn = 1;

  DELETE TMP_WINNER_NOMINATION_DETAIL;
  
  v_stage:= 'Insert TMP_WINNER_NOMINATION_DETAIL';
  INSERT INTO TMP_WINNER_NOMINATION_DETAIL
    SELECT c.claim_id,
           CASE WHEN nc.team_name IS NULL THEN NULL ELSE nc.team_id END team_id,
           (select cms_name from temp_table_session where asset_code = p.promo_name_asset_code) as promotion_name,
           NVL(cia.notification_date,cia.date_approved)  date_approved,
           CASE WHEN award_payout_type = 'cash' THEN
             (SELECT currency_code
                FROM user_address ua,
                     country c
               WHERE ua.user_id = p_in_logged_in_user_id
                 AND is_primary = 1
                 AND ua.country_id = c.country_id
              ) 
           ELSE NULL
           END currency_label ,
           ca.large_image_name ecard_image,
           nc.card_video_url,
           nc.card_video_image_url,
           decode(nc.team_id, NULL, 0, 1) is_team,
           (SELECT cms_name FROM temp_table_session WHERE asset_code = pnt.time_period_name_asset_code) time_period_name,
           nc.team_name,
           CASE WHEN NVL(p_in_winner_user_id,0) <> p_in_logged_in_user_id THEN
             au_rec.first_name
           ELSE NULL END nominee_first_name,
           CASE WHEN NVL(p_in_winner_user_id,0) <> p_in_logged_in_user_id THEN
             au_rec.last_name 
           ELSE NULL END nominee_last_name,
           CASE WHEN NVL(p_in_winner_user_id,0) = p_in_logged_in_user_id THEN
           CASE WHEN award_payout_type = 'cash' THEN a.cash_award_qty * v_currency_value
                  ELSE a.quantity
           END 
           ELSE NULL END award_amount,
           CASE WHEN NVL(p_in_winner_user_id,0) <> p_in_logged_in_user_id THEN
             n_rec.hierarchy_id 
           ELSE NULL END nominee_hierarchy_id,
           CASE WHEN NVL(p_in_winner_user_id,0) <> p_in_logged_in_user_id THEN
             n_rec.name 
           ELSE NULL END nominee_org_name,
           CASE WHEN NVL(p_in_winner_user_id,0) <> p_in_logged_in_user_id THEN
             (select cms_name from temp_table_session where asset_code ='picklist.positiontype.items' and cms_code = vw_rec.position_type) 
           ELSE NULL END nominee_job_position,
           CASE WHEN NVL(p_in_winner_user_id,0) <> p_in_logged_in_user_id THEN
             cu_rec.country_code 
           ELSE NULL END nominee_country_code,
           CASE WHEN NVL(p_in_winner_user_id,0) <> p_in_logged_in_user_id THEN
             (select cms_name from temp_table_session where asset_code = cu_rec.cm_asset_code ) 
           ELSE NULL END nominee_country_name,
           CASE WHEN NVL(p_in_winner_user_id,0) <> p_in_logged_in_user_id THEN
             pa_rec.avatar_small 
           ELSE NULL END nominee_avatar_url,
           c.submitter_id nominator_user_id,
           au_giv.first_name nominator_first_name,
           au_giv.last_name nominator_last_name,
           n_giv.hierarchy_id nominator_hierarchy_id,
           n_giv.name nominator_org_name,
           (select cms_name from temp_table_session where asset_code='picklist.positiontype.items' and cms_code = vw_giv.position_type)  nominator_job_position,
           cu_giv.country_code nominator_country_code,
           (select cms_name from temp_table_session where asset_code = cu_giv.cm_asset_code ) nominator_country_name,
           pa_giv.avatar_small nominator_avatar_url,
           nc.submitter_comments,
           nc.submitter_comments_lang_id,
           TRUNC(c.submission_date) date_submitted,
           nc.certificate_id,
           nc.own_card_name,
           (select cms_name from temp_table_session where asset_code='picklist.department.type.items' and cms_code = vw_giv.department_type)  nominator_department,
            CASE WHEN NVL(p_in_winner_user_id,0) <> p_in_logged_in_user_id THEN
             (select cms_name from temp_table_session where asset_code ='picklist.department.type.items' and cms_code = vw_rec.department_type) 
           ELSE NULL END nominee_department,
           cia.approval_round level_number,
           (SELECT cms_name FROM temp_table_session WHERE asset_code = pnl.level_label_asset_code) level_name ,
           pnl.payout_description_asset_code,
           c.claim_group_id,
           cr.participant_id nominee_user_id
     FROM claim c,
          claim_item ci,
          claim_item_approver cia,
          claim_recipient cr,
          nomination_claim nc,
          card ca,
          activity a,
          promo_nomination_level pnl,
          promo_nomination pn,
          promotion p,
          promo_nomination_time_period pnt,
          node n_rec,
          application_user au_rec,
          participant pa_rec,
          user_address ua_rec,
          country cu_rec,
          vw_curr_pax_employer vw_rec,
          node n_giv,
          application_user au_giv,
          participant pa_giv,
          user_address ua_giv,
          country cu_giv,
          vw_curr_pax_employer vw_giv
    WHERE c.claim_id = ci.claim_id
      AND (ci.claim_item_id = cia.claim_item_id OR c.claim_group_id = cia.claim_group_id)
      AND cia.approval_status_type = 'winner'
      AND ci.claim_item_id = cr.claim_item_id
      AND cr.participant_id = NVL(p_in_winner_user_id,cr.participant_id)
      AND cr.node_id = n_rec.node_id
      AND c.node_id = n_giv.node_id
      AND c.claim_id = nc.claim_id
      AND nc.card_id = ca.card_id (+)
      AND ((p_in_team_id IS NOT NULL AND nc.team_id = p_in_team_id) OR p_in_team_id IS NULL)
      AND (c.claim_id = a.claim_id OR c.claim_group_id = a.claim_group_id)
      AND a.activity_id = NVL(p_in_activity_id,a.activity_id)  --12/27/2016    Bug 70595 activity id would be passed as null for team nom  
      AND a.is_submitter = 0
      AND a.promotion_id = pnl.promotion_id(+)
      AND a.promotion_id = pn.promotion_id
      AND a.approval_round = cia.approval_round
      AND a.user_id = cr.participant_id
      AND ((a.approval_round = pnl.level_index AND pn.payout_level_type = 'eachLevel')
          OR (pn.payout_level_type = 'finalLevel' ))
      AND c.promotion_id = p.promotion_id
      AND c.promotion_id = pnt.promotion_id (+)  
      AND (pnt.nomination_time_period_ID = p_in_time_period_id  OR p_in_time_period_id IS NULL)
      AND cr.participant_id = au_rec.user_id 
      AND au_rec.user_id = pa_rec.user_id
      AND au_rec.user_id = ua_rec.user_id (+)
      AND ua_rec.is_primary (+) = 1
      AND ua_rec.country_id = cu_rec.country_id (+)
      AND au_rec.user_id = vw_rec.user_id (+)
      AND c.submitter_id = au_giv.user_id 
      AND au_giv.user_id = pa_giv.user_id
      AND au_giv.user_id = ua_giv.user_id (+)
      AND ua_giv.is_primary (+) = 1
      AND ua_giv.country_id = cu_giv.country_id (+)
      AND au_giv.user_id = vw_giv.user_id (+)
      AND ROWNUM <= 1;                                    --04/24/2019 Bug 78755
      
  v_stage := 'Return Nominee detail';
  OPEN p_out_nominee_dtl_res FOR
    SELECT claim_id,
           claim_group_id,
           promotion_name,
           date_approved,
           currency_label,
           ecard_name,
           card_video_url,
           card_video_image_url,
           team_nomination,
           time_period_name,
           team_name,
           DECODE(team_id, NULL,nominee_first_name) nominee_first_name,
           DECODE(team_id, NULL,nominee_last_name) nominee_last_name,
           trunc(nvl(award_amount,0),2) award_amount, 						--07/02/2019 Bug#78682 -- Handled trunc nad Nvl
           DECODE(team_id, NULL,nominee_hierarchy_id) nominee_hierarchy_id ,
           DECODE(team_id, NULL,nominee_org_name) nominee_org_name,
           DECODE(team_id, NULL,nominee_job_position) nominee_position,
           DECODE(team_id, NULL,nominee_country_code) nominee_country_code,
           DECODE(team_id, NULL,nominee_country_name) nominee_country_name,
           DECODE(team_id, NULL,nominee_avatar_url) nominee_avatar_url,           
           date_submitted,
          certificate_id,
           own_card_name,
           level_number,
           level_name,
           submitter_comments_lang_id, -- 11/30/2016   Bug 70276
           (SELECT cms_name FROM temp_table_session WHERE asset_code = payout_description_asset_code) payout_description,
           nominee_user_id,
           team_id   --12/27/2016    Bug 70595
      FROM (SELECT * FROM (select res.*,ROW_NUMBER ()
                                                                     OVER (
                                                                        PARTITION BY NVL(claim_group_id,claim_id)
                                                                        ORDER BY
                                                                           claim_id desc)
                                                                        AS rec_rank from tmp_winner_nomination_detail res ) WHERE rec_rank = 1);
   v_stage := 'Return Nominator detail';
  OPEN p_out_nominator_dtl_res FOR
    SELECT claim_id,
           claim_group_id,
           promotion_name,
           date_approved,
           currency_label,
           ecard_name,
           card_video_url,
           card_video_image_url,
           team_nomination,
           time_period_name,
           team_name,           
           award_amount,           
           nominator_user_id,
           nominator_first_name,
           nominator_last_name,
           nominator_hierarchy_id,
           nominator_org_name,
           nominator_job_position,
           nominator_country_code,
           nominator_country_name,
           nominator_avatar_url,
           submitter_comments,
           submitter_comments_lang_id,
           date_submitted,
          certificate_id,
           own_card_name,
           nominator_department,
           level_number,
           level_name,
           (SELECT cms_name FROM temp_table_session WHERE asset_code = payout_description_asset_code) payout_description
      FROM  --12/27/2016    Bug 70595 to restrict team nomi records to one
       (SELECT * FROM (select res.*,ROW_NUMBER ()  OVER (PARTITION BY NVL(claim_group_id,claim_id) ORDER BY claim_id desc)
                                                                        AS rec_rank from tmp_winner_nomination_detail res ) WHERE rec_rank = 1);
                                                                        
  v_stage := 'Return team members detail';
  OPEN p_out_team_memb_dtl_res FOR
   SELECT claim_id,
          nominee_first_name,
          nominee_last_name        
     FROM tmp_winner_nomination_detail
    WHERE team_id IS NOT NULL;
     
  v_stage := 'Return behavior detail';
  OPEN p_out_behavior_dtl_res FOR
  SELECT ncb.claim_id,
           (select cms_name from temp_table_session where asset_code = 'picklist.promo.nomination.behavior.items' and cms_code = behavior_type) behavior_name,
           bad.badge_name,
           badge_rule_id badge_id
      FROM promo_behavior pb,
           (SELECT bp.eligible_promotion_id , br.behavior_name, br.cm_asset_key badge_name,badge_rule_id
              FROM badge_promotion bp,
                   badge_rule br
             WHERE bp.promotion_id = br.promotion_id
               AND br.behavior_name IS NOT NULL
            ) bad,
           nomination_claim_behaviors ncb,
           --12/27/2016    Bug 70595 to restrict team nomi records to one
           (SELECT * FROM (select res.*,ROW_NUMBER () OVER ( PARTITION BY NVL(claim_group_id,claim_id)
                                                                        ORDER BY
                                                                           claim_id desc)
                                                                        AS rec_rank from tmp_winner_nomination_detail res ) WHERE rec_rank = 1) ta,
           claim c
     WHERE pb.promotion_id = c.promotion_id
       AND pb.promotion_id = bad.eligible_promotion_id (+)
       AND pb.behavior_type = bad.behavior_name  (+)
       AND pb.behavior_type = ncb.behavior
       AND ncb.claim_id = ta.claim_id
       AND ta.claim_id = c.claim_id;
           
  v_stage := 'Return Custom detail';
  OPEN p_out_custom_dtl_res FOR
    SELECT claim_id,
           claim_form_step_element_id,
           claim_form_step_element_name, 
           description claim_form_step_element_desc,
           cms_value claim_form_value,
           sequence_num,
           why_field
      FROM(
      SELECT *FROM  -- 06/26/2019
      (SELECT claim_id,
                   claim_cfse_id, 
                   sequence_num,
                   description,
                   why_field,
                   claim_form_step_element_id,
                   claim_form_step_element_name,
                   -- LISTAGG(cms_value,', ') WITHIN GROUP(ORDER BY cms_value) cms_value  -- Commented out 06/26/2019
                   cms_value,  -- 06/26/2019
                   ROW_NUMBER() OVER (PARTITION BY claim_id,   -- 06/26/2019
                                                   claim_cfse_id, 
                                                   sequence_num,
                                                   description,
                                                   why_field,
                                                   claim_form_step_element_id,
                                                   claim_form_step_element_name 
                                          ORDER BY claim_id ) rownumber
              FROM ( SELECT claim_id,
                            claim_cfse_id,
                            claim_form_step_element_id,
                            sequence_num,
                            description,
                            why_field,
                            cm_asset_code,
                            (SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND INSTR(cms_code,'ELEMENTLABEL_'||cm_key_fragment,1) > 0) claim_form_step_element_name,
                            -- NVL((SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND (lower(cms_code) = lower(value||'_'||cm_key_fragment) or lower(cms_code) = lower('label'||value||'_'||cm_key_fragment))), value)   cms_value  -- Commented out 06/26/2019
                            NVL((SELECT to_clob(cms_name) from temp_table_session 
                                where asset_code = cm_asset_code 
                                AND (dbms_lob.compare(lower(cms_code), to_clob(lower(value||'_'||cm_key_fragment))) = 0
                                 or dbms_lob.compare(lower(cms_code), to_clob(lower('label'||value||'_'||cm_key_fragment))) = 0)),value) cms_value -- 06/26/2019 
                       FROM ( SELECT cc.claim_id,
--                                     COLUMN_VALUE AS VALUE, -- 04/05/2018
                                     -- fnc_format_comments(cc.value) value, -- 04/05/2018 -- Commented out 06/26/2019
                                     cc.value,  -- 06/26/2019
                                     cc.claim_cfse_id,
                                     cf.cm_asset_code, 
                                     cfse.claim_form_step_element_id,
                                     cfse.sequence_num,
                                     cfse.cm_key_fragment,
                                     selection_pick_list_name,
                                     cfse.description,
                                     why_field
                                FROM claim_form cf, 
                                     claim_form_step cfs, 
                                     claim_form_step_element cfse,
                                     claim_cfse cc,
                                     tmp_winner_nomination_detail ta
                                     /*, -- 04/05/2018
                                     TABLE( CAST( MULTISET(
                                      SELECT SUBSTR(','||cc.value||',',
                                                   INSTR(','||cc.value||',', ',', 1, LEVEL)+1, 
                                                   INSTR(','||cc.value||',', ',', 1, LEVEL+1) - INSTR(','||cc.value||',', ',', 1, LEVEL)-1 
                                                   )
                                                                     
                                        FROM dual
                                     CONNECT BY LEVEL <= REGEXP_COUNT(','||cc.value, ',') 
                                     ) AS sys.odcivarchar2list ) ) P   */                               
                               WHERE cf.claim_form_id = cfs.claim_form_id
                                 AND cfs.claim_form_step_id = cfse.claim_form_step_id
                                 AND cfse.claim_form_step_element_id = cc.claim_form_step_element_id 
                                 AND cc.value IS NOT NULL
                                 AND claim_form_element_type_code <> 'copy'
                                 and cc.claim_id = ta.claim_id
                            ))) WHERE rownumber = 1  -- 06/26/2019
                 --  GROUP BY claim_id,claim_cfse_id, sequence_num,description,why_field,claim_form_step_element_id,claim_form_step_element_name  -- Commented out 06/26/2019
           ) cc ORDER BY CC.SEQUENCE_NUM;
           
  
  
EXCEPTION 
  WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Parms : p_in_logged_in_user_id: '||p_in_logged_in_user_id||
      ' ~ p_in_winner_user_id: '||p_in_winner_user_id||' ~ p_in_activity_id: '||p_in_activity_id||' ~ p_in_team_id: '||p_in_team_id||
      ' ~ Error at stage :'||v_stage||'-->'||SQLERRM, NULL);
      p_out_returncode := 99;
      OPEN p_out_nominee_dtl_res FOR SELECT NULL FROM DUAL;
      OPEN p_out_team_memb_dtl_res FOR SELECT NULL FROM DUAL;
      OPEN p_out_behavior_dtl_res FOR SELECT NULL FROM DUAL;
      OPEN p_out_custom_dtl_res FOR SELECT NULL FROM DUAL;
END prc_past_winner_detail;

END;
/