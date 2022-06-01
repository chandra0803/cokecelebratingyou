CREATE OR REPLACE PACKAGE pkg_claims_submitted_extract
  IS
/*******************************************************************************
-- Purpose: Used to create a data extract.
--          Currently called from within application i.e. Java code to give a result
--          set back. This result set will then be used to create a file in the
--          temp location on the app server and then emailed back to the requestor.
--          This can also be used within PLSQL by  creating a wrapper procedure
--          which will read the result set returned by this procedure and write
--          it into a file.
-- MODIFICATION HISTORY
-- Person      Date        Comments
-- ---------   ----------  -----------------------------------------------------
-- Raju N      09/04/2006  Creation
-- Arun S      05/07/2009  Bug # 30331 suggested fix applied to fnc_get_form_data
                           and prc_get_form_fields
-- chidamba    12/20/2011  Bug # 35717 - Add fnc_cms_asset_code_val_extr to translation report headers content in extract 
                           Added new p_in_locale get locale  e.g- en,fr 
*******************************************************************************/


PROCEDURE prc_get_form_fields
          ( p_in_promotion_id IN number,
            p_out_cfse_id_str OUT VARCHAR2,
            p_out_cfse_name_str OUT VARCHAR2 );
FUNCTION FNC_GET_FORM_DATA( p_in_claim_id IN number,
                            p_in_orderby_str IN VARCHAR2  ) RETURN VARCHAR2 ;
PROCEDURE PRC_RPT_CLAIMSUBMITTED_EXTRACT
   ( p_in_user_id       IN number,
     p_in_promotion_id  IN NUMBER,
     P_in_mediaType     IN VARCHAR2 ,
     p_in_claimStatus IN VARCHAR2 ,
     P_in_participantStatus IN VARCHAR2 ,
     P_in_parentNodeId  IN NUMBER,
     p_in_dept          IN VARCHAR2 ,
     p_in_position      IN VARCHAR2 ,
     p_in_from_date     IN VARCHAR2 , -- 'MM/DD/YYYY'
     p_in_to_date       IN VARCHAR2 , -- 'MM/DD/YYYY'
     p_in_display_form_data IN VARCHAR2,
     p_in_locale        IN  VARCHAR2,  --new parm for BugFix 35717
     p_out_return_code  OUT varchar2,
     p_out_result_set   OUT sys_refcursor);
END; -- Package spec
/

CREATE OR REPLACE PACKAGE BODY pkg_claims_submitted_extract
IS
/*******************************************************************************
-- Purpose: Used to create a data extract.
--          Currently called from within application i.e. Java code to give a result
--          set back. This result set will then be used to create a file in the
--          temp location on the app server and then emailed back to the requestor.
--          This can also be used within PLSQL by  creating a wrapper procedure
--          which will read the result set returned by this procedure and write
--          it into a file.
-- MODIFICATION HISTORY
-- Person      Date        Comments
-- ---------   ----------  -----------------------------------------------------
-- Raju N      09/04/2006  Creation
*******************************************************************************/

PROCEDURE prc_get_form_fields
          ( p_in_promotion_id IN number,
            p_out_cfse_id_str OUT VARCHAR2,
            p_out_cfse_name_str OUT VARCHAR2 ) IS

/*******************************************************************************
-- Purpose: Supporting procedure data extract.
--          referenced by the main procedure PRC_RPT_CLAIMSUBMITTED_EXTRACT
--          to retrieve the values of the form field names and ID's as a
--          concatenated string.
-- MODIFICATION HISTORY
-- Person      Date        Comments
-- ---------   ----------  -----------------------------------------------------
-- Raju N      09/12/2006  Creation 
*******************************************************************************/

  CURSOR c_form_fields ( p_promotion_id IN  NUMBER )IS
    SELECT description ,claim_form_step_element_id
      FROM claim_form_step_element
     WHERE claim_form_step_id IN
           (SELECT claim_form_step_id
              FROM claim_form_step
             WHERE claim_form_id =
                  (SELECT claim_form_ID
                     FROM promotion
                    WHERE promotion_status = 'live'
                      AND promotion_id = p_promotion_id
                   )
          )
    ORDER BY sequence_num ;
   v_cfse_name_str varchar2(3000) ;
   v_cfse_id_str varchar2(3000) ;
   v_loop_cntr number(2) := 0 ;
BEGIN
  FOR i IN c_form_fields (p_in_promotion_id) LOOP
    IF v_loop_cntr = 0 THEN
     v_cfse_name_str := i.description ;
     v_cfse_id_str := i.claim_form_step_element_id ;
    ELSE

     v_cfse_name_str := v_cfse_name_str||','||i.description ;
     v_cfse_id_str := v_cfse_id_str|| i.claim_form_step_element_id ;
    END IF ;
    v_loop_cntr:= v_loop_cntr+1;
  END  LOOP ;
  -- get the comma delimited column headers for the extract
  p_out_cfse_name_str :=  v_cfse_name_str ;
  -- get the concatenated id str to be used in the order by clause
  p_out_cfse_id_str := v_cfse_id_str ;
EXCEPTION
   WHEN OTHERS THEN
    p_out_cfse_name_str := NULL ;
    p_out_cfse_id_str := NULL ;
END  ;

FUNCTION FNC_GET_FORM_DATA( p_in_claim_id IN number,
                            p_in_orderby_str IN VARCHAR2  ) RETURN VARCHAR2 IS
/*******************************************************************************
-- Purpose: Supporting function data extract.
--          referenced by the main procedure PRC_RPT_CLAIMSUBMITTED_EXTRACT
--          to retrieve the data for the form as a comma delimited string.
-- MODIFICATION HISTORY
-- Person      Date        Comments
-- ---------   ----------  -----------------------------------------------------
-- Raju N      09/12/2006  Creation
-- Soma N      12/18/2009  Protect the data with comma Using double quotes for help
--                         from messing up the format.
*******************************************************************************/
CURSOR c_form_field_data ( p_claim_id IN  NUMBER , p_orderby_str IN VARCHAR2)IS
SELECT c.value,claim_form_step_element_id
 FROM claim_cfse c
WHERE claim_id = p_claim_id
ORDER BY instr(p_orderby_str,claim_form_step_element_id);
 v_return_string varchar2(3000) ;
 v_loop_cntr number(2) := 0 ;
 c_delimiter CONSTANT VARCHAR2(1) := '|' ;
BEGIN
  FOR i IN c_form_field_data (p_in_claim_id,p_in_orderby_str) LOOP
  --dbms_output.put_line(substr('Value of v_loop_cntr 1 ='||v_loop_cntr,1,255));
    IF v_loop_cntr = 0 THEN
    
     v_return_string := i.value ;
    ELSE
     v_return_string := v_return_string||c_delimiter||i.value ;
    END IF ;
    v_loop_cntr:=v_loop_cntr+1;
  END  LOOP ;
  RETURN v_return_string ;
END  ;

PROCEDURE PRC_RPT_CLAIMSUBMITTED_EXTRACT
   ( p_in_user_id       IN number,
     p_in_promotion_id  IN NUMBER,
     P_in_mediaType     IN VARCHAR2 ,
     p_in_claimStatus IN VARCHAR2 ,
     P_in_participantStatus IN VARCHAR2 ,
     P_in_parentNodeId  IN NUMBER,
     p_in_dept          IN VARCHAR2 ,
     p_in_position      IN VARCHAR2 ,
     p_in_from_date     IN VARCHAR2 , -- 'MM/DD/YYYY'
     p_in_to_date       IN VARCHAR2 , -- 'MM/DD/YYYY'
     p_in_display_form_data IN VARCHAR2,
     p_in_locale        IN  VARCHAR2,  --new parm for BugFix 35717
     p_out_return_code  OUT varchar2,
     p_out_result_set   OUT sys_refcursor)
   IS
/*******************************************************************************
-- Purpose: Used to create a data extract.
--          Currently called from within application i.e. Java code to give a result
--          set back. This result set will then be used to create a file in the
--          temp location on the app server and then emailed back to the requestor.
--          This can also be used within PLSQL by  creating a wrapper procedure
--          which will read the result set returned by this procedure and write
--          it into a file.
-- MODIFICATION HISTORY
-- Person      Date        Comments
-- ---------   ----------  -----------------------------------------------------
-- Raju N      08/09/2006  Creation
-- Raju N      10/15/2006  Bug#13586
-- Raju N      01/04/2007  Bug # 14582 have he characteristic name as the column
                           label for all the report extracts.
-- Raju N      01/15/2007  Bug # 14934 :
                           Added the function to enclose the string in double quotes.
                           Removed the approver name column as this will be handled later.
                           Fix the common function to return the characteristic values
                           appropriately.
-- Raju N     01/30/2007   Bug#14934 :
                           Removed the award amount column as it is not broken
                           up by product. it more a summary at claim level.
-- Raju N     02/02/2007   Bug#15202: replaced the submitter_user_id by user_name.
-- Arun S     01/09/2009   Bug # 20595 Placed double quotes (") around all export 
--                         field records.Changes for dtl.proxy_user_name to check
--                         for the delimiter with fnc_data_has_delimiter has been 
--                         removed and populated by enclosing with dblquotes(").  
-- Arun S      01/27/2009  Bug # 19713,Move characteristics to the end of the record 
-- Arun S      02/03/2009  Bug # 21220 fix,Added column 'Submitter Transaction Org Unit' 
--                         and word Current to Full Hierarchy Path (Top down to Pax Node)      
--Arun S       03/23/2009  Bug # 21220 fix, Path of last node assigned to the participant was 
                           populated to Submitter Current Full Hierarchy Path.                    
-- Arun S      03/25/2009  Bug 19713,Commented p_in_promotion_id while checking diplay form data   
-- Arun S      05/25/2011  Bug 35693 Fix, Use user_node_id rather than date_created 
--                         to determine latest user node
-- chidamba    12/20/2011  Bug # 35717 - Add fnc_cms_asset_code_val_extr to translation report headers content in extract 
                           Added new p_in_locale get locale  e.g- en,fr                                                
*******************************************************************************/

   -- constants
   c_delimiter CONSTANT VARCHAR2(1) := '|' ;
   c_debug     CONSTANT BOOLEAN := TRUE ;
   -- local variables
   v_cfse_id_str VARCHAR2(300) ;
   v_cfse_name_str VARCHAR2(3000) ;
BEGIN
  IF p_in_display_form_data = 'Y' THEN --OR p_in_promotion_id IS NULL THEN  -- display form data
     PKG_CLAIMS_SUBMITTED_EXTRACT.prc_get_form_fields(p_in_promotion_id,v_cfse_id_str,v_cfse_name_str) ;
     OPEN p_out_result_set FOR
         -- column headers
       SELECT textline FROM (
        SELECT  1,--''||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','CLAIM_NUMBER', p_in_locale)||c_delimiter||              --'Claim Number'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','CLAIM_STATUS', p_in_locale)||c_delimiter||              --'Claim Status'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','DATE_SUBMITTED', p_in_locale)||c_delimiter||            --'Date Submitted'||c_delimiter||
                --'Date Approved'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','FINAL_APPROVER', p_in_locale)||c_delimiter||            --'Final Approver'||c_delimiter||               
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','PROMOTION_NAME', p_in_locale)||c_delimiter||            --'Promotion Name'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','AWARD_AMOUNT', p_in_locale)||c_delimiter||              --'Award Amount'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','PRODUCT', p_in_locale)||c_delimiter||                   --'Product'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','PRODUCT_CATEGORY', p_in_locale)||c_delimiter||          --'Product Category'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','PRODUCT_SUB_CATEGORY', p_in_locale)||c_delimiter||      --'Product sub Category'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','PRODUCT_QUANTITY', p_in_locale)||c_delimiter||          --'Product Quantity'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','PROXY_NAME', p_in_locale)||c_delimiter||                --'Proxy Name'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','SUBMITTER_LAST_NAME', p_in_locale)||c_delimiter||       --'Submitter Last Name'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','SUBMITTER_FIRST_NAME', p_in_locale)||c_delimiter||      --'Submitter First Name'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','SUBMITTER_MIDDLE_NAME', p_in_locale)||c_delimiter||     --'Submitter Middle Name or Initial'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','SUBMITTER_LOGIN_ID', p_in_locale)||c_delimiter||        --'Submitter Login ID'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','JOB_TITLE', p_in_locale)||c_delimiter||                 --'Job Title'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','DEPARTMENT', p_in_locale)||c_delimiter||                --'Department'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','SUBMITTER_TRANS_ORG_UNIT', p_in_locale)||c_delimiter||  --'Submitter Transaction Org Unit'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','CURRENT_FULL_HIERARCHY_PATH', p_in_locale)||c_delimiter||   --'Current Full Hierarchy Path (Top down to Pax Node)'||c_delimiter||
                 v_cfse_name_str||c_delimiter||
                 pkg_report_characteristic.f_get_characteristic_label('USER')||c_delimiter||
                 pkg_report_characteristic.f_get_characteristic_label('NT') textline 
          FROM dual
        UNION
       -- actual data
       SELECT (ROWNUM + 1 ),dtl.claim_number               
             ||c_delimiter||dtl.claim_status               
             ||c_delimiter||dtl.date_submitted             
             --||c_delimiter||NULL --date_approved
             ||c_delimiter||p.approver_name                  --final_aprover
             ||c_delimiter||dtl.promotion_name             
             ||c_delimiter||p.product_award_amount         
             ||c_delimiter||p.product_name                 
             ||c_delimiter||p.product_category_name        
             ||c_delimiter||p.product_subcategory_name     
             ||c_delimiter||p.product_qty                  
             --||c_delimiter||fnc_data_has_delimiter(dtl.proxy_user_name,c_delimiter)  --proxy_name --09/01/2009 Arun, removed check for delimiter 
             ||c_delimiter||dtl.proxy_user_name            
             ||c_delimiter||dtl.submitter_last_name        
             ||c_delimiter||dtl.submitter_first_name       
             ||c_delimiter||dtl.submitter_middle_name      
             ||c_delimiter||(select user_name from application_user where user_id = dtl.submitter_user_id)
             ||c_delimiter||dtl.submitter_job_position     
             ||c_delimiter||dtl.submitter_department       
             ||c_delimiter||dtl.node_name                  
             --||c_delimiter||fnc_get_node_path(dtl.node_id)   -- node id
             ||c_delimiter||fnc_get_node_path 
                                         ((SELECT node_id 
                                             FROM user_node 
                                            WHERE user_id = dtl.submitter_user_id -- Bug fix # 28292 
                                              AND user_node_id =                --date_created        --Bug 35693
                                                    (SELECT MAX(user_node_id)  --MAX(date_created)   --Bug 35693 
                                                       FROM user_node 
                                                      WHERE user_id = dtl.submitter_user_id))) --Arun S 03/24/2009, Path of last node assigned to the participant    
             ||c_delimiter||pkg_claims_submitted_extract.FNC_GET_FORM_DATA(dtl.claim_id,v_cfse_id_str)  
             ||c_delimiter||pkg_report_characteristic.f_get_characteristic('USER',dtl.submitter_user_id)
             ||c_delimiter||pkg_report_characteristic.f_get_characteristic('NT',dtl.node_id)  -- node char
        FROM rpt_claim_detail dtl,
             rpt_claim_product p
       WHERE dtl.claim_id = p.claim_id
         AND dtl.claim_status = nvl(p_in_claimStatus,dtl.claim_status)
         AND dtl.award_type = nvl(P_in_mediaType,dtl.award_type)
         AND dtl.promotion_id = nvl(to_number(p_in_promotion_id),dtl.promotion_id)
         AND nvl(dtl.date_submitted,trunc(sysdate)) between fnc_locale_to_date_dt(p_in_from_date,p_in_locale)
                                                        and fnc_locale_to_date_dt(p_in_to_date,p_in_locale)
         AND dtl.submitter_pax_status = NVL(P_in_participantStatus, dtl.submitter_pax_status)
         AND dtl.submitter_job_position = NVL(p_in_position,dtl.submitter_job_position)
         AND dtl.submitter_department = NVL(p_in_dept,dtl.submitter_department)
         AND dtl.submitter_user_id = NVL(p_in_user_id,dtl.submitter_user_id )
         AND dtl.node_id IN
              (SELECT node_id FROM rpt_hierarchy
              CONNECT BY PRIOR node_id = parent_node_id
                START WITH node_id = nvl(P_in_parentNodeId,0) )
        ORDER BY 1) ;        


  ELSE
     OPEN p_out_result_set FOR
       -- column headers
       SELECT textline FROM (
        SELECT  1,--''||c_delimiter||                
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','CLAIM_NUMBER', p_in_locale)||c_delimiter||              --'Claim Number'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','CLAIM_STATUS', p_in_locale)||c_delimiter||              --'Claim Status'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','DATE_SUBMITTED', p_in_locale)||c_delimiter||            --'Date Submitted'||c_delimiter||
                --'Date Approved'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','FINAL_APPROVER', p_in_locale)||c_delimiter||            --'Final Approver'||c_delimiter||               
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','PROMOTION_NAME', p_in_locale)||c_delimiter||            --'Promotion Name'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','AWARD_AMOUNT', p_in_locale)||c_delimiter||              --'Award Amount'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','PRODUCT', p_in_locale)||c_delimiter||                   --'Product'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','PRODUCT_CATEGORY', p_in_locale)||c_delimiter||          --'Product Category'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','PRODUCT_SUB_CATEGORY', p_in_locale)||c_delimiter||      --'Product sub Category'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','PRODUCT_QUANTITY', p_in_locale)||c_delimiter||          --'Product Quantity'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','PROXY_NAME', p_in_locale)||c_delimiter||                --'Proxy Name'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','SUBMITTER_LAST_NAME', p_in_locale)||c_delimiter||       --'Submitter Last Name'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','SUBMITTER_FIRST_NAME', p_in_locale)||c_delimiter||      --'Submitter First Name'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','SUBMITTER_MIDDLE_NAME', p_in_locale)||c_delimiter||     --'Submitter Middle Name or Initial'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','SUBMITTER_LOGIN_ID', p_in_locale)||c_delimiter||        --'Submitter Login ID'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','JOB_TITLE', p_in_locale)||c_delimiter||                 --'Job Title'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','DEPARTMENT', p_in_locale)||c_delimiter||                --'Department'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','SUBMITTER_TRANS_ORG_UNIT', p_in_locale)||c_delimiter||  --'Submitter Transaction Org Unit'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.claimssubmitted.detail','CURRENT_FULL_HIERARCHY_PATH', p_in_locale)||c_delimiter||   --'Current Full Hierarchy Path (Top down to Pax Node)'||c_delimiter||                
                 pkg_report_characteristic.f_get_characteristic_label('USER')||c_delimiter||
                 pkg_report_characteristic.f_get_characteristic_label('NT')  textline
          FROM dual
        UNION
       -- actual data
       SELECT (ROWNUM + 1), dtl.claim_number               
             ||c_delimiter||dtl.claim_status               
             ||c_delimiter||dtl.date_submitted             
             --||c_delimiter||NULL --date_approved
             ||c_delimiter||p.approver_name                     --final_aprover
             ||c_delimiter||dtl.promotion_name             
             ||c_delimiter||p.product_award_amount             
             ||c_delimiter||p.product_name                 
             ||c_delimiter||p.product_category_name        
             ||c_delimiter||p.product_subcategory_name     
             ||c_delimiter||p.product_qty                  
             --||c_delimiter||fnc_data_has_delimiter(dtl.proxy_user_name,c_delimiter) --proxy_name  --09/01/2009 Arun, removed check for delimiter 
             ||c_delimiter||dtl.proxy_user_name            
             ||c_delimiter||dtl.submitter_last_name           
             ||c_delimiter||dtl.submitter_first_name       
             ||c_delimiter||dtl.submitter_middle_name      
             ||c_delimiter||(select user_name from application_user where user_id = dtl.submitter_user_id) 
             ||c_delimiter||dtl.submitter_job_position     
             ||c_delimiter||dtl.submitter_department       
             ||c_delimiter||dtl.node_name                               
             --||c_delimiter||fnc_get_node_path(dtl.node_id)   -- node id
             ||c_delimiter||fnc_get_node_path
                                         ((SELECT node_id 
                                            FROM user_node 
                                           WHERE user_id = dtl.submitter_user_id -- Bug fix # 28292 
                                             AND user_node_id = (SELECT MAX(user_node_id)   --date_created --MAX(date_created)  --05/25/2011
                                                                   FROM user_node 
                                                                  WHERE user_id = dtl.submitter_user_id )))   --Arun S 03/24/2009, Path of last node assigned to the participant    
             ||c_delimiter||pkg_report_characteristic.f_get_characteristic('USER',dtl.submitter_user_id)
             ||c_delimiter||pkg_report_characteristic.f_get_characteristic('NT',dtl.node_id)  -- node char
        FROM rpt_claim_detail dtl,
             rpt_claim_product p
       WHERE dtl.claim_id = p.claim_id
         AND dtl.claim_status = nvl(p_in_claimStatus,dtl.claim_status)
         AND dtl.award_type = nvl(P_in_mediaType,dtl.award_type)
         AND dtl.promotion_id = nvl(to_number(p_in_promotion_id),dtl.promotion_id)
         AND nvl(dtl.date_submitted,trunc(sysdate)) between fnc_locale_to_date_dt(p_in_from_date,p_in_locale)
                                                        and fnc_locale_to_date_dt(p_in_to_date,p_in_locale)
         AND dtl.submitter_pax_status = NVL(P_in_participantStatus, dtl.submitter_pax_status)
         AND dtl.submitter_job_position = NVL(p_in_position,dtl.submitter_job_position)
         AND dtl.submitter_department = NVL(p_in_dept,dtl.submitter_department)
         AND dtl.submitter_user_id = NVL(p_in_user_id,dtl.submitter_user_id )
         AND dtl.node_id IN
              (SELECT node_id FROM rpt_hierarchy
              CONNECT BY PRIOR node_id = parent_node_id
                START WITH node_id = nvl(P_in_parentNodeId,0) )
       ORDER BY 1 ) ;
  END IF ; -- display form data
    p_out_return_code :=  '00' ;
  EXCEPTION
      WHEN OTHERS  THEN
          p_out_return_code :=  '99' ;
          OPEN p_out_result_set FOR SELECT NULL FROM dual  ;
  END; -- Procedure
END;
/
