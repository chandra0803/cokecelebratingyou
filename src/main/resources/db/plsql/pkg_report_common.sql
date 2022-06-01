CREATE OR REPLACE PACKAGE pkg_report_common
IS
/*******************************************************************************

   Purpose:  This package is for common code that all reports will use.

   Person      Date        Comments
   ----------- ----------  -----------------------------------------------------
   D Murray    11/07/2005  Initial packaging of common procedures
   Chidamba    07/25/2012  G5 Report Changes to Incremental approach
   J Flees     04/22/2016  G5 added reporting utilities
   J Flees     12/15/2016  G6 added user connection purge
   J Flees     03/06/2017  G6 build temp node hierarchy/rollup tables for non-report queries
*******************************************************************************/
  PROCEDURE P_PROMOTION_DETAILS
   ( p_promo_id IN NUMBER,
     p_out_promoname OUT VARCHAR2,
     p_out_promotype OUT VARCHAR2,
     p_out_is_taxable OUT NUMBER,
     p_out_media_type  OUT VARCHAR2);

  PROCEDURE P_USER_DETAILS
   (p_in_user_id IN NUMBER,
    p_out_pax_name OUT VARCHAR2 ,
    p_out_first_name OUT VARCHAR2,
    p_out_middle_name OUT VARCHAR2,
    p_out_last_name OUT VARCHAR2,
    p_out_node_name OUT VARCHAR2 ,
    p_out_node_id OUT NUMBER ,
    p_out_hierarchy_path OUT VARCHAR2 ,
    p_out_hierarchy_id OUT NUMBER ,
    p_out_hierarchy_name OUT VARCHAR2 ,
    p_out_pax_current_status OUT VARCHAR2,
    p_out_user_name OUT VARCHAR2,
    p_out_position_type OUT VARCHAR2,
    p_out_department_type OUT VARCHAR2);

  PROCEDURE p_rpt_hierarchy_populate(p_in_requested_user_id IN  NUMBER,
                                     p_in_start_date        IN  DATE,
                                     p_in_end_date          IN  DATE,
                                     p_out_return_code      OUT NUMBER,
                                     p_out_error_message    OUT VARCHAR2);
   FUNCTION FNC_GET_PERIOD_DATE
  ( p_in_date         IN DATE,
    p_in_period_type  IN VARCHAR2)
  RETURN  VARCHAR2;

  PROCEDURE PRC_INSERT_CHAR_LOOKUP;
  
   PROCEDURE P_RPT_HIERARCHY_ROLLUP;
  
  PROCEDURE p_rpt_hierarchy_summary(p_in_requested_user_id IN  NUMBER,
                                    p_in_start_date        IN  DATE,
                                    p_in_end_date          IN  DATE,
                                    p_out_return_code      OUT NUMBER,
                                    p_out_error_message    OUT VARCHAR2);
  PROCEDURE p_rpt_pax_employer(p_in_requested_user_id IN  NUMBER,                                 
                                    p_out_return_code      OUT NUMBER,
                                    p_out_error_message    OUT VARCHAR2);

   -- 04/22/2016 begin block add
   FUNCTION f_get_root_node
   RETURN NUMBER
   RESULT_CACHE;

   FUNCTION f_get_default_locale
   RETURN VARCHAR2
   RESULT_CACHE;

   FUNCTION f_get_locale_date_pattern
   ( p_in_locale     IN locale_date_pattern.locale%TYPE
   ) RETURN VARCHAR2
   RESULT_CACHE;

   FUNCTION f_get_user_currency_code
   ( p_in_user_id    IN application_user.user_id%TYPE
   ) RETURN VARCHAR2;

   PROCEDURE p_stage_search_criteria
   ( p_in_parse_list             IN VARCHAR2,
     p_in_ref_text_1             IN VARCHAR2,
     p_in_is_number_list         IN NUMBER      DEFAULT 0,
     p_in_search_all_values      IN VARCHAR2    DEFAULT pkg_const.gc_search_all_values,
     p_in_null_id                IN NUMBER      DEFAULT pkg_const.gc_null_id
   );
   -- 04/22/2016 end block add

   -- 03/06/2017 begin block add
   PROCEDURE p_stage_node_hier_level
   ( p_in_node_id_list           IN VARCHAR2,
     p_in_ref_text_1             IN VARCHAR2    DEFAULT pkg_const.gc_ref_text_node_hier_level
   );

   PROCEDURE p_stage_hierarchy_rollup
   ( p_in_node_id_list           IN VARCHAR2,
     p_in_ref_text_1             IN VARCHAR2    DEFAULT pkg_const.gc_ref_text_hierarchy_rollup
   );
   -- 03/06/2017 end block add

   -- 12/15/2016 begin block add
   PROCEDURE p_user_connection_purge
   ( p_in_rec_retension_qty      IN NUMBER,
     p_out_return_code           OUT NUMBER
   );
   -- 12/15/2016 end block add
END pkg_report_common; -- Package spec
/

CREATE OR REPLACE PACKAGE BODY pkg_report_common
IS
/*******************************************************************************

   Purpose:  This package is for common code that all reports will use.

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray    11/07/2005  Initial packaging of common procedures
   D Murray    05/17/2006  p_rpt_hierarchy_summary - Added record to the rpt_refresh_date 
                           table for iReports to use on the participant activity history
                           report.  Different activity types are using this so putting
                           the record in at core level. (Bug 12207)
   Percy M     10/30/2006  Fix for bug# 12833
   Chidamba    07/25/2012   G5 Report Changes to Incremental approach 
   Ravi Dhanekula 08/28/2013 Fixed the defect # 48640
   J Flees     04/22/2016  G5 added reporting utilities
   J Flees     12/15/2016  G6 added user connection purge
   Suresh J    03/14/2017  G6-1935    - The budget issuance extract shows null values in Participant characteristics columns
   chidamba    02/15/2018  G6-3862/Bug#75408 - add is_deleted check for top node
*******************************************************************************/
-- package constants
gc_release_level                 CONSTANT execution_log.release_level%type := 1.0;
gc_pkg_name                      CONSTANT VARCHAR2(30) := UPPER('pkg_report_common');

gc_return_code_success           CONSTANT NUMBER := pkg_const.gc_return_code_success;
gc_return_code_failure           CONSTANT NUMBER := pkg_const.gc_return_code_failure;

gc_error                         CONSTANT execution_log.severity%TYPE := pkg_const.gc_error;
gc_debug                         CONSTANT execution_log.severity%TYPE := pkg_const.gc_debug;
gc_warn                          CONSTANT execution_log.severity%TYPE := pkg_const.gc_warn;
gc_info                          CONSTANT execution_log.severity%TYPE := pkg_const.gc_info;

gc_database_currency             CONSTANT VARCHAR2(30) := pkg_const.gc_database_currency; 
gc_default_date_pattern          CONSTANT VARCHAR2(30) := pkg_const.gc_default_date_pattern;


/*PROCEDURE p_rpt_hierarchy_populate IS                 -- 07/25/2012 
   CURSOR c_hier IS
   SELECT node_id,node_type_id,
          NAME as node_name,
          parent_node_id,
          is_deleted,
          (SELECT NAME FROM node WHERE node_id = n.parent_node_id) as parent_node_name,
          level AS hier_level,
          path,
          description
   FROM node n
   WHERE is_deleted != 1
    START WITH parent_node_id IS NULL
  CONNECT BY PRIOR node_id = parent_node_id ;

  v_node_type_name   VARCHAR2(120) ;
BEGIN

  DELETE  rpt_hierarchy;
  
  FOR i IN c_hier LOOP
     BEGIN
      SELECT substr(fnc_cms_asset_code_value(cm_asset_code),1,120)
        INTO v_node_type_name
        FROM node_type
       WHERE node_type_id = i.node_type_id ;
     END  ;
     INSERT INTO  rpt_hierarchy
        (rpt_hierarchy_id, node_id, node_type_id, node_name,
         parent_node_id, is_deleted, parent_node_name,
         hier_level, path,description, node_type_name,
         date_created, created_by )
     VALUES ( rpt_hierarchy_pk_sq.nextval,i.node_id,i.node_type_id,i.node_name
             ,i.parent_node_id,i.is_deleted,i.parent_node_name
             ,i.hier_level,i.path,i.description,v_node_type_name
             ,SYSDATE,0
              ) ;
  END  LOOP ;
UPDATE rpt_hierarchy
   SET is_leaf = 1
 WHERE node_id IN
 (SELECT node_id
    FROM node
  MINUS
  SELECT parent_node_id
    FROM node) ;

UPDATE rpt_hierarchy
   SET is_leaf = 0
  WHERE is_leaf IS NULL ;

END  ;*/                                             -- 07/25/2012 
PROCEDURE p_rpt_hierarchy_populate(p_in_requested_user_id IN  NUMBER,
                                   p_in_start_date        IN  DATE,
                                   p_in_end_date          IN  DATE,
                                   p_out_return_code      OUT NUMBER,
                                   p_out_error_message    OUT VARCHAR2)IS
  CURSOR c_hier(v_start_date DATE,v_end_date DATE) IS
  SELECT node_id,node_type_id,
         NAME as node_name,
         parent_node_id,
         is_deleted,
         (SELECT NAME FROM node WHERE node_id = n.parent_node_id) as parent_node_name,
         level AS hier_level,
         path,
         description
    FROM node n
   WHERE --is_deleted != 1 --08/28/2013
      (v_start_date   <=  n.date_created   AND n.date_created <= v_end_date         
         OR v_start_date <=  n.date_modified  AND n.date_modified <= v_end_date)    
   START WITH parent_node_id IS NULL
     AND is_deleted = 0 -- 02/15/2018
  CONNECT BY PRIOR node_id = parent_node_id ;
  v_start_date       DATE := p_in_start_date;
  v_end_date         DATE := p_in_end_date;
  v_rpt_hierarchy_id rpt_hierarchy.rpt_hierarchy_id%TYPE;   
  v_node_type_name   VARCHAR2(120) ;
 
BEGIN

 --DELETE  rpt_hierarchy;
  FOR i IN c_hier(p_in_start_date,p_in_end_date) LOOP
   
    BEGIN
      SELECT substr(fnc_cms_asset_code_value(cm_asset_code),1,120)
        INTO v_node_type_name
        FROM node_type
       WHERE node_type_id = i.node_type_id ;
    END;
    
    BEGIN  
       SELECT rpt_hierarchy_id
         INTO v_rpt_hierarchy_id
         FROM rpt_hierarchy
        WHERE node_id = i.node_id;  
       
      UPDATE rpt_hierarchy 
         SET node_type_id = i.node_type_id, 
             node_name    = i.node_name ,
             parent_node_id = i.parent_node_id, 
             is_deleted   = i.is_deleted  , 
             parent_node_name = i.parent_node_name,
             hier_level = i.hier_level, 
             path = i.path,
             description = i.description, 
             node_type_name = v_node_type_name, 
             date_modified  = SYSDATE,
             modified_by = p_in_requested_user_id
       WHERE rpt_hierarchy_id = v_rpt_hierarchy_id;
                         
    EXCEPTION
     WHEN NO_DATA_FOUND THEN       
       INSERT INTO rpt_hierarchy(rpt_hierarchy_id, 
                                 node_id, 
                                 node_type_id, 
                                 node_name,
                                 parent_node_id, 
                                 is_deleted, 
                                 parent_node_name,
                                 hier_level, 
                                 path,
                                 description, 
                                 node_type_name,
                                 date_created, 
                                 created_by)
                          VALUES(rpt_hierarchy_pk_sq.nextval,
                                 i.node_id,
                                 i.node_type_id,
                                 i.node_name,
                                 i.parent_node_id,
                                 i.is_deleted,
                                 i.parent_node_name,
                                 i.hier_level,
                                 i.path,
                                 i.description,
                                 v_node_type_name,
                                 SYSDATE,
                                 p_in_requested_user_id);
    END;
    
  END LOOP;
  
 UPDATE rpt_hierarchy
    SET is_leaf = 1
  WHERE node_id IN(SELECT node_id
                     FROM node n
                    WHERE (v_start_date <= date_created AND date_created <= v_end_date         
                       OR v_start_date <= date_modified AND date_modified <= v_end_date)
                   MINUS
                   SELECT parent_node_id
                     FROM node
                    WHERE (v_start_date   <=  date_created AND date_created <= v_end_date         
                        OR v_start_date <= date_modified AND date_modified <= v_end_date)) ;

  UPDATE rpt_hierarchy
     SET is_leaf = 0
   WHERE is_leaf IS NULL
     AND (v_start_date <= date_created AND date_created <= sysdate         
         OR v_start_date <= date_modified AND date_modified <= sysdate);

  p_out_return_code := 0;


EXCEPTION
   WHEN OTHERS THEN
      p_out_return_code   := 99;
      p_out_error_message := SUBSTR(SQLERRM,1,250);
      prc_execution_log_entry('p_rpt_hierarchy_populate', 1, 'ERROR', 'Error. ' || SQLERRM, NULL);
END;

PROCEDURE P_PROMOTION_DETAILS
( p_promo_id IN number,
  p_out_promoname OUT VARCHAR2,
  p_out_promotype OUT VARCHAR2,
  p_out_is_taxable OUT NUMBER,
  p_out_media_type  OUT VARCHAR2) IS

/*******************************************************************************

   Purpose:  Purpose: Return promotion details that reports will use.

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray    11/07/2005  Initial packaging of common procedures

*******************************************************************************/
  v_promo_name   VARCHAR2(120) ;
  v_promo_type   promotion.promotion_type%TYPE;
  v_is_taxable   promotion.is_taxable%TYPE;
  v_media_type   VARCHAR2(120) ;

BEGIN

   /* MERGE INTO rpt_refresh_date B
    USING(SELECT 'promotion' as report_name, 'details' as report_type, sysdate as refresh_date
          FROM dual) E
    ON (B.report_name = e.report_name and b.report_type = e.report_type)
    WHEN MATCHED THEN
        UPDATE SET B.refresh_date = e.refresh_date
    WHEN NOT MATCHED THEN
        INSERT (B.report_name, b.report_type, b.refresh_date)
        VALUES (E.report_name, E.report_type, e.refresh_date);
*/
  SELECT p.promotion_name, p.promotion_type, p.is_taxable, p.award_type
    INTO v_promo_name,v_promo_type,v_is_taxable,v_media_type
    FROM promotion p
   WHERE promotion_id = p_promo_id ;

  p_out_promoname := v_promo_name ;
  p_out_promotype := v_promo_type;
  p_out_is_taxable := v_is_taxable;
  p_out_media_type := v_media_type ;

EXCEPTION
  WHEN OTHERS THEN
    prc_execution_log_entry('p_promotion_details',1,'ERROR',SQLERRM,null);
    p_out_promoname := NULL ;
    p_out_promotype := NULL;
    p_out_is_taxable := NULL;
    p_out_media_type := NULL ;
END  ; -- p_promotion_details
--------------------------------------------------------------------------------

PROCEDURE P_USER_DETAILS
 (p_in_user_id IN number, -- in parm
  p_out_pax_name OUT VARCHAR2 ,
  p_out_first_name OUT VARCHAR2,
  p_out_middle_name OUT VARCHAR2,
  p_out_last_name OUT VARCHAR2,
  p_out_node_name OUT VARCHAR2 ,
  p_out_node_id OUT NUMBER ,
  p_out_hierarchy_path OUT VARCHAR2 ,
  p_out_hierarchy_id OUT NUMBER ,
  p_out_hierarchy_name OUT VARCHAR2 ,
  p_out_pax_current_status OUT VARCHAR2,
  p_out_user_name OUT VARCHAR2,
  p_out_position_type OUT VARCHAR2,
  p_out_department_type OUT VARCHAR2) IS

/*******************************************************************************

   Purpose:  Purpose: Return application_user, node, and user_employer details
             that reports will use.

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray    11/07/2005  Initial packaging of common procedures
   D Murray    12/16/2005  changed pax_status to be active or inactive rather than
                           Active or Inactive.
   S Majumder  10/24/2007  Bug # 17653: There may not be any employer attached to participant and
                           a participant belongs to a department until it reaches the termination date    
   M Lindvig   07/28/2008  Bug # 20648: Fixed Code using Vijay's code for US Bank                     
*******************************************************************************/

  v_employer_count  NUMBER(10);
BEGIN
  BEGIN

     /* MERGE INTO rpt_refresh_date B
      USING(SELECT 'user' as report_name, 'details' as report_type, sysdate as refresh_date
            FROM dual) E
    ON (B.report_name = e.report_name and b.report_type = e.report_type)
    WHEN MATCHED THEN
        UPDATE SET B.refresh_date = e.refresh_date
    WHEN NOT MATCHED THEN
        INSERT (B.report_name, b.report_type, b.refresh_date)
        VALUES (E.report_name, E.report_type, e.refresh_date);
*/
    SELECT initcap(last_name)||', '||initcap(first_name)
           || decode(ltrim(rtrim(middle_name)),null,null,(' '||ltrim(rtrim(middle_name))))
           || decode(ltrim(rtrim(suffix)),null,null,(' '||ltrim(rtrim(suffix)))) pax_name,
           initcap(first_name),
           ltrim(rtrim(middle_name)),
           initcap(last_name),
           un.node_id,
           decode(is_active,1,'active','inactive') pax_status,
           user_name
     INTO p_out_pax_name,p_out_first_name,p_out_middle_name,p_out_last_name,
          p_out_node_id,p_out_pax_current_status, p_out_user_name
     FROM application_user a,
          user_node un
     WHERE a.user_id = un.user_id
       AND a.user_id = p_in_user_id
       AND ROWNUM = 1 ;
  EXCEPTION
    WHEN OTHERS  THEN
     prc_execution_log_entry('P_USER_DETAILS',1,'ERROR','Error getting User info :'||SQLERRM,null);
     p_out_pax_name := NULL ;
     p_out_first_name := NULL;
     p_out_middle_name := NULL;
     p_out_last_name := NULL;
     p_out_node_id := NULL ;
     p_out_pax_current_status := NULL;
     p_out_user_name := NULL;
     RETURN ;
  END  ;
  BEGIN
   SELECT name,hierarchy_id,path,
          (SELECT fnc_cms_asset_code_value(cm_asset_code)
             FROM HIERARCHY WHERE is_primary = 1 ) hierarchy_name
     INTO p_out_node_name,p_out_hierarchy_id,p_out_hierarchy_path,
          p_out_hierarchy_name
     FROM node
    WHERE node_id = p_out_node_id ;
  EXCEPTION
     WHEN OTHERS THEN
       prc_execution_log_entry('P_USER_DETAILS',1,'ERROR','Error getting Hierarchy info :'||SQLERRM,null);
       p_out_node_name := null ;
       p_out_hierarchy_id := null ;
       p_out_hierarchy_path := NULL;
       p_out_hierarchy_name := null ;
  END ;

  BEGIN
  
   SELECT count(1)
     INTO v_employer_count   
     FROM participant_employer
    WHERE user_id = p_in_user_id
      AND (termination_date IS NULL OR termination_date >= trunc(SYSDATE));

   IF v_employer_count > 0 THEN         
      SELECT position_type, department_type
        INTO p_out_position_type, p_out_department_type
        FROM participant_employer
       WHERE user_id = p_in_user_id
         AND (termination_date IS NULL OR termination_date >= trunc(SYSDATE))
         AND ROWNUM = 1;
   ELSE
    BEGIN
      SELECT position_type, department_type
        INTO p_out_position_type, p_out_department_type
        FROM participant_employer
      WHERE user_id = p_in_user_id
        AND participant_employer_index =(SELECT MAX (participant_employer_index)
                                        FROM participant_employer
                                        WHERE user_id = p_in_user_id);
    EXCEPTION WHEN OTHERS THEN
        p_out_position_type := NULL;
        p_out_department_type := NULL;
    
    END;
   END IF;      
  EXCEPTION
     WHEN OTHERS THEN
       prc_execution_log_entry('P_USER_DETAILS',1,'ERROR','Error getting Participant Employer info :'||SQLERRM,null);
       p_out_position_type := null ;
  END ;

END  ; -- p_user_details
--------------------------------------------------------------------------------

FUNCTION FNC_GET_PERIOD_DATE
  ( p_in_date         IN DATE,
    p_in_period_type  IN VARCHAR2)
  RETURN VARCHAR2 IS

v_period_range    VARCHAR2(100);
v_ind             calendar.week_ind%TYPE;

BEGIN

    SELECT distinct DECODE(LOWER(p_in_period_type),'weekly',week_ind,
                                                   'monthly',month_ind,
                                                   'quarterly', quarter_ind,
                                                   'annually', null)
      into v_ind
    FROM calendar
    WHERE TRUNC(time_key) = TRUNC(p_in_date);

    SELECT DECODE(LOWER(p_in_period_type),'weekly', to_char(min(weekdaybeg),'DD-MON-YYYY')||' - '||to_char(max(weekdayend),'DD-MON-YYYY'),
                                                    to_char(min(time_key),'DD-MON-YYYY')||' - '||to_char(max(time_key),'DD-MON-YYYY'))
      into v_period_range
    FROM calendar
    WHERE DECODE(LOWER(p_in_period_type),'weekly',week_ind,
                                         'monthly',month_ind,
                                         'quarterly', quarter_ind,
                                         'annually', null)  =   V_IND
      AND to_char(time_key,'yyyy') = to_char(p_in_date,'yyyy');


    RETURN  v_period_range;

EXCEPTION WHEN OTHERS THEN
  v_period_range:=null;
  RETURN v_period_range;
END;
--------------------------------------------------------------------------------

PROCEDURE PRC_INSERT_CHAR_LOOKUP IS

/*******************************************************************************

   Purpose:  Populate the rpt_characteristic_lookup table

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   Percy M.     01/18/2006  Initial Creation
                            Insert statement was moved from the build xml script into this procedure
                            because it should be run as part of report refresh.
   Chidamba      11/27/2012  Increase from 5 to 15 since 10 more characteristic is increased   
   Ravi Dhanekula 07/21/2015 Bug # 63339 Inactivated characteristics are appearing in Login activity extract report 
   Gorantla       09/27/2017  JIRA# G6-3018 Modified reports and extract to mask pax characteristics based on do not show flag 
*******************************************************************************/

BEGIN

  DELETE rpt_characteristic_lookup;

  INSERT INTO rpt_characteristic_lookup
  (SELECT * FROM
    (   SELECT characteristic_type,characteristic_id, cm_asset_code,CHARACTERISTIC_DATA_TYPE,
            row_number() over (partition by characteristic_type ORDER BY instr('dateintbooleantxtmulti_selectsingle_select',characteristic_data_type)) display_order
       FROM characteristic WHERE is_active = 1 --Bug # 63339 07/21/2015
        AND characteristic_id NOT IN (SELECT characteristic_id                -- 09/27/2017
                                        FROM characteristic                   -- 09/27/2017
                                       WHERE visibility = 'hidden'            -- 09/27/2017
                                         AND characteristic_type = 'USER')    -- 09/27/2017
    )
  WHERE display_order <= 20);            --11/27/2012   --03/14/2017

END;
--------------------------------------------------------------------------------
PROCEDURE P_RPT_HIERARCHY_SUMMARY(p_in_requested_user_id IN  NUMBER,
                                  p_in_start_date        IN  DATE,
                                  p_in_end_date          IN  DATE,
                                  p_out_return_code      OUT NUMBER,
                                  p_out_error_message    OUT VARCHAR2)IS
/*******************************************************************************

   Purpose:  Populate the hierarchy_summary reporting table.  This can be used for
             any summary report that needs the hierarchy (drill-down) structure
             for iReports that does not depend on any other data.  If data counts
             are being performed by functions, rather than in the summary report,
             use this.  It will only need to be run once for all reports that use
             it.  (example:  Nominations are using this)  Future reports can use it too.

   Person        Date        Comments
   -----------   ----------  -----------------------------------------------------
   D Murray      05/08/2006  Initial Creation
   Chidamba      07/25/2012   G5 Report Changes to Incremental approach
*******************************************************************************/

   CURSOR cur_hier IS
     SELECT h.hier_level, h.parent_node_id, h.node_id, h.is_leaf
       FROM rpt_hierarchy h
      WHERE (p_in_start_date <=  h.date_created   AND h.date_created <= SYSDATE         
         OR  p_in_start_date <=  h.date_modified  AND h.date_modified <= SYSDATE)   
      ORDER BY h.hier_level desc, 
               h.parent_node_id, 
               h.node_id;

  -- EXECUTION LOG VARIABLES
  v_process_name      execution_log.process_name%type  := 'p_rpt_hierarchy_summary';
  v_release_level     execution_log.release_level%type := '1';
  v_stage             VARCHAR2(200);
  v_rec_count         NUMBER;  
  v_commit_cnt        INTEGER := 0;
  v_parm_list     	  execution_log.text_line%TYPE;   --03/11/2019  Bug 78631

BEGIN
v_stage := 'initialize variables'; 
v_parm_list := 
'p_in_requested_user_id :='||p_in_requested_user_id ||CHR(10)||
'p_in_start_date        :='||p_in_start_date        ||CHR(10)|| 
'p_in_end_date          :='||p_in_end_date          ; 
 /* v_stage := 'delete rpt_hierarchy_summary table';   --07/25/2012  
  DELETE  RPT_HIERARCHY_SUMMARY;

  v_stage := 'insert data into rpt_refresh_date table';
  MERGE INTO RPT_REFRESH_DATE B
    USING (
        SELECT 'hierarchy' AS report_name, 'summary' AS report_type, SYSDATE AS refresh_date
        FROM dual) E
    ON (B.report_name = e.report_name AND b.report_type = e.report_type)
    WHEN MATCHED THEN
        UPDATE SET B.refresh_date = e.refresh_date
    WHEN NOT MATCHED THEN
        INSERT (B.report_name, b.report_type, b.refresh_date)
        VALUES (E.report_name, E.report_type, e.refresh_date) ;
   */     

  v_stage := 'Hierarchy Loop';
  FOR rec_hier IN cur_hier LOOP

    SELECT COUNT(*)                 
      INTO v_rec_count
      FROM rpt_hierarchy_summary
     WHERE detail_node_id =  rec_hier.node_id;
   
    IF v_rec_count = 2  THEN 
    
      UPDATE rpt_hierarchy_summary
         SET header_node_id = rec_hier.parent_node_id,             
             hier_level     = rec_hier.hier_level,
             is_leaf        = rec_hier.is_leaf,        
             date_modified  = SYSDATE,
             modified_by    = p_in_requested_user_id
       WHERE detail_node_id =  rec_hier.node_id;       
    END IF;
    
    IF v_rec_count = 0  THEN       
        -- Add all teamsum records per hier_level
        v_stage := 'Insert teamsum record into rpt_hierarchy_summary table for header_node: '||
                    rec_hier.parent_node_id||' and detail_node: '||rec_hier.node_id;
        INSERT INTO rpt_hierarchy_summary
           (rpt_hierarchy_summary_id,
            record_type,
            header_node_id,
            detail_node_id,
            hier_level,
            is_leaf,        
            date_created,
            created_by)
        VALUES
           (rpt_hierarchy_summary_pk_sq.NEXTVAL,
            rec_hier.hier_level||'-teamsum',
            rec_hier.parent_node_id,
            rec_hier.node_id,
            rec_hier.hier_level,
            1,  -- all teamsum records are 1
            SYSDATE,
            p_in_requested_user_id);

        v_commit_cnt := v_commit_cnt + 1;

        -- Add all nodesum records per hier_level
        v_stage := 'Insert nodesum record into rpt_hierarchy_summary table for header_node: '||
                    rec_hier.parent_node_id||' and detail_node: '||rec_hier.node_id;
        INSERT INTO rpt_hierarchy_summary
           (rpt_hierarchy_summary_id,
            record_type,
            header_node_id,
            detail_node_id,
            hier_level,
            is_leaf,
            date_created,
            created_by)
        VALUES
           (rpt_hierarchy_summary_pk_sq.NEXTVAL,
            rec_hier.hier_level||'-nodesum',
            rec_hier.parent_node_id,
            rec_hier.node_id,
            rec_hier.hier_level,
            rec_hier.is_leaf,  -- nodesum records should use the is_leaf value
            SYSDATE,
            p_in_requested_user_id);
                  
        v_commit_cnt := v_commit_cnt + 1;
     
        IF v_commit_cnt > 500 THEN
        --  COMMIT;
          v_commit_cnt := 0;
        END IF;
        
    END IF;
  END LOOP; -- cur_hier
  --COMMIT;--Commented out as we want to run the report refresh as one single transaction
  p_out_return_code := 00;
  
EXCEPTION
   WHEN OTHERS THEN
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',v_parm_list||'-->'||'Error at stage: '||
                             v_stage||' --> '||SQLERRM,null);
    p_out_return_code := 99;
    p_out_error_message := v_stage||' - '||SQLERRM;
   --  COMMIT;

END; -- p_rpt_hierarchy_summary

PROCEDURE p_rpt_hierarchy_rollup IS
/*******************************************************************************
   Purpose:  Maintain the rpt_hierarchy_rollup table.
             This process creates a parent/child record for each child node in parent node's chain.
             Example: Every node is a child node in the root node's chain,
                      so the rollup table contains a record with each node associated with the root node.

   Person      Date        Comments
   ----------- ----------  -----------------------------------------------------
   J Flees     04/11/2013  Customization: Dexter 23301: Organizational Summary Report
                           Added report hierarchy rollup refresh process.
*******************************************************************************/
   c_created_by         CONSTANT rpt_hierarchy_rollup.created_by%TYPE := 0;
   v_process_name       execution_log.process_name%TYPE  := UPPER('p_rpt_hierarchy_rollup');
   v_release_level      execution_log.release_level%TYPE := '1';
   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;

BEGIN
   v_stage := 'Start';
   prc_execution_log_entry(v_process_name, v_release_level, 'INFO', v_stage, NULL);

   -- refresh report dates
--   v_stage := 'MERGE rpt_refresh_date';
--   MERGE INTO RPT_REFRESH_DATE B
--   USING ( SELECT 'hierarchy' AS report_name, 'rollup' AS report_type, SYSDATE AS refresh_date FROM dual
--         ) E
--      ON (B.report_name = E.report_name AND b.report_type = E.report_type)
--    WHEN MATCHED THEN
--      UPDATE SET B.refresh_date = E.refresh_date
--    WHEN NOT MATCHED THEN
--      INSERT (B.report_name, b.report_type, b.refresh_date)
--      VALUES (E.report_name, E.report_type, E.refresh_date);

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(v_process_name, v_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   -- add any new parent/child node records
   v_stage := 'MERGE rpt_hierarchy_rollup';
   MERGE INTO rpt_hierarchy_rollup d
   USING (  -- build node/child list based on the node connect by path
            SELECT pv.COLUMN_VALUE AS node_id,
                   np.node_id AS child_node_id
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
                   ) AS sys.odcinumberlist ) ) pv
         ) s
      ON (   d.node_id = s.node_id
         AND d.child_node_id = s.child_node_id
         )
    WHEN NOT MATCHED THEN
      INSERT
      (  node_id,
         child_node_id,
         created_by,
         date_created
      )
      VALUES
      (  s.node_id,
         s.child_node_id,
         c_created_by,
         SYSDATE
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(v_process_name, v_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   -- remove obsolete parent/child node records
   v_stage := 'DELETE obsolete rpt_hierarchy_rollup';
   DELETE rpt_hierarchy_rollup
    WHERE ROWID IN
          ( SELECT obs.hr_rowid
              FROM ( -- determine obsolete records by joining the build list to the rollup table
                     SELECT hr.ROWID AS hr_rowid,
                            b.node_id,
                            b.child_node_id
                       FROM ( -- build node/child list based on the node connect by path
                              SELECT pv.COLUMN_VALUE AS node_id,
                                     np.node_id AS child_node_id
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
                                     ) AS sys.odcinumberlist ) ) pv
                            ) b,
                            rpt_hierarchy_rollup hr
                         -- outer join build records to find obsolete table records
                      WHERE hr.node_id = b.node_id (+)
                        AND hr.child_node_id = b.child_node_id (+)
                   ) obs
                -- table record not matched to build list
             WHERE obs.node_id IS NULL
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(v_process_name, v_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   v_stage := 'Success';
   prc_execution_log_entry(v_process_name, v_release_level, 'INFO', v_stage, NULL);

EXCEPTION
   WHEN OTHERS THEN
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR','Error at stage: '||
                             v_stage||' --> '||SQLERRM,NULL);

END p_rpt_hierarchy_rollup;
-- 04/11/2013 End

PROCEDURE p_rpt_pax_employer(p_in_requested_user_id IN  NUMBER,                                 
                                    p_out_return_code      OUT NUMBER,
                                    p_out_error_message    OUT VARCHAR2) IS
/*******************************************************************************
   Purpose:  Maintain rpt_participant_employer table.

   Person          Date            Comments
   -----------     ----------      -----------------------------------------------------
   Ravi Dhanekula  06/12/2014      Initial  
   murphyc         06/17/2014      add NVLs around WHERE condition compares
*******************************************************************************/
 c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_pax_employer');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 5.3;

   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;
                                    
BEGIN

MERGE INTO RPT_PARTICIPANT_EMPLOYER d
USING (
SELECT              -- get most recent participant employer record per user
         r."REC_RANK",
          r."USER_ID",
          r."EMPLOYER_ID",         
          r."POSITION_TYPE",
          r."DEPARTMENT_TYPE",
          r."HIRE_DATE",
          r."TERMINATION_DATE",
          r."CREATED_BY",
          r."DATE_CREATED",
          r."MODIFIED_BY",
          r."DATE_MODIFIED"        
     FROM ( -- rank records by termination date and employer index in reverse order
           SELECT RANK ()
                  OVER (
                     PARTITION BY pe.user_id
                     ORDER BY
                        pe.termination_date DESC,
                        pe.participant_employer_index DESC)
                     AS rec_rank,
                  pe.*
             FROM participant_employer pe) r            
    -- the current employment record has the lowest ranking  
    WHERE r.rec_rank = 1) s    
    ON (s.user_id = d.user_id)
    WHEN MATCHED THEN
      UPDATE SET
         d.position_type = s.position_type,
         d.department_type =   s.department_type,
         d.hire_date =       s.hire_date,
         d.termination_date  =            s.termination_date,        
         d.modified_by        = p_in_requested_user_id,
         d.date_modified      = SYSDATE         
       WHERE ( -- only update records with different values
            DECODE(NVL(d.position_type,'x'),                 NVL(s.position_type,'x'),1,0) = 0
         OR DECODE(NVL(d.department_type,'x'),               NVL(s.department_type,'x'),1,0) = 0
         OR DECODE(NVL(d.hire_date,TRUNC(SYSDATE)),          NVL(s.hire_date,TRUNC(SYSDATE)),1,0) = 0
         OR DECODE(NVL(d.termination_date,TRUNC(SYSDATE)),NVL(s.termination_date,TRUNC(SYSDATE)),1,0) = 0              
             )   
    WHEN NOT MATCHED THEN
      INSERT
      ( user_id,
        employer_id, 
        position_type,
        department_type,
        hire_date,
        termination_date,
        created_by,
       date_created
      )
      VALUES
      ( s.user_id,
        s.employer_id, 
        s.position_type,
        s.department_type,
        s.hire_date,
        s.termination_date,
        p_in_requested_user_id,       
        SYSDATE   
      );

 v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

 p_out_return_code := 0;
  p_out_error_message := NULL;
EXCEPTION
   WHEN OTHERS THEN
      p_out_return_code   := 99;
      p_out_error_message := SUBSTR(SQLERRM,1,250);
      prc_execution_log_entry(c_process_name, c_release_level,  'ERROR',  'Param:'||p_in_requested_user_id||':'||'Error. ' || SQLERRM, NULL);

END p_rpt_pax_employer;    

-- 04/22/2016 begin block add
-----------------------------
-- Returns the hierarchy root node (node with no parent)
FUNCTION f_get_root_node
  RETURN NUMBER
  RESULT_CACHE
  RELIES_ON(rpt_hierarchy)
IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := 'f_get_root_node';
   v_stage              execution_log.text_line%TYPE;
   v_root_node_id       rpt_hierarchy.node_id%TYPE;

   CURSOR cur_get_root_node IS
   SELECT h.node_id
     FROM rpt_hierarchy h
    WHERE h.parent_node_id IS NULL
      AND h.is_deleted = 0
      ;
BEGIN
   v_stage := 'OPEN cur_get_root_node';
   OPEN cur_get_root_node;
   v_stage := 'FETCH cur_get_root_node';
   FETCH cur_get_root_node INTO v_root_node_id;
   CLOSE cur_get_root_node;

   RETURN v_root_node_id;
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END f_get_root_node;

-----------------------------
-- Returns the default locale.
-- Returns the en_US when locale data not found.
FUNCTION f_get_default_locale
  RETURN VARCHAR2
  RESULT_CACHE
  RELIES_ON(os_propertyset)
IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := 'f_get_default_locale';
   v_stage              execution_log.text_line%TYPE;
   v_default_locale     os_propertyset.string_val%TYPE;

   CURSOR cur_get_default_locale IS
   SELECT osp.string_val
     FROM os_propertyset osp
    WHERE osp.entity_name = 'default.language'
      ;

BEGIN
   v_stage := 'OPEN cur_get_default_locale';
   OPEN cur_get_default_locale;
   v_stage := 'FETCH cur_get_default_locale';
   FETCH cur_get_default_locale INTO v_default_locale;
   CLOSE cur_get_default_locale;

   IF (v_default_locale IS NULL) THEN
      v_default_locale := 'en_US';
   END IF;

   RETURN v_default_locale;
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END f_get_default_locale;

-----------------------------
-- Returns the date pattern based on the input locale.
-- Returns the default date pattern when locale data not found.
FUNCTION f_get_locale_date_pattern
( p_in_locale     IN locale_date_pattern.locale%TYPE
) RETURN VARCHAR2
  RESULT_CACHE
  RELIES_ON(locale_date_pattern)
IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := 'f_get_locale_date_pattern';
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_pattern            locale_date_pattern.pattern%TYPE;

   CURSOR cur_locale_date_pattern IS
   SELECT rd.pattern
     FROM ( -- get raw data
            SELECT ldp.pattern,
                   1 AS rec_seq
              FROM locale_date_pattern ldp
             WHERE ldp.locale = p_in_locale
             UNION ALL
            SELECT gc_default_date_pattern AS pattern,
                   99 AS rec_seq
              FROM dual
          ) rd
    ORDER BY rd.rec_seq
      ;
   
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_locale >' || p_in_locale
      || '<~';

   v_stage := 'OPEN cur_locale_date_pattern';
   OPEN cur_locale_date_pattern;
   v_stage := 'FETCH cur_locale_date_pattern';
   FETCH cur_locale_date_pattern INTO v_pattern;
   CLOSE cur_locale_date_pattern;

   RETURN v_pattern;
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END f_get_locale_date_pattern;

-----------------------------
-- Returns the currency code associated with the user's primary address country.
-- Defaults to the database currency when no data found for user/country.
FUNCTION f_get_user_currency_code
( p_in_user_id    IN application_user.user_id%TYPE
) RETURN VARCHAR2
IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := 'f_get_user_currency_code';
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_currency_code      country.currency_code%TYPE;

   CURSOR cur_get_user_currency_code IS
   SELECT rd.currency_code
     FROM ( -- get raw data
            -- get user currency
            SELECT c.currency_code,
                   1 AS rec_seq
              FROM user_address ua,
                   country c
             WHERE ua.user_id = p_in_user_id
               AND ua.is_primary = 1
               AND ua.country_id = c.country_id
               AND c.currency_code IS NOT NULL
             UNION ALL
            -- get database currency
            SELECT gc_database_currency AS currency_code,
                   99 AS rec_seq
              FROM dual
          ) rd
    ORDER BY rd.rec_seq
      ;
   
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_user_id >' || p_in_user_id
      || '<~';

   v_stage := 'OPEN cur_get_user_currency_code';
   OPEN cur_get_user_currency_code;
   v_stage := 'FETCH cur_get_user_currency_code';
   FETCH cur_get_user_currency_code INTO v_currency_code;
   CLOSE cur_get_user_currency_code;

   RETURN v_currency_code;
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;
END f_get_user_currency_code;

-----------------------------
-- Stages the input parse list into the ID list temp table.
PROCEDURE p_stage_search_criteria
( p_in_parse_list             IN VARCHAR2,
  p_in_ref_text_1             IN VARCHAR2,
  p_in_is_number_list         IN NUMBER      DEFAULT 0,
  p_in_search_all_values      IN VARCHAR2    DEFAULT pkg_const.gc_search_all_values,
  p_in_null_id                IN NUMBER      DEFAULT pkg_const.gc_null_id
) IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := 'p_stage_search_criteria';
   e_empty_list         EXCEPTION;
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_parse_list >' || p_in_parse_list
      || '<, p_in_ref_text_1 >' || p_in_ref_text_1
      || '<, p_in_is_number_list >' || p_in_is_number_list
      || '<, p_in_search_all_values >' || p_in_search_all_values
      || '<, p_in_null_id >' || p_in_null_id
      || '<~';

   IF (p_in_parse_list IS NULL) THEN
      RAISE e_empty_list;
   END IF;

   v_stage := 'MERGE gtt_id_list';
   MERGE INTO gtt_id_list d
   USING (  -- build ID list
            SELECT CASE
                     -- number list (which can contain the search all string value)
                     WHEN (p_in_is_number_list = 1 AND t.column_value != p_in_search_all_values) THEN TO_NUMBER(t.column_value)
                     ELSE p_in_null_id
                   END AS id,
                   ROWNUM AS rec_seq,
                   p_in_ref_text_1 AS ref_text_1,
                   -- string list (which can contain the search all string value from the number list)
                   CASE
                     WHEN (t.column_value = p_in_search_all_values) THEN t.column_value
                     WHEN (p_in_is_number_list = 0) THEN t.column_value
                   END AS ref_text_2
              FROM TABLE(get_array_varchar(p_in_parse_list)) t
         ) s
      ON (   d.ref_text_1 = s.ref_text_1
         AND d.rec_seq = s.rec_seq
         )
    WHEN NOT MATCHED THEN
      INSERT
      (id,
       rec_seq,
       ref_text_1,
       ref_text_2
      )
      VALUES
      (s.id,
       s.rec_seq,
       s.ref_text_1,
       s.ref_text_2
      );

EXCEPTION
   WHEN e_empty_list THEN
      -- nothing to parse
      NULL;
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;

END p_stage_search_criteria;
-- 04/22/2016 end block add

-- 03/06/2017 begin block add
-----------------------------
-- Builds a fresh version of the rpt_hierarchy node/hier_level fields
-- in the temp ID list table: gtt_id_list (gtt).
--    gtt.id        equates to node_id
--    gtt.ref_nbr_2 equates to hier_level
-- Note: The input node ID list resticts the stage records. A null input builds the entire hierarchy list.
PROCEDURE p_stage_node_hier_level
( p_in_node_id_list           IN VARCHAR2,
  p_in_ref_text_1             IN VARCHAR2    DEFAULT pkg_const.gc_ref_text_node_hier_level
) IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_stage_node_hier_level');
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_node_id_list       VARCHAR2(4000);
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_node_id_list >' || p_in_node_id_list
      || '<, p_in_ref_text_1 >' || p_in_ref_text_1
      || '<~';

   v_node_id_list := p_in_node_id_list;
   IF (v_node_id_list IS NULL) THEN
      -- default to the root node
      v_node_id_list := f_get_root_node;
   END IF;

   v_stage := 'INSERT gtt_id_list';
   INSERT INTO gtt_id_list
   ( id,
     rec_seq,
     ref_text_1,
     ref_nbr_1
   )
   (  -- build list
      SELECT h.node_id,
             ROWNUM AS rec_seq,
             p_in_ref_text_1,
             h.hier_level
        FROM ( -- build rpt hierarchy level
               SELECT DISTINCT
                      nhp.node_id,
                      nhp.hier_level
                 FROM ( -- get node hierarchy path
                        SELECT n.node_id,
                               LEVEL AS hier_level,
                               sys_connect_by_path(n.node_id, '/') || '/' AS node_path
                          FROM node n
                         START WITH n.parent_node_id IS NULL
                       CONNECT BY PRIOR n.node_id = n.parent_node_id
                      ) nhp,
                      ( -- get parent node restictor
                        SELECT '%/' || ga.column_value || '/%' AS node_id
                          FROM TABLE(get_array(v_node_id_list)) ga
                      ) pn
                   -- restrict to nodes in parent's hierarchy
                WHERE nhp.node_path LIKE pn.node_id
             ) h
   );

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;

END p_stage_node_hier_level;

-----------------------------
-- Builds a fresh version of the rpt_hierarchy_rollup table in the temp ID list table: gtt_id_list (gtt).
--    gtt.id        equates to node_id
--    gtt.ref_nbr_2 equates to child_node_id
-- Note: The input node ID list resticts the stage records. A null input builds the entire rollup list.
PROCEDURE p_stage_hierarchy_rollup
( p_in_node_id_list           IN VARCHAR2,
  p_in_ref_text_1             IN VARCHAR2    DEFAULT pkg_const.gc_ref_text_hierarchy_rollup
) IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_stage_hierarchy_rollup');
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_node_id_list >' || p_in_node_id_list
      || '<, p_in_ref_text_1 >' || p_in_ref_text_1
      || '<~';

   v_stage := 'INSERT gtt_id_list';
   INSERT INTO gtt_id_list
   ( id,
     rec_seq,
     ref_text_1,
     ref_nbr_1
   )
   (  -- build rpt hierarchy rollup
      SELECT pv.COLUMN_VALUE AS node_id,
             ROWNUM AS rec_seq,
             p_in_ref_text_1,
             np.node_id AS child_node_id
        FROM ( -- get node hierarchy path
               SELECT n.node_id,
                      LEVEL AS hier_level,
                      sys_connect_by_path(n.node_id, '/') || '/' AS node_path
                 FROM node n
                START WITH n.parent_node_id IS NULL
              CONNECT BY PRIOR n.node_id = n.parent_node_id
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
             ) AS sys.odcinumberlist ) ) pv
       WHERE (p_in_node_id_list IS NULL
             OR (pv.COLUMN_VALUE IN
                   ( SELECT ga.column_value AS node_id
                       FROM TABLE(get_array(p_in_node_id_list)) ga))
             )
   );

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      RAISE;

END p_stage_hierarchy_rollup;
-- 03/06/2017 end block add

-- 12/15/2016 begin block add
-----------------------------
-- Sequences user connection records for each sender/receiver by latest to earliest date.
-- Retains the latest records per sender/receiver up to the retension quantity and deletes the rest. 
PROCEDURE p_user_connection_purge
( p_in_rec_retension_qty      IN NUMBER,
  p_out_return_code           OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_user_connection_purge');
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_rec_retension_qty >' || p_in_rec_retension_qty
      || '<~';

   v_stage := 'DELETE user_connections (sender)';
   DELETE user_connections uc
    WHERE uc.id IN
          ( -- get obsolete sender connections
            SELECT con.id
              FROM ( -- sequence records per sending user
                     SELECT uc.id,
                            uc.sender_id,
                            ROW_NUMBER() OVER (PARTITION BY uc.sender_id
                                               ORDER BY uc.date_created DESC, uc.id DESC) AS rec_seq
                       FROM user_connections uc
                   ) con
                -- only records in excess of the retension level
             WHERE con.rec_seq > p_in_rec_retension_qty
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, gc_release_level, gc_info, v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   v_stage := 'DELETE user_connections (receiver)';
   DELETE user_connections uc
    WHERE uc.id IN
          ( -- get obsolete receiver connections
            SELECT con.id
              FROM ( -- sequence records per receiving user
                     SELECT uc.id,
                            uc.receiver_id,
                            ROW_NUMBER() OVER (PARTITION BY uc.receiver_id
                                               ORDER BY uc.date_created DESC, uc.id DESC) AS rec_seq
                       FROM user_connections uc
                   ) con
                -- only records in excess of the retension level
             WHERE con.rec_seq > p_in_rec_retension_qty
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, gc_release_level, gc_info, v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   p_out_return_code := 0;

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      p_out_return_code   := 99;

END p_user_connection_purge;
-- 12/15/2016 end block add

--------------------------------------------------------------------------------
END pkg_report_common;
/
