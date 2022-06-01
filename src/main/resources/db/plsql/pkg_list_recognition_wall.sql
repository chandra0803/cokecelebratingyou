CREATE OR REPLACE PACKAGE pkg_list_recognition_wall IS

/******************************************************************************
  NAME:       pkg_list_recognition_wall
  PURPOSE:    List out the data for the public recognition wall.

  Date                   Author           Description
  ----------           ---------------  ------------------------------------------------
  05/01/2014 Ravi Dhanekula    Initial code.              
******************************************************************************/

  FUNCTION fnc_check_public_recog_elig
 (p_user_id IN NUMBER,
 p_promotion_id IN NUMBER)
  RETURN VARCHAR2;
  
  FUNCTION fnc_get_recog_recvr_cnt
 (p_promotion_type VARCHAR2,
  p_team_id IN NUMBER)
  RETURN NUMBER;
  
 FUNCTION fnc_get_budget_conv_ratio
 (p_team_id IN NUMBER,
  p_user_id  IN NUMBER)
  RETURN FLOAT;

  PROCEDURE prc_list_recognition_wall
   ( p_in_userid  IN NUMBER,
     p_in_Listtype     IN VARCHAR2,
     p_in_listvalue    IN VARCHAR2,
     p_rownum_start IN NUMBER,
     p_rownum_end IN NUMBER,   
     p_out_return_code  OUT NUMBER,
     p_out_result_set   OUT sys_refcursor,
     p_out_result_set_recipients   OUT sys_refcursor,
     p_out_result_set_pub_likes   OUT sys_refcursor,
     p_out_result_set_pub_comments  OUT SYS_REFCURSOR,
     p_out_result_set_badge OUT SYS_REFCURSOR);
  
  PROCEDURE prc_list_recognition_wall_sa
   ( p_in_userid                    IN  NUMBER,
     p_in_listtype                  IN  VARCHAR2,
     p_in_listvalue                 IN  VARCHAR2,
     p_rownum_start                 IN  NUMBER,
     p_rownum_end                   IN  NUMBER,
     p_out_return_code              OUT NUMBER,
     p_out_result_set               OUT SYS_REFCURSOR,
     p_out_result_set_recipients    OUT SYS_REFCURSOR,          
     p_out_result_set_pub_likes     OUT SYS_REFCURSOR,
     p_out_result_set_pub_comments  OUT SYS_REFCURSOR,
     p_out_result_set_badge         OUT SYS_REFCURSOR);    
 
END pkg_list_recognition_wall;

/
CREATE OR REPLACE PACKAGE BODY      pkg_list_recognition_wall
IS
/******************************************************************************
  NAME:       pkg_list_recognition_wall
  PURPOSE:    List out the data for the public recognition wall.

  Date       Author           Description
  ---------- ---------------  ------------------------------------------------
  05/01/2014 Ravi Dhanekula   Initial code.
  10/18/2016 Ravi Dhanekula   Bug # 69574 --Changes done to all functions and procedures to include add points option for nominations with 5.6.3 release.
******************************************************************************/

   -- package constants
   gc_pkg_name                      CONSTANT VARCHAR2(30) := UPPER('pkg_list_recognition_wall');

   gc_return_code_success           CONSTANT NUMBER := pkg_const.gc_return_code_success;
   gc_return_code_failure           CONSTANT NUMBER := pkg_const.gc_return_code_failure;

   gc_error                         CONSTANT execution_log.severity%TYPE := pkg_const.gc_error;
   gc_info                          CONSTANT execution_log.severity%TYPE := pkg_const.gc_info;

   gc_approval_status_approved      CONSTANT claim_item_approver.approval_status_type%TYPE := pkg_const.gc_approval_status_approved;
   gc_approval_status_winner        CONSTANT claim_item_approver.approval_status_type%TYPE := pkg_const.gc_approval_status_winner;

   gc_cms_ac_promotion_badge        CONSTANT cms_asset.code%TYPE := pkg_const.gc_cms_ac_promotion_badge;
   gc_promotion_status_active       CONSTANT promotion.promotion_status%TYPE := pkg_const.gc_promotion_status_active;

   gc_promotion_type_nomination     CONSTANT promotion.promotion_type%TYPE := pkg_const.gc_promotion_type_nomination;
   gc_promotion_type_recognition    CONSTANT promotion.promotion_type%TYPE := pkg_const.gc_promotion_type_recognition;

   gc_ref_text_hierarchy_rollup     CONSTANT gtt_id_list.ref_text_1%TYPE := pkg_const.gc_ref_text_hierarchy_rollup;
   gc_ref_text_node_hier_level      CONSTANT gtt_id_list.ref_text_1%TYPE := pkg_const.gc_ref_text_node_hier_level;

   gc_ref_text_claim_id             CONSTANT gtt_id_list.ref_text_1%TYPE := 'claim_id';
   gc_ref_text_team_id              CONSTANT gtt_id_list.ref_text_1%TYPE := 'team_id';
   gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';
   gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';

  FUNCTION fnc_check_public_recog_elig (p_user_id        IN NUMBER,
                                         p_promotion_id   IN NUMBER)
      RETURN VARCHAR2
   IS
      /******************************************************
        Date                   Author           Description
        ----------           ---------------  ------------------------------------------------
        05/01/2014 Ravi Dhanekula    Initial code.
        11/11/2014 KrishnaDeepika    Bug # 57743 function creating too many entries in execution log table 
       11/12/2014  Suresh J         Resolved the issue with the above fix 57743
       12/04/2014  Ravi Dhanekula   Resolved a typo issue.
       06/24/2015  Ravi Dhanekula   Bug # 63064 Changes done to pick the current segment.
       09/28/2015  Ravi Dhanekula   Bug # 64114 Budget is not deducting properly for international from the Public recognition and delayed recognition    
       02/10/2017  Ravi Dhanekula Fixed the issue causing the function to fail for central budgets.
      ******************************************************/
      v_public_recog_budget_masterid   NUMBER;
      v_return                         VARCHAR2 (4000)
                                          := 'N' || ','|| ' ' || ',' || ' ' || ', '||' ';
      v_budget_type                    VARCHAR2 (10);
   BEGIN
   SELECT public_rec_budget_master_id INTO v_public_recog_budget_masterid FROM (
    SELECT public_rec_budget_master_id
        FROM promo_recognition pr
       WHERE promotion_id = p_promotion_id
    UNION ALL--Bug # 69574
   SELECT public_rec_budget_master_id
        FROM promo_nomination pr
       WHERE promotion_id = p_promotion_id);


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
            SELECT value INTO v_return FROM (SELECT    'Y'
                   || ','
                   || b.budget_id
                   || ','
                   || b.current_value
                   || ','
                   || bm.cm_asset_code value
--              INTO v_return
              FROM budget_master bm,
                   promo_recognition pr,
                   budget_segment bms,
                   budget b
             WHERE     pr.promotion_id = p_promotion_id
                   AND pr.public_rec_budget_master_id = bm.budget_master_id
                   AND bm.budget_master_id = bms.budget_master_id
                   AND bms.budget_segment_id = b.budget_segment_id
                   AND b.user_id = p_user_id
                   AND TRUNC (SYSDATE) BETWEEN TRUNC ( bms.start_date) AND TRUNC (NVL(bms.end_date,SYSDATE))--06/24/2015
                   AND NVL (public_rec_award_amount_fixed,
                            public_rec_award_amount_min) <= b.current_value
                  UNION ALL--Bug # 69574
                  SELECT    'Y'
                   || ','
                   || b.budget_id
                   || ','
                   || b.current_value
                   || ','
                   || bm.cm_asset_code value
--              INTO v_return
              FROM budget_master bm,
                   promo_nomination pr,
                   budget_segment bms,
                   budget b
             WHERE     pr.promotion_id = p_promotion_id
                   AND pr.public_rec_budget_master_id = bm.budget_master_id
                   AND bm.budget_master_id = bms.budget_master_id
                   AND bms.budget_segment_id = b.budget_segment_id
                   AND b.user_id = p_user_id
                   AND TRUNC (SYSDATE) BETWEEN TRUNC ( bms.start_date) AND TRUNC (NVL(bms.end_date,SYSDATE))--06/24/2015
                   AND NVL (public_rec_award_amount_fixed,
                            public_rec_award_amount_min) <= b.current_value);--09/28/2015
         ELSIF v_budget_type = 'node'
         THEN
         SELECT value INTO v_return FROM (
            SELECT    'Y'
                   || ','
                   || b.budget_id
                   || ','
                   || b.current_value
                   || ','
                   || bm.cm_asset_code value
--              INTO v_return
              FROM budget_master bm,
                   promo_recognition pr,
                   budget_segment bms,
                   budget b
             WHERE     pr.promotion_id = p_promotion_id
                   AND pr.public_rec_budget_master_id = bm.budget_master_id
                   AND bm.budget_master_id = bms.budget_master_id
                   AND bms.budget_segment_id = b.budget_segment_id
                   AND TRUNC (SYSDATE) BETWEEN TRUNC ( bms.start_date) AND TRUNC (NVL(bms.end_date,SYSDATE))--06/24/2015
                   AND EXISTS
                          (SELECT *
                             FROM user_node
                            WHERE     user_id = p_user_id
                                  AND node_id = b.node_id
                                  AND role IN ('own', 'mgr'))
                   AND NVL (public_rec_award_amount_fixed,
                            public_rec_award_amount_min) <= b.current_value
                            UNION ALL--Bug # 69574
                            SELECT    'Y'
                   || ','
                   || b.budget_id
                   || ','
                   || b.current_value
                   || ','
                   || bm.cm_asset_code value
--              INTO v_return
              FROM budget_master bm,
                   promo_nomination pr,
                   budget_segment bms,
                   budget b
             WHERE     pr.promotion_id = p_promotion_id
                   AND pr.public_rec_budget_master_id = bm.budget_master_id
                   AND bm.budget_master_id = bms.budget_master_id
                   AND bms.budget_segment_id = b.budget_segment_id
                   AND TRUNC (SYSDATE) BETWEEN TRUNC ( bms.start_date) AND TRUNC (NVL(bms.end_date,SYSDATE))--06/24/2015
                   AND EXISTS
                          (SELECT *
                             FROM user_node
                            WHERE     user_id = p_user_id
                                  AND node_id = b.node_id
                                  AND role IN ('own', 'mgr'))
                   AND NVL (public_rec_award_amount_fixed,
                            public_rec_award_amount_min) <= b.current_value); --09/28/2015
         ELSIF v_budget_type = 'central'
         THEN
         SELECT value INTO v_return FROM (
            SELECT    'Y'
                   || ','
                   || b.budget_id
                   || ','
                   || b.current_value
                   || ','
                   || bm.cm_asset_code value
--              INTO v_return
              FROM budget_master bm,
                   promo_recognition pr,
                   budget_segment bms,
                   budget b
             WHERE     pr.promotion_id = p_promotion_id
                   AND pr.public_rec_budget_master_id = bm.budget_master_id
                   AND bm.budget_master_id = bms.budget_master_id
                   AND bms.budget_segment_id = b.budget_segment_id
                   AND TRUNC (SYSDATE) BETWEEN TRUNC ( bms.start_date) AND TRUNC (NVL(bms.end_date,SYSDATE))--06/24/2015
                   AND EXISTS
                          (SELECT *
                             FROM user_node
                            WHERE user_id = p_user_id AND role IN ('own', 'mgr'))
                   AND NVL (public_rec_award_amount_fixed,
                            public_rec_award_amount_min) <= b.current_value
                            UNION ALL--Bug # 69574
                            SELECT    'Y'
                   || ','
                   || b.budget_id
                   || ','
                   || b.current_value
                   || ','
                   || bm.cm_asset_code value
--              INTO v_return
              FROM budget_master bm,
                   promo_nomination pr,--02/10/2017
                   budget_segment bms,
                   budget b
             WHERE     pr.promotion_id = p_promotion_id
                   AND pr.public_rec_budget_master_id = bm.budget_master_id
                   AND bm.budget_master_id = bms.budget_master_id
                   AND bms.budget_segment_id = b.budget_segment_id
                   AND TRUNC (SYSDATE) BETWEEN TRUNC ( bms.start_date) AND TRUNC (NVL(bms.end_date,SYSDATE))--06/24/2015
                   AND EXISTS
                          (SELECT *
                             FROM user_node
                            WHERE user_id = p_user_id AND role IN ('own', 'mgr'))
                   AND NVL (public_rec_award_amount_fixed,
                            public_rec_award_amount_min) <= b.current_value); --09/28/2015
         END IF;
      END IF;

      RETURN v_return;
   EXCEPTION
    WHEN NO_DATA_FOUND THEN  
--    v_return := NULL; --11/11/2014 bug fix 57743
      v_return := 'N' || ','|| ' ' || ',' || ' ' || ', '||' ';   --11/12/2014
      RETURN v_return;  --11/12/2014
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
 (p_team_id IN NUMBER,p_user_id IN NUMBER)--09/28/2015
      RETURN FLOAT
   IS
      /******************************************************
        Date                   Author           Description
        ----------           ---------------  ------------------------------------------------
        05/15/2014 Ravi Dhanekula    Initial code.
      ******************************************************/
      v_return FLOAT;
   BEGIN
   
   SELECT value INTO v_return FROM (
   SELECT NVL(c2.budget_media_value,1)/NVL(c1.budget_media_value,1) value FROM --activity a1,
                            activity a2,
                            recognition_claim rc,
                            user_address ua1,
                            user_address ua2,
                            country c1,
                            country c2 WHERE 
                            rc.team_id = p_team_id
--                            AND rc.claim_id = a1.claim_id
--                            AND a1.is_submitter =1
                            AND ua1.user_id = p_user_id--09/28/2015
                            AND ua1.is_primary = 1
                            AND ua1.country_id = c1.country_id
                            AND rc.claim_id = a2.claim_id
                            AND a2.is_submitter =0
                            AND a2.user_id = ua2.user_id
                            AND ua2.is_primary = 1
                            AND ua2.country_id = c2.country_id
                            UNION ALL--Bug # 69574
   SELECT NVL(c2.budget_media_value,1)/NVL(c1.budget_media_value,1) value FROM --activity a1,
                            activity a2,
                            nomination_claim rc,
                            user_address ua1,
                            user_address ua2,
                            country c1,
                            country c2 WHERE 
                            rc.team_id = p_team_id
--                            AND rc.claim_id = a1.claim_id
--                            AND a1.is_submitter =1
                            AND ua1.user_id = p_user_id--09/28/2015
                            AND ua1.is_primary = 1
                            AND ua1.country_id = c1.country_id
                            AND rc.claim_id = a2.claim_id
                            AND a2.is_submitter =0
                            AND a2.user_id = ua2.user_id
                            AND ua2.is_primary = 1
                            AND ua2.country_id = c2.country_id);

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
     FUNCTION fnc_get_recog_recvr_cnt
 (p_promotion_type VARCHAR2,p_team_id NUMBER)
  RETURN NUMBER IS 
     /******************************************************
        Date                   Author           Description
        ----------           ---------------  ------------------------------------------------
        06/26/2014 Ravi Dhanekula    Initial code. Added as a fix for Bug # 54214.
      ******************************************************/
      v_return NUMBER;
   BEGIN
   IF p_promotion_type ='recognition' THEN
   SELECT COUNT(1) INTO v_return FROM recognition_claim rc,claim_item ci,claim_recipient cr,participant p WHERE rc.team_id = p_team_id
          AND rc.claim_id = ci.claim_id AND ci.approval_status_type ='approv' AND ci.claim_item_id = cr.claim_item_id
          AND cr.participant_id = p.user_id AND p.allow_public_recognition = 1;
   ELSE
  SELECT COUNT(1) INTO v_return FROM nomination_claim rc,claim_item ci,claim_recipient cr,claim_participant cp,participant p WHERE rc.team_id = p_team_id
          AND rc.claim_id = ci.claim_id AND ci.approval_status_type ='winner' AND ci.claim_item_id = cr.claim_item_id
          AND cp.claim_id(+) = rc.claim_id
          AND NVL(cr.participant_id,cp.participant_id) = p.user_id AND p.allow_public_recognition = 1;
   
   END IF;
      
      RETURN v_return;
   EXCEPTION
      WHEN OTHERS
      THEN
         prc_execution_log_entry (
            'FNC_GET_RECOG_RECVR_CNT',
            1,
            'ERROR',
            'team_id: ' ||p_team_id || SQLERRM,
            NULL);
         v_return := 1;
         RETURN v_return;
   END;
   
PROCEDURE prc_list_recognition_wall
( p_in_userid                    IN  NUMBER,
  p_in_listtype                  IN  VARCHAR2,
  p_in_listvalue                 IN  VARCHAR2,
  p_rownum_start                 IN  NUMBER,
  p_rownum_end                   IN  NUMBER,
  p_out_return_code              OUT NUMBER,
  p_out_result_set               OUT SYS_REFCURSOR,
  p_out_result_set_recipients    OUT SYS_REFCURSOR,          
  p_out_result_set_pub_likes     OUT SYS_REFCURSOR,
  p_out_result_set_pub_comments  OUT SYS_REFCURSOR,
  p_out_result_set_badge         OUT SYS_REFCURSOR
) IS
/*******************************************************************************
-- Purpose: Used to create a data set results for recognition wall.
-- MODIFICATION HISTORY
-- Person               Date        Comments
-- ---------            ----------  -----------------------------------------------------
-- Ravi Dhanekula       04/22/2014  Initial
                        06/25/2014  Fixed the bug # 54156
                        06/25/2014  Fixed the bug # 54326 AND 54502.
                        06/26/2014  Fixed the bug # 54214.
                        06/26/2014  Fixed the bug # 54192.
                        06/26/2014  Fixed the bug # 54416.
-- Suresh J             08/13/2014  Fixed Bug#55725,55388
--                      09/11/2014 - Fixed Bug #56426 - Added condition for Group Claim nominations
--                      09/11/2014 - Bug#55475 - Added public_recognition_comment_id to the result set   
--Ravi Dhanekula        09/26/2014 - Bug # 54214 Fixed issue with team recognition approvals 
--Suresh J              11/10/2014 - Bug 57806 - Fixed Issue for the pax without budget points, 'Add points' link is getting displayed          
--Ravi Dhanekula        11/21/2014 - Bug # 58015 - We should not show cumulative nomination even if one of the claim is set to be hidden to public.            
                                    Added is_cumulative as ouput column with in p_out_result_set   and changed the receiver count to 1 in case of cumulative.             
                        11/24/2014  Bug # 57176, 51989 Fixed issues with sorting nominations data.    
                        1/8/2015    Bug # 58962. Approved nomination missing on the recognition wall   
-- murphyc              01/09/2015  Bug 58981 - need to check claim_recipient.notification_date
--Suresh J              02/19/2015  Bug 59761 -- Nominations of future notification date are shown on PR Wall (Cumulative Nomination)  
-- James Flees          09/22/2016  Bug # 69054 -- Restricted 'global' query by most recent client_item.date_approved values to fix performance issue.        
-- nagarajs             09/26/2016  Bug 69147 - Public Recognition - The nomination that shows up in the 'Team' tab doesn't show up on the 'All' tab of public recognition
-- J Flees              10/17/2016  Refactored process design to optimize query effiency.
-- J Flees              10/27/2016  Added 'recommended' list tab.
-- J Flees              11/14/2016  G6-347 Restrict 'team' tab node hierarchy levels based on property variable.
-- J Flees              01/23/2017  Added department/country list tabs (G6-1460/61). 
--Ravi Dhanekula       02/21/2017 G6-1624 SHow purls only if they have atleast one contributor.
--Arun S               02/22/2017 (G6-1635) Oracle - Avatar enhancements, pull avatar_original instead of avatar_small
-- J Flees              03/06/2017  G6 build temp node hierarchy/rollup tables for non-report queries
--Ravi Dhanekula        04/14/2017 G6-2177 add is_opt_out_of_awards flag
--Ravi Dhanekula        04/25/2017 G6-2210 exception. Caused by recognitions showing as PURL.
--Ravi Dhanekula        09/14/2017 Bug # 73742, G6-2995 Recog wall p_in_Listtype = 'mine' check missing
--Chidamba              03/12/2018 G6-3932 - Public Recognition Add Points - The 'Add Points' link is not appearing 
                                   for a participant who is part of multiple audience groups which in turn is part of the 'Public Recognition' Add Points audience
--Gorantla              04/03/2018 G6-3972/Bug 74889 - Public recognition wall : Like given to a recognition where multiple users 
                                   are recognized is considered only for one recipient among many.     
--Gorantla             07/02/2019  Bug 77739 - Nomination with the comments of 4000 characters length is not loading on nomination approval page.
--Loganathan           07/03/2019  Bug 79071 - Nomination department filter returns claims for pax no longer in the dept even if the claim came in after the dept change
--Gorantla             08/23/2019  GitLab# 2288 Nomination with 4K and Formatted Character are facing issue in display content on various pages & extract                                                                 
*******************************************************************************/
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_LIST_RECOGNITION_WALL';
   c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_check_days         os_propertyset.int_val%TYPE;     -- 09/22/2016
   v_team_levels        os_propertyset.int_val%TYPE;     -- 11/14/2016
   v_locale             VARCHAR2(10):= 'en_US';
   v_user_node_list     VARCHAR2(4000);                  -- 03/06/2017

   CURSOR cur_user_node_list IS  -- 03/06/2017
   SELECT LISTAGG(un.node_id,',') WITHIN GROUP(ORDER BY un.node_id) AS node_list
     FROM user_node un
    WHERE un.user_id = p_in_userid
      AND un.status = 1
      ;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_userid >' || p_in_userid
      || '<, p_in_listtype >' || p_in_listtype   
      || '<, p_in_listvalue >' || p_in_listvalue   
      || '<, p_rownum_start >' || p_rownum_start      
      || '<, p_rownum_end >' || p_rownum_end     
      || '<~';

     -- build team list based on list type
   v_stage := 'build team list';
   DELETE gtt_id_list;

   IF (p_in_Listtype IN ('global', 'recommended')) THEN
      v_check_days := fnc_get_osp_int('public.recog.days.check');    -- 09/22/2016

      INSERT INTO gtt_id_list
      ( id,
        rec_seq,
        ref_text_1,
        ref_text_2,
        ref_nbr_1
      )
      (  -- build team list
         SELECT tl.team_id,
                ROWNUM AS rec_seq,
                gc_ref_text_team_id AS ref_text_1,
                NULL AS ref_text_2,
                2 AS primary_sort
           FROM ( -- get teams
                  -- recognitions
                  SELECT rc.team_id
                    FROM recognition_claim rc,
                         claim_item ci
                   WHERE rc.claim_id = ci.claim_id
                     AND ci.date_approved > TRUNC(SYSDATE) - v_check_days
                     AND ci.approval_status_type = gc_approval_status_approved
                     AND rc.hide_public_recognition = 0
                   UNION
                  -- nominations
                  SELECT nc.team_id
                    FROM nomination_claim nc,
                         claim_item ci,
                         claim c,
                         claim_item_approver cia
                   WHERE nc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND (  ci.claim_item_id = cia.claim_item_id
                         OR c.claim_group_id = cia.claim_group_id)
                     AND cia.date_approved > TRUNC(SYSDATE) - v_check_days
                     AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                     AND cia.approval_status_type = gc_approval_status_winner
                     AND nc.hide_public_recognition = 0
                ) tl
      );
   END IF;  -- global

   IF (p_in_Listtype = 'team') THEN
      v_team_levels := fnc_get_osp_int('public.recog.levels.check.team');     -- 11/14/2016

      -- build fresh version of node hierarchy/rollup tables    03/06/2017
      OPEN cur_user_node_list;
      FETCH cur_user_node_list INTO v_user_node_list;
      CLOSE cur_user_node_list;
      pkg_report_common.p_stage_node_hier_level(v_user_node_list);
      pkg_report_common.p_stage_hierarchy_rollup(v_user_node_list);

      INSERT INTO gtt_id_list
      ( id,
        rec_seq,
        ref_text_1,
        ref_text_2,
        ref_nbr_1
      )
      (  -- build team list
         SELECT tl.team_id,
                ROWNUM AS rec_seq,
                gc_ref_text_team_id AS ref_text_1,
                NULL AS ref_text_2,
                1 AS primary_sort
           FROM ( -- get teams
                  WITH w_claim_item AS
                  (  -- get claims associated with input user's organization
                     SELECT cr.claim_item_id
                       FROM user_node un_ip,          -- input participant
                            gtt_id_list h_ip,         -- hierarchy of input participant   03/06/2017
                            gtt_id_list h_tl,         -- hierarchy of team level          03/06/2017
                            gtt_id_list hr,           -- hierarchy rollup                 03/06/2017
                            user_node un,
                            claim_recipient cr
                      WHERE un_ip.user_id = p_in_userid
                        AND un_ip.status = 1
                        AND hr.ref_text_1 = gc_ref_text_hierarchy_rollup   -- 03/06/2017
                        AND un_ip.node_id = hr.id                          -- 03/06/2017
                        AND hr.ref_nbr_1 = un.node_id                      -- 03/06/2017
                        AND un.status = 1
                        AND un.user_id = cr.participant_id
                         -- restrict by team hierarchy levels                 03/06/2017
                        AND h_ip.ref_text_1 = gc_ref_text_node_hier_level
                        AND h_tl.ref_text_1 = gc_ref_text_node_hier_level
                        AND un_ip.node_id = h_ip.id
                        AND hr.ref_nbr_1 = h_tl.id
                        AND h_tl.ref_nbr_1 BETWEEN h_ip.ref_nbr_1 AND (h_ip.ref_nbr_1 + v_team_levels)
                  )
                  -- get team claims
                  -- recognitions
                  SELECT rc.team_id
                    FROM recognition_claim rc,
                         claim_item ci,
                         w_claim_item wci
                   WHERE rc.claim_id = ci.claim_id
                     AND ci.claim_item_id = wci.claim_item_id
                     AND ci.approval_status_type = gc_approval_status_approved
                     AND rc.hide_public_recognition = 0
                   UNION
                  -- nominations
                  SELECT nc.team_id
                    FROM nomination_claim nc,
                         claim_item ci,
                         claim c,
                         claim_item_approver cia,
                         w_claim_item wci
                   WHERE nc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND (  ci.claim_item_id = cia.claim_item_id
                         OR c.claim_group_id = cia.claim_group_id)
                     AND ci.claim_item_id = wci.claim_item_id
                     AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                     AND cia.approval_status_type = gc_approval_status_winner
                     AND nc.hide_public_recognition = 0
                ) tl
      );

   ELSIF (p_in_Listtype = 'followed') THEN
      INSERT INTO gtt_id_list
      ( id,
        rec_seq,
        ref_text_1,
        ref_text_2,
        ref_nbr_1
      )
      (  -- build team list
         SELECT tl.team_id,
                ROWNUM AS rec_seq,
                gc_ref_text_team_id AS ref_text_1,
                NULL AS ref_text_2,
                1 AS primary_sort
           FROM ( -- get teams
                  WITH w_claim_item AS
                  (  -- get claims associated with input user's followed
                     SELECT cr.claim_item_id
                       FROM participant_followers pf,
                            claim_recipient cr
                      WHERE pf.participant_id = p_in_userid
                        AND pf.follower_id = cr.participant_id
                  )
                  -- get team claims
                  -- recognitions
                  SELECT rc.team_id
                    FROM recognition_claim rc,
                         claim_item ci,
                         w_claim_item wci
                   WHERE rc.claim_id = ci.claim_id
                     AND ci.claim_item_id = wci.claim_item_id
                     AND ci.approval_status_type = gc_approval_status_approved
                     AND rc.hide_public_recognition = 0
                   UNION
                  -- nominations
                  SELECT nc.team_id
                    FROM nomination_claim nc,
                         claim_item ci,
                         claim c,
                         claim_item_approver cia,
                         w_claim_item wci
                   WHERE nc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND (  ci.claim_item_id = cia.claim_item_id
                         OR c.claim_group_id = cia.claim_group_id)
                     AND ci.claim_item_id = wci.claim_item_id
                     AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                     AND cia.approval_status_type = gc_approval_status_winner
                     AND nc.hide_public_recognition = 0
                ) tl
      );

   ELSIF (p_in_Listtype = 'mine') THEN
      INSERT INTO gtt_id_list
      ( id,
        rec_seq,
        ref_text_1,
        ref_text_2,
        ref_nbr_1
      )
      (  -- build team list
         SELECT tl.team_id,
                ROWNUM AS rec_seq,
                gc_ref_text_team_id AS ref_text_1,
                NULL AS ref_text_2,
                1 AS primary_sort
           FROM ( -- get teams
                  WITH w_claim_item AS
                  (  -- get claims associated with input user
                     SELECT cr.claim_item_id
                       FROM claim_recipient cr
                      WHERE cr.participant_id = p_in_userid
                  )
                  -- get team claims
                  -- recognitions
                  SELECT rc.team_id
                    FROM recognition_claim rc,
                         claim_item ci,
                         w_claim_item wci
                   WHERE rc.claim_id = ci.claim_id
                     AND ci.claim_item_id = wci.claim_item_id
                     AND ci.approval_status_type = gc_approval_status_approved
--                     AND rc.hide_public_recognition = 0--For 'mine' we dont restrict any reocgnitions to be shown on wall.
                   UNION
                  -- nominations
                  SELECT nc.team_id
                    FROM nomination_claim nc,
                         claim_item ci,
                         claim c,
                         claim_item_approver cia,
                         w_claim_item wci
                   WHERE nc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND (  ci.claim_item_id = cia.claim_item_id
                         OR c.claim_group_id = cia.claim_group_id)
                     AND ci.claim_item_id = wci.claim_item_id
                     AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                     AND cia.approval_status_type = gc_approval_status_winner
--                     AND nc.hide_public_recognition = 0--For 'mine' we dont restrict any reocgnitions to be shown on wall.
                ) tl
      );

   ELSIF (p_in_Listtype = 'recommended') THEN
      v_check_days := fnc_get_osp_int('public.recog.days.check.recommended');

      INSERT INTO gtt_id_list
      ( id,
        rec_seq,
        ref_text_1,
        ref_text_2,
        ref_nbr_1
      )
      (  -- build team list
         SELECT tl.team_id,
                ROWNUM AS rec_seq,
                gc_ref_text_team_id AS ref_text_1,
                NULL AS ref_text_2,
                1 AS primary_sort
           FROM ( -- get teams
                  WITH w_claim_item AS
                  (  -- get claims relevant to input user
                     SELECT cr.claim_item_id
                       FROM ( -- get recommended list
                              -- get followed
                              SELECT pf.follower_id AS user_id
                                FROM participant_followers pf
                               WHERE pf.participant_id = p_in_userid
                               UNION
                              -- get team+1 members
                              SELECT un_t1.user_id
                                FROM user_node un_t1
                               WHERE un_t1.status = 1
                                 AND un_t1.node_id IN
                                     (-- get input user's team(s)
                                       SELECT un.node_id
                                         FROM user_node un
                                        WHERE un.user_id = p_in_userid
                                          AND un.status = 1
                                        UNION ALL
                                       -- get 1 level below input user's team(s)
                                       SELECT n.node_id
                                         FROM user_node un,
                                              node n
                                        WHERE un.user_id = p_in_userid
                                          AND un.status = 1
                                          AND un.node_id = n.parent_node_id
                                     )
                               UNION
                              -- get connections
                              SELECT con.user_id
                                FROM ( -- get recognized by input user
                                       SELECT uc.receiver_id AS user_id,
                                              ROW_NUMBER() OVER (ORDER BY uc.date_created DESC, uc.id DESC) AS rec_seq
                                         FROM user_connections uc
                                        WHERE uc.sender_id = p_in_userid
                                        UNION ALL
                                       -- get recognizers of input user
                                       SELECT uc.sender_id AS user_id,
                                              ROW_NUMBER() OVER (ORDER BY uc.date_created DESC, uc.id DESC) AS rec_seq
                                         FROM user_connections uc
                                        WHERE uc.receiver_id = p_in_userid
                                     ) con
                                  -- restrict to the lastest entries
                               WHERE con.rec_seq <= 30
                            ) ul,
                            claim_recipient cr
                      WHERE ul.user_id = cr.participant_id
                  )
                  -- get team claims
                  -- recognitions
                  SELECT rc.team_id
                    FROM recognition_claim rc,
                         claim_item ci,
                         w_claim_item wci
                   WHERE rc.claim_id = ci.claim_id
                     AND ci.claim_item_id = wci.claim_item_id
                     AND ci.date_approved > TRUNC(SYSDATE) - v_check_days
                     AND ci.approval_status_type = gc_approval_status_approved
                     AND rc.hide_public_recognition = 0
                   UNION
                  -- nominations
                  SELECT nc.team_id
                    FROM nomination_claim nc,
                         claim_item ci,
                         claim c,
                         claim_item_approver cia,
                         w_claim_item wci
                   WHERE nc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND (  ci.claim_item_id = cia.claim_item_id
                         OR c.claim_group_id = cia.claim_group_id)
                     AND cia.date_approved > TRUNC(SYSDATE) - v_check_days
                     AND ci.claim_item_id = wci.claim_item_id
                     AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                     AND cia.approval_status_type = gc_approval_status_winner
                     AND nc.hide_public_recognition = 0
                ) tl
      );

   -- 01/23/2017 begin block add
   ELSIF (p_in_Listtype = 'department') THEN
      pkg_report_common.p_stage_search_criteria(p_in_listvalue, gc_ref_text_department_type);

      INSERT INTO gtt_id_list
      ( id,
        rec_seq,
        ref_text_1,
        ref_text_2,
        ref_nbr_1
      )
      (  -- build team list
         SELECT tl.team_id,
                ROWNUM AS rec_seq,
                gc_ref_text_team_id AS ref_text_1,
                NULL AS ref_text_2,
                1 AS primary_sort
           FROM ( -- get teams
                  WITH w_claim_item AS
                  (  -- get claims associated with input department list			
                     /*SELECT cr.claim_item_id									--07/03/2019 Bug#79071 --Commented
                       FROM claim_recipient cr,
                            ( -- get participants by department type
                              SELECT pe.user_id,
                                     pe.department_type,
                                     RANK() OVER ( PARTITION BY pe.user_id
                                                   ORDER BY pe.termination_date DESC,
                                                            pe.participant_employer_index DESC
                                                 ) AS rec_rank
                                FROM gtt_id_list gil_dt,          -- department type
                                     participant_employer pe_dt,  -- department type
                                     participant_employer pe
                               WHERE gil_dt.ref_text_1 = gc_ref_text_department_type
                                 AND gil_dt.ref_text_2 = pe_dt.department_type
                                 AND pe_dt.user_id = pe.user_id
                            ) pdt
                      WHERE pdt.user_id = cr.participant_id
                         -- restrict to most recent employer record
                        AND pdt.rec_rank = 1*/									----07/03/2019 Bug#79071 commented upto here
                  
SELECT cr.claim_item_id                                        --07/03/2019 Bug#79071  --Rewritten the query
      FROM (  -- get claims associated with input department list
           SELECT pe.user_id, 
                           pe.department_type,
                           RANK() OVER ( PARTITION BY pe.user_id 
                                                     ORDER BY pe.termination_date DESC,
                                                                   pe.participant_employer_index DESC
                                              ) AS rec_rank
            FROM participant_employer pe) pe,
                gtt_id_list gil_dt,
                claim_recipient cr
      WHERE gil_dt.ref_text_1 = gc_ref_text_department_type
        AND gil_dt.ref_text_2 =  pe.department_type
        AND pe.user_id = cr.participant_id
        AND rec_rank=1										--07/03/2019 Bug#79071  upto here 
                            )
                  -- get team claims
                  -- recognitions
                  SELECT rc.team_id
                    FROM recognition_claim rc,
                         claim_item ci,
                         w_claim_item wci
                   WHERE rc.claim_id = ci.claim_id
                     AND ci.claim_item_id = wci.claim_item_id
                     AND ci.approval_status_type = gc_approval_status_approved
                     AND rc.hide_public_recognition = 0
                   UNION
                  -- nominations
                  SELECT nc.team_id
                    FROM nomination_claim nc,
                         claim_item ci,
                         claim c,
                         claim_item_approver cia,
                         w_claim_item wci
                   WHERE nc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND (  ci.claim_item_id = cia.claim_item_id
                         OR c.claim_group_id = cia.claim_group_id)
                     AND ci.claim_item_id = wci.claim_item_id
                     AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                     AND cia.approval_status_type = gc_approval_status_winner
                     AND nc.hide_public_recognition = 0
                ) tl
      );

   ELSIF (p_in_Listtype = 'country') THEN
      pkg_report_common.p_stage_search_criteria(p_in_listvalue, gc_ref_text_country_id, 1);

      INSERT INTO gtt_id_list
      ( id,
        rec_seq,
        ref_text_1,
        ref_text_2,
        ref_nbr_1
      )
      (  -- build team list
         SELECT tl.team_id,
                ROWNUM AS rec_seq,
                gc_ref_text_team_id AS ref_text_1,
                NULL AS ref_text_2,
                1 AS primary_sort
           FROM ( -- get teams
                  WITH w_claim_item AS
                  (  -- get claims associated with input country list
                     SELECT cr.claim_item_id
                       FROM gtt_id_list gil_c,        -- country
                            user_address ua,
                            claim_recipient cr
                      WHERE gil_c.ref_text_1 = gc_ref_text_country_id
                        AND gil_c.id = ua.country_id
                        AND ua.is_primary = 1
                        AND ua.user_id = cr.participant_id
                  )
                  -- get team claims
                  -- recognitions
                  SELECT rc.team_id
                    FROM recognition_claim rc,
                         claim_item ci,
                         w_claim_item wci
                   WHERE rc.claim_id = ci.claim_id
                     AND ci.claim_item_id = wci.claim_item_id
                     AND ci.approval_status_type = gc_approval_status_approved
                     AND rc.hide_public_recognition = 0
                   UNION
                  -- nominations
                  SELECT nc.team_id
                    FROM nomination_claim nc,
                         claim_item ci,
                         claim c,
                         claim_item_approver cia,
                         w_claim_item wci
                   WHERE nc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND (  ci.claim_item_id = cia.claim_item_id
                         OR c.claim_group_id = cia.claim_group_id)
                     AND ci.claim_item_id = wci.claim_item_id
                     AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                     AND cia.approval_status_type = gc_approval_status_winner
                     AND nc.hide_public_recognition = 0
                ) tl
      );
   -- 01/23/2017 end block add
   END IF;  -- build team list by list type

   -- restrict/sequence team list to the most recent public claims
   v_stage := 'build claim list';
   INSERT INTO gtt_id_list
   ( id,
     rec_seq,
     ref_text_1,
     ref_text_2,
     ref_nbr_1
   )
   (  -- most recent team claims
      SELECT sd.claim_id,
             sd.rec_seq,
             gc_ref_text_claim_id AS ref_text_1,
             NULL AS ref_text_2,
             sd.team_id
        FROM ( -- sequence data
               SELECT mr.*,
                      ROW_NUMBER() OVER (ORDER BY mr.primary_sort,
                                               mr.date_approved DESC,
                                               mr.claim_id DESC
                                        ) AS rec_seq
                 FROM ( -- get most recent team claim
                        SELECT tad.*,
                               ROW_NUMBER() OVER (PARTITION BY tad.team_id
                                                  ORDER BY tad.primary_sort,
                                                        tad.date_approved DESC,
                                                        tad.claim_id DESC
                                                 ) AS claim_seq
                          FROM ( -- get team approval dates
                                 -- recognition team claims
                                 SELECT rc.team_id,
                                        ci.date_approved,
                                        ci.claim_id,
                                        gil.ref_nbr_1 AS primary_sort
                                   FROM gtt_id_list gil,
                                        recognition_claim rc,
                                        claim_item ci,
                                        claim c,
                                        claim_recipient cr,
                                        participant pax_r,     -- recipient
                                        promo_recognition pr,
                                        promotion pm
                                  WHERE gil.ref_text_1 = gc_ref_text_team_id
                                    AND gil.id = rc.team_id
                                    AND rc.claim_id = ci.claim_id
                                    AND ci.claim_id = c.claim_id
                                    AND ci.claim_item_id = cr.claim_item_id
                                    AND cr.participant_id = pax_r.user_id
                                    AND c.promotion_id = pr.promotion_id
                                    AND pr.promotion_id = pm.promotion_id
                                     -- restrict to public approval
                                    AND NVL(cr.notification_date, SYSDATE - 1) < SYSDATE
                                    AND ci.approval_status_type = gc_approval_status_approved
                                    AND pm.promotion_status = gc_promotion_status_active
                                    AND pm.promotion_type = gc_promotion_type_recognition
                                    AND rc.hide_public_recognition = 0
                                    AND (pax_r.allow_public_recognition = 1 OR p_in_Listtype = 'mine')
                                    AND pr.allow_public_recognition = 1
                                    AND NOT EXISTS (                   --added on 02/21/2017
                                    SELECT 'X'
                                      FROM purl_recipient pc
                                      WHERE pc.claim_id   = c.claim_id
                                      AND state = 'expired')
                                  UNION ALL 
                                 -- nomination team claims
                                 SELECT nc.team_id,
                                        cia.date_approved,
                                        nc.claim_id,
                                        gil.ref_nbr_1 AS primary_sort
                                   FROM gtt_id_list gil,
                                        nomination_claim nc,
                                        claim_item ci,
                                        claim c,
                                        claim_item_approver cia,
                                        claim_recipient cr,
                                        participant pax_r,     -- recipient
                                        promo_nomination pn,
                                        promotion pm
                                  WHERE gil.ref_text_1 = gc_ref_text_team_id
                                    AND gil.id = nc.team_id
                                    AND nc.claim_id = ci.claim_id
                                    AND ci.claim_id = c.claim_id
                                    AND (ci.claim_item_id = cia.claim_item_id
                                        OR c.claim_group_id = cia.claim_group_id)
                                    AND ci.claim_item_id = cr.claim_item_id
                                    AND cr.participant_id = pax_r.user_id
                                    AND c.promotion_id = pn.promotion_id
                                    AND pn.promotion_id = pm.promotion_id
                                     -- restrict to public winner
                                    AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE                                                    
                                    AND cia.approval_status_type = gc_approval_status_winner
                                    AND pm.promotion_status = gc_promotion_status_active
                                    AND pm.promotion_type = gc_promotion_type_nomination                
                                    AND nc.hide_public_recognition = 0
                                    AND (pax_r.allow_public_recognition = 1 OR p_in_Listtype = 'mine')
                                    AND pn.allow_public_recognition = 1
                                    AND NOT EXISTS
                                        ( SELECT NULL
                                            FROM nomination_claim nc2
                                           WHERE nc2.team_id = nc.team_id
                                             AND nc2.hide_public_recognition = 1
                                        )
                               ) tad
                      ) mr
                WHERE mr.claim_seq = 1
             ) sd
       WHERE sd.rec_seq BETWEEN p_rownum_start AND p_rownum_end
   );

   v_stage := 'OPEN p_out_result_set';
   OPEN p_out_result_set FOR          
   WITH w_promo_audience AS
   (  -- get promtions for input user's audience
      SELECT DISTINCT 'Y' AS elig, --03/12/2018 
             pa.promotion_id
        FROM promo_audience pa,
             participant_audience paa
       WHERE pa.promo_audience_type = 'PUBLIC_RECOGNITION'
         AND pa.audience_id = paa.audience_id
         AND paa.user_id = p_in_userid
   )
   , w_user_budget AS
   (  -- get budget data associated with input user and claim promotions
      SELECT upb.promotion_id,
             upb.budget_data,
             SUBSTR(upb.budget_data, 1, 1) AS has_budget
        FROM ( -- get user promotion budget
               SELECT cpl.promotion_id,
                      pkg_list_recognition_wall.fnc_check_public_recog_elig(p_in_userid, cpl.promotion_id) AS budget_data
                 FROM ( -- get claim promotion list
                        SELECT DISTINCT
                               c.promotion_id
                          FROM gtt_id_list gil,
                               claim c
                         WHERE gil.ref_text_1 = gc_ref_text_claim_id
                           AND gil.id = c.claim_id
                      ) cpl
             ) upb
   )
   SELECT rd.team_id,
          rd.team_name,
          rd.date_approved,
          rd.giver_first_name,
          rd.giver_last_name,
          rd.giver_avatar,
          rd.submitter_comments,
          rd.submitter_comments_lang_id,
          rd.receiver_count,
          rd.giver_user_id,
          CASE
            WHEN (p_in_Listtype = 'mine') THEN 'N'
            WHEN (rd.receiver_count > 1)  THEN 'N'
            ELSE rd.add_points_elig
          END AS add_points_elig, --06/25/2014 Bug # 54326
          rd.budget_data,
          CASE
            WHEN (rd.receiver_count = 1 AND rd.add_points_elig ='Y') THEN
               pkg_list_recognition_wall.fnc_get_budget_conv_ratio(rd.team_id, p_in_userid) --09/28/2015
            ELSE NULL
          END AS budget_conversion_ratio,
          tlc.likes_count,
          tcc.comments_count,
          rd.promotion_id,
          rd.promotion_name,
          rd.promotion_type,
          rd.is_include_purl,
          rd.purl_recipient_id,
          rd.allow_public_recog_points,
          rd.public_rec_award_type_fixed,
          rd.public_rec_award_amount_min,
          rd.public_rec_award_amount_max,
          rd.public_rec_award_amount_fixed,
          rd.card_active,
          rd.card_id,
          rd.card_image_name,
          rd.hide_public_recognition,
          rd.is_cumulative,
          rd.video_url,
          rd.video_image_url
     FROM ( -- get raw data
            -- team recognitions
            SELECT lrc.team_id,
                   lrc.team_name,
                   lrc.date_approved,
--                   pr.is_include_purl,
                   CASE WHEN pr.is_include_purl = 1 AND pur.purl_recipient_id IS NOT NULL THEN 1 ELSE 0 END is_include_purl,--04/25/2017
                   pur.purl_recipient_id,
                   CASE
                     WHEN (pr.is_include_purl = 1) THEN NULL
                     ELSE au_g.user_id
                   END AS giver_user_id,
                   au_g.first_name AS giver_first_name,
                   au_g.last_name AS giver_last_name,
                   --pax_g.avatar_small AS giver_avatar,    --02/22/2017
                   pax_g.avatar_original AS giver_avatar,    --02/22/2017    
                   lrc.submitter_comments,
                   lrc.submitter_comments_lang_id,
                   trc.receiver_count,
                   CASE
                     WHEN (wub.has_budget = 'Y' AND pr.public_rec_audience_type = 'allactivepaxaudience') THEN 'Y'  --11/10/2014             
                     WHEN (wub.has_budget = 'Y' AND wpa.elig = 'Y') THEN 'Y'  --11/10/2014
                     ELSE 'N'  --11/10/2014
                   END AS add_points_elig,
                   wub.budget_data,
                   pm.promotion_id,
                   pm.promotion_name,
                   pm.promotion_type,
                   pr.allow_public_recog_points,
                   pr.public_rec_award_type_fixed,
                   pr.public_rec_award_amount_min,
                   pr.public_rec_award_amount_max,
                   pr.public_rec_award_amount_fixed,
                   pr.card_active,
                   lrc.card_id,
                   NVL(cd.large_image_name, lrc.own_card_name) AS card_image_name,
                   lrc.hide_public_recognition,
                   0 AS is_cumulative,
                   lrc.card_video_url AS video_url,
                   lrc.card_video_image_url AS video_image_url,
                   lrc.rec_seq
              FROM ( -- get latest recognition claims
                     SELECT trc.*
                       FROM ( -- get team recognition claims
                              SELECT gil.rec_seq,
                                     rc.team_id,
                                     NULL AS team_name,
                                     rc.submitter_comments,
                                     rc.submitter_comments_lang_id,
                                     rc.card_id,
                                     rc.own_card_name,
                                     rc.hide_public_recognition,
                                     rc.card_video_url,
                                     rc.card_video_image_url,
                                     c.claim_id,
                                     c.promotion_id,
                                     c.submitter_id,
                                     ci.date_approved,
                                     -- sequence by approver date
                                     ROW_NUMBER() OVER (PARTITION BY rc.team_id ORDER BY ci.date_approved DESC) AS approver_seq
                                FROM gtt_id_list gil,
                                     recognition_claim rc,
                                     claim c,
                                     claim_item ci,
                                     claim_recipient cr
                               WHERE gil.ref_text_1 = gc_ref_text_claim_id
                                 AND gil.id = rc.claim_id
                                 AND rc.claim_id = c.claim_id
                                 AND rc.claim_id = ci.claim_id
                                 AND ci.claim_item_id = cr.claim_item_id
                                 AND ci.approval_status_type = gc_approval_status_approved
                                 AND NVL(cr.notification_date, SYSDATE - 1) < SYSDATE
                            ) trc
                      WHERE trc.approver_seq = 1
                   ) lrc,
                   promo_recognition pr,
                   promotion pm,
                   application_user au_g,    -- giver
                   participant pax_g,        -- giver
                   card cd,
                   purl_recipient pur,
                   ( -- get team receiver counts
                     SELECT rc.team_id,
                            COUNT(rc_t.ROWID) AS receiver_count
                       FROM gtt_id_list gil,
                            recognition_claim rc,
                            recognition_claim rc_t,   -- team
                            claim_item ci,
                            claim_recipient cr,
                            participant pax
                      WHERE gil.ref_text_1 = gc_ref_text_claim_id
                        AND gil.id = rc.claim_id
                        AND rc.team_id = rc_t.team_id
                        AND rc_t.claim_id = ci.claim_id
                        AND ci.approval_status_type = gc_approval_status_approved
                        AND ci.claim_item_id = cr.claim_item_id
                        AND cr.participant_id = pax.user_id
                        AND (p_in_Listtype = 'mine' OR pax.allow_public_recognition = 1)
                      GROUP BY rc.team_id
                   ) trc,
                   w_promo_audience wpa,
                   w_user_budget wub
             WHERE lrc.promotion_id = pr.promotion_id
               AND pr.promotion_id = pm.promotion_id
               AND lrc.submitter_id = au_g.user_id
               AND lrc.submitter_id = pax_g.user_id
               AND lrc.team_id = trc.team_id
               AND lrc.card_id = cd.card_id (+)
               AND lrc.claim_id = pur.claim_id (+)
               AND lrc.promotion_id = wpa.promotion_id (+)         
               AND lrc.promotion_id = wub.promotion_id (+)
             UNION ALL
            -- team nominations
            SELECT lnc.team_id,
                   lnc.team_name,
                   lnc.date_approved,
                   0 AS is_include_purl,
                   NULL AS purl_recipient_id,
                   au_g.user_id AS giver_user_id,
                   au_g.first_name AS giver_first_name,
                   au_g.last_name AS giver_last_name,
                   --pax_g.avatar_small AS giver_avatar,       --02/22/2017
                   pax_g.avatar_original AS giver_avatar,    --02/22/2017    
                   CASE
                     WHEN lnc.claim_group_id IS NOT NULL THEN NULL
                     -- ELSE NVL(lnc.submitter_comments, cf.submitter_comments)  --  Commented out 07/02/2019
                     ELSE NVL(lnc.submitter_comments, SUBSTR (REGEXP_REPLACE(REGEXP_REPLACE (cf.submitter_comments,'<.+?>'), '<[^>]+>|\&(nbsp;)|(amp;)', ''),1,4000))  -- 07/02/2019 -- 08/23/2019 removed space(' ')
                   END AS submitter_comments,
                   lnc.submitter_comments_lang_id,
                   CASE
                     WHEN lnc.claim_group_id IS NOT NULL THEN 1
                     ELSE trc.receiver_count
                   END AS receiver_count,
                   CASE
                     WHEN (wub.has_budget = 'Y' AND pn.public_rec_audience_type = 'allactivepaxaudience') THEN 'Y'  --11/10/2014             
                     WHEN (wub.has_budget = 'Y' AND wpa.elig = 'Y') THEN 'Y'  --11/10/2014
                     ELSE 'N'  --11/10/2014
                   END AS add_points_elig,
                   wub.budget_data,
                   pm.promotion_id,
                   pm.promotion_name,
                   pm.promotion_type,
                   pn.allow_public_recog_points,      -- Bug # 69574 Added columns
                   pn.public_rec_award_type_fixed,
                   pn.public_rec_award_amount_min,
                   pn.public_rec_award_amount_max,
                   pn.public_rec_award_amount_fixed,  -- Bug # 69574
                   pn.card_active,
                   lnc.card_id,
                   NVL(cd.large_image_name, lnc.own_card_name) AS card_image_name,
                   lnc.hide_public_recognition,
                   CASE
                     WHEN lnc.claim_group_id IS NOT NULL THEN 1
                     ELSE 0
                   END AS is_cumulative,
                   lnc.card_video_url AS video_url,
                   lnc.card_video_image_url video_image_url,
                   lnc.rec_seq
              FROM ( -- get latest nomination claims
                     SELECT tnc.*
                       FROM ( -- get team nomination claims
                              SELECT gil.rec_seq,
                                     nc.team_id,
                                     nc.team_name,
                                     nc.submitter_comments,
                                     nc.submitter_comments_lang_id,
                                     nc.card_id,
                                     nc.own_card_name,
                                     nc.hide_public_recognition,
                                     nc.card_video_url,
                                     nc.card_video_image_url,
                                     c.claim_id,
                                     c.claim_group_id,
                                     c.promotion_id,
                                     c.submitter_id,
                                     cia.date_approved,
                                     -- sequence by approver date
                                     ROW_NUMBER() OVER (PARTITION BY nc.team_id ORDER BY cia.date_approved DESC) AS approver_seq
                                FROM gtt_id_list gil,
                                     nomination_claim nc,
                                     claim c,
                                     claim_item ci,
                                     claim_item_approver cia,
                                     claim_group cg
                               WHERE gil.ref_text_1 = gc_ref_text_claim_id
                                 AND gil.id = nc.claim_id
                                 AND nc.claim_id = c.claim_id
                                 AND nc.claim_id = ci.claim_id  
                                 AND (  ci.claim_item_id = cia.claim_item_id
                                     OR c.claim_group_id = cia.claim_group_id)
                                 AND cia.approval_status_type = gc_approval_status_winner
                                 AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                                 AND c.claim_group_id = cg.claim_group_id (+)  
                            ) tnc
                      WHERE tnc.approver_seq = 1
                   ) lnc,
                   claim_group cg,
                   promo_nomination pn,
                   promotion pm,
                   application_user au_g,    -- giver
                   participant pax_g,        -- giver
                   card cd,
                   ( -- get team receiver counts
                     SELECT nc.team_id,
                            COUNT(nc_t.ROWID) AS receiver_count
                       FROM gtt_id_list gil,
                            nomination_claim nc,
                            nomination_claim nc_t,    -- team
                            claim c,
                            claim_item ci,
                            claim_item_approver cia,
                            claim_recipient cr,
                            claim_participant cp,
                            participant pax
                      WHERE gil.ref_text_1 = gc_ref_text_claim_id
                        AND gil.id = nc.claim_id
                        AND nc.team_id = nc_t.team_id
                        AND nc_t.claim_id = c.claim_id
                        AND nc_t.claim_id = ci.claim_id
                        AND (  ci.claim_item_id = cia.claim_item_id
                            OR c.claim_group_id = cia.claim_group_id)
                        AND cia.approval_status_type = gc_approval_status_winner
                        AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                        AND ci.claim_item_id = cr.claim_item_id
                        AND ci.claim_id = cp.claim_id (+)
                        AND NVL(cr.participant_id, cp.participant_id) = pax.user_id
                        AND (p_in_Listtype = 'mine' OR pax.allow_public_recognition = 1)
                      GROUP BY nc.team_id
                   ) trc,
                   ( -- get submitter comments
                     SELECT cc.claim_id,
                            cc.value AS submitter_comments
                       FROM gtt_id_list gil,
                            claim_cfse cc,
                            claim_form_step_element cfse
                      WHERE gil.ref_text_1 = gc_ref_text_claim_id
                        AND gil.id = cc.claim_id
                        AND cc.claim_form_step_element_id = cfse.claim_form_step_element_id
                        AND cfse.why_field = 1
                   ) cf,
                   w_promo_audience wpa,
                   w_user_budget wub
             WHERE lnc.claim_group_id = cg.claim_group_id (+)  
               AND lnc.promotion_id = pn.promotion_id
               AND pn.promotion_id = pm.promotion_id
               AND lnc.submitter_id = au_g.user_id
               AND lnc.submitter_id = pax_g.user_id         
               AND lnc.team_id = trc.team_id (+)
               AND lnc.card_id = cd.card_id (+)
               AND lnc.claim_id = cf.claim_id (+)
               AND lnc.promotion_id = wpa.promotion_id (+)         
               AND lnc.promotion_id = wub.promotion_id (+)
          ) rd,
          ( -- get team like counts
            SELECT gil.ref_nbr_1 AS team_id,
                   COUNT(prl.ROWID) AS likes_count
              FROM gtt_id_list gil,
                   public_recognition_like prl
             WHERE gil.ref_text_1 = gc_ref_text_claim_id
               AND gil.ref_nbr_1 = prl.team_id (+)
             GROUP BY gil.ref_nbr_1
          ) tlc,
          ( -- get team comment counts
            SELECT gil.ref_nbr_1 AS team_id,
                   COUNT(prc.ROWID) AS comments_count
              FROM gtt_id_list gil,
                   public_recognition_comment prc
             WHERE gil.ref_text_1 = gc_ref_text_claim_id
               AND gil.ref_nbr_1 = prc.team_id (+)
             GROUP BY gil.ref_nbr_1
          ) tcc
    WHERE rd.team_id = tlc.team_id
      AND rd.team_id = tcc.team_id
    ORDER BY rd.rec_seq
      ; 

   v_stage := 'OPEN p_out_result_set_recipients';
   OPEN p_out_result_set_recipients FOR --09/30/2016 Adding more columns for G6 changes.
   -- recognitions
   SELECT rc.team_id,
          rc.claim_id,
          au.user_id AS recvr_user_id,
          au.first_name AS recvr_first_name,
          au.last_name AS recvr_last_name,
          --pax.avatar_small AS recvr_avatar,     --02/22/2017
          pax.avatar_original AS recvr_avatar,    --02/22/2017    
          pe.position_type,
          pe.department_type,
          n.name AS orgunit_name,
          pax.is_opt_out_of_awards--04/14/2017
     FROM gtt_id_list gil,
          recognition_claim rc,
          claim_item ci,
          claim_recipient cr,
          application_user au,
          participant pax,
          node n,
          user_node un,
          rpt_participant_employer pe
    WHERE gil.ref_text_1 = gc_ref_text_claim_id
      AND gil.ref_nbr_1 = rc.team_id
      AND rc.claim_id = ci.claim_id
      AND ci.claim_item_id = cr.claim_item_id
      AND cr.participant_id = au.user_id
      AND ci.approval_status_type = gc_approval_status_approved --06/26/2014 Bug # 54214
      AND au.user_id = pax.user_id
      AND (p_in_Listtype = 'mine' OR pax.allow_public_recognition = 1)
      AND un.user_id = au.user_id
      AND un.is_primary = 1
      AND un.node_id = n.node_id
      AND au.user_id = pe.user_id (+)
    UNION ALL
   -- nominations
   SELECT nc.team_id,
          nc.claim_id,
          au.user_id AS recvr_user_id,
          au.first_name AS recvr_first_name,
          au.last_name AS recvr_last_name,
          --pax.avatar_small AS recvr_avatar,     --02/22/2017
          pax.avatar_original AS recvr_avatar,    --02/22/2017    
          pe.position_type,
          pe.department_type,
          n.name AS orgunit_name,
          pax.is_opt_out_of_awards--04/14/2017
     FROM gtt_id_list gil,
          nomination_claim nc,
          claim c,
          claim_item ci,
          claim_item_approver cia,
          claim_recipient cr,
          claim_participant cp,
          application_user au,
          participant pax,
          node n,
          user_node un,
          rpt_participant_employer pe
    WHERE gil.ref_text_1 = gc_ref_text_claim_id
      AND gil.ref_nbr_1 = nc.team_id
      AND nc.claim_id = c.claim_id
      AND nc.claim_id = ci.claim_id
      AND (  ci.claim_item_id = cia.claim_item_id
          OR c.claim_group_id = cia.claim_group_id)
      AND cia.approval_status_type = gc_approval_status_winner
      AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
      AND ci.claim_item_id = cr.claim_item_id
      AND nc.claim_id = cp.claim_id(+)
      AND NVL(cr.participant_id,cp.participant_id) = au.user_id
      AND au.user_id = pax.user_id
      AND (p_in_Listtype = 'mine' OR pax.allow_public_recognition = 1)
      AND un.user_id = au.user_id
      AND un.is_primary = 1
      AND un.node_id = n.node_id
      AND au.user_id = pe.user_id (+)    
      AND NVL(cr.notification_date, SYSDATE - 1) < SYSDATE  -- 01/09/2015
    ORDER BY team_id DESC,
          claim_id DESC
      ;

   v_stage := 'OPEN p_out_result_set_pub_likes';
   OPEN p_out_result_set_pub_likes FOR
   SELECT prl.team_id,
          max(prl.claim_id) claim_id,  -- 04/03/2018
          au.user_id AS liker_user_id,
          au.first_name AS liker_first_name,
          au.last_name AS liker_last_name                                       
     FROM gtt_id_list gil,
          public_recognition_like prl,
          application_user au
    WHERE gil.ref_text_1 = gc_ref_text_claim_id
      AND gil.ref_nbr_1 = prl.team_id
      AND prl.user_id = au.user_id
   -- 04/03/2018 added group by      
    GROUP BY team_id,
             au.user_id,
             first_name,
             last_name
    ORDER BY prl.team_id DESC,
          claim_id DESC
      ;

   v_stage := 'OPEN p_out_result_set_pub_comments';
   OPEN p_out_result_set_pub_comments FOR
   SELECT prc.team_id,
          max(prc.claim_id) claim_id,  -- 04/03/2018
          prc.comments AS public_comments,
          prc.comments_lang_id AS public_comments_language,
          au.user_id AS commenter_user_id,
          au.first_name AS commenter_first_name,
          au.last_name AS commenter_last_name,
          max(prc.public_recognition_comment_id) public_recognition_comment_id,  -- 04/03/2018
          --pax.avatar_small   --09/11/2014       --02/22/2017
          pax.avatar_original AS avatar_small     --02/22/2017    
     FROM gtt_id_list gil,
          public_recognition_comment prc,
          application_user au,
          participant pax
    WHERE gil.ref_text_1 = gc_ref_text_claim_id
      AND gil.ref_nbr_1 = prc.team_id
      AND prc.user_id = au.user_id
      AND prc.user_id = pax.user_id
    -- 04/03/2018 added group by
    GROUP BY team_id,
             comments,
             comments_lang_id,
             au.user_id,
             first_name,
             last_name,
             avatar_original
    ORDER BY prc.team_id DESC,
          public_recognition_comment_id ASC   --08/13/2014
      ;

   v_stage := 'OPEN p_out_result_set_badge';
   OPEN p_out_result_set_badge FOR
   SELECT t.team_id,
          cav_bn.cms_value AS badge_name,
          cb_pb.earned_image_small AS badge_url
     FROM ( -- get team claims
            SELECT rc.team_id,
                   rc.claim_id
              FROM gtt_id_list gil,
                   recognition_claim rc
             WHERE gil.ref_text_1 = gc_ref_text_claim_id
               AND gil.ref_nbr_1 = rc.team_id
             UNION ALL
            SELECT nc.team_id,
                   nc.claim_id
              FROM gtt_id_list gil,
                   nomination_claim nc
             WHERE gil.ref_text_1 = gc_ref_text_claim_id
               AND gil.ref_nbr_1 = nc.team_id
          ) t,
          participant_badge pb,
          badge_rule br,
          vw_cms_asset_value cav_bn,   -- badge name
          mv_cms_badge cb_pb           -- promotion badge
    WHERE t.claim_id = pb.claim_id
      AND pb.badge_rule_id = br.badge_rule_id
      AND br.badge_name = cav_bn.asset_code (+)
      AND v_locale = cav_bn.locale (+)
      AND gc_cms_ac_promotion_badge = cb_pb.asset_code (+)
      AND br.cm_asset_key = cb_pb.cms_name (+)
      AND v_locale = cb_pb.locale (+)
    ORDER BY t.team_id DESC,
          badge_name
      ;

   -- successful
   p_out_return_code := gc_return_code_success;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := gc_return_code_failure;
      OPEN p_out_result_set FOR SELECT NULL FROM DUAL;
END prc_list_recognition_wall;

PROCEDURE prc_list_recognition_wall_sa
( p_in_userid                    IN  NUMBER,
  p_in_listtype                  IN  VARCHAR2,
  p_in_listvalue                 IN  VARCHAR2,
  p_rownum_start                 IN  NUMBER,
  p_rownum_end                   IN  NUMBER,
  p_out_return_code              OUT NUMBER,
  p_out_result_set               OUT SYS_REFCURSOR,
  p_out_result_set_recipients    OUT SYS_REFCURSOR,          
  p_out_result_set_pub_likes     OUT SYS_REFCURSOR,
  p_out_result_set_pub_comments  OUT SYS_REFCURSOR,
  p_out_result_set_badge         OUT SYS_REFCURSOR
) IS
/*******************************************************************************
-- Purpose: Used to create SA and Celebration data set results for recognition wall.
-- MODIFICATION HISTORY
-- Person               Date        Comments
-- ---------            ----------  -----------------------------------------------------
-- Gorantla            12/07/2018  Initial                            
*******************************************************************************/
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_LIST_RECOGNITION_WALL_1';
   c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_check_days         os_propertyset.int_val%TYPE;    
   v_team_levels        os_propertyset.int_val%TYPE;    
   v_locale             VARCHAR2(10):= 'en_US';
   v_user_node_list     VARCHAR2(4000);               
   gc_ref_text_sa_id    VARCHAR2(100) := 'sa_id';
   
   gc_return_code_success           CONSTANT NUMBER := pkg_const.gc_return_code_success;
   gc_return_code_failure           CONSTANT NUMBER := pkg_const.gc_return_code_failure;

   gc_error                         CONSTANT execution_log.severity%TYPE := pkg_const.gc_error;
   gc_info                          CONSTANT execution_log.severity%TYPE := pkg_const.gc_info;

   gc_approval_status_approved      CONSTANT claim_item_approver.approval_status_type%TYPE := pkg_const.gc_approval_status_approved;
   gc_approval_status_winner        CONSTANT claim_item_approver.approval_status_type%TYPE := pkg_const.gc_approval_status_winner;

   gc_cms_ac_promotion_badge        CONSTANT cms_asset.code%TYPE := pkg_const.gc_cms_ac_promotion_badge;
   gc_promotion_status_active       CONSTANT promotion.promotion_status%TYPE := pkg_const.gc_promotion_status_active;

   gc_promotion_type_nomination     CONSTANT promotion.promotion_type%TYPE := pkg_const.gc_promotion_type_nomination;
   gc_promotion_type_recognition    CONSTANT promotion.promotion_type%TYPE := pkg_const.gc_promotion_type_recognition;

   gc_ref_text_hierarchy_rollup     CONSTANT gtt_id_list.ref_text_1%TYPE := pkg_const.gc_ref_text_hierarchy_rollup;
   gc_ref_text_node_hier_level      CONSTANT gtt_id_list.ref_text_1%TYPE := pkg_const.gc_ref_text_node_hier_level;

   gc_ref_text_claim_id             CONSTANT gtt_id_list.ref_text_1%TYPE := 'claim_id';
   gc_ref_text_team_id              CONSTANT gtt_id_list.ref_text_1%TYPE := 'team_id';
   gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';
   gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
   

   CURSOR cur_user_node_list IS  
   SELECT LISTAGG(un.node_id,',') WITHIN GROUP(ORDER BY un.node_id) AS node_list
     FROM user_node un
    WHERE un.user_id = p_in_userid
      AND un.status = 1
      ;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_userid >' || p_in_userid
      || '<, p_in_listtype >' || p_in_listtype   
      || '<, p_in_listvalue >' || p_in_listvalue   
      || '<, p_rownum_start >' || p_rownum_start      
      || '<, p_rownum_end >' || p_rownum_end     
      || '<~';

     -- build team list based on list type
   v_stage := 'build team list';
   DELETE gtt_id_list;

   IF (p_in_Listtype IN ('global', 'recommended')) THEN
      v_check_days := fnc_get_osp_int('public.recog.days.check');   

      INSERT INTO gtt_id_list
      ( id,
        rec_seq,
        ref_text_1,
        ref_text_2,
        ref_nbr_1
      )
      (  -- build team list
         SELECT tl.team_id,
                ROWNUM AS rec_seq,
                gc_ref_text_team_id AS ref_text_1,
                NULL AS ref_text_2,
                2 AS primary_sort
           FROM ( -- get teams
                  -- SA & Celebrations recognitions
                  SELECT sii.team_id
                    FROM sa_celebration_info sii
                   WHERE award_date > TRUNC(SYSDATE) - v_check_days
                     AND sii.team_id is not null
                   UNION
                  -- non sa & celeb recognitions
                  SELECT rc.team_id
                    FROM recognition_claim rc,
                         claim_item ci,
                         claim c,
                         promo_recognition pr
                   WHERE rc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND c.promotion_id = pr.promotion_id
                     AND pr.include_celebrations = 0 
                     AND pr.is_include_purl = 0
                     AND ci.date_approved > TRUNC(SYSDATE) - v_check_days
                     AND ci.approval_status_type = gc_approval_status_approved
                     AND rc.hide_public_recognition = 0
                   UNION
                  -- nominations
                  SELECT nc.team_id
                    FROM nomination_claim nc,
                         claim_item ci,
                         claim c,
                         claim_item_approver cia
                   WHERE nc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND (  ci.claim_item_id = cia.claim_item_id
                         OR c.claim_group_id = cia.claim_group_id)
                     AND cia.date_approved > TRUNC(SYSDATE) - v_check_days
                     AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                     AND cia.approval_status_type = gc_approval_status_winner
                     AND nc.hide_public_recognition = 0
                ) tl
      );
   END IF;  -- global

   IF (p_in_Listtype = 'team') THEN
      v_team_levels := fnc_get_osp_int('public.recog.levels.check.team');  

      -- build fresh version of node hierarchy/rollup tables    
      OPEN cur_user_node_list;
      FETCH cur_user_node_list INTO v_user_node_list;
      CLOSE cur_user_node_list;
      pkg_report_common.p_stage_node_hier_level(v_user_node_list);
      pkg_report_common.p_stage_hierarchy_rollup(v_user_node_list);

      INSERT INTO gtt_id_list
      ( id,
        rec_seq,
        ref_text_1,
        ref_text_2,
        ref_nbr_1
      )
      (  -- build team list
         SELECT tl.team_id,
                ROWNUM AS rec_seq,
                gc_ref_text_team_id AS ref_text_1,
                NULL AS ref_text_2,
                1 AS primary_sort
           FROM ( -- get teams
                  WITH w_claim_item AS
                  (  -- get claims associated with input user's organization
                     SELECT cr.claim_item_id
                       FROM user_node un_ip,          -- input participant
                            gtt_id_list h_ip,         -- hierarchy of input participant   
                            gtt_id_list h_tl,         -- hierarchy of team level         
                            gtt_id_list hr,           -- hierarchy rollup                 
                            user_node un,
                            claim_recipient cr
                      WHERE un_ip.user_id = p_in_userid
                        AND un_ip.status = 1
                        AND hr.ref_text_1 = gc_ref_text_hierarchy_rollup 
                        AND un_ip.node_id = hr.id                         
                        AND hr.ref_nbr_1 = un.node_id                    
                        AND un.status = 1
                        AND un.user_id = cr.participant_id
                        AND h_ip.ref_text_1 = gc_ref_text_node_hier_level
                        AND h_tl.ref_text_1 = gc_ref_text_node_hier_level
                        AND un_ip.node_id = h_ip.id
                        AND hr.ref_nbr_1 = h_tl.id
                        AND h_tl.ref_nbr_1 BETWEEN h_ip.ref_nbr_1 AND (h_ip.ref_nbr_1 + v_team_levels)
                  ),
                  w_sa_rec AS
                  (  -- get records associated with input user's organization
                     SELECT sii.id
                       FROM user_node un_ip,          -- input participant
                            gtt_id_list h_ip,         -- hierarchy of input participant   
                            gtt_id_list h_tl,         -- hierarchy of team level          
                            gtt_id_list hr,           -- hierarchy rollup                 
                            user_node un,
                            sa_celebration_info sii
                      WHERE un_ip.user_id = p_in_userid
                        AND un_ip.status = 1
                        AND hr.ref_text_1 = gc_ref_text_hierarchy_rollup   
                        AND un_ip.node_id = hr.id                         
                        AND hr.ref_nbr_1 = un.node_id                      
                        AND un.status = 1
                        AND un.user_id = sii.recipient_id
                        AND sii.team_id is not null
                         -- restrict by team hierarchy levels                
                        AND h_ip.ref_text_1 = gc_ref_text_node_hier_level
                        AND h_tl.ref_text_1 = gc_ref_text_node_hier_level
                        AND un_ip.node_id = h_ip.id
                        AND hr.ref_nbr_1 = h_tl.id
                        AND h_tl.ref_nbr_1 BETWEEN h_ip.ref_nbr_1 AND (h_ip.ref_nbr_1 + v_team_levels)
                  )
                  -- get team claims
                  -- SA & Celebrations recognitions
                  SELECT sii.team_id
                    FROM sa_celebration_info sii,
                         w_sa_rec wsr
                   WHERE sii.id = wsr.id
                     AND sii.team_id is not null
                   UNION
                  -- non sa & celeb recognitions
                  SELECT rc.team_id
                    FROM recognition_claim rc,
                         claim_item ci,
                         w_claim_item wci,
                         claim c,
                         promo_recognition pr
                   WHERE rc.claim_id = ci.claim_id
                     AND ci.claim_item_id = wci.claim_item_id
                     AND ci.claim_id = c.claim_id
                     AND c.promotion_id = pr.promotion_id
                     AND pr.include_celebrations = 0 
                     AND pr.is_include_purl = 0
                     AND ci.approval_status_type = gc_approval_status_approved
                     AND rc.hide_public_recognition = 0
                   UNION
                  -- nominations
                  SELECT nc.team_id
                    FROM nomination_claim nc,
                         claim_item ci,
                         claim c,
                         claim_item_approver cia,
                         w_claim_item wci
                   WHERE nc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND (  ci.claim_item_id = cia.claim_item_id
                         OR c.claim_group_id = cia.claim_group_id)
                     AND ci.claim_item_id = wci.claim_item_id
                     AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                     AND cia.approval_status_type = gc_approval_status_winner
                     AND nc.hide_public_recognition = 0
                ) tl
      );

   ELSIF (p_in_Listtype = 'followed') THEN
      INSERT INTO gtt_id_list
      ( id,
        rec_seq,
        ref_text_1,
        ref_text_2,
        ref_nbr_1
      )
      (  -- build team list
         SELECT tl.team_id,
                ROWNUM AS rec_seq,
                gc_ref_text_team_id AS ref_text_1,
                NULL AS ref_text_2,
                1 AS primary_sort
           FROM ( -- get teams
                  WITH w_claim_item AS
                  (  -- get claims associated with input user's followed
                     SELECT cr.claim_item_id
                       FROM participant_followers pf,
                            claim_recipient cr
                      WHERE pf.participant_id = p_in_userid
                        AND pf.follower_id = cr.participant_id
                  ),
                   w_sa_rec AS
                  (  -- get claims associated with input user's followed
                     SELECT sii.id
                       FROM participant_followers pf,
                            sa_celebration_info sii
                      WHERE pf.participant_id = p_in_userid
                        AND pf.follower_id = sii.recipient_id
                        AND sii.team_id is not null
                  )
                  -- get team claims
                  -- SA & Celebrations recognitions
                  SELECT sii.team_id
                    FROM sa_celebration_info sii,
                         w_sa_rec wsr
                   WHERE sii.id = wsr.id
                     AND sii.team_id is not null
                   UNION
                  -- non sa & celeb recognitions
                  SELECT rc.team_id
                    FROM recognition_claim rc,
                         claim_item ci,
                         w_claim_item wci,
                         claim c,
                         promo_recognition pr
                   WHERE rc.claim_id = ci.claim_id
                     AND ci.claim_item_id = wci.claim_item_id
                     AND ci.claim_id = c.claim_id
                     AND c.promotion_id = pr.promotion_id
                     AND pr.include_celebrations = 0 
                     AND pr.is_include_purl = 0
                     AND ci.approval_status_type = gc_approval_status_approved
                     AND rc.hide_public_recognition = 0
                   UNION
                  -- nominations
                  SELECT nc.team_id
                    FROM nomination_claim nc,
                         claim_item ci,
                         claim c,
                         claim_item_approver cia,
                         w_claim_item wci
                   WHERE nc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND (  ci.claim_item_id = cia.claim_item_id
                         OR c.claim_group_id = cia.claim_group_id)
                     AND ci.claim_item_id = wci.claim_item_id
                     AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                     AND cia.approval_status_type = gc_approval_status_winner
                     AND nc.hide_public_recognition = 0
                ) tl
      );

   ELSIF (p_in_Listtype = 'mine') THEN
      INSERT INTO gtt_id_list
      ( id,
        rec_seq,
        ref_text_1,
        ref_text_2,
        ref_nbr_1
      )
      (  -- build team list
         SELECT tl.team_id,
                ROWNUM AS rec_seq,
                gc_ref_text_team_id AS ref_text_1,
                NULL AS ref_text_2,
                1 AS primary_sort
           FROM ( -- get teams
                  WITH w_claim_item AS
                  (  -- get claims associated with input user
                     SELECT cr.claim_item_id
                       FROM claim_recipient cr
                      WHERE cr.participant_id = p_in_userid
                  ),
                  w_sa_rec AS
                  (  -- get claims associated with input user
                     SELECT sii.id
                       FROM sa_celebration_info sii
                      WHERE sii.recipient_id = p_in_userid
                        AND sii.team_id is not null
                  )
                  -- get team claims
                  -- SA & Celebrations 
                  SELECT sii.team_id
                    FROM sa_celebration_info sii,
                         w_sa_rec wsr
                   WHERE sii.id = wsr.id
                     AND sii.team_id is not null
                   UNION
                  -- recognitions
                  SELECT rc.team_id
                    FROM recognition_claim rc,
                         claim_item ci,
                         w_claim_item wci,
                         claim c,
                         promo_recognition pr
                   WHERE rc.claim_id = ci.claim_id
                     AND ci.claim_item_id = wci.claim_item_id
                     AND ci.claim_id = c.claim_id
                     AND c.promotion_id = pr.promotion_id
                     AND pr.include_celebrations = 0 
                     AND pr.is_include_purl = 0
                     AND ci.approval_status_type = gc_approval_status_approved
                  --   AND rc.hide_public_recognition = 0--For 'mine' we dont restrict any reocgnitions to be shown on wall.
                   UNION
                  -- nominations
                  SELECT nc.team_id
                    FROM nomination_claim nc,
                         claim_item ci,
                         claim c,
                         claim_item_approver cia,
                         w_claim_item wci
                   WHERE nc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND (  ci.claim_item_id = cia.claim_item_id
                         OR c.claim_group_id = cia.claim_group_id)
                     AND ci.claim_item_id = wci.claim_item_id
                     AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                     AND cia.approval_status_type = gc_approval_status_winner
               --    AND nc.hide_public_recognition = 0--For 'mine' we dont restrict any reocgnitions to be shown on wall.
                ) tl
      );

   ELSIF (p_in_Listtype = 'recommended') THEN
      v_check_days := fnc_get_osp_int('public.recog.days.check.recommended');

      INSERT INTO gtt_id_list
      ( id,
        rec_seq,
        ref_text_1,
        ref_text_2,
        ref_nbr_1
      )
      (  -- build team list
         SELECT tl.team_id,
                ROWNUM AS rec_seq,
                gc_ref_text_team_id AS ref_text_1,
                NULL AS ref_text_2,
                1 AS primary_sort
           FROM ( -- get teams
                  WITH w_claim_item AS
                  (  -- get claims relevant to input user
                     SELECT cr.claim_item_id
                       FROM ( -- get recommended list
                              -- get followed
                              SELECT pf.follower_id AS user_id
                                FROM participant_followers pf
                               WHERE pf.participant_id = p_in_userid
                               UNION
                              -- get team+1 members
                              SELECT un_t1.user_id
                                FROM user_node un_t1
                               WHERE un_t1.status = 1
                                 AND un_t1.node_id IN
                                     (-- get input user's team(s)
                                       SELECT un.node_id
                                         FROM user_node un
                                        WHERE un.user_id = p_in_userid
                                          AND un.status = 1
                                        UNION ALL
                                       -- get 1 level below input user's team(s)
                                       SELECT n.node_id
                                         FROM user_node un,
                                              node n
                                        WHERE un.user_id = p_in_userid
                                          AND un.status = 1
                                          AND un.node_id = n.parent_node_id
                                     )
                               UNION
                              -- get connections
                              SELECT con.user_id
                                FROM ( -- get recognized by input user
                                       SELECT uc.receiver_id AS user_id,
                                              ROW_NUMBER() OVER (ORDER BY uc.date_created DESC, uc.id DESC) AS rec_seq
                                         FROM user_connections uc
                                        WHERE uc.sender_id = p_in_userid
                                        UNION ALL
                                       -- get recognizers of input user
                                       SELECT uc.sender_id AS user_id,
                                              ROW_NUMBER() OVER (ORDER BY uc.date_created DESC, uc.id DESC) AS rec_seq
                                         FROM user_connections uc
                                        WHERE uc.receiver_id = p_in_userid
                                     ) con
                                  -- restrict to the lastest entries
                               WHERE con.rec_seq <= 30
                            ) ul,
                            claim_recipient cr
                      WHERE ul.user_id = cr.participant_id
                  ),
                  w_sa_rec AS
                  (  -- get claims relevant to input user
                     SELECT sii.id
                       FROM ( -- get recommended list
                              -- get followed
                              SELECT pf.follower_id AS user_id
                                FROM participant_followers pf
                               WHERE pf.participant_id = p_in_userid
                               UNION
                              -- get team+1 members
                              SELECT un_t1.user_id
                                FROM user_node un_t1
                               WHERE un_t1.status = 1
                                 AND un_t1.node_id IN
                                     (-- get input user's team(s)
                                       SELECT un.node_id
                                         FROM user_node un
                                        WHERE un.user_id = p_in_userid
                                          AND un.status = 1
                                        UNION ALL
                                       -- get 1 level below input user's team(s)
                                       SELECT n.node_id
                                         FROM user_node un,
                                              node n
                                        WHERE un.user_id = p_in_userid
                                          AND un.status = 1
                                          AND un.node_id = n.parent_node_id
                                     )
                               UNION
                              -- get connections
                              SELECT con.user_id
                                FROM ( -- get recognized by input user
                                       SELECT uc.receiver_id AS user_id,
                                              ROW_NUMBER() OVER (ORDER BY uc.date_created DESC, uc.id DESC) AS rec_seq
                                         FROM user_connections uc
                                        WHERE uc.sender_id = p_in_userid
                                        UNION ALL
                                       -- get recognizers of input user
                                       SELECT uc.sender_id AS user_id,
                                              ROW_NUMBER() OVER (ORDER BY uc.date_created DESC, uc.id DESC) AS rec_seq
                                         FROM user_connections uc
                                        WHERE uc.receiver_id = p_in_userid
                                     ) con
                                  -- restrict to the lastest entries
                               WHERE con.rec_seq <= 30
                            ) ul,
                            sa_celebration_info sii
                      WHERE ul.user_id = sii.recipient_id
                        AND sii.team_id is not null
                  )
                  -- get team claims
                  -- SA & Celebrations recognitions
                  SELECT sii.team_id
                    FROM sa_celebration_info sii,
                         w_sa_rec wsr
                   WHERE sii.id = wsr.id
                     AND sii.team_id is not null
                  UNION
                  -- recognitions
                  SELECT rc.team_id
                    FROM recognition_claim rc,
                         claim_item ci,
                         w_claim_item wci,
                         claim c,
                         promo_recognition pr
                   WHERE rc.claim_id = ci.claim_id
                     AND ci.claim_item_id = wci.claim_item_id
                     AND ci.claim_id = c.claim_id
                     AND c.promotion_id = pr.promotion_id
                     AND pr.include_celebrations = 0 
                     AND pr.is_include_purl = 0
                     AND ci.date_approved > TRUNC(SYSDATE) - v_check_days
                     AND ci.approval_status_type = gc_approval_status_approved
                     AND rc.hide_public_recognition = 0
                   UNION
                  -- nominations
                  SELECT nc.team_id
                    FROM nomination_claim nc,
                         claim_item ci,
                         claim c,
                         claim_item_approver cia,
                         w_claim_item wci
                   WHERE nc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND (  ci.claim_item_id = cia.claim_item_id
                         OR c.claim_group_id = cia.claim_group_id)
                     AND cia.date_approved > TRUNC(SYSDATE) - v_check_days
                     AND ci.claim_item_id = wci.claim_item_id
                     AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                     AND cia.approval_status_type = gc_approval_status_winner
                     AND nc.hide_public_recognition = 0
                ) tl
      );

   ELSIF (p_in_Listtype = 'department') THEN
      pkg_report_common.p_stage_search_criteria(p_in_listvalue, gc_ref_text_department_type);

      INSERT INTO gtt_id_list
      ( id,
        rec_seq,
        ref_text_1,
        ref_text_2,
        ref_nbr_1
      )
      (  -- build team list
         SELECT tl.team_id,
                ROWNUM AS rec_seq,
                gc_ref_text_team_id AS ref_text_1,
                NULL AS ref_text_2,
                1 AS primary_sort
           FROM ( -- get teams
                  WITH w_claim_item AS
                  (  -- get claims associated with input department list
                     SELECT cr.claim_item_id
                       FROM claim_recipient cr,
                            ( -- get participants by department type
                              SELECT pe.user_id,
                                     pe.department_type,
                                     RANK() OVER ( PARTITION BY pe.user_id
                                                   ORDER BY pe.termination_date DESC,
                                                            pe.participant_employer_index DESC
                                                 ) AS rec_rank
                                FROM gtt_id_list gil_dt,          -- department type
                                     participant_employer pe_dt,  -- department type
                                     participant_employer pe
                               WHERE gil_dt.ref_text_1 = gc_ref_text_department_type
                                 AND gil_dt.ref_text_2 = pe_dt.department_type
                                 AND pe_dt.user_id = pe.user_id
                            ) pdt
                      WHERE pdt.user_id = cr.participant_id
                         -- restrict to most recent employer record
                        AND pdt.rec_rank = 1
                  ),
                  w_sa_rec AS
                  (  -- get claims associated with input department list
                     SELECT sii.id
                       FROM sa_celebration_info sii,
                            ( -- get participants by department type
                              SELECT pe.user_id,
                                     pe.department_type,
                                     RANK() OVER ( PARTITION BY pe.user_id
                                                   ORDER BY pe.termination_date DESC,
                                                            pe.participant_employer_index DESC
                                                 ) AS rec_rank
                                FROM gtt_id_list gil_dt,          -- department type
                                     participant_employer pe_dt,  -- department type
                                     participant_employer pe
                               WHERE gil_dt.ref_text_1 = gc_ref_text_department_type
                                 AND gil_dt.ref_text_2 = pe_dt.department_type
                                 AND pe_dt.user_id = pe.user_id
                            ) pdt
                      WHERE pdt.user_id = sii.recipient_id
                        AND sii.team_id is not null
                         -- restrict to most recent employer record
                        AND pdt.rec_rank = 1
                  )
                  -- get team claims
                  -- SA & Celebrations 
                  SELECT sii.team_id
                    FROM sa_celebration_info sii,
                         w_sa_rec wsr
                   WHERE sii.id = wsr.id
                     AND sii.team_id is not null
                   UNION
                  -- recognitions
                  SELECT rc.team_id
                    FROM recognition_claim rc,
                         claim_item ci,
                         w_claim_item wci,
                         claim c,
                         promo_recognition pr
                   WHERE rc.claim_id = ci.claim_id
                     AND ci.claim_item_id = wci.claim_item_id
                     AND ci.claim_id = c.claim_id
                     AND c.promotion_id = pr.promotion_id
                     AND pr.include_celebrations = 0 
                     AND pr.is_include_purl = 0
                     AND ci.approval_status_type = gc_approval_status_approved
                   UNION
                  -- nominations
                  SELECT nc.team_id
                    FROM nomination_claim nc,
                         claim_item ci,
                         claim c,
                         claim_item_approver cia,
                         w_claim_item wci
                   WHERE nc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND (  ci.claim_item_id = cia.claim_item_id
                         OR c.claim_group_id = cia.claim_group_id)
                     AND ci.claim_item_id = wci.claim_item_id
                     AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                     AND cia.approval_status_type = gc_approval_status_winner
                     AND nc.hide_public_recognition = 0
                ) tl
      );

   ELSIF (p_in_Listtype = 'country') THEN
      pkg_report_common.p_stage_search_criteria(p_in_listvalue, gc_ref_text_country_id, 1);

      INSERT INTO gtt_id_list
      ( id,
        rec_seq,
        ref_text_1,
        ref_text_2,
        ref_nbr_1
      )
      (  -- build team list
         SELECT tl.team_id,
                ROWNUM AS rec_seq,
                gc_ref_text_team_id AS ref_text_1,
                NULL AS ref_text_2,
                1 AS primary_sort
           FROM ( -- get teams
                  WITH w_claim_item AS
                  (  -- get claims associated with input country list
                     SELECT cr.claim_item_id
                       FROM gtt_id_list gil_c,        -- country
                            user_address ua,
                            claim_recipient cr
                      WHERE gil_c.ref_text_1 = gc_ref_text_country_id
                        AND gil_c.id = ua.country_id
                        AND ua.is_primary = 1
                        AND ua.user_id = cr.participant_id
                  ),
                  w_sa_rec AS
                  (  -- get claims associated with input country list
                     SELECT sii.id
                       FROM gtt_id_list gil_c,        -- country
                            user_address ua,
                            sa_celebration_info sii
                      WHERE gil_c.ref_text_1 = gc_ref_text_country_id
                        AND gil_c.id = ua.country_id
                        AND ua.is_primary = 1
                        AND ua.user_id = sii.recipient_id
                        AND sii.team_id is not null
                  )
                  -- get team claims
                  -- SA & Celebrations 
                  SELECT sii.team_id
                    FROM sa_celebration_info sii,
                         w_sa_rec wsr
                   WHERE sii.id = wsr.id
                     AND sii.team_id is not null
                   UNION
                  -- recognitions
                  SELECT rc.team_id
                    FROM recognition_claim rc,
                         claim_item ci,
                         w_claim_item wci,
                         claim c,
                         promo_recognition pr
                   WHERE rc.claim_id = ci.claim_id
                     AND ci.claim_item_id = wci.claim_item_id
                     AND ci.claim_id = c.claim_id
                     AND c.promotion_id = pr.promotion_id
                     AND pr.include_celebrations = 0 
                     AND pr.is_include_purl = 0
                     AND ci.approval_status_type = gc_approval_status_approved
                   UNION
                  -- nominations
                  SELECT nc.team_id
                    FROM nomination_claim nc,
                         claim_item ci,
                         claim c,
                         claim_item_approver cia,
                         w_claim_item wci
                   WHERE nc.claim_id = ci.claim_id
                     AND ci.claim_id = c.claim_id
                     AND (  ci.claim_item_id = cia.claim_item_id
                         OR c.claim_group_id = cia.claim_group_id)
                     AND ci.claim_item_id = wci.claim_item_id
                     AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                     AND cia.approval_status_type = gc_approval_status_winner
                     AND nc.hide_public_recognition = 0
                ) tl
      );
   END IF;  -- build team list by list type
   
    -- restrict/sequence team list to the most recent public claims
   v_stage := 'build claim list';
   INSERT INTO gtt_id_list
   ( id,
     rec_seq,
     ref_text_1,
     ref_text_2,
     ref_nbr_1
   )
   (  -- most recent team claims
      SELECT sd.claim_id,
             sd.rec_seq,
             gc_ref_text_claim_id AS ref_text_1,
             NULL AS ref_text_2,
             sd.team_id
        FROM ( -- sequence data
               SELECT mr.*,
                      ROW_NUMBER() OVER (ORDER BY mr.primary_sort,
                                               mr.date_approved DESC,
                                               mr.claim_id DESC
                                        ) AS rec_seq
                 FROM ( -- get most recent team claim
                        SELECT tad.*,
                               ROW_NUMBER() OVER (PARTITION BY tad.team_id
                                                  ORDER BY tad.primary_sort,
                                                        tad.date_approved DESC,
                                                        tad.claim_id DESC
                                                 ) AS claim_seq
                          FROM ( -- get team approval dates
                                 -- recognition team claims
                                 SELECT rc.team_id,
                                        ci.date_approved,
                                        ci.claim_id,
                                        gil.ref_nbr_1 AS primary_sort
                                   FROM gtt_id_list gil,
                                        recognition_claim rc,
                                        claim_item ci,
                                        claim c,
                                        claim_recipient cr,
                                        participant pax_r,     -- recipient
                                        promo_recognition pr,
                                        promotion pm
                                  WHERE gil.ref_text_1 = gc_ref_text_team_id
                                    AND gil.id = rc.team_id
                                    AND rc.claim_id = ci.claim_id
                                    AND ci.claim_id = c.claim_id
                                    AND ci.claim_item_id = cr.claim_item_id
                                    AND cr.participant_id = pax_r.user_id
                                    AND c.promotion_id = pr.promotion_id
                                    AND pr.promotion_id = pm.promotion_id
                                    AND pr.include_celebrations = 0 
                                    AND pr.is_include_purl = 0
                                     -- restrict to public approval
                                    AND NVL(cr.notification_date, SYSDATE - 1) < SYSDATE
                                    AND ci.approval_status_type = gc_approval_status_approved
                                    AND pm.promotion_status = gc_promotion_status_active
                                    AND pm.promotion_type = gc_promotion_type_recognition
                                    AND rc.hide_public_recognition = 0
                                    AND (pax_r.allow_public_recognition = 1 OR p_in_Listtype = 'mine')
                                    AND pr.allow_public_recognition = 1
                                  UNION ALL 
                                    -- recognition team claims
                                 SELECT sii.team_id,
                                        sii.award_date date_approved,
                                        sii.id claim_id,
                                        gil.ref_nbr_1 AS primary_sort
                                   FROM gtt_id_list gil,
                                        sa_celebration_info sii,
                                        participant pax_r
                                  WHERE gil.ref_text_1 = gc_ref_text_team_id
                                    AND gil.id = sii.team_id
                                    AND sii.team_id is not null
                                    AND sii.recipient_id = pax_r.user_id
                                    AND NVL(sii.award_date, SYSDATE - 1) < SYSDATE
                                    AND (pax_r.allow_public_recognition = 1 OR p_in_Listtype = 'mine')                                  
                                  UNION ALL
                                 -- nomination team claims
                                 SELECT nc.team_id,
                                        cia.date_approved,
                                        nc.claim_id,
                                        gil.ref_nbr_1 AS primary_sort
                                   FROM gtt_id_list gil,
                                        nomination_claim nc,
                                        claim_item ci,
                                        claim c,
                                        claim_item_approver cia,
                                        claim_recipient cr,
                                        participant pax_r,     -- recipient
                                        promo_nomination pn,
                                        promotion pm
                                  WHERE gil.ref_text_1 = gc_ref_text_team_id
                                    AND gil.id = nc.team_id
                                    AND nc.claim_id = ci.claim_id
                                    AND ci.claim_id = c.claim_id
                                    AND (ci.claim_item_id = cia.claim_item_id
                                        OR c.claim_group_id = cia.claim_group_id)
                                    AND ci.claim_item_id = cr.claim_item_id
                                    AND cr.participant_id = pax_r.user_id
                                    AND c.promotion_id = pn.promotion_id
                                    AND pn.promotion_id = pm.promotion_id
                                     -- restrict to public winner
                                    AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE                                                    
                                    AND cia.approval_status_type = gc_approval_status_winner
                                    AND pm.promotion_status = gc_promotion_status_active
                                    AND pm.promotion_type = gc_promotion_type_nomination                
                                    AND nc.hide_public_recognition = 0
                                    AND (pax_r.allow_public_recognition = 1 OR p_in_Listtype = 'mine')
                                    AND pn.allow_public_recognition = 1
                                    AND NOT EXISTS
                                        ( SELECT NULL
                                            FROM nomination_claim nc2
                                           WHERE nc2.team_id = nc.team_id
                                             AND nc2.hide_public_recognition = 1
                                        )
                               ) tad
                      ) mr
                WHERE mr.claim_seq = 1
             ) sd
       WHERE sd.rec_seq BETWEEN p_rownum_start AND p_rownum_end
   );
   
   -- restrict/sequence team list to the most recent SA records
 /*  v_stage := 'build SA records list';
   INSERT INTO gtt_id_list
   ( id,
     rec_seq,
     ref_text_1,
     ref_text_2,
     ref_nbr_1
   )
   (  -- most recent team claims
      SELECT sd.id,
             sd.rec_seq,
             gc_ref_text_sa_id AS ref_text_1,
             NULL AS ref_text_2,
             sd.team_id
        FROM ( -- sequence data
               SELECT mr.*,
                      ROW_NUMBER() OVER (ORDER BY mr.primary_sort,
                                               mr.award_date DESC,
                                               mr.id DESC
                                        ) AS rec_seq
                 FROM ( -- get most recent team claim
                        SELECT tad.*,
                               ROW_NUMBER() OVER (PARTITION BY tad.team_id
                                                  ORDER BY tad.primary_sort,
                                                        tad.award_date DESC,
                                                        tad.id DESC
                                                 ) AS claim_seq
                          FROM ( -- get team approval dates
                                 -- recognition team claims
                                 SELECT sii.team_id,
                                        sii.award_date,
                                        sii.id,
                                        gil.ref_nbr_1 AS primary_sort
                                   FROM gtt_id_list gil,
                                        sa_celebration_info sii,
                                        participant pax_r
                                  WHERE gil.ref_text_1 = gc_ref_text_team_id
                                    AND gil.id = sii.team_id
                                    AND sii.team_id is not null
                                    AND sii.recipient_id = pax_r.user_id
                                    AND NVL(sii.award_date, SYSDATE - 1) < SYSDATE
                                    AND (pax_r.allow_public_recognition = 1 OR p_in_Listtype = 'mine')
                               ) tad
                      ) mr
                WHERE mr.claim_seq = 1
             ) sd
       WHERE sd.rec_seq BETWEEN p_rownum_start AND p_rownum_end
   );  */

   v_stage := 'OPEN p_out_result_set';
   OPEN p_out_result_set FOR          
   WITH w_promo_audience AS
   (  -- get promtions for input user's audience
      SELECT DISTINCT 'Y' AS elig, 
             pa.promotion_id
        FROM promo_audience pa,
             participant_audience paa
       WHERE pa.promo_audience_type = 'PUBLIC_RECOGNITION'
         AND pa.audience_id = paa.audience_id
         AND paa.user_id = p_in_userid
   )
   , w_user_budget AS
   (  -- get budget data associated with input user and claim promotions
      SELECT upb.promotion_id,
             upb.budget_data,
             SUBSTR(upb.budget_data, 1, 1) AS has_budget
        FROM ( -- get user promotion budget
               SELECT cpl.promotion_id,
                      pkg_list_recognition_wall.fnc_check_public_recog_elig(p_in_userid, cpl.promotion_id) AS budget_data
                 FROM ( -- get claim promotion list
                        SELECT DISTINCT
                               c.promotion_id
                          FROM gtt_id_list gil,
                               claim c
                         WHERE gil.ref_text_1 = gc_ref_text_claim_id
                           AND gil.id = c.claim_id
                      ) cpl
             ) upb
   )
   SELECT team_id unique_id, sd.*
     FROM 
  (SELECT rd.team_id,
          null celebration_id,
          rd.team_name,
          rd.date_approved,
          rd.giver_first_name,
          rd.giver_last_name,
          rd.giver_avatar,
          rd.submitter_comments,
          rd.submitter_comments_lang_id,
          rd.receiver_count,
          rd.giver_user_id,
          CASE
            WHEN (p_in_Listtype = 'mine') THEN 'N'
            WHEN (rd.receiver_count > 1)  THEN 'N'
            ELSE rd.add_points_elig
          END AS add_points_elig, 
          rd.budget_data,
          CASE
            WHEN (rd.receiver_count = 1 AND rd.add_points_elig ='Y') THEN
               pkg_list_recognition_wall.fnc_get_budget_conv_ratio(rd.team_id, p_in_userid)
            ELSE NULL
          END AS budget_conversion_ratio,
          tlc.likes_count,
          tcc.comments_count,
          rd.promotion_id,
          rd.promotion_name,
          null program_name_cmx_asset_code,
          rd.promotion_type,
          rd.is_include_purl,
          rd.purl_recipient_id,
          rd.allow_public_recog_points,
          rd.public_rec_award_type_fixed,
          rd.public_rec_award_amount_min,
          rd.public_rec_award_amount_max,
          rd.public_rec_award_amount_fixed,
          rd.card_active,
          rd.card_id,
          rd.card_image_name,
          rd.hide_public_recognition,
          rd.is_cumulative,
          rd.video_url,
          rd.video_image_url,
          rd.rec_seq,
          null celeb_img_desc,
          null celeb_img_desc_cmx_asset_code,
          null celeb_msg,
          null celeb_msg_cmx_asset_code,
          null primary_color,
          null secondary_color,
          null program_header,
          null program_header_cmx_asset_code
     FROM ( -- get raw data
            -- team recognitions
            SELECT lrc.team_id,
                   lrc.team_name,
                   lrc.date_approved,
--                   pr.is_include_purl,
                   CASE WHEN pr.is_include_purl = 1 AND pur.purl_recipient_id IS NOT NULL THEN 1 ELSE 0 END is_include_purl,
                   pur.purl_recipient_id,
                   CASE
                     WHEN (pr.is_include_purl = 1) THEN NULL
                     ELSE au_g.user_id
                   END AS giver_user_id,
                   au_g.first_name AS giver_first_name,
                   au_g.last_name AS giver_last_name,
                   pax_g.avatar_original AS giver_avatar,    
                   lrc.submitter_comments,
                   lrc.submitter_comments_lang_id,
                   trc.receiver_count,
                   CASE
                     WHEN (wub.has_budget = 'Y' AND pr.public_rec_audience_type = 'allactivepaxaudience') THEN 'Y'               
                     WHEN (wub.has_budget = 'Y' AND wpa.elig = 'Y') THEN 'Y'
                     ELSE 'N'  
                   END AS add_points_elig,
                   wub.budget_data,
                   pm.promotion_id,
                   pm.promotion_name,
                   pm.promotion_type,
                   pr.allow_public_recog_points,
                   pr.public_rec_award_type_fixed,
                   pr.public_rec_award_amount_min,
                   pr.public_rec_award_amount_max,
                   pr.public_rec_award_amount_fixed,
                   pr.card_active,
                   lrc.card_id,
                   NVL(cd.large_image_name, lrc.own_card_name) AS card_image_name,
                   lrc.hide_public_recognition,
                   0 AS is_cumulative,
                   lrc.card_video_url AS video_url,
                   lrc.card_video_image_url AS video_image_url,
                   lrc.rec_seq
              FROM ( -- get latest recognition claims
                     SELECT trc.*
                       FROM ( -- get team recognition claims
                              SELECT gil.rec_seq,
                                     rc.team_id,
                                     NULL AS team_name,
                                     rc.submitter_comments,
                                     rc.submitter_comments_lang_id,
                                     rc.card_id,
                                     rc.own_card_name,
                                     rc.hide_public_recognition,
                                     rc.card_video_url,
                                     rc.card_video_image_url,
                                     c.claim_id,
                                     c.promotion_id,
                                     c.submitter_id,
                                     ci.date_approved,
                                     -- sequence by approver date
                                     ROW_NUMBER() OVER (PARTITION BY rc.team_id ORDER BY ci.date_approved DESC) AS approver_seq
                                FROM gtt_id_list gil,
                                     recognition_claim rc,
                                     claim c,
                                     claim_item ci,
                                     claim_recipient cr,
                                     promo_recognition pr
                               WHERE gil.ref_text_1 = gc_ref_text_claim_id
                                 AND gil.id = rc.claim_id
                                 AND rc.claim_id = c.claim_id
                                 AND rc.claim_id = ci.claim_id
                                 AND ci.claim_item_id = cr.claim_item_id
                                 AND pr.promotion_id = c.promotion_id
                                 AND pr.include_celebrations = 0
                                 AND pr.is_include_purl = 0
                                 AND ci.approval_status_type = gc_approval_status_approved
                                 AND NVL(cr.notification_date, SYSDATE - 1) < SYSDATE
                            ) trc
                      WHERE trc.approver_seq = 1
                   ) lrc,
                   promo_recognition pr,
                   promotion pm,
                   application_user au_g,    -- giver
                   participant pax_g,        -- giver
                   card cd,
                   purl_recipient pur,
                   ( -- get team receiver counts
                     SELECT rc.team_id,
                            COUNT(rc_t.ROWID) AS receiver_count
                       FROM gtt_id_list gil,
                            recognition_claim rc,
                            recognition_claim rc_t,   -- team
                            claim_item ci,
                            claim_recipient cr,
                            participant pax,
                            claim c,
                            promo_recognition pr
                      WHERE gil.ref_text_1 = gc_ref_text_claim_id
                        AND gil.id = rc.claim_id
                        AND rc.team_id = rc_t.team_id
                        AND rc_t.claim_id = ci.claim_id
                        AND ci.claim_id = c.claim_id
                        AND pr.promotion_id = c.promotion_id
                        AND pr.include_celebrations = 0
                        AND pr.is_include_purl = 0
                        AND ci.approval_status_type = gc_approval_status_approved
                        AND ci.claim_item_id = cr.claim_item_id
                        AND cr.participant_id = pax.user_id
                        AND (p_in_Listtype = 'mine' OR pax.allow_public_recognition = 1)
                      GROUP BY rc.team_id
                   ) trc,
                   w_promo_audience wpa,
                   w_user_budget wub
             WHERE lrc.promotion_id = pr.promotion_id
               AND pr.promotion_id = pm.promotion_id
               AND pr.include_celebrations = 0
               AND pr.is_include_purl = 0
               AND lrc.submitter_id = au_g.user_id
               AND lrc.submitter_id = pax_g.user_id
               AND lrc.team_id = trc.team_id
               AND lrc.card_id = cd.card_id (+)
               AND lrc.claim_id = pur.claim_id (+)
               AND lrc.promotion_id = wpa.promotion_id (+)         
               AND lrc.promotion_id = wub.promotion_id (+)
             UNION ALL
            -- team nominations
            SELECT lnc.team_id,
                   lnc.team_name,
                   lnc.date_approved,
                   0 AS is_include_purl,
                   NULL AS purl_recipient_id,
                   au_g.user_id AS giver_user_id,
                   au_g.first_name AS giver_first_name,
                   au_g.last_name AS giver_last_name,
                   pax_g.avatar_original AS giver_avatar,
                   CASE
                     WHEN lnc.claim_group_id IS NOT NULL THEN NULL
                     ELSE NVL(lnc.submitter_comments, cf.submitter_comments)
                   END AS submitter_comments,
                   lnc.submitter_comments_lang_id,
                   CASE
                     WHEN lnc.claim_group_id IS NOT NULL THEN 1
                     ELSE trc.receiver_count
                   END AS receiver_count,
                   CASE
                     WHEN (wub.has_budget = 'Y' AND pn.public_rec_audience_type = 'allactivepaxaudience') THEN 'Y'               
                     WHEN (wub.has_budget = 'Y' AND wpa.elig = 'Y') THEN 'Y' 
                     ELSE 'N' 
                   END AS add_points_elig,
                   wub.budget_data,
                   pm.promotion_id,
                   pm.promotion_name,
                   pm.promotion_type,
                   pn.allow_public_recog_points, 
                   pn.public_rec_award_type_fixed,
                   pn.public_rec_award_amount_min,
                   pn.public_rec_award_amount_max,
                   pn.public_rec_award_amount_fixed, 
                   pn.card_active,
                   lnc.card_id,
                   NVL(cd.large_image_name, lnc.own_card_name) AS card_image_name,
                   lnc.hide_public_recognition,
                   CASE
                     WHEN lnc.claim_group_id IS NOT NULL THEN 1
                     ELSE 0
                   END AS is_cumulative,
                   lnc.card_video_url AS video_url,
                   lnc.card_video_image_url video_image_url,
                   lnc.rec_seq
              FROM ( -- get latest nomination claims
                     SELECT tnc.*
                       FROM ( -- get team nomination claims
                              SELECT gil.rec_seq,
                                     nc.team_id,
                                     nc.team_name,
                                     nc.submitter_comments,
                                     nc.submitter_comments_lang_id,
                                     nc.card_id,
                                     nc.own_card_name,
                                     nc.hide_public_recognition,
                                     nc.card_video_url,
                                     nc.card_video_image_url,
                                     c.claim_id,
                                     c.claim_group_id,
                                     c.promotion_id,
                                     c.submitter_id,
                                     cia.date_approved,
                                     -- sequence by approver date
                                     ROW_NUMBER() OVER (PARTITION BY nc.team_id ORDER BY cia.date_approved DESC) AS approver_seq
                                FROM gtt_id_list gil,
                                     nomination_claim nc,
                                     claim c,
                                     claim_item ci,
                                     claim_item_approver cia,
                                     claim_group cg
                               WHERE gil.ref_text_1 = gc_ref_text_claim_id
                                 AND gil.id = nc.claim_id
                                 AND nc.claim_id = c.claim_id
                                 AND nc.claim_id = ci.claim_id  
                                 AND (  ci.claim_item_id = cia.claim_item_id
                                     OR c.claim_group_id = cia.claim_group_id)
                                 AND cia.approval_status_type = gc_approval_status_winner
                                 AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                                 AND c.claim_group_id = cg.claim_group_id (+)  
                            ) tnc
                      WHERE tnc.approver_seq = 1
                   ) lnc,
                   claim_group cg,
                   promo_nomination pn,
                   promotion pm,
                   application_user au_g,    -- giver
                   participant pax_g,        -- giver
                   card cd,
                   ( -- get team receiver counts
                     SELECT nc.team_id,
                            COUNT(nc_t.ROWID) AS receiver_count
                       FROM gtt_id_list gil,
                            nomination_claim nc,
                            nomination_claim nc_t,    -- team
                            claim c,
                            claim_item ci,
                            claim_item_approver cia,
                            claim_recipient cr,
                            claim_participant cp,
                            participant pax
                      WHERE gil.ref_text_1 = gc_ref_text_claim_id
                        AND gil.id = nc.claim_id
                        AND nc.team_id = nc_t.team_id
                        AND nc_t.claim_id = c.claim_id
                        AND nc_t.claim_id = ci.claim_id
                        AND (  ci.claim_item_id = cia.claim_item_id
                            OR c.claim_group_id = cia.claim_group_id)
                        AND cia.approval_status_type = gc_approval_status_winner
                        AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
                        AND ci.claim_item_id = cr.claim_item_id
                        AND ci.claim_id = cp.claim_id (+)
                        AND NVL(cr.participant_id, cp.participant_id) = pax.user_id
                        AND (p_in_Listtype = 'mine' OR pax.allow_public_recognition = 1)
                      GROUP BY nc.team_id
                   ) trc,
                   ( -- get submitter comments
                     SELECT cc.claim_id,
                            cc.value AS submitter_comments
                       FROM gtt_id_list gil,
                            claim_cfse cc,
                            claim_form_step_element cfse
                      WHERE gil.ref_text_1 = gc_ref_text_claim_id
                        AND gil.id = cc.claim_id
                        AND cc.claim_form_step_element_id = cfse.claim_form_step_element_id
                        AND cfse.why_field = 1
                   ) cf,
                   w_promo_audience wpa,
                   w_user_budget wub
             WHERE lnc.claim_group_id = cg.claim_group_id (+)  
               AND lnc.promotion_id = pn.promotion_id
               AND pn.promotion_id = pm.promotion_id
               AND lnc.submitter_id = au_g.user_id
               AND lnc.submitter_id = pax_g.user_id         
               AND lnc.team_id = trc.team_id (+)
               AND lnc.card_id = cd.card_id (+)
               AND lnc.claim_id = cf.claim_id (+)
               AND lnc.promotion_id = wpa.promotion_id (+)         
               AND lnc.promotion_id = wub.promotion_id (+)
          ) rd,
          ( -- get team like counts
            SELECT gil.ref_nbr_1 AS team_id,
                   COUNT(prl.ROWID) AS likes_count
              FROM gtt_id_list gil,
                   public_recognition_like prl
             WHERE gil.ref_text_1 = gc_ref_text_claim_id
               AND gil.ref_nbr_1 = prl.team_id (+)
             GROUP BY gil.ref_nbr_1
          ) tlc,
          ( -- get team comment counts
            SELECT gil.ref_nbr_1 AS team_id,
                   COUNT(prc.ROWID) AS comments_count
              FROM gtt_id_list gil,
                   public_recognition_comment prc
             WHERE gil.ref_text_1 = gc_ref_text_claim_id
               AND gil.ref_nbr_1 = prc.team_id (+)
             GROUP BY gil.ref_nbr_1
          ) tcc
    WHERE rd.team_id = tlc.team_id
      AND rd.team_id = tcc.team_id
    UNION ALL
   -- team SA records
     SELECT lrc.team_id,
            celebration_id,
            null team_name,
            lrc.award_date date_approved,
            null AS giver_first_name,
            null AS giver_last_name,
            null AS giver_avatar,      
            null submitter_comments,
            null submitter_comments_lang_id,
            receiver_count,
            null giver_user_id,
            null AS add_points_elig,
            null budget_data,
            null budget_conversion_ratio,
            null likes_count,
            null comments_count,
            pm.program_id promotion_id,
            pm.program_name promotion_name,
            pm.program_name_cmx_asset_code,
            pm.program_type promotion_type,
            1 is_include_purl,
            lrc.id purl_recipient_id,
            null allow_public_recog_points,
            null public_rec_award_type_fixed,
            null public_rec_award_amount_min,
            null public_rec_award_amount_max,
            null public_rec_award_amount_fixed,
            null card_active,
            null card_id,
            celeb_img_url AS card_image_name,
            null hide_public_recognition,
            0 AS is_cumulative,
            null AS video_url,
            null AS video_image_url,
            lrc.rec_seq,
            lrc.celeb_img_desc,
            lrc.celeb_img_desc_cmx_asset_code,
            lrc.celeb_msg,
            lrc.celeb_msg_cmx_asset_code,
            pm.primary_color,
            pm.secondary_color,
            pm.program_header,
            pm.program_header_cmx_asset_code
      FROM ( -- get latest SA records
             SELECT trc.*
               FROM ( -- get team SA records
                      SELECT gil.rec_seq,
                             sii.team_id,
                             sii.celebration_id,
                             sii.program_id,
                             sii.award_date,
                             sii.id,
                             pwl.celeb_img_url,
                             pwl.celeb_img_desc_cmx_asset_code,
                             pwl.celeb_msg,
                             pwl.celeb_img_desc,
                             pwl.celeb_msg_cmx_asset_code,
                             -- sequence by award date
                             ROW_NUMBER() OVER (PARTITION BY sii.team_id ORDER BY sii.award_date DESC) AS approver_seq
                        FROM gtt_id_list gil,
                             sa_celebration_info sii,
                             program_award_level pwl,
                             (SELECT ua.country_id,
                                       ua.user_id,
                                       c.country_code 
                                FROM user_address ua, 
                                     country c 
                                WHERE c.country_id=ua.country_id and ua.is_primary=1
                                ) cy --04/29/2019
                       WHERE gil.ref_text_1 = gc_ref_text_claim_id
                         AND gil.id = sii.id
                         AND pwl.program_id = sii.program_id
                         AND pwl.award_level = sii.award_level
                         AND pwl.country = cy.country_code (+)      --04/29/2019   
                         AND sii.recipient_id = cy.user_id (+)      --04/29/2019                                         
                         AND sii.team_id is not null
                    ) trc
              WHERE trc.approver_seq = 1
           ) lrc,
           ( -- get team receiver counts
                     SELECT sii.team_id,
                            COUNT(sii.ROWID) AS receiver_count
                       FROM gtt_id_list gil,
                            sa_celebration_info sii,
                            participant pax
                      WHERE gil.ref_text_1 = gc_ref_text_claim_id
                        AND gil.id = sii.id
                        AND sii.recipient_id = pax.user_id
                        AND NVL(sii.award_date, SYSDATE-1) < SYSDATE
                        AND (p_in_Listtype = 'mine' OR pax.allow_public_recognition = 1)
                      GROUP BY sii.team_id
                   ) trc,
           program pm
     WHERE lrc.program_id = pm.program_id
       AND lrc.team_id = trc.team_id(+)) sd
    ORDER BY sd.rec_seq
      ; 

   v_stage := 'OPEN p_out_result_set_recipients';
   OPEN p_out_result_set_recipients FOR 
   -- SA & Celebrations
   SELECT sii.team_id,
          null claim_id,
          au.user_id AS recvr_user_id,
          au.first_name AS recvr_first_name,
          au.last_name AS recvr_last_name,
          pax.avatar_original AS recvr_avatar,      
          pe.position_type,
          pe.department_type,
          n.name AS orgunit_name,
          pax.is_opt_out_of_awards
     FROM gtt_id_list gil,
          sa_celebration_info sii,
          application_user au,
          participant pax,
          node n,
          user_node un,
          rpt_participant_employer pe
    WHERE gil.ref_text_1 = gc_ref_text_claim_id
      AND gil.ref_nbr_1 = sii.team_id
      AND sii.team_id is not null
      AND sii.recipient_id = au.user_id
      AND au.user_id = pax.user_id
      AND (p_in_Listtype = 'mine' OR pax.allow_public_recognition = 1) 
      AND un.user_id = au.user_id
      AND un.is_primary = 1
      AND un.node_id = n.node_id
      AND au.user_id = pe.user_id (+)
    UNION ALL
   -- recognitions
   SELECT rc.team_id,
          rc.claim_id,
          au.user_id AS recvr_user_id,
          au.first_name AS recvr_first_name,
          au.last_name AS recvr_last_name,
          pax.avatar_original AS recvr_avatar,       
          pe.position_type,
          pe.department_type,
          n.name AS orgunit_name,
          pax.is_opt_out_of_awards
     FROM gtt_id_list gil,
          recognition_claim rc,
          claim c,
          promo_recognition pr,
          claim_item ci,
          claim_recipient cr,
          application_user au,
          participant pax,
          node n,
          user_node un,
          rpt_participant_employer pe
    WHERE gil.ref_text_1 = gc_ref_text_claim_id
      AND gil.ref_nbr_1 = rc.team_id
      AND rc.claim_id = ci.claim_id
      AND rc.claim_id = c.claim_id
      and c.promotion_id = pr.promotion_id
      AND pr.include_celebrations = 0 
      AND pr.is_include_purl = 0
      AND ci.claim_item_id = cr.claim_item_id
      AND cr.participant_id = au.user_id
      AND ci.approval_status_type = gc_approval_status_approved 
      AND au.user_id = pax.user_id
      AND (p_in_Listtype = 'mine' OR pax.allow_public_recognition = 1)
      AND un.user_id = au.user_id
      AND un.is_primary = 1
      AND un.node_id = n.node_id
      AND au.user_id = pe.user_id (+)
    UNION ALL
   -- nominations
   SELECT nc.team_id,
          nc.claim_id,
          au.user_id AS recvr_user_id,
          au.first_name AS recvr_first_name,
          au.last_name AS recvr_last_name,   
          pax.avatar_original AS recvr_avatar,        
          pe.position_type,
          pe.department_type,
          n.name AS orgunit_name,
          pax.is_opt_out_of_awards
     FROM gtt_id_list gil,
          nomination_claim nc,
          claim c,
          claim_item ci,
          claim_item_approver cia,
          claim_recipient cr,
          claim_participant cp,
          application_user au,
          participant pax,
          node n,
          user_node un,
          rpt_participant_employer pe
    WHERE gil.ref_text_1 = gc_ref_text_claim_id
      AND gil.ref_nbr_1 = nc.team_id
      AND nc.claim_id = c.claim_id
      AND nc.claim_id = ci.claim_id
      AND (  ci.claim_item_id = cia.claim_item_id
          OR c.claim_group_id = cia.claim_group_id)
      AND cia.approval_status_type = gc_approval_status_winner
      AND NVL(cia.notification_date, SYSDATE-1) < SYSDATE
      AND ci.claim_item_id = cr.claim_item_id
      AND nc.claim_id = cp.claim_id(+)
      AND NVL(cr.participant_id,cp.participant_id) = au.user_id
      AND au.user_id = pax.user_id
      AND (p_in_Listtype = 'mine' OR pax.allow_public_recognition = 1)
      AND un.user_id = au.user_id
      AND un.is_primary = 1
      AND un.node_id = n.node_id
      AND au.user_id = pe.user_id (+)    
      AND NVL(cr.notification_date, SYSDATE - 1) < SYSDATE  
    ORDER BY team_id DESC,
          claim_id DESC
      ;

   v_stage := 'OPEN p_out_result_set_pub_likes';
   OPEN p_out_result_set_pub_likes FOR
   SELECT prl.team_id,
          max(prl.claim_id) claim_id,  
          au.user_id AS liker_user_id,
          au.first_name AS liker_first_name,
          au.last_name AS liker_last_name                                       
     FROM gtt_id_list gil,
          public_recognition_like prl,
          application_user au
    WHERE gil.ref_text_1 = gc_ref_text_claim_id
      AND gil.ref_nbr_1 = prl.team_id
      AND prl.user_id = au.user_id    
    GROUP BY team_id,
             au.user_id,
             first_name,
             last_name
    ORDER BY prl.team_id DESC,
          claim_id DESC
      ;

   v_stage := 'OPEN p_out_result_set_pub_comments';
   OPEN p_out_result_set_pub_comments FOR
   SELECT prc.team_id,
          max(prc.claim_id) claim_id,  
          prc.comments AS public_comments,
          prc.comments_lang_id AS public_comments_language,
          au.user_id AS commenter_user_id,
          au.first_name AS commenter_first_name,
          au.last_name AS commenter_last_name,
          max(prc.public_recognition_comment_id) public_recognition_comment_id, 
          pax.avatar_original AS avatar_small     
     FROM gtt_id_list gil,
          public_recognition_comment prc,
          application_user au,
          participant pax
    WHERE gil.ref_text_1 = gc_ref_text_claim_id
      AND gil.ref_nbr_1 = prc.team_id
      AND prc.user_id = au.user_id
      AND prc.user_id = pax.user_id
    GROUP BY team_id,
             comments,
             comments_lang_id,
             au.user_id,
             first_name,
             last_name,
             avatar_original
    ORDER BY prc.team_id DESC,
          public_recognition_comment_id ASC  
      ;

   v_stage := 'OPEN p_out_result_set_badge';
   OPEN p_out_result_set_badge FOR
   SELECT t.team_id,
          cav_bn.cms_value AS badge_name,
          cb_pb.earned_image_small AS badge_url
     FROM ( -- get team claims
            SELECT rc.team_id,
                   rc.claim_id
              FROM gtt_id_list gil,
                   recognition_claim rc
             WHERE gil.ref_text_1 = gc_ref_text_claim_id
               AND gil.ref_nbr_1 = rc.team_id
             UNION ALL
            SELECT nc.team_id,
                   nc.claim_id
              FROM gtt_id_list gil,
                   nomination_claim nc
             WHERE gil.ref_text_1 = gc_ref_text_claim_id
               AND gil.ref_nbr_1 = nc.team_id
          ) t,
          participant_badge pb,
          badge_rule br,
          vw_cms_asset_value cav_bn,   -- badge name
          mv_cms_badge cb_pb           -- promotion badge
    WHERE t.claim_id = pb.claim_id
      AND pb.badge_rule_id = br.badge_rule_id
      AND br.badge_name = cav_bn.asset_code (+)
      AND v_locale = cav_bn.locale (+)
      AND gc_cms_ac_promotion_badge = cb_pb.asset_code (+)
      AND br.cm_asset_key = cb_pb.cms_name (+)
      AND v_locale = cb_pb.locale (+)
    ORDER BY t.team_id DESC,
          badge_name
      ;

   -- successful
   p_out_return_code := gc_return_code_success;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := gc_return_code_failure;
      OPEN p_out_result_set FOR SELECT NULL FROM DUAL;
END prc_list_recognition_wall_sa;

END pkg_list_recognition_wall;
/