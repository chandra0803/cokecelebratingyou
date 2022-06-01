CREATE OR REPLACE PROCEDURE prc_report_refresh
  (pi_requested_user_id  IN  NUMBER,
   po_return_code        OUT NUMBER,
   po_error_message      OUT VARCHAR2
   )
IS

PRAGMA AUTONOMOUS_TRANSACTION;

/******************************************************************************
   NAME:       PRC_REPORT_REFRESH
   PURPOSE:    To call all the report refresh procedures

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        06/12/2012  Ravi Dhanekula      Initial version..
              09/14/2012  Chidamba            commented for Qa testing only recognition and nomination module
              12/20/2012  Arun S              Added calling prc_report_promo_node_activity
              01/25/2013  Chidamba           Defect # 1967  - Open  - Report Refresh Process 
                                             1. Report refresh runs from previous successful refresh date whenever there is any failures
                                             2. Log files to be updated with appropriate failure notifications               
   2.0        02/01/2013  Ravi Dhanekula     Added lokkup for a specific module in system variable before attempting report refresh
                                             Added mailing process from Oracle itself instead of Java sending the E-mail.
              02/08/2013  Chidamba           Defect # 2528 - To avoide duplicate values,
                                             Commenting pkg_populate_promo_elig.p_pop_paxpromocritaudreceiver since both Pax and Criteria list 
                                             type are getting processed in pkg_populate_promo_elig.p_populate_PaxPromoEligibility.
              07/29/2013  Ravi Dhanekula     Added Goalquest,plateau awards and Challengepoint processes for report refresh.  
              10/1/2013   Ravi Dhanekula     Added Goalquest and Challengepoint Mgr override processes for report refresh.       
              12/10/2013  Ravi Dhanekula     Added Survey module to the report refresh process.    
              06/12/2014  Ravi Dhanekula     Added a call to pkg_report_common.p_rpt_pax_employer.
              07/14/2014  murphyc            Added calls to pkg_populate_promo_elig.p_rpt_pax_elig_allaud_team AND p_rpt_pax_elig_allaud_node
              07/22/2014  murphyc            Added calls to pkg_populate_promo_elig.p_rpt_pax_elig_speaud_team AND p_rpt_pax_elig_speaud_node          
              08/12/2014  Ravi Dhanekula     Added a seperate process for quiz eligibility data.
              02/05/2015  Suresh J           Fixed Bug 59389 - refresh_date is getting saved with failure     
              05/08/2015  Ravi Dhanekula     Added new procedure to clear LOB memory. 
              05/12/2015  Swati                 Added calls to prc_report_ssi_contest
              05/12/2015  Suresh J           Added calls to  prc_rpt_ssi_award_detail table for SSI Award CR  
              05/28/2015  Swati              Bug 62471 - Reports - Individual Activity Report - SSI Contest - Points are not displayed in Summary and Charts
              06/03/2015  Swati              Bug 62484 - GoalQuest with Plateau ? The GoalQuest with Plateau award issued to a Participant is not getting displayed in any of the Plateau related reports
              10/29/2015 Suresh J           Bug # 64471 - Survey Extract Report Excel is blank even after taking surveys with response count more than survey.report.response.count value  
              12/15/2015 nagarajs           Added calls to pkg_report_work_happier.prc_work_happier_detail
              12/21/2015 nagarajs           Bug 65020 - PRC_REPORT_REFRESH refresh_prev_date is getting set to refresh_date
              05/18/2016 Ravi Dhanekula     5.6.3 enhancements. Added refresh process for Behavior report.
              10/06/2016 nagarajs           5.6.3 enhancements, Added refresh porcess call for cash budget balance
              11/01/2016 J Flees             Added CMS badge material view refresh.
              12/19/2016 J Flees             Added user connection cleanup.
              04/21/2017 nagarajs           Commented P_RPT_PURL_CONTRIBUTION_SUM and P_RPT_RECOGNITION_SUMMARY call as part of performace tunning.
              09/01/2017 murphyc            Bug 73916/G6-2996 - refresh_date is getting saved with failure (replace 02/05/2015 changes)
                                            write log at begin of prc
              04/05/2018 Gorantla           G6-4006/Bug 75967 - materialized views refresh order fix
              04/01/2019 Suresh J           SA Integeration with DayMaker changes for reports       
              05/06/2019 Loganathan         Checking AWS enabled or not.If AWS enabled,avoiding the  plateau reports which required the DB link.        
******************************************************************************/

  --Exceptions
  e_report_fail     EXCEPTION;
  
  --Procedure variables
  v_start_date      rpt_refresh_date.refresh_prev_date%TYPE;
  v_end_date        rpt_refresh_date.refresh_date%TYPE;
  v_return_code     NUMBER         := 0;
  v_process         VARCHAR2(30)   := 'PRC_REPORT_REFRESH';
  v_error_message   VARCHAR2(4000) := NULL;
  v_stage           VARCHAR2(500);
  is_plateau_required NUMBER;

--  v_bkup_elig_start_date  DATE := TRUNC(SYSDATE);  --02/05/2015   -- 09/01/2017
--  v_bkup_elig_end_date    DATE := TRUNC(SYSDATE);    --02/05/2015  -- 09/01/2017
--  v_bkup_core_start_date  DATE := TRUNC(SYSDATE);  --02/05/2015  -- 09/01/2017
--  v_bkup_core_end_date    DATE := TRUNC(SYSDATE);    --02/05/2015  -- 09/01/2017
--  v_cnt_prev_process_rec  NUMBER := 0;        --02/05/2015 -- 09/01/2017
--  v_bkup_elig_boolean     NUMBER := 0;        --02/05/2015 -- 09/01/2017
--  v_bkup_core_boolean     NUMBER := 0;        --02/05/2015 -- 09/01/2017
  v_boolean               NUMBER(1):=0;        --12/15/2015
  
  c_init_hold_date        CONSTANT rpt_refresh_date.refresh_date%TYPE := TO_DATE('01/01/1900', 'MM/DD/YYYY'); -- 09/01/2017
  c_final_cat_type        CONSTANT rpt_refresh_date.report_category_type%TYPE := 'cashbudget';	-- 09/01/2017 update if addl processing added to PRC
  v_hold_prev_date        rpt_refresh_date.refresh_prev_date%TYPE; -- 09/01/2017
  v_hold_date             rpt_refresh_date.refresh_date%TYPE; -- 09/01/2017
  v_success_count         INTEGER;
  v_sa_enabled                           os_propertyset.boolean_val%TYPE;  --04/01/2019
  v_aws_enabled                          os_propertyset.boolean_val%TYPE;  --05/06/2019
  
  --Cursor  
  CURSOR cur_promo_type IS
    SELECT DISTINCT promotion_type
      FROM promotion
     WHERE promotion_status IN ('live','expired');  

   ----------------
   -- In-Line
   -- Gets the latest refresh date for the specified report
   -- and inserts a new report refresh record returning the previous refresh date
PROCEDURE set_rpt_refresh_date
      (pi_report_category_type        IN rpt_refresh_date.report_category_type%TYPE,
      -- pi_report_type        IN rpt_refresh_date.report_type%TYPE,
       pi_refresh_date       IN rpt_refresh_date.refresh_date%TYPE,
       po_refresh_prev_date  OUT rpt_refresh_date.refresh_prev_date%TYPE
       ) IS

      v_default_refresh_prev_date   CONSTANT rpt_refresh_date.refresh_prev_date%TYPE := TO_DATE('01/01/1900', 'MM/DD/YYYY');
BEGIN
    BEGIN

      SELECT refresh_date
        INTO po_refresh_prev_date
        FROM rpt_refresh_date
       WHERE UPPER(report_category_type) = UPPER(pi_report_category_type);
    EXCEPTION WHEN NO_DATA_FOUND THEN
       po_refresh_prev_date :='';
    END;
             
      prc_execution_log_entry( 'set_rpt_refresh_date', 0, 'SUCCESS', NULL, NULL);
     
    IF (po_refresh_prev_date IS NULL) THEN
    po_refresh_prev_date := v_default_refresh_prev_date;
      INSERT INTO rpt_refresh_date
                  (report_category_type,                  
                   refresh_date,
                   refresh_prev_date
                   )
           VALUES (pi_report_category_type,                 
                   pi_refresh_date,
                   po_refresh_prev_date
                   );
    ELSE

    UPDATE rpt_refresh_date
    SET refresh_date =pi_refresh_date,
           refresh_prev_date = po_refresh_prev_date
    WHERE report_category_type = pi_report_category_type;
          -- return default date when previous refresh date not found
      
      END IF;

    EXCEPTION
      WHEN OTHERS THEN
        prc_execution_log_entry(v_process, 0, 
                                'ERROR',
                                'Failure in in-line set_rpt_refresh_date'
                                   || '<, pi_report_category_type >' || pi_report_category_type                                  
                                   || '<, pi_refresh_date >' || pi_refresh_date
                                   || '<, '
                                   || SQLERRM, NULL);
        RAISE;

    END set_rpt_refresh_date;
    
   FUNCTION FNC_CHECK_FOR_MODULE(p_module VARCHAR2)
   RETURN NUMBER IS
   v_boolean NUMBER(1):=0;
   
   BEGIN
   SELECT boolean_val INTO v_boolean FROM os_propertyset WHERE entity_name='install.'||p_module;
   
   RETURN v_boolean;
   EXCEPTION WHEN OTHERS THEN
   RETURN 0;
   END;
   
   

--Main procedure begin starts
BEGIN
  
  prc_execution_log_entry( v_process, 0, 'INFO','Report Refresh Started',NULL); -- 09/01/2017
  
  po_return_code := 0;
  v_end_date     := SYSDATE;

  v_stage := 'To check on new SA enabled';
    BEGIN
      SELECT boolean_val                            --04/01/2019
        INTO v_sa_enabled
        FROM os_propertyset
       WHERE entity_name = 'new.service.anniversary.enabled';
     EXCEPTION
         WHEN OTHERS THEN
         v_sa_enabled := 0;
     END;

   v_stage := 'Refresh CMS MViews';
   --dbms_mview.refresh('mv_cms_code_value');   --  04/05/2018
   --dbms_mview.refresh('mv_cms_asset_value');  --  04/05/2018
   dbms_mview.refresh('mv_cms_asset_value');    --  04/05/2018
   dbms_mview.refresh('mv_cms_code_value');     --  04/05/2018
   dbms_mview.refresh('mv_cms_badge');          -- 11/01/2016 line added
   
   -- 09/01/2017 start
	 -- need to check the last cat_type processed by this PRC to insure the PRC has run successfully at least once
	 --         if it hasn't run successfully at least once, need to use c_init_hold_date
	 v_stage := 'check for successful run';
	 SELECT COUNT(1)
	 INTO   v_success_count
	 FROM   rpt_refresh_date
	 WHERE  report_category_type = c_final_cat_type;
	 
	 IF v_success_count > 0 THEN
	 
		 v_stage := 'get rpt_refresh_date dates';
		 BEGIN 
			 SELECT NVL(MIN(refresh_date),c_init_hold_date),
							NVL(MIN(refresh_prev_date),c_init_hold_date)
			 INTO   v_hold_date,
							v_hold_prev_date
			 FROM   rpt_refresh_date;
		 EXCEPTION
			 WHEN OTHERS THEN
				 v_hold_date := c_init_hold_date;
				 v_hold_prev_date := c_init_hold_date;
		 END;
		 
	 ELSE	 
		 v_hold_date := c_init_hold_date;
		 v_hold_prev_date := c_init_hold_date;
   END IF; -- IF v_success_count > 0
   -- 09/01/2017 end

  v_stage := 'Call Pkg_populate_promo_elig';
  FOR rec_promo_type IN cur_promo_type LOOP

    pkg_populate_promo_elig.p_populate_PaxPromoEligibility (rec_promo_type.promotion_type,
                                                            100000,
                                                            v_return_code
                                                            );

    IF v_return_code != 0 THEN 
      po_return_code := v_return_code; 
      po_error_message := 'p_populate_PaxPromoEligibility - '||v_error_message;       
      RAISE e_report_fail;
    END IF;
    
   /* pkg_populate_promo_elig.p_pop_paxpromocritaudreceiver (rec_promo_type.promotion_type,     --02/08/2013 start
                                                           pi_requested_user_id,
                                                           v_return_code,
                                                           v_error_message);

    IF v_return_code != 0 THEN 
      po_return_code := v_return_code; 
      po_error_message := 'p_pop_paxpromocritaudreceiver - '||v_error_message;       
      RAISE e_report_fail;   
    END IF;*/                                                                                    --02/08/2013 end    
    
  END LOOP;
  
  v_stage := 'p_populate_PaxPromoElig_Quiz' ; --08/12/2014
      pkg_populate_promo_elig.p_populate_PaxPromoElig_Quiz (
                                                            100000,
                                                            v_return_code
                                                            );

    IF v_return_code != 0 THEN 
      po_return_code := v_return_code; 
      po_error_message := 'p_populate_PaxPromoElig_Quiz - '||v_error_message;       
      RAISE e_report_fail;
    END IF;

/* -- 09/01/2017
    BEGIN    --02/05/2015
      v_bkup_elig_boolean := 1;
      SELECT refresh_prev_date,refresh_date
        INTO v_bkup_elig_start_date,v_bkup_elig_end_date
        FROM rpt_refresh_date
       WHERE UPPER(report_category_type) = UPPER('eligiblility');

    EXCEPTION 
      WHEN NO_DATA_FOUND THEN
       v_bkup_elig_start_date := NULL;
       v_bkup_elig_end_date :=NULL;
    
      WHEN OTHERS THEN
       v_bkup_elig_boolean := 0;
    END;
*/ -- 09/01/2017      
      set_rpt_refresh_date('eligiblility', v_end_date, v_start_date);
      prc_execution_log_entry('pkg_populate_promo_elig', 0, 'SUCCESS', NULL, NULL);
      
--Core Report Refresh 

   BEGIN
   
     v_stage := 'p_rpt_pax_employer' ; --06/12/2014
      pkg_report_common.p_rpt_pax_employer(pi_requested_user_id,                                               
                                                 po_return_code,
                                                 po_error_message);
      
      IF po_return_code != 0 THEN
        po_error_message := 'p_rpt_pax_employer - '||po_error_message;       
        RAISE e_report_fail;
      END IF;
   
          v_stage := 'p_rpt_hierarchy_populate' ;
      pkg_report_common.p_rpt_hierarchy_populate(pi_requested_user_id,
                                                 v_start_date,
                                                 v_end_date,
                                                 po_return_code,
                                                 po_error_message);
      
      IF po_return_code != 0 THEN
        po_error_message := 'p_rpt_hierarchy_populate - '||po_error_message;       
        RAISE e_report_fail;
      END IF;
      
      v_stage := 'p_rpt_hierarchy_summary';
      pkg_report_common.p_rpt_hierarchy_summary(pi_requested_user_id,
                                                v_start_date,
                                                v_end_date,
                                                po_return_code,
                                                po_error_message);    
      
      IF po_return_code != 0 THEN
        po_error_message := 'p_rpt_hierarchy_summary - '||po_error_message;       
        RAISE e_report_fail;
      END IF;
       prc_execution_log_entry('P_RPT_HIERARCHY_ROLLUP',1,'INFO',v_stage||'Completed'
                          ,NULL);
                          
                           v_stage := 'p_rpt_hierarchy_rollup';
      pkg_report_common.p_rpt_hierarchy_rollup;
--      (pi_requested_user_id,
--                                                v_start_date,
--                                                v_end_date,
--                                                po_return_code,
--                                                po_error_message);    
      
      IF po_return_code != 0 THEN
        po_error_message := 'p_rpt_hierarchy_rollup - '||po_error_message;       
        RAISE e_report_fail;
      END IF;

      -- 12/19/2016 begin block add
      v_stage := 'p_user_connection_purge';
      pkg_report_common.p_user_connection_purge(30, po_return_code);

      IF po_return_code != 0 THEN
        po_error_message := v_stage;       
        RAISE e_report_fail;
      END IF;
      -- 12/19/2016 end block add
   END;
/* -- 09/01/2017      
    BEGIN    --02/05/2015
      v_bkup_core_boolean := 1;
      SELECT refresh_prev_date,refresh_date
        INTO v_bkup_core_start_date,v_bkup_core_end_date
        FROM rpt_refresh_date
       WHERE UPPER(report_category_type) = UPPER('Core');

    EXCEPTION 
    WHEN NO_DATA_FOUND THEN
       v_bkup_core_start_date := NULL;
       v_bkup_core_end_date :=NULL;

    WHEN OTHERS THEN
       v_bkup_core_boolean := 0;
    END;
*/ -- 09/01/2017  
      set_rpt_refresh_date('Core', v_end_date, v_start_date);
      prc_execution_log_entry('RPT_HIERARCHY', 0, 'SUCCESS', NULL, NULL);
      
         BEGIN
      v_stage := 'pkg_populate_promo_elig.p_rpt_pax_elig_speaud_team' ; -- 07/22/2014
       pkg_populate_promo_elig.p_rpt_pax_elig_speaud_team (pi_requested_user_id,v_return_code, v_error_message);      
      IF po_return_code != 0 THEN
        po_error_message := 'pkg_populate_promo_elig.p_rpt_pax_elig_speaud_team - '||po_error_message;       
        RAISE e_report_fail;
      ELSE                            
        prc_execution_log_entry('P_RPT_PAX_ELIG_SPEAUD_TEAM', 0, 'SUCCESS', NULL, NULL);
      END IF;
                          
      v_stage := 'Call p_rpt_pax_elig_speaud_node';  -- 07/22/2014
      pkg_populate_promo_elig.p_rpt_pax_elig_speaud_node (pi_requested_user_id,v_return_code, v_error_message); 
      IF v_return_code != 0 THEN 
        po_return_code := v_return_code; 
        po_error_message := 'p_rpt_pax_elig_speaud_node - '||v_error_message;       
        RAISE e_report_fail;
      ELSE                            
        prc_execution_log_entry('P_RPT_PAX_ELIG_SPEAUD_NODE', 0, 'SUCCESS', NULL, NULL);
      END IF;
                          
      v_stage := 'Call p_rpt_pax_elig_allaud_team';  -- 07/14/2014
      pkg_populate_promo_elig.p_rpt_pax_elig_allaud_team (pi_requested_user_id,v_return_code, v_error_message); 
      IF v_return_code != 0 THEN 
        po_return_code := v_return_code; 
        po_error_message := 'p_rpt_pax_elig_allaud_team - '||v_error_message;       
        RAISE e_report_fail;
      ELSE                            
        prc_execution_log_entry('P_RPT_PAX_ELIG_ALLAUD_TEAM', 0, 'SUCCESS', NULL, NULL);
      END IF;
                          
      v_stage := 'Call p_rpt_pax_elig_allaud_node';  -- 07/14/2014
      pkg_populate_promo_elig.p_rpt_pax_elig_allaud_node (pi_requested_user_id,v_return_code, v_error_message); 
      IF v_return_code != 0 THEN 
        po_return_code := v_return_code; 
        po_error_message := 'p_rpt_pax_elig_allaud_node - '||v_error_message;       
        RAISE e_report_fail;
      ELSE                            
        prc_execution_log_entry('P_RPT_PAX_ELIG_ALLAUD_NODE', 0, 'SUCCESS', NULL, NULL);
      END IF;
    EXCEPTION
    WHEN OTHERS THEN
      prc_execution_log_entry('PRC_RPT_REFRESH',1,'ERROR',v_stage
                          ,NULL);
      v_stage := SUBSTR('Error '||TO_CHAR(SQLCODE)||': '||SQLERRM, 1, 255) ;
      prc_execution_log_entry('PRC_RPT_REFRESH',1,'ERROR',v_stage
                          ,NULL);

         RAISE e_report_fail;   
    END;
      
      
       BEGIN
      v_stage := 'PRC_INSERT_CHAR_LOOKUP' ;
      pkg_report_common.prc_insert_char_lookup;
      
    EXCEPTION
    WHEN OTHERS THEN
   
      prc_execution_log_entry('PRC_RPT_REFRESH',1,'ERROR',v_stage
                          ,NULL);
      v_stage := SUBSTR('Error '||TO_CHAR(SQLCODE)||': '||SQLERRM, 1, 255) ;
      prc_execution_log_entry('PRC_RPT_REFRESH',1,'ERROR',v_stage
                          ,NULL);
      RAISE e_report_fail;    
    END;
 prc_execution_log_entry('prc_report_refresh.sql',1,'INFO',v_stage||'Completed'
                          ,NULL);
   BEGIN
      v_stage := 'pkg_report_characteristic.p_rpt_characteristics' ;
      pkg_report_characteristic.p_rpt_characteristics;
      
      IF po_return_code != 0 THEN
         po_error_message := 'pkg_report_characteristic.p_rpt_characteristics - '||po_error_message;       
         RAISE e_report_fail;
      END IF;   
      prc_execution_log_entry('PRC_RPT_REFRESH',1,'INFO',v_stage||'Completed',NULL);

      v_stage := 'Call prc_rpt_user_char_refresh';  
      pkg_report_characteristic.prc_rpt_user_char_refresh (pi_requested_user_id,v_return_code, v_error_message); 

      IF v_return_code != 0 THEN 
         po_return_code := v_return_code; 
         po_error_message := 'prc_rpt_user_char_refresh - '||v_error_message;       
         RAISE e_report_fail;
      ELSE                             --01/25/2013 added else to update rpt_report_date table only for successful refresh
         prc_execution_log_entry('PRC_RPT_USER_CHAR_REFRESH', 0, 'SUCCESS', NULL, NULL);
      END IF;

      v_stage := 'Refresh CMS user characteristic MView';
      dbms_mview.refresh('mv_cms_user_characteristic');
                          
   EXCEPTION
      WHEN OTHERS THEN
         prc_execution_log_entry('PRC_RPT_REFRESH',1,'ERROR',v_stage,NULL);
         v_stage := SUBSTR('Error '||TO_CHAR(SQLCODE)||': '||SQLERRM, 1, 255) ;
         prc_execution_log_entry('PRC_RPT_REFRESH',1,'ERROR',v_stage,NULL);
         RAISE e_report_fail;   
   END;
    
    BEGIN
      v_stage := 'pkg_report_enrollment.prc_enrollment_detail' ;
         pkg_report_enrollment.prc_enrollment_detail(pi_requested_user_id,
                                                     v_start_date,
                                                     v_end_date,
                                                     po_return_code,
                                                     po_error_message);    
                
      IF po_return_code != 0 THEN
        po_error_message := 'pkg_report_enrollment.prc_enrollment_detail - '||po_error_message;       
        RAISE e_report_fail;
      END IF;   
      
       prc_execution_log_entry('prc_report_refresh.sql',1,'INFO',v_stage||'Completed'
                          ,NULL);
      
      v_stage := 'pkg_report_enrollment.prc_enrollment_summary' ;
         pkg_report_enrollment.prc_enrollment_summary(pi_requested_user_id,
                                                      po_return_code  ,
                                                      po_error_message)  ;
      
         
      IF po_return_code != 0 THEN
        po_error_message := 'pkg_report_enrollment.prc_enrollment_summary - '||po_error_message;       
        RAISE e_report_fail;
      END IF;      
      prc_execution_log_entry('prc_report_refresh.sql',1,'INFO',v_stage||'Completed'
                          ,NULL);
    EXCEPTION
    WHEN OTHERS THEN
      prc_execution_log_entry('PRC_RPT_REFRESH',1,'ERROR',v_stage
                          ,NULL);
      v_stage := SUBSTR('Error '||TO_CHAR(SQLCODE)||': '||SQLERRM, 1, 255) ;
      prc_execution_log_entry('PRC_RPT_REFRESH',1,'ERROR',v_stage
                          ,NULL);

    RAISE;
    END;
     set_rpt_refresh_date('enrollment', v_end_date, v_start_date);    
    
    
    BEGIN
    
      v_stage := 'pkg_report_budget.p_rpt_budget_promotion' ;
      pkg_report_budget.p_rpt_budget_promotion
      (pi_requested_user_id,
       v_start_date       , 
       v_end_date         , 
       po_return_code      ,
       po_error_message);

      IF po_return_code != 0 THEN 
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;
     prc_execution_log_entry('PRC_RPT_REFRESH',1,'INFO',v_stage||'Completed'
                          ,NULL);
      v_stage := 'pkg_report_budget.p_rpt_budget_trans_detail' ;
      pkg_report_budget.p_rpt_budget_trans_detail
      (pi_requested_user_id,
       v_start_date       , 
       v_end_date         , 
       po_return_code      ,
       po_error_message);

      IF po_return_code != 0 THEN 
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;
      prc_execution_log_entry('PRC_RPT_REFRESH',1,'INFO',v_stage||'Completed'
                          ,NULL);
      v_stage := 'pkg_report_budget.p_rpt_budget_usage_detail' ;
      pkg_report_budget.p_rpt_budget_usage_detail
      (pi_requested_user_id,
       v_start_date       , 
       v_end_date         , 
       po_return_code      ,
       po_error_message);

      IF po_return_code != 0 THEN 
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;
       prc_execution_log_entry('PRC_RPT_REFRESH',1,'INFO',v_stage||'Completed'
                          ,NULL);

--      v_stage := 'pkg_report_budget.p_rpt_budget_usage_summary' ; --Commented out as we no longer use the summary table. --04/25/2014
--      pkg_report_budget.p_rpt_budget_usage_summary
--      (pi_requested_user_id,
--       v_start_date       , 
--       v_end_date         , 
--       po_return_code      ,
--       po_error_message);
--        prc_execution_log_entry('PRC_RPT_REFRESH',1,'INFO',v_stage||'Completed'
--                          ,null);
--
--      IF po_return_code != 0 THEN 
--        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
--        RAISE e_report_fail;
--      END IF;
--    
    EXCEPTION
    WHEN OTHERS THEN
      prc_execution_log_entry('prc_report_refresh.sql',1,'ERROR',v_stage
                          ,NULL);
      v_stage := SUBSTR('Error '||TO_CHAR(SQLCODE)||': '||SQLERRM, 1, 255) ;
      prc_execution_log_entry('prc_report_refresh.sql',1,'ERROR',v_stage
                          ,NULL);

    RAISE;
    END;    
         set_rpt_refresh_date('budget', v_end_date, v_start_date); 
    BEGIN
      v_stage := 'pkg_report_awardmedia.p_rpt_awardmedia_detail' ;
      IF v_sa_enabled = 1 THEN                                          --04/01/2019
          pkg_report_awardmedia.p_rpt_awardmedia_detail_sa
          (pi_requested_user_id,
           v_start_date       , 
           v_end_date         , 
           po_return_code      ,
           po_error_message);
      ELSE
          pkg_report_awardmedia.p_rpt_awardmedia_detail
          (pi_requested_user_id,
           v_start_date       , 
           v_end_date         , 
           po_return_code      ,
           po_error_message);
      END IF;  

      IF po_return_code != 0 THEN 
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;
      prc_execution_log_entry('prc_report_refresh.sql',1,'INFO',v_stage||'Completed'
                          ,NULL);
      v_stage := 'pkg_report_awardmedia.p_rpt_awardmedia_summary' ;
      pkg_report_awardmedia.p_rpt_awardmedia_summary
      (pi_requested_user_id,
       v_start_date       , 
       v_end_date         , 
       po_return_code      ,
       po_error_message);

      IF po_return_code != 0 THEN 
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;
      
       prc_execution_log_entry('prc_report_refresh.sql',1,'INFO',v_stage||'Completed'
                          ,NULL);

    EXCEPTION
    WHEN OTHERS THEN
      prc_execution_log_entry('PRC_RPT_REFRESH',1,'ERROR',v_stage
                          ,NULL);
      v_stage := SUBSTR('Error '||TO_CHAR(SQLCODE)||': '||SQLERRM, 1, 255) ;
      prc_execution_log_entry('PRC_RPT_REFRESH',1,'ERROR',v_stage
                          ,NULL);

    RAISE;
    END;
    
      set_rpt_refresh_date('awards', v_end_date, v_start_date);
      set_rpt_refresh_date('activity', v_end_date, v_start_date);
    
       BEGIN
      v_stage := 'pkg_report_badge.p_rpt_badge_detail' ;
      pkg_report_badge.p_rpt_badge_detail
      (pi_requested_user_id,
       v_start_date       , 
       v_end_date         , 
       po_return_code      ,
       po_error_message);

      IF po_return_code != 0 THEN 
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;
      prc_execution_log_entry('PRC_RPT_REFRESH',1,'INFO',v_stage||'Completed'
                          ,NULL);
      v_stage := 'pkg_report_badge.p_rpt_badge_summary' ;
      pkg_report_badge.p_rpt_badge_summary
      (pi_requested_user_id,
       v_start_date       , 
       v_end_date         , 
       po_return_code      ,
       po_error_message);

      IF po_return_code != 0 THEN 
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;
      
       prc_execution_log_entry('PRC_RPT_REFRESH',1,'INFO',v_stage||'Completed'
                          ,NULL);

    EXCEPTION
    WHEN OTHERS THEN
      prc_execution_log_entry('PRC_RPT_REFRESH',1,'ERROR',v_stage
                          ,NULL);
      v_stage := SUBSTR('Error '||TO_CHAR(SQLCODE)||': '||SQLERRM, 1, 255) ;
      prc_execution_log_entry('PRC_RPT_REFRESH',1,'ERROR',v_stage
                          ,NULL);

    RAISE;
    END;
          set_rpt_refresh_date('badge', v_end_date, v_start_date);

  
  
    v_stage := 'Call prc_rpt_qcard_refresh';  
  prc_rpt_qcard_refresh (pi_requested_user_id, v_start_date, v_end_date, v_return_code, v_error_message);  
  IF v_return_code != 0 THEN 
    po_return_code := v_return_code; 
    po_error_message := 'prc_rpt_qcard_refresh - '||v_error_message;       
    RAISE e_report_fail;
  ELSE                             --01/25/2013 added else to update rpt_report_date table only for successful refresh
  --  set_rpt_refresh_date('Qcard', v_end_date, v_start_date);
    prc_execution_log_entry('PRC_RPT_QCARD_REFRESH', 0, 'SUCCESS', NULL, NULL);
  END IF;
     
  set_rpt_refresh_date('qcard', v_end_date, v_start_date);
  
  BEGIN
   v_stage := 'Call Prc_report_promo_node_activity';   --12/20/2012 added  
  prc_report_promo_node_activity (pi_requested_user_id,v_start_date,v_end_date,v_return_code,v_error_message);

  IF v_return_code != 0 THEN 
    po_return_code := v_return_code; 
    po_error_message := 'prc_report_promo_node_activity - '||v_error_message;       
    RAISE e_report_fail;
  ELSE                       --01/25/2013 added else to update rpt_report_date table only for successful refresh
    set_rpt_refresh_date('promonodeactivity', v_end_date, v_start_date);
    prc_execution_log_entry('prc_report_promo_node_activity', 0, 'SUCCESS', NULL, NULL);
  END IF; 
  END;
  
  
  BEGIN --05/18/2016 start
      v_stage := 'pkg_report_behavior.prc_behavior_detail' ;
      pkg_report_behavior.prc_behavior_detail
                       (pi_requested_user_id,
                        v_start_date, 
                        v_end_date, 
                        po_return_code,
                        po_error_message);

      IF po_return_code != 0 THEN
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;

      v_stage := 'pkg_report_behavior.prc_behavior_summary' ;
      pkg_report_behavior.prc_behavior_summary
                       (pi_requested_user_id,
                        po_return_code,
                        po_error_message);

      IF po_return_code != 0 THEN
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;

    EXCEPTION
    WHEN OTHERS THEN
      prc_execution_log_entry('PRC_PC_RPT_REFRESH',1,'ERROR',v_stage
                          ,NULL);
      v_stage := SUBSTR('Error '||TO_CHAR(SQLCODE)||': '||SQLERRM, 1, 255) ;
      prc_execution_log_entry('PRC_PC_RPT_REFRESH',1,'ERROR',v_stage
                          ,NULL);

           RAISE e_report_fail;
    END;
    
    set_rpt_refresh_date('behavior', v_end_date, v_start_date); --05/18/2016
   
  
   IF  FNC_CHECK_FOR_MODULE('productclaims') =1 THEN
    BEGIN
      v_stage := 'pkg_report_claim.prc_claim_detail' ;
      pkg_report_claim.prc_claim_detail
                       (pi_requested_user_id,
                        v_start_date, 
                        v_end_date, 
                        po_return_code,
                        po_error_message);

      IF po_return_code != 0 THEN
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;

      v_stage := 'pkg_report_claim.prc_claim_summary' ;
      pkg_report_claim.prc_claim_summary
                       (pi_requested_user_id,
                        v_start_date, 
                        v_end_date, 
                        po_return_code,
                        po_error_message);

      IF po_return_code != 0 THEN
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;

      v_stage := 'pkg_report_product_claim.prc_claim_detail_for_prd_claim' ;
      pkg_report_product_claim.prc_claim_detail_for_prd_claim
                               (pi_requested_user_id,
                                v_start_date, 
                                v_end_date, 
                                po_return_code,
                                po_error_message);

      IF po_return_code != 0 THEN
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;

      v_stage := 'pkg_report_product_claim.p_claim_product_summary' ;
      pkg_report_product_claim.p_claim_product_summary;

    EXCEPTION
    WHEN OTHERS THEN
      prc_execution_log_entry('PRC_PC_RPT_REFRESH',1,'ERROR',v_stage
                          ,NULL);
      v_stage := SUBSTR('Error '||TO_CHAR(SQLCODE)||': '||SQLERRM, 1, 255) ;
      prc_execution_log_entry('PRC_PC_RPT_REFRESH',1,'ERROR',v_stage
                          ,NULL);

           RAISE e_report_fail;
    END;
    
      set_rpt_refresh_date('claim', v_end_date, v_start_date);
   ELSE
   NULL;
   END IF;
      
        IF  FNC_CHECK_FOR_MODULE('recognition') =1 THEN
  BEGIN
    v_stage := 'pkg_report_recognition.p_rpt_recognition_detail' ;
    IF v_sa_enabled = 1 THEN                                          --04/01/2019
        pkg_report_recognition.p_rpt_recognition_detail_sa(pi_requested_user_id,
                                                        v_start_date       ,
                                                        v_end_date         ,
                                                        po_return_code     ,
                                                        po_error_message   );
    ELSE
        pkg_report_recognition.p_rpt_recognition_detail(pi_requested_user_id,
                                                        v_start_date       ,
                                                        v_end_date         ,
                                                        po_return_code     ,
                                                        po_error_message   );
    END IF;

    IF po_return_code != 0 THEN
      po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
      RAISE e_report_fail;
    END IF;


    /*v_stage := 'pkg_report_recognition.p_rpt_recognition_summary' ; --04/21/2017
    pkg_report_recognition.p_rpt_recognition_summary(pi_requested_user_id,
                                                     po_return_code,
                                                     po_error_message);

    IF po_return_code != 0 THEN
      po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
      RAISE e_report_fail;
    END IF;*/


    --v_stage := 'pkg_report_recognition.prc_recog_behavior_summary' ;
    --pkg_report_recog_behavior.prc_recog_behavior_summary(po_return_code);
    --IF po_return_code != 0 THEN
    --  po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
    --  RAISE e_report_fail;
    --END IF;
    
    set_rpt_refresh_date('recognition', v_end_date, v_start_date);
    
    v_stage := 'pkg_report_recognition.P_PROGRAM_REFERENCE_NBR' ;
    pkg_report_recognition.P_PROGRAM_REFERENCE_NBR(pi_requested_user_id,
                                                    v_start_date       ,
                                                    v_end_date         ,
                                                    po_return_code     ,
                                                    po_error_message   ); 

    IF po_return_code != 0 THEN
      po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
      RAISE e_report_fail;
    END IF;
    
--    select boolean_val
--        into v_boolean_val
--    from os_propertyset 
--    WHERE entity_name = 'spotlight.standalone.enable';
--    
--    IF(v_boolean_val = 0) THEN
--        v_stage := 'pkg_report_recognition.P_AWARD_EARNINGS_REPORT';
--        pkg_report_recognition.P_AWARD_EARNINGS_REPORT; 
--    END IF; 

    v_stage := 'pkg_report_recognition.p_rpt_purl_contribution_detail';
    pkg_report_recognition.p_rpt_purl_contribution_detail(pi_requested_user_id,
                                                    v_start_date       ,
                                                    v_end_date         ,
                                                    po_return_code     ,
                                                    po_error_message   );

    IF po_return_code != 0 THEN
      po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
      RAISE e_report_fail;
    END IF;

    /*v_stage := 'pkg_report_recognition.p_rpt_purl_contribution_sum'; --04/21/2017
    pkg_report_recognition.p_rpt_purl_contribution_sum(pi_requested_user_id,
                                                    v_start_date       ,
                                                    v_end_date         ,
                                                    po_return_code     ,
                                                    po_error_message   );

    IF po_return_code != 0 THEN
      po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
      RAISE e_report_fail;
    END IF;*/
    
    END;
    
     set_rpt_refresh_date('purlcontributions', v_end_date, v_start_date);
     
     ELSE
     NULL;
     END IF;
     
   IF  FNC_CHECK_FOR_MODULE('nominations') =1 THEN
     
      BEGIN
         v_stage := 'p_rpt_nomination_detail';
         pkg_report_nomination.p_rpt_nomination_detail
         ( pi_requested_user_id,
           v_start_date, 
           v_end_date, 
           po_return_code,
           po_error_message
         );

         IF po_return_code != 0 THEN
            po_error_message := 'Call to p_rpt_nomination_detail unsuccessful. '||po_error_message;
            RAISE e_report_fail;
         END IF; 
    
--         v_stage := 'prc_nom_behavior_summary' ;
--         pkg_report_nom_behavior.prc_nom_behavior_summary
--         ( pi_requested_user_id,
--           v_start_date, 
--           v_end_date, 
--           po_return_code,
--           po_error_message
--         );
--
--         IF po_return_code != 0 THEN
--            po_error_message := 'Call to prc_nom_behavior_summary unsuccessful. '||po_error_message;
--            RAISE e_report_fail;
--         END IF; 

         set_rpt_refresh_date('nomination', v_end_date, v_start_date);
      END;
  
   ELSE
      NULL;
   END IF;
   
  IF  FNC_CHECK_FOR_MODULE('quizzes') =1 THEN
  
   BEGIN
      v_stage := 'prc_rpt_quiz_activity_details' ;
      pkg_quiz_reports.prc_rpt_quiz_activity_details(pi_requested_user_id,
                                                     v_start_date,
                                                     v_end_date,
                                                     po_return_code,
                                                     po_error_message);
                                                           
      IF po_return_code != 0 THEN
        po_error_message := 'prc_rpt_quiz_activity_details - '||po_error_message;       
        RAISE e_report_fail;
      END IF;
  
      v_stage := 'prc_rpt_quiz_activity_summary' ;
      pkg_quiz_reports.prc_rpt_quiz_activity_summary(pi_requested_user_id,                                               
                                                     po_return_code,
                                                     po_error_message);
      
      IF po_return_code != 0 THEN
        po_error_message := 'prc_rpt_quiz_activity_summary - '||po_error_message;       
        RAISE e_report_fail;
      END IF;
      
      v_stage := 'prc_quiz_analysis_reports';      
      pkg_quiz_analysis_reports.prc_quiz_analysis_reports(pi_requested_user_id,
                                                          v_start_date,
                                                          v_end_date,
                                                          po_return_code,
                                                          po_error_message); 
      
      IF po_return_code != 0 THEN
        po_error_message := 'prc_quiz_analysis_reports - '||po_error_message;       
        RAISE e_report_fail;
      END IF;
     
   
   END;
    set_rpt_refresh_date('quiz', v_end_date, v_start_date);
    
    ELSE
    NULL;
    
    END IF;
        
  IF  FNC_CHECK_FOR_MODULE('goalquest') =1 THEN
  
   BEGIN
      v_stage := 'p_rpt_goalquest_detail' ;
        pkg_report_goalquest.p_rpt_goalquest_detail(pi_requested_user_id,
                                                  v_start_date,
                                                  v_end_date,
                                                  po_return_code,
                                                  po_error_message); 
                                                           
      IF po_return_code != 0 THEN
        po_error_message := 'p_rpt_goalquest_detail - '||po_error_message;       
        RAISE e_report_fail;
      END IF;
  
      v_stage := 'p_rpt_goalquest_summary' ;
      pkg_report_goalquest.p_rpt_goalquest_summary(pi_requested_user_id,
                                                 po_return_code,
                                                 po_error_message);
      
      IF po_return_code != 0 THEN
        po_error_message := 'p_rpt_goalquest_summary - '||po_error_message;       
        RAISE e_report_fail;
      END IF;      
      
        v_stage := 'p_rpt_goal_partner' ;
      pkg_report_goalquest.p_rpt_goal_partner(pi_requested_user_id,
                                                 po_return_code,
                                                 po_error_message);
      
      IF po_return_code != 0 THEN
        po_error_message := 'p_rpt_goal_partner - '||po_error_message;       
        RAISE e_report_fail;
      END IF;      
     
   v_stage := 'p_rpt_goal_manager_override' ;
        pkg_report_goalquest.p_rpt_goal_manager_override(pi_requested_user_id,                                                  
                                                  po_return_code,
                                                  po_error_message); 
                                                           
      IF po_return_code != 0 THEN
        po_error_message := 'p_rpt_goal_manager_override - '||po_error_message;       
        RAISE e_report_fail;
      END IF;
   
   END;
    set_rpt_refresh_date('goalquest', v_end_date, v_start_date);
    
    ELSE
    NULL;
    
    END IF;
            
 IF  FNC_CHECK_FOR_MODULE('challengepoint') =1 THEN
  
   BEGIN
      v_stage := 'p_rpt_cp_selection_detail' ;
        pkg_report_challengepoint.p_rpt_cp_selection_detail(pi_requested_user_id,
                                                  v_start_date,
                                                  v_end_date,
                                                  po_return_code,
                                                  po_error_message); 
                                                           
      IF po_return_code != 0 THEN
        po_error_message := 'p_rpt_cp_selection_detail - '||po_error_message;       
        RAISE e_report_fail;
      END IF;
  
      v_stage := 'p_rpt_cp_selection_summary' ;
      pkg_report_challengepoint.p_rpt_cp_selection_summary(pi_requested_user_id,
                                                  v_start_date,
                                                  v_end_date,
                                                  po_return_code,
                                                  po_error_message); 
      
      IF po_return_code != 0 THEN
        po_error_message := 'p_rpt_cp_selection_summary - '||po_error_message;       
        RAISE e_report_fail;
      END IF;       
      
      v_stage := 'p_rpt_cp_manager_override' ;
        pkg_report_challengepoint.p_rpt_cp_manager_override(pi_requested_user_id,                                                  
                                                  po_return_code,
                                                  po_error_message); 
                                                           
      IF po_return_code != 0 THEN
        po_error_message := 'p_rpt_cp_manager_override - '||po_error_message;       
        RAISE e_report_fail;
      END IF;
   
   END;
    set_rpt_refresh_date('challengepoint', v_end_date, v_start_date);
    
    ELSE
    NULL;
    
    END IF;
--Added for SSI CR - Individual Activity Report    
  IF  FNC_CHECK_FOR_MODULE('ssi') =1 THEN
       set_rpt_refresh_date('ssi', v_end_date, v_start_date); --05/28/2015 Bug 62471
       BEGIN  
          v_stage := 'prc_report_ssi_contest' ;
           prc_report_ssi_contest(pi_requested_user_id,v_start_date,v_end_date,po_return_code,po_error_message);
          IF po_return_code != 0 THEN
            po_error_message := 'prc_report_ssi_contest - '||po_error_message;       
            RAISE e_report_fail;
          END IF;  
       END;

       BEGIN   --05/12/2015   Suresh J
          v_stage := 'prc_rpt_ssi_award_detail' ;
           prc_rpt_ssi_award_detail(pi_requested_user_id,v_start_date,v_end_date,po_return_code,po_error_message);
          IF po_return_code != 0 THEN
            po_error_message := 'prc_rpt_ssi_award_detail - '||po_error_message;       
            RAISE e_report_fail;
          END IF;  
       END;
   --set_rpt_refresh_date('ssi', v_end_date, v_start_date); --12/21/2015 bug 65020

  ELSE
    NULL;
    
  END IF;
  
BEGIN   --05/06/2019 starts
SELECT boolean_val INTO v_aws_enabled FROM os_propertyset WHERE entity_name='aws.deployed';
EXCEPTION WHEN OTHERS THEN
v_aws_enabled:=NULL;
END;

IF v_aws_enabled=0 THEN  --05/06/2019 Checking Aws enabled or not 
   BEGIN
   SELECT COUNT(1) INTO
   is_plateau_required
   FROM promo_merch_country;
   END;

   IF is_plateau_required>=1 THEN
   BEGIN   
      set_rpt_refresh_date('award', v_end_date, v_start_date); --06/03/2015 Bug 62848
      v_stage := 'p_award_earnings_report' ;
        pkg_client_reports.p_award_earnings_report; 
        
        v_stage := 'p_award_order_detail_report' ;
        
       pkg_client_reports.p_award_order_detail_report; 
       
       
        
       v_stage := 'p_award_item_selection' ;
      IF v_sa_enabled = 1 THEN                                          --04/01/2019
       pkg_client_reports.p_award_item_selection_sa( pi_requested_user_id,
                                                      v_start_date,
                                                      v_end_date,
                                                      po_return_code,
                                                      po_error_message); 
      ELSE
       pkg_client_reports.p_award_item_selection(pi_requested_user_id,
                                                  v_start_date,
                                                  v_end_date,
                                                  po_return_code,
                                                  po_error_message); 
      END IF;  
                                                           
      IF po_return_code != 0 THEN
        po_error_message := 'p_award_item_selection - '||po_error_message;       
        RAISE e_report_fail;
      END IF;
  
      v_stage := 'p_award_item_activity' ;
      IF v_sa_enabled = 1 THEN                                          --04/01/2019
      pkg_client_reports.p_award_item_activity_sa(pi_requested_user_id,
                                                  v_start_date,
                                                  v_end_date,
                                                  po_return_code,
                                                  po_error_message); 
      ELSE
      pkg_client_reports.p_award_item_activity(pi_requested_user_id,
                                                  v_start_date,
                                                  v_end_date,
                                                  po_return_code,
                                                  po_error_message); 
      END IF;  
                                                  
      
      IF po_return_code != 0 THEN
        po_error_message := 'p_award_item_activity - '||po_error_message;       
        RAISE e_report_fail;
      END IF;       
   
   END;
    --set_rpt_refresh_date('award', v_end_date, v_start_date); --12/21/2015 bug 65020
    
  END IF;
  END IF;--05/06/2019
  IF  fnc_check_for_module('throwdown') =1 THEN
  
   BEGIN
      v_stage := 'p_rpt_throwdown_activity' ;
        pkg_report_throwdown.p_rpt_throwdown_activity(pi_requested_user_id,
                                                    po_return_code,
                                                    po_error_message);
      IF po_return_code != 0 THEN
        po_error_message := 'p_rpt_throwdown_activity - '||po_error_message;       
        RAISE e_report_fail;
      END IF;  
   
   END;
    set_rpt_refresh_date('throwdown', v_end_date, v_start_date);
    
    ELSE
    NULL;
    
    END IF;
  
   
  IF  fnc_check_for_module('survey') =1 THEN
  
   BEGIN
   
       set_rpt_refresh_date('survey', v_end_date, v_start_date);   --10/29/2015  Bug# 64471
   
      v_stage := 'prc_rpt_survey_detail' ;
        pkg_report_survey.prc_rpt_survey_detail (pi_requested_user_id,                                                 
                                                  po_return_code,
                                                  po_error_message); 
      IF po_return_code != 0 THEN
        po_error_message := 'prc_rpt_survey_detail - '||po_error_message;       
        RAISE e_report_fail;
      END IF;  
      
      v_stage := 'prc_rpt_survey_summary' ;
      pkg_report_survey.prc_rpt_survey_summary(pi_requested_user_id,                                                 
                                                  po_return_code,
                                                  po_error_message); 
      
      IF po_return_code != 0 THEN
        po_error_message := 'prc_rpt_survey_summary - '||po_error_message;       
        RAISE e_report_fail;
      END IF;       
   
      v_stage := 'prc_rpt_survey_response_detail' ;
        pkg_report_survey.prc_rpt_survey_response_detail (pi_requested_user_id,
                                                  v_start_date,
                                                  v_end_date,                                          
                                                  po_return_code,
                                                  po_error_message); 
      IF po_return_code != 0 THEN
        po_error_message := 'prc_rpt_survey_response_detail - '||po_error_message;       
        RAISE e_report_fail;
      END IF;  
      
      v_stage := 'prc_rpt_survey_response_sum' ;
      pkg_report_survey.prc_rpt_survey_response_sum(pi_requested_user_id,                                                                                             
                                                  po_return_code,
                                                  po_error_message); 
      
      IF po_return_code != 0 THEN
        po_error_message := 'prc_rpt_survey_response_sum - '||po_error_message;       
        RAISE e_report_fail;
      END IF;    
      
      v_stage := 'prc_rpt_survey_openend_resp' ;
      
      pkg_report_survey.prc_rpt_survey_openend_resp (pi_requested_user_id,
                                                  v_start_date,
                                                  v_end_date,                                          
                                                  po_return_code,
                                                  po_error_message); 
                                                  
        IF po_return_code != 0 THEN
        po_error_message := 'prc_rpt_survey_openend_resp - '||po_error_message;       
        RAISE e_report_fail;
      END IF;    
      
      v_stage := 'prc_rpt_survey_minimum_resp' ;
      pkg_report_survey.prc_rpt_survey_minimum_resp(pi_requested_user_id,                                                                                             
                                                  po_return_code,
                                                  po_error_message); 
      
      IF po_return_code != 0 THEN
        po_error_message := 'prc_rpt_survey_minimum_resp - '||po_error_message;       
        RAISE e_report_fail;
      END IF; 

   END;
--    set_rpt_refresh_date('survey', v_end_date, v_start_date);   --10/29/2015  Bug# 64471
    
    ELSE
    NULL;
    
    END IF; 
    
  BEGIN                     --12/15/2015 Start
   SELECT boolean_val 
     INTO v_boolean 
     FROM os_propertyset 
    WHERE entity_name='work.happier';
  EXCEPTION 
    WHEN OTHERS THEN
      v_boolean := 0;
  END;
    
  IF  v_boolean = 1 THEN  
  
    BEGIN
   
      set_rpt_refresh_date('workhappier', v_end_date, v_start_date); 
   
      v_stage := 'prc_rpt_survey_detail' ;
      pkg_report_work_happier.prc_work_happier_detail (pi_requested_user_id,   
                                                  v_start_date,
                                                  v_end_date,                                           
                                                  po_return_code,
                                                  po_error_message); 
      IF po_return_code != 0 THEN
        po_error_message := 'prc_work_happier_detail - '||po_error_message;       
        RAISE e_report_fail;
      END IF;  
      
    END;
    
  END IF;           --12/15/2015 End
  
  BEGIN             --10/06/2016 Start
    
      set_rpt_refresh_date('cashbudget', v_end_date, v_start_date); 
      
      v_stage := 'pkg_report_cash_budget.p_rpt_cash_budget_promotion' ;
      pkg_report_cash_budget.p_rpt_cash_budget_promotion
      (pi_requested_user_id,
       v_start_date       , 
       v_end_date         , 
       po_return_code      ,
       po_error_message);

      IF po_return_code != 0 THEN 
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;
     prc_execution_log_entry('PRC_RPT_REFRESH',1,'INFO',v_stage||'Completed'
                          ,NULL);
      v_stage := 'pkg_report_cash_budget.p_rpt_cash_budget_trans_detail' ;
      pkg_report_cash_budget.p_rpt_cash_budget_trans_detail
      (pi_requested_user_id,
       v_start_date       , 
       v_end_date         , 
       po_return_code      ,
       po_error_message);

      IF po_return_code != 0 THEN 
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;
      prc_execution_log_entry('PRC_RPT_REFRESH',1,'INFO',v_stage||'Completed'
                          ,NULL);
      v_stage := 'pkg_report_cash_budget.p_rpt_cash_budget_usage_detail' ;
      pkg_report_cash_budget.p_rpt_cash_budget_usage_detail
      (pi_requested_user_id,
       v_start_date       , 
       v_end_date         , 
       po_return_code      ,
       po_error_message);

      IF po_return_code != 0 THEN 
        po_error_message := v_process||' - stage: '||v_stage||'.'||po_error_message;       
        RAISE e_report_fail;
      END IF;
       prc_execution_log_entry('PRC_RPT_REFRESH',1,'INFO',v_stage||'Completed'
                          ,NULL);
    
  EXCEPTION
    WHEN OTHERS THEN
      prc_execution_log_entry('prc_report_refresh.sql',1,'ERROR',v_stage
                          ,NULL);
      v_stage := SUBSTR('Error '||TO_CHAR(SQLCODE)||': '||SQLERRM, 1, 255) ;
      prc_execution_log_entry('prc_report_refresh.sql',1,'ERROR',v_stage
                          ,NULL);

    RAISE;
  END;        --10/06/2016 End  

    
    prc_clear_lob_space (po_return_code); --05/08/2015
     IF po_return_code != 0 THEN
        po_error_message := 'prc_clear_lob_space - '||po_error_message;       
        RAISE e_report_fail;
      END IF;       
    
    COMMIT;
  po_return_code := 00; -- Success
  po_error_message := 'Report refresh is Successful';
  
  prc_execution_log_entry( v_process, 0, 'INFO','Report Refresh Process is completed Successfully',NULL);    

EXCEPTION
  WHEN e_report_fail THEN
    prc_execution_log_entry( v_process, 0, 'ERROR','Error at Stage : '||v_stage||' - '||po_error_message, NULL);    
    ROLLBACK;
    
    UPDATE rpt_refresh_date -- 09/01/2017
    SET    refresh_date = v_hold_date,
           refresh_prev_date = v_hold_prev_date
    WHERE  NVL(refresh_date,SYSDATE + 1) <> v_hold_date
    OR     NVL(refresh_prev_date,SYSDATE + 1) <> v_hold_prev_date;
    COMMIT;
    
/*	-- 09/01/2017
    --02/05/2015 To stop refresh_date is getting saved with failure - Eligibility Module
    IF v_bkup_elig_start_date IS NOT NULL AND v_bkup_elig_end_date IS NOT NULL
    THEN
        SELECT COUNT(*)    --02/05/2015 
        INTO v_cnt_prev_process_rec
        FROM rpt_refresh_date
        WHERE UPPER(report_category_type) = UPPER('eligiblility')  AND
              refresh_prev_date =  v_bkup_elig_start_date AND
              refresh_date = v_bkup_elig_end_date  ;

        IF v_cnt_prev_process_rec = 0 THEN
            UPDATE rpt_refresh_date
               SET refresh_prev_date = v_bkup_elig_start_date,
                   refresh_date = v_bkup_elig_end_date
             WHERE     UPPER (report_category_type) = UPPER ('eligiblility') AND 
                       v_bkup_elig_boolean = 1;
             prc_execution_log_entry( v_process, 0, 'INFO','Rolling Back rpt_refresh_date for ELIGIBLILITY..'||SQL%ROWCOUNT||' row(s) updated',NULL);
             COMMIT;          
        END IF;
    ELSIF v_bkup_elig_start_date IS NULL AND v_bkup_elig_end_date IS NULL  
    THEN
        DELETE 
        FROM rpt_refresh_date 
        WHERE UPPER (report_category_type) = UPPER ('eligiblility') AND
              v_bkup_elig_boolean = 1;
        prc_execution_log_entry( v_process, 0, 'INFO','Rolling Back rpt_refresh_date for eligiblility..'||SQL%ROWCOUNT||' row(s) deleted',NULL);
        COMMIT;
    END IF;    
    v_cnt_prev_process_rec := NULL;
    --02/05/2015 To stop refresh_date is getting saved with failure - Core Module
    IF v_bkup_core_start_date IS NOT NULL AND v_bkup_core_end_date IS NOT NULL
    THEN
        SELECT COUNT(*)    --02/05/2015 
        INTO v_cnt_prev_process_rec
        FROM rpt_refresh_date
        WHERE UPPER(report_category_type) = UPPER('Core')  AND
              refresh_prev_date =  v_bkup_core_start_date AND
              refresh_date = v_bkup_core_end_date  ;

        IF v_cnt_prev_process_rec = 0 THEN
            UPDATE rpt_refresh_date
               SET refresh_prev_date = v_bkup_core_start_date,
                   refresh_date = v_bkup_core_end_date
             WHERE     UPPER (report_category_type) = UPPER ('Core') AND
                       v_bkup_core_boolean = 1;
             prc_execution_log_entry( v_process, 0, 'INFO','Rolling Back rpt_refresh_date for Core..'||SQL%ROWCOUNT||' row(s) updated',NULL);
             COMMIT;          
        END IF;
    ELSIF v_bkup_core_start_date IS NULL AND v_bkup_core_end_date IS NULL  
    THEN
        DELETE 
        FROM rpt_refresh_date 
        WHERE UPPER (report_category_type) = UPPER ('Core') AND 
                    v_bkup_core_boolean = 1;
        prc_execution_log_entry( v_process, 0, 'INFO','Rolling Back rpt_refresh_date for Core..'||SQL%ROWCOUNT||' row(s) deleted',NULL);
        COMMIT;
    END IF;    
*/	-- 09/01/2017

    po_error_message := 'Report refresh failed at Stage : '||v_stage||' - '||po_error_message;

  WHEN OTHERS THEN
    prc_execution_log_entry( v_process, 0, 'ERROR', SQLERRM, NULL);
    po_return_code := 99; -- Error
    po_error_message := SQLERRM;
    ROLLBACK;
    
    UPDATE rpt_refresh_date -- 09/01/2017
    SET    refresh_date = v_hold_date,
           refresh_prev_date = v_hold_prev_date
    WHERE  NVL(refresh_date,SYSDATE + 1) <> v_hold_date
    OR     NVL(refresh_prev_date,SYSDATE + 1) <> v_hold_prev_date;
    COMMIT;

/*	-- 09/01/2017
    --02/05/2015 To stop refresh_date is getting saved with failure - Eligibility Module
    IF v_bkup_elig_start_date IS NOT NULL AND v_bkup_elig_end_date IS NOT NULL
    THEN
        SELECT COUNT(*)    --02/05/2015 
        INTO v_cnt_prev_process_rec
        FROM rpt_refresh_date
        WHERE UPPER(report_category_type) = UPPER('eligiblility')  AND
              refresh_prev_date =  v_bkup_elig_start_date AND
              refresh_date = v_bkup_elig_end_date  ;

        IF v_cnt_prev_process_rec = 0 THEN
            UPDATE rpt_refresh_date
               SET refresh_prev_date = v_bkup_elig_start_date,
                   refresh_date = v_bkup_elig_end_date
             WHERE     UPPER (report_category_type) = UPPER ('eligiblility') AND
                       v_bkup_elig_boolean = 1;
                       
             prc_execution_log_entry( v_process, 0, 'INFO','Rolling Back rpt_refresh_date for ELIGIBLILITY..'||SQL%ROWCOUNT||' row(s) updated',NULL);
             COMMIT;          
        END IF;
    ELSIF v_bkup_elig_start_date IS NULL AND v_bkup_elig_end_date IS NULL  
    THEN
        DELETE 
        FROM rpt_refresh_date 
        WHERE UPPER (report_category_type) = UPPER ('eligiblility')  AND
              v_bkup_elig_boolean = 1;
              
        prc_execution_log_entry( v_process, 0, 'INFO','Rolling Back rpt_refresh_date for eligiblility..'||SQL%ROWCOUNT||' row(s) deleted',NULL);
        COMMIT;
    END IF;    
    v_cnt_prev_process_rec := NULL;
    --02/05/2015 To stop refresh_date is getting saved with failure - Core Module
    IF v_bkup_core_start_date IS NOT NULL AND v_bkup_core_end_date IS NOT NULL
    THEN
        SELECT COUNT(*)    --02/05/2015 
        INTO v_cnt_prev_process_rec
        FROM rpt_refresh_date
        WHERE UPPER(report_category_type) = UPPER('Core')  AND
              refresh_prev_date =  v_bkup_core_start_date AND
              refresh_date = v_bkup_core_end_date  ;

        IF v_cnt_prev_process_rec = 0 THEN
            UPDATE rpt_refresh_date
               SET refresh_prev_date = v_bkup_core_start_date,
                   refresh_date = v_bkup_core_end_date
             WHERE     UPPER (report_category_type) = UPPER ('Core') AND
                       v_bkup_core_boolean = 1;
                       
             prc_execution_log_entry( v_process, 0, 'INFO','Rolling Back rpt_refresh_date for Core..'||SQL%ROWCOUNT||' row(s) updated',NULL);
             COMMIT;          
        END IF;
    ELSIF v_bkup_core_start_date IS NULL AND v_bkup_core_end_date IS NULL  
    THEN
        DELETE 
        FROM rpt_refresh_date 
        WHERE UPPER (report_category_type) = UPPER ('Core') AND
              v_bkup_core_boolean = 1;
              
        prc_execution_log_entry( v_process, 0, 'INFO','Rolling Back rpt_refresh_date for Core..'||SQL%ROWCOUNT||' row(s) deleted',NULL);
        COMMIT;
    END IF;    
     
*/	-- 09/01/2017
END;
/
