CREATE OR REPLACE PACKAGE PKG_REPORT_AWARDMEDIA  IS
 
 FUNCTION fnc_eligible_participants 
  (p_in_node_id     IN VARCHAR2,
   is_team     IN NUMBER,
   p_in_promo_status  IN promotion.promotion_status%TYPE,
   p_in_promo_id    IN VARCHAR2,      
   Is_OntheSpot_included IN VARCHAR2,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN VARCHAR2  
  )
  RETURN NUMBER;
FUNCTION fnc_award_received_cnt
  (p_in_node_id     IN user_node.node_id%TYPE,
   p_in_promo_id    IN VARCHAR2, 
   p_in_promo_status  IN promotion.promotion_status%TYPE,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   Is_OntheSpot_included IN VARCHAR2,
   p_in_pax_dept    IN VARCHAR2,
   p_in_start_date  IN VARCHAR2,
   p_in_end_date IN VARCHAR2, 
   p_in_locale   IN VARCHAR2   
  )
  RETURN NUMBER;

  PROCEDURE P_RPT_AWARDMEDIA_DETAIL
  (p_user_id       IN NUMBER,
   p_start_date    IN DATE, 
   p_end_date      IN DATE, 
   p_return_code   OUT NUMBER,
   p_error_message OUT VARCHAR2);

  PROCEDURE P_RPT_AWARDMEDIA_SUMMARY
  (p_user_id       IN NUMBER,
   p_start_date    IN DATE, 
   p_end_date      IN DATE, 
   p_return_code   OUT NUMBER,
   p_error_message OUT VARCHAR2);
  
  PROCEDURE P_INSERT_RPT_AWARDMEDIA_DETAIL
   (p_awardmedia_dtl_rec IN rpt_awardmedia_detail%rowtype);

  PROCEDURE p_rpt_awardmedia_detail_sa      --04/01/2019
      (p_user_id       IN NUMBER,
       p_start_date    IN DATE, 
       p_end_date      IN DATE, 
       p_return_code   OUT NUMBER,
       p_error_message OUT VARCHAR2);

END; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY PKG_REPORT_AWARDMEDIA
IS
 FUNCTION fnc_eligible_participants 
  (p_in_node_id     IN VARCHAR2,
   is_team IN number,
   p_in_promo_status  IN promotion.promotion_status%TYPE,
   p_in_promo_id    IN VARCHAR2,      
   Is_OntheSpot_included IN VARCHAR2,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN VARCHAR2  
  )
  RETURN NUMBER IS
  /*-----------------------------------------------------------------------------
  Purpose: Retrieves count of eligible participants for the input node and its
           child nodes
  
  Modification history
  Person        Date        Comments
  -----------   ----------  --------------------------------------------------
  Ravi          12/26/2012  Initial creation
  Suresh J      04/01/2019  SA Integeration with DayMaker changes for reports
-----------------------------------------------------------------------------*/

  v_eli_pax NUMBER := 0;
BEGIN

SELECT COUNT(user_id) 
INTO v_eli_pax
FROM (
SELECT p2.user_id
    --  INTO v_eli_pax
      FROM vw_curr_pax_employer vue,
           participant p2,
           user_node un
     WHERE Is_OntheSpot_included = 'Y' 
       AND   p2.user_id =vue.user_id(+)
       AND p2.status            = NVL (p_in_pax_status, p2.status)
       AND NVL(vue.position_type,'job')    = NVL (p_in_pax_role, NVL(vue.position_type,'job'))
       AND p2.user_id = un.user_id
       AND un.is_primary = 1
       AND (       
       NVL(is_Team,0)=0 AND un.NODE_ID IN (    SELECT node_id
                                         FROM NODE N2
                                   CONNECT BY PRIOR node_id = parent_node_id
                                   START WITH node_id  IN (SELECT * FROM TABLE(get_array_varchar(p_in_node_id))))
                             OR
                           (  is_Team=1 AND un.NODE_ID=p_in_node_id))
        AND ((p_in_pax_dept IS NULL) OR ( vue.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_pax_dept)))))      
      UNION
    SELECT p2.user_id 
--      INTO v_eli_pax
      FROM vw_curr_pax_employer vue,
           participant p2
     WHERE  p2.user_id = vue.user_id(+)
       AND p2.status            = NVL (p_in_pax_status, p2.status)
       AND NVL(vue.position_type,'job')    = NVL (p_in_pax_role, NVL(vue.position_type,'job'))
       AND ((p_in_pax_dept IS NULL) OR ( vue.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_pax_dept)))))
       AND EXISTS (SELECT 'X'
                     FROM rpt_pax_promo_eligibility ppe,
                          promotion p
                    WHERE ppe.promotion_id     = p.promotion_id
                      AND ppe.participant_id   = p2.user_id
                      AND ppe.giver_recvr_type = 'receiver'
                       AND ((p_in_promo_id IS NULL AND Is_OntheSpot_included = 'N' ) OR ( p.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promo_id)))))
                      AND p.promotion_status   = NVL(p_in_promo_status, p.promotion_status)
                       AND (       
       NVL(is_Team,0)=0 AND ppe.NODE_ID IN (    SELECT node_id
                                         FROM NODE N2
                                   CONNECT BY PRIOR node_id = parent_node_id
                                   START WITH node_id  IN (SELECT * FROM TABLE(get_array_varchar(p_in_node_id))))
                             OR
                           (  is_Team=1 AND ppe.NODE_ID=p_in_node_id))
        AND ((p_in_pax_dept IS NULL) OR ( vue.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_pax_dept)))))     
                      ));


  RETURN v_eli_pax;

EXCEPTION
  WHEN OTHERS THEN
    RETURN v_eli_pax;
END fnc_eligible_participants;

FUNCTION fnc_award_received_cnt
  (p_in_node_id     IN user_node.node_id%TYPE,
   p_in_promo_id    IN VARCHAR2, 
   p_in_promo_status  IN promotion.promotion_status%TYPE,
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   Is_OntheSpot_included IN VARCHAR2,
   p_in_pax_dept    IN VARCHAR2,
   p_in_start_date  IN VARCHAR2,
   p_in_end_date IN VARCHAR2, 
   p_in_locale   IN VARCHAR2 
  )
  RETURN NUMBER
IS

  /*-----------------------------------------------------------------------------
  Purpose: Retrieves count of participants who received award for the input node and its
           child nodes
  
  Modification history
  Person        Date        Comments
  -----------   ----------  --------------------------------------------------
  Ravi          12/26/2012  Initial creation
-----------------------------------------------------------------------------*/

  v_received_award_cnt NUMBER := 0;
  v_OnTheSpot_award_cnt NUMBER := 0;

BEGIN

 SELECT COUNT(distinct rad.user_id)   
      INTO v_received_award_cnt        
        FROM rpt_awardmedia_detail rad,
          promotion p,
          vw_curr_pax_employer vue,
          participant p2      
        WHERE rad.promotion_id   = p.promotion_id      
        AND ((p_in_promo_id IS NULL AND Is_OntheSpot_included = 'N') OR ( p.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promo_id)))))
        AND p.promotion_status   = NVL(p_in_promo_status,p.promotion_status)
         AND TRUNC(rad.media_date)  BETWEEN fnc_locale_to_date_dt(p_in_start_date, p_in_locale)  
                                 AND fnc_locale_to_date_dt(p_in_end_date, p_in_locale)     
        AND rad.node_id IN (SELECT node_id
                          FROM rpt_hierarchy
                       CONNECT BY PRIOR node_id = parent_node_id
                         START WITH node_id = p_in_node_id)
       AND rad.user_id = p2.user_id 
     AND p2.user_id  = vue.user_id(+)
     AND p2.status   = NVL (p_in_pax_status, p2.status)
     AND NVL(vue.position_type,'job')    = NVL (p_in_pax_role, NVL(vue.position_type,'job'))
     AND ((p_in_pax_dept IS NULL) OR ( vue.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_pax_dept)))));
     
     IF Is_OntheSpot_included ='Y' THEN
     
     SELECT COUNT(distinct rad.user_id)   
      INTO v_OnTheSpot_award_cnt        
        FROM rpt_qcard_detail rad,         
          vw_curr_pax_employer vue,
          participant p2      
        WHERE         
          TRUNC(rad.trans_date)  BETWEEN fnc_locale_to_date_dt(p_in_start_date, p_in_locale)  
                                 AND fnc_locale_to_date_dt(p_in_end_date, p_in_locale)     
        AND rad.node_id IN (SELECT node_id
                          FROM rpt_hierarchy
                       CONNECT BY PRIOR node_id = parent_node_id
                         START WITH node_id = p_in_node_id)
       AND rad.user_id = p2.user_id 
     AND p2.user_id  = vue.user_id(+)
     AND p2.status   = NVL (p_in_pax_status, p2.status)
     AND NVL(vue.position_type,'job')    = NVL (p_in_pax_role, NVL(vue.position_type,'job'))
     AND ((p_in_pax_dept IS NULL) OR ( vue.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_pax_dept)))));
     
     v_received_award_cnt := v_received_award_cnt+v_OnTheSpot_award_cnt;     
     END IF;     

  RETURN v_received_award_cnt;

EXCEPTION
  WHEN OTHERS THEN
    RETURN v_received_award_cnt;
END fnc_award_received_cnt;

PROCEDURE p_rpt_awardmedia_detail
  (p_user_id       IN NUMBER,
   p_start_date    IN DATE, 
   p_end_date      IN DATE, 
   p_return_code   OUT NUMBER,
   p_error_message OUT VARCHAR2)
IS

/*******************************************************************************
   Purpose:  Populate the awardmedia_detail reporting tables

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   Raju N        10/11/2005 Creation
   D Murray      10/31/2005 Added rpt_awardmedia_dtl_characteristic.
   D Murray      12/13/2005 Added department.
   Adit B        04/09/2007 Added a where clause to the outermost cursor to 
                 exclude certicate deposits
   Arun S        02/21/2011 Bug # 35014 Fix, Not populated record for AwardperQs Deposited Report
                            when promotion_type = 'product_claim' AND rec_main.status_type <> 'post'
   J Flees       08/31/2011 Report Refresh Performance.
                            Replaced looping process with single insert statement.
   Arun S        11/28/2011 Bug 38589, Added promotion_status = 'live' while inserting rpt_awardmedia_detail 
                            So that AwardperQs Deposited report generate only for live promotions 
   nagarajs      03/21/2012 Bug #40345 fix to generate AwardperQs Deposited report for live and expired promotions
                            but not for deleted promotion.                       
   Arun S        10/03/2012 G5 Report changes
                            Modified procedure to process based on input start and end date
   Ravi Dhanekula 08/07/2013   Fixed the defect # 3851.
   nagarajs       09/04/2013  Fixed report refresh failure - In case of user department , position type and pax status changed
   nagarajs       09/24/2013  Fixed report refresh failure - populate is_leaf as 1 for team records
   Ravi Dhanekula 02/24/2014  Bug # 51485. Fixed the issue where eversed transactions are displaying on Awards Deposited report
   Swati          04/08/2014  Bug #52680 Fixed
   Ravi Dhanekula 06/17/2014  Bug #54380 fixed.
   Ravi Dhanekula 09/09/2015 Bug # 63970
   Suresh J      05/05/2016  New nomination changes
   nagarajs       06/22/2016 Bug 66253 - Badge Points not displaying in Awards Received Reports 
   SherifBasha    04/10/2016 Bug 69353 - Report Refresh Failing
  -nagarajs       10/05/2016  New Nomination Report Changes
  Sherif Basha    05/12/2017  G6-2455 - Report refresh process is getting failed         
  nagarajs        05/18/2017  G6-2483 Report refresh process is getting failed         
  chidamba        08/24/2017  G6-2885-Point Reversal for Quiz and Product Claim should not get displayed in Award Received report.
  Gorantla        09/04/2018  Bug 77593 - When running the Awards By Participant report, 
                              the Sweepstakes Won column is being populated for awards/recognition that are not Sweepstakes.
  Loganathan      03/11/2019  Bug 78631 - PKG_REPORTs without logs or return code
  Loganathan      03/28/2019  Bug 78869 - File loaded recognition points deposited 
  							  and then reversed for a participant record is not present in the Awards Received extracts
*******************************************************************************/

  --Cursor to pick detail record
  CURSOR cur_detail IS  
    SELECT 
            CASE WHEN P.promotion_type = 'nomination' THEN j.award_type   -- 04/10/2016 Bug 69353
            -- WHEN j.award_type = 'cash' THEN 'cash' -- 04/10/2016 Bug 69353
               ELSE
                    P.award_type
            END  AS media_type,                               --05/05/2016
           unr.node_id,
           n.NAME                     AS node_name,
           TRUNC(j.transaction_date)  AS media_date,
           j.promotion_id,
           P.promotion_name,
           P.promotion_type,
           P.is_taxable,
           n.hierarchy_id,
           j.user_id,
           fnc_format_user_name(au.last_name, au.first_name, au.middle_name, au.suffix) AS participant_name,
           INITCAP(au.first_name)     AS pax_first_name,
           TRIM(au.middle_name)       AS pax_middle_name,
           INITCAP(au.last_name)      AS pax_last_name,
           DECODE(au.is_active, 1, 'active', 'inactive') AS participant_current_status,
           au.user_name,
           fnc_get_badge_count_by_user(au.user_id,P.promotion_type,TRUNC(j.transaction_date)) badges_earned,
           NVL(cpe.position_type, ' ') AS position_type,
           NVL(cpe.department_type, ' ') AS department,
           CASE WHEN P.promotion_type = 'nomination' AND j.award_type = 'cash' THEN j.transaction_cash_amt --10/05/2016
           ELSE j.transaction_amt
           END    AS media_amount,
           n.path                     AS hierarchy_path,
           j.journal_id,
          decode(j.journal_type,'Sweeps',1,0) Sweepstakes_won,
           au_su.last_name sender_last_name,
           au_su.first_name sender_first_name,
           au_su.user_id sender_user_id
           ,NVL(j.award_type,p.award_type) as award_payout_type  --05/05/2016 --06/22/2016
           ,j.user_currency as user_currency   --05/05/2016
           ,mo.asset_key_level_name            --05/05/2016 
      FROM journal j,
           promotion P,
           application_user au,
           application_user au_su,
           activity_journal aj,
           activity A,
           claim c,
           vw_curr_pax_employer cpe,
           ( -- rank user node records so result can be restricted to one record
             -- this select duplicates the PKG_REPORT_COMMON.P_USER_DETAILS process by randomly ranking the records for users in multiple nodes
             SELECT RANK() OVER (PARTITION BY un.user_id ORDER BY ROWNUM) AS un_rank,
                    un.user_id,
                    un.node_id
               FROM user_node un
           ) unr,
           node n
           ,(select mo.claim_id,
                   pm.promo_merch_program_level_id,
                   pm.cm_asset_key as asset_key_level_name,
                   pm.om_level_name
            from merch_order mo,
                 promo_merch_program_level pm
            where mo.promo_merch_program_level_id = pm.promo_merch_program_level_id) mo  --05/05/2016
     WHERE j.promotion_id = P.promotion_id
       AND SUBSTR(j.transaction_description,0,33) <> 'CERTIFICATE CONVERSION for Cert: '
       AND NOT(P.promotion_type = 'product_claim' AND j.status_type != 'post') --02/21/2011 Bug # 35014 Fix
       AND j.user_id = au.user_id
     --AND j.transaction_type<>'reverse' --03/28/2019 Bug 78869
       AND (CASE WHEN P.PROMOTION_TYPE='recognition' THEN '1' ELSE j.transaction_type END) <>  --03/28/2019 Bug 78869
                (CASE WHEN P.PROMOTION_TYPE='recognition' THEN '2' ELSE 'reverse' END)          --03/28/2019 Bug 78869  
       AND j.status_type <> 'approve'
       AND j.journal_id = aj.journal_id(+) AND aj.activity_id= A.activity_id(+) --08/07/2013
       AND A.claim_id= c.claim_id(+)
       AND c.submitter_id = au_su.user_id(+)
       AND au.user_id = unr.user_id
       AND unr.un_rank = 1
       AND unr.node_id = n.node_id
       AND j.user_id = cpe.user_id (+)
       --AND LOWER(p.promotion_status) = 'live'      -- Bug 38589
       AND LOWER(P.promotion_status) IN ('live','expired') --03/21/2012
       AND P.is_deleted = 0                                --03/21/2012       
     --AND  NOT exists (select * from recognition_claim where claim_id=c.claim_id and is_reverse=1) --02/24/2014 -03/28/2019 Bug 78869
       AND a.claim_id = mo.claim_id (+)                                         --05/05/2016 
       AND (p_start_date < j.date_created  AND j.date_created <= p_end_date
        OR  p_start_date < j.date_modified AND j.date_modified <= p_end_date)
        UNION ALL --09/09/2015
        SELECT P.award_type       AS media_type,
           a.node_id,
           n.NAME                     AS node_name,
           TRUNC(c.submission_date)  AS media_date,
           p.promotion_id,
           P.promotion_name,
           P.promotion_type,
           P.is_taxable,
           n.hierarchy_id,
           a.user_id,
           fnc_format_user_name(au.last_name, au.first_name, au.middle_name, au.suffix) AS participant_name,
           INITCAP(au.first_name)     AS pax_first_name,
           TRIM(au.middle_name)       AS pax_middle_name,
           INITCAP(au.last_name)      AS pax_last_name,
           DECODE(au.is_active, 1, 'active', 'inactive') AS participant_current_status,
           au.user_name,
           fnc_get_badge_count_by_user(au.user_id,P.promotion_type,TRUNC(c.submission_date)) badges_earned,
           NVL(cpe.position_type, ' ') AS position_type,
           NVL(cpe.department_type, ' ') AS department,
           0          AS media_amount,
           n.path                     AS hierarchy_path,
           NULL journal_id,
           0 Sweepstakes_won,
           au_su.last_name sender_last_name,
           au_su.first_name sender_first_name,
           au_su.user_id sender_user_id
           ,p.award_type  as award_payout_type --05/05/2016 --06/22/2016
           ,NULL  as user_currency   --05/05/2016
           ,NULL  as asset_key_level_name  --05/05/2016
  FROM activity a, claim c,
           promotion P,
           application_user au,
           application_user au_su,
           node n,
           vw_curr_pax_employer cpe
 WHERE
c.claim_id = a.claim_id
AND c.submitter_id = au_su.user_id
AND a.is_submitter = 0
AND a.user_id = au.user_id
AND c.promotion_id = p.promotion_id
AND NOT EXISTS (SELECT * FROM activity_journal WHERE activity_id = a.activity_id)
AND a.node_id = n.node_id
AND P.AWARD_TYPE ='merchandise'
AND a.user_id = cpe.user_id (+)
AND (p_start_date < c.submission_date  AND c.submission_date <= p_end_date
OR  p_start_date < c.date_modified AND c.date_modified <= p_end_date)
UNION ALL --05/05/2016
    SELECT pnl.award_payout_type  AS media_type,   --'other'
           a.node_id,
           n.NAME                     AS node_name,
           TRUNC(c.submission_date)  AS media_date,
           p.promotion_id,
           P.promotion_name,
           P.promotion_type,
           P.is_taxable,
           n.hierarchy_id,
           a.user_id,
           fnc_format_user_name(au.last_name, au.first_name, au.middle_name, au.suffix) AS participant_name,
           INITCAP(au.first_name)     AS pax_first_name,
           TRIM(au.middle_name)       AS pax_middle_name,
           INITCAP(au.last_name)      AS pax_last_name,
           DECODE(au.is_active, 1, 'active', 'inactive') AS participant_current_status,
           au.user_name,
           0 badges_earned,
           NVL(cpe.position_type, ' ') AS position_type,
           NVL(cpe.department_type, ' ') AS department,
           pnl.payout_value           AS media_amount,
           n.path                     AS hierarchy_path,
           NULL journal_id,
           0 Sweepstakes_won,
           au_su.last_name sender_last_name,
           au_su.first_name sender_first_name,
           au_su.user_id sender_user_id
           ,pnl.award_payout_type  as award_payout_type --05/05/2016
           ,pnl.payout_currency  as user_currency   --05/05/2016
           ,pnl.payout_description_asset_code  as asset_key_level_name  --10/05/2016
FROM claim_item_approver cia,
     claim_item ci,
     claim c,
     activity a,
     promo_nomination pn,
     promo_nomination_level pnl,
     promotion P,
     application_user au,
     application_user au_su,
     node n,
     vw_curr_pax_employer cpe
WHERE pnl.award_payout_type = 'other'  
     AND (cia.claim_item_id = ci.claim_item_id OR c.claim_group_id = cia.claim_group_id ) --10/05/2016
     AND ci.claim_id = c.claim_id 
     AND (c.claim_id = a.claim_id OR c.claim_group_id = a.claim_group_id )--10/05/2016
     AND c.promotion_id = pnl.promotion_id 
     AND c.promotion_id = pn.promotion_id --10/05/2016
     AND ((pnl.level_index = cia.approval_round AND pn.payout_level_type = 'eachLevel') OR pn.payout_level_type = 'finalLevel' ) --10/05/2016
      AND cia.approval_status_type = 'winner' --10/05/2016
      AND cia.approval_round = a.approval_round--10/05/2016
      AND c.submitter_id = au_su.user_id
      AND a.is_submitter = 0
      AND a.user_id = au.user_id
      AND c.promotion_id = p.promotion_id
      AND NOT EXISTS (SELECT aj.activity_id 
                        FROM activity_journal aj 
                        WHERE aj.activity_id = a.activity_id)
      AND a.node_id = n.node_id
      AND a.user_id = cpe.user_id (+)
      AND (p_start_date < cia.date_created  AND cia.date_created <= p_end_date--10/05/2016
      OR  p_start_date < cia.date_modified AND cia.date_modified <= p_end_date);--10/05/2016

  --Cursor to pick the users whose information is updated in
  --application_user,participant_employer table
  CURSOR cur_pax_info_changed IS
    SELECT au.user_id,
           INITCAP(au.last_name) last_name,--04/08/2014 Bug #52680
           INITCAP(au.first_name) first_name,--04/08/2014 Bug #52680
           au.middle_name,
           au.suffix,
           DECODE(au.is_active,1,'active','inactive') pax_status,
           vcpe.department_type,
           vcpe.position_type,
           rpt.participant_current_status  pax_status_old,
           rpt.position_type               job_position_old,
           rpt.department                  department_old,
           rpt.country_id           AS      country_id_old, --05/18/2017
           c.country_id,                                    --05/18/2017
           au2.user_id sender_user_id,
           au2.last_name sender_last_name,
           au2.first_name sender_first_name,
           rpt.node_id,
           rpt.journal_id,
           rpt.promotion_id,
           rpt.media_date,
           rpt.media_amount,
           rpt.media_type,          
           rpt.sweepstakes_won, 
           rpt.badges_earned, 
           rpt.plateau_earned
      FROM application_user au,
           rpt_awardmedia_detail rpt,
           application_user au2,
           vw_curr_pax_employer vcpe,
           user_address ua,  --05/18/2017
           country c         --05/18/2017
     WHERE au.user_id   = rpt.user_id
       AND vcpe.user_id = rpt.user_id
       AND rpt.sender_user_id = au2.user_id(+)
       AND au.user_id = ua.user_id  (+)     --05/18/2017
       AND ua.is_primary (+) = 1            --05/18/2017
       AND ua.country_id = c.country_id (+) --05/18/2017
          AND ((p_start_date < au.date_created AND au.date_created <= p_end_date)
            OR (p_start_date < au.date_modified  AND  au.date_modified <= p_end_date)
            OR (p_start_date < au2.date_created AND au2.date_created <= p_end_date)
            OR (p_start_date < au2.date_modified  AND  au2.date_modified <= p_end_date)
            OR (p_start_date < ua.date_created AND ua.date_created <= p_end_date)       --05/18/2017
            OR (p_start_date < ua.date_modified  AND  ua.date_modified <= p_end_date)   --05/18/2017
            OR (p_start_date < vcpe.date_modified  AND  vcpe.date_modified <= p_end_date) -- 06/17/2014
            OR (p_start_date < vcpe.date_created  AND  vcpe.date_created <= p_end_date)) --06/17/2014
       AND (au.last_name            <> rpt.pax_last_name
            OR au.first_name        <> rpt.pax_first_name
            OR au.middle_name       <> rpt.pax_middle_name
            OR DECODE(au.is_active,1,'active','inactive') <> rpt.participant_current_status
            OR vcpe.department_type <> rpt.department
            OR vcpe.position_type   <> rpt.position_type
             OR au2.last_name            <> rpt.sender_last_name
            OR au2.first_name        <> rpt.sender_first_name);
         
           /*--03/28/2019 Bug 78869               
         CURSOR cur_reversed IS --02/24/2014
         SELECT rpt.*
          FROM rpt_awardmedia_detail rpt
        WHERE EXISTS (SELECT * 
                        FROM journal j 
                       WHERE j.is_reverse = 1 
                         AND j.journal_id = rpt.journal_id); --08/24/2017  G6-2885
                         */ --03/28/2019 Bug 78869
           /*activity_journal aj,  --05/08/2017
           activity a
         WHERE rpt.journal_id =  aj.journal_id
         AND aj.activity_id  = a.activity_id
         AND EXISTS (SELECT * FROM recognition_claim WHERE is_reverse=1 AND claim_id = a.claim_id);*/ 
       
  --Cursor hierarchy
  CURSOR cur_hier (p_in_node_id rpt_hierarchy.node_id%TYPE) IS
    SELECT parent_node_id header_node_id,
           node_id,
           hier_level,
           is_leaf
      FROM rpt_hierarchy
     START WITH node_id = p_in_node_id
   CONNECT BY PRIOR parent_node_id = node_id;       

  --Cursor to pick modified node name
  CURSOR cur_node_changed IS
    SELECT node_id,
           NAME
      FROM node
     WHERE date_modified BETWEEN p_start_date AND p_end_date;  

  --Constants
  c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_awardmedia_detail');
  c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;

  --Procedure variables
  v_stage                   execution_log.text_line%TYPE;
  v_rec_cnt                 INTEGER;
  v_rpt_awardmedia_dtl_id   rpt_awardmedia_detail.rpt_awardmedia_detail_id%TYPE;
  v_ins_detail              VARCHAR2(1);
  v_claim_id                activity.claim_id%TYPE;
  v_sweepstakes_won         PLS_INTEGER := 0;
  v_plateau_earned          PLS_INTEGER := 0;
  v_media_amount_old        PLS_INTEGER := 0;  
  v_sweepstakes_won_old     PLS_INTEGER := 0;
  v_badges_earned_old       PLS_INTEGER := 0;
  v_plateau_earned_old      PLS_INTEGER := 0;
  v_country_id              rpt_awardmedia_detail.country_id%TYPE;
  v_tab_node_id             dbms_sql.number_table;
  v_tab_user_id              dbms_sql.number_table;
  v_tab_node_name           dbms_sql.varchar2_table;
  v_header_node_id          rpt_hierarchy.parent_node_id%TYPE;
  v_node_id                 rpt_hierarchy.node_id%TYPE;
  v_hier_level              rpt_hierarchy.hier_level%TYPE;
  v_is_leaf                 rpt_hierarchy.is_leaf%TYPE;
  v_summ_id                 rpt_awardmedia_summary.rpt_awardmedia_summary_id%TYPE;
  v_media_amount            rpt_awardmedia_summary.total_media_amount%TYPE;
  v_parm_list     execution_log.text_line%TYPE;   --03/11/2019  Bug 78631
  
BEGIN
v_stage := 'initialize variables'; 
v_parm_list := 
'p_in_requested_user_id :='||p_user_id ||CHR(10)||
'p_in_start_date        :='||p_start_date        ||CHR(10)|| 
'p_in_end_date          :='||p_end_date          ; 

  v_stage := 'Start';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
  
  FOR rec_detail IN cur_detail LOOP

    v_ins_detail          := 'N';
    v_media_amount_old    := 0;   
    v_sweepstakes_won_old := 0;
    v_sweepstakes_won     := 0;  -- 09/04/2018
    v_badges_earned_old   := 0;
    v_plateau_earned_old  := 0;
                 
    BEGIN
      v_stage := 'Find claim id';
      SELECT act.claim_id
        INTO v_claim_id
        FROM journal j,
             activity_journal aj,
             activity act
       WHERE j.journal_id   = aj.journal_id
         AND aj.activity_id = act.activity_id
         AND j.journal_id   = rec_detail.journal_id
         AND ROWNUM < 2;          

     v_sweepstakes_won := rec_detail.sweepstakes_won;

    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_claim_id := NULL;
    END;
      
    v_stage := 'Set plateau earned';
    IF rec_detail.media_type = 'merchandise' THEN
      v_plateau_earned := 1;
    ELSE
      v_plateau_earned := 0;  
    END IF;

    BEGIN
      v_stage := 'Find detail record';
      SELECT rpt_awardmedia_detail_id,
             media_amount,           
             sweepstakes_won,
             --badges_earned,
             plateau_earned
        INTO v_rpt_awardmedia_dtl_id,
             v_media_amount_old,            
             v_sweepstakes_won_old,
             --v_badges_earned_old,
             v_plateau_earned_old
        FROM rpt_awardmedia_detail
       WHERE journal_id = rec_detail.journal_id;  
    
      --update detail table only when changed
      IF rec_detail.media_amount <> v_media_amount_old OR        
         v_sweepstakes_won       <> v_sweepstakes_won_old THEN
        
        v_stage := 'Update rpt_awardmedia_detail';
        UPDATE rpt_awardmedia_detail
           SET media_amount    = rec_detail.media_amount,               
               sweepstakes_won = v_sweepstakes_won,
               modified_by     = p_user_id,
               date_modified   = SYSDATE
         WHERE rpt_awardmedia_detail_id = v_rpt_awardmedia_dtl_id;
          
      END IF;
      
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_ins_detail := 'Y';    
    END;

    IF v_ins_detail = 'Y' THEN

      BEGIN
        v_stage := 'Find country id for the user '||rec_detail.user_id;
        SELECT country_id
          INTO v_country_id
          FROM user_address
         WHERE user_id    = rec_detail.user_id
           AND is_primary = 1;
      EXCEPTION
        WHEN OTHERS THEN
          v_country_id := NULL;         
      END;

      v_stage := 'INSERT rpt_awardmedia_detail';
      INSERT INTO rpt_awardmedia_detail
                  (rpt_awardmedia_detail_id,
                   media_type,
                   node_id,
                   node_name,
                   media_date,
                   promotion_id,
                   promotion_name,
                   promotion_type,
                   is_taxable,
                   hierarchy_id,
                   user_id,
                   participant_name,
                   pax_first_name,
                   pax_middle_name,
                   pax_last_name,
                   participant_current_status,
                   user_name,
                   position_type,
                   department,
                   media_amount,
                   date_created,
                   created_by,
                   hierarchy_path,
                   journal_id,
                   country_id,
                   points,                   
                   sweepstakes_won,
                   badges_earned,
                   VERSION,
                   plateau_earned,
                   sender_first_name,
                   sender_last_name,
                   sender_user_id
                   ,award_payout_type       --05/05/2016
                   ,user_currency           --05/05/2016
                   ,asset_key_level_name    --05/05/2016
                   )
           VALUES (rpt_awardmedia_detail_pk_sq.NEXTVAL,
                   rec_detail.media_type,
                   rec_detail.node_id,
                   rec_detail.node_name,
                   rec_detail.media_date,
                   rec_detail.promotion_id,
                   rec_detail.promotion_name,
                   rec_detail.promotion_type,
                   rec_detail.is_taxable,
                   rec_detail.hierarchy_id,
                   rec_detail.user_id,
                   rec_detail.participant_name,
                   rec_detail.pax_first_name,
                   rec_detail.pax_middle_name,
                   rec_detail.pax_last_name,
                   rec_detail.participant_current_status,
                   rec_detail.user_name,
                   rec_detail.position_type,
                   rec_detail.department,
                   rec_detail.media_amount,
                   SYSDATE,
                   p_user_id,
                   rec_detail.hierarchy_path,
                   rec_detail.journal_id,
                   v_country_id,
                   rec_detail.media_amount,                   
                   v_sweepstakes_won,
                   rec_detail.badges_earned,
                   1,
                   v_plateau_earned,
                   rec_detail.sender_first_name,
                   rec_detail.sender_last_name,
                   rec_detail.sender_user_id
                   ,rec_detail.award_payout_type    --05/05/2016
                   ,rec_detail.user_currency        --05/05/2016
                   ,rec_detail.asset_key_level_name --05/05/2016
                   );
     
    END IF;  
   
  END LOOP;

  FOR rec_pax_info_changed IN cur_pax_info_changed LOOP
 
    --when pax info changed make adjustment to summary table
    --when pax info updated in detail table, we may not aware what info got changed to what
    --in summary process so doing summary table adjustment here itself  
    IF rec_pax_info_changed.pax_status      <> rec_pax_info_changed.pax_status_old OR
       rec_pax_info_changed.department_type <> rec_pax_info_changed.department_old OR
       rec_pax_info_changed.position_type   <> rec_pax_info_changed.job_position_old THEN

      v_stage := 'Get hier_level for node_id: '||rec_pax_info_changed.node_id;
      BEGIN
        SELECT parent_node_id header_node_id,
               node_id,
               hier_level,
               is_leaf
          INTO v_header_node_id,
               v_node_id,
               v_hier_level,
               v_is_leaf
          FROM rpt_hierarchy
         WHERE node_id = rec_pax_info_changed.node_id;
      END;

      v_stage := 'Check summary record for detail node_id: '||rec_pax_info_changed.node_id;
      BEGIN

        SELECT rpt_awardmedia_summary_id,
               total_media_amount
          INTO v_summ_id,
               v_media_amount     
          FROM rpt_awardmedia_summary
         WHERE record_type      = v_hier_level||'-teamsum'
           AND NVL(header_node_id, 0) = NVL(v_header_node_id, 0)
           AND detail_node_id   = rec_pax_info_changed.node_id
           AND promotion_id     = rec_pax_info_changed.promotion_id
           AND pax_status       = rec_pax_info_changed.pax_status_old
           AND job_position     = rec_pax_info_changed.job_position_old
           AND department       = rec_pax_info_changed.department_old
           AND media_date       = rec_pax_info_changed.media_date
           AND media_type       = rec_pax_info_changed.media_type --05/12/2017
           AND country_id       = rec_pax_info_changed.country_id_old --05/18/2017
           AND hier_level       = v_hier_level
           AND is_leaf          = 1;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_media_amount := 0;
      END;
       
      --Commented 09/04/2013 
      /*IF v_media_amount = rec_pax_info_changed.media_amount THEN
        --media amount matches so update pax info
        v_stage := 'Update summary teamsum record for detail node_id: '||rec_pax_info_changed.node_id;
        UPDATE rpt_awardmedia_summary
           SET pax_status   = rec_pax_info_changed.pax_status,
               job_position = rec_pax_info_changed.position_type,
               department   = rec_pax_info_changed.department_type
         WHERE rpt_awardmedia_summary_id = v_summ_id;
      
        FOR rec_hier IN cur_hier (rec_pax_info_changed.node_id) LOOP

          v_stage := 'Update summary nodesum record for node_id: '||rec_hier.node_id;
          UPDATE rpt_awardmedia_summary
             SET pax_status   = rec_pax_info_changed.pax_status,
                 job_position = rec_pax_info_changed.position_type,
                 department   = rec_pax_info_changed.department_type
           WHERE record_type      = rec_hier.hier_level||'-nodesum'
             AND NVL(header_node_id, 0) = NVL(rec_hier.header_node_id, 0)
             AND detail_node_id   = rec_hier.node_id
             AND promotion_id     = rec_pax_info_changed.promotion_id
             AND pax_status       = rec_pax_info_changed.pax_status_old
             AND job_position     = rec_pax_info_changed.job_position_old
             AND department       = rec_pax_info_changed.department_old
             AND media_date       = rec_pax_info_changed.media_date
             AND hier_level       = rec_hier.hier_level
             AND NVL(is_leaf,0)   = NVL(rec_hier.is_leaf,0);
        
        END LOOP;
      
      ELSIF v_media_amount > rec_pax_info_changed.media_amount THEN*/
        --media amount not matches so minus the amount for pax info changed records and
        --create new summary record to new info 
        v_stage := 'Minus summary teamsum record for detail node_id: '||rec_pax_info_changed.node_id;
        UPDATE rpt_awardmedia_summary
           SET total_media_amount = total_media_amount - rec_pax_info_changed.media_amount,
               date_modified      = SYSDATE,
               modified_by        = p_user_id
         WHERE rpt_awardmedia_summary_id = v_summ_id;
        
        v_stage := 'Add summary teamsum record for detail node_id: '||rec_pax_info_changed.node_id;
        UPDATE rpt_awardmedia_summary   --Added 09/04/2013 
           SET total_media_amount = total_media_amount + rec_pax_info_changed.media_amount,
               date_modified      = SYSDATE,
               modified_by        = p_user_id
         WHERE record_type      = v_hier_level||'-teamsum'
           AND NVL(header_node_id, 0) = NVL(v_header_node_id, 0)
           AND detail_node_id   = rec_pax_info_changed.node_id
           AND promotion_id     = rec_pax_info_changed.promotion_id
           AND pax_status       = rec_pax_info_changed.pax_status
           AND job_position     = rec_pax_info_changed.position_type
           AND department       = rec_pax_info_changed.department_type
           AND media_date       = rec_pax_info_changed.media_date
           AND media_type       = rec_pax_info_changed.media_type --05/12/2017
           AND country_id       = rec_pax_info_changed.country_id --05/18/2017
           AND hier_level       = v_hier_level
           AND is_leaf          = 1;
        
        IF SQL%ROWCOUNT = 0 THEN   --Added If 09/04/2013 
          v_stage := 'Insert rpt_awardmedia_summary for teamsum records'||rec_pax_info_changed.node_id||','||rec_pax_info_changed.media_date||','||rec_pax_info_changed.position_type||','||
                     rec_pax_info_changed.department_type||','||rec_pax_info_changed.promotion_id;
          INSERT INTO rpt_awardmedia_summary 
                    (rpt_awardmedia_summary_id, detail_node_id, record_type, 
                     promotion_id, pax_status, job_position, country_id, --05/18/2017
                     department, media_date, media_type, 
                     header_node_id, hier_level, is_leaf, 
                     total_media_amount, created_by, date_created, 
                      sweepstakes_won, badges_earned, 
                     plateau_earned) 
             VALUES (rpt_awardmedia_summary_pk_sq.NEXTVAL, rec_pax_info_changed.node_id, v_hier_level||'-teamsum',
                     rec_pax_info_changed.promotion_id, rec_pax_info_changed.pax_status, rec_pax_info_changed.position_type, rec_pax_info_changed.country_id, --05/18/2017
                     rec_pax_info_changed.department_type, rec_pax_info_changed.media_date, rec_pax_info_changed.media_type,
                     v_header_node_id, v_hier_level, 1, --v_is_leaf, --09/24/2013 Replaced with 1
                     rec_pax_info_changed.media_amount, p_user_id, SYSDATE,
                     rec_pax_info_changed.sweepstakes_won, rec_pax_info_changed.badges_earned, 
                     rec_pax_info_changed.plateau_earned);
        END IF;
        
        --FOR rec_hier IN cur_hier (rec_pax_info_changed.node_id) LOOP  --Commented 09/04/2013 

          v_stage := 'Minus summary nodesum record for node_id: '||rec_pax_info_changed.node_id;
          UPDATE rpt_awardmedia_summary
             SET total_media_amount = total_media_amount - rec_pax_info_changed.media_amount,
                 date_modified      = SYSDATE,
                 modified_by        = p_user_id
           WHERE record_type      = v_hier_level||'-nodesum' -- rec_hier.hier_level||'-nodesum'  --Commented 09/04/2013 
             AND NVL(header_node_id, 0) = NVL(v_header_node_id,0) --NVL(rec_hier.header_node_id, 0) --Commented 09/04/2013 
             AND detail_node_id   = rec_pax_info_changed.node_id --rec_hier.node_id   --Commented 09/04/2013 
             AND promotion_id     = rec_pax_info_changed.promotion_id
             AND pax_status       = rec_pax_info_changed.pax_status_old
             AND job_position     = rec_pax_info_changed.job_position_old
             AND department       = rec_pax_info_changed.department_old
             AND media_date       = rec_pax_info_changed.media_date
             AND media_type       = rec_pax_info_changed.media_type --05/12/2017
             AND country_id       = rec_pax_info_changed.country_id_old --05/18/2017
             AND hier_level       = v_hier_level --rec_hier.hier_level  --Commented 09/04/2013 
             AND NVL(is_leaf,0)   = v_is_leaf; --NVL(rec_hier.is_leaf,0);  --Commented 09/04/2013 
          
           v_stage := 'Add summary nodesum record for node_id: '||rec_pax_info_changed.node_id;
          UPDATE rpt_awardmedia_summary   --Added 09/04/2013 
             SET total_media_amount = total_media_amount + rec_pax_info_changed.media_amount,
                 date_modified      = SYSDATE,
                 modified_by        = p_user_id
           WHERE record_type      = v_hier_level||'-nodesum' 
             AND NVL(header_node_id, 0) = NVL(v_header_node_id,0) 
             AND detail_node_id   = rec_pax_info_changed.node_id 
             AND promotion_id     = rec_pax_info_changed.promotion_id
             AND pax_status       = rec_pax_info_changed.pax_status
             AND job_position     = rec_pax_info_changed.position_type
             AND department       = rec_pax_info_changed.department_type
             AND media_date       = rec_pax_info_changed.media_date
             AND media_type       = rec_pax_info_changed.media_type --05/12/2017
             AND country_id       = rec_pax_info_changed.country_id --05/18/2017
             AND hier_level       = v_hier_level 
             AND NVL(is_leaf,0)   = v_is_leaf; 
             
          IF SQL%ROWCOUNT = 0 THEN     --Added If 09/04/2013 
            v_stage := 'Insert rpt_awardmedia_summary for nodesum records'||rec_pax_info_changed.node_id||','||rec_pax_info_changed.media_date||','||rec_pax_info_changed.position_type||','||
                     rec_pax_info_changed.department_type||','||rec_pax_info_changed.promotion_id;
            INSERT INTO rpt_awardmedia_summary 
                      (rpt_awardmedia_summary_id, detail_node_id, record_type, 
                       promotion_id, pax_status, job_position,country_id, --05/18/2017 
                       department, media_date, media_type, 
                       header_node_id, hier_level, is_leaf, 
                       total_media_amount, created_by, date_created, 
                        sweepstakes_won, badges_earned, 
                       plateau_earned) 
               VALUES (rpt_awardmedia_summary_pk_sq.NEXTVAL, rec_pax_info_changed.node_id, v_hier_level||'-nodesum', --rec_hier.node_id, rec_hier.hier_level||'-nodesum', --Commented 09/04/2013 
                       rec_pax_info_changed.promotion_id, rec_pax_info_changed.pax_status, rec_pax_info_changed.position_type, rec_pax_info_changed.country_id, --05/18/2017
                       rec_pax_info_changed.department_type, rec_pax_info_changed.media_date, rec_pax_info_changed.media_type,
                       v_header_node_id,v_hier_level,v_is_leaf,-- rec_hier.header_node_id, rec_hier.hier_level, rec_hier.is_leaf,  --Commented 09/04/2013 
                       rec_pax_info_changed.media_amount, p_user_id, SYSDATE,
                       rec_pax_info_changed.sweepstakes_won, rec_pax_info_changed.badges_earned, 
                       rec_pax_info_changed.plateau_earned);
                       
          END IF;
        --END LOOP;  --Commented 09/04/2013 
          
      --END IF;  --Commented 09/04/2013 
       
    END IF;    

    --Update rpt_awardmedia_detail
    --Summary table adjustment already done, so don't set date modified else 
    --summary process will look date modified records for processing 
    v_stage := 'Update rpt_awardmedia_detail';
    UPDATE rpt_awardmedia_detail
       SET participant_name = fnc_format_user_name(rec_pax_info_changed.last_name, 
                                                   rec_pax_info_changed.first_name, 
                                                   rec_pax_info_changed.middle_name, 
                                                   rec_pax_info_changed.suffix),     
           pax_first_name   = rec_pax_info_changed.first_name,
           pax_middle_name  = rec_pax_info_changed.middle_name,
           pax_last_name    = rec_pax_info_changed.last_name,
           sender_first_name   = rec_pax_info_changed.sender_first_name,
           sender_last_name  = rec_pax_info_changed.sender_last_name,          
           participant_current_status = rec_pax_info_changed.pax_status,
           country_id       = rec_pax_info_changed.country_id, --05/18/2017
           position_type    = rec_pax_info_changed.position_type,
           department       = rec_pax_info_changed.department_type,
           date_modified    = SYSDATE,
           modified_by   = p_user_id,
           VERSION          = VERSION + 1
     WHERE user_id    = rec_pax_info_changed.user_id
       AND journal_id = rec_pax_info_changed.journal_id;    
             
  END LOOP;
/*--03/28/2019 Bug 78869  
  v_stage := 'Open and cursor to process reversed journals'; --02/24/2014
FOR rec_reversed IN cur_reversed LOOP

v_stage := 'Deduct Summary record for reversed journals';

UPDATE rpt_awardmedia_summary
SET total_media_amount = total_media_amount-rec_reversed.media_amount
WHERE
   detail_node_id = rec_reversed.node_id
   AND promotion_id     = rec_reversed.promotion_id
             AND pax_status       = rec_reversed.participant_current_status
             AND job_position     = rec_reversed.position_type
             AND department       = rec_reversed.department
             AND media_date       = rec_reversed.media_date
             AND media_type       = rec_reversed.media_type --05/18/2017
             AND country_id       = rec_reversed.country_id; --05/18/2017
             
             
v_stage := 'Remove detail record for reversed journals';
DELETE FROM rpt_awardmedia_detail WHERE rpt_awardmedia_detail_id = rec_reversed.rpt_awardmedia_detail_id;

END LOOP;  
*/--03/28/2019 Bug 78869
  v_stage := 'Open and Fetch cursor to pick modified node name';
  OPEN cur_node_changed;
  FETCH cur_node_changed BULK COLLECT
   INTO v_tab_node_id,
        v_tab_node_name;
  CLOSE cur_node_changed;   

  --Summary table won't have node name so don't set date modified else 
  --summary process will look date modified records for processing
  v_stage := 'Update modified node name for giver and receiver in rpt table';
  FORALL indx IN v_tab_node_id.FIRST .. v_tab_node_id.LAST
    UPDATE rpt_awardmedia_detail
       SET node_name     = DECODE (node_id, v_tab_node_id(indx), v_tab_node_name(indx), node_name),
           date_modified = SYSDATE,
           modified_by   = p_user_id,
           VERSION       = VERSION + 1
     WHERE (node_id        = v_tab_node_id(indx)
            AND node_name != v_tab_node_name(indx)
            );

  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  p_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    p_return_code := 99;
    p_error_message := SUBSTR(SQLERRM,1,250);
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage ||v_parm_list|| SQLCODE || ', ' || SQLERRM, NULL);
END p_rpt_awardmedia_detail;

PROCEDURE p_rpt_awardmedia_summary
  (p_user_id       IN NUMBER,
   p_start_date    IN DATE, 
   p_end_date      IN DATE, 
   p_return_code   OUT NUMBER,
   p_error_message OUT VARCHAR2)
IS

/*******************************************************************************

   Purpose:  Populate the awardmedia_summary reporting tables

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray    11/07/2005  Initial packaging of awardmedia report procedures
   D Murray    12/06/2005  Added nvl to all queries selecting promotion_id, media_type,
                           and media_date - can not have null values for Jasper report parameters.
   Raju N      12/14/2005  Fixed the code to include the pax_status,job_position
                           department.
   Raju N      01/05/2005  moved the PK into the trigger to avoid the duplicate values.
   TBD: Break up the select into to a loop.
   Rachel R    01/13/2006  Added refresh date logic for summary, pie, bar, and trend.
   J Flees     08/31/2011 Report Refresh Performance.
                          Replaced hierarchy looping process with the following bulk processng SQL statements.
                             1.) Remove obsolete node records.
                             2.) Derive summary records based on the detail records.
                             3.) Insert default permutations to ensure report data can be searched from root node to all leaf nodes.
                             4.) Remove default permutation summaries that now have detail derived summaries.
   Arun S      10/03/2012  G5 Report changes
                           Modified procedure to process based on input start and end date 
               07/12/2013  Fixed the issue causing the duplicate data in summary table. HAd to change OR to AND in merge statement. 
  Ravi Dhanekula 06/18/2014  Added a performance fix for default teamsum insert.
  Ravi Arumugam  Bug#65770 02/22/2016 Award received-Participant by organization-Exported file is mismatched point with deleted org unit. 
   SherifBasha   04/10/2016  Bug 69353 - Report Refresh Failing
*******************************************************************************/

   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_awardmedia_summary');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
   c_created_by         CONSTANT rpt_awardmedia_summary.created_by%TYPE := 0;

   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;
   v_parm_list     		execution_log.text_line%TYPE;   --03/11/2019  Bug 78631
BEGIN
v_stage := 'initialize variables'; 
v_parm_list := 
'p_in_requested_user_id :='||p_user_id ||CHR(10)||
'p_in_start_date        :='||p_start_date        ||CHR(10)|| 
'p_in_end_date          :='||p_end_date          ; 

  v_stage := 'Start';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  -- remove obsolete node summaries (node not found in rpt_hierarchy)
  v_stage := 'DELETE obsolete node summary records';
  DELETE
    FROM rpt_awardmedia_summary s
   WHERE s.detail_node_id NOT IN
         ( -- get node ID currently in the report hierarchy
           SELECT h.node_id
             FROM rpt_hierarchy h
         );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
   
      -- merge derived summary records
   v_stage := 'MERGE detail derived summary records';
   MERGE INTO rpt_awardmedia_summary d
   USING (
            WITH rpt_teamsum AS
            (  -- build team summary records
               SELECT -- key fields
                      d.node_id AS detail_node_id,
                      d.promotion_id,
                      d.participant_current_status AS pax_status,
                      d.position_type AS job_position,
                      d.department,
                      d.country_id, --03/09/2017
                      d.media_date,
                      -- reference fields
                      d.media_type,
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      -- count fields
                      NVL(SUM(d.media_amount),0)    AS total_media_amount,
                      NVL(SUM(d.sweepstakes_won),0) AS sweepstakes_won,
                      NVL(SUM(d.badges_earned),0)   AS badges_earned,
                      NVL(SUM(d.plateau_earned),0)  AS plateau_earned
                 FROM rpt_awardmedia_detail d,
                      rpt_hierarchy h,
                      journal j
                WHERE h.node_id    = d.node_id
                  AND d.journal_id = j.journal_id
                  AND (
                       (p_start_date < j.date_created  AND j.date_created  <= p_end_date)
                   OR  (p_start_date < j.date_modified AND j.date_modified <= p_end_date)
                   OR  (p_start_date < d.date_modified AND d.date_modified <= p_end_date)
                       )
                GROUP BY d.node_id,
                      d.promotion_id,
                      d.participant_current_status,
                      d.position_type,
                      d.department,
                      d.country_id, --03/09/2017
                      d.media_date,
                      d.media_type,
                      h.parent_node_id,
                      h.hier_level
            ), detail_derived_summary AS
            (  -- derive summaries based on team summary data
               SELECT -- key fields
                      rt.detail_node_id,
                      rt.hier_level||'-teamsum' AS sum_type,
                      rt.promotion_id,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.country_id, --03/09/2017
                      rt.media_date,
                      -- reference fields
                      rt.media_type,
                      rt.header_node_id,
                      rt.hier_level,
                      1 AS is_leaf, -- The team summary records are always a leaf
                      -- count fields
                      rt.total_media_amount,                      
                      rt.sweepstakes_won,
                      rt.badges_earned,
                      rt.plateau_earned
                 FROM rpt_teamsum rt
                UNION ALL
               SELECT -- key fields
                      h.node_id AS detail_node_id,
                      h.hier_level||'-nodesum' AS sum_type,
                      rt.promotion_id,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.country_id, --03/09/2017
                      rt.media_date,
                      -- reference fields
                      rt.media_type,
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      h.is_leaf,
                      -- count fields
                      NVL(SUM(rt.total_media_amount),0) AS total_media_amount,                      
                      NVL(SUM(rt.sweepstakes_won),0)    AS sweepstakes_won,
                      NVL(SUM(rt.badges_earned),0)      AS badges_earned,
                      NVL(SUM(rt.plateau_earned),0)     AS plateau_earned
                 FROM ( -- associate each node to all its hierarchy nodes
                        SELECT np.node_id,
                               p.column_value AS path_node_id
                          FROM ( -- get node hierarchy path
                                 SELECT h.node_id,
                                        h.hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                   FROM rpt_hierarchy h
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
                      rpt_hierarchy h,
                      rpt_teamsum rt
                WHERE (  rt.hier_level = h.hier_level    -- always create node summary at team summary level
                      AND rt.total_media_amount != 0      -- create node summary for team summaries with non-zero media amounts --07/12/2013
                      )
                   -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
                  AND rt.detail_node_id = npn.node_id
                  AND npn.path_node_id = h.node_id
                GROUP BY h.node_id,
                      rt.promotion_id,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.country_id, --03/09/2017
                      rt.media_date,
                      rt.media_type,
                      h.parent_node_id,
                      h.hier_level,
                      h.is_leaf
            ) -- end detail_derived_summary
            SELECT dds.*
              FROM detail_derived_summary dds
          ) s
      ON ( d.detail_node_id = s.detail_node_id AND
           d.record_type    = s.sum_type       AND
           d.promotion_id   = s.promotion_id   AND
           d.pax_status     = s.pax_status     AND
           d.job_position   = s.job_position   AND
           d.department     = s.department     AND
           d.country_id     = s.country_id     AND  --03/09/2017
           d.media_date     = s.media_date     AND
           NVL(d.header_node_id, 0) = NVL(s.header_node_id, 0) AND
           d.hier_level     = s.hier_level     AND
           NVL(d.is_leaf, 0) = NVL(s.is_leaf, 0) AND
           NVL(d.media_type,'') = NVL(s.media_type,'') -- 04/10/2016  Bug 69353
          )
    WHEN MATCHED THEN
      UPDATE SET
         --d.media_type         = s.media_type,        -- 04/10/2016  Bug 69353
         d.total_media_amount = s.total_media_amount,         
         d.sweepstakes_won    = s.sweepstakes_won,
         d.badges_earned      = s.badges_earned,
         d.plateau_earned     = s.plateau_earned,
         d.modified_by        = c_created_by,
         d.date_modified      = SYSDATE
       WHERE ( -- only update records with different values
               -- DECODE(d.media_type,         s.media_type,         1, 0) = 0 OR -- 04/10/2016  Bug 69353
                DECODE(d.header_node_id,     s.header_node_id,     1, 0) = 0
             OR DECODE(d.hier_level,         s.hier_level,         1, 0) = 0
             OR DECODE(d.is_leaf,            s.is_leaf,            1, 0) = 0
             OR DECODE(d.total_media_amount, s.total_media_amount, 1, 0) = 0             
             OR DECODE(d.sweepstakes_won,    s.sweepstakes_won,    1, 0) = 0
             OR DECODE(d.badges_earned,      s.badges_earned,      1, 0) = 0
             OR DECODE(d.plateau_earned,     s.plateau_earned,     1, 0) = 0
             )
    WHEN NOT MATCHED THEN
      INSERT
      (  rpt_awardmedia_summary_id,
         -- key fields
         detail_node_id,
         record_type,
         promotion_id,
         pax_status,
         job_position,
         department,
         country_id,  --03/09/2017
         media_date,
         -- reference fiel
         media_type,
         header_node_id,
         hier_level,
         is_leaf,
         -- count fields
         total_media_amount,
         -- audit fields
         created_by,
         date_created,
         modified_by,
         date_modified,
         -- count fields         
         sweepstakes_won,
         badges_earned,
         plateau_earned
      )
      VALUES
      (  rpt_awardmedia_summary_pk_sq.NEXTVAL,
         -- key fields
         s.detail_node_id,
         s.sum_type,  --s.record_type,
         s.promotion_id,
         s.pax_status,
         s.job_position,
         s.department,
         s.country_id,  --03/09/2017
         s.media_date,
         -- reference fiel
         s.media_type,
         s.header_node_id,
         s.hier_level,
         s.is_leaf,
         -- count fields
         s.total_media_amount,
         -- audit fields
         c_created_by,
         SYSDATE,
         NULL,
         NULL,
         -- count fields        
         s.sweepstakes_won,
         s.badges_earned,
         s.plateau_earned
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

/*05/12/2017 commented
   ------------
   -- add missing default summary permutations
   v_stage := 'INSERT missing default team summary permutations';
   INSERT INTO rpt_awardmedia_summary
   (  rpt_awardmedia_summary_id,
      -- key fields
      detail_node_id,
      record_type,
      promotion_id,
      pax_status,
      job_position,
      department,
      country_id,  --03/09/2017
      media_date,
      -- reference fiel
      media_type,
      header_node_id,
      hier_level,
      is_leaf,
      -- count fields
      total_media_amount,
      -- audit fields
      created_by,
      date_created,
      modified_by,
      date_modified,
      -- count fields      
      sweepstakes_won,
      badges_earned,
      plateau_earned
   )
   (  -- find missing default permutations
      SELECT rpt_awardmedia_summary_pk_sq.NEXTVAL,
             -- key fields
             nsp.detail_node_id,
             nsp.record_type,
             0    AS promotion_id,
             ' '  AS pax_status,
             ' '  AS job_position,
             ' '  AS department,
             0 AS country_id, --03/09/2017
             NULL AS media_date,
             -- reference fiel
             ' '  AS media_type,
             nsp.header_node_id,
             nsp.hier_level,
             nsp.is_leaf,
             -- count fields
             0 AS total_media_amount,
             -- audit fields
             c_created_by AS created_by,
             SYSDATE      AS date_created,
             NULL         AS modified_by,
             NULL         AS date_modified,
             -- count fields             
             0 AS sweepstakes_won,
             0 AS badges_earned,
             0 AS plateau_earned
        FROM ( -- get all possible node summary type permutations
               SELECT h.node_id AS detail_node_id,
                      h.hier_level || '-' || 'teamsum' AS record_type,
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      1 AS is_leaf   -- team summary always a leaf node
                 FROM rpt_hierarchy h
             ) nsp
          -- exclude default permutation when a matching summary record exists
        WHERE NOT EXISTS
             ( SELECT 1
                 FROM rpt_awardmedia_summary s
                WHERE s.detail_node_id = nsp.detail_node_id
                  AND s.record_type    = nsp.record_type
                   -- any media date value matches on team summary records because detail derived summaries replace default team permutations
             )
   );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   v_stage := 'INSERT missing default node summary permutations';
   INSERT INTO rpt_awardmedia_summary
   (  rpt_awardmedia_summary_id,
      -- key fields
      detail_node_id,
      record_type,
      promotion_id,
      pax_status,
      job_position,
      department,
      country_id, --03/09/2017
      media_date,
      -- reference fiel
      media_type,
      header_node_id,
      hier_level,
      is_leaf,
      -- count fields
      total_media_amount,
      -- audit fields
      created_by,
      date_created,
      modified_by,
      date_modified,
      -- count fields     
      sweepstakes_won,
      badges_earned,
      plateau_earned
   )
   (  -- find missing default permutations
      SELECT rpt_awardmedia_summary_pk_sq.NEXTVAL,
             -- key fields
             nsp.detail_node_id,
             nsp.record_type,
             0    AS promotion_id,
             ' '  AS pax_status,
             ' '  AS job_position,
             ' '  AS department,
             0 AS country_id, --03/09/2017
             NULL AS media_date,
             -- reference fiel
             ' '  AS media_type,
             nsp.header_node_id,
             nsp.hier_level,
             nsp.is_leaf,
             -- count fields
             0 AS total_media_amount,
             -- audit fields
             c_created_by AS created_by,
             SYSDATE      AS date_created,
             NULL         AS modified_by,
             NULL         AS date_modified,
             -- count fields             
             0 AS sweepstakes_won,
             0 AS badges_earned,
             0 AS plateau_earned
        FROM ( -- get all possible node summary type permutations
               SELECT h.node_id AS detail_node_id,
                      h.hier_level || '-' || 'nodesum' AS record_type,
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      h.is_leaf
                 FROM rpt_hierarchy h
             ) nsp
          -- exclude default permutation when a matching summary record exists
        WHERE NOT EXISTS
             ( SELECT 1
                 FROM rpt_awardmedia_summary s
                WHERE s.detail_node_id = nsp.detail_node_id
                  AND s.record_type    = nsp.record_type
                  AND s.media_date IS NULL
             )
          -- default node summary permutation must have default team summary permutation in its hierarchy
         AND nsp.detail_node_id IN --06/18/2014
             ( -- get team permutations under node permutation
               SELECT tp.detail_node_id
                 FROM rpt_awardmedia_summary tp
                WHERE SUBSTR(tp.record_type, -7) = 'teamsum'
                  AND tp.media_date IS NULL
                UNION ALL
               -- get team permutations under node permutation hierarchy
               SELECT tp.header_node_id
                 FROM rpt_awardmedia_summary tp
                   -- start with child node immediately under current node
                START WITH SUBSTR(tp.record_type, -7) = 'teamsum'
                       AND tp.media_date IS NULL
                CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
                       AND PRIOR SUBSTR(tp.record_type, -7) = SUBSTR(tp.record_type, -7)
                       AND tp.media_date IS NULL
             )
   );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);*/ --05/12/2017 commented
   
      v_stage:= 'Delete Distinct Records';                                   --Bug# 65770 02/22/2016
   DELETE FROM                                                               --Bug# 65770 02/22/2016          
             RPT_AWARDMEDIA_SUMMARY WHERE RPT_AWARDMEDIA_SUMMARY_id IN(     --Bug# 65770 02/22/2016 
             SELECT r.RPT_AWARDMEDIA_SUMMARY_id                             --Bug# 65770 02/22/2016
             FROM RPT_AWARDMEDIA_SUMMARY r , node n,node d                  --Bug# 65770 02/22/2016
             WHERE n.node_id = detail_node_id and d.node_id = header_node_id and  --Bug# 65770 02/22/2016
             (header_node_id,detail_node_id) NOT IN                               --Bug# 65770 02/22/2016 
             (SELECT node_id,child_node_id FROM RPT_HIERARCHY_ROLLUP)             --Bug# 65770 02/22/2016
     );
     
     v_rec_cnt := SQL%ROWCOUNT;
     prc_execution_log_entry(c_process_name,c_release_level,'INFO',v_stage || '(' || v_rec_cnt || ' records processed)', NULL);
    
   ------------
   -- remove team permutations when an associated detail derived summary exists
   v_stage := 'DELETE obsolete team permutations';
   DELETE rpt_awardmedia_summary
    WHERE ROWID IN
          ( -- get default team permutation with a corresponding detail derived team summary
            SELECT DISTINCT
                   s.ROWID
              FROM rpt_awardmedia_summary s,
                   rpt_awardmedia_summary dd
                -- substr matches functional index
             WHERE SUBSTR(dd.record_type, -7) = 'teamsum'
                -- detail derived summaries have a media date
               AND dd.media_date IS NOT NULL
               AND dd.detail_node_id          = s.detail_node_id
               AND SUBSTR(dd.record_type, -7) = SUBSTR(s.record_type, -7)
                -- default permutations have no media date
               AND s.media_date IS NULL
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   -- delete node permutation with no associated team permutation
   v_stage := 'DELETE obsolete node permutations with no associated team permutation';
   DELETE rpt_awardmedia_summary
    WHERE ROWID IN
          ( SELECT np.ROWID
              FROM rpt_awardmedia_summary np
             WHERE SUBSTR(np.record_type, -7) = 'nodesum'
               AND np.media_date IS NULL
                -- node permutation has no team permutation
               AND NOT EXISTS
                   ( -- get team permutations under node permutation
                     SELECT 1
                       FROM rpt_awardmedia_summary tp
                      WHERE tp.detail_node_id          = np.detail_node_id
                        AND SUBSTR(tp.record_type, -7) = 'teamsum'
                        AND tp.media_date IS NULL
                      UNION ALL
                     -- get team permutations under node permutation hierarchy
                     SELECT 1
                       FROM rpt_awardmedia_summary tp
                         -- start with child node immediately under current node
                      START WITH tp.header_node_id          = np.detail_node_id
                             AND SUBSTR(tp.record_type, -7) = 'teamsum'
                             AND tp.media_date IS NULL
                      CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
                             AND PRIOR SUBSTR(tp.record_type, -7) = SUBSTR(tp.record_type, -7)
                             AND tp.media_date IS NULL
                   )
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  p_return_code := 0;  

EXCEPTION
  WHEN OTHERS THEN
    p_return_code := 99;
    p_error_message := SUBSTR(SQLERRM,1,250);
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage ||':'||v_parm_list|| ': ' || SQLCODE || ', ' || SQLERRM, NULL);
END p_rpt_awardmedia_summary;
  

PROCEDURE P_INSERT_RPT_AWARDMEDIA_DETAIL
  (p_awardmedia_dtl_rec IN rpt_awardmedia_detail%rowtype) IS
/*******************************************************************************

   Purpose:  Insert record into rpt_awardmedia_detail reporting table

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray    11/01/2005  Initial packaging of awardmedia report procedures
   D Murray    12/13/2005  Added department.
*******************************************************************************/
  
BEGIN

 INSERT INTO rpt_awardmedia_detail
     (rpt_awardmedia_detail_id, media_type, node_id, node_name,hierarchy_path,
      media_date, promotion_id, promotion_name, promotion_type, is_taxable,
      hierarchy_id, user_id, participant_name, pax_first_name,sender_first_name,sender_last_name,sender_user_id,
      pax_middle_name, pax_last_name, participant_current_status,
      user_name, position_type, department, media_amount,journal_id, date_created,
      created_by)
 VALUES (p_awardmedia_dtl_rec.rpt_awardmedia_detail_id,
         nvl(p_awardmedia_dtl_rec.media_type,' '),
         p_awardmedia_dtl_rec.node_id, p_awardmedia_dtl_rec.node_name,
         p_awardmedia_dtl_rec.hierarchy_path,
         trunc(p_awardmedia_dtl_rec.media_date),
         nvl(p_awardmedia_dtl_rec.promotion_id,0),
         p_awardmedia_dtl_rec.promotion_name,
         p_awardmedia_dtl_rec.promotion_type, p_awardmedia_dtl_rec.is_taxable,
         p_awardmedia_dtl_rec.hierarchy_id, p_awardmedia_dtl_rec.user_id,
         p_awardmedia_dtl_rec.participant_name, p_awardmedia_dtl_rec.pax_first_name,p_awardmedia_dtl_rec.sender_first_name,p_awardmedia_dtl_rec.sender_last_name,p_awardmedia_dtl_rec.sender_user_id,
         p_awardmedia_dtl_rec.pax_middle_name, p_awardmedia_dtl_rec.pax_last_name,
         nvl(p_awardmedia_dtl_rec.participant_current_status,' '),
         p_awardmedia_dtl_rec.user_name,
         nvl(p_awardmedia_dtl_rec.position_type,' '),
         nvl(p_awardmedia_dtl_rec.department,' '),
         p_awardmedia_dtl_rec.media_amount, p_awardmedia_dtl_rec.journal_id,
         p_awardmedia_dtl_rec.date_created, p_awardmedia_dtl_rec.created_by) ;
EXCEPTION
   WHEN others THEN
    prc_execution_log_entry('P_INSERT_RPT_AWARDMEDIA_DETAIL',1,'ERROR',SQLERRM,null);
END; -- P_INSERT_RPT_AWARDMEDIA_DETAIL
--------------------------------------------------------------------------------

PROCEDURE P_INSERT_RPT_AWARDMEDIASUMMARY
  (p_awardmedia_sum_rec IN rpt_awardmedia_summary%rowtype) IS
/*******************************************************************************

   Purpose:  Insert record into rpt_awardmedia_summary reporting table

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray    11/07/2005  Initial creation
   D Murray    12/13/2005  Added pax_status, job_position, department.
*******************************************************************************/
  
BEGIN

 INSERT INTO rpt_awardmedia_summary
     (record_type, header_node_id, detail_node_id,
      total_media_amount, media_date, promotion_id,
      pax_status, job_position, department,
      hier_level, rpt_awardmedia_summary_id,
      is_leaf, date_created, created_by, media_type)
 VALUES (p_awardmedia_sum_rec.record_type, p_awardmedia_sum_rec.header_node_id,
         p_awardmedia_sum_rec.detail_node_id, p_awardmedia_sum_rec.total_media_amount,
         trunc(p_awardmedia_sum_rec.media_date),
         NVL(p_awardmedia_sum_rec.promotion_id,0),
         NVL(p_awardmedia_sum_rec.pax_status,' '),
         NVL(p_awardmedia_sum_rec.job_position,' '),
         NVL(p_awardmedia_sum_rec.department,' '),
         p_awardmedia_sum_rec.hier_level, p_awardmedia_sum_rec.rpt_awardmedia_summary_id,
         p_awardmedia_sum_rec.is_leaf, p_awardmedia_sum_rec.date_created,
         p_awardmedia_sum_rec.created_by, nvl(p_awardmedia_sum_rec.media_type,' '));
EXCEPTION
   WHEN others THEN
    prc_execution_log_entry('P_INSERT_RPT_AWARDMEDIASUMMARY',1,'ERROR',SQLERRM,null);
END; -- P_INSERT_RPT_AWARDMEDIASUMMARY
--------------------------------------------------------------------------------

PROCEDURE p_rpt_awardmedia_detail_sa            --04/01/2019
  (p_user_id       IN NUMBER,
   p_start_date    IN DATE, 
   p_end_date      IN DATE, 
   p_return_code   OUT NUMBER,
   p_error_message OUT VARCHAR2)
IS
/************************************************************************************************************************
   Purpose:  Populates rpt_awardmedia_detail reporting table.  
             Note that this procedure excludes PURL/Celebration promotions whose award type is merchandise.
             This procedure will be called from the wrapper procedure only when *new.service.anniversary.enabled* is true  
   Date           Author       Comments
   -----------   ----------    -----------------------------------------------------
   04/01/2019     Suresh J     SA Integeration with DayMaker changes for reports
****************************************************************************************************************************/
  --Cursor to pick detail record
CURSOR cur_detail IS  
SELECT 
           CASE WHEN P.promotion_type = 'nomination' THEN j.award_type   
               ELSE
                    P.award_type
           END  AS media_type,                               
           unr.node_id,
           n.NAME                     AS node_name,
           TRUNC(j.transaction_date)  AS media_date,
           j.promotion_id,
           P.promotion_name,
           P.promotion_type,
           P.is_taxable,
           n.hierarchy_id,
           j.user_id,
           fnc_format_user_name(au.last_name, au.first_name, au.middle_name, au.suffix) AS participant_name,
           INITCAP(au.first_name)     AS pax_first_name,
           TRIM(au.middle_name)       AS pax_middle_name,
           INITCAP(au.last_name)      AS pax_last_name,
           DECODE(au.is_active, 1, 'active', 'inactive') AS participant_current_status,
           au.user_name,
           fnc_get_badge_count_by_user(au.user_id,P.promotion_type,TRUNC(j.transaction_date)) badges_earned,
           NVL(cpe.position_type, ' ') AS position_type,
           NVL(cpe.department_type, ' ') AS department,
           CASE WHEN P.promotion_type = 'nomination' AND j.award_type = 'cash' THEN j.transaction_cash_amt --10/05/2016
           ELSE j.transaction_amt
           END    AS media_amount,
           n.path                     AS hierarchy_path,
           j.journal_id,
          decode(j.journal_type,'Sweeps',1,0) Sweepstakes_won,
           au_su.last_name sender_last_name,
           au_su.first_name sender_first_name,
           au_su.user_id sender_user_id
           ,NVL(j.award_type,p.award_type) as award_payout_type  
           ,j.user_currency as user_currency   
           ,mo.asset_key_level_name             
      FROM journal j,
           promotion P,
           application_user au,
           application_user au_su,
           activity_journal aj,
           activity A,
           claim c,
           vw_curr_pax_employer cpe,
           ( -- rank user node records so result can be restricted to one record
             -- this select duplicates the PKG_REPORT_COMMON.P_USER_DETAILS process by randomly ranking the records for users in multiple nodes
             SELECT RANK() OVER (PARTITION BY un.user_id ORDER BY ROWNUM) AS un_rank,
                    un.user_id,
                    un.node_id
               FROM user_node un
           ) unr,
           node n
           ,(select mo.claim_id,
                   pm.promo_merch_program_level_id,
                   pm.cm_asset_key as asset_key_level_name,
                   pm.om_level_name
            from merch_order mo,
                 promo_merch_program_level pm
            where mo.promo_merch_program_level_id = pm.promo_merch_program_level_id) mo  
     WHERE j.promotion_id = P.promotion_id
           AND SUBSTR(j.transaction_description,0,33) <> 'CERTIFICATE CONVERSION for Cert: '
           AND NOT(P.promotion_type = 'product_claim' AND j.status_type != 'post') 
           AND j.user_id = au.user_id
           AND j.transaction_type<>'reverse' 
           AND j.status_type <> 'approve'
           AND j.journal_id = aj.journal_id(+) AND aj.activity_id= A.activity_id(+) 
           AND A.claim_id= c.claim_id(+)
           AND c.submitter_id = au_su.user_id(+)
           AND au.user_id = unr.user_id
           AND unr.un_rank = 1
           AND unr.node_id = n.node_id
           AND j.user_id = cpe.user_id (+)
           AND LOWER(P.promotion_status) IN ('live','expired') 
           AND NOT EXISTS (SELECT r.promotion_id                                    --04/01/2019
                                FROM promo_recognition r,
                                     promotion pp   
                                WHERE r.promotion_id = pp.promotion_id AND
                                      r.promotion_id = p.promotion_id  AND
                                      --pp.award_type = 'merchandise' AND 
                                      (r.is_include_purl = 1 OR r.include_celebrations = 1))            --Exclude PURL since SA replaces PURL
           AND P.is_deleted = 0                                       
           AND  NOT exists (select * from recognition_claim where claim_id=c.claim_id and is_reverse=1) 
           AND a.claim_id = mo.claim_id (+)                                          
           AND (p_start_date < j.date_created  AND j.date_created <= p_end_date
            OR  p_start_date < j.date_modified AND j.date_modified <= p_end_date)
UNION ALL  --Query for promotions with merchandise as award type
        SELECT P.award_type       AS media_type,
           a.node_id,
           n.NAME                     AS node_name,
           TRUNC(c.submission_date)  AS media_date,
           p.promotion_id,
           P.promotion_name,
           P.promotion_type,
           P.is_taxable,
           n.hierarchy_id,
           a.user_id,
           fnc_format_user_name(au.last_name, au.first_name, au.middle_name, au.suffix) AS participant_name,
           INITCAP(au.first_name)     AS pax_first_name,
           TRIM(au.middle_name)       AS pax_middle_name,
           INITCAP(au.last_name)      AS pax_last_name,
           DECODE(au.is_active, 1, 'active', 'inactive') AS participant_current_status,
           au.user_name,
           fnc_get_badge_count_by_user(au.user_id,P.promotion_type,TRUNC(c.submission_date)) badges_earned,
           NVL(cpe.position_type, ' ') AS position_type,
           NVL(cpe.department_type, ' ') AS department,
           0          AS media_amount,
           n.path                     AS hierarchy_path,
           NULL journal_id,
           0 Sweepstakes_won,
           au_su.last_name sender_last_name,
           au_su.first_name sender_first_name,
           au_su.user_id sender_user_id
           ,p.award_type  as award_payout_type 
           ,NULL  as user_currency   
           ,NULL  as asset_key_level_name  
  FROM    activity a, 
          claim c,
          promotion P,
          application_user au,
          application_user au_su,
          node n,
          vw_curr_pax_employer cpe
 WHERE
        c.claim_id = a.claim_id
        AND c.submitter_id = au_su.user_id
        AND a.is_submitter = 0
        AND a.user_id = au.user_id
        AND c.promotion_id = p.promotion_id
        AND NOT EXISTS (SELECT r.promotion_id                           --04/01/2019
                            FROM promo_recognition r,
                                 promotion pp   
                            WHERE r.promotion_id = pp.promotion_id AND
                                  r.promotion_id = p.promotion_id  AND
                                  --pp.award_type = 'merchandise' AND 
                                  (r.is_include_purl = 1 OR r.include_celebrations = 1))            --Exclude PURL since SA replaces PURL
        AND NOT EXISTS (SELECT * FROM activity_journal WHERE activity_id = a.activity_id)
        AND a.node_id = n.node_id
        AND P.award_type ='merchandise'
        AND a.user_id = cpe.user_id (+)
        AND (p_start_date < c.submission_date  AND c.submission_date <= p_end_date
        OR  p_start_date < c.date_modified AND c.date_modified <= p_end_date)
UNION ALL  --Query for Nomination Promotions
    SELECT pnl.award_payout_type  AS media_type,   --'other'
           a.node_id,
           n.NAME                     AS node_name,
           TRUNC(c.submission_date)  AS media_date,
           p.promotion_id,
           P.promotion_name,
           P.promotion_type,
           P.is_taxable,
           n.hierarchy_id,
           a.user_id,
           fnc_format_user_name(au.last_name, au.first_name, au.middle_name, au.suffix) AS participant_name,
           INITCAP(au.first_name)     AS pax_first_name,
           TRIM(au.middle_name)       AS pax_middle_name,
           INITCAP(au.last_name)      AS pax_last_name,
           DECODE(au.is_active, 1, 'active', 'inactive') AS participant_current_status,
           au.user_name,
           0 badges_earned,
           NVL(cpe.position_type, ' ') AS position_type,
           NVL(cpe.department_type, ' ') AS department,
           pnl.payout_value           AS media_amount,
           n.path                     AS hierarchy_path,
           NULL journal_id,
           0 Sweepstakes_won,
           au_su.last_name sender_last_name,
           au_su.first_name sender_first_name,
           au_su.user_id sender_user_id
           ,pnl.award_payout_type  as award_payout_type 
           ,pnl.payout_currency  as user_currency   
           ,pnl.payout_description_asset_code  as asset_key_level_name  
FROM claim_item_approver cia,
     claim_item ci,
     claim c,
     activity a,
     promo_nomination pn,
     promo_nomination_level pnl,
     promotion P,
     application_user au,
     application_user au_su,
     node n,
     vw_curr_pax_employer cpe
WHERE pnl.award_payout_type = 'other'  
     AND (cia.claim_item_id = ci.claim_item_id OR c.claim_group_id = cia.claim_group_id ) 
     AND ci.claim_id = c.claim_id 
     AND (c.claim_id = a.claim_id OR c.claim_group_id = a.claim_group_id )
     AND c.promotion_id = pn.promotion_id 
     AND ((pnl.level_index = cia.approval_round AND pn.payout_level_type = 'eachLevel') OR pn.payout_level_type = 'finalLevel' ) 
      AND cia.approval_status_type = 'winner' 
      AND cia.approval_round = a.approval_round 
      AND c.submitter_id = au_su.user_id
      AND a.is_submitter = 0
      AND a.user_id = au.user_id
      AND c.promotion_id = p.promotion_id
      AND NOT EXISTS (SELECT r.promotion_id                         --04/01/2019
                                FROM promo_recognition r,
                                     promotion pp   
                                WHERE r.promotion_id = pp.promotion_id AND
                                      r.promotion_id = p.promotion_id  AND
                                      --pp.award_type = 'merchandise' AND 
                                      (r.is_include_purl = 1 OR r.include_celebrations = 1))            --Exclude PURL since SA replaces PURL
      AND NOT EXISTS (SELECT aj.activity_id 
                        FROM activity_journal aj 
                        WHERE aj.activity_id = a.activity_id)
      AND a.node_id = n.node_id
      AND a.user_id = cpe.user_id (+)
      AND (p_start_date < cia.date_created  AND cia.date_created <= p_end_date
      OR  p_start_date < cia.date_modified AND cia.date_modified <= p_end_date);

  --Cursor to pick the users whose information is updated in
  --application_user,participant_employer table
  CURSOR cur_pax_info_changed IS
    SELECT au.user_id,
           INITCAP(au.last_name) last_name,
           INITCAP(au.first_name) first_name,
           au.middle_name,
           au.suffix,
           DECODE(au.is_active,1,'active','inactive') pax_status,
           vcpe.department_type,
           vcpe.position_type,
           rpt.participant_current_status  pax_status_old,
           rpt.position_type               job_position_old,
           rpt.department                  department_old,
           rpt.country_id           AS      country_id_old, 
           c.country_id,                                    
           au2.user_id sender_user_id,
           au2.last_name sender_last_name,
           au2.first_name sender_first_name,
           rpt.node_id,
           rpt.journal_id,
           rpt.promotion_id,
           rpt.media_date,
           rpt.media_amount,
           rpt.media_type,          
           rpt.sweepstakes_won, 
           rpt.badges_earned, 
           rpt.plateau_earned
      FROM application_user au,
           rpt_awardmedia_detail rpt,
           application_user au2,
           vw_curr_pax_employer vcpe,
           user_address ua,  
           country c         
     WHERE au.user_id   = rpt.user_id
       AND vcpe.user_id = rpt.user_id
       AND rpt.sender_user_id = au2.user_id(+)
       AND au.user_id = ua.user_id  (+)     
       AND ua.is_primary (+) = 1            
       AND ua.country_id = c.country_id (+) 
          AND ((p_start_date < au.date_created AND au.date_created <= p_end_date)
            OR (p_start_date < au.date_modified  AND  au.date_modified <= p_end_date)
            OR (p_start_date < au2.date_created AND au2.date_created <= p_end_date)
            OR (p_start_date < au2.date_modified  AND  au2.date_modified <= p_end_date)
            OR (p_start_date < ua.date_created AND ua.date_created <= p_end_date)       
            OR (p_start_date < ua.date_modified  AND  ua.date_modified <= p_end_date)   
            OR (p_start_date < vcpe.date_modified  AND  vcpe.date_modified <= p_end_date)
            OR (p_start_date < vcpe.date_created  AND  vcpe.date_created <= p_end_date))
       AND (au.last_name            <> rpt.pax_last_name
            OR au.first_name        <> rpt.pax_first_name
            OR au.middle_name       <> rpt.pax_middle_name
            OR DECODE(au.is_active,1,'active','inactive') <> rpt.participant_current_status
            OR vcpe.department_type <> rpt.department
            OR vcpe.position_type   <> rpt.position_type
             OR au2.last_name            <> rpt.sender_last_name
            OR au2.first_name        <> rpt.sender_first_name);
                        
         CURSOR cur_reversed IS 
         SELECT rpt.*
          FROM rpt_awardmedia_detail rpt
        WHERE EXISTS (SELECT * 
                        FROM journal j 
                       WHERE j.is_reverse = 1 
                         AND j.journal_id = rpt.journal_id); 
       
  --Cursor hierarchy
  CURSOR cur_hier (p_in_node_id rpt_hierarchy.node_id%TYPE) IS
    SELECT parent_node_id header_node_id,
           node_id,
           hier_level,
           is_leaf
      FROM rpt_hierarchy
     START WITH node_id = p_in_node_id
   CONNECT BY PRIOR parent_node_id = node_id;       

  --Cursor to pick modified node name
  CURSOR cur_node_changed IS
    SELECT node_id,
           NAME
      FROM node
     WHERE date_modified BETWEEN p_start_date AND p_end_date;  

  --Constants
  c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_awardmedia_detail_sa');
  c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;

  --Procedure variables
  v_stage                   execution_log.text_line%TYPE;
  v_rec_cnt                 INTEGER;
  v_rpt_awardmedia_dtl_id   rpt_awardmedia_detail.rpt_awardmedia_detail_id%TYPE;
  v_ins_detail              VARCHAR2(1);
  v_claim_id                activity.claim_id%TYPE;
  v_sweepstakes_won         PLS_INTEGER := 0;
  v_plateau_earned          PLS_INTEGER := 0;
  v_media_amount_old        PLS_INTEGER := 0;  
  v_sweepstakes_won_old     PLS_INTEGER := 0;
  v_badges_earned_old       PLS_INTEGER := 0;
  v_plateau_earned_old      PLS_INTEGER := 0;
  v_country_id              rpt_awardmedia_detail.country_id%TYPE;
  v_tab_node_id             dbms_sql.number_table;
  v_tab_user_id              dbms_sql.number_table;
  v_tab_node_name           dbms_sql.varchar2_table;
  v_header_node_id          rpt_hierarchy.parent_node_id%TYPE;
  v_node_id                 rpt_hierarchy.node_id%TYPE;
  v_hier_level              rpt_hierarchy.hier_level%TYPE;
  v_is_leaf                 rpt_hierarchy.is_leaf%TYPE;
  v_summ_id                 rpt_awardmedia_summary.rpt_awardmedia_summary_id%TYPE;
  v_media_amount            rpt_awardmedia_summary.total_media_amount%TYPE;
  
BEGIN

  v_stage := 'Start';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
  
  FOR rec_detail IN cur_detail LOOP

    v_ins_detail          := 'N';
    v_media_amount_old    := 0;   
    v_sweepstakes_won_old := 0;
    v_sweepstakes_won     := 0;  
    v_badges_earned_old   := 0;
    v_plateau_earned_old  := 0;
                 
    BEGIN
      v_stage := 'Find claim id';
      SELECT act.claim_id
        INTO v_claim_id
        FROM journal j,
             activity_journal aj,
             activity act
       WHERE j.journal_id   = aj.journal_id
         AND aj.activity_id = act.activity_id
         AND j.journal_id   = rec_detail.journal_id
         AND ROWNUM < 2;          

     v_sweepstakes_won := rec_detail.sweepstakes_won;

    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_claim_id := NULL;
    END;
      
    v_stage := 'Set plateau earned';
    IF rec_detail.media_type = 'merchandise' THEN
      v_plateau_earned := 1;
    ELSE
      v_plateau_earned := 0;  
    END IF;

    BEGIN
      v_stage := 'Find detail record';
      SELECT rpt_awardmedia_detail_id,
             media_amount,           
             sweepstakes_won,
             plateau_earned
        INTO v_rpt_awardmedia_dtl_id,
             v_media_amount_old,            
             v_sweepstakes_won_old,
             v_plateau_earned_old
        FROM rpt_awardmedia_detail
       WHERE journal_id = rec_detail.journal_id;  
    
      --update detail table only when changed
      IF rec_detail.media_amount <> v_media_amount_old OR        
         v_sweepstakes_won       <> v_sweepstakes_won_old THEN
        
        v_stage := 'Update rpt_awardmedia_detail';
        UPDATE rpt_awardmedia_detail
           SET media_amount    = rec_detail.media_amount,               
               sweepstakes_won = v_sweepstakes_won,
               modified_by     = p_user_id,
               date_modified   = SYSDATE
         WHERE rpt_awardmedia_detail_id = v_rpt_awardmedia_dtl_id;
          
      END IF;
      
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_ins_detail := 'Y';    
    END;

    IF v_ins_detail = 'Y' THEN

      BEGIN
        v_stage := 'Find country id for the user '||rec_detail.user_id;
        SELECT country_id
          INTO v_country_id
          FROM user_address
         WHERE user_id    = rec_detail.user_id
           AND is_primary = 1;
      EXCEPTION
        WHEN OTHERS THEN
          v_country_id := NULL;         
      END;

      v_stage := 'INSERT rpt_awardmedia_detail';
      INSERT INTO rpt_awardmedia_detail
                  (rpt_awardmedia_detail_id,
                   media_type,
                   node_id,
                   node_name,
                   media_date,
                   promotion_id,
                   promotion_name,
                   promotion_type,
                   is_taxable,
                   hierarchy_id,
                   user_id,
                   participant_name,
                   pax_first_name,
                   pax_middle_name,
                   pax_last_name,
                   participant_current_status,
                   user_name,
                   position_type,
                   department,
                   media_amount,
                   date_created,
                   created_by,
                   hierarchy_path,
                   journal_id,
                   country_id,
                   points,                   
                   sweepstakes_won,
                   badges_earned,
                   VERSION,
                   plateau_earned,
                   sender_first_name,
                   sender_last_name,
                   sender_user_id
                   ,award_payout_type       
                   ,user_currency           
                   ,asset_key_level_name    
                   )
           VALUES (rpt_awardmedia_detail_pk_sq.NEXTVAL,
                   rec_detail.media_type,
                   rec_detail.node_id,
                   rec_detail.node_name,
                   rec_detail.media_date,
                   rec_detail.promotion_id,
                   rec_detail.promotion_name,
                   rec_detail.promotion_type,
                   rec_detail.is_taxable,
                   rec_detail.hierarchy_id,
                   rec_detail.user_id,
                   rec_detail.participant_name,
                   rec_detail.pax_first_name,
                   rec_detail.pax_middle_name,
                   rec_detail.pax_last_name,
                   rec_detail.participant_current_status,
                   rec_detail.user_name,
                   rec_detail.position_type,
                   rec_detail.department,
                   rec_detail.media_amount,
                   SYSDATE,
                   p_user_id,
                   rec_detail.hierarchy_path,
                   rec_detail.journal_id,
                   v_country_id,
                   rec_detail.media_amount,                   
                   v_sweepstakes_won,
                   rec_detail.badges_earned,
                   1,
                   v_plateau_earned,
                   rec_detail.sender_first_name,
                   rec_detail.sender_last_name,
                   rec_detail.sender_user_id
                   ,rec_detail.award_payout_type    
                   ,rec_detail.user_currency        
                   ,rec_detail.asset_key_level_name 
                   );
     
    END IF;  
   
  END LOOP;

  FOR rec_pax_info_changed IN cur_pax_info_changed LOOP
 
    --when pax info changed make adjustment to summary table
    --when pax info updated in detail table, we may not aware what info got changed to what
    --in summary process so doing summary table adjustment here itself  
    IF rec_pax_info_changed.pax_status      <> rec_pax_info_changed.pax_status_old OR
       rec_pax_info_changed.department_type <> rec_pax_info_changed.department_old OR
       rec_pax_info_changed.position_type   <> rec_pax_info_changed.job_position_old THEN

      v_stage := 'Get hier_level for node_id: '||rec_pax_info_changed.node_id;
      BEGIN
        SELECT parent_node_id header_node_id,
               node_id,
               hier_level,
               is_leaf
          INTO v_header_node_id,
               v_node_id,
               v_hier_level,
               v_is_leaf
          FROM rpt_hierarchy
         WHERE node_id = rec_pax_info_changed.node_id;
      END;

      v_stage := 'Check summary record for detail node_id: '||rec_pax_info_changed.node_id;
      BEGIN

        SELECT rpt_awardmedia_summary_id,
               total_media_amount
          INTO v_summ_id,
               v_media_amount     
          FROM rpt_awardmedia_summary
         WHERE record_type      = v_hier_level||'-teamsum'
           AND NVL(header_node_id, 0) = NVL(v_header_node_id, 0)
           AND detail_node_id   = rec_pax_info_changed.node_id
           AND promotion_id     = rec_pax_info_changed.promotion_id
           AND pax_status       = rec_pax_info_changed.pax_status_old
           AND job_position     = rec_pax_info_changed.job_position_old
           AND department       = rec_pax_info_changed.department_old
           AND media_date       = rec_pax_info_changed.media_date
           AND media_type       = rec_pax_info_changed.media_type       
           AND country_id       = rec_pax_info_changed.country_id_old   
           AND hier_level       = v_hier_level
           AND is_leaf          = 1;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_media_amount := 0;
      END;
       
        --media amount not matches so minus the amount for pax info changed records and
        --create new summary record to new info 
        v_stage := 'Minus summary teamsum record for detail node_id: '||rec_pax_info_changed.node_id;
        UPDATE rpt_awardmedia_summary
           SET total_media_amount = total_media_amount - rec_pax_info_changed.media_amount,
               date_modified      = SYSDATE,
               modified_by        = p_user_id
         WHERE rpt_awardmedia_summary_id = v_summ_id;
        
        v_stage := 'Add summary teamsum record for detail node_id: '||rec_pax_info_changed.node_id;
        UPDATE rpt_awardmedia_summary   --Added 09/04/2013 
           SET total_media_amount = total_media_amount + rec_pax_info_changed.media_amount,
               date_modified      = SYSDATE,
               modified_by        = p_user_id
         WHERE record_type      = v_hier_level||'-teamsum'
           AND NVL(header_node_id, 0) = NVL(v_header_node_id, 0)
           AND detail_node_id   = rec_pax_info_changed.node_id
           AND promotion_id     = rec_pax_info_changed.promotion_id
           AND pax_status       = rec_pax_info_changed.pax_status
           AND job_position     = rec_pax_info_changed.position_type
           AND department       = rec_pax_info_changed.department_type
           AND media_date       = rec_pax_info_changed.media_date
           AND media_type       = rec_pax_info_changed.media_type 
           AND country_id       = rec_pax_info_changed.country_id 
           AND hier_level       = v_hier_level
           AND is_leaf          = 1;
        
        IF SQL%ROWCOUNT = 0 THEN   --Added If 09/04/2013 
          v_stage := 'Insert rpt_awardmedia_summary for teamsum records'||rec_pax_info_changed.node_id||','||rec_pax_info_changed.media_date||','||rec_pax_info_changed.position_type||','||
                     rec_pax_info_changed.department_type||','||rec_pax_info_changed.promotion_id;
          INSERT INTO rpt_awardmedia_summary 
                    (rpt_awardmedia_summary_id, detail_node_id, record_type, 
                     promotion_id, pax_status, job_position, country_id, --05/18/2017
                     department, media_date, media_type, 
                     header_node_id, hier_level, is_leaf, 
                     total_media_amount, created_by, date_created, 
                      sweepstakes_won, badges_earned, 
                     plateau_earned) 
             VALUES (rpt_awardmedia_summary_pk_sq.NEXTVAL, rec_pax_info_changed.node_id, v_hier_level||'-teamsum',
                     rec_pax_info_changed.promotion_id, rec_pax_info_changed.pax_status, rec_pax_info_changed.position_type, rec_pax_info_changed.country_id, --05/18/2017
                     rec_pax_info_changed.department_type, rec_pax_info_changed.media_date, rec_pax_info_changed.media_type,
                     v_header_node_id, v_hier_level, 1, --v_is_leaf, --09/24/2013 Replaced with 1
                     rec_pax_info_changed.media_amount, p_user_id, SYSDATE,
                     rec_pax_info_changed.sweepstakes_won, rec_pax_info_changed.badges_earned, 
                     rec_pax_info_changed.plateau_earned);
        END IF;
        
        --FOR rec_hier IN cur_hier (rec_pax_info_changed.node_id) LOOP  --Commented 09/04/2013 

          v_stage := 'Minus summary nodesum record for node_id: '||rec_pax_info_changed.node_id;
          UPDATE rpt_awardmedia_summary
             SET total_media_amount = total_media_amount - rec_pax_info_changed.media_amount,
                 date_modified      = SYSDATE,
                 modified_by        = p_user_id
           WHERE record_type      = v_hier_level||'-nodesum' -- rec_hier.hier_level||'-nodesum'  --Commented 09/04/2013 
             AND NVL(header_node_id, 0) = NVL(v_header_node_id,0) --NVL(rec_hier.header_node_id, 0) --Commented 09/04/2013 
             AND detail_node_id   = rec_pax_info_changed.node_id --rec_hier.node_id   --Commented 09/04/2013 
             AND promotion_id     = rec_pax_info_changed.promotion_id
             AND pax_status       = rec_pax_info_changed.pax_status_old
             AND job_position     = rec_pax_info_changed.job_position_old
             AND department       = rec_pax_info_changed.department_old
             AND media_date       = rec_pax_info_changed.media_date
             AND media_type       = rec_pax_info_changed.media_type --05/12/2017
             AND country_id       = rec_pax_info_changed.country_id_old --05/18/2017
             AND hier_level       = v_hier_level --rec_hier.hier_level  --Commented 09/04/2013 
             AND NVL(is_leaf,0)   = v_is_leaf; --NVL(rec_hier.is_leaf,0);  --Commented 09/04/2013 
          
           v_stage := 'Add summary nodesum record for node_id: '||rec_pax_info_changed.node_id;
          UPDATE rpt_awardmedia_summary   --Added 09/04/2013 
             SET total_media_amount = total_media_amount + rec_pax_info_changed.media_amount,
                 date_modified      = SYSDATE,
                 modified_by        = p_user_id
           WHERE record_type      = v_hier_level||'-nodesum' 
             AND NVL(header_node_id, 0) = NVL(v_header_node_id,0) 
             AND detail_node_id   = rec_pax_info_changed.node_id 
             AND promotion_id     = rec_pax_info_changed.promotion_id
             AND pax_status       = rec_pax_info_changed.pax_status
             AND job_position     = rec_pax_info_changed.position_type
             AND department       = rec_pax_info_changed.department_type
             AND media_date       = rec_pax_info_changed.media_date
             AND media_type       = rec_pax_info_changed.media_type --05/12/2017
             AND country_id       = rec_pax_info_changed.country_id --05/18/2017
             AND hier_level       = v_hier_level 
             AND NVL(is_leaf,0)   = v_is_leaf; 
             
          IF SQL%ROWCOUNT = 0 THEN     --Added If 09/04/2013 
            v_stage := 'Insert rpt_awardmedia_summary for nodesum records'||rec_pax_info_changed.node_id||','||rec_pax_info_changed.media_date||','||rec_pax_info_changed.position_type||','||
                     rec_pax_info_changed.department_type||','||rec_pax_info_changed.promotion_id;
            INSERT INTO rpt_awardmedia_summary 
                      (rpt_awardmedia_summary_id, detail_node_id, record_type, 
                       promotion_id, pax_status, job_position,country_id, --05/18/2017 
                       department, media_date, media_type, 
                       header_node_id, hier_level, is_leaf, 
                       total_media_amount, created_by, date_created, 
                        sweepstakes_won, badges_earned, 
                       plateau_earned) 
               VALUES (rpt_awardmedia_summary_pk_sq.NEXTVAL, rec_pax_info_changed.node_id, v_hier_level||'-nodesum', --rec_hier.node_id, rec_hier.hier_level||'-nodesum', --Commented 09/04/2013 
                       rec_pax_info_changed.promotion_id, rec_pax_info_changed.pax_status, rec_pax_info_changed.position_type, rec_pax_info_changed.country_id, --05/18/2017
                       rec_pax_info_changed.department_type, rec_pax_info_changed.media_date, rec_pax_info_changed.media_type,
                       v_header_node_id,v_hier_level,v_is_leaf,-- rec_hier.header_node_id, rec_hier.hier_level, rec_hier.is_leaf,  --Commented 09/04/2013 
                       rec_pax_info_changed.media_amount, p_user_id, SYSDATE,
                       rec_pax_info_changed.sweepstakes_won, rec_pax_info_changed.badges_earned, 
                       rec_pax_info_changed.plateau_earned);
                       
          END IF;
        --END LOOP;  --Commented 09/04/2013 
          
      --END IF;  --Commented 09/04/2013 
       
    END IF;    

    --Update rpt_awardmedia_detail
    --Summary table adjustment already done, so don't set date modified else 
    v_stage := 'Update rpt_awardmedia_detail';
    UPDATE rpt_awardmedia_detail
       SET participant_name = fnc_format_user_name(rec_pax_info_changed.last_name, 
                                                   rec_pax_info_changed.first_name, 
                                                   rec_pax_info_changed.middle_name, 
                                                   rec_pax_info_changed.suffix),     
           pax_first_name   = rec_pax_info_changed.first_name,
           pax_middle_name  = rec_pax_info_changed.middle_name,
           pax_last_name    = rec_pax_info_changed.last_name,
           sender_first_name   = rec_pax_info_changed.sender_first_name,
           sender_last_name  = rec_pax_info_changed.sender_last_name,          
           participant_current_status = rec_pax_info_changed.pax_status,
           country_id       = rec_pax_info_changed.country_id, --05/18/2017
           position_type    = rec_pax_info_changed.position_type,
           department       = rec_pax_info_changed.department_type,
           date_modified    = SYSDATE,
           modified_by   = p_user_id,
           VERSION          = VERSION + 1
     WHERE user_id    = rec_pax_info_changed.user_id
       AND journal_id = rec_pax_info_changed.journal_id;    
             
  END LOOP;
  
  v_stage := 'Open and cursor to process reversed journals'; --02/24/2014
FOR rec_reversed IN cur_reversed LOOP

v_stage := 'Deduct Summary record for reversed journals';

UPDATE rpt_awardmedia_summary
SET total_media_amount = total_media_amount-rec_reversed.media_amount
WHERE
   detail_node_id = rec_reversed.node_id
   AND promotion_id     = rec_reversed.promotion_id
             AND pax_status       = rec_reversed.participant_current_status
             AND job_position     = rec_reversed.position_type
             AND department       = rec_reversed.department
             AND media_date       = rec_reversed.media_date
             AND media_type       = rec_reversed.media_type --05/18/2017
             AND country_id       = rec_reversed.country_id; --05/18/2017
             
             
v_stage := 'Remove detail record for reversed journals';
DELETE FROM rpt_awardmedia_detail WHERE rpt_awardmedia_detail_id = rec_reversed.rpt_awardmedia_detail_id;

END LOOP;  

  v_stage := 'Open and Fetch cursor to pick modified node name';
  OPEN cur_node_changed;
  FETCH cur_node_changed BULK COLLECT
   INTO v_tab_node_id,
        v_tab_node_name;
  CLOSE cur_node_changed;   

  --Summary table won't have node name so don't set date modified else 
  --summary process will look date modified records for processing
  v_stage := 'Update modified node name for giver and receiver in rpt table';
  FORALL indx IN v_tab_node_id.FIRST .. v_tab_node_id.LAST
    UPDATE rpt_awardmedia_detail
       SET node_name     = DECODE (node_id, v_tab_node_id(indx), v_tab_node_name(indx), node_name),
           date_modified = SYSDATE,
           modified_by   = p_user_id,
           VERSION       = VERSION + 1
     WHERE (node_id        = v_tab_node_id(indx)
            AND node_name != v_tab_node_name(indx)
            );

  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  p_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    p_return_code := 99;
    p_error_message := SUBSTR(SQLERRM,1,250);
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ': ' || SQLCODE || ', ' || SQLERRM, NULL);
END p_rpt_awardmedia_detail_sa;

END;  -- PKG_REPORT_AWARDMEDIA
/
