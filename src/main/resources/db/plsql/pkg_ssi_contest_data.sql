CREATE OR REPLACE PACKAGE pkg_ssi_contest_data
IS
   /***********************************************************************************
      Purpose:  Package to hold all the procedures created for SSI contest module data.

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
     Ravi Dhanekula   1/26/2015     Initial Version
   ************************************************************************************/

   PROCEDURE prc_ssi_contest_progress (p_in_ssi_contest_id     IN     NUMBER,
      p_in_user_id IN NUMBER,    --02/04/2015
      p_out_contest_type  OUT NUMBER,
      p_out_return_code    OUT NUMBER,
      p_out_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_level_ref_cursor OUT SYS_REFCURSOR,
      p_out_sr_ref_cursor OUT SYS_REFCURSOR,
      p_out_sr_payout_ref_cursor OUT SYS_REFCURSOR,
      p_out_stackrank_cursor           OUT SYS_REFCURSOR,
      p_out_sr_pax_ref_cursor OUT SYS_REFCURSOR);

   PROCEDURE prc_ssi_contest_pax_progress (p_in_ssi_contest_id     IN     NUMBER,
      p_in_user_id             IN NUMBER,
      p_out_contest_type  OUT NUMBER,
      p_out_return_code    OUT NUMBER,
      p_out_obj_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_ref_cursor OUT SYS_REFCURSOR,
      p_out_DTGT_cursor OUT SYS_REFCURSOR,
      p_out_sr_ref_cursor OUT SYS_REFCURSOR,
      p_out_sr_payout_ref_cursor OUT SYS_REFCURSOR,
      p_out_sr_pax_ref_cursor OUT SYS_REFCURSOR);

      PROCEDURE prc_ssi_contest_pax_list (p_in_ssi_contest_id     IN     NUMBER,
      p_in_sortColName     IN     VARCHAR2,
      p_in_sortedBy           IN     VARCHAR2,
      p_in_rowNumStart               IN NUMBER,
      p_in_rowNumEnd                 IN NUMBER,
      p_in_user_id             IN NUMBER,    --02/04/2015
      p_out_contest_type  OUT NUMBER,
      p_out_return_code    OUT NUMBER,
      p_out_size_data          OUT    NUMBER,
      p_out_obj_ref_cursor OUT SYS_REFCURSOR,
      p_out_obj_total_ref_cursor OUT SYS_REFCURSOR,
      p_out_dtgt_ref_cursor OUT SYS_REFCURSOR,
      p_out_total_dtgt_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_total_ref_cursor OUT SYS_REFCURSOR);

       PROCEDURE prc_ssi_atn_issuance_sum (p_in_ssi_contest_id     IN     NUMBER,
      p_in_sortColName     IN     VARCHAR2,
      p_in_sortedBy           IN     VARCHAR2,
      p_in_rowNumStart               IN NUMBER,
      p_in_rowNumEnd                 IN NUMBER,
      p_out_issuance_count  OUT NUMBER,
      p_out_return_code    OUT NUMBER,
      p_out_ref_cursor OUT SYS_REFCURSOR
      );

      PROCEDURE prc_ssi_contest_export (p_in_ssi_contest_id     IN     NUMBER,
      p_in_user_id             IN NUMBER,    --02/04/2015
      p_in_locale           IN VARCHAR2,
      p_out_return_code    OUT NUMBER,
      p_out_ref_cursor OUT SYS_REFCURSOR);

      PROCEDURE prc_ssi_contest_stackrank_list (p_in_ssi_contest_id     IN     NUMBER,
      p_in_user_id             IN NUMBER,
      is_team                    IN NUMBER,
      p_in_include_all           IN NUMBER,
      p_in_contest_activity_id IN NUMBER,
      p_in_rowNumStart               IN NUMBER,
      p_in_rowNumEnd                 IN NUMBER,
      p_out_return_code    OUT NUMBER,
      p_out_pax_count      OUT NUMBER,
      p_out_ref_cursor OUT SYS_REFCURSOR);

      PROCEDURE prc_ssi_contest_approvals_list (p_in_ssi_contest_id     IN     NUMBER,
      p_in_contest_activity_id IN NUMBER,
      p_in_sortColName     IN     VARCHAR2,
      p_in_sortedBy           IN     VARCHAR2,
      p_in_rowNumStart               IN NUMBER,
      p_in_rowNumEnd                 IN NUMBER,
      p_out_contest_type  OUT NUMBER,
      p_out_pax_count      OUT NUMBER,
      p_out_return_code    OUT NUMBER,
      p_out_obj_ref_cursor OUT SYS_REFCURSOR,
      p_out_obj_total_ref_cursor OUT SYS_REFCURSOR,
      p_out_dtgt_ref_cursor OUT SYS_REFCURSOR,
      p_out_total_dtgt_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_total_ref_cursor OUT SYS_REFCURSOR,
      p_out_sr_ref_cursor OUT SYS_REFCURSOR,
      p_out_sr_total_ref_cursor OUT SYS_REFCURSOR);

      PROCEDURE prc_ssi_contest_creator_tile (p_in_ssi_contest_id     IN     NUMBER,
      p_out_contest_type  OUT NUMBER,
      p_out_return_code    OUT NUMBER,
      p_out_obj_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_ref_cursor OUT SYS_REFCURSOR,
      p_out_sr_ref_cursor OUT SYS_REFCURSOR);

      PROCEDURE prc_ssi_contest_app_export (p_in_ssi_contest_id     IN     NUMBER,
      p_in_locale           IN VARCHAR2,
      p_out_return_code    OUT NUMBER,
      p_out_ref_cursor OUT SYS_REFCURSOR);

      PROCEDURE prc_ssi_contest_claims_export ( 
      p_in_ssi_contest_id       IN     NUMBER,
      p_in_locale               IN VARCHAR2,      
      p_out_return_code         OUT NUMBER,      
      p_out_ref_cursor          OUT SYS_REFCURSOR);
      
      PROCEDURE ssi_contest_claims_sort
       (p_in_ssi_contest_id     IN     NUMBER,
        p_in_locale                 IN   VARCHAR2,
        p_in_status                 IN VARCHAR2,
         p_in_sortColName     IN     VARCHAR2,
         p_in_sortedBy           IN     VARCHAR2,
         p_in_rowNumStart               IN NUMBER,
         p_in_rowNumEnd                 IN NUMBER,
         p_out_return_code    OUT NUMBER,  
         p_out_claims_count   OUT NUMBER,
         p_out_claims_submitted_count OUT NUMBER,
         p_out_claims_pending_count OUT NUMBER,
         p_out_claims_approved_count OUT NUMBER,
         p_out_claims_denied_count OUT NUMBER,         
         p_out_ref_cursor OUT SYS_REFCURSOR);
       
       PROCEDURE prc_ssi_contest_pax_payout
       (p_in_ssi_contest_id     IN     NUMBER,
        p_in_csv_user_ids           IN VARCHAR2,
        p_in_csv_payout_amounts     IN VARCHAR2,
        p_in_csv_payout_desc     IN VARCHAR2,        
        p_in_sortColName     IN     VARCHAR2,
         p_in_sortedBy           IN     VARCHAR2,
         p_in_rowNumStart               IN NUMBER,
         p_in_rowNumEnd                 IN NUMBER,
         p_out_return_code    OUT NUMBER,  
         p_out_pax_count      OUT NUMBER,
         p_out_ref_cursor OUT SYS_REFCURSOR);
       

END;                                                           -- Package spec
/
CREATE OR REPLACE PACKAGE BODY pkg_ssi_contest_data
IS
   /***********************************************************************************
      Purpose:  Package to hold all the procedures created for SSI contest module data.

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
     Ravi Dhanekula   1/26/2015      Initial Version
     Suresh J         02/04/2015     Modified code to add user_ID as IN parameter and validation for Managers.
     Suresh J         02/10/2015     Modified Contest_goal for Managers in p_out_siu_ref_cursor
     Suresh J         03/06/2015     Bug Fix 59926 - SR Activity Amount shown on Team Tile was wrong
     Suresh J         03/10/2015     Bug Fix 60203 - Creator view-DTGT-'Potential Points' is always showing 0
     Swati            03/25/2015     Bug 60703 - Manager View - Objective - Details Page - Goal value is displayed wrongly
     Swati            03/25/2015     Bug 60713 - In Do This Get That contest, Contest Activities is displaying randomly in tile and manage contest page
     Ravi Dhanekula   03/25/2015     Bug # 60738 03/25/2015 -- For the Do this Get that contest Stack rank is not displaying top 5 participants
     Suresh J         03/31/2015     Bug#59926 - Fixed Cartesian Product issue in SR Query
     Ravi Dhanekula   04/07/2015     Bug # 60068 - Issue with manager page go al has been fixed.
     Swati            04/08/2015     Bug 60970 - Contest creator - Stack Rank - Update results - Contest payout screen issues
     Ravi Dhanekula   04/09/2015     Bug # 61257 - Contest creator view - Step it up - Details page - Potential Payout value is displayed wrongly
     Suresh J         04/13/2015     Bug #60068 - Fixed the Manager Page Goal Issue
     Suresh J         04/13/2015     Bug # 61257 - Fixed the issue with the previous fix for this bug.
     Suresh J         04/13/2015     Bug # 61255 - Fixed the issue of Level values bar chart to display correct bonus_payout count
     Suresh J         04/14/2015     Bug #61305 - Fixed column header issue
     Suresh J         04/14/2015     Bug #61359 - Fixed PRC_SSI_CONTEST_STACKRANK_LIST to show pax even when no progress loaded for SR
     Suresh J         04/14/2015     Bug #61355 - Fixed issue of SIU contest Team Goal  AN ToGo
     Suresh J         04/15/2015     Bug #60705 - Fixed % display on the tile (progress) for cases Obj Desc varies by pax
     Swati            04/30/2015     Bug 61893 - SSI Manager View - DTGT - Details page - Activities are displayed twice
     Swati            05/05/2015     Bug 62029 - Contest Creator - Step it up - details page - Participant Bar chart count is displayed wrongly
    Suresh J          05/06/2015     Bug 61773 - All SSI CSV extracts should display decimals correctly to match the online data
    Ravi Dhanekula    06/06/2015     Bug # 62070  Contest Creator - Step it up - details page - Participant Bar chart - Bonus Bar is displayed 0
    KrishnaDeepika    05/14/2015     Bug # 62163  Step it up tile is spinning for participant and Manager.
    Suresh J          05/19/2015     Bug 62315 and 62321 - Fixed Sorting & # display issue
    KrishnaDeepika    05/19/2015     Bug 62260 - Manager detail screen-DTGT-Team activity is incorrect
    KrishnaDeepika    05/19/2015     Replaced created_by with new dedicated column CONTEST_OWNER_ID added to the SSI_CONTEST
    Suresh J          05/28/2015     SSI CR - Stack Rank - Individual and Total Payout Changes
    Suresh J          06/05/2015     SSI Claim Submission Changes - Added a new procedure for Claim Extract 
    Suresh J          06/12/2015     Stack Rank Payout Approvals and Issue Payout changes   
    Suresh J          06/16/2015     Bug 62792 - Team Goal and Objective Total for my team should display the same
    Sherif Basha      08/23/2016     Bug 68062 - Activity header column in browser shows activity description
                                                 however in the procedure it is activity claim amount.
    Sherif Basha      09/15/2016     Bug 68030 - The hardcoded Date Submitted column key is now replaced with CMS key
                                                Also if locale key for a non english language is not found use values of en_US.
                                                Included asset DATE_SUBMITTED     
    Sherif Basha      09/20/2016     Bug 67638 - Date format is applied for each locale in submission_date column in prc_ssi_contest_claims_export
    Sherif Basha      09/23/2016     Bug 69033 - Date format is applied for each locale in acivity_as_of_date column in prc_ssi_contest_export  
    Sherif Basha      09/29/2016     Bug 69286 - SSI Contest Extract - Blank is getting displayed under Manager Name Column for all Contest  
    Ravi Dhanekula    05/17/2017     G6.2 - Opt_out_of_awards option is considered for payouts.   
    Gorantla          04/12/2018     G6-4024 - Sales maker pilot / Name issue : The "Submitted by" column name format should be changed to Last name, First name.    
    Gorantla          10/17/2018     Bug 76833 - Maximum contest potential value is showing as goal value on update activity page
                                     Reverted changes done for bug 62792
   ************************************************************************************/

   PROCEDURE prc_ssi_contest_progress (p_in_ssi_contest_id     IN     NUMBER,
      p_in_user_id IN NUMBER,    --02/04/2015
      p_out_contest_type  OUT NUMBER,
      p_out_return_code    OUT NUMBER,
      p_out_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_level_ref_cursor OUT SYS_REFCURSOR,
      p_out_sr_ref_cursor OUT SYS_REFCURSOR,
      p_out_sr_payout_ref_cursor OUT SYS_REFCURSOR,
      p_out_stackrank_cursor           OUT SYS_REFCURSOR,
      p_out_sr_pax_ref_cursor OUT SYS_REFCURSOR)
      IS

      v_out_contest_type NUMBER;
      v_stack_rank          NUMBER;
      v_progress_uploaded NUMBER;
      v_creator_user_id     NUMBER;
      v_locale           VARCHAR2(10);
      v_status           VARCHAR2(20);

      BEGIN

      SELECT contest_type,include_stack_rank,CASE WHEN last_progress_update_date IS NOT NULL THEN 1
                                                                    ELSE 0 END,contest_owner_id --created_by --05/21/2015
                                                                    ,status
      INTO v_out_contest_type,v_stack_rank,v_progress_uploaded,v_creator_user_id,v_status 
      FROM ssi_contest WHERE ssi_contest_id = p_in_ssi_contest_id;

      IF v_out_contest_type = 8 THEN
      SELECT NVL(language_id,(SELECT STRING_VAL FROM os_propertyset WHERE entity_name = 'default.language')) INTO v_locale FROM application_user WHERE user_id = v_creator_user_id;
      END IF;

     IF p_in_user_id IS NULL THEN    --02/04/2015 IF Creator
      IF v_out_contest_type IN (2,4) THEN

      OPEN p_out_ref_cursor FOR
      SELECT ssi_contest_id,
       ssi_contest_activity_id,
       activity_description,
       goal,
       team_activity,
--      FLOOR(NVL(team_activity,0) / goal * 100) AS perc_achieved,   --04/15/2015
      CASE WHEN v_out_contest_type = 4 AND activity_measure_type = 'units' AND activity_description = 'Varies by Participant' THEN   --04/15/2015
           FLOOR(NVL(pax_achieved,0) / total_pax * 100)
      ELSE
           FLOOR(NVL(team_activity,0) / goal * 100)
      END AS perc_achieved,
      CASE WHEN NVL(team_activity,0) > goal THEN 0
               ELSE goal-NVL(team_activity,0)
               END to_go,
       NVL(pax_achieved,0) pax_achieved,
       total_pax,
       Potential_payout,
       Maximum_Payout,
       payout_cap_amount,
       Maximum_Payout_with_bonus,
       min_qualifier,
       for_every,
       will_earn,
       objective_amount total_objective_amount,
       payout_description,
       payout_quantity
  FROM (
  SELECT sc.ssi_contest_id,
                            NULL ssi_contest_activity_id,
                 sc.contest_goal goal,   --06/16/2015   --10/17/2018 uncommented
                 -- SUM (scp.objective_amount)  goal,   --06/16/2015   --10/17/2018 commented out
                 SUM (scpp.activity_amt) team_activity,
                 SUM (scp.objective_amount) objective_amount,
                 SUM (
                    CASE
                       WHEN scpp.activity_amt >= scp.objective_amount THEN 1
                       ELSE 0
                    END)
                    pax_achieved,
                 COUNT (DISTINCT scp.ssi_contest_participant_id) total_pax,
                 SUM (
                   CASE WHEN scpp.activity_amt >= scp.objective_amount AND sc.include_bonus = 0 THEN objective_payout
                        WHEN scpp.activity_amt >= scp.objective_amount AND sc.include_bonus = 1 AND
                             (scpp.activity_amt - scp.objective_amount)* scp.objective_bonus_payout/ scp.objective_bonus_increment  >= objective_bonus_cap THEN objective_payout + objective_bonus_cap
                        WHEN scpp.activity_amt >= scp.objective_amount AND sc.include_bonus = 1 AND
                             (scpp.activity_amt - scp.objective_amount)* scp.objective_bonus_payout/ scp.objective_bonus_increment  < objective_bonus_cap THEN
                             objective_payout + FLOOR((scpp.activity_amt - scp.objective_amount)/scp.objective_bonus_increment)* scp.objective_bonus_payout
                             ELSE 0 END )
                   Potential_payout, --Bug # 60138
                 SUM (scp.objective_payout) Maximum_Payout,
                 SUM (scp.objective_payout + scp.objective_bonus_cap)
                    Maximum_Payout_with_bonus,
                    NULL min_qualifier,
                    NULL for_every,
                    NULL will_earn,
                    NULL payout_cap_amount,
    CASE WHEN COUNT(DISTINCT scp.activity_description) <= 1 THEN (SELECT activity_description  FROM ssi_contest_participant  --Bug 61875 04/27/2015
    WHERE ssi_contest_id = p_in_ssi_contest_id AND ROWNUM <2)
    ELSE 'Varies by Participant' END   AS activity_description,
    CASE WHEN COUNT(DISTINCT scp.objective_payout_description) <= 1 THEN (SELECT objective_payout_description  FROM ssi_contest_participant   --Bug 61875 04/27/2015
    WHERE ssi_contest_id = p_in_ssi_contest_id AND ROWNUM <2)
    ELSE 'Varies by Participant' END   AS payout_description,
                    NULL payout_quantity,
                    sc.activity_measure_type
            FROM ssi_contest sc,
                 ssi_contest_pax_progress scpp,
                 ssi_contest_participant scp,
                 application_user au
           WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                 AND sc.ssi_contest_id = scp.ssi_contest_id
                 AND scp.user_id = scpp.user_id(+)
                 AND scp.user_id = au.user_id
                 AND au.is_active = 1
                 AND scP.ssi_contest_id = scpp.ssi_contest_id(+)
                --AND sc.last_progress_update_date IS NOT NULL  --Bug # 60019
                AND sc.contest_type = 4
        GROUP BY sc.ssi_contest_id,sc.contest_goal,sc.activity_measure_type--,scp.objective_payout_description
       UNION ALL
         SELECT sc.ssi_contest_id,
                            sca.ssi_contest_activity_id,
                 sca.goal_amount goal,
                 SUM (scpp.activity_amt) team_activity,
                 NULL objective_amount,
                 NULL pax_achieved,
                 (SELECT COUNT(1) FROM ssi_contest_participant scp, application_user au WHERE ssi_contest_id = p_in_ssi_contest_id AND
                                                                    scp.user_id = au.user_id AND au.is_active = 1 ) total_pax,
        SUM (
                 CASE WHEN scpp.activity_amt > sca.min_qualifier THEN
              FLOOR( (
                 CASE WHEN scpp.activity_amt <= (sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount) THEN scpp.activity_amt
                 ELSE  (sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount) END - sca.min_qualifier)/sca.increment_amount )* sca.payout_amount
               ELSE 0 END ) Potential_payout,
                 NULL Maximum_Payout,
                 NULL Maximum_Payout_with_bonus,
                    sca.min_qualifier,
                    sca.increment_amount for_every,
                    sca.payout_amount will_earn,
                    sca.payout_cap_amount payout_cap_amount,
                    sca.description activity_description,
                    sca.payout_description,
                    SUM (   CASE WHEN scpp.activity_amt > sca.min_qualifier THEN
              FLOOR( (
                 CASE WHEN scpp.activity_amt <= (sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount) THEN scpp.activity_amt
                 ELSE  (sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount) END - sca.min_qualifier)/sca.increment_amount )
               ELSE 0 END  ) payout_quantity,
               sc.activity_measure_type
            FROM ssi_contest sc,
                 ssi_contest_pax_progress scpp,
                 ssi_contest_activity sca,
                 ssi_contest_participant scp,
                 application_user au
           WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                 AND sc.ssi_contest_id = sca.ssi_contest_id
                 AND sca.ssi_contest_id = scpp.ssi_contest_id
                 AND sc.ssi_contest_id = scp.ssi_contest_id
                 AND scp.user_id = au.user_id
                 AND au.is_active = 1
                 AND sca.ssi_contest_activity_id = scpp.ssi_contest_activity_id
                 AND scp.user_id = scpp.user_id
        GROUP BY sc.ssi_contest_id,sca.goal_amount,sca.payout_cap_amount,sca.ssi_contest_activity_id,sca.increment_amount,sca.payout_amount,sca.min_qualifier,sca.description,sca.payout_description,sc.activity_measure_type
        UNION ALL
        SELECT sc.ssi_contest_id,
                            sca.ssi_contest_activity_id,
                 sca.goal_amount goal,
                 0 team_activity,
                 NULL objective_amount,
                 NULL pax_achieved,
                 (SELECT COUNT(1) FROM ssi_contest_participant WHERE ssi_contest_id = p_in_ssi_contest_id ) total_pax,
                 0 Potential_payout,
                 NULL Maximum_Payout,
                 NULL Maximum_Payout_with_bonus,
                    sca.min_qualifier,
                    sca.increment_amount for_every,
                    sca.payout_amount will_earn,
                    sca.payout_cap_amount payout_cap_amount,
                    sca.description activity_description,
                    sca.payout_description,
                    0 payout_quantity,
                    sc.activity_measure_type
            FROM ssi_contest sc,
                 ssi_contest_activity sca,
                 ssi_contest_participant scp,
                 application_user au
           WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                 AND sc.ssi_contest_id = sca.ssi_contest_id
                 AND sc.ssi_contest_id = scp.ssi_contest_id
                 AND scp.user_id = au.user_id
                 AND au.is_active = 1
                 AND NOT EXISTS (SELECT * FROM ssi_contest_pax_progress WHERE sca.ssi_contest_activity_id = ssi_contest_activity_id)
          GROUP BY sc.ssi_contest_id,sca.goal_amount,sca.payout_cap_amount,sca.ssi_contest_activity_id,sca.increment_amount,sca.payout_amount,sca.min_qualifier,sca.description,sca.payout_description,sc.activity_measure_type)
          order by ssi_contest_id,ssi_contest_activity_id; -- 03/25/2015 Bug 60713

        OPEN p_out_siu_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_siu_level_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_sr_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_sr_payout_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_sr_pax_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;


        ELSIF v_out_contest_type = 16 THEN --SIU

        OPEN p_out_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_sr_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_sr_payout_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

         OPEN p_out_sr_pax_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_siu_ref_cursor FOR
        SELECT contest_goal,         --04/13/2015
               sit_indv_baseline_type,
               SUM(activity_amt) AS activity,
               CASE WHEN NVL(SUM(activity_amt),0) > contest_goal THEN 0
               ELSE contest_goal-NVL(SUM(activity_amt),0)
               END to_go,
               sum(total_payout) total_payout,
               sum(potential_payout) potential_payout,
               sum(total_potential_payout) total_potential_payout,
               (sum(total_potential_payout)-sum(total_payout)) remaining_payout,
               FLOOR(sum(total_payout)/sum(total_potential_payout)*100)  perc_payout
               FROM (
       SELECT sc.ssi_contest_id,
            au.user_id,
            au.last_name,au.first_name,
            n.name org_name,
            sc.activity_amt,
            NVL(sc.level_completed,0) level_completed, --Bug # 60745 03/25/2015
            sc.level_payout,
            sc.bonus_payout,
            sc.total_payout
            ,(SELECT payout_amount FROM (SELECT payout_amount,RANK() OVER (PARTITION BY ssi_contest_id ORDER BY  payout_amount DESC,sequence_number DESC ) as rec_rank FROM ssi_contest_level scl WHERE ssi_contest_id = p_in_ssi_contest_id)
            WHERE rec_rank =1) + NVL(sit_bonus_cap,0) total_potential_payout
            ,(SELECT payout_amount FROM (SELECT payout_amount,RANK() OVER (PARTITION BY ssi_contest_id ORDER BY  payout_amount DESC,sequence_number DESC ) as rec_rank FROM ssi_contest_level scl WHERE ssi_contest_id = p_in_ssi_contest_id)
            WHERE rec_rank =1) potential_payout
            ,contest_goal
            ,sit_indv_baseline_type
                                            FROM (
             SELECT DISTINCT ssi_contest_id,user_id,activity_amt,CASE WHEN level_completed =0 THEN NULL ELSE level_completed END level_completed,level_payout,
             CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                                         ELSE bonus_payout END,0)
             ELSE 0
             END bonus_payout,
             level_payout + CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                                         ELSE bonus_payout END,0)
             ELSE 0
             END total_payout
             ,sit_bonus_cap
             ,contest_goal
             ,sit_indv_baseline_type
               FROM (
            SELECT sc.ssi_contest_id,scp.user_id,scpp.activity_amt, scl.sequence_number,scl.goal_amount,CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
                      WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
                      WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END level_completed,
            RANK() OVER (PARTITION BY scp.user_id ORDER BY CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
                      WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
                      WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank,
            CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.payout_amount
                      WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.payout_amount
                      WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.payout_amount ELSE 0 END level_payout,
                CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN sit_bonus_cap
                     WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment)   * sit_bonus_payout
                     WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN sit_bonus_cap
                     WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment)* sit_bonus_payout
                     WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap
                     WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment) * sit_bonus_payout
                     ELSE 0
                END bonus_payout,
                     (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = p_in_ssi_contest_id) max_level,sit_bonus_cap
                     ,contest_goal
                     ,sit_indv_baseline_type
              FROM ssi_contest sc,
                   ssi_contest_level scl,
                   ssi_contest_participant scp,
                   ssi_contest_pax_progress scpp
             WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                   AND sc.ssi_contest_id = scl.ssi_contest_id
                   AND sc.ssi_contest_id = scp.ssi_contest_id
                   AND scp.user_id = scpp.user_id(+)
                   AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                   ) WHERE rec_rank = 1 ) sc, application_user au, participant p,user_node un, node n
                   WHERE sc.user_id = au.user_id
                   AND sc.user_id = p.user_id
                   AND sc.user_id = un.user_id
                   AND un.status = 1
                   AND un.is_primary = 1
                   AND au.is_active = 1
                   AND un.node_id = n.node_id
                                     )  GROUP BY contest_goal,sit_indv_baseline_type;

--        SELECT contest_goal,   --04/13/2015
--               sit_indv_baseline_type,
--               activity,
--               CASE WHEN NVL(activity,0) > contest_goal THEN 0
--               ELSE contest_goal-NVL(activity,0)
--               END to_go,
--               total_payout,
--               potential_payout,
--               total_potential_payout,
--               (total_potential_payout-total_payout) remaining_payout,
--               FLOOR(total_payout/total_potential_payout*100)  perc_payout
--               FROM (
--                    SELECT contest_goal,
--                           sit_indv_baseline_type,
--                           SUM(activity_amt) activity,
--                           SUM(level_payout) level_payout,
--                           SUM(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
--                                                                             ELSE bonus_payout END) bonus_payout,
--                           SUM(level_payout+ NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
--                                                                             ELSE bonus_payout END,0) ) total_payout,
--                           SUM(total_potential_payout) total_potential_payout,
--                           SUM(potential_payout) potential_payout
--                    FROM (
--                    SELECT contest_goal,ssi_contest_id,sit_indv_baseline_type,user_id,activity_amt,level_payout,bonus_payout,sit_bonus_cap,
--                    (SELECT payout_amount FROM (SELECT payout_amount,RANK() OVER (PARTITION BY ssi_contest_id ORDER BY  payout_amount DESC,sequence_number DESC ) as rec_rank FROM ssi_contest_level scl WHERE ssi_contest_id = p_in_ssi_contest_id)
--WHERE rec_rank =1) + NVL(sit_bonus_cap,0) total_potential_payout,
--                    (SELECT payout_amount FROM (SELECT payout_amount,RANK() OVER (PARTITION BY ssi_contest_id ORDER BY  payout_amount DESC,sequence_number DESC ) as rec_rank FROM ssi_contest_level scl WHERE ssi_contest_id = p_in_ssi_contest_id)
--WHERE rec_rank =1) potential_payout FROM (
--                    SELECT DISTINCT contest_goal,ssi_contest_id,sit_indv_baseline_type,user_id,activity_amt,level_payout,CASE WHEN max_level = level_completed THEN bonus_payout ELSE 0 END bonus_payout,sit_bonus_cap--Bug # 61257
--                    FROM (
--                      SELECT sc.ssi_contest_id,
--                                  sc.contest_goal,
--                            sc.sit_indv_baseline_type,
--                            scp.user_id,
--                            scpp.activity_amt,
--                            CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
--          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
--          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END level_completed,
--                            RANK() OVER (PARTITION BY scp.user_id ORDER BY CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
--          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
--          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank,
--                            CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.payout_amount
--                                 WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.payout_amount
--                                 WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.payout_amount
--                                 ELSE 0
--                            END level_payout,
--                            CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment) * sit_bonus_payout
--                                 WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment) * sit_bonus_payout
--                                 WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment ) * sit_bonus_payout
--                                 ELSE 0
--                            END bonus_payout,
--               (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = p_in_ssi_contest_id) max_level,
--                            sit_bonus_cap
--                      FROM ssi_contest sc,
--                           ssi_contest_level scl,
--                           ssi_contest_participant scp,
--                           ssi_contest_pax_progress scpp,
--                           application_user au
--                     WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
--                           AND sc.ssi_contest_id = scl.ssi_contest_id
--                           AND sc.ssi_contest_id = scp.ssi_contest_id
--                           AND scp.user_id = au.user_id
--                           AND au.is_active = 1
--                           AND scp.user_id = scpp.user_id(+)
--                           AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
--                           )
--                            WHERE rec_rank = 1 )     )
--                           GROUP BY contest_goal,sit_indv_baseline_type,sit_bonus_cap
--                         );

       OPEN p_out_siu_level_ref_cursor FOR
       WITH pax_level_completed AS   --04/13/2015
        (
         SELECT DISTINCT user_id,level_completed,bonus_payout FROM  (
         SELECT user_id,CASE WHEN sequence_number = max_level AND level_completed = -1 THEN -1 --Bug # 61255
                                          WHEN sequence_number <> max_level AND level_completed = -1 THEN sequence_number
                                          ELSE level_completed END level_completed,rec_rank
                                          ,CASE WHEN sequence_number = max_level THEN bonus_payout ELSE 0 END bonus_payout
                                           FROM (
         SELECT scp.user_id,
         CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount + sit_bonus_increment THEN -1
                  WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
                  WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
                  WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END level_completed,
        RANK() OVER (PARTITION BY scp.user_id ORDER BY CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
                  WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
                  WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank,
                                 (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = p_in_ssi_contest_id) max_level ,
                                 scl.sequence_number
                        ,CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN sit_bonus_cap
                             WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment)   * sit_bonus_payout
                             WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN sit_bonus_cap
                             WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment)* sit_bonus_payout
                             WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap
                             WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment) * sit_bonus_payout
                             ELSE 0
                        END bonus_payout
          FROM ssi_contest sc,
               ssi_contest_level scl,
               ssi_contest_participant scp,
               ssi_contest_pax_progress scpp,
               application_user au
         WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
               AND sc.ssi_contest_id = scl.ssi_contest_id
               AND sc.ssi_contest_id = scp.ssi_contest_id
               AND scp.user_id = au.user_id
               AND au.is_active = 1
               AND scp.user_id = scpp.user_id(+)
               AND scp.ssi_contest_id = scpp.ssi_contest_id(+))
               )
               WHERE rec_rank = 1
                )
       SELECT sequence_number,goal_amount,payout_amount,payout_description
       , CASE WHEN sequence_number = -1 THEN bonus_cnt ELSE pax_count END pax_count  --04/13/2015
       FROM (SELECT scl.sequence_number,scl.goal_amount,scl.payout_amount,scl.payout_description,COUNT(plc.user_id) pax_count
               ,CASE WHEN scl.sequence_number = -1 THEN (select count(p.user_id) from pax_level_completed p where p.bonus_payout > 0) END bonus_cnt -- Bug 62029 05/05/2015
               FROM (SELECT * FROM pax_level_completed WHERE bonus_payout = 0) plc , --Bug # 62070
               (SELECT sequence_number, scl.goal_amount,payout_amount,scl.payout_description FROM ssi_contest_level scl,ssi_contest sc  --Bug # 60717 03/24/2015
               WHERE scl.ssi_contest_id = p_in_ssi_contest_id AND scl.ssi_contest_id = sc.ssi_contest_id
               UNION ALL
               SELECT 0,NULL,NULL,NULL FROM DUAL
               UNION
               SELECT -1,NULL,NULL,NULL FROM DUAL) scl
              WHERE scl.sequence_number = plc.level_completed(+)
               GROUP BY scl.sequence_number,scl.goal_amount,scl.payout_amount,scl.payout_description);

--       WITH pax_level_completed AS   --04/13/2015
--(
-- SELECT DISTINCT user_id,level_completed FROM  (
-- SELECT user_id,CASE WHEN sequence_number = max_level AND level_completed = -1 THEN -1 --Bug # 61255
--                                  WHEN sequence_number <> max_level AND level_completed = -1 THEN sequence_number
--                                  ELSE level_completed END level_completed,rec_rank FROM (
-- SELECT scp.user_id,
-- CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount + sit_bonus_increment THEN -1
--          WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
--          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
--          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END level_completed,
--RANK() OVER (PARTITION BY scp.user_id ORDER BY CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
--          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
--          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank,
--                         (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = p_in_ssi_contest_id) max_level ,
--                         scl.sequence_number
--  FROM ssi_contest sc,
--       ssi_contest_level scl,
--       ssi_contest_participant scp,
--       ssi_contest_pax_progress scpp,
--       application_user au
-- WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
--       AND sc.ssi_contest_id = scl.ssi_contest_id
--       AND sc.ssi_contest_id = scp.ssi_contest_id
--       AND scp.user_id = au.user_id
--       AND au.is_active = 1
--       AND scp.user_id = scpp.user_id(+)
--       AND scp.ssi_contest_id = scpp.ssi_contest_id(+))
--       )
--       WHERE rec_rank = 1
--        )
--       SELECT scl.sequence_number,scl.goal_amount,scl.payout_amount,scl.payout_description,COUNT(plc.user_id) pax_count  FROM pax_level_completed plc ,
--       (SELECT sequence_number, scl.goal_amount,payout_amount,scl.payout_description FROM ssi_contest_level scl,ssi_contest sc  --Bug # 60717 03/24/2015
--       WHERE scl.ssi_contest_id = p_in_ssi_contest_id AND scl.ssi_contest_id = sc.ssi_contest_id
--       UNION ALL
--       SELECT 0,NULL,NULL,NULL FROM DUAL
--       UNION
--       SELECT -1,NULL,NULL,NULL FROM DUAL) scl
--      WHERE scl.sequence_number = plc.level_completed(+)
--       GROUP BY scl.sequence_number,scl.goal_amount,scl.payout_amount,scl.payout_description;

       ELSE
       DELETE FROM temp_table_session;

      INSERT INTO temp_table_session
                SELECT cav.asset_code,
                          MAX (DECODE (cav.key, 'NAME', cav.cms_value, NULL)) AS cms_name,
                         MAX (DECODE (cav.key, 'EARNED_IMAGE_SMALL', cav.cms_value, NULL)) AS cms_code
                       FROM vw_cms_asset_value cav
                       WHERE asset_code='promotion.badge'
                       AND locale = v_locale
                   GROUP BY cav.asset_code,
                            cav.locale,
                            cav.asset_id,
                            cav.content_key_id,
                            cav.content_id;

        OPEN p_out_siu_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_siu_level_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;


      IF v_status = 'finalize_results' THEN    --05/28/2015  

        OPEN p_out_sr_ref_cursor FOR    --05/28/2015   
              SELECT contest_goal,
                     SUM (activity_amt) activity_amt,
                     CASE
                        WHEN contest_goal >= SUM (NVL (activity_amt, 0))
                        THEN
                           (contest_goal - SUM (NVL (activity_amt, 0)))
                        ELSE
                           0
                     END
                        To_go,
                     (SELECT SUM (payout_amount)
                        FROM ssi_contest_pax_payout
                       WHERE ssi_contest_id = p_in_ssi_contest_id)
                        maximum_points,
                     stack_rank_qualifier_amount,
                     SUM (payout_amount) potential_payout,
                     (SELECT COUNT (user_id)
                        FROM ssi_contest_participant
                       WHERE ssi_contest_id = p_in_ssi_contest_id)
                        total_pax                                  --Bug #61354 04/21/2015
                                 ,
                     (SELECT COUNT (user_id)
                        FROM ssi_contest_pax_payout
                       WHERE ssi_contest_id = p_in_ssi_contest_id)
                        pax_achieved                               --Bug #61354 04/21/2015
                FROM (SELECT sc2.contest_goal,
                             sc2.activity_amt,
                             sc2.stack_rank_qualifier_amount,
                             CASE
                                WHEN     sc2.stack_rank_qualifier_amount IS NOT NULL
                                     AND sc2.activity_amt < sc2.stack_rank_qualifier_amount
                                THEN
                                   0
                                ELSE
                                   NVL (scsp.payout_amount, 0)
                             END
                                payout_amount
                        FROM (SELECT scp.ssi_contest_id,
                                     scp.user_id,
                                     scpsr.stack_rank_position,
                                     scpp.activity_amt,
                                     sc.contest_goal,
                                     sc.stack_rank_qualifier_amount
                                FROM ssi_contest_pax_progress scpp,
                                     ssi_contest_pax_stack_rank scpsr,
                                     ssi_contest sc,
                                     application_user au,
                                     ssi_contest_participant scp
                               WHERE     scp.ssi_contest_id = p_in_ssi_contest_id
                                     AND scp.ssi_contest_id = scpsr.ssi_contest_id(+)
                                     AND scp.user_id = scpsr.user_id(+)
                                     AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                                     AND scp.user_id = scpp.user_id(+)
                                     AND scp.user_id = au.user_id
                                     AND au.is_active = 1
                                     AND sc.ssi_contest_id = scp.ssi_contest_id) sc2,
                             ssi_contest_pax_payout scsp
                       WHERE     scsp.ssi_contest_id(+) = sc2.ssi_contest_id
                             AND sc2.user_id = scsp.user_id (+))
            GROUP BY contest_goal, stack_rank_qualifier_amount;      
      
      ELSE 

        OPEN p_out_sr_ref_cursor FOR    --03/31/2015
              SELECT contest_goal,
                     SUM (activity_amt) activity_amt,
                     CASE
                        WHEN contest_goal >= SUM (NVL (activity_amt, 0))
                        THEN
                           (contest_goal - SUM (NVL (activity_amt, 0)))
                        ELSE
                           0
                     END
                        To_go,
                     (SELECT SUM (payout_amount)
                        FROM ssi_contest_sr_payout
                       WHERE ssi_contest_id = p_in_ssi_contest_id)
                        maximum_points,
                     stack_rank_qualifier_amount,
                     SUM (paymount_amount) potential_payout,
                     (SELECT COUNT (user_id)
                        FROM ssi_contest_participant
                       WHERE ssi_contest_id = p_in_ssi_contest_id)
                        total_pax                                  --Bug #61354 04/21/2015
                                 ,
                     (SELECT COUNT (user_id)
                        FROM ssi_contest_pax_payout
                       WHERE ssi_contest_id = p_in_ssi_contest_id)
                        pax_achieved                               --Bug #61354 04/21/2015
                FROM (SELECT sc2.contest_goal,
                             sc2.activity_amt,
                             sc2.stack_rank_qualifier_amount,
                             CASE
                                WHEN     sc2.stack_rank_qualifier_amount IS NOT NULL
                                     AND sc2.activity_amt < sc2.stack_rank_qualifier_amount
                                THEN
                                   0
                                ELSE
                                   NVL (scsp.payout_amount, 0)
                             END
                                paymount_amount
                        FROM (SELECT scp.ssi_contest_id,
                                     scp.user_id,
                                     scpsr.stack_rank_position,
                                     scpp.activity_amt,
                                     sc.contest_goal,
                                     sc.stack_rank_qualifier_amount
                                FROM ssi_contest_pax_progress scpp,
                                     ssi_contest_pax_stack_rank scpsr,
                                     ssi_contest sc,
                                     application_user au,
                                     ssi_contest_participant scp
                               WHERE     scp.ssi_contest_id = p_in_ssi_contest_id
                                     AND scp.ssi_contest_id = scpsr.ssi_contest_id(+)
                                     AND scp.user_id = scpsr.user_id(+)
                                     AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                                     AND scp.user_id = scpp.user_id(+)
                                     AND scp.user_id = au.user_id
                                     AND au.is_active = 1
                                     AND sc.ssi_contest_id = scp.ssi_contest_id) sc2,
                             ssi_contest_sr_payout scsp
                       WHERE     scsp.ssi_contest_id(+) = sc2.ssi_contest_id
                             AND sc2.stack_rank_position = scsp.rank_position(+))
            GROUP BY contest_goal, stack_rank_qualifier_amount;      

END IF;
      
--        SELECT sc.contest_goal,    --03/31/2015
--         SUM (scpp.activity_amt) activity_amt,
--        CASE WHEN sc.contest_goal >= SUM (NVL (scpp.activity_amt, 0)) THEN  (sc.contest_goal - SUM (NVL (scpp.activity_amt, 0)))
--                 ELSE 0 END To_go,
--         scsp.maximum_points,  --03/06/2015--03/27/2015 Bug # 60074
--         sc.stack_rank_qualifier_amount
--    FROM ssi_contest sc,
--         ssi_contest_pax_progress scpp,
--         ssi_contest_participant scp,--03/20/2015 Bug # 60607
--         application_user au,
--         (select ssi_contest_id, sum(payout_amount) maximum_points from ssi_contest_sr_payout
--         where ssi_contest_id = p_in_ssi_contest_id group by ssi_contest_id ) scsp  --03/06/2015
--   WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
--         AND sc.ssi_contest_id = scpp.ssi_contest_id(+)
--         AND sc.ssi_contest_id = scp.ssi_contest_id
--         AND scp.user_id = au.user_id
--         AND au.is_active = 1
--         AND sc.ssi_contest_id = scsp.ssi_contest_id
--GROUP BY sc.contest_goal,sc.stack_rank_qualifier_amount,scsp.maximum_points;

        OPEN p_out_sr_payout_ref_cursor FOR
        SELECT rank_position, payout_amount,payout_desc,br.badge_rule_id,NVL((SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale = v_locale),(SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale ='en_US')) badge_name
        ,cms_badge.image_small_URL badge_image FROM ssi_contest_sr_payout sc,badge_rule br,
         (select asset_code, cms_name, cms_code image_small_URL from temp_table_session ) cms_badge
          WHERE ssi_contest_id = p_in_ssi_contest_id AND sc.badge_rule_id = br.badge_rule_id(+)
          AND br.cm_asset_key = cms_badge.cms_name (+);

        OPEN p_out_sr_pax_ref_cursor FOR
          SELECT rank_position,au.user_id,au.first_name,au.last_name, p.avatar_small avatar_url,scpp.activity_amt, 0 AS is_teammember,sc.payout_amount,sc.payout_desc payout_description
          FROM ssi_contest_sr_payout sc,ssi_contest scon,
         application_user au, participant p ,ssi_contest_pax_stack_rank scpsr,ssi_contest_pax_progress scpp
          WHERE sc.ssi_contest_id = p_in_ssi_contest_id
          AND sc.ssi_contest_id = scon.ssi_contest_id
          AND sc.ssi_contest_id = scpsr.ssi_contest_id
          AND sc.rank_position = scpsr.stack_rank_position
          AND sc.ssi_contest_id = scpp.ssi_contest_id
          AND scpsr.user_id = scpp.user_id
          AND scpsr.user_id = scpsr.user_id
          AND scpsr.user_id = p.user_id
          AND scpsr.user_id = au.user_id
          AND au.is_active = 1
          AND scon.status <> 'finalize_results'
          AND scpp.activity_amt >= NVL(scon.stack_rank_qualifier_amount,0) --Checking minimum qualifier 04/08/2015 Bug 60970
          UNION ALL
          SELECT scpsr.stack_rank_position rank_position,au.user_id,au.first_name,au.last_name, p.avatar_small avatar_url,scpp.activity_amt, 0 AS is_teammember,sc.payout_amount,sc.payout_description
          FROM ssi_contest_pax_payout sc,ssi_contest scon,
         application_user au, participant p ,ssi_contest_pax_stack_rank scpsr,ssi_contest_pax_progress scpp
          WHERE sc.ssi_contest_id = p_in_ssi_contest_id
          AND sc.ssi_contest_id = scon.ssi_contest_id
          AND sc.user_id = scpsr.user_id
          AND sc.ssi_contest_id = scpsr.ssi_contest_id
          AND sc.ssi_contest_id = scpp.ssi_contest_id
          AND sc.user_id = scpp.user_id
          AND sc.user_id = au.user_id
          AND au.is_active =1
          AND au.user_id = p.user_id
          AND scon.status = 'finalize_results' ;

        END IF;

        OPEN p_out_stackrank_cursor FOR-- Bug # 60609
        SELECT ssi_contest_activity_id,stack_rank,
         participant_id,first_name,last_name,avatar,
         pax_activity,payout_amount, payout_desc FROM (
        SELECT NULL ssi_contest_activity_id,scpsr.stack_rank_position stack_rank,
         au.user_id participant_id,au.first_name,au.last_name,p.avatar_small avatar,
         scpp.activity_amt pax_activity,NULL payout_amount, NULL payout_desc
    FROM ssi_contest_pax_stack_rank scpsr,
         ssi_contest_pax_progress scpp,
         application_user au,
         ssi_contest sc,
         participant p
   WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
         AND sc.contest_type IN (4,16)
         AND scpsr.ssi_contest_id = sc.ssi_contest_id
         AND scpsr.user_id = scpp.user_id
         AND scpsr.ssi_contest_id = scpp.ssi_contest_id
         AND scpsr.user_id = au.user_id
         AND au.user_id = p.user_id
         AND au.is_active = 1 ORDER BY stack_rank) WHERE ROWNUM<6
         UNION ALL
         SELECT ssi_contest_activity_id,stack_rank,
         participant_id,first_name,last_name,avatar,
         pax_activity,payout_amount, payout_desc FROM (
SELECT sca.ssi_contest_activity_id,scpsr.stack_rank_position stack_rank,
         au.user_id participant_id,au.first_name,au.last_name,p.avatar_small avatar,
         scpp.activity_amt pax_activity, NULL payout_amount, NULL payout_desc,RANK() OVER (PARTITION BY sca.ssi_contest_activity_id ORDER BY scpsr.stack_rank_position,au.last_name,au.user_id ) as rec_rank --Bug # 60738 03/25/2015
    FROM ssi_contest_pax_stack_rank scpsr,
         ssi_contest_pax_progress scpp,
         application_user au,
         ssi_contest_activity sca,
         participant p
   WHERE     scpsr.ssi_contest_id = p_in_ssi_contest_id
         AND scpsr.user_id = scpp.user_id
         AND scpsr.ssi_contest_id = scpp.ssi_contest_id
         AND scpsr.ssi_contest_activity_id = scpp.ssi_contest_activity_id
         AND scpsr.user_id = au.user_id
         AND scpsr.ssi_contest_id = sca.ssi_contest_id
         AND scpsr.ssi_contest_activity_id = sca.ssi_contest_activity_id
         AND au.user_id = p.user_id
         AND au.is_active = 1 ORDER BY stack_rank,last_name
         ) WHERE rec_rank <=5
   UNION ALL --Stack rank contest
         SELECT NULL ssi_contest_activity_id,scpsr.stack_rank_position stack_rank,
         au.user_id participant_id,au.first_name,au.last_name,p.avatar_small avatar,
         scpp.activity_amt pax_activity, CASE WHEN sc.contest_end_date < SYSDATE THEN ssifp.payout_amount ELSE NULL END payout_amount,  --05/28/2015
         CASE WHEN sc.contest_end_date < SYSDATE THEN scsp.payout_desc ELSE NULL END payout_desc
    FROM ssi_contest_pax_stack_rank scpsr,
         ssi_contest_pax_progress scpp,
         application_user au,
         participant p,
         ssi_contest_sr_payout scsp,
         ssi_contest sc
         ,ssi_contest_pax_payout ssifp  --05/28/2015
   WHERE     scpsr.ssi_contest_id = p_in_ssi_contest_id
         AND scpsr.user_id = scpp.user_id
         AND scpsr.ssi_contest_id = scpp.ssi_contest_id
         AND scpsr.user_id = au.user_id
         AND au.user_id = p.user_id
         AND au.is_active = 1
         AND scsp.rank_position = scpsr.stack_rank_position
         AND scsp.ssi_contest_id = scpsr.ssi_contest_id
         AND scsp.ssi_contest_id = sc.ssi_contest_id
         AND ssifp.ssi_contest_id = scpsr.ssi_contest_id   --05/28/2015
         AND ssifp.user_id = scpp.user_id   --05/28/2015
ORDER BY stack_rank;
ELSE    --02/04/2015  IF Manager

      DELETE FROM gtt_node_and_below_users;

      INSERT INTO gtt_node_and_below_users (user_id)--Bug # 61461
      SELECT   DISTINCT au.user_id
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
                                                                       (SELECT node_id,NAME as node_name,parent_node_id,path
                                                                        FROM node n
                                                                        START WITH parent_node_id IS NULL
                                                                        CONNECT BY PRIOR node_id = parent_node_id) ip
                                                                        ,user_node un
                                                                        ,application_user au
                                        where ip.node_id = npn.path_node_id
                                              AND npn.node_id = un.node_id
                                              AND un.user_id = au.user_id
                                              AND ip.node_id IN ((select node_id from user_node where user_id = p_in_user_id and status = 1 and role in ('own','mgr')))
                                              AND au.is_active = 1
                                              AND au.user_type = 'pax'
                                              AND un.status = 1;

 IF v_out_contest_type IN (2,4) THEN

      OPEN p_out_ref_cursor FOR
      SELECT ssi_contest_id,
       ssi_contest_activity_id,
       activity_description,
       goal,
       team_activity,
--      FLOOR(NVL(team_activity,0) / goal * 100) AS perc_achieved,   --04/15/2015
      CASE WHEN v_out_contest_type = 4 AND activity_measure_type = 'units' AND activity_description = 'Varies by Participant' THEN   --04/15/2015
           FLOOR(NVL(pax_achieved,0) / total_pax * 100)
      ELSE
           FLOOR(NVL(team_activity,0) / goal * 100)
      END AS perc_achieved,
      CASE WHEN NVL(team_activity,0) > goal THEN 0
               ELSE goal-NVL(team_activity,0)
      END to_go,
       NVL(pax_achieved,0) pax_achieved,
       total_pax,
       Potential_payout,
       Maximum_Payout,
       payout_cap_amount,
       Maximum_Payout_with_bonus,
       min_qualifier,
       for_every,
       will_earn,
       objective_amount total_objective_amount,
       payout_description,
       payout_quantity
  FROM (
  SELECT sc.ssi_contest_id,
                            NULL ssi_contest_activity_id,
                 --sc.contest_goal goal,
                 FLOOR(SUM(scp.objective_amount) * sc.goal_percentage/100)  goal,   --03/25/2015 Bug 60703
                 SUM (scpp.activity_amt) team_activity,
                 SUM (scp.objective_amount) objective_amount,
                 SUM (
                    CASE
                       WHEN scpp.activity_amt >= scp.objective_amount THEN 1
                       ELSE 0
                    END)
                    pax_achieved,
                 COUNT (DISTINCT scp.ssi_contest_participant_id) total_pax,
                 SUM (
                   CASE WHEN scpp.activity_amt >= scp.objective_amount AND sc.include_bonus = 0 THEN objective_payout
                        WHEN scpp.activity_amt >= scp.objective_amount AND sc.include_bonus = 1 AND
                             (scpp.activity_amt - scp.objective_amount)* scp.objective_bonus_payout/ scp.objective_bonus_increment  >= objective_bonus_cap THEN objective_payout + objective_bonus_cap
                        WHEN scpp.activity_amt >= scp.objective_amount AND sc.include_bonus = 1 AND
                             (scpp.activity_amt - scp.objective_amount)* scp.objective_bonus_payout/ scp.objective_bonus_increment  < objective_bonus_cap THEN
                             objective_payout + FLOOR((scpp.activity_amt - scp.objective_amount)/scp.objective_bonus_increment)* scp.objective_bonus_payout
                             ELSE 0 END )
                   Potential_payout,--Bug # 60138
                 SUM (scp.objective_payout) Maximum_Payout,
                 SUM (scp.objective_payout + scp.objective_bonus_cap)
                    Maximum_Payout_with_bonus,
                    NULL min_qualifier,
                    NULL for_every,
                    NULL will_earn,
                    NULL payout_cap_amount,
--                    NULL activity_description,
                    CASE WHEN COUNT(DISTINCT scp.activity_description) <= 1 THEN   --Bug 61875 04/27/2015
                    (SELECT activity_description  FROM ssi_contest_participant scp, gtt_node_and_below_users nab
    WHERE scp.ssi_contest_id = p_in_ssi_contest_id AND scp.user_id = nab.user_id AND ROWNUM <2)
    ELSE 'Varies by Participant' END   AS activity_description,
                   -- scp.objective_payout_description payout_description,
                    CASE WHEN COUNT(DISTINCT scp.objective_payout_description) <= 1 THEN   --Bug 61875 04/27/2015
                    (SELECT objective_payout_description  FROM ssi_contest_participant scp, gtt_node_and_below_users nab
    WHERE scp.ssi_contest_id = p_in_ssi_contest_id AND scp.user_id = nab.user_id AND ROWNUM <2)
    ELSE 'Varies by Participant' END   AS payout_description,
                    NULL payout_quantity,
                    sc.activity_measure_type
            FROM ssi_contest sc,
                 ssi_contest_pax_progress scpp,
                 ssi_contest_participant scp,
                 application_user au
           WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                 AND sc.ssi_contest_id = scp.ssi_contest_id
                 AND scp.user_id = au.user_id
                 AND au.is_active = 1
                 AND scp.user_id = scpp.user_id(+)
                 AND scP.ssi_contest_id = scpp.ssi_contest_id(+)
                --AND sc.last_progress_update_date IS NOT NULL --Bug # 60019
                AND sc.contest_type = 4
                AND EXISTS
                 (select   nab.user_id
                  from  gtt_node_and_below_users nab
                  where nab.user_id = scp.user_id )
        GROUP BY sc.ssi_contest_id,sc.goal_percentage,sc.contest_goal,sc.activity_measure_type--,scp.objective_payout_description
        UNION ALL
        SELECT ssi_contest_id,
                   ssi_contest_activity_id,
                   --goal_percentage/100*total_pax*maximum_activity goal,
                   goal_amount/total_pax*team_pax goal,--Bug # 60821
                   team_activity,
                   NULL objective_amount,
                   pax_achieved,
                   team_pax total_pax,--Java is using total_pax column to display the count.
                   Potential_payout,
                   Maximum_Payout,
                   Maximum_Payout_with_bonus,
                   min_qualifier,
                   for_every,
                   will_earn,
                   payout_cap_amount,
                   activity_description,payout_description,
                   payout_quantity,
                   activity_measure_type
                   FROM (
         SELECT sc.ssi_contest_id,
                            sca.ssi_contest_activity_id,
--                  (SELECT SUM(activity_amt) FROM ssi_contest_pax_progress WHERE ssi_contest_id = sc.ssi_contest_id AND ssi_contest_activity_id = sca.ssi_contest_activity_id) as team_activity, --05/19/2015 Bug 62260
                  (SELECT SUM(activity_amt) FROM ssi_contest_pax_progress scpp ,gtt_node_and_below_users nab WHERE ssi_contest_id = sc.ssi_contest_id AND ssi_contest_activity_id = sca.ssi_contest_activity_id  AND nab.user_id = scpp.user_id) as team_activity, --05/19/2015 Bug 62260
                 NULL pax_achieved,
                 (SELECT COUNT(1) FROM ssi_contest_participant scp,application_user au  WHERE scp.ssi_contest_id = p_in_ssi_contest_id AND
                 au.user_id = scp.user_id ANd au.is_active = 1) total_pax,
                 SUM(CASE WHEN scpp.activity_amt > sca.min_qualifier THEN
              FLOOR( (
                 CASE WHEN scpp.activity_amt <= (sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount) THEN scpp.activity_amt
                 ELSE  (sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount) END - sca.min_qualifier)/sca.increment_amount )* sca.payout_amount
               ELSE 0 END ) Potential_payout,
                 NULL Maximum_Payout,
                 NULL Maximum_Payout_with_bonus,
                    sca.min_qualifier,
                    sca.increment_amount for_every,
                    sca.payout_amount will_earn,
                    --sca.min_qualifier + (sca.payout_cap_amount /sca.payout_amount*sca.increment_amount) maximum_activity,
                    sca.min_qualifier + (FLOOR(sca.payout_cap_amount /sca.payout_amount)*sca.increment_amount) maximum_activity, --Bug # 60068
                    sca.payout_cap_amount payout_cap_amount,
                    sca.description activity_description
                    ,SUM(scpp.activity_amt) activity_amt,    --03/10/2015,
                    sca.payout_description,
              SUM (   CASE WHEN scpp.activity_amt > sca.min_qualifier THEN
              FLOOR( (
                 CASE WHEN scpp.activity_amt <= (sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount) THEN scpp.activity_amt
                 ELSE  (sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount) END - sca.min_qualifier)/sca.increment_amount )
               ELSE 0 END  ) payout_quantity,
               --COUNT(scp.ssi_contest_participant_id) team_pax,--Bug # 60821
                (SELECT
--                COUNT(1)   --04/13/2015
                  COUNT (DISTINCT nab.user_id)  --04/13/2015
                FROM ssi_contest_participant scp,application_user au,gtt_node_and_below_users nab  WHERE scp.ssi_contest_id = p_in_ssi_contest_id AND
                 au.user_id = scp.user_id ANd au.is_active = 1 AND scp.user_id = nab.user_id ) team_pax, --Bug # 60068
               sca.goal_amount,
               sc.activity_measure_type
            FROM ssi_contest sc,
                 ssi_contest_participant scp,
                 application_user au,
                 ssi_contest_activity sca,
                 ssi_contest_pax_progress scpp
           WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                 AND sc.ssi_contest_id = sca.ssi_contest_id
                 AND sca.ssi_contest_id = scp.ssi_contest_id
                 AND scp.user_id = au.user_id
                 AND au.is_active = 1
                 AND sca.ssi_contest_id = scpp.ssi_contest_id
                 AND sca.ssi_contest_activity_id = scpp.ssi_contest_activity_id
                 AND scpp.user_id = scp.user_id
                 AND EXISTS
                 (select   nab.user_id
                  from  gtt_node_and_below_users nab
                  where nab.user_id = scp.user_id )
        GROUP BY sc.ssi_contest_id,sca.goal_amount,sca.payout_cap_amount,sca.ssi_contest_activity_id,
                 sca.increment_amount,sca.payout_amount,sca.min_qualifier,sca.description,sca.payout_description,sc.activity_measure_type
        UNION ALL
        SELECT sc.ssi_contest_id,
                            sca.ssi_contest_activity_id,
                  (SELECT SUM(activity_amt) FROM ssi_contest_pax_progress WHERE ssi_contest_id = sc.ssi_contest_id AND ssi_contest_activity_id = sca.ssi_contest_activity_id) as team_activity,
                 NULL pax_achieved,
                 (SELECT COUNT(1) FROM ssi_contest_participant scp,application_user au  WHERE scp.ssi_contest_id = p_in_ssi_contest_id AND
                 au.user_id = scp.user_id ANd au.is_active = 1) total_pax,
                 NULL Potential_payout,
                 NULL Maximum_Payout,
                 NULL Maximum_Payout_with_bonus,
                    sca.min_qualifier,
                    sca.increment_amount for_every,
                    sca.payout_amount will_earn,
                    --sca.min_qualifier + (sca.payout_cap_amount /sca.payout_amount*sca.increment_amount) maximum_activity,
                    sca.min_qualifier + (FLOOR(sca.payout_cap_amount /sca.payout_amount)*sca.increment_amount) maximum_activity,--Bug # 60068
                    sca.payout_cap_amount payout_cap_amount,
                    sca.description activity_description,
                    0 activity_amt,    --03/10/2015,
                    sca.payout_description,
                    0 payout_quantity,
               COUNT(scp.ssi_contest_participant_id) team_pax,--Bug # 60821
               sca.goal_amount,
               sc.activity_measure_type
            FROM ssi_contest sc,
                 ssi_contest_participant scp,
                 application_user au,
                 ssi_contest_activity sca
           WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                 AND sc.ssi_contest_id = sca.ssi_contest_id
                 AND sca.ssi_contest_id = scp.ssi_contest_id
                 AND scp.user_id = au.user_id
                 AND au.is_active = 1
                 AND EXISTS
                 (select   nab.user_id
                  from  gtt_node_and_below_users nab
                  where nab.user_id = scp.user_id )
--                 AND NOT EXISTS (SELECT * FROM ssi_contest_pax_progress WHERE ssi_contest_activity_id = sca.ssi_contest_activity_id AND user_id = scp.user_id) --Bug # 61438   --Bug 60068 04/15/2015
--                 AND NOT EXISTS (SELECT * FROM ssi_contest_pax_progress WHERE ssi_contest_id = scp.ssi_contest_id AND ssi_contest_activity_id = sca.ssi_contest_activity_id AND user_id = scp.user_id)   --Bug 60068 04/15/2015
                   AND NOT EXISTS (SELECT * FROM ssi_contest_pax_progress WHERE ssi_contest_id = scp.ssi_contest_id AND ssi_contest_activity_id = sca.ssi_contest_activity_id )   --04/28/2015 Bug 61893
        GROUP BY sc.ssi_contest_id,sca.goal_amount,sca.payout_cap_amount,sca.ssi_contest_activity_id,
                 sca.increment_amount,sca.payout_amount,sca.min_qualifier,sca.description,sca.payout_description,sc.activity_measure_type))
        order by ssi_contest_id,ssi_contest_activity_id; -- 03/25/2015 Bug 60713

        OPEN p_out_siu_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_siu_level_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_sr_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_sr_payout_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

         OPEN p_out_sr_pax_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        ELSIF v_out_contest_type = 16 THEN --SIU contest type

        OPEN p_out_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_sr_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_sr_payout_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

         OPEN p_out_sr_pax_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_siu_ref_cursor FOR
        SELECT contest_goal/total_pax*team_pax contest_goal,--Bug # 60821         --04/13/2015

               sit_indv_baseline_type,
               activity,
               CASE WHEN NVL(activity,0) > contest_goal/total_pax*team_pax THEN 0
               ELSE contest_goal/total_pax*team_pax-NVL(activity,0)
               END to_go,
               total_payout,
               potential_payout,
               total_potential_payout,
               (total_potential_payout-total_payout) remaining_payout,
               FLOOR(total_payout/total_potential_payout*100)  perc_payout
               FROM (
                    SELECT contest_goal,
                           sit_indv_baseline_type,
                           SUM(activity_amt) activity,
                           SUM(level_payout) level_payout,
                           SUM(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                             ELSE bonus_payout END) bonus_payout,
                           SUM(level_payout+ NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                             ELSE bonus_payout END,0) ) total_payout,
                           SUM(total_potential_payout) total_potential_payout,
                           SUM(potential_payout) potential_payout,
                           (SELECT COUNT(1) FROM ssi_contest_participant scp,application_user au,gtt_node_and_below_users nab  WHERE scp.ssi_contest_id = p_in_ssi_contest_id AND
                 au.user_id = scp.user_id ANd au.is_active = 1 AND scp.user_id = nab.user_id) team_pax,--Bug # 60821
                 (SELECT COUNT(1) FROM ssi_contest_participant scp,application_user au  WHERE scp.ssi_contest_id = p_in_ssi_contest_id AND
                 au.user_id = scp.user_id ANd au.is_active = 1) total_pax--Bug # 60821
                    FROM (
                    SELECT contest_goal,ssi_contest_id,sit_indv_baseline_type,user_id,activity_amt,level_payout,bonus_payout,sit_bonus_cap,
                    (SELECT payout_amount FROM (SELECT payout_amount,RANK() OVER (PARTITION BY ssi_contest_id ORDER BY  payout_amount DESC,sequence_number DESC ) as rec_rank FROM ssi_contest_level scl WHERE ssi_contest_id = p_in_ssi_contest_id)
WHERE rec_rank =1) + NVL(sit_bonus_cap,0) total_potential_payout,
                    (SELECT payout_amount FROM (SELECT payout_amount,RANK() OVER (PARTITION BY ssi_contest_id ORDER BY  payout_amount DESC,sequence_number DESC ) as rec_rank FROM ssi_contest_level scl WHERE ssi_contest_id = p_in_ssi_contest_id)
WHERE rec_rank =1) potential_payout FROM (
                    SELECT DISTINCT contest_goal,ssi_contest_id,sit_indv_baseline_type,user_id,activity_amt,level_payout,CASE WHEN max_level = level_completed THEN bonus_payout ELSE 0 END bonus_payout,sit_bonus_cap
                    FROM (
                      SELECT sc.ssi_contest_id,
--                                   CASE WHEN sc.sit_indv_baseline_type = 'no' THEN MAX(scl.goal_amount) OVER (PARTITION BY scl.ssi_contest_id)
--                                WHEN sc.sit_indv_baseline_type = 'percent' THEN scp.siu_baseline_amount *(100+MAX(scl.goal_amount) OVER (PARTITION BY scl.ssi_contest_id))/100
--                               WHEN sc.sit_indv_baseline_type = 'currency' THEN scp.siu_baseline_amount + MAX(scl.goal_amount) OVER (PARTITION BY scl.ssi_contest_id)
--                            END contest_goal,   --02/10/2015
                            sc.contest_goal,
                            sc.sit_indv_baseline_type,
                            scp.user_id,
                            scpp.activity_amt,
                            CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END level_completed,
                            --sc.goal_percentage,
                            RANK() OVER (PARTITION BY scp.user_id ORDER BY CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank,
                            CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.payout_amount
                                 WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.payout_amount
                                 WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.payout_amount
                                 ELSE 0
                            END level_payout,
                            CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment) * sit_bonus_payout
                                 WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment) * sit_bonus_payout
                                 WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment ) * sit_bonus_payout

                                 ELSE 0
                            END bonus_payout,
               (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = p_in_ssi_contest_id) max_level,
                            sit_bonus_cap
                      FROM ssi_contest sc,
                           ssi_contest_level scl,
                           ssi_contest_participant scp,
                           ssi_contest_pax_progress scpp,
                           application_user au
                     WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                           AND sc.ssi_contest_id = scl.ssi_contest_id
                           AND sc.ssi_contest_id = scp.ssi_contest_id
                           AND scp.user_id = au.user_id
                           AND au.is_active = 1
                           AND scp.user_id = scpp.user_id(+)
                           AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                           AND EXISTS
                                             (select   nab.user_id
                                              from  gtt_node_and_below_users nab
                                              where nab.user_id = scp.user_id
                                             )
                           )
                            WHERE rec_rank = 1
                            ) )
                           GROUP BY contest_goal,sit_indv_baseline_type,sit_bonus_cap
                         );

       OPEN p_out_siu_level_ref_cursor FOR
       WITH pax_level_completed AS   --04/13/2015
        (
         SELECT DISTINCT user_id,level_completed,bonus_payout FROM  (
         SELECT user_id,CASE WHEN sequence_number = max_level AND level_completed = -1 THEN -1 --Bug # 61255
                                          WHEN sequence_number <> max_level AND level_completed = -1 THEN sequence_number
                                          ELSE level_completed END level_completed,rec_rank
                                          ,CASE WHEN sequence_number = max_level THEN bonus_payout ELSE 0 END bonus_payout
                                           FROM (
         SELECT scp.user_id,
         CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount + sit_bonus_increment THEN -1
                  WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
                  WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
                  WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END level_completed,
        RANK() OVER (PARTITION BY scp.user_id ORDER BY CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
                  WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
                  WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank,
                                 (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = p_in_ssi_contest_id) max_level ,
                                 scl.sequence_number
                        ,CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN sit_bonus_cap
                             WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment)   * sit_bonus_payout
                             WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN sit_bonus_cap
                             WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment)* sit_bonus_payout
                             WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap
                             WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment) * sit_bonus_payout
                             ELSE 0
                        END bonus_payout
          FROM ssi_contest sc,
               ssi_contest_level scl,
               ssi_contest_participant scp,
               ssi_contest_pax_progress scpp,
               application_user au
         WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
               AND sc.ssi_contest_id = scl.ssi_contest_id
               AND sc.ssi_contest_id = scp.ssi_contest_id
               AND scp.user_id = au.user_id
               AND au.is_active = 1
               AND scp.user_id = scpp.user_id(+)
               AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
               AND EXISTS
                         (select   nab.user_id
                          from  gtt_node_and_below_users nab
                          where nab.user_id = scp.user_id
                         ))
               )
               WHERE rec_rank = 1
                )
       SELECT sequence_number,goal_amount,payout_amount,payout_description
       , CASE WHEN sequence_number = -1 THEN bonus_cnt ELSE pax_count END pax_count  --04/13/2015
       FROM (SELECT scl.sequence_number,scl.goal_amount,scl.payout_amount,scl.payout_description,COUNT(plc.user_id) pax_count
               ,CASE WHEN scl.sequence_number = -1 THEN (select count(p.user_id) from pax_level_completed p where p.bonus_payout > 0) END bonus_cnt -- Bug 62029 05/05/2015
               FROM (SELECT * FROM pax_level_completed WHERE bonus_payout = 0) plc , --Bug # 62070
               (SELECT sequence_number, scl.goal_amount,payout_amount,scl.payout_description FROM ssi_contest_level scl,ssi_contest sc  --Bug # 60717 03/24/2015
               WHERE scl.ssi_contest_id = p_in_ssi_contest_id AND scl.ssi_contest_id = sc.ssi_contest_id
               UNION ALL
               SELECT 0,NULL,NULL,NULL FROM DUAL
               UNION
               SELECT -1,NULL,NULL,NULL FROM DUAL) scl
              WHERE scl.sequence_number = plc.level_completed(+)
               GROUP BY scl.sequence_number,scl.goal_amount,scl.payout_amount,scl.payout_description);

-- WITH pax_level_completed AS
--(
-- SELECT DISTINCT user_id,level_completed FROM  (
-- SELECT user_id,CASE WHEN sequence_number = max_level AND level_completed = -1 THEN -1 --Bug # 61255
--                                  WHEN sequence_number <> max_level AND level_completed = -1 THEN sequence_number
--                                  ELSE level_completed END level_completed,rec_rank FROM (
-- SELECT scp.user_id,
-- CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount + sit_bonus_increment THEN -1
--          WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
--          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
--          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END level_completed,
--RANK() OVER (PARTITION BY scp.user_id ORDER BY CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
--          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
--          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank,
--                         (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = p_in_ssi_contest_id) max_level ,
--                         scl.sequence_number
--  FROM ssi_contest sc,
--       ssi_contest_level scl,
--       ssi_contest_participant scp,
--       ssi_contest_pax_progress scpp,
--       application_user au
-- WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
--       AND sc.ssi_contest_id = scl.ssi_contest_id
--       AND sc.ssi_contest_id = scp.ssi_contest_id
--       AND scp.user_id = au.user_id
--       AND au.is_active = 1
--       AND scp.user_id = scpp.user_id(+)
--       AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
--       AND EXISTS
--                 (select   nab.user_id
--                  from  gtt_node_and_below_users nab
--                  where nab.user_id = scp.user_id
--                 ))
--       )
--       WHERE rec_rank = 1
--        )
--       SELECT scl.sequence_number,scl.goal_amount,scl.payout_amount,scl.payout_description,COUNT(plc.user_id) pax_count  FROM pax_level_completed plc ,
--       (SELECT sequence_number, scl.goal_amount,payout_amount,scl.payout_description FROM ssi_contest_level scl,ssi_contest sc  --Bug # 60717 03/24/2015
--       WHERE scl.ssi_contest_id = p_in_ssi_contest_id AND scl.ssi_contest_id = sc.ssi_contest_id
--       UNION ALL
--       SELECT 0,NULL,NULL,NULL FROM DUAL
--       UNION
--       SELECT -1,NULL,NULL,NULL FROM DUAL) scl
--      WHERE scl.sequence_number = plc.level_completed(+)
--       GROUP BY scl.sequence_number,scl.goal_amount,scl.payout_amount,scl.payout_description;

       ELSE

        IF v_out_contest_type = 8 THEN
      SELECT NVL(language_id,(SELECT STRING_VAL FROM os_propertyset WHERE entity_name = 'default.language')) INTO v_locale FROM application_user WHERE user_id = p_in_user_id;
      END IF;

      DELETE FROM temp_table_session;

 INSERT INTO temp_table_session
                SELECT cav.asset_code,
                          MAX (DECODE (cav.key, 'NAME', cav.cms_value, NULL)) AS cms_name,
                         MAX (DECODE (cav.key, 'EARNED_IMAGE_SMALL', cav.cms_value, NULL)) AS cms_code
                       FROM vw_cms_asset_value cav
                       WHERE asset_code='promotion.badge'
                       AND locale = v_locale
                   GROUP BY cav.asset_code,
                            cav.locale,
                            cav.asset_id,
                            cav.content_key_id,
                            cav.content_id;

        OPEN p_out_siu_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_siu_level_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        OPEN p_out_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;

        IF v_status = 'finalize_results' THEN    --05/28/2015  

        OPEN p_out_sr_ref_cursor FOR    --05/28/2015   
              SELECT contest_goal,
                     SUM (activity_amt) activity_amt,
                     CASE
                        WHEN contest_goal >= SUM (NVL (activity_amt, 0))
                        THEN
                           (contest_goal - SUM (NVL (activity_amt, 0)))
                        ELSE
                           0
                     END
                        To_go,
                     (SELECT SUM (payout_amount)
                        FROM ssi_contest_pax_payout
                       WHERE ssi_contest_id = p_in_ssi_contest_id)
                        maximum_points,
                     stack_rank_qualifier_amount,
                     SUM (payout_amount) potential_payout,
                     (SELECT COUNT (user_id)
                        FROM ssi_contest_participant
                       WHERE ssi_contest_id = p_in_ssi_contest_id)
                        total_pax                                  --Bug #61354 04/21/2015
                                 ,
                     (SELECT COUNT (user_id)
                        FROM ssi_contest_pax_payout
                       WHERE ssi_contest_id = p_in_ssi_contest_id)
                        pax_achieved                               --Bug #61354 04/21/2015
                FROM (SELECT sc2.contest_goal,
                             sc2.activity_amt,
                             sc2.stack_rank_qualifier_amount,
                             CASE
                                WHEN     sc2.stack_rank_qualifier_amount IS NOT NULL
                                     AND sc2.activity_amt < sc2.stack_rank_qualifier_amount
                                THEN
                                   0
                                ELSE
                                   NVL (scsp.payout_amount, 0)
                             END
                                payout_amount
                        FROM (SELECT scp.ssi_contest_id,
                                     scp.user_id,
                                     scpsr.stack_rank_position,
                                     scpp.activity_amt,
                                     sc.contest_goal,
                                     sc.stack_rank_qualifier_amount
                                FROM ssi_contest_pax_progress scpp,
                                     ssi_contest_pax_stack_rank scpsr,
                                     ssi_contest sc,
                                     application_user au,
                                     ssi_contest_participant scp
                               WHERE     scp.ssi_contest_id = p_in_ssi_contest_id
                                     AND scp.ssi_contest_id = scpsr.ssi_contest_id(+)
                                     AND scp.user_id = scpsr.user_id(+)
                                     AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                                     AND scp.user_id = scpp.user_id(+)
                                     AND scp.user_id = au.user_id
                                     AND au.is_active = 1
                                     AND sc.ssi_contest_id = scp.ssi_contest_id
                                     AND EXISTS
                                         (select   nab.user_id
                                          from  gtt_node_and_below_users nab
                                          where nab.user_id = scp.user_id)) sc2,
                             ssi_contest_pax_payout scsp
                       WHERE     scsp.ssi_contest_id(+) = sc2.ssi_contest_id
                             AND sc2.user_id = scsp.user_id (+))
            GROUP BY contest_goal, stack_rank_qualifier_amount;      
      
        ELSE 

        OPEN p_out_sr_ref_cursor FOR  --03/31/2015   
         SELECT contest_goal,
                SUM (activity_amt) activity_amt,  
                CASE WHEN contest_goal >= SUM (NVL (activity_amt, 0)) THEN  (contest_goal - SUM (NVL (activity_amt, 0)))
                     ELSE 0 
                END To_go,
                (SELECT SUM(payout_amount) FROM ssi_contest_sr_payout WHERE ssi_contest_id = p_in_ssi_contest_id )  maximum_points,
                 stack_rank_qualifier_amount,
                 SUM(paymount_amount) potential_payout
                 ,(SELECT COUNT (user_id) FROM ssi_contest_participant where ssi_contest_id = p_in_ssi_contest_id) total_pax  --Bug #61354 04/21/2015
                 ,(SELECT COUNT (user_id) FROM ssi_contest_pax_payout WHERE ssi_contest_id = p_in_ssi_contest_id)  pax_achieved --Bug #61354 04/21/2015
                 FROM (
                        SELECT sc2.contest_goal,
                               sc2.activity_amt,
                               sc2.stack_rank_qualifier_amount,
                               CASE WHEN sc2.stack_rank_qualifier_amount  IS NOT NULL AND sc2.activity_amt < sc2.stack_rank_qualifier_amount THEN 0 
                                    ELSE NVL(scsp.payout_amount,0) 
                               END paymount_amount
                         FROM (
                                SELECT scp.ssi_contest_id,scp.user_id,scpsr.stack_rank_position,scpp.activity_amt,sc.contest_goal,
                                       sc.stack_rank_qualifier_amount 
                                       FROM
                                        ssi_contest_pax_progress scpp,
                                        ssi_contest_pax_stack_rank scpsr,
                                        ssi_contest sc,
                                        application_user au, 
                                        ssi_contest_participant scp
                 WHERE scp.ssi_contest_id  = p_in_ssi_contest_id
                         AND scp.ssi_contest_id = scpsr.ssi_contest_id (+)
                          AND scp.user_id = scpsr.user_id (+)
                          AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                          AND scp.user_id = scpp.user_id (+)
                            AND scp.user_id = au.user_id
                            AND au.is_active = 1
                            AND sc.ssi_contest_id = scp.ssi_contest_id
                            AND EXISTS
                             (select   nab.user_id
                              from  gtt_node_and_below_users nab
                              where nab.user_id = scp.user_id)) sc2,
                            ssi_contest_sr_payout scsp
                            WHERE scsp.ssi_contest_id(+) = sc2.ssi_contest_id
                            AND    sc2.stack_rank_position = scsp.rank_position(+)) GROUP BY contest_goal,stack_rank_qualifier_amount;
        END IF;
        --  SELECT contest_goal,      --03/31/2015
        --            activity_amt,
        --            CASE WHEN NVL(activity_amt,0) > contest_goal THEN 0
        --               ELSE contest_goal-NVL(activity_amt,0)
        --               END to_go,
        --               maximum_points,
        --               stack_rank_qualifier_amount FROM (
        --SELECT sc.contest_goal,
        ----         SUM (scpp.activity_amt) activity_amt,
        --         (SELECT SUM(activity_amt) FROM ssi_contest_pax_progress WHERE ssi_contest_id = p_in_ssi_contest_id) activity_amt,
        ----         (sc.contest_goal - SUM (NVL (scpp.activity_amt, 0))) To_go,
        --         scsp.maximum_points,   --03/06/2015 --03/27/2015 Bug # 60074
        --         sc.stack_rank_qualifier_amount
        --    FROM ssi_contest sc,
        --         ssi_contest_participant scp,--Bug # 60183
        --         application_user au,
        ----         ssi_contest_sr_payout scsp   --03/06/2015
        --         (select ssi_contest_id, sum(payout_amount) maximum_points
        --         from ssi_contest_sr_payout where ssi_contest_id = p_in_ssi_contest_id group by ssi_contest_id )scsp  --03/06/2015
        --   WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
        --         AND sc.ssi_contest_id = scp.ssi_contest_id
        --         AND sc.ssi_contest_id = scsp.ssi_contest_id
        --         AND scp.user_id = au.user_id
        --         AND au.is_active = 1
        --         AND EXISTS
        --                 (select   nab.user_id
        --                  from  gtt_node_and_below_users nab
        --                  where nab.user_id = scp.user_id
        --                 )
        --GROUP BY sc.contest_goal,sc.stack_rank_qualifier_amount,scsp.maximum_points);

        OPEN p_out_sr_payout_ref_cursor FOR
SELECT rank_position,
       payout_amount,
       payout_desc,
       br.badge_rule_id,
       NVL((SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale = v_locale),(SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale ='en_US')) badge_name,--Bug # 62924
       cms_badge.image_small_URL badge_image
  FROM ssi_contest_sr_payout sc,
       badge_rule br,
       (SELECT asset_code, cms_name, cms_code image_small_URL
          FROM temp_table_session) cms_badge
 WHERE     ssi_contest_id = p_in_ssi_contest_id
       AND sc.badge_rule_id = br.badge_rule_id(+)
       AND br.cm_asset_key = cms_badge.cms_name(+);

                     prc_execution_log_entry ('PRC_SSI_CONTEST_PROGRESS',1,'INFO',p_in_ssi_contest_id|| ' ' || SQLERRM,NULL);

        OPEN p_out_sr_pax_ref_cursor FOR
          SELECT rank_position,au.user_id,au.first_name,au.last_name, p.avatar_small avatar_url,scpp.activity_amt,1 as is_teammember,sc.payout_amount,sc.payout_desc payout_description
          FROM ssi_contest_sr_payout sc,ssi_contest scon,
         application_user au, participant p ,ssi_contest_pax_stack_rank scpsr,ssi_contest_pax_progress scpp
          WHERE sc.ssi_contest_id = p_in_ssi_contest_id
          AND sc.ssi_contest_id = scon.ssi_contest_id
          AND sc.ssi_contest_id = scpsr.ssi_contest_id
          AND sc.rank_position = scpsr.stack_rank_position
          AND sc.ssi_contest_id = scpp.ssi_contest_id
          AND scpsr.user_id = scpp.user_id
          AND scpsr.user_id = scpsr.user_id
          AND scpsr.user_id = p.user_id
          AND scon.status <> 'finalize_results'
          AND EXISTS
                 (select   nab.user_id
                  from  gtt_node_and_below_users nab
                  where nab.user_id = scpp.user_id
                 )
          AND scpsr.user_id = au.user_id
          AND au.is_active = 1
          AND scpp.activity_amt >= NVL(scon.stack_rank_qualifier_amount,0)--Checking minimum qualifier 04/08/2015 Bug 60970
          UNION ALL
          SELECT rank_position,au.user_id,au.first_name,au.last_name, p.avatar_small avatar_url,scpp.activity_amt,0 as is_teammember,sc.payout_amount,sc.payout_desc payout_description
          FROM ssi_contest_sr_payout sc,ssi_contest scon,
         application_user au, participant p ,ssi_contest_pax_stack_rank scpsr,ssi_contest_pax_progress scpp
          WHERE sc.ssi_contest_id = p_in_ssi_contest_id
          AND sc.ssi_contest_id = scon.ssi_contest_id
          AND sc.ssi_contest_id = scpsr.ssi_contest_id
          AND sc.rank_position = scpsr.stack_rank_position
          AND sc.ssi_contest_id = scpp.ssi_contest_id
          AND scpsr.user_id = scpp.user_id
          AND scpsr.user_id = scpsr.user_id
          AND scpsr.user_id = p.user_id
          AND scon.status <> 'finalize_results'
          AND NOT EXISTS
                 (select   nab.user_id
                  from  gtt_node_and_below_users nab
                  where nab.user_id = scpp.user_id
                 )
          AND scpsr.user_id = au.user_id
          AND au.is_active = 1
          AND scpp.activity_amt >= NVL(scon.stack_rank_qualifier_amount,0)--Checking minimum qualifier 04/08/2015 Bug 60970
          UNION ALL
          SELECT scpsr.stack_rank_position rank_position,au.user_id,au.first_name,au.last_name, p.avatar_small avatar_url,scpp.activity_amt, 0 AS is_teammember,sc.payout_amount,sc.payout_description
          FROM ssi_contest_pax_payout sc,ssi_contest scon,
         application_user au, participant p ,ssi_contest_pax_stack_rank scpsr,ssi_contest_pax_progress scpp
          WHERE sc.ssi_contest_id = p_in_ssi_contest_id
          AND sc.ssi_contest_id = scon.ssi_contest_id
          AND sc.user_id = scpsr.user_id
          AND sc.ssi_contest_id = scpsr.ssi_contest_id
          AND sc.ssi_contest_id = scpp.ssi_contest_id
          AND sc.user_id = scpp.user_id
          AND sc.user_id = au.user_id
          AND au.is_active =1
          AND au.user_id = p.user_id
          AND scon.status = 'finalize_results'
          AND NOT EXISTS
                 (select   nab.user_id
                  from  gtt_node_and_below_users nab
                  where nab.user_id = scpp.user_id
                 )
           UNION ALL
           SELECT scpsr.stack_rank_position rank_position,au.user_id,au.first_name,au.last_name, p.avatar_small avatar_url,scpp.activity_amt, 1 AS is_teammember,sc.payout_amount,sc.payout_description
          FROM ssi_contest_pax_payout sc,ssi_contest scon,
         application_user au, participant p ,ssi_contest_pax_stack_rank scpsr,ssi_contest_pax_progress scpp
          WHERE sc.ssi_contest_id = p_in_ssi_contest_id
          AND sc.ssi_contest_id = scon.ssi_contest_id
          AND sc.user_id = scpsr.user_id
          AND sc.ssi_contest_id = scpsr.ssi_contest_id
          AND sc.ssi_contest_id = scpp.ssi_contest_id
          AND sc.user_id = scpp.user_id
          AND sc.user_id = au.user_id
          AND au.is_active =1
          AND au.user_id = p.user_id
          AND scon.status = 'finalize_results'
          AND EXISTS
                 (select   nab.user_id
                  from  gtt_node_and_below_users nab
                  where nab.user_id = scpp.user_id
                 );

  END IF;
   OPEN p_out_stackrank_cursor FOR --Bug # 60609
   SELECT ssi_contest_activity_id,stack_rank,
         participant_id,first_name,last_name,avatar,
         pax_activity,payout_amount, payout_desc FROM (
        SELECT NULL ssi_contest_activity_id,scpsr.stack_rank_position stack_rank,
         au.user_id participant_id,au.first_name,au.last_name,p.avatar_small avatar,
         scpp.activity_amt pax_activity,NULL payout_amount, NULL payout_desc
    FROM ssi_contest_pax_stack_rank scpsr,
         ssi_contest_pax_progress scpp,
         application_user au,
         ssi_contest sc,
         participant p
   WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
         AND sc.contest_type IN (4,16)
         AND scpsr.ssi_contest_id = sc.ssi_contest_id
         AND scpsr.user_id = scpp.user_id
         AND scpsr.ssi_contest_id = scpp.ssi_contest_id
         AND scpsr.user_id = au.user_id
         AND au.user_id = p.user_id
         AND au.is_active = 1
         AND EXISTS
         (select   nab.user_id
          from  gtt_node_and_below_users nab
          where nab.user_id = p.user_id
        ) ORDER BY stack_rank) WHERE ROWNUM<6
         UNION ALL
         SELECT ssi_contest_activity_id,stack_rank,
         participant_id,first_name,last_name,avatar,
         pax_activity,payout_amount, payout_desc FROM (
SELECT sca.ssi_contest_activity_id,scpsr.stack_rank_position stack_rank,
         au.user_id participant_id,au.first_name,au.last_name,p.avatar_small avatar,
         scpp.activity_amt pax_activity, NULL payout_amount, NULL payout_desc,RANK() OVER (PARTITION BY sca.ssi_contest_activity_id ORDER BY scpsr.stack_rank_position,au.last_name,au.user_id ) as rec_rank --Bug # 60738 03/25/2015
    FROM ssi_contest_pax_stack_rank scpsr,
         ssi_contest_pax_progress scpp,
         application_user au,
         ssi_contest_activity sca,
         participant p
   WHERE     scpsr.ssi_contest_id = p_in_ssi_contest_id
         AND scpsr.user_id = scpp.user_id
         AND scpsr.ssi_contest_id = scpp.ssi_contest_id
         AND scpsr.ssi_contest_activity_id = scpp.ssi_contest_activity_id
         AND scpsr.user_id = au.user_id
         AND scpsr.ssi_contest_id = sca.ssi_contest_id
         AND scpsr.ssi_contest_activity_id = sca.ssi_contest_activity_id
         AND au.user_id = p.user_id
         AND au.is_active = 1
         AND EXISTS
         (select   nab.user_id
          from  gtt_node_and_below_users nab
          where nab.user_id = p.user_id
        ) ORDER BY stack_rank,last_name
         ) WHERE rec_rank <=5
   UNION ALL --Stack rank contest
         SELECT NULL ssi_contest_activity_id,scpsr.stack_rank_position stack_rank,
         au.user_id participant_id,au.first_name,au.last_name,p.avatar_small avatar,
         scpp.activity_amt pax_activity, CASE WHEN sc.contest_end_date < SYSDATE THEN ssifp.payout_amount ELSE NULL END payout_amount,  --05/28/2015
         CASE WHEN sc.contest_end_date < SYSDATE THEN scsp.payout_desc ELSE NULL END payout_desc
    FROM ssi_contest_pax_stack_rank scpsr,
         ssi_contest_pax_progress scpp,
         application_user au,
         participant p,
         ssi_contest_sr_payout scsp,
         ssi_contest sc
         ,ssi_contest_pax_payout ssifp  --05/28/2015         
   WHERE     scpsr.ssi_contest_id = p_in_ssi_contest_id
         AND scpsr.user_id = scpp.user_id
         AND scpsr.ssi_contest_id = scpp.ssi_contest_id
         AND scpsr.user_id = au.user_id
         AND au.user_id = p.user_id
         AND au.is_active = 1
         AND scsp.rank_position = scpsr.stack_rank_position
         AND scsp.ssi_contest_id = scpsr.ssi_contest_id
         AND scsp.ssi_contest_id = sc.ssi_contest_id
         AND ssifp.ssi_contest_id = scpsr.ssi_contest_id   --05/28/2015
         AND ssifp.user_id = scpp.user_id   --05/28/2015
         AND EXISTS
         (select   nab.user_id
          from  gtt_node_and_below_users nab
          where nab.user_id = scpp.user_id
        )
ORDER BY stack_rank;

END IF;        --02/04/2015
     p_out_contest_type  := v_out_contest_type;
      p_out_return_code :=0;

      EXCEPTION WHEN OTHERS THEN
       p_out_contest_type  := NULL;
      p_out_return_code :=99;
       prc_execution_log_entry ('PRC_SSI_CONTEST_PROGRESS',1,'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| SQLERRM,NULL);
      END prc_ssi_contest_progress;

      PROCEDURE prc_ssi_contest_pax_progress (p_in_ssi_contest_id     IN     NUMBER,
      p_in_user_id             IN NUMBER,
      p_out_contest_type  OUT NUMBER,
      p_out_return_code    OUT NUMBER,
      p_out_obj_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_ref_cursor OUT SYS_REFCURSOR,
      p_out_DTGT_cursor OUT SYS_REFCURSOR,
      p_out_sr_ref_cursor OUT SYS_REFCURSOR,
      p_out_sr_payout_ref_cursor OUT SYS_REFCURSOR,
      p_out_sr_pax_ref_cursor OUT SYS_REFCURSOR)
      IS
       /***********************************************************************************
      Purpose:  Procedure to provide the detail pax progress for a contest.

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
      Swati              05/14/2015     Bug 62176 - Stack rank details page - add activity amount to the winners stack rank section
      Chidamba           09/25/2017     G6-3020 - Added additional output column in cursor p_out_obj_ref_cursor 
   ************************************************************************************/ 
      v_out_contest_type NUMBER;
      v_stack_rank          NUMBER;
      v_progress_uploaded NUMBER;
      v_locale           VARCHAR2(10);
      v_status           VARCHAR2(20);
      
      BEGIN

      SELECT contest_type,include_stack_rank,CASE WHEN last_progress_update_date IS NOT NULL THEN 1
                                                                    ELSE 0 END
                                                                    ,status
                   INTO v_out_contest_type,v_stack_rank,v_progress_uploaded,v_status FROM ssi_contest WHERE ssi_contest_id = p_in_ssi_contest_id;

      IF v_out_contest_type IN (8,16) THEN
      SELECT NVL(language_id,(SELECT STRING_VAL FROM os_propertyset WHERE entity_name = 'default.language')) INTO v_locale FROM application_user WHERE user_id = p_in_user_id;
      END IF;

      IF v_out_contest_type = 4 THEN
      OPEN p_out_obj_ref_cursor FOR
      SELECT sc.ssi_contest_id,
              scp.objective_amount,
              scpp.activity_amt,
              CASE WHEN NVL(scpp.activity_amt,0) <=scp.objective_amount THEN (scp.objective_amount - NVL(scpp.activity_amt,0))
              ELSE 0 END to_go,
               FLOOR(NVL(scpp.activity_amt,0) / scp.objective_amount * 100) AS perc_achieved,
               CASE WHEN scpp.activity_amt >= scp.objective_amount AND sc.include_bonus = 0 THEN objective_payout
                        WHEN scpp.activity_amt >= scp.objective_amount AND sc.include_bonus = 1 AND
                             (scpp.activity_amt - scp.objective_amount)* scp.objective_bonus_payout/ scp.objective_bonus_increment  >= objective_bonus_cap THEN objective_payout + objective_bonus_cap
                        WHEN scpp.activity_amt >= scp.objective_amount AND sc.include_bonus = 1 AND
                             (scpp.activity_amt - scp.objective_amount)* scp.objective_bonus_payout/ scp.objective_bonus_increment  < objective_bonus_cap THEN
                             objective_payout + FLOOR((scpp.activity_amt - scp.objective_amount)/scp.objective_bonus_increment)* scp.objective_bonus_payout
                             ELSE 0 END potential_payout, --Bug # 60138
             CASE WHEN sc.include_bonus = 0 THEN NULL
                        WHEN scpp.activity_amt >= scp.objective_amount AND sc.include_bonus = 1 AND
                             (scpp.activity_amt - scp.objective_amount)* scp.objective_bonus_payout/ scp.objective_bonus_increment  >= objective_bonus_cap THEN objective_bonus_cap
                        WHEN scpp.activity_amt >= scp.objective_amount AND sc.include_bonus = 1 AND
                             (scpp.activity_amt - scp.objective_amount)* scp.objective_bonus_payout/ scp.objective_bonus_increment  < objective_bonus_cap THEN
                             FLOOR((scpp.activity_amt - scp.objective_amount)/scp.objective_bonus_increment)* scp.objective_bonus_payout
                             ELSE 0 END bonus_payout, --Bug # 60156
          scp.objective_payout,
          scp.objective_bonus_payout,
          scp.objective_bonus_increment,
          scpsr.stack_rank_position stack_rank,
          (SELECT COUNT(1) FROM  ssi_contest_participant  WHERE ssi_contest_id = p_in_ssi_contest_id) total_pax,
          sc.last_progress_update_date,
           p.avatar_small avatar_url,
             CASE WHEN sc.include_stack_rank = 1 THEN sc.activity_description
                    ELSE scp.activity_description END activity_description  --Bug # 61347
           ,scp.objective_payout_description   --02/24/2015
           ,scp.objective_bonus_cap             --09/25/2017 G6-3020
            FROM ssi_contest sc,
                 ssi_contest_pax_progress scpp,
                 ssi_contest_participant scp,
                 ssi_contest_pax_stack_rank scpsr,
                 participant p
           WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                 AND scp.user_id = p_in_user_id
                 AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                 AND sc.ssi_contest_id = scp.ssi_contest_id
                 AND scp.user_id = scpp.user_id(+)
                 AND scp.ssi_contest_id = scpsr.ssi_contest_id(+)
                 AND scp.user_id = scpsr.user_id(+)
                 AND scp.user_id = p.user_id
                 AND p.status = 'active';

               OPEN p_out_DTGT_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;
               OPEN p_out_siu_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;
               OPEN p_out_sr_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;
               OPEN p_out_sr_payout_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;
               OPEN p_out_sr_pax_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;

        ELSIF v_out_contest_type = 2 THEN

               OPEN p_out_obj_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;
               OPEN p_out_siu_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;
               OPEN p_out_sr_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;
               OPEN p_out_sr_payout_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;
               OPEN p_out_sr_pax_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;

      OPEN p_out_DTGT_cursor FOR
      SELECT ssi_contest_id,
            ssi_contest_activity_id,
            description,
            goal,
            activity_amt progress,
            FLOOR(activity_amt /goal*100) perc_of_progress,
            min_qualifier,
            increment_amount for_every,
            payout_amount will_earn,
            payout_cap_amount,
           CASE WHEN activity_amt > min_qualifier THEN
           FLOOR ((
           CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt
           ELSE  maximum_payout_activity END- min_qualifier)/increment_amount) * payout_amount
           ELSE 0 END payout_value,
           total_pax,
           avatar_url,
           stack_rank,
           payout_description,
         CASE WHEN activity_amt > min_qualifier THEN
              FLOOR( (
                 CASE WHEN activity_amt <= (min_qualifier + payout_cap_amount/payout_amount*increment_amount) THEN activity_amt
                 ELSE  (min_qualifier + payout_cap_amount/payout_amount*increment_amount) END - min_qualifier)/increment_amount )
               ELSE 0 END payout_quantity
           FROM (
SELECT sc.ssi_contest_id,
       sca.ssi_contest_activity_id,
       sca.description,
       sca.goal_amount goal,
       (SELECT activity_amt
          FROM ssi_contest_pax_progress scpp
         WHERE     scpp.ssi_contest_activity_id = sca.ssi_contest_activity_id
               AND scpp.user_id = scp.user_id) activity_amt,
         sca.increment_amount,
         sca.payout_amount,
         sca.min_qualifier,
         sca.payout_cap_amount,
         (SELECT COUNT(1) FROM ssi_contest_participant WHERE ssi_contest_id = p_in_ssi_contest_id) total_pax,
         (SELECT STACK_RANK_POSITION FROM ssi_contest_pax_stack_rank WHERE ssi_contest_activity_id = sca.ssi_contest_activity_id AND user_id = p_in_user_id) stack_rank,
         sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount AS maximum_payout_activity,
         p.avatar_small avatar_url,
         sca.payout_description
  FROM ssi_contest sc, ssi_contest_activity sca, ssi_contest_participant scp,participant p
 WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
 AND scp.user_id = p_in_user_id
       AND sc.ssi_contest_id = sca.ssi_contest_id
       AND sc.ssi_contest_id = scp.ssi_contest_id
       AND scp.user_id = p.user_id
       AND p.status = 'active') ORDER BY ssi_contest_activity_id ASC;

       ELSIF v_out_contest_type = 16 THEN
       DELETE FROM temp_table_session;

       INSERT INTO temp_table_session
                SELECT cav.asset_code,
                          MAX (DECODE (cav.key, 'NAME', cav.cms_value, NULL)) AS cms_name,
                         MAX (DECODE (cav.key, 'EARNED_IMAGE_SMALL', cav.cms_value, NULL)) AS cms_code
                       FROM vw_cms_asset_value cav
                       WHERE asset_code='promotion.badge'
                       AND locale = v_locale
                   GROUP BY cav.asset_code,
                            cav.locale,
                            cav.asset_id,
                            cav.content_key_id,
                            cav.content_id;

               OPEN p_out_DTGT_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;
               OPEN p_out_obj_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;
               OPEN p_out_sr_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;
               OPEN p_out_sr_payout_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;
               OPEN p_out_sr_pax_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;


                OPEN p_out_siu_ref_cursor FOR
                SELECT ssi_contest_id,user_id,progress,sequence_number,payout_amount,goal_amount,level_completed,is_current_level,goal_perc,remaining,
siu_baseline_amount,avatar_url,total_pax,stack_rank,badge_rule_id,badge_name,badge_image,CASE WHEN level_completed  = max_level THEN bonus_payout
 ELSE 0
 END bonus_payout,payout_description FROM (
SELECT sc.ssi_contest_id,scp.user_id,scpp.activity_amt progress, scl.sequence_number,
scl.payout_amount,
CASE WHEN sc.sit_indv_baseline_type = 'no'THEN scl.goal_amount
         WHEN sc.sit_indv_baseline_type = 'percent' THEN scp.siu_baseline_amount*(100+goal_amount)/100
         WHEN sc.sit_indv_baseline_type = 'currency' THEN (scp.siu_baseline_amount+goal_amount)
         ELSE scl.goal_amount       END goal_amount,
CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END level_completed,
CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt < scl.goal_amount THEN 1
          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt < (scp.siu_baseline_amount*(100+goal_amount)/100) THEN 1
          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt < (scp.siu_baseline_amount+goal_amount) THEN 1
          WHEN scpp.activity_amt IS NULL  and scl.sequence_number = 1 THEN 1 -- Bug # 60747
          ELSE 0 END is_current_level,
          CASE WHEN sc.sit_indv_baseline_type = 'percent' THEN (100+scl.goal_amount)  ELSE NULL END goal_perc,
CASE WHEN sc.sit_indv_baseline_type = 'no' THEN  scl.goal_amount-NVL(scpp.activity_amt,0)
          WHEN sc.sit_indv_baseline_type = 'percent' THEN  (scp.siu_baseline_amount*(100+goal_amount)/100)- NVL(scpp.activity_amt,0)
          WHEN sc.sit_indv_baseline_type = 'currency' THEN  (scp.siu_baseline_amount+goal_amount) - NVL(scpp.activity_amt,0) ELSE 0 END remaining, --Bug # 59968 03/14/2015
          scp.siu_baseline_amount,
CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment)   * sit_bonus_payout
         WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment)* sit_bonus_payout
         WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment) * sit_bonus_payout
         ELSE 0
         END bonus_payout,
          p.avatar_small avatar_url,
(SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = p_in_ssi_contest_id) max_level,
          (SELECT COUNT(1) FROM ssi_contest_participant WHERE ssi_contest_id = p_in_ssi_contest_id) total_pax,
         (SELECT STACK_RANK_POSITION FROM ssi_contest_pax_stack_rank WHERE ssi_contest_id = p_in_ssi_contest_id AND user_id = p_in_user_id) stack_rank,
         br.badge_rule_id,NVL((SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale = 'en_US'),(SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale ='en_US')) badge_name
        ,cms_badge.image_small_URL badge_image,
        scl.payout_description
         FROM ssi_contest sc,
       ssi_contest_level scl,
       ssi_contest_participant scp,
       ssi_contest_pax_progress scpp,participant p,badge_rule br,
         (select asset_code, cms_name, cms_code image_small_URL from temp_table_session ) cms_badge
 WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
       AND sc.ssi_contest_id = scl.ssi_contest_id
       AND sc.ssi_contest_id = scp.ssi_contest_id
       AND scp.user_id = scpp.user_id(+)
       AND scp.user_id = p_in_user_id
       AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
       AND scp.user_id = p.user_id
       AND p.status = 'active'
       AND scl.badge_rule_id = br.badge_rule_id(+)
          AND br.cm_asset_key = cms_badge.cms_name (+)
       ORDER BY scl.sequence_number );

       ELSIF v_out_contest_type = 8 THEN

       DELETE FROM temp_table_session;

        INSERT INTO temp_table_session
                SELECT cav.asset_code,
                          MAX (DECODE (cav.key, 'NAME', cav.cms_value, NULL)) AS cms_name,
                         MAX (DECODE (cav.key, 'EARNED_IMAGE_SMALL', cav.cms_value, NULL)) AS cms_code
                       FROM vw_cms_asset_value cav
                       WHERE asset_code='promotion.badge'
                       AND locale = v_locale
                   GROUP BY cav.asset_code,
                            cav.locale,
                            cav.asset_id,
                            cav.content_key_id,
                            cav.content_id;

               OPEN p_out_DTGT_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;
               OPEN p_out_obj_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;
               OPEN p_out_siu_ref_cursor FOR SELECT * FROM DUAL WHERE 1 = 2;

               OPEN p_out_sr_payout_ref_cursor FOR
          SELECT rank_position, payout_amount,payout_desc,br.badge_rule_id,NVL((SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale = v_locale),(SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale ='en_US')) badge_name
        ,cms_badge.image_small_URL badge_image FROM ssi_contest_sr_payout sc,badge_rule br,
         (select asset_code, cms_name, cms_code image_small_URL from temp_table_session ) cms_badge
          WHERE sc.ssi_contest_id = p_in_ssi_contest_id AND sc.badge_rule_id = br.badge_rule_id(+)
          AND br.cm_asset_key = cms_badge.cms_name (+);


          OPEN p_out_sr_pax_ref_cursor FOR
          SELECT rank_position,au.user_id,au.first_name,au.last_name, p.avatar_small avatar_url,scpp.activity_amt,sc.payout_amount,sc.payout_desc payout_description,
br.badge_rule_id,NVL((SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale = v_locale),(SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale ='en_US')) badge_name
        ,cms_badge.image_small_URL badge_image
          FROM ssi_contest_sr_payout sc,ssi_contest scon,
         application_user au, participant p ,ssi_contest_pax_stack_rank scpsr,ssi_contest_pax_progress scpp,
         badge_rule br,
         (select asset_code, cms_name, cms_code image_small_URL from temp_table_session ) cms_badge
          WHERE sc.ssi_contest_id = p_in_ssi_contest_id
          AND sc.ssi_contest_id = scon.ssi_contest_id
          AND sc.ssi_contest_id = scpsr.ssi_contest_id
          AND sc.rank_position = scpsr.stack_rank_position
          AND sc.ssi_contest_id = scpp.ssi_contest_id
          AND scpsr.user_id = scpp.user_id
          AND scpsr.user_id = scpsr.user_id
          AND scpsr.user_id = p.user_id
          AND scpsr.user_id = au.user_id
          AND au.is_active = 1
          AND scon.status <> 'finalize_results'
          AND scpp.activity_amt >= NVL(scon.stack_rank_qualifier_amount,0)
          AND sc.badge_rule_id = br.badge_rule_id(+)
          AND br.cm_asset_key = cms_badge.cms_name (+)
          UNION ALL
          SELECT scpsr.stack_rank_position rank_position,au.user_id,au.first_name,au.last_name, p.avatar_small avatar_url,scpp.activity_amt,sc.payout_amount,sc.payout_description,
          br.badge_rule_id,NVL((SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale = v_locale),(SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale ='en_US')) badge_name
        ,cms_badge.image_small_URL badge_image
          FROM ssi_contest_pax_payout sc,ssi_contest scon,
         application_user au, participant p ,ssi_contest_pax_stack_rank scpsr,ssi_contest_pax_progress scpp,
         badge_rule br,
         (select asset_code, cms_name, cms_code image_small_URL from temp_table_session ) cms_badge
          WHERE sc.ssi_contest_id = p_in_ssi_contest_id
          AND sc.ssi_contest_id = scon.ssi_contest_id
          AND sc.user_id = scpsr.user_id
          AND sc.ssi_contest_id = scpsr.ssi_contest_id
          AND sc.ssi_contest_id = scpp.ssi_contest_id
          AND sc.user_id = scpp.user_id
          AND sc.user_id = au.user_id
          AND au.is_active =1
          AND au.user_id = p.user_id
          AND scon.status = 'finalize_results'
          AND sc.badge_rule_id = br.badge_rule_id(+)
          AND br.cm_asset_key = cms_badge.cms_name (+);

        IF v_status = 'finalize_results' THEN    --05/28/2015
            OPEN p_out_sr_ref_cursor FOR   --05/28/2015
               SELECT avatar,
                      activity_amt,
                      stack_rank_position,
                      max_stack_rank,(max_activity_amt - activity_amt) behind_leader,
                      stack_rank_qualifier_amount,
                      (SELECT payout_amount FROM ssi_contest_pax_payout p
                            WHERE  p.ssi_contest_id = p_in_ssi_contest_id 
                                    AND p.user_id = sc.user_id) payout_amount,   --05/28/2015
                      (SELECT payout_desc FROM ssi_contest_sr_payout 
                            WHERE  ssi_contest_id = p_in_ssi_contest_id 
                                   AND rank_position = stack_rank_position) payout_description FROM 
                            (
                              SELECT p.avatar_small avatar,
                                     scpp.activity_amt,
                                     scpsr.stack_rank_position
                                    --,(SELECT MAX(stack_rank_position) FROM ssi_contest_pax_stack_rank WHERE ssi_contest_id = p_in_ssi_contest_id) max_stack_rank  --Bug 61468 --04/16/2015
                                    ,(SELECT COUNT(DISTINCT user_id) FROM ssi_contest_pax_stack_rank WHERE ssi_contest_id = p_in_ssi_contest_id) max_stack_rank,    --Bug 61468 --04/16/2015
                                     (SELECT CASE WHEN sc.stack_rank_order ='desc' THEN MAX(activity_amt) ELSE MIN(activity_amt) end  FROM ssi_contest_pax_progress WHERE ssi_contest_id = sc.ssi_contest_id) max_activity_amt,sc.stack_rank_qualifier_amount--Bug # 61120
                                    ,scp.user_id   --05/28/2015                                      
                                FROM
                                ssi_contest sc,
                                ssi_contest_participant scp,
                                ssi_contest_pax_progress scpp,
                                ssi_contest_pax_stack_rank scpsr,
                                participant p
                                WHERE
                                sc.ssi_contest_id = p_in_ssi_contest_id
                                AND sc.ssi_contest_id = scp.ssi_contest_id
                                AND scp.user_id = p_in_user_id
                                AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                                AND scp.user_id = scpp.user_id(+)
                                AND scp.ssi_contest_id = scpsr.ssi_contest_id(+)-- Bug # 60120
                                AND scp.user_id = scpsr.user_id(+) -- Bug # 60120
                                AND scp.user_id = p.user_id
                                AND p.status = 'active') sc;

         ELSE
           OPEN p_out_sr_ref_cursor FOR
               SELECT avatar,
                      activity_amt,
                      stack_rank_position,
                      max_stack_rank,(max_activity_amt - activity_amt) behind_leader,
                      stack_rank_qualifier_amount,
                      (SELECT payout_amount FROM ssi_contest_sr_payout 
                            WHERE  ssi_contest_id = p_in_ssi_contest_id 
                                    AND rank_position = stack_rank_position) payout_amount,
                      (SELECT payout_desc FROM ssi_contest_sr_payout 
                            WHERE  ssi_contest_id = p_in_ssi_contest_id 
                                   AND rank_position = stack_rank_position) payout_description FROM 
                            (
                              SELECT p.avatar_small avatar,
                                     scpp.activity_amt,
                                     scpsr.stack_rank_position
                                    --,(SELECT MAX(stack_rank_position) FROM ssi_contest_pax_stack_rank WHERE ssi_contest_id = p_in_ssi_contest_id) max_stack_rank  --Bug 61468 --04/16/2015
                                    ,(SELECT COUNT(DISTINCT user_id) FROM ssi_contest_pax_stack_rank WHERE ssi_contest_id = p_in_ssi_contest_id) max_stack_rank,    --Bug 61468 --04/16/2015
                                     (SELECT CASE WHEN sc.stack_rank_order ='desc' THEN MAX(activity_amt) ELSE MIN(activity_amt) end  FROM ssi_contest_pax_progress WHERE ssi_contest_id = sc.ssi_contest_id) max_activity_amt,sc.stack_rank_qualifier_amount--Bug # 61120
                                FROM
                                ssi_contest sc,
                                ssi_contest_participant scp,
                                ssi_contest_pax_progress scpp,
                                ssi_contest_pax_stack_rank scpsr,
                                participant p
                                WHERE
                                sc.ssi_contest_id = p_in_ssi_contest_id
                                AND sc.ssi_contest_id = scp.ssi_contest_id
                                AND scp.user_id = p_in_user_id
                                AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                                AND scp.user_id = scpp.user_id(+)
                                AND scp.ssi_contest_id = scpsr.ssi_contest_id(+)-- Bug # 60120
                                AND scp.user_id = scpsr.user_id(+) -- Bug # 60120
                                AND scp.user_id = p.user_id
                                AND p.status = 'active');
        END IF;
      END IF;
      p_out_contest_type  := v_out_contest_type;
      p_out_return_code :=0;

      EXCEPTION WHEN OTHERS THEN
       p_out_contest_type  := NULL;
      p_out_return_code :=99;
       prc_execution_log_entry ('PRC_SSI_CONTEST_PAX_PROGRESS',1,'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| SQLERRM,NULL);
      END prc_ssi_contest_pax_progress;

      PROCEDURE prc_ssi_contest_pax_list (p_in_ssi_contest_id     IN     NUMBER,
      p_in_sortColName     IN     VARCHAR2,
      p_in_sortedBy           IN     VARCHAR2,
      p_in_rowNumStart               IN NUMBER,
      p_in_rowNumEnd                 IN NUMBER,
      p_in_user_id             IN NUMBER,    --02/04/2015
      p_out_contest_type  OUT NUMBER,
      p_out_return_code    OUT NUMBER,
      p_out_size_data          OUT    NUMBER,
      p_out_obj_ref_cursor OUT SYS_REFCURSOR,
      p_out_obj_total_ref_cursor OUT SYS_REFCURSOR,
      p_out_dtgt_ref_cursor OUT SYS_REFCURSOR,
      p_out_total_dtgt_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_total_ref_cursor OUT SYS_REFCURSOR)
      IS

       /***********************************************************************************
      Purpose:  Procedure to provide the detail pax list for a contest.

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
     Ravi Dhanekula   1/26/2015     Initial Version
     Ravi Dhanekula   03/06/2015    Fixed the bug # 59977,59992
     Ravi Dhanekula   03/24/2015    Bug 60668 - Contest creator view - DTGT - details screen - Payout is displayed wrongly
     Ravi Dhanekula   03/25/2015    Bug # 60745 - Creator view-Step it up-Participant table-Level completed sort functionality is not working
     Ravi Dhanekula   04/07/2015    Bug # 60834 Sequence of activities for DTGT was wrong.
     Swati            04/16/2015    Bug 61500 - creator view-Issue in participants sorting
     Ravi Dhanekula   04/17/2015    Bug # 61566 -- Have to revoke the changes done for 61500.
     Suresh J         04/20/2015    Bug #61540 - Fixed Sorting to be compatiable for both char and numeric. And, CASEed invalid Java assigned column alias for DTGT
     Ravi Dhanekula   04/20/2015    Bug # 61624 - Fixed issue with level_completed column on  p_out_siu_ref_cursor (manager view)
     Ravi Dhanekula   05/01/2015    Bug # 61980  Changed the logic for LEAD function on p_out_dtgt_ref_cursor and p_out_total_dtgt_ref_cursor for both creator and manager views.
     Swati            05/11/2015    Bug 62116 - Step It Up - Display the payout description instead of the value on the contest details table
                      05/12/2015    Bug 62116 - Display the payout description of Objective contest instead of the value on the contest details table
     Swati            05/14/2015    Bug 62200 - Total the baseline column in the contest details table for SIU contest
     Swati            05/19/2015    Bug 62316 - Contest creator view - Step it up - Details page - Participant table - Pax are displayed twice in this table
     Swati            05/20/2015    Bug 62354 - Creator view-Step it up-After progress load-Payout value in participant table is calculated wrongly
     KrishnaDeepika   05/28/2015    Bug 62343 - Creator or Manager - participant drilldown(edit) - added ssi_contest_id column to the cursors output
     Ravi Dhanekula   06/15/2015    Bug # 62817 - Fixed sortingg issue for activity_amt on p_out_obj_ref_cursor.
     Suresh J          07/14/2015    Bug # 63043 -Sorting Issue   
     nagarajs          10/07/2015    Bug # 63811 -SSI-Objective contest-Sort is not working in participant table for '% to Objective' field.
     Gorantla         05/15/2018    G6-4061/ Bug 76336 - Extra comma in procedure causes update to error     
   ************************************************************************************/
     v_out_contest_type NUMBER(18);
     v_sortCol             VARCHAR2(200);
     l_query VARCHAR2(32767);

      BEGIN
   
      SELECT contest_type
                   INTO v_out_contest_type FROM ssi_contest WHERE ssi_contest_id = p_in_ssi_contest_id;


      IF p_in_sortColName in ('first_name','last_name','org_name','name') THEN   --04/20/2015 Bug 61540
      v_sortCol := ' '|| CASE WHEN v_out_contest_type = 2 AND p_in_sortColName = 'org_name' THEN 'lower(name)' ELSE CASE WHEN p_in_sortColName = 'last_name' THEN 'lower(last_name)'||' ' || p_in_sortedBy||', lower(first_name)' ELSE lower(p_in_sortColName) END END || ' ' || p_in_sortedBy;--04/16/2015 Bug 61500  --07/14/2015
      ELSE  --04/20/2015 Bug 61540
      v_sortCol := ' '||p_in_sortColName|| ' ' || p_in_sortedBy;
      END IF;

          l_query  := 'SELECT * FROM
  ( ';

            IF p_in_user_id IS NULL THEN    --02/04/2015 IF Creator
              IF v_out_contest_type = 4 THEN

              l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                               FROM (
                  SELECT contest_id,user_id,last_name,first_name,org_name,objective_amount,activity_amt,perc_achieved,objective_payout, --05/28/2015
                  objective_bonus_payout,(objective_payout + NVL(objective_bonus_payout,0)) total_payout, -- Bug # 61223
                  objective_payout_description --05/12/2015 Bug 62116
                  FROM (
                  SELECT sc.ssi_contest_id contest_id, --05/28/2015
                        au.user_id,
                        au.last_name,
                        au.first_name,
                        n.name org_name,
                          scp.objective_amount,
                          NVL(scpp.activity_amt,0) activity_amt,
                         --  CASE WHEN NVL(scpp.activity_amt,0)<=scp.objective_amount THEN (scp.objective_amount - NVL(scpp.activity_amt,0))
                         -- ELSE 0 END to_go,
                           NVL(FLOOR(scpp.activity_amt / scp.objective_amount * 100),0) AS perc_achieved, --10/07/2015 added NVL
                        CASE       WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
                                   WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout
                                   ELSE 0
                                END objective_payout,
                      CASE WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
                           WHEN CASE                                                       --03/06/Bug # 59992
                                   WHEN scpp.activity_amt > scp.objective_amount
                                   THEN
                                          FLOOR ((scpp.activity_amt - scp.objective_amount)
                                        / scp.objective_bonus_increment ) * scp.objective_bonus_payout
                                   ELSE
                                      0
                                END  > OBJECTIVE_BONUS_CAP THEN OBJECTIVE_BONUS_CAP
                                ELSE CASE
                                   WHEN scpp.activity_amt > scp.objective_amount
                                   THEN
                                           FLOOR ((scpp.activity_amt - scp.objective_amount)
                                        / scp.objective_bonus_increment ) * scp.objective_bonus_payout
                                   ELSE
                                      0
                                END END AS objective_bonus_payout,
                                scp.objective_payout_description --05/12/2015 Bug 62116
                        FROM ssi_contest sc,
                             ssi_contest_pax_progress scpp,
                             ssi_contest_participant scp,
                             application_user au,
                             user_node un,
                             node n,
                             participant p
                       WHERE     sc.ssi_contest_id = '''||p_in_ssi_contest_id||'''
                             AND sc.ssi_contest_id = scp.ssi_contest_id
                             AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                             AND scp.user_id = scpp.user_id(+)
                             AND scp.user_id = au.user_id
                             AND scp.user_id = un.user_id
                             AND un.is_primary = 1
                             AND un.status = 1
                             AND au.is_active = 1
                             AND au.user_id = p.user_id
                             AND un.node_id = n.node_id )
                             ORDER BY '|| v_sortCol ||'
                ) RS) WHERE RN >= ' ||p_in_rowNumStart||' AND RN   <= '|| p_in_rowNumEnd;

                OPEN p_out_obj_ref_cursor FOR l_query;
                OPEN p_out_obj_total_ref_cursor FOR
                SELECT SUM(scp.objective_amount) objective_amount,
                          SUM(scpp.activity_amt) activity_amt,
                         FLOOR(SUM(scpp.activity_amt) / SUM(scp.objective_amount) * 100) AS perc_achieved,
                           SUM(                CASE WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
                                                    WHEN scp.objective_bonus_payout IS NOT NULL AND scpp.activity_amt >= scp.objective_amount
                                   THEN
                                        objective_payout
                                      +   FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment) * scp.objective_bonus_payout
                                   WHEN scp.objective_bonus_payout IS NULL AND scpp.activity_amt >= scp.objective_amount THEN objective_payout
                                   ELSE
                                      0
                                END) total_potential_payout,
                                 SUM( CASE WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
                                   WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout
                                   ELSE 0
                                END  ) total_objective_payout,
                                 SUM(               CASE 
                                  WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
                                  WHEN CASE                    --03/06/Bug # 59992
                                   WHEN scpp.activity_amt > scp.objective_amount
                                   THEN
                                           FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment) * scp.objective_bonus_payout
                                   ELSE
                                      0
                                END  > OBJECTIVE_BONUS_CAP THEN OBJECTIVE_BONUS_CAP
                                ELSE CASE
                                   WHEN scpp.activity_amt > scp.objective_amount
                                   THEN
                                           FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment)* scp.objective_bonus_payout
                                   ELSE
                                      0
                                END END) total_bonus_payout
                        FROM ssi_contest sc,
                             ssi_contest_pax_progress scpp,
                             ssi_contest_participant scp,
                             application_user au,
                             participant p
                       WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                             AND sc.ssi_contest_id = scp.ssi_contest_id
                             AND scp.user_id = au.user_id
                             AND au.is_active = 1
                             AND au.user_id = p.user_id
                             AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                             AND scp.user_id = scpp.user_id(+);

                            OPEN p_out_dtgt_ref_cursor FOR
                            select * from dual where 1=2;

                             OPEN p_out_total_dtgt_ref_cursor FOR
                             select * from dual where 1=2;

                             OPEN p_out_siu_ref_cursor FOR
                            select * from dual where 1=2;

                             OPEN p_out_siu_total_ref_cursor FOR
                             select * from dual where 1=2;


                ELSIF  v_out_contest_type = 2 THEN--If contest_type = 2 (Do This Get That)

                OPEN p_out_obj_ref_cursor FOR
                select * from dual where 1=2;

                 OPEN p_out_obj_total_ref_cursor FOR
                 select * from dual where 1=2;

                 OPEN p_out_siu_ref_cursor FOR
                select * from dual where 1=2;

                 OPEN p_out_siu_total_ref_cursor FOR
                 select * from dual where 1=2;

               DELETE FROM gtt_ssi_dtgt;

                INSERT INTO gtt_ssi_dtgt
                SELECT ssi_contest_id,
                        ssi_contest_activity_id,
                        description,
                        user_id,
                        activity_amt,
                       CASE WHEN is_opt_out_of_awards = 1 THEN 0 --05/17/2017
                            WHEN activity_amt > min_qualifier THEN
                       FLOOR ((
                       CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt
                       ELSE  maximum_payout_activity END- min_qualifier)/increment_amount)
                       ELSE 0 END payout_quantity,
                       CASE WHEN is_opt_out_of_awards = 1 THEN 0 --05/17/2017
                            WHEN activity_amt > min_qualifier THEN
                       FLOOR(( --03/24/2015 Bug 60668 Added Floor Function
                       CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt
                       ELSE  maximum_payout_activity END- min_qualifier)/increment_amount) * payout_amount
                       ELSE 0 END payout_value
                       FROM (
            SELECT sc.ssi_contest_id,
                   sca.ssi_contest_activity_id,
                   sca.description,
                   scp.user_id,
                   NVL((SELECT activity_amt
                      FROM ssi_contest_pax_progress scpp
                     WHERE     scpp.ssi_contest_activity_id = sca.ssi_contest_activity_id
                           AND scpp.user_id = scp.user_id),0) activity_amt,
                     sca.increment_amount,
                     sca.payout_amount,
                     sca.min_qualifier,
                     sca.payout_cap_amount,
                     sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount AS maximum_payout_activity,
                     p.is_opt_out_of_awards
              FROM ssi_contest sc, ssi_contest_activity sca, ssi_contest_participant scp,application_user au,participant p
             WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                   AND sc.ssi_contest_id = sca.ssi_contest_id
                   AND sc.ssi_contest_id = scp.ssi_contest_id
                   AND scp.user_id = au.user_id
                   AND scp.user_id = p.user_id
                   AND au.is_active = 1   ORDER BY ssi_contest_activity_id asc
                   ) ;


                 l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                               FROM (
                   WITH t
                 AS (SELECT ROW_NUMBER () OVER (PARTITION BY USER_ID ORDER BY Activity_sort) rn,
                            user_id,
                            ssi_contest_id contest_id,  --05/28/2015
                            ssi_contest_activity_id Activity1,
                            description Activity_description1,
                            payout_value payout_value1,
                            activity_amt activity_amt1,
                            payout_quantity payout_quantity1,
                            LEAD (ssi_contest_activity_id)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity2,
                            LEAD (description)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description2,
                            LEAD (payout_value)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value2,
                            LEAD (activity_amt)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt2,
                            LEAD (payout_quantity)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity2,
                            LEAD (ssi_contest_activity_id, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity3,
                               LEAD (description,2)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description3,
                            LEAD (payout_value, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value3,
                            LEAD (activity_amt, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt3,
                            LEAD (payout_quantity, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity3 ,
                            LEAD (ssi_contest_activity_id, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity4,
                               LEAD (description,3)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description4,
                            LEAD (payout_value, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value4,
                            LEAD (activity_amt, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt4,
                            LEAD (payout_quantity, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity4,
                            LEAD (ssi_contest_activity_id, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity5,
                               LEAD (description,4)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description5,
                            LEAD (payout_value, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value5,
                            LEAD (activity_amt, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt5,
                            LEAD (payout_quantity, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity5,
                            LEAD (ssi_contest_activity_id, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity6,
                               LEAD (description,5)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description6,
                            LEAD (payout_value, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value6,
                            LEAD (activity_amt, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt6,
                            LEAD (payout_quantity, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity6,
                            LEAD (ssi_contest_activity_id, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity7,
                               LEAD (description,6)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description7,
                            LEAD (payout_value, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value7,
                            LEAD (activity_amt, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt7,
                            LEAD (payout_quantity, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity7,
                            LEAD (ssi_contest_activity_id, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity8,
                               LEAD (description,7)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description8,
                            LEAD (payout_value, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value8,
                            LEAD (activity_amt, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt8,
                            LEAD (payout_quantity, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity8,
                            LEAD (ssi_contest_activity_id, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity9,
                               LEAD (description,8)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description9,
                            LEAD (payout_value, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value9,
                            LEAD (activity_amt, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt9,
                            LEAD (payout_quantity, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity9 ,
                            LEAD (ssi_contest_activity_id, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity10,
                               LEAD (description,9)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description10,
                            LEAD (payout_value, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value10,
                            LEAD (activity_amt, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt10,
                            LEAD (payout_quantity, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity10
                                          FROM (SELECT ROWNUM Activity_sort, S.* FROM (
                        SELECT * FROM gtt_ssi_dtgt ORDER BY user_id,ssi_contest_activity_id) s
                   ))
            SELECT t.user_id,au.last_name,au.first_name,n.name,
                   contest_id, --05/28/2015
                   Activity1,
                   Activity_description1,
                   payout_value1,
                   activity_amt1,
                   payout_quantity1,
                   Activity2,
                   Activity_description2,
                   payout_value2,
                   activity_amt2,
                   payout_quantity2,
                   Activity3,
                   Activity_description3,
                   payout_value3,
                   activity_amt3,
                   payout_quantity3,
                   Activity4,
                   Activity_description4,
                   payout_value4,
                   activity_amt4,
                   payout_quantity4,
                   Activity5,
                   Activity_description5,
                   payout_value5,
                   activity_amt5,
                   payout_quantity5,
                   Activity6,
                   Activity_description6,
                   payout_value6,
                   activity_amt6,
                   payout_quantity6,
                   Activity7,
                   Activity_description7,
                   payout_value7,
                   activity_amt7,
                   payout_quantity7,
                   Activity8,
                   Activity_description8,
                   payout_value8,
                   activity_amt8,
                   payout_quantity8,
                   Activity9,
                   Activity_description9,
                   payout_value9,
                   activity_amt9,
                   payout_quantity9,
                   Activity10,
                   Activity_description10,
                   payout_value10,
                   activity_amt10,
                   payout_quantity10,
                   (NVL(payout_value1,0)+NVL(payout_value2,0)+NVL(payout_value3,0)+NVL(payout_value4,0)+NVL(payout_value5,0)+NVL(payout_value6,0)+NVL(payout_value7,0)+NVL(payout_value8,0)+nvl(payout_value9,0)+NVL(payout_value10,0)) total_payout_value
              FROM t,application_user au, user_node un,node n
             WHERE rn = 1
             AND t.user_id = au.user_id
             AND au.user_id = un.user_id
             AND un.node_id = n.node_id
             AND un.is_primary = 1
             AND au.is_active = 1
             AND un.status = 1  ORDER BY '|| v_sortCol ||'
                ) RS) WHERE RN >= ' ||p_in_rowNumStart||' AND RN   <= '|| p_in_rowNumEnd;

            OPEN p_out_dtgt_ref_cursor FOR l_query;

                    OPEN p_out_total_dtgt_ref_cursor FOR
                       SELECT SUM(payout_value1) payout_value_total1,SUM(activity_amt1) activity_amt_total1,SUM(payout_quantity1) payout_quantity_total1,
                        SUM(payout_value2) payout_value_total2,SUM(activity_amt2) activity_amt_total2,SUM(payout_quantity2) payout_quantity_total2,
                        SUM(payout_value3) payout_value_total3,SUM(activity_amt3) activity_amt_total3,SUM(payout_quantity3) payout_quantity_total3,
                        SUM(payout_value4) payout_value_total4,SUM(activity_amt4) activity_amt_total4,SUM(payout_quantity4) payout_quantity_total4,
                        SUM(payout_value5) payout_value_total5,SUM(activity_amt5) activity_amt_total5,SUM(payout_quantity5) payout_quantity_total5,
                        SUM(payout_value6) payout_value_total6,SUM(activity_amt6) activity_amt_total6,SUM(payout_quantity6) payout_quantity_total6,
                        SUM(payout_value7) payout_value_total7,SUM(activity_amt7) activity_amt_total7,SUM(payout_quantity7) payout_quantity_total7,
                        SUM(payout_value8) payout_value_total8,SUM(activity_amt8) activity_amt_total8,SUM(payout_quantity8) payout_quantity_total8,
                        SUM(payout_value9) payout_value_total9,SUM(activity_amt9) activity_amt_total9,SUM(payout_quantity9) payout_quantity_total9,
                        SUM(payout_value10) payout_value_total10,SUM(activity_amt10) activity_amt_total10,SUM(payout_quantity10) payout_quantity_total10,
                        SUM(total_payout_value) total_payout_value FROM (WITH t
                 AS (SELECT ROW_NUMBER () OVER (PARTITION BY USER_ID ORDER BY Activity_sort) rn,
                            user_id,
                            ssi_contest_activity_id Activity1,
                            payout_value payout_value1,
                            activity_amt activity_amt1,
                            payout_quantity payout_quantity1,
                            LEAD (ssi_contest_activity_id)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity2,
                            LEAD (payout_value)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value2,
                            LEAD (activity_amt)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt2,
                            LEAD (payout_quantity)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity2,
                            LEAD (ssi_contest_activity_id, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity3,
                            LEAD (payout_value, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value3,
                            LEAD (activity_amt, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt3,
                            LEAD (payout_quantity, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity3 ,
                            LEAD (ssi_contest_activity_id, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity4,
                            LEAD (payout_value, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value4,
                            LEAD (activity_amt, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt4,
                            LEAD (payout_quantity, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity4,
                            LEAD (ssi_contest_activity_id, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity5,
                            LEAD (payout_value, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value5,
                            LEAD (activity_amt, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt5,
                            LEAD (payout_quantity, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity5,
                            LEAD (ssi_contest_activity_id, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity6,
                            LEAD (payout_value, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value6,
                            LEAD (activity_amt, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt6,
                            LEAD (payout_quantity, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity6,
                            LEAD (ssi_contest_activity_id, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity7,
                            LEAD (payout_value, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value7,
                            LEAD (activity_amt, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt7,
                            LEAD (payout_quantity, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity7,
                            LEAD (ssi_contest_activity_id, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity8,
                            LEAD (payout_value, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value8,
                            LEAD (activity_amt, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt8,
                            LEAD (payout_quantity, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity8,
                            LEAD (ssi_contest_activity_id, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity9,
                            LEAD (payout_value, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value9,
                            LEAD (activity_amt, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt9,
                            LEAD (payout_quantity, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity9 ,
                            LEAD (ssi_contest_activity_id, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity10,
                            LEAD (payout_value, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value10,
                            LEAD (activity_amt, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt10,
                            LEAD (payout_quantity, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity10
                                          FROM (SELECT ROWNUM Activity_sort, S.* FROM (
                        SELECT * FROM gtt_ssi_dtgt ORDER BY user_id,ssi_contest_activity_id) s
                   ))
            SELECT t.user_id,
                   Activity1,
                   payout_value1,
                   activity_amt1,
                   payout_quantity1,
                   Activity2,
                   payout_value2,
                   activity_amt2,
                   payout_quantity2,
                   Activity3,
                   payout_value3,
                   activity_amt3,
                   payout_quantity3,
                   Activity4,
                   payout_value4,
                   activity_amt4,
                   payout_quantity4,
                   Activity5,
                   payout_value5,
                   activity_amt5,
                   payout_quantity5,
                   Activity6,
                   payout_value6,
                   activity_amt6,
                   payout_quantity6,
                   Activity7,
                   payout_value7,
                   activity_amt7,
                   payout_quantity7,
                   Activity8,
                   payout_value8,
                   activity_amt8,
                   payout_quantity8,
                   Activity9,
                   payout_value9,
                   activity_amt9,
                   payout_quantity9,
                   Activity10,
                   payout_value10,
                   activity_amt10,
                   payout_quantity10,
                   (NVL(payout_value1,0)+NVL(payout_value2,0)+NVL(payout_value3,0)+NVL(payout_value4,0)+NVL(payout_value5,0)+NVL(payout_value6,0)+NVL(payout_value7,0)+NVL(payout_value8,0)+nvl(payout_value9,0)+NVL(payout_value10,0)) total_payout_value
              FROM t
             WHERE rn = 1);

             ELSIF v_out_contest_type = 16 THEN

             l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                               FROM (
             SELECT sc.ssi_contest_id contest_id, --05/28/2015
                        au.user_id,
                        au.last_name,au.first_name,
                        n.name org_name,
                        NVL(sc.activity_amt,0) activity_amt,
                        NVL(sc.level_completed,0) level_completed, --Bug # 60745 03/25/2015
                        CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE sc.level_payout END level_payout,--05/17/2017 Start
                        CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE sc.bonus_payout END bonus_payout,
                        CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE sc.total_payout END total_payout,
--                        sc.bonus_payout,
--                        sc.total_payout--05/17/2017 End
                        sc.siu_baseline_amount, --Bug #61306 04/22/2015 --05/15/2018 removed the extra preceding comma
                        sc.payout_description --05/11/2015 Bug 62116
                    FROM (
 SELECT DISTINCT ssi_contest_id,user_id,activity_amt,CASE WHEN level_completed =0 THEN NULL ELSE level_completed END level_completed,level_payout,
 CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                             ELSE bonus_payout END,0)
 ELSE 0
 END bonus_payout,
 level_payout + CASE WHEN include_bonus =1 THEN (CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                             ELSE bonus_payout END,0)
 ELSE 0
 END) ELSE 0 END total_payout --05/20/2015 Bug 62354
 ,siu_baseline_amount, --Bug #61306 04/22/2015
 CASE WHEN level_completed = 0 then NULL
 ELSE payout_description
 END payout_description --05/11/2015 Bug 62116 --05/19/2015 Bug 62316
  FROM (
SELECT sc.ssi_contest_id,scp.user_id,scpp.activity_amt, scl.sequence_number,scl.goal_amount,CASE WHEN sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END level_completed,
RANK() OVER (PARTITION BY scp.user_id ORDER BY CASE WHEN sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank,
CASE WHEN sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount THEN scl.payout_amount
          WHEN sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.payout_amount
          WHEN sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.payout_amount ELSE 0 END level_payout,
CASE WHEN sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment)   * sit_bonus_payout
         WHEN sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment)* sit_bonus_payout
         WHEN sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment) * sit_bonus_payout
         ELSE 0
         END bonus_payout,
         (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = '''||p_in_ssi_contest_id||''') max_level,sit_bonus_cap
          ,scp.siu_baseline_amount, --Bug #61306 04/22/2015
          scl.payout_description, --05/11/2015 Bug 62116
          sc.include_bonus --05/20/2015 Bug 62354
  FROM ssi_contest sc,
       ssi_contest_level scl,
       ssi_contest_participant scp,
       ssi_contest_pax_progress scpp
 WHERE     sc.ssi_contest_id = '''||p_in_ssi_contest_id||'''
       AND sc.ssi_contest_id = scl.ssi_contest_id
       AND sc.ssi_contest_id = scp.ssi_contest_id
       AND scp.user_id = scpp.user_id(+)
       AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
       ) WHERE rec_rank = 1 ) sc, application_user au, participant p,user_node un, node n
       WHERE sc.user_id = au.user_id
       AND sc.user_id = p.user_id
       AND sc.user_id = un.user_id
       AND un.status = 1
       AND un.is_primary = 1
       AND au.is_active = 1
       AND un.node_id = n.node_id
       ORDER BY '|| v_sortCol ||'
                ) RS) WHERE RN >= ' ||p_in_rowNumStart||' AND RN   <= '|| p_in_rowNumEnd;

                 OPEN p_out_siu_ref_cursor FOR l_query;

                 OPEN p_out_siu_total_ref_cursor FOR
                 SELECT SUM(activity_amt) activity_amt, SUM(level_payout) level_payout,SUM(CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                             ELSE bonus_payout END,0)
 ELSE 0
 END) bonus_payout,
 SUM(level_payout + CASE WHEN include_bonus = 1 THEN (CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                             ELSE bonus_payout END,0)
 ELSE 0
 END)
 ELSE 0 END) total_payout --05/20/2015 Bug 62354
 ,SUM (siu_baseline_amount) as siu_baseline_amount  --Bug #61306 04/22/2015
   FROM (
 SELECT DISTINCT ssi_contest_id,user_id,activity_amt,level_completed,level_payout, --03/06/2015 bUG # 59977
 bonus_payout,max_level,sit_bonus_cap
 ,siu_baseline_amount,  --Bug #61306 04/22/2015
 include_bonus --05/20/2015 Bug 62354
 FROM (SELECT sc.ssi_contest_id,scp.user_id,scpp.activity_amt, scl.sequence_number,scl.goal_amount,CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END level_completed,
RANK() OVER (PARTITION BY scp.user_id ORDER BY CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank,
CASE WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
     WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.payout_amount
          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.payout_amount
          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.payout_amount ELSE 0 END level_payout,
CASE WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
     WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment)   * sit_bonus_payout
         WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment)* sit_bonus_payout
         WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment) * sit_bonus_payout
         ELSE 0
         END bonus_payout,
         (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = p_in_ssi_contest_id) max_level,sit_bonus_cap
          ,scp.siu_baseline_amount, --Bug #61306 04/22/2015
          scl.payout_description, --05/11/2015 Bug 62116
          sc.include_bonus --05/20/2015 Bug 62354
  FROM ssi_contest sc,
       ssi_contest_level scl,
       ssi_contest_participant scp,
       ssi_contest_pax_progress scpp,
       participant p --05/17/2017
 WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
       AND sc.ssi_contest_id = scl.ssi_contest_id
       AND sc.ssi_contest_id = scp.ssi_contest_id
       AND scp.user_id = scpp.user_id(+)
       AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
       AND scpp.user_id = p.user_id
       ) WHERE rec_rank = 1);

                 OPEN p_out_obj_ref_cursor FOR
                select * from dual where 1=2;

                 OPEN p_out_obj_total_ref_cursor FOR
                 select * from dual where 1=2;

                 OPEN p_out_dtgt_ref_cursor FOR
                select * from dual where 1=2;

                 OPEN p_out_total_dtgt_ref_cursor FOR
                 select * from dual where 1=2;

              END IF; --If the contest_type !=2

              SELECT COUNT(1) INTO  p_out_size_data
            FROM ssi_contest_participant scp, application_user au
           WHERE     scp.ssi_contest_id = p_in_ssi_contest_id
                 AND scp.user_id = au.user_id
                 AND au.is_active = 1;

            ELSE --02/04/2015  IF Manager

      DELETE FROM gtt_node_and_below_users;

      INSERT INTO gtt_node_and_below_users (user_id)--Bug # 61461
      SELECT   DISTINCT au.user_id
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
                                                                       (SELECT node_id,NAME as node_name,parent_node_id,path
                                                                        FROM node n
                                                                        START WITH parent_node_id IS NULL
                                                                        CONNECT BY PRIOR node_id = parent_node_id) ip
                                                                        ,user_node un
                                                                        ,application_user au
                                        where ip.node_id = npn.path_node_id
                                              AND npn.node_id = un.node_id
                                              AND un.user_id = au.user_id
                                              AND ip.node_id IN ((select node_id from user_node where user_id = p_in_user_id and status = 1 and role in ('own','mgr')))
                                              AND au.is_active = 1
                                              AND au.user_type = 'pax'
                                              AND un.status = 1;

            IF v_out_contest_type = 4 THEN

              l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                               FROM (
                  SELECT contest_id,user_id,last_name,first_name,org_name,objective_amount,activity_amt,perc_achieved,objective_payout  --05/28/2015
                  ,objective_bonus_payout,(objective_payout + NVL(objective_bonus_payout,0)) total_payout, -- Bug # 61223
                  objective_payout_description --05/12/2015 Bug 62116
                  FROM (
                  SELECT sc.ssi_contest_id contest_id,  --05/28/2015
                        au.user_id,au.last_name,au.first_name,
                        n.name org_name,
                          scp.objective_amount,
                          NVL(scpp.activity_amt,0) activity_amt,
                          -- CASE WHEN scpp.activity_amt<=scp.objective_amount THEN (scp.objective_amount - scpp.activity_amt)
                          --ELSE 0 END to_go,
                           NVL(FLOOR(scpp.activity_amt / scp.objective_amount * 100),0) AS perc_achieved, --10/07/2015 added NVL
                         /*  CASE
                                   WHEN scp.objective_bonus_payout IS NOT NULL AND scpp.activity_amt >= scp.objective_amount
                                   THEN
                                        objective_payout
                                      +   (scpp.activity_amt - scp.objective_amount)
                                        * scp.objective_bonus_payout
                                        / scp.objective_bonus_increment
                                   WHEN scp.objective_bonus_payout IS NULL AND scpp.activity_amt >= scp.objective_amount THEN objective_payout
                                   ELSE
                                      0
                                END potential_payout,*/
                      CASE         WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
                                   WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout
                                   ELSE 0
                                END objective_payout,
                      CASE WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
                           WHEN CASE
                                   WHEN scpp.activity_amt > scp.objective_amount
                                   THEN
                                           FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment) * scp.objective_bonus_payout
                                   ELSE
                                      0
                                END  > OBJECTIVE_BONUS_CAP THEN OBJECTIVE_BONUS_CAP
                                ELSE CASE
                                   WHEN scpp.activity_amt > scp.objective_amount
                                   THEN
                                           FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment)* scp.objective_bonus_payout
                                   ELSE
                                      0
                                END END AS objective_bonus_payout,
                                scp.objective_payout_description --05/12/2015 Bug 62116
                        FROM ssi_contest sc,
                             ssi_contest_pax_progress scpp,
                             ssi_contest_participant scp,
                             application_user au,
                             user_node un,
                             node n,
                             participant p
                       WHERE     sc.ssi_contest_id = '''||p_in_ssi_contest_id||'''
                             AND sc.ssi_contest_id = scp.ssi_contest_id
                             AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                             AND scp.user_id = scpp.user_id(+)
                             AND scp.user_id = au.user_id
                             AND scp.user_id = un.user_id
                             AND un.is_primary = 1
                             AND un.status = 1
                             AND au.is_active = 1
                             AND au.user_id = p.user_id
                             AND un.node_id = n.node_id
                             AND EXISTS        --02/04/2015  IF Manager
                                 (select nab.user_id
                                  from gtt_node_and_below_users nab
                                  where nab.user_id = scp.user_id
                                  ) )
                             ORDER BY '|| v_sortCol ||'
                ) RS) WHERE RN >= ' ||p_in_rowNumStart||' AND RN   <= '|| p_in_rowNumEnd;

                OPEN p_out_obj_ref_cursor FOR l_query;

                OPEN p_out_obj_total_ref_cursor FOR
                SELECT SUM(scp.objective_amount) objective_amount,
                          SUM(scpp.activity_amt) activity_amt,
                         FLOOR(SUM(scpp.activity_amt) / SUM(scp.objective_amount) * 100) AS perc_achieved,
                          SUM(                CASE WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
                          WHEN scp.objective_bonus_payout IS NOT NULL AND scpp.activity_amt >= scp.objective_amount
                                   THEN
                                        objective_payout
                                      +   (scpp.activity_amt - scp.objective_amount)
                                        * scp.objective_bonus_payout
                                        / scp.objective_bonus_increment
                                   WHEN scp.objective_bonus_payout IS NULL AND scpp.activity_amt >= scp.objective_amount THEN objective_payout
                                   ELSE
                                      0
                                END) total_potential_payout,
                                 SUM( CASE WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
                                   WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout
                                   ELSE 0
                                END  ) total_objective_payout,
                                 SUM(               CASE WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
                                                         WHEN CASE                    --03/06/Bug # 59992
                                   WHEN scpp.activity_amt > scp.objective_amount
                                   THEN
                                           (scpp.activity_amt - scp.objective_amount)
                                        * scp.objective_bonus_payout
                                        / scp.objective_bonus_increment
                                   ELSE
                                      0
                                END  > OBJECTIVE_BONUS_CAP THEN OBJECTIVE_BONUS_CAP
                                ELSE CASE
                                   WHEN scpp.activity_amt > scp.objective_amount
                                   THEN
                                           (scpp.activity_amt - scp.objective_amount)
                                        * scp.objective_bonus_payout
                                        / scp.objective_bonus_increment
                                   ELSE
                                      0
                                END END) total_bonus_payout
                        FROM ssi_contest sc,
                             ssi_contest_pax_progress scpp,
                             ssi_contest_participant scp,
                             application_user au,
                             participant p
                       WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                             AND sc.ssi_contest_id = scp.ssi_contest_id
                             AND scp.user_id = au.user_id
                             AND au.is_active = 1
                             AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                             AND scp.user_id = scpp.user_id(+)
                             AND au.user_id = p.user_id
                 AND EXISTS   --02/04/2015  IF Manager
                 (select nab.user_id
                                  from gtt_node_and_below_users nab
                                  where nab.user_id = scp.user_id
                 );

                            OPEN p_out_dtgt_ref_cursor FOR
                            select * from dual where 1=2;

                             OPEN p_out_total_dtgt_ref_cursor FOR
                             select * from dual where 1=2;

                            OPEN p_out_siu_ref_cursor FOR
                            select * from dual where 1=2;

                             OPEN p_out_siu_total_ref_cursor FOR
                             select * from dual where 1=2;


                ELSIF v_out_contest_type = 2 THEN --If contest_type = 2 (Do This Get That)
                OPEN p_out_obj_ref_cursor FOR
                select * from dual where 1=2;

                 OPEN p_out_obj_total_ref_cursor FOR
                 select * from dual where 1=2;

                 OPEN p_out_siu_ref_cursor FOR
                select * from dual where 1=2;

                 OPEN p_out_siu_total_ref_cursor FOR
                 select * from dual where 1=2;

               DELETE FROM gtt_ssi_dtgt;

                INSERT INTO gtt_ssi_dtgt
                SELECT ssi_contest_id,
                        ssi_contest_activity_id,
                        description,
                        user_id,
                        activity_amt,
                       CASE WHEN activity_amt > min_qualifier THEN
                       FLOOR ((
                       CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt
                       ELSE  maximum_payout_activity END- min_qualifier)/increment_amount)
                       ELSE 0 END payout_quantity,
                       CASE WHEN activity_amt > min_qualifier THEN
                       FLOOR(( --03/24/2015 Bug 60668 Added Floor Function
                       CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt
                       ELSE  maximum_payout_activity END- min_qualifier)/increment_amount) * payout_amount
                       ELSE 0 END payout_value
                       FROM (
            SELECT sc.ssi_contest_id,
                   sca.ssi_contest_activity_id,
                   sca.description,
                   scp.user_id,
                   NVL((SELECT activity_amt
                      FROM ssi_contest_pax_progress scpp
                     WHERE     scpp.ssi_contest_activity_id = sca.ssi_contest_activity_id
                           AND scpp.user_id = scp.user_id),0) activity_amt,
                     sca.increment_amount,
                     sca.payout_amount,
                     sca.min_qualifier,
                     sca.payout_cap_amount,
                     sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount AS maximum_payout_activity
              FROM ssi_contest sc, ssi_contest_activity sca, ssi_contest_participant scp,application_user au
             WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                   AND sc.ssi_contest_id = sca.ssi_contest_id
                   AND sc.ssi_contest_id = scp.ssi_contest_id
                   AND scp.user_id = au.user_id
                   AND au.is_active = 1
                   AND EXISTS   --02/04/2015  IF Manager
                      (select nab.user_id
                                  from gtt_node_and_below_users nab
                                  where nab.user_id = scp.user_id
                      ) ORDER BY user_id asc,ssi_contest_activity_id asc
                   ) ;

                 l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                               FROM (
                   WITH t
                 AS (SELECT ROW_NUMBER () OVER (PARTITION BY USER_ID ORDER BY Activity_sort) rn,
                            user_id,
                            ssi_contest_id contest_id, --05/28/2015
                            ssi_contest_activity_id Activity1,
                            description Activity_description1,
                            payout_value payout_value1,
                            activity_amt activity_amt1,
                            payout_quantity payout_quantity1,
                            LEAD (ssi_contest_activity_id)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity2,
                            LEAD (description)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description2,
                            LEAD (payout_value)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value2,
                            LEAD (activity_amt)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt2,
                            LEAD (payout_quantity)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity2,
                            LEAD (ssi_contest_activity_id, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity3,
                               LEAD (description,2)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description3,
                            LEAD (payout_value, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value3,
                            LEAD (activity_amt, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt3,
                            LEAD (payout_quantity, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity3 ,
                            LEAD (ssi_contest_activity_id, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity4,
                               LEAD (description,3)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description4,
                            LEAD (payout_value, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value4,
                            LEAD (activity_amt, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt4,
                            LEAD (payout_quantity, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity4,
                            LEAD (ssi_contest_activity_id, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity5,
                               LEAD (description,4)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description5,
                            LEAD (payout_value, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value5,
                            LEAD (activity_amt, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt5,
                            LEAD (payout_quantity, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity5,
                            LEAD (ssi_contest_activity_id, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity6,
                               LEAD (description,5)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description6,
                            LEAD (payout_value, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value6,
                            LEAD (activity_amt, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt6,
                            LEAD (payout_quantity, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity6,
                            LEAD (ssi_contest_activity_id, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity7,
                               LEAD (description,6)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description7,
                            LEAD (payout_value, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value7,
                            LEAD (activity_amt, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt7,
                            LEAD (payout_quantity, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity7,
                            LEAD (ssi_contest_activity_id, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity8,
                               LEAD (description,7)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description8,
                            LEAD (payout_value, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value8,
                            LEAD (activity_amt, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt8,
                            LEAD (payout_quantity, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity8,
                            LEAD (ssi_contest_activity_id, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity9,
                               LEAD (description,8)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description9,
                            LEAD (payout_value, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value9,
                            LEAD (activity_amt, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt9,
                            LEAD (payout_quantity, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity9 ,
                            LEAD (ssi_contest_activity_id, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity10,
                               LEAD (description,9)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity_description10,
                            LEAD (payout_value, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value10,
                            LEAD (activity_amt, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt10,
                            LEAD (payout_quantity, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity10
                                        FROM (SELECT ROWNUM Activity_sort, S.* FROM (
                        SELECT * FROM gtt_ssi_dtgt ORDER BY user_id,ssi_contest_activity_id) s
                   ))
            SELECT t.user_id,au.last_name,au.first_name,n.name,
                   contest_id, --05/28/2015
                   Activity1,
                   Activity_description1,
                   payout_value1,
                   activity_amt1,
                   payout_quantity1,
                   Activity2,
                   Activity_description2,
                   payout_value2,
                   activity_amt2,
                   payout_quantity2,
                   Activity3,
                   Activity_description3,
                   payout_value3,
                   activity_amt3,
                   payout_quantity3,
                   Activity4,
                   Activity_description4,
                   payout_value4,
                   activity_amt4,
                   payout_quantity4,
                   Activity5,
                   Activity_description5,
                   payout_value5,
                   activity_amt5,
                   payout_quantity5,
                   Activity6,
                   Activity_description6,
                   payout_value6,
                   activity_amt6,
                   payout_quantity6,
                   Activity7,
                   Activity_description7,
                   payout_value7,
                   activity_amt7,
                   payout_quantity7,
                   Activity8,
                   Activity_description8,
                   payout_value8,
                   activity_amt8,
                   payout_quantity8,
                   Activity9,
                   Activity_description9,
                   payout_value9,
                   activity_amt9,
                   payout_quantity9,
                   Activity10,
                   Activity_description10,
                   payout_value10,
                   activity_amt10,
                   payout_quantity10,
                   (NVL(payout_value1,0)+NVL(payout_value2,0)+NVL(payout_value3,0)+NVL(payout_value4,0)+NVL(payout_value5,0)+NVL(payout_value6,0)+NVL(payout_value7,0)+NVL(payout_value8,0)+nvl(payout_value9,0)+NVL(payout_value10,0)) total_payout_value
              FROM t,application_user au, user_node un,node n
             WHERE rn = 1
             AND t.user_id = au.user_id
             AND au.user_id = un.user_id
             AND au.is_active = 1
             AND un.node_id = n.node_id
             AND un.is_primary = 1
             AND un.status = 1  ORDER BY '|| v_sortCol ||'
                ) RS) WHERE RN >= ' ||p_in_rowNumStart||' AND RN   <= '|| p_in_rowNumEnd;

            OPEN p_out_dtgt_ref_cursor FOR l_query;

                    OPEN p_out_total_dtgt_ref_cursor FOR
                       SELECT SUM(payout_value1) payout_value_total1,SUM(activity_amt1) activity_amt_total1,SUM(payout_quantity1) payout_quantity_total1,
                        SUM(payout_value2) payout_value_total2,SUM(activity_amt2) activity_amt_total2,SUM(payout_quantity2) payout_quantity_total2,
                        SUM(payout_value3) payout_value_total3,SUM(activity_amt3) activity_amt_total3,SUM(payout_quantity3) payout_quantity_total3,
                        SUM(payout_value4) payout_value_total4,SUM(activity_amt4) activity_amt_total4,SUM(payout_quantity4) payout_quantity_total4,
                        SUM(payout_value5) payout_value_total5,SUM(activity_amt5) activity_amt_total5,SUM(payout_quantity5) payout_quantity_total5,
                        SUM(payout_value6) payout_value_total6,SUM(activity_amt6) activity_amt_total6,SUM(payout_quantity6) payout_quantity_total6,
                        SUM(payout_value7) payout_value_total7,SUM(activity_amt7) activity_amt_total7,SUM(payout_quantity7) payout_quantity_total7,
                        SUM(payout_value8) payout_value_total8,SUM(activity_amt8) activity_amt_total8,SUM(payout_quantity8) payout_quantity_total8,
                        SUM(payout_value9) payout_value_total9,SUM(activity_amt9) activity_amt_total9,SUM(payout_quantity9) payout_quantity_total9,
                        SUM(payout_value10) payout_value_total10,SUM(activity_amt10) activity_amt_total10,SUM(payout_quantity10) payout_quantity_total10,
                        SUM(total_payout_value) total_payout_value FROM (WITH t
                 AS (SELECT ROW_NUMBER () OVER (PARTITION BY USER_ID ORDER BY Activity_sort) rn,
                            user_id,
                            ssi_contest_activity_id Activity1,
                            payout_value payout_value1,
                            activity_amt activity_amt1,
                            payout_quantity payout_quantity1,
                            LEAD (ssi_contest_activity_id)
                               OVER (PARTITION BY USER_ID ORDER BY Activity_sort)
                               Activity2,
                            LEAD (payout_value)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value2,
                            LEAD (activity_amt)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt2,
                            LEAD (payout_quantity)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity2,
                            LEAD (ssi_contest_activity_id, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity3,
                            LEAD (payout_value, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value3,
                            LEAD (activity_amt, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt3,
                            LEAD (payout_quantity, 2)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity3 ,
                            LEAD (ssi_contest_activity_id, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity4,
                            LEAD (payout_value, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value4,
                            LEAD (activity_amt, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt4,
                            LEAD (payout_quantity, 3)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity4,
                            LEAD (ssi_contest_activity_id, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity5,
                            LEAD (payout_value, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value5,
                            LEAD (activity_amt, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt5,
                            LEAD (payout_quantity, 4)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity5,
                            LEAD (ssi_contest_activity_id, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity6,
                            LEAD (payout_value, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value6,
                            LEAD (activity_amt, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt6,
                            LEAD (payout_quantity, 5)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity6,
                            LEAD (ssi_contest_activity_id, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity7,
                            LEAD (payout_value, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value7,
                            LEAD (activity_amt, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt7,
                            LEAD (payout_quantity, 6)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity7,
                            LEAD (ssi_contest_activity_id, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity8,
                            LEAD (payout_value, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value8,
                            LEAD (activity_amt, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt8,
                            LEAD (payout_quantity, 7)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity8,
                            LEAD (ssi_contest_activity_id, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity9,
                            LEAD (payout_value, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value9,
                            LEAD (activity_amt, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt9,
                            LEAD (payout_quantity, 8)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity9 ,
                            LEAD (ssi_contest_activity_id, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               Activity10,
                            LEAD (payout_value, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_value10,
                            LEAD (activity_amt, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               activity_amt10,
                            LEAD (payout_quantity, 9)
                               OVER (PARTITION BY user_id ORDER BY Activity_sort)
                               payout_quantity10
                                    FROM (SELECT ROWNUM Activity_sort, S.* FROM (
                        SELECT * FROM gtt_ssi_dtgt ORDER BY user_id,ssi_contest_activity_id) s
                   ))
            SELECT t.user_id,
                   Activity1,
                   payout_value1,
                   activity_amt1,
                   payout_quantity1,
                   Activity2,
                   payout_value2,
                   activity_amt2,
                   payout_quantity2,
                   Activity3,
                   payout_value3,
                   activity_amt3,
                   payout_quantity3,
                   Activity4,
                   payout_value4,
                   activity_amt4,
                   payout_quantity4,
                   Activity5,
                   payout_value5,
                   activity_amt5,
                   payout_quantity5,
                   Activity6,
                   payout_value6,
                   activity_amt6,
                   payout_quantity6,
                   Activity7,
                   payout_value7,
                   activity_amt7,
                   payout_quantity7,
                   Activity8,
                   payout_value8,
                   activity_amt8,
                   payout_quantity8,
                   Activity9,
                   payout_value9,
                   activity_amt9,
                   payout_quantity9,
                   Activity10,
                   payout_value10,
                   activity_amt10,
                   payout_quantity10,
                   (NVL(payout_value1,0)+NVL(payout_value2,0)+NVL(payout_value3,0)+NVL(payout_value4,0)+NVL(payout_value5,0)+NVL(payout_value6,0)+NVL(payout_value7,0)+NVL(payout_value8,0)+nvl(payout_value9,0)+NVL(payout_value10,0)) total_payout_value
              FROM t
             WHERE rn = 1);

             ELSIF v_out_contest_type = 16 THEN

             l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                               FROM (
             SELECT sc.ssi_contest_id contest_id, --05/28/2015
                        au.user_id,
                        au.last_name,
                        au.first_name,
                        n.name org_name,
                        NVL(sc.activity_amt,0) activity_amt,
                        NVL(sc.level_completed,0) level_completed, --Bug # 60745 03/25/2015
                        sc.level_payout,
                        sc.bonus_payout,
                        sc.total_payout
                        ,sc.siu_baseline_amount, --Bug #61306 04/22/2015
                         sc.payout_description --05/11/2015 Bug 62116
                              FROM (
 SELECT DISTINCT ssi_contest_id,user_id,activity_amt,CASE WHEN level_completed =0 THEN NULL ELSE level_completed END level_completed,level_payout,
 CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                             ELSE bonus_payout END,0)
 ELSE 0
 END bonus_payout,
 level_payout + CASE WHEN include_bonus = 1 THEN (CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                             ELSE bonus_payout END,0)
 ELSE 0
 END) ELSE 0 END total_payout  --05/20/2015 Bug 62354
,siu_baseline_amount, --Bug #61306 04/22/2015
 CASE WHEN level_completed = 0 then NULL
 ELSE payout_description
 END payout_description --05/11/2015 Bug 62116 --05/19/2015 Bug 62316
 FROM (
SELECT sc.ssi_contest_id,scp.user_id,scpp.activity_amt, scl.sequence_number,scl.goal_amount,CASE WHEN sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END level_completed,
RANK() OVER (PARTITION BY scp.user_id ORDER BY CASE WHEN sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank,
CASE WHEN sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount THEN scl.payout_amount
          WHEN sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.payout_amount
          WHEN sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.payout_amount ELSE 0 END level_payout,
CASE WHEN sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment)   * sit_bonus_payout
         WHEN sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment)* sit_bonus_payout
         WHEN sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment) * sit_bonus_payout
         ELSE 0
         END bonus_payout,
         (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = '''||p_in_ssi_contest_id||''') max_level
         ,scp.siu_baseline_amount --Bug #61306 04/22/2015
         ,sit_bonus_cap,
          scl.payout_description, --05/11/2015 Bug 62116
          sc.include_bonus -- 05/20/2015 Bug 62354
  FROM ssi_contest sc,
       ssi_contest_level scl,
       ssi_contest_participant scp,
       ssi_contest_pax_progress scpp
 WHERE     sc.ssi_contest_id = '''||p_in_ssi_contest_id||'''
       AND sc.ssi_contest_id = scl.ssi_contest_id
       AND sc.ssi_contest_id = scp.ssi_contest_id
       AND scp.user_id = scpp.user_id(+)
       AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
       AND EXISTS        --02/04/2015  IF Manager
                                 (select nab.user_id
                                  from gtt_node_and_below_users nab
                                  where nab.user_id = scp.user_id
                                  )
       ) WHERE rec_rank = 1 ) sc, application_user au, participant p,user_node un, node n
       WHERE sc.user_id = au.user_id
       AND sc.user_id = p.user_id
       AND sc.user_id = un.user_id
       AND un.status = 1
       AND un.is_primary = 1
       AND au.is_active = 1
       AND un.node_id = n.node_id
       ORDER BY '|| v_sortCol ||'
                ) RS) WHERE RN >= ' ||p_in_rowNumStart||' AND RN   <= '|| p_in_rowNumEnd;

                 OPEN p_out_siu_ref_cursor FOR l_query;

                 OPEN p_out_siu_total_ref_cursor FOR
                 SELECT SUM(activity_amt) activity_amt, SUM(level_payout) level_payout,
                 SUM(CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                             ELSE bonus_payout END,0)
 ELSE 0
 END) bonus_payout,
 SUM(level_payout + CASE WHEN include_bonus =1 THEN (CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                             ELSE bonus_payout END,0)
 ELSE 0
 END) ELSE 0 END) total_payout,                 --Bug # 61622
 SUM (siu_baseline_amount) as siu_baseline_amount--05/14/2015 Bug 62200
                   FROM (
SELECT DISTINCT ssi_contest_id,user_id,activity_amt,level_completed,level_payout, --03/06/2015 Bug # 59977
 bonus_payout,max_level,sit_bonus_cap
 ,siu_baseline_amount,  --Bug #61306 04/22/2015
 include_bonus -- 05/20/2015 Bug 62354
 FROM (SELECT sc.ssi_contest_id,scp.user_id,scpp.activity_amt, scl.sequence_number,scl.goal_amount,CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END level_completed,
RANK() OVER (PARTITION BY scp.user_id ORDER BY CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank,
CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.payout_amount
          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.payout_amount
          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.payout_amount ELSE 0 END level_payout,
CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment)   * sit_bonus_payout
         WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment)* sit_bonus_payout
         WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment) * sit_bonus_payout
         ELSE 0
         END bonus_payout,
         (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = p_in_ssi_contest_id) max_level
         ,scp.siu_baseline_amount --Bug #61306 04/22/2015
         ,sit_bonus_cap,
          scl.payout_description, --05/11/2015 Bug 62116
          sc.include_bonus -- 05/20/2015 Bug 62354
  FROM ssi_contest sc,
       ssi_contest_level scl,
       ssi_contest_participant scp,
       ssi_contest_pax_progress scpp
 WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
       AND sc.ssi_contest_id = scl.ssi_contest_id
       AND sc.ssi_contest_id = scp.ssi_contest_id
       AND scp.user_id = scpp.user_id(+)
       AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
       AND EXISTS        --02/04/2015  IF Manager
                                 (select nab.user_id
                                  from gtt_node_and_below_users nab
                                  where nab.user_id = scp.user_id
                                  )
       ) WHERE rec_rank = 1) ;

                 OPEN p_out_obj_ref_cursor FOR
                select * from dual where 1=2;

                 OPEN p_out_obj_total_ref_cursor FOR
                 select * from dual where 1=2;

                 OPEN p_out_dtgt_ref_cursor FOR
                select * from dual where 1=2;

                 OPEN p_out_total_dtgt_ref_cursor FOR
                 select * from dual where 1=2;

              END IF; --If the contest_type !=2
              SELECT COUNT(1) INTO  p_out_size_data
            FROM ssi_contest_participant scp, application_user au
           WHERE     scp.ssi_contest_id = p_in_ssi_contest_id
             AND scp.user_id = au.user_id
             AND au.is_active = 1
           AND EXISTS        --02/04/2015  IF Manager
                                 (select nab.user_id
                                  from gtt_node_and_below_users nab
                                  where nab.user_id = scp.user_id
                                  )   ;
            END IF; --02/04/2015 IF Creator/Manager


     p_out_contest_type  := v_out_contest_type;
      p_out_return_code :=0;

      EXCEPTION WHEN OTHERS THEN
       p_out_return_code :=99;
       p_out_contest_type := NULL;
       prc_execution_log_entry ('prc_ssi_contest_pax_list',1,'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| SQLERRM,NULL);

      END prc_ssi_contest_pax_list;

      PROCEDURE prc_ssi_atn_issuance_sum (p_in_ssi_contest_id     IN     NUMBER,
      p_in_sortColName     IN     VARCHAR2,
      p_in_sortedBy           IN     VARCHAR2,
      p_in_rowNumStart               IN NUMBER,
      p_in_rowNumEnd                 IN NUMBER,
      p_out_issuance_count  OUT NUMBER,
      p_out_return_code    OUT NUMBER,
      p_out_ref_cursor OUT SYS_REFCURSOR
      ) IS

        /***********************************************************************************
      Purpose:  procedure for summary data of AwardThemNow contest.

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
     Ravi Dhanekula   1/26/2015     Initial Version
     Ravi Dhanekula   03/19/2015   Bug # 60525.
   ************************************************************************************/

      l_query VARCHAR2(32767);
      v_sortCol             VARCHAR2(200);
      v_issuance_count NUMBER;


      BEGIN

        SELECT COUNT(1) INTO v_issuance_count FROM ssi_contest_atn WHERE ssi_contest_id = p_in_ssi_contest_id  AND issuance_status NOT IN ('inProgress','cancelled' );

      p_out_issuance_count  := v_issuance_count;

      v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;

        l_query  := 'SELECT * FROM
  ( ';

      l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                               FROM (
     SELECT atn.issuance_date                        AS dateCreated,
    NVL(SUM(scp.objective_amount),0)               AS amount,
    NVL(SUM(scp.objective_payout),0)               AS payoutAmount,
    atn.issuance_status                            AS status,
    CASE WHEN p.payoutdescriptionCount = 1 THEN (SELECT objective_payout_description  FROM ssi_contest_participant
    WHERE ssi_contest_id = '''||p_in_ssi_contest_id||''' AND ROWNUM <2)
    ELSE ''Varies by Participant'' END   AS payoutDescription, --03/19/2015   Bug # 60525.
    p.participantsCount                            AS participantsCount,
    sc.payout_type,
    atn.denied_reason,
    CASE WHEN p.payoutdescriptionCount > 1 THEN 0
    ELSE 1
    END payoutdescriptionCount,
    atn.issuance_number,
    atn.approval_level_action_taken,
    atn.approved_by_level1
  FROM ssi_contest_participant scp,
    ssi_contest_atn atn,
    ssi_contest sc,
    application_user au,
    (SELECT COUNT(1) AS participantsCount,
      ssi_contest_id,
      award_issuance_number,
      COUNT(DISTINCT objective_payout_description ) AS payoutdescriptionCount
    FROM ssi_contest_participant
    WHERE ssi_contest_id = '''||p_in_ssi_contest_id||'''
    GROUP BY ssi_contest_id,
      award_issuance_number
    )p
  WHERE scp.ssi_contest_id     = atn.ssi_contest_id
  AND p.ssi_contest_id         = atn.ssi_contest_id
  AND p.award_issuance_number  = atn.issuance_number
  AND scp.award_issuance_number=atn.issuance_number
  AND scp.user_id = au.user_id
  AND au.is_active = 1
  AND atn.issuance_status NOT IN (''inProgress'',''cancelled'' )
  AND scp.ssi_contest_id       = '''||p_in_ssi_contest_id||'''
  AND atn.ssi_contest_id = sc.ssi_contest_id
  GROUP BY atn.issuance_number,atn.approval_level_action_taken, atn.approved_by_level1 ,
    atn.issuance_date,
    atn.issuance_status,
    p.participantsCount,
    sc.payout_type,
    atn.denied_reason,
    p.payoutdescriptionCount
    ORDER BY '|| v_sortCol ||'
                ) RS) WHERE RN >= ' ||p_in_rowNumStart||' AND RN   <= '|| p_in_rowNumEnd;

OPEN p_out_ref_cursor FOR l_query;

      p_out_return_code:=0;

      EXCEPTION WHEN OTHERS THEN
      p_out_return_code :=99;
      p_out_issuance_count :=0;
       prc_execution_log_entry ('prc_ssi_atn_issuance_sum',1,'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| SQLERRM,NULL);
      END prc_ssi_atn_issuance_sum;

      PROCEDURE prc_ssi_contest_export (p_in_ssi_contest_id     IN     NUMBER,
      p_in_user_id             IN NUMBER,    --02/04/2015
      p_in_locale  IN VARCHAR2,
      p_out_return_code    OUT NUMBER,
      p_out_ref_cursor OUT SYS_REFCURSOR)

   /***********************************************************************************
      Purpose:  Procedure for SSI Extracts

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
    Suresh J           02/17/2015       Initial Version
    Suresh J           03/09/2015       Added Export for Stack Rank and Fixed issues related to column order and display
    Suresh J           03/09/2015       Bug Fix 60149 - Issue of Objective Description being NULL in Objective extract
    Suresh J           03/10/2015       Bug Fix 60206 - Fixed issue with the calculation for Increment and Total Potential Payout
    Suresh J           03/20/2015       Bug Fix 60280  - Participant name is displayed more then once.
    Swati              03/24/2015       Bug 60612 - Manager view-Objective contest-Bonus payout is calculated wrongly in extract report
    Swati              03/25/2015       Bug 60667 - Contest creator view - DTGT - Extract Issues
    Swati              03/26/2015       Bug 60706 - Creator view-Step it up contest-Prior to first progress-Extract-'N' should be caps in the word no for baseline column
    Swati              03/26/2015       Bug 60709 - Creator view-Update Results-After progress load-Payout value is not displayed in extract report
    Swati              03/30/2015       Bug 60835 - Manager View-DTGT-After progress load-Extract report-Increment field displays more than four decimal values
    Swati              03/31/2015       Bug 60827 - Creator view-DTGT-After Progress load-Extract report-'Total Potential Payout Value' is incorrect in extract report
    Swati              03/31/2015       Bug 60868 - Creator view-DTGT-Final Results-Extract Report-Issues in extract report
    Suresh J           04/02/2015       Bug 60964 - SIU 'Bonus Payout' column shouldn't be shown when not inclded in the contest
    Swati              04/07/2015       Bug 60871 - Creator view-DTGT-Final results-'Payout Issued' text appears when payout is not done for that activity
    Suresh J           04/07/2015       Bug 61118 - SR - Not all the pax are shown on the screen
    Swati              05/20/2015       Bug 62357 - Creator view-Step it up-After progress load-Extract report displays payout description when no level is completed
    Suresh J          05/20/2015       Bug 62187 - Added 3 new columns Manager first, last names and Activity Date in Objectives,SR, SIU and DTGT contest
    Ravi Dhanekula    06/16/2015       Bug # 62842 -Creator view-After final results-Extract is displayed as 'Total Potential Payout'
   Suresh J           09/03/2015      Bug 63812 - if there are no winners, we should not display payout description or payout value.  
   ************************************************************************************/

      IS

      -- constants
      c_delimiter CONSTANT VARCHAR2(1) := ',' ;
      c2_delimiter CONSTANT VARCHAR2(1) := '"' ;

      v_out_contest_type NUMBER(18);
      v_payout_type VARCHAR2(30);
      v_include_stack_rank  ssi_contest.include_stack_rank%TYPE;
      v_include_bonus  ssi_contest.include_bonus%TYPE;
      v_contest_end_date ssi_contest.contest_end_date%TYPE; --03/31/2015 Bug 60868
      v_status ssi_contest.status%TYPE; --03/31/2015 Bug 60868
      v_activity_measure_type SSI_CONTEST.activity_measure_type%TYPE;
      v_locale_date_format VARCHAR2(100):= fnc_java_get_date_pattern(p_in_locale); -- bug 69033 09/23/2016 

      --Contest Types
--      c_award_them_now constant number := 1;
      c_do_this_get_that constant number := 2;
      c_objectives constant number := 4;
      c_stack_rank constant number := 8;
      c_step_it_up constant number := 16;

      BEGIN

      SELECT contest_type,payout_type,NVL(include_stack_rank,0),NVL(include_bonus,0),contest_end_date,status,activity_measure_type  --03/31/2015 Bug 60868   --05/06/2015
        INTO v_out_contest_type,v_payout_type,v_include_stack_rank,v_include_bonus,v_contest_end_date,v_status,v_activity_measure_type  --03/31/2015 Bug 60868 --05/06/2015
        FROM ssi_contest WHERE ssi_contest_id = p_in_ssi_contest_id;

      DELETE temp_table_session;
                -- 09/15/2016 Bug 68030 
      INSERT INTO temp_table_session (asset_code, cms_name, cms_code)
        SELECT t2.asset_code,
               CASE WHEN t1.cms_value IS NULL THEN t2.cms_value ELSE t1.cms_value END cms_value,
               t2.key
          FROM
                (SELECT asset_code,cms_value,key
                   FROM vw_cms_asset_value
                  WHERE key in ('LAST_NAME','FIRST_NAME','PAX','ORG_UNIT','OBJECTIVE','PER_TO_OBJECTIVE','OBJECTIVE_PAYOUT','BONUS_PAYOUT',
                                'TOTAL_POTENTIAL_PAYOUT','PAYOUT_VALUE','TOTAL_POTENTIAL_VALUE','ACTIVITY','PAYOUT_QUANTITY',
                                'TOTAL_POTENTIAL_POINTS','LEVEL_COMPLETED','LEVEL_PAYOUT','TOTAL_PAYOUT','ACTIVITY_DESCRIPTION','TO_GO','PAYOUT_DESCRIPTION',
                                'MINIMUM_QUALIFIER','INCREMENT','TOTAL_POTENTIAL_PAYOUT_VALUE','STACK_RANK','BASE_LINE','ACTIVITY_QUALIFIED_PAYOUT','RANK','POTENTIAL_PAYOUT',
                                'TOTAL_PAYOUT_VALUE','TOTAL_PAYOUT','TOTAL_VALUE','TOTAL_POINTS','TOTAL_POTENTIAL_VALUE','TOTAL_POTENTIAL_POINTS','OBJECTIVES_DESCRIPTION',
                                'ACTIVITY_DATE','MANAGER_FIRST_NAME','MANAGER_LAST_NAME','DATE_SUBMITTED')
                    AND asset_code in ('ssi_contest.details.export','ssi_contest.participant','ssi_contest.preview','ssi_contest.payout_objectives',
                                       'ssi_contest.payout_stepitup','ssi_contest.payout_stackrank','ssi_contest.creator','ssi_contest.claims')
                    AND locale = p_in_locale) t1,
                (SELECT asset_code,cms_value,key
                   FROM vw_cms_asset_value
                  WHERE key in ('LAST_NAME','FIRST_NAME','PAX','ORG_UNIT','OBJECTIVE','PER_TO_OBJECTIVE','OBJECTIVE_PAYOUT','BONUS_PAYOUT',
                                'TOTAL_POTENTIAL_PAYOUT','PAYOUT_VALUE','TOTAL_POTENTIAL_VALUE','ACTIVITY','PAYOUT_QUANTITY',
                                'TOTAL_POTENTIAL_POINTS','LEVEL_COMPLETED','LEVEL_PAYOUT','TOTAL_PAYOUT','ACTIVITY_DESCRIPTION','TO_GO','PAYOUT_DESCRIPTION',
                                'MINIMUM_QUALIFIER','INCREMENT','TOTAL_POTENTIAL_PAYOUT_VALUE','STACK_RANK','BASE_LINE','ACTIVITY_QUALIFIED_PAYOUT','RANK','POTENTIAL_PAYOUT',
                                'TOTAL_PAYOUT_VALUE','TOTAL_PAYOUT','TOTAL_VALUE','TOTAL_POINTS','TOTAL_POTENTIAL_VALUE','TOTAL_POTENTIAL_POINTS','OBJECTIVES_DESCRIPTION',
                                'ACTIVITY_DATE','MANAGER_FIRST_NAME','MANAGER_LAST_NAME','DATE_SUBMITTED')
                    AND asset_code in ('ssi_contest.details.export','ssi_contest.participant','ssi_contest.preview','ssi_contest.payout_objectives',
                                       'ssi_contest.payout_stepitup','ssi_contest.payout_stackrank','ssi_contest.creator','ssi_contest.claims')                
                    AND locale = 'en_US') t2
         WHERE t2.asset_code = t1.asset_code (+) 
           AND t2.key = t1.key (+);

IF p_in_user_id IS NULL THEN   --IF Creator
      IF v_out_contest_type = c_objectives THEN
             OPEN p_out_ref_cursor FOR
     SELECT textline FROM (
        SELECT 1,
     CASE
     WHEN v_payout_type = 'points' THEN
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'DATE_SUBMITTED' and  asset_code = 'ssi_contest.claims')||c2_delimiter||c_delimiter|| --09/15/2016
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVES_DESCRIPTION' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||   --04/14/2015
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVE' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PER_TO_OBJECTIVE' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TO_GO' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVE_PAYOUT' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        CASE WHEN v_include_bonus = 1 THEN
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'BONUS_PAYOUT' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter
        END||
        CASE WHEN v_status = 'finalize_results' THEN   --06/16/2015
                        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_PAYOUT' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter
                ELSE
                        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POTENTIAL_PAYOUT' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter
                END||
        CASE WHEN v_include_stack_rank = 1 THEN
           c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
        END
     WHEN v_payout_type = 'other' THEN
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'DATE_SUBMITTED' and  asset_code = 'ssi_contest.claims')||c2_delimiter||c_delimiter||         --09/15/2016
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVES_DESCRIPTION' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||   --04/14/2015
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVE' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PER_TO_OBJECTIVE' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TO_GO' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_DESCRIPTION' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_VALUE' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
        CASE WHEN v_include_stack_rank = 1 THEN
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
        END
     END AS Textline
     FROM dual
      UNION  ALL
      SELECT
        (ROWNUM+1),
   CASE
   WHEN v_payout_type = 'points' THEN
        c2_delimiter||first_name||c2_delimiter||c_delimiter||
        c2_delimiter||last_name||c2_delimiter||c_delimiter||
        c2_delimiter||org_unit||c2_delimiter||c_delimiter||
        c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
        c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
        c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter|| -- bug 69033 09/23/2016  --05/20/2015
        c2_delimiter||activity_description||c2_delimiter||c_delimiter||    --03/02/2015
        c2_delimiter||objective_amount||c2_delimiter||c_delimiter||
        c2_delimiter||activity_amt||c2_delimiter||c_delimiter||
        c2_delimiter||perc_achieved||c2_delimiter||c_delimiter||
        c2_delimiter||to_go||c2_delimiter||c_delimiter||               --03/02/2015
        c2_delimiter||objective_payout||c2_delimiter||c_delimiter||
        CASE WHEN v_include_bonus = 1 THEN
        c2_delimiter||bonus_payout||c2_delimiter||c_delimiter
        END||
        c2_delimiter||potential_payout||c2_delimiter||c_delimiter||
        CASE WHEN v_include_stack_rank = 1 THEN
        c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
        END
   WHEN v_payout_type = 'other' THEN
        c2_delimiter||first_name||c2_delimiter||c_delimiter||
        c2_delimiter||last_name||c2_delimiter||c_delimiter||
        c2_delimiter||org_unit||c2_delimiter||c_delimiter||
        c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
        c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
        c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter|| -- bug 69033 09/23/2016  --05/20/2015
        c2_delimiter||activity_description||c2_delimiter||c_delimiter||    --03/02/2015
        c2_delimiter||objective_amount||c2_delimiter||c_delimiter||
        c2_delimiter||activity_amt||c2_delimiter||c_delimiter||
        c2_delimiter||perc_achieved||c2_delimiter||c_delimiter||
        c2_delimiter||to_go||c2_delimiter||c_delimiter||               --03/02/2015
        c2_delimiter||payout_description||c2_delimiter||c_delimiter||   --03/02/2015
        c2_delimiter||Payout_Value||c2_delimiter||c_delimiter||
        CASE WHEN v_include_stack_rank = 1 THEN
        c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
        END
     END AS Textline
        FROM --04/01/2015 Bug 60612 Starts
        (SELECT first_name,last_name,org_unit,activity_description,
                mgr_first_name,   --05/20/2015
                mgr_last_name,      --05/20/2015
                Activity_As_Of_Date,   --05/20/2015
                CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(objective_amount,'999999999D99')) ELSE trim(to_char(objective_amount,'999999999D9999')) END objective_amount,  --05/06/2015
--              objective_amount
                CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(activity_amt,'999999999D99')) ELSE trim(to_char(activity_amt,'999999999D9999')) END activity_amt,  --05/06/2015
--              activity_amt,
                perc_achieved,to_go,
                payout_description,Payout_Value,objective_payout,
                bonus_payout,
                CASE WHEN bonus_payout IS NOT NULL
                    THEN objective_payout + bonus_payout
                  ELSE objective_payout
                  END potential_payout,
                stack_rank_position
        FROM --04/01/2015 Bug 60612 Ends
        (select au.first_name,
             au.last_name,
             n.name org_unit,
             au_mgr.first_name as mgr_first_name,   --05/20/2015
             au_mgr.last_name as mgr_last_name,      --05/20/2015
             scpp.activity_date as Activity_As_Of_Date,   --05/20/2015
             NVL(sc.activity_description,scp.activity_description) as activity_description,   --09/03/2015
             scp.objective_amount,
             scpp.activity_amt,
             FLOOR(scpp.activity_amt / scp.objective_amount * 100) AS perc_achieved,
             CASE WHEN NVL(scpp.activity_amt,0)<=scp.objective_amount THEN (scp.objective_amount - NVL(scpp.activity_amt,0))
                ELSE 0
             END to_go,
             scp.objective_payout_description as payout_description,
             CASE WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout
                    ELSE
                    0
             END Payout_Value,
             CASE WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout
               ELSE 0
             END objective_payout,
              CASE WHEN
                CASE
                   WHEN scpp.activity_amt > scp.objective_amount
                   THEN
                          FLOOR ((scpp.activity_amt - scp.objective_amount)
                        / scp.objective_bonus_increment ) * scp.objective_bonus_payout
                   ELSE
                      0
                END  > OBJECTIVE_BONUS_CAP THEN OBJECTIVE_BONUS_CAP
              ELSE CASE
                   WHEN scpp.activity_amt > scp.objective_amount
                   THEN
                           FLOOR ((scpp.activity_amt - scp.objective_amount)
                        / scp.objective_bonus_increment ) * scp.objective_bonus_payout
                   ELSE
                      0
            END END AS bonus_payout, --04/01/2015 Bug 60612
                  scpsr.stack_rank_position as stack_rank_position
        from
                     ssi_contest sc,
                     ssi_contest_participant scp,
                     ssi_contest_pax_progress scpp,
                     ssi_contest_pax_stack_rank scpsr,
                     application_user au,
                     node n,
                     user_node un
                    ,(select um.node_id,mgr.user_id,mgr.last_name,mgr.first_name
                        from application_user mgr,
                             user_node um
                             where mgr.user_id = um.user_id
                                   and um.role = 'own'
                                 --  and um.is_primary = 1    --  09/29/2016    Bug 69286
                                   and mgr.is_active = 1) au_mgr  --05/20/2015
              where      sc.ssi_contest_id = p_in_ssi_contest_id
                     and sc.ssi_contest_id = scp.ssi_contest_id
                     and scp.ssi_contest_id = scpp.ssi_contest_id (+)
                     and scp.ssi_contest_id = scpsr.ssi_contest_id (+)
                     and scp.user_id = scpp.user_id (+)
                     and scp.user_id = scpsr.user_id  (+)
                     and scp.user_id = au.user_id
                     and un.user_id  = au.user_id
                     and au.is_active = 1
                     and un.node_id  = n.node_id
                     and un.is_primary = 1
                     and un.node_id = au_mgr.node_id (+)  --05/20/2015
                 ORDER BY au.last_name
                     )));

      ELSIF v_out_contest_type = c_do_this_get_that THEN
              OPEN p_out_ref_cursor  FOR
             SELECT textline FROM (
                SELECT 1,
            CASE
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DATE' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||                --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MINIMUM_QUALIFIER' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||--03/31/2015 Bug 60868
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_QUALIFIED_PAYOUT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'INCREMENT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                CASE WHEN v_contest_end_date <= SYSDATE AND v_status = 'finalize_results' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POINTS' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                ELSE
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POTENTIAL_POINTS' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END||--03/31/2015 Bug 60868 --04/07/2015 Bug 60871
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DATE' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||                --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MINIMUM_QUALIFIER' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||--03/31/2015 Bug 60868
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_QUALIFIED_PAYOUT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'INCREMENT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_QUANTITY' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
                CASE WHEN v_contest_end_date <= SYSDATE AND v_status = 'finalize_results' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_VALUE' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                ELSE
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POTENTIAL_VALUE' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END||--03/31/2015 Bug 60868 --04/07/2015 Bug 60871
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END
            END AS Textline
                FROM dual
              UNION  ALL
              SELECT
                (ROWNUM+1),
            CASE
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
                c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
                c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter|| -- bug 69033 09/23/2016  --05/20/2015
                c2_delimiter||activity_description||c2_delimiter||c_delimiter||         --03/02/2015
                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||
                c2_delimiter||min_qualifier||c2_delimiter||c_delimiter||                --03/02/2015
                c2_delimiter||qualified_activity||c2_delimiter||c_delimiter||           --03/02/2015
                c2_delimiter||increment_dtgt||c2_delimiter||c_delimiter||             --03/02/2015   --03/10/2015
                c2_delimiter||payout_value||c2_delimiter||c_delimiter||       --03/02/2015
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter            --03/02/2015
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
                c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
                c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter|| -- bug 69033 09/23/2016  --05/20/2015
                c2_delimiter||activity_description||c2_delimiter||c_delimiter||         --03/02/2015
                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||
                c2_delimiter||min_qualifier||c2_delimiter||c_delimiter||                --03/02/2015
                c2_delimiter||qualified_activity||c2_delimiter||c_delimiter||             --03/02/2015
                c2_delimiter||increment_dtgt||c2_delimiter||c_delimiter||             --03/02/2015   --03/10/2015
                c2_delimiter||payout_description||c2_delimiter||c_delimiter||           --03/02/2015
                c2_delimiter||payout_quantity||c2_delimiter||c_delimiter||
                c2_delimiter||Total_Potential_Payout_Value||c2_delimiter||c_delimiter|| --03/02/2015
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
                END
            END  AS Textline
                FROM
                (      select au.first_name,
                     au.last_name,
                     n.name org_unit,
                    au_mgr.first_name as mgr_first_name,   --05/20/2015
                    au_mgr.last_name as mgr_last_name,      --05/20/2015
                    sc.activity_date as Activity_As_Of_Date,   --05/20/2015
                     sc.activity_description,
--                     sc.activity_amt,
                     CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(sc.activity_amt,'999999999D99')) ELSE trim(to_char(sc.activity_amt,'999999999D9999')) END activity_amt,  --05/06/2015
                     sc.min_qualifier,
                       CASE WHEN sc.activity_amt > sc.min_qualifier THEN
                       FLOOR((--03/25/2015 Bug 60667
                         CASE WHEN sc.activity_amt <= sc.maximum_payout_activity THEN sc.activity_amt
                         ELSE  sc.maximum_payout_activity END- sc.min_qualifier)/increment_amount)* payout_amount
                       ELSE 0 END payout_value,
                     CASE WHEN activity_amt > min_qualifier THEN
                              FLOOR(( --03/31/2015 Bug 60827
                               CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt
                               ELSE  maximum_payout_activity END- min_qualifier)/increment_amount)* payout_amount
                               ELSE 0
                     END Total_Potential_Payout_Value,
                     CASE WHEN activity_amt > min_qualifier THEN
                         FLOOR ((
                         CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt
                         ELSE  maximum_payout_activity END- min_qualifier)/increment_amount)
                         ELSE 0
                     END  payout_quantity,
                     CASE WHEN activity_amt > min_qualifier THEN
                     (activity_amt - min_qualifier)
                     ELSE 0
                     END qualified_activity,
                       NVL(sc.payout_description,sc.objective_payout_description) as payout_description,
                     sc.increment_amount,
                     CASE WHEN sc.increment_amount> 0 THEN
                     FLOOR((CASE WHEN activity_amt > min_qualifier THEN (activity_amt - min_qualifier) ELSE 0 END)/sc.increment_amount )--03/25/2015 Bug 60667
                     ELSE 0
                     END AS increment_dtgt ,   --03/10/2015
                    CASE WHEN include_stack_rank = 1 THEN stack_rank_position ELSE NULL END stack_rank_position
                FROM  (
                SELECT sc.ssi_contest_id,
                           sca.ssi_contest_activity_id,
                           sca.description activity_description,
                           scp.user_id,
                           sc.payout_type,
                           scp.objective_payout_description,
                           sca.payout_description,
                           NVL((SELECT activity_amt
                              FROM ssi_contest_pax_progress scpp
                             WHERE     scpp.ssi_contest_activity_id = sca.ssi_contest_activity_id
                                   AND scpp.user_id = scp.user_id),0) activity_amt,
                            (SELECT activity_date
                              FROM ssi_contest_pax_progress scpp
                             WHERE     scpp.ssi_contest_activity_id = sca.ssi_contest_activity_id
                                   AND scpp.user_id = scp.user_id) activity_date,   --05/20/2015
                             sca.increment_amount,
                             sca.payout_amount,
                             sca.min_qualifier,
                             sca.payout_cap_amount,
                             sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount AS maximum_payout_activity,
                             (SELECT stack_rank_position FROM ssi_contest_pax_stack_rank WHERE ssi_contest_id = p_in_ssi_contest_id AND ssi_contest_activity_id = sca.ssi_contest_activity_id AND user_id = scp.user_id ) stack_rank_position
                             ,sc.include_stack_rank
                      FROM ssi_contest sc, ssi_contest_activity sca, ssi_contest_participant scp
                     WHERE
                           sc.ssi_contest_id = p_in_ssi_contest_id and
                           sc.ssi_contest_id = sca.ssi_contest_id
                           AND sc.ssi_contest_id = scp.ssi_contest_id
                           ) sc,
                             application_user au,
                             node n,
                             user_node un
                               ,(select um.node_id,mgr.user_id,mgr.last_name,mgr.first_name
                                    from application_user mgr,
                                         user_node um
                                         where mgr.user_id = um.user_id
                                               and um.role = 'own'
                                          --  and um.is_primary = 1   -- 09/29/2016    Bug 69286
                                               and mgr.is_active = 1) au_mgr  --05/20/2015
                      where   sc.user_id = au.user_id
                             and un.user_id  = au.user_id
                             and un.node_id  = n.node_id
                             and un.is_primary = 1
                             and au.is_active = 1
                             and un.node_id = au_mgr.node_id (+)  --05/20/2015
                          ORDER BY au.last_name
                             ));

      ELSIF v_out_contest_type = c_step_it_up THEN
            OPEN p_out_ref_cursor FOR
             SELECT textline FROM (
                SELECT 1,
            CASE
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DATE' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||                --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'BASE_LINE' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LEVEL_COMPLETED' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LEVEL_PAYOUT' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                CASE WHEN v_include_bonus = 1 THEN  --04/02/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'BONUS_PAYOUT' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter
                END||
--                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POTENTIAL_PAYOUT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||    --Bug# 61602 04/21/2015
                CASE WHEN v_contest_end_date <= SYSDATE AND v_status = 'finalize_results' THEN   --04/21/2015
                        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_PAYOUT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter   --Bug# 61602 04/21/2015
                ELSE
                        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POTENTIAL_PAYOUT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END||
                CASE WHEN v_include_stack_rank = 1 THEN
                        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DATE' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||                --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'BASE_LINE' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LEVEL_COMPLETED' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_VALUE' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END
            END AS Textline
                FROM dual
              UNION  ALL
              SELECT
                (ROWNUM+1),
            CASE
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
                c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
                c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter||    -- bug 69033 09/23/2016  --05/20/2015
                c2_delimiter||siu_baseline_amount||c2_delimiter||c_delimiter||    --03/02/2015 --03/26/2015 Bug 60706  --Bug 61635 04/23/2015
                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||
                c2_delimiter||level_completed||c2_delimiter||c_delimiter||
                c2_delimiter||level_payout||c2_delimiter||c_delimiter||
                CASE WHEN v_include_bonus = 1 THEN   --04/02/2015
                c2_delimiter||bonus_payout||c2_delimiter||c_delimiter
                END||
                c2_delimiter||total_potential_payout||c2_delimiter||c_delimiter||  --total_payout
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
                END
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
                c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
                c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter||    -- bug 69033 09/23/2016  --05/20/2015
                c2_delimiter||siu_baseline_amount||c2_delimiter||c_delimiter||    --03/02/2015 --03/26/2015 Bug 60706 --Bug 61635 04/23/2015
                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||
                c2_delimiter||level_completed||c2_delimiter||c_delimiter||
                c2_delimiter||payout_description||c2_delimiter||c_delimiter||  --03/02/2015
                c2_delimiter||total_potential_payout||c2_delimiter||c_delimiter||  --Payout Value **Need to check whether it is a valid column for SIU
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
                END
               END AS Textline
                FROM
                (     SELECT         au.first_name,au.last_name,
                                n.name org_unit,
                                au_mgr.first_name as mgr_first_name,   --05/20/2015
                                au_mgr.last_name as mgr_last_name,      --05/20/2015
                                sc.activity_date as Activity_As_Of_Date,   --05/20/2015
                                sit_indv_baseline_type baseline,
--                                sc.activity_amt,
                                CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(sc.activity_amt,'999999999D99')) ELSE trim(to_char(sc.activity_amt,'999999999D9999')) END activity_amt,  --05/06/2015
                                nvl(sc.level_completed,0) level_completed, --03/26/2015 Bug 60709
                                sc.level_payout,
                                sc.bonus_payout,
                                sc.total_payout total_potential_payout,
                                sc.payout_description as payout_description,
                                sc.stack_rank_position   --03/02/2015
                                ,siu_baseline_amount --Bug 61635 04/23/2015
                                ,sc.activity_date --05/20/2015
                                FROM (
             SELECT DISTINCT ssi_contest_id,
                             user_id,
                             activity_amt,
                             sit_indv_baseline_type,
                             payout_type,
                             CASE WHEN level_completed =0 THEN NULL ELSE payout_description END payout_description, --05/20/2015 Bug 62357
                             CASE WHEN level_completed =0 THEN NULL ELSE level_completed END level_completed,
                             level_payout,
                             CASE WHEN LEVEL_COMPLETED  = max_level THEN
                                CASE WHEN bonus_payout > sit_bonus_cap THEN
                                          sit_bonus_cap
                                     ELSE bonus_payout
                                END
                             ELSE 0 END bonus_payout,   --05/06/2015
                             level_payout + CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                             ELSE bonus_payout END,0)
                                            ELSE 0
                             END  AS total_payout --03/26/2015 Bug 60709
                             ,stack_rank_position  --03/02/2015
                             ,CASE WHEN level_completed = 0 THEN sequence_number     --03/20/2015
                                  ELSE rec_rank
                             END rec_rank
                             ,siu_baseline_amount --Bug 61635 04/23/2015
                             ,activity_date --05/20/2015
                             FROM (
                    SELECT sc.ssi_contest_id,
                           scp.user_id,
                           scpp.activity_amt,
                           sc.payout_type,
                           scl.payout_description,
                           scl.sequence_number,
                           scl.goal_amount,
                           CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
                                WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
                                WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number
                                ELSE 0
                                END level_completed,
                                RANK() OVER (
                                  PARTITION BY scp.user_id ORDER BY
                                  CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
                                       WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
                                       WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number
                                       ELSE 0
                                  END  DESC) as rec_rank,
                                --03/26/2015 Bug 60709 Starts
                                  CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.payout_amount
                                      WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.payout_amount
                                      WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount)
                                      THEN scl.payout_amount
                                      ELSE 0
                                  END level_payout,
                                  CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN sit_bonus_cap
                                     WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment)   * sit_bonus_payout
                                     WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN sit_bonus_cap
                                     WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment)* sit_bonus_payout
                                     WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap
                                     WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment) * sit_bonus_payout
                                     ELSE 0
                                  END bonus_payout, --03/26/2015 Bug 60709 Ends
                                (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = p_in_ssi_contest_id) max_level,
                                sit_indv_baseline_type,
                                sit_bonus_cap,--03/26/2015 Bug 60709
                               CASE WHEN sc.include_stack_rank = 1 THEN (SELECT stack_rank_position FROM ssi_contest_pax_stack_rank WHERE ssi_contest_id = p_in_ssi_contest_id AND user_id = scp.user_id ) END stack_rank_position   --03/02/2015
                                ,scp.siu_baseline_amount --Bug 61635 04/23/2015
                                ,scpp.activity_date  --05/20/2015
                          FROM ssi_contest sc,
                               ssi_contest_level scl,
                               ssi_contest_participant scp,
                               ssi_contest_pax_progress scpp
                         WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                               AND sc.ssi_contest_id = scl.ssi_contest_id
                               AND sc.ssi_contest_id = scp.ssi_contest_id
                               AND scp.user_id = scpp.user_id(+)
                               AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                               ) WHERE rec_rank = 1 ) sc, application_user au, participant p,user_node un, node n
                               ,(select um.node_id,mgr.user_id,mgr.last_name,mgr.first_name
                                    from application_user mgr,
                                         user_node um
                                         where mgr.user_id = um.user_id
                                               and um.role = 'own'
                                          --  and um.is_primary = 1   -- 09/29/2016    Bug 69286
                                               and mgr.is_active = 1) au_mgr  --05/20/2015
                               WHERE sc.user_id = au.user_id
                               AND sc.user_id = p.user_id
                               AND sc.user_id = un.user_id
                               AND un.status = 1
                               AND un.is_primary = 1
                               AND au.is_active = 1
                               AND un.node_id = n.node_id
                               AND un.node_id = au_mgr.node_id (+)  --05/20/2015
                               AND rec_rank = 1    --03/20/2015
                    ORDER BY au.last_name
                               ));

      ELSIF v_out_contest_type = c_stack_rank THEN    --03/09/2015
            OPEN p_out_ref_cursor FOR
             SELECT textline FROM (
                SELECT 1,
            CASE
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DATE' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||                --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'RANK' and  asset_code = 'ssi_contest.payout_stackrank')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'POTENTIAL_PAYOUT' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DATE' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||                --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'RANK' and  asset_code = 'ssi_contest.payout_stackrank')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_VALUE' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter
            END AS Textline
                FROM dual
              UNION  ALL
              SELECT
                (ROWNUM+1),
            CASE
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
                c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
                c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter||    -- bug 69033 09/23/2016  --05/20/2015
--                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||  --05/06/2015
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter||
                c2_delimiter||CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(activity_amt,'999999999D99')) ELSE trim(to_char(activity_amt,'999999999D9999')) END||c2_delimiter||c_delimiter||  --05/06/2015
                c2_delimiter||total_payout||c2_delimiter||c_delimiter
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
                c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
                c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter||    -- bug 69033 09/23/2016  --05/20/2015
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter||
--                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||  --05/06/2015
                c2_delimiter||CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(activity_amt,'999999999D99')) ELSE trim(to_char(activity_amt,'999999999D9999')) END||c2_delimiter||c_delimiter||  --05/06/2015
                c2_delimiter||payout_description||c2_delimiter||c_delimiter||
                c2_delimiter||total_payout||c2_delimiter||c_delimiter
               END AS Textline
                FROM
                (SELECT  scpsr.stack_rank_position ,
                   au.user_id,
                   au.last_name,
                   au.first_name,
                   n.name as org_unit,
                   au_mgr.first_name as mgr_first_name,   --05/20/2015
                   au_mgr.last_name as mgr_last_name,      --05/20/2015
                   scpp.activity_date as Activity_As_Of_Date,   --05/20/2015
                   scpp.activity_amt,
                   sc.activity_description as description,
                  CASE WHEN v_status = 'finalize_results'-- AND v_payout_type = 'points'
                  THEN NVL(ssip.payout_amount,0)   --09/03/2015  Bug 63812
                       ELSE scsrp.payout_amount 
                  END  as total_payout,   --05/28/2015
--                  scsrp.payout_desc as payout_description,    --09/03/2015  Bug 63812
                  CASE WHEN v_status = 'finalize_results'
                  THEN CASE WHEN NVL(ssip.payout_amount,0) = 0 THEN NULL ELSE scsrp.payout_desc END
                       ELSE scsrp.payout_desc 
                  END  AS payout_description,   --09/03/2015  Bug 63812
                   sc.activity_measure_type
            FROM ssi_contest_pax_stack_rank scpsr,
                 ssi_contest_pax_progress scpp,
                 ssi_contest_sr_payout scsrp,
                 application_user au,
                 ssi_contest sc,
                 node n,
                 user_node un
                ,(select um.node_id,mgr.user_id,mgr.last_name,mgr.first_name
                    from application_user mgr,
                         user_node um
                         where mgr.user_id = um.user_id
                               and um.role = 'own'
                           --  and um.is_primary = 1   -- 09/29/2016    Bug 69286
                               and mgr.is_active = 1) au_mgr  --05/20/2015
                 ,ssi_contest_pax_payout ssip  --05/28/2015                             
            WHERE
                 sc.ssi_contest_id = p_in_ssi_contest_id
             AND sc.ssi_contest_id = scpsr.ssi_contest_id        --04/07/2015
             AND sc.ssi_contest_id = scsrp.ssi_contest_id
             AND scpsr.ssi_contest_id = scpp.ssi_contest_id (+)    --04/07/2015
             AND scpsr.user_id = scpp.user_id (+)    --04/07/2015
             AND scpsr.user_id = au.user_id
             AND scpsr.stack_rank_position = scsrp.rank_position
             AND un.user_id  = au.user_id
             AND un.node_id  = n.node_id
             AND un.is_primary = 1
             AND au.is_active = 1
             AND un.node_id = au_mgr.node_id (+)  --05/20/2015
             AND scpsr.ssi_contest_id = ssip.ssi_contest_id (+)   --05/28/2015
             AND scpsr.user_id = ssip.user_id (+)       --05/28/2015
                 UNION ALL  --03/20/2015 Bug # 60466
                 SELECT  scpsr.stack_rank_position ,
                   au.user_id,
                   au.last_name,
                   au.first_name,
                   n.name as org_unit,
                   au_mgr.first_name as mgr_first_name,   --05/20/2015
                   au_mgr.last_name as mgr_last_name,      --05/20/2015
                   scpp.activity_date as Activity_As_Of_Date,   --05/20/2015
                   scpp.activity_amt,
                   sc.activity_description as description,
                  0  as total_payout,
                  NULL as payout_description,
                   sc.activity_measure_type
            FROM ssi_contest_pax_stack_rank scpsr,
                 ssi_contest_pax_progress scpp,
                 application_user au,
                 ssi_contest sc,
                 node n,
                 user_node un
                ,(select um.node_id,mgr.user_id,mgr.last_name,mgr.first_name
                    from application_user mgr,
                         user_node um
                         where mgr.user_id = um.user_id
                               and um.role = 'own'
                          --  and um.is_primary = 1   -- 09/29/2016    Bug 69286
                               and mgr.is_active = 1) au_mgr  --05/20/2015
            WHERE
                     sc.ssi_contest_id = p_in_ssi_contest_id
                 AND sc.ssi_contest_id = scpsr.ssi_contest_id   --04/07/2015
                 AND sc.ssi_contest_id = scpp.ssi_contest_id
                 AND scpsr.ssi_contest_id = scpp.ssi_contest_id (+)  --04/07/2015
                 AND scpsr.user_id = scpp.user_id (+)  --04/07/2015
                 AND scpsr.user_id = au.user_id
                 AND un.user_id  = au.user_id
                 AND un.node_id  = n.node_id
                 AND au.is_active = 1
                 AND un.is_primary = 1
                 AND NOT EXISTS (SELECT * FROM ssi_contest_sr_payout WHERE ssi_contest_id = p_in_ssi_contest_id AND rank_position = scpsr.stack_rank_position)
                 AND un.node_id = au_mgr.node_id (+)  --05/20/2015
                 ORDER BY stack_rank_position            --05/19/2015 Bug 62315
                 ));
        END IF;  -- End of Contest Type Extract SQL Condition

     ELSIF p_in_user_id IS NOT NULL THEN   -- IF Manager
      DELETE FROM gtt_node_and_below_users;

      INSERT INTO gtt_node_and_below_users (user_id)--Bug # 61461
      SELECT   DISTINCT au.user_id
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
                                                                       (SELECT node_id,NAME as node_name,parent_node_id,path
                                                                        FROM node n
                                                                        START WITH parent_node_id IS NULL
                                                                        CONNECT BY PRIOR node_id = parent_node_id) ip
                                                                        ,user_node un
                                                                        ,application_user au
                                        where ip.node_id = npn.path_node_id
                                              AND npn.node_id = un.node_id
                                              AND un.user_id = au.user_id
                                              AND ip.node_id IN ((select node_id from user_node where user_id = p_in_user_id and status = 1 and role in ('own','mgr')))
                                              AND au.is_active = 1
                                              AND au.user_type = 'pax'
                                              AND un.status = 1;

      IF v_out_contest_type = c_objectives THEN

             OPEN p_out_ref_cursor FOR
     SELECT textline FROM (
        SELECT 1,
     CASE
     WHEN v_payout_type = 'points' THEN
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DATE' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||                --05/20/2015
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVES_DESCRIPTION' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||   --04/14/2015
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVE' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PER_TO_OBJECTIVE' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TO_GO' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVE_PAYOUT' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        CASE WHEN v_include_bonus = 1 THEN
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'BONUS_PAYOUT' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter
        END||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POTENTIAL_PAYOUT' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        CASE WHEN v_include_stack_rank = 1 THEN
           c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
        END
     WHEN v_payout_type = 'other' THEN
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DATE' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||                --05/20/2015
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVES_DESCRIPTION' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||   --04/14/2015
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVE' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PER_TO_OBJECTIVE' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TO_GO' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_DESCRIPTION' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_VALUE' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
        CASE WHEN v_include_stack_rank = 1 THEN
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
        END
     END AS Textline
     FROM dual
      UNION  ALL
      SELECT
        (ROWNUM+1),
   CASE
   WHEN v_payout_type = 'points' THEN
        c2_delimiter||first_name||c2_delimiter||c_delimiter||
        c2_delimiter||last_name||c2_delimiter||c_delimiter||
        c2_delimiter||org_unit||c2_delimiter||c_delimiter||
        c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
        c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
        c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter||    -- bug 69033 09/23/2016  --05/20/2015
        c2_delimiter||activity_description||c2_delimiter||c_delimiter||    --03/02/2015
        c2_delimiter||objective_amount||c2_delimiter||c_delimiter||
        c2_delimiter||activity_amt||c2_delimiter||c_delimiter||
        c2_delimiter||perc_achieved||c2_delimiter||c_delimiter||
        c2_delimiter||to_go||c2_delimiter||c_delimiter||               --03/02/2015
        c2_delimiter||objective_payout||c2_delimiter||c_delimiter||
        CASE WHEN v_include_bonus = 1 THEN
        c2_delimiter||bonus_payout||c2_delimiter||c_delimiter
        END||
        c2_delimiter||potential_payout||c2_delimiter||c_delimiter||
        CASE WHEN v_include_stack_rank = 1 THEN
        c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
        END
   WHEN v_payout_type = 'other' THEN
        c2_delimiter||first_name||c2_delimiter||c_delimiter||
        c2_delimiter||last_name||c2_delimiter||c_delimiter||
        c2_delimiter||org_unit||c2_delimiter||c_delimiter||
        c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
        c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
        c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter||    -- bug 69033 09/23/2016  --05/20/2015
        c2_delimiter||activity_description||c2_delimiter||c_delimiter||    --03/02/2015
        c2_delimiter||objective_amount||c2_delimiter||c_delimiter||
        c2_delimiter||activity_amt||c2_delimiter||c_delimiter||
        c2_delimiter||perc_achieved||c2_delimiter||c_delimiter||
        c2_delimiter||to_go||c2_delimiter||c_delimiter||               --03/02/2015
        c2_delimiter||payout_description||c2_delimiter||c_delimiter||   --03/02/2015
        c2_delimiter||Payout_Value||c2_delimiter||c_delimiter||
        CASE WHEN v_include_stack_rank = 1 THEN
        c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
        END
     END AS Textline
        FROM --03/24/2015 Bug 60612 Starts
        (SELECT first_name,last_name,org_unit,
                mgr_first_name,   --05/20/2015
                mgr_last_name,      --05/20/2015
                Activity_As_Of_Date,   --05/20/2015
        activity_description,
                CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(objective_amount,'999999999D99')) ELSE trim(to_char(objective_amount,'999999999D9999')) END objective_amount,  --05/06/2015
--              objective_amount
                CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(activity_amt,'999999999D99')) ELSE trim(to_char(activity_amt,'999999999D9999')) END activity_amt,  --05/06/2015
--              activity_amt,
                perc_achieved,to_go,
                payout_description,Payout_Value,objective_payout,
                bonus_payout,
                CASE WHEN bonus_payout IS NOT NULL
                    THEN objective_payout + bonus_payout
                  ELSE objective_payout
                  END potential_payout,
                stack_rank_position
        FROM --03/24/2015 Bug 60612 Ends
        (select au.first_name,
             au.last_name,
             n.name org_unit,
             au_mgr.first_name as mgr_first_name,   --05/20/2015
             au_mgr.last_name as mgr_last_name,      --05/20/2015
             scpp.activity_date as Activity_As_Of_Date,   --05/20/2015
             NVL(sc.activity_description,scp.activity_description) as activity_description,   --09/03/2015
             scp.objective_amount,
             scpp.activity_amt,
             FLOOR(scpp.activity_amt / scp.objective_amount * 100) AS perc_achieved,
             CASE WHEN NVL(scpp.activity_amt,0)<=scp.objective_amount THEN (scp.objective_amount - NVL(scpp.activity_amt,0))
                ELSE 0
             END to_go,
             scp.objective_payout_description as payout_description,
             CASE WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout
                    ELSE
                    0
             END Payout_Value,
             CASE WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout
               ELSE 0
             END objective_payout,
                CASE WHEN CASE --03/24/2015 Bug 60612 Starts
                           WHEN scpp.activity_amt > scp.objective_amount
                           THEN
                                   FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment) * scp.objective_bonus_payout
                           ELSE
                              0
                        END  > OBJECTIVE_BONUS_CAP THEN OBJECTIVE_BONUS_CAP
                    ELSE CASE
                           WHEN scpp.activity_amt > scp.objective_amount
                           THEN
                                   FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment)* scp.objective_bonus_payout
                           ELSE
                              0
                    END END AS bonus_payout, --03/24/2015 Bug 60612 ENDS
                  scpsr.stack_rank_position as stack_rank_position
        from
                     ssi_contest sc,
                     ssi_contest_participant scp,
                     ssi_contest_pax_progress scpp,
                     ssi_contest_pax_stack_rank scpsr,
                     application_user au,
                     node n,
                     user_node un
                    ,(select um.node_id,mgr.user_id,mgr.last_name,mgr.first_name
                        from application_user mgr,
                             user_node um
                             where mgr.user_id = um.user_id
                                   and um.role = 'own'
                              --  and um.is_primary = 1   -- 09/29/2016    Bug 69286
                                   and mgr.is_active = 1) au_mgr  --05/20/2015
              where      sc.ssi_contest_id = p_in_ssi_contest_id
                     and sc.ssi_contest_id = scp.ssi_contest_id
                     and scp.ssi_contest_id = scpp.ssi_contest_id (+)
                     and scp.ssi_contest_id = scpsr.ssi_contest_id (+)
                     and scp.user_id = scpp.user_id (+)
                     and scp.user_id = scpsr.user_id  (+)
                     and scp.user_id = au.user_id
                     and exists
                         (select   nab.user_id
                          from  gtt_node_and_below_users nab
                          where nab.user_id = scp.user_id
                         )
                     and un.user_id  = au.user_id
                     and un.node_id  = n.node_id
                     and un.is_primary = 1
                     and au.is_active = 1
                     and un.node_id = au_mgr.node_id (+)  --05/20/2015
                 ORDER BY au.last_name
                     )));

      ELSIF v_out_contest_type = c_do_this_get_that THEN

              OPEN p_out_ref_cursor  FOR
             SELECT textline FROM (
                SELECT 1,
            CASE
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DATE' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||                --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MINIMUM_QUALIFIER' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||--03/31/2015 Bug 60868
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_QUALIFIED_PAYOUT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'INCREMENT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                CASE WHEN v_contest_end_date <= SYSDATE AND v_status = 'finalize_results' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POINTS' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                ELSE
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POTENTIAL_POINTS' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END||--03/31/2015 Bug 60868 --04/07/2015 Bug 60871
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DATE' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||                --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MINIMUM_QUALIFIER' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||--03/31/2015 Bug 60868
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_QUALIFIED_PAYOUT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'INCREMENT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_QUANTITY' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
                CASE WHEN v_contest_end_date <= SYSDATE AND v_status = 'finalize_results' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_VALUE' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                ELSE
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POTENTIAL_VALUE' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END||--03/31/2015 Bug 60868 --04/07/2015 Bug 60871
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END
            END AS Textline
                FROM dual
              UNION  ALL
              SELECT
                (ROWNUM+1),
            CASE
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
                c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
                c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter||    -- bug 69033 09/23/2016  --05/20/2015
                c2_delimiter||activity_description||c2_delimiter||c_delimiter||         --03/02/2015
                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||
                c2_delimiter||min_qualifier||c2_delimiter||c_delimiter||                --03/02/2015
                c2_delimiter||qualified_activity||c2_delimiter||c_delimiter||           --03/02/2015
                c2_delimiter||increment_dtgt||c2_delimiter||c_delimiter||             --03/02/2015     --03/10/2015
                c2_delimiter||payout_value||c2_delimiter||c_delimiter||       --03/02/2015
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter            --03/02/2015
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
                c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
                c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter||    -- bug 69033 09/23/2016  --05/20/2015
                c2_delimiter||activity_description||c2_delimiter||c_delimiter||         --03/02/2015
                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||
                c2_delimiter||min_qualifier||c2_delimiter||c_delimiter||                --03/02/2015
                c2_delimiter||qualified_activity||c2_delimiter||c_delimiter||             --03/02/2015
                c2_delimiter||increment_dtgt||c2_delimiter||c_delimiter||             --03/02/2015  --03/10/2015
                c2_delimiter||payout_description||c2_delimiter||c_delimiter||           --03/02/2015
                c2_delimiter||payout_quantity||c2_delimiter||c_delimiter||
                c2_delimiter||Total_Potential_Payout_Value||c2_delimiter||c_delimiter|| --03/02/2015
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
                END
            END  AS Textline
                FROM
                (      select au.first_name,
                     au.last_name,
                     n.name org_unit,
                    au_mgr.first_name as mgr_first_name,   --05/20/2015
                    au_mgr.last_name as mgr_last_name,      --05/20/2015
                    sc.activity_date as Activity_As_Of_Date,   --05/20/2015
                     sc.activity_description,
--                     sc.activity_amt,
                     CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(sc.activity_amt,'999999999D99')) ELSE trim(to_char(sc.activity_amt,'999999999D9999')) END activity_amt,  --05/06/2015
                     sc.min_qualifier,
                       CASE WHEN sc.activity_amt > sc.min_qualifier THEN
                       (
                         CASE WHEN sc.activity_amt <= sc.maximum_payout_activity THEN sc.activity_amt
                         ELSE  sc.maximum_payout_activity END- sc.min_qualifier)/increment_amount* payout_amount
                       ELSE 0 END payout_value,
                     CASE WHEN activity_amt > min_qualifier THEN
                               FLOOR(( --03/31/2015 Bug 60827
                               CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt
                               ELSE  maximum_payout_activity END- min_qualifier)/increment_amount)* payout_amount
                               ELSE 0
                     END Total_Potential_Payout_Value,
                     CASE WHEN activity_amt > min_qualifier THEN
                         FLOOR ((
                         CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt
                         ELSE  maximum_payout_activity END- min_qualifier)/increment_amount)
                         ELSE 0
                     END  payout_quantity,
                     CASE WHEN activity_amt > min_qualifier THEN
                     (activity_amt - min_qualifier)
                     ELSE 0
                     END qualified_activity,
                       NVL(sc.payout_description,sc.objective_payout_description) as payout_description,
                     sc.increment_amount,
                     CASE WHEN sc.increment_amount> 0 THEN
                     FLOOR((CASE WHEN activity_amt > min_qualifier THEN (activity_amt - min_qualifier) ELSE 0 END)/sc.increment_amount )--03/30/2015 Bug 60835
                     ELSE 0
                     END AS increment_dtgt ,   --03/10/2015
                    CASE WHEN include_stack_rank = 1 THEN stack_rank_position ELSE NULL END stack_rank_position
                FROM  (
                SELECT sc.ssi_contest_id,
                           sca.ssi_contest_activity_id,
                           sca.description activity_description,
                           scp.user_id,
                           sc.payout_type,
                           scp.objective_payout_description,
                           sca.payout_description,
                           NVL((SELECT activity_amt
                              FROM ssi_contest_pax_progress scpp
                             WHERE     scpp.ssi_contest_activity_id = sca.ssi_contest_activity_id
                                   AND scpp.user_id = scp.user_id),0) activity_amt,
                            (SELECT activity_date
                              FROM ssi_contest_pax_progress scpp
                             WHERE     scpp.ssi_contest_activity_id = sca.ssi_contest_activity_id
                                   AND scpp.user_id = scp.user_id) activity_date,   --05/20/2015
                             sca.increment_amount,
                             sca.payout_amount,
                             sca.min_qualifier,
                             sca.payout_cap_amount,
                             sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount AS maximum_payout_activity,
                             (SELECT stack_rank_position FROM ssi_contest_pax_stack_rank WHERE ssi_contest_id = p_in_ssi_contest_id AND ssi_contest_activity_id = sca.ssi_contest_activity_id AND user_id = scp.user_id ) stack_rank_position
                             ,sc.include_stack_rank
                      FROM ssi_contest sc, ssi_contest_activity sca, ssi_contest_participant scp
                     WHERE
                           sc.ssi_contest_id = p_in_ssi_contest_id and
                           sc.ssi_contest_id = sca.ssi_contest_id
                           AND sc.ssi_contest_id = scp.ssi_contest_id
                           and exists
                                (select   nab.user_id
                                 from  gtt_node_and_below_users nab
                                 where nab.user_id = scp.user_id
                                )
                           ) sc,
                             application_user au,
                             node n,
                             user_node un
                           ,(select um.node_id,mgr.user_id,mgr.last_name,mgr.first_name
                                from application_user mgr,
                                     user_node um
                                     where mgr.user_id = um.user_id
                                           and um.role = 'own'
                                      --  and um.is_primary = 1   -- 09/29/2016    Bug 69286
                                           and mgr.is_active = 1) au_mgr  --05/20/2015
                      where   sc.user_id = au.user_id
                             and un.user_id  = au.user_id
                             and un.node_id  = n.node_id
                             and un.is_primary = 1
                             and au.is_active = 1
                             and un.node_id = au_mgr.node_id (+)  --05/20/2015
                      ORDER BY au.last_name
                             ));

      ELSIF v_out_contest_type = c_step_it_up THEN
            OPEN p_out_ref_cursor FOR
             SELECT textline FROM (
                SELECT 1,
            CASE
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DATE' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||                --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'BASE_LINE' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LEVEL_COMPLETED' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LEVEL_PAYOUT' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                CASE WHEN v_include_bonus = 1 THEN  --04/02/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'BONUS_PAYOUT' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter
                END||
--                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POTENTIAL_PAYOUT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||   --Bug# 61602 04/21/2015
                CASE WHEN v_contest_end_date <= SYSDATE AND v_status = 'finalize_results' THEN   --04/21/2015
                        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_PAYOUT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter   --Bug# 61602 04/21/2015
                ELSE
                        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POTENTIAL_PAYOUT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END||
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DATE' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||                --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'BASE_LINE' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LEVEL_COMPLETED' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_VALUE' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END
            END AS Textline
                FROM dual
              UNION  ALL
              SELECT
                (ROWNUM+1),
            CASE
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
                c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
                c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter||    -- bug 69033 09/23/2016  --05/20/2015
                c2_delimiter||siu_baseline_amount||c2_delimiter||c_delimiter||    --03/02/2015 --03/26/2015 Bug 60706  --Bug 61635 04/23/2015
                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||
                c2_delimiter||level_completed||c2_delimiter||c_delimiter||
                c2_delimiter||level_payout||c2_delimiter||c_delimiter||
                CASE WHEN v_include_bonus = 1 THEN   --04/02/2015
                c2_delimiter||bonus_payout||c2_delimiter||c_delimiter
                END||
                c2_delimiter||total_potential_payout||c2_delimiter||c_delimiter||  --total_payout
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
                END
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
                c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
                c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter||    -- bug 69033 09/23/2016  --05/20/2015
                c2_delimiter||siu_baseline_amount||c2_delimiter||c_delimiter||    --03/02/2015 --03/26/2015 Bug 60706  --Bug 61635 04/23/2015
                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||
                c2_delimiter||level_completed||c2_delimiter||c_delimiter||
                c2_delimiter||payout_description||c2_delimiter||c_delimiter||  --03/02/2015
                c2_delimiter||total_potential_payout||c2_delimiter||c_delimiter||  --Payout Value **Need to check whether it is a valid column for SIU
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
                END
               END AS Textline
                FROM
                (     SELECT         au.first_name,au.last_name,
                                n.name org_unit,
                                au_mgr.first_name as mgr_first_name,   --05/20/2015
                                au_mgr.last_name as mgr_last_name,      --05/20/2015
                                sc.activity_date as Activity_As_Of_Date,   --05/20/2015
                                sit_indv_baseline_type baseline,
--                                sc.activity_amt,
                                CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(sc.activity_amt,'999999999D99')) ELSE trim(to_char(sc.activity_amt,'999999999D9999')) END activity_amt,  --05/06/2015
                                nvl(sc.level_completed,0) level_completed,--03/26/2015 Bug 60709
                                sc.level_payout,
                                sc.bonus_payout,
                                sc.total_payout total_potential_payout,
                                sc.payout_description as payout_description,
                                sc.stack_rank_position   --03/02/2015
                                ,siu_baseline_amount --Bug 61635 04/23/2015
                                ,sc.activity_date --05/20/2015
                                FROM (
             SELECT DISTINCT ssi_contest_id,
                             user_id,
                             activity_amt,
                             sit_indv_baseline_type,
                             payout_type,
                             CASE WHEN level_completed =0 THEN NULL ELSE payout_description END payout_description, --05/20/2015 Bug 62357
                             CASE WHEN level_completed =0 THEN NULL ELSE level_completed END level_completed,
                             level_payout,
                             CASE WHEN LEVEL_COMPLETED  = max_level THEN
                                CASE WHEN bonus_payout > sit_bonus_cap THEN
                                          sit_bonus_cap
                                     ELSE bonus_payout    --Bug 62049 05/06/2015
                                END
                             ELSE 0 END bonus_payout,
                             level_payout + CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                             ELSE bonus_payout END,0)
                                            ELSE 0
                             END  AS total_payout --03/26/2015 Bug 60709  --05/06/2015
                             ,stack_rank_position  --03/02/2015
                             ,CASE WHEN level_completed = 0 THEN sequence_number     --03/20/2015
                                  ELSE rec_rank
                             END rec_rank
                            ,siu_baseline_amount --Bug 61635 04/23/2015
                             ,activity_date --05/20/2015
                             FROM (
                    SELECT sc.ssi_contest_id,
                           scp.user_id,
                           scpp.activity_amt,
                           sc.payout_type,
                           scl.payout_description,
                           scl.sequence_number,
                           scl.goal_amount,
                           CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
                                WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
                                WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number
                                ELSE 0
                                END level_completed,
                                RANK() OVER (
                                  PARTITION BY scp.user_id ORDER BY
                                  CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
                                       WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
                                       WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number
                                       ELSE 0
                                  END  DESC) as rec_rank,
                                  --03/26/2015 Bug 60709 Starts
                                  CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.payout_amount
                                    WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.payout_amount
                                    WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount)
                                    THEN scl.payout_amount ELSE 0
                                  END level_payout,
                                  CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN sit_bonus_cap
                                    WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment)   * sit_bonus_payout
                                    WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN sit_bonus_cap
                                    WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment)* sit_bonus_payout
                                    WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap
                                    WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment) * sit_bonus_payout
                                    ELSE 0
                                  END bonus_payout,--03/26/2015 Bug 60709 Ends
                                (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = p_in_ssi_contest_id) max_level,
                                sit_indv_baseline_type
                              ,CASE WHEN sc.include_stack_rank = 1 THEN (SELECT stack_rank_position FROM ssi_contest_pax_stack_rank WHERE ssi_contest_id = p_in_ssi_contest_id AND user_id = scp.user_id ) END stack_rank_position   --03/02/2015
                              ,scp.siu_baseline_amount --Bug 61635 04/23/2015
                              ,sc.sit_bonus_cap --Bug 62049 05/06/2015
                              ,scpp.activity_date  --05/20/2015
                          FROM ssi_contest sc,
                               ssi_contest_level scl,
                               ssi_contest_participant scp,
                               ssi_contest_pax_progress scpp
                         WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                               AND sc.ssi_contest_id = scl.ssi_contest_id
                               AND sc.ssi_contest_id = scp.ssi_contest_id
                               AND scp.user_id = scpp.user_id(+)
                               AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                               ) WHERE rec_rank = 1 ) sc, application_user au, participant p,user_node un, node n
                               ,(select um.node_id,mgr.user_id,mgr.last_name,mgr.first_name
                                    from application_user mgr,
                                         user_node um
                                         where mgr.user_id = um.user_id
                                               and um.role = 'own'
                                          --  and um.is_primary = 1   -- 09/29/2016    Bug 69286
                                               and mgr.is_active = 1) au_mgr   --05/20/2015
                               WHERE sc.user_id = au.user_id
                               AND au.is_active = 1
                               AND sc.user_id = p.user_id
                               AND sc.user_id = un.user_id
                               AND un.status = 1
                               AND un.is_primary = 1
                               AND un.node_id = n.node_id
                               AND rec_rank = 1    --03/20/2015
                               AND un.node_id = au_mgr.node_id (+)  --05/20/2015
                               and exists
                                   (select   nab.user_id
                                    from  gtt_node_and_below_users nab
                                    where nab.user_id = sc.user_id
                                   )
                 ORDER BY au.last_name
                                               ));

      ELSIF v_out_contest_type = c_stack_rank THEN            --03/09/2015
            OPEN p_out_ref_cursor FOR
             SELECT textline FROM (
                SELECT 1,
            CASE
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DATE' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||                --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'RANK' and  asset_code = 'ssi_contest.payout_stackrank')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'POTENTIAL_PAYOUT' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_FIRST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||     --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MANAGER_LAST_NAME' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||      --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DATE' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter||                --05/20/2015
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'RANK' and  asset_code = 'ssi_contest.payout_stackrank')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_VALUE' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter
            END AS Textline
                FROM dual
              UNION  ALL
              SELECT
                (ROWNUM+1),
            CASE
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
                c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
                c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter||    -- bug 69033 09/23/2016  --05/20/2015
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter||
--                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||  --05/06/2015
                c2_delimiter||CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(activity_amt,'999999999D99')) ELSE trim(to_char(activity_amt,'999999999D9999')) END||c2_delimiter||c_delimiter||  --05/06/2015
                c2_delimiter||total_payout||c2_delimiter||c_delimiter
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||mgr_first_name||c2_delimiter||c_delimiter||   --05/20/2015
                c2_delimiter||mgr_last_name||c2_delimiter||c_delimiter||    --05/20/2015
                c2_delimiter||TO_CHAR(Activity_As_Of_Date,v_locale_date_format)||c2_delimiter||c_delimiter||    -- bug 69033 09/23/2016  --05/20/2015
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter||
--                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||  --05/06/2015
                c2_delimiter||CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(activity_amt,'999999999D99')) ELSE trim(to_char(activity_amt,'999999999D9999')) END||c2_delimiter||c_delimiter||  --05/06/2015
                c2_delimiter||payout_description||c2_delimiter||c_delimiter||
                c2_delimiter||total_payout||c2_delimiter||c_delimiter
               END AS Textline
                FROM
                (
                SELECT  scpsr.stack_rank_position ,
                   au.user_id,
                   au.last_name,
                   au.first_name,
                   n.name as org_unit,
                   au_mgr.first_name as mgr_first_name,   --05/20/2015
                   au_mgr.last_name as mgr_last_name,      --05/20/2015
                   scpp.activity_date as Activity_As_Of_Date,   --05/20/2015
                   scpp.activity_amt,
                  sc.activity_description as description,
                  CASE WHEN v_status = 'finalize_results' AND v_payout_type = 'points' THEN NVL(ssip.payout_amount,0)    --09/03/2015 Bug 63812
                       ELSE NVL(scsrp.payout_amount,0)   --09/03/2015 Bug 63812
                  END  as total_payout,   --05/28/2015
--                  scsrp.payout_desc as payout_description,    --09/03/2015 Bug 63812
                  CASE WHEN v_status = 'finalize_results'
                  THEN CASE WHEN NVL(ssip.payout_amount,0) = 0 THEN NULL ELSE scsrp.payout_desc END
                       ELSE scsrp.payout_desc 
                  END  AS payout_description,   --09/03/2015 Bug 63812
                   sc.activity_measure_type
            FROM ssi_contest_pax_stack_rank scpsr,
                 ssi_contest_pax_progress scpp,
                 ssi_contest_sr_payout scsrp,
                 application_user au,
                 ssi_contest sc,
                 node n,
                 user_node un
                ,(select um.node_id,mgr.user_id,mgr.last_name,mgr.first_name
                    from application_user mgr,
                         user_node um
                         where mgr.user_id = um.user_id
                               and um.role = 'own'
                          --  and um.is_primary = 1   -- 09/29/2016    Bug 69286
                               and mgr.is_active = 1) au_mgr  --05/20/2015
                 ,ssi_contest_pax_payout ssip   --05/28/2015                               
            WHERE
                 sc.ssi_contest_id = p_in_ssi_contest_id
             AND sc.ssi_contest_id = scpsr.ssi_contest_id        --04/07/2015
             AND sc.ssi_contest_id = scsrp.ssi_contest_id
             AND scpsr.ssi_contest_id = scpp.ssi_contest_id (+)    --04/07/2015
             AND scpsr.user_id = scpp.user_id (+)    --04/07/2015
             AND scpsr.user_id = au.user_id
             AND scpsr.stack_rank_position = scsrp.rank_position
             AND un.user_id  = au.user_id
             AND un.node_id  = n.node_id
             AND un.is_primary = 1
             AND au.is_active = 1
             AND un.node_id = au_mgr.node_id (+)  --05/20/2015
             AND scpsr.ssi_contest_id = ssip.ssi_contest_id (+)   --05/28/2015
             AND scpsr.user_id = ssip.user_id (+)       --05/28/2015
             AND EXISTS
                   (SELECT   nab.user_id
                    FROM  gtt_node_and_below_users nab
                    WHERE nab.user_id = au.user_id
                   )
                 UNION ALL  --03/20/2015 Bug # 60466
                 SELECT  scpsr.stack_rank_position ,
                   au.user_id,
                   au.last_name,
                   au.first_name,
                   n.name as org_unit,
                   au_mgr.first_name as mgr_first_name,   --05/20/2015
                   au_mgr.last_name as mgr_last_name,      --05/20/2015
                   scpp.activity_date as Activity_As_Of_Date,   --05/20/2015
                   scpp.activity_amt,
                   sc.activity_description as description,
                  0  as total_payout,
                  NULL as payout_description,
                   sc.activity_measure_type
            FROM ssi_contest_pax_stack_rank scpsr,
                 ssi_contest_pax_progress scpp,
                 application_user au,
                 ssi_contest sc,
                 node n,
                 user_node un
                ,(select um.node_id,mgr.user_id,mgr.last_name,mgr.first_name
                    from application_user mgr,
                         user_node um
                         where mgr.user_id = um.user_id
                               and um.role = 'own'
                          --  and um.is_primary = 1   -- 09/29/2016    Bug 69286
                               and mgr.is_active = 1) au_mgr  --05/20/2015
            WHERE
                     sc.ssi_contest_id = p_in_ssi_contest_id
                 AND sc.ssi_contest_id = scpsr.ssi_contest_id   --04/07/2015
                 AND sc.ssi_contest_id = scpp.ssi_contest_id
                 AND scpsr.ssi_contest_id = scpp.ssi_contest_id (+)  --04/07/2015
                 AND scpsr.user_id = scpp.user_id (+)  --04/07/2015
                 AND scpsr.user_id = au.user_id
                 AND un.user_id  = au.user_id
                 AND un.node_id  = n.node_id
                 AND au.is_active = 1
                 AND un.is_primary = 1
                 AND un.node_id = au_mgr.node_id (+)  --05/20/2015
                 AND EXISTS
                       (SELECT   nab.user_id
                        FROM  gtt_node_and_below_users nab
                        WHERE nab.user_id = au.user_id
                       )
                 AND NOT EXISTS (SELECT * FROM ssi_contest_sr_payout WHERE ssi_contest_id = p_in_ssi_contest_id AND rank_position = scpsr.stack_rank_position)
                 ORDER BY stack_rank_position            --05/19/2015 Bug 62315
                 ));

      END IF;  -- End of Contest Type Extract SQL Condition
    END IF;   -- End of Creator/Manager condition

    p_out_return_code :=0 ;

     EXCEPTION WHEN OTHERS THEN
       p_out_return_code :=99;
       prc_execution_log_entry ('prc_ssi_contest_export',1,'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| SQLERRM,NULL);
      END prc_ssi_contest_export;

      PROCEDURE prc_ssi_contest_stackrank_list (p_in_ssi_contest_id     IN     NUMBER,
      p_in_user_id             IN NUMBER,
      is_team                    IN NUMBER,
      p_in_include_all           IN NUMBER,--06/12/2015
      p_in_contest_activity_id IN NUMBER,
      p_in_rowNumStart               IN NUMBER,
      p_in_rowNumEnd                 IN NUMBER,
      p_out_return_code    OUT NUMBER,
      p_out_pax_count      OUT NUMBER,
      p_out_ref_cursor OUT SYS_REFCURSOR)
      IS

      /***********************************************************************************
      Purpose:  Procedure to provide the stack rank detail list for a stack rank procedure.

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
     Ravi Dhanekula   1/26/2015     Initial Version
     Ravi Dhanekula   03/06/2015    Fixed the bug # 59929
     Ravi Dhanekula   03/25/2015    Bug # 60740 For the Do this Get that contest pagination issue in My Team tab
                      06/12/2015    Bug # 62545.. Added new parameter 'p_in_include_all' to determine when to exclude the pax with payout.
     Suresh J         07/17/2015   Bug #62989 - Don't show rank until progress of atleast one pax loaded.          
     Suresh J         07/31/2015    Bug # 63043 -Sorting Issue  
     Loganathan       06/18/2019    Bug 79069 - Contest Wizard - All contest Dashboards and participant results include pax 
                                     inactive and do not qualify for points.               
   ************************************************************************************/

      v_pax_count NUMBER;

      --Contest Types
      c_award_them_now constant number := 1;
      c_do_this_get_that constant number := 2;
      c_objectives constant number := 4;
      c_stack_rank constant number := 8;
      c_step_it_up constant number := 16;

      l_query VARCHAR2(32767);   --05/19/2015 Bug 62315
      v_contest_type NUMBER(18);   --05/19/2015 Bug 62315
      v_sortCol             VARCHAR2(200);  --05/19/2015 Bug 62315
      v_pax_progress_cnt  NUMBER(18);  --07/31/2015
      v_out_return_code    NUMBER;          --06/18/2019 Bug#79069 
      v_out_error_message  VARCHAR2(500);   --06/18/2019 Bug#79069 
      
      BEGIN

      SELECT contest_type
                   INTO v_contest_type FROM ssi_contest WHERE ssi_contest_id = p_in_ssi_contest_id;   --05/19/2015 Bug 62315

      IF  v_contest_type = c_stack_rank THEN  
            SELECT COUNT(*) INTO v_pax_progress_cnt FROM ssi_contest_pax_progress where ssi_contest_id = p_in_ssi_contest_id;  --07/31/2015
             
            IF v_pax_progress_cnt = 0 THEN   --07/31/2015
               v_sortCol := 'last_name,first_name';   --07/31/2015
            ELSE
               v_sortCol := 'stack_rank';
            END IF;  
            PKG_SSI_CONTEST.prc_upd_ssi_contest_stackrank(p_in_ssi_contest_id,v_out_return_code,v_out_error_message); --06/18/2019 Bug#79069
      ELSE
          v_sortCol := 'stack_rank,last_name';
      END IF;

      DELETE FROM gtt_node_and_below_users;

      INSERT INTO gtt_node_and_below_users (user_id)--Bug # 61461
      SELECT   DISTINCT au.user_id
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
                                                                       (SELECT node_id,NAME as node_name,parent_node_id,path
                                                                        FROM node n
                                                                        START WITH parent_node_id IS NULL
                                                                        CONNECT BY PRIOR node_id = parent_node_id) ip
                                                                        ,user_node un
                                                                        ,application_user au
                                        where ip.node_id = npn.path_node_id
                                              AND npn.node_id = un.node_id
                                              AND un.user_id = au.user_id
                                              AND ip.node_id IN ((select node_id from user_node where user_id = p_in_user_id and status = 1 and role in ('own','mgr')))
                                              AND au.is_active = 1
                                              AND au.user_type = 'pax'
                                              AND un.status = 1;
      --05/19/2015 Bug 62315
      l_query := 'SELECT * FROM (
      SELECT ROWNUM RN,stack_rank,participant_id,first_name,last_name,avatar,pax_activity,is_team_memeber FROM (
        SELECT   CASE WHEN scpp_cnt.pax_progress_cnt = 0 THEN NULL ELSE scpsr.stack_rank_position END stack_rank,     --07/17/2015
                 au.user_id participant_id,au.first_name,au.last_name,p.avatar_small avatar,
                 scpp.activity_amt pax_activity,0 is_team_memeber
            FROM ssi_contest_pax_stack_rank scpsr,
                 ssi_contest_pax_progress scpp,
                 application_user au,
                 ssi_contest sc,
                 participant p
                 ,ssi_contest_participant scp
                 ,(SELECT COUNT(*) AS pax_progress_cnt FROM ssi_contest_pax_progress where ssi_contest_id = '''||p_in_ssi_contest_id||''') scpp_cnt  --07/17/2015
           WHERE     sc.ssi_contest_id = '''||p_in_ssi_contest_id||'''
                 AND sc.contest_type = '''||c_stack_rank||'''
                 AND sc.ssi_contest_id = scp.ssi_contest_id
                 AND scp.user_id = scpsr.user_id (+)
                 AND scp.ssi_contest_id = scpsr.ssi_contest_id (+)
                 AND scp.user_id = scpp.user_id(+)
                 AND scp.ssi_contest_id = scpp.ssi_contest_id (+)
                 AND scp.user_id = au.user_id
                 AND au.user_id = p.user_id
                 AND '||is_team||' = 0
                 AND au.is_active = 1
                 AND NOT EXISTS        --02/04/2015  IF Manager
                                         (select nab.user_id
                                          from gtt_node_and_below_users nab
                                          where nab.user_id = scp.user_id
                                          )
                 AND ('''||p_in_include_all ||'''= 1
                      OR
                      ( '''||p_in_include_all ||''' = 0
                  AND NOT EXISTS (select * from ssi_contest_pax_payout WHERE ssi_contest_id = '''||p_in_ssi_contest_id||''' AND user_id = scp.user_id)))
                   UNION ALL
                  SELECT CASE WHEN scpp_cnt.pax_progress_cnt = 0 THEN NULL ELSE scpsr.stack_rank_position END stack_rank,    --07/17/2015
                 au.user_id participant_id,au.first_name,au.last_name,p.avatar_small avatar,
                 scpp.activity_amt pax_activity,1 is_team_memeber
            FROM ssi_contest_pax_stack_rank scpsr,
                 ssi_contest_pax_progress scpp,
                 application_user au,
                 ssi_contest sc,
                 participant p
                 ,ssi_contest_participant scp
                 ,(SELECT COUNT(*) AS pax_progress_cnt FROM ssi_contest_pax_progress where ssi_contest_id = '''||p_in_ssi_contest_id||''') scpp_cnt  --07/17/2015                 
           WHERE     sc.ssi_contest_id = '''||p_in_ssi_contest_id||'''
                 AND sc.contest_type = '''||c_stack_rank||'''
                 AND sc.ssi_contest_id = scp.ssi_contest_id
                 AND scp.user_id = scpsr.user_id (+)
                 AND scp.ssi_contest_id = scpsr.ssi_contest_id (+)
                 AND scp.user_id = scpp.user_id(+)
                 AND scp.ssi_contest_id = scpp.ssi_contest_id (+)
                 AND scp.user_id = au.user_id
                 AND au.user_id = p.user_id
                 AND au.is_active = 1
                 AND EXISTS        --02/04/2015  IF Manager
                                         (select nab.user_id
                                          from gtt_node_and_below_users nab
                                          where nab.user_id = scp.user_id
                                          )
                 AND ('''||p_in_include_all ||''' = 1
                      OR
                      ( '''||p_in_include_all ||''' = 0
                 AND NOT EXISTS (select * from ssi_contest_pax_payout WHERE ssi_contest_id = '''||p_in_ssi_contest_id||''' AND user_id = scp.user_id)))
        UNION ALL
        SELECT CASE WHEN scpp_cnt.pax_progress_cnt = 0 THEN NULL ELSE scpsr.stack_rank_position END stack_rank,   --07/17/2015
                 au.user_id participant_id,au.first_name,au.last_name,p.avatar_small avatar,
                 scpp.activity_amt pax_activity,0 is_team_memeber
            FROM ssi_contest_pax_stack_rank scpsr,
                 ssi_contest_pax_progress scpp,
                 application_user au,
                 ssi_contest sc,
                 participant p
                 ,(SELECT COUNT(*) AS pax_progress_cnt FROM ssi_contest_pax_progress where ssi_contest_id = '''||p_in_ssi_contest_id||''') scpp_cnt  --07/17/2015                 
           WHERE     sc.ssi_contest_id = '''||p_in_ssi_contest_id||'''
                 AND sc.contest_type IN ('''||c_objectives||''','''||c_step_it_up||''')
                 AND scpsr.ssi_contest_id = sc.ssi_contest_id
                 AND scpsr.user_id = scpp.user_id(+)
                 AND scpsr.ssi_contest_id = scpp.ssi_contest_id (+)
                 AND scpsr.user_id = au.user_id
                 AND au.user_id = p.user_id
                 AND au.is_active = 1
                 AND '||is_team||' = 0
                 AND NOT EXISTS        --02/04/2015  IF Manager
                                         (select nab.user_id
                                          from gtt_node_and_below_users nab
                                          where nab.user_id = scpsr.user_id
                                          )
        UNION ALL
        SELECT CASE WHEN scpp_cnt.pax_progress_cnt = 0 THEN NULL ELSE scpsr.stack_rank_position END stack_rank,   --07/17/2015
                 au.user_id participant_id,au.first_name,au.last_name,p.avatar_small avatar,
                 scpp.activity_amt pax_activity,1 is_team_memeber
            FROM ssi_contest_pax_stack_rank scpsr,
                 ssi_contest_pax_progress scpp,
                 application_user au,
                 ssi_contest sc,
                 participant p
                 ,(SELECT COUNT(*) AS pax_progress_cnt FROM ssi_contest_pax_progress where ssi_contest_id = '''||p_in_ssi_contest_id||''') scpp_cnt  --07/17/2015                 
           WHERE     sc.ssi_contest_id = '''||p_in_ssi_contest_id||'''
                 AND sc.contest_type IN ('''||c_objectives||''','''||c_step_it_up||''')
                 AND scpsr.ssi_contest_id = sc.ssi_contest_id
                 AND scpsr.user_id = scpp.user_id(+)
                 AND scpsr.ssi_contest_id = scpp.ssi_contest_id (+)
                 AND scpsr.user_id = au.user_id
                 AND au.user_id = p.user_id
                 AND au.is_active = 1
                 AND EXISTS        --02/04/2015  IF Manager
                                         (select nab.user_id
                                          from gtt_node_and_below_users nab
                                          where nab.user_id = scpsr.user_id
                                          )
        UNION ALL
        SELECT CASE WHEN scpp_cnt.pax_progress_cnt = 0 THEN NULL ELSE scpsr.stack_rank_position END stack_rank,  --07/17/2015
                 au.user_id participant_id,au.first_name,au.last_name,p.avatar_small avatar,
                 scpp.activity_amt pax_activity,0 is_team_memeber
            FROM ssi_contest_pax_stack_rank scpsr,
                 ssi_contest_pax_progress scpp,
                 application_user au,
                 ssi_contest_activity sca,
                 participant p
                 ,(SELECT COUNT(*) AS pax_progress_cnt FROM ssi_contest_pax_progress where ssi_contest_id = '''||p_in_ssi_contest_id||''') scpp_cnt  --07/17/2015                 
           WHERE     scpsr.ssi_contest_id = '''||p_in_ssi_contest_id||'''
                 AND scpsr.user_id = scpp.user_id(+)
                 AND scpsr.ssi_contest_id = scpp.ssi_contest_id(+)
                 AND scpsr.ssi_contest_activity_id = scpp.ssi_contest_activity_id(+)
                 AND scpsr.user_id = au.user_id
                 AND sca.ssi_contest_activity_id = '''||p_in_contest_activity_id||'''
                 AND scpsr.ssi_contest_id = sca.ssi_contest_id
                 AND scpsr.ssi_contest_activity_id = sca.ssi_contest_activity_id
                 AND au.user_id = p.user_id
                 AND au.is_active = 1
                 AND '||is_team||' = 0
                 AND NOT EXISTS        --02/04/2015  IF Manager
                                         (select nab.user_id
                                          from gtt_node_and_below_users nab
                                          where nab.user_id = scpsr.user_id
                                          )
                UNION ALL
        SELECT CASE WHEN scpp_cnt.pax_progress_cnt = 0 THEN NULL ELSE scpsr.stack_rank_position END stack_rank,   --07/17/2015
                 au.user_id participant_id,au.first_name,au.last_name,p.avatar_small avatar,
                 scpp.activity_amt pax_activity,1 is_team_memeber
            FROM ssi_contest_pax_stack_rank scpsr,
                 ssi_contest_pax_progress scpp,
                 application_user au,
                 ssi_contest_activity sca,
                 participant p
                 ,(SELECT COUNT(*) AS pax_progress_cnt FROM ssi_contest_pax_progress where ssi_contest_id = '''||p_in_ssi_contest_id||''') scpp_cnt  --07/17/2015                 
           WHERE     scpsr.ssi_contest_id = '''||p_in_ssi_contest_id||'''
                 AND scpsr.user_id = scpp.user_id(+)
                 AND scpsr.ssi_contest_id = scpp.ssi_contest_id(+)
                 AND scpsr.ssi_contest_activity_id = scpp.ssi_contest_activity_id(+)
                 AND scpsr.user_id = au.user_id
                 AND sca.ssi_contest_activity_id = '''||p_in_contest_activity_id||'''
                 AND scpsr.ssi_contest_id = sca.ssi_contest_id
                 AND scpsr.ssi_contest_activity_id = sca.ssi_contest_activity_id
                 AND au.user_id = p.user_id
                 AND au.is_active = 1
                 AND EXISTS        --02/04/2015  IF Manager
                                         (select nab.user_id
                                          from gtt_node_and_below_users nab
                                          where nab.user_id = scpsr.user_id
                                          )
          ORDER BY  '||v_sortCol||' )) WHERE RN >= '||p_in_rowNumStart||' AND RN <='||p_in_rowNumEnd;--03/06/2015 bUG # 59929

      OPEN p_out_ref_cursor FOR
      l_query;

  IF is_team = 0 THEN
  SELECT COUNT(1) INTO v_pax_count
FROM (
select scp.user_id
FROM ssi_contest_participant scp,application_user au,ssi_contest sc WHERE scp.ssi_contest_id =  p_in_ssi_contest_id
AND sc.ssi_contest_id = scp.ssi_contest_id
AND sc.contest_type <> 8
  AND scp.user_id = au.user_id
  AND au.is_active = 1
  UNION ALL
  select scp.user_id
FROM ssi_contest_participant scp,application_user au,ssi_contest sc WHERE scp.ssi_contest_id = p_in_ssi_contest_id
AND sc.ssi_contest_id = scp.ssi_contest_id
AND sc.contest_type = 8
  AND scp.user_id = au.user_id
  AND au.is_active = 1
  AND (p_in_include_all = 1
  OR
  ( p_in_include_all = 0
  AND NOT EXISTS (select * from ssi_contest_pax_payout WHERE ssi_contest_id = p_in_ssi_contest_id AND user_id = scp.user_id)))
  );

  ELSE
  SELECT COUNT(1) INTO v_pax_count FROM (
  SELECT scpsr.stack_rank_position stack_rank,
         scpp.activity_amt pax_activity
    FROM ssi_contest_pax_stack_rank scpsr,
         ssi_contest_pax_progress scpp,
         application_user au,
         ssi_contest sc,
         participant p
         ,ssi_contest_participant scp
   WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
         AND sc.contest_type NOT IN (2,8)
         AND sc.ssi_contest_id = scp.ssi_contest_id
         AND scp.user_id = scpsr.user_id (+)
         AND scp.ssi_contest_id = scpsr.ssi_contest_id (+)
         AND scp.user_id = scpp.user_id(+)
         AND scp.ssi_contest_id = scpp.ssi_contest_id (+)
         AND scp.user_id = au.user_id
         AND au.user_id = p.user_id
         AND au.is_active = 1
         AND EXISTS        --02/04/2015  IF Manager
                                 (select nab.user_id
                                  from gtt_node_and_below_users nab
                                  where nab.user_id = scp.user_id
                                  )
          UNION ALL
            SELECT scpsr.stack_rank_position stack_rank,
         scpp.activity_amt pax_activity
    FROM ssi_contest_pax_stack_rank scpsr,
         ssi_contest_pax_progress scpp,
         ssi_contest sc
         ,application_user au
         ,ssi_contest_participant scp
   WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
         AND sc.contest_type = c_stack_rank
         AND sc.ssi_contest_id = scp.ssi_contest_id
         AND scp.user_id = scpsr.user_id (+)
         AND scp.ssi_contest_id = scpsr.ssi_contest_id (+)
         AND scp.user_id = scpp.user_id(+)
         AND scp.ssi_contest_id = scpp.ssi_contest_id (+)
         AND scp.user_id = au.user_id
         AND is_team = 0
         AND au.is_active = 1
         AND NOT EXISTS        --02/04/2015  IF Manager
                                 (select nab.user_id
                                  from gtt_node_and_below_users nab
                                  where nab.user_id = scp.user_id
                                  )
         AND (p_in_include_all = 1
                      OR
                      (p_in_include_all = 0
          AND NOT EXISTS (select * from ssi_contest_pax_payout WHERE ssi_contest_id = p_in_ssi_contest_id AND user_id = scp.user_id)))
           UNION ALL
          SELECT scpsr.stack_rank_position stack_rank,
         scpp.activity_amt pax_activity
    FROM ssi_contest_pax_stack_rank scpsr,
         ssi_contest_pax_progress scpp,
         application_user au,
         ssi_contest sc
         ,ssi_contest_participant scp
   WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
         AND sc.contest_type = c_stack_rank
         AND sc.ssi_contest_id = scp.ssi_contest_id
         AND scp.user_id = scpsr.user_id (+)
         AND scp.ssi_contest_id = scpsr.ssi_contest_id (+)
         AND scp.user_id = scpp.user_id(+)
         AND scp.ssi_contest_id = scpp.ssi_contest_id (+)
         AND scp.user_id = au.user_id
         AND au.is_active = 1
         AND EXISTS        --02/04/2015  IF Manager
                                 (select nab.user_id
                                  from gtt_node_and_below_users nab
                                  where nab.user_id = scp.user_id
                                  )
         AND (p_in_include_all = 1
                      OR
                      ( p_in_include_all = 0
         AND NOT EXISTS (select * from ssi_contest_pax_payout WHERE ssi_contest_id = p_in_ssi_contest_id AND user_id = scp.user_id)))
UNION ALL
SELECT scpsr.stack_rank_position stack_rank,
         scpp.activity_amt pax_activity
    FROM ssi_contest_pax_stack_rank scpsr,
         ssi_contest_pax_progress scpp,
         ssi_contest_activity sca,
         application_user au
   WHERE     scpsr.ssi_contest_id = p_in_ssi_contest_id
         AND scpsr.user_id = scpp.user_id(+)
         AND scpsr.ssi_contest_id = scpp.ssi_contest_id(+)
         AND scpsr.ssi_contest_activity_id = scpp.ssi_contest_activity_id(+)
         AND sca.ssi_contest_activity_id = p_in_contest_activity_id
         AND scpsr.ssi_contest_id = sca.ssi_contest_id
         AND scpsr.ssi_contest_activity_id = sca.ssi_contest_activity_id
         AND scpsr.user_id = au.user_id
         AND au.is_active = 1
         AND EXISTS        --02/04/2015  IF Manager
                                 (select nab.user_id
                                  from gtt_node_and_below_users nab
                                  where nab.user_id = scpsr.user_id
                                  ));

  END IF;


  p_out_return_code := 0;

   p_out_pax_count := v_pax_count;



  EXCEPTION WHEN OTHERS THEN
      p_out_return_code :=99;
      p_out_pax_count := 0;
      prc_execution_log_entry ('PRC_SSI_CONTEST_STACKRANK_LIST',1,'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| SQLERRM,NULL);

      END prc_ssi_contest_stackrank_list;

      PROCEDURE prc_ssi_contest_approvals_list (p_in_ssi_contest_id     IN     NUMBER,
      p_in_contest_activity_id IN NUMBER,
      p_in_sortColName     IN     VARCHAR2,
      p_in_sortedBy           IN     VARCHAR2,
      p_in_rowNumStart               IN NUMBER,
      p_in_rowNumEnd                 IN NUMBER,
      p_out_contest_type  OUT NUMBER,
      p_out_pax_count      OUT NUMBER,
      p_out_return_code   OUT NUMBER,
      p_out_obj_ref_cursor OUT SYS_REFCURSOR,
      p_out_obj_total_ref_cursor OUT SYS_REFCURSOR,
      p_out_dtgt_ref_cursor OUT SYS_REFCURSOR,
      p_out_total_dtgt_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_total_ref_cursor OUT SYS_REFCURSOR,
      p_out_sr_ref_cursor OUT SYS_REFCURSOR,
      p_out_sr_total_ref_cursor OUT SYS_REFCURSOR)
      IS
       /***********************************************************************************
      Purpose: 

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
      Suresh J          08/06/2015     Bug 63589 - Stack rank - Issue payouts page - Payouts are editable even when there is no tie
   ************************************************************************************/

      v_out_contest_type NUMBER(18);
      v_sortCol             VARCHAR2(200);
     l_query VARCHAR2(32767);
     v_activity_count NUMBER;
     v_pax_count       NUMBER;
     v_in_rownum_end  NUMBER;
     v_creator_user_id     NUMBER;
     v_locale           VARCHAR2(10);
     v_has_tie            NUMBER;   --06/12/2015
     
      BEGIN

      SELECT contest_type,contest_owner_id --created_by --05/21/2015
                   INTO v_out_contest_type,v_creator_user_id FROM ssi_contest WHERE ssi_contest_id = p_in_ssi_contest_id;

       IF v_out_contest_type = 8 THEN
      SELECT NVL(language_id,(SELECT STRING_VAL FROM os_propertyset WHERE entity_name = 'default.language')) INTO v_locale FROM application_user WHERE user_id = v_creator_user_id;
      END IF;
          p_out_contest_type := v_out_contest_type;
--      v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;
      
IF upper(p_in_sortColName) <> 'LAST_NAME' THEN
 
 v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy||' ,'|| 'LAST_NAME' || ' ' || p_in_sortedBy||' ,'|| 'FIRST_NAME' || ' ' || p_in_sortedBy;
 
ELSE 
 
 v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;
 
 END IF; 

          l_query  := 'SELECT * FROM
  ( ';

   IF v_out_contest_type = 4 THEN --Objective Contest Type

              l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                               FROM (
                  SELECT sc.ssi_contest_id,
                        au.last_name,
                        au.first_name,
                        au.user_id,
                        n.name org_name,
                          scp.objective_amount,
                          scpp.activity_amt,
                          CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE scp.objective_payout END payout,
                          scp.objective_payout_description payout_description,
                          CASE WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
                               WHEN CASE
                                   WHEN scpp.activity_amt > scp.objective_amount
                                   THEN
                                           FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment) * scp.objective_bonus_payout
                                   ELSE
                                      0
                                END  > OBJECTIVE_BONUS_CAP THEN OBJECTIVE_BONUS_CAP
                                ELSE CASE
                                   WHEN scpp.activity_amt > scp.objective_amount
                                   THEN
                                           FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment)* scp.objective_bonus_payout
                                   ELSE
                                      0
                                END END AS bonus_payout,
                         CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 --05/17/2017
                              WHEN CASE
                                   WHEN scpp.activity_amt >= scp.objective_amount
                                   THEN
                                           NVL(FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment) * scp.objective_bonus_payout,0)
                                   ELSE
                                      0
                                END  > OBJECTIVE_BONUS_CAP THEN scp.objective_payout + OBJECTIVE_BONUS_CAP
                                ELSE CASE
                                   WHEN scpp.activity_amt >= scp.objective_amount
                                   THEN
                                           scp.objective_payout + NVL(FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment)* scp.objective_bonus_payout,0)
                                   ELSE
                                      0
                                END END AS total_payout,
                                   sc.activity_measure_type,
                                   sc.include_bonus,
                                   p.is_opt_out_of_awards--05/17/2017
                        FROM ssi_contest sc,
                             ssi_contest_pax_progress scpp,
                             ssi_contest_participant scp,
                             application_user au,
                             user_node un,
                             node n,
                             participant p
                       WHERE     sc.ssi_contest_id = '''||p_in_ssi_contest_id||'''
                             AND sc.ssi_contest_id = scp.ssi_contest_id
                             AND scp.ssi_contest_id = scpp.ssi_contest_id
                             AND scp.user_id = scpp.user_id
                             AND scp.user_id = au.user_id
                             AND scpp.activity_amt >= scp.objective_amount
                             AND scp.user_id = un.user_id
                             AND un.is_primary = 1
                             AND un.status = 1
                             AND au.is_active = 1
                             AND un.node_id = n.node_id
                             AND scp.user_id = p.user_id
                             ORDER BY '|| v_sortCol ||'
                ) RS) WHERE RN >= ' ||p_in_rowNumStart||' AND RN   <= '|| p_in_rowNumEnd;

                OPEN p_out_obj_ref_cursor FOR l_query;

                SELECT COUNT(1) INTO v_pax_count FROM
                        ssi_contest sc,
                             ssi_contest_pax_progress scpp,
                             ssi_contest_participant scp,
                             application_user au
                       WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                             AND sc.ssi_contest_id = scp.ssi_contest_id
                             AND scp.ssi_contest_id = scpp.ssi_contest_id
                             AND scp.user_id = scpp.user_id
                             AND scpp.activity_amt >= scp.objective_amount
                             AND scp.user_id = au.user_id
                             AND au.is_active = 1;

                OPEN p_out_obj_total_ref_cursor FOR
                SELECT SUM(scp.objective_amount) objective_amount,
                          SUM(scpp.activity_amt) activity_amt,
                          SUM( CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE objective_payout END) total_objective_payout,--05/17/2017
                          SUM(  CASE WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
                                     WHEN CASE
                                   WHEN scpp.activity_amt > scp.objective_amount
                                   THEN
                                           FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment) * scp.objective_bonus_payout
                                   ELSE
                                      0
                                END  > objective_bonus_cap THEN objective_bonus_cap
                                ELSE CASE
                                   WHEN scpp.activity_amt > scp.objective_amount
                                   THEN
                                           FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment)* scp.objective_bonus_payout
                                   ELSE
                                      0
                                END END
                                ) total_bonus_payout,
                           SUM(                CASE WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
                                   WHEN scp.objective_bonus_payout IS NOT NULL AND scpp.activity_amt >= scp.objective_amount
                                   THEN
                                        objective_payout
                                      +   CASE WHEN FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment) * scp.objective_bonus_payout > objective_bonus_cap THEN objective_bonus_cap
                                                     ELSE  FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment) * scp.objective_bonus_payout
                                                     END
                                   WHEN scp.objective_bonus_payout IS NULL THEN objective_payout
                                   ELSE
                                      0
                                END) total_payout,
                                sc.activity_measure_type,
                                sc.include_bonus
                        FROM ssi_contest sc,
                             ssi_contest_pax_progress scpp,
                             ssi_contest_participant scp,
                             application_user au,
                             participant p--05/17/2017
                       WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                             AND sc.ssi_contest_id = scp.ssi_contest_id
                             AND scp.ssi_contest_id = scpp.ssi_contest_id
                             AND scpp.activity_amt >= scp.objective_amount
                             AND scp.user_id = scpp.user_id
                             AND scpp.user_id = au.user_id
                             AND au.is_active = 1
                             AND au.user_id = p.user_id--05/17/2017
                             GROUP BY sc.activity_measure_type,sc.include_bonus;

                            OPEN p_out_dtgt_ref_cursor FOR
                            select * from dual where 1=2;

                             OPEN p_out_total_dtgt_ref_cursor FOR
                             select * from dual where 1=2;

                             OPEN p_out_siu_ref_cursor FOR
                            select * from dual where 1=2;

                             OPEN p_out_siu_total_ref_cursor FOR
                             select * from dual where 1=2;

                              OPEN p_out_sr_ref_cursor FOR
                            select * from dual where 1=2;

                             OPEN p_out_sr_total_ref_cursor FOR
                             select * from dual where 1=2;


                ELSIF  v_out_contest_type = 2 THEN--(Do This Get That)

                IF p_in_contest_activity_id IS NULL THEN
                SELECT COUNT(1) INTO v_activity_count FROM ssi_contest_activity WHERE ssi_contest_id = p_in_ssi_contest_id;
                v_in_rownum_end := v_activity_count * p_in_rowNumEnd;
                ELSE
                v_in_rownum_end := p_in_rowNumEnd;
                END IF;

                OPEN p_out_obj_ref_cursor FOR
                select * from dual where 1=2;

                 OPEN p_out_obj_total_ref_cursor FOR
                 select * from dual where 1=2;

                 OPEN p_out_siu_ref_cursor FOR
                select * from dual where 1=2;

                 OPEN p_out_siu_total_ref_cursor FOR
                 select * from dual where 1=2;

                  OPEN p_out_sr_ref_cursor FOR
                            select * from dual where 1=2;

                  OPEN p_out_sr_total_ref_cursor FOR
                  select * from dual where 1=2;


                 l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                               FROM (
                   SELECT s.ssi_contest_id,
            s.ssi_contest_activity_id,
            s.description,
            s.user_id,
            au.last_name,
            au.first_name,
            s.activity_date,
            s.activity_amt,
            s.for_every,
            CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE s.increment_payout END total_payout,--05/17/2017
            s.min_qualifier,
            qualified_activity,
             FLOOR((CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt
                       ELSE  maximum_payout_activity END- min_qualifier)/for_every) AS number_of_increments,
             CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 --05/17/2017
                  ELSE FLOOR((CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt
                       ELSE  maximum_payout_activity END- min_qualifier)/for_every)* increment_payout
                  END AS payout_value,
                       s.activity_measure_type
                       ,s.payout_description    --02/26/2015
                       ,p.is_opt_out_of_awards --05/17/2017
                       FROM (
            SELECT sc.ssi_contest_id,
                   sca.ssi_contest_activity_id,
                   sca.description,
                   scp.user_id,
                   scpp.activity_date,
                   scpp.activity_amt,
                     sca.increment_amount for_every,
                     sca.payout_amount increment_payout,
                     sca.min_qualifier,
                     (scpp.activity_amt - sca.min_qualifier) qualified_activity,
                    sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount AS maximum_payout_activity,
                    sc.activity_measure_type
                    ,sca.payout_description    --02/26/2015
              FROM ssi_contest sc, ssi_contest_activity sca, ssi_contest_participant scp,
              ssi_contest_pax_progress scpp
             WHERE     sc.ssi_contest_id = '''||p_in_ssi_contest_id||'''
                   AND sc.ssi_contest_id = sca.ssi_contest_id
                   AND sc.ssi_contest_id = scp.ssi_contest_id
                   AND sca.ssi_contest_activity_id = NVL('''||p_in_contest_activity_id||''',sca.ssi_contest_activity_id)
                   AND scp.user_id = scpp.user_id
                   AND sca.ssi_contest_activity_id = scpp.ssi_contest_activity_id
                   AND scpp.activity_amt >= sca.min_qualifier + increment_amount -- Bug # 60859,61131
                   ) s, application_user au, participant p
                   WHERE s.user_id = au .user_id AND au.is_active = 1 AND au.user_id = p.user_id ORDER BY '|| v_sortCol ||'
                ) RS) WHERE RN >= ' ||p_in_rowNumStart||' AND RN   <= '|| v_in_rownum_end;

            OPEN p_out_dtgt_ref_cursor FOR l_query;

            SELECT COUNT(1) INTO v_pax_count
              FROM ssi_contest sc, ssi_contest_activity sca, ssi_contest_participant scp,
              ssi_contest_pax_progress scpp, application_user au
             WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                   AND sc.ssi_contest_id = sca.ssi_contest_id
                   AND sc.ssi_contest_id = scp.ssi_contest_id
                   AND sca.ssi_contest_activity_id = NVL(p_in_contest_activity_id,sca.ssi_contest_activity_id)
                   AND scp.user_id = scpp.user_id
                   AND scpp.user_id = au.user_id
                   AND au.is_active = 1
                   AND sca.ssi_contest_activity_id = scpp.ssi_contest_activity_id
                   AND scpp.activity_amt >= sca.min_qualifier + increment_amount;--Bug # 60740,60982,61131

                    OPEN p_out_total_dtgt_ref_cursor FOR
                       SELECT
             ssi_contest_activity_id,
             description,
             payout_description,
             for_every,
             increment_payout will_earn,
             min_qualifier,
             payout_other_cur_code,
            SUM(activity_amt) activity_amt,
            SUM(qualified_activity) qualified_activity,
            SUM( FLOOR((CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt
                       ELSE  maximum_payout_activity END- min_qualifier)/for_every)) number_of_increments,
            SUM(FLOOR((CASE WHEN activity_amt2 <= maximum_payout_activity THEN activity_amt2--05/17/2017
                       ELSE  maximum_payout_activity END- min_qualifier)/for_every)* increment_payout) AS payout_value,
            activity_measure_type
                       FROM (
            SELECT sc.ssi_contest_id,
                   sca.ssi_contest_activity_id,
                   sca.description,
                   scp.user_id,
                   sc.payout_other_cur_code,
                   scpp.activity_amt,
                     sca.increment_amount for_every,
                     sca.payout_amount increment_payout,
                     sca.min_qualifier,
                     (scpp.activity_amt - sca.min_qualifier) qualified_activity,
                    sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount AS maximum_payout_activity,
                    sca.payout_description,
                    sc.activity_measure_type,
                    CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE scpp.activity_amt END activity_amt2--05/17/2017
              FROM ssi_contest sc, ssi_contest_activity sca, ssi_contest_participant scp,
              ssi_contest_pax_progress scpp, application_user au, participant p
             WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                   AND sc.ssi_contest_id = sca.ssi_contest_id
                   AND sc.ssi_contest_id = scp.ssi_contest_id
                   AND sca.ssi_contest_activity_id = NVL(p_in_contest_activity_id,sca.ssi_contest_activity_id)
                   AND scp.user_id = scpp.user_id
                   AND scpp.user_id = au.user_id
                   AND au.is_active = 1
                   AND au.user_id = p.user_id
                   AND sca.ssi_contest_activity_id = scpp.ssi_contest_activity_id
                   AND scpp.activity_amt >= sca.min_qualifier + increment_amount ) -- Bug # 60859
                   GROUP BY ssi_contest_activity_id,description,payout_description,
             for_every,
             increment_payout,
             min_qualifier,
             payout_other_cur_code,activity_measure_type
             ORDER BY ssi_contest_activity_id; --Bug # 60398

             ELSIF v_out_contest_type = 16 THEN

             l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                               FROM (
             SELECT sc.ssi_contest_id,
                        au.user_id,
                        au.last_name,au.first_name,
                        n.name org_name,
                        sc.activity_amt,
                        sc.level_completed,
                        sc.payout_description,
                        CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE sc.level_payout END payout,--05/17/2017
                        CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE sc.bonus_payout END bonus_payout,--05/17/2017
                        CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE sc.total_payout END total_payout,--05/17/2017
                        sc.activity_measure_type,sc.include_bonus,p.is_opt_out_of_awards --05/17/2017
                            FROM (
 SELECT DISTINCT ssi_contest_id,user_id,activity_amt,payout_description, CASE WHEN level_completed =0 THEN NULL ELSE level_completed END level_completed,level_payout,
 CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                                         ELSE bonus_payout END,0)
             ELSE 0
 END bonus_payout,
 level_payout + CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                                         ELSE bonus_payout END,0)
             ELSE 0
 END total_payout,activity_measure_type,include_bonus  FROM (
SELECT sc.ssi_contest_id,sc.activity_measure_type,scp.user_id,scpp.activity_amt, scl.sequence_number,scl.payout_description,scl.goal_amount,CASE WHEN scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number ELSE 0 END level_completed,
RANK() OVER (PARTITION BY scp.user_id ORDER BY CASE WHEN sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank,
CASE WHEN sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount THEN scl.payout_amount
          WHEN sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.payout_amount
          WHEN sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.payout_amount ELSE 0 END level_payout,
CASE WHEN sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment)   * sit_bonus_payout
         WHEN sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment)* sit_bonus_payout
         WHEN sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment) * sit_bonus_payout
         ELSE 0
         END bonus_payout,
         (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = '''||p_in_ssi_contest_id||''') max_level,sit_bonus_cap,
         sc.include_bonus
  FROM ssi_contest sc,
       ssi_contest_level scl,
       ssi_contest_participant scp,
       ssi_contest_pax_progress scpp
 WHERE     sc.ssi_contest_id = '''||p_in_ssi_contest_id||'''
       AND sc.ssi_contest_id = scl.ssi_contest_id
       AND sc.ssi_contest_id = scp.ssi_contest_id
       AND scp.user_id = scpp.user_id(+)
       AND --scpp.activity_amt >= scl.goal_amount
       (( sc.sit_indv_baseline_type = ''no'' AND scpp.activity_amt >= scl.goal_amount)
       OR (sc.sit_indv_baseline_type = ''percent'' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100))
       OR (sc.sit_indv_baseline_type = ''currency'' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount)))
       AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
       ) WHERE rec_rank = 1 ) sc, application_user au, participant p,user_node un, node n
       WHERE sc.user_id = au.user_id
       AND sc.user_id = p.user_id
       AND sc.user_id = un.user_id
       AND un.status = 1
       AND un.is_primary = 1
       AND au.is_active = 1
       AND un.node_id = n.node_id
       ORDER BY '|| v_sortCol ||'
                ) RS) WHERE RN >= ' ||p_in_rowNumStart||' AND RN   <= '|| p_in_rowNumEnd;

                 OPEN p_out_siu_ref_cursor FOR l_query;
                 SELECT COUNT(DISTINCT user_id) INTO v_pax_count FROM (
SELECT sc.ssi_contest_id,scp.user_id,scpp.activity_amt, scl.sequence_number,scl.payout_description,scl.goal_amount,
RANK() OVER (PARTITION BY scp.user_id ORDER BY CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank
  FROM ssi_contest sc,
       ssi_contest_level scl,
       ssi_contest_participant scp,
       ssi_contest_pax_progress scpp
       ,application_user au    --02/23/2015
 WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
       AND sc.ssi_contest_id = scl.ssi_contest_id
       AND sc.ssi_contest_id = scp.ssi_contest_id
       AND scp.user_id = au.user_id --02/23/2015
       AND au.is_active = 1         --02/23/2015
       AND scp.user_id = scpp.user_id
       AND --scpp.activity_amt >= scl.goal_amount
       (( sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount)
       OR (sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100))
       OR (sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount)))
       AND scp.ssi_contest_id = scpp.ssi_contest_id
       ) WHERE rec_rank = 1;

                 OPEN p_out_siu_total_ref_cursor FOR   --Bug # 60570 03/23/2015
                 SELECT SUM(activity_amt) activity_amt, SUM(level_payout) level_payout,SUM( CASE WHEN LEVEL_COMPLETED  = max_level THEN bonus_payout
 ELSE 0
 END) bonus_payout, SUM(level_payout+ CASE WHEN LEVEL_COMPLETED  = max_level THEN bonus_payout
 ELSE 0
 END) total_payout,activity_measure_type,include_bonus  FROM (
SELECT sc.ssi_contest_id,scp.user_id,scpp.activity_amt,sc.activity_measure_type,
CASE WHEN scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number ELSE 0 END level_completed,
RANK() OVER (PARTITION BY scp.user_id ORDER BY CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank,
CASE      WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
          WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.payout_amount
          WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.payout_amount
          WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.payout_amount ELSE 0 END level_payout,
CASE     WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
         WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment)   * sit_bonus_payout
         WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment)* sit_bonus_payout
         WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap
         WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment) * sit_bonus_payout
         ELSE 0
         END bonus_payout,
         (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = p_in_ssi_contest_id) max_level,
         sc.include_bonus
  FROM ssi_contest sc,
       ssi_contest_level scl,
       ssi_contest_participant scp,
       ssi_contest_pax_progress scpp,
       application_user au,
       participant p--05/17/2017
 WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
       AND sc.ssi_contest_id = scl.ssi_contest_id
       AND sc.ssi_contest_id = scp.ssi_contest_id
       AND scp.user_id = scpp.user_id
       AND scp.ssi_contest_id = scpp.ssi_contest_id
       AND scpp.activity_amt >= scl.goal_amount
       AND scpp.user_id = au.user_id
       AND au.is_active = 1
       AND au.user_id = p.user_id
       ) WHERE rec_rank = 1 GROUP BY activity_measure_type,include_bonus;

                 OPEN p_out_obj_ref_cursor FOR
                select * from dual where 1=2;

                 OPEN p_out_obj_total_ref_cursor FOR
                 select * from dual where 1=2;

                 OPEN p_out_dtgt_ref_cursor FOR
                select * from dual where 1=2;

                 OPEN p_out_total_dtgt_ref_cursor FOR
                 select * from dual where 1=2;

                  OPEN p_out_sr_ref_cursor FOR
                  select * from dual where 1=2;

                  OPEN p_out_sr_total_ref_cursor FOR
                  select * from dual where 1=2;

                ELSIF v_out_contest_type = 8 THEN--(Stack Rank contest)

                DELETE FROM temp_table_session;

                INSERT INTO temp_table_session
                SELECT cav.asset_code,
                          MAX (DECODE (cav.key, 'NAME', cav.cms_value, NULL)) AS cms_name,
                         MAX (DECODE (cav.key, 'EARNED_IMAGE_SMALL', cav.cms_value, NULL)) AS cms_code
                       FROM vw_cms_asset_value cav
                       WHERE asset_code='promotion.badge'
                       AND locale = v_locale
                   GROUP BY cav.asset_code,
                            cav.locale,
                            cav.asset_id,
                            cav.content_key_id,
                            cav.content_id;

                OPEN p_out_obj_ref_cursor FOR
                select * from dual where 1=2;

                 OPEN p_out_obj_total_ref_cursor FOR
                 select * from dual where 1=2;

                 OPEN p_out_dtgt_ref_cursor FOR
                select * from dual where 1=2;

                 OPEN p_out_total_dtgt_ref_cursor FOR
                 select * from dual where 1=2;

                 OPEN p_out_siu_ref_cursor FOR
                  select * from dual where 1=2;

                  OPEN p_out_siu_total_ref_cursor FOR
                  select * from dual where 1=2;

                   l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                               FROM (
                  SELECT  scpsr.stack_rank_position stack_rank,
                           au.user_id,
                           au.last_name,
                           au.first_name,
                           p.avatar_small avatar,
                           scpp.activity_amt,
                           sc.activity_description as description,
                           CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE scsrp.payout_amount END as total_payout,
                           scsrp.payout_desc as payout_description,
                           scsrp.badge_rule_id as badge_id,
                           NVL((SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale = '''||v_locale||'''),(SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale =''en_US'')) badge_name,
                           cms_badge.image_small_URL badge_image,
                           sc.activity_measure_type,
                           p.is_opt_out_of_awards--05/17/2017
                    FROM ssi_contest_pax_stack_rank scpsr,
                         ssi_contest_pax_progress scpp,
                         ssi_contest_sr_payout scsrp,
                         application_user au,
                         ssi_contest sc,
                         participant p,
                         badge_rule br,
                         (select asset_code, cms_name, cms_code image_small_URL from temp_table_session ) cms_badge
                   WHERE     scpsr.ssi_contest_id = '''||p_in_ssi_contest_id||'''
                         AND scpsr.user_id = scpp.user_id
                         AND scpsr.ssi_contest_id = scpp.ssi_contest_id --04/08/2015 Bug 60970
                         AND sc.ssi_contest_id = scpp.ssi_contest_id
                         AND scpsr.user_id = au.user_id
                         AND au.is_active = 1
                         AND scpsr.ssi_contest_id = scsrp.ssi_contest_id --04/08/2015 Bug 60970
                         AND sc.ssi_contest_id = scsrp.ssi_contest_id
                         AND scsrp.rank_position = scpsr.stack_rank_position
                         AND scsrp.badge_rule_id = br.badge_rule_id  (+)
                         AND br.cm_asset_key = cms_badge.cms_name (+)
                         AND scpp.activity_amt >= NVL(sc.stack_rank_qualifier_amount,0)   --Checking minimum qualifer
                         AND au.user_id = p.user_id
                        ORDER BY '|| v_sortCol ||'
                ) RS) WHERE RN >= ' ||p_in_rowNumStart||' AND RN   <= '|| p_in_rowNumEnd;

                OPEN p_out_sr_ref_cursor FOR l_query;

                SELECT COUNT(1) INTO v_pax_count
                    FROM ssi_contest_pax_stack_rank scpsr,
                         ssi_contest_pax_progress scpp,
                         ssi_contest_sr_payout scsrp,
                         application_user au,
                         ssi_contest sc
                   WHERE     scpsr.ssi_contest_id = p_in_ssi_contest_id
                         AND scpsr.user_id = scpp.user_id
                         AND scpsr.user_id = au.user_id
                         AND au.is_active = 1
                         AND scpsr.ssi_contest_id = scpp.ssi_contest_id
                         AND scpsr.ssi_contest_id = scsrp.ssi_contest_id
                          AND scpsr.ssi_contest_id = sc.ssi_contest_id
                         AND scpp.activity_amt >= NVL(sc.stack_rank_qualifier_amount,0)   --Checking minimum qualifer
                         AND scsrp.rank_position = scpsr.stack_rank_position;

                  OPEN p_out_sr_total_ref_cursor FOR --04/08/2015 Bug 60970
                  SELECT activity_amt, total_payout,payout_cap,
                    CASE WHEN tie_indicator > 0
                        THEN 1
                        ELSE 0
                        END has_tie,activity_measure_type
                  FROM
                  (SELECT   sum(scpp.activity_amt) activity_amt,
                           sum(scsrp.payout_amount)  as total_payout,
                           sc.activity_measure_type,
                           (SELECT SUM( payout_amount) FROM ssi_contest_sr_payout WHERE ssi_contest_id = p_in_ssi_contest_id) payout_cap,
                           (select NVL(sum(tie_cnt),0) tie_rank_cnt
                            from (select count(stack_rank_position) tie_cnt
                            from ssi_contest_pax_stack_rank scpsr,
                        ssi_contest_sr_payout scsp
                        WHERE scpsr.ssi_contest_id = p_in_ssi_contest_id
                            AND scpsr.ssi_contest_id = scsp.ssi_contest_id
                            AND scsp.rank_position = scpsr.stack_rank_position
                            GROUP BY stack_rank_position   --08/06/2015
                            having count(stack_rank_position) > 1)
                            ) tie_indicator
                    FROM ssi_contest_pax_stack_rank scpsr,
                         ssi_contest_pax_progress scpp,
                         ssi_contest_sr_payout scsrp,
                         application_user au,
                         ssi_contest sc,
                         participant p,
                         badge_rule br,
                         (select asset_code, cms_name, cms_code image_small_URL from temp_table_session ) cms_badge
                   WHERE     scpsr.ssi_contest_id = p_in_ssi_contest_id
                         AND scpsr.user_id = scpp.user_id --04/08/2015 Bug 60970
                         AND scpsr.ssi_contest_id = scpp.ssi_contest_id
                         AND sc.ssi_contest_id = scpp.ssi_contest_id
                         AND scpsr.user_id = au.user_id
                         AND au.is_active = 1
                         AND scpsr.ssi_contest_id = scsrp.ssi_contest_id --04/08/2015 Bug 60970
                         AND sc.ssi_contest_id = scsrp.ssi_contest_id
                         AND scsrp.rank_position = scpsr.stack_rank_position
                         AND scsrp.badge_rule_id = br.badge_rule_id  (+)
                         AND br.cm_asset_key = cms_badge.cms_name (+)
                         AND scpp.activity_amt >= NVL(sc.stack_rank_qualifier_amount,0)   --Checking minimum qualifer
                         AND au.user_id = p.user_id
                  GROUP BY activity_measure_type);
      
      --When a contest of type Stack Rank has a tie in any of the payout positions,   --06/12/2015 
      --the procedure PKG_SSI_CONTEST_DATA.prc_ssi_contest_approvals_list should INSERT records into the SSI_CONTEST_PAX_PAYOUT table 
      --for the participants in the contest that are eligible for a payout.         

      SELECT tie_indicator.tie_rank_cnt into v_has_tie
      FROM
        (SELECT NVL(sum(tie_cnt),0) tie_rank_cnt
         FROM (SELECT COUNT(stack_rank_position) tie_cnt
              FROM ssi_contest_pax_stack_rank scpsr,
                        ssi_contest_sr_payout scsp
              WHERE scpsr.ssi_contest_id = p_in_ssi_contest_id
                   AND scpsr.ssi_contest_id = scsp.ssi_contest_id
                   AND scsp.rank_position = scpsr.stack_rank_position
              GROUP BY stack_rank_position
                            HAVING COUNT(stack_rank_position) > 1)) tie_indicator;

      IF v_has_tie > 0 THEN  --06/12/2015 

      --To clean-up from any previous attempts by the Creator to do Issue Payout and decided not the complete at that time --06/12/2015
      DELETE FROM ssi_contest_pax_payout WHERE ssi_contest_id = p_in_ssi_contest_id; 
      
      MERGE INTO ssi_contest_pax_payout d USING
        (
        SELECT scpsr.ssi_contest_id,
               scpsr.user_id,
               scsrp.payout_amount  as total_payout,
               scsrp.payout_desc as payout_description,
               scsrp.badge_rule_id,
               sc.activity_measure_type,
               scpsr.stack_rank_position,
               scpp.activity_amt AS activity_amount
        FROM ssi_contest_pax_stack_rank scpsr,
             ssi_contest_pax_progress scpp,
             ssi_contest_sr_payout scsrp,
             ssi_contest sc
        WHERE     scpsr.ssi_contest_id = p_in_ssi_contest_id
             AND scpsr.user_id = scpp.user_id
             AND scpsr.ssi_contest_id = scpp.ssi_contest_id --04/08/2015 Bug 60970
             AND sc.ssi_contest_id = scpp.ssi_contest_id
             AND scpsr.ssi_contest_id = scsrp.ssi_contest_id --04/08/2015 Bug 60970
             AND sc.ssi_contest_id = scsrp.ssi_contest_id
             AND scsrp.rank_position = scpsr.stack_rank_position
             AND scpp.activity_amt >= NVL(sc.stack_rank_qualifier_amount,0)   --Checking minimum qualifer
            ORDER BY 1
         ) s  
      ON (d.ssi_contest_id = s.ssi_contest_id AND d.user_id = s.user_id)
        WHEN NOT MATCHED THEN
        INSERT (
           ssi_contest_pax_payout_id, 
           ssi_contest_id, 
           ssi_contest_activity_id, 
           user_id, 
           payout_amount, 
           journal_id, 
           created_by, 
           date_created, 
           modified_by, 
           date_modified, 
           version 
           ,payout_description
           ,badge_rule_id
           ,activity_amount
           ,stack_rank_position
           )       
        VALUES (
           ssi_contest_pax_payout_pk_sq.NEXTVAL,
           s.ssi_contest_id,
           NULL, 
           s.user_id,
           s.total_payout,
           NULL,
           v_creator_user_id,
           SYSDATE,
           NULL,
           NULL,
           0
           ,s.payout_description           
           ,s.badge_rule_id
           ,s.activity_amount
           ,s.stack_rank_position
        );            

      
      END IF; --v_has_tie > 0
 
   END IF; --If the contest_type !=2

              p_out_pax_count     := v_pax_count;
              p_out_return_code :=0;

      EXCEPTION WHEN OTHERS THEN
       p_out_return_code :=99;
       p_out_pax_count := 0;
       prc_execution_log_entry ('prc_ssi_contest_approvals_list',1,'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| SQLERRM,NULL);

      END prc_ssi_contest_approvals_list;
      PROCEDURE prc_ssi_contest_creator_tile (p_in_ssi_contest_id     IN     NUMBER,
      p_out_contest_type  OUT NUMBER,
      p_out_return_code    OUT NUMBER,
      p_out_obj_ref_cursor OUT SYS_REFCURSOR,
      p_out_siu_ref_cursor OUT SYS_REFCURSOR,
      p_out_sr_ref_cursor OUT SYS_REFCURSOR )
      IS
      /***********************************************************************************
      Purpose:  Procedure to give data for creator tile.

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
     Ravi Dhanekula   4/14/2015     Initial Version
   ************************************************************************************/
      v_out_contest_type NUMBER;
      v_stack_rank          NUMBER;
      v_progress_uploaded NUMBER;
      v_creator_user_id     NUMBER;

      BEGIN

      SELECT contest_type,include_stack_rank,CASE WHEN last_progress_update_date IS NOT NULL THEN 1
                                                                    ELSE 0 END,contest_owner_id --created_by --05/21/2015
                   INTO v_out_contest_type,v_stack_rank,v_progress_uploaded,v_creator_user_id FROM ssi_contest WHERE ssi_contest_id = p_in_ssi_contest_id;
      
       IF v_out_contest_type = 4 THEN

      OPEN p_out_obj_ref_cursor FOR
      SELECT sc.ssi_contest_id,
                 SUM (scpp.activity_amt) team_activity,                
                 SUM (
                    CASE
                       WHEN scpp.activity_amt >= scp.objective_amount THEN 1
                       ELSE 0
                    END)
                    pax_achieved,
                 COUNT (DISTINCT scp.ssi_contest_participant_id) total_pax 
            FROM ssi_contest sc,
                 ssi_contest_pax_progress scpp,
                 ssi_contest_participant scp,
                 application_user au                 
           WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                 AND sc.ssi_contest_id = scp.ssi_contest_id                 
                 AND scp.user_id = scpp.user_id(+)  
                 AND scp.user_id = au.user_id
                 AND au.is_active = 1               
                 AND scP.ssi_contest_id = scpp.ssi_contest_id(+)
                AND sc.contest_type = 4
        GROUP BY sc.ssi_contest_id,sc.contest_goal;
        
        OPEN p_out_siu_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;
        
        OPEN p_out_sr_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;
        
        ELSIF v_out_contest_type = 16 THEN 
        
        OPEN p_out_obj_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;
        
        OPEN p_out_sr_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;
        
        OPEN p_out_siu_ref_cursor FOR
        SELECT sc.ssi_contest_id,
             contest_goal,             
               SUM(activity_amt) AS activity              
              FROM ssi_contest sc,                   
                   ssi_contest_participant scp,
                   ssi_contest_pax_progress scpp
             WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                   AND sc.ssi_contest_id = scp.ssi_contest_id
                   AND scp.user_id = scpp.user_id(+)
                   AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                  GROUP BY contest_goal,sc.ssi_contest_id;
        
       ELSE
      
        OPEN p_out_siu_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;        
     
        OPEN p_out_obj_ref_cursor FOR
        SELECT * FROM DUAL WHERE 1=2;
        
        OPEN p_out_sr_ref_cursor FOR    --03/31/2015
        SELECT scpp.activity_amt score,scpsr.stack_rank_position,                
                 scp.user_id,au.first_name,au.last_name,p.avatar_small avatar_url,
                 (SELECT SUM(activity_amt) FROM ssi_contest_pax_progress WHERE ssi_contest_id = p_in_ssi_contest_id) activity_amt
            FROM ssi_contest sc,
                 ssi_contest_participant scp,
                 ssi_contest_pax_progress scpp,
                 application_user au,
                 ssi_contest_pax_stack_rank scpsr,
                 participant p                 
           WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                 AND sc.ssi_contest_id = scp.ssi_contest_id
                 AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                 AND scp.user_id = scpp.user_id(+)
                 AND scp.user_id = au.user_id
                 AND au.is_active = 1
                 AND scpsr.ssi_contest_id = sc.ssi_contest_id
                 AND scpsr.user_id = scp.user_id
                 AND scpsr.stack_rank_position = 1
                 AND scp.user_id = p.user_id
                 AND rownum <2;
        
        END IF;

     p_out_contest_type  := v_out_contest_type;
      p_out_return_code :=0;
      
      EXCEPTION WHEN OTHERS THEN
       p_out_contest_type  := NULL;
      p_out_return_code :=99;
       prc_execution_log_entry ('PRC_SSI_CONTEST_CREATOR_TILE',1,'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| SQLERRM,NULL);
      END prc_ssi_contest_creator_tile;
      
      PROCEDURE prc_ssi_contest_app_export (p_in_ssi_contest_id     IN     NUMBER,
      p_in_locale           IN VARCHAR2,      
      p_out_return_code    OUT NUMBER,      
      p_out_ref_cursor OUT SYS_REFCURSOR) IS  
      
      c_delimiter CONSTANT VARCHAR2(1) := ',' ;
      c2_delimiter CONSTANT VARCHAR2(1) := '"' ;

      v_out_contest_type NUMBER(18);
      v_payout_type VARCHAR2(30);
      v_include_stack_rank  ssi_contest.include_stack_rank%TYPE;
      v_include_bonus  ssi_contest.include_bonus%TYPE; 
      v_contest_end_date ssi_contest.contest_end_date%TYPE;
      v_status ssi_contest.status%TYPE;
      v_activity_measure_type SSI_CONTEST.activity_measure_type%TYPE;   --05/06/2015 
            
      c_do_this_get_that constant number := 2;
      c_objectives constant number := 4;
      c_stack_rank constant number := 8;
      c_step_it_up constant number := 16;

      BEGIN
      
      SELECT contest_type,payout_type,NVL(include_stack_rank,0),NVL(include_bonus,0),contest_end_date,status,activity_measure_type   --05/06/2015
        INTO v_out_contest_type,v_payout_type,v_include_stack_rank,v_include_bonus,v_contest_end_date,v_status,v_activity_measure_type  --05/06/2015
        FROM ssi_contest WHERE ssi_contest_id = p_in_ssi_contest_id;

      DELETE temp_table_session;

      INSERT INTO temp_table_session (asset_code, cms_name, cms_code)
      SELECT asset_code,cms_value,key 
      FROM vw_cms_asset_value 
      where key in ('LAST_NAME','FIRST_NAME','PAX','ORG_UNIT','OBJECTIVE','PER_TO_OBJECTIVE','OBJECTIVE_PAYOUT','BONUS_PAYOUT',
                    'TOTAL_POTENTIAL_PAYOUT','PAYOUT_VALUE','TOTAL_POTENTIAL_VALUE','ACTIVITY','PAYOUT_QUANTITY',
                    'TOTAL_POTENTIAL_POINTS','LEVEL_COMPLETED','LEVEL_PAYOUT','TOTAL_PAYOUT','ACTIVITY_DESCRIPTION','TO_GO','PAYOUT_DESCRIPTION',
                    'MINIMUM_QUALIFIER','INCREMENT','TOTAL_POTENTIAL_PAYOUT_VALUE','STACK_RANK','BASE_LINE','ACTIVITY_QUALIFIED_PAYOUT','RANK','POTENTIAL_PAYOUT',
                    'TOTAL_PAYOUT_VALUE','TOTAL_PAYOUT',
                    'TOTAL_VALUE','TOTAL_POINTS','TOTAL_POTENTIAL_VALUE','TOTAL_POTENTIAL_POINTS','OBJECTIVES_DESCRIPTION') 
            and asset_code in ('ssi_contest.details.export','ssi_contest.participant','ssi_contest.preview','ssi_contest.payout_objectives', 
                               'ssi_contest.payout_stepitup','ssi_contest.payout_stackrank','ssi_contest.creator')
            and locale = p_in_locale;

      IF v_out_contest_type = c_objectives THEN
             OPEN p_out_ref_cursor FOR 
     SELECT textline FROM (
        SELECT 1, 
     CASE 
     WHEN v_payout_type = 'points' THEN  
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter|| 
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVES_DESCRIPTION' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||  
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVE' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||                          
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PER_TO_OBJECTIVE' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TO_GO' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVE_PAYOUT' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        CASE WHEN v_include_bonus = 1 THEN
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'BONUS_PAYOUT' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter
        END||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POTENTIAL_PAYOUT' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        CASE WHEN v_include_stack_rank = 1 THEN
           c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
        END   
     WHEN v_payout_type = 'other' THEN
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter|| 
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVES_DESCRIPTION' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||  
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'OBJECTIVE' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||                          
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PER_TO_OBJECTIVE' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TO_GO' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_DESCRIPTION' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_VALUE' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||        
        CASE WHEN v_include_stack_rank = 1 THEN
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
        END   
     END AS Textline
     FROM dual
      UNION  ALL
      SELECT 
        (ROWNUM+1),
   CASE 
   WHEN v_payout_type = 'points' THEN
        c2_delimiter||first_name||c2_delimiter||c_delimiter||
        c2_delimiter||last_name||c2_delimiter||c_delimiter||
        c2_delimiter||org_unit||c2_delimiter||c_delimiter||
        c2_delimiter||activity_description||c2_delimiter||c_delimiter||   
        c2_delimiter||objective_amount||c2_delimiter||c_delimiter||
        c2_delimiter||activity_amt||c2_delimiter||c_delimiter||        
        c2_delimiter||perc_achieved||c2_delimiter||c_delimiter||   
        c2_delimiter||to_go||c2_delimiter||c_delimiter||            
        c2_delimiter||objective_payout||c2_delimiter||c_delimiter||
        CASE WHEN v_include_bonus = 1 THEN
        c2_delimiter||bonus_payout||c2_delimiter||c_delimiter
        END||
        c2_delimiter||potential_payout||c2_delimiter||c_delimiter||
        CASE WHEN v_include_stack_rank = 1 THEN
        c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
        END   
   WHEN v_payout_type = 'other' THEN
        c2_delimiter||first_name||c2_delimiter||c_delimiter||
        c2_delimiter||last_name||c2_delimiter||c_delimiter||
        c2_delimiter||org_unit||c2_delimiter||c_delimiter||
        c2_delimiter||activity_description||c2_delimiter||c_delimiter|| 
        c2_delimiter||objective_amount||c2_delimiter||c_delimiter||
        c2_delimiter||activity_amt||c2_delimiter||c_delimiter||        
        c2_delimiter||perc_achieved||c2_delimiter||c_delimiter||   
        c2_delimiter||to_go||c2_delimiter||c_delimiter||    
        c2_delimiter||payout_description||c2_delimiter||c_delimiter||  
        c2_delimiter||Payout_Value||c2_delimiter||c_delimiter||  
        CASE WHEN v_include_stack_rank = 1 THEN
        c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
        END   
     END AS Textline
        FROM 
        (SELECT first_name,last_name,org_unit,activity_description,
                CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(objective_amount,'999999999D99')) ELSE trim(to_char(objective_amount,'999999999D9999')) END objective_amount,  --05/06/2015
--              objective_amount
                CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(activity_amt,'999999999D99')) ELSE trim(to_char(activity_amt,'999999999D9999')) END activity_amt,  --05/06/2015
--              activity_amt,
                perc_achieved,to_go,
                payout_description,Payout_Value,objective_payout,
                bonus_payout,
                CASE WHEN bonus_payout IS NOT NULL 
                    THEN objective_payout + bonus_payout 
                  ELSE objective_payout 
                  END potential_payout,
                stack_rank_position 
        FROM
        (select au.first_name,
             au.last_name,
             n.name org_unit,
             NVL(sc.activity_description,scp.activity_description) as activity_description,
             scp.objective_amount,
             scpp.activity_amt,             
             FLOOR(scpp.activity_amt / scp.objective_amount * 100) AS perc_achieved,
             CASE WHEN NVL(scpp.activity_amt,0)<=scp.objective_amount THEN (scp.objective_amount - NVL(scpp.activity_amt,0))
                ELSE 0 
             END to_go,
             scp.objective_payout_description as payout_description,
             CASE WHEN p.is_opt_out_of_awards = 1 THEN 0
                  WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout 
                    ELSE
                    0
             END Payout_Value,
             CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 
                  WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout 
               ELSE 0
             END objective_payout,        
              CASE WHEN p.is_opt_out_of_awards = 1 THEN 0--05/17/2017
                   WHEN 
                CASE                                                       
                   WHEN scpp.activity_amt > scp.objective_amount --AND p.is_opt_out_of_awards = 0--05/17/2017
                   THEN
                          FLOOR ((scpp.activity_amt - scp.objective_amount)                                        
                        / scp.objective_bonus_increment ) * scp.objective_bonus_payout
                   ELSE
                      0
                END  > OBJECTIVE_BONUS_CAP THEN OBJECTIVE_BONUS_CAP
              ELSE CASE
                   WHEN scpp.activity_amt > scp.objective_amount
                   THEN
                           FLOOR ((scpp.activity_amt - scp.objective_amount)                                        
                        / scp.objective_bonus_increment ) * scp.objective_bonus_payout
                   ELSE
                      0
            END END AS bonus_payout,
                  scpsr.stack_rank_position as stack_rank_position
        from 
                     ssi_contest sc,
                     ssi_contest_participant scp,
                     ssi_contest_pax_progress scpp,
                     ssi_contest_pax_stack_rank scpsr,
                     application_user au,
                     node n,
                     user_node un,
                     participant p
              where      sc.ssi_contest_id = p_in_ssi_contest_id
                     and sc.ssi_contest_id = scp.ssi_contest_id    
                     and scp.ssi_contest_id = scpp.ssi_contest_id
                     AND scpp.activity_amt >= scp.objective_amount
                     and scp.ssi_contest_id = scpsr.ssi_contest_id
                     and scp.user_id = scpp.user_id 
                     and scp.user_id = scpsr.user_id
                     and scp.user_id = au.user_id 
                     and un.user_id  = au.user_id 
                     and au.is_active = 1 
                     and un.node_id  = n.node_id 
                     and un.is_primary = 1
                     AND au.user_id = p.user_id
                 ORDER BY au.last_name                     
                     )));

      ELSIF v_out_contest_type = c_do_this_get_that THEN
              OPEN p_out_ref_cursor  FOR 
             SELECT textline FROM (
                SELECT 1, 
            CASE 
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter|| 
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MINIMUM_QUALIFIER' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_QUALIFIED_PAYOUT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'INCREMENT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                CASE WHEN v_contest_end_date <= SYSDATE AND v_status = 'finalize_results' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POINTS' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                ELSE
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POTENTIAL_POINTS' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END||
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END   
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter|| 
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'MINIMUM_QUALIFIER' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY_QUALIFIED_PAYOUT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'INCREMENT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_QUANTITY' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter||
                CASE WHEN v_contest_end_date <= SYSDATE AND v_status = 'finalize_results' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_VALUE' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                ELSE
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POTENTIAL_VALUE' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END||
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END   
            END AS Textline
                FROM dual
              UNION  ALL
              SELECT 
                (ROWNUM+1),
            CASE 
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||activity_description||c2_delimiter||c_delimiter||       
                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||
                c2_delimiter||min_qualifier||c2_delimiter||c_delimiter||             
                c2_delimiter||qualified_activity||c2_delimiter||c_delimiter||      
                c2_delimiter||increment_dtgt||c2_delimiter||c_delimiter||           
                c2_delimiter||payout_value||c2_delimiter||c_delimiter||       
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter          
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||activity_description||c2_delimiter||c_delimiter||      
                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||
                c2_delimiter||min_qualifier||c2_delimiter||c_delimiter||              
                c2_delimiter||qualified_activity||c2_delimiter||c_delimiter||        
                c2_delimiter||increment_dtgt||c2_delimiter||c_delimiter||          
                c2_delimiter||payout_description||c2_delimiter||c_delimiter||  
                c2_delimiter||payout_quantity||c2_delimiter||c_delimiter||
                c2_delimiter||Total_Potential_Payout_Value||c2_delimiter||c_delimiter|| 
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
                END   
            END  AS Textline
                FROM 
                (      select au.first_name,
                     au.last_name,
                     n.name org_unit,
                     sc.activity_description,
--                     sc.activity_amt,  --05/06/2015
                     CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(sc.activity_amt,'999999999D99')) ELSE trim(to_char(sc.activity_amt,'999999999D9999')) END activity_amt,  --05/06/2015
                     sc.min_qualifier,
                       CASE WHEN sc.activity_amt > sc.min_qualifier AND p.is_opt_out_of_awards = 0 THEN --05/17/2017
                       FLOOR((
                         CASE WHEN sc.activity_amt <= sc.maximum_payout_activity THEN sc.activity_amt 
                         ELSE  sc.maximum_payout_activity END- sc.min_qualifier)/increment_amount)* payout_amount
                       ELSE 0 END payout_value,
                     CASE WHEN activity_amt > min_qualifier AND p.is_opt_out_of_awards = 0 THEN 
                              FLOOR(( 
                               CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt 
                               ELSE  maximum_payout_activity END- min_qualifier)/increment_amount)* payout_amount
                               ELSE 0  
                     END Total_Potential_Payout_Value,
                     CASE WHEN activity_amt > min_qualifier AND p.is_opt_out_of_awards = 0 THEN 
                         FLOOR ((
                         CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt 
                         ELSE  maximum_payout_activity END- min_qualifier)/increment_amount) 
                         ELSE 0  
                     END  payout_quantity,
                     CASE WHEN activity_amt > min_qualifier THEN
                     (activity_amt - min_qualifier) 
                     ELSE 0
                     END qualified_activity,
                       NVL(sc.payout_description,sc.objective_payout_description) as payout_description,                       
                     sc.increment_amount,
                     CASE WHEN sc.increment_amount> 0 THEN 
                     FLOOR((CASE WHEN activity_amt > min_qualifier THEN (activity_amt - min_qualifier) ELSE 0 END)/sc.increment_amount )--03/25/2015 Bug 60667
                     ELSE 0 
                     END AS increment_dtgt ,
                    CASE WHEN include_stack_rank = 1 THEN stack_rank_position ELSE NULL END stack_rank_position
                FROM  ( 
                SELECT sc.ssi_contest_id,
                           sca.ssi_contest_activity_id,
                           sca.description activity_description,
                           scp.user_id,
                           sc.payout_type,
                           scp.objective_payout_description,
                           sca.payout_description,
                           scpp.activity_amt,
                             sca.increment_amount,
                             sca.payout_amount,
                             sca.min_qualifier,
                             sca.payout_cap_amount,
                             sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount AS maximum_payout_activity,
                             (SELECT stack_rank_position FROM ssi_contest_pax_stack_rank WHERE ssi_contest_id = p_in_ssi_contest_id AND ssi_contest_activity_id = sca.ssi_contest_activity_id AND user_id = scp.user_id ) stack_rank_position
                             ,sc.include_stack_rank                     
                      FROM ssi_contest sc, ssi_contest_activity sca, ssi_contest_participant scp,ssi_contest_pax_progress scpp
                     WHERE     
                           sc.ssi_contest_id = p_in_ssi_contest_id and
                           sc.ssi_contest_id = sca.ssi_contest_id
                           AND sc.ssi_contest_id = scp.ssi_contest_id
                           AND scp.user_id = scpp.user_id
                           AND sca.ssi_contest_activity_id = scpp.ssi_contest_activity_id
                           AND scpp.activity_amt >= sca.min_qualifier + increment_amount                           
                           ) sc,                    
                             application_user au,
                             node n,
                             user_node un,
                             participant p
                      where   sc.user_id = au.user_id 
                             and un.user_id  = au.user_id  
                             and un.node_id  = n.node_id 
                             and un.is_primary = 1
                             and au.is_active = 1
                             and au.user_id = p.user_id
                          ORDER BY au.last_name                             
                             ));
      
      ELSIF v_out_contest_type = c_step_it_up THEN
            OPEN p_out_ref_cursor FOR 
             SELECT textline FROM (
                SELECT 1, 
            CASE 
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter|| 
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'BASE_LINE' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LEVEL_COMPLETED' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||                          
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LEVEL_PAYOUT' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||                          
                CASE WHEN v_include_bonus = 1 THEN  
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'BONUS_PAYOUT' and  asset_code = 'ssi_contest.payout_objectives')||c2_delimiter||c_delimiter
                END||
              CASE WHEN v_contest_end_date <= SYSDATE AND v_status = 'finalize_results' THEN
                        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_PAYOUT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                ELSE
                        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'TOTAL_POTENTIAL_PAYOUT' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END||
                CASE WHEN v_include_stack_rank = 1 THEN
                        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END   
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter|| 
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'BASE_LINE' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LEVEL_COMPLETED' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||                          
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_VALUE' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter||
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STACK_RANK' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter
                END   
            END AS Textline
                FROM dual
              UNION  ALL
              SELECT 
                (ROWNUM+1),
            CASE 
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||initcap(baseline)||c2_delimiter||c_delimiter||
                c2_delimiter||activity_amt||c2_delimiter||c_delimiter|| 
                c2_delimiter||level_completed||c2_delimiter||c_delimiter||
                c2_delimiter||level_payout||c2_delimiter||c_delimiter||        
                CASE WHEN v_include_bonus = 1 THEN
                c2_delimiter||bonus_payout||c2_delimiter||c_delimiter
                END||
                c2_delimiter||total_potential_payout||c2_delimiter||c_delimiter||
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
                END   
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||initcap(baseline)||c2_delimiter||c_delimiter||
                c2_delimiter||activity_amt||c2_delimiter||c_delimiter|| 
                c2_delimiter||level_completed||c2_delimiter||c_delimiter||
                c2_delimiter||payout_description||c2_delimiter||c_delimiter||
                c2_delimiter||total_potential_payout||c2_delimiter||c_delimiter||
                CASE WHEN v_include_stack_rank = 1 THEN
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter
                END   
               END AS Textline
                FROM 
                (     SELECT         au.first_name,au.last_name,
                                n.name org_unit, 
                                sit_indv_baseline_type baseline,                          
--                                sc.activity_amt,   --05/06/2015 
                                CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(sc.activity_amt,'999999999D99')) ELSE trim(to_char(sc.activity_amt,'999999999D9999')) END activity_amt,  --05/06/2015                                
                                nvl(sc.level_completed,0) level_completed,
                                CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE sc.level_payout END AS level_payout,--05/17/2017 Start
                                CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE sc.bonus_payout END AS bonus_payout,
                                CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE sc.total_payout END AS total_potential_payout,
--                                SC.level_payout,
--                                sc.bonus_payout,
--                                sc.total_payout total_potential_payout,--05/17/2017 End
                                sc.payout_description as payout_description,                                
                                sc.stack_rank_position
                                FROM (
             SELECT DISTINCT ssi_contest_id,
                             user_id,
                             activity_amt,
                             sit_indv_baseline_type,
                             payout_type,
                             payout_description,
                             CASE WHEN level_completed =0 THEN NULL ELSE level_completed END level_completed,
                             level_payout,
                             CASE WHEN LEVEL_COMPLETED  = max_level THEN bonus_payout ELSE 0 END bonus_payout,
                             level_payout + CASE WHEN LEVEL_COMPLETED  = max_level THEN NVL(CASE WHEN bonus_payout > sit_bonus_cap THEN sit_bonus_cap
                                                                             ELSE bonus_payout END,0)
                                            ELSE 0
                             END  AS total_payout
                             ,stack_rank_position
                             ,CASE WHEN level_completed = 0 THEN sequence_number
                                  ELSE rec_rank
                             END rec_rank
                             FROM (
                    SELECT sc.ssi_contest_id,
                           scp.user_id,
                           scpp.activity_amt,
                           sc.payout_type, 
                           scl.payout_description,
                           scl.sequence_number,
                           scl.goal_amount,
                           CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
                                WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
                                WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number 
                                ELSE 0 
                                END level_completed,
                                RANK() OVER (
                                  PARTITION BY scp.user_id ORDER BY 
                                  CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
                                       WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
                                       WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number 
                                       ELSE 0 
                                  END  DESC) as rec_rank,
                                  CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.payout_amount
                                      WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.payout_amount
                                      WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) 
                                      THEN scl.payout_amount 
                                      ELSE 0 
                                  END level_payout,          
                                  CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN sit_bonus_cap
                                     WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment/sit_bonus_payout THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment)   * sit_bonus_payout               
                                     WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN sit_bonus_cap
                                     WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment/sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))/sit_bonus_increment)* sit_bonus_payout
                                     WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap
                                     WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))/sit_bonus_increment) * sit_bonus_payout
                                     ELSE 0 
                                  END bonus_payout,
                                (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = p_in_ssi_contest_id) max_level,
                                sit_indv_baseline_type,
                                sit_bonus_cap,
                               CASE WHEN sc.include_stack_rank = 1 THEN (SELECT stack_rank_position FROM ssi_contest_pax_stack_rank WHERE ssi_contest_id = p_in_ssi_contest_id AND user_id = scp.user_id ) END stack_rank_position   --03/02/2015                                                 
                          FROM ssi_contest sc,
                               ssi_contest_level scl,
                               ssi_contest_participant scp,
                               ssi_contest_pax_progress scpp
                         WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                               AND sc.ssi_contest_id = scl.ssi_contest_id
                               AND sc.ssi_contest_id = scp.ssi_contest_id
                               AND --scpp.activity_amt >= scl.goal_amount
       (( sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount)
       OR (sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100))
       OR (sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount)))
                               AND scp.user_id = scpp.user_id
                               AND scp.ssi_contest_id = scpp.ssi_contest_id
                               ) WHERE rec_rank = 1 ) sc, application_user au, participant p,user_node un, node n
                               WHERE sc.user_id = au.user_id
                               AND sc.user_id = p.user_id
                               AND sc.user_id = un.user_id
                               AND un.status = 1
                               AND un.is_primary = 1
                               AND au.is_active = 1
                               AND un.node_id = n.node_id
                               AND rec_rank = 1
                    ORDER BY au.last_name
                               ));

      ELSIF v_out_contest_type = c_stack_rank THEN
            OPEN p_out_ref_cursor FOR 
             SELECT textline FROM (
                SELECT 1, 
            CASE 
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter|| 
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'RANK' and  asset_code = 'ssi_contest.payout_stackrank')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'POTENTIAL_PAYOUT' and  asset_code = 'ssi_contest.creator')||c2_delimiter||c_delimiter
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'FIRST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'LAST_NAME' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter|| 
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ORG_UNIT' and  asset_code = 'ssi_contest.preview')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'RANK' and  asset_code = 'ssi_contest.payout_stackrank')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_DESCRIPTION' and  asset_code = 'ssi_contest.details.export')||c2_delimiter||c_delimiter||
                c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PAYOUT_VALUE' and  asset_code = 'ssi_contest.payout_stepitup')||c2_delimiter||c_delimiter
            END AS Textline
                FROM dual
              UNION  ALL
              SELECT 
                (ROWNUM+1),
            CASE 
            WHEN v_payout_type = 'points' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter||    
--                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||  --05/06/2015
                c2_delimiter||CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(activity_amt,'999999999D99')) ELSE trim(to_char(activity_amt,'999999999D9999')) END||c2_delimiter||c_delimiter||  --05/06/2015 
                c2_delimiter||total_payout||c2_delimiter||c_delimiter
            WHEN v_payout_type = 'other' THEN
                c2_delimiter||first_name||c2_delimiter||c_delimiter||
                c2_delimiter||last_name||c2_delimiter||c_delimiter||
                c2_delimiter||org_unit||c2_delimiter||c_delimiter||
                c2_delimiter||stack_rank_position||c2_delimiter||c_delimiter||    
--                c2_delimiter||activity_amt||c2_delimiter||c_delimiter||  --05/06/2015
                c2_delimiter||CASE WHEN v_activity_measure_type = 'currency' THEN trim(to_char(activity_amt,'999999999D99')) ELSE trim(to_char(activity_amt,'999999999D9999')) END||c2_delimiter||c_delimiter||  --05/06/2015 
                c2_delimiter||payout_description||c2_delimiter||c_delimiter||    
                c2_delimiter||total_payout||c2_delimiter||c_delimiter      
               END AS Textline
                FROM 
                (SELECT  scpsr.stack_rank_position ,
                   au.user_id,
                   au.last_name,
                   au.first_name,
                   n.name as org_unit,
                   scpp.activity_amt,
                   sc.activity_description as description,
                  CASE WHEN v_status = 'finalize_results' AND v_payout_type = 'points' THEN ssip.payout_amount 
                       ELSE scsrp.payout_amount 
                  END  as total_payout,   --05/28/2015
                  scsrp.payout_desc as payout_description,
                   sc.activity_measure_type
            FROM ssi_contest_pax_stack_rank scpsr,
                 ssi_contest_pax_progress scpp,
                 ssi_contest_sr_payout scsrp,
                 application_user au,
                 ssi_contest sc,
                 node n,
                 user_node un 
                 ,ssi_contest_pax_payout ssip           --05/28/2015
            WHERE     
                 sc.ssi_contest_id = p_in_ssi_contest_id
             AND sc.ssi_contest_id = scpsr.ssi_contest_id
             AND sc.ssi_contest_id = scsrp.ssi_contest_id  
             AND scpsr.ssi_contest_id = scpp.ssi_contest_id
             AND scpsr.user_id = scpp.user_id
             AND scpsr.user_id = au.user_id  
             AND scpsr.stack_rank_position = scsrp.rank_position 
             AND scpp.activity_amt >= NVL(sc.stack_rank_qualifier_amount,0)
             AND un.user_id  = au.user_id  
             AND un.node_id  = n.node_id 
             AND un.is_primary = 1
             AND au.is_active = 1
             AND scpsr.ssi_contest_id = ssip.ssi_contest_id (+)   --05/28/2015
             AND scpsr.user_id = ssip.user_id (+)       --05/28/2015
                 ORDER BY last_name                
                 ));  
        END IF;  -- End of Contest Type Extract SQL Condition
    
    p_out_return_code :=0 ;
    
     EXCEPTION WHEN OTHERS THEN       
       p_out_return_code :=99;
       prc_execution_log_entry ('prc_ssi_contest_export',1,'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| SQLERRM,NULL);      
      END prc_ssi_contest_app_export;
      
      PROCEDURE prc_ssi_contest_claims_export (   --06/05/2015
      p_in_ssi_contest_id       IN     NUMBER,
      p_in_locale               IN VARCHAR2,      
      p_out_return_code         OUT NUMBER,      
      p_out_ref_cursor          OUT SYS_REFCURSOR
      ) IS  
      
      c_delimiter CONSTANT VARCHAR2(1) := ',' ;
      c2_delimiter CONSTANT VARCHAR2(1) := '"' ;

      c_do_this_get_that constant number := 2;
      c_objectives constant number := 4;
      c_stack_rank constant number := 8;
      c_step_it_up constant number := 16;

      BEGIN
      prc_execution_log_entry ('PRC_SSI_CONTEST_CLAIMS_EXPORT',1,'INFO','Claims Extract for ssi_contest_id:  '|| p_in_ssi_contest_id,NULL);      
      DELETE temp_table_session;

      INSERT INTO temp_table_session (asset_code, cms_name, cms_code)
        SELECT asset_code,cms_value,key 
        FROM vw_cms_asset_value 
        where 
            key in ('CLAIM_NUMBER','DATE_SUBMITTED','CUSTOMER_NAME','ACTIVITY','APPROVED_BY','PARTICIPANTNAME','STATUS') and 
            asset_code in ('ssi_contest.claims','report.claim','ssi_contest.participant')
            and locale = p_in_locale
            UNION
            SELECT asset_code,cms_name,cms_code
                FROM vw_cms_code_value
            WHERE asset_code = 'picklist.ssi.claim.status.items' 
                  AND locale = p_in_locale;

      OPEN p_out_ref_cursor FOR 
        SELECT textline FROM (
        SELECT 1, 
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'CLAIM_NUMBER' and  asset_code = 'ssi_contest.claims')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'DATE_SUBMITTED' and  asset_code = 'ssi_contest.claims')||c2_delimiter||c_delimiter|| 
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'PARTICIPANTNAME' and  asset_code = 'report.claim')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'APPROVED_BY' and  asset_code = 'ssi_contest.claims')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'ACTIVITY' and  asset_code = 'ssi_contest.claims')||c2_delimiter||c_delimiter||
        c2_delimiter||(select cms_name from temp_table_session where cms_code = 'STATUS' and  asset_code = 'ssi_contest.participant')||c2_delimiter||c_delimiter
        AS Textline
       FROM dual
      UNION  ALL
      SELECT 
        (ROWNUM+1),
        c2_delimiter||Claim_Number||c2_delimiter||c_delimiter||
        c2_delimiter||TO_CHAR(submission_date,fnc_java_get_date_pattern(p_in_locale))||c2_delimiter||c_delimiter||  --  09/20/2016     Bug 67638
        c2_delimiter||pax_name||c2_delimiter||c_delimiter||
        c2_delimiter||Approved_By||c2_delimiter||c_delimiter||   
        c2_delimiter||activity_description||c2_delimiter||c_delimiter||
        c2_delimiter||Status_description||c2_delimiter||c_delimiter
        AS Textline
        FROM            --Bug 68062 The previous inner select is removed and the following used to be in sync with ssi_contest_claims_sort proc
        (
         SELECT '#'||scpc.claim_number AS Claim_Number,
                scpc.date_created as submission_date,
                scpc.status,
                (select cms_name from temp_table_session where asset_code = 'picklist.ssi.claim.status.items' and cms_code = scpc.status) AS Status_description, 
                au.first_name ||' , '|| au.last_name AS pax_name, 
                CASE WHEN au_approval.user_id IS NOT NULL 
                        THEN au_approval.first_name ||' , '|| au_approval.last_name 
                     ELSE NULL END AS Approved_By,
                ssi_contest_pax_claim_id,
                sc.ssi_contest_id,
                CASE WHEN sc.contest_type = 4 AND sc.is_same_obj_description = 1
                        THEN sc.activity_description
                     WHEN sc.contest_type = 4 AND sc.is_same_obj_description = 0
                        THEN scp.activity_description
                     ELSE sc.activity_description END AS activity_description
           FROM ssi_contest_pax_claim scpc,
                application_user au,
                application_user au_approval,
                ssi_contest sc,
                ssi_contest_participant scp
          WHERE sc.contest_type <> 2
            AND scpc.submitter_id = au.user_id
            AND scpc.submitter_id = scp.user_id
            AND sc.ssi_contest_id = scp.ssi_contest_id 
            AND scpc.ssi_contest_id = p_in_ssi_contest_id
            AND scpc.ssi_contest_id = sc.ssi_contest_id
            AND scpc.approver_id = au_approval.user_id(+)
           
         UNION ALL
             
         SELECT '#'||claim_number,
                a.date_created as submission_date,
                a.status,
                (select cms_name from temp_table_session where asset_code = 'picklist.ssi.claim.status.items' and cms_code = a.status) AS status_description, 
                au.first_name ||au.last_name AS pax_name,
                CASE WHEN au_approver.user_id IS NOT NULL 
                        THEN au_approver.first_name ||au_approver.last_name
                    ELSE NULL END AS Approved_By,
                 a.ssi_contest_pax_claim_id,
                 sca.ssi_contest_id,
                 LISTAGG (sca.description,',') WITHIN GROUP (ORDER BY rec_rank2) AS activity_description 
           FROM (
                 SELECT ssi_contest_id,
                        ssi_contest_pax_claim_id,
                        claim_number,status,
                        date_created,
                        submitter_id user_id,
                        approver_id,
                        text activity_amount,
                        RANK () OVER (PARTITION BY rec_rank ORDER BY rn) AS rec_rank2
                   FROM (
                         SELECT RANK() OVER (PARTITION BY scpc.ssi_contest_id ORDER BY scpc.claim_number) AS rec_rank,
                                scpc.ssi_contest_id,
                                scpc.status,scpc.
                                ssi_contest_pax_claim_id,
                                scpc.claim_number,
                                scpc.date_created,
                                scpc.submitter_id,
                                scpc.approver_id,
                                trim(regexp_substr(REPLACE(activities_amount_quantity,',',','), '[^,]+', 1, column_value)) text,
                                rownum rn
                           FROM ssi_contest sc,
                                ssi_contest_pax_claim scpc,
                                TABLE(CAST(MULTISET(SELECT LEVEL FROM dual CONNECT BY instr(REPLACE(activities_amount_quantity,',',','), ',', 1, LEVEL - 1) > 0) AS sys.odciNumberList))                                                                 
                          WHERE sc.ssi_contest_id = p_in_ssi_contest_id
                            AND sc.ssi_contest_id = scpc.ssi_contest_id
                            AND sc.contest_type = 2)) a,
                                ssi_contest_activity sca,
                                application_user au,
                                application_user au_approver
                          WHERE a.ssi_contest_id = sca.ssi_contest_id
                            AND a.user_id = au.user_id
                            AND a.approver_id = au_approver.user_id(+)
                            AND a.rec_rank2 = sca.sequence_number
                            AND a.activity_amount IS NOT NULL
                            GROUP BY sca.ssi_contest_id,claim_number,a.status,a.date_created,au.first_name,au.last_name,
                                     au_approver.user_id,au_approver.first_name,au_approver.last_name,a.ssi_contest_pax_claim_id

        ));

    
      p_out_return_code :=0 ;
    
       EXCEPTION WHEN OTHERS THEN       
         p_out_return_code :=99;
         prc_execution_log_entry ('PRC_SSI_CONTEST_CLAIMS_EXPORT',1,'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| SQLERRM,NULL);      
    END prc_ssi_contest_claims_export;
    
PROCEDURE ssi_contest_claims_sort
(p_in_ssi_contest_id     IN     NUMBER,
 p_in_locale                 IN   VARCHAR2,
 p_in_status                 IN VARCHAR2,
 p_in_sortColName     IN     VARCHAR2,
 p_in_sortedBy           IN     VARCHAR2,
 p_in_rowNumStart               IN NUMBER,
 p_in_rowNumEnd                 IN NUMBER,
 p_out_return_code    OUT NUMBER,  
 p_out_claims_count   OUT NUMBER,
 p_out_claims_submitted_count OUT NUMBER,
 p_out_claims_pending_count OUT NUMBER,
 p_out_claims_approved_count OUT NUMBER,
 p_out_claims_denied_count OUT NUMBER,     
 p_out_ref_cursor OUT SYS_REFCURSOR) IS
 
 v_sortCol             VARCHAR2(200);
 l_query VARCHAR2(32767);
 v_contest_type NUMBER;
 BEGIN
 
 DELETE temp_table_session;
 INSERT INTO temp_table_session
 SELECT asset_code,cms_name,cms_code
     FROM vw_cms_code_value E
    WHERE asset_code in ( 'picklist.ssi.claim.status.items') and e.locale = p_in_locale;
 
 SELECT contest_type INTO v_contest_type FROM ssi_contest WHERE ssi_contest_id = p_in_ssi_contest_id;
 
 v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;
 
 l_query  := 'SELECT * FROM
  ( ';
  
  l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                               FROM (
                  SELECT scpc.claim_number AS claimNumber,scpc.date_created as dateSubmitted,scpc.status,
                  (select cms_name from temp_table_session where cms_code = scpc.status) AS status_description, 
                 -- au.first_name ||'' , ''|| au.last_name AS submitter,  -- 04/12/2018
                  au.last_name ||'' , ''|| au.first_name AS submitter,  -- 04/12/2018
                 -- CASE WHEN au_approval.user_id IS NOT NULL THEN au_approval.first_name ||'' , ''|| au_approval.last_name  -- 04/12/2018
                  CASE WHEN au_approval.user_id IS NOT NULL THEN au_approval.last_name ||'' , ''|| au_approval.first_name  -- 04/12/2018
                  ELSE NULL END AS approver, ssi_contest_pax_claim_id,sc.ssi_contest_id,
                  CASE WHEN sc.contest_type = 4 AND sc.is_same_obj_description = 1 --06/16/2015
                     THEN sc.activity_description
                     WHEN sc.contest_type = 4 AND sc.is_same_obj_description = 0 --06/16/2015
                     THEN scp.activity_description
                     ELSE sc.activity_description
                  END AS activity_description
FROM ssi_contest_pax_claim scpc, application_user au, application_user au_approval,ssi_contest sc,ssi_contest_participant scp
WHERE '''||v_contest_type||''' <> 2
AND scpc.submitter_id = au.user_id
AND scpc.submitter_id = scp.user_id
AND sc.ssi_contest_id = scp.ssi_contest_id 
AND scpc.ssi_contest_id = '''||p_in_ssi_contest_id||'''
AND scpc.ssi_contest_id = sc.ssi_contest_id
AND scpc.status IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_status||'''  ) ))
AND scpc.approver_id = au_approval.user_id(+)
UNION ALL
SELECT claim_number,a.date_created as dateSubmitted,a.status,
(select cms_name from temp_table_session where cms_code = a.status) AS status_description, 
-- au.first_name ||'' , ''|| au.last_name AS submitter,  -- 04/12/2018
au.last_name ||'' , ''|| au.first_name AS submitter,  -- 04/12/2018
-- CASE WHEN au_approver.user_id IS NOT NULL THEN au_approver.first_name ||'' , ''|| au_approver.last_name  -- 04/12/2018
CASE WHEN au_approver.user_id IS NOT NULL THEN au_approver.last_name ||'' , ''|| au_approver.first_name  -- 04/12/2018
                  ELSE NULL END AS approver, a.ssi_contest_pax_claim_id,sca.ssi_contest_id,
 LISTAGG (sca.description, '', '') WITHIN GROUP (ORDER BY rec_rank2) FROM (      
      SELECT ssi_contest_id,ssi_contest_pax_claim_id,claim_number,status,date_created,submitter_id user_id,approver_id,text activity_amount,RANK () 
                                                                     OVER (
                                                                        PARTITION BY rec_rank
                                                                        ORDER BY
                                                                           rn)
                                                                        AS rec_rank2 FROM (
     SELECT RANK () 
                                                                     OVER (
                                                                        PARTITION BY ssi_contest_id
                                                                        ORDER BY
                                                                           claim_number)
                                                                        AS rec_rank,
                                                                        ssi_contest_id,status,ssi_contest_pax_claim_id,claim_number,date_created,submitter_id,approver_id,trim(regexp_substr(REPLACE(activities_amount_quantity,'','','' , ''), ''[^,]+'', 1, column_value)) text,rownum rn
    FROM ssi_contest_pax_claim,
      TABLE (CAST (MULTISET
     (SELECT LEVEL FROM dual CONNECT BY instr(REPLACE(activities_amount_quantity,'','','' , ''), '','', 1, LEVEL - 1) > 0)
                   AS sys.odciNumberList
                 )
        )                                                                 
                                                                 WHERE ssi_contest_id= '''||p_in_ssi_contest_id||'''                                                               
                                                                  AND '''||v_contest_type||''' = 2
                                                                 AND status IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_status||'''  ) )))) a,
                                                                 ssi_contest_activity sca,
                                                                 application_user au,
                                                                 application_user au_approver
                                                                 WHERE a.ssi_contest_id = sca.ssi_contest_id
                                                                 AND a.user_id = au.user_id
                                                                 AND a.approver_id = au_approver.user_id(+)
                                                                 AND a.rec_rank2 = sca.sequence_number
                                                                 AND a.activity_amount IS NOT NULL
                                                                 GROUP BY sca.ssi_contest_id,claim_number,a.status,a.date_created,au.first_name,au.last_name,
                                                                 au_approver.user_id,au_approver.first_name,au_approver.last_name,a.ssi_contest_pax_claim_id
ORDER BY '|| v_sortCol ||'
                ) RS) WHERE RN >= ' ||p_in_rowNumStart||' AND RN   <= '|| p_in_rowNumEnd;
 OPEN p_out_ref_cursor FOR l_query;
 
 SELECT COUNT(1) INTO p_out_claims_count
 FROM ssi_contest_pax_claim
 WHERE ssi_contest_id= p_in_ssi_contest_id 
 AND status IN (SELECT * FROM TABLE(get_array_varchar( p_in_status  ) ));
 
  SELECT COUNT(1) INTO p_out_claims_submitted_count
 FROM ssi_contest_pax_claim
 WHERE ssi_contest_id= p_in_ssi_contest_id ;
 
  SELECT COUNT(1) INTO p_out_claims_pending_count
 FROM ssi_contest_pax_claim
 WHERE ssi_contest_id= p_in_ssi_contest_id 
 AND status ='waiting_for_approval';
 
  SELECT COUNT(1) INTO p_out_claims_approved_count
 FROM ssi_contest_pax_claim
 WHERE ssi_contest_id= p_in_ssi_contest_id 
 AND status = 'approved';
 
  SELECT COUNT(1) INTO p_out_claims_denied_count
 FROM ssi_contest_pax_claim
 WHERE ssi_contest_id= p_in_ssi_contest_id 
 AND status = 'denied';
 
 p_out_return_code:=0;
                
 EXCEPTION WHEN OTHERS THEN       
 p_out_return_code :=99;
 prc_execution_log_entry ('SSI_CONTEST_CLAIMS_SORT',1,'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| SQLERRM,NULL);      
     
 END;
 
    PROCEDURE prc_ssi_contest_pax_payout
(p_in_ssi_contest_id     IN     NUMBER,
 p_in_csv_user_ids           IN VARCHAR2,
 p_in_csv_payout_amounts     IN VARCHAR2,
 p_in_csv_payout_desc     IN VARCHAR2,   
 p_in_sortColName     IN     VARCHAR2,
 p_in_sortedBy           IN     VARCHAR2,
 p_in_rowNumStart               IN NUMBER,
 p_in_rowNumEnd                 IN NUMBER,
 p_out_return_code    OUT NUMBER,  
 p_out_pax_count      OUT NUMBER,
 p_out_ref_cursor OUT SYS_REFCURSOR) IS
 
 v_sortCol             VARCHAR2(200);
 l_query VARCHAR2(32767);
 v_payout_type VARCHAR2(100);
 e_no_payout_desc_fail     EXCEPTION;
 BEGIN
 
 DELETE FROM temp_table_session;

                INSERT INTO temp_table_session
                SELECT cav.asset_code,
                          MAX (DECODE (cav.key, 'NAME', cav.cms_value, NULL)) AS cms_name,
                         MAX (DECODE (cav.key, 'EARNED_IMAGE_SMALL', cav.cms_value, NULL)) AS cms_code
                       FROM vw_cms_asset_value cav
                       WHERE asset_code='promotion.badge'
                       AND locale = 'en_US'
                   GROUP BY cav.asset_code,
                            cav.locale,
                            cav.asset_id,
                            cav.content_key_id,
                            cav.content_id;
 
  IF p_in_csv_payout_amounts IS NOT NULL THEN   --Bug 62992 06/24/2015  Suresh J 
     SELECT payout_type INTO v_payout_type FROM ssi_contest WHERE ssi_contest_id = p_in_ssi_contest_id;

     IF v_payout_type = 'other' AND p_in_csv_payout_desc IS NULL THEN
         RAISE e_no_payout_desc_fail;
     ELSE NULL; 
     END IF;

  END IF;
   
 IF upper(p_in_sortColName) <> 'LAST_NAME' THEN
 
 v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy||' ,'|| 'LAST_NAME' || ' ' || p_in_sortedBy||' ,'|| 'FIRST_NAME' || ' ' || p_in_sortedBy;
 
ELSE 
 
 v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;
 
 END IF; 
 
 l_query  := 'SELECT * FROM
  ( ';
  
  l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                               FROM (
                   SELECT scpc.stack_rank_position AS stack_rank,au.user_id,au.first_name, au.last_name,
                  scpc.activity_amount as activity_amt,
                  CASE WHEN p.is_opt_out_of_awards = 1 THEN 0 ELSE scpc.payout_amount END as total_payout,--05/17/2017
                  scpc.payout_description,
                  scsp.badge_rule_id as badge_id,
                           (SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale = ''en_US'') badge_name,
                           cms_badge.image_small_URL badge_image,
                           sc.activity_measure_type,
                           p.avatar_small avatar,
                           p.is_opt_out_of_awards--05/17/2017
FROM ssi_contest_pax_payout scpc, application_user au,ssi_contest sc, ssi_contest_sr_payout scsp,
badge_rule br,participant p,
 (select asset_code, cms_name, cms_code image_small_URL from temp_table_session ) cms_badge
WHERE scpc.user_id = au.user_id
AND scpc.ssi_contest_id = '''||p_in_ssi_contest_id||'''
AND scpc.stack_rank_position = scsp.rank_position
AND scsp.ssi_contest_id = sc.ssi_contest_id
AND scpc.ssi_contest_id = sc.ssi_contest_id
AND scsp.badge_rule_id = br.badge_rule_id  (+)
AND br.cm_asset_key = cms_badge.cms_name (+)
AND au.is_active = 1
AND au.user_id = p.user_id
ORDER BY '|| v_sortCol ||'
                ) RS) WHERE RN >= ' ||p_in_rowNumStart||' AND RN   <= '|| p_in_rowNumEnd;
 
 OPEN p_out_ref_cursor FOR l_query;
   
 MERGE INTO ssi_contest_pax_payout d USING
        (
        SELECT user_id_csv.user_id AS user_id,
       TO_NUMBER(payout_amt_csv.payout_amt) AS payout_amt,
       payout_desc_csv.payout_desc AS payout_desc    
  FROM (SELECT ROWNUM row_num, COLUMN_VALUE AS user_id
          FROM TABLE (get_array_varchar (p_in_csv_user_ids))) user_id_csv,
       (SELECT ROWNUM row_num, COLUMN_VALUE AS payout_amt
          FROM TABLE (get_array_varchar (p_in_csv_payout_amounts))) payout_amt_csv,
          (SELECT ROWNUM row_num, COLUMN_VALUE AS payout_desc
          FROM TABLE (get_array_varchar (p_in_csv_payout_desc))) payout_desc_csv
 WHERE p_in_csv_payout_amounts IS NOT NULL
       AND user_id_csv.row_num = payout_amt_csv.row_num
       AND user_id_csv.row_num = payout_desc_csv.row_num
         ) s  
      ON (d.ssi_contest_id = p_in_ssi_contest_id AND d.user_id = s.user_id)
        WHEN MATCHED THEN UPDATE
        SET payout_amount =  s.payout_amt,
               payout_description = s.payout_desc;               
               
               SELECT COUNT(1) INTO p_out_pax_count
               FROM
               (SELECT scpc.stack_rank_position AS stack_rank,au.user_id,au.first_name, au.last_name,
                  scpc.activity_amount as activity_amt,scsp.payout_amount as total_payout,scsp.payout_desc as payout_description,
                  scsp.badge_rule_id as badge_id,
                           (SELECT cms_value FROM vw_cms_asset_value WHERE asset_code = br.badge_name AND locale = 'en_US') badge_name,
                           cms_badge.image_small_URL badge_image,
                           sc.activity_measure_type,
                           p.avatar_small avatar
FROM ssi_contest_pax_payout scpc, application_user au,ssi_contest sc, ssi_contest_sr_payout scsp,
badge_rule br,participant p,
 (select asset_code, cms_name, cms_code image_small_URL from temp_table_session ) cms_badge
WHERE scpc.user_id = au.user_id
AND scpc.ssi_contest_id = p_in_ssi_contest_id
AND scpc.stack_rank_position = scsp.rank_position
AND scsp.ssi_contest_id = sc.ssi_contest_id
AND scpc.ssi_contest_id = sc.ssi_contest_id
AND scsp.badge_rule_id = br.badge_rule_id  (+)
AND br.cm_asset_key = cms_badge.cms_name (+)
AND au.is_active = 1
AND au.user_id = p.user_id);
               
                p_out_return_code:=0;

                
  EXCEPTION WHEN e_no_payout_desc_fail THEN
   p_out_return_code :=99;
    prc_execution_log_entry( 'PRC_SSI_CONTEST_PAX_PAYOUT', 0, 'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| 'Payout Descriptions are Mandatory',NULL);
      WHEN OTHERS THEN       
         p_out_return_code :=99;
         prc_execution_log_entry ('PRC_SSI_CONTEST_PAX_PAYOUT',1,'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| SQLERRM,NULL);      
 
 END;
 

END;
/
