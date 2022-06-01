CREATE OR REPLACE PACKAGE pkg_engagement_extract IS
/*******************************************************************************
-- Purpose: To Populate Engagement Detail Data And Summary Data
-- MODIFICATION HISTORY
-- Person      Date            Comments
-- ---------   ----------      ------------------------------------------------
--  Swati      04/21/2014      Creation
*******************************************************************************/  
PROCEDURE p_eng_elig_user(p_user_id        IN  NUMBER,   
   p_in_promotion_id IN NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);
   
   PROCEDURE p_eng_elig_manager(
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);
   
      PROCEDURE p_eng_audience_sync_check(
   p_in_promotion_id IN NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);
   
PROCEDURE p_eng_user_weight_rule(p_user_id        IN  NUMBER,
   p_in_promotion_id IN NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);
   
PROCEDURE p_engagement_score_detail(p_user_id        IN  NUMBER,
  p_in_promotion_id IN NUMBER,
  p_in_start_date     IN  DATE, 
  p_in_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);
                                        
PROCEDURE p_engagement_score_summary(p_user_id        IN  NUMBER,
   p_in_start_date     IN  DATE, 
   p_in_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

PROCEDURE p_engagement_user_recent_recog(p_user_id        IN  NUMBER,
   p_in_promotion_id IN NUMBER,
   p_in_start_date     IN  DATE, 
   p_in_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

PROCEDURE p_engagement_log  (p_user_id        IN  NUMBER,
   p_in_promotion_id IN NUMBER,
   p_in_start_date     IN  DATE, 
   p_in_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

PROCEDURE p_engagement_score_wrapper (pi_requested_user_id  IN  NUMBER,
   po_return_code        OUT NUMBER,
   po_error_message      OUT VARCHAR2
   );

PROCEDURE p_engagement_behavior_summary (p_user_id        IN  NUMBER,
   p_in_start_date     IN  DATE, 
   p_in_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);
   
PROCEDURE p_engagement_behavior_usermode (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);
   
PROCEDURE p_engagement_quart_login_count
 (p_user_id        IN  NUMBER,  
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

PROCEDURE p_engagement_recog_sent (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

PROCEDURE p_engagement_recog_received (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

PROCEDURE p_eng_recog_sent_by_user (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

PROCEDURE p_eng_recog_recvd_by_user (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);
   
PROCEDURE p_eng_recog_sent_user_mode (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

PROCEDURE p_eng_recog_recvd_user_mode (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

PROCEDURE p_eng_recog_sent_by_promo (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

PROCEDURE p_eng_recog_recvd_by_promo (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);
   
PROCEDURE p_eng_recog_sent_bypromo_user (p_user_id        IN  NUMBER, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);
   
PROCEDURE p_eng_recog_recvd_bypromo_user (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);
   
PROCEDURE p_eng_user_connected_to_from(p_in_promotion_id IN NUMBER,
  p_in_start_date   IN  DATE, 
  p_in_end_date     IN  DATE, 
  p_return_code        OUT NUMBER,
  p_error_message      OUT VARCHAR2);   

END pkg_engagement_extract;

/
CREATE OR REPLACE PACKAGE BODY      pkg_engagement_extract IS
    v_stage              VARCHAR2(300);
    c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
    c_created_by         NUMBER := 0;
    --v_sa_enabled         os_propertyset.boolean_val%type;   
/*******************************************************************************
-- Procedure Name: p_eng_elig_manager
-- MODIFICATION HISTORY
-- Person               Date            Comments
-- ---------           ----------      ------------------------------------------------
-- Ravi Dhanekula      06/09/2014      Creation
-- Suresh J            11/20/2014      Bug Fix 58076 - Getting the audience out of sync error while running the Engagement Refresh Process
--Ravi Dhanekula      07/01/2015      Fixed performance issue with the process. 
                     07/20/2016      Replaced obfuscation toolkit with dbms_crypto
  Suresh J	         04/01/2019      SA Integeration with DayMaker. Excludes/Includes PURL/Celebration promotions based on boolean value of new.service.anniversary.enabled                     
*******************************************************************************/                                            

 PROCEDURE p_eng_audience_sync_check(
   p_in_promotion_id IN NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS
   
   v_allactive_aud_count_rpm NUMBER :=0;
   v_allactive_aud_count_elig NUMBER :=0;
   v_spec_aud_mismtach_count NUMBER :=0;
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_eng_audience_sync_check');
   v_sa_enabled         os_propertyset.boolean_val%type;  --04/01/2019
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

   
   BEGIN
        SELECT COUNT(1) INTO v_allactive_aud_count_rpm from promo_eng_rules_audience pera, promo_engagement_rules per
        WHERE pera.rules_id = per.id
        AND per. promotion_id = p_in_promotion_id
        AND pera.audience_type = 'allactivepaxaudience';

        SELECT 1 INTO v_allactive_aud_count_elig FROM promotion p, (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep WHERE pep.promotion_id =p_in_promotion_id   --04/01/2019 
        AND pep.eligible_promotion_id = p.promotion_id
        AND EXISTS (SELECT * FROM promotion WHERE promotion_id = p.promotion_id AND (primary_audience_type  = 'allactivepaxaudience' OR secondary_audience_type = 'allactivepaxaudience'));   --11/20/2014

    EXCEPTION WHEN OTHERS THEN
     v_allactive_aud_count_rpm :=0;
     v_allactive_aud_count_elig :=0;
    END;

IF v_allactive_aud_count_elig > 0 THEN
v_allactive_aud_count_elig := 1;
END IF;

  v_stage := 'Check if allactivepaxaudience audience is in sync';
  IF v_allactive_aud_count_rpm <> v_allactive_aud_count_elig THEN
   p_return_code :=99;
   p_error_message := 'Audience is not in Sync for RPM promotion and its eligible promotions.';
  ELSE IF v_allactive_aud_count_rpm = 0 THEN
 
  v_stage := 'Check if specifyaudience audience is in sync';
  
SELECT COUNT(1) INTO  v_spec_aud_mismtach_count FROM (SELECT pera.audience_id from promo_engagement_rules per,promo_eng_rules_audience pera
WHERE per.promotion_id = p_in_promotion_id
AND per.id = pera.rules_id
MINUS
SELECT audience_id from promo_audience pa,(SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep    --04/01/2019
WHERE pep.promotion_id = p_in_promotion_id
AND pep.eligible_promotion_id = pa.promotion_id);
  END IF ;
  
  END IF;
  IF p_return_code <>99 AND v_spec_aud_mismtach_count <> 0 THEN
  p_return_code :=99;
  p_error_message := 'Audience is not in Sync for RPM promotion and its eligible promotions.';
  END IF;
  EXCEPTION WHEN OTHERS THEN 
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
   END;
   
PROCEDURE p_eng_user_weight_rule(p_user_id        IN  NUMBER,
   p_in_promotion_id IN NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS

 --Ravi and Suresh   04/03/2015    Fine Tunned target calculation for faster performance using BULK processing (New columns addition etc...)
 --chidamba          09/15/2016    Bug 68688 - Lower values are displayed as Target for Audience when user is part of multiple audience and user higher expectation score is set. 
 --Suresh J          04/09/2019    Bug 72640 - p_eng_user_weight_rule merge failure        
   
   v_user_id            dbms_sql.number_table;
   
   v_received_target        dbms_sql.number_table;
   v_sent_target            dbms_sql.number_table;
   v_connected_target       dbms_sql.number_table;
   v_connected_from_target  dbms_sql.number_table;
   v_login_activity_target  dbms_sql.number_table;

   v_received_int_target        dbms_sql.number_table;
   v_sent_int_target            dbms_sql.number_table;
   v_connected_int_target       dbms_sql.number_table;
   v_connected_from_int_target  dbms_sql.number_table;
   v_login_activity_int_target  dbms_sql.number_table;

   v_received_adv_target        dbms_sql.number_table;
   v_sent_adv_target            dbms_sql.number_table;
   v_connected_adv_target       dbms_sql.number_table;
   v_connected_from_adv_target  dbms_sql.number_table;
   v_login_activity_adv_target  dbms_sql.number_table;

   v_received_weight        dbms_sql.number_table;
   v_sent_weight            dbms_sql.number_table;
   v_connected_weight       dbms_sql.number_table;
   v_connected_from_weight  dbms_sql.number_table;
   v_login_activity_weight  dbms_sql.number_table;

    v_score_preference      promo_engagement.score_preference%TYPE;
    v_is_score_active  NUMBER := 0;
    v_rule_count                 NUMBER(10);
    v_bulk_limit     NUMBER := 10000;          --02/25/2016    

    TYPE score_cur_type IS REF CURSOR; 
    cur_score  score_cur_type;

  CURSOR cur_user_score(v_cur_score_pref promo_engagement.score_preference%TYPE )  IS
  SELECT  user_id, 
--        rules_id,
--        rec_rank,
--        sum_targets,
        received_target,sent_target,connected_target,connected_from_target,login_activity_target,
        received_int_target,sent_int_target,connected_int_target,connected_from_int_target,login_activity_int_target,
        received_adv_target,sent_adv_target,connected_adv_target,connected_from_adv_target,login_activity_adv_target,
        received_weight,sent_weight,connected_weight,connected_from_weight,login_activity_weight
    FROM
    (SELECT eru.user_id, 
            per.id rules_id,
            CASE WHEN v_cur_score_pref = 'lowexpectationscore' THEN  
                        RANK() OVER (PARTITION BY eru.user_id ORDER BY (per.received_target + per.sent_target) ASC)
                 WHEN v_cur_score_pref = 'highexpectationscore' THEN  
                        RANK() OVER (PARTITION BY eru.user_id ORDER BY (per.received_target + per.sent_target) DESC)
            END rec_rank,
            per.received_target,
            per.sent_target,
            (per.received_target + per.sent_target) as sum_targets,
            per.connected_target,per.connected_from_target,per.login_activity_target,
            per.received_int_target,per.sent_int_target,per.connected_int_target,per.connected_from_int_target,per.login_activity_int_target,
            per.received_adv_target,per.sent_adv_target,per.connected_adv_target,per.connected_from_adv_target,per.login_activity_adv_target,
            per.received_weight,per.sent_weight,per.connected_weight,per.connected_from_weight,per.login_activity_weight
    FROM promo_engagement_rules per,eng_rules_user eru                
    WHERE per.promotion_id = p_in_promotion_id 
          AND per.id = eru.rule_id
    ) WHERE rec_rank = 1;
   
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_eng_user_weight_rule');
   v_stage VARCHAR2(200);
   
      
   BEGIN
   
   v_stage := 'First MERGE';
   prc_execution_log_entry (c_process_name,c_release_level,'INFO','Start'||v_stage,NULL);    --10/31/2014
   
   EXECUTE IMMEDIATE 'TRUNCATE TABLE ENG_RULES_USER';
   
   SELECT COUNT(1) INTO v_rule_count 
                FROM promo_engagement_rules per WHERE per.promotion_id = p_in_promotion_id ;
                
   SELECT score_preference INTO v_score_preference FROM promo_engagement pe WHERE pe.promotion_id = p_in_promotion_id; 
                
   IF v_rule_count = 1 THEN
                                NULL;                
                ELSE   
   MERGE INTO eng_rules_user d
   USING (
   WITH rule_list AS
            (  -- get driver list of rules
               SELECT per.id rules_id, (received_target + sent_target) target
                 FROM promo_engagement_rules per
               WHERE per.promotion_id = p_in_promotion_id               
            )
    SELECT participant_id,              --09/15/2016 Start
           rules_id
     FROM ( SELECT participant_id,
                   rules_id,
                   CASE WHEN v_score_preference = 'lowexpectationscore' THEN  
                        RANK() OVER (PARTITION BY participant_id ORDER BY target ASC)
                         WHEN v_score_preference = 'highexpectationscore' THEN  
                                RANK() OVER (PARTITION BY participant_id ORDER BY target DESC)
                   END rec_rank
              FROM (                    --09/15/2016 End
              SELECT 
                      p.user_id AS participant_id,
                      pl.rules_id, pl.target --09/15/2016
                 FROM rule_list pl,
                      promo_eng_rules_audience pera,
                      participant p,
                      user_node un
                WHERE pl.rules_id = pera.rules_id
                  AND  pera.audience_type = 'allactivepaxaudience'                   
                  AND p.status = 'active'
                  AND p.user_id = un.user_id
                  AND un.is_primary = 1                                                 
                  UNION                                     --09/15/2016 Replaced UNION ALL              
               SELECT pa.user_id,per.rules_id, per.target   --09/15/2016       
                 FROM promo_eng_rules_audience pera, --promo_engagement_rules per,          --09/15/2016
                      rule_list per,
                      participant_audience PA
                WHERE per.rules_id = pera.rules_id --p_in_promotion_id = per.promotion_id   --09/15/2016
                  AND pera.audience_id = pa.audience_id
                 -- AND  NOT EXISTS (SELECT * FROM promo_eng_rules_audience WHERE audience_type = 'allactivepaxaudience') --09/15/2016
                  UNION                                     --09/15/2016 Replaced UNION ALL
                  SELECT i.participant_id,          
          i.rules_id , i.target --09/15/2016         
     FROM (            
            WITH promo_list AS
            (  -- get driver list of promotions
               SELECT pera.eligible_promotion_id promotion_id,                      
                      per.rules_id, --09/15/2016
                      per.target,   --09/15/2016
                      pera.audience_type
                 FROM rule_list per,--09/15/2016 --promo_engagement_rules
                      promo_eng_rules_audience pera 
                WHERE per.rules_id = pera.rules_id--p_in_promotion_id = per.promotion_id --09/15/2016
               AND pera.eligible_promotion_id IS NOT NULL  
               AND  NOT EXISTS (SELECT * FROM promo_eng_rules_audience WHERE audience_type = 'allactivepaxaudience')   
            ), primary_list AS
            (  SELECT pl.*,
                      pa.user_id AS participant_id,
                   un.node_id                   
                 FROM promo_list pl,
                      promo_audience pra,
                      audience a,
                      participant_audience pa,
                      user_node un                      
                WHERE 
                  pl.promotion_id = pra.promotion_id
                  AND pra.promo_audience_type = 'PRIMARY'
                  AND pra.audience_id = a.audience_id                  
                  AND a.audience_id = pa.audience_id
                  AND pa.user_id = un.user_id
            )            
            , promo_root_node AS
            ( -- get root node level(s) for entire promotion based on secondary audience and giver type
            SELECT pnl.promotion_id,
                   pnl.node_id AS root_node,
                   pnl.rules_id,
                   pnl.target --09/15/2016
--                   , pnl.promotion_type
--                   , pnl.primary_audience_type
                   , pnl.audience_type
              FROM (
                     -- get promotions by node level
                     SELECT DISTINCT
                            pri.promotion_id,
                            pri.node_id,
                            nl.node_level,
                            pri.rules_id,
                            pri.target, --09/15/2016
                            MIN(nl.node_level) OVER (PARTITION BY pri.promotion_id) AS min_node_level
--                            , pri.promotion_type
--                            , pri.primary_audience_type
                            , pri.audience_type
                       from primary_list pri,
                            ( -- get all active nodes by level
                              SELECT node_id,
                                     level AS node_level
                                FROM node
                               WHERE is_deleted = 0
                             CONNECT BY PRIOR node_id = parent_node_id
                               START WITH parent_node_id IS NULL
                            ) nl
                      WHERE pri.node_id = nl.node_id
                        AND pri.audience_type = 'activepaxfromprimarynodebelowaudience'
--                        AND pri.giver_recvr_type = 'giver'
                   ) pnl
             WHERE pnl.node_level = min_node_level
            )
            -- primary audience records
            SELECT p.promotion_id,
                   p.participant_id,
                   p.node_id,
                   p.rules_id,
                   p.target --09/15/2016
                   , p.audience_type
              FROM ( -- get records with pax sequence
                     SELECT pri.promotion_id,
                            pri.participant_id,
                            pri.node_id,
                            pri.rules_id,
                            pri.target --09/15/2016
                            -- sequence records
                            , ROW_NUMBER() OVER (PARTITION BY  pri.promotion_id, pri.participant_id
                                                 ORDER BY pri.node_id
                                                ) AS pax_seq
--                            , pri.promotion_type
--                            , pri.primary_audience_type
                            , pri.audience_type
                       FROM primary_list pri
                   ) p
             UNION ALL            
            SELECT DISTINCT
                   pri.promotion_id,
                   p.user_id AS participant_id,
                   un.node_id,
                   pri.rules_id,
                   pri.target --09/15/2016
                   , pri.audience_type
              FROM primary_list pri,
                   user_node un,
                   participant p
             WHERE pri.audience_type = 'activepaxfromprimarynodeaudience'
               AND pri.node_id = un.node_id
               AND un.user_id = p.user_id
               AND p.status = 'active'
             UNION ALL
            SELECT DISTINCT
                   prn.promotion_id,
                   p.user_id AS participant_id,
                   un.node_id,
                   prn.rules_id,
                   prn.target --09/15/2016
                   , prn.audience_type
              FROM ( -- get nodes and below for all promo root nodes
                     SELECT node_id,
                            sys_connect_by_path(node_id, '/') as node_path
                       FROM node
                      WHERE is_deleted = 0
                    CONNECT BY PRIOR node_id = parent_node_id
                      START WITH node_id IN
                            (SELECT root_node
                               FROM promo_root_node
                            )
                   ) np,
                   participant p,
                   user_node un,
                   promo_root_node prn
             WHERE un.user_id = p.user_id
               and p.status = 'active'
               AND un.node_id = np.node_id
                -- match promo root node to node path root
               AND prn.root_node = NVL(TO_NUMBER(SUBSTR( np.node_path, 2, (INSTR( np.node_path, '/', 2)-2))), np.node_id)               
          ) i
          ))                --09/15/2016
    WHERE rec_rank = 1      --09/15/2016  
          ) s
          ON (d.rule_id = s.rules_id AND d.user_id = s.participant_id)
          WHEN NOT MATCHED THEN 
          INSERT 
          (user_id,
          rule_id,
          date_created,
          created_by)
          VALUES
          (s.participant_id,s.rules_id,SYSDATE,p_user_id);
   
   
   v_stage := 'SCORE Calc';
   prc_execution_log_entry (c_process_name,c_release_level,'INFO','Start'||v_stage,NULL);    --10/31/2014
        
    --Processing the records that has only rule_id in eng_rules_user table.
    
    MERGE INTO eng_rules_user d
     USING (SELECT eru.user_id,eru.rule_id,
  per.received_target,
  per.sent_target,
  per.connected_target,
  per.connected_from_target,
  per.login_activity_target,
  per.received_int_target,
  per.sent_int_target,
  per.connected_int_target,
  per.connected_from_int_target,
  per.login_activity_int_target,
  per.received_adv_target,
  per.sent_adv_target,
  per.connected_adv_target,
  per.connected_from_adv_target,
  per.login_activity_adv_target,
  per.received_weight,
  per.sent_weight,
  per.connected_weight,
  per.connected_from_weight,
  per.login_activity_weight
 FROM eng_rules_user eru, promo_engagement_rules per
WHERE eru.rule_id = per.id                       
)s
    ON (d.user_id = s.user_id AND d.rule_id = s.rule_id)
           WHEN MATCHED THEN UPDATE
SET received_target_month  = received_target,
  sent_target_month      = sent_target,
  connected_target_month     = connected_target,
  connected_from_target_month   = connected_from_target,
  login_activity_target_month   = login_activity_target,
  received_target_quarter     = received_int_target,
  sent_target_quarter          = sent_int_target,
  connected_target_quarter     = connected_int_target,
  connected_from_target_quarter  = connected_from_int_target,
  login_activity_target_quarter  = login_activity_int_target,
  received_target_year         = received_adv_target,
  sent_target_year              = sent_adv_target,
  connected_target_year       = connected_adv_target,
  connected_from_target_year    = connected_from_adv_target,
  login_activity_target_year    = login_activity_adv_target,
  received_weight     = s.received_weight,
  sent_weight         = s.sent_weight,
  connected_weight    = s.connected_weight,
  d.connected_from_weight    = s.connected_from_weight,
  d.login_activity_weight = s.login_activity_weight
WHEN NOT MATCHED THEN INSERT
(user_id,
 rule_id,
 received_target_month,
  sent_target_month,
  connected_target_month,
  connected_from_target_month,
  login_activity_target_month,
  received_target_quarter,
  sent_target_quarter,
  connected_target_quarter,
  connected_from_target_quarter,
  login_activity_target_quarter,
  received_target_year,
  sent_target_year,
  connected_target_year,
  connected_from_target_year,
  login_activity_target_year,
  received_weight,
  sent_weight,
  connected_weight,
  d.connected_from_weight,
  d.login_activity_weight)
  VALUES
  (s.user_id,
 s.rule_id,
 s.received_target,
 s.sent_target,
  s.connected_target,
  s.connected_from_target,
  s.login_activity_target,
  s.received_int_target,
  s.sent_int_target,
  s.connected_int_target,
  s.connected_from_int_target,
  s.login_activity_int_target,
  s.received_adv_target,
  s.sent_adv_target,
  s.connected_adv_target,
  s.connected_from_adv_target,
  s.login_activity_adv_target,
  s.received_weight,
  s.sent_weight,
  s.connected_weight,
  s.connected_from_weight,
  s.login_activity_weight );
     
  commit;
  
   v_stage := 'FOR LOOP';
   prc_execution_log_entry (c_process_name,c_release_level,'INFO',' Start'||v_stage,NULL);    --10/31/2014
   
    OPEN cur_user_score(v_score_preference);                        --03/19/2015
    
    LOOP    --02/25/2016
    
    FETCH cur_user_score BULK COLLECT          
     INTO   v_user_id,
            v_received_target,
            v_sent_target,
            v_connected_target,
            v_connected_from_target,
            v_login_activity_target,
            v_received_int_target,
            v_sent_int_target,
            v_connected_int_target,
            v_connected_from_int_target,
            v_login_activity_int_target,
            v_received_adv_target,
            v_sent_adv_target,
            v_connected_adv_target,
            v_connected_from_adv_target,
            v_login_activity_adv_target,
            v_received_weight,
            v_sent_weight,
            v_connected_weight,
            v_connected_from_weight,
            v_login_activity_weight LIMIT v_bulk_limit;    --02/25/2016


        IF v_user_id.COUNT > 0 THEN    
            FORALL indx IN v_user_id.FIRST .. v_user_id.LAST         --03/19/2015
                UPDATE ENG_RULES_USER a
                SET  received_target_month = v_received_target(indx),
                     sent_target_month = v_sent_target(indx),
                     connected_target_month = v_connected_target(indx),
                     connected_from_target_month = v_connected_from_target(indx),
                     login_activity_target_month = v_login_activity_target(indx),
                     received_weight = v_received_weight(indx),
                     sent_weight = v_sent_weight(indx),
                     connected_weight = v_connected_weight(indx),
                     connected_from_weight = v_connected_from_weight(indx),
                     login_activity_weight = v_login_activity_weight(indx),
                     received_target_quarter = v_received_int_target(indx),
                     sent_target_quarter = v_sent_int_target(indx),
                     connected_target_quarter = v_connected_int_target(indx),
                     connected_from_target_quarter = v_connected_from_int_target(indx),
                     login_activity_target_quarter = v_login_activity_int_target(indx),
                     received_target_year = v_received_adv_target(indx),
                     sent_target_year = v_sent_adv_target(indx),
                     connected_target_year = v_connected_adv_target(indx),
                     connected_from_target_year = v_connected_from_adv_target(indx),
                     login_activity_target_year = v_login_activity_adv_target(indx)
                WHERE a.user_id = v_user_id(indx);          
        END IF; --if v_user_id.COUNT         

        EXIT WHEN v_user_id.COUNT = 0;   --02/25/2016

    END LOOP;   --02/25/2016
     
    CLOSE cur_user_score;
    prc_execution_log_entry (c_process_name,c_release_level,'INFO',' END CODE'||v_stage,NULL);    --10/31/2014
                
   END IF;--v_rule_count =1
      v_stage := 'End of Procedure RUN';
      p_return_code := 0;
      prc_execution_log_entry (c_process_name,c_release_level,'INFO','END'||v_stage,NULL);    --10/31/2014
      
    EXCEPTION WHEN OTHERS THEN 
     prc_execution_log_entry (c_process_name,c_release_level,'ERROR','Error at '||v_stage||'->'||SQLERRM,NULL);    
     p_return_code :=99;
     p_error_message := SQLERRM;
  END;

PROCEDURE p_eng_elig_user(p_user_id        IN  NUMBER,   
   p_in_promotion_id IN NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS

/*******************************************************************************
-- Procedure Name: p_eng_elig_manager
-- MODIFICATION HISTORY
-- Person               Date            Comments
-- ---------           ----------      ------------------------------------------------
-- Ravi Dhanekula      06/09/2014      Creation
-- Swati               05/21/2015      Bug 62397 - RPM Team Dashboard shows incorrect Pax count and inactive Pax as team memeber
   Suresh J	           04/01/2019      SA Integeration with DayMaker. Excludes/Includes PURL/Celebration promotions based on boolean value of new.service.anniversary.enabled
*******************************************************************************/                                               
   
   v_allactive_aud_count         NUMBER;
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_eng_elig_user');
   v_sa_enabled         os_propertyset.boolean_val%type;    --04/01/2019
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

    BEGIN
        SELECT COUNT(1) INTO v_allactive_aud_count from promo_eng_rules_audience pera, promo_engagement_rules per
        WHERE pera.rules_id = per.id
        AND per. promotion_id = p_in_promotion_id
        AND pera.audience_type = 'allactivepaxaudience';
    END;

--Deleting records for Obsolete Node and User combo           --05/21/2015 Bug 62397
DELETE 
  FROM engagement_elig_user eeu
 WHERE (NOT EXISTS
          (SELECT un.user_id
             FROM user_node un
            WHERE un.user_id = eeu.user_id AND un.node_id = eeu.node_id) 
        OR 
       EXISTS (select au.user_id from application_user au where au.is_active = 0 and au.user_id = eeu.user_id)) ;
       
IF v_allactive_aud_count = 1 THEN --If allactiveparticipants are audience.

MERGE INTO engagement_elig_user d
USING (SELECT pax.user_id,un.node_id,un.status FROM participant pax, user_node un
WHERE pax.status = 'active'
AND pax.user_id = un.user_id ) s
ON (d.user_id = s.user_id AND s.node_id = d.node_id)
WHEN MATCHED THEN
UPDATE SET status = s.status
WHEN NOT MATCHED THEN
INSERT
(user_id,
 node_id,
 status,
 date_created,
 created_by)
 VALUES
 (s.user_id,
  s.node_id,
  1,
  SYSDATE,
  5662);

ELSE

MERGE INTO engagement_elig_user d
USING (
SELECT DISTINCT
          i.participant_id,
          i.node_id
     FROM (
            WITH promo_list AS
            (  -- get driver list of promotions
               SELECT p.promotion_id,
                      p.promotion_type,
                      LOWER(p.primary_audience_type) AS primary_audience_type,
                      LOWER(p.secondary_audience_type) AS secondary_audience_type
                 FROM promotion p,(SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep  --04/01/2019
               WHERE p.promotion_id = pep.eligible_promotion_id AND p.promotion_type = 'recognition'
                  AND pep.promotion_id = p_in_promotion_id
                  AND p.promotion_status IN ('live','expired')
                  AND p.is_deleted = 0  
            ), primary_list AS
            (  
               SELECT pl.*,
                      pa.user_id AS participant_id,
                      un.node_id,
                      a.audience_id
                 FROM promo_list pl,
                      promo_audience pra,
                      audience a,
                      participant_audience pa,
                      user_node un
                WHERE pl.primary_audience_type = 'specifyaudience'
                  AND pl.promotion_id = pra.promotion_id
                  AND pra.promo_audience_type = 'PRIMARY'
                  AND pra.audience_id = a.audience_id
                  AND a.audience_id = pa.audience_id
                  AND pa.user_id = un.user_id
            ), promo_root_node AS
            ( -- get root node level(s) for entire promotion based on secondary audience and giver type
            SELECT pnl.promotion_id,
                   pnl.node_id AS root_node
                   , pnl.promotion_type
                   , pnl.primary_audience_type
                   , pnl.secondary_audience_type
              FROM (
                     -- get promotions by node level
                     SELECT DISTINCT
                            pri.promotion_id,
                            pri.node_id,
                            nl.node_level,
                            MIN(nl.node_level) OVER (PARTITION BY pri.promotion_id) AS min_node_level
                            , pri.promotion_type
                            , pri.primary_audience_type
                            , pri.secondary_audience_type
                       from primary_list pri,
                            ( -- get all active nodes by level
                              SELECT node_id,
                                     level AS node_level
                                FROM node
                               WHERE is_deleted = 0
                             CONNECT BY PRIOR node_id = parent_node_id
                               START WITH parent_node_id IS NULL
                            ) nl
                      WHERE pri.node_id = nl.node_id
                        AND pri.secondary_audience_type = 'activepaxfromprimarynodebelowaudience'
--                        AND pri.giver_recvr_type = 'giver'
                   ) pnl
             WHERE pnl.node_level = min_node_level
            )
            -- primary audience records
            SELECT p.promotion_id,
                   p.participant_id,
                   p.node_id,
                   p.audience_id
                   , p.promotion_type
                   , p.primary_audience_type
                   , p.secondary_audience_type
              FROM ( -- get records with pax sequence
                     SELECT pri.promotion_id,
                            pri.participant_id,
                            pri.node_id,
                            pri.audience_id
                            -- sequence records
                            , ROW_NUMBER() OVER (PARTITION BY  pri.promotion_id, pri.participant_id
                                                 ORDER BY pri.node_id
                                                ) AS pax_seq
                            , pri.promotion_type
                            , pri.primary_audience_type
                            , pri.secondary_audience_type
                       FROM primary_list pri
                   ) p
             WHERE 
                       pax_seq = 1
             UNION ALL          
            SELECT pl.promotion_id,
                   pa.user_id AS participant_id,
                   un.node_id,
                   a.audience_id
                   , pl.promotion_type
                   , pl.primary_audience_type
                   , pl.secondary_audience_type
              FROM promo_list pl,
                   promo_audience pra,
                   audience a,
                   participant_audience pa,
                   user_node un
             WHERE pl.secondary_audience_type = 'specifyaudience'
               AND pl.promotion_id = pra.promotion_id
               AND pra.promo_audience_type = 'SECONDARY'
               AND pra.audience_id = a.audience_id
               --AND a.list_type = 'pax'
               AND a.audience_id = pa.audience_id
               AND pa.user_id = un.user_id
             UNION ALL
            -- secondary audience SameAsPrimaryAudience records
            -- get reciever participants based on primary list giver
            SELECT pri.promotion_id,
                   pri.participant_id,
                   pri.node_id,
                   pri.audience_id
--                   'receiver' AS giver_recvr_type
                   , pri.promotion_type
                   , pri.primary_audience_type
                   , pri.secondary_audience_type
              FROM primary_list pri
             WHERE pri.secondary_audience_type = 'sameasprimaryaudience'
--               AND pri.giver_recvr_type = 'giver'
             UNION ALL
            -- secondary audience ActivePaxFromPrimaryNodeAudience records
            -- get reciever participants based on primary list giver node
            SELECT DISTINCT
                   pri.promotion_id,
                   p.user_id AS participant_id,
                   un.node_id,
                   0 AS audience_id
--                   'receiver' AS giver_recvr_type
                   , pri.promotion_type
                   , pri.primary_audience_type
                   , pri.secondary_audience_type
              FROM primary_list pri,
                   user_node un,
                   participant p
             WHERE pri.secondary_audience_type = 'activepaxfromprimarynodeaudience'
--               AND pri.giver_recvr_type = 'giver'
               AND pri.node_id = un.node_id
               AND un.user_id = p.user_id
               AND p.status = 'active'
             UNION ALL
            -- secondary audience ActivePaxFromPrimaryNodeBelowAudience records
            -- get reciever participants based on primary list giver root node
            SELECT DISTINCT
                   prn.promotion_id,
                   p.user_id AS participant_id,
                   un.node_id,
                   0 AS audience_id
--                   'receiver' AS giver_recvr_type
                   , prn.promotion_type
                   , prn.primary_audience_type
                   , prn.secondary_audience_type
              FROM ( -- get nodes and below for all promo root nodes
                     SELECT node_id,
                            sys_connect_by_path(node_id, '/') as node_path
                       FROM node
                      WHERE is_deleted = 0
                    CONNECT BY PRIOR node_id = parent_node_id
                      START WITH node_id IN
                            (SELECT root_node
                               FROM promo_root_node
                            )
                   ) np,
                   participant p,
                   user_node un,
                   promo_root_node prn
             WHERE un.user_id = p.user_id
               and p.status = 'active'
               AND un.node_id = np.node_id
                -- match promo root node to node path root
               AND prn.root_node = NVL(TO_NUMBER(SUBSTR( np.node_path, 2, (INSTR( np.node_path, '/', 2)-2))), np.node_id)
          ) i ) s
ON (d.user_id = s.participant_id AND s.node_id = d.node_id)
WHEN MATCHED THEN
UPDATE SET status = 1
WHEN NOT MATCHED THEN
INSERT
(user_id,
 node_id,
 status,
 date_created,
 created_by)
 VALUES
 (s.participant_id,
  s.node_id,
  1,
  SYSDATE,
  5662);

END IF;
v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
END p_eng_elig_user;

/*******************************************************************************
-- Procedure Name: p_eng_elig_manager
-- MODIFICATION HISTORY
-- Person                  Date            Comments
-- ---------             ----------      ------------------------------------------------
--  Ravi Dhanekula       06/09/2014      Creation
--  Suresh J             10/15/2014      Bug Fix 56187 - Added Users Count in Node and Below for each Manager's node 
--  Suresh J             11/25/2014      RPM Performance Tuning - Re-wrote the entire SQL Query for faster SQL response time 
*******************************************************************************/                                            

PROCEDURE p_eng_elig_manager(p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS
  
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_eng_elig_manager');
 
BEGIN
  
EXECUTE IMMEDIATE 'TRUNCATE TABLE ENGAGEMENT_ELIG_MANAGER';
--INSERT INTO ENGAGEMENT_ELIG_MANAGER (user_id,date_created,created_by)    --10/15/2014
INSERT INTO ENGAGEMENT_ELIG_MANAGER (user_id,date_created,created_by,Node_Users_Count)
select user_id,SYSDATE,5662,SUM(Node_And_Below_User_Count) as Node_And_Below_User_Count    -- 11/25/2014      
from (select  un_mgr.user_id,SUM(User_Count) as Node_And_Below_User_Count
                        FROM                        (SELECT np.node_id,
                                                       p.column_value AS path_node_id
                                                  FROM ( -- get node hierarchy path
                                                         SELECT h.node_id,
                                                                level AS hier_level,
                                                                sys_connect_by_path(node_id, '/') || '/' AS node_path
                                                           FROM node h
                                                          START WITH h.parent_node_id IS NULL
                                                        CONNECT BY PRIOR h.node_id = h.parent_node_id
                                                       ) np,
                                                       -- parse node path into individual nodes
                                                       -- pivoting the node path into separate records
                                                       TABLE( CAST( MULTISET(
                                                          SELECT TO_NUMBER(
                                                                    SUBSTR(np.node_path,
                                                                           INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                                                           INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                                                    )
                                                                 )
                                                            FROM dual
                                                         CONNECT BY LEVEL <= np.hier_level 
                                                       ) AS sys.odcinumberlist ) ) p) npn,
                                                       (select node_id,count(*) as user_count from engagement_elig_user group by node_id) eeu,
                                                       (SELECT node_id,NAME as node_name,parent_node_id      
                                                        FROM node n
                                                        START WITH parent_node_id IS NULL
                                                        CONNECT BY PRIOR node_id = parent_node_id) ip,
                                                        (select node_id,user_id from user_node where role in ('own','mgr')) un_mgr
                        where ip.node_id = npn.node_id
                              AND ip.node_id = eeu.node_id
                              AND npn.path_node_id = un_mgr.node_id 
                        group by npn.path_node_id,un_mgr.user_id) 
group by user_id;                          
             
v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
END p_eng_elig_manager;
PROCEDURE p_engagement_score_detail(p_user_id        IN  NUMBER,
   p_in_promotion_id IN NUMBER,
   p_in_start_date     IN  DATE, 
   p_in_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS
/*******************************************************************************
-- Purpose: To Populate Engagement Detail Data
-- MODIFICATION HISTORY
-- Person              Date            Comments
-- ---------           ----------      ------------------------------------------------
--  Swati             04/21/2014    Creation
--  Ravi Dhanekula    06/16/2014    Changes done as per latest requirements.
                                    Default start date is from first login_activity record.
                                    Process to be updated to calculate data(actual,target and Score) on 3-monthly and yearly as well.
                      08/18/2014    Fixed the bug # 55839
--  Swati             08/28/2014    Bug 56066 - RPM - Promotion Setup - Scores Page - "If PAX In Multiple Audience" field functionality is not working                           
--  Swati             09/01/2014    Bug 56135 - RPM - My Dashboard - Recognition received Count is displayed wrongly when the promotion has Manual approval
--  Suresh J          10/31/2014    Changes done as part of RPM Refresh Performance Tunning
                                    i.Replaced 4 Target Secore Cursors with One Global Cursor
                                    ii.Added UPDATE Score statements for NULL Targets
                                    iii.Added/Correced log entries for execution_log inserts    
--  Ravi and Suresh   04/03/2015    Fine Tunned score calculation for faster performance using BULK processing (using new a score staging table, newly added columns etc...)                                                   
-- Swati              04/10/2015    Bug 61253 - RPM: The Score is not calculated/displayed in the Dashboard
-- Ravi Dhanekula     07/06/2015    Bug # 63144 Data Details are not displayed in My Dashboard and Team Dashboard
--Suresh J            02/25/2016    Bug 65873 (WIP 12111) RPM Refresh process failed with PGA memory error
-- Ramkumar           08/28/2018    Bug 73671 engagement_score_detail not getting updated if start date > first of month
   Suresh J	          04/01/2019    SA Integeration with DayMaker. Excludes/Includes PURL/Celebration promotions based on boolean value of new.service.anniversary.enabled
*******************************************************************************/    
    
    c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_engagement_score_detail');    

    v_received_target        NUMBER;
    v_sent_target            NUMBER;
    v_connected_target       NUMBER;
    v_connected_from_target  NUMBER;
    v_login_activity_target  NUMBER;
    v_received_weight        NUMBER;
    v_sent_weight            NUMBER;
    v_connected_weight       NUMBER;
    v_connected_from_weight  NUMBER;
    v_login_activity_weight  NUMBER;
    
    v_received_int_target  NUMBER;
    v_sent_int_target  NUMBER;
    v_connected_int_target  NUMBER;
    v_connected_from_int_target  NUMBER;
    v_login_activity_int_target  NUMBER;
    v_received_adv_target  NUMBER;
    v_sent_adv_target  NUMBER;
    v_connected_adv_target  NUMBER;
    v_connected_from_adv_target  NUMBER;
    v_login_activity_adv_target  NUMBER;    
                
                
    v_rules_id            NUMBER;
    v_rules_id_final      NUMBER;
    
    received_score          NUMBER := 0;
    sent_score              NUMBER := 0;
    connected_score         NUMBER := 0;
    connected_from_score    NUMBER := 0;
    login_activity_score    NUMBER := 0;
    v_score                 NUMBER := 0;
    v_is_score_active  NUMBER := 0;
    v_count_aud_id          NUMBER := 0;
    v_score_preference      promo_engagement.score_preference%TYPE;
    I                       NUMBER := 0;
    v_sum_targets_prev      NUMBER := 0;
    v_sum_targets           NUMBER := 0;      
          
    p_start_date             DATE :=p_in_start_date;
    P_default_start_date     DATE;
    p_quarter_start_date     DATE;
    p_year_start_date         DATE;
    p_month_start_date         DATE;
    p_end_date              DATE :=SYSDATE;
    
    v_company_goal            NUMBER(3);
    v_rule_count                 NUMBER(10);
    
    v_user_id   dbms_sql.number_table;
    v_eng_score_detail_id   dbms_sql.number_table;
    v_node_id   dbms_sql.number_table;    
    v_trans_month   dbms_sql.number_table;
    v_trans_year   dbms_sql.number_table;    
    v_total_score  dbms_sql.number_table;
    
    vt_received_target_mon dbms_sql.number_table;
    vt_sent_target_mon     dbms_sql.number_table;
    vt_connected_to_target_mon   dbms_sql.number_table; 
    vt_connected_from_target_mon dbms_sql.number_table;
    vt_login_activity_target_mon dbms_sql.number_table;

    vt_received_target_quarter dbms_sql.number_table;
    vt_sent_target_quarter     dbms_sql.number_table;
    vt_connected_to_target_quarter   dbms_sql.number_table; 
    vt_connected_from_target_qtr dbms_sql.number_table;
    vt_login_activity_target_qtr dbms_sql.number_table;

    vt_received_target_year dbms_sql.number_table;
    vt_sent_target_year     dbms_sql.number_table;
    vt_connected_to_target_year   dbms_sql.number_table; 
    vt_connected_from_target_year dbms_sql.number_table;
    vt_login_activity_target_year dbms_sql.number_table;

    vt_received_weight       dbms_sql.number_table;
    vt_sent_weight           dbms_sql.number_table;
    vt_connected_to_weight   dbms_sql.number_table; 
    vt_connected_from_weight dbms_sql.number_table;
    vt_login_activity_weight dbms_sql.number_table;
    
    v_return_code     NUMBER;
    v_error_message  VARCHAR2(1000);
    v_bulk_limit     NUMBER := 10000;          --02/25/2016
    exit_proc_exception EXCEPTION;             --04/09/2019     
    v_sa_enabled         os_propertyset.boolean_val%type;   --04/01/2019
    TYPE score_cur_type IS REF CURSOR; 
    cur_score   score_cur_type;  
    
    --    picks actual counts to calculate score for monthly data
    CURSOR cur_user_score
    IS
    SELECT engagement_detail_id,user_id,node_id,trans_month,trans_year
           ,received_target_month, sent_target_month, connected_target_month, connected_from_target_month, login_activity_target_month
           ,received_weight, sent_weight, connected_weight, connected_from_weight, login_activity_weight
           ,(received_score + sent_score + connected_score + connected_from_score + login_activity_score) as total_score 
    FROM
    (SELECT  esd.engagement_detail_id,esd.user_id,esd.node_id,esd.trans_month,esd.trans_year,
            esd.received_count,esd.sent_count,
            esd.connected_to_count,esd.connected_from_count,
            esd.login_activity_count
           ,eru.received_weight, eru.sent_weight, eru.connected_weight, eru.connected_from_weight, eru.login_activity_weight            
           ,eru.received_target_month, eru.sent_target_month, eru.connected_target_month, eru.connected_from_target_month, eru.login_activity_target_month
            ,CASE  WHEN esd.received_count <> 0 AND esd.received_count < eru.received_target_month THEN     --03/19/2015        
                        eru.received_weight/(eru.received_target_month/esd.received_count)
                   WHEN esd.received_count >= eru.received_target_month THEN
                        eru.received_weight 
                   ELSE 0     
             END received_score
            ,CASE WHEN esd.sent_count <> 0 AND esd.sent_count < eru.sent_target_month THEN     --03/19/2015        
                        eru.sent_weight/(eru.sent_target_month/esd.sent_count)
                  WHEN esd.sent_count >= eru.sent_target_month THEN
                       eru.sent_weight
                  ELSE 0                         
             END sent_score
            ,CASE WHEN esd.connected_to_count <> 0 AND esd.connected_to_count < eru.connected_target_month THEN      --03/19/2015
                       eru.connected_weight/(eru.connected_target_month/esd.connected_to_count) 
                  WHEN esd.connected_to_count >= eru.connected_target_month THEN
                       eru.connected_weight
                  ELSE 0                       
             END  connected_score
            ,CASE WHEN esd.connected_from_count <> 0 AND esd.connected_from_count < eru.connected_from_target_month THEN      --03/19/2015
                       eru.connected_from_weight/(eru.connected_from_target_month/esd.connected_from_count) 
                  WHEN esd.connected_from_count >= eru.connected_from_target_month THEN
                      eru.connected_from_weight
                  ELSE 0                      
             END  connected_from_score
            ,CASE WHEN esd.login_activity_count <> 0 AND esd.login_activity_count < eru.login_activity_target_month THEN      --03/19/2015
                       eru.login_activity_weight/(eru.login_activity_target_month/esd.login_activity_count)
                  WHEN esd.login_activity_count >= eru.login_activity_target_month THEN
                       eru.login_activity_weight
                  ELSE 0                        
             END login_activity_score
    FROM eng_stage_score_detail esd, eng_rules_user eru,gtt_months_between_dates --04/10/2015    Bug 61253
    WHERE esd.time_frame  = 'month' 
        AND esd.user_id = eru.user_id  
        AND TO_DATE(esd.trans_month||'/'||1||'/'||esd.trans_year,'MM/DD/YYYY') BETWEEN MONTH_START_DATE AND nvl(MONTH_END_DATE,sysdate) --04/10/2015    Bug 61253
    );
        
        --    picks actual counts to calculate score for quarterly data
    CURSOR cur_user_score_quarter
    IS
    SELECT engagement_detail_id,user_id,node_id,trans_month,trans_year
           ,received_target_quarter, sent_target_quarter, connected_target_quarter, connected_from_target_quarter, login_activity_target_quarter
           ,received_weight, sent_weight, connected_weight, connected_from_weight, login_activity_weight
           ,(received_score + sent_score + connected_score + connected_from_score + login_activity_score) as total_score 
    FROM
    (SELECT  esd.engagement_detail_id,esd.user_id,esd.node_id,esd.trans_month,esd.trans_year,
            esd.received_count,esd.sent_count,
            esd.connected_to_count,esd.connected_from_count,
            esd.login_activity_count
           ,eru.received_weight, eru.sent_weight, eru.connected_weight, eru.connected_from_weight, eru.login_activity_weight            
           ,eru.received_target_quarter, eru.sent_target_quarter, eru.connected_target_quarter, eru.connected_from_target_quarter, eru.login_activity_target_quarter
            ,CASE  WHEN esd.received_count <> 0 AND esd.received_count < eru.received_target_quarter THEN     --03/19/2015        
                        eru.received_weight/(eru.received_target_quarter/esd.received_count)
                   WHEN esd.received_count >= eru.received_target_quarter THEN
                        eru.received_weight 
                   ELSE 0     
             END received_score
            ,CASE WHEN esd.sent_count <> 0 AND esd.sent_count < eru.sent_target_quarter THEN     --03/19/2015        
                        eru.sent_weight/(eru.sent_target_quarter/esd.sent_count)
                  WHEN esd.sent_count >= eru.sent_target_quarter THEN
                       eru.sent_weight
                  ELSE 0                         
             END sent_score
            ,CASE WHEN esd.connected_to_count <> 0 AND esd.connected_to_count < eru.connected_target_quarter THEN      --03/19/2015
                       eru.connected_weight/(eru.connected_target_quarter/esd.connected_to_count) 
                  WHEN esd.connected_to_count >= eru.connected_target_quarter THEN
                       eru.connected_weight
                  ELSE 0                       
             END  connected_score
            ,CASE WHEN esd.connected_from_count <> 0 AND esd.connected_from_count < eru.connected_from_target_quarter THEN      --03/19/2015
                       eru.connected_from_weight/(eru.connected_from_target_quarter/esd.connected_from_count) 
                  WHEN esd.connected_from_count >= eru.connected_from_target_quarter THEN
                      eru.connected_from_weight
                  ELSE 0                      
             END  connected_from_score
            ,CASE WHEN esd.login_activity_count <> 0 AND esd.login_activity_count < eru.login_activity_target_quarter THEN      --03/19/2015
                       eru.login_activity_weight/(eru.login_activity_target_quarter/esd.login_activity_count)
                  WHEN esd.login_activity_count >= eru.login_activity_target_quarter THEN
                       eru.login_activity_weight
                  ELSE 0                        
             END login_activity_score
    FROM eng_stage_score_detail esd, eng_rules_user eru,gtt_months_between_dates --04/10/2015    Bug 61253
    WHERE esd.time_frame  = 'quarter' 
        AND esd.user_id = eru.user_id  
        AND TO_DATE(esd.trans_month||'/'||1||'/'||esd.trans_year,'MM/DD/YYYY') BETWEEN QUARTER_START_DATE AND nvl(MONTH_END_DATE,sysdate) --04/10/2015    Bug 61253
    );

    --    picks actual counts to calculate score for Yearly data
    CURSOR cur_user_score_annual
    IS
    SELECT engagement_detail_id,user_id,node_id,trans_month,trans_year
           ,received_target_year, sent_target_year, connected_target_year, connected_from_target_year, login_activity_target_year
           ,received_weight, sent_weight, connected_weight, connected_from_weight, login_activity_weight
           ,(received_score + sent_score + connected_score + connected_from_score + login_activity_score) as total_score 
    FROM
    (SELECT  esd.engagement_detail_id,esd.user_id,esd.node_id,esd.trans_month,esd.trans_year,
            esd.received_count,esd.sent_count,
            esd.connected_to_count,esd.connected_from_count,
            esd.login_activity_count
           ,eru.received_weight, eru.sent_weight, eru.connected_weight, eru.connected_from_weight, eru.login_activity_weight            
           ,eru.received_target_year, eru.sent_target_year, eru.connected_target_year, eru.connected_from_target_year, eru.login_activity_target_year
            ,CASE  WHEN esd.received_count <> 0 AND esd.received_count < eru.received_target_year THEN     --03/19/2015        
                        eru.received_weight/(eru.received_target_year/esd.received_count)
                   WHEN esd.received_count >= eru.received_target_year THEN
                        eru.received_weight 
                   ELSE 0     
             END received_score
            ,CASE WHEN esd.sent_count <> 0 AND esd.sent_count < eru.sent_target_year THEN     --03/19/2015        
                        eru.sent_weight/(eru.sent_target_year/esd.sent_count)
                  WHEN esd.sent_count >= eru.sent_target_year THEN
                       eru.sent_weight
                  ELSE 0                         
             END sent_score
            ,CASE WHEN esd.connected_to_count <> 0 AND esd.connected_to_count < eru.connected_target_year THEN      --03/19/2015
                       eru.connected_weight/(eru.connected_target_year/esd.connected_to_count) 
                  WHEN esd.connected_to_count >= eru.connected_target_year THEN
                       eru.connected_weight
                  ELSE 0                       
             END  connected_score
            ,CASE WHEN esd.connected_from_count <> 0 AND esd.connected_from_count < eru.connected_from_target_year THEN      --03/19/2015
                       eru.connected_from_weight/(eru.connected_from_target_year/esd.connected_from_count) 
                  WHEN esd.connected_from_count >= eru.connected_from_target_year THEN
                      eru.connected_from_weight
                  ELSE 0                      
             END  connected_from_score
            ,CASE WHEN esd.login_activity_count <> 0 AND esd.login_activity_count < eru.login_activity_target_year THEN      --03/19/2015
                       eru.login_activity_weight/(eru.login_activity_target_year/esd.login_activity_count)
                  WHEN esd.login_activity_count >= eru.login_activity_target_year THEN
                       eru.login_activity_weight
                  ELSE 0                        
             END login_activity_score
    FROM eng_stage_score_detail esd, eng_rules_user eru,gtt_months_between_dates --04/10/2015    Bug 61253
    WHERE esd.time_frame  = 'year' 
        AND esd.user_id = eru.user_id  
        AND TO_DATE(esd.trans_month||'/'||1||'/'||esd.trans_year,'MM/DD/YYYY') BETWEEN YEAR_START_DATE AND nvl(MONTH_END_DATE,sysdate)--04/10/2015    Bug 61253
    );

        
        CURSOR cur_user_score_rc --(When rule count is 1)
          IS
            SELECT engagement_detail_id,user_id,node_id,trans_month,trans_year
            ,received_target,sent_target,connected_target,connected_from_target,login_activity_target
            ,received_weight,sent_weight,connected_weight,connected_from_weight,login_activity_weight
            ,ROUND(received_score + sent_score + connected_score + connected_from_score + login_activity_score) as total_score 
            FROM
            (SELECT  engagement_detail_id,esd.user_id,esd.node_id,esd.trans_month,esd.trans_year,
                    esd.received_count,esd.sent_count,
                    esd.connected_to_count,esd.connected_from_count,
                    esd.login_activity_count
                    ,per.received_target,per.sent_target,per.connected_target,per.connected_from_target,per.login_activity_target,
                    per.received_weight,per.sent_weight,per.connected_weight,per.connected_from_weight,per.login_activity_weight
                    ,CASE  WHEN esd.received_count <> 0 AND esd.received_count < per.received_target THEN     --03/19/2015        
                           per.received_weight/(per.received_target/esd.received_count)
                           WHEN esd.received_count >= per.received_target THEN
                           per.received_weight        
                           ELSE 0                    
                     END received_score
                    ,CASE  WHEN esd.sent_count <> 0 AND esd.sent_count < per.sent_target THEN     --03/19/2015        
                           per.sent_weight/(per.sent_target/esd.sent_count)
                           WHEN esd.sent_count >= per.sent_target THEN
                           per.sent_weight 
                           ELSE 0
                     END sent_score
                    ,CASE WHEN esd.connected_to_count <> 0 AND esd.connected_to_count < per.connected_target THEN      --03/19/2015
                         per.connected_weight/(per.connected_target/esd.connected_to_count) 
                         WHEN esd.connected_to_count >= per.connected_target THEN
                         per.connected_weight
                         ELSE 0
                     END  connected_score
                    ,CASE WHEN esd.connected_from_count <> 0 AND esd.connected_from_count < per.connected_from_target THEN      --03/19/2015
                         per.connected_from_weight/(per.connected_from_target/esd.connected_from_count) 
                         WHEN esd.connected_from_count >= per.connected_from_target THEN
                         per.connected_from_weight
                         ELSE 0
                     END  connected_from_score
                    ,CASE  WHEN esd.login_activity_count <> 0 AND esd.login_activity_count < per.login_activity_target THEN      --03/19/2015
                           per.login_activity_weight/(per.login_activity_target/esd.login_activity_count)
                           WHEN esd.login_activity_count >= per.login_activity_target THEN
                           per.login_activity_weight
                           ELSE 0
                     END login_activity_score
            FROM eng_stage_score_detail esd,
            (
            SELECT  received_target,sent_target,connected_target,connected_from_target,login_activity_target,
                    received_int_target,sent_int_target,connected_int_target,connected_from_int_target,login_activity_int_target,
                    received_adv_target,sent_adv_target,connected_adv_target,connected_from_adv_target,login_activity_adv_target,
                    received_weight,sent_weight,connected_weight,connected_from_weight,login_activity_weight
            FROM promo_engagement_rules per WHERE per.promotion_id = p_in_promotion_id
            ) per,
            gtt_months_between_dates --04/10/2015    Bug 61253
            WHERE esd.time_frame  = 'month'       
                  AND TO_DATE(esd.trans_month||'/'||1||'/'||esd.trans_year,'MM/DD/YYYY') BETWEEN MONTH_START_DATE AND nvl(MONTH_END_DATE,sysdate)--04/10/2015    Bug 61253
            );

        
         --    picks actual counts to calculate score for quarterly data (When rule count is 1)
    CURSOR cur_user_score_quarter_rc
    IS
            SELECT engagement_detail_id,user_id,node_id,trans_month,trans_year
            ,received_int_target,sent_int_target,connected_int_target,connected_from_int_target,login_activity_int_target,
            received_weight,sent_weight,connected_weight,connected_from_weight,login_activity_weight
            ,ROUND(received_score + sent_score + connected_score + connected_from_score + login_activity_score) as total_score 
            FROM
            (SELECT  esd.engagement_detail_id,esd.user_id,esd.node_id,esd.trans_month,esd.trans_year,
                    esd.received_count,esd.sent_count,
                    esd.connected_to_count,esd.connected_from_count,
                    esd.login_activity_count
                    ,per.received_int_target,per.sent_int_target,per.connected_int_target,per.connected_from_int_target,per.login_activity_int_target,
                    per.received_weight,per.sent_weight,per.connected_weight,per.connected_from_weight,per.login_activity_weight
                    ,CASE  WHEN esd.received_count <> 0 AND esd.received_count < per.received_int_target THEN     --03/19/2015        
                           per.received_weight/(per.received_int_target/esd.received_count)
                           WHEN esd.received_count >= per.received_int_target THEN
                           per.received_weight 
                           ELSE 0
                     END received_score
                    ,CASE  WHEN esd.sent_count <> 0 AND esd.sent_count < per.sent_int_target THEN     --03/19/2015        
                           per.sent_weight/(per.sent_int_target/esd.sent_count)
                           WHEN esd.sent_count >= per.sent_int_target THEN
                           per.sent_weight 
                           ELSE 0
                     END sent_score
                    ,CASE WHEN esd.connected_to_count <> 0 AND esd.connected_to_count < per.connected_int_target THEN      --03/19/2015
                         per.connected_weight/(per.connected_int_target/esd.connected_to_count) 
                         WHEN esd.connected_to_count >= per.connected_int_target THEN
                         per.connected_weight
                         ELSE 0
                     END  connected_score
                    ,CASE WHEN esd.connected_from_count <> 0 AND esd.connected_from_count < per.connected_from_int_target THEN      --03/19/2015
                         per.connected_from_weight/(per.connected_from_int_target/esd.connected_from_count) 
                         WHEN esd.connected_from_count >= per.connected_from_int_target THEN
                         per.connected_from_weight
                         ELSE 0
                     END  connected_from_score
                    ,CASE  WHEN esd.login_activity_count <> 0 AND esd.login_activity_count < per.login_activity_int_target THEN      --03/19/2015
                           per.login_activity_weight/(per.login_activity_int_target/esd.login_activity_count)
                           WHEN esd.login_activity_count >= per.login_activity_int_target THEN
                           per.login_activity_weight
                           ELSE 0
                     END login_activity_score
            FROM eng_stage_score_detail esd,
            (
            SELECT  received_target,sent_target,connected_target,connected_from_target,login_activity_target,
                    received_int_target,sent_int_target,connected_int_target,connected_from_int_target,login_activity_int_target,
                    received_adv_target,sent_adv_target,connected_adv_target,connected_from_adv_target,login_activity_adv_target,
                    received_weight,sent_weight,connected_weight,connected_from_weight,login_activity_weight
            FROM promo_engagement_rules per WHERE per.promotion_id = p_in_promotion_id
            ) per,
            gtt_months_between_dates--04/10/2015    Bug 61253
            WHERE esd.time_frame  = 'quarter'       
                  AND TO_DATE(esd.trans_month||'/'||1||'/'||esd.trans_year,'MM/DD/YYYY') BETWEEN QUARTER_START_DATE AND nvl(MONTH_END_DATE,sysdate)--04/10/2015    Bug 61253
            );
        
              --    picks actual counts to calculate score for Yearly data (When rule count is 1)
    CURSOR cur_user_score_annual_rc
    IS
            SELECT engagement_detail_id,user_id,node_id,trans_month,trans_year
                   ,received_adv_target,sent_adv_target,connected_adv_target,connected_from_adv_target,login_activity_adv_target      
                   ,received_weight,sent_weight,connected_weight,connected_from_weight,login_activity_weight                         
            ,ROUND(received_score + sent_score + connected_score + connected_from_score + login_activity_score) as total_score 
            FROM
            (SELECT  esd.engagement_detail_id,esd.user_id,esd.node_id,esd.trans_month,esd.trans_year,
                    esd.received_count,esd.sent_count,
                    esd.connected_to_count,esd.connected_from_count,
                    esd.login_activity_count
                    ,per.received_adv_target,per.sent_adv_target,per.connected_adv_target,per.connected_from_adv_target,per.login_activity_adv_target,
                    per.received_weight,per.sent_weight,per.connected_weight,per.connected_from_weight,per.login_activity_weight
                    ,CASE  WHEN esd.received_count <> 0 AND esd.received_count < per.received_adv_target THEN     --03/19/2015        
                           per.received_weight/(per.received_adv_target/esd.received_count)
                           WHEN esd.received_count >= per.received_adv_target THEN
                           per.received_weight 
                           ELSE 0
                     END received_score
                    ,CASE  WHEN esd.sent_count <> 0 AND esd.sent_count < per.sent_adv_target THEN     --03/19/2015        
                           per.sent_weight/(per.sent_adv_target/esd.sent_count)
                           WHEN esd.sent_count >= per.sent_adv_target THEN
                           per.sent_weight 
                           ELSE 0
                     END sent_score
                    ,CASE WHEN esd.connected_to_count <> 0 AND esd.connected_to_count < per.connected_adv_target THEN      --03/19/2015
                         per.connected_weight/(per.connected_adv_target/esd.connected_to_count) 
                         WHEN esd.connected_to_count >= per.connected_adv_target THEN
                         per.connected_weight
                         ELSE 0
                     END  connected_score
                    ,CASE WHEN esd.connected_from_count <> 0 AND esd.connected_from_count < per.connected_from_adv_target THEN      --03/19/2015
                         per.connected_from_weight/(per.connected_from_adv_target/esd.connected_from_count) 
                         WHEN esd.connected_from_count >= per.connected_from_adv_target THEN
                         per.connected_from_weight
                         ELSE 0
                     END  connected_from_score
                    ,CASE  WHEN esd.login_activity_count <> 0 AND esd.login_activity_count < per.login_activity_adv_target THEN      --03/19/2015
                           per.login_activity_weight/(per.login_activity_adv_target/esd.login_activity_count)
                           WHEN esd.login_activity_count >= per.login_activity_adv_target THEN
                           per.login_activity_weight
                           ELSE 0
                     END login_activity_score
            FROM eng_stage_score_detail esd,
            (
            SELECT  received_target,sent_target,connected_target,connected_from_target,login_activity_target,
                    received_int_target,sent_int_target,connected_int_target,connected_from_int_target,login_activity_int_target,
                    received_adv_target,sent_adv_target,connected_adv_target,connected_from_adv_target,login_activity_adv_target,
                    received_weight,sent_weight,connected_weight,connected_from_weight,login_activity_weight
            FROM promo_engagement_rules per WHERE per.promotion_id = p_in_promotion_id
            ) per,
            gtt_months_between_dates --04/10/2015    Bug 61253
            WHERE esd.time_frame  = 'year'       
                  AND TO_DATE(esd.trans_month||'/'||1||'/'||esd.trans_year,'MM/DD/YYYY') BETWEEN YEAR_START_DATE AND nvl(MONTH_END_DATE,sysdate)--04/10/2015    Bug 61253
            );
        
BEGIN
    v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', 'p_in_start_date :' || p_in_start_date||'p_in_end_date :'||p_end_date, NULL);

    BEGIN
      SELECT boolean_val                            --04/01/2019
        INTO v_sa_enabled
        FROM os_propertyset
       WHERE entity_name = 'new.service.anniversary.enabled';
     EXCEPTION
         WHEN OTHERS THEN
         v_sa_enabled := 0;
     END;
    
      v_stage:='select company goal to populate IS_SCORE_TARGET_ACHIEVED field in detail';
    SELECT NVL(company_goal,0),is_score_active,score_preference --08/28/2014 Bug # 56066
        INTO v_company_goal,v_is_score_active,v_score_preference --08/28/2014 Bug # 56066
    FROM promo_engagement pe
        WHERE pe.promotion_id = p_in_promotion_id; 
    
         
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENG_STAGE_SCORE_DETAIL';    
           --base query picks all eligible participants from eligible promotions 
       MERGE INTO eng_stage_score_detail d
        USING
        (SELECT b.user_id,b.node_id,e.trans_month,e.trans_year,'quarter' time_frame FROM 
        (        
        SELECT  user_id,node_id         
        FROM    engagement_elig_user WHERE status = 1
        ) b,
        (SELECT month trans_month,
        year trans_year,quarter_start_date
        FROM gtt_months_between_dates) e    
      WHERE e.quarter_start_date IS NOT NULL
      UNION ALL
      SELECT b.user_id,b.node_id,e.trans_month,e.trans_year,'year' time_frame FROM 
        (        
        SELECT  user_id,node_id         
        FROM    engagement_elig_user WHERE status = 1
        ) b,
        (SELECT month trans_month,
        year trans_year,year_start_date
        FROM gtt_months_between_dates) e    
      WHERE e.year_start_date IS NOT NULL
      UNION ALL
      SELECT b.user_id,b.node_id,e.trans_month,e.trans_year,'month' time_frame FROM 
        (        
        SELECT  user_id,node_id         
        FROM    engagement_elig_user WHERE status = 1
        ) b,
        (SELECT month trans_month,
        year trans_year,quarter_start_date
        FROM gtt_months_between_dates) e                                                                       
        )a
        ON (a.user_id = d.user_id AND a.node_id = d.node_id AND a.trans_month = d.trans_month 
            AND a.trans_year = d.trans_year AND a.time_frame = d.time_frame)
        WHEN NOT MATCHED THEN
            INSERT(engagement_detail_id,user_id,node_id,trans_month,trans_year,time_frame,
                    received_count,sent_count,connected_to_count,connected_from_count,login_activity_count)
            VALUES (engagement_score_detail_id_seq.NEXTVAL,a.user_id,a.node_id,a.trans_month,a.trans_year,a.time_frame,0,0,0,0,0);      
    
       
    --received count AND connected_from_count for monthly data
     v_stage :='received count AND connected_from_count for monthly data';
    MERGE INTO eng_stage_score_detail A
    USING
    (
    SELECT     cr.participant_id ,cr.node_id,    
            timeline.month trans_month,timeline.year trans_year,
            count(c.submitter_id)  received_count,
            count(DISTINCT c.submitter_id)  connected_from_count,
            'month' time_frame
    FROM     claim c,
            claim_item ci,
            claim_recipient cr,
             gtt_months_between_dates timeline,
             (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep   --04/01/2019            
    WHERE     c.claim_id = ci.claim_id
            AND ci.claim_item_id = cr.claim_item_id            
            AND c.is_open = 0 -- 09/01/2014 Bug # 56135
            AND ci.approval_status_type = 'approv'
            AND c.promotion_id = pep.eligible_promotion_id
            AND pep.promotion_id = p_in_promotion_id                                        
            AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = cr.participant_id)
            AND trunc(ci.date_created) BETWEEN trunc(month_start_date) AND trunc(NVL(month_end_date,SYSDATE))              
    group by cr.participant_id,cr.node_id,
            timeline.month,timeline.year 
    )B
    ON     (a.user_id = b.participant_id AND a.node_id = b.node_id 
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = b.time_frame)
    WHEN MATCHED THEN
        UPDATE SET a.received_count = b.received_count,
                            a.connected_from_count = b.connected_from_count    
    WHEN NOT MATCHED THEN
        INSERT (engagement_detail_id,user_id,node_id,trans_month,trans_year,time_frame,received_count,
                sent_count,connected_to_count,connected_from_count,login_activity_count)
        VALUES (engagement_score_detail_id_SEQ.NEXTVAL,b.participant_id,b.node_id,b.trans_month, b.trans_year,b.time_frame,b.received_count,0,0,b.connected_from_count,0);

    --Sent count AND connected_to_count for monthly data.
v_stage :='Sent count AND connected_to_count for monthly data';
    MERGE INTO eng_stage_score_detail A
    USING    
    (
    SELECT  c.submitter_id,c.node_id,
            timeline.month trans_month,timeline.year trans_year,
            COUNT(cr.participant_id) sent_count,
            COUNT(DISTINCT cr.participant_id) connected_to_count,
            'month' time_frame
    FROM     claim c,
            claim_item ci,
            claim_recipient cr ,
             gtt_months_between_dates timeline,
             (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep   --04/01/2019
    WHERE   c.claim_id = ci.claim_id
            AND ci.claim_item_id = cr.claim_item_id
            AND c.is_open = 0 -- 09/23/2014 Bug # 56135
            AND ci.approval_status_type = 'approv'
            AND c.promotion_id = pep.eligible_promotion_id
            AND pep.promotion_id = p_in_promotion_id   
            AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = c.submitter_id)
           AND trunc(c.submission_date) BETWEEN trunc(month_start_date) AND trunc(NVL(month_end_date,SYSDATE))     
    GROUP BY c.submitter_id,c.node_id,
            timeline.month,timeline.year
    )B
    ON     (a.user_id = b.submitter_id AND a.node_id = b.node_id
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = b.time_frame)
    WHEN MATCHED THEN
        UPDATE SET a.sent_count = b.sent_count,
                            a.connected_to_count = b.connected_to_count
    WHEN NOT MATCHED THEN
        INSERT (engagement_detail_id,user_id,node_id,trans_month,trans_year,time_frame,sent_count,received_count,
                connected_to_count,connected_from_count,login_activity_count)
        VALUES (engagement_score_detail_id_SEQ.NEXTVAL,b.submitter_id,b.node_id,b.trans_month, b.trans_year,b.time_frame,b.sent_count,0,b.connected_to_count,0,0);

    --Login Activity Count for monthly data.
v_stage :='Login Activity Count for monthly data';
    MERGE INTO eng_stage_score_detail A
    USING
    (
    SELECT la.user_id,node_id,COUNT(*) login_activity_count,
            timeline.month trans_month,timeline.year trans_year,'month' time_frame
    FROM     login_activity la, engagement_elig_user eeu,
     gtt_months_between_dates timeline
    WHERE la.user_id = eeu.user_id AND TRUNC(login_date_time) BETWEEN trunc(month_start_date) AND trunc(NVL(month_end_date,SYSDATE))        
    GROUP BY la.user_id,eeu.node_id,
            timeline.month,timeline.year
    )B
    ON    (a.user_id = b.user_id AND a.node_id = b.node_id
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = b.time_frame)
    WHEN MATCHED THEN
        UPDATE SET a.login_activity_count = b.login_activity_count
    WHEN NOT MATCHED THEN
        INSERT (engagement_detail_id,user_id,node_id,trans_month,trans_year,time_frame,login_activity_count,
                sent_count,received_count,connected_to_count,connected_from_count)
        VALUES (engagement_score_detail_id_SEQ.NEXTVAL,b.user_id,b.node_id,b.trans_month, b.trans_year,b.time_frame,b.login_activity_count,0,0,0,0);
        
         --received count AND connected_from_count for Quarterly data
v_stage :='received count AND connected_from_count for Quarterly data';
    MERGE INTO eng_stage_score_detail A
    USING
    (
    SELECT     cr.participant_id ,cr.node_id,
            timeline.month trans_month,timeline.year trans_year,'quarter' time_frame,
            count(c.submitter_id)  received_count,
            count(DISTINCT c.submitter_id)  connected_from_count
    FROM     claim c,
            claim_item ci,
            claim_recipient cr,
             gtt_months_between_dates timeline,
             (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep   --04/01/2019
    WHERE     c.claim_id = ci.claim_id
            AND ci.claim_item_id = cr.claim_item_id            
            AND c.is_open = 0 -- 09/01/2014 Bug # 56135
            AND ci.approval_status_type = 'approv'
            AND c.promotion_id = pep.eligible_promotion_id
            AND pep.promotion_id = p_in_promotion_id   
             AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = cr.participant_id)
             AND trunc(ci.date_created) BETWEEN quarter_start_date and month_end_date
  group by cr.participant_id,cr.node_id,timeline.month,timeline.year    
    )B
    ON     (a.user_id = b.participant_id AND a.node_id = b.node_id
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = b.time_frame)
    WHEN MATCHED THEN
        UPDATE SET a.received_count = b.received_count,
                            a.connected_from_count = b.connected_from_count    
    WHEN NOT MATCHED THEN
        INSERT (engagement_detail_id,user_id,node_id,trans_month,trans_year,time_frame,received_count,
                sent_count,connected_to_count,connected_from_count,login_activity_count)
        VALUES (engagement_score_detail_id_SEQ.NEXTVAL,b.participant_id,b.node_id,b.trans_month, b.trans_year,'quarter',b.received_count,0,0,b.connected_from_count,0);

    --Sent count AND connected_to_count for Quarterly data.
v_stage :='Sent count AND connected_to_count for Quarterly data.';
    MERGE INTO eng_stage_score_detail A
    USING    
    (
    SELECT  c.submitter_id,c.node_id,
            timeline.month trans_month,timeline.year trans_year,'quarter' time_frame,
            COUNT(cr.participant_id) sent_count,
            COUNT(DISTINCT cr.participant_id) connected_to_count
    FROM     claim c,
            claim_item ci,
            claim_recipient cr,
            gtt_months_between_dates timeline,
            (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep    --04/01/2019
    WHERE   c.claim_id = ci.claim_id
            AND ci.claim_item_id = cr.claim_item_id            
            AND c.is_open = 0 -- 09/23/2014 Bug # 56135
            AND ci.approval_status_type = 'approv'
            AND c.promotion_id = pep.eligible_promotion_id
            AND pep.promotion_id = p_in_promotion_id     
           AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = c.submitter_id)   
           AND trunc(c.submission_date) BETWEEN quarter_start_date and month_end_date
           AND EXISTS (SELECT * FROM eng_stage_score_detail WHERE user_id = c.submitter_id)   --04/03/2015
    GROUP BY c.submitter_id,c.node_id,
            timeline.month,timeline.year       
    )B
    ON     (a.user_id = b.submitter_id AND a.node_id = b.node_id
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = 'quarter')
    WHEN MATCHED THEN
        UPDATE SET a.sent_count = b.sent_count,
                   a.connected_to_count = b.connected_to_count
    WHEN NOT MATCHED THEN
        INSERT (engagement_detail_id,user_id,node_id,trans_month,trans_year,time_frame,sent_count,received_count,
                connected_to_count,connected_from_count,login_activity_count)
        VALUES (engagement_score_detail_id_SEQ.NEXTVAL,b.submitter_id,b.node_id,b.trans_month, b.trans_year,'quarter',b.sent_count,0,b.connected_to_count,0,0);

    --Login Activity Count for Quarterly data.
v_stage :='Login Activity Count for Quarterly data.';
    MERGE INTO eng_stage_score_detail A
    USING
    (
    SELECT  el.user_id,eeu.node_id,COUNT(*) login_activity_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame
         FROM login_activity el,
                    gtt_months_between_dates timeline,
                    engagement_elig_user eeu          
  WHERE el.user_id = eeu.user_id
            AND eeu.status = 1 AND login_date_time BETWEEN quarter_start_date and month_end_date
          AND EXISTS (SELECT * FROM eng_stage_score_detail WHERE user_id = el.user_id)  --04/03/2015
        GROUP BY el.user_id,eeu.node_id,timeline.month,timeline.year
    )B
    ON    (a.user_id = b.user_id AND a.node_id = b.node_id
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = 'quarter')
    WHEN MATCHED THEN
        UPDATE SET a.login_activity_count = b.login_activity_count
    WHEN NOT MATCHED THEN
        INSERT (engagement_detail_id,user_id,node_id,trans_month,trans_year,time_frame,login_activity_count,
                sent_count,received_count,connected_to_count,connected_from_count)
        VALUES (engagement_score_detail_id_SEQ.NEXTVAL,b.user_id,b.node_id,b.trans_month, b.trans_year,'quarter',b.login_activity_count,0,0,0,0);
        
                 --received count AND connected_from_count for Yearly data
v_stage :='received count AND connected_from_count for Yearly data.';
    MERGE INTO eng_stage_score_detail A
    USING
    (
    SELECT     cr.participant_id ,cr.node_id,
            timeline.month trans_month,timeline.year trans_year,'year' time_frame,
            count(c.submitter_id)  received_count,
            count(DISTINCT c.submitter_id)  connected_from_count
    FROM     claim c,
            claim_item ci,
            claim_recipient cr,
             gtt_months_between_dates timeline,
             (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep   --04/01/2019
    WHERE     c.claim_id = ci.claim_id
            AND ci.claim_item_id = cr.claim_item_id            
            AND c.is_open = 0 -- 09/01/2014 Bug # 56135
            AND ci.approval_status_type = 'approv'
            AND c.promotion_id = pep.eligible_promotion_id
            AND pep.promotion_id = p_in_promotion_id   
             AND trunc(ci.date_created) BETWEEN year_start_date and month_end_date
             AND EXISTS (SELECT * FROM eng_stage_score_detail WHERE user_id = cr.participant_id)   --04/03/2015
  group by cr.participant_id,cr.node_id,timeline.month,timeline.year
    )B
    ON     (a.user_id = b.participant_id AND a.node_id = b.node_id
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = b.time_frame)
    WHEN MATCHED THEN
        UPDATE SET a.received_count = b.received_count,
                            a.connected_from_count = b.connected_from_count    
    WHEN NOT MATCHED THEN
        INSERT (engagement_detail_id,user_id,node_id,trans_month,trans_year,time_frame,received_count,
                sent_count,connected_to_count,connected_from_count,login_activity_count)
        VALUES (engagement_score_detail_id_SEQ.NEXTVAL,b.participant_id,b.node_id,b.trans_month, b.trans_year,'year',b.received_count,0,0,b.connected_from_count,0);

    --Sent count AND connected_to_count for Yearly data.
v_stage :='Sent count AND connected_to_count for Yearly data.';

    MERGE INTO eng_stage_score_detail A
    USING    
    (
    SELECT  c.submitter_id,c.node_id,
            timeline.month trans_month,timeline.year trans_year,'year' time_frame,
            COUNT(cr.participant_id) sent_count,
            COUNT(DISTINCT cr.participant_id) connected_to_count
    FROM     claim c,
            claim_item ci,
            claim_recipient cr,
            gtt_months_between_dates timeline,
            (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep    --04/01/2019
    WHERE   c.claim_id = ci.claim_id
            AND ci.claim_item_id = cr.claim_item_id            
            AND c.is_open = 0 -- 09/23/2014 Bug # 56135
            AND ci.approval_status_type = 'approv'
            AND c.promotion_id = pep.eligible_promotion_id
            AND pep.promotion_id = p_in_promotion_id      
            AND EXISTS (SELECT * FROM eng_stage_score_detail WHERE user_id = c.submitter_id)      --04/03/2015
           AND trunc(c.submission_date) BETWEEN year_start_date and month_end_date
     GROUP BY c.submitter_id,c.node_id,
            timeline.month,timeline.year     
    )B
    ON     (a.user_id = b.submitter_id AND a.node_id = b.node_id
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = 'year')
    WHEN MATCHED THEN
        UPDATE SET a.sent_count = b.sent_count,
                            a.connected_to_count = b.connected_to_count
    WHEN NOT MATCHED THEN
        INSERT (engagement_detail_id,user_id,node_id,trans_month,trans_year,time_frame,sent_count,received_count,
                connected_to_count,connected_from_count,login_activity_count)
        VALUES (engagement_score_detail_id_SEQ.NEXTVAL,b.submitter_id,b.node_id,b.trans_month, b.trans_year,'year',b.sent_count,0,b.connected_to_count,0,0);

    --Login Activity Count for Yearly data.
v_stage :='Login Activity Count for Yearly data..';
    MERGE INTO eng_stage_score_detail A
    USING
    (
    SELECT  el.user_id,eeu.node_id,COUNT(*) login_activity_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame
         FROM login_activity el,
                    gtt_months_between_dates timeline,
                    engagement_elig_user eeu 
  WHERE el.user_id = eeu.user_id AND eeu.status = 1AND login_date_time BETWEEN year_start_date and month_end_date
          AND EXISTS (SELECT * FROM eng_stage_score_detail WHERE user_id = el.user_id)  --04/03/2015  
        GROUP BY el.user_id,eeu.node_id,timeline.month,timeline.year
    )B
    ON    (a.user_id = b.user_id AND a.node_id = b.node_id
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = 'year')
    WHEN MATCHED THEN
        UPDATE SET a.login_activity_count = b.login_activity_count
    WHEN NOT MATCHED THEN
        INSERT (engagement_detail_id,user_id,node_id,trans_month,trans_year,time_frame,login_activity_count,
                sent_count,received_count,connected_to_count,connected_from_count)
        VALUES (engagement_score_detail_id_SEQ.NEXTVAL,b.user_id,b.node_id,b.trans_month, b.trans_year,'year',b.login_activity_count,0,0,0,0);
        
            BEGIN 
        SYS.DBMS_STATS.GATHER_TABLE_STATS 
        ( OWNNAME          => USER,
          TABNAME          => 'ENG_STAGE_SCORE_DETAIL',
          ESTIMATE_PERCENT => 10,
          CASCADE          => TRUE,
          METHOD_OPT       => 'for all columns size auto'
        );
    END;

        IF v_is_score_active =1 THEN
    --Score Calculation for monthly data
    v_stage :='Score Calculation for monthly data.';
    prc_execution_log_entry (c_process_name,c_release_level,'INFO','Start: '||v_stage,NULL);    --10/31/2014
    SELECT COUNT(1) INTO v_rule_count 
                FROM promo_engagement_rules per WHERE per.promotion_id = p_in_promotion_id ; 
     IF v_rule_count > 1 THEN
     
     p_eng_user_weight_rule(p_user_id,p_in_promotion_id,v_return_code,v_error_message);
     IF  v_return_code <> 0 THEN        --04/09/2019
        RAISE exit_proc_exception;
     END IF;
     
     
    OPEN cur_user_score;                        --03/19/2015
    
    LOOP        --02/25/2016
    
    FETCH cur_user_score BULK COLLECT          
     INTO v_eng_score_detail_id,v_user_id,
          v_node_id,
          v_trans_month,
          v_trans_year,
          vt_received_target_mon,
          vt_sent_target_mon,
          vt_connected_to_target_mon,   
          vt_connected_from_target_mon, 
          vt_login_activity_target_mon,
          vt_received_weight,      
          vt_sent_weight,          
          vt_connected_to_weight,  
          vt_connected_from_weight,
          vt_login_activity_weight,
          v_total_score LIMIT v_bulk_limit;   --02/25/2016 



        IF v_user_id.COUNT > 0 THEN    
          FORALL indx IN v_user_id.FIRST .. v_user_id.LAST         --03/19/2015
          
            UPDATE ENG_STAGE_SCORE_DETAIL
            SET received_target = vt_received_target_mon(indx),
                sent_target = vt_sent_target_mon(indx),
                connected_target = vt_connected_to_target_mon(indx),
                connected_from_target = vt_connected_from_target_mon(indx),
                login_activity_target = vt_login_activity_target_mon(indx),
                received_weight = vt_received_weight(indx),
                sent_weight = vt_sent_weight(indx),
                connected_weight = vt_connected_to_weight(indx),
                connected_from_weight = vt_connected_from_weight(indx),
                login_activity_weight = vt_login_activity_weight(indx),
                total_score = v_total_score(indx)
            WHERE engagement_detail_id = v_eng_score_detail_id(indx);
            commit;
        END IF; --if v_user_id.COUNT         


       EXIT WHEN v_user_id.COUNT = 0;     
       --prc_execution_log_entry (c_process_name,c_release_level,'INFO','Post Bulk Collect Month :'||'Value of v_user_id.COUNT:'||v_user_id.COUNT,NULL);    --10/31/2014

    END LOOP;

    CLOSE cur_user_score;

    prc_execution_log_entry (c_process_name,c_release_level,'INFO','End: '||v_stage,NULL);    --10/31/2014
    
    --Score Calculation for quarterly data
    v_stage :='Score Calculation for quarterly data.';  --10/31/2014
    prc_execution_log_entry (c_process_name,c_release_level,'INFO','Start: '||v_stage,NULL);    --10/31/2014

      v_eng_score_detail_id.DELETE;
      v_user_id.DELETE;     --03/19/2015
      v_node_id.DELETE;
      v_trans_month.DELETE;
      v_trans_year.DELETE;
      vt_received_target_quarter.DELETE;
      vt_sent_target_quarter.DELETE;
      vt_connected_to_target_quarter.DELETE;
      vt_connected_from_target_qtr.DELETE;
      vt_login_activity_target_qtr.DELETE;
      vt_received_weight.DELETE;
      vt_sent_weight.DELETE;
      vt_connected_to_weight.DELETE;
      vt_connected_from_weight.DELETE;
      vt_login_activity_weight.DELETE;
      v_total_score.DELETE;

   OPEN cur_user_score_quarter;                        --03/19/2015

    LOOP  --02/25/2016
        FETCH cur_user_score_quarter BULK COLLECT          
         INTO v_eng_score_detail_id,v_user_id,
              v_node_id,
              v_trans_month,
              v_trans_year,
              vt_received_target_quarter,
              vt_sent_target_quarter,
              vt_connected_to_target_quarter,
              vt_connected_from_target_qtr,
              vt_login_activity_target_qtr,
              vt_received_weight,
              vt_sent_weight,
              vt_connected_to_weight,
              vt_connected_from_weight,
              vt_login_activity_weight,
              v_total_score  LIMIT v_bulk_limit;   --02/25/2016




        IF v_user_id.COUNT > 0 THEN    
          FORALL indx IN v_user_id.FIRST .. v_user_id.LAST         --03/19/2015
          
          UPDATE ENG_STAGE_SCORE_DETAIL
            SET received_target = vt_received_target_quarter(indx),
                sent_target = vt_sent_target_quarter(indx),
                connected_target = vt_connected_to_target_quarter(indx),
                connected_from_target = vt_connected_from_target_qtr(indx),
                login_activity_target = vt_login_activity_target_qtr(indx),
                received_weight = vt_received_weight(indx),
                sent_weight = vt_sent_weight(indx),
                connected_weight = vt_connected_to_weight(indx),
                connected_from_weight = vt_connected_from_weight(indx),
                login_activity_weight = vt_login_activity_weight(indx),
                total_score = v_total_score(indx)
            WHERE engagement_detail_id = v_eng_score_detail_id(indx);
            commit;

        END IF; --if v_user_id.COUNT         

       EXIT WHEN v_user_id.COUNT = 0;     
       --prc_execution_log_entry (c_process_name,c_release_level,'INFO','Post Bulk Collect Quarter :'||'Value of v_user_id.COUNT:'||v_user_id.COUNT,NULL);    --10/31/2014
    END LOOP;

    CLOSE cur_user_score_quarter;   

    prc_execution_log_entry (c_process_name,c_release_level,'INFO','End: '||v_stage,NULL);    --10/31/2014
  
      --Score Calculation for Year data
    v_stage :='Score Calculation for Year data';  --10/31/2014
    prc_execution_log_entry (c_process_name,c_release_level,'INFO','Start: '||v_stage,NULL);    --10/31/2014

      v_eng_score_detail_id.DELETE;
      v_user_id.DELETE;     --03/19/2015
      v_node_id.DELETE;
      v_trans_month.DELETE;
      v_trans_year.DELETE;
      v_total_score.DELETE;
      vt_received_target_year.DELETE;
      vt_sent_target_year.DELETE;
      vt_connected_to_target_year.DELETE;
      vt_connected_from_target_year.DELETE;
      vt_login_activity_target_year.DELETE;
      vt_received_weight.DELETE;
      vt_sent_weight.DELETE;
      vt_connected_to_weight.DELETE;
      vt_connected_from_weight.DELETE;
      vt_login_activity_weight.DELETE;

    OPEN cur_user_score_annual;                        --03/19/2015

    LOOP --02/25/2016

    FETCH cur_user_score_annual BULK COLLECT          
     INTO v_eng_score_detail_id,v_user_id,
          v_node_id,
          v_trans_month,
          v_trans_year,
          vt_received_target_year,
          vt_sent_target_year,
          vt_connected_to_target_year,
          vt_connected_from_target_year,
          vt_login_activity_target_year,
          vt_received_weight,
          vt_sent_weight,
          vt_connected_to_weight,
          vt_connected_from_weight,
          vt_login_activity_weight,
          v_total_score LIMIT v_bulk_limit;  --02/25/2016





        IF v_user_id.COUNT > 0 THEN
          FORALL indx IN v_user_id.FIRST .. v_user_id.LAST         --03/19/2015
          
            UPDATE ENG_STAGE_SCORE_DETAIL
            SET received_target = vt_received_target_year(indx),
                sent_target = vt_sent_target_year(indx),
                connected_target = vt_connected_to_target_year(indx),
                connected_from_target = vt_connected_from_target_year(indx),
                login_activity_target = vt_login_activity_target_year(indx),
                received_weight = vt_received_weight(indx),
                sent_weight = vt_sent_weight(indx),
                connected_weight = vt_connected_to_weight(indx),
                connected_from_weight = vt_connected_from_weight(indx),
                login_activity_weight = vt_login_activity_weight(indx),
                total_score = v_total_score(indx)
            WHERE engagement_detail_id = v_eng_score_detail_id(indx);
            commit;

        END IF; --if v_user_id.COUNT     

       EXIT WHEN v_user_id.COUNT = 0;     
       --prc_execution_log_entry (c_process_name,c_release_level,'INFO','Post Bulk Collect Year :'||'Value of v_user_id.COUNT:'||v_user_id.COUNT,NULL);    --10/31/2014

    END LOOP;
      
    CLOSE cur_user_score_annual;   
   
   ELSIF v_rule_count = 1 THEN

    OPEN cur_user_score_rc;                        --03/25/2015
   
    LOOP   --02/25/2016
    
    FETCH cur_user_score_rc BULK COLLECT          
     INTO v_eng_score_detail_id,v_user_id,
          v_node_id,
          v_trans_month,
          v_trans_year,
          vt_received_target_mon,
          vt_sent_target_mon,
          vt_connected_to_target_mon,
          vt_connected_from_target_mon,
          vt_login_activity_target_mon,
          vt_received_weight,
          vt_sent_weight,
          vt_connected_to_weight,
          vt_connected_from_weight,
          vt_login_activity_weight,
          v_total_score  LIMIT v_bulk_limit;   --02/25/2016



        IF v_user_id.COUNT > 0 THEN    
          FORALL indx IN v_user_id.FIRST .. v_user_id.LAST         --03/19/2015
          
          UPDATE ENG_STAGE_SCORE_DETAIL
            SET received_target = vt_received_target_mon(indx),
                sent_target = vt_sent_target_mon(indx),
                connected_target = vt_connected_to_target_mon(indx),
                connected_from_target = vt_connected_from_target_mon(indx),
                login_activity_target = vt_login_activity_target_mon(indx),
                received_weight = vt_received_weight(indx),
                sent_weight = vt_sent_weight(indx),
                connected_weight = vt_connected_to_weight(indx),
                connected_from_weight = vt_connected_from_weight(indx),
                login_activity_weight = vt_login_activity_weight(indx),
                total_score = v_total_score(indx)
            WHERE engagement_detail_id = v_eng_score_detail_id(indx);
            commit;
           
        END IF; --if v_user_id.COUNT         


       EXIT WHEN v_user_id.COUNT = 0;   
       --prc_execution_log_entry (c_process_name,c_release_level,'INFO','FORALL>> Post Bulk Collect Month :'||'Value of v_user_id.COUNT:'||v_user_id.COUNT,NULL);    --10/31/2014

    END LOOP;

    CLOSE cur_user_score_rc;
    prc_execution_log_entry (c_process_name,c_release_level,'INFO','End: '||v_stage,NULL);    --10/31/2014
    
    --Score Calculation for quarterly data
    v_stage :='Score Calculation for quarterly data.';  --10/31/2014
    prc_execution_log_entry (c_process_name,c_release_level,'INFO','Start: '||v_stage,NULL);    --10/31/2014

      v_eng_score_detail_id.DELETE;
      v_user_id.DELETE;     --03/19/2015
      v_node_id.DELETE;
      v_trans_month.DELETE;
      v_trans_year.DELETE;
      vt_received_target_quarter.DELETE;
      vt_sent_target_quarter.DELETE;
      vt_connected_to_target_quarter.DELETE;
      vt_connected_from_target_qtr.DELETE;
      vt_login_activity_target_qtr.DELETE;
      vt_received_weight.DELETE;
      vt_sent_weight.DELETE;
      vt_connected_to_weight.DELETE;
      vt_connected_from_weight.DELETE;
      vt_login_activity_weight.DELETE;
      v_total_score.DELETE;

   OPEN cur_user_score_quarter_rc;                        --03/19/2015
 
    LOOP   --02/25/2016
    
    FETCH cur_user_score_quarter_rc BULK COLLECT          
     INTO v_eng_score_detail_id,v_user_id,
          v_node_id,
          v_trans_month,
          v_trans_year,
          vt_received_target_quarter,
          vt_sent_target_quarter,
          vt_connected_to_target_quarter,
          vt_connected_from_target_qtr,
          vt_login_activity_target_qtr,
          vt_received_weight,
          vt_sent_weight,
          vt_connected_to_weight,
          vt_connected_from_weight,
          vt_login_activity_weight,
          v_total_score LIMIT v_bulk_limit;          --02/25/2016




        IF v_user_id.COUNT > 0 THEN    
          FORALL indx IN v_user_id.FIRST .. v_user_id.LAST         --03/19/2015
          UPDATE ENG_STAGE_SCORE_DETAIL
            SET received_target = vt_received_target_quarter(indx),
                sent_target = vt_sent_target_quarter(indx),
                connected_target = vt_connected_to_target_quarter(indx),
                connected_from_target = vt_connected_from_target_qtr(indx),
                login_activity_target = vt_login_activity_target_qtr(indx),
                received_weight = vt_received_weight(indx),
                sent_weight = vt_sent_weight(indx),
                connected_weight = vt_connected_to_weight(indx),
                connected_from_weight = vt_connected_from_weight(indx),
                login_activity_weight = vt_login_activity_weight(indx),
                total_score = v_total_score(indx)
            WHERE engagement_detail_id = v_eng_score_detail_id(indx);
            commit;

        END IF; --if v_user_id.COUNT         

    EXIT WHEN v_user_id.COUNT = 0;   
    --prc_execution_log_entry (c_process_name,c_release_level,'INFO','Post Bulk Collect Quarter :'||'Value of v_user_id.COUNT:'||v_user_id.COUNT,NULL);    --10/31/2014

    END LOOP;

    CLOSE cur_user_score_quarter_rc;   



    prc_execution_log_entry (c_process_name,c_release_level,'INFO','End: '||v_stage,NULL);    --10/31/2014
  
      --Score Calculation for Year data
    v_stage :='Score Calculation for Year data';  --10/31/2014
    prc_execution_log_entry (c_process_name,c_release_level,'INFO','Start: '||v_stage,NULL);    --10/31/2014

    v_eng_score_detail_id.DELETE;
    v_user_id.DELETE;     --03/19/2015
      v_node_id.DELETE;
      v_trans_month.DELETE;
      v_trans_year.DELETE;
      v_total_score.DELETE;
      vt_received_target_year.DELETE;
      vt_sent_target_year.DELETE;
      vt_connected_to_target_year.DELETE;
      vt_connected_from_target_year.DELETE;
      vt_login_activity_target_year.DELETE;
      vt_received_weight.DELETE;
      vt_sent_weight.DELETE;
      vt_connected_to_weight.DELETE;
      vt_connected_from_weight.DELETE;
      vt_login_activity_weight.DELETE;

   OPEN cur_user_score_annual_rc;                        --03/19/2015

    LOOP          --02/25/2016
    
    FETCH cur_user_score_annual_rc BULK COLLECT          
     INTO v_eng_score_detail_id,v_user_id,
          v_node_id,
          v_trans_month,
          v_trans_year,
          vt_received_target_year,
          vt_sent_target_year,
          vt_connected_to_target_year,
          vt_connected_from_target_year,
          vt_login_activity_target_year,
          vt_received_weight,
          vt_sent_weight,
          vt_connected_to_weight,
          vt_connected_from_weight,
          vt_login_activity_weight,
          v_total_score LIMIT v_bulk_limit;     --02/25/2016





        IF v_user_id.COUNT > 0 THEN
          FORALL indx IN v_user_id.FIRST .. v_user_id.LAST         --03/19/2015
           UPDATE ENG_STAGE_SCORE_DETAIL
            SET received_target = vt_received_target_year(indx),
                sent_target = vt_sent_target_year(indx),
                connected_target = vt_connected_to_target_year(indx),
                connected_from_target = vt_connected_from_target_year(indx),
                login_activity_target = vt_login_activity_target_year(indx),
                received_weight = vt_received_weight(indx),
                sent_weight = vt_sent_weight(indx),
                connected_weight = vt_connected_to_weight(indx),
                connected_from_weight = vt_connected_from_weight(indx),
                login_activity_weight = vt_login_activity_weight(indx),
                total_score = v_total_score(indx)
            WHERE engagement_detail_id = v_eng_score_detail_id(indx);
            commit;

        END IF;

    EXIT WHEN v_user_id.COUNT = 0;   
    --prc_execution_log_entry (c_process_name,c_release_level,'INFO','Post Bulk Collect Year :'||'Value of v_user_id.COUNT:'||v_user_id.COUNT,NULL);    --10/31/2014

    END LOOP;
    CLOSE cur_user_score_annual_rc;   

    prc_execution_log_entry (c_process_name,c_release_level,'INFO','End: '||v_stage,NULL);    --10/31/2014

  END IF; --07/06/2015
  
  END IF;--Is score active? --07/06/2015
    BEGIN 
        SYS.DBMS_STATS.GATHER_TABLE_STATS 
        ( OWNNAME          => USER,
          TABNAME          => 'ENGAGEMENT_SCORE_DETAIL',
          ESTIMATE_PERCENT => 10,
          CASCADE          => TRUE,
          METHOD_OPT       => 'for all columns size auto'
        );
    END;
    v_stage :='Merging SCORE Data..';
    prc_execution_log_entry (c_process_name,c_release_level,'INFO','Start: '||v_stage,NULL);    --10/31/2014
    
    MERGE INTO engagement_score_detail A
    USING
    (
        SELECT engagement_detail_id,user_id,node_id,time_frame,trans_month,trans_year,total_score
               ,received_target, sent_target, connected_target, connected_from_target, login_activity_target
               ,received_weight, sent_weight, connected_weight, connected_from_weight, login_activity_weight,
               received_count,sent_count,connected_to_count,connected_from_count,login_activity_count 
        FROM eng_stage_score_detail
    )B
    ON    (a.node_id = b.node_id AND a.user_id = b.user_id AND  a.time_frame = b.time_frame AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year )
    WHEN MATCHED THEN
        UPDATE SET a.score = b.total_score,
                   a.received_target = b.received_target, 
                   a.sent_target = b.sent_target, 
                   a.connected_target = b.connected_target,
                   a.connected_from_target = b.connected_from_target,
                   a.login_activity_target = b.login_activity_target,
                   a.received_weight = b.received_weight, 
                   a.sent_weight = b.sent_weight,
                   a.connected_weight = b.connected_weight, 
                   a.connected_from_weight = b.connected_from_weight,
                   a.login_activity_weight = b.login_activity_weight,
                   a.received_count = b.received_count,
                   a.sent_count = b.sent_count,
                   a.connected_to_count = b.connected_to_count,
                   a.connected_from_count = b.connected_from_count,
                   a.login_activity_count = b.login_activity_count
                   WHEN NOT MATCHED THEN
            INSERT(engagement_detail_id,user_id,node_id,trans_month,trans_year,time_frame,
                    received_count,sent_count,connected_to_count,connected_from_count,login_activity_count,
                    received_target, sent_target, connected_target, connected_from_target, login_activity_target
               ,received_weight, sent_weight, connected_weight, connected_from_weight, login_activity_weight,score)
            VALUES (b.engagement_detail_id,b.user_id,b.node_id,b.trans_month,b.trans_year,b.time_frame,
                         b.received_count,b.sent_count,b.connected_to_count,b.connected_from_count,b.login_activity_count,
                         b.received_target, b.sent_target, b.connected_target, b.connected_from_target, b.login_activity_target,
                         b.received_weight, b.sent_weight, b.connected_weight, b.connected_from_weight, b.login_activity_weight,b.total_score);   
                   
    prc_execution_log_entry (c_process_name,c_release_level,'INFO','End: '||v_stage,NULL);    --10/31/2014

   v_stage :='Targets - Sent, Received, ConnectedTo and ConnectedFrom';  --10/31/2014
  prc_execution_log_entry (c_process_name,c_release_level,'INFO','Start: '||v_stage,NULL);    --10/31/2014
  FOR rec_score IN (    --10/31/2014
  SELECT user_id,trans_month,trans_year,time_frame
      ,sent_target,SUM (sent_count) over (partition by user_id,trans_month,trans_year,time_frame ) sent_count
      ,received_target,SUM (received_count) over (partition by user_id,trans_month,trans_year,time_frame ) received_count
      ,connected_target,SUM (connected_to_count) over (partition by user_id,trans_month,trans_year,time_frame ) connected_to_count  
      ,connected_from_target,SUM (connected_from_count) over (partition by user_id,trans_month,trans_year,time_frame ) connected_from_count
  FROM engagement_score_detail
  WHERE (sent_target is not null 
        and received_target is not null 
        and connected_target is not null 
        and connected_from_target is not null 
        --AND TO_DATE(trans_month||'/'||1||'/'||trans_year,'MM/DD/YYYY') BETWEEN p_start_date AND nvl(p_end_date,sysdate)))  --08/28/2018 commented and replaced with line below
        AND TO_char(trans_year) || TO_CHAR(trans_month) 
              BETWEEN extract(year from p_start_date)||extract(month from p_start_date)  AND 
                   extract(year from nvl(p_end_date,sysdate))||extract(month from nvl(p_end_date,sysdate))  --08/28/2018 added
                   )
    )
     LOOP
         
         UPDATE engagement_score_detail    --10/31/2014
         SET    
         is_recog_sent_target_achieved = (CASE 
                    WHEN rec_score.sent_count >= rec_score.sent_target THEN 1
                    ELSE 0
                 END),
          is_recog_recv_target_achieved = (CASE 
                    WHEN rec_score.received_count >= rec_score.received_target THEN 1
                    ELSE 0
                 END),
          is_conn_to_target_achieved = (CASE 
                    WHEN rec_score.connected_to_count >= rec_score.connected_target THEN 1
                    ELSE 0
                 END),
          is_conn_from_target_achieved = (CASE 
                    WHEN rec_score.connected_from_count >= rec_score.connected_from_target THEN 1
                    ELSE 0
                 END)
          WHERE user_id = rec_score.user_id
                AND trans_month = rec_score.trans_month 
                AND trans_year= rec_score.trans_year 
                AND time_frame = rec_score.time_frame 
                AND (sent_target is not null 
                AND received_target is not null 
                AND connected_target is not null 
                AND connected_from_target is not null);
   END LOOP;       
   prc_execution_log_entry (c_process_name,c_release_level,'INFO','End: '||v_stage,NULL);    --10/31/2014

    v_stage :='UPDATE engagement_score_detail';  --10/31/2014
    prc_execution_log_entry (c_process_name,c_release_level,'INFO','Start: '||v_stage,NULL);    --10/31/2014
    
    update engagement_score_detail set is_recog_sent_target_achieved = 0 where sent_target is null;   --10/31/2014 Added for cases where target is null
    update engagement_score_detail set is_recog_recv_target_achieved = 0 where received_target is null;    --10/31/2014 Added for cases where target is null
    update engagement_score_detail set is_conn_to_target_achieved = 0 where connected_target is null;   --10/31/2014 Added for cases where target is null
    update engagement_score_detail set is_conn_from_target_achieved = 0 where connected_from_target is null;   --10/31/2014 Added for cases where target is null

                                       
    UPDATE engagement_score_detail
      SET IS_LOGIN_TARGET_ACHIEVED = (SELECT CASE 
                                                 WHEN LOGIN_ACTIVITY_COUNT >= LOGIN_ACTIVITY_TARGET THEN 1
                                                 ELSE 0
                                              END
                                      FROM DUAL);   
    
    UPDATE engagement_score_detail
      SET IS_SCORE_TARGET_ACHIEVED = (SELECT CASE 
                                                 WHEN Score >= v_company_goal THEN 1
                                                 ELSE 0
                                              END
                                      FROM DUAL);   
    prc_execution_log_entry (c_process_name,c_release_level,'INFO','End: '||v_stage,NULL);    --10/31/2014
--    END IF;--IS the score active?
    
--  END IF;
    --COMMIT; 
    
    v_stage := 'END of Process: '||c_process_name;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
    
    p_return_code :=0;

    EXCEPTION
      WHEN exit_proc_exception THEN     --04/09/2019
        prc_execution_log_entry (c_process_name,c_release_level,'ERROR',v_stage|| ' Error while calling p_eng_user_weight_rule procedure..',NULL);
        p_return_code :=99;
        p_error_message := 'Error while calling p_eng_user_weight_rule procedure..';
      WHEN OTHERS
      THEN         
        prc_execution_log_entry (c_process_name,c_release_level,'ERROR',v_stage|| SQLERRM,NULL);
        p_return_code :=99;
        p_error_message := SQLERRM;
END p_engagement_score_detail;

PROCEDURE p_engagement_score_summary(p_user_id        IN  NUMBER,
   p_in_start_date     IN  DATE, 
   p_in_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS

/*******************************************************************************
-- Purpose: To Populate Engagement Summary Data
-- MODIFICATION HISTORY
-- Person      Date            Comments
-- ---------   ----------      ------------------------------------------------
--  Swati      04/28/2014      Creation
--Ravi Dhanekula 09/09/2014    Fixed the bug # 56011
*******************************************************************************/    
    
    c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_engagement_score_summary');
    
BEGIN
    v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
     MERGE INTO engagement_score_summary d
    USING 
    (                
    WITH rpt_teamsum AS
        (  -- build team summary records                    
         SELECT d.node_id,
                h.parent_node_id,
                d.trans_month,
                d.trans_year,
                d.time_frame,
                SUM(d.score) score, --09/09/2014
                COUNT(d.user_id) total_participant_count,
                SUM(d.RECEIVED_COUNT) RECEIVED_COUNT,
                SUM(d.SENT_COUNT) SENT_COUNT,
                SUM(d.CONNECTED_TO_COUNT) CONNECTED_TO_COUNT,
                SUM(d.CONNECTED_FROM_COUNT) CONNECTED_FROM_COUNT,
                SUM(d.LOGIN_ACTIVITY_COUNT) LOGIN_ACTIVITY_COUNT,
                SUM(d.RECEIVED_TARGET) RECEIVED_TARGET,
                SUM(d.SENT_TARGET) SENT_TARGET,
                SUM(d.CONNECTED_TARGET) CONNECTED_TARGET,
                SUM(d.CONNECTED_FROM_TARGET) CONNECTED_FROM_TARGET,
                SUM(d.LOGIN_ACTIVITY_TARGET) LOGIN_ACTIVITY_TARGET,
                SUM(is_recog_sent_target_achieved) sent_achieved_count,
                SUM(is_recog_recv_target_achieved) recv_achieved_count,           
                SUM(is_conn_to_target_achieved) conn_to_achieved_count,            
                SUM(is_conn_from_target_achieved) conn_from_achieved_count,
                SUM(is_login_target_achieved) login_achieved_count,
                SUM(is_score_target_achieved) score_achieved_count                
            FROM engagement_score_detail d,
                 (SELECT node_id,node_type_id,
         NAME as node_name,
         parent_node_id,
         is_deleted,
         path,
         description
    FROM node n
    START WITH parent_node_id IS NULL
  CONNECT BY PRIOR node_id = parent_node_id) h
            WHERE d.node_id = h.node_id
            AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = d.user_id AND node_id = d.node_id)
            GROUP BY d.node_id,parent_node_id,trans_month,trans_year,d.time_frame
        ) , detail_derived_summary AS
        ( -- derive summaries based on team summary data
            SELECT -- key fields
                rt.node_id,
                'teamsum' AS sum_type,
                rt.parent_node_id,
                rt.trans_month,
                rt.trans_year,
                rt.time_frame,
                (rt.score/rt.total_participant_count) score, --09/09/2014
                rt.total_participant_count,
                rt.received_count,
                rt.sent_count,
                rt.connected_to_count,
                rt.connected_from_count,
                rt.login_activity_count,
                rt.received_target,
                rt.sent_target,
                rt.connected_target,
                rt.connected_from_target,
                rt.login_activity_target,
                rt.sent_achieved_count,
                rt.recv_achieved_count,
                rt.conn_to_achieved_count,
                rt.conn_from_achieved_count,
                rt.login_achieved_count,
                rt.score_achieved_count                
            FROM rpt_teamsum rt
            UNION ALL
            SELECT -- key fields
                h.node_id,
                'nodesum' AS sum_type,
                h.parent_node_id,
                rt.trans_month,
                rt.trans_year,
                rt.time_frame,                
                SUM(rt.score)/SUM(rt.total_participant_count) score,                
                SUM(rt.total_participant_count) AS total_participant_count,
                nvl(sum(rt.received_count),0) AS received_count,
                nvl(sum(rt.sent_count),0) AS sent_count,
                nvl(sum(rt.connected_to_count),0) AS connected_to_count,
                nvl(sum(rt.connected_from_count),0) AS connected_from_count,
                nvl(sum(rt.login_activity_count),0) AS login_activity_count,
                nvl(sum(rt.received_target),0) AS received_target,
                nvl(sum(rt.sent_target),0) AS sent_target,
                nvl(sum(rt.connected_target),0) AS connected_target,
                nvl(sum(rt.connected_from_target),0) AS connected_from_target,
                nvl(sum(rt.login_activity_target),0) AS login_activity_target,
                nvl(sum(rt.sent_achieved_count),0) AS sent_achieved_count,
                nvl(sum(rt.recv_achieved_count),0) AS recv_achieved_count,
                nvl(sum(rt.conn_to_achieved_count),0) AS conn_to_achieved_count,
                nvl(sum(rt.conn_from_achieved_count),0) AS conn_from_achieved_count,
                nvl(sum(rt.login_achieved_count),0) AS login_achieved_count,
                nvl(sum(rt.score_achieved_count),0) AS score_achieved_count
--                h.hier_level
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                   FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
                  (SELECT node_id,node_type_id,
         NAME as node_name,
         parent_node_id,
         is_deleted,
         (SELECT NAME FROM node WHERE node_id = n.parent_node_id) as parent_node_name,
         level AS hier_level,
         path,
         description
    FROM node n
    START WITH parent_node_id IS NULL
  CONNECT BY PRIOR node_id = parent_node_id) h,
                  rpt_teamsum rt
            WHERE -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
                rt.node_id = npn.node_id
                AND npn.path_node_id = h.node_id
            GROUP BY h.node_id,                        
                    h.parent_node_id,
                    rt.trans_month,
                    rt.trans_year,
                    rt.time_frame
                    )-- end detail_derived_summary  
    SELECT es.s_rowid,
                   dds.sum_type AS record_type,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.node_id,
                            s2.record_type AS sum_type,                                                  
                            s2.parent_node_id,
                            s2.trans_month,
                            s2.trans_year,
                            s2.time_frame
                       FROM engagement_score_summary s2                      
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                   FULL OUTER JOIN detail_derived_summary dds
                   ON (       es.node_id    = dds.node_id
                        AND es.sum_type = dds.sum_type
                        AND NVL(es.parent_node_id,0) = NVL(dds.parent_node_id,0)
                        AND es.trans_month = dds.trans_month
                        AND es.trans_year = dds.trans_year
                        AND es.time_frame = dds.time_frame
                      )
    )s        
    ON (d.ROWID = s.s_rowid)        
    --ON (d.node_id = s.node_id AND d.trans_month = s.trans_month AND d.trans_year = s.trans_year)
    WHEN MATCHED THEN
    UPDATE SET 
        d.score                      =    s.score,
        d.total_participant_count    =    s.total_participant_count,
        d.received_count             =    s.received_count,
        d.sent_count                 =    s.sent_count,
        d.connected_to_count         =    s.connected_to_count,
        d.connected_from_count       =    s.connected_from_count,
        d.login_activity_count       =    s.login_activity_count,
        d.received_target            =    s.received_target,
        d.sent_target                =    s.sent_target,
        d.connected_target           =    s.connected_target,
        d.connected_from_target      =    s.connected_from_target,
        d.login_activity_target      =    s.login_activity_target,
        d.sent_achieved_count         =      s.sent_achieved_count,
        d.recv_achieved_count         =      s.recv_achieved_count,
        d.conn_to_achieved_count     =       s.conn_to_achieved_count,
        d.conn_from_achieved_count     =      s.conn_from_achieved_count,
        d.login_achieved_count         =       s.login_achieved_count,
        d.score_achieved_count         =      s.score_achieved_count,
        d.modified_by                =    c_created_by,
        d.date_modified              =    SYSDATE
    WHEN NOT MATCHED THEN
    INSERT (engagement_summary_id,
            record_type,
            node_id,
            parent_node_id,
            trans_month,
            trans_year,
            time_frame,
            score,
            total_participant_count,
            received_count,
            sent_count,
            connected_to_count,
            connected_from_count,
            login_activity_count,
            received_target,
            sent_target,
            connected_target,
            connected_from_target,
            login_activity_target,
            sent_achieved_count,
            recv_achieved_count,
            conn_to_achieved_count,
            conn_from_achieved_count,
            login_achieved_count,
            score_achieved_count,
            date_created,
            created_by,
            VERSION)
    VALUES (ENGAGEMENT_SCORE_SUM_ID_SEQ.NEXTVAL,
            s.sum_type,
            s.node_id,
            s.parent_node_id,
            s.trans_month,
            s.trans_year,
            s.time_frame,
            s.score,
            s.total_participant_count,
            s.received_count,
            s.sent_count,
            s.connected_to_count,
            s.connected_from_count,
            s.login_activity_count,
            s.received_target,
            s.sent_target,
            s.connected_target,
            s.connected_from_target,
            s.login_activity_target,
            s.sent_achieved_count,
            s.recv_achieved_count,
            s.conn_to_achieved_count,
            s.conn_from_achieved_count,
            s.login_achieved_count,
            s.score_achieved_count,
            SYSDATE,
            c_created_by,
            0);
       
    v_stage := 'END of Summary process: ';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
    
     v_stage := 'Start of process Merging Connected data: ';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
    MERGE INTO engagement_score_summary d
    USING 
    ( 
    WITH rpt_teamsum AS
        (  -- build team summary records                         
        SELECT d.node_id,
                h.parent_node_id,
                d.trans_month,
                d.trans_year,
                d.time_frame,
                ROUND(SUM(d.connected_to_count)/COUNT(d.user_id)) connected_avg,
                ROUND(SUM(d.connected_from_count)/COUNT(d.user_id)) connected_from_avg,
                ROUND(SUM(d.connected_target)/COUNT(d.user_id)) connected_avg_target,
                ROUND(SUM(d.connected_from_target)/COUNT(d.user_id)) connected_from_avg_target,
                COUNT(d.user_id) total_participant_count         
            FROM engagement_score_detail d,
                 (SELECT node_id,node_type_id,
         NAME as node_name,
         parent_node_id,
         is_deleted,
         path,
         description
    FROM node n
    START WITH parent_node_id IS NULL
  CONNECT BY PRIOR node_id = parent_node_id) h
            WHERE d.node_id = h.node_id     
            AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = d.user_id AND node_id = d.node_id)       
            GROUP BY d.node_id,parent_node_id,trans_month,trans_year,d.time_frame            
        ) , detail_derived_summary AS
        ( -- derive summaries based on team summary data
            SELECT -- key fields
                rt.node_id,
                'teamsum' AS sum_type,
                rt.parent_node_id,
                rt.trans_month,
                rt.trans_year,
                rt.time_frame,                
                rt.connected_avg_target,
                rt.connected_from_avg_target,
                rt.connected_avg,
                rt.connected_from_avg              
            FROM rpt_teamsum rt
            UNION ALL
            SELECT -- key fields
                h.node_id,
                'nodesum' AS sum_type,
                h.parent_node_id,
                rt.trans_month,
                rt.trans_year,
                rt.time_frame,
                SUM(rt.connected_avg_target * rt.total_participant_count)/SUM(rt.total_participant_count) connected_avg_target,
                SUM(rt.connected_from_avg_target * rt.total_participant_count)/SUM(rt.total_participant_count) connected_from_avg_target,
                SUM(rt.connected_avg * rt.total_participant_count)/SUM(rt.total_participant_count) connected_avg,
                SUM(rt.connected_from_avg * rt.total_participant_count)/SUM(rt.total_participant_count) connected_from_avg
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                   FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
                  (SELECT node_id,node_type_id,
         NAME as node_name,
         parent_node_id,
         is_deleted,
         (SELECT NAME FROM node WHERE node_id = n.parent_node_id) as parent_node_name,
         level AS hier_level,
         path,
         description
    FROM node n
    START WITH parent_node_id IS NULL
  CONNECT BY PRIOR node_id = parent_node_id) h,
                  rpt_teamsum rt
            WHERE -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
                rt.node_id = npn.node_id
                AND npn.path_node_id = h.node_id
            GROUP BY h.node_id,                        
                    h.parent_node_id,
                    rt.trans_month,
                    rt.trans_year,
                    rt.time_frame
                    )-- end detail_derived_summary                   
    SELECT es.s_rowid,
                   dds.sum_type AS record_type,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.node_id,
                            s2.record_type AS sum_type,                                                  
                            s2.parent_node_id,
                            s2.trans_month,
                            s2.trans_year,
                            s2.time_frame
                       FROM engagement_score_summary s2                      
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                   FULL OUTER JOIN detail_derived_summary dds
                   ON (       es.node_id    = dds.node_id
                        AND es.sum_type = dds.sum_type
                        AND NVL(es.parent_node_id,0) = NVL(dds.parent_node_id,0)
                        AND es.trans_month = dds.trans_month
                        AND es.trans_year = dds.trans_year
                        AND es.time_frame = dds.time_frame
                      )
    )s        
    ON (d.ROWID = s.s_rowid)       
    WHEN MATCHED THEN
    UPDATE SET 
        d.connected_avg_target                      =    s.connected_avg_target,
        d.connected_from_avg_target    =    s.connected_from_avg_target,
        d.connected_avg             =    s.connected_avg,
        d.connected_from_avg                 =    s.connected_from_avg,
        d.modified_by                =    c_created_by,
        d.date_modified              =    SYSDATE
    WHEN NOT MATCHED THEN
    INSERT (engagement_summary_id,
            record_type,
            node_id,
            parent_node_id,
            trans_month,
            trans_year,
            time_frame,
            connected_avg_target,
            connected_from_avg_target,
            connected_avg,
            connected_from_avg,
            date_created,
            created_by,
            VERSION)
    VALUES (ENGAGEMENT_SCORE_SUM_ID_SEQ.NEXTVAL,
            s.sum_type,
            s.node_id,
            s.parent_node_id,
            s.trans_month,
            s.trans_year,
            s.time_frame,
            s.connected_avg_target,
            s.connected_from_avg_target,
            s.connected_avg,
            s.connected_from_avg,
            SYSDATE,
            c_created_by,
            0);
       
     v_stage := 'END of process: '||c_process_name;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
    
    p_return_code :=0;
    EXCEPTION
      WHEN OTHERS
      THEN         
        prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);
        p_return_code :=99;
        p_error_message := SQLERRM;
END p_engagement_score_summary;

PROCEDURE P_engagement_user_recent_recog(p_user_id        IN  NUMBER,
   p_in_promotion_id IN NUMBER,
   p_in_start_date     IN  DATE, 
   p_in_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS
/*******************************************************************************
-- Purpose: To populate the table ENGAGEMENT_RECENT_RECOGNITION with the list of users who received atleast one recognition in last 30 days.
-- MODIFICATION HISTORY
-- Person                Date            Comments
-- ---------             ----------      ------------------------------------------------
--  Ravi Dhanekula       06/16/2014      Creation
	Suresh J	         04/01/2019      SA Integeration with DayMaker. Excludes/Includes PURL/Celebration promotions based on boolean value of new.service.anniversary.enabled
*******************************************************************************/                                            

    v_stage              varchar2(300);
    c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_engagement_user_recent_recog');
    c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
    c_created_by         NUMBER := 0;
    v_sa_enabled         os_propertyset.boolean_val%type;       --04/01/2019
       
BEGIN
    v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

    BEGIN
      SELECT boolean_val                            --04/01/2019
        INTO v_sa_enabled
        FROM os_propertyset
       WHERE entity_name = 'new.service.anniversary.enabled';
     EXCEPTION
         WHEN OTHERS THEN
         v_sa_enabled := 0;
     END;

  
      EXECUTE IMMEDIATE ('TRUNCATE TABLE engagement_recent_recognition');
    
    INSERT INTO engagement_recent_recognition e
    (user_id,date_created,created_by)
    (SELECT DISTINCT cr.participant_id,SYSDATE,5662
    FROM claim c,claim_item ci,claim_recipient cr,recognition_claim rc,(SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep --04/01/2019
    WHERE c.claim_id = ci.claim_id
    AND ci.claim_item_id = cr.claim_item_id
    AND rc.claim_id = c.claim_id
    AND (   ci.approval_status_type =
                                                               'winner'
                                                         OR ci.approval_status_type =
                                                               'approv')
    AND c.promotion_id = pep.eligible_promotion_id
    AND pep.promotion_id = p_in_promotion_id
    AND ci.date_approved BETWEEN p_in_end_date-30 AND p_in_end_date);

v_stage := 'END of process: '||c_process_name;
p_return_code :=0; 
prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
END p_engagement_user_recent_recog;

PROCEDURE p_engagement_log  (p_user_id        IN  NUMBER,
   p_in_promotion_id IN NUMBER,
   p_in_start_date     IN  DATE, 
   p_in_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS
/*******************************************************************************
-- Purpose: To Have An Engagement Back Up Log
-- MODIFICATION HISTORY
-- Person      Date            Comments
-- ---------   ----------      -----------------------------------------------------
--  Swati       04/23/2014      Creation
--  Swati       09/12/2014      Bug 56182 - RPM - Team dashboard - Recogntion sent tab - Tile and chart count is not matching.
                09/18/2014        Bug 56182 - RPM - Team dashboard - Recogntion received tab - Tile and chart count is not matching.
-- Ravi Dhanekula 05/01/2017    G6-2276 - RPM counts doesn't match.
   Suresh J	    04/01/2019      SA Integeration with DayMaker. Excludes/Includes PURL/Celebration promotions based on boolean value of new.service.anniversary.enabled
*******************************************************************************/    

    v_stage              varchar2(300);
    c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_engagement_log');
    c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
    c_created_by         NUMBER := 0;
    v_sa_enabled         os_propertyset.boolean_val%type;   --04/01/2019
    
BEGIN
    v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
    
    BEGIN
      SELECT boolean_val                            --04/01/2019
        INTO v_sa_enabled
        FROM os_propertyset
       WHERE entity_name = 'new.service.anniversary.enabled';
     EXCEPTION
         WHEN OTHERS THEN
         v_sa_enabled := 0;
     END;
    
INSERT INTO engagement_log e
    (Engagement_log_Id,sender_user_id,receiver_user_id,promotion_id,sender_node_id,
        receiver_node_id,behavior,date_created,created_by,version)
(SELECT ENGAGEMENT_LOG_ID_SEQ.NEXTVAL,sender_user_id,
           receiver_user_id,
           promotion_id,
           sender_node_id,
           receiver_node_id,
           behavior,
           date_created,
           created_by,
           0      FROM (                                 
           SELECT c.submitter_id sender_user_id ,
           cr.participant_id receiver_user_id,
           c.promotion_id,
           c.node_id sender_node_id,
           cr.node_id receiver_node_id,
           rc.behavior,
           c.date_created,
           c.created_by,
           0      
    FROM claim c,claim_item ci,claim_recipient cr,recognition_claim rc,(SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep --04/01/2019
    WHERE c.claim_id = ci.claim_id
    AND ci.claim_item_id = cr.claim_item_id
    AND rc.claim_id = c.claim_id
    AND (   ci.approval_status_type =
                                                               'winner'
                                                         OR ci.approval_status_type =
                                                               'approv')
    AND c.promotion_id = pep.eligible_promotion_id
    AND pep.promotion_id = p_in_promotion_id
--    AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = cr.participant_id)--05/01/2017 commented out
--    AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = c.submitter_id)--05/01/2017 commented out
    AND ci.date_approved between p_in_start_date AND p_in_end_date
--UNION ALL--05/01/2017 commented out.
--SELECT c.submitter_id sender_user_id ,
--           NULL receiver_user_id,
--           c.promotion_id,
--           c.node_id sender_node_id,
--           NULL receiver_node_id,
--           rc.behavior,
--           c.date_created,
--           c.created_by,
--           0      
--    FROM claim c,claim_item ci,claim_recipient cr,recognition_claim rc,(SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep
--    WHERE c.claim_id = ci.claim_id
--    AND ci.claim_item_id = cr.claim_item_id
--    AND rc.claim_id = c.claim_id
--    AND (   ci.approval_status_type =
--                                                               'winner'
--                                                         OR ci.approval_status_type =
--                                                               'approv')
--    AND c.promotion_id = pep.eligible_promotion_id
--    AND pep.promotion_id = p_in_promotion_id
--    AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = c.submitter_id)
--    AND NOT EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = cr.participant_id)
--    AND ci.date_approved between p_in_start_date AND p_in_end_date 
--    UNION ALL    
--    SELECT NULL sender_user_id ,
--           NULL receiver_user_id,
--           c.promotion_id,
--           NULL sender_node_id,
--           NULL receiver_node_id,
--           rc.behavior,
--           c.date_created,
--           c.created_by,
--           0      
--    FROM claim c,claim_item ci,claim_recipient cr,recognition_claim rc,(SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep
--    WHERE c.claim_id = ci.claim_id
--    AND ci.claim_item_id = cr.claim_item_id
--    AND rc.claim_id = c.claim_id
--    AND (   ci.approval_status_type =
--                                                               'winner'
--                                                         OR ci.approval_status_type =
--                                                               'approv')
--    AND c.promotion_id = pep.eligible_promotion_id
--    AND pep.promotion_id = p_in_promotion_id
--    AND NOT EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = c.submitter_id)
--    AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = cr.participant_id)
--    AND ci.date_approved between p_in_start_date AND p_in_end_date      
    ));

    v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
END p_engagement_log;

PROCEDURE p_engagement_score_wrapper (pi_requested_user_id  IN  NUMBER,
   po_return_code        OUT NUMBER,
   po_error_message      OUT VARCHAR2
   ) IS
/*******************************************************************************
-- Purpose: Wrapper to call all the engagement related procedures.
-- MODIFICATION HISTORY
-- Person                        Date            Comments
-- ---------                      ----------      ------------------------------------------------
--  Ravi Dhanekula       06/09/2014      Creation
--Suresh J              11/25/2014   RPM Peformance Tunning Fixes:
                                    i) Added Gather Stats call on lookup tables and also when truncating data from all tables
                                      in order to present an accurate execution plan with stats to the optimizer 
                                   ii) Re-sequenced UPDATE command to be called post Truncate DDL command to keep the transaction state intact.
                                  iii) Added Drop Index at the start and Create Index at the end on ENG_USER_CONNECTED_TO_FROM when truncating 
                                       and reloading the table to fix performance Issue.
-- Ravi Dhanekula      10/21/2015 Bug # 64360 - pkg_engagement_extract incorrect data with node change
*******************************************************************************/                                            

PRAGMA AUTONOMOUS_TRANSACTION;

p_in_start_date DATE;
p_promotion_start_date DATE;
p_prev_process_date DATE;
p_in_end_date  DATE := SYSDATE;
 
e_refresh_fail     EXCEPTION;
v_stage       VARCHAR2(250);
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_engagement_score_wrapper');   
c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
c_created_by         CONSTANT engagement_behavior_summary.created_by%TYPE:= 0;
v_live_promotion_id NUMBER(10);
v_index_exist  NUMBER(2);    --11/25/2014

CURSOR cur_gather_stat IS    --11/25/2014 
    SELECT *
    FROM user_tables
    WHERE table_name LIKE 'ENG_%';

CURSOR C1 IS --10/21/2015
select e.engagement_detail_id,e2.login_activity_count from engagement_score_detail e,engagement_score_detail e2 where
  e2.user_id = e.user_id AND e2.time_frame=e.time_frame AND e.trans_month=e2.trans_month AND e.trans_year = e2.trans_year AND e2.login_activity_count <>0
 AND e.login_activity_count=0;

BEGIN

 v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
 
 BEGIN
 
 BEGIN
 v_stage := 'Get live promotion_id for engagement.'; 
 SELECT promotion_id INTO v_live_promotion_id FROM promotion
                                               WHERE promotion_type = 'engagement' AND promotion_status = 'live';
EXCEPTION WHEN OTHERS THEN
RAISE e_refresh_fail;                                           
END;


v_stage := 'p_eng_audience_sync_check' ;
 p_eng_audience_sync_check 
 (v_live_promotion_id, 
  po_return_code,
  po_error_message);
  
    IF po_return_code != 0 THEN
        po_error_message := 'p_eng_audience_sync_check - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;

                                               
  v_stage := 'Get prev_process_date from promo_engagement.'; 
           
         SELECT prev_process_date,NVL(prev_process_date,ADD_MONTHS ( TRUNC (promotion_start_date, 'mm'), ROWNUM                  - 1)),promotion_start_date 
         INTO p_prev_process_date,p_in_start_date,p_promotion_start_date
        FROM promo_engagement pe,
            promotion p
        WHERE pe.promotion_id = p.promotion_id
            AND p.promotion_status = 'live' ;  
--   v_stage := 'Update prev_process_date on promo_engagement.';   --11/25/2014           
--        UPDATE promo_engagement
--        SET prev_process_date = p_in_end_date,
--               as_of_date = p_in_end_date
--        WHERE promotion_id IN (SELECT promotion_id FROM promotion
--                                               WHERE promotion_type = 'engagement' AND promotion_status = 'live');            
    END;
    
    IF p_prev_process_date IS NULL THEN
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENG_RECOG_RECVD_BY_PROMO';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENG_RECOG_RECVD_BY_PROMO_USER';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENG_RECOG_RECVD_USER_MODE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENG_RECOG_SENT_BY_PROMO';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENG_RECOG_SENT_BY_PROMO_USER';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENG_RECOG_SENT_USER_MODE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENG_RULES_USER';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENGAGEMENT_BEHAVIOR_SUMMARY';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENGAGEMENT_BEHAVIOR_USER_MODE';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENGAGEMENT_ELIG_USER';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENGAGEMENT_LOG';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENGAGEMENT_QUART_LOGIN_CNT';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENGAGEMENT_RECENT_RECOGNITION';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENGAGEMENT_RECOG_RECEIVED';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENGAGEMENT_RECOG_RECVD_BY_USER';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENGAGEMENT_RECOG_SENT';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENGAGEMENT_RECOG_SENT_BY_USER';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENGAGEMENT_SCORE_DETAIL';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENGAGEMENT_SCORE_SUMMARY';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE ENG_USER_CONNECTED_TO_FROM';
    
    FOR rec IN cur_gather_stat LOOP      --11/25/2014
        BEGIN 
        SYS.DBMS_STATS.GATHER_TABLE_STATS 
        ( OWNNAME          => USER,
          TABNAME          => UPPER(rec.table_name),
          ESTIMATE_PERCENT => 10,
          CASCADE          => TRUE,
          METHOD_OPT       => 'for all columns size auto'
        );
        END;
    END LOOP;

    SELECT count(*) INTO v_index_exist   --11/25/2014  
    FROM user_indexes 
    WHERE index_name = 'ENG_USER_CONN_IDX2';
    
    IF v_index_exist = 1 THEN   --11/25/2014
        EXECUTE IMMEDIATE 'DROP INDEX ENG_USER_CONN_IDX2';   --11/25/2014
    END IF;
        
   END IF;
    
    DELETE FROM gtt_months_between_dates;
   INSERT INTO gtt_months_between_dates       
SELECT TO_CHAR ( ADD_MONTHS ( TRUNC ( p_in_start_date, 'mm'), ROWNUM - 1), 'MM') AS month,
    TO_CHAR ( ADD_MONTHS ( TRUNC ( p_in_start_date, 'mm'), ROWNUM       - 1), 'YYYY')  AS YEAR,
    ADD_MONTHS ( TRUNC (p_in_start_date, 'mm'), ROWNUM                  - 1)         AS month_start_date,
    last_DAY(ADD_MONTHS ( TRUNC (p_in_start_date, 'mm'), ROWNUM                  - 1))         AS month_end_date,
    CASE WHEN ADD_MONTHS ( TRUNC (p_in_start_date, 'mm'), ROWNUM                  - 3) < TRUNC (p_promotion_start_date, 'mm') THEN NULL
    ELSE
    ADD_MONTHS ( TRUNC (p_in_start_date, 'mm'), ROWNUM                  - 3) 
    END        AS quarter_start_date,
     CASE WHEN ADD_MONTHS ( TRUNC (p_in_start_date, 'mm'), ROWNUM                  - 12) < TRUNC (p_promotion_start_date, 'mm') THEN NULL
    ELSE
    ADD_MONTHS ( TRUNC (p_in_start_date, 'mm'), ROWNUM                  - 12) 
    END        AS year_start_date
  FROM all_objects
  WHERE ROWNUM <= MONTHS_BETWEEN ( TRUNC (sysdate, 'mm'), TRUNC ( p_in_start_date, 'mm')) + 1;     


v_stage := 'p_eng_elig_user' ;
 p_eng_elig_user 
 (pi_requested_user_id, 
  v_live_promotion_id,
  po_return_code,
  po_error_message);
  
    IF po_return_code != 0 THEN
        po_error_message := 'p_eng_elig_user - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;

    IF p_prev_process_date IS NULL THEN    --11/25/2014
    v_stage := 'Gather Stats for ENGAGEMENT_ELIG_USER if p_prev_process_date is NULL';      --11/25/2014
        SYS.DBMS_STATS.GATHER_TABLE_STATS         --11/25/2014
        ( OWNNAME          => USER,
          TABNAME          => 'ENGAGEMENT_ELIG_USER',
          ESTIMATE_PERCENT => 10,
          CASCADE          => TRUE,
          METHOD_OPT       => 'for all columns size auto'
        );
    END IF;
      
      v_stage := 'p_eng_elig_manager' ;
 p_eng_elig_manager 
 (po_return_code,
  po_error_message);
  
    IF po_return_code != 0 THEN
        po_error_message := 'p_eng_elig_manager - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;
      
v_stage := 'p_engagement_score_detail' ;
 p_engagement_score_detail 
 (pi_requested_user_id,
  v_live_promotion_id,
  p_in_start_date,
  p_in_end_date,
  po_return_code,
  po_error_message);
  
    IF po_return_code != 0 THEN
        po_error_message := 'p_engagement_score_detail - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;
   
  
  v_stage := 'p_engagement_score_summary' ;
  p_engagement_score_summary 
 (pi_requested_user_id,
  p_in_start_date,
  p_in_end_date,
  po_return_code,
  po_error_message);
  
  
    IF po_return_code != 0 THEN
        po_error_message := 'p_engagement_score_summary - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;
      
  v_stage := 'p_eng_user_connected_to_from' ;
 p_eng_user_connected_to_from 
 (v_live_promotion_id,
  p_in_start_date,
  p_in_end_date,
  po_return_code,
  po_error_message);
  
    IF po_return_code != 0 THEN
        po_error_message := 'p_eng_user_connected_to_from - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;
  
  v_stage := 'p_engagement_log' ;
  
  p_engagement_log 
  (pi_requested_user_id,
  v_live_promotion_id,
  p_in_start_date,
  p_in_end_date,
  po_return_code,
  po_error_message);
  
  
    IF po_return_code != 0 THEN
        po_error_message := 'p_engagement_log - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;
  
  v_stage := 'p_engagement_behavior_summary' ;

    IF p_prev_process_date IS NULL THEN    --11/25/2014
    v_stage := 'Gather Stats for ENGAGEMENT_LOG if p_prev_process_date is NULL';      --11/25/2014
        SYS.DBMS_STATS.GATHER_TABLE_STATS         --11/25/2014
        ( OWNNAME          => USER,
          TABNAME          => 'ENGAGEMENT_LOG',
          ESTIMATE_PERCENT => 10,
          CASCADE          => TRUE,
          METHOD_OPT       => 'for all columns size auto'
        );
    END IF;
  
  p_engagement_behavior_summary
  (pi_requested_user_id,
  p_in_start_date,
  p_in_end_date,
  po_return_code,
  po_error_message);
  
    IF po_return_code != 0 THEN
        po_error_message := 'p_engagement_behavior_summary - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;
  
  v_stage := 'p_engagement_behavior_usermode' ;
  
  p_engagement_behavior_usermode
   (pi_requested_user_id,
  po_return_code,
  po_error_message);
  
   IF po_return_code != 0 THEN
        po_error_message := 'p_engagement_behavior_usermode - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;
  
  v_stage := 'p_engagement_recog_sent' ;
  
  p_engagement_recog_sent 
 (pi_requested_user_id,
  po_return_code,
  po_error_message);
  
   IF po_return_code != 0 THEN
        po_error_message := 'p_engagement_recog_sent - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;
      
  v_stage := 'p_engagement_recog_received' ;
  
  p_engagement_recog_received 
 (pi_requested_user_id,
  po_return_code,
  po_error_message);
  
   IF po_return_code != 0 THEN
        po_error_message := 'p_engagement_recog_received - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;
  
  v_stage := 'p_engagement_quart_login_count' ;
  
  p_engagement_quart_login_count
  (pi_requested_user_id, 
  po_return_code,
  po_error_message);
  
   IF po_return_code != 0 THEN
        po_error_message := 'p_engagement_quart_login_count - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;
  
  v_stage := 'p_eng_recog_sent_by_user' ;
  
  p_eng_recog_sent_by_user
  (pi_requested_user_id,
  po_return_code,
  po_error_message);
  
   IF po_return_code != 0 THEN
        po_error_message := 'p_eng_recog_sent_by_user - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;
  
  v_stage := 'p_eng_recog_recvd_by_user' ;
  
  p_eng_recog_recvd_by_user
  (pi_requested_user_id,
  po_return_code,
  po_error_message);
  
   IF po_return_code != 0 THEN
        po_error_message := 'p_eng_recog_recvd_by_user - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;
  
  v_stage := 'p_eng_recog_sent_by_promo' ;
  
  p_eng_recog_sent_by_promo
  (pi_requested_user_id,
  po_return_code,
  po_error_message);
  
   IF po_return_code != 0 THEN
        po_error_message := 'p_eng_recog_sent_by_promo - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;
  
  v_stage := 'p_eng_recog_recvd_by_promo' ;
  
  p_eng_recog_recvd_by_promo
  (pi_requested_user_id,
  po_return_code,
  po_error_message);
  
   IF po_return_code != 0 THEN
        po_error_message := 'p_eng_recog_recvd_by_promo - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;

 v_stage := 'p_eng_recog_sent_user_mode' ;  
  p_eng_recog_sent_user_mode
  (pi_requested_user_id,
  po_return_code,
  po_error_message);
  
   IF po_return_code != 0 THEN
        po_error_message := 'p_eng_recog_sent_user_mode - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;

v_stage := 'p_eng_recog_recvd_user_mode' ;  
  p_eng_recog_recvd_user_mode
  (pi_requested_user_id,
  po_return_code,
  po_error_message);
  
   IF po_return_code != 0 THEN
        po_error_message := 'p_eng_recog_recvd_user_mode - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;

 IF po_return_code != 0 THEN
        po_error_message := 'p_eng_recog_sent_user_mode - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;

v_stage := 'p_engagement_user_recent_recog' ;  
  p_engagement_user_recent_recog
  (pi_requested_user_id,
  v_live_promotion_id,
  p_in_start_date,
  p_in_end_date,
  po_return_code,
  po_error_message);
  
   IF po_return_code != 0 THEN
        po_error_message := 'p_engagement_user_recent_recog - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;

v_stage := 'p_eng_recog_sent_bypromo_user' ;  
  p_eng_recog_sent_bypromo_user
  (pi_requested_user_id,
  po_return_code,
  po_error_message);
  
   IF po_return_code != 0 THEN
        po_error_message := 'p_eng_recog_sent_bypromo_user - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;

v_stage := 'p_eng_recog_recvd_bypromo_user' ;  
  p_eng_recog_recvd_bypromo_user
  (pi_requested_user_id,
  po_return_code,
  po_error_message);
  
   IF po_return_code != 0 THEN
        po_error_message := 'p_eng_recog_recvd_bypromo_user - '||po_error_message;       
        RAISE e_refresh_fail;
      END IF;

    IF p_prev_process_date IS NULL THEN    --11/25/2014
        
        SELECT count(*) INTO v_index_exist     
        FROM user_indexes 
        WHERE index_name = 'ENG_USER_CONN_IDX2';
        
        IF v_index_exist = 0 THEN   --11/25/2014
            v_stage := 'Adding Index ENG_USER_CONN_IDX2';      --11/25/2014
            EXECUTE IMMEDIATE 'CREATE INDEX ENG_USER_CONN_IDX2 ON ENG_USER_CONNECTED_TO_FROM (USER_ID,TIME_FRAME, TRANS_MONTH, TRANS_YEAR)';   --11/25/2014
        END IF;
        
    END IF;

FOR C1_R IN C1 LOOP --10/21/2015
 
 UPDATE engagement_score_detail
 SET login_activity_count = c1_r.login_activity_count
 WHERE engagement_detail_id = c1_r. engagement_detail_id;
 
 END LOOP;

v_stage := 'Update prev_process_date on promo_engagement.';      --11/25/2014           
    UPDATE promo_engagement    --11/25/2014
    SET prev_process_date = p_in_end_date,
           as_of_date = p_in_end_date
    WHERE promotion_id IN (SELECT promotion_id FROM promotion
                                           WHERE promotion_type = 'engagement' AND promotion_status = 'live');            


COMMIT;

po_return_code := 00; -- Success
po_error_message := 'Engagement data refresh is Successful';
  
  prc_execution_log_entry( c_process_name, 0, 'INFO','Engagement data Refresh Process is completed Successfully',NULL);    

EXCEPTION
  WHEN e_refresh_fail THEN
    prc_execution_log_entry( c_process_name, 0, 'ERROR','Error at Stage : '||v_stage||' - '||po_error_message, NULL);    
    ROLLBACK;
    po_error_message := 'Report refresh failed at Stage : '||v_stage||' - '||po_error_message;

  WHEN OTHERS THEN
    prc_execution_log_entry( c_process_name, 0, 'ERROR', SQLERRM, NULL);
    po_return_code := 99; -- Error
    po_error_message := SQLERRM;
    ROLLBACK;
END p_engagement_score_wrapper;

PROCEDURE p_engagement_behavior_summary(p_user_id        IN  NUMBER,
   p_in_start_date     IN  DATE, 
   p_in_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS
--
--
-- Purpose: Populate engagement_behavior_summary table.
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
-- Ravi Dhanekula 06/16/2014 Initial
--Suresh J        11/05/2014 i) Resolved issues related to MERGE that was updating Node ID and Counts as NULL for certain set of records.
--Ravii Dhanekula 11/26/2014 Bug # 58743.No Behavior Badge On RPM Secondary Page
--Suresh J       12/02/2014 Performance Fix - Added Hint to detail_derived_summary to force NESTED LOOP over MERGE JOIN CARTESIAN  
   
--  c_created_by  CONSTANT engagement_behavior_summary.created_by%TYPE:=0;
v_stage       VARCHAR2(250);
v_rec_cnt  INTEGER;

c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_engagement_behavior_summary');
c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
c_created_by         CONSTANT engagement_behavior_summary.created_by%TYPE:= 0;
c_default_key_field_hash   CONSTANT engagement_behavior_summary.key_field_hash%TYPE :=
                               dbms_crypto.hash( utl_raw.cast_to_raw(   --07/20/2016                                        
                                 '/' || ' '  -- behavior
                                 || '/' || ' '  -- trans_month
                                 || '/' || ' '  -- trans_year                                 
                               ),2);

BEGIN

  v_stage := 'Start';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
   

   ------------
   -- merge derived summary records
   v_stage := 'MERGE detail derived summary records';
   
   MERGE INTO engagement_behavior_summary d
   USING (   
   WITH rpt_teamsum AS
            (  -- build team summary records            
            SELECT -- key fields
                      detail_node_id,
                      SUM(received_count) received_count,                                                 
                      behavior           AS behavior,                                         
                      trans_month,
                      trans_year,
                      time_frame,
                     SUM(sent_count) sent_count FROM (                     
               SELECT
                      h.node_id                     AS detail_node_id,
                      0 received_count,
                      d.behavior           AS behavior,                                         
                      timeline.month trans_month,
                      timeline.year trans_year,
                      'month' time_frame,
                     1 sent_count
                 FROM  engagement_log d,
                  node h,
                  gtt_months_between_dates timeline  
                WHERE h.node_id = d.sender_node_id
                AND d.behavior <>'none' --11/26/2014
                AND TRUNC(d.date_created) BETWEEN month_start_date and month_end_date
         UNION ALL
         SELECT -- key fields
                      h.node_id                     AS detail_node_id,
                      1 received_count,                                                          
                      d.behavior           AS behavior,                                         
                      timeline.month trans_month,
                      timeline.year trans_year,
                      'month' time_frame,
                     0 sent_count              
                 FROM  engagement_log d,
                   node h,
                   gtt_months_between_dates timeline 
                WHERE h.node_id = d.receiver_node_id
                AND  d.behavior <>'none' --11/26/2014
                AND TRUNC(d.date_created) BETWEEN month_start_date and month_end_date
                UNION ALL
                SELECT
                      h.node_id                     AS detail_node_id,
                      0 received_count,
                      d.behavior           AS behavior,                                         
                      timeline.month trans_month,
                      timeline.year trans_year,
                      'quarter' time_frame,
                     1 sent_count
                 FROM  engagement_log d,
                  node h,
                  gtt_months_between_dates timeline  
                WHERE h.node_id = d.sender_node_id
                AND  d.behavior <>'none' --11/26/2014
                AND TRUNC(d.date_created) BETWEEN quarter_start_date and month_end_date
         UNION ALL
         SELECT -- key fields
                      h.node_id                     AS detail_node_id,
                      1 received_count,                                                          
                      d.behavior           AS behavior,                                         
                      timeline.month trans_month,
                      timeline.year trans_year,
                      'quarter' time_frame,
                     0 sent_count              
                 FROM  engagement_log d,
                   node h,
                   gtt_months_between_dates timeline 
                WHERE h.node_id = d.receiver_node_id
                AND  d.behavior <>'none' --11/26/2014
                AND TRUNC(d.date_created) BETWEEN quarter_start_date and month_end_date
                UNION ALL                
                SELECT
                      h.node_id                     AS detail_node_id,
                      0 received_count,
                      d.behavior           AS behavior,                                         
                      timeline.month trans_month,
                      timeline.year trans_year,
                      'year' time_frame,
                     1 sent_count
                 FROM  engagement_log d,
                  node h,
                  gtt_months_between_dates timeline  
                WHERE h.node_id = d.sender_node_id
                AND  d.behavior <>'none' --11/26/2014
                AND TRUNC(d.date_created) BETWEEN year_start_date and month_end_date
         UNION ALL
         SELECT -- key fields
                      h.node_id                     AS detail_node_id,
                      1 received_count,                                                          
                      d.behavior           AS behavior,                                         
                      timeline.month trans_month,
                      timeline.year trans_year,
                      'year' time_frame,
                     0 sent_count              
                 FROM  engagement_log d,
                   node h,
                   gtt_months_between_dates timeline 
                WHERE h.node_id = d.receiver_node_id
                AND  d.behavior <>'none' --11/26/2014
                AND TRUNC(d.date_created) BETWEEN year_start_date and month_end_date
                )                
                GROUP BY detail_node_id,                          
                      behavior,                    
                      trans_month,
                      trans_year,
                      time_frame
--                      header_node_id        
                              ), detail_derived_summary AS    --12/02/2014
            (  -- derive summaries based on detail data
               SELECT /*+ ordered use_nl(RPT_TEAMSUM)  */   -- key fields
                      h.node_id AS detail_node_id,
                      'nodesum' AS sum_type,                     
                      rt.behavior,
                      rt.time_frame,
                      rt.trans_month,
                      rt.trans_year,
                      -- reference fields
                                            dbms_crypto.hash( utl_raw.cast_to_raw(--07/20/2016
                        rt.behavior
                        || '/' || rt.trans_month
                        || '/' || rt.trans_year
                        || '/' || rt.time_frame                        
                      ),2) as key_field_hash,
--                      h.parent_node_id AS header_node_id,                    
                      -- count fields                     
                      SUM(rt.sent_count)          AS sent_count,
                      SUM(rt.received_count)          AS received_count                                         
                 FROM ( -- associate each node to all its hierarchy nodes
                        SELECT np.node_id,
                               p.column_value AS path_node_id
                          FROM ( -- get node hierarchy path
                                 SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                   FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                               ) np,
                               -- parse node path into individual nodes
                               -- pivoting the node path into separate records
                               TABLE( CAST( MULTISET(
                                  SELECT TO_NUMBER(
                                            SUBSTR(np.node_path,
                                                   INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                                   INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                            )
                                         )
                                    FROM dual
                                 CONNECT BY LEVEL <= np.hier_level 
                               ) AS sys.odcinumberlist ) ) p
                      ) npn,
                       (SELECT node_id,node_type_id,
         NAME as node_name,
         parent_node_id,
         is_deleted,
         (SELECT NAME FROM node WHERE node_id = n.parent_node_id) as parent_node_name,
         level AS hier_level,
         path,
         description
    FROM node n
    START WITH parent_node_id IS NULL
  CONNECT BY PRIOR node_id = parent_node_id) h,
                      rpt_teamsum rt
                   -- recognition count restriction does not apply to non-pax merch
                WHERE rt.detail_node_id = npn.node_id
                  AND npn.path_node_id = h.node_id
                GROUP BY h.node_id,                    
                      rt.behavior,                     
                      rt.trans_month,
                      rt.time_frame,
                      rt.trans_year           
            ) -- end detail_derived_summary
--            select * from detail_derived_summary            
            -- compare existing summary records with detail derived summaries
              SELECT es.s_rowid,   
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.header_node_id,                                               
                            s2.key_field_hash
                       FROM engagement_behavior_summary s2
                            ,gtt_months_between_dates gtt  --11/05/2014   
                       WHERE s2.trans_month = gtt.month AND s2.trans_year = gtt.year  --11/05/2014      
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                   FULL OUTER JOIN detail_derived_summary dds
                   ON (   es.header_node_id = dds.detail_node_id                      
                      AND es.key_field_hash   = dds.key_field_hash
                      )
         ) s
      ON (d.ROWID = s.s_rowid)
    WHEN MATCHED THEN
      UPDATE SET
         d.header_node_id           = s.detail_node_id,
         d.sent_count                  = s.sent_count,
         d.received_count            = s.received_count,
         d.modified_by              = c_created_by,
         d.date_modified            = SYSDATE
       WHERE ( -- only update records with different values
                DECODE(d.header_node_id,           s.detail_node_id,           1, 0) = 0              
             OR DECODE(d.received_count,          s.received_count,          1, 0) = 0
             OR DECODE(d.sent_count,          s.sent_count,          1, 0) = 0        
             )     
    WHEN NOT MATCHED THEN
      INSERT
      ( 
         behavior,         
         -- reference fields
         key_field_hash,
         header_node_id,        
         -- count fields        
         sent_count,
         received_count,
         time_frame,
         trans_month,
         trans_year,  
         -- audit fields
         created_by,
         date_created,
         modified_by,
         date_modified
      )
      VALUES
      (s.behavior,         
         s.key_field_hash,
         s.detail_node_id,        
         -- count fields        
         s.sent_count,
         s.received_count,  
         s.time_frame,
         s.trans_month,
         s.trans_year,        
         -- audit fields
         1,
         SYSDATE,
         NULL,
         NULL
      );
      
      
   v_rec_cnt := SQL%ROWCOUNT;
   v_stage := 'End of Process: '||c_process_name;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);     
   p_return_code :=0;

EXCEPTION
  WHEN others THEN
  p_return_code :=99;
  p_error_message := SQLERRM;
    prc_execution_log_entry('p_engagement_behavior_summary',1,'ERROR','stage '||v_stage||SQLERRM,null);
END;

   
PROCEDURE p_engagement_behavior_usermode (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS
   
   /*******************************************************************************
-- Purpose: To Populate usermode behavior counts.
-- MODIFICATION HISTORY
-- Person              Date            Comments
-- ---------           ----------      ------------------------------------------------
--  Ravi Dhanekula       07/28/2014      Creation
*******************************************************************************/    
    c_process_name           CONSTANT execution_log.process_name%TYPE := UPPER('p_engagement_behavior_usermode');
    v_rec_cnt  INTEGER;
   BEGIN
   
    v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

MERGE INTO engagement_behavior_user_mode d
    USING 
        (                  
    WITH detail_derived_summary AS
        ( -- build team summary records        
        SELECT user_id,node_id,behavior,SUM(sent_count) sent_count, SUM(received_count) received_count,trans_month,trans_year,time_frame FROM (                        
        SELECT  el.sender_user_id user_id,el.sender_node_id node_id,1 sent_count,0 received_count ,timeline.month trans_month,timeline.year trans_year,'month' time_frame,el.behavior
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN month_start_date and month_end_date
        AND el.behavior <>'none'  
        UNION ALL        
        SELECT  el.receiver_user_id user_id,el.receiver_node_id node_id,0 sent_count,1 received_count,timeline.month trans_month,timeline.year trans_year,'month' time_frame,el.behavior
         FROM engagement_log el,
                  gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN month_start_date and month_end_date
        AND el.behavior <>'none'     
        UNION ALL        
        SELECT  el.sender_user_id user_id,el.sender_node_id node_id,1 sent_count,0 received_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame,el.behavior
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN quarter_start_date and month_end_date
        AND el.behavior <>'none'     
        UNION ALL
        SELECT  el.receiver_user_id user_id,el.receiver_node_id node_id,0 sent_count,1 received_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame,el.behavior
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN quarter_start_date and month_end_date
        AND el.behavior <>'none'
        UNION ALL        
        SELECT  el.sender_user_id user_id,el.sender_node_id node_id,1 sent_count,0 received_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame,el.behavior
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN year_start_date and month_end_date
        AND el.behavior <>'none'
        UNION ALL
        SELECT  el.receiver_user_id user_id,el.receiver_node_id node_id,0 sent_count,1 received_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame,el.behavior
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN year_start_date and month_end_date
        AND el.behavior <>'none'  
        )
        GROUP BY user_id,node_id,trans_month,trans_year,time_frame,behavior             
        )
          SELECT es.s_rowid,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.user_id,
                            s2.node_id,
                            s2.behavior,
                            s2.time_frame,
                            s2.trans_month,
                            s2.trans_year,
                            s2.sent_count,
                            s2.received_count                                                        
                       FROM engagement_behavior_user_mode s2, gtt_months_between_dates gtt
                       WHERE s2.trans_month = gtt.month AND s2.trans_year = gtt.year                      
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                 FULL OUTER JOIN detail_derived_summary dds
                   ON (       es.user_id    = dds.user_id
                       AND es.node_id = dds.node_id
                       AND es.time_frame = dds.time_frame
                       AND es.trans_month = dds.trans_month
                       AND es.trans_year = dds.trans_year
                       AND es.behavior = dds.behavior
                      )
         ) s         
      ON (d.ROWID = s.s_rowid)            
        WHEN MATCHED THEN
         UPDATE SET  d.sent_count = s.sent_count,
                     d.received_count = s.received_count
      WHERE NOT (   DECODE(d.sent_count,  s.sent_count,                    1, 0) = 1
                 AND DECODE(d.received_count, s.received_count,                   1, 0) = 1                             
                 )
      WHEN NOT MATCHED THEN
      INSERT ( user_id,            
               node_id,   
               sent_count,
               received_count,
               trans_month,
               trans_year,
               time_frame,
               behavior,
               date_created,
               created_by
               )
               VALUES
              (s.user_id,       
               s.node_id,        
               s.sent_count,
               s.received_count,
               s.trans_month,
               s.trans_year,
               s.time_frame,
               s.behavior,
               SYSDATE,
               p_user_id              
              );

    v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    v_rec_cnt := SQL%ROWCOUNT;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
    
EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;          
END;

PROCEDURE p_engagement_quart_login_count
 (p_user_id        IN  NUMBER,   
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS
/*******************************************************************************
-- Purpose: To Populate Quarterly Login Count At Node Level
-- MODIFICATION HISTORY
-- Person              Date            Comments
-- ---------           ----------      ------------------------------------------------
--  Swati              07/08/2014      Creation
*******************************************************************************/    
    
    p_quarter_start_date     DATE;
    p_start_date            DATE;
    p_run_date                DATE;    
    c_process_name           CONSTANT execution_log.process_name%TYPE := UPPER('p_engagement_quart_login_count');
    
    v_rec_cnt  INTEGER;
BEGIN
    v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
    
    p_run_date := sysdate;
        
    BEGIN    
        SELECT  TRUNC(last_DAY(ADD_MONTHS(p_run_date, -3))+1) INTO p_quarter_start_date
        FROM    dual;
    END;
    
      --v_stage := Select  primary_audience_type for the live engagement promotion.;     
                
    IF p_quarter_start_date < p_start_date THEN
        p_quarter_start_date := p_start_date;
    END IF;
    
    v_stage := 'Quarterly Count';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
    
          MERGE INTO engagement_quart_login_cnt d
    USING
    (    
    WITH rpt_teamsum AS
        (  -- build team summary records        
            SELECT users.node_id,trunc(la.login_date_time) date_created,count(*) login_count FROM (
SELECT     user_id,node_id FROM engagement_elig_user WHERE status = 1) users,
        login_activity la
        WHERE la.user_id = users.user_id
        AND la.login_date_time BETWEEN p_quarter_start_date and p_run_date GROUP BY users.node_id,trunc(la.login_date_time)
        ) , detail_derived_summary AS
        ( -- derive summaries based on team summary data
           SELECT -- key fields
               npn.path_node_id node_id,rt.date_created,       
                SUM(rt.login_count) login_activity_count
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                   FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
--                  (SELECT node_id,parent_node_id,
--         level AS hier_level
--    FROM node n
--    START WITH parent_node_id IS NULL
--  CONNECT BY PRIOR node_id = parent_node_id) h,
                  rpt_teamsum rt
            WHERE -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
                  rt.node_id = npn.node_id 
--                 AND npn.path_node_id = h.node_id
            GROUP BY
                    rt.date_created,npn.path_node_id
        )-- end detail_derived_summary 
--       select * from detail_derived_summary      
    SELECT es.s_rowid,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.node_id,
                           s2.date_created,
                           s2.login_activity_count                            
                       FROM engagement_quart_login_cnt s2                      
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                 FULL OUTER JOIN detail_derived_summary dds
                   ON (       es.node_id    = dds.node_id
                        AND es.date_created = dds.date_created
                      )
    )s        
    ON (d.ROWID = s.s_rowid)
    WHEN MATCHED THEN
    UPDATE SET 
        d.login_activity_count = s.login_activity_count        
    WHEN NOT MATCHED THEN
    INSERT (node_id,
            date_created,
            login_activity_count)
    VALUES (
            s.node_id,
            s.date_created,
            s.login_activity_count);     
    
          
    v_stage := 'Deleting Older Records';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
    
    DELETE FROM engagement_quart_login_cnt
   WHERE DATE_CREATED < p_quarter_start_date ;    
    
    v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    v_rec_cnt := SQL%ROWCOUNT;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
    
EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
    
END p_engagement_quart_login_count;

PROCEDURE p_engagement_recog_sent (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) is

/*******************************************************************************
-- Purpose: To get the counts of recognitions sent by each nodesum
-- MODIFICATION HISTORY
-- Person               Date            Comments
-- ---------            ----------      ------------------------------------------------
--  KrishnaDeepika      07/18/2014      Creation
--Suresh J             10/16/2014      Bug 57277 - Replaced Count(*) with Count(DISTINCT ID) to include unique people only 
--Suresh J       12/02/2014 Performance Fix - Added Hint to detail_derived_summary to force NESTED LOOP over MERGE JOIN CARTESIAN
--Ravi Dhanekula 12/11/2014 Re-wrote the merge query to have unique people counts as per the CR listed in bug # 57277.
*******************************************************************************/    
    
    c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_engagement_recog_sent');
    v_rec_cnt  INTEGER;
BEGIN
    v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
    
   /*MERGE INTO engagement_recog_sent d
    USING 
    (  
    WITH rpt_teamsum AS
        (  -- build team summary records         
--        SELECT  el.sender_node_id,el.receiver_node_id,count(DISTINCT receiver_user_id) received_count ,timeline.month trans_month,timeline.year trans_year,'month' time_frame  --10/16/2014 
        SELECT  el.receiver_node_id,count(DISTINCT receiver_user_id) received_count ,timeline.month trans_month,timeline.year trans_year,'month' time_frame  --10/16/2014
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN month_start_date and month_end_date
--        GROUP BY sender_node_id,receiver_node_id,timeline.month,timeline.year 
        GROUP BY receiver_node_id,timeline.month,timeline.year
        UNION ALL
--        SELECT  el.sender_node_id,el.receiver_node_id,count(DISTINCT receiver_user_id) received_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame   --10/16/2014
        SELECT  el.receiver_node_id,count(DISTINCT receiver_user_id) received_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame   --10/16/2014
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN quarter_start_date and month_end_date
--        GROUP BY sender_node_id,receiver_node_id,timeline.month,timeline.year 
        GROUP BY receiver_node_id,timeline.month,timeline.year
        UNION ALL
--        SELECT  el.sender_node_id,el.receiver_node_id,count(DISTINCT receiver_user_id) received_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame     --10/16/2014
        SELECT  el.receiver_node_id,count(DISTINCT receiver_user_id) received_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame     --10/16/2014
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN year_start_date and month_end_date
--        GROUP BY sender_node_id,receiver_node_id,timeline.month,timeline.year        
        GROUP BY receiver_node_id,timeline.month,timeline.year
        ) , detail_derived_summary AS  --12/02/2014
        ( -- derive summaries based on team summary data           
           SELECT /*+ ordered use_nl(RPT_TEAMSUM)   -- key fields
               h.node_id,
                rt.receiver_node_id,  
                SUM(rt.received_count) received_count,
                rt.trans_month,
                rt.trans_year,
                rt.time_frame,
         RANK ()
         OVER (PARTITION BY h.node_id
               ORDER BY SUM(rt.received_count) desc,rt.receiver_node_id)
            RANK
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                  FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
                  (SELECT node_id,parent_node_id,
         level AS hier_level
    FROM node n
    START WITH parent_node_id IS NULL
  CONNECT BY PRIOR node_id = parent_node_id) h,
                  rpt_teamsum rt
            WHERE -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
--                  rt.sender_node_id = npn.node_id 
                  rt.receiver_node_id = npn.node_id
                 AND npn.path_node_id = h.node_id
            GROUP BY
                    h.node_id,rt.receiver_node_id,rt.trans_month,rt.trans_year,rt.time_frame
                    order by h.node_id
        )        
          SELECT es.s_rowid,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.node_id,
                            s2.trans_month,
                            s2.trans_year,
                            s2.time_frame,
                            s2.receiver_node_id,
                            s2.received_count                            
                       FROM engagement_recog_sent s2, gtt_months_between_dates gtt
                       WHERE s2.trans_month = gtt.month AND s2.trans_year = gtt.year                
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                 FULL OUTER JOIN detail_derived_summary dds
                   ON (       es.node_id    = dds.node_id
                                 AND es.receiver_node_id    = dds.receiver_node_id
                                 AND es.time_frame    = dds.time_frame
                                 AND es.trans_month    = dds.trans_month
                                 AND es.trans_year    = dds.trans_year                                  
                      )
         ) s         
      ON (d.ROWID = s.s_rowid)
        WHEN MATCHED THEN
         UPDATE SET d.received_count = s.received_count,                   
                    d.rank = s.rank
      WHEN NOT MATCHED THEN
      INSERT ( node_id,
               receiver_node_id,
               received_count,
               trans_month,
               trans_year,
               time_frame,
                     rank,
                date_created,
                created_by)
               VALUES
              (s.node_id,
               s.receiver_node_id,
               s.received_count,
               s.trans_month,
               s.trans_year,
               s.time_frame,
               s.rank,
               SYSDATE,
               p_user_id
              );
*/

MERGE INTO engagement_recog_sent d --12/11/2014
    USING 
    (    
        WITH detail_derived_summary AS
        (                      
           SELECT
           el.receiver_node_id,npn.path_node_id node_id,COUNT(DISTINCT receiver_user_id) received_count ,timeline.month trans_month,timeline.year trans_year,'month' time_frame,              
         RANK ()
         OVER (PARTITION BY npn.path_node_id
               ORDER BY COUNT(DISTINCT receiver_user_id) desc,el.receiver_node_id)
            RANK
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                  FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
--                  (SELECT node_id,parent_node_id,--07/01/2015
--         level AS hier_level
--    FROM node n
--    START WITH parent_node_id IS NULL
--  CONNECT BY PRIOR node_id = parent_node_id) h,
                  engagement_log el,
                  gtt_months_between_dates timeline
            WHERE TRUNC(date_created) BETWEEN month_start_date and month_end_date
  AND npn.node_id = el.sender_node_id
        GROUP BY receiver_node_id,npn.path_node_id,timeline.month,timeline.year 
         UNION ALL
         SELECT
           el.receiver_node_id,npn.path_node_id node_id,COUNT(DISTINCT receiver_user_id) received_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame,              
         RANK ()
         OVER (PARTITION BY npn.path_node_id
               ORDER BY COUNT(DISTINCT receiver_user_id) desc,el.receiver_node_id)
            RANK
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                  FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
--                  (SELECT node_id,parent_node_id,--07/01/2015
--         level AS hier_level
--    FROM node n
--    START WITH parent_node_id IS NULL
--  CONNECT BY PRIOR node_id = parent_node_id) h,
                  engagement_log el,
                  gtt_months_between_dates timeline
            WHERE TRUNC(date_created) BETWEEN quarter_start_date and month_end_date
  AND npn.node_id = el.sender_node_id
        GROUP BY receiver_node_id,npn.path_node_id,timeline.month,timeline.year     
        UNION ALL
         SELECT
           el.receiver_node_id,npn.path_node_id node_id,COUNT(DISTINCT receiver_user_id) received_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame,              
         RANK ()
         OVER (PARTITION BY npn.path_node_id
               ORDER BY COUNT(DISTINCT receiver_user_id) desc,el.receiver_node_id)
            RANK
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                  FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
--                  (SELECT node_id,parent_node_id,--07/01/2015
--         level AS hier_level
--    FROM node n
--    START WITH parent_node_id IS NULL
--  CONNECT BY PRIOR node_id = parent_node_id) h,
                  engagement_log el,
                  gtt_months_between_dates timeline
            WHERE TRUNC(date_created) BETWEEN year_start_date and month_end_date
  AND npn.node_id = el.sender_node_id
        GROUP BY receiver_node_id,npn.path_node_id,timeline.month,timeline.year        
        )        
          SELECT /*+ ordered use_nl(es)  */  es.s_rowid,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.node_id,
                            s2.trans_month,
                            s2.trans_year,
                            s2.time_frame,
                            s2.receiver_node_id,
                            s2.received_count                            
                       FROM engagement_recog_sent s2, gtt_months_between_dates gtt
                       WHERE s2.trans_month = gtt.month AND s2.trans_year = gtt.year                
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                 FULL OUTER JOIN detail_derived_summary dds
                   ON (       es.node_id    = dds.node_id
                                 AND es.receiver_node_id    = dds.receiver_node_id
                                 AND es.time_frame    = dds.time_frame
                                 AND es.trans_month    = dds.trans_month
                                 AND es.trans_year    = dds.trans_year                                  
                      )
         ) s         
      ON (d.ROWID = s.s_rowid)
        WHEN MATCHED THEN
         UPDATE SET d.received_count = s.received_count,                   
                    d.rank = s.rank
      WHEN NOT MATCHED THEN
      INSERT ( node_id,
               receiver_node_id,
               received_count,
               trans_month,
               trans_year,
               time_frame,
                     rank,
                date_created,
                created_by)
               VALUES
              (s.node_id,
               s.receiver_node_id,
               s.received_count,
               s.trans_month,
               s.trans_year,
               s.time_frame,
               s.rank,
               SYSDATE,
               p_user_id
              );
              
   v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    v_rec_cnt := SQL%ROWCOUNT;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
               
EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
END p_engagement_recog_sent;

PROCEDURE p_engagement_recog_received (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) is

/*******************************************************************************
-- Purpose: To get the counts of recognitions received 
-- MODIFICATION HISTORY
-- Person               Date            Comments
-- ---------            ----------      ------------------------------------------------
--  KrishnaDeepika      07/21/2014      Creation
--Suresh J             10/16/2014      Bug 57277 - Replaced Count(*) with Count(DISTINCT ID) to include unique people only
--Suresh J       12/02/2014 Performance Fix - Added Hint to detail_derived_summary to force NESTED LOOP over MERGE JOIN CARTESIAN
--Ravi Dhanekula 12/11/2014 Re-wrote the merge query to have unique people counts as per the CR listed in bug # 57277.
*******************************************************************************/    
    
    c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_engagement_recog_received'); --10/31/2014  Replaced with the correct procedure name
    v_rec_cnt INTEGER;
BEGIN
    v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
    
  /* MERGE INTO engagement_recog_received d
    USING 
    (  
    WITH rpt_teamsum AS
        (  -- build team summary records         
        SELECT  el.sender_node_id,count(DISTINCT sender_user_id) sent_count ,timeline.month trans_month,timeline.year trans_year,'month' time_frame     --10/16/2014
--        ,el.receiver_node_id  --10/24/2014
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN month_start_date and month_end_date
        GROUP BY sender_node_id,timeline.month,timeline.year
--      ,receiver_node_id --10/24/2014
        UNION ALL
        SELECT  el.sender_node_id,count(DISTINCT sender_user_id) sent_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame     --10/16/2014   
--        ,el.receiver_node_id  --10/24/2014
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN quarter_start_date and month_end_date
        GROUP BY sender_node_id,timeline.month,timeline.year
--        ,receiver_node_id  --10/24/2014
        UNION ALL
        SELECT  el.sender_node_id,count(DISTINCT sender_user_id) sent_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame        --10/16/2014 
--        ,el.receiver_node_id --10/24/2014
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN year_start_date and month_end_date
        GROUP BY sender_node_id,timeline.month,timeline.year
--        ,receiver_node_id    --10/24/2014    
        ) , detail_derived_summary AS  --12/02/2014
        ( -- derive summaries based on team summary data           
           SELECT + ordered use_nl(RPT_TEAMSUM)   -- key fields
               h.node_id,
                rt.sender_node_id,  
                SUM(rt.sent_count) sent_count,
                rt.trans_month,
                rt.trans_year,
                rt.time_frame,
         RANK ()
         OVER (PARTITION BY h.node_id
               ORDER BY SUM(rt.sent_count) desc,rt.sender_node_id)
            RANK
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                  FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
                  (SELECT node_id,parent_node_id,
         level AS hier_level
    FROM node n
    START WITH parent_node_id IS NULL
  CONNECT BY PRIOR node_id = parent_node_id) h,
                  rpt_teamsum rt
            WHERE -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
--                  rt.receiver_node_id = npn.node_id           --10/24/2014 
                  rt.sender_node_id = npn.node_id      --10/24/2014
                 AND npn.path_node_id = h.node_id
            GROUP BY
                    h.node_id,rt.sender_node_id,rt.trans_month,rt.trans_year,rt.time_frame
                    order by h.node_id
        )        
          SELECT es.s_rowid,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.node_id,
                            s2.trans_month,
                            s2.trans_year,
                            s2.time_frame,
                            s2.sender_node_id,
                            s2.sent_count                            
                       FROM engagement_recog_received s2, gtt_months_between_dates gtt
                       WHERE s2.trans_month = gtt.month AND s2.trans_year = gtt.year                            
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                 FULL OUTER JOIN detail_derived_summary dds
                   ON (       es.node_id    = dds.node_id
                                 AND es.sender_node_id    = dds.sender_node_id
                                 AND es.time_frame    = dds.time_frame
                                 AND es.trans_month    = dds.trans_month
                                 AND es.trans_year    = dds.trans_year                                  
                      )
         ) s         
      ON (d.ROWID = s.s_rowid)
        WHEN MATCHED THEN
         UPDATE SET d.sent_count = s.sent_count,                   
                    d.rank = s.rank
      WHEN NOT MATCHED THEN
      INSERT ( node_id,
               sender_node_id,
               sent_count,
               trans_month,
               trans_year,
               time_frame,
               rank,
               date_created,
               created_by)
               VALUES
              (s.node_id,
               s.sender_node_id,
               s.sent_count,
               s.trans_month,
               s.trans_year,
               s.time_frame,
               s.rank,
               SYSDATE,
               p_user_id
              ); */
              

 MERGE INTO engagement_recog_received d --12/11/2014
    USING 
    (    
        WITH detail_derived_summary AS
        (           
           SELECT
           el.sender_node_id,npn.path_node_id node_id,COUNT(DISTINCT sender_user_id) sent_count ,timeline.month trans_month,timeline.year trans_year,'month' time_frame,              
         RANK ()
         OVER (PARTITION BY npn.path_node_id
               ORDER BY COUNT(DISTINCT sender_user_id) desc,el.sender_node_id)
            RANK
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                  FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
--                  (SELECT node_id,parent_node_id,--07/01/2015
--         level AS hier_level
--    FROM node n
--    START WITH parent_node_id IS NULL
--  CONNECT BY PRIOR node_id = parent_node_id) h,
                  engagement_log el,
                  gtt_months_between_dates timeline
            WHERE TRUNC(date_created) BETWEEN month_start_date and month_end_date
  AND npn.node_id = el.receiver_node_id
        GROUP BY sender_node_id,npn.path_node_id,timeline.month,timeline.year 
         UNION ALL
         SELECT
           el.sender_node_id,npn.path_node_id node_id,COUNT(DISTINCT sender_user_id) sent_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame,              
         RANK ()
         OVER (PARTITION BY npn.path_node_id
               ORDER BY COUNT(DISTINCT sender_user_id) desc,el.sender_node_id)
            RANK
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                  FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
--                  (SELECT node_id,parent_node_id,--07/01/2015
--         level AS hier_level
--    FROM node n
--    START WITH parent_node_id IS NULL
--  CONNECT BY PRIOR node_id = parent_node_id) h,
                  engagement_log el,
                  gtt_months_between_dates timeline
            WHERE TRUNC(date_created) BETWEEN quarter_start_date and month_end_date
  AND npn.node_id = el.receiver_node_id
        GROUP BY sender_node_id,npn.path_node_id,timeline.month,timeline.year     
        UNION ALL
         SELECT
           el.sender_node_id,npn.path_node_id node_id,COUNT(DISTINCT sender_user_id) sent_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame,              
         RANK ()
         OVER (PARTITION BY npn.path_node_id
               ORDER BY COUNT(DISTINCT sender_user_id) desc,el.sender_node_id)
            RANK
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                  FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
 --                  (SELECT node_id,parent_node_id,--07/01/2015
--         level AS hier_level
--    FROM node n
--    START WITH parent_node_id IS NULL
--  CONNECT BY PRIOR node_id = parent_node_id) h,
                  engagement_log el,
                  gtt_months_between_dates timeline
            WHERE TRUNC(date_created) BETWEEN year_start_date and month_end_date
  AND npn.node_id = el.receiver_node_id
        GROUP BY sender_node_id,npn.path_node_id,timeline.month,timeline.year
        )        
          SELECT  /*+ ordered use_nl(es)  */  es.s_rowid,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.node_id,
                            s2.trans_month,
                            s2.trans_year,
                            s2.time_frame,
                            s2.sender_node_id,
                            s2.sent_count                            
                       FROM engagement_recog_received s2, gtt_months_between_dates gtt
                       WHERE s2.trans_month = gtt.month AND s2.trans_year = gtt.year                            
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                 FULL OUTER JOIN detail_derived_summary dds
                   ON (       es.node_id    = dds.node_id
                                 AND es.sender_node_id    = dds.sender_node_id
                                 AND es.time_frame    = dds.time_frame
                                 AND es.trans_month    = dds.trans_month
                                 AND es.trans_year    = dds.trans_year                                  
                      )
         ) s         
      ON (d.ROWID = s.s_rowid)
        WHEN MATCHED THEN
         UPDATE SET d.sent_count = s.sent_count,                   
                    d.rank = s.rank
      WHEN NOT MATCHED THEN
      INSERT ( node_id,
               sender_node_id,
               sent_count,
               trans_month,
               trans_year,
               time_frame,
               rank,
               date_created,
               created_by)
               VALUES
              (s.node_id,
               s.sender_node_id,
               s.sent_count,
               s.trans_month,
               s.trans_year,
               s.time_frame,
               s.rank,
               SYSDATE,
               p_user_id
              );
              


v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    v_rec_cnt := SQL%ROWCOUNT;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
    
EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
END p_engagement_recog_received;

PROCEDURE p_eng_recog_sent_by_user(p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS

/*******************************************************************************
-- Purpose: To get the counts of recognitions received 
-- MODIFICATION HISTORY
-- Person               Date            Comments
-- ---------            ----------      ------------------------------------------------
--  Ravi Dhanekula      07/21/2014      Creation
-- Suresh J            12/12/2014     Bug Fix 58618 - Tree view count and header count is not matching.
*******************************************************************************/    
    
    c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_engagement_recog_recived');
    v_rec_cnt INTEGER;
BEGIN
    v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
        
    MERGE INTO engagement_recog_sent_by_user d
    USING 
        (                  
    WITH rpt_teamsum AS
        ( -- build team summary records         
        SELECT  el.sender_node_id,el.receiver_node_id,el.receiver_user_id,count(distinct sender_user_id) received_count ,timeline.month trans_month,timeline.year trans_year,'month' time_frame  --12/12/2014
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN month_start_date and month_end_date
        GROUP BY sender_node_id,receiver_node_id,receiver_user_id,timeline.month,timeline.year 
        UNION ALL
        SELECT  el.sender_node_id,el.receiver_node_id,el.receiver_user_id,count(distinct sender_user_id) received_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame  --12/12/2014
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN quarter_start_date and month_end_date
        GROUP BY sender_node_id,receiver_node_id,receiver_user_id,timeline.month,timeline.year 
        UNION ALL
        SELECT  el.sender_node_id,el.receiver_node_id,el.receiver_user_id,count(distinct sender_user_id) received_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame  --12/12/2014
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN year_start_date and month_end_date
        GROUP BY sender_node_id,receiver_node_id,receiver_user_id,timeline.month,timeline.year    
        ) , detail_derived_summary AS
        ( -- derive summaries based on team summary data           
           SELECT -- key fields
               npn.path_node_id node_id,--07/01/2015
                rt.receiver_node_id,
                rt.receiver_user_id,
                rt.time_frame,
                SUM(rt.received_count) received_count,
                rt.trans_month,
                rt.trans_year
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                  FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
--                  (SELECT node_id,parent_node_id,--07/01/2015
--         level AS hier_level
--    FROM node n
--    START WITH parent_node_id IS NULL
--  CONNECT BY PRIOR node_id = parent_node_id) h,
                  rpt_teamsum rt
--               engagement_recog_received ers
            WHERE -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
                  rt.sender_node_id = npn.node_id 
--                 AND npn.path_node_id = h.node_id  --07/01/2015               
            GROUP BY
                    npn.path_node_id,rt.receiver_node_id,rt.receiver_user_id,rt.time_frame,rt.trans_month,rt.trans_year
--                    order by h.node_id--07/01/2015
        ) 
          SELECT es.s_rowid,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.node_id,
                            s2.time_frame,
                            s2.receiver_node_id,
                            s2.receiver_user_id,
                            s2.trans_month,
                            s2.trans_year,
                            s2.received_count                            
                       FROM engagement_recog_sent_by_user s2, gtt_months_between_dates gtt
                       WHERE s2.trans_month = gtt.month AND s2.trans_year = gtt.year                        
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                 FULL OUTER JOIN detail_derived_summary dds
                   ON (       es.node_id    = dds.node_id
                       AND es.receiver_node_id = dds.receiver_node_id
                       AND es.receiver_user_id = dds.receiver_user_id
                       AND es.time_frame = dds.time_frame
                       AND es.trans_month = dds.trans_month
                       AND es.trans_year = dds.trans_year
                      )
         ) s         
      ON (d.ROWID = s.s_rowid)            
        WHEN MATCHED THEN
         UPDATE SET  d.received_count = s.received_count
      WHEN NOT MATCHED THEN
      INSERT ( node_id,
               receiver_node_id,
               receiver_user_id,
               received_count,
               trans_month,
               trans_year,
               time_frame,
               date_created,
               created_by
               )
               VALUES
              (s.node_id,
               s.receiver_node_id,
               s.receiver_user_id,
               s.received_count,
               s.trans_month,
               s.trans_year,
               s.time_frame,
               SYSDATE,
               p_user_id               
              );
                          
               
    v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    v_rec_cnt := SQL%ROWCOUNT;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
    
EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
END p_eng_recog_sent_by_user;

PROCEDURE p_eng_recog_recvd_by_user(p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS

/*******************************************************************************
-- Purpose: To get the counts of recognitions received 
-- MODIFICATION HISTORY
-- Person               Date            Comments
-- ---------            ----------      ------------------------------------------------
--  Ravi Dhanekula      07/21/2014      Creation
-- Suresh J            12/12/2014     Bug Fix 58618 - Tree view count and header count is not matching.  
*******************************************************************************/    
    
    c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_engagement_recog_recived');
    v_rec_cnt INTEGER;
BEGIN
    v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
    
   MERGE INTO engagement_recog_recvd_by_user d
    USING 
        (                  
    WITH rpt_teamsum AS
        ( -- build team summary records         
        SELECT  el.sender_node_id,el.receiver_node_id,el.sender_user_id,count(distinct receiver_user_id) sent_count ,timeline.month trans_month,timeline.year trans_year,'month' time_frame --12/12/2014
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN month_start_date and month_end_date
        GROUP BY sender_node_id,receiver_node_id,sender_user_id,timeline.month,timeline.year 
        UNION ALL
        SELECT  el.sender_node_id,el.receiver_node_id,el.sender_user_id,count(distinct receiver_user_id) sent_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame  --12/12/2014
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN quarter_start_date and month_end_date
        GROUP BY sender_node_id,receiver_node_id,sender_user_id,timeline.month,timeline.year 
        UNION ALL
        SELECT  el.sender_node_id,el.receiver_node_id,el.sender_user_id,count(distinct receiver_user_id) sent_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame  --12/12/2014
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN year_start_date and month_end_date
        GROUP BY sender_node_id,receiver_node_id,sender_user_id,timeline.month,timeline.year    
        ) , detail_derived_summary AS
        ( -- derive summaries based on team summary data           
           SELECT -- key fields
               npn.path_node_id node_id,--07/01/2015
                rt.sender_node_id,
                rt.sender_user_id,
                rt.time_frame,
                SUM(rt.sent_count) sent_count,
                rt.trans_month,
                rt.trans_year
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                  FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
--                  (SELECT node_id,parent_node_id,--07/01/2015
--         level AS hier_level
--    FROM node n
--    START WITH parent_node_id IS NULL
--  CONNECT BY PRIOR node_id = parent_node_id) h,
                  rpt_teamsum rt
--               engagement_recog_received ers
            WHERE -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
                  rt.receiver_node_id = npn.node_id 
--                 AND npn.path_node_id = h.node_id                 
            GROUP BY
                    npn.path_node_id,rt.sender_node_id,rt.sender_user_id,rt.time_frame,rt.trans_month,rt.trans_year
--                    order by h.node_id--07/01/2015
        ) 
          SELECT es.s_rowid,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.node_id,
                            s2.time_frame,
                            s2.sender_node_id,
                            s2.sender_user_id,
                            s2.trans_month,
                            s2.trans_year,
                            s2.sent_count                            
                       FROM ENGAGEMENT_RECOG_recvd_by_user s2, gtt_months_between_dates gtt
                       WHERE s2.trans_month = gtt.month AND s2.trans_year = gtt.year                        
                   ) es
                   FULL OUTER JOIN detail_derived_summary dds
                   ON (       es.node_id    = dds.node_id
                       AND es.sender_node_id = dds.sender_node_id
                       AND es.sender_user_id = dds.sender_user_id
                       AND es.time_frame = dds.time_frame
                       AND es.trans_month = dds.trans_month
                       AND es.trans_year = dds.trans_year
                      )
         ) s         
      ON (d.ROWID = s.s_rowid)            
        WHEN MATCHED THEN
         UPDATE SET  d.SENT_COUNT = s.SENT_COUNT
      WHEN NOT MATCHED THEN
      INSERT ( node_id,
               sender_node_id,
               sender_user_id,
               sent_count,
               trans_month,
               trans_year,
               time_frame,
               date_created,
               created_by
               )
               VALUES
              (s.node_id,
               s.sender_node_id,
               s.sender_user_id,
               s.sent_count,
               s.trans_month,
               s.trans_year,
               s.time_frame,
               SYSDATE,
               p_user_id               
              );
              
v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    v_rec_cnt := SQL%ROWCOUNT;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
END p_eng_recog_recvd_by_user;

PROCEDURE p_eng_recog_sent_user_mode(p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS

/*******************************************************************************
-- Purpose: To get the counts of recognitions sent by a user(eng_recog_sent_user_mode). This is used for user dashboard
-- MODIFICATION HISTORY
-- Person               Date            Comments
-- ---------            ----------      ------------------------------------------------
--  Ravi Dhanekula      07/21/2014      Creation
*******************************************************************************/    
    
    c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_eng_recog_sent_user_mode');
    v_rec_cnt INTEGER;
BEGIN
    v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
   
    MERGE INTO eng_recog_sent_user_mode d
    USING 
        (                  
    WITH detail_derived_summary AS
        (   
        SELECT  el.sender_user_id user_id,el.sender_node_id node_id,el.receiver_user_id,count(*) received_count ,timeline.month trans_month,timeline.year trans_year,'month' time_frame
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN month_start_date and month_end_date
        GROUP BY sender_user_id,el.sender_node_id,receiver_user_id,timeline.month,timeline.year 
        UNION ALL
        SELECT  el.sender_user_id user_id,el.sender_node_id node_id,el.receiver_user_id,count(*) received_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN quarter_start_date and month_end_date
        GROUP BY sender_user_id,el.sender_node_id,receiver_user_id,timeline.month,timeline.year 
        UNION ALL
        SELECT  el.sender_user_id user_id,el.sender_node_id node_id,el.receiver_user_id,count(*) received_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN year_start_date and month_end_date
        GROUP BY sender_user_id,el.sender_node_id,receiver_user_id,timeline.month,timeline.year    
        ) 
          SELECT es.s_rowid,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.user_id,
                            s2.node_id,
                            s2.time_frame,                          
                            s2.receiver_user_id,
                            s2.trans_month,
                            s2.trans_year,
                            s2.received_count                            
                       FROM ENG_RECOG_SENT_USER_MODE s2, gtt_months_between_dates gtt
                       WHERE s2.trans_month = gtt.month AND s2.trans_year = gtt.year                        
                   ) es                 
                 FULL OUTER JOIN detail_derived_summary dds
                   ON (       es.user_id    = dds.user_id     
                       AND es.node_id = dds.node_id                 
                       AND es.receiver_user_id = dds.receiver_user_id
                       AND es.time_frame = dds.time_frame
                       AND es.trans_month = dds.trans_month
                       AND es.trans_year = dds.trans_year
                      )
         ) s         
      ON (d.ROWID = s.s_rowid)            
        WHEN MATCHED THEN
         UPDATE SET  d.received_count = s.received_count
      WHEN NOT MATCHED THEN
      INSERT ( user_id,               
               node_id,
               receiver_user_id,
               received_count,
               trans_month,
               trans_year,
               time_frame,
               date_created,
               created_by
               )
               VALUES
              (s.user_id,    
               s.node_id,           
               s.receiver_user_id,
               s.received_count,
               s.trans_month,
               s.trans_year,
               s.time_frame,
               SYSDATE,
               p_user_id               
              );
                          
    v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    v_rec_cnt := SQL%ROWCOUNT;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
               
EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
END p_eng_recog_sent_user_mode;

PROCEDURE p_eng_recog_recvd_user_mode(p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS

/*******************************************************************************
-- Purpose: To get the counts of recognitions received by a user(eng_recog_recvd_user_mode). This is used for user dashboard
-- MODIFICATION HISTORY
-- Person               Date            Comments
-- ---------            ----------      ------------------------------------------------
--  Ravi Dhanekula      07/21/2014      Creation
*******************************************************************************/    
    
    c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_eng_recog_recvd_user_mode'); --10/31/2014  Replaced with the correct procedure name
    v_rec_cnt INTEGER;
BEGIN
    v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
   
MERGE INTO eng_recog_recvd_user_mode d
    USING 
        (                  
    WITH detail_derived_summary AS
        ( 
        SELECT  el.receiver_user_id user_id,el.receiver_node_id node_id,el.sender_user_id,count(*) sent_count ,timeline.month trans_month,timeline.year trans_year,'month' time_frame
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN month_start_date and month_end_date
        GROUP BY receiver_user_id,el.receiver_node_id,sender_user_id,timeline.month,timeline.year 
        UNION ALL
        SELECT  el.receiver_user_id user_id,el.receiver_node_id node_id,el.sender_user_id,count(*) sent_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN quarter_start_date and month_end_date
        GROUP BY receiver_user_id,el.receiver_node_id,sender_user_id,timeline.month,timeline.year 
        UNION ALL
        SELECT  el.receiver_user_id user_id,el.receiver_node_id node_id,el.sender_user_id,count(*) sent_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN year_start_date and month_end_date
        GROUP BY receiver_user_id,el.receiver_node_id,sender_user_id,timeline.month,timeline.year    
        )
          SELECT es.s_rowid,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.user_id,
                            s2.node_id,
                            s2.time_frame,                        
                            s2.sender_user_id,
                            s2.trans_month,
                            s2.trans_year,
                            s2.sent_count                            
                       FROM ENG_RECOG_RECVD_USER_MODE s2, gtt_months_between_dates gtt
                       WHERE s2.trans_month = gtt.month AND s2.trans_year = gtt.year                        
                   ) es                 
                 FULL OUTER JOIN detail_derived_summary dds
                   ON (       es.user_id    = dds.user_id    
                       AND es.node_id = dds.node_id                  
                       AND es.sender_user_id = dds.sender_user_id
                       AND es.time_frame = dds.time_frame
                       AND es.trans_month = dds.trans_month
                       AND es.trans_year = dds.trans_year
                      )
         ) s         
      ON (d.ROWID = s.s_rowid)            
        WHEN MATCHED THEN
         UPDATE SET  d.SENT_COUNT = s.SENT_COUNT
      WHEN NOT MATCHED THEN
      INSERT ( user_id,
               node_id,
               sender_user_id,
               sent_count,
               trans_month,
               trans_year,
               time_frame,
               date_created,
               created_by
               )
               VALUES
              (s.user_id,
               s.node_id,
               s.sender_user_id,
               s.sent_count,
               s.trans_month,
               s.trans_year,
               s.time_frame,
               SYSDATE,
               p_user_id               
              );
              
     v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    v_rec_cnt := SQL%ROWCOUNT;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
               
EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
END p_eng_recog_recvd_user_mode;


PROCEDURE p_eng_recog_sent_by_promo (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2)
IS
/*******************************************************************************
-- Purpose: To get the counts of recognitions sent by promo (eng_recog_sent_by_promo) 
-- MODIFICATION HISTORY
-- Person               Date            Comments
-- ---------            ----------      ------------------------------------------------
--  Ravi Dhanekula      07/23/2014      Creation
--Suresh J       12/02/2014 Performance Fix - Added Hint to detail_derived_summary to force NESTED LOOP over MERGE JOIN CARTESIAN
*******************************************************************************/ 
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_eng_recog_sent_by_promo');
v_rec_cnt INTEGER;
BEGIN

MERGE INTO eng_recog_sent_by_promo d
    USING 
    (  
    WITH rpt_teamsum AS
        (
    SELECT  el.sender_node_id,el.promotion_id,count(*) sent_count ,timeline.month trans_month,timeline.year trans_year,'month' time_frame
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN month_start_date and month_end_date
        GROUP BY sender_node_id,promotion_id,timeline.month,timeline.year
        UNION ALL
        SELECT  el.sender_node_id,el.promotion_id,count(*) sent_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN quarter_start_date and month_end_date
        GROUP BY sender_node_id,promotion_id,timeline.month,timeline.year
        UNION ALL
        SELECT  el.sender_node_id,el.promotion_id,count(*) sent_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN year_start_date and month_end_date
        GROUP BY sender_node_id,promotion_id,timeline.month,timeline.year        
        ) , detail_derived_summary AS   --12/02/2014
        ( -- derive summaries based on team summary data           
           SELECT /*+ ordered use_nl(RPT_TEAMSUM)  */  -- key fields
               npn.path_node_id node_id,--07/01/2015
               rt.promotion_id,  
               rt.time_frame,
               SUM(rt.sent_count) sent_count,
               -- date_created,
               trans_month,
               trans_year         
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                  FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
--                  (SELECT node_id,parent_node_id,--07/01/2015
--         level AS hier_level
--    FROM node n
--    START WITH parent_node_id IS NULL
--  CONNECT BY PRIOR node_id = parent_node_id) h,
                  rpt_teamsum rt
            WHERE -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
                  rt.sender_node_id = npn.node_id 
--                 AND npn.path_node_id = h.node_id--07/01/2015
            GROUP BY
                    npn.path_node_id,rt.promotion_id,rt.time_frame,trans_year,trans_month
--                    order by h.node_id--07/01/2015
        )        
          SELECT es.s_rowid,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.node_id,
                            s2.time_frame,
                            s2.promotion_id,
                            s2.trans_year,
                            s2.trans_month,
                            s2.sent_count                            
                       FROM eng_recog_sent_by_promo s2, gtt_months_between_dates gtt
                       WHERE s2.trans_month = gtt.month AND s2.trans_year = gtt.year                        
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                 FULL OUTER JOIN detail_derived_summary dds
                   ON (       es.node_id    = dds.node_id
                       AND es.time_frame = dds.time_frame
                       AND es.promotion_id = dds.promotion_id
                       AND es.trans_year = dds.trans_year
                       AND es.trans_month = dds.trans_month
                       )
         ) s         
      ON (d.ROWID = s.s_rowid)            
        WHEN MATCHED THEN
         UPDATE SET d.sent_count = s.sent_count
          WHEN NOT MATCHED THEN
      INSERT ( node_id,
               promotion_id,
               sent_count,
               trans_month,
               trans_year,               
               time_frame,
               date_created,
               created_by
              )
               VALUES
              (s.node_id,
               s.promotion_id,
               s.sent_count,
               s.trans_month,
               s.trans_year,               
               s.time_frame,
               SYSDATE,
               p_user_id               
              );
  v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    v_rec_cnt := SQL%ROWCOUNT;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
               
EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
END;

PROCEDURE p_eng_recog_recvd_by_promo (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2)
IS
/*******************************************************************************
-- Purpose: To get the counts of recognitions received by promo (eng_recog_recvd_by_promo) 
-- MODIFICATION HISTORY
-- Person               Date            Comments
-- ---------            ----------      ------------------------------------------------
--  Ravi Dhanekula      07/24/2014      Creation
--Suresh J       12/02/2014 Performance Fix - Added Hint to detail_derived_summary to force NESTED LOOP over MERGE JOIN CARTESIAN
*******************************************************************************/    
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_eng_recog_recvd_by_promo');
v_rec_cnt INTEGER;
BEGIN

MERGE INTO eng_recog_recvd_by_promo d
    USING 
    (  
    WITH rpt_teamsum AS
        ( 
    SELECT  el.receiver_node_id,el.promotion_id,count(*) received_count ,timeline.month trans_month,timeline.year trans_year,'month' time_frame
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN month_start_date and month_end_date
        GROUP BY receiver_node_id,promotion_id,timeline.month,timeline.year
        UNION ALL
        SELECT  el.receiver_node_id,el.promotion_id,count(*) received_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN quarter_start_date and month_end_date
        GROUP BY receiver_node_id,promotion_id,timeline.month,timeline.year
        UNION ALL
        SELECT  el.receiver_node_id,el.promotion_id,count(*) received_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN year_start_date and month_end_date
        GROUP BY receiver_node_id,promotion_id,timeline.month,timeline.year        
        ) , detail_derived_summary AS   --12/02/2014
        ( -- derive summaries based on team summary data           
           SELECT /*+ ordered use_nl(RPT_TEAMSUM)  */ -- key fields
               npn.path_node_id node_id,--07/01/2015
               rt.promotion_id,  
               rt.time_frame,
               SUM(rt.received_count) received_count,
               -- date_created,
               trans_month,
               trans_year         
            FROM 
                ( -- associate each node to all its hierarchy nodes
                    SELECT np.node_id,
                           p.column_value AS path_node_id
                    FROM ( -- get node hierarchy path
                            SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                  FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                           ) np,
                           -- parse node path into individual nodes
                           -- pivoting the node path into separate records
                           TABLE( CAST( MULTISET(
                              SELECT TO_NUMBER(
                                        SUBSTR(np.node_path,
                                               INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                               INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                        )
                                     )
                                FROM dual
                             CONNECT BY LEVEL <= np.hier_level 
                           ) AS sys.odcinumberlist ) ) p
                ) npn,
--                  (SELECT node_id,parent_node_id,--07/01/2015
--         level AS hier_level
--    FROM node n
--    START WITH parent_node_id IS NULL
--  CONNECT BY PRIOR node_id = parent_node_id) h,
                  rpt_teamsum rt
            WHERE -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
                  rt.receiver_node_id = npn.node_id 
--                 AND npn.path_node_id = h.node_id--07/01/2015
            GROUP BY
                    npn.path_node_id,rt.promotion_id,rt.time_frame,trans_year,trans_month
--                    order by h.node_id--07/01/2015
        )        
          SELECT es.s_rowid,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.node_id,
                            s2.time_frame,
                            s2.promotion_id,
                            s2.trans_year,
                            s2.trans_month,
                            s2.received_count                            
                       FROM eng_recog_recvd_by_promo s2, gtt_months_between_dates gtt
                       WHERE s2.trans_month = gtt.month AND s2.trans_year = gtt.year                        
                   ) es                  
                 FULL OUTER JOIN detail_derived_summary dds
                   ON (       es.node_id    = dds.node_id
                       AND es.time_frame = dds.time_frame
                       AND es.promotion_id = dds.promotion_id
                       AND es.trans_year = dds.trans_year
                       AND es.trans_month = dds.trans_month
                       )
         ) s         
      ON (d.ROWID = s.s_rowid)            
        WHEN MATCHED THEN
         UPDATE SET d.received_count = s.received_count
          WHEN NOT MATCHED THEN
      INSERT ( node_id,
               promotion_id,
               received_count,
               trans_month,
               trans_year,               
               time_frame,
               date_created,
               created_by
              )
               VALUES
              (s.node_id,
               s.promotion_id,
               s.received_count,
               s.trans_month,
               s.trans_year,               
               s.time_frame,
               SYSDATE,
               p_user_id               
              );
  v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    v_rec_cnt := SQL%ROWCOUNT;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
               
EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
END;

PROCEDURE p_eng_recog_sent_bypromo_user (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2)
IS
/*******************************************************************************
-- Purpose: To get the counts of recognitions sent by promo AND by user. (eng_recog_sent_by_promo_user) 
-- MODIFICATION HISTORY
-- Person               Date            Comments
-- ---------            ----------      ------------------------------------------------
--  Ravi Dhanekula      07/24/2014      Creation
*******************************************************************************/    
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_eng_recog_sent_bypromo_user');  --10/31/2014  Replaced with the correct procedure name
v_rec_cnt INTEGER;
BEGIN

MERGE INTO eng_recog_sent_by_promo_user d
    USING 
        (
    SELECT  el.sender_user_id,sender_node_id,el.promotion_id,count(*) sent_count ,timeline.month trans_month,timeline.year trans_year,'month' time_frame
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN month_start_date and month_end_date
        GROUP BY sender_user_id,sender_node_id,sender_node_id,promotion_id,timeline.month,timeline.year
        UNION ALL
        SELECT  el.sender_user_id,sender_node_id,el.promotion_id,count(*) sent_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN quarter_start_date and month_end_date
        GROUP BY sender_user_id,sender_node_id,promotion_id,timeline.month,timeline.year
        UNION ALL
        SELECT  el.sender_user_id,sender_node_id,el.promotion_id,count(*) sent_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN year_start_date and month_end_date
        GROUP BY sender_user_id,sender_node_id,promotion_id,timeline.month,timeline.year        
        ) s
          ON (d.user_id = s.sender_user_id AND d.node_id = s.sender_node_id AND d.promotion_id = s. promotion_id AND d.trans_month = s.trans_month AND d.trans_year = s.trans_year AND d.time_frame = s.time_frame)
        WHEN MATCHED THEN UPDATE 
         SET d.sent_count = s.sent_count
         WHERE NOT (   d.sent_count = s.sent_count)
          WHEN NOT MATCHED THEN
      INSERT ( user_id,
               node_id,
               promotion_id,
               sent_count,
               trans_month,
               trans_year,               
               time_frame,
               date_created,
               created_by
              )
               VALUES
              (s.sender_user_id,
               s.sender_node_id,
               s.promotion_id,
               s.sent_count,
               s.trans_month,
               s.trans_year,               
               s.time_frame,
               SYSDATE,
               p_user_id               
              );
  v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    v_rec_cnt := SQL%ROWCOUNT;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
               
EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
END;

PROCEDURE p_eng_recog_recvd_bypromo_user (p_user_id        IN  NUMBER,
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2)
IS
/*******************************************************************************
-- Purpose: To get the counts of recognitions received by promo AND by user. (eng_recog_recvd_by_promo_user) 
-- MODIFICATION HISTORY
-- Person               Date            Comments
-- ---------            ----------      ------------------------------------------------
--  Ravi Dhanekula      07/24/2014      Creation
*******************************************************************************/    
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_eng_recog_recvd_bypromo_user');  --10/31/2014  Replaced with the correct procedure name
v_rec_cnt INTEGER;
BEGIN


MERGE INTO eng_recog_recvd_by_promo_user d
    USING 
        (
    SELECT  el.receiver_user_id,receiver_node_id,el.promotion_id,count(*) received_count ,timeline.month trans_month,timeline.year trans_year,'month' time_frame
         FROM engagement_log el,
                   gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN month_start_date and month_end_date
        GROUP BY receiver_user_id,receiver_node_id,promotion_id,timeline.month,timeline.year
        UNION ALL
        SELECT  el.receiver_user_id,receiver_node_id,el.promotion_id,count(*) received_count ,timeline.month trans_month,timeline.year trans_year,'quarter' time_frame
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN quarter_start_date and month_end_date
        GROUP BY receiver_user_id,receiver_node_id,promotion_id,timeline.month,timeline.year
        UNION ALL
        SELECT  el.receiver_user_id,receiver_node_id,el.promotion_id,count(*) received_count ,timeline.month trans_month,timeline.year trans_year,'year' time_frame
         FROM engagement_log el,
                    gtt_months_between_dates timeline 
  WHERE TRUNC(date_created) BETWEEN year_start_date and month_end_date
        GROUP BY receiver_user_id,receiver_node_id,promotion_id,timeline.month,timeline.year        
        ) s
          ON (d.user_id = s.receiver_user_id AND d.node_id = s.receiver_node_id AND d.promotion_id = s. promotion_id AND d.trans_month = s.trans_month AND d.trans_year = s.trans_year AND d.time_frame = s.time_frame)
        WHEN MATCHED THEN UPDATE 
         SET d.received_count = s.received_count
         WHERE NOT (   d.received_count = s.received_count)
          WHEN NOT MATCHED THEN
      INSERT ( user_id,
               node_id,
               promotion_id,
               received_count,
               trans_month,
               trans_year,               
               time_frame,
               date_created,
               created_by
              )
               VALUES
              (s.receiver_user_id,
               s.receiver_node_id,
               s.promotion_id,
               s.received_count,
               s.trans_month,
               s.trans_year,               
               s.time_frame,
               SYSDATE,
               p_user_id               
              );
  v_stage := 'END of process: '||c_process_name;
    p_return_code :=0; 
    v_rec_cnt := SQL%ROWCOUNT;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
               
EXCEPTION
  WHEN OTHERS
  THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',SQLERRM,NULL);    
    p_return_code :=99;
    p_error_message := SQLERRM;
END;

PROCEDURE p_eng_user_connected_to_from(p_in_promotion_id IN NUMBER,
   p_in_start_date     IN  DATE, 
   p_in_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2) IS
/*******************************************************************************
-- Purpose: To Populate eng_user_connected_to_from
            To get distinct count of no of people to whom the user is connected to and from 
            in case the user is part of more than one node.
-- MODIFICATION HISTORY
-- Person              Date              Comments
-- ---------           ----------       ------------------------------------------------
--  Swati              10/17/2014       Creation
                                        Created to fix Bug 57351 - RPM-Barbara Reagan My dashboard --> I recognized show extra one count than that of Activity History
--  Suresh J           11/28/2014       Replaced quarter_start_date with year_start_date in Yearly Data Section       
--  Swati               03/17/2015       Added Distinct Keyword to avoid duplicates in eng_user_connected_to_from
	Suresh J	        04/01/2019       SA Integeration with DayMaker. Excludes/Includes PURL/Celebration promotions based on boolean value of new.service.anniversary.enabled                                  
*******************************************************************************/    
    
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_eng_user_connected_to_from');    
v_rec_cnt INTEGER;
v_sa_enabled         os_propertyset.boolean_val%type;   --04/01/2019

BEGIN
    v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

    BEGIN
      SELECT boolean_val                            --04/01/2019
        INTO v_sa_enabled
        FROM os_propertyset
       WHERE entity_name = 'new.service.anniversary.enabled';
     EXCEPTION
         WHEN OTHERS THEN
         v_sa_enabled := 0;
     END;

    MERGE INTO eng_user_connected_to_from d
    USING
    (SELECT b.user_id,e.trans_month,e.trans_year,'quarter' time_frame FROM 
        (        
        SELECT  distinct user_id  --03/17/2015     
        FROM    engagement_elig_user WHERE status = 1
        ) b,
        (SELECT month trans_month,
        year trans_year,quarter_start_date
        FROM gtt_months_between_dates) e    
        WHERE e.quarter_start_date IS NOT NULL
        UNION ALL
        SELECT b.user_id,e.trans_month,e.trans_year,'year' time_frame FROM 
        (        
        SELECT distinct user_id   --03/17/2015      
        FROM    engagement_elig_user WHERE status = 1
        ) b,
        (SELECT month trans_month,
        year trans_year,year_start_date
        FROM gtt_months_between_dates) e    
        WHERE e.year_start_date IS NOT NULL
        UNION ALL
        SELECT b.user_id,e.trans_month,e.trans_year,'month' time_frame FROM 
        (        
        SELECT  distinct user_id    --03/17/2015     
        FROM    engagement_elig_user WHERE status = 1
        ) b,
        (SELECT month trans_month,
        year trans_year,quarter_start_date
        FROM gtt_months_between_dates) e                                                                       
    )a
    ON (a.user_id = d.user_id AND a.trans_month = d.trans_month 
    AND a.trans_year = d.trans_year AND a.time_frame = d.time_frame)
    WHEN NOT MATCHED THEN
    INSERT(user_id,connected_to,connected_from,trans_month,trans_year,time_frame)
    VALUES (a.user_id,0,0,a.trans_month,a.trans_year,a.time_frame); 

    v_stage := 'Initial Data Inserts Complete:';
    v_rec_cnt := SQL%ROWCOUNT;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

    --connected_from for monthly data
    v_stage :='connected_from for monthly data';

    MERGE INTO eng_user_connected_to_from A
    USING
    (
        SELECT cr.participant_id ,
            timeline.month trans_month,timeline.year trans_year,            
            count(DISTINCT c.submitter_id)  connected_from,
            'month' time_frame
        FROM  claim c,
            claim_item ci,
            claim_recipient cr,
            gtt_months_between_dates timeline,
            (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep    --04/01/2019               
        WHERE  c.claim_id = ci.claim_id
            AND ci.claim_item_id = cr.claim_item_id 
            AND c.promotion_id = pep.eligible_promotion_id
            AND pep.promotion_id = p_in_promotion_id   
            AND c.is_open = 0 
            AND ci.approval_status_type = 'approv'            
            AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = cr.participant_id)
            AND trunc(ci.date_created) BETWEEN trunc(month_start_date) AND trunc(NVL(month_end_date,SYSDATE))              
        group by cr.participant_id,
            timeline.month,timeline.year 
    )B
    ON  (a.user_id = b.participant_id 
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = b.time_frame)
    WHEN MATCHED THEN
        UPDATE SET a.connected_from = b.connected_from
    WHEN NOT MATCHED THEN
        INSERT(user_id,connected_to,connected_from,trans_month,trans_year,time_frame)
        VALUES (b.participant_id,0,b.connected_from,b.trans_month,b.trans_year,b.time_frame); 
            
    --connected_to for monthly data.
    v_stage :='Connected_to for monthly data';

    MERGE INTO eng_user_connected_to_from A
    USING    
        (
        SELECT c.submitter_id,
            timeline.month trans_month,timeline.year trans_year,
            COUNT(DISTINCT cr.participant_id) connected_to,
            'month' time_frame
        FROM claim c,
            claim_item ci,
            claim_recipient cr ,
            gtt_months_between_dates timeline,
            (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep    --04/01/2019   
        WHERE c.claim_id = ci.claim_id
            AND ci.claim_item_id = cr.claim_item_id
            AND c.promotion_id = pep.eligible_promotion_id
            AND pep.promotion_id = p_in_promotion_id   
            AND c.is_open = 0 
            AND ci.approval_status_type = 'approv'            
            AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = c.submitter_id)
            AND trunc(c.submission_date) BETWEEN trunc(month_start_date) AND trunc(NVL(month_end_date,SYSDATE))     
        GROUP BY c.submitter_id,
            timeline.month,timeline.year
    )B
    ON (a.user_id = b.submitter_id 
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = b.time_frame)
    WHEN MATCHED THEN
        UPDATE SET a.connected_to = b.connected_to
    WHEN NOT MATCHED THEN
        INSERT(user_id,connected_to,connected_from,trans_month,trans_year,time_frame)
        VALUES (b.submitter_id,b.connected_to,0,b.trans_month,b.trans_year,b.time_frame);

    --connected_from for Quarterly data
    v_stage :='connected_from for Quarterly data';

    MERGE INTO eng_user_connected_to_from A
    USING
    (
        SELECT cr.participant_id ,
            timeline.month trans_month,timeline.year trans_year,            
            count(DISTINCT c.submitter_id)  connected_from,
            'quarter' time_frame
        FROM  claim c,
            claim_item ci,
            claim_recipient cr,
            gtt_months_between_dates timeline,
            (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep    --04/01/2019               
        WHERE  c.claim_id = ci.claim_id
            AND ci.claim_item_id = cr.claim_item_id
            AND c.promotion_id = pep.eligible_promotion_id
            AND pep.promotion_id = p_in_promotion_id               
            AND c.is_open = 0 
            AND ci.approval_status_type = 'approv'            
            AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = cr.participant_id)
            AND trunc(ci.date_created) BETWEEN quarter_start_date and month_end_date              
        group by cr.participant_id,
            timeline.month,timeline.year 
    )B
    ON  (a.user_id = b.participant_id 
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = b.time_frame)
    WHEN MATCHED THEN
        UPDATE SET a.connected_from = b.connected_from
    WHEN NOT MATCHED THEN
        INSERT(user_id,connected_to,connected_from,trans_month,trans_year,time_frame)
        VALUES (b.participant_id,0,b.connected_from,b.trans_month,b.trans_year,b.time_frame); 
            
    --connected_to for Quarterly data.
    v_stage :='Connected_to for Quarterly data';

    MERGE INTO eng_user_connected_to_from A
    USING    
        (
        SELECT c.submitter_id,
            timeline.month trans_month,timeline.year trans_year,
            COUNT(DISTINCT cr.participant_id) connected_to,
            'quarter' time_frame
        FROM claim c,
            claim_item ci,
            claim_recipient cr ,
            gtt_months_between_dates timeline,
            (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep    --04/01/2019   
        WHERE c.claim_id = ci.claim_id
            AND ci.claim_item_id = cr.claim_item_id
            AND c.promotion_id = pep.eligible_promotion_id
            AND pep.promotion_id = p_in_promotion_id   
            AND c.is_open = 0        
            AND ci.approval_status_type = 'approv'            
            AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = c.submitter_id)
            AND trunc(c.submission_date) BETWEEN quarter_start_date and month_end_date
        GROUP BY c.submitter_id,
            timeline.month,timeline.year
    )B
    ON (a.user_id = b.submitter_id 
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = b.time_frame)
    WHEN MATCHED THEN
        UPDATE SET a.connected_to = b.connected_to
    WHEN NOT MATCHED THEN
        INSERT(user_id,connected_to,connected_from,trans_month,trans_year,time_frame)
        VALUES (b.submitter_id,b.connected_to,0,b.trans_month,b.trans_year,b.time_frame);

    --connected_from for Yearly data
    v_stage :='connected_from for Yearly data';

    MERGE INTO eng_user_connected_to_from A
    USING
    (
        SELECT cr.participant_id ,
            timeline.month trans_month,timeline.year trans_year,            
            count(DISTINCT c.submitter_id)  connected_from,
            'year' time_frame
        FROM  claim c,
            claim_item ci,
            claim_recipient cr,
            gtt_months_between_dates timeline,
            (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep    --04/01/2019               
        WHERE  c.claim_id = ci.claim_id
            AND ci.claim_item_id = cr.claim_item_id 
            AND c.promotion_id = pep.eligible_promotion_id
            AND pep.promotion_id = p_in_promotion_id   
            AND c.is_open = 0 
            AND ci.approval_status_type = 'approv'            
            AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = cr.participant_id)
            AND trunc(ci.date_created) BETWEEN year_start_date and month_end_date
        group by cr.participant_id,
            timeline.month,timeline.year 
    )B
    ON  (a.user_id = b.participant_id 
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = b.time_frame)
    WHEN MATCHED THEN
        UPDATE SET a.connected_from = b.connected_from
    WHEN NOT MATCHED THEN
        INSERT(user_id,connected_to,connected_from,trans_month,trans_year,time_frame)
        VALUES (b.participant_id,0,b.connected_from,b.trans_month,b.trans_year,b.time_frame); 
            
    --connected_to for Yearly data.
    v_stage :='Connected_to for Yearly data';

    MERGE INTO eng_user_connected_to_from A
    USING    
        (
        SELECT c.submitter_id,
            timeline.month trans_month,timeline.year trans_year,
            COUNT(DISTINCT cr.participant_id) connected_to,
            'year' time_frame
        FROM claim c,
            claim_item ci,
            claim_recipient cr ,
            gtt_months_between_dates timeline,
            (SELECT * FROM promo_engagement_promotions p WHERE ((v_sa_enabled = 1 AND NOT EXISTS (SELECT r.promotion_id FROM promo_recognition r WHERE r.promotion_id = p.eligible_promotion_id  AND (r.is_include_purl = 1 OR r.include_celebrations = 1))) OR v_sa_enabled=0)) pep    --04/01/2019   
        WHERE c.claim_id = ci.claim_id
            AND ci.claim_item_id = cr.claim_item_id
            AND c.promotion_id = pep.eligible_promotion_id
            AND pep.promotion_id = p_in_promotion_id   
            AND c.is_open = 0           
            AND ci.approval_status_type = 'approv'            
            AND EXISTS (SELECT * FROM engagement_elig_user WHERE user_id = c.submitter_id)
--            AND trunc(c.submission_date) BETWEEN quarter_start_date and month_end_date  --11/28/2014
            AND trunc(c.submission_date) BETWEEN year_start_date and month_end_date     --11/28/2014
        GROUP BY c.submitter_id,
            timeline.month,timeline.year
    )B
    ON (a.user_id = b.submitter_id 
        AND a.trans_month = b.trans_month AND a.trans_year = b.trans_year AND a.time_frame = b.time_frame)
    WHEN MATCHED THEN
        UPDATE SET a.connected_to = b.connected_to
    WHEN NOT MATCHED THEN
        INSERT(user_id,connected_to,connected_from,trans_month,trans_year,time_frame)
        VALUES (b.submitter_id,b.connected_to,0,b.trans_month,b.trans_year,b.time_frame);

    v_stage := 'END of process: '||c_process_name;
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
EXCEPTION
    WHEN OTHERS
    THEN         
    prc_execution_log_entry (c_process_name,c_release_level,'ERROR',v_stage|| SQLERRM,NULL);
    p_return_code :=99;
    p_error_message := SQLERRM;
        
END p_eng_user_connected_to_from;

END pkg_engagement_extract;
/
