CREATE OR REPLACE PACKAGE pkg_query_nomi_given
IS
/*********************************************************************
Purpose: Provides nomination nominator query utilities.

MODIFICATION HISTORY
   Person          Date        Comments
   ------------    ----------  ---------------------------------------
   J Flees         05/27/2016  Initial version  
**********************************************************************/

PROCEDURE prc_summary_nominator
( p_in_parent_node_id_list IN VARCHAR2,
  p_in_department_list     IN VARCHAR2,
  p_in_job_type_list       IN VARCHAR2,
  p_in_country_id_list     IN VARCHAR2,
  p_in_promotion_id_list   IN VARCHAR2,
  p_in_participant_status  IN VARCHAR2,
  p_in_locale_date_pattern IN VARCHAR2,
  p_in_from_date           IN VARCHAR2,
  p_in_to_date             IN VARCHAR2,
  p_in_locale              IN VARCHAR2,
  p_in_viewer_user_id      IN NUMBER, --04/09/2019
  p_in_rownum_start        IN NUMBER,
  p_in_rownum_end          IN NUMBER,
  p_in_sort_col_name       IN VARCHAR2,
  p_in_sorted_by           IN VARCHAR2,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR,
  p_out_totals_data        OUT SYS_REFCURSOR
);

PROCEDURE prc_nomination_nominator
( p_in_user_id             IN NUMBER,
  p_in_locale_date_pattern IN VARCHAR2,
  p_in_from_date           IN VARCHAR2,
  p_in_to_date             IN VARCHAR2,
  p_in_promotion_id_list   IN VARCHAR2,
  p_in_locale              IN VARCHAR2,
  p_in_rownum_start        IN NUMBER,
  p_in_rownum_end          IN NUMBER,
  p_in_sort_col_name       IN VARCHAR2,
  p_in_sorted_by           IN VARCHAR2,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR,
  p_out_totals_data        OUT SYS_REFCURSOR
);

PROCEDURE prc_top_nominator
( p_in_user_id             IN NUMBER, --03/14/2017 
  p_in_parent_node_id_list IN VARCHAR2,
  p_in_department_list     IN VARCHAR2,
  p_in_job_type_list       IN VARCHAR2,
  p_in_country_id_list     IN VARCHAR2,
  p_in_promotion_id_list   IN VARCHAR2,
  p_in_participant_status  IN VARCHAR2,
  p_in_locale_date_pattern IN VARCHAR2,
  p_in_from_date           IN VARCHAR2,
  p_in_to_date             IN VARCHAR2,
  p_in_top_n_count         IN NUMBER,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR
);

END pkg_query_nomi_given;
/
CREATE OR REPLACE PACKAGE BODY pkg_query_nomi_given
IS
/*********************************************************************
Purpose: Provides nomination nominator query utilities.

MODIFICATION HISTORY
   Person          Date        Comments
   ------------    ----------  ---------------------------------------
   J Flees         05/27/2016  Initial version 
   nagarajs        02/22/2017  Return data for expired promotions too 
**********************************************************************/
-- package constants
gc_release_level                 CONSTANT execution_log.release_level%type := 1.0;
gc_pkg_name                      CONSTANT VARCHAR2(30) := UPPER('pkg_query_nomi_given');

gc_return_code_success           CONSTANT NUMBER := pkg_const.gc_return_code_success;
gc_return_code_failure           CONSTANT NUMBER := pkg_const.gc_return_code_failure;

gc_error                         CONSTANT execution_log.severity%TYPE := pkg_const.gc_error;
gc_debug                         CONSTANT execution_log.severity%TYPE := pkg_const.gc_debug;
gc_warn                          CONSTANT execution_log.severity%TYPE := pkg_const.gc_warn;
gc_info                          CONSTANT execution_log.severity%TYPE := pkg_const.gc_info;

gc_pax_status_active             CONSTANT participant.status%TYPE := pkg_const.gc_pax_status_active;
--gc_promotion_status_active       CONSTANT promotion.promotion_status%TYPE := pkg_const.gc_promotion_status_active; --02/22/2017

gc_award_type_other              CONSTANT promotion.award_type%TYPE := pkg_const.gc_award_type_other;

gc_approval_status_winner        CONSTANT claim_item_approver.approval_status_type%TYPE := pkg_const.gc_approval_status_winner;

gc_cms_key_fld_promotion_name    CONSTANT cms_content_data.key%TYPE := pkg_const.gc_cms_key_fld_promotion_name; 

gc_default_top_n_count           CONSTANT NUMBER(18) := pkg_const.gc_default_top_n_count;
gc_null_id                       CONSTANT NUMBER(18) := pkg_const.gc_null_id;
gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;

gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';
gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';

-----------------------
-- Private package processes
-----------------------

-----------------------
-- Public package processes
-----------------------
-- Queries nomination nominators based on input parameters and summerizes the results by nominator
PROCEDURE prc_summary_nominator
( p_in_parent_node_id_list IN VARCHAR2,
  p_in_department_list     IN VARCHAR2,
  p_in_job_type_list       IN VARCHAR2,
  p_in_country_id_list     IN VARCHAR2,
  p_in_promotion_id_list   IN VARCHAR2,
  p_in_participant_status  IN VARCHAR2,
  p_in_locale_date_pattern IN VARCHAR2,
  p_in_from_date           IN VARCHAR2,
  p_in_to_date             IN VARCHAR2,
  p_in_locale              IN VARCHAR2,
  p_in_viewer_user_id      IN NUMBER, --04/09/2019
  p_in_rownum_start        IN NUMBER,
  p_in_rownum_end          IN NUMBER,
  p_in_sort_col_name       IN VARCHAR2,
  p_in_sorted_by           IN VARCHAR2,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR,
  p_out_totals_data        OUT SYS_REFCURSOR
) IS
/*********************************************************************
Purpose: Provides nomination nominator query utilities.

MODIFICATION HISTORY
   Person          Date        Comments
   ------------    ----------  ---------------------------------------
   J Flees         05/27/2016  Initial version  
   nagarajs        06/30/2016  Bug 67230 - Inactive participants is not showing up while filtering by "Show all" 
                               in participant status option under Nomination Submissions - List of Nominators report
  nagarajs        01/18/2017  Bug 70977 - Nominations List of Nominators: Showing Awarded Amounts for Pending Nominees 
 nagarajs         02/07/2017  For nominator report Team nomination should be counted as 1 for submitted count column
DeepakrajS      04/09/2019 BUG 75188 Added logic to display awards in respective logged in user currency code                        
**********************************************************************/

   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name       CONSTANT execution_log.process_name%TYPE := 'prc_summary_nominator';
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_locale             os_propertyset.string_val%TYPE;
   v_data_sort          VARCHAR2(50);
   v_from_date          DATE;
   v_to_date            DATE;
   v_fetch_count        INTEGER;
   v_cash_currency_value   cash_currency_current.bpom_entered_rate%TYPE; --04/09/2019
   v_budget_media_value    NUMBER(12,4); --04/09/2019
   v_in_pax_countryratio   country.budget_media_value%TYPE; --04/09/2019

   CURSOR cur_query_ref IS
   SELECT CAST(NULL AS NUMBER) AS giver_user_id,
          CAST(NULL AS VARCHAR2(100)) AS nominator,
          CAST(NULL AS VARCHAR2(300)) AS giver_country_name,
          CAST(NULL AS VARCHAR2(30)) AS giver_node_name,
          CAST(NULL AS NUMBER) AS submitted_cnt,
          CAST(NULL AS NUMBER) AS points_issued,
          CAST(NULL AS NUMBER) AS cash_issued,
          CAST(NULL AS NUMBER) AS other_qty_issued,
          CAST(NULL AS NUMBER) AS other_amt_issued,
          CAST(NULL AS NUMBER) AS promotion_id,
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
      || '<, p_in_participant_status >' || p_in_participant_status
      || '<, p_in_locale_date_pattern >' || p_in_locale_date_pattern
      || '<, p_in_from_date >' || p_in_from_date
      || '<, p_in_to_date >' || p_in_to_date
      || '<, p_in_locale >' || p_in_locale
      || '<, p_in_rownum_start >' || p_in_rownum_start      
      || '<, p_in_rownum_end >' || p_in_rownum_end
      || '<, p_in_sort_col_name >' || p_in_sort_col_name
      || '<, p_in_sorted_by >' || p_in_sorted_by
      || '<~';     

   -- ensure locale has a value
   v_locale := NVL(p_in_locale, pkg_report_common.f_get_default_locale);

   -- set data sort
   IF (LOWER(p_in_sorted_by) = 'desc') THEN
      v_data_sort := LOWER(p_in_sorted_by || '/' || p_in_sort_col_name);
   ELSE
      v_data_sort := LOWER(p_in_sort_col_name);
   END IF;

   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_from_date, p_in_locale_date_pattern);
   v_to_date   := TO_DATE(p_in_to_date,   p_in_locale_date_pattern);
  

  --Added on 04/09/2019
   v_stage := 'Get currency value';
     BEGIN    
        SELECT ccc.bpom_entered_rate
          INTO v_cash_currency_value
          FROM user_address ua,
               country c,
               cash_currency_current ccc 
         WHERE ua.user_id = p_in_viewer_user_id
           AND is_primary = 1
           AND ua.country_id = c.country_id
           AND c.currency_code = ccc.to_cur;
     EXCEPTION
        WHEN OTHERS THEN
          v_cash_currency_value := 1;
     END;
     
     --Added on 04/09/2019
        v_stage := 'Get Logged in User Media value';
         BEGIN
             SELECT c.BUDGET_MEDIA_VALUE
               INTO v_in_pax_countryratio
               FROM user_address ua, country c
              WHERE ua.user_id = p_in_viewer_user_id 
              AND ua.country_id = c.country_id;
                                                                               
              SELECT (budget_media_value / v_in_pax_countryratio)
               INTO v_budget_media_value  
               FROM country c   
              WHERE c.cm_asset_code ='country_data.country.usa'; 
         EXCEPTION WHEN OTHERS THEN
            v_budget_media_value := 1;                        
         END; 
         
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parent_node_id_list, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_department_list, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_job_type_list, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_country_id_list, gc_search_all_values), gc_ref_text_country_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotion_id_list, gc_search_all_values), gc_ref_text_promotion_id, 1);

   -- query data
   v_stage := 'OPEN p_out_data';
   OPEN p_out_data FOR
   SELECT sd.*
     FROM ( -- sequence the data
            SELECT rd.*,
                   COUNT(rd.giver_user_id) OVER() AS total_records,
                   -- calc record sort order
                   ROW_NUMBER() OVER (ORDER BY
                     -- sort totals record first
                     DECODE(rd.giver_user_id, NULL, 0, 99),
                     -- ascending sorts
                     DECODE(v_data_sort, 'giver_user_id',      rd.giver_user_id),
                     DECODE(v_data_sort, 'nominator',          LOWER(rd.nominator)),
                     DECODE(v_data_sort, 'giver_country_name', LOWER(rd.giver_country_name)),
                     DECODE(v_data_sort, 'giver_node_name',    LOWER(rd.giver_node_name)),
                     DECODE(v_data_sort, 'submitted_cnt',      rd.submitted_cnt),
                     DECODE(v_data_sort, 'points_issued',      rd.points_issued),
                     DECODE(v_data_sort, 'cash_issued',        rd.cash_issued),
                     DECODE(v_data_sort, 'other_qty_issued',   rd.other_qty_issued),
                     DECODE(v_data_sort, 'other_amt_issued',   rd.other_amt_issued),
                     -- descending sorts
                     DECODE(v_data_sort, 'desc/giver_user_id',      rd.giver_user_id) DESC,
                     DECODE(v_data_sort, 'desc/nominator',          LOWER(rd.nominator)) DESC,
                     DECODE(v_data_sort, 'desc/giver_country_name', LOWER(rd.giver_country_name)) DESC,
                     DECODE(v_data_sort, 'desc/giver_node_name',    LOWER(rd.giver_node_name)) DESC,
                     DECODE(v_data_sort, 'desc/submitted_cnt',      rd.submitted_cnt) DESC,
                     DECODE(v_data_sort, 'desc/points_issued',      rd.points_issued) DESC,
                     DECODE(v_data_sort, 'desc/cash_issued',        rd.cash_issued) DESC,
                     DECODE(v_data_sort, 'desc/other_qty_issued',   rd.other_qty_issued) DESC,
                     DECODE(v_data_sort, 'desc/other_amt_issued',   rd.other_amt_issued) DESC,
                     -- default sort fields
                     LOWER(rd.nominator),
                     rd.giver_user_id
                   ) -1 AS rec_seq
              FROM ( -- get raw data
                     SELECT dtl.giver_user_id,
                            fnc_format_user_name(dtl.giver_last_name, dtl.giver_first_name) AS nominator,
                            cav_cn.cms_value AS giver_country_name,
                            dtl.giver_node_name,
                            --COUNT(dtl.ROWID) AS submitted_cnt, --02/07/2017
                            COUNT(DISTINCT dtl.team_id)  AS submitted_cnt, --02/07/2017
                            --SUM(CASE WHEN dtl.claim_item_status = gc_approval_status_winner THEN dtl.points_award_amt ELSE 0 END) AS points_issued, --01/18/2017 Added CASE
                            SUM(CASE WHEN dtl.claim_item_status = gc_approval_status_winner THEN ROUND(dtl.points_award_amt * v_budget_media_value,2) ELSE 0 END) AS points_issued, --04/09/2019
                            --SUM(CASE WHEN dtl.claim_item_status = gc_approval_status_winner THEN dtl.cash_award_amt ELSE 0 END)  AS cash_issued, --01/18/2017 Added CASE --04/09/2019
                            SUM(CASE WHEN dtl.claim_item_status = gc_approval_status_winner THEN ROUND(dtl.cash_award_amt * v_cash_currency_value,2) ELSE 0 END)  AS cash_issued, --04/09/2019
                            SUM(
                              CASE WHEN (dtl.r1_award_type = gc_award_type_other AND dtl.claim_item_status = gc_approval_status_winner) THEN 1 ELSE 0 END  --01/18/2017 Replace r1_approval_status start
                            + CASE WHEN (dtl.r2_award_type = gc_award_type_other AND dtl.claim_item_status = gc_approval_status_winner) THEN 1 ELSE 0 END
                            + CASE WHEN (dtl.r3_award_type = gc_award_type_other AND dtl.claim_item_status = gc_approval_status_winner) THEN 1 ELSE 0 END
                            + CASE WHEN (dtl.r4_award_type = gc_award_type_other AND dtl.claim_item_status = gc_approval_status_winner) THEN 1 ELSE 0 END
                            + CASE WHEN (dtl.r5_award_type = gc_award_type_other AND dtl.claim_item_status = gc_approval_status_winner) THEN 1 ELSE 0 END  --01/18/2017 Replace r1_approval_status End
                            ) AS other_qty_issued,
                            --SUM(dtl.other_award_amt) AS other_amt_issued, --04/09/2019
                            SUM(ROUND(dtl.other_award_amt * v_cash_currency_value,2)) AS other_amt_issued, --04/09/2019
                            dtl.promotion_id
                       FROM rpt_nomination_detail dtl,
                            rpt_hierarchy_rollup hr,
                            country c,
                            mv_cms_asset_value cav_cn, -- country name
                            gtt_id_list gil_hr, -- hierarchy rollup
                            gtt_id_list gil_c,  -- country
                            gtt_id_list gil_dt, -- department type
                            gtt_id_list gil_p,  -- promotion
                            gtt_id_list gil_pt  -- position type
                      WHERE dtl.giver_country_id = c.country_id
                        AND c.name_cm_key = cav_cn.key (+)
                        AND c.cm_asset_code = cav_cn.asset_code (+)
                        AND v_locale = cav_cn.locale (+)
                         -- restrict by required fields
                        AND dtl.date_submitted BETWEEN v_from_date AND v_to_date
                        AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id
                        AND gil_hr.id = hr.node_id
                        AND hr.child_node_id = dtl.giver_node_id
                        AND dtl.claim_id IS NOT NULL
                        --AND dtl.promotion_status = gc_promotion_status_active --02/22/2017
                         -- restrict by optional fields
                        AND NVL(p_in_participant_status, dtl.giver_pax_status) = dtl.giver_pax_status --gc_pax_status_active --06/30/2016 
                        AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                        AND (  gil_p.ref_text_2 = gc_search_all_values
                            OR gil_p.id = dtl.promotion_id )
                        AND gil_c.ref_text_1 = gc_ref_text_country_id
                        AND (  gil_c.ref_text_2 = gc_search_all_values
                            OR gil_c.id = dtl.giver_country_id ) 
                        AND gil_dt.ref_text_1 = gc_ref_text_department_type
                        AND (  gil_dt.ref_text_2 = gc_search_all_values
                            OR gil_dt.ref_text_2 = dtl.giver_department_type )
                        AND gil_pt.ref_text_1 = gc_ref_text_position_type
                        AND (  gil_pt.ref_text_2 = gc_search_all_values
                            OR gil_pt.ref_text_2 = dtl.giver_position_type )
                      GROUP BY GROUPING SETS
                            ((),
                             ( dtl.giver_user_id,
                               dtl.giver_first_name,
                               dtl.giver_last_name,
                               cav_cn.cms_value,
                               dtl.giver_node_name,
                               dtl.promotion_id )
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
   SELECT rec_query.submitted_cnt    AS submitted_cnt,
          rec_query.points_issued    AS points_issued,
          rec_query.cash_issued      AS cash_issued,
          rec_query.other_qty_issued AS other_qty_issued,
          rec_query.other_amt_issued AS other_amt_issued
     FROM dual
    WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_data NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
      OPEN p_out_data FOR
      SELECT CAST(NULL AS NUMBER) AS giver_user_id,
             CAST(NULL AS VARCHAR2(100)) AS nominator,
             CAST(NULL AS VARCHAR2(300)) AS giver_country_name,
             CAST(NULL AS VARCHAR2(30)) AS giver_node_name,
             CAST(NULL AS NUMBER) AS submitted_cnt,
             CAST(NULL AS NUMBER) AS points_issued,
             CAST(NULL AS NUMBER) AS cash_issued,
             CAST(NULL AS NUMBER) AS other_qty_issued,
             CAST(NULL AS NUMBER) AS other_amt_issued,
             CAST(NULL AS NUMBER) AS promotion_id,
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

END prc_summary_nominator;

-----------------------
-- Queries the nominations based on the specified user and input parameters
PROCEDURE prc_nomination_nominator
( p_in_user_id             IN NUMBER,
  p_in_locale_date_pattern IN VARCHAR2,
  p_in_from_date           IN VARCHAR2,
  p_in_to_date             IN VARCHAR2,
  p_in_promotion_id_list   IN VARCHAR2,
  p_in_locale              IN VARCHAR2,
  p_in_rownum_start        IN NUMBER,
  p_in_rownum_end          IN NUMBER,
  p_in_sort_col_name       IN VARCHAR2,
  p_in_sorted_by           IN VARCHAR2,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR,
  p_out_totals_data        OUT SYS_REFCURSOR
) IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := 'prc_nomination_nominator';
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_locale             os_propertyset.string_val%TYPE;
   v_data_sort          VARCHAR2(50);
   v_from_date          DATE;
   v_to_date            DATE;
   v_fetch_count        INTEGER;

   CURSOR cur_query_ref IS
   SELECT CAST(NULL AS NUMBER) AS claim_id,
          CAST(NULL AS DATE) AS date_submitted,
          CAST(NULL AS VARCHAR2(100)) AS nominator,
          CAST(NULL AS VARCHAR2(300)) AS giver_country_name,
          CAST(NULL AS VARCHAR2(30)) AS giver_node_name,
          CAST(NULL AS VARCHAR2(30)) AS team_name,
          CAST(NULL AS VARCHAR2(100)) AS nominee,
          CAST(NULL AS VARCHAR2(300)) AS recvr_country_name,
          CAST(NULL AS VARCHAR2(30)) AS recvr_node_name,
          CAST(NULL AS VARCHAR2(1000)) AS promotion_name,
          CAST(NULL AS VARCHAR2(300)) AS claim_item_status,
          CAST(NULL AS NUMBER) AS points_award_amt,
          CAST(NULL AS NUMBER) AS cash_award_amt,
          CAST(NULL AS VARCHAR2(300)) AS other_award_desc,
          CAST(NULL AS NUMBER) AS other_award_amt,
          CAST(NULL AS NUMBER) AS promotion_id,
          CAST(NULL AS NUMBER) AS total_records,
          CAST(NULL AS NUMBER) AS rec_seq
     FROM dual;

   rec_query      cur_query_ref%ROWTYPE;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_locale_date_pattern >' || p_in_locale_date_pattern
      || '<, p_in_from_date >' || p_in_from_date
      || '<, p_in_to_date >' || p_in_to_date
      || '<, p_in_promotion_id_list >' || p_in_promotion_id_list
      || '<, p_in_locale >' || p_in_locale
      || '<, p_in_rownum_start >' || p_in_rownum_start      
      || '<, p_in_rownum_end >' || p_in_rownum_end
      || '<, p_in_sort_col_name >' || p_in_sort_col_name
      || '<, p_in_sorted_by >' || p_in_sorted_by
      || '<~';     

   -- ensure locale has a value
   v_locale := NVL(p_in_locale, pkg_report_common.f_get_default_locale);

   -- set data sort
   IF (LOWER(p_in_sorted_by) = 'desc') THEN
      v_data_sort := LOWER(p_in_sorted_by || '/' || p_in_sort_col_name);
   ELSE
      v_data_sort := LOWER(p_in_sort_col_name);
   END IF;

   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_from_date, p_in_locale_date_pattern);
   v_to_date   := TO_DATE(p_in_to_date,   p_in_locale_date_pattern);

pkg_report_common.p_stage_search_criteria(NVL(p_in_promotion_id_list, gc_search_all_values), gc_ref_text_promotion_id, 1);
   -- query data
   v_stage := 'OPEN p_out_data';
   OPEN p_out_data FOR
   SELECT sd.*
     FROM ( -- sequence the data
            SELECT rd.*,
                   COUNT(rd.claim_id) OVER() AS total_records,
                   -- calc record sort order
                   ROW_NUMBER() OVER (ORDER BY
                     -- sort totals record first
                     DECODE(rd.claim_id, NULL, 0, 99),
                     -- ascending sorts
                     DECODE(v_data_sort, 'claim_id',           rd.claim_id),
                     DECODE(v_data_sort, 'date_submitted',     rd.date_submitted),
                     DECODE(v_data_sort, 'nominator',          LOWER(rd.nominator)),
                     DECODE(v_data_sort, 'giver_country_name', LOWER(rd.giver_country_name)),
                     DECODE(v_data_sort, 'giver_node_name',    LOWER(rd.giver_node_name)),
                     DECODE(v_data_sort, 'team_name',          LOWER(rd.team_name)),
                     DECODE(v_data_sort, 'nominee',            LOWER(rd.nominee)),
                     DECODE(v_data_sort, 'recvr_country_name', LOWER(rd.recvr_country_name)),
                     DECODE(v_data_sort, 'recvr_node_name',    LOWER(rd.recvr_node_name)),
                     DECODE(v_data_sort, 'promotion_name',     LOWER(rd.promotion_name)),
                     DECODE(v_data_sort, 'claim_item_status',  LOWER(rd.claim_item_status)),
                     DECODE(v_data_sort, 'points_award_amt',   rd.points_award_amt),
                     DECODE(v_data_sort, 'cash_award_amt',     rd.cash_award_amt),
                     DECODE(v_data_sort, 'other_award_desc',   rd.other_award_desc),
                     DECODE(v_data_sort, 'other_award_amt',    rd.other_award_amt),
                     -- descending sorts
                     DECODE(v_data_sort, 'desc/claim_id',           rd.claim_id) DESC,
                     DECODE(v_data_sort, 'desc/date_submitted',     rd.date_submitted) DESC,
                     DECODE(v_data_sort, 'desc/nominator',          LOWER(rd.nominator)) DESC,
                     DECODE(v_data_sort, 'desc/giver_country_name', LOWER(rd.giver_country_name)) DESC,
                     DECODE(v_data_sort, 'desc/giver_node_name',    LOWER(rd.giver_node_name)) DESC,
                     DECODE(v_data_sort, 'desc/team_name',          LOWER(rd.team_name)) DESC,
                     DECODE(v_data_sort, 'desc/nominee',            LOWER(rd.nominee)) DESC,
                     DECODE(v_data_sort, 'desc/recvr_country_name', LOWER(rd.recvr_country_name)) DESC,
                     DECODE(v_data_sort, 'desc/recvr_node_name',    LOWER(rd.recvr_node_name)) DESC,
                     DECODE(v_data_sort, 'desc/promotion_name',     LOWER(rd.promotion_name)) DESC,
                     DECODE(v_data_sort, 'desc/claim_item_status',  LOWER(rd.claim_item_status)) DESC,
                     DECODE(v_data_sort, 'desc/points_award_amt',   rd.points_award_amt) DESC,
                     DECODE(v_data_sort, 'desc/cash_award_amt',     rd.cash_award_amt) DESC,
                     DECODE(v_data_sort, 'desc/other_award_desc',   rd.other_award_desc) DESC,
                     DECODE(v_data_sort, 'desc/other_award_amt',    rd.other_award_amt) DESC,
                     -- default sort fields
                     rd.date_submitted,
                     rd.claim_id,
                     rd.promotion_id
                   ) -1 AS rec_seq
              FROM ( -- get raw data
                     SELECT dtl.claim_id,
                            dtl.date_submitted,
                            fnc_format_user_name(dtl.giver_last_name, dtl.giver_first_name) AS nominator,
                            cav_cn_g.cms_value AS giver_country_name,
                            dtl.giver_node_name,
                            dtl.team_name,
                            fnc_format_user_name(dtl.recvr_last_name, dtl.recvr_first_name) AS nominee,
                            cav_cn_r.cms_value AS recvr_country_name,
                            dtl.recvr_node_name,
                            NVL(cav_pn.cms_value, dtl.promotion_name) AS promotion_name,
                            NVL(ccv_as.cms_name, dtl.claim_item_status) AS claim_item_status,
                            SUM(CASE WHEN dtl.claim_item_status = gc_approval_status_winner THEN dtl.points_award_amt ELSE 0 END) AS points_award_amt, --01/18/2017 Added CASE
                            SUM(CASE WHEN dtl.claim_item_status = gc_approval_status_winner THEN dtl.cash_award_amt ELSE 0 END)  AS cash_award_amt, --01/18/2017 Added CASE
                            dtl.other_award_desc,
                            SUM(CASE WHEN dtl.claim_item_status = gc_approval_status_winner THEN dtl.other_award_amt ELSE 0 END)  AS other_award_amt, --01/18/2017 Added CASE
                            dtl.promotion_id
                       FROM rpt_nomination_detail dtl,
                            country c_g,  -- giver country
                            country c_r,  -- receiver country
                            promotion p,
                            mv_cms_asset_value cav_cn_g, -- giver country name
                            mv_cms_asset_value cav_cn_r,  -- receiver country name
                            mv_cms_asset_value cav_pn,   -- promotion name
                            mv_cms_code_value ccv_as, --claim item status
                            gtt_id_list gil_p
                      WHERE dtl.giver_country_id = c_g.country_id
                        AND c_g.name_cm_key = cav_cn_g.key (+)
                        AND c_g.cm_asset_code = cav_cn_g.asset_code (+)
                        AND v_locale = cav_cn_g.locale (+)
                        AND dtl.recvr_country_id = c_r.country_id
                        AND c_r.name_cm_key = cav_cn_r.key (+)
                        AND c_r.cm_asset_code = cav_cn_r.asset_code (+)
                        AND v_locale = cav_cn_r.locale (+)
                        AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                        AND (  gil_p.ref_text_2 = gc_search_all_values
                            OR gil_p.id = dtl.promotion_id )
                        AND dtl.promotion_id              = p.promotion_id
                        AND p.promo_name_asset_code       = cav_pn.asset_code (+)
                        AND gc_cms_key_fld_promotion_name = cav_pn.key (+)
                        AND v_locale                      = cav_pn.locale (+)
                        AND 'picklist.approval.status.items' = ccv_as.asset_code (+)
                        AND dtl.claim_item_status        = ccv_as.cms_code (+)
                        AND v_locale                    = ccv_as.locale (+)
                         -- restrict by required fields
                        AND dtl.giver_user_id = p_in_user_id
                        AND dtl.date_submitted BETWEEN v_from_date AND v_to_date
                        AND dtl.claim_id IS NOT NULL
                        --AND dtl.promotion_status = gc_promotion_status_active --02/22/2017
                   GROUP BY GROUPING SETS
                            ((),
                             ( dtl.claim_id,
                               dtl.date_submitted,
                               dtl.giver_last_name,
                               dtl.giver_first_name,
                               cav_cn_g.cms_value,
                               dtl.giver_node_name,
                               dtl.team_name,
                               dtl.recvr_last_name,
                               dtl.recvr_first_name,
                               cav_cn_r.cms_value,
                               dtl.recvr_node_name,
                               cav_pn.cms_value,
                               dtl.promotion_name,
                               ccv_as.cms_name,
                               dtl.claim_item_status,
                               dtl.other_award_desc,
                               dtl.promotion_id )
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
   SELECT rec_query.points_award_amt AS points_award_amt,
          rec_query.cash_award_amt   AS cash_award_amt,
          rec_query.other_award_amt  AS other_award_amt
     FROM dual
    WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_data NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
      OPEN p_out_data FOR
      SELECT CAST(NULL AS NUMBER) AS claim_id,
             CAST(NULL AS DATE) AS date_submitted,
             CAST(NULL AS VARCHAR2(100)) AS nominator,
             CAST(NULL AS VARCHAR2(300)) AS giver_country_name,
             CAST(NULL AS VARCHAR2(30)) AS giver_node_name,
             CAST(NULL AS VARCHAR2(30)) AS team_name,
             CAST(NULL AS VARCHAR2(100)) AS nominee,
             CAST(NULL AS VARCHAR2(300)) AS recvr_country_name,
             CAST(NULL AS VARCHAR2(30)) AS recvr_node_name,
             CAST(NULL AS VARCHAR2(300)) AS claim_item_status,
             CAST(NULL AS NUMBER) AS points_award_amt,
             CAST(NULL AS NUMBER) AS cash_award_amt,
             CAST(NULL AS VARCHAR2(300)) AS other_award_desc,
             CAST(NULL AS NUMBER) AS other_award_amt,
             CAST(NULL AS NUMBER) AS promotion_id,
             CAST(NULL AS NUMBER) AS total_records,
             CAST(NULL AS NUMBER) AS rec_seq             
        FROM dual
       WHERE 0=1;
   END IF;

   -- successful
   p_out_return_code := gc_return_code_success;
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      p_out_return_code := gc_return_code_failure;
      OPEN p_out_data FOR SELECT NULL FROM dual;
      OPEN p_out_totals_data FOR SELECT NULL FROM dual;

END prc_nomination_nominator;

-----------------------
-- Queries the top N nominators based on input parameters
PROCEDURE prc_top_nominator
( p_in_user_id             IN NUMBER, --03/14/2017
  p_in_parent_node_id_list IN VARCHAR2,
  p_in_department_list     IN VARCHAR2,
  p_in_job_type_list       IN VARCHAR2,
  p_in_country_id_list     IN VARCHAR2,
  p_in_promotion_id_list   IN VARCHAR2,
  p_in_participant_status  IN VARCHAR2,
  p_in_locale_date_pattern IN VARCHAR2,
  p_in_from_date           IN VARCHAR2,
  p_in_to_date             IN VARCHAR2,
  p_in_top_n_count         IN NUMBER,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR
) IS
/*********************************************************************
Purpose: Provides nomination nominator query utilities.

MODIFICATION HISTORY
   Person          Date        Comments
   ------------    ----------  ---------------------------------------
   J Flees         05/27/2016  Initial version  
   nagarajs        06/30/2016 Bug 67230 - Inactive participants is not showing up while filtering by "Show all" 
                              in participant status option under Nomination Submissions - List of Nominators report
   nagarajs       03/14/2017  G6-1936 - List of Nominator and nominee Report - Summary Table export records is not matching with Summary Table records
**********************************************************************/
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name       CONSTANT execution_log.process_name%TYPE := 'prc_top_nominator';
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_from_date          DATE;
   v_to_date            DATE;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_parent_node_id_list >' || p_in_parent_node_id_list
      || '<, p_in_department_list >' || p_in_department_list
      || '<, p_in_job_type_list >' || p_in_job_type_list
      || '<, p_in_country_id_list >' || p_in_country_id_list
      || '<, p_in_promotion_id_list >' || p_in_promotion_id_list
      || '<, p_in_participant_status >' || p_in_participant_status
      || '<, p_in_locale_date_pattern >' || p_in_locale_date_pattern
      || '<, p_in_from_date >' || p_in_from_date
      || '<, p_in_to_date >' || p_in_to_date
      || '<, p_in_top_n_count >' || p_in_top_n_count
      || '<~';     

   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_from_date, p_in_locale_date_pattern);
   v_to_date   := TO_DATE(p_in_to_date,   p_in_locale_date_pattern);

   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parent_node_id_list, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_department_list, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_job_type_list, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_country_id_list, gc_search_all_values), gc_ref_text_country_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotion_id_list, gc_search_all_values), gc_ref_text_promotion_id, 1);

   -- query data
   v_stage := 'OPEN p_out_data';
   OPEN p_out_data FOR
   SELECT sd.nominator,
          sd.submitted_cnt
     FROM ( -- sequence the data
            SELECT rd.*,
                   -- calc record sort order
                   ROW_NUMBER() OVER (ORDER BY rd.submitted_cnt DESC, LOWER(rd.nominator)) AS rec_seq
              FROM ( -- get raw data
                     SELECT fnc_format_user_name(dtl.giver_last_name, dtl.giver_first_name) AS nominator,
                            COUNT(dtl.ROWID) AS submitted_cnt
                       FROM rpt_nomination_detail dtl,
                            rpt_hierarchy_rollup hr,
                            gtt_id_list gil_hr, -- hierarchy rollup
                            gtt_id_list gil_c,  -- country
                            gtt_id_list gil_dt, -- department type
                            gtt_id_list gil_p,  -- promotion
                            gtt_id_list gil_pt  -- position type
                         -- restrict by required fields
                      WHERE dtl.date_submitted BETWEEN v_from_date AND v_to_date
                        AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id
                        AND gil_hr.id = hr.node_id
                        AND hr.child_node_id = dtl.giver_node_id
                        AND dtl.giver_user_id = NVL(p_in_user_id, dtl.giver_user_id) --03/14/2017
                        AND dtl.claim_id IS NOT NULL
                        --AND dtl.promotion_status = gc_promotion_status_active --02/22/2017
                         -- restrict by optional fields
                        AND NVL(p_in_participant_status, dtl.giver_pax_status) = dtl.giver_pax_status --gc_pax_status_active --06/30/2016
                        AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                        AND (  gil_p.ref_text_2 = gc_search_all_values
                            OR gil_p.id = dtl.promotion_id )
                        AND gil_c.ref_text_1 = gc_ref_text_country_id
                        AND (  gil_c.ref_text_2 = gc_search_all_values
                            OR gil_c.id = dtl.giver_country_id ) 
                        AND gil_dt.ref_text_1 = gc_ref_text_department_type
                        AND (  gil_dt.ref_text_2 = gc_search_all_values
                            OR gil_dt.ref_text_2 = dtl.giver_department_type )
                        AND gil_pt.ref_text_1 = gc_ref_text_position_type
                        AND (  gil_pt.ref_text_2 = gc_search_all_values
                            OR gil_pt.ref_text_2 = dtl.giver_position_type )
                      GROUP BY dtl.giver_user_id,
                            dtl.giver_first_name,
                            dtl.giver_last_name
                   ) rd
          ) sd
       -- reduce sequenced data set to just the top N records
    WHERE sd.rec_seq <= NVL(p_in_top_n_count, gc_default_top_n_count)
    ORDER BY sd.rec_seq
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

END prc_top_nominator;

END pkg_query_nomi_given;
/
