CREATE OR REPLACE PROCEDURE PRC_RPT_ENROLLMENT_EXTRACT
   ( P_in_parentNodeId  IN NUMBER,
     p_in_from_date     IN VARCHAR2 ,
     p_in_to_date       IN VARCHAR2  ,
     P_in_participantStatus IN VARCHAR2 ,
     p_in_position      IN VARCHAR2 ,
     p_in_dept          IN VARCHAR2 ,
     p_in_locale       IN  VARCHAR2,  --new parm for BugFix 35717
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
-- Raju N      08/08/2006  Creation
-- Raju N      10/15/2006  Bug#13586
-- Raju N      01/04/2007  Bug # 14582 have he characteristic name as the column
                           label for all the report extracts.
-- Arun S      09/01/2009  Bug 20595 Placed double quotes (") around all export 
--                         field records 
-- Arun S      01/27/2009  Bug # 19713,Move characteristics to the end of the record
-- Arun S      05/11/2010  Bug # 32555 Fix, International address changes 
                           Removed Address4, Address5, Address6 from report extract                                                                                 
-- J Flees     05/18/2011  Bug # 34143, Modify the enrollment report extract.
                           Add user node role and name to report.
-- J Flees     06/08/2011  Bug # 34143, Modify the enrollment report extract.
                           Add employer and login ID to report.
-- J Flees     08/22/2011  Bug # 34143, Modify the enrollment report extract.
                           Return state abbreviation rather than name.
-- J Flees     08/31/2011  Report Refresh Performance.
                           Join select to CMS view rather than execute CMS function per record selected.
-- chidamba    12/20/2011  Bug # 35717 - Add fnc_cms_asset_code_val_extr to translation report headers content in extract 
--                         Added new p_in_locale get locale  e.g- en,fr
*******************************************************************************/

   -- constants
   c_delimiter CONSTANT VARCHAR2(1) := '|' ;
   c_debug     CONSTANT BOOLEAN := TRUE ;
   -- local variables
BEGIN
OPEN p_out_result_set FOR
     -- column headers
       SELECT textline FROM (
         SELECT 1,fnc_cms_asset_code_val_extr('extract.enrollment.detail','EMPLOYER', p_in_locale)||c_delimiter||                 --'Employer'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','LOGIN_ID', p_in_locale)||c_delimiter||                   --'Login ID'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','HIERARCHY_ROLE', p_in_locale)||c_delimiter||             --'Hierarchy Role'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','HIERARCHY_ROLE_NAME', p_in_locale)||c_delimiter||            --'Hierarchy Role Name'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','LAST_NAME', p_in_locale)||c_delimiter||                  --'LAST Name'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','FIRST_NAME', p_in_locale)||c_delimiter||                 --'First Name'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','MIDDLE_NAME', p_in_locale)||c_delimiter||                --'MiddleName'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','TITLE', p_in_locale)||c_delimiter||                      --'Title'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','SUFFIX', p_in_locale)||c_delimiter||                     --'Suffix'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','DATE_OF_BARTH', p_in_locale)||c_delimiter||              --'Date of Birth'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','GENDER', p_in_locale)||c_delimiter||                     --'Gender'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','ENROLLMENT_DATE', p_in_locale)||c_delimiter||            --'Enrollment Date'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','ENROLLMENT_SOURCE', p_in_locale)||c_delimiter||          --'Enrollment Source'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','PARTICIPANT_STATUS', p_in_locale)||c_delimiter||         --'Participant Status'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','JOB_TITLE', p_in_locale)||c_delimiter||                  --'Job Title'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','DEPARTMENT', p_in_locale)||c_delimiter||                 --'Department'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','HIERARCHY_PATH', p_in_locale)||c_delimiter||             --'Hierarchy Path'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','PRIMARY_ADD_TYPE', p_in_locale)||c_delimiter||           --'Primary Address Type'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','COUNTRY', p_in_locale)||c_delimiter||                    --'Country'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','ADDRESS_1', p_in_locale)||c_delimiter||                  --'Address1'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','ADDRESS_2', p_in_locale)||c_delimiter||                  --'Address2'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','ADDRESS_3', p_in_locale)||c_delimiter||                  --'Address3'||c_delimiter||
                --'Address4'||c_delimiter||
                --'Address5'||c_delimiter||
                --'Address6'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','CITY', p_in_locale)||c_delimiter||                       --'City'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','STATE_PROVINCE', p_in_locale)||c_delimiter||             --'State/Province'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','ZIP_CODE', p_in_locale)||c_delimiter||                   --'Zip/Postal Code'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','PRIMARY_TELEPHONE_TYPE', p_in_locale)||c_delimiter||     --'Primary Telephone Type'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','PRIMARY_TELEPHONE_NO', p_in_locale)||c_delimiter||       --'Primary Telephone Number'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','PRIMARY_EMAIL_ADD_TYPE', p_in_locale)||c_delimiter||     --'Primary Email address Type'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','PRIMARY_EMAIL_ADDRESS', p_in_locale)||c_delimiter||      --'Primary Email Address'||c_delimiter||
                fnc_cms_asset_code_val_extr('extract.enrollment.detail','LANGUAGE_PREFERENCE', p_in_locale)||c_delimiter||        --'Language Preference'||c_delimiter||
                --'Allow Contact by list' 
                pkg_report_characteristic.f_get_characteristic_label('USER')||c_delimiter||
                pkg_report_characteristic.f_get_characteristic_label('NT')    textline
        FROM dual
      UNION
     -- actual data
     SELECT (rownum+1),   emp.name                  
           ||c_delimiter||au.user_name              
           ||c_delimiter||un.role                   
           ||c_delimiter||n.name                    
           ||c_delimiter||e.last_name               
           ||c_delimiter||e.first_name              
           ||c_delimiter||e.middle_init             
           ||c_delimiter||e.title                   
           ||c_delimiter||e.suffix                  
           ||c_delimiter||e.birth_date              
           ||c_delimiter||e.gender                  
           ||c_delimiter||e.enrollment_date         
           ||c_delimiter||e.enrollment_source       
           ||c_delimiter||e.status                  
           ||c_delimiter||e.job_position            
           ||c_delimiter||e.department              
           ||c_delimiter||fnc_get_node_path(e.node_id) 
           ||c_delimiter||e.primary_address_type    
--           ||c_delimiter||fnc_cms_state_code_value('picklist.countrytype.items',c.country_code)   -- 08/31/2011
           ||c_delimiter||ccv_c.cms_name                                                            -- 08/31/2011
           ||c_delimiter||e.address1                
           ||c_delimiter||e.address2                
           ||c_delimiter||e.address3                
           --||c_delimiter||e.address4                
           --||c_delimiter||e.address5                
           --||c_delimiter||e.address6                
           ||c_delimiter||e.city                    
--           ||c_delimiter||fnc_cms_state_code_value('picklist.statetype.items',e.state, 'ABBR')    -- 08/22/2011
           ||c_delimiter||ccv_s.cms_abbr                                                            -- 08/31/2011
           ||c_delimiter||e.postal_code             
           ||c_delimiter||e.primary_phone_type      
           ||c_delimiter||e.primary_phone_number    
           ||c_delimiter||e.primary_email_addr_type 
           ||c_delimiter||e.primary_email_address      
           ||c_delimiter||e.language_preference     
           ||c_delimiter||pkg_report_characteristic.f_get_characteristic('USER',e.user_id)
           ||c_delimiter||pkg_report_characteristic.f_get_characteristic('NT',e.node_id)
       FROM rpt_enrollment_detail e,
            country c,
            user_node un,
            node n,
            participant_employer pe,
            employer emp,
            application_user au,
            vw_cms_code_value ccv_c,   -- country  08/31/2011
            vw_cms_code_value ccv_s    -- state    08/31/2011
      WHERE e.status= nvl(p_in_participantstatus,e.status)
        AND e.job_position = nvl(p_in_position,e.job_position)
        AND e.department = nvl(p_in_dept,e.department)
        AND c.country_id = e.country_id
        AND 'picklist.countrytype.items' = ccv_c.asset_code (+)   -- 08/31/2011
        AND c.country_code = ccv_c.cms_code (+)                   -- 08/31/2011
        AND 'picklist.statetype.items' = ccv_s.asset_code (+)     -- 08/31/2011
        AND e.state = ccv_s.cms_code (+)                          -- 08/31/2011
        AND e.user_id = un.user_id
        AND e.user_id = pe.user_id (+)
        AND e.hire_date = pe.hire_date (+)
        AND pe.termination_date IS NULL
        AND pe.employer_id = emp.employer_id (+)
        AND e.user_id = au.user_id
        AND un.node_id = n.node_id
        AND nvl(trunc(e.enrollment_date),trunc(sysdate))
              between fnc_locale_to_date_dt(p_in_from_date,p_in_locale)
                  and fnc_locale_to_date_dt(p_in_to_date,p_in_locale)
        AND e.node_id
             IN (SELECT node_id
                   FROM rpt_hierarchy
                CONNECT BY PRIOR node_id = parent_node_id
                  START WITH node_id = nvl(P_in_parentNodeId,0) )
      ORDER BY 1) ;
  p_out_return_code :=  '00' ;
EXCEPTION
    WHEN OTHERS  THEN
        p_out_return_code :=  '99' ;
        OPEN p_out_result_set FOR SELECT NULL FROM dual  ;
END PRC_RPT_ENROLLMENT_EXTRACT;
/
