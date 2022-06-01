CREATE OR REPLACE PACKAGE pkg_report_badge  IS

  PROCEDURE P_RPT_BADGE_DETAIL
  (p_user_id       IN NUMBER,
   p_start_date    IN DATE, 
   p_end_date      IN DATE, 
   p_return_code   OUT NUMBER,
   p_out_error_message OUT VARCHAR2);

  PROCEDURE P_RPT_BADGE_SUMMARY
  (p_user_id       IN NUMBER,
   p_start_date    IN DATE, 
   p_end_date      IN DATE, 
   p_return_code   OUT NUMBER,
   p_error_message OUT VARCHAR2);
   
   FUNCTION fnc_get_badges_count
               (p_job_position IN VARCHAR2,
               p_in_node_id IN NUMBER,
                p_department IN VARCHAR2,
                p_pax_status IN VARCHAR2,
                p_in_from_date IN VARCHAR2,
                p_in_to_date IN VARCHAR2,
                locale IN VARCHAR2)
                  RETURN NUMBER;
END; -- Package spec

/
CREATE OR REPLACE PACKAGE BODY      pkg_report_badge
IS
 FUNCTION fnc_get_badges_count
               (p_job_position IN VARCHAR2,
               p_in_node_id IN NUMBER,
                p_department IN VARCHAR2,
                p_pax_status IN VARCHAR2,
                p_in_from_date IN VARCHAR2,
                p_in_to_date IN VARCHAR2,
                locale IN VARCHAR2)
                return number is
                p_count  NUMBER:=0;
                BEGIN
                
               SELECT count(rad.participant_badge_id) INTO p_count 
               FROM rpt_badge_detail rad
               WHERE
                (rad.department IN (SELECT * FROM TABLE(get_array_varchar(p_department)))
           OR (p_department is NULL))
               AND rad.position_type              = NVL(p_job_position, rad.position_type) 
               AND rad.participant_current_status = NVL(p_pax_status, rad.participant_current_status) 
               AND NVL(TRUNC(rad.earned_date), TRUNC(SYSDATE)) BETWEEN fnc_locale_to_date_dt(p_in_from_date,locale) AND fnc_locale_to_date_dt(p_in_to_date,locale) 
               and rad.node_id in (select node_id from rpt_hierarchy connect by prior node_id = parent_node_id start with node_id = p_in_node_id);
               
               return p_count;
              END;
PROCEDURE P_RPT_BADGE_DETAIL
 (p_user_id      IN  NUMBER,
  p_start_date             IN  DATE,
  p_end_date               IN  DATE,
  p_return_code           OUT NUMBER,
  p_out_error_message         OUT VARCHAR2)
  IS

/*******************************************************************************
   Purpose:  Populate the rpt_badge_detail reporting tables

   Person                 Date          Comments
   -----------            ----------    ------------------------------
   Ravi Dhanekula         11/19/2012    Creation
                          12/13/2013    Fixed the bug # 50271
   Swati                  06/10/2014    Fixed the Bug 53813 - Badge Activity Report - Promotion Name        
  Suresh J                  07/21/2014    Fixed the issue related to the the previous Bug fix 53813
  nagarajs                08/01/2014    Fixed the issue related to the the previous Bug fix 53813
  Ravi Dhanekula          01/27/2015    Bug # 59326. Added a fix to include file load badges in the report.
 Suresh J                01/29/2015   Bug #57300 - Fixed the issue of Node Name Changes that are not reflected in reports                             
******************************************************************************/ 
            
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('P_RPT_BADGE_DETAIL');
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  
  v_stage                   execution_log.text_line%TYPE;
  v_rec_cnt                 INTEGER;
  v_RPT_BADGE_DETAIL_ID   rpt_badge_detail.RPT_BADGE_DETAIL_ID%TYPE;
   v_tab_node_id        dbms_sql.number_table;   --01/29/2015  
   v_tab_node_name      dbms_sql.varchar2_table;  --01/29/2015  

  
    --Cursor to pick modified node name   
  CURSOR cur_node_changed IS          --01/29/2015
    SELECT node_id,
           NAME
      FROM node
     WHERE date_modified BETWEEN p_start_date AND p_end_date;  
     
BEGIN

  v_stage := 'Start';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
  
  
  MERGE INTO rpt_badge_detail rpt
   USING (
 SELECT a.earned_date,
a.participant_id user_id,
b.first_name AS pax_first_name,
b.last_name AS pax_last_name,
fnc_format_user_name(b.last_name, b.first_name, b.middle_name, b.suffix) AS participant_name,
ua.country_id,
d.NAME AS node_name,
d.node_id,
pe.position_type,
pe.department_type department,
decode(b.is_active,1,'active','inactive') participant_current_status,
br.badge_rule_id,
br.promotion_id,
br.badge_name badge_name,
a.participant_badge_id,
--(SELECT DECODE(p.promotion_type,'recognition',fnc_cms_picklist_value('picklist.promotiontype.items','recognition',NVL(b.language_id,'en_US')),p.promotion_name) AS promotion_name 
/*(SELECT p.promotion_name AS promotion_name --06/10/2014  Bug # 53813 --08/01/2014 
FROM
--badge_promotion bp,      --07/21/2014
promotion p
WHERE
--bp.promotion_id=br.promotion_id   --07/21/2014
--AND bp.eligible_promotion_id=p.promotion_id and rownum <2) AS promotion_name,   --07/21/2014
  br.promotion_id=p.promotion_id and rownum <2) AS promotion_name,        --07/21/2014
  */
LISTAGG(p.promo_name_asset_code , ',') WITHIN GROUP (ORDER BY a.promotion_id) AS promotion_name,  --08/01/2014 
b.user_name AS login_id,
b.middle_name AS user_middle_name
FROM participant_badge A,
application_user b,
user_node C,
user_address ua,
vw_curr_pax_employer pe,
node d,
badge_rule br,
badge_promotion bp, --08/01/2014
promotion p         --08/01/2014
WHERE a.participant_id = b.user_id
AND a.participant_id  = ua.user_id
AND a.participant_id = pe.user_id(+)
AND b.user_id = c.user_id
AND c.node_id = d.node_id
AND a.badge_rule_id = br.badge_rule_id
AND bp.promotion_id(+)=a.promotion_id   --08/01/2014 --01/27/2015
AND bp.eligible_promotion_id=p.promotion_id(+) --08/01/2014 --01/27/2015
AND ua.is_primary = 1
AND a.is_earned = 1  --12/13/2013
AND c.is_primary =1
GROUP BY a.earned_date, --08/01/2014 Added Group by
a.participant_id ,
b.first_name ,
b.last_name ,
fnc_format_user_name(b.last_name, b.first_name, b.middle_name, b.suffix),
ua.country_id,
d.NAME,
d.node_id,
pe.position_type,
pe.department_type,
decode(b.is_active,1,'active','inactive'),
br.badge_rule_id,
br.promotion_id,
br.badge_name,
a.participant_badge_id,
b.user_name,
b.middle_name) s
          ON (rpt.participant_badge_id = s.participant_badge_id)
  WHEN MATCHED THEN UPDATE 
         SET user_last_name = s.pax_last_name,
                user_first_name = s.pax_first_name,
                user_middle_name = s.user_middle_name,
                user_full_name = s.participant_name,
                participant_current_status = nvl(s.participant_current_status,' '),
                position_type = nvl(s.position_type,' '),
                department = nvl(s.department,' '),
                node_id = s.node_id                
        WHERE NOT (   DECODE(rpt.user_first_name,  s.pax_first_name,                    1, 0) = 1                 
                 AND DECODE(rpt.user_last_name,   s.pax_last_name,                     1, 0) = 1
                 AND DECODE(rpt.participant_current_status,   s.participant_current_status,                     1, 0) = 1
                 AND DECODE(rpt.position_type,   s.position_type,                     1, 0) = 1
                 AND DECODE(rpt.department,   s.department,                     1, 0) = 1
                 AND DECODE(rpt.node_id,   s.node_id,                     1, 0) = 1                 
                 AND DECODE(rpt.user_full_name,   s.participant_name,                     1, 0) = 1                              
                 )
        WHEN NOT MATCHED THEN INSERT
                (rpt_badge_detail_id,                   
                   node_id,
                   node_name,       
                   earned_date,           
                   user_id,                   
                   user_first_name,                   
                   user_last_name, 
                   user_middle_name,                         
                   user_full_name,                           
                   date_created,
                   created_by,           
                   country_id,                    
                   position_type,
                   department,
                   participant_current_status,
                   badge_name,
                   promotion_id,
                   badge_rule_id,
                   participant_badge_id,
                   promotion_name,
                   version,
                   login_id                  
                   )
           VALUES (rpt_badge_detail_id_seq.NEXTVAL,                  
                   s.node_id,
                   s.node_name,
                   s.earned_date,                  
                   s.user_id,                  
                   s.pax_first_name,                   
                   s.pax_last_name,
                   s.user_middle_name,
                   s.participant_name,                      
                   SYSDATE,
                   p_user_id,                 
                   s.country_id,                         
                   s.position_type,
                   s.department,
                   s.participant_current_status,
                   s.badge_name,
                   s.promotion_id,
                   s.badge_rule_id,
                   s.participant_badge_id,
                   s.promotion_name,
                   1 ,
                   s.login_id                  
                   );     

  v_stage := 'Open and Fetch cursor to pick modified node name';      --01/29/2015
  OPEN cur_node_changed;   --01/29/2015
  FETCH cur_node_changed BULK COLLECT
   INTO v_tab_node_id,
        v_tab_node_name;
  CLOSE cur_node_changed;   

  v_stage := 'Update modified node name in rpt table';    --01/29/2015
  FORALL indx IN v_tab_node_id.FIRST .. v_tab_node_id.LAST    --01/29/2015
    UPDATE rpt_badge_detail
       SET node_name     = DECODE (node_id, v_tab_node_id(indx), v_tab_node_name(indx), node_name),
           date_modified = SYSDATE,
           modified_by   = p_user_id,
           VERSION       = VERSION + 1
     WHERE (node_id        = v_tab_node_id(indx)
            AND node_name != v_tab_node_name(indx)
            );

  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  p_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    p_return_code := 99;
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ': ' || SQLCODE || ', ' || SQLERRM, NULL);
END P_RPT_BADGE_DETAIL;

PROCEDURE p_rpt_badge_summary
  (p_user_id       IN NUMBER,
   p_start_date    IN DATE, 
   p_end_date      IN DATE, 
   p_return_code   OUT NUMBER,
   p_error_message OUT VARCHAR2)
IS

/*******************************************************************************

   Purpose:  Populate the rpt_badge_summary reporting table

   Person                      Date       Comments
   -----------                ---------- -----------------------------------------------------
   Ravi Dhanekula    11/19/2012   Initial Version.
                              06/18/2014   Performance fix for default teamsum records.
   Ravi Dhanekula    04/24/2015   Removed default permutation code as we no longer need that.
   Loganathan        03/11/2019   Bug 78631 - PKG_REPORTs without logs or return code 
*******************************************************************************/

   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_badge_summary');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
   c_created_by         CONSTANT rpt_badge_summary.created_by%TYPE := 0;

   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;
   v_parm_list     execution_log.text_line%TYPE;   --03/11/2019  Bug 78631
BEGIN
v_stage := 'initialize variables'; 
v_parm_list := 
'p_user_id 			 :='||p_user_id ||CHR(10)||
'p_start_date        :='||p_start_date        ||CHR(10)|| 
'p_end_date          :='||p_end_date          ; 
	
  v_stage := 'Start';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  -- remove obsolete node summaries (node not found in rpt_hierarchy)
  v_stage := 'DELETE obsolete node summary records';
  DELETE
    FROM rpt_badge_summary s
   WHERE s.detail_node_id NOT IN
         ( -- get node ID currently in the report hierarchy
           SELECT h.node_id
             FROM rpt_hierarchy h
         );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   -- merge derived summary records
   v_stage := 'MERGE detail derived summary records';
   MERGE INTO rpt_badge_summary d
   USING (
            WITH rpt_teamsum AS
            (  -- build team summary records
               SELECT -- key fields
                      d.node_id AS detail_node_id,                     
                      d.participant_current_status AS pax_status,
                      d.position_type AS job_position,
                      d.department,
                      d.earned_date,                   
                      h.parent_node_id AS header_node_id,
                      h.hier_level,                      
                      COUNT(d.badge_rule_id)    AS badges_earned                     
                 FROM rpt_badge_detail d,
                      rpt_hierarchy h,
                      participant_badge pb                     
                WHERE h.node_id    = d.node_id            
                  AND d.participant_badge_id = pb.participant_badge_id
                   AND    (p_start_date < pb.date_created  AND pb.date_created  <= p_end_date)
                GROUP BY d.node_id,                    
                      d.participant_current_status,
                      d.position_type,
                      d.department,
                      d.earned_date,                     
                      h.parent_node_id,
                      h.hier_level
            ), detail_derived_summary AS
            (  -- derive summaries based on team summary data
               SELECT -- key fields
                      rt.detail_node_id,
                      rt.hier_level||'-teamsum' AS sum_type,                  
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.earned_date,
                      -- reference fields                    
                      rt.header_node_id,
                      rt.hier_level,
                      1 AS is_leaf, -- The team summary records are always a leaf
                                            -- count fields                   
                      rt.badges_earned                      
                 FROM rpt_teamsum rt
                UNION ALL
               SELECT -- key fields
                      h.node_id AS detail_node_id,
                      h.hier_level||'-nodesum' AS sum_type,                    
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.earned_date,
                      -- reference fields                     
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      h.is_leaf,
                      -- count fields                    
                      NVL(SUM(rt.badges_earned),0)      AS badges_earned                   
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
                WHERE (  rt.hier_level = h.hier_level    -- always create node summary at team summary level
                      OR rt.badges_earned != 0      -- create node summary for team summaries with non-zero media amounts
                      )
                   -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
                  AND rt.detail_node_id = npn.node_id
                  AND npn.path_node_id = h.node_id
                GROUP BY h.node_id,               
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.earned_date,                    
                      h.parent_node_id,
                      h.hier_level,
                      h.is_leaf
            ) -- end detail_derived_summary
            SELECT dds.*
              FROM detail_derived_summary dds
          ) s
      ON ( d.detail_node_id = s.detail_node_id AND
           d.record_type    = s.sum_type       AND         
           d.pax_status     = s.pax_status     AND
           d.job_position   = s.job_position   AND
           d.department     = s.department     AND
           d.earned_date     = s.earned_date     AND
           NVL(d.header_node_id, 0) = NVL(s.header_node_id, 0) AND
           d.hier_level     = s.hier_level     AND
           NVL(d.is_leaf, 0) = NVL(s.is_leaf, 0)
          )
    WHEN MATCHED THEN
      UPDATE SET                
         d.badges_earned      = s.badges_earned,        
         d.modified_by        = c_created_by,
         d.date_modified      = SYSDATE
       WHERE ( -- only update records with different values              
              DECODE(d.header_node_id,     s.header_node_id,     1, 0) = 0
             OR DECODE(d.hier_level,         s.hier_level,         1, 0) = 0
             OR DECODE(d.is_leaf,            s.is_leaf,            1, 0) = 0                     
             OR DECODE(d.badges_earned,      s.badges_earned,      1, 0) = 0          
             )
    WHEN NOT MATCHED THEN
      INSERT
      (  rpt_badge_summary_id,
         -- key fields
         detail_node_id,
         record_type,      
         pax_status,
         job_position,
         department,
         earned_date,
         -- reference fiel       
         header_node_id,
         hier_level,
         is_leaf,
               -- audit fields
         created_by,
         date_created,
         modified_by,
         date_modified,
         -- count fields       
         badges_earned
      )
      VALUES
      (  rpt_badge_summary_id_seq.NEXTVAL,
         -- key fields
         s.detail_node_id,
         s.sum_type,  --s.record_type,        
         s.pax_status,
         s.job_position,
         s.department,
         s.earned_date,
         -- reference fiel       
         s.header_node_id,
         s.hier_level,
         s.is_leaf,
         -- audit fields
         c_created_by,
         SYSDATE,
         NULL,
         NULL,
         -- count fields      
         s.badges_earned  
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  p_return_code := 0;  

EXCEPTION
  WHEN OTHERS THEN
    p_return_code := 99;
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage||':'||v_parm_list||':'|| SQLCODE || ', ' || SQLERRM, NULL);
END p_rpt_badge_summary;

END;  -- pkg_report_badge
/