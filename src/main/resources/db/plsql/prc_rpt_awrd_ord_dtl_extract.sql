CREATE OR REPLACE PROCEDURE PRC_RPT_AWRD_ORD_DTL_EXTRACT
   ( p_in_promotion_name IN  varchar2,
     p_in_country_name  IN  varchar2,
     p_in_locale        IN  varchar2,  -- 12/20/2011 added for Bug # 35717
     p_out_return_code  OUT varchar2,
     p_out_result_set   OUT sys_refcursor)
   IS
/*******************************************************************************
-- Purpose: 
-- This procedure returns the result set which will be used by Java to send the extracted
-- detail from RPT_AWARD_ORDER_DETAIL table
-- InPut Parameters:
-- Promotion_name: NULL if user wants to see all promotions
-- Country_code:   NULL if user wants to see all country data
-- Person      Date        Comments
-- ---------   ----------  -----------------------------------------------------
-- S. Majumder 04/25/2008  Initial Version  
-- Arun S      12/01/2009  Bug # 20595 fix,Placed double quotes (") around all export 
--                         field records   
-- nagarajs    12/20/2011  Bug # 35717 - Add fnc_cms_asset_code_val_extr to 
                           translation of report headers content in extract. 
                           Added new p_in_locale get locale  e.g- en,fr    
*******************************************************************************/

   -- constants
   c_delimiter CONSTANT VARCHAR2(1) := '|' ;
   -- local variables
BEGIN
OPEN p_out_result_set FOR
     -- column headers
     SELECT textline FROM (
        SELECT 1,fnc_cms_asset_code_val_extr('extract.awardorder.detail','REFERENCE_NUMBER', p_in_locale)||c_delimiter||    --'Reference Number'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','AWARD_LEVEL', p_in_locale)||c_delimiter||         --'Award Level'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','DATE_ISSUED', p_in_locale)||c_delimiter||         --'Date Issued'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','DATE_REDEEMED', p_in_locale)||c_delimiter||       --'Date Redeemed'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','DATE_CANCELLED', p_in_locale)||c_delimiter||      --'Date Cancelled'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','ORDER_NUMBER', p_in_locale)||c_delimiter||        --'Order Number'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','ITEM_DESCRIPTION', p_in_locale)||c_delimiter||    --'Item Description'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','LOGIN_ID', p_in_locale)||c_delimiter||            --'Login ID'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','FIRST_NAME', p_in_locale)||c_delimiter||          --'First Name (database)'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','MIDDLE_INITIAL', p_in_locale)||c_delimiter||      --'Middle Initial (database)'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','LAST_NAME', p_in_locale)||c_delimiter||           --'Last Name (database)'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','ADDRESS_1', p_in_locale)||c_delimiter||           --'Address 1 (database)'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','ADDRESS_2', p_in_locale)||c_delimiter||           --'Address 2 (database)'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','CITY', p_in_locale)||c_delimiter||                --'City (database)'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','STATE', p_in_locale)||c_delimiter||               --'State (database)'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','POSTAL_CODE', p_in_locale)||c_delimiter||         --'Postal Code (database)'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','HIRE_DATE', p_in_locale)||c_delimiter||           --'Hire Date'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','SHIP_TO_NAME', p_in_locale)||c_delimiter||        --'Ship To Name'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','SHIP_TO_ADDRESS1', p_in_locale)||c_delimiter||    --'Ship To Address1'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','SHIP_TO_ADDRESS2', p_in_locale)||c_delimiter||    --'Ship To Address2'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','SHIP_TO_CITY', p_in_locale)||c_delimiter||        --'Ship To City'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','SHIP_TO_STATE', p_in_locale)||c_delimiter||       --'Ship To State'||c_delimiter||
                 fnc_cms_asset_code_val_extr('extract.awardorder.detail','SHIP_TO_POSTAL_CODE', p_in_locale) textline       --'Ship To Postal Code' textline
        FROM dual
      UNION
       SELECT (rownum+1),REFERENCE_NUMBER||c_delimiter||
               AWARD_LEVEL         ||c_delimiter|| 
               DATE_ISSUED         ||c_delimiter||
               DATE_REDEEMED       ||c_delimiter|| 
               DATE_CANCELLED      ||c_delimiter|| 
               ORDER_NUMBER        ||c_delimiter|| 
               ITEM_DESCRIPTION    ||c_delimiter||  
               EMPLOYEE_ID         ||c_delimiter|| 
               FIRST_NAME          ||c_delimiter||
               MIDDLE_NAME         ||c_delimiter||
               LAST_NAME           ||c_delimiter||  
               ADDRESS_1           ||c_delimiter||
               ADDRESS_2           ||c_delimiter||
               CITY                ||c_delimiter||
               STATE               ||c_delimiter||
               POSTAL_CODE         ||c_delimiter||
               HIRE_DATE           ||c_delimiter||
               SHIP_TO_CNTCT_NAME1 ||c_delimiter||
               SHIP_TO_ADDRESS_1   ||c_delimiter||
               SHIP_TO_ADDRESS_2   ||c_delimiter||
               SHIP_TO_CITY        ||c_delimiter||
               SHIP_TO_STATE       ||c_delimiter||
               SHIP_TO_POSTAL_CODE 
       FROM RPT_AWARD_ORDER_DETAIL
       WHERE promotion_name = nvl(p_in_promotion_name, promotion_name)
           AND  nvl(country_code,' ') = nvl(p_in_country_name, nvl(country_code,' ') ) 
       ORDER BY 1) ;
  p_out_return_code :=  '00' ;
EXCEPTION
    WHEN OTHERS  THEN
        p_out_return_code :=  '99' ;
        OPEN p_out_result_set FOR SELECT NULL FROM dual  ;
END PRC_RPT_AWRD_ORD_DTL_EXTRACT; 
/

