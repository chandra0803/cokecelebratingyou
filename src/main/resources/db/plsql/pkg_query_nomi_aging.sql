CREATE OR REPLACE PACKAGE pkg_query_nomi_aging
IS
/*********************************************************************
Purpose: Provides nomination status/aging query utilities.

MODIFICATION HISTORY
   Person          Date        Comments
   ------------    ----------  ---------------------------------------
   J Flees         05/17/2016  Initial version  
**********************************************************************/

PROCEDURE prc_summary_aging
( p_in_parent_node_id_list IN VARCHAR2,
  p_in_department_list     IN VARCHAR2,
  p_in_job_type_list       IN VARCHAR2,
  p_in_country_id_list     IN VARCHAR2,
  p_in_promotion_id_list   IN VARCHAR2,
  p_in_nomi_status_list    IN VARCHAR2,
  p_in_approval_round_list IN VARCHAR2,
  p_in_participant_status  IN VARCHAR2,
  p_in_giver_recvr_type    IN VARCHAR2,
  p_in_locale_date_pattern IN VARCHAR2,
  p_in_from_date           IN VARCHAR2,
  p_in_to_date             IN VARCHAR2,
  p_in_rownum_start        IN NUMBER,
  p_in_rownum_end          IN NUMBER,
  p_in_sort_col_name       IN VARCHAR2,
  p_in_sorted_by           IN VARCHAR2,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR,
  p_out_totals_data        OUT SYS_REFCURSOR
);

PROCEDURE prc_nominations_by_status
( p_in_parent_node_id_list IN VARCHAR2,
  p_in_department_list     IN VARCHAR2,
  p_in_job_type_list       IN VARCHAR2,
  p_in_country_id_list     IN VARCHAR2,
  p_in_promotion_id_list   IN VARCHAR2,
  p_in_nomi_status_list    IN VARCHAR2,
  p_in_approval_round_list IN VARCHAR2,
  p_in_participant_status  IN VARCHAR2,
  p_in_giver_recvr_type    IN VARCHAR2,
  p_in_locale_date_pattern IN VARCHAR2,
  p_in_from_date           IN VARCHAR2,
  p_in_to_date             IN VARCHAR2,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR
);

END pkg_query_nomi_aging;

/
CREATE OR REPLACE PACKAGE BODY pkg_query_nomi_aging
IS
/*********************************************************************
Purpose: Provides nomination status/aging query utilities.

MODIFICATION HISTORY
   Person          Date        Comments
   ------------    ----------  ---------------------------------------
   J Flees         05/17/2016  Initial version  
   nagarajs       02/10/2017   Bug 71336 - Nomination Status/Aging Report - Summary Counts for Winner and Non-Winner is same 
   nagarajs       02/15/2017  Bug 71357 - Nomination Status/Aging Report - Team is counting each person rather then 1 Team
   nagarajs       02/22/2017  Return data for expired promotions too 
   Suresh J       01/18/2019  Bug 78444 Chart and Extract not matching for "Pending" status
**********************************************************************/
-- package constants
gc_release_level                 CONSTANT execution_log.release_level%type := 1.0;
gc_pkg_name                      CONSTANT VARCHAR2(30) := UPPER('pkg_query_nomi_aging');

gc_return_code_success           CONSTANT NUMBER := pkg_const.gc_return_code_success;
gc_return_code_failure           CONSTANT NUMBER := pkg_const.gc_return_code_failure;

gc_error                         CONSTANT execution_log.severity%TYPE := pkg_const.gc_error;
gc_debug                         CONSTANT execution_log.severity%TYPE := pkg_const.gc_debug;
gc_warn                          CONSTANT execution_log.severity%TYPE := pkg_const.gc_warn;
gc_info                          CONSTANT execution_log.severity%TYPE := pkg_const.gc_info;

gc_pax_status_active             CONSTANT participant.status%TYPE := pkg_const.gc_pax_status_active;
gc_pax_status_inactive           CONSTANT participant.status%TYPE := pkg_const.gc_pax_status_inactive;

--gc_promotion_status_active       CONSTANT promotion.promotion_status%TYPE := pkg_const.gc_promotion_status_active; --02/22/2017

gc_promotion_type_nomination     CONSTANT promotion.promotion_type%TYPE := pkg_const.gc_promotion_type_nomination;            

gc_approval_status_approved      CONSTANT claim_item_approver.approval_status_type%TYPE := pkg_const.gc_approval_status_approved;
gc_approval_status_more_info     CONSTANT claim_item_approver.approval_status_type%TYPE := pkg_const.gc_approval_status_more_info;
gc_approval_status_non_winner    CONSTANT claim_item_approver.approval_status_type%TYPE := pkg_const.gc_approval_status_non_winner;
gc_approval_status_pending       CONSTANT claim_item_approver.approval_status_type%TYPE := pkg_const.gc_approval_status_pending;
gc_approval_status_winner        CONSTANT claim_item_approver.approval_status_type%TYPE := pkg_const.gc_approval_status_winner;

gc_elg_type_giver                rpt_pax_promo_eligibility.giver_recvr_type%TYPE := pkg_const.gc_elg_type_giver;
gc_elg_type_receiver             rpt_pax_promo_eligibility.giver_recvr_type%TYPE := pkg_const.gc_elg_type_receiver;

gc_null_id                       CONSTANT NUMBER(18) := pkg_const.gc_null_id;
gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;

gc_ref_text_approval_round       CONSTANT gtt_id_list.ref_text_1%TYPE := 'approval_round';
gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';
gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
gc_ref_text_nomination_status    CONSTANT gtt_id_list.ref_text_1%TYPE := 'nomination_status';
gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';

-----------------------
-- Private package processes
-----------------------

-----------------------
-- Public package processes
-----------------------
-- Queries nominations based on input parameters and summerizes the results by promotion
PROCEDURE prc_summary_aging
( p_in_parent_node_id_list IN VARCHAR2,
  p_in_department_list     IN VARCHAR2,
  p_in_job_type_list       IN VARCHAR2,
  p_in_country_id_list     IN VARCHAR2,
  p_in_promotion_id_list   IN VARCHAR2,
  p_in_nomi_status_list    IN VARCHAR2,
  p_in_approval_round_list IN VARCHAR2,
  p_in_participant_status  IN VARCHAR2,
  p_in_giver_recvr_type    IN VARCHAR2,
  p_in_locale_date_pattern IN VARCHAR2,
  p_in_from_date           IN VARCHAR2,
  p_in_to_date             IN VARCHAR2,
  p_in_rownum_start        IN NUMBER,
  p_in_rownum_end          IN NUMBER,
  p_in_sort_col_name       IN VARCHAR2,
  p_in_sorted_by           IN VARCHAR2,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR,
  p_out_totals_data        OUT SYS_REFCURSOR
) IS
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name       CONSTANT execution_log.process_name%TYPE := 'prc_summary_aging';
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_data_sort          VARCHAR2(50);
   v_from_date          DATE;
   v_to_date            DATE;
   v_fetch_count        INTEGER;

   -- build query ref cursor record type
   CURSOR cur_query_ref IS
   SELECT CAST(NULL AS NUMBER) AS promotion_id,
          CAST(NULL AS VARCHAR2(1000)) AS promotion_name,
          CAST(NULL AS VARCHAR2(50)) AS time_period_name,
          CAST(NULL AS NUMBER) AS r1_pending_cnt,
          CAST(NULL AS NUMBER) AS r1_avg_pending_days,
          CAST(NULL AS NUMBER) AS r1_approved_cnt,
          CAST(NULL AS NUMBER) AS r1_winner_cnt,
          CAST(NULL AS NUMBER) AS r1_non_winner_cnt,
          CAST(NULL AS NUMBER) AS r1_more_info_cnt,
          CAST(NULL AS NUMBER) AS r2_pending_cnt,
          CAST(NULL AS NUMBER) AS r2_avg_pending_days,
          CAST(NULL AS NUMBER) AS r2_approved_cnt,
          CAST(NULL AS NUMBER) AS r2_winner_cnt,
          CAST(NULL AS NUMBER) AS r2_non_winner_cnt,
          CAST(NULL AS NUMBER) AS r2_more_info_cnt,
          CAST(NULL AS NUMBER) AS r3_pending_cnt,
          CAST(NULL AS NUMBER) AS r3_avg_pending_days,
          CAST(NULL AS NUMBER) AS r3_approved_cnt,
          CAST(NULL AS NUMBER) AS r3_winner_cnt,
          CAST(NULL AS NUMBER) AS r3_non_winner_cnt,
          CAST(NULL AS NUMBER) AS r3_more_info_cnt,
          CAST(NULL AS NUMBER) AS r4_pending_cnt,
          CAST(NULL AS NUMBER) AS r4_avg_pending_days,
          CAST(NULL AS NUMBER) AS r4_approved_cnt,
          CAST(NULL AS NUMBER) AS r4_winner_cnt,
          CAST(NULL AS NUMBER) AS r4_non_winner_cnt,
          CAST(NULL AS NUMBER) AS r4_more_info_cnt,
          CAST(NULL AS NUMBER) AS r5_pending_cnt,
          CAST(NULL AS NUMBER) AS r5_avg_pending_days,
          CAST(NULL AS NUMBER) AS r5_approved_cnt,
          CAST(NULL AS NUMBER) AS r5_winner_cnt,
          CAST(NULL AS NUMBER) AS r5_non_winner_cnt,
          CAST(NULL AS NUMBER) AS r5_more_info_cnt,
          CAST(NULL AS NUMBER) AS total_records,
          CAST(NULL AS NUMBER) AS rec_seq
     FROM dual;

   rec_query      cur_query_ref%ROWTYPE;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_parent_node_id_list >' || p_in_parent_node_id_list
      || '<, p_in_department_list >' || p_in_department_list
      || '<, p_in_job_type_list >' || p_in_job_type_list
      || '<, p_in_country_id_list >' || p_in_country_id_list
      || '<, p_in_promotion_id_list >' || p_in_promotion_id_list
      || '<, p_in_nomi_status_list >' || p_in_nomi_status_list
      || '<, p_in_approval_round_list >' || p_in_approval_round_list
      || '<, p_in_participant_status >' || p_in_participant_status
      || '<, p_in_giver_recvr_type >' || p_in_giver_recvr_type
      || '<, p_in_locale_date_pattern >' || p_in_locale_date_pattern
      || '<, p_in_from_date >' || p_in_from_date
      || '<, p_in_to_date >' || p_in_to_date
      || '<, p_in_rownum_start >' || p_in_rownum_start      
      || '<, p_in_rownum_end >' || p_in_rownum_end
      || '<, p_in_sort_col_name >' || p_in_sort_col_name
      || '<, p_in_sorted_by >' || p_in_sorted_by
      || '<~';     

   -- set data sort
   IF (LOWER(p_in_sorted_by) = 'desc') THEN
      v_data_sort := LOWER(p_in_sorted_by || '/' || p_in_sort_col_name);
   ELSE
      v_data_sort := LOWER(p_in_sort_col_name);
   END IF;

   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_from_date, p_in_locale_date_pattern);
   v_to_date   := TO_DATE(p_in_to_date,   p_in_locale_date_pattern);

   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parent_node_id_list, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_department_list, gc_search_all_values),  gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_job_type_list, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_country_id_list, gc_search_all_values),   gc_ref_text_country_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotion_id_list, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_nomi_status_list, gc_search_all_values), gc_ref_text_nomination_status);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_approval_round_list, gc_search_all_values),  gc_ref_text_approval_round, 1);

   -- query data
   v_stage := 'OPEN p_out_data';
   OPEN p_out_data FOR
   SELECT sd.*
     FROM ( -- sequence the data
            SELECT rd.*,
                   COUNT(rd.promotion_id) OVER() AS total_records,
                   -- calc record sort order
                   ROW_NUMBER() OVER (ORDER BY
                     -- sort totals record first
                     DECODE(rd.promotion_id, NULL, 0, 99),
                     -- ascending sorts
                     DECODE(v_data_sort, 'promotion_id',        rd.promotion_id),
                     DECODE(v_data_sort, 'promotion_name',      LOWER(rd.promotion_name)),
                     DECODE(v_data_sort, 'time_period_name',    LOWER(rd.time_period_name)),
                     DECODE(v_data_sort, 'r1_pending_cnt',      rd.r1_pending_cnt),
                     DECODE(v_data_sort, 'r1_avg_pending_days', rd.r1_avg_pending_days),
                     DECODE(v_data_sort, 'r1_approved_cnt',     rd.r1_approved_cnt),
                     DECODE(v_data_sort, 'r1_winner_cnt',       rd.r1_winner_cnt),
                     DECODE(v_data_sort, 'r1_non_winner_cnt',   rd.r1_non_winner_cnt),
                     DECODE(v_data_sort, 'r1_more_info_cnt',    rd.r1_more_info_cnt),
                     DECODE(v_data_sort, 'r2_pending_cnt',      rd.r2_pending_cnt),
                     DECODE(v_data_sort, 'r2_avg_pending_days', rd.r2_avg_pending_days),
                     DECODE(v_data_sort, 'r2_approved_cnt',     rd.r2_approved_cnt),
                     DECODE(v_data_sort, 'r2_winner_cnt',       rd.r2_winner_cnt),
                     DECODE(v_data_sort, 'r2_non_winner_cnt',   rd.r2_non_winner_cnt),
                     DECODE(v_data_sort, 'r2_more_info_cnt',    rd.r2_more_info_cnt),
                     DECODE(v_data_sort, 'r3_pending_cnt',      rd.r3_pending_cnt),
                     DECODE(v_data_sort, 'r3_avg_pending_days', rd.r3_avg_pending_days),
                     DECODE(v_data_sort, 'r3_approved_cnt',     rd.r3_approved_cnt),
                     DECODE(v_data_sort, 'r3_winner_cnt',       rd.r3_winner_cnt),
                     DECODE(v_data_sort, 'r3_non_winner_cnt',   rd.r3_non_winner_cnt),
                     DECODE(v_data_sort, 'r3_more_info_cnt',    rd.r3_more_info_cnt),
                     DECODE(v_data_sort, 'r4_pending_cnt',      rd.r4_pending_cnt),
                     DECODE(v_data_sort, 'r4_avg_pending_days', rd.r4_avg_pending_days),
                     DECODE(v_data_sort, 'r4_approved_cnt',     rd.r4_approved_cnt),
                     DECODE(v_data_sort, 'r4_winner_cnt',       rd.r4_winner_cnt),
                     DECODE(v_data_sort, 'r4_non_winner_cnt',   rd.r4_non_winner_cnt),
                     DECODE(v_data_sort, 'r4_more_info_cnt',    rd.r4_more_info_cnt),
                     DECODE(v_data_sort, 'r5_pending_cnt',      rd.r5_pending_cnt),
                     DECODE(v_data_sort, 'r5_avg_pending_days', rd.r5_avg_pending_days),
                     DECODE(v_data_sort, 'r5_approved_cnt',     rd.r5_approved_cnt),
                     DECODE(v_data_sort, 'r5_winner_cnt',       rd.r5_winner_cnt),
                     DECODE(v_data_sort, 'r5_non_winner_cnt',   rd.r5_non_winner_cnt),
                     DECODE(v_data_sort, 'r5_more_info_cnt',    rd.r5_more_info_cnt),
                     -- descending sorts
                     DECODE(v_data_sort, 'desc/promotion_id',        rd.promotion_id) DESC,
                     DECODE(v_data_sort, 'desc/promotion_name',      LOWER(rd.promotion_name)) DESC,
                     DECODE(v_data_sort, 'desc/time_period_name',    LOWER(rd.time_period_name)) DESC,
                     DECODE(v_data_sort, 'desc/r1_pending_cnt',      rd.r1_pending_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r1_avg_pending_days', rd.r1_avg_pending_days) DESC,
                     DECODE(v_data_sort, 'desc/r1_approved_cnt',     rd.r1_approved_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r1_winner_cnt',       rd.r1_winner_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r1_non_winner_cnt',   rd.r1_non_winner_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r1_more_info_cnt',    rd.r1_more_info_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r2_pending_cnt',      rd.r2_pending_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r2_avg_pending_days', rd.r2_avg_pending_days) DESC,
                     DECODE(v_data_sort, 'desc/r2_approved_cnt',     rd.r2_approved_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r2_winner_cnt',       rd.r2_winner_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r2_non_winner_cnt',   rd.r2_non_winner_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r2_more_info_cnt',    rd.r2_more_info_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r3_pending_cnt',      rd.r3_pending_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r3_avg_pending_days', rd.r3_avg_pending_days) DESC,
                     DECODE(v_data_sort, 'desc/r3_approved_cnt',     rd.r3_approved_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r3_winner_cnt',       rd.r3_winner_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r3_non_winner_cnt',   rd.r3_non_winner_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r3_more_info_cnt',    rd.r3_more_info_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r4_pending_cnt',      rd.r4_pending_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r4_avg_pending_days', rd.r4_avg_pending_days) DESC,
                     DECODE(v_data_sort, 'desc/r4_approved_cnt',     rd.r4_approved_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r4_winner_cnt',       rd.r4_winner_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r4_non_winner_cnt',   rd.r4_non_winner_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r4_more_info_cnt',    rd.r4_more_info_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r5_pending_cnt',      rd.r5_pending_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r5_avg_pending_days', rd.r5_avg_pending_days) DESC,
                     DECODE(v_data_sort, 'desc/r5_approved_cnt',     rd.r5_approved_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r5_winner_cnt',       rd.r5_winner_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r5_non_winner_cnt',   rd.r5_non_winner_cnt) DESC,
                     DECODE(v_data_sort, 'desc/r5_more_info_cnt',    rd.r5_more_info_cnt) DESC,
                     -- default sort fields
                     LOWER(rd.promotion_name),
                     rd.promotion_id
                   ) -1 AS rec_seq
              FROM ( -- get raw data
                     SELECT dtl.promotion_id,
                            dtl.promotion_name,
                            dtl.time_period_name,
                            -- round 1 fields
                            NVL(COUNT(
                              CASE -- claim still open at the approval round
                                 WHEN (   --dtl.step_number >= 1 --02/10/2017
                                      --AND 
                                      dtl.claim_approval_round = 1
                                      AND dtl.is_open = 1
                                      AND dtl.r1_approval_status IS NULL ) THEN 1
                              END
                            ), 0) AS r1_pending_cnt,
                            ROUND(NVL(AVG(
                              CASE
                                 WHEN (   --dtl.step_number >= 1 --02/10/2017
                                      --AND 
                                      dtl.claim_approval_round = 1
                                      AND dtl.is_open = 1
                                      AND dtl.r1_approval_status IS NULL ) THEN TRUNC(SYSDATE) - dtl.date_submitted
                              END
                            ), 0), 0) AS r1_avg_pending_days,
                            NVL(COUNT(DECODE(dtl.r1_approval_status, gc_approval_status_approved, 1)), 0) AS r1_approved_cnt,
                            NVL(COUNT(DECODE(dtl.r1_approval_status, gc_approval_status_winner, 1)), 0) AS r1_winner_cnt,
                            NVL(COUNT(
                              CASE -- claim denied or no longer open
                                 WHEN (  (   dtl.r1_approval_status IS NOT NULL
                                         AND dtl.r1_approval_status = gc_approval_status_non_winner) 
                                      OR (   --dtl.step_number >= 1 02/10/2017
                                             dtl.r1_approval_status IS NULL --02/10/2017
                                             AND dtl.claim_approval_round = 1
                                             AND dtl.is_open = 0 )
                                      ) THEN 1
                              END
                            ), 0) AS r1_non_winner_cnt,
                            NVL(COUNT(DECODE(dtl.r1_approval_status, gc_approval_status_more_info, 1)), 0) AS r1_more_info_cnt,
                            -- round 2 fields
                            NVL(COUNT(
                              CASE -- claim still open at the approval round
                                 WHEN (   --dtl.step_number >= 2 --02/10/2017
                                      --AND 
                                      dtl.claim_approval_round = 2
                                      AND dtl.is_open = 1
                                      AND dtl.r1_approval_status = gc_approval_status_winner
                                      AND dtl.r2_approval_status IS NULL ) THEN 1
                              END
                            ), 0) AS r2_pending_cnt,
                            ROUND(NVL(AVG(
                              CASE
                                 WHEN (   --dtl.step_number >= 2 --02/10/2017
                                     -- AND 
                                      dtl.claim_approval_round = 2
                                      AND dtl.is_open = 1
                                      AND dtl.r1_approval_status = gc_approval_status_winner
                                      AND dtl.r2_approval_status IS NULL ) THEN TRUNC(SYSDATE) - dtl.r1_approval_date
                              END
                            ), 0), 0) AS r2_avg_pending_days,
                            NVL(COUNT(DECODE(dtl.r2_approval_status, gc_approval_status_approved, 1)), 0) AS r2_approved_cnt,
                            NVL(COUNT(DECODE(dtl.r2_approval_status, gc_approval_status_winner, 1)), 0) AS r2_winner_cnt,
                            NVL(COUNT(
                              CASE -- claim denied or no longer open
                                 WHEN (  (   dtl.r2_approval_status IS NOT NULL
                                         AND dtl.r2_approval_status = gc_approval_status_non_winner) 
                                      OR (   --dtl.step_number >= 2 --02/10/2017
                                             dtl.r2_approval_status IS NULL  --02/10/2017
                                             AND dtl.claim_approval_round = 2
                                             AND dtl.is_open = 0 )
                                      ) THEN 1
                              END
                            ), 0) AS r2_non_winner_cnt,
                            NVL(COUNT(DECODE(dtl.r2_approval_status, gc_approval_status_more_info, 1)), 0) AS r2_more_info_cnt,
                            -- round 3 fields
                            NVL(COUNT(
                              CASE -- claim still open at the approval round
                                 WHEN (   --dtl.step_number >= 3 --02/10/2017
                                      --AND 
                                      dtl.claim_approval_round = 3
                                      AND dtl.is_open = 1
                                      AND dtl.r2_approval_status = gc_approval_status_winner
                                      AND dtl.r3_approval_status IS NULL ) THEN 1
                              END
                            ), 0) AS r3_pending_cnt,
                            ROUND(NVL(AVG(
                              CASE
                                 WHEN (   --dtl.step_number >= 3 --02/10/2017
                                      --AND 
                                      dtl.claim_approval_round = 3
                                      AND dtl.is_open = 1
                                      AND dtl.r2_approval_status = gc_approval_status_winner
                                      AND dtl.r3_approval_status IS NULL ) THEN TRUNC(SYSDATE) - dtl.r2_approval_date
                              END
                            ), 0), 0) AS r3_avg_pending_days,
                            NVL(COUNT(DECODE(dtl.r3_approval_status, gc_approval_status_approved, 1)), 0) AS r3_approved_cnt,
                            NVL(COUNT(DECODE(dtl.r3_approval_status, gc_approval_status_winner, 1)), 0) AS r3_winner_cnt,
                            NVL(COUNT(
                              CASE -- claim denied or no longer open
                                 WHEN (  (   dtl.r3_approval_status IS NOT NULL
                                         AND dtl.r3_approval_status = gc_approval_status_non_winner) 
                                      OR (   --dtl.step_number >= 3 --02/10/2017
                                             dtl.r3_approval_status IS NULL  --02/10/2017
                                             AND dtl.claim_approval_round = 3
                                             AND dtl.is_open = 0 )
                                      ) THEN 1
                              END
                            ), 0) AS r3_non_winner_cnt,
                            NVL(COUNT(DECODE(dtl.r3_approval_status, gc_approval_status_more_info, 1)), 0) AS r3_more_info_cnt,
                            -- round 4 fields
                            NVL(COUNT(
                              CASE -- claim still open at the approval round
                                 WHEN (   --dtl.step_number >= 4  --02/10/2017
                                      --AND 
                                      dtl.claim_approval_round = 4
                                      AND dtl.is_open = 1
                                      AND dtl.r3_approval_status = gc_approval_status_winner
                                      AND dtl.r4_approval_status IS NULL ) THEN 1
                              END
                            ), 0) AS r4_pending_cnt,
                            ROUND(NVL(AVG(
                              CASE
                                 WHEN (   --dtl.step_number >= 4  --02/10/2017
                                      --AND 
                                      dtl.claim_approval_round = 4
                                      AND dtl.is_open = 1
                                      AND dtl.r3_approval_status = gc_approval_status_winner
                                      AND dtl.r4_approval_status IS NULL ) THEN TRUNC(SYSDATE) - dtl.r3_approval_date
                              END
                            ), 0), 0) AS r4_avg_pending_days,
                            NVL(COUNT(DECODE(dtl.r4_approval_status, gc_approval_status_approved, 1)), 0) AS r4_approved_cnt,
                            NVL(COUNT(DECODE(dtl.r4_approval_status, gc_approval_status_winner, 1)), 0) AS r4_winner_cnt,
                            NVL(COUNT(
                              CASE -- claim denied or no longer open
                                 WHEN (  (   dtl.r4_approval_status IS NOT NULL
                                         AND dtl.r4_approval_status = gc_approval_status_non_winner) 
                                      OR (   --dtl.step_number >= 4  --02/10/2017
                                             dtl.r4_approval_status IS NULL  --02/10/2017
                                             AND dtl.claim_approval_round = 4
                                             AND dtl.is_open = 0 )
                                      ) THEN 1
                              END
                            ), 0) AS r4_non_winner_cnt,
                            NVL(COUNT(DECODE(dtl.r4_approval_status, gc_approval_status_more_info, 1)), 0) AS r4_more_info_cnt,
                            -- round 5 fields
                            NVL(COUNT(
                              CASE -- claim still open at the approval round
                                 WHEN (   --dtl.step_number >= 5 --02/10/2017
                                      --AND 
                                      dtl.claim_approval_round = 5
                                      AND dtl.is_open = 1
                                      AND dtl.r4_approval_status = gc_approval_status_winner
                                      AND dtl.r5_approval_status IS NULL ) THEN 1
                              END
                            ), 0) AS r5_pending_cnt,
                            ROUND(NVL(AVG(
                              CASE
                                 WHEN (   --dtl.step_number >= 5 --02/10/2017
                                      --AND 
                                      dtl.claim_approval_round = 5
                                      AND dtl.is_open = 1
                                      AND dtl.r4_approval_status = gc_approval_status_winner
                                      AND dtl.r5_approval_status IS NULL ) THEN TRUNC(SYSDATE) - dtl.r4_approval_date
                              END
                            ), 0), 0) AS r5_avg_pending_days,
                            NVL(COUNT(DECODE(dtl.r5_approval_status, gc_approval_status_approved, 1)), 0) AS r5_approved_cnt,
                            NVL(COUNT(DECODE(dtl.r5_approval_status, gc_approval_status_winner, 1)), 0) AS r5_winner_cnt,
                            NVL(COUNT(
                              CASE -- claim denied or no longer open
                                 WHEN (  (   dtl.r5_approval_status IS NOT NULL
                                         AND dtl.r5_approval_status = gc_approval_status_non_winner) 
                                      OR (   --dtl.step_number >= 5 --02/10/2017
                                             dtl.r5_approval_status IS NULL --02/10/2017
                                             AND dtl.claim_approval_round = 5
                                             AND dtl.is_open = 0 )
                                      ) THEN 1
                              END
                            ), 0) AS r5_non_winner_cnt,
                            NVL(COUNT(DECODE(dtl.r5_approval_status, gc_approval_status_more_info, 1)), 0) AS r5_more_info_cnt
                       FROM (SELECT dtl.promotion_id,  --02/15/2017 Start
                                    dtl.promotion_name,
                                    dtl.time_period_name,
                                    dtl.claim_approval_round,
                                    dtl.is_open,
                                    dtl.r1_approval_status,
                                    dtl.r2_approval_status,
                                    dtl.r3_approval_status,
                                    dtl.r4_approval_status,
                                    dtl.r5_approval_status,
                                    dtl.r1_approval_date,
                                    dtl.r2_approval_date,
                                    dtl.r3_approval_date,
                                    dtl.r4_approval_date,
                                    dtl.date_submitted,
                                    dtl.claim_id --02/15/2017 End
                                    ,dtl.claim_item_id --01/18/2019
                               FROM rpt_nomination_detail dtl,
                                    rpt_hierarchy_rollup hr,
                                    gtt_id_list gil_hr, -- hierarchy rollup
                                    gtt_id_list gil_al, -- approval level
                                    gtt_id_list gil_c,  -- country
                                    gtt_id_list gil_dt, -- department type
                                    gtt_id_list gil_ns, -- nomination status
                                    gtt_id_list gil_p,  -- promotion
                                    gtt_id_list gil_pt  -- position type
                                 -- restrict by required fields
                              WHERE dtl.date_submitted BETWEEN v_from_date AND v_to_date
                                AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id
                                AND gil_hr.id = hr.node_id
                                AND hr.child_node_id = DECODE(p_in_giver_recvr_type, gc_elg_type_giver, dtl.giver_node_id, dtl.recvr_node_id)
                                AND dtl.claim_id IS NOT NULL
                                --AND dtl.promotion_status = gc_promotion_status_active --02/22/2017
                                 -- restrict by optional fields
                                AND gil_al.ref_text_1 = gc_ref_text_approval_round
                                AND (  gil_al.ref_text_2 = gc_search_all_values
                                    OR gil_al.id = dtl.claim_approval_round )
                                AND gil_ns.ref_text_1 = gc_ref_text_nomination_status
              AND (  gil_ns.ref_text_2 = gc_search_all_values
                  OR gil_ns.ref_text_2 IN ( dtl.claim_item_status ) )  --01/18/2019
--                                AND (  gil_ns.ref_text_2 = gc_search_all_values
--                                    OR gil_ns.ref_text_2 IN (dtl.r1_approval_status,
--                                                             dtl.r2_approval_status,
--                                                             dtl.r3_approval_status,
--                                                             dtl.r4_approval_status,
--                                                             dtl.r5_approval_status
--                                                            ) )
                                AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                                AND (  gil_p.ref_text_2 = gc_search_all_values
                                    OR gil_p.id = dtl.promotion_id )
                                AND gil_c.ref_text_1 = gc_ref_text_country_id
                                AND (  gil_c.ref_text_2 = gc_search_all_values
                                    OR gil_c.id = DECODE(p_in_giver_recvr_type, gc_elg_type_giver, dtl.giver_country_id, dtl.recvr_country_id) ) 
                                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                AND (  gil_dt.ref_text_2 = gc_search_all_values
                                    OR gil_dt.ref_text_2 = DECODE(p_in_giver_recvr_type, gc_elg_type_giver, dtl.giver_department_type, dtl.recvr_department_type) )
                                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                AND (  gil_pt.ref_text_2 = gc_search_all_values
                                    OR gil_pt.ref_text_2 = DECODE(p_in_giver_recvr_type, gc_elg_type_giver, dtl.giver_position_type, dtl.recvr_position_type) )
                                AND (  p_in_participant_status IS NULL
                                    OR p_in_participant_status = DECODE(p_in_giver_recvr_type, gc_elg_type_giver, dtl.giver_pax_status, dtl.recvr_pax_status) )
                              GROUP BY  dtl.promotion_id,  --02/15/2017 Start
                                    dtl.promotion_name,
                                    dtl.time_period_name,
                                    dtl.claim_approval_round,
                                    dtl.is_open,
                                    dtl.r1_approval_status,
                                    dtl.r2_approval_status,
                                    dtl.r3_approval_status,
                                    dtl.r4_approval_status,
                                    dtl.r5_approval_status,
                                    dtl.r1_approval_date,
                                    dtl.r2_approval_date,
                                    dtl.r3_approval_date,
                                    dtl.r4_approval_date,
                                    dtl.date_submitted,
                                    dtl.claim_id --02/15/2017 End
                                    ,dtl.claim_item_id --01/18/2019
                            ) dtl
                      GROUP BY GROUPING SETS
                            ((),
                             (dtl.promotion_id,
                              dtl.promotion_name,
                              dtl.time_period_name )
                            )
                   ) rd
          ) sd
    WHERE (  sd.rec_seq = 0   -- totals record
          OR -- reduce sequenced data set to just the output page's records
             (   sd.rec_seq > p_in_rownum_start
             AND sd.rec_seq < p_in_rownum_end )
          )
    ORDER BY sd.rec_seq
      ;

   -- query totals data
   v_stage := 'FETCH p_out_data totals record';
   FETCH p_out_data INTO rec_query;
   v_fetch_count := p_out_data%ROWCOUNT;

   v_stage := 'OPEN p_out_totals_data';
   OPEN p_out_totals_data FOR
   SELECT rec_query.r1_pending_cnt      AS r1_pending_cnt,
          rec_query.r1_avg_pending_days AS r1_avg_pending_days,
          rec_query.r1_approved_cnt     AS r1_approved_cnt,
          rec_query.r1_winner_cnt       AS r1_winner_cnt,
          rec_query.r1_non_winner_cnt   AS r1_non_winner_cnt,
          rec_query.r1_more_info_cnt    AS r1_more_info_cnt,
          rec_query.r2_pending_cnt      AS r2_pending_cnt,
          rec_query.r2_avg_pending_days AS r2_avg_pending_days,
          rec_query.r2_approved_cnt     AS r2_approved_cnt,
          rec_query.r2_winner_cnt       AS r2_winner_cnt,
          rec_query.r2_non_winner_cnt   AS r2_non_winner_cnt,
          rec_query.r2_more_info_cnt    AS r2_more_info_cnt,
          rec_query.r3_pending_cnt      AS r3_pending_cnt,
          rec_query.r3_avg_pending_days AS r3_avg_pending_days,
          rec_query.r3_approved_cnt     AS r3_approved_cnt,
          rec_query.r3_winner_cnt       AS r3_winner_cnt,
          rec_query.r3_non_winner_cnt   AS r3_non_winner_cnt,
          rec_query.r3_more_info_cnt    AS r3_more_info_cnt,
          rec_query.r4_pending_cnt      AS r4_pending_cnt,
          rec_query.r4_avg_pending_days AS r4_avg_pending_days,
          rec_query.r4_approved_cnt     AS r4_approved_cnt,
          rec_query.r4_winner_cnt       AS r4_winner_cnt,
          rec_query.r4_non_winner_cnt   AS r4_non_winner_cnt,
          rec_query.r4_more_info_cnt    AS r4_more_info_cnt,
          rec_query.r5_pending_cnt      AS r5_pending_cnt,
          rec_query.r5_avg_pending_days AS r5_avg_pending_days,
          rec_query.r5_approved_cnt     AS r5_approved_cnt,
          rec_query.r5_winner_cnt       AS r5_winner_cnt,
          rec_query.r5_non_winner_cnt   AS r5_non_winner_cnt,
          rec_query.r5_more_info_cnt    AS r5_more_info_cnt
     FROM dual
    WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_data NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
      OPEN p_out_data FOR
      SELECT CAST(NULL AS NUMBER) AS promotion_id,
             CAST(NULL AS VARCHAR2(1000)) AS promotion_name,
             CAST(NULL AS VARCHAR2(50)) AS time_period_name,
             CAST(NULL AS NUMBER) AS r1_pending_cnt,
             CAST(NULL AS NUMBER) AS r1_avg_pending_days,
             CAST(NULL AS NUMBER) AS r1_winner_cnt,
             CAST(NULL AS NUMBER) AS r1_non_winner_cnt,
             CAST(NULL AS NUMBER) AS r2_pending_cnt,
             CAST(NULL AS NUMBER) AS r2_avg_pending_days,
             CAST(NULL AS NUMBER) AS r2_winner_cnt,
             CAST(NULL AS NUMBER) AS r2_non_winner_cnt,
             CAST(NULL AS NUMBER) AS r3_pending_cnt,
             CAST(NULL AS NUMBER) AS r3_avg_pending_days,
             CAST(NULL AS NUMBER) AS r3_winner_cnt,
             CAST(NULL AS NUMBER) AS r3_non_winner_cnt,
             CAST(NULL AS NUMBER) AS r4_pending_cnt,
             CAST(NULL AS NUMBER) AS r4_avg_pending_days,
             CAST(NULL AS NUMBER) AS r4_winner_cnt,
             CAST(NULL AS NUMBER) AS r4_non_winner_cnt,
             CAST(NULL AS NUMBER) AS r5_pending_cnt,
             CAST(NULL AS NUMBER) AS r5_avg_pending_days,
             CAST(NULL AS NUMBER) AS r5_winner_cnt,
             CAST(NULL AS NUMBER) AS r5_non_winner_cnt,
             CAST(NULL AS NUMBER) AS total_records,
             CAST(NULL AS NUMBER) AS rec_seq
        FROM dual
       WHERE 0=1;
   END IF;

   -- successful
   p_out_return_code := gc_return_code_success;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := gc_return_code_failure;
      OPEN p_out_data FOR SELECT NULL FROM dual;
      OPEN p_out_totals_data FOR SELECT NULL FROM dual;

END prc_summary_aging;

PROCEDURE prc_nominations_by_status
( p_in_parent_node_id_list IN VARCHAR2,
  p_in_department_list     IN VARCHAR2,
  p_in_job_type_list       IN VARCHAR2,
  p_in_country_id_list     IN VARCHAR2,
  p_in_promotion_id_list   IN VARCHAR2,
  p_in_nomi_status_list    IN VARCHAR2,
  p_in_approval_round_list IN VARCHAR2,
  p_in_participant_status  IN VARCHAR2,
  p_in_giver_recvr_type    IN VARCHAR2,
  p_in_locale_date_pattern IN VARCHAR2,
  p_in_from_date           IN VARCHAR2,
  p_in_to_date             IN VARCHAR2,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR
) IS
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name       CONSTANT execution_log.process_name%TYPE := 'prc_nominations_by_status';
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_from_date          DATE;
   v_to_date            DATE;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_parent_node_id_list >' || p_in_parent_node_id_list
      || '<, p_in_department_list >' || p_in_department_list
      || '<, p_in_job_type_list >' || p_in_job_type_list
      || '<, p_in_country_id_list >' || p_in_country_id_list
      || '<, p_in_promotion_id_list >' || p_in_promotion_id_list
      || '<, p_in_nomi_status_list >' || p_in_nomi_status_list
      || '<, p_in_approval_round_list >' || p_in_approval_round_list
      || '<, p_in_participant_status >' || p_in_participant_status
      || '<, p_in_giver_recvr_type >' || p_in_giver_recvr_type
      || '<, p_in_locale_date_pattern >' || p_in_locale_date_pattern
      || '<, p_in_from_date >' || p_in_from_date
      || '<, p_in_to_date >' || p_in_to_date
      || '<~';     

   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_from_date, p_in_locale_date_pattern);
   v_to_date   := TO_DATE(p_in_to_date,   p_in_locale_date_pattern);

   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parent_node_id_list, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_department_list, gc_search_all_values),  gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_job_type_list, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_country_id_list, gc_search_all_values),   gc_ref_text_country_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotion_id_list, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_nomi_status_list, gc_search_all_values), gc_ref_text_nomination_status);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_approval_round_list, gc_search_all_values),  gc_ref_text_approval_round, 1);

   -- query data
   v_stage := 'OPEN p_out_data';
   OPEN p_out_data FOR
   SELECT dtl.claim_item_status,
          COUNT(*) AS nomination_cnt
     FROM (SELECT dtl.claim_id,   --02/15;2017
                  dtl.claim_item_status,  --02/15;2017
                  dtl.claim_item_id  --01/18/2019
             FROM rpt_nomination_detail dtl,
                  rpt_hierarchy_rollup hr,
                  gtt_id_list gil_hr, -- hierarchy rollup
                  gtt_id_list gil_al, -- approval level
                  gtt_id_list gil_c,  -- country
                  gtt_id_list gil_dt, -- department type
                  gtt_id_list gil_ns, -- nomination status
                  gtt_id_list gil_p,  -- promotion
                  gtt_id_list gil_pt  -- position type
               -- restrict by required fields
            WHERE dtl.date_submitted BETWEEN v_from_date AND v_to_date
              AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id
              AND gil_hr.id = hr.node_id
              AND hr.child_node_id = DECODE(p_in_giver_recvr_type, gc_elg_type_giver, dtl.giver_node_id, dtl.recvr_node_id)
              AND dtl.claim_id IS NOT NULL
              --AND dtl.promotion_status = gc_promotion_status_active --02/22/2017
               -- restrict by optional fields
              AND gil_al.ref_text_1 = gc_ref_text_approval_round
              AND (  gil_al.ref_text_2 = gc_search_all_values
                  OR gil_al.id = dtl.claim_approval_round )
              AND gil_ns.ref_text_1 = gc_ref_text_nomination_status
              AND (  gil_ns.ref_text_2 = gc_search_all_values
                  OR gil_ns.ref_text_2 IN ( dtl.claim_item_status ) )  --01/18/2019
--              AND (  gil_ns.ref_text_2 = gc_search_all_values
--                  OR gil_ns.ref_text_2 IN (dtl.r1_approval_status,
--                                           dtl.r2_approval_status,
--                                           dtl.r3_approval_status,
--                                           dtl.r4_approval_status,
--                                           dtl.r5_approval_status
--                                          ) )
              AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = dtl.promotion_id )
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND gil_c.ref_text_1 = gc_ref_text_country_id
              AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = DECODE(p_in_giver_recvr_type, gc_elg_type_giver, dtl.giver_country_id, dtl.recvr_country_id) ) 
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = DECODE(p_in_giver_recvr_type, gc_elg_type_giver, dtl.giver_department_type, dtl.recvr_department_type) )
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                  OR gil_pt.ref_text_2 = DECODE(p_in_giver_recvr_type, gc_elg_type_giver, dtl.giver_position_type, dtl.recvr_position_type) )
              AND (  p_in_participant_status IS NULL
                  OR p_in_participant_status = DECODE(p_in_giver_recvr_type, gc_elg_type_giver, dtl.giver_pax_status, dtl.recvr_pax_status) )
            GROUP BY dtl.claim_id,   --02/15;2017
                  dtl.claim_item_status,  --02/15;2017
                  dtl.claim_item_id  --01/18/2019
          )dtl
    GROUP BY dtl.claim_item_status
    ORDER BY DECODE(dtl.claim_item_status,
               gc_approval_status_pending,    1,
               gc_approval_status_approved,   2,
               gc_approval_status_winner,     3,
               gc_approval_status_non_winner, 4,
               gc_approval_status_more_info,  5,
               6)
      ;

   -- successful
   p_out_return_code := gc_return_code_success;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := gc_return_code_failure;
      OPEN p_out_data FOR SELECT NULL FROM dual;

END prc_nominations_by_status;

END pkg_query_nomi_aging;
/
