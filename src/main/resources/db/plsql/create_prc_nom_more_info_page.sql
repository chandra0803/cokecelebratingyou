CREATE OR REPLACE PROCEDURE prc_nom_more_info_page (
   p_in_claim_id         IN     NUMBER,
   p_in_user_id          IN     NUMBER,
   p_in_locale           IN     VARCHAR2,
   p_out_data               OUT SYS_REFCURSOR,
   p_out_custom_data       OUT SYS_REFCURSOR,
   p_out_behavior_data      OUT SYS_REFCURSOR,
   p_out_team_data          OUT SYS_REFCURSOR)
IS
   /***********************************************************************************
      Purpose:  To provide data for nominations more info page.

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
     Ravi Dhanekula   06/08/2016     Initial Version
     Sherif Basha     12/15/2016     Bug 69263 - UAT - Nomination - Need the ability to attach multiple (attachments)-- Added new column to the resultset
     Gorantla         04/18/2018     Bug 75860 - TABLE( CAST( MULTISET is causing comments to flip around based on commas within the text
                                     fix is stop using TABLE( CAST( MULTISET and just use claim_cfse.value
     Gorantla         08/28/2019   Bug 77739 - Nomination with the comments of 4000 characters length is not loading on nomination approval page.                              
   ************************************************************************************/

   is_team                    NUMBER (1):=0;
   approver_country_id        NUMBER;

   c_process_name    CONSTANT execution_log.process_name%TYPE := 'PRC_NOM_MORE_INFO_PAGE' ;
   c_release_level   CONSTANT execution_log.release_level%TYPE := '1.0';
   v_stage           VARCHAR2 (500);
   v_promotion_id    promotion.promotion_id%TYPE;
   v_default_locale         os_propertyset.string_val%TYPE;
BEGIN
            
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
     SELECT asset_code,cms_name,cms_code
       FROM (SELECT asset_code,cms_name,cms_code,  RANK() OVER(PARTITION BY asset_code, cms_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn 
               FROM vw_cms_code_value 
              WHERE asset_code IN ( 'picklist.promo.nomination.behavior.items', 'picklist.positiontype.items','picklist.department.type.items')
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
                    claim_form_step cfs, claim_form cf, promotion p, claim c
              WHERE asset_code =  cfse.selection_pick_list_name 
                AND cfse.claim_form_step_id = cfs.claim_form_step_id 
                AND cfs.claim_form_id = cf.claim_form_id
                and cf.claim_form_id = p.claim_form_id
                AND p.promotion_id = c.promotion_id
                AND c.claim_id = p_in_claim_id
                AND locale IN (v_default_locale, p_in_locale)
                AND cms_status = 'true' 
              UNION ALL
             SELECT asset_code,cms_value,key ,RANK() OVER(PARTITION BY key,cms_value ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key IN (SELECT 'LABELTRUE_'||cm_key_fragment from claim_form_step_element 
                            UNION SELECT 'LABELFALSE_'||cm_key_fragment from claim_form_step_element )
                AND locale IN (v_default_locale, p_in_locale)
               ) WHERE rn = 1;
        
   v_stage := 'Check if the claim is a team or individual nomination';

   BEGIN
      SELECT CASE WHEN TEAM_NAME IS NULL THEN 0 ELSE 1 END,
             promotion_id
        INTO is_team,
             v_promotion_id
        FROM nomination_claim nc,
             claim c
       WHERE c.claim_id = p_in_claim_id
         AND c.claim_id = nc.claim_id;
   EXCEPTION
      WHEN OTHERS
      THEN
         prc_execution_log_entry (
            'PRC_NOM_MORE_INFO_PAGE',
            1,
            'ERROR',
               p_in_claim_id
            || ': '
            || 'p_in_claim_id'
            || p_in_user_id
            || ': '
            || 'p_in_user_id'
            || CHR (10)
            || SQLERRM,
            NULL);
   END;


   v_stage := 'Get approver country_id';

   BEGIN
      SELECT ua.country_id
        INTO approver_country_id
        FROM user_address ua
       WHERE     p_in_user_id = ua.user_id
             AND ua.is_primary = 1;
   EXCEPTION
      WHEN OTHERS
      THEN
         prc_execution_log_entry (
            'PRC_NOM_MORE_INFO_PAGE',
            1,
            'ERROR',
               p_in_claim_id
            || ': '
            || 'p_in_claim_id'
            || p_in_user_id
            || ': '
            || 'p_in_user_id'
            || CHR (10)
            || SQLERRM,
            NULL);
   END;
   
   IF is_team = 0
   THEN
      OPEN p_out_data FOR
         SELECT p.promotion_name,
                cia.approver_comments,
                c.submission_date,
                cr.participant_id,
                au_rec.first_name,
                au_rec.last_name,
                (select cms_name from temp_table_session where asset_code='picklist.positiontype.items' and cms_code = position_type) position_type,
                (select cms_name from temp_table_session where asset_code='picklist.department.type.items' and cms_code = department_type) department_type,
                ua_rec.country_id,
                cntry.country_code,
                cntry.cm_asset_code,
                n_rec.node_id,
                n_rec.name node_name,
                nc.certificate_id,
                nc.card_video_image_url,
                nc.card_video_url,
                card.card_name,
                card.large_image_name,
                nc.why_attachment_name,
                nc.why_attachment_url,  -- 12/15/2016   Bug 69263
                nc.submitter_comments_lang_id,
                nc.team_id,
                nc.team_name,
                nc.submitter_comments reason,
                 nc.own_card_name
           FROM claim c,
                claim_item ci,
                claim_item_approver cia,
                application_user au_rec,
                promotion p,
                user_address ua_rec,
                node n_rec,
                nomination_claim nc,
                claim_recipient cr,
                card,
                vw_curr_pax_employer vw,
                country cntry,
                user_address ua_sub
          WHERE     c.claim_id = p_in_claim_id
                AND c.claim_id = ci.claim_id
                AND (ci.claim_item_id = cia.claim_item_id OR c.claim_group_id = cia.claim_group_id )
                AND cia.approval_status_type = 'more_info'
                AND c.claim_id = nc.claim_id
                AND cr.node_id = n_rec.node_id
                AND c.promotion_id = p.promotion_id
                AND cr.participant_id = au_rec.user_id
                AND au_rec.is_active = 1
                AND cr.participant_id = ua_rec.user_id
                AND nc.card_id = card.card_id(+)
                AND ci.claim_id = p_in_claim_id
                AND ci.claim_item_id = cr.claim_item_id
                AND cr.participant_id = vw.user_id(+)
                AND ua_rec.country_id = cntry.country_id
                AND ua_rec.is_primary = 1
                AND c.submitter_id = ua_sub.user_id
                AND ua_sub.is_primary = 1;

      OPEN p_out_team_data FOR 
        SELECT NULL user_id, NULL first_name, NULL last_name
          FROM dual 
         WHERE 1 = 2;
   ELSE
      
      OPEN p_out_data FOR
         SELECT p.promotion_name,
                (SELECT DISTINCT approver_comments
                   FROM claim_item ci, 
                        claim_item_approver cia
                  WHERE ci.claim_id = c.claim_id
                    AND (ci.claim_item_id = cia.claim_item_id OR c.claim_group_id = cia.claim_group_id )
                    AND cia.approval_status_type = 'more_info'
                  ) approver_comments,
                c.submission_date,
                NULL participant_id,
                NULL first_name,
                NULL last_name,
                NULL position_type,
                NULL department_type,
                NULL country_id,
                NULL country_code,
                NULL cm_asset_code,
                NULL node_id,
                NULL node_name,
                nc.certificate_id,
                nc.card_video_image_url,
                nc.card_video_url,
                card.card_name,
                card.large_image_name,
                nc.why_attachment_name,
                nc.why_attachment_url,      -- 12/15/2016   Bug 69263
                nc.submitter_comments_lang_id,
                nc.team_id,
                nc.team_name,
                nc.submitter_comments reason,
                nc.own_card_name
           FROM claim c,
                promotion p,
                nomination_claim nc,
                card,
                user_address ua_sub
          WHERE     c.claim_id = p_in_claim_id
                AND c.claim_id = nc.claim_id
                AND c.promotion_id = p.promotion_id
                AND nc.card_id = card.card_id(+)
                AND c.submitter_id = ua_sub.user_id
                AND ua_sub.is_primary = 1;

      OPEN p_out_team_data FOR
         SELECT au.user_id, au.first_name, au.last_name
           FROM claim_item ci, 
                claim_recipient cr, 
                application_user au
          WHERE ci.claim_id = p_in_claim_id
            AND ci.claim_item_id = cr.claim_item_id
            AND cr.participant_id = au.user_id
            AND au.is_active = 1;
   END IF;

   OPEN p_out_behavior_data FOR
      SELECT nomination_claim_behaviors_id behavior_id, 
      (select cms_name from temp_table_session where asset_code = 'picklist.promo.nomination.behavior.items' and cms_code = ncb.behavior) behavior_name, bad.badge_name
        FROM nomination_claim_behaviors ncb,    
            (SELECT bp.eligible_promotion_id , br.behavior_name, br.badge_name
               FROM badge_promotion bp,
                    badge_rule br
              WHERE bp.promotion_id = br.promotion_id
                AND br.behavior_name IS NOT NULL
             ) bad
       WHERE     ncb.claim_id = p_in_claim_id
             AND ncb.behavior = bad.behavior_name(+)
             AND bad.eligible_promotion_id (+)= v_promotion_id;
             
    OPEN p_out_custom_data FOR
       SELECT claim_form_step_element_id,
               claim_form_step_element_name name, 
               cms_value description,
               cms_value claim_form_value,
               sequence_num,
               why_field
          FROM (
          SELECT * FROM 
          (SELECT claim_id,
                       claim_cfse_id, 
                       sequence_num,
                       why_field,
                       claim_form_step_element_id,
                       claim_form_step_element_name,
                       cms_value,  -- 08/28/2019
                       ROW_NUMBER() OVER (PARTITION BY claim_id,   -- 08/28/2019
                                                   claim_cfse_id, 
                                                   sequence_num,
                                                   claim_form_step_element_id 
                                          ORDER BY claim_id ) rownumber
                     --  LISTAGG(cms_value,', ') WITHIN GROUP(ORDER BY cms_value) cms_value   -- Commented out 08/28/2019
                  FROM ( SELECT claim_id,
                                claim_cfse_id,
                                claim_form_step_element_id,
                                sequence_num,
                                why_field,
                                cm_asset_code,
                                (SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND INSTR(cms_code,'ELEMENTLABEL_'||cm_key_fragment,1) > 0) claim_form_step_element_name,
                                --NVL((SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND (lower(cms_code) = lower(value||'_'||cm_key_fragment) or lower(cms_code) = lower('label'||value||'_'||cm_key_fragment))), value)   cms_value  -- 08/28/2019
                                NVL((SELECT to_clob(cms_name) from temp_table_session 
                                where asset_code = cm_asset_code 
                                AND (dbms_lob.compare(lower(cms_code), to_clob(lower(value||'_'||cm_key_fragment))) = 0
                                 or dbms_lob.compare(lower(cms_code), to_clob(lower('label'||value||'_'||cm_key_fragment))) = 0)),value) cms_value -- 08/28/2019 
                           FROM ( SELECT cc.claim_id,
--                                       column_value as value, -- 04/18/2018
                                         cc.value, -- 04/18/2018
                                         cc.claim_cfse_id,
                                         cf.cm_asset_code, 
                                         cfse.claim_form_step_element_id,
                                         cfse.sequence_num,
                                         cfse.cm_key_fragment,
                                         selection_pick_list_name,
                                         cfse.why_field
                                    FROM claim_form cf, 
                                         claim_form_step cfs, 
                                         claim_form_step_element cfse,
                                         claim_cfse cc
                                         /*, -- 04/18/2018
                                         TABLE( CAST( MULTISET(
                                          SELECT SUBSTR(','||cc.value||',',
                                                       INSTR(','||cc.value||',', ',', 1, LEVEL)+1, 
                                                       INSTR(','||cc.value||',', ',', 1, LEVEL+1) - INSTR(','||cc.value||',', ',', 1, LEVEL)-1 
                                                       )
                                                                         
                                            FROM dual
                                         CONNECT BY LEVEL <= REGEXP_COUNT(','||cc.value, ',') 
                                         ) AS sys.odcivarchar2list ) ) p   */                               
                                   WHERE cf.claim_form_id = cfs.claim_form_id
                                     AND cfs.claim_form_step_id = cfse.claim_form_step_id
                                     AND cfse.claim_form_step_element_id = cc.claim_form_step_element_id 
                                     AND cc.value IS NOT NULL
                                     AND claim_form_element_type_code <> 'copy'
                                     and cc.claim_id = p_in_claim_id
                                ))) WHERE rownumber = 1  -- 08/28/2019
                      -- GROUP BY claim_id,claim_cfse_id, sequence_num,why_field,claim_form_step_element_id,claim_form_step_element_name  -- commented out 08/28/2019
               ) cc ;
                 
  
EXCEPTION
   WHEN OTHERS
   THEN
      prc_execution_log_entry (
         'PRC_NOM_MORE_INFO_PAGE',
         1,
         'ERROR',
         'at->'||v_stage||
            p_in_claim_id
         || ': '
         || 'p_in_claim_id'
         || p_in_user_id
         || ': '
         || 'p_in_user_id'
         || CHR (10)
         || SQLERRM,
         NULL);

      OPEN p_out_data FOR SELECT NULL FROM DUAL;

      OPEN p_out_behavior_data FOR SELECT NULL FROM DUAL;

      OPEN p_out_team_data FOR SELECT NULL FROM DUAL;
      
      OPEN p_out_custom_data FOR SELECT NULL FROM DUAL;
END;
/
