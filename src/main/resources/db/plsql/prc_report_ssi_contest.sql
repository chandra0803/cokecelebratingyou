CREATE OR REPLACE PROCEDURE prc_report_ssi_contest
  (p_user_id              IN NUMBER,
   p_start_date           IN DATE, 
   p_end_date             IN DATE, 
   p_return_code          OUT NUMBER,
   p_error_message        OUT VARCHAR2
   )
  IS
/******************************************************************************
   NAME:       prc_report_ssi_contest
   PURPOSE:   Populates rpt_ssi_contest_detail table based on ssi_contest_pax_payout data. 
             --ssi_contest_pax_payout contains record for both points & Others               

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        5/13/2015   Swati            Created this procedure.  
              06/08/2015  Suresh J         Bug 62571 - Added TRUNC in sync with Payout Issue Date  
              06/10/2015  Swati            62578 - Reports - Individual Activity Report - SSI Contest - Other Awards are not displayed in Summary table  
              10/01/2015  nagarajs         Bug 64147 - Duplicate records are populating into rpt_ssi_contest_detail table            
******************************************************************************/  
--Execution log variables
  c_process_name            execution_log.process_name%type  := 'prc_report_ssi_contest';
  c_release_level           execution_log.release_level%type := '1';
  v_stage                   VARCHAR2(250);
  c_award_them_now          constant number := 1;  --06/04/2015
  c_do_this_get_that        constant number := 2;  --06/04/2015
BEGIN
    v_stage := 'Start';
    prc_execution_log_entry(c_process_name, c_release_level, 'INFO ', v_stage||'p_user_id->'||p_user_id||'p_start_date->'||p_start_date||'p_end_date->'||p_end_date, NULL);
    
MERGE INTO rpt_ssi_contest_detail d
USING  
    (SELECT 
        scpp.ssi_contest_id,
        sc.cm_asset_code,
        scpp.ssi_contest_activity_id,
        sca.description,
        sc.promotion_id,
        p.promotion_name,
        scpp.user_id,
        scpp.payout_amount,
        scpp.journal_id,
        scpp.award_issuance_number,
        decode(sc.payout_type,'points',scpp.payout_amount,0) POINTS,
        decode(sc.payout_type,'other',CASE WHEN scpp2.activity_amt > sca.min_qualifier THEN 
            FLOOR((CASE WHEN scpp2.activity_amt <= (sca.min_qualifier + sca.payout_cap_amount/sca.payout_amount*increment_amount) 
                    THEN scpp2.activity_amt 
                   ELSE (sca.min_qualifier + sca.payout_cap_amount/sca.payout_amount*increment_amount) END - sca.min_qualifier)/sca.increment_amount )
        ELSE 0 
        END,0) OTHER,
        --decode(sc.payout_type,'other',1,0) OTHER, --06/10/2015 Bug 62578
        decode(sc.payout_type,'other',scpp.payout_amount,0) OTHER_VALUE,
        sc.activity_measure_cur_code,
        sc.payout_issue_date,
        0,
        SYSDATE
    FROM ssi_contest_pax_payout scpp,
       ssi_contest_activity sca,
       ssi_contest sc,
       promotion p,
       ssi_contest_pax_progress scpp2       
    WHERE scpp.ssi_contest_id = sc.ssi_contest_id
        AND sc.status = 'finalize_results'
        AND sc.payout_issue_date IS NOT NULL    
        AND scpp.ssi_contest_activity_id = sca.ssi_contest_activity_id
        AND sc.promotion_id = p.promotion_id
        AND sc.ssi_contest_id = scpp2.ssi_contest_id
        AND sca.ssi_contest_activity_id = scpp2.ssi_contest_activity_id 
        AND scpp.user_id = scpp2.user_id   
        AND sc.contest_type = c_do_this_get_that  
        AND scpp.payout_amount <> 0   --06/04/2015     
        AND sc.payout_issue_date between TRUNC(p_start_date) and TRUNC(p_end_date)  --06/08/2015 
    UNION ALL
    SELECT 
            scpp.ssi_contest_id,
            sc.cm_asset_code,
            scpp.ssi_contest_activity_id,
            sca.description,
            sc.promotion_id,
            p.promotion_name,
            scpp.user_id,
            scpp.payout_amount,
            scpp.journal_id,
            scpp.award_issuance_number,
            decode(sc.payout_type,'points',scpp.payout_amount,0) POINTS,
            decode(sc.payout_type,'other',1,0) OTHER, --06/10/2015 Bug 62578
            decode(sc.payout_type,'other',scpp.payout_amount,0) OTHER_VALUE,
            sc.activity_measure_cur_code,
            CASE WHEN sc.contest_type = c_award_them_now THEN atn.payout_issue_date
             ELSE sc.payout_issue_date 
            END AS payout_issue_date, --06/10/2015 Bug 62578
            0,
            SYSDATE
      FROM ssi_contest_pax_payout scpp,
           ssi_contest_activity sca,
           ssi_contest sc,
           promotion p,
           ssi_contest_atn atn
     WHERE scpp.ssi_contest_id = sc.ssi_contest_id           
        AND scpp.ssi_contest_activity_id = sca.ssi_contest_activity_id(+)
        AND sc.promotion_id = p.promotion_id    
        AND sc.contest_type <> c_do_this_get_that
        AND scpp.ssi_contest_id = atn.ssi_contest_id (+)
        AND scpp.award_issuance_number = atn.issuance_number (+) --10/01/2015
        AND ((sc.contest_type <> c_award_them_now AND sc.status = 'finalize_results')        --06/04/2015       
               OR (sc.contest_type = c_award_them_now AND atn.issuance_status = 'finalize_results')  --06/04/2015
             )
        AND ((sc.contest_type <> c_award_them_now AND sc.payout_issue_date BETWEEN TRUNC(p_start_date) AND TRUNC(p_end_date))        --06/04/2015       --06/08/2015
               OR (sc.contest_type = c_award_them_now AND atn.payout_issue_date BETWEEN TRUNC(p_start_date) AND TRUNC(p_end_date))  --06/04/2015  --06/08/2015
             )
        AND scpp.payout_amount <> 0   --05/28/2015              
    ) s
ON (d.ssi_contest_id = s.ssi_contest_id AND 
    DECODE(s.ssi_contest_activity_id,d.ssi_contest_activity_id,1,0) = 1 AND 
    d.user_id = s.user_id AND     
    s.promotion_id = d.promotion_id
    )
WHEN NOT MATCHED THEN
INSERT 
    (
    RPT_SSI_DETAIL_ID,
    SSI_CONTEST_ID,
    SSI_CONTEST_NAME,
    SSI_CONTEST_ACTIVITY_ID,
    SSI_CONTEST_ACTIVITY_NAME,
    PROMOTION_ID,
    PROMOTION_NAME,
    USER_ID,              
    PAYOUT_AMOUNT,        
    JOURNAL_ID,           
    AWARD_ISSUANCE_NUMBER,
    POINTS,
    OTHER,--Number of payouts earned of other type
    OTHER_VALUE,
    CUR_CODE,
    PAYOUT_ISSUE_DATE,
    CREATED_BY,
    DATE_CREATED
    )
VALUES    
( RPT_SSI_DETAIL_PK_SQ.NEXTVAL,
        s.ssi_contest_id,
        s.cm_asset_code,
        s.ssi_contest_activity_id,
        s.description,
        s.promotion_id,
        s.promotion_name,
        s.user_id,
        s.payout_amount,
        s.journal_id,
        s.award_issuance_number,
        s.POINTS,
        s.OTHER,
        s.OTHER_VALUE,
        s.activity_measure_cur_code,
        s.payout_issue_date,
        0,
        SYSDATE);
    
  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
  
	p_return_code := 0;

EXCEPTION

WHEN OTHERS THEN
     p_return_code := 99;
     p_error_message := SUBSTR(SQLERRM,1,250);
     prc_execution_log_entry(c_process_name,c_release_level,'ERROR','Error at stage: '||
                             v_stage||' --> '||SQLERRM,null);
							 
END prc_report_ssi_contest;