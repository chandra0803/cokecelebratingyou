CREATE OR REPLACE PROCEDURE prc_nomination_certificate_dtl
         ( p_in_claim_id             IN  NUMBER,
           p_out_returncode          OUT NUMBER,
           p_out_nom_certificate_dtl OUT SYS_REFCURSOR,
           p_out_team_dtl            OUT SYS_REFCURSOR,
           p_out_custom_data         OUT SYS_REFCURSOR)
AS
 /*******************************************************************************
  -- Purpose: To return the nominations with certificate details
  --
  -- Person             Date         Comments
  -- -----------        --------    -----------------------------------------------------
  -- nagarajs          07/18/2016    Initial Creation       
  -- gorantla          11/27/2018    Bug 78287 - HTML formatting displaying in Certificates
  *******************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_NOMINATION_CERTIFICATE_DTL';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';
   
BEGIN

   
  p_out_returncode := 0;
  
  OPEN p_out_nom_certificate_dtl FOR 
    SELECT CASE WHEN evaluation_type = 'cumulative' THEN evaluation_type
                WHEN evaluation_type <> 'cumulative' AND NC.TEAM_NAME IS NULL THEN 'individual'
           ELSE 'team' 
           END AS claim_type,
           au_r.first_name nominee_first_name,
           au_r.last_name nominee_last_name,
           au.first_name nominator_first_name,
           au.last_name nominator_last_name,
           c.submission_date,
           pnl.level_index level_index,
           pnl.level_label_asset_code level_name,
           nc.team_name,
           pnt.time_period_name_asset_code time_period_name,
           p.promo_name_asset_code promotion_name,
           FNC_FORMAT_COMMENTS (nc.submitter_comments) submitter_comments,  --11/27/2018 
           nc.submitter_comments_lang_id,
           nc.certificate_id
      FROM promotion p,
           promo_nomination pn,
           claim c,
           claim_item ci,
           claim_recipient cr,
           application_user au,
           application_user au_r,
           promo_nomination_level pnl,
           promo_nomination_time_period pnt,
           nomination_claim nc    
     WHERE p.promotion_id = pn.promotion_id
       AND pn.promotion_id = c.promotion_id
       AND c.claim_id = p_in_claim_id
       AND c.claim_id = ci.claim_id
       AND ci.claim_item_id = cr.claim_item_id
       AND c.submitter_id = au.user_id
       AND cr.participant_id = au_r.user_id
       AND c.promotion_id = pnl.promotion_id (+)
       AND c.approval_round = pnl.level_index (+)
       AND c.promotion_id = pnt.promotion_id (+)
       AND (nc.nomination_time_period_id = pnt.nomination_time_period_id OR nc.nomination_time_period_id IS NULL)
       AND c.claim_id = nc.claim_id
       AND nc.team_name IS NULL
     UNION
    SELECT CASE WHEN evaluation_type = 'cumulative' THEN evaluation_type
           WHEN evaluation_type <> 'cumulative' AND NC.TEAM_NAME IS NULL THEN 'individual'
           ELSE 'team' 
           END AS claim_type,
           NULL nominee_first_name,
           NULL nominee_last_name,
           au.first_name nominator_first_name,
           au.last_name nominator_last_name,
           c.submission_date,
           pnl.level_index level_index,
           pnl.level_label_asset_code level_name,
           nc.team_name,
           pnt.time_period_name_asset_code time_period_name,
           p.promo_name_asset_code promotion_name,
           FNC_FORMAT_COMMENTS (nc.submitter_comments) submitter_comments,  --11/27/2018
           nc.submitter_comments_lang_id,
           nc.certificate_id
      FROM promotion p,
           promo_nomination pn,
           claim c,
           application_user au,
           promo_nomination_level pnl,
           promo_nomination_time_period pnt,
           nomination_claim nc    
     WHERE p.promotion_id = pn.promotion_id
       AND pn.promotion_id = c.promotion_id
       AND c.claim_id = p_in_claim_id
       AND c.submitter_id = au.user_id
       AND c.promotion_id = pnl.promotion_id (+)
       AND c.approval_round = pnl.level_index (+)
       AND c.promotion_id = pnt.promotion_id (+)
       AND (nc.nomination_time_period_id = pnt.nomination_time_period_id OR nc.nomination_time_period_id IS NULL)
       AND c.claim_id = nc.claim_id
       AND nc.team_name IS NOT NULL;
   
   OPEN p_out_team_dtl FOR
     SELECT ci.claim_id,
            au_r.first_name nominee_first_name,
           au_r.last_name nominee_last_name
       FROM claim_item ci,
            claim_recipient cr,
            application_user au_r,
            nomination_claim nc
      WHERE ci.claim_id = p_in_claim_id
        AND ci.claim_item_id = cr.claim_item_id
        AND cr.participant_id = au_r.user_id
        AND ci.claim_id  = nc.claim_id
        AND nc.team_name IS NOT NULL;
    
   OPEN p_out_custom_data FOR
     SELECT claim_form_step_element_id,
            claim_form_step_element_name name, 
            cms_value claim_form_value,
            sequence_num,
            why_field
        FROM (SELECT claim_id,
                     claim_cfse_id, 
                     sequence_num,
                     why_field,
                     claim_form_step_element_id,
                     claim_form_step_element_name,
                     LISTAGG(cms_value,', ') WITHIN GROUP(ORDER BY cms_value) cms_value 
                FROM (   SELECT claim_id,
                                claim_cfse_id,
                                claim_form_step_element_id,
                                sequence_num,
                                why_field,
                                cm_asset_code,
                                (SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND INSTR(cms_code,'ELEMENTLABEL_'||cm_key_fragment,1) > 0) claim_form_step_element_name,
                                NVL((SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND (lower(cms_code) = lower(value||'_'||cm_key_fragment) or lower(cms_code) = lower('label'||value||'_'||cm_key_fragment))), value)   cms_value 
                           FROM ( SELECT cc.claim_id,
                                         column_value as value,
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
                                         claim_cfse cc,
                                         TABLE( CAST( MULTISET(
                                          SELECT SUBSTR(','||cc.value||',',
                                                       INSTR(','||cc.value||',', ',', 1, LEVEL)+1, 
                                                       INSTR(','||cc.value||',', ',', 1, LEVEL+1) - INSTR(','||cc.value||',', ',', 1, LEVEL)-1 
                                                       )
                                                                         
                                            FROM dual
                                         CONNECT BY LEVEL <= REGEXP_COUNT(','||cc.value, ',') 
                                         ) AS sys.odcivarchar2list ) ) p                                  
                                   WHERE cf.claim_form_id = cfs.claim_form_id
                                     AND cfs.claim_form_step_id = cfse.claim_form_step_id
                                     AND cfse.claim_form_step_element_id = cc.claim_form_step_element_id 
                                     AND cc.value IS NOT NULL
                                     AND claim_form_element_type_code <> 'copy'
                                     and cc.claim_id = p_in_claim_id
                                ))
                       GROUP BY claim_id,claim_cfse_id, sequence_num,why_field,claim_form_step_element_id,claim_form_step_element_name
               ) cc ;                                             
EXCEPTION 
  WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', SQLERRM, NULL);
      p_out_returncode := 99;
      OPEN p_out_nom_certificate_dtl FOR SELECT NULL FROM DUAL;
      OPEN p_out_team_dtl FOR SELECT NULL FROM DUAL;
      OPEN p_out_custom_data FOR SELECT NULL FROM DUAL;

END;
/
