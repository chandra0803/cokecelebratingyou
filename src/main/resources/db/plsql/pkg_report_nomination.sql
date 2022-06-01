CREATE OR REPLACE PACKAGE pkg_report_nomination
IS
/*******************************************************************************

   Purpose:  This package contains the functions that Jasper reports will use for
             the nomination reports.  It contains the procedures to populate the
             nomination detail reporting table.  The summary information is handled
             by pkg_report_common.prc_rpt_hierarchy_summary.

   Person         Date        Comments
   -----------    ----------  -----------------------------------------------------
   D Murray       05/05/2006  Initial Creation
   D Murray       05/17/2006  p_rpt_nomination_detail - Added summary report to the 
                              report_refresh_date table because Java code is looking 
                              for it to populate a date/time at the top of iReports. (Bug 12207)
   D Murray       05/19/2006  Not all records should show on reports - changed the
                              functions and detail procedure to have show_on_report_flag.
   Arun S         07/25/2012  G5 Report changes, added new in param to p_rpt_nomination_detail
   chidamba       12/15/2012  G5 Change - filters promotion_status,department,country and promotion 
   J Flees        04/12/2016  G5 Redesign
*******************************************************************************/
   PROCEDURE p_rpt_nomination_detail
   ( p_in_user_id       IN NUMBER,
     p_in_start_date    IN DATE, 
     p_in_end_date      IN DATE, 
     p_out_return_code  OUT NUMBER,
     p_out_err_msg      OUT VARCHAR2
   );

END pkg_report_nomination;
/
CREATE OR REPLACE PACKAGE BODY pkg_report_nomination
IS
/*******************************************************************************

   Purpose:  This package contains the functions that Jasper reports will use for
             the nomination reports.  It contains the procedures to populate the
             nomination detail reporting table.  The summary information is handled
             by pkg_report_common.prc_rpt_hierarchy_summary.

   Person         Date        Comments
   -----------    ----------  -----------------------------------------------------
   D Murray       05/05/2006  Initial Creation
   D Murray       05/17/2006  p_rpt_nomination_detail - Added summary report to the
                              report_refresh_date table because Java code is looking
                              for it to populate a date/time at the top of iReports. (Bug 12207)
   D Murray       05/19/2006  Not all records should show on reports - changed the
                              functions and detail procedure to have show_on_report_flag.
   J Flees        04/12/2016  G5 Redesign
   Sherif Basha   03/17/2017  [JIRA] (G6-1893) Nomination Activity by Organisation - Owner/Manager report - The summary details don't seem to be correct
   Ravi Dhanekula 05/17/2017  G6-2479 Not showing approver info for cummulative noms.
   Chidamba       09/06/2017  G6-2927- fix: Include nomination 'Pend' claim, who have not approved at least 1-level of approval.
   murphyc        01/26/2018  Bug 75388 - add date_modified to p_refresh_new_claim to include claims that were not completed on the same date they were created
*******************************************************************************/
-- package constants
gc_release_level                 CONSTANT execution_log.release_level%type := 2.0;
gc_pkg_name                      CONSTANT VARCHAR2(30) := UPPER('pkg_report_nomination');

gc_return_code_success           CONSTANT NUMBER := pkg_const.gc_return_code_success;
gc_return_code_failure           CONSTANT NUMBER := pkg_const.gc_return_code_failure;

gc_error                         CONSTANT execution_log.severity%TYPE := pkg_const.gc_error;
gc_debug                         CONSTANT execution_log.severity%TYPE := pkg_const.gc_debug;
gc_warn                          CONSTANT execution_log.severity%TYPE := pkg_const.gc_warn;
gc_info                          CONSTANT execution_log.severity%TYPE := pkg_const.gc_info;

gc_award_type_cash               CONSTANT promotion.award_type%TYPE := pkg_const.gc_award_type_cash;
gc_award_type_other              CONSTANT promotion.award_type%TYPE := pkg_const.gc_award_type_other;
gc_award_type_points             CONSTANT promotion.award_type%TYPE := pkg_const.gc_award_type_points;

gc_approval_status_winner        CONSTANT claim_item_approver.approval_status_type%TYPE := pkg_const.gc_approval_status_winner;

gc_journal_status_post           CONSTANT journal.status_type%TYPE := pkg_const.gc_journal_status_post;

gc_pax_status_active             CONSTANT VARCHAR(30) := pkg_const.gc_pax_status_active;
gc_pax_status_inactive           CONSTANT VARCHAR(30) := pkg_const.gc_pax_status_inactive;

gc_promotion_status_active       CONSTANT promotion.promotion_status%TYPE := pkg_const.gc_promotion_status_active;
gc_promotion_status_expired      CONSTANT promotion.promotion_status%TYPE := pkg_const.gc_promotion_status_expired;            

gc_act_disc_sweep                CONSTANT activity.activity_discrim%TYPE := pkg_const.gc_act_disc_sweep;
gc_act_disc_sweeplevel           CONSTANT activity.activity_discrim%TYPE := pkg_const.gc_act_disc_sweeplevel;

-----------------------
-- private processes
-----------------------
-----------------------
-- Adds new sweepstake activity records to the reporting table
PROCEDURE p_refresh_new_sweeps
( p_in_user_id       IN NUMBER,
  p_in_start_date    IN DATE, 
  p_in_end_date      IN DATE, 
  p_in_timestamp     IN DATE
) IS
   c_process_name                CONSTANT execution_log.process_name%type  := UPPER('p_refresh_new_sweeps');
   v_stage                       execution_log.text_line%TYPE;
   v_parm_list                   execution_log.text_line%TYPE;
   v_rec_count                   INTEGER;
BEGIN
   v_stage := 'initialzie variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_start_date >' || TO_CHAR(p_in_start_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_end_date >' || TO_CHAR(p_in_end_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_timestamp >' || TO_CHAR(p_in_timestamp,'MM/DD/YYYY HH24:MI:SS')
      || '<~';

   v_stage := 'MERGE rpt_nomination_detail';
   MERGE INTO rpt_nomination_detail d
   USING (  -- build report data
            SELECT -- nomination fields
                   a.activity_id,
                   a.promotion_id,
                   p.promotion_name,
                   p.promotion_status,
                   p.award_type AS promotion_award_type,
                   TRUNC(a.date_created) AS date_submitted,
                   TRUNC(a.date_created) AS date_approved,
                   0 AS is_open,
                   1 AS sweepstakes_won,
                   -- receiver fields
                   a.user_id AS recvr_user_id,
                   a.node_id AS recvr_node_id,
                   ua_r.country_id AS recvr_country_id,
                   paxe_r.department_type AS recvr_department_type,
                   paxe_r.position_type AS recvr_position_type,
                   DECODE(au_r.is_active, 1, gc_pax_status_active, gc_pax_status_inactive) AS recvr_pax_status,
                   au_r.user_name AS recvr_user_name,
                   INITCAP(au_r.first_name) AS recvr_first_name,
                   INITCAP(TRIM(au_r.middle_name)) AS recvr_middle_name,
                   INITCAP(au_r.last_name) AS recvr_last_name,
                   n_r.name AS recvr_node_name,
                   un_r.role AS recvr_org_role,
                   -- award fields
                   0 AS cash_award_amt,
                   a.quantity AS points_award_amt,
                   0 AS other_award_amt
              FROM activity a,
                   promotion p,
                   promo_nomination pn,
                   -- receiver
                   application_user au_r,
                   user_address ua_r,
                   rpt_participant_employer paxe_r,
                   node n_r,
                   user_node un_r
             WHERE p_in_start_date < a.date_created
               AND a.date_created <= p_in_end_date
               AND a.activity_discrim IN (gc_act_disc_sweep, gc_act_disc_sweeplevel) --10/07/2013
               AND a.promotion_id = p.promotion_id
               AND p.promotion_status IN (gc_promotion_status_active, gc_promotion_status_expired)
               AND p.is_deleted = 0
               AND p.promotion_id = pn.promotion_id
                -- link nominee details
               AND a.user_id = au_r.user_id (+)
               AND a.user_id = ua_r.user_id (+)
               AND 1 = ua_r.is_primary (+)
               AND a.user_id = paxe_r.user_id (+)
               AND a.node_id = n_r.node_id (+)
               AND a.user_id = un_r.user_id (+)
               AND a.user_id = un_r.node_id (+)
               AND 1 = un_r.is_primary (+)
               AND 1 = un_r.status (+)
         ) s
      ON (d.activity_id = s.activity_id)
    WHEN NOT MATCHED THEN
      INSERT
      ( rpt_nomination_detail_id,
        activity_id,
        promotion_id,
        promotion_name,
        promotion_status,
        promotion_award_type,
        date_submitted,
        date_approved,
        is_open,
        sweepstakes_won,
        recvr_user_id,
        recvr_node_id,
        recvr_country_id,
        recvr_department_type,
        recvr_position_type,
        recvr_pax_status,
        recvr_user_name,
        recvr_first_name,
        recvr_middle_name,
        recvr_last_name,
        recvr_node_name,
        recvr_org_role,
        cash_award_amt,
        points_award_amt,
        other_award_amt,
        created_by,
        date_created
      )
      VALUES
      ( rpt_nomination_detail_pk_sq.NEXTVAL,
        s.activity_id,
        s.promotion_id,
        s.promotion_name,
        s.promotion_status,
        s.promotion_award_type,
        s.date_submitted,
        s.date_approved,
        s.is_open,
        s.sweepstakes_won,
        s.recvr_user_id,
        s.recvr_node_id,
        s.recvr_country_id,
        s.recvr_department_type,
        s.recvr_position_type,
        s.recvr_pax_status,
        s.recvr_user_name,
        s.recvr_first_name,
        s.recvr_middle_name,
        s.recvr_last_name,
        s.recvr_node_name,
        s.recvr_org_role,
        s.cash_award_amt,
        s.points_award_amt,
        s.other_award_amt,
        p_in_user_id,   -- created_by
        p_in_timestamp  -- date_created
      );

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ':' || v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END p_refresh_new_sweeps;

-----------------------
-- Adds new nomination claim records to the reporting table
PROCEDURE p_refresh_new_claim
( p_in_user_id       IN NUMBER,
  p_in_start_date    IN DATE, 
  p_in_end_date      IN DATE, 
  p_in_timestamp     IN DATE
) IS
   c_process_name                CONSTANT execution_log.process_name%type  := UPPER('p_refresh_new_claim');
   v_stage                       execution_log.text_line%TYPE;
   v_parm_list                   execution_log.text_line%TYPE;
   v_rec_count                   INTEGER;
BEGIN
   v_stage := 'initialzie variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_start_date >' || TO_CHAR(p_in_start_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_end_date >' || TO_CHAR(p_in_end_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_timestamp >' || TO_CHAR(p_in_timestamp,'MM/DD/YYYY HH24:MI:SS')
      || '<~';

   v_stage := 'MERGE rpt_nomination_detail';
   MERGE INTO rpt_nomination_detail d
   USING (  -- build report data
--            SELECT -- nomination fields    --09/06/2017 G6-2927 commented ***
--                   c.claim_id,
--                   ci.claim_item_id,
--                   c.promotion_id,
--                   p.promotion_name,
--                   p.promotion_status,
--                   TRUNC(c.submission_date) AS date_submitted,
--                   TRUNC(ci.date_approved) AS date_approved,
--                   c.is_open,
--                   nc.step_number,
--                   c.approval_round AS claim_approval_round,
--                   nc.status AS claim_status,
--                   ci.approval_status_type AS claim_item_status,
--                   nc.nomination_time_period_id, 
--                   pntp.time_period_name,
--                   c.claim_group_id,
--                   nc.team_id,
--                   nc.team_name,
--                   -- nominator fields
--                   c.submitter_id AS giver_user_id,
--                   c.node_id AS giver_node_id,
--                   ua_g.country_id AS giver_country_id,
--                   paxe_g.department_type AS giver_department_type,
--                   paxe_g.position_type AS giver_position_type,
--                   DECODE(au_g.is_active, 1, gc_pax_status_active, gc_pax_status_inactive) AS giver_pax_status,
--                   au_g.user_name AS giver_user_name,
--                   INITCAP(au_g.first_name) AS giver_first_name,
--                   INITCAP(TRIM(au_g.middle_name)) AS giver_middle_name,
--                   INITCAP(au_g.last_name) AS giver_last_name,
--                   n_g.name AS giver_node_name,
--                   un_g.role AS giver_org_role,
--                   -- receiver fields
--                   cr.participant_id AS recvr_user_id,
--                   cr.node_id AS recvr_node_id,
--                   ua_r.country_id AS recvr_country_id,
--                   paxe_r.department_type AS recvr_department_type,
--                   paxe_r.position_type AS recvr_position_type,
--                   DECODE(au_r.is_active, 1, gc_pax_status_active, gc_pax_status_inactive) AS recvr_pax_status,
--                   au_r.user_name AS recvr_user_name,
--                   INITCAP(au_r.first_name) AS recvr_first_name,
--                   INITCAP(TRIM(au_r.middle_name)) AS recvr_middle_name,
--                   INITCAP(au_r.last_name) AS recvr_last_name,
--                   n_r.name AS recvr_node_name,
--                   un_r.role AS recvr_org_role,
--                   -- award fields
--                   0 AS cash_award_amt,
--                   0 AS points_award_amt,
--                   0 AS other_award_amt,
--                   par.other_award_desc, 
--                   -- pivot approval records into approval round fields
--                   -- round 1
--                   par.r1_award_type,
--                   MAX(DECODE(cia.approval_round, 1, cia.approval_status_type)) AS r1_approval_status,
--                   MAX(DECODE(cia.approval_round, 1, TRUNC(cia.date_approved))) AS r1_approval_date, 
--                   MAX(DECODE(cia.approval_round, 1, cia.approver_user_id))     AS r1_approver_user_id, 
--                   MAX(DECODE(cia.approval_round, 1, au_a.user_name))           AS r1_approver_user_name, 
--                   MAX(DECODE(cia.approval_round, 1, INITCAP(au_a.first_name))) AS r1_approver_first_name, 
--                   MAX(DECODE(cia.approval_round, 1, INITCAP(au_a.last_name)))  AS r1_approver_last_name, 
--                   -- round 2
--                   par.r2_award_type,
--                   MAX(DECODE(cia.approval_round, 2, cia.approval_status_type)) AS r2_approval_status,
--                   MAX(DECODE(cia.approval_round, 2, TRUNC(cia.date_approved))) AS r2_approval_date, 
--                   MAX(DECODE(cia.approval_round, 2, cia.approver_user_id))     AS r2_approver_user_id, 
--                   MAX(DECODE(cia.approval_round, 2, au_a.user_name))           AS r2_approver_user_name, 
--                   MAX(DECODE(cia.approval_round, 2, INITCAP(au_a.first_name))) AS r2_approver_first_name, 
--                   MAX(DECODE(cia.approval_round, 2, INITCAP(au_a.last_name)))  AS r2_approver_last_name, 
--                   -- round 3
--                   par.r3_award_type,
--                   MAX(DECODE(cia.approval_round, 3, cia.approval_status_type)) AS r3_approval_status,
--                   MAX(DECODE(cia.approval_round, 3, TRUNC(cia.date_approved))) AS r3_approval_date, 
--                   MAX(DECODE(cia.approval_round, 3, cia.approver_user_id))     AS r3_approver_user_id, 
--                   MAX(DECODE(cia.approval_round, 3, au_a.user_name))           AS r3_approver_user_name, 
--                   MAX(DECODE(cia.approval_round, 3, INITCAP(au_a.first_name))) AS r3_approver_first_name, 
--                   MAX(DECODE(cia.approval_round, 3, INITCAP(au_a.last_name)))  AS r3_approver_last_name, 
--                   -- round 4
--                   par.r4_award_type,
--                   MAX(DECODE(cia.approval_round, 4, cia.approval_status_type)) AS r4_approval_status,
--                   MAX(DECODE(cia.approval_round, 4, TRUNC(cia.date_approved))) AS r4_approval_date, 
--                   MAX(DECODE(cia.approval_round, 4, cia.approver_user_id))     AS r4_approver_user_id, 
--                   MAX(DECODE(cia.approval_round, 4, au_a.user_name))           AS r4_approver_user_name, 
--                   MAX(DECODE(cia.approval_round, 4, INITCAP(au_a.first_name))) AS r4_approver_first_name, 
--                   MAX(DECODE(cia.approval_round, 4, INITCAP(au_a.last_name)))  AS r4_approver_last_name, 
--                   -- round 5
--                   par.r5_award_type,
--                   MAX(DECODE(cia.approval_round, 5, cia.approval_status_type)) AS r5_approval_status,
--                   MAX(DECODE(cia.approval_round, 5, TRUNC(cia.date_approved))) AS r5_approval_date, 
--                   MAX(DECODE(cia.approval_round, 5, cia.approver_user_id))     AS r5_approver_user_id, 
--                   MAX(DECODE(cia.approval_round, 5, au_a.user_name))           AS r5_approver_user_name, 
--                   MAX(DECODE(cia.approval_round, 5, INITCAP(au_a.first_name))) AS r5_approver_first_name, 
--                   MAX(DECODE(cia.approval_round, 5, INITCAP(au_a.last_name)))  AS r5_approver_last_name
--              FROM claim c,
--                   nomination_claim nc,
--                   claim_item ci,
--                   claim_recipient cr,
--                   promotion p,
--                   promo_nomination_time_period pntp,
--                   ( -- flatten promotion award rounds
--                     SELECT pnl.promotion_id,
--                            MAX(DECODE(pnl.level_index, 1, pnl.award_payout_type)) AS r1_award_type,
--                            MAX(DECODE(pnl.level_index, 2, pnl.award_payout_type)) AS r2_award_type,
--                            MAX(DECODE(pnl.level_index, 3, pnl.award_payout_type)) AS r3_award_type,
--                            MAX(DECODE(pnl.level_index, 4, pnl.award_payout_type)) AS r4_award_type,
--                            MAX(DECODE(pnl.level_index, 5, pnl.award_payout_type)) AS r5_award_type,
--                            LISTAGG(DECODE(pnl.award_payout_type, gc_award_type_other, pnl.payout_description), ', ')
--                              WITHIN GROUP (ORDER BY pnl.level_index) AS other_award_desc
--                       FROM promo_nomination_level pnl
--                      GROUP BY pnl.promotion_id
--                   ) par,
--                   -- approval
--                   claim_item_approver cia,
--                   application_user au_a,
--                   -- giver
--                   application_user au_g,
--                   user_address ua_g,
--                   rpt_participant_employer paxe_g,
--                   node n_g,
--                   user_node un_g,
--                   -- receiver
--                   application_user au_r,
--                   user_address ua_r,
--                   rpt_participant_employer paxe_r,
--                   node n_r,
--                   user_node un_r
--             WHERE p_in_start_date < c.date_created
--               AND c.date_created <= p_in_end_date
--               AND c.claim_id = nc.claim_id
--               AND nc.status = 'complete'
--               AND c.claim_id = ci.claim_id
--               AND ci.claim_item_id = cr.claim_item_id
--               AND c.promotion_id = p.promotion_id
--               AND p.promotion_status IN (gc_promotion_status_active, gc_promotion_status_expired)
--               AND p.is_deleted = 0
--               AND nc.nomination_time_period_id = pntp.nomination_time_period_id (+)
--               AND c.promotion_id = par.promotion_id
--                -- link approvals
----               AND ci.claim_item_id = cia.claim_item_id (+)--G6-2479 Start
--               AND (ci.claim_item_id = cia.claim_item_id 
--                                     OR c.claim_group_id = cia.claim_group_id )--G6-2479 End
--               AND cia.approver_user_id = au_a.user_id (+)
--                -- link nominator details
--               AND c.submitter_id = au_g.user_id (+)
--               AND c.submitter_id = ua_g.user_id (+)
--               AND 1 = ua_g.is_primary (+)
--               AND c.submitter_id = paxe_g.user_id (+)
--               AND c.node_id = n_g.node_id (+)
--               AND c.submitter_id = un_g.user_id (+)
--               AND c.node_id = un_g.node_id (+)
--               AND 1 = un_g.is_primary (+)
--               AND 1 = un_g.status (+)
--                -- link nominee details
--               AND cr.participant_id = au_r.user_id (+)
--               AND cr.participant_id = ua_r.user_id (+)
--               AND 1 = ua_r.is_primary (+)
--               AND cr.participant_id = paxe_r.user_id (+)
--               AND cr.node_id = n_r.node_id (+)
--               AND cr.participant_id = un_r.user_id (+)
--               AND cr.node_id = un_r.node_id (+)
--               AND 1 = un_r.is_primary (+)
--               AND 1 = un_r.status (+)
--             GROUP BY c.claim_id,
--                   ci.claim_item_id,
--                   c.promotion_id,
--                   p.promotion_name,
--                   p.promotion_status,
--                   TRUNC(c.submission_date),
--                   TRUNC(ci.date_approved),
--                   c.is_open,
--                   nc.step_number,
--                   c.approval_round,
--                   nc.status,
--                   ci.approval_status_type,
--                   nc.nomination_time_period_id, 
--                   pntp.time_period_name,
--                   c.claim_group_id,
--                   nc.team_id,
--                   nc.team_name,
--                   -- nominator fields
--                   c.submitter_id,
--                   c.node_id,
--                   ua_g.country_id,
--                   paxe_g.department_type,
--                   paxe_g.position_type,
--                   au_g.is_active,
--                   au_g.user_name,
--                   au_g.first_name,
--                   au_g.middle_name,
--                   au_g.last_name,
--                   n_g.name,
--                   un_g.role,
--                   -- nominee fields
--                   cr.participant_id,
--                   cr.node_id,
--                   ua_r.country_id,
--                   paxe_r.department_type,
--                   paxe_r.position_type,
--                   au_r.is_active,
--                   au_r.user_name,
--                   au_r.first_name,
--                   au_r.middle_name,
--                   au_r.last_name,
--                   n_r.name,
--                   un_r.role,
--                   par.other_award_desc, 
--                   par.r1_award_type,
--                   par.r2_award_type,
--                   par.r3_award_type,
--                   par.r4_award_type,
--                   par.r5_award_type          --09/06/2017 G6-2927 commented ***
  SELECT -- nomination fields
        c.claim_id,
        ci.claim_item_id,
        c.promotion_id,
        p.promotion_name,
        p.promotion_status,
        TRUNC(c.submission_date) AS date_submitted,
        TRUNC(ci.date_approved) AS date_approved,
        c.is_open,
        nc.step_number,
        c.approval_round AS claim_approval_round,
        nc.status AS claim_status,
        ci.approval_status_type AS claim_item_status,
        nc.nomination_time_period_id, 
        pntp.time_period_name,
        c.claim_group_id,
        nc.team_id,
        nc.team_name,
        -- nominator fields
        c.submitter_id AS giver_user_id,
        c.node_id AS giver_node_id,
        ua_g.country_id AS giver_country_id,
        paxe_g.department_type AS giver_department_type,
        paxe_g.position_type AS giver_position_type,
        DECODE(au_g.is_active, 1, gc_pax_status_active, gc_pax_status_inactive) AS giver_pax_status,
        au_g.user_name AS giver_user_name,
        INITCAP(au_g.first_name) AS giver_first_name,
        INITCAP(TRIM(au_g.middle_name)) AS giver_middle_name,
        INITCAP(au_g.last_name) AS giver_last_name,
        n_g.name AS giver_node_name,
        un_g.role AS giver_org_role,
        -- receiver fields
        cr.participant_id AS recvr_user_id,
        cr.node_id AS recvr_node_id,
        ua_r.country_id AS recvr_country_id,
        paxe_r.department_type AS recvr_department_type,
        paxe_r.position_type AS recvr_position_type,
        DECODE(au_r.is_active, 1, gc_pax_status_active, gc_pax_status_inactive) AS recvr_pax_status,
        au_r.user_name AS recvr_user_name,
        INITCAP(au_r.first_name) AS recvr_first_name,
        INITCAP(TRIM(au_r.middle_name)) AS recvr_middle_name,
        INITCAP(au_r.last_name) AS recvr_last_name,
        n_r.name AS recvr_node_name,
        un_r.role AS recvr_org_role,
        -- award fields
        0 AS cash_award_amt,
        0 AS points_award_amt,
        0 AS other_award_amt,
        par.other_award_desc, 
        -- pivot approval records into approval round fields
        -- round 1
        r1_award_type,
        r1_approval_status,
        r1_approval_date, 
        r1_approver_user_id, 
        r1_approver_user_name, 
        r1_approver_first_name, 
        r1_approver_last_name, 
        -- round 2
        r2_award_type,
        r2_approval_status,
        r2_approval_date, 
        r2_approver_user_id, 
        r2_approver_user_name, 
        r2_approver_first_name, 
        r2_approver_last_name, 
        -- round 3
        r3_award_type,
        r3_approval_status,
        r3_approval_date, 
        r3_approver_user_id, 
        r3_approver_user_name, 
        r3_approver_first_name, 
        r3_approver_last_name, 
        -- round 4
        r4_award_type,
        r4_approval_status,
        r4_approval_date, 
        r4_approver_user_id, 
        r4_approver_user_name, 
        r4_approver_first_name, 
        r4_approver_last_name, 
            -- round 5
        r5_award_type,
        r5_approval_status,
        r5_approval_date, 
        r5_approver_user_id, 
        r5_approver_user_name, 
        r5_approver_first_name, 
        r5_approver_last_name
      FROM claim c,
           nomination_claim nc,
           claim_item ci,
           claim_recipient cr,
           promotion p,
           promo_nomination_time_period pntp,
           ( -- flatten promotion award rounds
             SELECT pnl.promotion_id,
                    MAX(DECODE(pnl.level_index, 1, pnl.award_payout_type)) AS r1_award_type,
                    MAX(DECODE(pnl.level_index, 2, pnl.award_payout_type)) AS r2_award_type,
                    MAX(DECODE(pnl.level_index, 3, pnl.award_payout_type)) AS r3_award_type,
                    MAX(DECODE(pnl.level_index, 4, pnl.award_payout_type)) AS r4_award_type,
                    MAX(DECODE(pnl.level_index, 5, pnl.award_payout_type)) AS r5_award_type,
                    LISTAGG(DECODE(pnl.award_payout_type, gc_award_type_other, pnl.payout_description), ', ')
                      WITHIN GROUP (ORDER BY pnl.level_index) AS other_award_desc
               FROM promo_nomination_level pnl
              GROUP BY pnl.promotion_id
           ) par,
           -- approval
--                   claim_item_approver cia, --G6-2927  commented tables and replace with below query 
--                   application_user au_a, --G6-2927  commented tables and replace with below query
            (SELECT --G6-2927  start remodified query
                    c.claim_group_id, ci.claim_item_id,
                    MAX(DECODE(cia.approval_round, 1, cia.approval_status_type)) AS r1_approval_status,
                    MAX(DECODE(cia.approval_round, 1, TRUNC(cia.date_approved))) AS r1_approval_date, 
                    MAX(DECODE(cia.approval_round, 1, cia.approver_user_id))     AS r1_approver_user_id, 
                    MAX(DECODE(cia.approval_round, 1, au_a.user_name))           AS r1_approver_user_name, 
                    MAX(DECODE(cia.approval_round, 1, INITCAP(au_a.first_name))) AS r1_approver_first_name, 
                    MAX(DECODE(cia.approval_round, 1, INITCAP(au_a.last_name)))  AS r1_approver_last_name, 
                    -- round 2
                    MAX(DECODE(cia.approval_round, 2, cia.approval_status_type)) AS r2_approval_status,
                    MAX(DECODE(cia.approval_round, 2, TRUNC(cia.date_approved))) AS r2_approval_date, 
                    MAX(DECODE(cia.approval_round, 2, cia.approver_user_id))     AS r2_approver_user_id, 
                    MAX(DECODE(cia.approval_round, 2, au_a.user_name))           AS r2_approver_user_name, 
                    MAX(DECODE(cia.approval_round, 2, INITCAP(au_a.first_name))) AS r2_approver_first_name, 
                    MAX(DECODE(cia.approval_round, 2, INITCAP(au_a.last_name)))  AS r2_approver_last_name, 
                    -- round 3
                    MAX(DECODE(cia.approval_round, 3, cia.approval_status_type)) AS r3_approval_status,
                    MAX(DECODE(cia.approval_round, 3, TRUNC(cia.date_approved))) AS r3_approval_date, 
                    MAX(DECODE(cia.approval_round, 3, cia.approver_user_id))     AS r3_approver_user_id, 
                    MAX(DECODE(cia.approval_round, 3, au_a.user_name))           AS r3_approver_user_name, 
                    MAX(DECODE(cia.approval_round, 3, INITCAP(au_a.first_name))) AS r3_approver_first_name, 
                    MAX(DECODE(cia.approval_round, 3, INITCAP(au_a.last_name)))  AS r3_approver_last_name, 
                    -- round 4
                    MAX(DECODE(cia.approval_round, 4, cia.approval_status_type)) AS r4_approval_status,
                    MAX(DECODE(cia.approval_round, 4, TRUNC(cia.date_approved))) AS r4_approval_date, 
                    MAX(DECODE(cia.approval_round, 4, cia.approver_user_id))     AS r4_approver_user_id, 
                    MAX(DECODE(cia.approval_round, 4, au_a.user_name))           AS r4_approver_user_name, 
                    MAX(DECODE(cia.approval_round, 4, INITCAP(au_a.first_name))) AS r4_approver_first_name, 
                    MAX(DECODE(cia.approval_round, 4, INITCAP(au_a.last_name)))  AS r4_approver_last_name, 
                    -- round 5
                    MAX(DECODE(cia.approval_round, 5, cia.approval_status_type)) AS r5_approval_status,
                    MAX(DECODE(cia.approval_round, 5, TRUNC(cia.date_approved))) AS r5_approval_date, 
                    MAX(DECODE(cia.approval_round, 5, cia.approver_user_id))     AS r5_approver_user_id, 
                    MAX(DECODE(cia.approval_round, 5, au_a.user_name))           AS r5_approver_user_name, 
                    MAX(DECODE(cia.approval_round, 5, INITCAP(au_a.first_name))) AS r5_approver_first_name, 
                    MAX(DECODE(cia.approval_round, 5, INITCAP(au_a.last_name)))  AS r5_approver_last_name                            
               FROM claim c, claim_item ci, claim_item_approver cia, application_user au_a
              WHERE c.claim_id = ci.claim_id
                AND cia.approver_user_id = au_a.user_id(+) 
                AND NVL(ci.claim_item_id,c.claim_group_id) = NVL(cia.claim_item_id(+),cia.claim_group_id(+))  --G6-2927 End approved claim also for cummulative nomination
              GROUP BY c.claim_group_id, ci.claim_item_id
                       ) approv,     --G6-2927  end remodified query 
        ---------------------------------------------------------------------
           -- giver
           application_user au_g,
           user_address ua_g,
           rpt_participant_employer paxe_g,
           node n_g,
           user_node un_g,
           -- receiver
           application_user au_r,
           user_address ua_r,
           rpt_participant_employer paxe_r,
           node n_r,
           user_node un_r
     WHERE ((p_in_start_date < c.date_created -- 01/26/2018
               AND c.date_created <= p_in_end_date)   -- 01/26/2018
                OR (p_in_start_date < c.date_modified -- 01/26/2018
               AND c.date_modified <= p_in_end_date)) -- 01/26/2018
       AND c.claim_id = nc.claim_id
       AND nc.status = 'complete'
       AND c.claim_id = ci.claim_id
       AND ci.claim_item_id = cr.claim_item_id
       AND c.promotion_id = p.promotion_id
       AND p.promotion_status IN (gc_promotion_status_active, gc_promotion_status_expired)
       AND p.is_deleted = 0
       AND nc.nomination_time_period_id = pntp.nomination_time_period_id (+)
       AND c.promotion_id = par.promotion_id
        -- link approvals
        --G6-2927 Commented starts
--               AND ci.claim_item_id = cia.claim_item_id (+)--G6-2479 Start
--               AND ((ci.claim_item_id = cia.claim_item_id 
--                                     OR c.claim_group_id = cia.claim_group_id)--G6-2479 End approved claim also for cummulative nomination
--                        OR CI.APPROVAL_STATUS_TYPE =  'pend')--G6-2479 End                                     
--               AND approv.approver_user_id = au_a.user_id (+)
         --G6-2927 Commented Ends
       AND  NVL(ci.claim_item_id, c.claim_group_id) = NVL(approv.claim_item_id, approv.claim_group_id)  --G6-2927 re-return as 
        -- link nominator details
       AND c.submitter_id = au_g.user_id (+)
       AND c.submitter_id = ua_g.user_id (+)
       AND 1 = ua_g.is_primary (+)
       AND c.submitter_id = paxe_g.user_id (+)
       AND c.node_id = n_g.node_id (+)
       AND c.submitter_id = un_g.user_id (+)
       AND c.node_id = un_g.node_id (+)
       AND 1 = un_g.is_primary (+)
       AND 1 = un_g.status (+)
        -- link nominee details
       AND cr.participant_id = au_r.user_id (+)
       AND cr.participant_id = ua_r.user_id (+)
       AND 1 = ua_r.is_primary (+)
       AND cr.participant_id = paxe_r.user_id (+)
       AND cr.node_id = n_r.node_id (+)
       AND cr.participant_id = un_r.user_id (+)
       AND cr.node_id = un_r.node_id (+)
       AND 1 = un_r.is_primary (+)
       AND 1 = un_r.status (+)
         ) s
      ON (d.claim_item_id = s.claim_item_id)
    WHEN NOT MATCHED THEN
      INSERT
      ( rpt_nomination_detail_id,
        claim_id,
        claim_item_id,
        promotion_id,
        promotion_name,
        promotion_status,
        date_submitted,
        date_approved,
        is_open,
        step_number,
        claim_approval_round,
        claim_status,
        claim_item_status,
        nomination_time_period_id,
        time_period_name,
        claim_group_id,
        team_id,
        team_name,
        giver_user_id,
        giver_node_id,
        giver_country_id,
        giver_department_type,
        giver_position_type,
        giver_pax_status,
        giver_user_name,
        giver_first_name,
        giver_middle_name,
        giver_last_name,
        giver_node_name,
        giver_org_role,
        recvr_user_id,
        recvr_node_id,
        recvr_country_id,
        recvr_department_type,
        recvr_position_type,
        recvr_pax_status,
        recvr_user_name,
        recvr_first_name,
        recvr_middle_name,
        recvr_last_name,
        recvr_node_name,
        recvr_org_role,
        cash_award_amt,
        points_award_amt,
        other_award_amt,
        other_award_desc,
        r1_award_type,
        r2_award_type,
        r3_award_type,
        r4_award_type,
        r5_award_type,
        r1_approval_status,
        r2_approval_status,
        r3_approval_status,
        r4_approval_status,
        r5_approval_status,
        r1_approval_date,
        r2_approval_date,
        r3_approval_date,
        r4_approval_date,
        r5_approval_date,
        r1_approver_user_id,
        r2_approver_user_id,
        r3_approver_user_id,
        r4_approver_user_id,
        r5_approver_user_id,
        r1_approver_user_name,
        r2_approver_user_name,
        r3_approver_user_name,
        r4_approver_user_name,
        r5_approver_user_name,
        r1_approver_first_name,
        r2_approver_first_name,
        r3_approver_first_name,
        r4_approver_first_name,
        r5_approver_first_name,
        r1_approver_last_name,
        r2_approver_last_name,
        r3_approver_last_name,
        r4_approver_last_name,
        r5_approver_last_name,
        created_by,
        date_created,
        version
      )
      VALUES
      ( rpt_nomination_detail_pk_sq.NEXTVAL,
        s.claim_id,
        s.claim_item_id,
        s.promotion_id,
        s.promotion_name,
        s.promotion_status,
        s.date_submitted,
        s.date_approved,
        s.is_open,
        s.step_number,
        s.claim_approval_round,
        s.claim_status,
        s.claim_item_status,
        s.nomination_time_period_id,
        s.time_period_name,
        s.claim_group_id,
        s.team_id,
        s.team_name,
        s.giver_user_id,
        s.giver_node_id,
        s.giver_country_id,
        s.giver_department_type,
        s.giver_position_type,
        s.giver_pax_status,
        s.giver_user_name,
        s.giver_first_name,
        s.giver_middle_name,
        s.giver_last_name,
        s.giver_node_name,
        s.giver_org_role,
        s.recvr_user_id,
        s.recvr_node_id,
        s.recvr_country_id,
        s.recvr_department_type,
        s.recvr_position_type,
        s.recvr_pax_status,
        s.recvr_user_name,
        s.recvr_first_name,
        s.recvr_middle_name,
        s.recvr_last_name,
        s.recvr_node_name,
        s.recvr_org_role,
        s.cash_award_amt,
        s.points_award_amt,
        s.other_award_amt,
        s.other_award_desc,
        s.r1_award_type,
        s.r2_award_type,
        s.r3_award_type,
        s.r4_award_type,
        s.r5_award_type,
        s.r1_approval_status,
        s.r2_approval_status,
        s.r3_approval_status,
        s.r4_approval_status,
        s.r5_approval_status,
        s.r1_approval_date,
        s.r2_approval_date,
        s.r3_approval_date,
        s.r4_approval_date,
        s.r5_approval_date,
        s.r1_approver_user_id,
        s.r2_approver_user_id,
        s.r3_approver_user_id,
        s.r4_approver_user_id,
        s.r5_approver_user_id,
        s.r1_approver_user_name,
        s.r2_approver_user_name,
        s.r3_approver_user_name,
        s.r4_approver_user_name,
        s.r5_approver_user_name,
        s.r1_approver_first_name,
        s.r2_approver_first_name,
        s.r3_approver_first_name,
        s.r4_approver_first_name,
        s.r5_approver_first_name,
        s.r1_approver_last_name,
        s.r2_approver_last_name,
        s.r3_approver_last_name,
        s.r4_approver_last_name,
        s.r5_approver_last_name,
        p_in_user_id,   -- created_by
        p_in_timestamp,  -- date_created
        0
      );

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ':' || v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END p_refresh_new_claim;

-----------------------
-- Refreshes any modified claim associated fields
PROCEDURE p_refresh_claim
( p_in_user_id       IN NUMBER,
  p_in_start_date    IN DATE, 
  p_in_end_date      IN DATE, 
  p_in_timestamp     IN DATE
) IS
   c_process_name                CONSTANT execution_log.process_name%type  := UPPER('p_refresh_claim');
   v_stage                       execution_log.text_line%TYPE;
   v_parm_list                   execution_log.text_line%TYPE;
   v_rec_count                   INTEGER;
BEGIN
   v_stage := 'initialzie variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_start_date >' || TO_CHAR(p_in_start_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_end_date >' || TO_CHAR(p_in_end_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_timestamp >' || TO_CHAR(p_in_timestamp,'MM/DD/YYYY HH24:MI:SS')
      || '<~';

   -- refresh stale report records
   v_stage := 'MERGE rpt_nomination_detail';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   c.promotion_id,
                   TRUNC(c.submission_date) AS date_submitted,
                   c.is_open,
                   nc.step_number,
                   c.approval_round AS claim_approval_round,
                   nc.status AS claim_status,
                   nc.nomination_time_period_id, 
                   pntp.time_period_name,
                   c.claim_group_id,
                   nc.team_id,
                   nc.team_name,
                   c.submitter_id AS giver_user_id,
                   c.node_id AS giver_node_id,
                   un.role AS giver_org_role
              FROM claim c,
                   nomination_claim nc,
                   promo_nomination_time_period pntp,
                   rpt_nomination_detail nd,
                   user_node un
             WHERE p_in_start_date < c.date_modified
               AND c.date_modified <= p_in_end_date
               AND c.claim_id = nc.claim_id
               AND nc.nomination_time_period_id = pntp.nomination_time_period_id (+)
               AND c.claim_id = nd.claim_id
               AND c.submitter_id = un.user_id (+)
               AND c.node_id = un.node_id (+)
               AND 1 = un.is_primary (+)
               AND 1 = un.status (+)
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT
                   (   c.promotion_id                       = nd.promotion_id
                   AND TRUNC(c.submission_date)             = nd.date_submitted
                   AND DECODE(c.is_open,                    nd.is_open,                   1, 0) = 1
                   AND DECODE(nc.step_number,               nd.step_number,               1, 0) = 1
                   AND DECODE(c.approval_round,             nd.claim_approval_round,      1, 0) = 1
                   AND DECODE(nc.status,                    nd.claim_status,              1, 0) = 1
                   AND DECODE(nc.nomination_time_period_id, nd.nomination_time_period_id, 1, 0) = 1
                   AND DECODE(pntp.time_period_name,        nd.time_period_name,          1, 0) = 1
                   AND DECODE(c.claim_group_id,             nd.claim_group_id,            1, 0) = 1
                   AND DECODE(nc.team_id,                   nd.team_id,                   1, 0) = 1
                   AND DECODE(nc.team_name,                 nd.team_name,                 1, 0) = 1
                   AND DECODE(c.submitter_id,               nd.giver_user_id,             1, 0) = 1
                   AND DECODE(c.node_id,                    nd.giver_node_id,             1, 0) = 1
                   AND DECODE(un.role,                      nd.giver_org_role,            1, 0) = 1
                   )
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET promotion_id              = s.promotion_id,
             date_submitted            = s.date_submitted,
             is_open                   = s.is_open,
             step_number               = s.step_number,
             claim_approval_round      = s.claim_approval_round,
             claim_status              = s.claim_status,
             nomination_time_period_id = s.nomination_time_period_id,
             time_period_name          = s.time_period_name,
             claim_group_id            = s.claim_group_id,
             team_id                   = s.team_id,
             team_name                 = s.team_name,
             giver_user_id             = s.giver_user_id,
             giver_node_id             = s.giver_node_id,
             giver_org_role            = s.giver_org_role,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ':' || v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END p_refresh_claim;

-----------------------
-- Refreshes any modified claim item associated fields
PROCEDURE p_refresh_claim_item
( p_in_user_id       IN NUMBER,
  p_in_start_date    IN DATE, 
  p_in_end_date      IN DATE, 
  p_in_timestamp     IN DATE
) IS
   c_process_name                CONSTANT execution_log.process_name%type  := UPPER('p_refresh_claim_item');
   v_stage                       execution_log.text_line%TYPE;
   v_parm_list                   execution_log.text_line%TYPE;
   v_rec_count                   INTEGER;
BEGIN
   v_stage := 'initialzie variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_start_date >' || TO_CHAR(p_in_start_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_end_date >' || TO_CHAR(p_in_end_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_timestamp >' || TO_CHAR(p_in_timestamp,'MM/DD/YYYY HH24:MI:SS')
      || '<~';

   -- refresh stale report records
   v_stage := 'MERGE rpt_nomination_detail';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   TRUNC(ci.date_approved) AS date_approved,
                   ci.approval_status_type AS claim_item_status
              FROM claim_item ci,
                   rpt_nomination_detail nd
             WHERE p_in_start_date < ci.date_modified
               AND ci.date_modified <= p_in_end_date
               AND ci.claim_item_id = nd.claim_item_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT
                   (   DECODE(TRUNC(ci.date_approved), nd.date_approved,     1, 0) = 1
                   AND DECODE(ci.approval_status_type, nd.claim_item_status, 1, 0) = 1
                   )
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET date_approved     = s.date_approved,
             claim_item_status = s.claim_item_status,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ':' || v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END p_refresh_claim_item;

-----------------------
-- Refreshes any modified claim approval associated fields
PROCEDURE p_refresh_claim_approval
( p_in_user_id       IN NUMBER,
  p_in_start_date    IN DATE, 
  p_in_end_date      IN DATE, 
  p_in_timestamp     IN DATE
) IS
   c_process_name                CONSTANT execution_log.process_name%type  := UPPER('p_refresh_claim_approval');
   v_stage                       execution_log.text_line%TYPE;
   v_parm_list                   execution_log.text_line%TYPE;
   v_rec_count                   INTEGER;
BEGIN
   v_stage := 'initialzie variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_start_date >' || TO_CHAR(p_in_start_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_end_date >' || TO_CHAR(p_in_end_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_timestamp >' || TO_CHAR(p_in_timestamp,'MM/DD/YYYY HH24:MI:SS')
      || '<~';

   -- refresh stale report records
   v_stage := 'MERGE rpt_nomination_detail';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   -- round 1
                   fd.r1_approval_status,
                   fd.r1_approval_date, 
                   fd.r1_approver_user_id, 
                   fd.r1_approver_user_name, 
                   fd.r1_approver_first_name, 
                   fd.r1_approver_last_name, 
                   -- round 2
                   fd.r2_approval_status,
                   fd.r2_approval_date, 
                   fd.r2_approver_user_id, 
                   fd.r2_approver_user_name, 
                   fd.r2_approver_first_name, 
                   fd.r2_approver_last_name, 
                   -- round 3
                   fd.r3_approval_status,
                   fd.r3_approval_date, 
                   fd.r3_approver_user_id, 
                   fd.r3_approver_user_name, 
                   fd.r3_approver_first_name, 
                   fd.r3_approver_last_name, 
                   -- round 4
                   fd.r4_approval_status,
                   fd.r4_approval_date, 
                   fd.r4_approver_user_id, 
                   fd.r4_approver_user_name, 
                   fd.r4_approver_first_name, 
                   fd.r4_approver_last_name, 
                   -- round 5
                   fd.r5_approval_status,
                   fd.r5_approval_date, 
                   fd.r5_approver_user_id, 
                   fd.r5_approver_user_name, 
                   fd.r5_approver_first_name, 
                   fd.r5_approver_last_name
              FROM ( -- format data
                     SELECT cia.claim_item_id,
                            -- pivot approval records into approval round fields
                            -- round 1
                            MAX(DECODE(cia.approval_round, 1, cia.approval_status_type)) AS r1_approval_status,
                            MAX(DECODE(cia.approval_round, 1, TRUNC(cia.date_approved))) AS r1_approval_date, 
                            MAX(DECODE(cia.approval_round, 1, cia.approver_user_id))     AS r1_approver_user_id, 
                            MAX(DECODE(cia.approval_round, 1, au.user_name))             AS r1_approver_user_name, 
                            MAX(DECODE(cia.approval_round, 1, INITCAP(au.first_name)))   AS r1_approver_first_name, 
                            MAX(DECODE(cia.approval_round, 1, INITCAP(au.last_name)))    AS r1_approver_last_name, 
                            -- round 2
                            MAX(DECODE(cia.approval_round, 2, cia.approval_status_type)) AS r2_approval_status,
                            MAX(DECODE(cia.approval_round, 2, TRUNC(cia.date_approved))) AS r2_approval_date, 
                            MAX(DECODE(cia.approval_round, 2, cia.approver_user_id))     AS r2_approver_user_id, 
                            MAX(DECODE(cia.approval_round, 2, au.user_name))             AS r2_approver_user_name, 
                            MAX(DECODE(cia.approval_round, 2, INITCAP(au.first_name)))   AS r2_approver_first_name, 
                            MAX(DECODE(cia.approval_round, 2, INITCAP(au.last_name)))    AS r2_approver_last_name, 
                            -- round 3
                            MAX(DECODE(cia.approval_round, 3, cia.approval_status_type)) AS r3_approval_status,
                            MAX(DECODE(cia.approval_round, 3, TRUNC(cia.date_approved))) AS r3_approval_date, 
                            MAX(DECODE(cia.approval_round, 3, cia.approver_user_id))     AS r3_approver_user_id, 
                            MAX(DECODE(cia.approval_round, 3, au.user_name))             AS r3_approver_user_name, 
                            MAX(DECODE(cia.approval_round, 3, INITCAP(au.first_name)))   AS r3_approver_first_name, 
                            MAX(DECODE(cia.approval_round, 3, INITCAP(au.last_name)))    AS r3_approver_last_name, 
                            -- round 4
                            MAX(DECODE(cia.approval_round, 4, cia.approval_status_type)) AS r4_approval_status,
                            MAX(DECODE(cia.approval_round, 4, TRUNC(cia.date_approved))) AS r4_approval_date, 
                            MAX(DECODE(cia.approval_round, 4, cia.approver_user_id))     AS r4_approver_user_id, 
                            MAX(DECODE(cia.approval_round, 4, au.user_name))             AS r4_approver_user_name, 
                            MAX(DECODE(cia.approval_round, 4, INITCAP(au.first_name)))   AS r4_approver_first_name, 
                            MAX(DECODE(cia.approval_round, 4, INITCAP(au.last_name)))    AS r4_approver_last_name, 
                            -- round 5
                            MAX(DECODE(cia.approval_round, 5, cia.approval_status_type)) AS r5_approval_status,
                            MAX(DECODE(cia.approval_round, 5, TRUNC(cia.date_approved))) AS r5_approval_date, 
                            MAX(DECODE(cia.approval_round, 5, cia.approver_user_id))     AS r5_approver_user_id, 
                            MAX(DECODE(cia.approval_round, 5, au.user_name))             AS r5_approver_user_name, 
                            MAX(DECODE(cia.approval_round, 5, INITCAP(au.first_name)))   AS r5_approver_first_name, 
                            MAX(DECODE(cia.approval_round, 5, INITCAP(au.last_name)))    AS r5_approver_last_name
                       FROM ( -- get raw data
                              -- union allows for separate index searches
                              SELECT cia1.ROWID AS cia_rowid
                                FROM claim_item_approver cia1
                               WHERE p_in_start_date < cia1.date_created
                                 AND cia1.date_created <= p_in_end_date
                               UNION
                              SELECT cia1.ROWID AS cia_rowid
                                FROM claim_item_approver cia1
                               WHERE p_in_start_date < cia1.date_modified
                                 AND cia1.date_modified <= p_in_end_date
                            ) rd,
                            claim_item_approver cia,
                            application_user au
                      WHERE rd.cia_rowid = cia.ROWID
                        AND cia.approver_user_id = au.user_id (+)
             GROUP BY cia.claim_item_id
                   ) fd,
                   rpt_nomination_detail nd
             WHERE fd.claim_item_id = nd.claim_item_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT
                   (-- round 1
                       DECODE(fd.r1_approval_status,     nd.r1_approval_status,     1, 0) = 1
                   AND DECODE(fd.r1_approval_date,       nd.r1_approval_date,       1, 0) = 1
                   AND DECODE(fd.r1_approver_user_id,    nd.r1_approver_user_id,    1, 0) = 1
                   AND DECODE(fd.r1_approver_user_name,  nd.r1_approver_user_name,  1, 0) = 1
                   AND DECODE(fd.r1_approver_first_name, nd.r1_approver_first_name, 1, 0) = 1
                   AND DECODE(fd.r1_approver_last_name,  nd.r1_approver_last_name,  1, 0) = 1
                    -- round 2
                   AND DECODE(fd.r2_approval_status,     nd.r2_approval_status,     1, 0) = 1
                   AND DECODE(fd.r2_approval_date,       nd.r2_approval_date,       1, 0) = 1
                   AND DECODE(fd.r2_approver_user_id,    nd.r2_approver_user_id,    1, 0) = 1
                   AND DECODE(fd.r2_approver_user_name,  nd.r2_approver_user_name,  1, 0) = 1
                   AND DECODE(fd.r2_approver_first_name, nd.r2_approver_first_name, 1, 0) = 1
                   AND DECODE(fd.r2_approver_last_name,  nd.r2_approver_last_name,  1, 0) = 1
                    -- round 3
                   AND DECODE(fd.r3_approval_status,     nd.r3_approval_status,     1, 0) = 1
                   AND DECODE(fd.r3_approval_date,       nd.r3_approval_date,       1, 0) = 1
                   AND DECODE(fd.r3_approver_user_id,    nd.r3_approver_user_id,    1, 0) = 1
                   AND DECODE(fd.r3_approver_user_name,  nd.r3_approver_user_name,  1, 0) = 1
                   AND DECODE(fd.r3_approver_first_name, nd.r3_approver_first_name, 1, 0) = 1
                   AND DECODE(fd.r3_approver_last_name,  nd.r3_approver_last_name,  1, 0) = 1
                    -- round 4
                   AND DECODE(fd.r4_approval_status,     nd.r4_approval_status,     1, 0) = 1
                   AND DECODE(fd.r4_approval_date,       nd.r4_approval_date,       1, 0) = 1
                   AND DECODE(fd.r4_approver_user_id,    nd.r4_approver_user_id,    1, 0) = 1
                   AND DECODE(fd.r4_approver_user_name,  nd.r4_approver_user_name,  1, 0) = 1
                   AND DECODE(fd.r4_approver_first_name, nd.r4_approver_first_name, 1, 0) = 1
                   AND DECODE(fd.r4_approver_last_name,  nd.r4_approver_last_name,  1, 0) = 1
                    -- round 5
                   AND DECODE(fd.r5_approval_status,     nd.r5_approval_status,     1, 0) = 1
                   AND DECODE(fd.r5_approval_date,       nd.r5_approval_date,       1, 0) = 1
                   AND DECODE(fd.r5_approver_user_id,    nd.r5_approver_user_id,    1, 0) = 1
                   AND DECODE(fd.r5_approver_user_name,  nd.r5_approver_user_name,  1, 0) = 1
                   AND DECODE(fd.r5_approver_first_name, nd.r5_approver_first_name, 1, 0) = 1
                   AND DECODE(fd.r5_approver_last_name,  nd.r5_approver_last_name,  1, 0) = 1
                   )
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET -- round 1
             r1_approval_status     = s.r1_approval_status,
             r1_approval_date       = s.r1_approval_date, 
             r1_approver_user_id    = s.r1_approver_user_id, 
             r1_approver_user_name  = s.r1_approver_user_name, 
             r1_approver_first_name = s.r1_approver_first_name, 
             r1_approver_last_name  = s.r1_approver_last_name, 
             -- round 2
             r2_approval_status     = s.r2_approval_status,
             r2_approval_date       = s.r2_approval_date, 
             r2_approver_user_id    = s.r2_approver_user_id, 
             r2_approver_user_name  = s.r2_approver_user_name, 
             r2_approver_first_name = s.r2_approver_first_name, 
             r2_approver_last_name  = s.r2_approver_last_name, 
             -- round 3
             r3_approval_status     = s.r3_approval_status,
             r3_approval_date       = s.r3_approval_date, 
             r3_approver_user_id    = s.r3_approver_user_id, 
             r3_approver_user_name  = s.r3_approver_user_name, 
             r3_approver_first_name = s.r3_approver_first_name, 
             r3_approver_last_name  = s.r3_approver_last_name, 
             -- round 4
             r4_approval_status     = s.r4_approval_status,
             r4_approval_date       = s.r4_approval_date, 
             r4_approver_user_id    = s.r4_approver_user_id, 
             r4_approver_user_name  = s.r4_approver_user_name, 
             r4_approver_first_name = s.r4_approver_first_name, 
             r4_approver_last_name  = s.r4_approver_last_name, 
             -- round 5
             r5_approval_status     = s.r5_approval_status,
             r5_approval_date       = s.r5_approval_date, 
             r5_approver_user_id    = s.r5_approver_user_id, 
             r5_approver_user_name  = s.r5_approver_user_name, 
             r5_approver_first_name = s.r5_approver_first_name, 
             r5_approver_last_name  = s.r5_approver_last_name,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ':' || v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END p_refresh_claim_approval;

-----------------------
-- Refreshes any modified claim award associated fields
PROCEDURE p_refresh_claim_award
( p_in_user_id       IN NUMBER,
  p_in_start_date    IN DATE, 
  p_in_end_date      IN DATE, 
  p_in_timestamp     IN DATE
) IS
   c_process_name                CONSTANT execution_log.process_name%type  := UPPER('p_refresh_claim_award');
   v_stage                       execution_log.text_line%TYPE;
   v_parm_list                   execution_log.text_line%TYPE;
   v_rec_count                   INTEGER;
BEGIN
   v_stage := 'initialzie variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_start_date >' || TO_CHAR(p_in_start_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_end_date >' || TO_CHAR(p_in_end_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_timestamp >' || TO_CHAR(p_in_timestamp,'MM/DD/YYYY HH24:MI:SS')
      || '<~';

   -- refresh stale report records
   v_stage := 'MERGE rpt_nomination_detail (cash/points)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT sd.nd_rowid,
                   -- subtract any award reversal
                   ( SUM(sd.cash_award_amt) --08/03/2016
                   - NVL(SUM(DECODE(j.award_type, gc_award_type_cash, j.transaction_cash_amt)), 0)
                   ) AS cash_award_amt,
                   ( SUM(sd.points_award_amt) --08/03/2016
                   - NVL(SUM(DECODE(j.award_type, gc_award_type_points, j.transaction_amt)), 0)
                   ) AS points_award_amt
              FROM ( -- sum data by claim item activity for all winning approvals
                     SELECT nd.ROWID AS nd_rowid,
                            a.activity_id,
                            NVL(SUM(DECODE(pnl.award_payout_type, gc_award_type_cash, a.cash_award_qty)), 0) AS cash_award_amt,
                            NVL(SUM(DECODE(pnl.award_payout_type, gc_award_type_points, a.quantity)), 0) AS points_award_amt
                       FROM ( -- get raw data
                              -- union allows for separate index searches
                              SELECT cia2.claim_item_id
                                FROM claim_item_approver cia2
                               WHERE p_in_start_date < cia2.date_created
                                 AND cia2.date_created <= p_in_end_date
                               UNION
                              SELECT cia2.claim_item_id
                                FROM claim_item_approver cia2
                               WHERE p_in_start_date < cia2.date_modified
                                 AND cia2.date_modified <= p_in_end_date
                            ) rd,
                            rpt_nomination_detail nd,
                            claim_item_approver cia,
                            promo_nomination_level pnl,
                            activity a
                      WHERE rd.claim_item_id = nd.claim_item_id
                        AND nd.claim_item_id = cia.claim_item_id
                        AND cia.approval_status_type = gc_approval_status_winner
                        AND nd.promotion_id = pnl.promotion_id
                        AND cia.approval_round = pnl.level_index
                        AND pnl.award_payout_type IN (gc_award_type_cash, gc_award_type_points)
                        AND nd.claim_id = a.claim_id
                        AND nd.recvr_user_id = a.user_id
                        AND cia.approval_round = NVL(a.approval_round, 1)
                        AND a.is_submitter = 0
                      GROUP BY nd.ROWID,
                            a.activity_id
                   ) sd,
                   activity_journal aj,
                   journal j
             WHERE sd.activity_id = aj.activity_id (+)
               AND aj.journal_id = j.journal_id (+)
               AND gc_journal_status_post = j.status_type (+)
               AND 1 = j.is_reverse (+)
             GROUP BY sd.nd_rowid
                   --sd.cash_award_amt,--08/03/2016
                   --sd.points_award_amt--08/03/2016
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET cash_award_amt   = s.cash_award_amt,
             points_award_amt = s.points_award_amt,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1
          -- restrict to report records with stale data
       WHERE NOT 
            (   d.cash_award_amt = s.cash_award_amt
            AND d.points_award_amt = s.points_award_amt
            )
         ;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

   v_stage := 'MERGE rpt_nomination_detail (cash/points reverse)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT sd.nd_rowid,
                   -- subtract any award reversal
                   ( SUM(sd.cash_award_amt)                     --08/18/2017  G6-2857
                   - NVL(SUM(DECODE(j.award_type, gc_award_type_cash, j.transaction_cash_amt)), 0)
                   ) AS cash_award_amt,
                   ( SUM(sd.points_award_amt)                   --08/18/2017  G6-2857
                   - NVL(SUM(DECODE(j.award_type, gc_award_type_points, j.transaction_amt)), 0)
                   ) AS points_award_amt
              FROM ( -- sum data by claim item activity for all winning approvals
                     SELECT nd.ROWID AS nd_rowid,
                            a.activity_id,
                            NVL(SUM(DECODE(pnl.award_payout_type, gc_award_type_cash, a.cash_award_qty)), 0) AS cash_award_amt,
                            NVL(SUM(DECODE(pnl.award_payout_type, gc_award_type_points, a.quantity)), 0) AS points_award_amt
                       FROM ( -- get raw data
                              -- union allows for separate index searches
                              SELECT j2.journal_id
                                FROM journal j2
                               WHERE p_in_start_date < j2.date_created
                                 AND j2.date_created <= p_in_end_date
                                 AND j2.status_type = gc_journal_status_post
                                 AND j2.is_reverse = 1
                               UNION
                              SELECT j2.journal_id
                                FROM journal j2
                               WHERE p_in_start_date < j2.date_modified
                                 AND j2.date_modified <= p_in_end_date
                                 AND j2.status_type = gc_journal_status_post
                                 AND j2.is_reverse = 1
                            ) rd,
                            activity_journal aj2,
                            activity a2,
                            claim_item ci,
                            rpt_nomination_detail nd,
                            claim_item_approver cia,
                            promo_nomination_level pnl,
                            activity a
                      WHERE rd.journal_id = aj2.journal_id
                        AND aj2.activity_id = a2.activity_id
                        AND a2.claim_id = ci.claim_id
                        AND ci.claim_item_id = nd.claim_item_id
                        AND nd.recvr_user_id = a2.user_id           --08/18/2017  G6-2857 
                        AND a2.approval_round = cia.approval_round  --08/18/2017  G6-2857
                        AND nd.claim_item_id = cia.claim_item_id
                        AND cia.approval_status_type = gc_approval_status_winner
                        AND nd.promotion_id = pnl.promotion_id
                        AND cia.approval_round = pnl.level_index
                        AND pnl.award_payout_type IN (gc_award_type_cash, gc_award_type_points)
                        AND nd.claim_id = a.claim_id
                        AND nd.recvr_user_id = a.user_id
                        AND cia.approval_round = NVL(a.approval_round, 1)
                        AND a.is_submitter = 0
                      GROUP BY nd.ROWID,
                            a.activity_id
                   ) sd,
                   activity_journal aj,
                   journal j
             WHERE sd.activity_id = aj.activity_id (+)
               AND aj.journal_id = j.journal_id (+)
               AND gc_journal_status_post = j.status_type (+)
               AND 1 = j.is_reverse (+)
             GROUP BY sd.nd_rowid--,
                 --  sd.cash_award_amt,--08/18/2017  G6-2857
                --   sd.points_award_amt--08/18/2017  G6-2857
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET cash_award_amt   = s.cash_award_amt,
             points_award_amt = s.points_award_amt,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1
          -- restrict to report records with stale data
       WHERE NOT 
            (   d.cash_award_amt = s.cash_award_amt
            AND d.points_award_amt = s.points_award_amt
            )
         ;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

   v_stage := 'MERGE rpt_nomination_detail (other)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   ( nd.other_award_amt
                   + NVL(SUM((pnl.payout_value*pnl.quantity)/NVL(bpom_entered_rate,1)), 0)     --G6-1893 03/17/2017 store it as USD
                   ) AS other_award_amt
              FROM claim_item_approver cia,
                   rpt_nomination_detail nd,
                   promo_nomination_level pnl,
                   cash_currency_current cc     --G6-1893 03/17/2017
             WHERE p_in_start_date < cia.date_created
               AND cia.date_created <= p_in_end_date
               AND cia.approval_status_type = gc_approval_status_winner
               AND cia.claim_item_id = nd.claim_item_id
               AND nd.promotion_id = pnl.promotion_id
               AND cia.approval_round = pnl.level_index
               AND pnl.award_payout_type = gc_award_type_other
               AND pnl.payout_currency = cc.to_cur(+)           --G6-1893 03/17/2017
             GROUP BY nd.ROWID,
                   nd.other_award_amt
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET other_award_amt = s.other_award_amt,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1
          -- restrict to report records with stale data
       WHERE NOT (d.other_award_amt = s.other_award_amt)
         ;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ':' || v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END p_refresh_claim_award;

-----------------------
-- Refreshes any modified application user
PROCEDURE p_refresh_app_user
( p_in_user_id       IN NUMBER,
  p_in_start_date    IN DATE, 
  p_in_end_date      IN DATE, 
  p_in_timestamp     IN DATE
) IS
   c_process_name                CONSTANT execution_log.process_name%type  := UPPER('p_refresh_app_user');
   v_stage                       execution_log.text_line%TYPE;
   v_parm_list                   execution_log.text_line%TYPE;
   v_rec_count                   INTEGER;
BEGIN
   v_stage := 'initialzie variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_start_date >' || TO_CHAR(p_in_start_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_end_date >' || TO_CHAR(p_in_end_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_timestamp >' || TO_CHAR(p_in_timestamp,'MM/DD/YYYY HH24:MI:SS')
      || '<~';

   -- refresh stale report records
   v_stage := 'MERGE rpt_nomination_detail (giver)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   rd.pax_status AS giver_pax_status,
                   rd.user_name AS giver_user_name,
                   rd.first_name AS giver_first_name,
                   rd.middle_name AS giver_middle_name,
                   rd.last_name AS giver_last_name
              FROM ( -- get raw data
                     SELECT au.user_id,
                            DECODE(au.is_active, 1, gc_pax_status_active, gc_pax_status_inactive) AS pax_status,
                            au.user_name,
                            INITCAP(au.first_name) AS first_name,
                            INITCAP(TRIM(au.middle_name)) AS middle_name,
                            INITCAP(au.last_name) AS last_name
                       FROM application_user au
                      WHERE p_in_start_date < au.date_modified
                        AND au.date_modified <= p_in_end_date
                   ) rd,
                   rpt_nomination_detail nd
             WHERE rd.user_id = nd.giver_user_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT
                   (   DECODE(rd.pax_status,  nd.giver_pax_status,  1, 0) = 1
                   AND DECODE(rd.user_name,   nd.giver_user_name,   1, 0) = 1
                   AND DECODE(rd.first_name,  nd.giver_first_name,  1, 0) = 1
                   AND DECODE(rd.middle_name, nd.giver_middle_name, 1, 0) = 1
                   AND DECODE(rd.last_name,   nd.giver_last_name,   1, 0) = 1
                   )
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET giver_pax_status  = s.giver_pax_status,
             giver_user_name   = s.giver_user_name,
             giver_first_name  = s.giver_first_name,
             giver_middle_name = s.giver_middle_name,
             giver_last_name   = s.giver_last_name,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

   v_stage := 'MERGE rpt_nomination_detail (receiver)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   rd.pax_status AS recvr_pax_status,
                   rd.user_name AS recvr_user_name,
                   rd.first_name AS recvr_first_name,
                   rd.middle_name AS recvr_middle_name,
                   rd.last_name AS recvr_last_name
              FROM ( -- get raw data
                     SELECT au.user_id,
                            DECODE(au.is_active, 1, gc_pax_status_active, gc_pax_status_inactive) AS pax_status,
                            au.user_name,
                            INITCAP(au.first_name) AS first_name,
                            INITCAP(TRIM(au.middle_name)) AS middle_name,
                            INITCAP(au.last_name) AS last_name
                       FROM application_user au
                      WHERE p_in_start_date < au.date_modified
                        AND au.date_modified <= p_in_end_date
                   ) rd,
                   rpt_nomination_detail nd
             WHERE rd.user_id = nd.recvr_user_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT
                   (   DECODE(rd.pax_status,  nd.recvr_pax_status,  1, 0) = 1
                   AND DECODE(rd.user_name,   nd.recvr_user_name,   1, 0) = 1
                   AND DECODE(rd.first_name,  nd.recvr_first_name,  1, 0) = 1
                   AND DECODE(rd.middle_name, nd.recvr_middle_name, 1, 0) = 1
                   AND DECODE(rd.last_name,   nd.recvr_last_name,   1, 0) = 1
                   )
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET recvr_pax_status  = s.recvr_pax_status,
             recvr_user_name   = s.recvr_user_name,
             recvr_first_name  = s.recvr_first_name,
             recvr_middle_name = s.recvr_middle_name,
             recvr_last_name   = s.recvr_last_name,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

   v_stage := 'MERGE rpt_nomination_detail (round 1)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   rd.user_name  AS r1_approver_user_name,
                   rd.first_name AS r1_approver_first_name,
                   rd.last_name  AS r1_approver_last_name
              FROM ( -- get raw data
                     SELECT au.user_id,
                            au.user_name,
                            INITCAP(au.first_name) AS first_name,
                            INITCAP(au.last_name) AS last_name
                       FROM application_user au
                      WHERE p_in_start_date < au.date_modified
                        AND au.date_modified <= p_in_end_date
                   ) rd,
                   rpt_nomination_detail nd
             WHERE rd.user_id = nd.r1_approver_user_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT
                   (   DECODE(rd.user_name,  nd.r1_approver_user_name,  1, 0) = 1
                   AND DECODE(rd.first_name, nd.r1_approver_first_name, 1, 0) = 1
                   AND DECODE(rd.last_name,  nd.r1_approver_last_name,  1, 0) = 1
                   )
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET r1_approver_user_name  = s.r1_approver_user_name,
             r1_approver_first_name = s.r1_approver_first_name,
             r1_approver_last_name  = s.r1_approver_last_name,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

   v_stage := 'MERGE rpt_nomination_detail (round 2)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   rd.user_name  AS r2_approver_user_name,
                   rd.first_name AS r2_approver_first_name,
                   rd.last_name  AS r2_approver_last_name
              FROM ( -- get raw data
                     SELECT au.user_id,
                            au.user_name,
                            INITCAP(au.first_name) AS first_name,
                            INITCAP(au.last_name) AS last_name
                       FROM application_user au
                      WHERE p_in_start_date < au.date_modified
                        AND au.date_modified <= p_in_end_date
                   ) rd,
                   rpt_nomination_detail nd
             WHERE rd.user_id = nd.r2_approver_user_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT
                   (   DECODE(rd.user_name,  nd.r2_approver_user_name,  1, 0) = 1
                   AND DECODE(rd.first_name, nd.r2_approver_first_name, 1, 0) = 1
                   AND DECODE(rd.last_name,  nd.r2_approver_last_name,  1, 0) = 1
                   )
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET r2_approver_user_name  = s.r2_approver_user_name,
             r2_approver_first_name = s.r2_approver_first_name,
             r2_approver_last_name  = s.r2_approver_last_name,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

   v_stage := 'MERGE rpt_nomination_detail (round 3)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   rd.user_name  AS r3_approver_user_name,
                   rd.first_name AS r3_approver_first_name,
                   rd.last_name  AS r3_approver_last_name
              FROM ( -- get raw data
                     SELECT au.user_id,
                            au.user_name,
                            INITCAP(au.first_name) AS first_name,
                            INITCAP(au.last_name) AS last_name
                       FROM application_user au
                      WHERE p_in_start_date < au.date_modified
                        AND au.date_modified <= p_in_end_date
                   ) rd,
                   rpt_nomination_detail nd
             WHERE rd.user_id = nd.r3_approver_user_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT
                   (   DECODE(rd.user_name,  nd.r3_approver_user_name,  1, 0) = 1
                   AND DECODE(rd.first_name, nd.r3_approver_first_name, 1, 0) = 1
                   AND DECODE(rd.last_name,  nd.r3_approver_last_name,  1, 0) = 1
                   )
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET r3_approver_user_name  = s.r3_approver_user_name,
             r3_approver_first_name = s.r3_approver_first_name,
             r3_approver_last_name  = s.r3_approver_last_name,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

   v_stage := 'MERGE rpt_nomination_detail (round 4)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   rd.user_name  AS r4_approver_user_name,
                   rd.first_name AS r4_approver_first_name,
                   rd.last_name  AS r4_approver_last_name
              FROM ( -- get raw data
                     SELECT au.user_id,
                            au.user_name,
                            INITCAP(au.first_name) AS first_name,
                            INITCAP(au.last_name) AS last_name
                       FROM application_user au
                      WHERE p_in_start_date < au.date_modified
                        AND au.date_modified <= p_in_end_date
                   ) rd,
                   rpt_nomination_detail nd
             WHERE rd.user_id = nd.r4_approver_user_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT
                   (   DECODE(rd.user_name,  nd.r4_approver_user_name,  1, 0) = 1
                   AND DECODE(rd.first_name, nd.r4_approver_first_name, 1, 0) = 1
                   AND DECODE(rd.last_name,  nd.r4_approver_last_name,  1, 0) = 1
                   )
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET r4_approver_user_name  = s.r4_approver_user_name,
             r4_approver_first_name = s.r4_approver_first_name,
             r4_approver_last_name  = s.r4_approver_last_name,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

   v_stage := 'MERGE rpt_nomination_detail (round 5)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   rd.user_name  AS r5_approver_user_name,
                   rd.first_name AS r5_approver_first_name,
                   rd.last_name  AS r5_approver_last_name
              FROM ( -- get raw data
                     SELECT au.user_id,
                            au.user_name,
                            INITCAP(au.first_name) AS first_name,
                            INITCAP(au.last_name) AS last_name
                       FROM application_user au
                      WHERE p_in_start_date < au.date_modified
                        AND au.date_modified <= p_in_end_date
                   ) rd,
                   rpt_nomination_detail nd
             WHERE rd.user_id = nd.r5_approver_user_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT
                   (   DECODE(rd.user_name,  nd.r5_approver_user_name,  1, 0) = 1
                   AND DECODE(rd.first_name, nd.r5_approver_first_name, 1, 0) = 1
                   AND DECODE(rd.last_name,  nd.r5_approver_last_name,  1, 0) = 1
                   )
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET r5_approver_user_name  = s.r5_approver_user_name,
             r5_approver_first_name = s.r5_approver_first_name,
             r5_approver_last_name  = s.r5_approver_last_name,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ':' || v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END p_refresh_app_user;

-----------------------
-- Refreshes any modified node name
PROCEDURE p_refresh_node_name
( p_in_user_id       IN NUMBER,
  p_in_start_date    IN DATE, 
  p_in_end_date      IN DATE, 
  p_in_timestamp     IN DATE
) IS
   c_process_name                CONSTANT execution_log.process_name%type  := UPPER('p_refresh_node_name');
   v_stage                       execution_log.text_line%TYPE;
   v_parm_list                   execution_log.text_line%TYPE;
   v_rec_count                   INTEGER;
BEGIN
   v_stage := 'initialzie variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_start_date >' || TO_CHAR(p_in_start_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_end_date >' || TO_CHAR(p_in_end_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_timestamp >' || TO_CHAR(p_in_timestamp,'MM/DD/YYYY HH24:MI:SS')
      || '<~';

   -- refresh stale report records
   v_stage := 'MERGE rpt_nomination_detail (giver)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   n.name AS giver_node_name
              FROM node n,
                   rpt_nomination_detail nd
             WHERE p_in_start_date < n.date_modified
               AND n.date_modified <= p_in_end_date
               AND n.node_id = nd.giver_node_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT DECODE(n.name, nd.giver_node_name, 1, 0) = 1
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET giver_node_name = s.giver_node_name,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

   v_stage := 'MERGE rpt_nomination_detail (receiver)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   n.name AS recvr_node_name
              FROM node n,
                   rpt_nomination_detail nd
             WHERE p_in_start_date < n.date_modified
               AND n.date_modified <= p_in_end_date
               AND n.node_id = nd.recvr_node_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT DECODE(n.name, nd.recvr_node_name, 1, 0) = 1
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET recvr_node_name = s.recvr_node_name,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ':' || v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END p_refresh_node_name;

-----------------------
-- Refreshes any modified participant address country
PROCEDURE p_refresh_pax_country
( p_in_user_id       IN NUMBER,
  p_in_start_date    IN DATE, 
  p_in_end_date      IN DATE, 
  p_in_timestamp     IN DATE
) IS
   c_process_name                CONSTANT execution_log.process_name%type  := UPPER('p_refresh_pax_country');
   v_stage                       execution_log.text_line%TYPE;
   v_parm_list                   execution_log.text_line%TYPE;
   v_rec_count                   INTEGER;
BEGIN
   v_stage := 'initialzie variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_start_date >' || TO_CHAR(p_in_start_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_end_date >' || TO_CHAR(p_in_end_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_timestamp >' || TO_CHAR(p_in_timestamp,'MM/DD/YYYY HH24:MI:SS')
      || '<~';

   -- refresh stale report records
   v_stage := 'MERGE rpt_nomination_detail (giver)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   rd.country_id AS giver_country_id
              FROM ( -- get raw data
                     -- union allows for separate index searches
                     SELECT ua.user_id,
                            ua.country_id
                       FROM user_address ua
                      WHERE p_in_start_date < ua.date_created
                        AND ua.date_created <= p_in_end_date
                        AND ua.is_primary = 1
                      UNION
                     SELECT ua.user_id,
                            ua.country_id
                       FROM user_address ua
                      WHERE p_in_start_date < ua.date_modified
                        AND ua.date_modified <= p_in_end_date
                        AND ua.is_primary = 1
                   ) rd,
                   rpt_nomination_detail nd
             WHERE rd.user_id = nd.giver_user_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT DECODE(rd.country_id, nd.giver_country_id, 1, 0) = 1
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET giver_country_id = s.giver_country_id,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

   v_stage := 'MERGE rpt_nomination_detail (receiver)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   rd.country_id AS recvr_country_id
              FROM ( -- get raw data
                     -- union allows for separate index searches
                     SELECT ua.user_id,
                            ua.country_id
                       FROM user_address ua
                      WHERE p_in_start_date < ua.date_created
                        AND ua.date_created <= p_in_end_date
                        AND ua.is_primary = 1
                      UNION
                     SELECT ua.user_id,
                            ua.country_id
                       FROM user_address ua
                      WHERE p_in_start_date < ua.date_modified
                        AND ua.date_modified <= p_in_end_date
                        AND ua.is_primary = 1
                   ) rd,
                   rpt_nomination_detail nd
             WHERE rd.user_id = nd.recvr_user_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT DECODE(rd.country_id, nd.recvr_country_id, 1, 0) = 1
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET recvr_country_id = s.recvr_country_id,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ':' || v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END p_refresh_pax_country;

-----------------------
-- Refreshes any modified participant employment
PROCEDURE p_refresh_pax_employer
( p_in_user_id       IN NUMBER,
  p_in_start_date    IN DATE, 
  p_in_end_date      IN DATE, 
  p_in_timestamp     IN DATE
) IS
   c_process_name                CONSTANT execution_log.process_name%type  := UPPER('p_refresh_pax_employer');
   v_stage                       execution_log.text_line%TYPE;
   v_parm_list                   execution_log.text_line%TYPE;
   v_rec_count                   INTEGER;
BEGIN
   v_stage := 'initialzie variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_start_date >' || TO_CHAR(p_in_start_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_end_date >' || TO_CHAR(p_in_end_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_timestamp >' || TO_CHAR(p_in_timestamp,'MM/DD/YYYY HH24:MI:SS')
      || '<~';

   -- refresh stale report records
   v_stage := 'MERGE rpt_nomination_detail (giver)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   rd.position_type AS giver_position_type,
                   rd.department_type AS giver_department_type
              FROM ( -- get raw data
                     -- union allows for separate index searches
                     SELECT paxe.user_id,
                            paxe.position_type,
                            paxe.department_type
                       FROM rpt_participant_employer paxe
                      WHERE p_in_start_date < paxe.date_created
                        AND paxe.date_created <= p_in_end_date
                      UNION
                     SELECT paxe.user_id,
                            paxe.position_type,
                            paxe.department_type
                       FROM rpt_participant_employer paxe
                      WHERE p_in_start_date < paxe.date_modified
                        AND paxe.date_modified <= p_in_end_date
                   ) rd,
                   rpt_nomination_detail nd
             WHERE rd.user_id = nd.giver_user_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT
                   (   DECODE(rd.position_type,   nd.giver_position_type,   1, 0) = 1
                   AND DECODE(rd.department_type, nd.giver_department_type, 1, 0) = 1
                   )
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET giver_position_type   = s.giver_position_type,
             giver_department_type = s.giver_department_type,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

   v_stage := 'MERGE rpt_nomination_detail (receiver)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   rd.position_type AS recvr_position_type,
                   rd.department_type AS recvr_department_type
              FROM ( -- get raw data
                     -- union allows for separate index searches
                     SELECT paxe.user_id,
                            paxe.position_type,
                            paxe.department_type
                       FROM rpt_participant_employer paxe
                      WHERE p_in_start_date < paxe.date_created
                        AND paxe.date_created <= p_in_end_date
                      UNION
                     SELECT paxe.user_id,
                            paxe.position_type,
                            paxe.department_type
                       FROM rpt_participant_employer paxe
                      WHERE p_in_start_date < paxe.date_modified
                        AND paxe.date_modified <= p_in_end_date
                   ) rd,
                   rpt_nomination_detail nd
             WHERE rd.user_id = nd.recvr_user_id
                -- restrict to report records with stale data (decode handles null comparisons)
               AND NOT
                   (   DECODE(rd.position_type,   nd.recvr_position_type,   1, 0) = 1
                   AND DECODE(rd.department_type, nd.recvr_department_type, 1, 0) = 1
                   )
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET recvr_position_type   = s.recvr_position_type,
             recvr_department_type = s.recvr_department_type,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ':' || v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END p_refresh_pax_employer;

-----------------------
-- Refreshes any modified participant organizational role
PROCEDURE p_refresh_pax_org_role
( p_in_user_id       IN NUMBER,
  p_in_start_date    IN DATE, 
  p_in_end_date      IN DATE, 
  p_in_timestamp     IN DATE
) IS
   c_process_name                CONSTANT execution_log.process_name%type  := UPPER('p_refresh_pax_org_role');
   v_stage                       execution_log.text_line%TYPE;
   v_parm_list                   execution_log.text_line%TYPE;
   v_rec_count                   INTEGER;
BEGIN
   v_stage := 'initialzie variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_start_date >' || TO_CHAR(p_in_start_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_end_date >' || TO_CHAR(p_in_end_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_timestamp >' || TO_CHAR(p_in_timestamp,'MM/DD/YYYY HH24:MI:SS')
      || '<~';

   -- refresh stale report records
   v_stage := 'MERGE rpt_nomination_detail (giver)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   rd.role AS giver_org_role
              FROM ( -- get raw data
                     -- union allows for separate index searches
                     SELECT un.user_id,
                            un.node_id,
                            un.role
                       FROM user_node un
                      WHERE p_in_start_date < un.date_created
                        AND un.date_created <= p_in_end_date
                        AND un.is_primary = 1
                        AND un.status = 1
                      UNION
                     SELECT un.user_id,
                            un.node_id,
                            un.role
                       FROM user_node un
                      WHERE p_in_start_date < un.date_modified
                        AND un.date_modified <= p_in_end_date
                        AND un.is_primary = 1
                        AND un.status = 1
                   ) rd,
                   rpt_nomination_detail nd
             WHERE rd.user_id = nd.giver_user_id
               AND rd.node_id = nd.giver_node_id
                -- only report records with stale data (decode handles null comparisons)
               AND NOT DECODE(rd.role, nd.giver_org_role, 1, 0) = 1
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET giver_org_role = s.giver_org_role,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

   v_stage := 'MERGE rpt_nomination_detail (receiver)';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   rd.role AS recvr_org_role
              FROM ( -- get raw data
                     -- union allows for separate index searches
                     SELECT un.user_id,
                            un.node_id,
                            un.role
                       FROM user_node un
                      WHERE p_in_start_date < un.date_created
                        AND un.date_created <= p_in_end_date
                        AND un.is_primary = 1
                        AND un.status = 1
                      UNION
                     SELECT un.user_id,
                            un.node_id,
                            un.role
                       FROM user_node un
                      WHERE p_in_start_date < un.date_modified
                        AND un.date_modified <= p_in_end_date
                        AND un.is_primary = 1
                        AND un.status = 1
                   ) rd,
                   rpt_nomination_detail nd
             WHERE rd.user_id = nd.recvr_user_id
               AND rd.node_id = nd.recvr_node_id
                -- only report records with stale data (decode handles null comparisons)
               AND NOT DECODE(rd.role, nd.recvr_org_role, 1, 0) = 1
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET recvr_org_role = s.recvr_org_role,
             modified_by   = p_in_user_id,
             date_modified = p_in_timestamp,
             version       = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ':' || v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END p_refresh_pax_org_role;

-----------------------
-- Refreshes any modified program associated fields
PROCEDURE p_refresh_program
( p_in_user_id       IN NUMBER,
  p_in_start_date    IN DATE, 
  p_in_end_date      IN DATE, 
  p_in_timestamp     IN DATE
) IS
   c_process_name                CONSTANT execution_log.process_name%type  := UPPER('p_refresh_program');
   v_stage                       execution_log.text_line%TYPE;
   v_parm_list                   execution_log.text_line%TYPE;
   v_rec_count                   INTEGER;
BEGIN
   v_stage := 'initialzie variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_start_date >' || TO_CHAR(p_in_start_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_end_date >' || TO_CHAR(p_in_end_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_timestamp >' || TO_CHAR(p_in_timestamp,'MM/DD/YYYY HH24:MI:SS')
      || '<~';

   -- refresh stale report records
   v_stage := 'MERGE rpt_nomination_detail';
   MERGE INTO rpt_nomination_detail d
   USING (  -- get fresh data for stale report records
            SELECT nd.ROWID AS nd_rowid,
                   p.promotion_name,
                   p.promotion_status
              FROM promotion p,
                   promo_nomination pn,
                   rpt_nomination_detail nd
             WHERE p_in_start_date < p.date_modified
               AND p.date_modified <= p_in_end_date
               AND p.promotion_status IN (gc_promotion_status_active, gc_promotion_status_expired)
               AND p.is_deleted = 0
               AND p.promotion_id = pn.promotion_id
               AND p.promotion_id = nd.promotion_id
                -- restrict to report records with stale data
               AND NOT
                   (   p.promotion_name = nd.promotion_name
                   AND p.promotion_status = nd.promotion_status
                   )
         ) s
      ON (d.ROWID = s.nd_rowid)
    WHEN MATCHED THEN
      UPDATE
         SET promotion_name = s.promotion_name,
             promotion_status = s.promotion_status,
             modified_by = p_in_user_id,
             date_modified = p_in_timestamp,
             version = d.version + 1;

   v_rec_count := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ':' || v_stage || ' (' || v_rec_count || ' records' || ')', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ':' || v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END p_refresh_program;

-----------------------
-- public processes
-----------------------
PROCEDURE p_rpt_nomination_detail
( p_in_user_id       IN NUMBER,
  p_in_start_date    IN DATE, 
  p_in_end_date      IN DATE, 
  p_out_return_code  OUT NUMBER,
  p_out_err_msg      OUT VARCHAR2
) IS
/*******************************************************************************
   Purpose: Controls the report nomination detail/summary tables refresh.
            There are 3 kinds of nominations.
             Regular --> 1 giver (nominator) and 1 receiver (nominee).
             Team --> 1 giver (nominator) and many receivers (nomminees).
             Group --> Many givers (nominators) and 1 receiver (nominee) where
                       the claims are cumulative and rolled into 1 group.


    Person        Date        Comments
    -----------   ----------  -----------------------------------------------------
    D Murray      05/08/2006  Initial Creation
    D Murray      05/17/2006  Added summary report to the report_refresh_date table
                              because Java code is looking for it to populate a date/time
                              at the top of iReports. (Bug 12207)
    D Murray      05/18/2006  Need to exclude records from the iReport if the promotion_notification
                              is turned off for the nominee or nominee manager on submission until
                              the notification date has been reached once the nomination has been
                              approved or denied.
    Percy M.      08/02/2006  Fix for bug# 12942 and 12983
    Percy M.      08/28/2006  Fix for bug# 13367
    Raju  N       01/26/2006  removed the commented out code.
    Raju  N       01/26/2006  Bug#15150 fixed the behavior to be populated with value from pick list instead of code.
    Raju N        01/27/2006  Fix for BUg# 14854  added new function fnc_display_report_flag.
    Raju N        01/31/2006  Changed the code to get the award amount by claim_group_id for cumulative claims. Bu#15133
    John E.       04/18/2007  Adding final approver to rpt_nomination_detail table to be pulled by report extract
    Sandip M.     10/24/2007  Bug # 18158, when NOMINATION_CLAIM.BEHAVIOR is NULL no need to do a CM Lookup                            
    Arun S        01/20/2011  Bug # 34810 Fix, Added claim table to cur_grp_recvr
    Arun S        01/20/2011  Bug # 35967 and 35988 Fix, For Cumulative type group claim populated award_amt and 
                              won_flag for one nominator instead of poulating for all nominator
    Arun S        06/08/2011  Bug 37328 fix added new column evaluation_type,award_amount_fixed and populated
    nagarajs      03/21/2012  Bug #40345 fix to generate Nomination report for live and expired promotions
                              but not for deleted promotion. 
    Arun S        07/25/2012  G5 report changes
                              Modified process with incremental approach based on input p_start_date and p_end_date
                              Added logic to process Giver or Receiver info changed in rpt_nom_behavior_summary     
    Ravi Dhanekula 10/25/2012 Added processing for badges_earned.
                  01/28/2013  Made changes to exclude pending transactions
                  10/07/2013  Change required to include merch level sweepstakes.
    Swati         05/26/2014  Fixed Bug # 51044 Reverse transactions are not accounted for in reporting
    Ramkumar      02/04/2015  Bug # 59416 Replace giver/receiver with submitted/received 
                              for nomination behavior summary report table      
    J Flees       04/12/2016  G5 Redesign - refactored process with smaller sub-processes
    nagarajs      08/03/2016  Bug 67913 - Admin View \ Processes \Report Refresh process : Report Refresh
                              process fails and an exception occurs when trying to launch
    Chidamba      08/18/2017  G6-2857 - Nomination Pax Transaction History - Other Pax points are displaying and doing reversal,Application error occurs and also report refresh fails
*******************************************************************************/
   --execution log variables
   c_process_name                CONSTANT execution_log.process_name%type  := UPPER('p_rpt_nomination_detail');
   c_timestamp                   CONSTANT DATE := SYSDATE;
   v_stage                       execution_log.text_line%TYPE;

BEGIN
   -- log process start
   v_stage := 'Start'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<, p_in_start_date >' || TO_CHAR(p_in_start_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, p_in_end_date >' || TO_CHAR(p_in_end_date,'MM/DD/YYYY HH24:MI:SS')
      || '<, c_timestamp >' || TO_CHAR(c_timestamp,'MM/DD/YYYY HH24:MI:SS')
      || '<';
   prc_execution_log_entry(c_process_name, gc_release_level, gc_info, v_stage, NULL);

   -- add new detail records
   v_stage := 'add new detail records';
   p_refresh_new_sweeps(p_in_user_id, p_in_start_date, p_in_end_date, c_timestamp); 
   p_refresh_new_claim (p_in_user_id, p_in_start_date, p_in_end_date, c_timestamp); 

   -- refresh modified records
   p_refresh_claim         (p_in_user_id, p_in_start_date, p_in_end_date, c_timestamp);
   p_refresh_claim_item    (p_in_user_id, p_in_start_date, p_in_end_date, c_timestamp);
   p_refresh_claim_approval(p_in_user_id, p_in_start_date, p_in_end_date, c_timestamp);
   p_refresh_claim_award   (p_in_user_id, p_in_start_date, p_in_end_date, c_timestamp);
   p_refresh_app_user      (p_in_user_id, p_in_start_date, p_in_end_date, c_timestamp);
   p_refresh_node_name     (p_in_user_id, p_in_start_date, p_in_end_date, c_timestamp);
   p_refresh_pax_country   (p_in_user_id, p_in_start_date, p_in_end_date, c_timestamp);
   p_refresh_pax_employer  (p_in_user_id, p_in_start_date, p_in_end_date, c_timestamp);
   p_refresh_pax_org_role  (p_in_user_id, p_in_start_date, p_in_end_date, c_timestamp);
   p_refresh_program       (p_in_user_id, p_in_start_date, p_in_end_date, c_timestamp); 

   -- successful
   p_out_return_code := gc_return_code_success;
   v_stage := 'Success';
   prc_execution_log_entry(c_process_name, gc_release_level, gc_info, v_stage, NULL);

EXCEPTION
   WHEN OTHERS THEN
      p_out_return_code := gc_return_code_failure;
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || '~' || SQLCODE || ':' || SQLERRM, NULL);
END p_rpt_nomination_detail;

END pkg_report_nomination;
/
