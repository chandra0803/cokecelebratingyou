CREATE OR REPLACE PACKAGE pkg_report_enrollment
  IS
/*******************************************************************************
--
-- Purpose: Proocedures and functions used to assist the online enrollment report
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
--  ????       ?????      Creation
-- Raju N      10/16/2006 removed the f_total from package spec as it is no
                          longer being used.
*******************************************************************************/
 PROCEDURE prc_enrollment_detail(p_user_id IN number,
 p_start_date   IN DATE, 
 p_end_date     IN DATE, 
 p_return_code   OUT NUMBER,
 p_error_message OUT VARCHAR2) ;

 PROCEDURE prc_enrollment_summary
 (p_user_id       IN number,
  p_return_code   OUT NUMBER,
  p_error_message OUT VARCHAR2);
 /*  no longer in use
FUNCTION f_total (p_in_header_node_id IN number,
                     p_in_start_date DATE ,
                     p_in_end_date DATE,
                     p_in_status IN VARCHAR2  ) RETURN NUMBER;*/


  FUNCTION  FNC_GET_COUNT(
              p_in_node_id IN NUMBER,
              p_in_record_type IN VARCHAR2,
              p_in_from_date IN DATE,
              p_in_to_date IN DATE,
              p_in_status IN VARCHAR2,
              p_in_dept IN VARCHAR2,
              p_in_country IN VARCHAR2,
              p_in_job_title IN VARCHAR2,
              p_in_role IN VARCHAR2,
              p_in_count_type IN VARCHAR2)
  RETURN NUMBER;
    
END; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY pkg_report_enrollment
IS
FUNCTION f_total (p_in_header_node_id IN number,
                         p_in_start_date DATE ,
                         p_in_end_date DATE,
                         p_in_status IN VARCHAR2  ) RETURN NUMBER IS
v_total_count    number(18):= 0 ;
BEGIN
SELECT sum(enrollment_count)
   INTO v_total_count
  FROM
 (SELECT SUM(enrollment_count) enrollment_count
   FROM rpt_enrollment_summary
  WHERE header_node_id = nvl(p_in_header_node_id,0)
    AND enrollment_date BETWEEN p_in_start_date AND p_in_end_date
    AND nvl(status,'inactive') = p_in_status
    AND record_type LIKE '%node%'
UNION
 SELECT SUM(enrollment_count)
   FROM rpt_enrollment_summary
  WHERE detail_node_id = nvl(p_in_header_node_id,0)
    AND enrollment_date BETWEEN p_in_start_date AND p_in_end_date
    AND nvl(status,'inactive') = p_in_status
    AND record_type LIKE '%team%') ;
/* SELECT SUM (enrollment_count)
   INTO v_total_count
   FROM rpt_enrollment_summary
  WHERE header_node_id = nvl(p_in_header_node_id,0)
    AND enrollment_date BETWEEN p_in_start_date AND p_in_end_date
    AND nvl(status,'inactive') = p_in_status ;*/
   RETURN v_total_count ;
EXCEPTION
  WHEN OTHERS THEN
   v_total_count := 0 ;
   RETURN v_total_count ;
END ;
PROCEDURE prc_enrollment_detail(p_user_id IN number,
                                 p_start_date IN DATE, 
                                 p_end_date IN DATE, 
                                 p_return_code OUT NUMBER,
                                 p_error_message OUT VARCHAR2) 
IS
/*******************************************************************************
--
-- Purpose: Populate rpt_enrollment_detail table.
--
-- MODIFICATION HISTORY
-- Person              Date        Comments
-- ---------           ------      ------------------------------------------
-- Bethke              11/07/2005  Initial code.
-- D Murray            12/14/2005  Added nvl for status, job_position and department
                                - can not be null for Jasper report parameters.
-- Rachel R            01/13/2006  Added refresh date logic for detail.
-- Raju N              01/04/2007  BUG#13603. dafeult the NULL enrollment date to
--                                 application_user.date_created
-- Arun S              05/11/2010  Bug # 32555 Fix, International address changes
                                Removed addr4, addr5, addr6 populate to report table
-- Chidamba           07/27/2012  Changes to implement incremental approach 
-- Ravi Dhanekula     09/28/2012  Changed the country to country_id and removed cm_asset_code from detail report table. 
                    05/10/2013  Added changes to participant_employer also for consideration to update report data.
                    06/18/2013     Replaced loop processing code with bulk processing for performance tuning
-- nagarajs           03/11/2014  Fixed to update the new node Bug # 52085
-- Swati            07/09/2014  Fixed Bug 54495 Enrolment Extract- not exporting the email address and phone number when the pax does have one
-- nagarajs         07/15/2014  Fixed Bug 54765 -  Enrollment Report not pulling Inactive participant 
*******************************************************************************/
  c_created_by  CONSTANT rpt_enrollment_detail.created_by%TYPE:=p_user_id;
  v_stage       VARCHAR2(250);
  v_commit_cnt  INTEGER;
  v_user_count               NUMBER;
  pi_enrollment_details rpt_enrollment_detail%rowtype;
  -- get all pax
  /*CURSOR cur_pax IS
    SELECT user_id,
           status,
           date_status_change
    FROM   participant;
  -- Get the user association with primary hierarchy
  CURSOR cur_node(p_user_id VARCHAR2) IS
    SELECT node_id
    FROM   user_node
    WHERE  user_id = p_user_id
    AND    node_id IN (SELECT node_id
                       FROM rpt_hierarchy a);*/
                         
    /*
  CURSOR cur_pax(p_start_date IN DATE, p_end_date IN DATE) IS
        SELECT p.user_id,p.status,p.date_status_change status_change_date,au.title,
             au.first_name,
             un.node_id,
             au.middle_name,
             au.last_name,
             au.suffix,
             au.birth_date,
             au.gender,
             au.language_id language_preference,
             au.enrollment_source,
             trunc(nvl(au.enrollment_date,au.date_created)) enrollment_date,
             pe.position_type job_position,
             un.role,
             pe.department_type department,
             pe.hire_date,
             ua.address_type primary_address_type,
             ua.country_id country,
             ua.addr1,
             ua.addr2,
             ua.addr3,
             ua.city,
             ua.state,
             ua.postal_code,
             usp.phone_type primary_phone_type,
             usp.phone_nbr primary_phone_number,
             ue.email_type primary_email_addr_type,
             ue.email_addr primary_email_address,
             ua.country_id
            -- c.cm_asset_code
            FROM participant p,
            user_node  un,
            application_user au,
             --participant_employer pe, --05/10/2013
            vw_curr_pax_employer pe,  --05/10/2013
            user_address ua,
           -- country c,
                 (SELECT user_id,email_type,email_addr,is_primary 
                    FROM user_email_address uea 
                   WHERE (p_start_date < uea.DATE_CREATED  AND  uea.DATE_CREATED <= p_end_date)
                      OR (p_start_date < uea.DATE_MODIFIED  AND  uea.DATE_MODIFIED <= p_end_date)) ue,
                 (SELECT user_id,phone_type,phone_nbr,is_primary 
                    FROM user_phone up 
                   WHERE (p_start_date < up.DATE_CREATED  AND  up.DATE_CREATED <= p_end_date)
                      OR (p_start_date < up.DATE_MODIFIED  AND  up.DATE_MODIFIED <= p_end_date)) usp
        WHERE p.USER_ID = un.USER_ID
          AND un.node_id IN (SELECT node_id
                       FROM rpt_hierarchy)
         AND p.USER_ID=au.user_id(+)
         AND p.USER_ID=pe.user_id(+)
         AND pe.termination_date IS NULL
         AND p.user_id=ua.user_id
       --  AND ua.country_id = c.country_id(+)
         AND ua.is_primary(+)=1
         AND p.USER_ID = ue.USER_ID(+)
         AND ue.is_primary(+)=1
         AND p.USER_ID = usp.USER_ID(+)
         AND usp.is_primary(+)=1
         AND (p_start_date < au.date_created and au.date_created <= p_end_date
             OR p_start_date < au.date_modified  and  au.date_modified <=p_end_date
             OR p_start_date < ua.date_created and ua.date_created <= p_end_date
             OR p_start_date < ua.date_modified  and  ua.date_modified <=p_end_date
             OR p_start_date < pe.date_created and pe.date_created <= p_end_date    --05/10/2013
             OR p_start_date < pe.date_modified  and  pe.date_modified <=p_end_date);--05/10/2013
  
   
BEGIN

  
  FOR rec_pax IN cur_pax(p_start_date,p_end_date) LOOP
  
    --check to see if this record is new or an update
        SELECT COUNT(user_id)
            INTO v_user_count
        FROM rpt_enrollment_detail r
        WHERE r.USER_ID = rec_pax.user_id;
          
    IF(v_user_count = 1) THEN
      v_stage := 'insert rpt_enrollment_detail';  
      UPDATE rpt_enrollment_detail r
         SET last_name=rec_pax.last_name,
             first_name= rec_pax.first_name,
             middle_init= rec_pax.middle_name,
             title= rec_pax.title,
             suffix=rec_pax.suffix,
             birth_date=rec_pax.birth_date,
             gender=rec_pax.gender,
             enrollment_date=rec_pax.enrollment_date,
             enrollment_source=rec_pax.enrollment_source,
             status=nvl(rec_pax.status,' '),
             status_change_date=rec_pax.status_change_date,
             hire_date=rec_pax.hire_date,
             job_position=nvl(rec_pax.job_position,' '),
             role   =  rec_pax.role,
             department=nvl(rec_pax.department,' '),
             node_id=rec_pax.node_id,
             primary_address_type=rec_pax.primary_address_type,
             country_id=rec_pax.country_id,
             address1=rec_pax.addr1,
             address2=rec_pax.addr2,
             address3=rec_pax.addr3,
             city=rec_pax.city,
             state=rec_pax.state,
             postal_code=rec_pax.postal_code,
             primary_phone_type=rec_pax.primary_phone_type,
             primary_phone_number=rec_pax.primary_phone_number,
             primary_email_addr_type=rec_pax.primary_email_addr_type,
             primary_email_address=rec_pax.primary_email_address,
             language_preference=rec_pax.language_preference,
             modified_by        = c_created_by,
             date_modified      = SYSDATE
           --  cm_asset_code=rec_pax.cm_asset_code
       WHERE r.USER_ID = rec_pax.user_id;
    
    ELSE
  
      v_stage := 'insert rpt_enrollment_detail';
      INSERT INTO rpt_enrollment_detail
            (rpt_enrollment_detail_id,
             user_id,
             last_name,
             first_name,
             middle_init,
             title,
             suffix,
             birth_date,
             gender,
             enrollment_date,
             enrollment_source,
             status,
             status_change_date,
             hire_date,
             job_position,
             role,
             department,
             node_id,
             primary_address_type,
             country_id,
             address1,
             address2,
             address3,
             city,
             state,
             postal_code,
             primary_phone_type,
             primary_phone_number,
             primary_email_addr_type,
             primary_email_address,
             language_preference,
            -- cm_asset_code,
             date_created,
             created_by)
      VALUES(RPT_ENROLLMENT_DETAIL_PK_SQ.nextval,
             rec_pax.user_id,
             rec_pax.last_name,
             rec_pax.first_name,
             rec_pax.middle_name,
             rec_pax.title,
             rec_pax.suffix,
             rec_pax.birth_date,
             rec_pax.gender,
             rec_pax.enrollment_date,
             rec_pax.enrollment_source,
             nvl(rec_pax.status,' '),
             rec_pax.status_change_date,
             rec_pax.hire_date,
             nvl(rec_pax.job_position,' '),
             rec_pax.role,
             nvl(rec_pax.department,' '),
             rec_pax.node_id,
             rec_pax.primary_address_type,
             rec_pax.country_id,
             rec_pax.addr1,
             rec_pax.addr2,
             rec_pax.addr3,
             rec_pax.city,
             rec_pax.state,
             rec_pax.postal_code,
             rec_pax.primary_phone_type,
             rec_pax.primary_phone_number,
             rec_pax.primary_email_addr_type,
             rec_pax.primary_email_address,
             rec_pax.language_preference,
--             rec_pax.cm_asset_code,
             sysdate,
             c_created_by);
             
     /*        pi_enrollment_details.user_id:=  rec_pax.user_id;
             pi_enrollment_details.title:=rec_pax.title;
             pi_enrollment_details.suffix:= rec_pax.suffix;
             pi_enrollment_details.birth_date:=rec_pax.birth_date;
             pi_enrollment_details.gender:= rec_pax.gender;
             pi_enrollment_details.enrollment_date:=rec_pax.enrollment_date;
             pi_enrollment_details.enrollment_source:= rec_pax.enrollment_source;
             pi_enrollment_details.status:=nvl(rec_pax.status,' ');
             pi_enrollment_details.status_change_date:= rec_pax.status_change_date;
             pi_enrollment_details.hire_date:=rec_pax.hire_date;
             pi_enrollment_details.job_position:=  nvl(rec_pax.job_position,' ');
             pi_enrollment_details.department:=  nvl(rec_pax.department,' ');
             pi_enrollment_details.node_id:= rec_pax.node_id;
          
                  
           prc_enrollment_summary (pi_enrollment_details,p_return_code,p_error_message);*/
     /* v_commit_cnt := v_commit_cnt + 1;
      IF v_commit_cnt > 500 THEN

        v_commit_cnt := 0;
      END IF;
      
    END IF;
    
  END LOOP; --rec_pax
*/

BEGIN

  MERGE INTO rpt_enrollment_detail d    
     USING (            
        WITH detail_enroll AS          
              (              
              SELECT p.user_id,p.status,p.date_status_change status_change_date,au.title,
             au.first_name,
             un.node_id,
             au.middle_name,
             au.last_name,
             au.suffix,
             au.birth_date,
             au.gender,
             au.language_id language_preference,
             au.enrollment_source,
             trunc(nvl(au.enrollment_date,au.date_created)) enrollment_date,
             pe.position_type job_position,
             un.role,
             pe.department_type department,
             pe.hire_date,
             ua.address_type primary_address_type,
             ua.country_id country,
             ua.addr1,
             ua.addr2,
             ua.addr3,
             ua.city,
             ua.state,
             ua.postal_code,
             usp.phone_type primary_phone_type,
             usp.phone_nbr primary_phone_number,
             ue.email_type primary_email_addr_type,
             ue.email_addr primary_email_address,
             ua.country_id         
            FROM participant p,
            user_node  un,
            application_user au,           
            vw_curr_pax_employer pe,  --05/10/2013 
            user_address ua,           
                 (SELECT user_id,email_type,email_addr,is_primary,date_created,date_modified 
                    FROM user_email_address uea 
                   --WHERE (p_start_date < uea.DATE_CREATED  AND  uea.DATE_CREATED <= p_end_date) 07/09/2014 Bug 54495
                      --OR (p_start_date < uea.DATE_MODIFIED  AND  uea.DATE_MODIFIED <= p_end_date)
                      ) ue,
                 (SELECT user_id,phone_type,phone_nbr,is_primary,date_created,date_modified 
                    FROM user_phone up 
                   --WHERE (p_start_date < up.DATE_CREATED  AND  up.DATE_CREATED <= p_end_date) 07/09/2014 Bug 54495
                      --OR (p_start_date < up.DATE_MODIFIED  AND  up.DATE_MODIFIED <= p_end_date)
                      ) usp
        WHERE p.USER_ID = un.USER_ID
          AND un.is_primary =1
         AND p.user_id=au.user_id(+)
         AND p.user_id=pe.user_id(+)
         --AND pe.termination_date IS NULL --07/15/2014 Bug # 54765
         AND p.user_id=ua.user_id       
         AND ua.is_primary(+)=1
         AND p.USER_ID = ue.USER_ID(+)
         AND ue.is_primary(+)=1
         AND p.USER_ID = usp.USER_ID(+)
         AND usp.is_primary(+)=1
         AND (p_start_date < au.date_created and au.date_created <= p_end_date
             OR p_start_date < au.date_modified  and  au.date_modified <=p_end_date
             OR p_start_date < ua.date_created and ua.date_created <= p_end_date
             OR p_start_date < ua.date_modified  and  ua.date_modified <=p_end_date
             OR p_start_date < pe.date_created and pe.date_created <= p_end_date   
             OR p_start_date < pe.date_modified  and  pe.date_modified <=p_end_date
             OR p_start_date < ua.date_created and ua.date_created <= p_end_date
             OR p_start_date < ue.DATE_CREATED  AND  ue.DATE_CREATED <= p_end_date --07/09/2014 Bug 54495
             OR p_start_date < ue.DATE_MODIFIED  AND  ue.DATE_MODIFIED <= p_end_date
             OR p_start_date < usp.DATE_CREATED  AND  usp.DATE_CREATED <= p_end_date
             OR p_start_date < usp.DATE_MODIFIED  AND  usp.DATE_MODIFIED <= p_end_date))  --03/11/2014        
             (select * from detail_enroll)) detail_enroll2     
             ON (d.user_id = detail_enroll2.user_id)
     WHEN MATCHED THEN
     UPDATE SET
     last_name=detail_enroll2.last_name,
             first_name= detail_enroll2.first_name,
             middle_init= detail_enroll2.middle_name,
             title= detail_enroll2.title,
             suffix=detail_enroll2.suffix,
             birth_date=detail_enroll2.birth_date,
             gender=detail_enroll2.gender,
             enrollment_date=detail_enroll2.enrollment_date,
             enrollment_source=detail_enroll2.enrollment_source,
             status=nvl(detail_enroll2.status,' '),
             status_change_date=detail_enroll2.status_change_date,
             hire_date=detail_enroll2.hire_date,
             job_position=nvl(detail_enroll2.job_position,' '),
             role   =  detail_enroll2.role,
             department=nvl(detail_enroll2.department,' '),
             node_id=detail_enroll2.node_id,
             primary_address_type=detail_enroll2.primary_address_type,
             country_id=detail_enroll2.country_id,
             address1=detail_enroll2.addr1,
             address2=detail_enroll2.addr2,
             address3=detail_enroll2.addr3,
             city=detail_enroll2.city,
             state=detail_enroll2.state,
             postal_code=detail_enroll2.postal_code,
             primary_phone_type=detail_enroll2.primary_phone_type,
             primary_phone_number=detail_enroll2.primary_phone_number,
             primary_email_addr_type=detail_enroll2.primary_email_addr_type,
             primary_email_address=detail_enroll2.primary_email_address,
             language_preference=detail_enroll2.language_preference,
             modified_by        = c_created_by,
             date_modified      = SYSDATE
             WHEN NOT MATCHED THEN
             INSERT 
             (rpt_enrollment_detail_id,
             user_id,
             last_name,
             first_name,
             middle_init,
             title,
             suffix,
             birth_date,
             gender,
             enrollment_date,
             enrollment_source,
             status,
             status_change_date,
             hire_date,
             job_position,
             role,
             department,
             node_id,
             primary_address_type,
             country_id,
             address1,
             address2,
             address3,
             city,
             state,
             postal_code,
             primary_phone_type,
             primary_phone_number,
             primary_email_addr_type,
             primary_email_address,
             language_preference,            
             date_created,
             created_by)
             VALUES(RPT_ENROLLMENT_DETAIL_PK_SQ.nextval,
             detail_enroll2.user_id,
             detail_enroll2.last_name,
             detail_enroll2.first_name,
             detail_enroll2.middle_name,
             detail_enroll2.title,
             detail_enroll2.suffix,
             detail_enroll2.birth_date,
             detail_enroll2.gender,
             detail_enroll2.enrollment_date,
             detail_enroll2.enrollment_source,
             nvl(detail_enroll2.status,' '),
             detail_enroll2.status_change_date,
             detail_enroll2.hire_date,
             nvl(detail_enroll2.job_position,' '),
             detail_enroll2.role,
             nvl(detail_enroll2.department,' '),
             detail_enroll2.node_id,
             detail_enroll2.primary_address_type,
             detail_enroll2.country_id,
             detail_enroll2.addr1,
             detail_enroll2.addr2,
             detail_enroll2.addr3,
             detail_enroll2.city,
             detail_enroll2.state,
             detail_enroll2.postal_code,
             detail_enroll2.primary_phone_type,
             detail_enroll2.primary_phone_number,
             detail_enroll2.primary_email_addr_type,
             detail_enroll2.primary_email_address,
             detail_enroll2.language_preference,
             sysdate,
             c_created_by);
          
  p_return_code := 0;
                                 
EXCEPTION
  WHEN others THEN
     p_return_code    := 99; 
     p_error_message  := v_stage||' '||SQLERRM; 
     prc_execution_log_entry('prc_enrollment_detail',1,'ERROR',v_stage||' '||SQLERRM,null);
END;

PROCEDURE prc_enrollment_summary(p_user_id       IN  NUMBER,
                                 p_return_code   OUT NUMBER,
                                 p_error_message OUT VARCHAR2)

IS
--
--
-- Purpose: Populate rpt_enrollment_summary table.
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
-- Bethke      11/08/2005 Initial code.
-- D Murray    12/06/2005 Added nvl to all queries for status
--                         - can not be null for Jasper report parameters.
-- D Murray    12/12/2005 Added job_position and department.
-- Rachel R    01/13/2006 Added refresh date logic for summary, pie, bar, and trend.
-- chidamba    07/27/2012 Changes to incremental approach
-- Chidamba    11/15/2012 adding role attribute to detail and summary    

  c_created_by  CONSTANT rpt_enrollment_detail.created_by%TYPE:= p_user_id;
  v_stage       VARCHAR2(250);
  v_commit_cnt  INTEGER;

  v_rec_cnt            INTEGER;
  c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('prc_enrollment_summary');
  c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;  

   CURSOR cur_level is
     SELECT DISTINCT hier_level
       FROM rpt_hierarchy
      ORDER BY hier_level desc;

   CURSOR cur_hier (p_hier_level NUMBER) IS
     SELECT parent_node_id, node_id, is_leaf
       FROM rpt_hierarchy
      WHERE hier_level = p_hier_level
      ORDER BY parent_node_id, node_id;
      
  v_rpt_enrollment_summary_id rpt_enrollment_summary.rpt_enrollment_summary_id%TYPE;
  
BEGIN


  /*DELETE  rpt_enrollment_summary;
    
   MERGE INTO rpt_refresh_date B
    USING (
        SELECT 'enrollment' as report_name, 'summary' as report_type, sysdate as refresh_date
        FROM dual
    UNION
         SELECT 'enrollment' as report_name, 'trend' as report_type, sysdate as refresh_date
        FROM dual
    UNION
         SELECT 'enrollment' as report_name, 'piechart' as report_type, sysdate as refresh_date
        FROM dual
    UNION
         SELECT 'enrollment' as report_name, 'barchart' as report_type, sysdate as refresh_date
        FROM dual) E
    ON (B.report_name = e.report_name and b.report_type = e.report_type)
    WHEN MATCHED THEN
        UPDATE SET B.refresh_date = e.refresh_date
    WHEN NOT MATCHED THEN
        INSERT (B.report_name, b.report_type, b.refresh_date)
        VALUES (E.report_name, E.report_type, e.refresh_date);*/


   ------------
   -- remove obsolete node summaries (node not found in rpt_hierarchy)
   v_stage := 'DELETE obsolete node summary records';
   DELETE
     FROM rpt_enrollment_summary s
    WHERE s.detail_node_id NOT IN
          ( -- get node ID currently in the report hierarchy
            SELECT h.node_id
              FROM rpt_hierarchy h
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);   
   
   
   ------------
   -- merge derived summary records
   v_stage := 'MERGE detail derived summary records';
   MERGE INTO rpt_enrollment_summary d
   USING (
             WITH rpt_teamsum AS
            (  -- build team summary records        
              SELECT h.parent_node_id AS header_node_id
                    --,nvl(d.node_id,h.node_id) AS detail_node_id
                    ,d.node_id AS detail_node_id
                    ,COUNT(d.user_id) AS enrollment_count
                    ,TRUNC(enrollment_date) enrollment_date
                    ,nvl(d.status,' ') AS pax_status
                    ,NVL(d.job_position,' ') AS job_position
                    ,NVL(d.role,' ') as role                     --11/15/2012
                    ,NVL(d.country_id,0) as country_id               
                    ,NVL(d.department,' ') AS department
                    ,h.hier_level
               FROM rpt_enrollment_detail d
                   ,rpt_hierarchy h
              WHERE h.node_id = d.node_id
              GROUP BY h.parent_node_id ,d.node_id
                       ,status, job_position, department, enrollment_date,h.hier_level,d.country_id,d.role
            ), detail_derived_summary AS
            (  -- derive summaries based on team summary data
               SELECT -- key fields
                      rt.detail_node_id,
                      'teamsum' AS sum_type,
                      rt.pax_status,
                      rt.job_position,
                      rt.role,
                      rt.country_id,                          
                      rt.department,
                      rt.enrollment_date,                      
                      -- reference fields
                      rt.header_node_id,
                      rt.hier_level,
                      1 AS is_leaf, -- The team summary records are always a leaf
                      -- count fields
                      rt.enrollment_count
                 FROM rpt_teamsum rt
                UNION ALL
               SELECT -- key fields
                      h.node_id AS detail_node_id,
                      'nodesum' AS sum_type,
                      rt.pax_status,
                      rt.job_position,
                      rt.role,
                      rt.country_id,
                      rt.department,
                      rt.enrollment_date,                      
                      -- reference fields
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      h.is_leaf,
                      -- count fields
                      NVL(SUM(rt.enrollment_count),0) AS enrollment_count
                 FROM ( -- associate each node to all its hierarchy nodes
                        SELECT np.node_id,
                               p.column_value AS path_node_id
                          FROM ( -- get node hierarchy path
                                 SELECT h.node_id,
                                        h.hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                   FROM rpt_hierarchy h
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
                               ) AS sys.odcinumberlist ) ) p
                      ) npn,
                      rpt_hierarchy h,
                      rpt_teamsum rt
                WHERE  (rt.hier_level = h.hier_level OR rt.enrollment_count != 0)  -- always create node summary at team summary level                
                   -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
                  AND rt.detail_node_id = npn.node_id
                  AND npn.path_node_id = h.node_id
                GROUP BY h.node_id,                      
                      rt.pax_status,
                      rt.job_position,
                      rt.role,rt.country_id,
                      rt.department,
                      rt.enrollment_date,                                          
                      h.parent_node_id,
                      h.hier_level,
                      h.is_leaf
            ) -- end detail_derived_summary
            -- compare existing summary records with detail derived summaries
            SELECT es.s_rowid,
                   dds.hier_level || '-' || dds.sum_type AS record_type,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.detail_node_id,
                            SUBSTR(s2.record_type, -7) AS sum_type,                      
                            s2.status,
                            s2.job_position,
                            s2.role,                           --11/15/2012
                            s2.country_id,
                            s2.department,
                            s2.enrollment_date                            
                       FROM rpt_enrollment_summary s2
                      WHERE s2.enrollment_date IS NOT NULL
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                   FULL OUTER JOIN detail_derived_summary dds
                   ON (   es.detail_node_id = dds.detail_node_id
                      AND es.sum_type       = dds.sum_type                      
                      AND es.status         = dds.pax_status
                      AND es.job_position   = dds.job_position
                      AND es.role           = dds.role          --11/15/2012
                      AND es.country_id     = dds.country_id
                      AND es.department     = dds.department
                      AND es.enrollment_date = dds.enrollment_date                      
                      )
         ) s
      ON (d.ROWID = s.s_rowid)
    WHEN MATCHED THEN
      UPDATE SET       
         d.header_node_id     = s.header_node_id,
         d.hier_level         = s.hier_level,
         d.is_leaf            = s.is_leaf,
         d.enrollment_count   = s.enrollment_count,
         d.role               = s.role,  
         d.country_id         = s.country_id,   
         d.modified_by        = c_created_by,
         d.date_modified      = SYSDATE
       WHERE ( -- only update records with different values                
                DECODE(d.header_node_id,     s.header_node_id,     1, 0) = 0
             OR DECODE(d.hier_level,         s.hier_level,         1, 0) = 0
             OR DECODE(d.is_leaf,            s.is_leaf,            1, 0) = 0
             OR DECODE(d.enrollment_count, s.enrollment_count, 1, 0) = 0
             OR DECODE(d.role            , s.role            , 1, 0) = 0
             OR DECODE(d.country_id      , s.country_id      , 1, 0) = 0
             )
      -- remove existing summaries that no longer have product details
    WHEN NOT MATCHED THEN
      INSERT
      (  rpt_enrollment_summary_id,
         -- key fields
         record_type,
         header_node_id,
         detail_node_id,
         enrollment_count,
         enrollment_date,
         status,
         job_position,
         role,
         country_id,
         department,         
         hier_level,
         is_leaf,
         created_by,
         date_created,
         modified_by,
         date_modified)
  VALUES (RPT_ENROLLMENT_SUMMARY_PK_SQ.NEXTVAL,
         -- key fields         
         s.record_type,
         s.header_node_id,
         s.detail_node_id,
         s.enrollment_count,
         s.enrollment_date,
         s.pax_status,
         s.job_position,
         s.role,
         s.country_id,
         s.department,                 
         s.hier_level,
         s.is_leaf,         
         c_created_by,
         SYSDATE,
         NULL,
         NULL);

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   ------------
   -- add missing default summary permutations
   v_stage := 'INSERT missing default team summary permutations';
         INSERT INTO rpt_enrollment_summary
      (  rpt_enrollment_summary_id,
         -- key fields
         record_type,
         header_node_id,
         detail_node_id,
         enrollment_count,
         enrollment_date,
         status,
         job_position,
         role,
         country_id,
         department,
         hier_level,
         is_leaf,
         created_by,
         date_created,
         modified_by,
         date_modified)

  SELECT rpt_enrollment_summary_pk_sq.NEXTVAL,
             -- key fields
             nsp.record_type,
             nsp.header_node_id,
             nsp.detail_node_id,
             0 AS enrollment_count,
             NULL AS enrollment_date,
             ' '  AS pax_status,
             ' '  AS job_position,
             ' '  AS role,
             0    AS country_id,
             ' '  AS department,              
             nsp.hier_level,
             nsp.is_leaf,           
             -- audit fields
             c_created_by AS created_by,
             SYSDATE      AS date_created,
             NULL         AS modified_by,
             NULL         AS date_modified
        FROM ( -- get all possible node summary type permutations
               SELECT h.node_id AS detail_node_id,
                      h.hier_level || '-' || 'teamsum' AS record_type,
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      1 AS is_leaf   -- team summary always a leaf node
                 FROM rpt_hierarchy h
             ) nsp
          -- exclude default permutation when a matching summary record exists
        WHERE NOT EXISTS
             ( SELECT 1
                 FROM rpt_enrollment_summary s
                WHERE s.detail_node_id = nsp.detail_node_id
                  AND s.record_type    = nsp.record_type
                   -- any media date value matches on team summary records because detail derived summaries replace default team permutations
             );
             
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   v_stage := 'INSERT missing default node summary permutations';
   INSERT INTO rpt_enrollment_summary
      (  rpt_enrollment_summary_id,
         -- key fields
         record_type,
         header_node_id,
         detail_node_id,
         enrollment_count,
         enrollment_date,
         status,
         job_position,
         role,
         country_id,
         department,                  
         hier_level,
         is_leaf,
         created_by,
         date_created,
         modified_by,
         date_modified)

   SELECT rpt_enrollment_summary_pk_sq.NEXTVAL,
             -- key fields
             nsp.record_type,
             nsp.header_node_id,
             nsp.detail_node_id,
             0 AS enrollment_count,
             NULL AS enrollment_date,
             ' '  AS pax_status,
             ' '  AS job_position,
             ' '  AS role, 
             0    AS country_id,
             ' '  AS department,
             nsp.hier_level,
             nsp.is_leaf,           
             -- audit fields
             c_created_by AS created_by,
             SYSDATE      AS date_created,
             NULL         AS modified_by,
             NULL         AS date_modified
        FROM ( -- get all possible node summary type permutations
               SELECT h.node_id AS detail_node_id,
                      h.hier_level || '-' || 'nodesum' AS record_type,
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      h.is_leaf
                 FROM rpt_hierarchy h
             ) nsp
          -- exclude default permutation when a matching summary record exists
        WHERE 
          NOT EXISTS
             ( SELECT 1
                 FROM rpt_enrollment_summary s
                WHERE s.detail_node_id = nsp.detail_node_id
                  AND s.record_type    = nsp.record_type
                  AND s.enrollment_date IS NULL
             )
          -- default node summary permutation must have default team summary permutation in its hierarchy
        AND  EXISTS
             ( -- get team permutations under node permutation
               SELECT 1
                 FROM rpt_enrollment_summary tp
                WHERE tp.detail_node_id          = nsp.detail_node_id
                  AND SUBSTR(tp.record_type, -7) = 'teamsum'
                  AND tp.enrollment_date IS NULL
                /*UNION ALL
               -- get team permutations under node permutation hierarchy
               SELECT 1
                 FROM rpt_enrollment_summary tp
                   -- start with child node immediately under current node
                START WITH tp.header_node_id          = nsp.detail_node_id
                       AND SUBSTR(tp.record_type, -7) = 'teamsum'
                       AND tp.enrollment_date  IS NULL
                CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
                       AND PRIOR SUBSTR(tp.record_type, -7) = SUBSTR(tp.record_type, -7)
                       AND tp.enrollment_date IS  NULL*/
             );
             
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   ------------
   -- remove team permutations when an associated detail derived summary exists
   v_stage := 'DELETE obsolete team permutations';
   DELETE rpt_enrollment_summary
    WHERE ROWID IN
          ( -- get default team permutation with a corresponding detail derived team summary
            SELECT DISTINCT
                   s.ROWID
              FROM rpt_enrollment_summary s,
                   rpt_enrollment_summary dd
                -- substr matches functional index
             WHERE SUBSTR(dd.record_type, -7) = 'teamsum'
                -- detail derived summaries have a media date
               AND dd.enrollment_date IS NOT NULL
               AND dd.detail_node_id          = s.detail_node_id
               AND SUBSTR(dd.record_type, -7) = SUBSTR(s.record_type, -7)
                -- default permutations have no media date
               AND s.enrollment_date IS NULL
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   -- delete node permutation with no associated team permutation
   v_stage := 'DELETE obsolete node permutations with no associated team permutation';
   DELETE rpt_enrollment_summary
    WHERE ROWID IN
          ( SELECT np.ROWID
              FROM rpt_enrollment_summary np
             WHERE SUBSTR(np.record_type, -7) = 'nodesum'
               AND np.enrollment_date IS NULL
                -- node permutation has no team permutation
               AND NOT EXISTS
                   ( -- get team permutations under node permutation
                     SELECT 1
                       FROM rpt_enrollment_summary tp
                      WHERE tp.detail_node_id          = np.detail_node_id
                        AND SUBSTR(tp.record_type, -7) = 'teamsum'
                        AND tp.enrollment_date IS NULL
                      /*UNION ALL
                     -- get team permutations under node permutation hierarchy
                     SELECT 1
                       FROM rpt_enrollment_summary tp
                         -- start with child node immediately under current node
                      START WITH tp.header_node_id          = np.detail_node_id
                             AND SUBSTR(tp.record_type, -7) = 'teamsum'
                             AND tp.enrollment_date IS NULL
                      CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
                             AND PRIOR SUBSTR(tp.record_type, -7) = SUBSTR(tp.record_type, -7)
                             AND tp.enrollment_date IS NULL*/
                   )
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);


   v_stage := 'Success';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

   p_return_code := 0;
  /*FOR rec_level IN cur_level LOOP

    SELECT rpt_enrollment_summary_pk_sq.NEXTVAL
      INTO v_rpt_enrollment_summary_id
      FROM dual ;

    --insert teamsum records
    INSERT INTO rpt_enrollment_summary
           (record_type,
           header_node_id,
           detail_node_id,
           enrollment_count,
           enrollment_date,
           status,
           job_position,
           department,
           hier_level,
           is_leaf,
          -- rpt_enrollment_summary_id, --is taken care of by yhe trigger
           date_created,
           created_by)
    (SELECT h.hier_level||'-teamsum'
            ,h.parent_node_id
            ,nvl(d.node_id,h.node_id)
            ,COUNT(d.user_id)
            ,trunc(enrollment_date) enrollment_date
            ,nvl(d.status,' ') status
            ,NVL(d.job_position,' ') job_position
            ,NVL(d.department,' ') department
            ,h.hier_level
            ,1  -- The team summary records are always a leaf
           -- ,v_rpt_enrollment_summary_id --is taken care of by yhe trigger
            ,sysdate
            ,c_created_by
       FROM rpt_enrollment_detail d
           ,rpt_hierarchy h
      WHERE h.node_id = d.node_id (+)
        AND h.hier_level = rec_level.hier_level
      --  AND p_start_date < au.date_modified  and  au.date_modified <=p_end_date
      GROUP BY h.parent_node_id ,nvl(d.node_id,h.node_id)
               ,status, job_position, department, enrollment_date,h.hier_level);

    v_commit_cnt := SQL%ROWCOUNT + 1;
    
    FOR rec_hier IN cur_hier(rec_level.hier_level) LOOP

      IF rec_hier.is_leaf = 1 THEN --they have no level below them

        SELECT rpt_enrollment_summary_pk_sq.NEXTVAL
          INTO v_rpt_enrollment_summary_id
          FROM dual ;
        --insert the same exact records as the teamsum insert
        --except change the record type
        INSERT INTO rpt_enrollment_summary
              (record_type,
               header_node_id,
               detail_node_id,
               enrollment_count,
               enrollment_date,
               status,
               job_position,
               department,
               hier_level,
               is_leaf,
               --rpt_enrollment_summary_id,
               date_created,
               created_by)
        (SELECT d.hier_level||'-nodesum'
                ,d.header_node_id
                ,d.detail_node_id
                ,d.enrollment_count
                ,d.enrollment_date
                ,d.status
                ,d.job_position
                ,d.department
                ,d.hier_level
                ,d.is_leaf
                --,v_rpt_enrollment_summary_id
                ,sysdate
                ,c_created_by
           FROM rpt_enrollment_summary d
          WHERE d.hier_level = rec_level.hier_level
          AND   d.detail_node_id = rec_hier.node_id);
          
          v_commit_cnt := SQL%ROWCOUNT + 1;
          
      ELSE --rec_hier.is_leaf != 1

        SELECT rpt_enrollment_summary_pk_sq.NEXTVAL
          INTO v_rpt_enrollment_summary_id
          FROM dual ;
          
        --sum the node and the team records
        INSERT INTO rpt_enrollment_summary
                       (record_type,
                       header_node_id,
                       detail_node_id,
                       enrollment_count,
                       enrollment_date,
                       status,
                       job_position,
                       department,
                       hier_level,
                       is_leaf,
                      -- rpt_enrollment_summary_id,
                       date_created,
                       created_by)
        (SELECT a.record_type,
                a.parent_node_id,
                a.node_id,
                SUM(a.enrollment_count),
                trunc(a.enrollment_date) enrollment_date,
                nvl(a.status,' ') status,
                NVL(a.job_position,' ') job_position,
                NVL(a.department,' ') department,
                a.hier_level,
                rec_hier.is_leaf,
               -- v_rpt_enrollment_summary_id,
                SYSDATE,
                c_created_by
        FROM (
        SELECT h.hier_level||'-nodesum' record_type
              ,h.parent_node_id
              ,h.node_id
              ,nodesum.enrollment_count
              ,nodesum.enrollment_date
              ,nodesum.status
              ,nodesum.job_position
              ,nodesum.department
              ,h.hier_level
         FROM rpt_enrollment_summary nodesum
              ,rpt_hierarchy h
         WHERE h.node_id = nodesum.header_node_id
           AND nodesum.hier_level = rec_level.hier_level+1
           AND nodesum.record_type LIKE '%node%'
           AND nodesum.header_node_id = rec_hier.node_id
           AND nodesum.enrollment_count != 0
        UNION ALL
        SELECT nodesum.hier_level||'-nodesum' record_type
              ,nodesum.header_node_id
              ,nodesum.detail_node_id
              ,nodesum.enrollment_count
              ,nodesum.enrollment_date
              ,nodesum.status
              ,nodesum.job_position
              ,nodesum.department
              ,nodesum.hier_level
          FROM rpt_enrollment_summary nodesum
         WHERE nodesum.hier_level = rec_level.hier_level
           AND nodesum.record_type LIKE '%team%'
           AND nodesum.detail_node_id = rec_hier.node_id
           AND nodesum.enrollment_count != 0
        ) a
        GROUP BY a.record_type,a.parent_node_id,a.node_id,
        a.enrollment_date,a.status,a.job_position,a.department,a.hier_level);

        v_commit_cnt := SQL%ROWCOUNT + 1;
        
        IF SQL%ROWCOUNT = 0 THEN
        
          --if the above insert does not insert any records
          --then insert an empty record for the level
          INSERT INTO rpt_enrollment_summary
                 (record_type,
                 header_node_id,
                 detail_node_id,
                 enrollment_count,
                 enrollment_date,
                 status,
                 job_position,
                 department,
                 hier_level,
                 is_leaf,
             --    rpt_enrollment_summary_id,
                 date_created,
                 created_by)
          (SELECT h.hier_level||'-nodesum'
                  ,h.parent_node_id
                  ,h.node_id
                  ,sum(s.enrollment_count)
                  ,trunc(s.enrollment_date)
                  ,nvl(s.status,' ') status
                  ,NVL(s.job_position,' ') job_position
                  ,NVL(s.department,' ') department
                  ,h.hier_level
                  ,rec_hier.is_leaf
                 -- ,v_rpt_enrollment_summary_id
                  ,sysdate
                  ,c_created_by
             FROM rpt_enrollment_summary s
                 ,rpt_hierarchy h
            WHERE h.node_id = s.header_node_id (+)
            and   s.hier_level = rec_level.hier_level+1
            AND   s.record_type LIKE '%node%'
            AND   s.header_node_id = rec_hier.node_id
            GROUP BY h.parent_node_id ,h.node_id
                     ,status,job_position, department,enrollment_date,h.hier_level);
                     
          v_commit_cnt := SQL%ROWCOUNT + 1;
          
        END IF;
        
      END IF;
      
    END LOOP;
    
    IF v_commit_cnt > 500 THEN

      v_commit_cnt := 0;
    END IF;
      
  END LOOP;*/ --rec_level

EXCEPTION
  WHEN others THEN
     p_return_code := 99;
     prc_execution_log_entry('prc_enrollment_summary',1,'ERROR',v_stage||' '||SQLERRM,null);
     p_error_message := v_stage||' '||SQLERRM;
END;



FUNCTION  FNC_GET_COUNT(
                  p_in_node_id IN NUMBER,
                  p_in_record_type IN VARCHAR2,
                  p_in_from_date IN DATE,
                  p_in_to_date IN DATE,
                  p_in_status IN VARCHAR2,
                  p_in_dept IN VARCHAR2,
                  p_in_country IN VARCHAR2,
                  p_in_job_title IN VARCHAR2,
                  p_in_role      IN VARCHAR2,
                  p_in_count_type IN VARCHAR2)
      RETURN NUMBER IS

v_active_count        NUMBER;
v_inactive_count      NUMBER;
/*
--
-- Purpose: referenced in the online report query.
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
-- ???         ???        creation
-- Raju N     10/16/2006  BUG#13603. Count only the distinct users.
--                        If a user is attached to multiple nodes then this count
--                        will be accurate at the higher level. The side effect
--                        is that the drill down total between the parent and
--                        the child might not reconcile.
*/


BEGIN

  IF lower(p_in_record_type) LIKE '%team%' THEN
    SELECT SUM(DECODE(status,'active',1,0)), SUM(DECODE(status,'inactive',1,0))
      into v_active_count, v_inactive_count
    FROM ( select distinct red.user_id, status from rpt_enrollment_detail red
    WHERE NVL(red.enrollment_date, trunc(sysdate)) BETWEEN p_in_from_date and p_in_to_date
      AND red.status        = NVL(p_in_status,red.status)
      AND red.job_position  = NVL(p_in_job_title,red.job_position)
      AND red.role          = NVL(p_in_role,red.role)
      --AND red.department    = NVL(p_in_dept,red.department)
      AND ((p_in_country IS NULL) OR (p_in_country is NOT NULL AND red.country_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_country)))))
      AND ((p_in_dept IS NULL) OR (p_in_dept is NOT NULL AND red.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_dept)))))
      AND NVL(red.node_id,0) = NVL(p_in_node_id,0));
  ELSE
    SELECT SUM(DECODE(status,'active',1,0)), SUM(DECODE(status,'inactive',1,0))
      into v_active_count, v_inactive_count
    FROM ( select distinct red.user_id, status FROM rpt_enrollment_detail red
    WHERE NVL(red.enrollment_date, trunc(sysdate)) BETWEEN p_in_from_date and p_in_to_date
      AND red.status        = NVL(p_in_status,red.status)
      AND red.job_position  = NVL(p_in_job_title,red.job_position)
      AND red.role          = NVL(p_in_role,red.role)
      AND ((p_in_country IS NULL) OR (p_in_country is NOT NULL AND red.country_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_country)))))
      AND ((p_in_dept IS NULL) OR (p_in_dept is NOT NULL AND red.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_dept)))))
      AND NVL(red.node_id,0) IN
                      (SELECT node_id
                        FROM rpt_hierarchy
                         CONNECT BY prior node_id = parent_node_id
                         START WITH node_id = p_in_node_id));
  END IF;


  IF UPPER(p_in_count_type) = 'ACTIVE' THEN
    RETURN v_active_count;
  ELSIF UPPER(p_in_count_type) = 'INACTIVE' THEN
    RETURN v_inactive_count;
  ELSIF UPPER(p_in_count_type) = 'TOTAL' THEN
    RETURN v_active_count + v_inactive_count;
  ELSE
    RETURN NULL;
  END IF;

EXCEPTION WHEN OTHERS THEN
  RETURN NULL;
END;

END;
/