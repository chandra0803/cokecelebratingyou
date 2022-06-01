CREATE OR REPLACE PACKAGE pkg_inactive_budget_rd IS

/*-----------------------------------------------------------------------------
   Purpose:  Provide the ability to generate an on-demand BIW Admin extract that will list budgets with no active givers 
   associated with them (inactive budgets) and to be able redistribute the budget

   Person           Date           Comments
   -----------      ----------     -------------------------------------------
   Suresh J         10/23/2015     Initial creation                                          
-----------------------------------------------------------------------------*/

PROCEDURE p_inactive_budgets_extract 
  (p_in_budget_master_id     IN  NUMBER,   
   p_in_budget_segment_id    IN NUMBER,
   p_out_return_code      OUT varchar2,
   p_out_result_set       OUT sys_refcursor
   );
PROCEDURE p_load_inactive_budgets
  (
   p_in_import_file_id    IN     NUMBER,
   p_in_budget_master_id  IN NUMBER,
   p_in_budget_segment_id IN NUMBER,
   p_in_load_type                IN VARCHAR2,
   p_file_records_count      OUT NUMBER,
   p_processed_records_count OUT NUMBER,
   p_out_returncode          OUT NUMBER
   );
END pkg_inactive_budget_rd; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY pkg_inactive_budget_rd
  IS
  /*-----------------------------------------------------------------------------
       Purpose:  Provide the ability to generate an on-demand BIW Admin extract that will list budgets with no active givers 
       associated with them (inactive budgets) and to be able redistribute the budget

       Person           Date           Comments
       -----------      ----------     -------------------------------------------
       Suresh J         10/23/2015     Initial creation                                          
       nagarajs         01/07/2016     Bug 65149 - Org unit-type budget is able to be transferred to pax-type budget,
                                       while it should be able to be distributed only to another org unit-type budget
      Suresh J          02/18/2016    Bug 65805 - Extract doesn't show as expected when owner of the node is inactive or moved out    
      Ravi Dhanekula 03/02/2016 Made changes to extract and load org_unit_id (new column added). 
      Gorantla          05/22/2018     GitLab#1099/Bug 76559 - Extract Inactive Budgets process does not maintain leading zeroes on login ids.  
     -----------------------------------------------------------------------------*/

PROCEDURE p_inactive_budgets_extract 
  (p_in_budget_master_id    IN  NUMBER,
   p_in_budget_segment_id   IN  NUMBER,
   p_out_return_code      OUT varchar2,
   p_out_result_set       OUT sys_refcursor
   )
AS
   -- constants
   c_delimiter CONSTANT VARCHAR2(1) := ',' ;
   c2_delimiter CONSTANT VARCHAR2(1) := '"' ;
   c3_delimiter CONSTANT VARCHAR2(2) := '="' ; -- 05/22/2018

   -- local variables
   l_cursor  SYS_REFCURSOR; 
   file_handler           UTL_FILE.file_type;
   v_extract varchar2(4000);
   v_file_location VARCHAR2(1000);
   directory_error EXCEPTION;
    
   c_process_name        CONSTANT execution_log.process_name%TYPE := 'P_INACTIVE_BUDGETS_EXTRACT';    
   c_parms               CONSTANT VARCHAR2(4000) := 
                                SUBSTR(
                                'p_in_budget_master_id '||p_in_budget_master_id||
                                ', p_in_budget_segment_id '||p_in_budget_segment_id,
                                1,4000);
    

BEGIN
prc_execution_log_entry(c_process_name,1,'PARMS',c_parms,NULL);

  OPEN p_out_result_set FOR 
SELECT textline FROM (
SELECT (ROWNUM+1),
        c2_delimiter||budget_master_name||c2_delimiter||c_delimiter||
        c2_delimiter||Budget_Time_Period||c2_delimiter||c_delimiter||  
        c2_delimiter||Org_Unit_ID||c2_delimiter||c_delimiter||  
        c2_delimiter||Budget_Owner_Name||c2_delimiter||c_delimiter||
        c3_delimiter||budget_owner_login_id||c2_delimiter||c_delimiter|| -- 05/22/2018
        c2_delimiter||Budget_Amount_USD||c2_delimiter||c_delimiter|| 
        c2_delimiter||Transfer_To_Owner1||c2_delimiter||c_delimiter||
        c2_delimiter||Amount_Owner1_USD||c2_delimiter||c_delimiter||
        c2_delimiter||Transfer_To_Owner2||c2_delimiter||c_delimiter||
        c2_delimiter||Amount_Owner2_USD||c2_delimiter||c_delimiter||
        c2_delimiter||Transfer_To_Owner3||c2_delimiter||c_delimiter||
        c2_delimiter||Amount_Owner3_USD||c2_delimiter||c_delimiter Textline
FROM ( 
select CMS.CMS_VALUE as budget_master_name,
       to_char(bs.start_date,'mm/dd/yyyy')||' - '||to_char(bs.end_date,'mm/dd/yyyy') as Budget_Time_Period,
       to_char(bm.start_date,'mm/dd/yyyy')||' - '||to_char(bm.end_date,'mm/dd/yyyy') as Budget_Master_Time_Period,     
       NULL as org_unit_id,  
       au.user_name as budget_owner_login_id,
       au.first_name||' '||au.last_name as Budget_Owner_Name,
       b.current_value as Budget_Amount_USD,
       NULL  Transfer_To_Owner1,
       NULL  Amount_Owner1_USD,       
       NULL  Transfer_To_Owner2,
       NULL  Amount_Owner2_USD,       
       NULL  Transfer_To_Owner3,
       NULL  Amount_Owner3_USD              
from budget b,
     budget_master bm,
     budget_segment bs,
     vw_cms_asset_value cms,
     participant pax,
     application_user au
where b.budget_segment_id = bs.budget_segment_id
      and bs.budget_master_id = bm.budget_master_id
      and b.user_id = pax.user_id
      and pax.status = 'inactive'
      and pax.user_id = au.user_id 
      and cms.asset_code = bm.cm_asset_code
      and cms.key = 'BUDGET_NAME'
      and cms.locale = 'en_US'      
      and b.current_value <> 0
      and bs.budget_segment_id = p_in_budget_segment_id
UNION
      select CMS.CMS_VALUE as budget_master_name,
       to_char(bs.start_date,'mm/dd/yyyy')||' - '||to_char(bs.end_date,'mm/dd/yyyy') as Budget_Time_Period,
       to_char(bm.start_date,'mm/dd/yyyy')||' - '||to_char(bm.end_date,'mm/dd/yyyy') as Budget_Master_Time_Period,       
       b.node_id as org_unit_id,
       NULL as budget_owner_login_id,
       --n.name as Budget_Owner_Name,    --02/18/2016
       SUBSTR(n.name,1,INSTR(n.name,'-',-1,3)-1) as Budget_Owner_Name,    --02/18/2016
       b.current_value as Budget_Amount_USD,
       NULL  Transfer_To_Owner1,
       NULL  Amount_Owner1_USD,       
       NULL  Transfer_To_Owner2,
       NULL  Amount_Owner2_USD,       
       NULL  Transfer_To_Owner3,
       NULL  Amount_Owner3_USD              
from budget b,
     budget_master bm,
     budget_segment bs,
     vw_cms_asset_value cms,
     node n
where b.budget_segment_id = bs.budget_segment_id
      and bs.budget_master_id = bm.budget_master_id
      and b.node_id = n.node_id
      and n.is_deleted = 1
      and cms.asset_code = bm.cm_asset_code
      and cms.key = 'BUDGET_NAME'
      and cms.locale = 'en_US'      
      and b.current_value <> 0
      and bs.budget_segment_id = p_in_budget_segment_id
UNION--To get the active nodes with no active assignments
      select CMS.CMS_VALUE as budget_master_name,
       to_char(bs.start_date,'mm/dd/yyyy')||' - '||to_char(bs.end_date,'mm/dd/yyyy') as Budget_Time_Period,
       to_char(bm.start_date,'mm/dd/yyyy')||' - '||to_char(bm.end_date,'mm/dd/yyyy') as Budget_Master_Time_Period,
       b.node_id as org_unit_id,       
       NULL as budget_owner_login_id,
       n.name as Budget_Owner_Name,
       b.current_value as Budget_Amount_USD,
       NULL  Transfer_To_Owner1,
       NULL  Amount_Owner1_USD,       
       NULL  Transfer_To_Owner2,
       NULL  Amount_Owner2_USD,       
       NULL  Transfer_To_Owner3,
       NULL  Amount_Owner3_USD              
from budget b,
     budget_master bm,
     budget_segment bs,
     vw_cms_asset_value cms,
     node n
where b.budget_segment_id = bs.budget_segment_id
      and bs.budget_master_id = bm.budget_master_id
      and b.node_id = n.node_id
      and n.is_deleted = 0
      and cms.asset_code = bm.cm_asset_code
      and cms.key = 'BUDGET_NAME'
      and cms.locale = 'en_US'      
      and b.current_value <> 0
      and bs.budget_segment_id = p_in_budget_segment_id
AND NOT EXISTS (SELECT un.node_id 
                   FROM user_node un 
                   WHERE un.node_id = n.node_id AND 
                         un.status = 1)
UNION
select CMS.CMS_VALUE as budget_master_name,
       to_char(bs.start_date,'mm/dd/yyyy')||' - '||to_char(bs.end_date,'mm/dd/yyyy') as Budget_Time_Period,
       to_char(bm.start_date,'mm/dd/yyyy')||' - '||to_char(bm.end_date,'mm/dd/yyyy') as Budget_Master_Time_Period,       
       b.node_id as org_unit_id,
       NULL as budget_owner_login_id,
       n.name as Budget_Owner_Name,
       b.current_value as Budget_Amount_USD,
       NULL  Transfer_To_Owner1,
       NULL  Amount_Owner1_USD,       
       NULL  Transfer_To_Owner2,
       NULL  Amount_Owner2_USD,       
       NULL  Transfer_To_Owner3,
       NULL  Amount_Owner3_USD              
from budget b,
     budget_master bm,
     budget_segment bs,
     vw_cms_asset_value cms,
     node n,
     promotion p
where b.budget_segment_id = bs.budget_segment_id
      and bs.budget_master_id = bm.budget_master_id
      and b.node_id = n.node_id
      and n.is_deleted = 0
      and cms.asset_code = bm.cm_asset_code
      and cms.key = 'BUDGET_NAME'
      and cms.locale = 'en_US'      
      and b.current_value <> 0
      and bs.budget_segment_id = p_in_budget_segment_id
      AND bm.budget_master_id = p.award_budget_master_id
      AND p.primary_audience_type <> 'allactivepaxaudience'
AND NOT EXISTS (SELECT un.node_id,a.promotion_id,pa.audience_id 
                    FROM participant_audience pa,
                         promo_audience a,
                         user_node un 
                    WHERE un.node_id = n.node_id AND 
                          un.status = 1 AND 
                          p.promotion_id =  a.promotion_id AND 
                          a.audience_id = pa.audience_id AND 
                          pa.user_id = un.user_id)
                          )) s; 
  p_out_return_code := '00';
  
EXCEPTION
  WHEN OTHERS THEN    
    p_out_return_code := '99';   
    prc_execution_log_entry(c_process_name,1,'ERROR',SQLERRM,NULL);
    OPEN p_out_result_set FOR SELECT NULL FROM dual;

END p_inactive_budgets_extract;  
PROCEDURE p_load_inactive_budgets
  (
   p_in_import_file_id    IN     NUMBER,
   p_in_budget_master_id  IN NUMBER,
   p_in_budget_segment_id IN NUMBER,
   p_in_load_type         IN VARCHAR2,
   p_file_records_count      OUT NUMBER,
   p_processed_records_count OUT NUMBER,
   p_out_returncode          OUT NUMBER
   )
IS
 /*-----------------------------------------------------------------------------
       Purpose:  Provide the ability to generate an on-demand BIW Admin extract that will list budgets with no active givers 
       associated with them (inactive budgets) and to be able redistribute the budget

       Person           Date           Comments
       -----------      ----------     -------------------------------------------
       Suresh J         10/23/2015     Initial creation  
       nagarajs         01/07/2016     Bug 65149 - Org unit-type budget is able to be transferred to pax-type budget,
                                       while it should be able to be distributed only to another org unit-type budget                                        
       Ravi Dhanekula   01/22/2016     Bug 65331 - Budget Meter - Total budget number is not updating
       nagarajs         01/23/2016     Bug 65363 - Inactive budget file getting failed in verified stage
       nagarajs         01/25/2016     Bug 65343 - Transfer of partial inactive budget amount fails
       Ravi Dhanekula   02/05/2016     Bug 65532 -  File Load Process allows Transfer of Active Budgets
       Suresh J         02/24/2016     Bug 65895 - Import of File Should Error when Loading File to Transfer from and 
                                                  Inactive Org Unit Budget to Active Org Unit Budget with no assigned givers
       Suresh J         03/02/2016     Bug 65895 - Staging of  file should not error out when staging from a 
                                                  deleted Org Unit Budget to Active Org Unit Budget
       Suresh J         03/03/2016     Bug 66038 - Active Org unit Budget amount becomes null when the inactive org unit budget amount value 
                                                  from extract is transferred to active org unit budget
                                       Bug 66036 - Budget Redistribution File Load - Budget load is successful but there is no change 
                                                  in the budget amount when the inactive org unit budget amount value from extract is changed 
                                                  to value less than inactive org unit budget amount
      Gorantla         08/22/2017     JIRA G6-2877 - Budget Redistribution - Via file load not able to transfer budget to an active user.                                                  
     -----------------------------------------------------------------------------*/
PRAGMA AUTONOMOUS_TRANSACTION;
   --MISCELLANIOUS
   c_delimiter        CONSTANT VARCHAR2 (10) := '|';
   v_count                     NUMBER := 0;                 --for commit count
   v_stage                     VARCHAR2 (500);
   v_return_code               NUMBER (5);          -- return from insert call
   v_out_error_message         VARCHAR2 (500);
   v_header_rec                BOOLEAN := FALSE;
   v_first_rec                 BOOLEAN := TRUE;
   v_text                      VARCHAR2 (2000);
   v_file_name                 VARCHAR2 (500);
   v_directory_path            VARCHAR2 (4000);
   v_directory_name            VARCHAR2 (30);
   v_is_tied_to_promo          NUMBER(1):=0;    -- 08/22/2017     JIRA G6-2877
   
   --EXCEPTIONS
   e_program_exit              EXCEPTION;           --01/07/2016

   -- RECORD TYPE DECLARATION
   rec_inactive_budget_rd        stage_inactive_budget_rd%ROWTYPE;
   rec_import_record_error     import_record_error%ROWTYPE;

   v_import_record_id               stage_inactive_budget_rd.import_record_id%TYPE;
   v_import_file_id                 stage_inactive_budget_rd.import_file_id%TYPE;
   v_created_by                     stage_inactive_budget_rd.created_by%TYPE
                                    := 0;
   v_timestamp                      stage_inactive_budget_rd.date_created%TYPE
                                    := SYSDATE;
   v_budget_master_name             stage_inactive_budget_rd.budget_master_name%TYPE; 
   v_budget_time_period             stage_inactive_budget_rd.budget_time_period%TYPE; 
   v_budget_master_time_period      stage_inactive_budget_rd.budget_master_time_period%TYPE;    
   v_budget_owner_id            stage_inactive_budget_rd.budget_owner_login_id%TYPE; 
   v_budget_owner_name              stage_inactive_budget_rd.budget_owner_name%TYPE; 
   v_budget_amount                  stage_inactive_budget_rd.budget_amount%TYPE;
   v_transfer_to_owner1             stage_inactive_budget_rd.transfer_to_owner1%TYPE; 
   v_amount_owner1                  stage_inactive_budget_rd.amount_owner1%TYPE;
   v_transfer_to_owner2             stage_inactive_budget_rd.transfer_to_owner2%TYPE; 
   v_amount_owner2                  stage_inactive_budget_rd.amount_owner2%TYPE;
   v_transfer_to_owner3             stage_inactive_budget_rd.transfer_to_owner3%TYPE;
   v_amount_owner3                  stage_inactive_budget_rd.amount_owner3%TYPE;
   v_budget_id                      budget.budget_id%TYPE; 
   v_budget_current_value           budget.current_value%TYPE;

   v_valid_budget_amount            NUMBER (18, 4);
   v_valid_amount_owner1            NUMBER (18, 4);
   v_valid_amount_owner2            NUMBER (18, 4);
   v_valid_amount_owner3            NUMBER (18, 4);      

   v_trf_current_value             budget.current_value%TYPE;
   v_trf_original_value            budget.original_value%TYPE;

   v_valid_budget_master_name       stage_inactive_budget_rd.budget_master_name%TYPE;
   v_budget_master_id               BUDGET_MASTER.budget_master_id%TYPE;
   v_budget_type                    BUDGET_MASTER.budget_type%TYPE;
   v_user_id                        budget.user_id%TYPE;
   v_node_id                        budget.node_id%TYPE;   
   v_valid_pax_id                   budget.user_id%TYPE;
   v_valid_node_id                  budget.node_id%TYPE;      
   v_budget_segment_id              budget_segment.budget_segment_id%TYPE; 
   v_total_transfer                 NUMBER (18, 4); --01/25/2016
   -- EXECUTION LOG VARIABLES
   v_process_name              execution_log.process_name%TYPE
                                  := 'p_load_inactive_budgets';
   v_release_level             execution_log.release_level%TYPE := '1';
   v_execution_log_msg         execution_log.text_line%TYPE;

   -- COUNTS
   v_recs_in_file_cnt          INTEGER := 0;
   v_field_cnt                 INTEGER := 0;
   v_recs_loaded_cnt           INTEGER := 0;
   v_error_count               PLS_INTEGER := 0;
   v_error_tbl_count           PLS_INTEGER := 0;
   v_act_amt_length               NUMBER := 0;
   v_total_rec                 NUMBER(18);          --01/07/2016
   v_bud_rec                   NUMBER(18);          --01/07/2016

   -- EXCEPTIONS
   exit_program_exception      EXCEPTION;
 
  CURSOR cur_verify_stg (cv_imp_file_id NUMBER) IS 
  SELECT    s.import_record_id, 
            s.import_file_id, 
            s.budget_master_name, 
            s.budget_time_period, 
            s.budget_master_time_period, 
            s.org_unit_id,
            s.budget_owner_login_id as budget_owner_id, 
            s.budget_owner_name, 
            s.budget_amount, 
            s.transfer_to_owner1, 
            s.amount_owner1, 
            s.transfer_to_owner2, 
            s.amount_owner2, 
            s.transfer_to_owner3, 
            s.amount_owner3, 
            s.budget_master_id,
            s.budget_segment_id,
            s.budget_id,
            s.user_id,
            s.node_id,
            s.budget_type,
            s.date_created, 
            s.created_by, 
            s.date_modified, 
            s.modified_by, 
            s.version,
            b.original_value,
            b.current_value
  FROM stage_inactive_budget_rd s,
       budget b
  WHERE s.import_file_id = cv_imp_file_id AND
        s.budget_id = b.budget_id (+) AND
        s.budget_master_id = p_in_budget_master_id AND
        s.budget_segment_id = p_in_budget_segment_id AND
        NOT EXISTS (SELECT e.import_record_id   --Skip errored out records during staging 
                        FROM import_record_error e 
                        WHERE e.import_file_id = cv_imp_file_id AND
                              e.import_record_id = s.import_record_id );  

  
  CURSOR cur_stg (cv_imp_file_id NUMBER) IS 
  SELECT    s.import_record_id, 
            s.import_file_id, 
            s.budget_master_name, 
            s.budget_time_period, 
            s.budget_master_time_period, 
            s.org_unit_id,
            s.budget_owner_login_id, 
            s.budget_owner_name, 
            s.budget_amount, 
            s.transfer_to_owner1, 
            s.amount_owner1, 
            s.transfer_to_owner2, 
            s.amount_owner2, 
            s.transfer_to_owner3, 
            s.amount_owner3, 
            s.budget_master_id,
            s.budget_segment_id,
            s.budget_id,
            s.user_id,
            s.node_id,
            s.budget_type,
            s.date_created, 
            s.created_by, 
            s.date_modified, 
            s.modified_by, 
            s.version,
            b.original_value,
            b.current_value
  FROM stage_inactive_budget_rd s,
       budget b
  WHERE s.import_file_id = cv_imp_file_id AND
        s.budget_id = b.budget_id AND
        s.budget_master_id = p_in_budget_master_id AND
        s.budget_segment_id = p_in_budget_segment_id AND
        NOT EXISTS (SELECT e.import_record_id 
                        FROM import_record_error e 
                        WHERE e.import_file_id = cv_imp_file_id AND
                              e.import_record_id = s.import_record_id );  

 BEGIN
   v_stage := 'Write start to execution_log table';
   v_import_file_id := p_in_import_file_id;
   p_out_returncode := 0;

   v_recs_loaded_cnt := 0; 
   v_error_count := 0;
   
   prc_execution_log_entry (v_process_name,
                            v_release_level,
                            'INFO',
                            'Procedure Started for file ID ' || p_in_import_file_id,
                            NULL);


   SELECT COUNT(1)                                   --01/07/2016
     INTO v_total_rec
     FROM stage_inactive_budget_rd
    WHERE import_file_id = v_import_file_id;
 
   SELECT COUNT(1)                                   --01/07/2016
     INTO v_bud_rec
     FROM stage_inactive_budget_rd
    WHERE import_file_id = v_import_file_id
      AND budget_segment_id = p_in_budget_segment_id --01/23/2016
      AND budget_master_id = p_in_budget_master_id;  --01/23/2016
   
   IF v_total_rec <> v_bud_rec THEN                  --01/07/2016
     INSERT INTO import_record_error a
            (import_record_error_id,
             import_file_id,
             import_record_id,
             item_key,
             param1,
             param2,
             param3,
             param4,
             created_by,
             date_created)
      SELECT IMPORT_record_error_pk_SQ.NEXTVAL,
             import_file_id,
             import_record_id,
             'admin.fileload.errors.BUDGET_MASTER_DIFFERENT',
             NULL,
             NULL,
             NULL,
             NULL,
             v_created_by,
             SYSDATE
        FROM stage_inactive_budget_rd
       WHERE import_file_id = v_import_file_id;

     RAISE e_program_exit;

   END IF;

 IF p_in_load_type = 'L' THEN
  
      -- import staged records
      v_stage := 'BUDGET being decremented and incremented.........';
  
      FOR rec_cur_stg IN cur_stg (v_import_file_id) LOOP

        v_budget_id := NULL;
        v_trf_current_value := NULL;
        v_trf_original_value := NULL;

        IF  rec_cur_stg.budget_id IS NOT NULL THEN
        
        
            IF rec_cur_stg.current_value >= rec_cur_stg.budget_amount AND     --03/03/2016 
               rec_cur_stg.budget_amount >= (NVL(rec_cur_stg.amount_owner1,0) + NVL(rec_cur_stg.amount_owner2,0) + NVL(rec_cur_stg.amount_owner3,0)) THEN--01/25/2016
                v_total_transfer := (NVL(rec_cur_stg.amount_owner1,0) + NVL(rec_cur_stg.amount_owner2,0) + NVL(rec_cur_stg.amount_owner3,0)); --01/25/2016
            update budget b
            set  b.current_value = b.current_value - v_total_transfer, --rec_cur_stg.budget_amount,--01/25/2016
                  b.original_value = b.original_value - v_total_transfer, --rec_cur_stg.budget_amount,--01/25/2016
                 b.date_modified = SYSDATE,
                 b.modified_by = 0,
                 b.version = b.version + 1       
            where b.budget_id = rec_cur_stg.budget_id AND
                  b.budget_segment_id = rec_cur_stg.budget_segment_id AND
                  ((rec_cur_stg.budget_type = 'pax' AND b.user_id =  rec_cur_stg.user_id) OR  
                   (rec_cur_stg.budget_type = 'node' AND b.node_id =  rec_cur_stg.node_id));
                   
               insert into budget_history (budget_history_id, budget_id, original_value_before_xaction, current_value_before_xaction, 
                                       original_value_after_xaction, current_value_after_xaction, action_type, 
                                       created_by, date_created)
                               values (budget_history_pk_sq.nextval, rec_cur_stg.budget_id, rec_cur_stg.original_value,rec_cur_stg.current_value,
                                       rec_cur_stg.original_value - v_total_transfer,rec_cur_stg.current_value - v_total_transfer,'transfer', --rec_cur_stg.budget_amount--01/25/2016
                                       0,SYSDATE);  
               

                  IF rec_cur_stg.budget_type = 'pax' THEN
                  
                  
                    IF rec_cur_stg.transfer_to_owner1 IS NOT NULL AND NVL(rec_cur_stg.amount_owner1,0) > 0 THEN   --03/03/2016
                  
                     BEGIN                  

                      SELECT b.budget_id, b.current_value,b.original_value 
                            INTO v_budget_id, v_trf_current_value, v_trf_original_value
                        FROM budget b 
                        WHERE b.user_id = (select user_id from application_user where user_name = UPPER(rec_cur_stg.transfer_to_owner1)) AND
                              b.budget_segment_id = rec_cur_stg.budget_segment_id;
                      EXCEPTION 
                            WHEN NO_DATA_FOUND THEN
                            v_budget_id := NULL;
                            v_trf_current_value := NULL;
                            v_trf_original_value := NULL;
                     END;
                                       
                              
                        IF v_budget_id IS NOT NULL THEN
                        
                            update budget b
                            set  b.current_value = NVL(b.current_value,0) + rec_cur_stg.amount_owner1,
                                  b.original_value = NVL(b.original_value,0) +  rec_cur_stg.amount_owner1,--01/22/2016
                                 b.date_modified = SYSDATE,
                                 b.modified_by = 0,
                                 b.version = b.version + 1       
                            where b.user_id = (select user_id from application_user where user_name = UPPER(rec_cur_stg.transfer_to_owner1)) AND
                                  b.budget_segment_id = rec_cur_stg.budget_segment_id AND
                                  b.budget_id = v_budget_id;   

                           insert into budget_history (budget_history_id, budget_id, original_value_before_xaction, current_value_before_xaction, 
                                                       original_value_after_xaction, current_value_after_xaction, action_type, 
                                                       created_by, date_created)
                                               values (budget_history_pk_sq.nextval, v_budget_id, v_trf_original_value,v_trf_current_value,
                                                       v_trf_original_value + rec_cur_stg.amount_owner1,v_trf_current_value + rec_cur_stg.amount_owner1,'transfer',
                                                       0,SYSDATE);  
                        ELSE

                                insert into budget (budget_id, budget_segment_id, user_id,original_value, current_value, status,action_type, created_by, date_created, version)
                                values (budget_pk_sq.nextval, rec_cur_stg.budget_segment_id, (select user_id from application_user where user_name = UPPER(rec_cur_stg.transfer_to_owner1)), rec_cur_stg.amount_owner1,rec_cur_stg.amount_owner1, 'active','deposit',v_created_by, sysdate, 0);
    
                                insert into budget_history (budget_history_id, budget_id, original_value_before_xaction, current_value_before_xaction, original_value_after_xaction, current_value_after_xaction,action_type, created_by, date_created)
                                values (budget_history_pk_sq.nextval, budget_pk_sq.currval, 0, 0,  rec_cur_stg.amount_owner1 , rec_cur_stg.amount_owner1,'deposit', v_created_by, sysdate);--04/21/2015 Changed initial value of CURRENT_VALUE_BEFORE_XACTION to 0
                                                
                        END IF; --IF v_budget_id IS NOT NULL THEN
                        
                        END IF; --IF rec_cur_stg.transfer_to_owner1 IS NOT NULL
                    v_budget_id := NULL;
                    v_trf_current_value := NULL;
                    v_trf_original_value := NULL;

                    IF rec_cur_stg.transfer_to_owner2 IS NOT NULL AND NVL(rec_cur_stg.amount_owner2,0) > 0 THEN  --03/03/2016
                  
                  
                     BEGIN
                     
                     SELECT b.budget_id, b.current_value,b.original_value 
                            INTO v_budget_id, v_trf_current_value, v_trf_original_value
                        FROM budget b 
                        WHERE b.user_id = (select user_id from application_user where user_name = UPPER(rec_cur_stg.transfer_to_owner2)) AND
                              b.budget_segment_id = rec_cur_stg.budget_segment_id;
                     
                     EXCEPTION 
                            WHEN NO_DATA_FOUND THEN
                            v_budget_id := NULL;
                            v_trf_current_value := NULL;
                            v_trf_original_value := NULL;
                     END;         
                        IF v_budget_id IS NOT NULL THEN
                            update budget b
                            set  b.current_value = NVL(b.current_value,0) + rec_cur_stg.amount_owner2,
                                  b.original_value = NVL(b.original_value,0) +  rec_cur_stg.amount_owner2,--01/22/2016
                                 b.date_modified = SYSDATE,
                                 b.modified_by = 0,
                                 b.version = b.version + 1       
                            where b.user_id =  (select user_id from application_user where user_name = UPPER(rec_cur_stg.transfer_to_owner2)) AND
                                  b.budget_segment_id = rec_cur_stg.budget_segment_id AND
                                  b.budget_id = v_budget_id;   
                          
                        
                           insert into budget_history (budget_history_id, budget_id, original_value_before_xaction, current_value_before_xaction, 
                                                       original_value_after_xaction, current_value_after_xaction, action_type, 
                                                       created_by, date_created)
                                               values (budget_history_pk_sq.nextval, v_budget_id, v_trf_original_value,v_trf_current_value,
                                                       v_trf_original_value + rec_cur_stg.amount_owner2,v_trf_current_value + rec_cur_stg.amount_owner2,'transfer',
                                                       0,SYSDATE);  
                        ELSE

                                insert into budget (budget_id, budget_segment_id, user_id,original_value, current_value, status,action_type, created_by, date_created, version)
                                values (budget_pk_sq.nextval, rec_cur_stg.budget_segment_id, (select user_id from application_user where user_name = UPPER(rec_cur_stg.transfer_to_owner2)), rec_cur_stg.amount_owner2,rec_cur_stg.amount_owner2, 'active','deposit',v_created_by, sysdate, 0);
    
                                insert into budget_history (budget_history_id, budget_id, original_value_before_xaction, current_value_before_xaction, original_value_after_xaction, current_value_after_xaction,action_type, created_by, date_created)
                                values (budget_history_pk_sq.nextval, budget_pk_sq.currval, 0, 0,  rec_cur_stg.amount_owner2 , rec_cur_stg.amount_owner2,'deposit', v_created_by, sysdate);
                                                
                        END IF; --IF v_budget_id IS NOT NULL THEN
                        
                        END IF; --IF rec_cur_stg.transfer_to_owner2 IS NOT NULL

                    v_budget_id := NULL;
                    v_trf_current_value := NULL;
                    v_trf_original_value := NULL;

                    IF rec_cur_stg.transfer_to_owner3 IS NOT NULL AND NVL(rec_cur_stg.amount_owner3,0) > 0 THEN    --03/03/2016
                  
                   
                     BEGIN 
                        SELECT b.budget_id, b.current_value,b.original_value 
                            INTO v_budget_id, v_trf_current_value, v_trf_original_value
                        FROM budget b 
                        WHERE b.user_id = (select user_id from application_user where user_name = UPPER(rec_cur_stg.transfer_to_owner3)) AND
                              b.budget_segment_id = rec_cur_stg.budget_segment_id;
                      EXCEPTION 
                            WHEN NO_DATA_FOUND THEN
                            v_budget_id := NULL;
                            v_trf_current_value := NULL;
                            v_trf_original_value := NULL;
                      END;
                               
                        IF v_budget_id IS NOT NULL THEN
                        
                            update budget b
                            set  b.current_value = NVL(b.current_value,0) + rec_cur_stg.amount_owner3,
                                  b.original_value = NVL(b.original_value,0) +  rec_cur_stg.amount_owner3,--01/22/2016
                                 b.date_modified = SYSDATE,
                                 b.modified_by = 0,
                                 b.version = b.version + 1       
                            where b.user_id =  (select user_id from application_user where user_name = UPPER(rec_cur_stg.transfer_to_owner3)) AND
                                  b.budget_segment_id = rec_cur_stg.budget_segment_id AND
                                  b.budget_id = v_budget_id;   

                        
                           insert into budget_history (budget_history_id, budget_id, original_value_before_xaction, current_value_before_xaction, 
                                                       original_value_after_xaction, current_value_after_xaction, action_type, 
                                                       created_by, date_created)
                                               values (budget_history_pk_sq.nextval, v_budget_id, v_trf_original_value,v_trf_current_value,
                                                       v_trf_original_value + rec_cur_stg.amount_owner3,v_trf_current_value + rec_cur_stg.amount_owner3,'transfer',
                                                       0,SYSDATE);  
                        ELSE

                                insert into budget (budget_id, budget_segment_id, user_id,original_value, current_value, status,action_type, created_by, date_created, version)
                                values (budget_pk_sq.nextval, rec_cur_stg.budget_segment_id, (select user_id from application_user where user_name = UPPER(rec_cur_stg.transfer_to_owner3)), rec_cur_stg.amount_owner3,rec_cur_stg.amount_owner3, 'active','deposit',v_created_by, sysdate, 0);
    
                                insert into budget_history (budget_history_id, budget_id, original_value_before_xaction, current_value_before_xaction, original_value_after_xaction, current_value_after_xaction,action_type, created_by, date_created)
                                values (budget_history_pk_sq.nextval, budget_pk_sq.currval, 0, 0,  rec_cur_stg.amount_owner3 , rec_cur_stg.amount_owner3,'deposit', v_created_by, sysdate);
                                                
                        END IF; --IF v_budget_id IS NOT NULL THEN
                        
                        END IF; --IF rec_cur_stg.transfer_to_owner3 IS NOT NULL
                  --------------------------------------------------------------------------------------------------------------------------------------------
                  ELSIF rec_cur_stg.budget_type = 'node' THEN

                    IF rec_cur_stg.transfer_to_owner1 IS NOT NULL AND NVL(rec_cur_stg.amount_owner1,0) > 0 THEN    --03/03/2016
                  
                    BEGIN 
                     SELECT b.budget_id, b.current_value,b.original_value 
                            INTO v_budget_id, v_trf_current_value, v_trf_original_value
                        FROM budget b 
                        WHERE b.node_id = (select node_id from node where UPPER(name) = UPPER(rec_cur_stg.transfer_to_owner1)) AND
                              b.budget_segment_id = rec_cur_stg.budget_segment_id;
                      
                      EXCEPTION 
                            WHEN NO_DATA_FOUND THEN
                            v_budget_id := NULL;
                            v_trf_current_value := NULL;
                            v_trf_original_value := NULL;
                    END;
                              
                        IF v_budget_id IS NOT NULL THEN
                            update budget b
                            set  b.current_value = NVL(b.current_value,0) + rec_cur_stg.amount_owner1,
                                  b.original_value = NVL(b.original_value,0) +  rec_cur_stg.amount_owner1,--01/22/2016
                                 b.date_modified = SYSDATE,
                                 b.modified_by = 0,
                                 b.version = b.version + 1       
                            where b.node_id =  (select node_id from node where UPPER(name) = UPPER(rec_cur_stg.transfer_to_owner1)) AND
                                  b.budget_segment_id = rec_cur_stg.budget_segment_id AND
                                  b.budget_id = v_budget_id;   

                        
                           insert into budget_history (budget_history_id, budget_id, original_value_before_xaction, current_value_before_xaction, 
                                                       original_value_after_xaction, current_value_after_xaction, action_type, 
                                                       created_by, date_created)
                                               values (budget_history_pk_sq.nextval, v_budget_id, v_trf_original_value,v_trf_current_value,
                                                       v_trf_original_value + rec_cur_stg.amount_owner1,v_trf_current_value + rec_cur_stg.amount_owner1,'transfer',
                                                       0,SYSDATE);  
                        ELSE
                             
                                insert into budget (budget_id, budget_segment_id, node_id,original_value, current_value, status,action_type, created_by, date_created, version)
                                values (budget_pk_sq.nextval, rec_cur_stg.budget_segment_id, (select node_id from node where UPPER(name) = UPPER(rec_cur_stg.transfer_to_owner1)), rec_cur_stg.amount_owner1,rec_cur_stg.amount_owner1, 'active','deposit',v_created_by, sysdate, 0);
    
                                insert into budget_history (budget_history_id, budget_id, original_value_before_xaction, current_value_before_xaction, original_value_after_xaction, current_value_after_xaction,action_type, created_by, date_created)
                                values (budget_history_pk_sq.nextval, budget_pk_sq.currval, 0, 0,  rec_cur_stg.amount_owner1 , rec_cur_stg.amount_owner1,'deposit', v_created_by, sysdate);--04/21/2015 Changed initial value of CURRENT_VALUE_BEFORE_XACTION to 0
                                                
                        END IF; --IF v_budget_id IS NOT NULL THEN
                        
                        END IF; --IF rec_cur_stg.transfer_to_owner1 IS NOT NULL
                    v_budget_id := NULL;
                    v_trf_current_value := NULL;
                    v_trf_original_value := NULL;

                    IF rec_cur_stg.transfer_to_owner2 IS NOT NULL AND NVL(rec_cur_stg.amount_owner2,0) > 0 THEN        --03/03/2016
                  
                     BEGIN
                      SELECT b.budget_id, b.current_value,b.original_value 
                            INTO v_budget_id, v_trf_current_value, v_trf_original_value
                        FROM budget b 
                        WHERE b.node_id = (select node_id from node where UPPER(name) = UPPER(rec_cur_stg.transfer_to_owner2)) AND
                              b.budget_segment_id = rec_cur_stg.budget_segment_id;
                     
                      EXCEPTION 
                            WHEN NO_DATA_FOUND THEN
                            v_budget_id := NULL;
                            v_trf_current_value := NULL;
                            v_trf_original_value := NULL;
                     END;
                              
                        IF v_budget_id IS NOT NULL THEN
                            update budget b
                            set  b.current_value = NVL(b.current_value,0) + rec_cur_stg.amount_owner2,
                                  b.original_value = NVL(b.original_value,0) +  rec_cur_stg.amount_owner2,--01/22/2016
                                 b.date_modified = SYSDATE,
                                 b.modified_by = 0,
                                 b.version = b.version + 1       
                            where b.node_id =  (select node_id from node where UPPER(name) = UPPER(rec_cur_stg.transfer_to_owner2)) AND
                                  b.budget_segment_id = rec_cur_stg.budget_segment_id AND
                                  b.budget_id = v_budget_id;   

                        
                           insert into budget_history (budget_history_id, budget_id, original_value_before_xaction, current_value_before_xaction, 
                                                       original_value_after_xaction, current_value_after_xaction, action_type, 
                                                       created_by, date_created)
                                               values (budget_history_pk_sq.nextval, v_budget_id, v_trf_original_value,v_trf_current_value,
                                                       v_trf_original_value + rec_cur_stg.amount_owner2,v_trf_current_value + rec_cur_stg.amount_owner2,'transfer',
                                                       0,SYSDATE);  
                        ELSE
                             
                                insert into budget (budget_id, budget_segment_id, node_id,original_value, current_value, status,action_type, created_by, date_created, version)
                                values (budget_pk_sq.nextval, rec_cur_stg.budget_segment_id, (select node_id from node where UPPER(name) = UPPER(rec_cur_stg.transfer_to_owner2)), rec_cur_stg.amount_owner2,rec_cur_stg.amount_owner2, 'active','deposit',v_created_by, sysdate, 0);
    
                                insert into budget_history (budget_history_id, budget_id, original_value_before_xaction, current_value_before_xaction, original_value_after_xaction, current_value_after_xaction,action_type, created_by, date_created)
                                values (budget_history_pk_sq.nextval, budget_pk_sq.currval, 0, 0,  rec_cur_stg.amount_owner2 , rec_cur_stg.amount_owner2,'deposit', v_created_by, sysdate);
                                                
                        END IF; --IF v_budget_id IS NOT NULL THEN
                        
                        END IF; --IF rec_cur_stg.transfer_to_owner2 IS NOT NULL

                    v_budget_id := NULL;
                    v_trf_current_value := NULL;
                    v_trf_original_value := NULL;

                    IF rec_cur_stg.transfer_to_owner3 IS NOT NULL AND NVL(rec_cur_stg.amount_owner3,0) > 0 THEN     --03/03/2016
                  
                    BEGIN
                    
                     SELECT b.budget_id, b.current_value,b.original_value 
                            INTO v_budget_id, v_trf_current_value, v_trf_original_value
                        FROM budget b 
                        WHERE b.node_id = (select node_id from node where UPPER(name) = UPPER(rec_cur_stg.transfer_to_owner3)) AND
                              b.budget_segment_id = rec_cur_stg.budget_segment_id;
                    
                     EXCEPTION 
                            WHEN NO_DATA_FOUND THEN
                            v_budget_id := NULL;
                            v_trf_current_value := NULL;
                            v_trf_original_value := NULL;
                    END;
                              
                        IF v_budget_id IS NOT NULL THEN
                            update budget b
                            set  b.current_value = NVL(b.current_value,0) + rec_cur_stg.amount_owner3,
                                  b.original_value = NVL(b.original_value,0) +  rec_cur_stg.amount_owner3,--01/22/2016
                                 b.date_modified = SYSDATE,
                                 b.modified_by = 0,
                                 b.version = b.version + 1       
                            where b.node_id =  (select node_id from node where UPPER(name) = UPPER(rec_cur_stg.transfer_to_owner3)) AND
                                  b.budget_segment_id = rec_cur_stg.budget_segment_id AND
                                  b.budget_id = v_budget_id;   

                        
                           insert into budget_history (budget_history_id, budget_id, original_value_before_xaction, current_value_before_xaction, 
                                                       original_value_after_xaction, current_value_after_xaction, action_type, 
                                                       created_by, date_created)
                                               values (budget_history_pk_sq.nextval, v_budget_id, v_trf_original_value,v_trf_current_value,
                                                       v_trf_original_value + rec_cur_stg.amount_owner3,v_trf_current_value + rec_cur_stg.amount_owner3,'transfer',
                                                       0,SYSDATE);  
                        ELSE
                             
                                insert into budget (budget_id, budget_segment_id, node_id,original_value, current_value, status,action_type, created_by, date_created, version)
                                values (budget_pk_sq.nextval, rec_cur_stg.budget_segment_id, (select node_id from node where UPPER(name) = UPPER(rec_cur_stg.transfer_to_owner3)), rec_cur_stg.amount_owner3,rec_cur_stg.amount_owner3, 'active','deposit',v_created_by, sysdate, 0);
    
                                insert into budget_history (budget_history_id, budget_id, original_value_before_xaction, current_value_before_xaction, original_value_after_xaction, current_value_after_xaction,action_type, created_by, date_created)
                                values (budget_history_pk_sq.nextval, budget_pk_sq.currval, 0, 0,  rec_cur_stg.amount_owner3 , rec_cur_stg.amount_owner3,'deposit', v_created_by, sysdate);
                                                
                        END IF; --IF v_budget_id IS NOT NULL THEN
                        
                        END IF; --IF rec_cur_stg.transfer_to_owner3 IS NOT NULL
                  --------------------------------------------------------------------------------------------------------------------------------------------                  
                  
                    END IF;  --IF rec_cur_stg.budget_type = 'pax' or ELSIF  rec_cur_stg.budget_type = 'node'
                    
                    v_recs_loaded_cnt :=  v_recs_loaded_cnt + 1;
                    
                  END IF;  --IF rec_cur_stg.current_value = rec_cur_stg.budget_amount

           END IF; --rec_cur_stg.budget_id IS NOT NULL THEN
      
      END LOOP;  --FOR rec_cur_stg IN cur_stg (v_import_file_id) LOOP
      
   COMMIT;

    SELECT COUNT (DISTINCT (import_record_id))
    INTO v_error_tbl_count
    FROM import_record_error
    WHERE import_file_id = v_import_file_id;


   v_stage := 'Total error count';
   v_error_count := v_error_count + v_error_tbl_count;
 
   v_stage := 'Update import_file table with record counts';
  -- UPDATE import_file f
   --   SET version = version + 1,
   --       status = 'imp',
    --      date_imported = SYSDATE,
    --      import_record_count = v_recs_loaded_cnt,
    --      import_record_error_count = v_error_count
    --WHERE import_file_id = v_import_file_id;

   p_processed_records_count := v_recs_loaded_cnt; 
   p_file_records_count := v_error_count ;



   v_stage := 'Write counts and ending to execution_log table';
   prc_execution_log_entry (
      v_process_name,
      v_release_level,
      'INFO',
         'Count of records on file: '
      || v_recs_in_file_cnt
      || '  Count of records in error: '
      || v_error_count,
      NULL);

    COMMIT;
 
  prc_execution_log_entry (v_process_name,
                            v_release_level,
                            'INFO',
                            'Procedure completed for file ID ' || p_in_import_file_id,
                            NULL);
      
   COMMIT;

 ELSIF p_in_load_type = 'V' THEN
 
   BEGIN
   
    v_stage := 'Write start to execution_log table';
    p_out_returncode := 0;
    v_recs_loaded_cnt := 0;
    v_error_count  := 0;
    
   prc_execution_log_entry (v_process_name,
                            v_release_level,
                            'INFO',
                            'Verify Process Started for file ' || v_import_file_id,
                            NULL);

    FOR rec_cur_stg IN cur_verify_stg (v_import_file_id) LOOP
    
        v_stage := 'Reset variables';
        v_field_cnt := 0;
        v_budget_owner_id   := NULL;
        v_budget_owner_name       := NULL;
      
        v_budget_current_value       := NULL;
        v_valid_budget_amount        := NULL;   
        v_valid_amount_owner1        := NULL; 
        v_valid_amount_owner2        := NULL;
        v_valid_amount_owner3        := NULL;
        v_valid_budget_master_name   := NULL;
        v_user_id                    := NULL;
        v_node_id                    := NULL;
        v_valid_pax_id               := NULL;
        v_valid_node_id              := NULL;
        v_budget_master_id           := NULL;
        v_budget_segment_id          := NULL;
        v_budget_id                  := NULL;    
      
      BEGIN

            v_import_record_id  := rec_cur_stg.import_record_id;

            rec_import_record_error.import_record_id := v_import_record_id;
            rec_import_record_error.import_file_id := v_import_file_id;

               -- Budget Master Name should be valid and live
               IF (rec_cur_stg.budget_master_name IS NOT NULL)
               THEN
                  BEGIN
                     SELECT cms.cms_value,budget_master_id,budget_type
                       INTO v_valid_budget_master_name,v_budget_master_id,v_budget_type
                       FROM budget_master bm,
                            vw_cms_asset_value cms
                      WHERE cms.cms_value = rec_cur_stg.budget_master_name AND
                            cms.asset_code = bm.cm_asset_code AND
                            cms.key = 'BUDGET_NAME' AND
                            cms.locale = 'en_US';
                  EXCEPTION
                     WHEN NO_DATA_FOUND
                     THEN
                          v_execution_log_msg := 'Budget Master Name should be Valid';
                           rec_import_record_error.item_key :=
                              'admin.fileload.errors.MISSING_PROPERTY';
                           rec_import_record_error.param1 := 'Budget Master Name:'||rec_cur_stg.budget_master_name;
                           rec_import_record_error.created_by := v_created_by;
                           prc_insert_import_record_error (rec_import_record_error);
                           --v_budget_master_name := NULL;
                           v_budget_type := NULL;
                        --EXIT;
                  END;
               END IF;

               IF rec_cur_stg.budget_time_period IS NOT NULL
               THEN
                  BEGIN
                     
                     SELECT bs.budget_segment_id 
                            INTO v_budget_segment_id 
                     FROM budget_segment bs,
                          budget_master bm
                     WHERE bs.budget_master_id = bm.budget_master_id and
                           bm.budget_master_id = v_budget_master_id and
                           TRIM(to_char(bs.start_date,'mm/dd/yyyy')||' - '||to_char(bs.end_date,'mm/dd/yyyy')) = rec_cur_stg.budget_time_period;      
                       
                  EXCEPTION
                     WHEN OTHERS
                     THEN                     
                        rec_import_record_error.item_key :=
                           'system.errors.DATE';
                        rec_import_record_error.param1 := rec_cur_stg.budget_time_period;
                        rec_import_record_error.param2 :=
                           SUBSTR (SQLERRM, 1, 250);
                        rec_import_record_error.created_by := v_created_by;
                        prc_insert_import_record_error (
                           rec_import_record_error);
                        --v_budget_time_period := NULL;
                  END;
               END IF;
 
 
          IF v_budget_type = 'pax' THEN   
          
          -- 08/22/2017     JIRA G6-2877
               BEGIN
                  SELECT 1
                    INTO v_is_tied_to_promo
                    FROM dual WHERE EXISTS (SELECT '1' FROM promotion p WHERE p.award_budget_master_id  = v_budget_master_id);
                 EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.budgetdistribution.BUDGET_NOT_TIED_TO_PROGRAM';
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
               END;
 

            -- Login id for participant (not non-pax) is valid and participant is active status.
            IF (rec_cur_stg.budget_owner_id IS NOT NULL)
            THEN
               BEGIN
                  SELECT user_id
                    INTO v_user_id
                    FROM application_user
                   WHERE UPPER (user_name) = UPPER (rec_cur_stg.budget_owner_id);
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'User Name';
                     rec_import_record_error.param2 := rec_cur_stg.budget_owner_id;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_user_id := NULL;
               END;                                   --  check if user exists
            END IF;



            IF (v_user_id IS NOT NULL)
            THEN
               BEGIN
                  SELECT au.user_id
                    INTO v_valid_pax_id
                    FROM application_user au
                   WHERE     au.user_id = v_user_id
                         AND EXISTS
                                (SELECT b.user_id
                                   FROM budget b
                                  WHERE     b.budget_segment_id = v_budget_segment_id
                                        AND b.user_id = au.user_id);
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'User ID';
                     rec_import_record_error.param2 := v_user_id;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_pax_id := NULL;
               END;                                --  check if user has valid budget
            END IF;
            
             IF (v_user_id IS NOT NULL) --02/05/2016 Bug # 65532
            THEN
               BEGIN
                  SELECT pax.user_id
                    INTO v_valid_pax_id
                    FROM application_user pax
                   WHERE UPPER (user_name) = UPPER (rec_cur_stg.budget_owner_id) AND is_active = 0;
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.budgetdistribution.SOURCE_BUDGET_IS_NOT_AN_INACTIVE';
                     rec_import_record_error.param1 :=
                        'User Name - ' || rec_cur_stg.budget_owner_id;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_pax_id := NULL;
               END;                                --  check if user is Active (should not take from active budget)
            END IF;
          
           ---------------------------------------------------------------------------------------------------------------------------------
            v_user_id := NULL; 
           --Validating v_transfer_to_owner1 column
            IF LTRIM (RTRIM (rec_cur_stg.transfer_to_owner1)) IS NULL
            THEN
               rec_import_record_error.item_key :=
                  'admin.fileload.errors.MISSING_PROPERTY';
               rec_import_record_error.param1 := 'Transfer To User Name1';
               rec_import_record_error.created_by := v_created_by;
               prc_insert_import_record_error (rec_import_record_error);
            END IF;

            -- Login id for participant (not non-pax) is valid and participant is active status.
            IF (rec_cur_stg.transfer_to_owner1 IS NOT NULL)
            THEN
               BEGIN
                  SELECT user_id
                    INTO v_user_id
                    FROM application_user
                   WHERE UPPER (user_name) = UPPER (rec_cur_stg.transfer_to_owner1);
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'User Name 1';
                     rec_import_record_error.param2 := rec_cur_stg.transfer_to_owner1;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_user_id := NULL;
               END;                                   --  check if user exists
            END IF;



            IF (v_user_id IS NOT NULL)
            THEN
            
            --08/22/2017  JIRA G6-2877 splitted the query to provide appropriate error message 
            
               BEGIN
                  SELECT au.user_id
                    INTO v_valid_pax_id
                    FROM application_user au
                   WHERE au.user_id = v_user_id
                     AND is_active = 1;
                 EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.budgetdistribution.TRANSFER_TO_OWNER1_IS_INACTIVE';
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_pax_id := NULL;
               END; 
               
                                             
               IF (v_is_tied_to_promo != 0 AND v_valid_pax_id IS NOT NULL) THEN
                BEGIN
                  SELECT au.user_id
                    INTO v_valid_pax_id
                    FROM application_user au
                   WHERE     au.user_id = v_user_id
                         AND is_active = 1
                         AND (EXISTS
                                (SELECT pax_aud.user_id
                                   FROM participant_audience pax_aud,
                                        promo_audience promo_aud,
                                        promotion p
                                  WHERE   pax_aud.user_id = au.user_id and
                                          pax_aud.audience_id = promo_aud.audience_id and
                                          promo_aud.promotion_id = p.promotion_id and
                                          p.award_budget_master_id = v_budget_master_id )  OR 
                                                          EXISTS (SELECT pax.user_id 
                                                                    FROM participant pax,
                                                                         promotion p 
                                                                    WHERE p.award_budget_master_id = v_budget_master_id and
                                                                          p.primary_audience_type = 'allactivepaxaudience' and
                                                                          pax.status = 'active' and 
                                                                          pax.user_id = au.user_id));
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.budgetdistribution.TRANSFER_OWNER_NOT_IN_ELIGIBLE_AUDIENCE';
--                     rec_import_record_error.param1 := 'Transfer To User ID 1 - ';--02/05/2016
--                      rec_import_record_error.param2 := v_user_id; ----02/05/2016
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_pax_id := NULL;
               END;                                --  check if user is Active
             END IF;  
            END IF;

           ---------------------------------------------------------------------------------------------------------------------------------          
           --Validating v_transfer_to_owner2 column 
           v_user_id := NULL;
            -- Budget Owner Login ID is Required
                --            IF LTRIM (RTRIM (v_transfer_to_owner2)) IS NULL
                --            THEN
                --               rec_import_record_error.item_key :=
                --                  'admin.fileload.errors.MISSING_PROPERTY';
                --               rec_import_record_error.param1 := 'Transfer To User Name1';
                --               rec_import_record_error.created_by := v_created_by;
                --               prc_insert_import_record_error (rec_import_record_error);
                --            END IF;

            -- Login id for participant (not non-pax) is valid and participant is active status.
            IF (rec_cur_stg.transfer_to_owner2 IS NOT NULL)
            THEN
               BEGIN
                  SELECT user_id
                    INTO v_user_id
                    FROM application_user
                   WHERE UPPER (user_name) = UPPER (rec_cur_stg.transfer_to_owner2);
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'User Name 2';
                     rec_import_record_error.param2 := rec_cur_stg.transfer_to_owner2;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_user_id := NULL;
               END;                                   --  check if user exists
            END IF;

          IF (v_user_id IS NOT NULL)
            THEN
            
            -- 08/22/2017  JIRA G6-2877 splitted the previouse query to provide appropriate error message
            
               BEGIN
                  SELECT au.user_id
                    INTO v_valid_pax_id
                    FROM application_user au
                   WHERE au.user_id = v_user_id
                     AND is_active = 1;
                 EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.budgetdistribution.TRANSFER_TO_OWNER1_IS_INACTIVE';
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_pax_id := NULL;
               END; 
               
                                             
               IF (v_is_tied_to_promo != 0 AND v_valid_pax_id IS NOT NULL) THEN
                BEGIN
                  SELECT au.user_id
                    INTO v_valid_pax_id
                    FROM application_user au
                   WHERE     au.user_id = v_user_id
                         AND is_active = 1
                         AND (EXISTS
                                (SELECT pax_aud.user_id
                                   FROM participant_audience pax_aud,
                                        promo_audience promo_aud,
                                        promotion p
                                  WHERE   pax_aud.user_id = au.user_id and
                                          pax_aud.audience_id = promo_aud.audience_id and
                                          promo_aud.promotion_id = p.promotion_id and
                                          p.award_budget_master_id = v_budget_master_id )  OR 
                                                          EXISTS (SELECT pax.user_id 
                                                                    FROM participant pax,
                                                                         promotion p 
                                                                    WHERE p.award_budget_master_id = v_budget_master_id and
                                                                          p.primary_audience_type = 'allactivepaxaudience' and
                                                                          pax.status = 'active' and 
                                                                          pax.user_id = au.user_id));
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.budgetdistribution.TRANSFER_OWNER_NOT_IN_ELIGIBLE_AUDIENCE';
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_pax_id := NULL;
               END;
             END IF;  
               
            END IF;
            
            -- 08/22/2017  JIRA G6-2877 commented and splitted the query below to provide appropriate error message

           /* IF (v_user_id IS NOT NULL)
            THEN
               BEGIN
                  SELECT au.user_id
                    INTO v_valid_pax_id
                    FROM application_user au
                   WHERE     au.user_id = v_user_id
                         AND is_active = 1
                         AND (EXISTS
                                (SELECT pax_aud.user_id
                                   FROM participant_audience pax_aud,
                                        promo_audience promo_aud,
                                        promotion p
                                  WHERE   pax_aud.user_id = au.user_id and
                                          pax_aud.audience_id = promo_aud.audience_id and
                                          promo_aud.promotion_id = p.promotion_id and
                                          p.award_budget_master_id = v_budget_master_id )  OR 
                                                          EXISTS (SELECT pax.user_id 
                                                                    FROM participant pax,
                                                                         promotion p 
                                                                    WHERE p.award_budget_master_id = v_budget_master_id and
                                                                          p.primary_audience_type = 'allactivepaxaudience' and
                                                                          pax.status = 'active' and 
                                                                          pax.user_id = au.user_id));
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Transfer To User ID 2';
                     rec_import_record_error.param2 := v_user_id;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_pax_id := NULL;
               END;                                --  check if user is Active
            END IF;*/

           ---------------------------------------------------------------------------------------------------------------------------------          
           --Validating v_transfer_to_owner3 column 
           v_user_id := NULL;
            -- Budget Owner Login ID is Required
                --            IF LTRIM (RTRIM (v_transfer_to_owner3)) IS NULL
                --            THEN
                --               rec_import_record_error.item_key :=
                --                  'admin.fileload.errors.MISSING_PROPERTY';
                --               rec_import_record_error.param1 := 'Transfer To User Name1';
                --               rec_import_record_error.created_by := v_created_by;
                --               prc_insert_import_record_error (rec_import_record_error);
                --            END IF;

            -- Login id for participant (not non-pax) is valid and participant is active status.
            IF (rec_cur_stg.transfer_to_owner3 IS NOT NULL)
            THEN
               BEGIN
                  SELECT user_id
                    INTO v_user_id
                    FROM application_user
                   WHERE UPPER (user_name) = UPPER (rec_cur_stg.transfer_to_owner3);
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'User Name 3';
                     rec_import_record_error.param2 := rec_cur_stg.transfer_to_owner3;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_user_id := NULL;
               END;                                   --  check if user exists
            END IF;



            IF (v_user_id IS NOT NULL)
            THEN
              -- 08/22/2017  JIRA G6-2877 splitted the previouse query to provide appropriate error message
               BEGIN
                  SELECT au.user_id
                    INTO v_valid_pax_id
                    FROM application_user au
                   WHERE au.user_id = v_user_id
                     AND is_active = 1;
                 EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.budgetdistribution.TRANSFER_TO_OWNER1_IS_INACTIVE';
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_pax_id := NULL;
               END; 
               
               IF (v_is_tied_to_promo != 0 AND v_valid_pax_id IS NOT NULL) THEN
                BEGIN
                  SELECT au.user_id
                    INTO v_valid_pax_id
                    FROM application_user au
                   WHERE     au.user_id = v_user_id
                         AND is_active = 1
                         AND (EXISTS
                                (SELECT pax_aud.user_id
                                   FROM participant_audience pax_aud,
                                        promo_audience promo_aud,
                                        promotion p
                                  WHERE   pax_aud.user_id = au.user_id and
                                          pax_aud.audience_id = promo_aud.audience_id and
                                          promo_aud.promotion_id = p.promotion_id and
                                          p.award_budget_master_id = v_budget_master_id ) OR 
                                                          EXISTS (SELECT pax.user_id 
                                                                    FROM participant pax,
                                                                         promotion p 
                                                                    WHERE p.award_budget_master_id = v_budget_master_id and
                                                                          p.primary_audience_type = 'allactivepaxaudience' and
                                                                          pax.status = 'active' and 
                                                                          pax.user_id = au.user_id));
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.budgetdistribution.TRANSFER_OWNER_NOT_IN_ELIGIBLE_AUDIENCE';
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_pax_id := NULL;
               END;
             END IF;  
               
            END IF;

            -- 08/22/2017  JIRA G6-2877 commented and splitted the query below to provide appropriate error message

/*
            IF (v_user_id IS NOT NULL)
            THEN
               BEGIN
                  SELECT au.user_id
                    INTO v_valid_pax_id
                    FROM application_user au
                   WHERE     au.user_id = v_user_id
                         AND is_active = 1
                         AND (EXISTS
                                (SELECT pax_aud.user_id
                                   FROM participant_audience pax_aud,
                                        promo_audience promo_aud,
                                        promotion p
                                  WHERE   pax_aud.user_id = au.user_id and
                                          pax_aud.audience_id = promo_aud.audience_id and
                                          promo_aud.promotion_id = p.promotion_id and
                                          p.award_budget_master_id = v_budget_master_id ) OR 
                                                          EXISTS (SELECT pax.user_id 
                                                                    FROM participant pax,
                                                                         promotion p 
                                                                    WHERE p.award_budget_master_id = v_budget_master_id and
                                                                          p.primary_audience_type = 'allactivepaxaudience' and
                                                                          pax.status = 'active' and 
                                                                          pax.user_id = au.user_id));
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Transfer To User ID 3';
                     rec_import_record_error.param2 := v_user_id;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_pax_id := NULL;
               END;                                --  check if user is Active
            END IF;*/
            
          
          ELSIF   v_budget_type = 'node' THEN
          
            v_node_id := NULL;
            IF (rec_cur_stg.budget_owner_name IS NOT NULL)
            THEN
               BEGIN

                --SELECT node_id      --03/02/2016 
                --INTO v_node_id
                --FROM node
                --WHERE UPPER (name) = UPPER (rec_cur_stg.budget_owner_name);

                  SELECT node_id                
                    INTO v_node_id
                    FROM node WHERE node_id = rec_cur_stg.org_unit_id;
--                    (
--                    SELECT node_id,name as node_name
--                        FROM node
--                       WHERE UPPER (name) = UPPER (rec_cur_stg.budget_owner_name) AND
--                             is_deleted = 0
--                    UNION ALL     --03/02/2016
--                    --Deleted node name should be truncated to remove the timestamp      --03/02/2016 
--                    --since the incoming file contains only truncated name for deleted nodes
--                    SELECT node_id,SUBSTR(n.name,1,INSTR(n.name,'-',-1,3)-1) as node_name     --03/02/2016
--                        FROM node n
--                       WHERE UPPER (SUBSTR(n.name,1,INSTR(n.name,'-',-1,3)-1)) = UPPER (rec_cur_stg.budget_owner_name) AND
--                             n.is_deleted = 1
--                             ) nv
--                   WHERE UPPER (nv.node_name) = UPPER (rec_cur_stg.budget_owner_name);


               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Node ID';
                     rec_import_record_error.param2 := rec_cur_stg.org_unit_id;

                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_node_id := NULL;
               END;                                   --  check if user exists
            END IF;


            --To check if the node budget is valid     
            IF (v_node_id IS NOT NULL)
            THEN
               BEGIN
                  SELECT n.node_id
                    INTO v_valid_node_id
                    FROM node n
                   WHERE     n.node_id = v_node_id
                         AND EXISTS
                                (SELECT b.node_id
                                   FROM budget b
                                  WHERE     b.budget_segment_id = v_budget_segment_id
                                        AND b.node_id = n.node_id);
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Node ID';
                     rec_import_record_error.param2 := v_node_id;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_node_id := NULL;
               END;                                
            END IF;

            --To check if the node is inactive. Only inactive budgets can be transferred  --02/18/2016
            IF (v_node_id IS NOT NULL)
            THEN
               BEGIN
                SELECT s.node_id INTO v_valid_node_id
                FROM
                (SELECT n.node_id
                FROM node n
                WHERE node_id = rec_cur_stg.org_unit_id AND   --02/18/2016  
                      n.is_deleted = 1 
                UNION
                select n.node_id              
                from budget b,
                     budget_master bm,
                     budget_segment bs,
                     vw_cms_asset_value cms,
                     node n
                where b.budget_segment_id = bs.budget_segment_id
                      and bs.budget_master_id = bm.budget_master_id
                      and b.node_id = n.node_id
                      and n.is_deleted = 0
                      and cms.asset_code = bm.cm_asset_code
                      and cms.key = 'BUDGET_NAME'
                      and cms.locale = 'en_US'      
                      and b.current_value <> 0
                      and bs.budget_segment_id = p_in_budget_segment_id
--                      and UPPER(n.name) = UPPER(rec_cur_stg.budget_owner_name)
                      AND n.node_id = rec_cur_stg.org_unit_id --03/02/2016
                      AND NOT EXISTS (SELECT un.node_id 
                                   FROM user_node un 
                                   WHERE un.node_id = n.node_id AND 
                                         un.status = 1)
                UNION
                SELECT n.node_id
                FROM budget b,
                     budget_master bm,
                     budget_segment bs,
                     vw_cms_asset_value cms,
                     node n,
                     promotion p
                WHERE b.budget_segment_id = bs.budget_segment_id
                      and bs.budget_master_id = bm.budget_master_id
                      and b.node_id = n.node_id
                      and n.is_deleted = 0
                      and cms.asset_code = bm.cm_asset_code
                      and cms.key = 'BUDGET_NAME'
                      and cms.locale = 'en_US'      
                      and b.current_value <> 0
                      and bs.budget_segment_id = p_in_budget_segment_id
--                      and UPPER(n.name) = UPPER(rec_cur_stg.budget_owner_name)
                      AND n.node_id = rec_cur_stg.org_unit_id --03/02/2016
                      AND bm.budget_master_id = p.award_budget_master_id
                      AND p.primary_audience_type <> 'allactivepaxaudience'
                      AND NOT EXISTS (SELECT un.node_id,a.promotion_id,pa.audience_id 
                                            FROM participant_audience pa,
                                                 promo_audience a,
                                                 user_node un 
                                            WHERE un.node_id = n.node_id AND 
                                                  un.status = 1 AND 
                                                  p.promotion_id =  a.promotion_id AND 
                                                  a.audience_id = pa.audience_id AND 
                                                  pa.user_id = un.user_id)
                                                  ) s;                                   
                   EXCEPTION
                      WHEN NO_DATA_FOUND
                      THEN
                         rec_import_record_error.item_key :=
                            'admin.fileload.budgetdistribution.SOURCE_BUDGET_IS_NOT_AN_INACTIVE';
                         rec_import_record_error.param1 :=
                            'Node Name - ' || rec_cur_stg.budget_owner_name;
                         rec_import_record_error.created_by := v_created_by;
                         prc_insert_import_record_error (rec_import_record_error);
                         v_valid_node_id := NULL;
                 END;                                
            END IF;

           ---------------------------------------------------------------------------------------------------------------------------------
 
           --Validating v_transfer_to_owner1 column
            IF LTRIM (RTRIM (rec_cur_stg.transfer_to_owner1)) IS NULL
            THEN
               rec_import_record_error.item_key :=
                  'admin.fileload.errors.MISSING_PROPERTY';
               rec_import_record_error.param1 := 'Transfer To Node Name1';
               rec_import_record_error.created_by := v_created_by;
               prc_insert_import_record_error (rec_import_record_error);
            END IF;

            v_node_id := NULL;
            IF (rec_cur_stg.transfer_to_owner1 IS NOT NULL)
            THEN
               BEGIN
                  SELECT node_id
                    INTO v_node_id
                    FROM node 
                   WHERE UPPER (name) = UPPER (rec_cur_stg.transfer_to_owner1);
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Node Name 1';
                     rec_import_record_error.param2 := rec_cur_stg.transfer_to_owner1;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_node_id := NULL;
               END;                                   --  check if node exists
            END IF;


            v_valid_node_id := NULL;
            IF (v_node_id IS NOT NULL)
            THEN
               BEGIN
                  SELECT n.node_id
                    INTO v_valid_node_id
                    FROM node n
                   WHERE n.node_id = v_node_id
                         AND is_deleted = 0
                         AND EXISTS (SELECT un.node_id   --02/24/2016
                                     FROM user_node un
                                     WHERE un.node_id = n.node_id
                                           AND un.status = 1  
                                           AND (EXISTS
                                                (SELECT pax_aud.user_id
                                                   FROM participant_audience pax_aud,
                                                        promo_audience promo_aud,
                                                        promotion p
                                                  WHERE   pax_aud.user_id = un.user_id and
                                                          pax_aud.audience_id = promo_aud.audience_id and
                                                          promo_aud.promotion_id = p.promotion_id and
                                                          p.award_budget_master_id = v_budget_master_id ) OR 
                                                          EXISTS (SELECT pax.user_id 
                                                                    FROM participant pax,
                                                                         promotion p 
                                                                    WHERE p.award_budget_master_id = v_budget_master_id and
                                                                          p.primary_audience_type = 'allactivepaxaudience' and
                                                                          pax.status = 'active' and 
                                                                          pax.user_id = un.user_id)));
                         
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'system.errors.NODE_DOES_NOT_CONTAIN_ELIGIBLE_SUBMITTER';   --02/24/2016
                     rec_import_record_error.param1 :=
                        'Transfer To Node ID 1 - ' || v_node_id;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_node_id := NULL;
               END;                                --  check if user is Active
            END IF;

            v_node_id := NULL;
            IF (rec_cur_stg.transfer_to_owner2 IS NOT NULL)
            THEN
               BEGIN
                  SELECT node_id
                    INTO v_node_id
                    FROM node 
                   WHERE UPPER (name) = UPPER (rec_cur_stg.transfer_to_owner2);
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Node Name 2';
                     rec_import_record_error.param2 := rec_cur_stg.transfer_to_owner2;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_node_id := NULL;
               END;                                   --  check if node exists
            END IF;


            v_valid_node_id := NULL;
            IF (v_node_id IS NOT NULL)
            THEN
               BEGIN
                  SELECT n.node_id
                    INTO v_valid_node_id
                    FROM node n
                   WHERE n.node_id = v_node_id
                         AND is_deleted = 0
                         AND EXISTS (SELECT un.node_id     --02/24/2016
                                     FROM user_node un
                                     WHERE un.node_id = n.node_id
                                           AND un.status = 1  
                                           AND (EXISTS
                                                (SELECT pax_aud.user_id
                                                   FROM participant_audience pax_aud,
                                                        promo_audience promo_aud,
                                                        promotion p
                                                  WHERE   pax_aud.user_id = un.user_id and
                                                          pax_aud.audience_id = promo_aud.audience_id and
                                                          promo_aud.promotion_id = p.promotion_id and
                                                          p.award_budget_master_id = v_budget_master_id ) OR 
                                                          EXISTS (SELECT pax.user_id 
                                                                    FROM participant pax,
                                                                         promotion p 
                                                                    WHERE p.award_budget_master_id = v_budget_master_id and
                                                                          p.primary_audience_type = 'allactivepaxaudience' and
                                                                          pax.status = 'active' and 
                                                                          pax.user_id = un.user_id)))
                                                                          ;
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'system.errors.NODE_DOES_NOT_CONTAIN_ELIGIBLE_SUBMITTER';    --02/24/2016
                     rec_import_record_error.param1 := rec_cur_stg.transfer_to_owner2;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_node_id := NULL;
               END;                                --  check if user is Active
            END IF;


            v_node_id := NULL;
            IF (rec_cur_stg.transfer_to_owner3 IS NOT NULL)
            THEN
               BEGIN
                  SELECT node_id
                    INTO v_node_id
                    FROM node 
                   WHERE UPPER (name) = UPPER (rec_cur_stg.transfer_to_owner3);
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Node Name 3';
                     rec_import_record_error.param2 := rec_cur_stg.transfer_to_owner3;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_node_id := NULL;
               END;                                   --  check if node exists
            END IF;


            v_valid_node_id := NULL;
            IF (v_node_id IS NOT NULL)
            THEN
               BEGIN
                  SELECT n.node_id
                    INTO v_valid_node_id
                    FROM node n
                   WHERE n.node_id = v_node_id
                         AND is_deleted = 0
                         AND EXISTS (SELECT un.node_id   --02/24/2016
                                     FROM user_node un
                                     WHERE un.node_id = n.node_id
                                           AND un.status = 1  
                                           AND (EXISTS
                                                (SELECT pax_aud.user_id
                                                   FROM participant_audience pax_aud,
                                                        promo_audience promo_aud,
                                                        promotion p
                                                  WHERE   pax_aud.user_id = un.user_id and
                                                          pax_aud.audience_id = promo_aud.audience_id and
                                                          promo_aud.promotion_id = p.promotion_id and
                                                          p.award_budget_master_id = v_budget_master_id ) OR 
                                                          EXISTS (SELECT pax.user_id 
                                                                    FROM participant pax,
                                                                         promotion p 
                                                                    WHERE p.award_budget_master_id = v_budget_master_id and
                                                                          p.primary_audience_type = 'allactivepaxaudience' and
                                                                          pax.status = 'active' and 
                                                                          pax.user_id = un.user_id)))
                                                                          ;
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'system.errors.NODE_DOES_NOT_CONTAIN_ELIGIBLE_SUBMITTER';    --02/24/2016
                     rec_import_record_error.param1 :=
                        'Transfer To Node ID 3 - ' || v_node_id;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_node_id := NULL;
               END;                                --  check if user is Active
            END IF;
           

          END IF; --IF v_budget_type = 'pax' THEN  
          

           IF rec_cur_stg.budget_amount IS NOT NULL  THEN
           
               BEGIN    
               v_valid_budget_amount := TO_NUMBER(LTRIM (RTRIM (rec_cur_stg.budget_amount)));
               
               SELECT b.budget_id,b.current_value
                 INTO v_budget_id,v_budget_current_value   
                    FROM budget b 
                     WHERE b.budget_segment_id = v_budget_segment_id AND
                           ((v_budget_type = 'pax' AND b.user_id = rec_cur_stg.user_id) OR 
                             (v_budget_type = 'node' AND b.node_id = rec_cur_stg.node_id));  
                 
                IF  NVL(v_budget_current_value,0) < v_valid_budget_amount  THEN
                      rec_import_record_error.item_key :=
                         'system.errors.NOT_ENOUGH_BUDGET_AVAILABLE';
                      rec_import_record_error.param1 := rec_cur_stg.budget_amount;
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_budget_amount := NULL;
                      v_budget_id := NULL;
                END IF;


               EXCEPTION
                   WHEN VALUE_ERROR
                   THEN
                      rec_import_record_error.item_key :=
                         'user.characteristic.errors.INTEGER';
                      rec_import_record_error.param1 := 'Budget Amount:'||rec_cur_stg.budget_amount;
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_budget_amount := NULL;
                      v_budget_id := NULL;
                      
                   WHEN OTHERS
                   THEN
                --                      rec_import_record_error.item_key :=
                --                         'admin.fileload.errors.INVALID_PROPERTY';
                --                      rec_import_record_error.param1 := 'Budget Amount:'||rec_cur_stg.budget_amount;
                --                      rec_import_record_error.created_by := v_created_by;
                --                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_budget_amount := NULL;
                      v_budget_id := NULL;

                END;

           ELSE
               rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';   --03/03/2016
               rec_import_record_error.param1 := 'Budget Amount';
               rec_import_record_error.created_by := v_created_by;
               prc_insert_import_record_error (rec_import_record_error);
           END IF; 

            

           IF rec_cur_stg.amount_owner1 IS NOT NULL  THEN
           
               BEGIN    
               v_valid_amount_owner1 := TO_NUMBER(LTRIM (RTRIM (rec_cur_stg.amount_owner1)));

               EXCEPTION
                   WHEN VALUE_ERROR
                   THEN
                      rec_import_record_error.item_key :=
                         'user.characteristic.errors.INTEGER';
                      rec_import_record_error.param1 := 'Amount Owner 1';
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_amount_owner1 := NULL;
                END;
                   
           END IF; 


           IF rec_cur_stg.amount_owner2 IS NOT NULL  THEN
           
               BEGIN    
               v_valid_amount_owner2 := TO_NUMBER(LTRIM (RTRIM (rec_cur_stg.amount_owner2)));

               EXCEPTION
                   WHEN VALUE_ERROR
                   THEN
                      rec_import_record_error.item_key :=
                         'user.characteristic.errors.INTEGER';
                      rec_import_record_error.param1 := 'Amount Owner 2:'||rec_cur_stg.amount_owner2;
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_amount_owner2 := NULL;
                END;
                   
           END IF; 

           IF rec_cur_stg.amount_owner3 IS NOT NULL  THEN
           
               BEGIN    
               v_valid_amount_owner3 := TO_NUMBER(LTRIM (RTRIM (rec_cur_stg.amount_owner3)));

               EXCEPTION
                   WHEN VALUE_ERROR
                   THEN
                      rec_import_record_error.item_key :=
                         'user.characteristic.errors.INTEGER';
                      rec_import_record_error.param1 := 'Amount Owner 3'||rec_cur_stg.amount_owner3;
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_amount_owner3 := NULL;
                END;
                   
           END IF; 

           IF v_valid_budget_amount < ( NVL(v_valid_amount_owner1,0) + NVL(v_valid_amount_owner2,0) + NVL(v_valid_amount_owner3,0) ) THEN --01/25/2016
           
           rec_import_record_error.item_key := 'system.errors.NOT_ENOUGH_BUDGET_AVAILABLE';
           rec_import_record_error.param1 := ( NVL(v_valid_amount_owner1,0) + NVL(v_valid_amount_owner2,0) + NVL(v_valid_amount_owner3,0) );
           rec_import_record_error.created_by := v_created_by;
           prc_insert_import_record_error (rec_import_record_error);

            -- Initialize the import record error values
            rec_import_record_error.item_key := NULL;
            rec_import_record_error.param1 := NULL;
            rec_import_record_error.param2 := NULL;


            IF rec_cur_stg.budget_amount IS NOT NULL THEN                          
               v_count := v_count + 1;
            END IF;
           ELSE
                v_recs_loaded_cnt := v_recs_loaded_cnt + 1;
           END IF; 
           
           v_recs_loaded_cnt := v_recs_loaded_cnt + 1;

            IF MOD (v_count, 10) = 0 
            THEN
               COMMIT;
            END IF;
      
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
               prc_execution_log_entry (
                  v_process_name,
                  v_release_level,
                  'ERROR',
                  'Stage: ' || v_stage || ' --> ' || SQLERRM,
                  NULL);
            COMMIT;

      END;
      
         prc_execution_log_entry (v_process_name,
                            v_release_level,
                            'INFO',
                            'Inside the loop for file ' || v_import_file_id,
                            NULL);

      
    END LOOP; -- *** end of loop to move data to stage_inactive_budget_rd table. ***

    COMMIT;  
   
    SELECT COUNT (DISTINCT (import_record_id))
    INTO v_error_tbl_count
    FROM import_record_error
    WHERE import_file_id = v_import_file_id;

   v_stage := 'Total error count';
   v_error_count := v_error_count + v_error_tbl_count;
 
   --UPDATE import_file f
  --   SET version = nvl(version,0) + 1,
   ----      status = 'verified',
   --      date_verified = SYSDATE,
   --      import_record_count = v_recs_loaded_cnt,
  --       import_record_error_count = v_error_count
  -- WHERE import_file_id = v_import_file_id;
       
   p_processed_records_count := v_recs_loaded_cnt; 
   p_file_records_count := v_error_count ;

   prc_execution_log_entry (v_process_name,
                            v_release_level,
                            'INFO',
                            'Verify Process Completed for file ' || v_import_file_id||', '||' p_processed_records_count: ' || p_processed_records_count||', p_file_records_count:'||p_file_records_count,
                            NULL);

     EXCEPTION
     WHEN OTHERS
     THEN
           prc_execution_log_entry (
              v_process_name,
              v_release_level,
              'ERROR',
              'Stage: ' || v_stage || ' --> ' || SQLERRM,
              NULL);

   
    END; --Begin 

 END IF ; --IF p_in_load_type = 'L' THEN

EXCEPTION
   WHEN e_program_exit THEN             --01/07/2016
     SELECT COUNT (DISTINCT (import_record_id))
         INTO v_error_tbl_count
         FROM import_record_error
        WHERE import_file_id = v_import_file_id;
       
   p_processed_records_count := 0; 
   p_file_records_count := v_error_tbl_count ;

      prc_execution_log_entry (
         v_process_name,
         v_release_level,
         'ERROR',
            'File ID: '
         || p_in_import_file_id
         || ' failed : Selected Budget master name for verify/load differs from staged Budget master name',
         NULL);
      p_out_returncode := 99;
    COMMIT; 
   WHEN OTHERS
   THEN

   v_stage := 'Count distinct records that have an error';

       SELECT COUNT (DISTINCT (import_record_id))
         INTO v_error_tbl_count
         FROM import_record_error
        WHERE import_file_id = v_import_file_id;

       v_stage := 'Total error count';
       v_error_count := v_error_count + v_error_tbl_count;
 
   --   UPDATE import_file f
   --      SET version = nvl(version,0) + 1,
     --        status = 'verify_fail',
    --         import_record_count = v_recs_in_file_cnt,
     --        import_record_error_count = v_error_count
     --  WHERE import_file_id = v_import_file_id;
       
   p_processed_records_count := 0; 
   p_file_records_count := v_error_count ;

      prc_execution_log_entry (
         v_process_name,
         v_release_level,
         'ERROR',
            'File ID: '
         || p_in_import_file_id
         || ' failed at load: '
         || v_stage
         || ' --> '
         || SQLERRM,
         NULL);
      p_out_returncode := 99;
END p_load_inactive_budgets;   

END pkg_inactive_budget_rd;
/
