CREATE OR REPLACE PACKAGE pkg_query_individual_activity AS
/******************************************************************************
   NAME:       pkg_query_Individual_Activity
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        7/29/2014      keshaboi       1. Created this package.
             10/01/2014    Suresh J        Bug Fix 57070,57071,57076 Updated Char and Summary queries for Challengepoint Points changes 
******************************************************************************/

  /* getIndividualActivityPointsReceivedChart */
   PROCEDURE prc_getPointsReceived(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR);
    /* getIndividualActivityPointsGivenReceivedChart */
PROCEDURE prc_getPointsGivenReceived(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR);
   /* getIndividualActivityTotalActivityChart */
PROCEDURE prc_getTotalActivityChart(
 p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR);
   /* getIndividualActivityMetricsChart */
PROCEDURE prc_getMetricsChart(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR);
   /* getIndividualActivityTabularResults */
procedure prc_getTabularResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR);
   /* getIndividualActivityGoalQuestTabularResults */
procedure prc_getGoalQuestTabularResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR);
   /* getIndividualActivityChallengePointTabularResults */
procedure prc_getCPTabularResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR);
   /* getIndividualActivityOnTheSpotTabularResults */
procedure prc_getOnTheSpotTabularResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR);
   /* INDIVIDUAL ACTIVITY REPORT - AWARDS RECEIVED */
/* getIndividualActivityAwardsReceivedTabularResults */
procedure prc_getAwardsReceived(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR);
   /* INDIVIDUAL ACTIVITY REPORT - NOMINATIONS RECEIVE */
/* getIndividualActivityNominationsReceivedTabularResults */
procedure prc_getNominationsReceived(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER);
   /* INDIVIDUAL ACTIVITY REPORT - NOMINATIONS GIVEN */
/* getIndividualActivityNominationsGivenTabularResults */
procedure prc_getNominationsGiven(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER);
   /* INDIVIDUAL ACTIVITY REPORT - PRODUCT CLAIM */
/* getIndividualActivityProductClaimTabularResults */
procedure prc_getProductClaim(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER);
   /* INDIVIDUAL ACTIVITY REPORT - RECOGNITIONS GIVEN */
/* getIndividualActivityRecognitionsGivenTabularResults */
procedure prc_getRecognitionsGiven(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER);
   /* INDIVIDUAL ACTIVITY REPORT - RECOGNITIONS RECEIVED */
/* getIndividualActivityRecognitionsReceivedTabularResults */
procedure prc_getRecognitionsReceived(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER);
    
/* INDIVIDUAL ACTIVITY REPORT - QUIZ */
/* getIndividualActivityQuizTabularResults */
procedure prc_getQuizTabularResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR);
   /* INDIVIDUAL ACTIVITY REPORT - THROWDOWN */
/* getIndividualActivityThrowdownTabularResults */
procedure prc_getThrowdownTabularResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR);
   /* QUIZ DETAIL REPORTS */
/* getIndividualActivityQuizDetailResults */
procedure prc_getQuizDetailResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER);
   /* THROWDOWN DETAIL REPORTS */
/* getIndividualActivityThrowdownDetailResults */
procedure prc_getThrowdownDetailResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR);
   
   procedure prc_getBadgeTabularResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR);
   
   procedure prc_getSSIContests(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,   
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER);
  
END pkg_query_Individual_Activity;
/
CREATE OR REPLACE PACKAGE BODY pkg_query_Individual_Activity
AS
/******************************************************************************
   NAME:       prc_getPointsReceived
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
             10/06/2014    Suresh J        Bug Fix 57183 - Added GQ Query and updated WHERE condition for the TRANS_DATE column  
             1/23/2015     Ravi Dhanekula  Bug # 59285 - Add Badge to summary and for the charts too.
             04/29/2015    Swati           SSI - Phase 2 Include SSI points to individual activity Graphs
             05/28/2015    Swati           Bug 62471 - Reports - Individual Activity Report - SSI Contest - Points are not displayed in Summary and Charts
             06/04/2015    Swati           Bug 62563 - Reports - Individual Activity Report - SSI Contest - Points are not Matching between Summary and Charts
             07/08/2015   Ravi Dhanekula   Bug #63158 - Individual Activity - Individual Points Received chart is showing value twice the actual value
             07/16/2015   Suresh J         Bug 63070 - Chart & Summary is not matching  
             08/25/2015   Venkateswarlu M  Bug 63516 - Reports - Individual Activity Report - Chart execution for testing is not matching 
             03/02/2018   Chidamba         G6-3432 - Changes to the Report due to OTS cards Enhancement 
******************************************************************************/
 /* getIndividualActivityPointsReceivedChart */
   PROCEDURE prc_getPointsReceived(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId         IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getPointsReceived' ;
      v_stage                VARCHAR2 (500);
 BEGIN
      v_stage   := 'getPointsReceived';
      OPEN p_out_data FOR
 SELECT month_trans.cms_name 
       || '-' 
       || YEAR AS months, 
       total_points_count 
     FROM 
       (WITH month_list AS 
       (SELECT TO_CHAR(ADD_MONTHS(TRUNC(TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM - 1), 'MON' ) MONTH, 
         TO_CHAR(ADD_MONTHS(TRUNC(TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM       - 1), 'yy' ) YEAR, 
         ADD_MONTHS(TRUNC(TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM               - 1) month_sort 
       FROM dual 
         CONNECT BY LEVEL <= MONTHS_BETWEEN(TRUNC(TO_DATE(p_in_toDate,p_in_localeDatePattern), 'mm'), TRUNC(TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm')) + 1 
       ) 
     SELECT ml.month, 
       ml.year, 
       NVL(rpt_c.totalpoints, 0) total_points_count 
     FROM 
       (SELECT SUM(DTL.POINTS) totalpoints, 
         TO_CHAR(DATE_SUBMITTED,'MON') AS MONTH , 
         TO_CHAR(DATE_SUBMITTED,'YY')  AS YEAR 
       FROM 
         (
         SELECT TRUNC (payout_issue_date) AS DATE_SUBMITTED,  --1/23/2015
           SUM (NVL (points, 0))     AS POINTS 
         --FROM rpt_ssi_award_detail -- 07/08/2015--Bug # 63158--Changes rpt_awardmedia_detail to rpt_ssi_award-detail to avoid duplicate data counted.
         FROM rpt_ssi_contest_detail -- 08/25/2015 Bug # 63516
         WHERE user_id = NVL (p_in_paxId, user_id) 
         AND NVL (TRUNC (payout_issue_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         GROUP BY TRUNC (payout_issue_date)   
         UNION ALL --06/04/2015 uncommented code as a part of Bug 62563
         SELECT TRUNC (date_submitted) AS DATE_SUBMITTED,  --1/23/2015
           SUM (NVL (points_award_amt, 0))     AS POINTS  --07/01/2016
         FROM rpt_nomination_detail 
         WHERE recvr_user_id = NVL (p_in_paxId, recvr_user_id) 
         AND NVL (TRUNC (date_submitted), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         GROUP BY TRUNC (date_submitted)    
          UNION ALL      --08/25/2015 Code as a part of Bug 63516   
          SELECT TRUNC (date_submitted) AS DATE_SUBMITTED,  
           SUM (NVL (points_award_amt, 0))     AS POINTS  --07/01/2016
         FROM rpt_nomination_detail 
         WHERE giver_user_id = NVL (p_in_paxId, recvr_user_id) 
         AND NVL (TRUNC (date_submitted), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         GROUP BY TRUNC (date_submitted)         
         UNION ALL 
         SELECT TRUNC (date_submitted)     AS DATE_SUBMITTED, 
           SUM (NVL (submitter_points, 0)) AS POINTS 
         FROM rpt_claim_detail 
         WHERE submitter_user_id = NVL (p_in_paxId, submitter_user_id) 
         AND NVL (TRUNC (date_submitted), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         GROUP BY TRUNC (date_submitted) 
         UNION ALL 
         SELECT TRUNC (date_approved)                                  AS DATE_SUBMITTED, 
           SUM ( NVL ( DECODE (award_type, 'points', award_amt, 0), 0)) AS POINTS 
         FROM rpt_recognition_detail 
         WHERE recvr_user_id = NVL (p_in_paxId, recvr_user_id) 
         AND NVL (TRUNC (date_approved), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         GROUP BY TRUNC (date_approved) 
         UNION ALL --07/16/2015
         SELECT TRUNC (date_approved)                                  AS DATE_SUBMITTED, 
           SUM ( NVL ( DECODE (award_type, 'points', award_amt, 0), 0)) AS POINTS 
         FROM rpt_recognition_detail   --07/16/2015
         WHERE giver_user_id = NVL (p_in_paxId, giver_user_id)   --07/16/2015
         AND NVL (TRUNC (date_approved), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         GROUP BY TRUNC (date_approved) 
         UNION ALL 
         SELECT TRUNC (trans_date)  AS DATE_SUBMITTED, 
           SUM (transaction_amount) AS POINTS 
         FROM rpt_qcard_detail 
         WHERE user_id = NVL (p_in_paxId, user_id) 
         AND NVL (TRUNC (trans_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         GROUP BY TRUNC (trans_date) 
         UNION ALL 
         SELECT TRUNC (quiz_date)      AS DATE_SUBMITTED, 
           SUM (NVL (award_amount, 0)) AS POINTS 
         FROM rpt_quiz_activity_detail 
         WHERE participant_id = NVL (p_in_paxId, participant_id) 
         AND NVL (TRUNC (quiz_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         GROUP BY TRUNC (quiz_date) 
         UNION ALL 
         SELECT TRUNC (payout_date) AS DATE_SUBMITTED, 
           SUM (NVL (payout, 0))    AS POINTS 
         FROM rpt_throwdown_activity 
         WHERE user_id = NVL (p_in_paxId, user_id) 
         AND NVL (TRUNC (payout_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         GROUP BY TRUNC (payout_date)
         UNION ALL --10/01/2014
         SELECT TRUNC (trans_date)  AS DATE_SUBMITTED,   --10/01/2014 
          NVL(SUM(calculated_payout),0)+NVL(SUM(basic_award_deposited),0) AS POINTS   --10/01/2014 
         FROM rpt_cp_selection_detail    --10/01/2014
         WHERE user_id = NVL (p_in_paxId, user_id)   --10/01/2014
         AND TRUNC (trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)  --10/01/2014 
         GROUP BY TRUNC (trans_date)  --10/01/2014 
         UNION ALL  --10/06/2014
         SELECT TRUNC (trans_date)  AS DATE_SUBMITTED,   --10/06/2014 
         NVL(SUM(calculated_payout),0) AS POINTS   --10/06/2014 
         FROM rpt_goal_selection_detail    --10/06/2014
         WHERE user_id = NVL (p_in_paxId, user_id)   --10/06/2014
         AND TRUNC (trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)  --10/06/2014 
         GROUP BY TRUNC (trans_date)  --10/06/2014 
         UNION ALL
          SELECT TRUNC (media_date)  AS DATE_SUBMITTED,   --10/06/2014 
         NVL(SUM(media_amount),0) AS POINTS   --10/06/2014 
         FROM rpt_awardmedia_detail r 
         WHERE user_id = NVL(p_in_paxId, user_id) 
         AND promotion_type = 'badge'
         AND media_date BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
         GROUP BY TRUNC (media_date)
         )DTL 
       GROUP BY TO_CHAR(DATE_SUBMITTED,'MON') , 
         TO_CHAR(DATE_SUBMITTED,'YY') 
       ) rpt_c, 
       month_list ml 
     WHERE ml.month = rpt_c.month(+) 
     AND ml.year    = rpt_c.year(+) 
     ORDER BY ml.month_sort 
       ) ss, 
            (SELECT cms_code,
                    cms_name 
               FROM vw_cms_code_value vw 
              WHERE asset_code = 'picklist.monthname.type.items' and locale = (SELECT string_val FROM os_propertyset WHERE entity_name = 'default.language') 
                AND NOT EXISTS (SELECT * FROM vw_cms_code_value WHERE asset_code = 'picklist.monthname.type.items' and locale = p_in_languageCode) 
             UNION ALL 
              SELECT cms_code,
                     cms_name 
                FROM vw_cms_code_value 
               WHERE asset_code = 'picklist.monthname.type.items' and locale = p_in_languageCode ) month_trans 
     WHERE UPPER(month_trans.cms_code)=UPPER(ss.month);  
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
         OPEN p_out_data FOR SELECT NULL FROM DUAL;
 END prc_getPointsReceived;
  /* getIndividualActivityPointsGivenReceivedChart */
PROCEDURE prc_getPointsGivenReceived(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getPointsGivenReceived' ;
      v_stage                VARCHAR2 (500);
 BEGIN
      v_stage   := 'getPointsGivenReceived';
      OPEN p_out_data FOR
 SELECT user_id         AS USER_ID, 
       PROMOTION_NAME       AS PROMOTION_NAME, 
       SUM(POINTS_RECEIVED) AS POINTS_RECEIVED, 
       SUM(POINTS_GIVEN)    AS POINTS_GIVEN 
     FROM 
       (SELECT promotion_name AS PROMOTION_NAME, 
         SUM(NVL(points_award_amt,0)) AS POINTS_RECEIVED, --07/01/2016
         0                     AS POINTS_GIVEN, 
         recvr_user_id user_id 
       FROM rpt_nomination_detail 
       WHERE recvr_user_id = NVL(p_in_paxId, recvr_user_id) 
       AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       GROUP BY recvr_user_id, 
         promotion_name 
         UNION
         SELECT promotion_name AS PROMOTION_NAME, 
         SUM(NVL(calculated_payout,0)) AS POINTS_RECEIVED, 
         0                     AS POINTS_GIVEN, 
         user_id                      
         FROM rpt_goal_selection_detail 
         WHERE user_id = NVL(p_in_paxId, user_id) 
         AND TRUNC(trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         GROUP BY user_id, 
         promotion_name                 
       UNION 
       SELECT promotion_name   AS PROMOTION_NAME, 
         0                     AS POINTS_RECEIVED, 
         SUM(NVL(points_award_amt,0)) AS POINTS_GIVEN,  --07/01/2016
         giver_user_id user_id 
       FROM rpt_nomination_detail 
       WHERE giver_user_id = NVL(p_in_paxId, giver_user_id) 
       AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       GROUP BY giver_user_id, 
         promotion_name 
       UNION 
       SELECT promotion_name          AS PROMOTION_NAME, 
         SUM(NVL(submitter_points,0)) AS POINTS_RECEIVED, 
         0                            AS POINTS_GIVEN, 
         submitter_user_id user_id 
       FROM rpt_claim_detail 
       WHERE submitter_user_id = NVL(p_in_paxId, submitter_user_id) 
       AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       GROUP BY submitter_user_id, 
         promotion_name 
       UNION 
       SELECT promotion_name                                    AS PROMOTION_NAME, 
         SUM(NVL(DECODE(award_type, 'points', award_amt, 0),0)) AS POINTS_RECEIVED, 
         0                                                      AS POINTS_GIVEN, 
         recvr_user_id user_id 
       FROM rpt_recognition_detail 
       WHERE recvr_user_id = NVL(p_in_paxId, recvr_user_id) 
       AND NVL(TRUNC(date_approved), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       GROUP BY recvr_user_id, 
         promotion_name 
       UNION 
       SELECT promotion_name                                    AS PROMOTION_NAME, 
         0                                                      AS POINTS_RECEIVED, 
         SUM(NVL(DECODE(award_type, 'points', award_amt, 0),0)) AS POINTS_GIVEN, 
         giver_user_id user_id 
       FROM rpt_recognition_detail 
       WHERE giver_user_id = NVL(p_in_paxId, giver_user_id) 
       AND NVL(TRUNC(date_approved), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       GROUP BY giver_user_id, 
         promotion_name 
       UNION 
       SELECT promo_quiz_name     AS PROMOTION_NAME, 
         SUM(NVL(award_amount,0)) AS POINTS_RECEIVED, 
         0                        AS POINTS_GIVEN, 
         participant_id user_id 
       FROM rpt_quiz_activity_detail 
       WHERE participant_id = NVL(p_in_paxId, participant_id) 
       AND NVL(TRUNC(quiz_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       GROUP BY participant_id, 
         promo_quiz_name 
       UNION 
       SELECT promotion_name AS PROMOTION_NAME, 
         SUM(NVL(payout,0))  AS POINTS_RECEIVED, 
         0                   AS POINTS_GIVEN, 
         user_id 
       FROM rpt_throwdown_activity 
       WHERE user_id = NVL(p_in_paxId, user_id) 
       GROUP BY user_id, 
         promotion_name 
       UNION  --10/01/2014
       SELECT promotion_name     AS PROMOTION_NAME,   --10/01/2014
           NVL(SUM(calculated_payout),0)+NVL(SUM(basic_award_deposited),0) AS POINTS_RECEIVED,   --10/01/2014        
         0                        AS POINTS_GIVEN, --10/01/2014
         user_id user_id    --10/01/2014
         FROM rpt_cp_selection_detail    --10/01/2014
         WHERE user_id = NVL (p_in_paxId, user_id)   --10/01/2014
         AND TRUNC (trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)  --10/01/2014 --10/06/2014
         GROUP BY user_id,promotion_name  --10/01/2014 
       UNION --04/29/2015
       SELECT promotion_name AS PROMOTION_NAME, 
         SUM(NVL(POINTS,0))  AS POINTS_RECEIVED, 
         0                   AS POINTS_GIVEN, 
         user_id 
       FROM rpt_ssi_contest_detail 
       WHERE user_id = NVL(p_in_paxId, user_id) 
       GROUP BY user_id, 
         promotion_name 
       ) 
     GROUP BY user_id, 
       promotion_name 
     ORDER BY promotion_name  ;
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
         OPEN p_out_data FOR SELECT NULL FROM DUAL;
 END prc_getPointsGivenReceived; 
/* getIndividualActivityTotalActivityChart */
PROCEDURE prc_getTotalActivityChart(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR)
   IS
/******************************************************************************
   NAME:       prc_getTotalActivityChart
   PURPOSE:

   REVISIONS:
    Author            Date        Description
    ---------          ----------  ---------------  
    Swati            10/06/2014  Bug 57210 - Reports - Individual Activity - Pie Chart Issues  GQ
                    10/07/2014    Bug 57210 - Reports - Individual Activity - Pie Chart Issues  CP
    Swati           05/28/2015  Bug 62471 - Reports - Individual Activity Report - SSI Contest - Points are not displayed in Summary and Charts
    chidamba        03/02/2018  G6-3432 - Changes to the Report due to OTS cards Enhancement 
******************************************************************************/   
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getTotalActivityChart' ;
      v_stage                VARCHAR2 (500);
      gc_str_token      CONSTANT VARCHAR2 (500):= '#';  --03/02/2018
 BEGIN
    /*********************03/02/2018 Start********************************/
    DELETE temp_table_session;
    INSERT INTO temp_table_session (asset_code, cms_name, cms_code)
    SELECT asset_code, cms_value, key
      FROM mv_cms_asset_value
     WHERE key = 'OTS_BATCH_NAME_'
           AND locale = p_in_languageCode;
    /**********************03/02/2018 End****************************/
           
      v_stage   := 'getTotalActivityChart';
      OPEN p_out_data FOR
SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','challengePoint',p_in_languageCode) AS Activity,  --10/01/2014
  COUNT(rpt_cp_selection_detail_id)                                                                           AS Act_count   --10/01/2014
FROM rpt_cp_selection_detail  --10/01/2014
WHERE user_id = NVL(p_in_paxId, user_id)  --10/01/2014
AND TRUNC(trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)  --10/01/2014 -- 10/07/2014 Bug 57210
UNION --10/01/2014
SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','goalQuest',p_in_languageCode) AS Activity,
  COUNT(rpt_goal_selection_detail_id)                                                                           AS Act_count
FROM rpt_goal_selection_detail
WHERE user_id = NVL(p_in_paxId, user_id)
AND TRUNC(trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) -- 10/06/2014 Bug 57210
UNION
SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','nominationsReceived',p_in_languageCode) AS Activity,
  COUNT(claim_id)                                                                                                         AS Act_count
FROM rpt_nomination_detail
WHERE recvr_user_id = NVL(p_in_paxId, recvr_user_id)
AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
UNION
SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','nominationsGiven',p_in_languageCode) AS Activity,
  COUNT(claim_id)                                                                                                      AS Act_count
FROM rpt_nomination_detail
WHERE giver_user_id = NVL(p_in_paxId, giver_user_id)
AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
UNION
SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','productClaims',p_in_languageCode) AS Activity,
  COUNT(RPT_CLAIM_DETAIL_ID)                                                                                        AS Act_count
FROM rpt_claim_detail
WHERE submitter_user_id = NVL(p_in_paxId, submitter_user_id)
AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
UNION
SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','recognitionsReceived',p_in_languageCode) AS Activity,
  COUNT(claim_id)                                                                                                          AS Act_count
FROM rpt_recognition_detail
WHERE recvr_user_id = NVL(p_in_paxId, recvr_user_id)
AND NVL(TRUNC(date_approved), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
UNION
SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','recognitionsGiven',p_in_languageCode) AS Activity,
  COUNT(claim_id)                                                                                                       AS Act_count
FROM rpt_recognition_detail
WHERE giver_user_id = NVL(p_in_paxId, giver_user_id)
AND NVL(TRUNC(date_approved), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
UNION
SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','quizzes',p_in_languageCode) AS Activity,
  COUNT(quiz_claim_id)                                                                                        AS Act_count
FROM rpt_quiz_activity_detail
WHERE participant_id = NVL(p_in_paxId, participant_id)
AND NVL(TRUNC(quiz_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
UNION
SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','throwdown',p_in_languageCode) AS Activity,
  COUNT(rpt_throwdown_activity_id)                                                                              AS Act_count
FROM rpt_throwdown_activity
WHERE user_id = NVL(p_in_paxId, user_id)
AND NVL(TRUNC(payout_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
UNION
SELECT CASE WHEN r.batch_description IS NULL THEN 
        fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','onTheSpot',p_in_languageCode)
        WHEN r.batch_description like '%'||gc_str_token THEN REPLACE(r.batch_description,gc_str_token)            
        ELSE (SELECT t.cms_name FROM temp_table_session t WHERE t.asset_code= r.batch_description) END   AS Activity,--03/02/2018 
  COUNT(qcard_detail_id)                                                                                        AS Act_count
FROM rpt_qcard_detail r
WHERE user_id = NVL(p_in_paxId, user_id)
AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
group by r.batch_description --03/02/2018
UNION --05/28/2015 Bug 62471
SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','ssi_contest',p_in_languageCode) AS Activity,
  SUM (NVL (points, 0))                                                                                        AS Act_count
FROM rpt_ssi_contest_detail r
WHERE user_id = NVL(p_in_paxId, user_id)
AND NVL(TRUNC(PAYOUT_ISSUE_DATE), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern);

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
         OPEN p_out_data FOR SELECT NULL FROM DUAL;
 END prc_getTotalActivityChart;  
/* getIndividualActivityMetricsChart */
PROCEDURE prc_getMetricsChart(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getMetricsChart' ;
      v_stage                VARCHAR2 (500);
 BEGIN
      v_stage   := 'getMetricsChart';
      OPEN p_out_data FOR
SELECT au.usr_name AS USER_NAME,
  u_g.usr_rec_g    AS USER_RECOGNITIONS_SENT,
  u_r.usr_rec_r    AS USER_RECOGNITIONS_RCVD,
  CASE
    WHEN o_r.org_rec_g = 0
    THEN 0
    ELSE ROUND(o_r.org_rec_g/o_r.org_total_user, 2)
  END AS ORG_AVG_RECOGNITIONS_SENT,
  CASE
    WHEN o_r.org_rec_r = 0
    THEN 0
    ELSE ROUND(o_r.org_rec_r/o_r.org_total_user, 2)
  END AS ORG_AVG_RECOGNITIONS_RCVD,
  CASE
    WHEN c.company_rec_g = 0
    THEN 0
    ELSE ROUND(c.company_rec_g/c.company_user_count, 2)
  END AS COMPANY_AVG_RECOGNITIONS_SENT,
  CASE
    WHEN c.company_rec_r = 0
    THEN 0
    ELSE ROUND(c.company_rec_r/c.company_user_count, 2)
  END AS COMPANY_AVG_RECOGNITIONS_RCVD
FROM
  (SELECT COUNT(giver_user_id) usr_rec_g
  FROM rpt_recognition_detail
  WHERE giver_user_id = p_in_paxId
  AND TRUNC(date_approved) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
  ) u_g,
  (SELECT au.first_name
    || ' '
    || au.last_name AS usr_name
  FROM application_user au
  WHERE user_id = p_in_paxId
  ) AU,
  (SELECT COUNT(claim_id) usr_rec_r
  FROM rpt_recognition_detail
  WHERE recvr_user_id = p_in_paxId
  AND TRUNC(date_approved) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
  ) u_r,
  (SELECT pkg_report_recognition.fnc_actual_recognition_count (un.node_id,NULL,NULL,NULL,NULL,TO_DATE(p_in_fromDate,p_in_localeDatePattern),TO_DATE(p_in_toDate,p_in_localeDatePattern),'giver','team', NULL,NULL,NULL) org_rec_g,
    pkg_report_recognition.fnc_actual_recognition_count (un.node_id,NULL,NULL,NULL,NULL,TO_DATE(p_in_fromDate,p_in_localeDatePattern),TO_DATE(p_in_toDate,p_in_localeDatePattern),'receiver','team', NULL,NULL,NULL) org_rec_r,
    COUNT(uc.user_id) org_total_user
  FROM user_node un,
    user_node uc
  WHERE un.node_id = uc.node_id
  AND un.user_id   = p_in_paxId
  GROUP BY un.node_id
  ) o_r,
  (SELECT pkg_report_recognition.fnc_actual_recognition_count (c_n.node_id,NULL,NULL,NULL,NULL,TO_DATE(p_in_fromDate,p_in_localeDatePattern),TO_DATE(p_in_toDate,p_in_localeDatePattern),'giver','node', NULL,NULL,NULL) company_rec_g,
    pkg_report_recognition.fnc_actual_recognition_count (c_n.node_id,NULL,NULL,NULL,NULL,TO_DATE(p_in_fromDate,p_in_localeDatePattern),TO_DATE(p_in_toDate,p_in_localeDatePattern),'receiver','node', NULL,NULL,NULL) company_rec_r,
    c_u.company_user_count
  FROM
    (SELECT child_node_id AS node_id
    FROM rpt_hierarchy_rollup
    WHERE node_id IN
      (SELECT node_id FROM user_node WHERE user_id = p_in_paxId
      )
    ) c_n,
    (SELECT COUNT(user_id) company_user_count
    FROM user_node
    WHERE node_id IN
      (SELECT node_id
      FROM node
        CONNECT BY PRIOR node_id = parent_node_id
        START WITH node_id      IN
        (SELECT node_id
        FROM rpt_hierarchy
        WHERE hier_level                  = 1
          CONNECT BY PRIOR parent_node_id = node_id
          START WITH node_id             IN
          (SELECT node_id FROM user_node WHERE user_id = p_in_paxId
          )
        )
      )
    ) c_u
  ) c ;
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
         OPEN p_out_data FOR SELECT NULL FROM DUAL;
 END prc_getMetricsChart; 
/* getIndividualActivityTabularResults */
/* getIndividualActivityTabularResults */
procedure prc_getTabularResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getTabularResults';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
  gc_str_token    CONSTANT VARCHAR2 (500):= '#';  --03/02/2018
   BEGIN
       /*********************03/02/2018 Start********************************/
        DELETE temp_table_session;
        INSERT INTO temp_table_session (asset_code, cms_name, cms_code)
        SELECT asset_code, cms_value, key
          FROM mv_cms_asset_value
         WHERE key = 'OTS_BATCH_NAME_'
               AND locale = p_in_languageCode;
    /**********************03/02/2018 End****************************/
      v_stage   := 'getTabularResults';      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROWNUM RN, 
         rs.* 
       FROM 
         (SELECT fnc_cms_picklist_value(''picklist.report.individualactivity.module.type.items'',''goalQuest'','''||p_in_languageCode||''') AS Activity, 
           ''goalQuest''                                                                                                  AS ACTIVITY_CODE, 
           NVL(SUM(calculated_payout),0)                                                                                  AS Points, 
           NVL(NULL,0)                                                                                                    AS Plateau_Earned, 
           NVL(NULL,0)                                                                                                    AS Sweepstakes_Won,                      
           NVL(COUNT(rpt_goal_selection_detail_id),0)                                                                     AS Received, 
           NVL(NULL,0)                                                                                                    AS Sent, 
           NULL                                                                                                           AS View_All_Awards,
           NVL(NULL,0)                                                                                                    AS Other_Awards --04/29/2014
         FROM rpt_goal_selection_detail 
         WHERE user_id = NVL('''||p_in_paxId||''', user_id) 
         AND TRUNC(trans_date) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
         UNION
         SELECT fnc_cms_picklist_value(''picklist.report.individualactivity.module.type.items'',''challengePoint'','''||p_in_languageCode||''') AS Activity, 
           ''challengePoint''                                                                                             AS ACTIVITY_CODE, 
--           NVL(SUM(calculated_payout),0)                                                                                AS Points,     --10/01/2014
           (NVL(SUM(calculated_payout),0)+NVL(SUM(basic_award_deposited),0))                                              AS Points,  --10/01/20114
           NVL(NULL,0)                                                                                                    AS Plateau_Earned, 
           NVL(NULL,0)                                                                                                    AS Sweepstakes_Won,            
           NVL(COUNT(rpt_cp_selection_detail_id),0)                                                                       AS Received, 
           NVL(NULL,0)                                                                                                    AS Sent, 
           NULL                                                                                                           AS View_All_Awards,
           NVL(NULL,0)                                                                                                    AS Other_Awards --04/29/2014
         FROM rpt_cp_selection_detail 
         WHERE user_id = NVL('''||p_in_paxId||''', user_id) 
         AND TRUNC(trans_date) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
         UNION 
         SELECT fnc_cms_picklist_value(''picklist.report.individualactivity.module.type.items'',''nominationsReceived'','''||p_in_languageCode||''') AS Activity, 
           ''nominationsReceived''                                                                                                 AS ACTIVITY_CODE, 
           NVL(SUM(points_award_amt),0)                                                                                                   AS Points,  --07/01/2016
           --NVL(SUM(DECODE(award_type, ''merchandise'', 1, 0)),0)                                                                   AS Plateau_Earned,--07/01/2016
           0                                                                                                               AS plateau_earned, --07/01/2016 
           NVL(SUM(sweepstakes_won),0)                                                                                       AS Sweepstakes_Won,    --07/01/2016         
           NVL(COUNT(claim_id),0)                                                                                                  AS Received, 
           NVL(NULL,0)                                                                                                             AS Sent, 
           NULL                                                                                                                    AS View_All_Awards ,
           NVL(NULL,0)                                                                                                             AS Other_Awards --04/29/2014
         FROM rpt_nomination_detail 
         WHERE recvr_user_id = NVL('''||p_in_paxId||''', recvr_user_id) 
         AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
         UNION 
         SELECT fnc_cms_picklist_value(''picklist.report.individualactivity.module.type.items'',''nominationsGiven'','''||p_in_languageCode||''') AS Activity, 
           ''nominationsGiven''                                                                                                 AS ACTIVITY_CODE, 
           NVL(SUM(points_award_amt),0)                                                                                                AS Points,  --07/01/2016
           NVL(NULL,0)                                                                                                          AS Plateau_Earned, 
           --NVL(SUM(giver_sweepstakes_won),0)                                                                                    AS Sweepstakes_Won,  --07/01/2016
           0                                                                                                            AS Sweepstakes_Won,   --07/01/2016         
           NVL(NULL,0)                                                                                                          AS Received, 
           NVL(COUNT(claim_id),0)                                                                                               AS Sent, 
           NULL                                                                                                                 AS View_All_Awards,
           NVL(NULL,0)                                                                                                          AS Other_Awards --04/29/2014
         FROM rpt_nomination_detail 
         WHERE giver_user_id = NVL('''||p_in_paxId||''', giver_user_id) 
         AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
         UNION 
         SELECT fnc_cms_picklist_value(''picklist.report.individualactivity.module.type.items'',''productClaims'','''||p_in_languageCode||''') AS Activity, 
           ''productClaims''                                                                                                 AS ACTIVITY_CODE, 
           NVL(SUM(submitter_points),0)                                                                                      AS Points, 
           NVL(SUM(DECODE(award_type, ''merchandise'', 1, 0)),0)                                                             AS Plateau_Earned, 
           NVL(SUM(submitter_sweepstakes_won),0)                                                                             AS Sweepstakes_Won,            
           NVL(COUNT(RPT_CLAIM_DETAIL_ID),0)                                                                                 AS Received, 
           NVL(NULL,0)                                                                                                       AS Sent, 
           NULL                                                                                                              AS View_All_Awards ,
           NVL(NULL,0)                                                                                                       AS Other_Awards --04/29/2014
         FROM rpt_claim_detail 
         WHERE submitter_user_id = NVL('''||p_in_paxId||''', submitter_user_id) 
         AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
         UNION 
         SELECT fnc_cms_picklist_value(''picklist.report.individualactivity.module.type.items'',''recognitionsReceived'','''||p_in_languageCode||''') AS Activity, 
           ''recognitionsReceived''                                                                                                 AS ACTIVITY_CODE, 
           NVL(SUM(DECODE(award_type, ''points'', award_amt, 0)),0)                                                                 AS Points, 
           NVL(SUM(recvr_plateau_earned),0)                                                                                         AS Plateau_Earned, 
           NVL(SUM(recvr_sweepstakes_won),0)                                                                                        AS Sweepstakes_Won,            
           NVL(COUNT(claim_id),0)                                                                                                   AS Received, 
           NVL(NULL,0)                                                                                                              AS Sent, 
           NULL                                                                                                                     AS View_All_Awards ,
           NVL(NULL,0)                                                                                                              AS Other_Awards --04/29/2014
         FROM rpt_recognition_detail 
         WHERE recvr_user_id = NVL('''||p_in_paxId||''', recvr_user_id) 
         AND NVL(TRUNC(date_approved), TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
         UNION 
         SELECT fnc_cms_picklist_value(''picklist.report.individualactivity.module.type.items'',''recognitionsGiven'','''||p_in_languageCode||''') AS Activity, 
           ''recognitionsGiven''                                                                                                 AS ACTIVITY_CODE, 
           NVL(SUM(award_amt),0)                                                                                                 AS Points, 
           NVL(NULL,0)                                                                                                           AS Plateau_Earned, 
           NVL(SUM(giver_sweepstakes_won),0)                                                                                     AS Sweepstakes_Won,                      
           NVL(NULL,0)                                                                                                           AS Received, 
           NVL(COUNT(claim_id),0)                                                                                                AS Sent, 
           NULL                                                                                                                  AS View_All_Awards ,
           NVL(NULL,0)                                                                                                           AS Other_Awards --04/29/2014
         FROM rpt_recognition_detail 
         WHERE giver_user_id = NVL('''||p_in_paxId||''', giver_user_id) 
         AND NVL(TRUNC(date_approved), TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
         UNION 
         SELECT fnc_cms_picklist_value(''picklist.report.individualactivity.module.type.items'',''quizzes'','''||p_in_languageCode||''') AS Activity, 
           ''quizzes''                                                                                                 AS ACTIVITY_CODE, 
           NVL(SUM(award_amount),0)                                                                                    AS Points, 
           NVL(SUM(DECODE(award_type, ''merchandise'', 1, 0)),0)                                                       AS Plateau_Earned, 
           NVL(SUM(sweepstakes_won),0)                                                                                 AS Sweepstakes_Won,                      
           NVL(COUNT(quiz_claim_id),0)                                                                                 AS Received, 
           NVL(NULL,0)                                                                                                 AS Sent, 
           NULL                                                                                                        AS View_All_Awards ,
           NVL(NULL,0)                                                                                                 AS Other_Awards --04/29/2014
         FROM rpt_quiz_activity_detail 
         WHERE participant_id = NVL('''||p_in_paxId||''', participant_id) 
         AND NVL(TRUNC(quiz_date), TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
         UNION 
         SELECT CASE WHEN r.batch_description IS NULL THEN 
                fnc_cms_picklist_value(''picklist.report.individualactivity.module.type.items'',''onTheSpot'','''||p_in_languageCode||''')                
                WHEN r.batch_description like ''%''||'''||gc_str_token||''' THEN REPLACE(r.batch_description,'''||gc_str_token||''')           
                ELSE (SELECT t.cms_name FROM temp_table_session t WHERE t.asset_code= r.batch_description) END           AS Activity, --03/02/2018
           ''onTheSpot''                                                                                                 AS ACTIVITY_CODE, 
           NVL(SUM(transaction_amount),0)                                                                                AS Points, 
           NVL(NULL,0)                                                                                                   AS Plateau_Earned, 
           NVL(NULL,0)                                                                                                   AS Sweepstakes_Won,            
           NVL(COUNT(qcard_detail_id),0)                                                                                 AS Received, 
           NVL(NULL,0)                                                                                                   AS Sent, 
           NULL                                                                                                          AS View_All_Awards ,
           NVL(NULL,0)                                                                                                   AS Other_Awards --04/29/2014
         FROM rpt_qcard_detail r 
         WHERE user_id = NVL('''||p_in_paxId||''', user_id) 
         AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')
         GROUP BY r.batch_description 
         UNION 
         SELECT fnc_cms_picklist_value(''picklist.report.individualactivity.module.type.items'',''throwdown'','''||p_in_languageCode||''') AS Activity, 
           ''throwdown''                                                                                                 AS ACTIVITY_CODE, 
           NVL(SUM(payout),0)                                                                                            AS Points, 
           0                                                                                                             AS Plateau_Earned, 
           0                                                                                                             AS Sweepstakes_Won,           
           0                                                                                                             AS Received, 
           0                                                                                                             AS Sent, 
           NULL                                                                                                          AS View_All_Awards ,
           NVL(NULL,0)                                                                                                   AS Other_Awards --04/29/2014
         FROM rpt_throwdown_activity r 
         WHERE user_id = NVL('''||p_in_paxId||''', user_id) 
         AND payout_date BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
         UNION
         SELECT fnc_cms_picklist_value(''picklist.report.individualactivity.module.type.items'',''badge'','''||p_in_languageCode||''') AS Activity, 
           ''badge''                                                                                                     AS ACTIVITY_CODE, 
           NVL(SUM(media_amount),0)                                                                                      AS Points, 
           0                                                                                                             AS Plateau_Earned, 
           0                                                                                                             AS Sweepstakes_Won,                      
           0                                                                                                             AS Received, 
           0                                                                                                             AS Sent, 
           NULL                                                                                                          AS View_All_Awards ,
           NVL(NULL,0)                                                                                                   AS Other_Awards --04/29/2014
         FROM rpt_awardmedia_detail r 
         WHERE user_id = NVL('''||p_in_paxId||''', user_id) 
         AND promotion_type = ''badge''
         AND media_date BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
         UNION
         SELECT fnc_cms_picklist_value(''picklist.report.individualactivity.module.type.items'',''ssi_contest'','''||p_in_languageCode||''') AS Activity, 
           ''ssi_contest''                                                                                                AS ACTIVITY_CODE, 
           NVL(SUM(points),0)                                                                                             AS Points, 
           NVL(NULL,0)                                                                                                    AS Plateau_Earned, 
           NVL(NULL,0)                                                                                                    AS Sweepstakes_Won,           
           NVL(NULL,0)                                                                                                    AS Received, 
           NVL(NULL,0)                                                                                                    AS Sent, 
           NULL                                                                                                           AS View_All_Awards ,
           NVL(SUM(other),0)                                                                                              AS Other_Awards --6/10/2015 Bug 62578
         FROM rpt_ssi_contest_detail 
         WHERE user_id = NVL('''||p_in_paxId||''', user_id) 
         AND TRUNC(PAYOUT_ISSUE_DATE) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
         UNION 
         SELECT NULL         AS Activity, 
           NULL              AS ACTIVITY_CODE, 
           NULL              AS Points, 
           NULL              AS Plateau_Earned, 
           NULL              AS Sweepstakes_Won,           
           NULL              AS Received, 
           NULL              AS Sent, 
           fnc_cms_asset_code_val_extr (''report.individual.activity'', ''VIEW_ALL_AWARDS'', '''||p_in_languageCode||''') AS View_All_Awards ,
           NULL              AS Other_Awards --04/29/2014   --06/24/2015
         FROM dual         
         ) rs 
       ORDER BY '|| v_sortCol ||'
   ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
  
      OPEN p_out_data FOR l_query;
/* getIndividualActivityTabularResultsSize */
  SELECT COUNT (1) INTO p_out_size_data
     FROM 
       (SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','goalQuest',p_in_languageCode) AS Activity, 
         'goalQuest'                                                                                                    AS ACTIVITY_CODE, 
         NVL(SUM(calculated_payout),0)                                                                                  AS Points, 
         NVL(NULL,0)                                                                                                    AS Plateau_Earned, 
         NVL(NULL,0)                                                                                                    AS Sweepstakes_Won,                     
         NVL(COUNT(rpt_goal_selection_detail_id),0)                                                                     AS Received, 
         NVL(NULL,0)                                                                                                    AS Sent, 
         NULL                                                                                                           AS View_All_Awards,
         NVL(NULL,0)                                                                                                    AS Other_Awards --04/29/2015
       FROM rpt_goal_selection_detail 
       WHERE user_id = NVL(p_in_paxId, user_id) 
       AND TRUNC(trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION
         SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','challengePoint','en_US') AS Activity, 
           'challengePoint'                                                                                               AS ACTIVITY_CODE, 
--           NVL(SUM(calculated_payout),0)                                                                                AS Points,     --10/01/2014
           (NVL(SUM(calculated_payout),0)+NVL(SUM(basic_award_deposited),0))                                              AS Points,  --10/01/20114
           NVL(NULL,0)                                                                                                    AS Plateau_Earned, 
           NVL(NULL,0)                                                                                                    AS Sweepstakes_Won,            
           NVL(COUNT(rpt_cp_selection_detail_id),0)                                                                       AS Received, 
           NVL(NULL,0)                                                                                                    AS Sent, 
           NULL                                                                                                           AS View_All_Awards,
           NVL(NULL,0)                                                                                                    AS Other_Awards --04/29/2015
         FROM rpt_cp_selection_detail 
         WHERE user_id = NVL(p_in_paxId, user_id) 
       AND TRUNC(trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','nominationsReceived',p_in_languageCode) AS Activity, 
         'nominationsReceived'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(points_award_amt),0)                                                                                                   AS Points,  --07/01/2016
         --NVL(SUM(DECODE(award_type, 'merchandise', 1, 0)),0)                                                                     AS Plateau_Earned,--07/01/2016
         0                                                                                                                  AS Plateau_Earned, --07/01/2016 
         NVL(SUM(sweepstakes_won),0)                                                                                       AS Sweepstakes_Won,         
         NVL(COUNT(claim_id),0)                                                                                                  AS Received, 
         NVL(NULL,0)                                                                                                             AS Sent, 
         NULL                                                                                                                    AS View_All_Awards,
         NVL(NULL,0)                                                                                                             AS Other_Awards --04/29/2015
       FROM rpt_nomination_detail 
       WHERE recvr_user_id = NVL(p_in_paxId, recvr_user_id) 
       AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','nominationsGiven',p_in_languageCode) AS Activity, 
         'nominationsGiven'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(points_award_amt),0)                                                                                                AS Points,  --07/01/2016
         NVL(NULL,0)                                                                                                          AS Plateau_Earned, 
         --NVL(SUM(giver_sweepstakes_won),0)                                                                                    AS Sweepstakes_Won,  --07/01/2016
         0                                                                                                                    AS Sweepstakes_Won,        
         NVL(NULL,0)                                                                                                          AS Received, 
         NVL(COUNT(claim_id),0)                                                                                               AS Sent, 
         NULL                                                                                                                 AS View_All_Awards,
         NVL(NULL,0)                                                                                                          AS Other_Awards --04/29/2015
       FROM rpt_nomination_detail 
       WHERE giver_user_id = NVL(p_in_paxId, giver_user_id) 
       AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','productClaims',p_in_languageCode) AS Activity, 
         'productClaims'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(submitter_points),0)                                                                                      AS Points, 
         NVL(SUM(DECODE(award_type, 'merchandise', 1, 0)),0)                                                               AS Plateau_Earned, 
         NVL(SUM(submitter_sweepstakes_won),0)                                                                             AS Sweepstakes_Won,              
         NVL(COUNT(RPT_CLAIM_DETAIL_ID),0)                                                                                 AS Received, 
         NVL(NULL,0)                                                                                                       AS Sent, 
         NULL                                                                                                              AS View_All_Awards,
         NVL(NULL,0)                                                                                                       AS Other_Awards --04/29/2015
       FROM rpt_claim_detail 
       WHERE submitter_user_id = NVL(p_in_paxId, submitter_user_id) 
       AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','recognitionsReceived',p_in_languageCode) AS Activity, 
         'recognitionsReceived'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(DECODE(award_type, 'points', award_amt, 0)),0)                                                                   AS Points, 
         NVL(SUM(recvr_plateau_earned),0)                                                                                         AS Plateau_Earned, 
         NVL(SUM(recvr_sweepstakes_won),0)                                                                                        AS Sweepstakes_Won,                  
         NVL(COUNT(claim_id),0)                                                                                                   AS Received, 
         NVL(NULL,0)                                                                                                              AS Sent, 
         NULL                                                                                                                     AS View_All_Awards,
         NVL(NULL,0)                                                                                                              AS Other_Awards --04/29/2015
       FROM rpt_recognition_detail 
       WHERE recvr_user_id = NVL(p_in_paxId, recvr_user_id) 
       AND NVL(TRUNC(date_approved), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','recognitionsGiven',p_in_languageCode) AS Activity, 
         'recognitionsGiven'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(award_amt),0)                                                                                                 AS Points, 
         NVL(NULL,0)                                                                                                           AS Plateau_Earned, 
         NVL(SUM(giver_sweepstakes_won),0)                                                                                     AS Sweepstakes_Won,          
         NVL(NULL,0)                                                                                                           AS Received, 
         NVL(COUNT(claim_id),0)                                                                                                AS Sent, 
         NULL                                                                                                                  AS View_All_Awards,
         NVL(NULL,0)                                                                                                           AS Other_Awards --04/29/2015
       FROM rpt_recognition_detail 
       WHERE giver_user_id = NVL(p_in_paxId, giver_user_id) 
       AND NVL(TRUNC(date_approved), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','quizzes',p_in_languageCode) AS Activity, 
         'quizzes'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(award_amount),0)                                                                                    AS Points, 
         NVL(SUM(DECODE(award_type, 'merchandise', 1, 0)),0)                                                         AS Plateau_Earned, 
         NVL(SUM(sweepstakes_won),0)                                                                                 AS Sweepstakes_Won,                         
         NVL(COUNT(quiz_claim_id),0)                                                                                 AS Received, 
         NVL(NULL,0)                                                                                                 AS Sent, 
         NULL                                                                                                        AS View_All_Awards,
         NVL(NULL,0)                                                                                                 AS Other_Awards --04/29/2015
       FROM rpt_quiz_activity_detail 
       WHERE participant_id = NVL(p_in_paxId, participant_id) 
       AND NVL(TRUNC(quiz_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT 
       CASE WHEN r.batch_description IS NULL THEN 
        fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','onTheSpot',p_in_languageCode)
        WHEN r.batch_description like '%'||gc_str_token THEN REPLACE(r.batch_description,gc_str_token)                
        ELSE (SELECT t.cms_name FROM temp_table_session t WHERE t.asset_code= r.batch_description) END   AS Activity,--03/02/2018  
         'onTheSpot'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(transaction_amount),0)                                                                                AS Points, 
         NVL(NULL,0)                                                                                                   AS Plateau_Earned, 
         NVL(NULL,0)                                                                                                   AS Sweepstakes_Won,              
         NVL(COUNT(qcard_detail_id),0)                                                                                 AS Received, 
         NVL(NULL,0)                                                                                                   AS Sent, 
         NULL                                                                                                          AS View_All_Awards,
         NVL(NULL,0)                                                                                                   AS Other_Awards --04/29/2015
       FROM rpt_qcard_detail r 
       WHERE user_id = NVL(p_in_paxId, user_id) 
       AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
       GROUP BY r.batch_description 
       UNION 
         SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','throwdown',p_in_languageCode) AS Activity, 
           'throwdown'                                                                                                   AS ACTIVITY_CODE, 
           NVL(SUM(payout),0)                                                                                            AS Points, 
           0                                                                                                             AS Plateau_Earned, 
           NVL(NULL,0)                                                                                                   AS Sweepstakes_Won,                      
           NULL                                                                                                          AS Received, 
           NULL                                                                                                          AS Sent, 
           NULL                                                                                                          AS View_All_Awards,
           NVL(NULL,0)                                                                                                   AS Other_Awards --04/29/2015
         FROM rpt_throwdown_activity r 
         WHERE user_id = NVL(p_in_paxId, user_id) 
         AND payout_date BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         UNION
         SELECT 'badge' AS Activity, 
           'badge'                                                                                                       AS ACTIVITY_CODE, 
           NVL(SUM(media_amount),0)                                                                                      AS Points, 
           0                                                                                                             AS Plateau_Earned, 
           0                                                                                                             AS Sweepstakes_Won,                        
           0                                                                                                             AS Received, 
           0                                                                                                             AS Sent, 
           NULL                                                                                                          AS View_All_Awards,
           NVL(NULL,0)                                                                                                   AS Other_Awards --04/29/2015
         FROM rpt_awardmedia_detail r 
         WHERE user_id = NVL(p_in_paxId, user_id) 
         AND promotion_type = 'badge'
         AND media_date BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT 'ssi_contest'                                                                                               AS Activity, 
           'ssi_contest'                                                                                                  AS ACTIVITY_CODE, 
           NVL(SUM(points),0)                                                                                             AS Points, 
           NVL(NULL,0)                                                                                                    AS Plateau_Earned, 
           NVL(NULL,0)                                                                                                    AS Sweepstakes_Won,           
           NVL(NULL,0)                                                                                                    AS Received, 
           NVL(NULL,0)                                                                                                    AS Sent, 
           NULL                                                                                                           AS View_All_Awards,
           NVL(SUM(other),0)                                                                                              AS Other_Awards--04/29/2015           
         FROM rpt_ssi_contest_detail 
         WHERE user_id = NVL(p_in_paxId, user_id)  
         AND TRUNC(PAYOUT_ISSUE_DATE) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
       UNION
       SELECT NULL         AS Activity, 
         NULL              AS ACTIVITY_CODE, 
         NULL              AS Points, 
         NULL              AS Plateau_Earned, 
         NULL              AS Sweepstakes_Won,         
         NULL              AS Received, 
         NULL              AS Sent, 
         fnc_cms_asset_code_val_extr ('report.individual.activity', 'VIEW_ALL_AWARDS', p_in_languageCode) AS View_All_Awards ,
         NULL              AS Other_Awards --04/29/2015
       FROM dual       
       ) ;
/* getIndividualActivityTabularResultsTotals */
OPEN p_out_totals_data FOR
 SELECT NVL(SUM(Points),0)         AS Points, 
       NVL(SUM(Plateau_Earned),0)  AS Plateau_Earned, 
       NVL(SUM(Sweepstakes_Won),0) AS Sweepstakes_Won,        
       NVL(SUM(Received),0)        AS Received, 
       NVL(SUM(Sent),0)            AS Sent ,
       NVL(SUM(Other_Awards),0)    AS Other_Awards
     FROM 
       (SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','goalQuest',p_in_languageCode) AS Activity, 
         'goalQuest'                                                                                                    AS ACTIVITY_CODE, 
         NVL(SUM(calculated_payout),0)                                                                                  AS Points, 
         NVL(NULL,0)                                                                                                    AS Plateau_Earned, 
         NVL(NULL,0)                                                                                                    AS Sweepstakes_Won,                             
         NVL(COUNT(rpt_goal_selection_detail_id),0)                                                                     AS Received, 
         NVL(NULL,0)                                                                                                    AS Sent, 
         NULL                                                                                                           AS View_All_Awards ,
         NVL(NULL,0)                                                                                                    AS Other_Awards --04/29/2015
       FROM rpt_goal_selection_detail 
       WHERE user_id = NVL(p_in_paxId, user_id) 
       AND TRUNC(trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION
         SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','challengePoint','en_US') AS Activity, 
           'challengePoint'                                                                                               AS ACTIVITY_CODE, 
--           NVL(SUM(calculated_payout),0)                                                                                AS Points,     --10/01/2014
           (NVL(SUM(calculated_payout),0)+NVL(SUM(basic_award_deposited),0))                                              AS Points,  --10/01/20114
           NVL(NULL,0)                                                                                                    AS Plateau_Earned, 
           NVL(NULL,0)                                                                                                    AS Sweepstakes_Won,            
           NVL(COUNT(rpt_cp_selection_detail_id),0)                                                                       AS Received, 
           NVL(NULL,0)                                                                                                    AS Sent, 
           NULL                                                                                                           AS View_All_Awards,
           NVL(NULL,0)                                                                                                    AS Other_Awards --04/29/2015
         FROM rpt_cp_selection_detail 
         WHERE user_id = NVL(p_in_paxId, user_id) 
       AND TRUNC(trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','nominationsReceived',p_in_languageCode) AS Activity, 
         'nominationsReceived'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(points_award_amt),0)                                                                                                   AS Points,  --07/01/2016
         --NVL(SUM(DECODE(award_type, 'merchandise', 1, 0)),0)                                                                     AS Plateau_Earned,  --07/01/2016
         0                                                                                                                  AS Plateau_Earned, --07/01/2016
         NVL(SUM(sweepstakes_won),0)                                                                                       AS Sweepstakes_Won,      --07/01/2016            
         NVL(COUNT(claim_id),0)                                                                                                  AS Received, 
         NVL(NULL,0)                                                                                                             AS Sent, 
         NULL                                                                                                                    AS View_All_Awards,
         NVL(NULL,0)                                                                                                             AS Other_Awards --04/29/2015
       FROM rpt_nomination_detail 
       WHERE recvr_user_id = NVL(p_in_paxId, recvr_user_id) 
       AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','nominationsGiven',p_in_languageCode) AS Activity, 
         'nominationsGiven'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(points_award_amt),0)                                                                                                AS Points, --07/01/2016
         NVL(NULL,0)                                                                                                          AS Plateau_Earned, 
         --NVL(SUM(giver_sweepstakes_won),0)                                                                                    AS Sweepstakes_Won,    --07/01/2016               
         0                                                                                                                    AS Sweepstakes_Won, --07/01/2016,
         NVL(NULL,0)                                                                                                          AS Received, 
         NVL(COUNT(claim_id),0)                                                                                               AS Sent, 
         NULL                                                                                                                 AS View_All_Awards,
         NVL(NULL,0)                                                                                                          AS Other_Awards --04/29/2015
       FROM rpt_nomination_detail 
       WHERE giver_user_id = NVL(p_in_paxId, giver_user_id) 
       AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','productClaims',p_in_languageCode) AS Activity, 
         'productClaims'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(submitter_points),0)                                                                                      AS Points, 
         NVL(SUM(DECODE(award_type, 'merchandise', 1, 0)),0)                                                               AS Plateau_Earned, 
         NVL(SUM(submitter_sweepstakes_won),0)                                                                             AS Sweepstakes_Won,          
         NVL(COUNT(RPT_CLAIM_DETAIL_ID),0)                                                                                 AS Received, 
         NVL(NULL,0)                                                                                                       AS Sent, 
         NULL                                                                                                              AS View_All_Awards,
         NVL(NULL,0)                                                                                                       AS Other_Awards --04/29/2015
       FROM rpt_claim_detail 
       WHERE submitter_user_id = NVL(p_in_paxId, submitter_user_id) 
       AND NVL(TRUNC(date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','recognitionsReceived',p_in_languageCode) AS Activity, 
         'recognitionsReceived'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(DECODE(award_type, 'points', award_amt, 0)),0)                                                                   AS Points, 
         NVL(SUM(recvr_plateau_earned),0)                                                                                         AS Plateau_Earned, 
         NVL(SUM(recvr_sweepstakes_won),0)                                                                                        AS Sweepstakes_Won,                  
         NVL(COUNT(claim_id),0)                                                                                                   AS Received, 
         NVL(NULL,0)                                                                                                              AS Sent, 
         NULL                                                                                                                     AS View_All_Awards,
         NVL(NULL,0)                                                                                                              AS Other_Awards --04/29/2015
       FROM rpt_recognition_detail 
       WHERE recvr_user_id = NVL(p_in_paxId, recvr_user_id) 
       AND NVL(TRUNC(date_approved), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','recognitionsGiven',p_in_languageCode) AS Activity, 
         'recognitionsGiven'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(award_amt),0)                                                                                                 AS Points, 
         NVL(NULL,0)                                                                                                           AS Plateau_Earned, 
         NVL(SUM(giver_sweepstakes_won),0)                                                                                     AS Sweepstakes_Won,                             
         NVL(NULL,0)                                                                                                           AS Received, 
         NVL(COUNT(claim_id),0)                                                                                                AS Sent, 
         NULL                                                                                                                  AS View_All_Awards,
         NVL(NULL,0)                                                                                                           AS Other_Awards --04/29/2015
       FROM rpt_recognition_detail 
       WHERE giver_user_id = NVL(p_in_paxId, giver_user_id) 
       AND NVL(TRUNC(date_approved), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','quizzes',p_in_languageCode) AS Activity, 
         'quizzes'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(award_amount),0)                                                                                    AS Points, 
         NVL(SUM(DECODE(award_type, 'merchandise', 1, 0)),0)                                                         AS Plateau_Earned, 
         NVL(SUM(sweepstakes_won),0)                                                                                 AS Sweepstakes_Won,                  
         NVL(COUNT(quiz_claim_id),0)                                                                                 AS Received, 
         NVL(NULL,0)                                                                                                 AS Sent, 
         NULL                                                                                                        AS View_All_Awards,
         NVL(NULL,0)                                                                                                 AS Other_Awards --04/29/2015
       FROM rpt_quiz_activity_detail 
       WHERE participant_id = NVL(p_in_paxId, participant_id) 
       AND NVL(TRUNC(quiz_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION 
       SELECT CASE WHEN r.batch_description IS NULL THEN 
        fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','onTheSpot',p_in_languageCode)
        WHEN r.batch_description like '%'||gc_str_token THEN REPLACE(r.batch_description,gc_str_token)   
        ELSE (SELECT t.cms_name FROM temp_table_session t WHERE t.asset_code= r.batch_description) END   AS Activity,--03/02/2018 
         'onTheSpot'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(transaction_amount),0)                                                                                AS Points, 
         NVL(NULL,0)                                                                                                   AS Plateau_Earned, 
         NVL(NULL,0)                                                                                                   AS Sweepstakes_Won,                  
         NVL(COUNT(qcard_detail_id),0)                                                                                 AS Received, 
         NVL(NULL,0)                                                                                                   AS Sent, 
         NULL                                                                                                          AS View_All_Awards,
         NVL(NULL,0)                                                                                                   AS Other_Awards --04/29/2015
       FROM rpt_qcard_detail r 
       WHERE user_id = NVL(p_in_paxId, user_id) 
       AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
       GROUP BY r.batch_description 
       UNION 
       SELECT fnc_cms_picklist_value('picklist.report.individualactivity.module.type.items','throwdown',p_in_languageCode) AS Activity, 
         'throwdown'                                                                                                   AS ACTIVITY_CODE, 
         NVL(SUM(payout),0)                                                                                            AS Points, 
         0                                                                                                             AS Plateau_Earned, 
         0                                                                                                             AS Sweepstakes_Won,          
         0                                                                                                             AS Received, 
         0                                                                                                             AS Sent, 
         NULL                                                                                                          AS View_All_Awards,
         0                                                                                                             AS Other_Awards --04/29/2015
       FROM rpt_throwdown_activity r 
       WHERE user_id = NVL(p_in_paxId, user_id) 
       AND payout_date BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       UNION       
       SELECT 'badge' AS Activity, 
         'badge'                                                                                                       AS ACTIVITY_CODE, 
         NVL(SUM(media_amount),0)                                                                                      AS Points, 
         0                                                                                                             AS Plateau_Earned, 
         0                                                                                                             AS Sweepstakes_Won,          
         0                                                                                                             AS Received, 
         0                                                                                                             AS Sent, 
         NULL                                                                                                          AS View_All_Awards,
         0                                                                                                             AS Other_Awards --04/29/2015
       FROM rpt_awardmedia_detail r 
       WHERE user_id = NVL(p_in_paxId, user_id) 
       AND promotion_type = 'badge'
       AND media_date BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
       UNION
       SELECT 'ssi_contest'                                                                                               AS Activity, 
           'ssi_contest'                                                                                                  AS ACTIVITY_CODE, 
           NVL(SUM(points),0)                                                                                             AS Points, 
           NVL(NULL,0)                                                                                                    AS Plateau_Earned, 
           NVL(NULL,0)                                                                                                    AS Sweepstakes_Won,           
           NVL(NULL,0)                                                                                                    AS Received, 
           NVL(NULL,0)                                                                                                    AS Sent, 
           NULL                                                                                                           AS View_All_Awards,
           NVL(SUM(other),0)                                                                                              AS Other_Awards --06/10/2015 Bug 62578
         FROM rpt_ssi_contest_detail 
         WHERE user_id = NVL(p_in_paxId, user_id)  
         AND TRUNC(PAYOUT_ISSUE_DATE) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)       
       ) ;
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

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;         
 END prc_getTabularResults;       
/* getIndividualActivityGoalQuestTabularResults */
procedure prc_getGoalQuestTabularResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getGoalQuestTabularResults';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
   BEGIN
   
DELETE temp_table_session;

INSERT INTO temp_table_session
SELECT asset_code,cms_value,key from vw_cms_asset_value where key='GOALS' and locale = p_in_languageCode;
      v_stage   := 'getGoalQuestTabularResults';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROW_NUMBER() OVER(ORDER BY 
       '|| v_sortCol ||'
    ) RN, 
        rs.*
      FROM 
    ( select rpt.node_name AS Org_Name, 
    rpt.Promotion_name AS Promotion,
    (SELECT cms_name FROM temp_table_session WHERE cms_code=''GOALS'' AND asset_code=goal_level_name) AS Level_Selected,
    rpt.base_quantity AS Base, 
    rpt.amount_to_achieve AS Goal, 
    rpt.current_value AS Actual_Results, 
    rpt.percent_of_goal AS pct_of_Goal, 
    decode(rpt.achieved,1,''Yes'',''No'') AS Achieved, 
    rpt.calculated_payout AS Points, 
    rpt.plateau_earned AS Plateau_Earned 
    FROM rpt_goal_selection_detail rpt 
    WHERE TRUNC(rpt.trans_date) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')  AND rpt.user_id = NVL('''||p_in_paxId||''',rpt.user_id) 
        ) rs 
      )  
   WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
      OPEN p_out_data FOR l_query;
/* getIndividualActivityGoalQuestTabularResultsSize */
  SELECT COUNT (1) INTO p_out_size_data
    from  
    ( select rpt.node_name AS Org_Name, 
    rpt.Promotion_name AS Promotion, 
    goal_level_name  AS  Level_Selected, 
    rpt.base_quantity AS Base, 
    rpt.amount_to_achieve AS Goal, 
    rpt.current_value AS Actual_Results, 
    rpt.percent_of_goal AS pct_of_Goal, 
    decode(rpt.achieved,1,'Yes','No') AS Achieved, 
    rpt.calculated_payout AS Points, 
    rpt.plateau_earned AS Plateau_Earned 
    FROM rpt_goal_selection_detail rpt 
    WHERE TRUNC(rpt.trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) AND rpt.user_id = NVL(p_in_paxId,rpt.user_id) 
     );
/* getIndividualActivityGoalQuestTabularResultsTotals */
OPEN p_out_totals_data FOR
SELECT NVL(SUM(NVL(Points,0)),0)  AS Points, 
    NVL(SUM(NVL(Plateau_Earned,0)),0) AS Plateau_Earned 
      FROM 
    ( select rpt.node_name AS Org_Name, 
    rpt.Promotion_name AS Promotion, 
    goal_level_name  AS  Level_Selected, 
    rpt.base_quantity AS Base, 
    rpt.amount_to_achieve AS Goal, 
    rpt.current_value AS Actual_Results, 
    rpt.percent_of_goal AS pct_of_Goal, 
    decode(rpt.achieved,1,'Yes','No') AS Achieved, 
    rpt.calculated_payout AS Points, 
    rpt.plateau_earned AS Plateau_Earned 
    FROM rpt_goal_selection_detail rpt 
    WHERE TRUNC(rpt.trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) AND rpt.user_id = NVL(p_in_paxId,rpt.user_id) 
        );
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

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;         
 END prc_getGoalQuestTabularResults;
/* getIndividualActivityOnTheSpotTabularResults */
procedure prc_getOnTheSpotTabularResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getOnTheSpotTabularResults';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getOnTheSpotTabularResults';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROW_NUMBER() OVER(ORDER BY 
    '|| v_sortCol ||'
    ) RN, 
        rs.* 
      FROM 
     ( SELECT trans_date      AS Trans_Date, 
                user_first_name||user_last_name  AS Recipient, 
                node_name                 AS Org_Name, 
                NVL(transaction_amount,0) AS Points, 
                cert_num                  AS OnTheSpot 
         FROM rpt_qcard_detail r 
        WHERE user_id = NVL('''||p_in_paxId||''', user_id) 
          AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')    
        ) rs 
      ) 
    WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
    OPEN p_out_data FOR l_query;
/* getIndividualActivityOnTheSpotTabularResultsSize */
SELECT COUNT (1) INTO p_out_size_data
    from  
     ( SELECT trans_date      AS Trans_Date, 
    user_first_name||user_last_name  AS Recipient, 
    node_name                 AS Org_Name, 
    NVL(transaction_amount,0) AS Points, 
    cert_num                  AS OnTheSpot 
    FROM rpt_qcard_detail r 
    WHERE user_id = NVL(p_in_paxId, user_id) 
    AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
     ) ;
/* getIndividualActivityOnTheSpotTabularResultsTotals */
OPEN p_out_totals_data FOR
SELECT NVL(SUM(NVL(Points,0)),0) AS Points 
      FROM 
     ( SELECT trans_date      AS Trans_Date, 
    user_first_name||user_last_name  AS Recipient, 
    node_name                 AS Org_Name, 
    NVL(transaction_amount,0) AS Points, 
    cert_num                  AS OnTheSpot 
    FROM rpt_qcard_detail r 
    WHERE user_id = NVL(p_in_paxId, user_id) 
    AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
      );
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

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;         
 END prc_getOnTheSpotTabularResults;
 /* INDIVIDUAL ACTIVITY REPORT - AWARDS RECEIVED */
/* getIndividualActivityAwardsReceivedTabularResults */
procedure prc_getAwardsReceived(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getAwardsReceived';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
  gc_str_token      CONSTANT VARCHAR2 (500):= '#';  --03/02/2018
   BEGIN
    /*********************03/02/2018 Start********************************/
        DELETE temp_table_session;
        INSERT INTO temp_table_session (asset_code, cms_name, cms_code)
        SELECT asset_code, cms_value, key
          FROM mv_cms_asset_value
         WHERE key = 'OTS_BATCH_NAME_'
           AND locale = p_in_languageCode;
    /**********************03/02/2018 End****************************/
      v_stage   := 'getAwardsReceived';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROWNUM RN, 
         rs.* 
       FROM 
         (SELECT media_date                                                   AS date_submitted, 
           node_name                                                          AS org_name, 
           r.Promotion_name                                                   AS promotion_name, 
           NVL(points,0)                                                      AS points, 
           NVL(plateau_earned,0)                                              AS plateau_earned, 
           NVL(sweepstakes_won,0)                                             AS sweepstakes_won, 
           NULL                                                               AS onthespot, 
           participant_name                                                   AS recipient, 
           fnc_format_user_name(sender_last_name,sender_first_name,NULL,NULL) AS sender 
         FROM rpt_awardmedia_detail r 
         WHERE user_id = NVL('''||p_in_paxId||''', user_id) 
         AND NVL(TRUNC(media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')
         UNION ALL 
         SELECT trans_date           AS date_submitted, 
           node_name                 AS org_name, 
           --NULL                      AS promotion_name, --03/02/2018
           CASE WHEN r.batch_description IS NULL THEN 
                fnc_cms_picklist_value(''picklist.report.individualactivity.module.type.items'',''onTheSpot'','''||p_in_languageCode||''')
                WHEN r.batch_description like ''%''||'''||gc_str_token||''' THEN REPLACE(r.batch_description,'''||gc_str_token||''')         
                ELSE (SELECT t.cms_name FROM temp_table_session t WHERE t.asset_code= r.batch_description) END  AS promotion_name,  --03/02/2018 replaced
           NVL(transaction_amount,0) AS points, 
           0                         AS plateau_earned, 
           0                         AS sweepstakes_won, 
           cert_num                  AS onthespot, 
           user_full_name            AS recipient, 
           NULL                      AS sender 
         FROM rpt_qcard_detail r 
         WHERE user_id = NVL('''||p_in_paxId||''', user_id) 
         AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')
         ) rs 
       ORDER BY '|| v_sortCol ||'
       ) 
     WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
       OPEN p_out_data FOR l_query;
/* getIndividualActivityAwardsReceivedTabularResultsSize */
  SELECT COUNT (1) INTO p_out_size_data
     FROM 
         (SELECT media_date                                                   AS date_submitted, 
           node_name                                                          AS org_name, 
           r.Promotion_name                                                   AS promotion_name, 
           NVL(points,0)                                                      AS points, 
           NVL(plateau_earned,0)                                              AS plateau_earned, 
           NVL(sweepstakes_won,0)                                             AS sweepstakes_won, 
           NULL                                                               AS onthespot, 
           participant_name                                                   AS recipient, 
           fnc_format_user_name(sender_last_name,sender_first_name,NULL,NULL) AS sender 
         FROM rpt_awardmedia_detail r 
         WHERE user_id = NVL(p_in_paxId, user_id) 
         AND NVL(TRUNC(media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         UNION ALL 
         SELECT trans_date           AS date_submitted, 
           node_name                 AS org_name, 
           NULL                      AS promotion_name, 
           NVL(transaction_amount,0) AS points, 
           0                         AS plateau_earned, 
           0                         AS sweepstakes_won, 
           cert_num                  AS onthespot, 
           user_full_name            AS recipient, 
           NULL                      AS sender 
         FROM rpt_qcard_detail r 
         WHERE user_id = NVL(p_in_paxId, user_id) 
         AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       );
/* getIndividualActivityAwardsReceivedTabularResultsTotals */
OPEN p_out_totals_data FOR
 SELECT NVL(SUM(NVL(points,0)),0)     AS points, 
       NVL(SUM(NVL(plateau_earned,0)),0)  AS plateau_earned, 
       NVL(SUM(NVL(sweepstakes_won,0)),0) AS sweepstakes_won 
     FROM 
         (SELECT media_date                                                   AS date_submitted, 
           node_name                                                          AS org_name, 
           r.Promotion_name                                                   AS promotion_name, 
           NVL(points,0)                                                      AS points, 
           NVL(plateau_earned,0)                                              AS plateau_earned, 
           NVL(sweepstakes_won,0)                                             AS sweepstakes_won, 
           NULL                                                               AS onthespot, 
           participant_name                                                   AS recipient, 
           fnc_format_user_name(sender_last_name,sender_first_name,NULL,NULL) AS sender 
         FROM rpt_awardmedia_detail r 
         WHERE user_id = NVL(p_in_paxId, user_id) 
         AND NVL(TRUNC(media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         UNION ALL 
         SELECT trans_date           AS date_submitted, 
           node_name                 AS org_name, 
           NULL                      AS promotion_name, 
           NVL(transaction_amount,0) AS points, 
           0                         AS plateau_earned, 
           0                         AS sweepstakes_won, 
           cert_num                  AS onthespot, 
           user_full_name            AS recipient, 
           NULL                      AS sender 
         FROM rpt_qcard_detail r 
         WHERE user_id = NVL(p_in_paxId, user_id) 
         AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       ) ; 
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

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;         
 END prc_getAwardsReceived;
/* INDIVIDUAL ACTIVITY REPORT - NOMINATIONS RECEIVE */

/******************************************************************************
  NAME:       prc_getNominationsReceived
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
                                        Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J               09/23/2014     Bug Fix 56842  - Null Point exception Issue        
  Suresh J               11/10/2014     Bug Fix 57808  - Sweepstake points and count is not displayed after drilldown the report                             
 ******************************************************************************/

/* getIndividualActivityNominationsReceivedTabularResults */
procedure prc_getNominationsReceived(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getNominationsReceived';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getNominationsReceived';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROW_NUMBER() OVER(ORDER BY 
   '|| v_sortCol ||'
    ) RN, 
        rs.* 
      FROM 
    ( SELECT rpt.promotion_name      AS promo_name, 
    rpt.date_approved                AS date_approved, 
    --rpt.giver_full_name              AS nominator, 
    rpt.giver_last_name||'',''||rpt.giver_first_name AS nominator, --07/01/2016
    NVL(rpt.points_award_amt,0)             AS points,  --07/01/2016
    NVL(rpt.sweepstakes_won,0) AS sweepstakes_won,  --07/01/2016
    rpt.claim_id                     AS claim_id 
    FROM rpt_nomination_detail rpt 
    WHERE rpt.recvr_user_id  = NVL('''||p_in_paxId||''', rpt.recvr_user_id) 
    AND NVL(TRUNC(rpt.date_submitted),TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')
    AND rpt.recvr_user_id IS NOT NULL 
    AND rpt.date_approved IS NOT NULL 
--    AND rpt.claim_id IS NOT NULL   --09/23/2014  --11/10/2014    
        ) rs 
      ) 
    WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
    
    OPEN p_out_data FOR l_query;
/* getIndividualActivityNominationsReceivedTabularResultsSize */
  SELECT COUNT (1) INTO p_out_size_data
     FROM 
    ( SELECT rpt.promotion_name      AS promo_name, 
    rpt.date_approved                AS date_approved, 
    --rpt.giver_full_name              AS nominator,  --07/01/2016
    rpt.giver_last_name||', '||rpt.giver_first_name AS nominator, --07/01/2016
    NVL(rpt.points_award_amt,0)             AS points,  --07/01/2016
    NVL(rpt.sweepstakes_won,0) AS sweepstakes_won  --07/01/2016
    FROM rpt_nomination_detail rpt 
    WHERE rpt.recvr_user_id  = NVL(p_in_paxId, rpt.recvr_user_id) 
    AND NVL(TRUNC(rpt.date_submitted),TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
    AND rpt.recvr_user_id IS NOT NULL 
    AND rpt.date_approved IS NOT NULL 
--    AND rpt.claim_id IS NOT NULL   --09/23/2014   --11/10/2014    
     );
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

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;  
 END prc_getNominationsReceived;
/* INDIVIDUAL ACTIVITY REPORT - NOMINATIONS GIVEN */
/* getIndividualActivityNominationsGivenTabularResults */
procedure prc_getNominationsGiven(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getNominationsGiven';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getNominationsGiven';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROW_NUMBER() OVER(ORDER BY 
     '|| v_sortCol ||'
    ) RN, 
        rs.* 
      FROM 
    ( SELECT rpt.promotion_name      AS promo_name, 
    rpt.date_approved                AS date_approved, 
    --rpt.recvr_full_name              AS nominee, 
    rpt.recvr_last_name||'', ''||rpt.recvr_first_name  AS nominee, --07/01/2016
    NVL(rpt.points_award_amt,0)             AS points,  --07/01/2016
    NVL(rpt.sweepstakes_won,0) AS sweepstakes_won,  --07/01/2016
    rpt.claim_id                     AS claim_id 
    FROM rpt_nomination_detail rpt 
    WHERE rpt.giver_user_id  = NVL('''||p_in_paxId||''', rpt.giver_user_id) 
    AND NVL(TRUNC(rpt.date_submitted),TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')
    AND rpt.date_approved IS NOT NULL 
        ) rs 
      ) 
    WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
    OPEN p_out_data FOR l_query;
    /* getIndividualActivityNominationsGivenTabularResultsSize */
  SELECT COUNT (1) INTO p_out_size_data
    FROM 
    ( SELECT rpt.promotion_name      AS promo_name, 
    rpt.date_approved                AS date_approved, 
    --rpt.recvr_full_name              AS nominee,  --07/01/2016
    rpt.recvr_last_name||', '||rpt.recvr_first_name  AS nominee, --07/01/2016
    NVL(rpt.points_award_amt,0)             AS points, --07/01/2016
    NVL(rpt.sweepstakes_won,0) AS sweepstakes_won  --07/01/2016
    FROM rpt_nomination_detail rpt 
    WHERE rpt.giver_user_id  = NVL(p_in_paxId, rpt.giver_user_id) 
    AND NVL(TRUNC(rpt.date_submitted),TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
    AND rpt.date_approved IS NOT NULL 
      );
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

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;  
 END prc_getNominationsGiven;
/* INDIVIDUAL ACTIVITY REPORT - PRODUCT CLAIM */
/* getIndividualActivityProductClaimTabularResults */
procedure prc_getProductClaim(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getProductClaim';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getProductClaim';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROW_NUMBER() OVER(ORDER BY 
    '|| v_sortCol ||'
    ) RN, 
        rs.*  
      FROM 
        (SELECT rcd.claim_number                                                                    AS CLAIM_NUMBER, 
          rcd.promotion_name                                                                        AS PROMOTION, 
          rcd.date_submitted                                                                        AS DATE_SUBMITTED, 
          rcd.claim_company_name                                                                    AS SOLD_TO, 
          fnc_cms_picklist_value(''picklist.claim.status.type.items'',rcd.claim_status,'''||p_in_languageCode||''') AS CLAIM_STATUS, 
          rcd.claim_id                                                                              AS CLAIM_ID 
        FROM rpt_claim_detail rcd 
        WHERE rcd.submitter_user_id = NVL('''||p_in_paxId||''',rcd.submitter_user_id) 
        AND NVL(TRUNC(rcd.date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
        ) rs 
      ) 
    WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
      OPEN p_out_data FOR l_query;
/* getIndividualActivityProductClaimTabularResultsSize */
SELECT COUNT (1) INTO p_out_size_data
    FROM 
        (SELECT rcd.claim_number                                                                    AS CLAIM_NUMBER, 
          rcd.promotion_name                                                                        AS PROMOTION, 
          rcd.date_submitted                                                                        AS DATE_SUBMITTED, 
          rcd.claim_company_name                                                                    AS SOLD_TO, 
          fnc_cms_picklist_value('picklist.claim.status.type.items',rcd.claim_status,p_in_languageCode) AS CLAIM_STATUS 
        FROM rpt_claim_detail rcd 
        WHERE rcd.submitter_user_id = NVL(p_in_paxId,rcd.submitter_user_id) 
        AND NVL(TRUNC(rcd.date_submitted), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
     ) ;    
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

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;  
 END prc_getProductClaim;     
/* INDIVIDUAL ACTIVITY REPORT - RECOGNITIONS GIVEN */
/* getIndividualActivityRecognitionsGivenTabularResults */
procedure prc_getRecognitionsGiven(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getRecognitionsGiven';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getRecognitionsGiven';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROW_NUMBER() OVER(ORDER BY 
    '|| v_sortCol ||'
    ) RN, 
        rs.*  
      FROM 
        (SELECT rpt.promotion_name         AS PROMO_NAME, 
          rpt.date_submitted               AS DATE_SUBMITTED, 
          rpt.recvr_full_name              AS RECEIVER_NAME, 
          NVL(rpt.recvr_points,0)          AS POINTS_CNT, 
          NVL(rpt.recvr_plateau_earned,0)  AS PLATEAU_EARNED_CNT, 
          NVL(rpt.recvr_sweepstakes_won,0) AS SWEEPSTAKES_WON_CNT, 
          NVL(rpt.recvr_pax_status,0)      AS STATUS, 
          rpt.claim_id                     AS CLAIM_ID 
        FROM rpt_recognition_detail rpt 
        WHERE TRUNC(rpt.date_approved) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
        AND rpt.giver_user_id = NVL('''||p_in_paxId||''',rpt.giver_user_id) 
        ) rs 
      ) 
    WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
    OPEN p_out_data FOR l_query;
/* getIndividualActivityRecognitionsGivenTabularResultsSize */
SELECT COUNT (1) INTO p_out_size_data
        FROM ( SELECT rpt.promotion_name         AS PROMO_NAME, 
          rpt.date_submitted               AS DATE_SUBMITTED, 
          rpt.recvr_full_name              AS RECEIVER_NAME, 
          NVL(rpt.recvr_points,0)          AS POINTS_CNT, 
          NVL(rpt.recvr_plateau_earned,0)  AS PLATEAU_EARNED_CNT, 
          NVL(rpt.recvr_sweepstakes_won,0) AS SWEEPSTAKES_WON_CNT, 
         NVL(rpt.recvr_pax_status,0)      AS STATUS 
        FROM rpt_recognition_detail rpt 
        WHERE TRUNC(rpt.date_approved) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
        AND rpt.giver_user_id = NVL(p_in_paxId,rpt.giver_user_id) 
     ) ;
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

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;  
 END prc_getRecognitionsGiven; 
 /* INDIVIDUAL ACTIVITY REPORT - RECOGNITIONS RECEIVED */
/* getIndividualActivityRecognitionsReceivedTabularResults */
procedure prc_getRecognitionsReceived(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getRecognitionsReceived';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getRecognitionsReceived';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROW_NUMBER() OVER(ORDER BY 
    '|| v_sortCol ||'
    ) RN, 
        rs.*  
      FROM 
        (SELECT rpt.promotion_name                   AS PROMO_NAME, 
          rpt.date_submitted                         AS DATE_SUBMITTED, 
          nvl(rpt.giver_full_name,''Sweepstakes'')   AS GIVER_NAME, 
          NVL(rpt.award_amt,0)                       AS POINTS_CNT, 
          NVL(rpt.recvr_plateau_earned,0)            AS PLATEAU_EARNED_CNT, 
          NVL(rpt.recvr_sweepstakes_won,0)           AS SWEEPSTAKES_WON_CNT, 
          rpt.claim_id                               AS CLAIM_ID 
        FROM rpt_recognition_detail rpt 
        WHERE TRUNC(rpt.date_approved) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
        AND rpt.recvr_user_id = NVL('''||p_in_paxId||''',rpt.recvr_user_id) 
        ) rs 
      )      
   WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
         OPEN p_out_data FOR l_query;
/* getIndividualActivityRecognitionsReceivedTabularResultsSize */
SELECT COUNT (1) INTO p_out_size_data
        FROM ( SELECT rpt.promotion_name   AS PROMO_NAME, 
          rpt.date_submitted               AS DATE_SUBMITTED, 
          rpt.giver_full_name              AS GIVER_NAME, 
          NVL(rpt.recvr_points,0)          AS POINTS_CNT, 
          NVL(rpt.recvr_plateau_earned,0)  AS PLATEAU_EARNED_CNT, 
          NVL(rpt.recvr_sweepstakes_won,0) AS SWEEPSTAKES_WON_CNT 
        FROM rpt_recognition_detail rpt 
        WHERE TRUNC(rpt.date_approved) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
        AND rpt.recvr_user_id = NVL(p_in_paxId,rpt.recvr_user_id) 
      ) ;
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

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;  
 END prc_getRecognitionsReceived;     
 
/* INDIVIDUAL ACTIVITY REPORT - QUIZ */
/* getIndividualActivityQuizTabularResults */
procedure prc_getQuizTabularResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getQuizTabularResults';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getQuizTabularResults';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROW_NUMBER() OVER(ORDER BY 
    '|| v_sortCol ||'
    ) RN, 
        rs.*  
      FROM 
        (SELECT 
          promo_quiz_name                       AS PROMO_NAME, 
          COUNT(1)                              AS QUIZ_ATTEMPTS, 
          SUM(DECODE(quiz_result,''failed'',1,0)) AS QUIZ_FAILED, 
          SUM(DECODE(quiz_result,''passed'',1,0)) AS QUIZ_PASSED, 
          SUM(award_amount)                     AS POINTS, 
          SUM(sweepstakes_won)                  AS SWEEPSTAKES_WON, 
          promotion_id                          AS PROMOTION_ID 
        FROM rpt_quiz_activity_detail 
        WHERE NVL(TRUNC(quiz_date), TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')
        AND participant_id = NVL('''||p_in_paxId||''',participant_id) 
        GROUP BY 
          promo_quiz_name, promotion_id 
        ) rs 
      ) 
    WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
    OPEN p_out_data FOR l_query;
/* getIndividualActivityQuizTabularResultsSize */
SELECT COUNT (1) INTO p_out_size_data
    FROM 
        (SELECT 
          promo_quiz_name                       AS PROMO_NAME, 
          COUNT(1)                              AS QUIZ_ATTEMPTS, 
          SUM(DECODE(quiz_result,'failed',1,0)) AS QUIZ_FAILED, 
          SUM(DECODE(quiz_result,'passed',1,0)) AS QUIZ_PASSED, 
          SUM(award_amount)                     AS POINTS, 
          SUM(sweepstakes_won)                  AS SWEEPSTAKES_WON 
        FROM rpt_quiz_activity_detail 
        WHERE NVL(TRUNC(quiz_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
        AND participant_id = NVL(p_in_paxId,participant_id) 
        GROUP BY 
          promo_quiz_name 
      );
/* getIndividualActivityQuizTabularResultsTotals */
OPEN p_out_totals_data FOR
SELECT NVL(SUM(NVL(QUIZ_ATTEMPTS,0)),0)  AS QUIZ_ATTEMPTS, 
    NVL(SUM(NVL(QUIZ_FAILED,0)),0)  AS QUIZ_FAILED, 
    NVL(SUM(NVL(QUIZ_PASSED,0)),0)  AS QUIZ_PASSED, 
    NVL(SUM(NVL(POINTS,0)),0)  AS POINTS, 
    NVL(SUM(NVL(SWEEPSTAKES_WON,0)),0)  AS SWEEPSTAKES_WON 
      FROM 
        (SELECT 
          promo_quiz_name                       AS PROMO_NAME, 
          COUNT(1)                              AS QUIZ_ATTEMPTS, 
          SUM(DECODE(quiz_result,'failed',1,0)) AS QUIZ_FAILED, 
          SUM(DECODE(quiz_result,'passed',1,0)) AS QUIZ_PASSED, 
          SUM(award_amount)                     AS POINTS, 
          SUM(sweepstakes_won)                  AS SWEEPSTAKES_WON 
        FROM rpt_quiz_activity_detail 
        WHERE NVL(TRUNC(quiz_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
        AND participant_id = NVL(p_in_paxId,participant_id) 
        GROUP BY 
          promo_quiz_name 
        )  ;
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
         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;
 END prc_getQuizTabularResults;
/* INDIVIDUAL ACTIVITY REPORT - THROWDOWN */
/* getIndividualActivityThrowdownTabularResults */
procedure prc_getThrowdownTabularResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getThrowdownTabularResults';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getThrowdownTabularResults';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || ' WITH pax_rank AS 
       (SELECT ts.promotion_id, 
         tsp.user_id, 
         tsp.rank 
       FROM throwdown_stackrank ts, 
         throwdown_stackrank_node tsn, 
         throwdown_stackrank_pax tsp 
       WHERE ts.stackrank_id     = tsn.stackrank_id 
       AND ts.round_number      IS NULL 
       AND tsn.stackrank_node_id = tsp.stackrank_node_id 
       AND tsp.user_id           = '''||p_in_paxId||''' 
       ) 
     SELECT rpt.promotion_id, 
       rpt.promotion_name, 
       SUM (DECODE(outcome,''win'',1,0)) Wins, 
       SUM (DECODE(outcome,''loss'',1,0)) Losses, 
       SUM (DECODE(outcome,''tie'',1,0)) Ties, 
       SUM (current_value) activity, 
       pax_rank.RANK, 
       SUM(payout) AS payout 
     FROM rpt_throwdown_activity rpt, 
       pax_rank 
     WHERE rpt.user_id    = '''||p_in_paxId||''' 
     AND rpt.promotion_id = pax_rank.promotion_id(+) 
     AND payout_date BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')
     GROUP BY rpt.promotion_name, 
       rpt.promotion_id, 
       pax_rank.rank 
     ORDER BY ORDER BY '|| v_sortCol ||'
       ) 
     WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
     OPEN p_out_data FOR l_query;
/* getIndividualActivityThrowdownTabularResultsSize */
SELECT COUNT (1) INTO p_out_size_data
     FROM 
       ( WITH pax_rank AS 
       (SELECT ts.promotion_id, 
         tsp.user_id, 
         tsp.rank 
       FROM throwdown_stackrank ts, 
         throwdown_stackrank_node tsn, 
         throwdown_stackrank_pax tsp 
       WHERE ts.stackrank_id     = tsn.stackrank_id 
       AND ts.round_number      IS NULL 
       AND tsn.stackrank_node_id = tsp.stackrank_node_id 
       AND tsp.user_id           = p_in_paxId 
       ) 
     SELECT rpt.promotion_id, 
       rpt.promotion_name, 
       SUM (DECODE(outcome,'win',1,0)) Wins, 
       SUM (DECODE(outcome,'loss',1,0)) Losses, 
       SUM (DECODE(outcome,'tie',1,0)) Ties, 
       SUM (current_value) activity, 
       pax_rank.RANK, 
       SUM(payout) AS payout 
     FROM rpt_throwdown_activity rpt, 
       pax_rank 
     WHERE rpt.user_id    = p_in_paxId 
     AND rpt.promotion_id = pax_rank.promotion_id(+) 
     AND payout_date BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
     GROUP BY rpt.promotion_name, 
       rpt.promotion_id, 
       pax_rank.rank 
       ) ;
/* getIndividualActivityThrowdownTabularResultsTotals */
OPEN p_out_totals_data FOR
 SELECT SUM (DECODE(outcome,'win',1,0)) Wins, 
       SUM (DECODE(outcome,'loss',1,0)) Losses, 
       SUM (DECODE(outcome,'tie',1,0)) Ties, 
       SUM (current_value) activity, 
       SUM(payout) AS payout 
     FROM rpt_throwdown_activity rpt 
     WHERE rpt.user_id = p_in_paxId 
     AND payout_date BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) ;
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
         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;
 END prc_getThrowdownTabularResults;
/* QUIZ DETAIL REPORTS */
/* getIndividualActivityQuizDetailResults */
procedure prc_getQuizDetailResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getQuizDetailResults';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getQuizDetailResults';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROW_NUMBER() OVER(ORDER BY 
       '|| v_sortCol ||'
    ) RN, 
        rs.* 
      FROM 
        (SELECT 
          promo_quiz_name                      AS PROMOTION, 
          quiz_date                            AS QUIZ_DATE, 
          quiz_result                          AS QUIZ_RESULT, 
          award_amount                         AS POINTS, 
          sweepstakes_won                      AS SWEEPSTAKES_WON, 
          quiz_claim_id                        AS CLAIM_ID, 
          participant_id                       AS SUBMITTER_ID 
        FROM rpt_quiz_activity_detail 
        WHERE NVL(TRUNC(quiz_date), TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
        AND participant_id = NVL('''||p_in_paxId||''',participant_id) 
        AND promotion_id = '''||p_in_promotionId||''' 
        ) rs 
      ) 
    WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
      OPEN p_out_data FOR l_query;
/* getIndividualActivityQuizDetailResultsSize */
SELECT COUNT (1) INTO p_out_size_data
    FROM  
        ( SELECT 
          promo_quiz_name                       AS PROMOTION, 
          quiz_date                             AS QUIZ_DATE, 
          quiz_result                           AS QUIZ_RESULT, 
          award_amount                          AS POINTS, 
          sweepstakes_won                       AS SWEEPSTAKES_WON, 
          quiz_claim_id                         AS CLAIM_ID, 
          participant_id                        AS SUBMITTER_ID 
        FROM rpt_quiz_activity_detail 
        WHERE NVL(TRUNC(quiz_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
        AND participant_id = NVL(p_in_paxId,participant_id) 
       ) ;
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
         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;
 END prc_getQuizDetailResults; 
/* THROWDOWN DETAIL REPORTS */
/* getIndividualActivityThrowdownDetailResults */
procedure prc_getThrowdownDetailResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getThrowdownDetailResults';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
 BEGIN
  v_stage   := 'getThrowdownDetailResults';
  v_sortCol := p_in_sortColName || ' ' || NVL(p_in_sortedBy, ' ');
    OPEN p_out_data FOR   
SELECT * 
    FROM 
       ( WITH pax_rank AS 
       (SELECT ts.promotion_id, 
         ts.round_number, 
         tsp.user_id, 
         tsp.rank 
       FROM throwdown_stackrank ts, 
         throwdown_stackrank_node tsn, 
         throwdown_stackrank_pax tsp 
       WHERE ts.stackrank_id     = tsn.stackrank_id 
       AND ts.round_number      IS NOT NULL 
       AND tsn.stackrank_node_id = tsp.stackrank_node_id 
       AND tsp.user_id           = p_in_paxId 
       AND ts.promotion_id       = p_in_promotionId 
       ) 
     SELECT rpt.promotion_name, 
       rpt.round_number, 
       SUM (DECODE(outcome,'win',1,0)) Wins, 
       SUM (DECODE(outcome,'loss',1,0)) Losses, 
       SUM (DECODE(outcome,'tie',1,0)) Ties, 
       SUM (current_value) activity, 
       pax_rank.RANK, 
       SUM(payout) AS payout 
     FROM rpt_throwdown_activity rpt, 
       pax_rank 
     WHERE rpt.user_id    = p_in_paxId 
     AND rpt.promotion_id = p_in_promotionId 
     AND rpt.promotion_id = pax_rank.promotion_id(+) 
     AND rpt.round_number = pax_rank.round_number(+) 
     AND payout_date BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
     GROUP BY rpt.promotion_name, 
       rpt.round_number, 
       pax_rank.rank 
     ORDER BY 
     v_sortCol
       ) 
     WHERE rownum > p_in_rowNumStart 
     AND rownum   < p_in_rowNumEnd ;
/* getIndividualActivityThrowdownDetailResultsSize */
SELECT COUNT (1) INTO p_out_size_data
     FROM 
       ( WITH pax_rank AS 
       (SELECT ts.promotion_id, 
         ts.round_number, 
         tsp.user_id, 
         tsp.rank 
       FROM throwdown_stackrank ts, 
         throwdown_stackrank_node tsn, 
         throwdown_stackrank_pax tsp 
       WHERE ts.stackrank_id     = tsn.stackrank_id 
       AND ts.round_number      IS NOT NULL 
       AND tsn.stackrank_node_id = tsp.stackrank_node_id 
       AND tsp.user_id           = p_in_paxId 
       AND ts.promotion_id       = p_in_promotionId 
       ) 
     SELECT rpt.promotion_name, 
       rpt.round_number, 
       SUM (DECODE(outcome,'win',1,0)) Wins, 
       SUM (DECODE(outcome,'loss',1,0)) Losses, 
       SUM (DECODE(outcome,'tie',1,0)) Ties, 
       SUM (current_value) activity, 
       pax_rank.RANK, 
       SUM(payout) AS payout 
     FROM rpt_throwdown_activity rpt, 
       pax_rank 
     WHERE rpt.user_id    = p_in_paxId 
     AND rpt.promotion_id = p_in_promotionId 
     AND rpt.promotion_id = pax_rank.promotion_id(+) 
     AND rpt.round_number = pax_rank.round_number(+) 
     AND payout_date BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
     GROUP BY rpt.promotion_name, 
       rpt.round_number, 
       pax_rank.rank 
       ) ;
/* getIndividualActivityThrowdownDetailResultsTotals*/
OPEN p_out_totals_data FOR
 SELECT SUM (DECODE(outcome,'win',1,0)) Wins, 
       SUM (DECODE(outcome,'loss',1,0)) Losses, 
       SUM (DECODE(outcome,'tie',1,0)) Ties, 
       SUM (current_value) activity, 
       SUM(payout) AS payout 
     FROM rpt_throwdown_activity rpt 
     WHERE rpt.user_id    = p_in_paxId 
     AND rpt.promotion_id = p_in_promotionId 
     AND payout_date BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern);
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
         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;
 END prc_getThrowdownDetailResults;

--Suresh J      10/01/2014 Bug#57071,57076 -- Updated Points to Include Basic Points also 
  procedure prc_getCPTabularResults(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getCPTabularResults';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
   BEGIN
DELETE temp_table_session;

INSERT INTO temp_table_session
SELECT asset_code,cms_value,key from vw_cms_asset_value where key='GOALS' and locale = p_in_languageCode;
      v_stage   := 'getCPTabularResults';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROW_NUMBER() OVER(ORDER BY 
       '|| v_sortCol ||'
    ) RN, 
        rs.* 
      FROM 
    ( select n.name AS Org_Name, 
    rpt.Promotion_name AS Promotion, 
    (SELECT cms_name FROM temp_table_session temp WHERE temp.cms_code=''GOALS'' AND temp.asset_code=gg.goal_level_cm_asset_code) AS Level_Selected,
    rpt.base_quantity AS Base, 
    rpt.amount_to_achieve AS Challengepoint, 
    rpt.current_value AS Actual_Results, 
--    NULL AS pct_of_CP,  --10/01/2014
    DECODE(rpt.amount_to_achieve,0,0,ROUND((NVL(rpt.current_value,0)/rpt.amount_to_achieve)*100,2)) AS pct_of_CP, --10/01/2014  
    decode(rpt.achieved,1,''Yes'',''No'') AS Achieved, 
--    rpt.calculated_payout AS Points,    --10/01/2014
    (NVL(rpt.calculated_payout,0)+NVL(rpt.basic_award_deposited,0)) AS Points,   --10/01/2014 
    NULL AS Plateau_Earned 
    FROM rpt_cp_selection_detail rpt ,
    node n,goalquest_goallevel gg WHERE rpt.node_id = n.node_id AND rpt.level_id = gg.goallevel_id AND TRUNC(rpt.trans_date) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')  AND rpt.user_id = NVL('''||p_in_paxId||''',rpt.user_id) 
        ) rs 
      )  
   WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
 
      OPEN p_out_data FOR l_query;
/* getIndividualActivityChallengePointTabularResultsSize */
  SELECT COUNT (1) INTO p_out_size_data
    from  
    ( select n.name AS Org_Name, 
    rpt.Promotion_name AS Promotion, 
    level_id  AS  Level_Selected, 
    rpt.base_quantity AS Base, 
    rpt.amount_to_achieve AS Challengepoint, 
    rpt.current_value AS Actual_Results, 
    NULL AS pct_of_CP, 
    decode(rpt.achieved,1,'Yes','No') AS Achieved, 
--    rpt.calculated_payout AS Points,    --10/01/2014
    (NVL(rpt.calculated_payout,0)+NVL(rpt.basic_award_deposited,0)) AS Points,   --10/01/2014 
    NULL AS Plateau_Earned 
    FROM rpt_cp_selection_detail rpt , node n 
    WHERE rpt.node_id = n.node_id AND TRUNC(rpt.trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)  AND rpt.user_id = NVL(p_in_paxId,rpt.user_id )
     );
/* getIndividualActivityChallengePointTabularResultsTotals */
OPEN p_out_totals_data FOR
SELECT NVL(SUM(NVL(Points,0)),0)  AS Points, 
    NVL(SUM(NVL(Plateau_Earned,0)),0) AS Plateau_Earned 
      FROM 
    ( select n.name AS Org_Name, 
    rpt.Promotion_name AS Promotion, 
    level_id  AS  Level_Selected, 
    rpt.base_quantity AS Base, 
    rpt.amount_to_achieve AS Challengepoint, 
    rpt.current_value AS Actual_Results, 
    NULL AS pct_of_CP, 
    decode(rpt.achieved,1,'Yes','No') AS Achieved, 
--    rpt.calculated_payout AS Points,    --10/01/2014
    (NVL(rpt.calculated_payout,0)+NVL(rpt.basic_award_deposited,0)) AS Points,   --10/01/2014 
    NULL AS Plateau_Earned 
    FROM rpt_cp_selection_detail rpt , node n 
    WHERE rpt.node_id = n.node_id AND TRUNC(rpt.trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)  AND rpt.user_id = NVL(p_in_paxId,rpt.user_id ) 
        );
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

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;         
 END prc_getCPTabularResults;
 
 procedure prc_getBadgeTabularResults( --1/23/2015
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getBadgeTabularResults';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
   BEGIN

      v_stage   := 'getBadgeTabularResults';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROW_NUMBER() OVER(ORDER BY 
       '|| v_sortCol ||'
    ) RN, 
        rs.* 
      FROM 
    ( select promotion_name,media_date,media_amount points from rpt_awardmedia_detail WHERE promotion_type =''badge'' AND 
    TRUNC(media_date) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')  AND user_id = NVL('''||p_in_paxId||''',user_id) 
        ) rs 
      )  
   WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
 
      OPEN p_out_data FOR l_query;
/* getIndividualActivityChallengePointTabularResultsSize */
  SELECT COUNT (1) INTO p_out_size_data
    from  
    ( select promotion_name,media_date,media_amount  from rpt_awardmedia_detail WHERE promotion_type ='badge' AND 
    TRUNC(media_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)  AND user_id = NVL(p_in_paxId,user_id) 
     );
/* getIndividualActivityChallengePointTabularResultsTotals */
OPEN p_out_totals_data FOR
SELECT NVL(SUM(NVL(media_amount,0)),0)  AS Points
      FROM 
    (  select promotion_name,media_date,media_amount from rpt_awardmedia_detail WHERE promotion_type ='badge' AND 
    TRUNC(media_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)  AND user_id = NVL(p_in_paxId,user_id) 
        );
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

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;         
 END prc_getBadgeTabularResults; 
Procedure prc_getSSIContests(
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,   
   p_in_paxId               IN     NUMBER,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER)
IS
/******************************************************************************
   NAME:       prc_getSSIContests
   PURPOSE:

   REVISIONS:
    Date        Author           Description
   ----------  ---------------  ------------------------------------
    04/27/2015  Swati           Initial Version to include the SSI Contests in Individual Activity Report         
    06/03/2015  KrishnaDeepika  Included column ssi_contest_id to the query result
    09/03/2015  Ravi Dhanekula  Bug # 63917
    09/07/2015  Suresh J       Bug # 63917
******************************************************************************/
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getSSIContests';
  v_stage        VARCHAR2(500);
  v_sortCol      VARCHAR2(200);
  l_query        VARCHAR2 (32767);
   BEGIN
   DELETE temp_table_session;

INSERT INTO temp_table_session
SELECT asset_code,cms_value,key from vw_cms_asset_value where key='CONTEST_NAME' and locale = p_in_languageCode;
      v_stage   := 'getSSIContests';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROW_NUMBER() OVER(ORDER BY 
    '|| v_sortCol ||'
    ) RN, 
        rs.*  
      FROM 
        (SELECT (select cms_name from temp_table_session where asset_code = rpt.ssi_contest_name) SSI_CONTEST_NAME,
            rpt.SSI_CONTEST_ID,
            rpt.PAYOUT_ISSUE_DATE,
            rpt.POINTS,
            rpt.OTHER,
            --OTHER_VALUE            
            CASE WHEN rpt.other > 0
            THEN NVL(cur.currency_symbol,''$'')|| TO_CHAR(NVL (rpt.OTHER_VALUE, 0)) ELSE NVL(cur.currency_symbol,''$'')||''0'' END    AS OTHER_VALUE,
            CASE WHEN sc.contest_type = 1 THEN ''Y'' ELSE ''N'' END AS IS_ATN_CONTEST --09/03/2015  --09/07/2015
        FROM rpt_ssi_contest_detail rpt,
            (select sc.ssi_contest_id, 
                 sc.activity_measure_type,
                 sc.activity_measure_cur_code,
                 c.currency_code,
                 c.currency_symbol,
                 sc.contest_type --09/03/2015
           from ssi_contest sc,
                currency c 
           where sc.activity_measure_type = ''currency''  and 
                 UPPER(sc.activity_measure_cur_code) = c.currency_code (+) 
                 ) cur --06/10/2015 Bug 62578
                 ,ssi_contest sc  --09/07/2015
        WHERE TRUNC(rpt.PAYOUT_ISSUE_DATE) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
        AND rpt.USER_ID = NVL('''||p_in_paxId||''',rpt.USER_ID) 
        AND rpt.ssi_contest_id = sc.ssi_contest_id  --09/07/2015        
        AND rpt.ssi_contest_id = cur.ssi_contest_id  (+)
        ) rs 
      ) 
    WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
    OPEN p_out_data FOR l_query;
/* getIndividualActivitySSIContestsTabularResultsSize */
SELECT COUNT (1) INTO p_out_size_data
        FROM ( SELECT SSI_CONTEST_NAME,
            SSI_CONTEST_ID,
            PAYOUT_ISSUE_DATE,
            POINTS,
            OTHER,
            OTHER_VALUE            
        FROM rpt_ssi_contest_detail rpt 
        WHERE TRUNC(rpt.PAYOUT_ISSUE_DATE) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
        AND rpt.USER_ID = NVL(p_in_paxId,rpt.user_id)
     ) ;
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

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;  
 END prc_getSSIContests;
END pkg_query_Individual_Activity;
/
