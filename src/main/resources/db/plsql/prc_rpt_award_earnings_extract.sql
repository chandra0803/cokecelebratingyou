CREATE OR REPLACE PROCEDURE PRC_RPT_AWARD_EARNINGS_EXTRACT ( p_in_promotion_id IN  NUMBER,
                                                            p_in_country_id  IN  NUMBER,
                                                            p_in_start_date IN VARCHAR2,
                                                            p_in_end_date IN VARCHAR2,
  															p_in_locale        IN  VARCHAR2,  --new parm for BugFix 35717
  															p_out_return_code  OUT VARCHAR2,
                                                            p_out_result_set   OUT sys_refcursor) IS
/*******************************************************************************
-- Purpose: 
-- MODIFICATION HISTORY
-- Person      Date        Comments
-- ---------   ----------  -----------------------------------------------------
-- MLindvig   07/24/2008  Creation   
-- Arun S     01/13/2009  Bug # 20595 fix,Placed double quotes (") around all export 
--                        field records  
-- Arun S     01/29/2009  Bug # 21432,Made to display ‘0’ prefix to the reference number in csv file 
-- chidamba   12/20/2011  Bug # 35717 - Add fnc_cms_asset_code_val_extr to translation report headers content in extract 
--                        Added new p_in_locale get locale  e.g- en,fr
*******************************************************************************/

   -- constants
   c_delimiter CONSTANT VARCHAR2(1) := '|' ;

BEGIN
         prc_execution_log_entry ('P_AWARD_ITEM_ACTIVITY',
                                  1,
                                  'INFO',
                                  p_in_promotion_id || c_delimiter || p_in_country_id || c_delimiter || p_in_start_date || c_delimiter || p_in_end_date,
                                  NULL
                                 );

OPEN p_out_result_set FOR
SELECT textline FROM (SELECT 1,
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','LAST_NAME', p_in_locale)||c_delimiter||           --'Last Name'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','FIRST_NAME', p_in_locale)||c_delimiter||          --'First Name'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','AWARD_LEVEL', p_in_locale)||c_delimiter||         --'Award Level'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','REFERENCE_NUMBER', p_in_locale)||c_delimiter||    --'Reference Number'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','PROGRAM_NUMBER', p_in_locale)||c_delimiter||      --'Program Number'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','MAX_LEVEL_VALUE', p_in_locale)||c_delimiter||     --'Max Level Value'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','ITEM_VALUE', p_in_locale)||c_delimiter||          --'Item Value'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','DATE_REDEEMED', p_in_locale)||c_delimiter||       --'Date Redeemed'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','DATE_ISSUED', p_in_locale)||c_delimiter||         --'Date Issued'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','ITEM_REDEEMED', p_in_locale)||c_delimiter||       --'Item Redeemed'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','EMAIL_ADDRESS', p_in_locale)||c_delimiter||       --'Email Address'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','SSN', p_in_locale)||c_delimiter||                 --'SSN'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','ADDRESS_1', p_in_locale)||c_delimiter||           --'Address 1'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','ADDRESS_2', p_in_locale)||c_delimiter||           --'Address 2'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','ADDRESS_3', p_in_locale)||c_delimiter||           --'Address 3'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','CITY', p_in_locale)||c_delimiter||                --'City'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','STATE', p_in_locale)||c_delimiter||               --'State'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','ZIP', p_in_locale)||c_delimiter||                 --'Zip'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','COUNTRY', p_in_locale)||c_delimiter||             --'Country'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','LOGIN_ID', p_in_locale)||c_delimiter||            --'Login ID'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','CHARACTERISTIC_1', p_in_locale)||c_delimiter||    --'Characteristic 1'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','CHARACTERISTIC_2', p_in_locale)||c_delimiter||    --'Characteristic 2'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','CHARACTERISTIC_3', p_in_locale)||c_delimiter||    --'Characteristic 3'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','CHARACTERISTIC_4', p_in_locale)||c_delimiter||    --'Characteristic 4'||c_delimiter||
                      fnc_cms_asset_code_val_extr('extract.awardearnings.detail','CHARACTERISTIC_5', p_in_locale)     textline      --'Characteristic 5' 
                      FROM DUAL
                     UNION
                     SELECT (rownum+1),
                      LAST_NAME            ||c_delimiter||
                      FIRST_NAME           ||c_delimiter||
                      AWARD_LEVEL          ||c_delimiter||
                      REFERENCE_NUMBER     ||c_delimiter||
                      PROGRAM_NUMBER       ||c_delimiter||
                      MAX_VALUE_LEVEL      ||c_delimiter||
                      ITEM_VALUE           ||c_delimiter||
                      DATE_REDEEMED        ||c_delimiter||
                      DATE_ISSUED          ||c_delimiter||
                      ITEM_REDEEMED        ||c_delimiter||
                      EMAIL_ADDRESS        ||c_delimiter||
                      SSN                  ||c_delimiter||
                      ADDR1                ||c_delimiter||
                      ADDR2                ||c_delimiter||
                      ADDR3                ||c_delimiter||
                      CITY                 ||c_delimiter||
                      STATE                ||c_delimiter||
                      ZIP                  ||c_delimiter||
                      COUNTRY              ||c_delimiter||
                      LOGIN_ID             ||c_delimiter||
                      CHARACTERISTIC_VALUE1||c_delimiter||
                      CHARACTERISTIC_VALUE2||c_delimiter||
                      CHARACTERISTIC_VALUE3||c_delimiter||
                      CHARACTERISTIC_VALUE4||c_delimiter||
                      CHARACTERISTIC_VALUE5
                      FROM RPT_AWARD_EARNINGS r
                      WHERE nvl(TRUNC(r.DATE_REDEEMED),TRUNC(SYSDATE))
                               BETWEEN fnc_locale_to_date_dt(p_in_start_date,p_in_locale) AND fnc_locale_to_date_dt(p_in_end_date,p_in_locale)
                        AND r.COUNTRY_ID = NVL(p_in_country_id,r.COUNTRY_ID)
                        AND r.PROMOTION_ID = NVL(p_in_promotion_id, r.PROMOTION_ID));
                        
                        
      p_out_return_code :=  '00' ;

EXCEPTION
    WHEN OTHERS  THEN
        p_out_return_code :=  '99' ;
        OPEN p_out_result_set FOR SELECT NULL FROM dual;
END; 
/

