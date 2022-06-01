CREATE OR REPLACE PROCEDURE PRC_RA_UNUSED_BUD_DETAILS(p_in_giver_id     IN  application_user.user_id%type,
                                                      p_out_return_code OUT NUMBER,
                                                      p_out_user_data   OUT SYS_REFCURSOR) 
 IS
  PRAGMA AUTONOMOUS_TRANSACTION;
    /*******************************************************************************
   -- Purpose: To generate result set for unused budget notifications
   --
   -- Person        Date         Comments
   -- -----------   --------     -----------------------------------------------------
   -- Gorantla      09/04/2018   Initial creation                                                  
   *******************************************************************************/
  c_process_name             CONSTANT execution_log.process_name%TYPE:='PRC_RA_UNUSED_BUD_DETAILS';
  v_stage                    VARCHAR2(400);
  v_days_notused             NUMBER(18);
  v_usa_country_media_value  country.budget_media_value%TYPE;
  v_user_country_media_value country.budget_media_value%TYPE;
  
BEGIN
 
  v_stage := 'Check SA system variable';  
  SELECT int_val
    INTO v_days_notused
    FROM os_propertyset
   WHERE entity_name = 'ra.Program.NumberOfDays.NotUsed';
   
  v_stage := 'Get USA country media value';  
  BEGIN 
    SELECT NVL(budget_media_value,1) 
      INTO v_usa_country_media_value 
      FROM country
     WHERE cm_asset_code = 'country_data.country.usa';
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      v_usa_country_media_value := 1;
  END;
   
  v_stage := 'Get giver country media value'; 
  SELECT NVL(budget_media_value,v_usa_country_media_value)
    INTO v_user_country_media_value
    FROM user_address ua,
         country c
   WHERE ua.country_id = c.country_id
     AND is_primary = 1
     AND user_id = p_in_giver_id;      

  v_stage := 'Get eligible programs list'; 
  DELETE FROM gtt_ra_program;
  INSERT INTO gtt_ra_program
    WITH PROMO AS
    (SELECT p.*,
            pr.award_amount_type_fixed,
            pr.award_amount_fixed,
            pr.award_amount_min
      FROM promotion p,
           promo_recognition pr
     WHERE p.promotion_id = pr.promotion_id
       AND pr.is_include_purl = 0
       AND pr.include_celebrations = 0
       AND promotion_type = 'recognition'
       AND award_type != 'merchandise'
       AND promotion_status = 'live'
       AND (promotion_end_date >= trunc(sysdate) or promotion_end_date IS NULL)
       AND is_fileload_entry = 0
       AND p.award_budget_master_id IS NOT NULL)
    -- Get the list of promotion assocaited to pax
    SELECT *
      FROM 
    (SELECT p.promotion_id,
           promotion_name,
           promotion_status,
           award_budget_master_id,
           primary_audience_type,
           DECODE(secondary_audience_type, 'sameasprimaryaudience',primary_audience_type,secondary_audience_type) secondary_audience_type,
           p.promotion_end_date,
           p.award_amount_type_fixed,
           p.award_amount_fixed,
           p.award_amount_min
      FROM participant_audience pax_aud,
           promo_audience promo_aud,
           promo p
     WHERE pax_aud.audience_id = promo_aud.audience_id 
       AND promo_audience_type = 'PRIMARY' 
       AND promo_aud.promotion_id = p.promotion_id
       AND pax_aud.user_id = p_in_giver_id
    UNION
    -- Get the list of promotion assocaited to pax(all active pax - pax is part of allactive)
    SELECT p.promotion_id,
           promotion_name,
           promotion_status,
           award_budget_master_id,
           primary_audience_type,
           DECODE(secondary_audience_type, 'sameasprimaryaudience',primary_audience_type,secondary_audience_type) secondary_audience_type,
           promotion_end_date,
           p.award_amount_type_fixed,
           p.award_amount_fixed,
           p.award_amount_min
      FROM promo p 
     WHERE p.primary_audience_type = 'allactivepaxaudience')
     -- Get the list of promotion assocaited to receiver pax
     WHERE (secondary_audience_type IN  ('allactivepaxaudience','specifyaudience')
                     OR EXISTS (SELECT 1
                                  FROM promo_audience proa,
                                       participant_audience para 
                                 WHERE proa.audience_id = para.audience_id
                                   AND promotion_id = proa.promotion_id
                                   AND proa.promo_audience_type = 'SECONDARY')
                     );
                       
    v_stage := 'Get eligible list of program details'; 
    OPEN p_out_user_data FOR 
      WITH bud_promo AS
         (SELECT *
           FROM
         (SELECT p.promotion_name,
                 p.promotion_end_date,
                 (CASE WHEN award_amount_type_fixed = 1 AND b.current_value >= award_amount_fixed THEN b.current_value 
                      WHEN award_amount_type_fixed = 0 AND b.current_value >= award_amount_min THEN b.current_value
                 ELSE 0  
                 END)* v_usa_country_media_value/v_user_country_media_value points,
                 c.last_rec_date,
                 bm.budget_type,
                 b.user_id,
                 b.node_id,
                 bs.end_Date,
                 b.current_value
           FROM budget b,
                budget_segment bs,
                budget_master bm,
                gtt_ra_program p,
                (SELECT max(submission_date) last_rec_date,
                        submitter_id,
                        promotion_id
                   FROM claim
                  WHERE submitter_id = p_in_giver_id
                  GROUP BY submitter_id,
                           promotion_id) c
          WHERE b.budget_segment_id = bs.budget_segment_id
            AND bs.budget_master_id = bm.budget_master_id
            AND p.award_budget_master_id =bm.budget_master_id
            AND p.promotion_id = c.promotion_id(+)
            AND trunc(sysdate) between bs.start_Date and nvl(bs.end_date,trunc(sysdate))
            AND b.status = 'active'
            AND b.current_value != 0)
           WHERE points > 0)
              SELECT CASE WHEN days_since_last_rec IS NULL THEN 'Red'
                          WHEN days_since_last_rec <> 0 AND days_since_last_rec <= floor(v_days_notused*1.5) THEN 'Gray'
                          WHEN days_since_last_rec > floor(v_days_notused*1.5) AND days_since_last_rec <= v_days_notused*2 THEN 'Orange'
                          WHEN days_since_last_rec > v_days_notused*2 THEN 'Red'
                     END days_since_last_color,
                     CASE WHEN days_to_promo_exp >= 0 AND days_to_promo_exp <= 15 THEN 'Red'
                          WHEN days_to_promo_exp > 15 AND days_to_promo_exp <= 30 THEN 'Orange'
                          WHEN days_to_promo_exp > 30 THEN 'Gray'
                     END days_to_promo_exp_color,
                     promotion_name,
                     days_since_last_rec,
                     Days_to_promo_exp,
                     floor(points) points,
                     budget_type,
                     last_rec_date
                FROM 
              (SELECT promotion_name,
                     last_rec_date,
                     CASE WHEN last_rec_date IS NOT NULL THEN trunc(sysdate) - trunc(last_rec_date)
                        ELSE  null
                     END days_since_last_rec,
                     points,
                     budget_type,
                     CASE WHEN promotion_end_date IS NULL THEN NULL
                       ELSE trunc(promotion_end_date) - trunc(sysdate)
                     END days_to_promo_exp
                FROM (
                     -- Get list of promo details which are tied to pax budget
                      SELECT *
                        FROM bud_promo
                       WHERE user_id = p_in_giver_id
                      UNION
                      -- Get list of promo details which are tied to node budget
                      SELECT *
                        FROM bud_promo
                       WHERE (node_id IN (SELECT node_id
                                          FROM user_node
                                         WHERE user_id = p_in_giver_id
                                           AND status = 1))
                      UNION
                      -- Get list of promo details which are tied to central budget
                      SELECT *
                        FROM bud_promo
                       WHERE user_id IS NULL
                         AND node_id IS NULL))
             WHERE ( days_since_last_rec IS NULL
                OR days_since_last_rec > v_days_notused)
       ORDER BY days_since_last_rec desc nulls first,
                days_to_promo_exp,
                points desc,
                upper(promotion_name);               
                    
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