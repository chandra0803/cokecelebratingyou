CREATE OR REPLACE 
PROCEDURE PRC_RPT_BUDGET_DTL_EXTRACT
   ( p_in_budget_status IN  varchar2,
     p_in_parentNodeId  IN  number, -----------new parm for BugFix 20696
     p_in_locale        IN  varchar2,  -- 12/20/2011 added for Bug # 35717
     p_in_ratio         IN  NUMBER,            --New param for Bug 42071 Fix
     p_out_return_code  OUT varchar2,
     p_out_result_set   OUT sys_refcursor)
   IS
/*******************************************************************************
-- Purpose: 
-- MODIFICATION HISTORY
-- Person      Date        Comments
-- ---------   ----------  -----------------------------------------------------
-- MLindvig   10/29/2007  Creation   
-- chidamba   12/20/2011  Bug # 35717 - Add fnc_cms_asset_code_val_extr to translation report headers content in extract 
--                        Added new p_in_locale get locale  e.g- en,fr                 
-- chidamba   03/08/2012  Bug # 39795 - Remove references to 'node' in report extracts and replace as 'Org Unit'           
-- Arun S     07/06/2012  Bug 42071 Fix. i) Added new in parameter p_in_ratio.
--                        ii) Total Budget, Budget Used, Budget Remaining and  
--                        % Utilized calcualted based on input media ratio (p_in_ratio)
--Ravi Dhanekula 10/11/2012 added new column reallocaton_amoubnt to the extract.
*******************************************************************************/

   -- constants
   c_delimiter CONSTANT VARCHAR2(1) := '|' ;
   -- local variables
   v_in_ratio           NUMBER;
      
BEGIN
  
  v_in_ratio := NVL(p_in_ratio, 1);     --Bug 42071 Fix
  
OPEN p_out_result_set FOR
     -- column headers
     SELECT textline FROM (
        SELECT 1,fnc_cms_asset_code_val_extr('extract.budget.detail','BUDGET_MASTER_NAME', p_in_locale)||c_delimiter||    --'Budget Master Name'||c_delimiter||     
              fnc_cms_asset_code_val_extr('extract.budget.detail','ACTIVE', p_in_locale)||c_delimiter||                   --'Active'||c_delimiter||
              fnc_cms_asset_code_val_extr('extract.budget.detail','TYPE', p_in_locale)||c_delimiter||                     --'Type'||c_delimiter||
              fnc_cms_asset_code_val_extr('extract.budget.detail','AVAILABLE_PROMOTIONS', p_in_locale)||c_delimiter||     --'Available Promotions'||c_delimiter||
              fnc_cms_asset_code_val_extr('extract.budget.detail','CAP_TYPE', p_in_locale)||c_delimiter||                 --'Cap Type'||c_delimiter||
              fnc_cms_asset_code_val_extr('extract.budget.detail','START_DATE', p_in_locale)||c_delimiter||               --'Start Date'||c_delimiter||
              fnc_cms_asset_code_val_extr('extract.budget.detail','END_DATE', p_in_locale)||c_delimiter||                 --'End Date'||c_delimiter||
              fnc_cms_asset_code_val_extr('extract.budget.detail','RULE_FOR_FINAL_PAYOUT', p_in_locale)||c_delimiter||    --'Rule for Final Payout'||c_delimiter||
              fnc_cms_asset_code_val_extr('extract.budget.detail','BUDGET_OWNER', p_in_locale)||c_delimiter||             --'Budget Owner'||c_delimiter||
              fnc_cms_asset_code_val_extr('extract.budget.detail','ORG_UNIT_OWNER', p_in_locale)||c_delimiter||    --03/08/2012           --'Node Owner'||c_delimiter||
              fnc_cms_asset_code_val_extr('extract.budget.detail','TOTAL_BUDGET', p_in_locale)||c_delimiter||             --'Total Budget'||c_delimiter||
              fnc_cms_asset_code_val_extr('extract.budget.detail','TOTAL_BUDGET_USED', p_in_locale)||c_delimiter||        --'Total Budget Used'||c_delimiter||
              fnc_cms_asset_code_val_extr('extract.budget.detail','REALLOCATED_BUDGET', p_in_locale)||c_delimiter||           
              fnc_cms_asset_code_val_extr('extract.budget.detail','BUDGET_REMAINING', p_in_locale)||c_delimiter||         --'Budget Remaining'||c_delimiter||
              fnc_cms_asset_code_val_extr('extract.budget.detail','BUDGET_UTILIZED', p_in_locale) textline                --'% of Budget Utilized' 
        FROM dual
      UNION
       SELECT (rownum+1),budget_master_name||c_delimiter|| 
              decode(is_active, 1, 'Yes', 0, 'No', null)||c_delimiter|| 
              budget_type||c_delimiter|| 
              promotion_string||c_delimiter||
              overrideable||c_delimiter||
              start_date||c_delimiter|| 
              end_date||c_delimiter||
              rule_for_final_payout||c_delimiter||
              budget_owner_name||c_delimiter||
              node_owner||c_delimiter||
              --Bug 42071 Fix start 
              --cap_amount||c_delimiter||
              --budget_used||c_delimiter||
              --(cap_amount - budget_used)||c_delimiter||
              --to_char(decode ( Nvl(cap_amount,0),0,0, (Nvl(budget_used,0) / Nvl(cap_amount,0) ) ) * 100,'999') || '%'
              (cap_amount * v_in_ratio)||c_delimiter||
              (budget_used * v_in_ratio)||c_delimiter||
               (reallocated_amount * v_in_ratio)||c_delimiter||--10/11/2012 
              ((cap_amount * v_in_ratio) - (budget_used * v_in_ratio))||c_delimiter||
              to_char(decode ( Nvl((cap_amount * v_in_ratio),0),0,0, (Nvl((budget_used * v_in_ratio),0) / Nvl((cap_amount * v_in_ratio),0) ) ) * 100,'999') || '%'
              --Bug 42071 Fix end
       FROM rpt_budget_usage_detail
       WHERE budget_status = nvl(p_in_budget_status, budget_status)  AND budget_master_id = 
       (SELECT DISTINCT award_budget_master_id  FROM promotion  WHERE promotion_status = 'live' AND award_budget_master_id = budget_master_id) 
       AND   (node_id IS NULL or NVL(node_id,0)  IN (SELECT node_id FROM 
       rpt_hierarchy   CONNECT BY PRIOR node_id = parent_node_id  START WITH node_id = NVL(p_in_parentNodeId,0))) -- BugFix 20696 Display the node budgets of current and child nodes
       ORDER BY 1 );
  p_out_return_code :=  '00' ;
EXCEPTION
    WHEN OTHERS  THEN
        p_out_return_code :=  '99' ;
        OPEN p_out_result_set FOR SELECT NULL FROM dual  ;
END PRC_RPT_BUDGET_DTL_EXTRACT;
/