CREATE OR REPLACE PROCEDURE prc_rpt_ssi_award_detail(
  p_user_id              IN NUMBER,  
  p_start_date             IN  DATE,
  p_end_date               IN  DATE,
  p_return_code            OUT NUMBER,
  p_out_error_message      OUT VARCHAR2
) IS
/******************************************************************************
   NAME:       prc_rpt_ssi_award_detail
   PURPOSE:   Populates rpt_ssi_award_detail table based on ssi_contest_pax_payout data. 
             --ssi_contest_pax_payout contains record for both points and Others 
             --but Journal is created where payout_type not equals to Other   

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        5/12/2015   Suresh J       1. Created this procedure.
             06/05/2015   Suresh J        Bug 62472 - SSI Award Report not showing Pax records  
             07/22/2015   Suresh J         Bug 63093 - Report shows wrong other value for DTGT contest
             08/04/2015  Suresh J         Bug 63560 - SSI points not shown in "Awards Received - Participation by Organization" report
             08/07/2015  Suresh J        Bug 63611 - ATN Contest details are not been displayed in Award Received Report         
             08/10/2015  Suresh J        Bug 63642 - Duplicate Award Them Now records are getting displayed in PAX detail page       
             08/10/2015  Ravi Dhanekula Bug # 63635 - Contests in closed status are not picked for reporting.,
             08/11/2015  Suresh J       Bug #63663 - The contest is not getting displayed for having Activity Type = Currency/Revenue and Payout Type = Other
             09/08/2015  Suresh J       Bug #63850 In the report the details are not getting displayed for Payout Type = Other.
             10/14/2016  nagarajs       Award report change as part of new nomination changes
******************************************************************************/
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('prc_rpt_ssi_award_detail');
c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
v_stage                   execution_log.text_line%TYPE;
c_award_them_now      CONSTANT number := 1;  --05/28/2015
c_do_this_get_that    CONSTANT number := 2;  
BEGIN
  v_stage := 'Start';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage||'p_user_id->'||p_user_id||'p_start_date->'||to_char(p_start_date,'MM/DD/YYYY')||'p_end_date->'||to_char(p_end_date,'MM/DD/YYYY') , NULL);

   
MERGE INTO rpt_ssi_award_detail d
USING (
SELECT 
        sp.ssi_contest_id,            
        NULL ssi_contest_activity_id,   
        sc.promotion_id,              
        sp.user_id,                   
        j.journal_id,
        ua.country_id,
        n.NAME AS node_name,
        n.node_id,
        j.transaction_amt AS trans_amount,
        j.transaction_date AS trans_date,        
        pe.position_type,
        pe.department_type department,
        sc.cm_asset_code as ssi_contest_name,          
        au.user_name,                 
        au.first_name,            
        au.middle_name,           
        au.last_name,             
        NULL ssi_contest_activity_name, 
        p.promotion_name promotion_name,            
        sc.payout_type,
        sp.payout_amount,             
        sc.payout_issue_date,
           DECODE(sc.payout_type,'other',1,0) 
            other,    --07/22/2015
        scp.objective_payout_description AS payout_description  --10/14/2016 
FROM ssi_contest_pax_payout sp,
     ssi_contest sc,     
     journal j,
     application_user au,
     user_node un,
     user_address ua,
     vw_curr_pax_employer pe,
     node n,
     promotion p,    
     ssi_contest_pax_progress scpp , 
     ssi_contest_participant scp    --10/14/2016      
WHERE       sp.user_id = au.user_id
        AND au.user_id  = ua.user_id
        AND sc.contest_type NOT IN (c_award_them_now,c_do_this_get_that)
        AND au.user_id = pe.user_id
        AND au.user_id = un.user_id
        AND un.is_primary =1
        AND ua.is_primary = 1 
        AND un.node_id = n.node_id
        AND sp.journal_id = j.journal_id(+) 
        AND sp.user_id = j.user_id(+)-- Bug # 63850
        AND sp.ssi_contest_id = sc.ssi_contest_id
        AND sc.promotion_id = p.promotion_id
        AND sc.status IN ('finalize_results','closed')
        AND sc.payout_issue_date BETWEEN trunc(p_start_date) AND trunc(p_end_date)        --05/28/2015   --06/05/2015  
        AND sp.payout_amount <> 0   --05/28/2015       
        AND sp.ssi_contest_id = scpp.ssi_contest_id    --08/07/2015
        AND sp.user_id = scpp.user_id   --08/07/2015
        AND scpp.ssi_contest_id = scp.ssi_contest_id --10/14/2016
        AND scpp.user_id = scp.user_id   --10/14/2016
        UNION ALL
        SELECT 
        sp.ssi_contest_id,            
        sca.ssi_contest_activity_id,   
        sc.promotion_id,              
        sp.user_id,                   
        j.journal_id,
        ua.country_id,
        n.NAME AS node_name,
        n.node_id,
        j.transaction_amt AS trans_amount,
        j.transaction_date AS trans_date,        
        pe.position_type,
        pe.department_type department,
        sc.cm_asset_code as ssi_contest_name,          
        au.user_name,                 
        au.first_name,            
        au.middle_name,           
        au.last_name,             
        sca.description ssi_contest_activity_name, 
        p.promotion_name promotion_name,            
        sc.payout_type,
        sp.payout_amount,             
        sc.payout_issue_date
            ,CASE WHEN sc.contest_type = c_do_this_get_that THEN
                 DECODE( sc.payout_type,'other',CASE WHEN scpp.activity_amt > sca.min_qualifier THEN    --07/22/2015 
                    FLOOR((CASE WHEN scpp.activity_amt <= (sca.min_qualifier + sca.payout_cap_amount/sca.payout_amount*increment_amount) 
                            THEN scpp.activity_amt 
                           ELSE (sca.min_qualifier + sca.payout_cap_amount/sca.payout_amount*increment_amount) END - sca.min_qualifier)/sca.increment_amount )
                ELSE 0 
                END,0) 
             ELSE DECODE(sc.payout_type,'other',1,0) 
            END other,    --07/22/2015
        scp.objective_payout_description AS payout_description  --10/14/2016
FROM ssi_contest_pax_payout sp,
     ssi_contest sc,
     ssi_contest_activity sca,
     journal j,
     application_user au,
     user_node un,
     user_address ua,
     vw_curr_pax_employer pe,
     node n,
     promotion p
     ,ssi_contest_pax_progress scpp  
     ,ssi_contest_participant scp    --10/14/2016      
WHERE       sp.user_id = au.user_id
        AND au.user_id  = ua.user_id
        AND au.user_id = pe.user_id
        AND au.user_id = un.user_id
        AND un.is_primary =1
        AND ua.is_primary = 1 
        AND sc.contest_type = c_do_this_get_that
        AND un.node_id = n.node_id
        AND sp.journal_id = j.journal_id (+)
        AND sp.user_id = j.user_id (+)
        AND sp.ssi_contest_id = sc.ssi_contest_id 
        AND sp.ssi_contest_id = sca.ssi_contest_id (+)   --08/04/2015
        AND sp.ssi_contest_activity_id = sca.ssi_contest_activity_id (+)
        AND sc.promotion_id = p.promotion_id
        AND  sc.status IN ('finalize_results','closed')        --05/28/2015       , Bug # 63635        
        AND sc.payout_issue_date BETWEEN trunc(p_start_date) AND trunc(p_end_date)        --05/28/2015   --06/05/2015              
        AND sp.payout_amount <> 0   --05/28/2015       
        AND sp.ssi_contest_id = scpp.ssi_contest_id    --08/07/2015
        AND sp.user_id = scpp.user_id  --08/07/2015         
        AND sca.ssi_contest_activity_id = scpp.ssi_contest_activity_id  --08/04/2015
        AND scpp.ssi_contest_id = scp.ssi_contest_id --10/14/2016
        AND scpp.user_id = scp.user_id   --10/14/2016         
        UNION ALL
        SELECT 
        sp.ssi_contest_id,            
        NULL ssi_contest_activity_id,   
        sc.promotion_id,              
        sp.user_id,                   
        j.journal_id,
        ua.country_id,
        n.NAME AS node_name,
        n.node_id,
        j.transaction_amt AS trans_amount,
        j.transaction_date AS trans_date,        
        pe.position_type,
        pe.department_type department,
        sc.cm_asset_code as ssi_contest_name,          
        au.user_name,                 
        au.first_name,            
        au.middle_name,           
        au.last_name,             
        NULL ssi_contest_activity_name, 
        p.promotion_name promotion_name,            
        sc.payout_type,
        sp.payout_amount,             
       atn.payout_issue_date,
         DECODE(sc.payout_type,'other',1,0) other,
       scp.objective_payout_description AS payout_description  --10/14/2016         
FROM ssi_contest_pax_payout sp,
     ssi_contest sc,
     journal j,
     application_user au,
     user_node un,
     user_address ua,
     vw_curr_pax_employer pe,
     node n,
     promotion p
     ,ssi_contest_atn atn
     ,ssi_contest_participant scp    --10/14/2016   
WHERE       sp.user_id = au.user_id
        AND au.user_id  = ua.user_id
        AND au.user_id = pe.user_id
        AND au.user_id = un.user_id
        AND un.is_primary =1
        AND ua.is_primary = 1 
        AND sc.contest_type =c_award_them_now
        AND un.node_id = n.node_id
        AND sp.journal_id = j.journal_id (+)
        AND sp.user_id = j.user_id (+)   --08/11/2015
        AND sp.ssi_contest_id = sc.ssi_contest_id 
        AND sp.ssi_contest_id = atn.ssi_contest_id
        AND sc.promotion_id = p.promotion_id
        AND atn.issuance_status = 'finalize_results'             
        AND atn.payout_issue_date BETWEEN trunc(p_start_date) AND trunc(p_end_date)  --05/28/2015    --06/05/2015        
        AND sp.payout_amount <> 0   --05/28/2015       
        AND sp.award_issuance_number = atn.issuance_number
        AND sp.ssi_contest_id = scp.ssi_contest_id --10/14/2016
        AND sp.user_id = scp.user_id   --10/14/2016
) s
ON (d.ssi_contest_id = s.ssi_contest_id AND 
    DECODE(s.ssi_contest_activity_id,d.ssi_contest_activity_id,1,0) = 1 AND 
    d.user_id = s.user_id AND 
    s.node_id = d.node_id AND
    s.promotion_id = d.promotion_id
    )
WHEN NOT MATCHED THEN
INSERT
(
     rpt_ssi_award_detail_id, 
     ssi_contest_id, 
     ssi_contest_activity_id, 
     promotion_id, 
     node_id, 
     user_id, 
     journal_id, 
     country_id,
     node_name, 
     ssi_contest_name, 
     user_name, 
     pax_first_name, 
     pax_middle_name, 
     pax_last_name, 
     position_type, 
     department, 
     ssi_contest_activity_name, 
     promotion_name,
     payout_type, 
     payout_amount, 
     payout_issue_date, 
     payout_description, --10/14/2016
     created_by, 
     date_created
     ,other    --07/22/2015
 )
 VALUES
(
     RPT_SSI_AWARD_DETAIL_PK_SQ.NEXTVAL, 
     s.ssi_contest_id, 
     s.ssi_contest_activity_id, 
     s.promotion_id, 
     s.node_id, 
     s.user_id, 
     s.journal_id, 
     s.country_id,
     s.node_name, 
     s.ssi_contest_name, 
     s.user_name, 
     s.first_name, 
     s.middle_name, 
     s.last_name, 
     s.position_type, 
     s.department, 
     s.ssi_contest_activity_name, 
     s.promotion_name,
     s.payout_type, 
     s.payout_amount,
     s.payout_issue_date, 
     s.payout_description, --10/14/2016
     p_user_id, 
     SYSDATE
     ,s.other    --07/22/2015     
);

  
  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  p_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    p_return_code := 99;
    p_out_error_message := v_stage || ': ' || SQLCODE || ', ' || SQLERRM;    
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', p_out_error_message, NULL);

END prc_rpt_ssi_award_detail;
/
