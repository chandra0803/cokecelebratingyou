CREATE OR REPLACE PACKAGE pkg_query_enroll_reports AS
/******************************************************************************
   NAME:       pkg_query_enroll_reports
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        7/28/2014      keshaboi       1. Created this package.
******************************************************************************/
   /* getEnrollmentSummaryResults */
  PROCEDURE prc_getEnrollmentSummary (
   p_in_countryIds          IN     VARCHAR,
   p_in_departments         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_jobPosition         IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_participantStatus   IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_role                IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT SYS_REFCURSOR,
   p_out_size_data          OUT NUMBER,
   p_out_totals_data        OUT SYS_REFCURSOR
   );
     /* getEnrollmentDetailsResults */
PROCEDURE prc_getEnrollmentDetails(
   p_in_countryIds          IN     VARCHAR,
   p_in_departments         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_jobPosition         IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_participantStatus   IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_role                IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT SYS_REFCURSOR,
   p_out_size_data          OUT NUMBER
   );
   /* getTotalEnrollmentsBarResults */
PROCEDURE prc_getTotalEnrollmentBar(
   p_in_countryIds          IN     VARCHAR,
   p_in_departments         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_jobPosition         IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_participantStatus   IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_role                IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT SYS_REFCURSOR
   );
   /* getEnrollmentStatusPercentageBarResults */
PROCEDURE prc_getEnrollStatusPercentBar(
   p_in_countryIds          IN     VARCHAR,
   p_in_departments         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_jobPosition         IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_participantStatus   IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_role                IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT SYS_REFCURSOR
   );
     /* getEnrollmentActiveInactiveBarResults */
PROCEDURE prc_getEnrollActiveInactiveBar(
   p_in_countryIds          IN     VARCHAR,
   p_in_departments         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_jobPosition         IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_participantStatus   IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_role                IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT SYS_REFCURSOR);
   /* getPieResults */
PROCEDURE prc_getPieResults(
   p_in_countryIds          IN     VARCHAR,
   p_in_departments         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_jobPosition         IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_participantStatus   IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_role                IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT SYS_REFCURSOR
   );  

END pkg_query_enroll_reports;
/
CREATE OR REPLACE PACKAGE BODY pkg_query_enroll_reports
IS
   PROCEDURE prc_getEnrollmentSummary (
   p_in_countryIds          IN     VARCHAR,
   p_in_departments         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_jobPosition         IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_participantStatus   IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_role                IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT SYS_REFCURSOR,
   p_out_size_data          OUT NUMBER,
   p_out_totals_data        OUT SYS_REFCURSOR
   )
   IS
   
     /******************************************************************************
  NAME:       prc_getAwardsSummaryPax
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
 ??????              ???????              Initial..(the queries were in Java before the release 5.4. So no comments available so far)  
 nagarajs            04/07/2016          Bug 66362 - Enrollment Activity - By Organization report keep on spinning and not returning any results                              
  ******************************************************************************/
     
   /* getEnrollmentSummaryResults */
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getEnrollmentSummary' ;
      v_stage                   VARCHAR2 (500);
      v_sortCol                 VARCHAR2 (200);
      l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getEnrollmentSummary';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      --04/07/2016 start Replaced query with new tunned query
      
      l_query := l_query
         || '  '
         || 'SELECT ROWNUM RN, rs.* 
                FROM (
        SELECT REPLACE (node_name, ''_'', '''') hierarchy_node_name,
               rh.node_id hierarchy_node_id,
               NVL (SUM(active_cnt), 0) active_cnt,
               NVL (SUM(inactive_cnt), 0) inactive_cnt,
               NVL (SUM(active_cnt) + SUM(inactive_cnt), 0) total_cnt,
               NVL (rh.is_leaf, 0) is_leaf, 
               0 is_team
          FROM (SELECT node_id detail_node_id,
                       SUM(DECODE(status,''active'',1,0)) active_cnt, 
                       SUM(DECODE(status,''inactive'',1,0)) inactive_cnt
                  FROM ( SELECT DISTINCT red.node_id, red.user_id, status 
                           FROM rpt_enrollment_detail red
                          WHERE NVL(red.enrollment_date, trunc(sysdate)) BETWEEN TO_DATE('''||p_in_fromdate||''','''||p_in_localedatepattern||''') and TO_DATE('''||p_in_todate||''','''||p_in_localedatepattern||''')
                            AND red.status        = NVL('''||p_in_participantstatus||''',red.status)
                            AND red.job_position  = NVL('''||p_in_jobposition||''',red.job_position)
                            AND red.role          = NVL('''||p_in_role||''',red.role)
                            AND (('''||p_in_countryids||''' IS NULL) OR ('''||p_in_countryids||''' IS NOT NULL AND red.country_id IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_countryids||''')))))
                            AND (('''||p_in_departments||''' IS NULL) OR ('''||p_in_departments||''' IS NOT NULL AND red.department IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_departments||''')))))
                            AND NVL(red.node_id,0) IN  (SELECT child_node_id
                                                          FROM rpt_hierarchy_rollup
                                                         WHERE node_id  IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_parentnodeid||''')))))
                          GROUP BY node_id    
                ) ras,
                rpt_hierarchy rh,
                rpt_hierarchy_rollup rhr
          WHERE rhr.child_node_id = ras.detail_node_id (+)
            AND rh.node_id = rhr.nodE_id 
            AND NVL(parent_node_id, 0) IN (SELECT * FROM TABLE(get_array_varchar( NVL('''||p_in_parentnodeid||''',0) ) ))
            AND rh.is_deleted = 0
          GROUP BY REPLACE (node_name, ''_'', '''') ,
                rh.node_id,
                rh.is_leaf,
                0
          UNION
         SELECT node_name|| '' Team'' hierarchy_node_name,
                rh.node_id hierarchy_node_id,
                NVL (active_cnt, 0) active_cnt,
                NVL (inactive_cnt, 0) inactive_cnt,
                NVL (active_cnt + inactive_cnt, 0) total_cnt,
                NVL (rh.is_leaf, 0) is_leaf, 
                1 is_team
          FROM (SELECT node_id detail_node_id,
                       SUM(DECODE(status,''active'',1,0)) active_cnt, 
                       SUM(DECODE(status,''inactive'',1,0)) inactive_cnt
                  FROM ( SELECT DISTINCT red.node_id, red.user_id, status 
                           FROM rpt_enrollment_detail red
                          WHERE NVL(red.enrollment_date, trunc(sysdate)) BETWEEN TO_DATE('''||p_in_fromdate||''','''||p_in_localedatepattern||''') and TO_DATE('''||p_in_todate||''','''||p_in_localedatepattern||''')
                            AND red.status        = NVL('''||p_in_participantstatus||''',red.status)
                            AND red.job_position  = NVL('''||p_in_jobposition||''',red.job_position)
                            AND red.role          = NVL('''||p_in_role||''',red.role)
                            AND (('''||p_in_countryids||''' IS NULL) OR ('''||p_in_countryids||''' IS NOT NULL AND red.country_id IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_countryids||''')))))
                            AND (('''||p_in_departments||''' IS NULL) OR ('''||p_in_departments||''' IS NOT NULL AND red.department IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_departments||''')))))
                            AND NVL(red.node_id,0) IN  (SELECT * FROM TABLE(get_array_varchar('''||p_in_parentnodeid||'''))))
                          GROUP BY node_id    
                ) ras,
                rpt_hierarchy rh
          WHERE rh.node_id = ras.detail_node_id (+)
            AND NVL(node_id, 0) IN (SELECT * FROM TABLE(get_array_varchar( NVL('''||p_in_parentnodeid||''',0) ) ))
            AND rh.is_deleted = 0 )rs
                ORDER BY '|| v_sortCol ||'
           ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
   dbms_output.put_line (l_query);
   
      OPEN p_out_data FOR l_query;
     
     /* getEnrollmentSummaryResultsSize */
 SELECT COUNT(1)  INTO p_out_size_data
FROM
  (SELECT REPLACE (node_name, '_', '') hierarchy_node_name,
       rh.node_id hierarchy_node_id,
       NVL (SUM(active_cnt), 0) active_cnt,
       NVL (SUM(inactive_cnt), 0) inactive_cnt,
       NVL (SUM(active_cnt) + SUM(inactive_cnt), 0) total_cnt
  FROM (SELECT node_id detail_node_id,
               SUM(DECODE(status,'active',1,0)) active_cnt, 
               SUM(DECODE(status,'inactive',1,0)) inactive_cnt
          FROM ( SELECT DISTINCT red.node_id, red.user_id, status 
                   FROM rpt_enrollment_detail red
                  WHERE NVL(red.enrollment_date, trunc(sysdate)) BETWEEN TO_DATE(p_in_fromdate,p_in_localedatepattern) and TO_DATE(p_in_todate,p_in_localedatepattern)
                    AND red.status        = NVL(p_in_participantstatus,red.status)
                    AND red.job_position  = NVL(p_in_jobposition,red.job_position)
                    AND red.role          = NVL(p_in_role,red.role)
                    AND ((p_in_countryids IS NULL) OR (p_in_countryids IS NOT NULL AND red.country_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_countryids)))))
                    AND ((p_in_departments IS NULL) OR (p_in_departments IS NOT NULL AND red.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))
                    AND NVL(red.node_id,0) IN  (SELECT child_node_id
                                                  FROM rpt_hierarchy_rollup
                                                 WHERE node_id  IN (SELECT * FROM TABLE(get_array_varchar(p_in_parentnodeid)))))
                  GROUP BY node_id    
        ) ras,
        rpt_hierarchy rh,
        rpt_hierarchy_rollup rhr
  WHERE rhr.child_node_id = ras.detail_node_id (+)
    AND rh.node_id = rhr.nodE_id 
    AND NVL(parent_node_id, 0) IN (SELECT * FROM TABLE(get_array_varchar( NVL(p_in_parentnodeid,0) ) ))
    AND rh.is_deleted = 0
  GROUP BY REPLACE (node_name, '_', '') ,
        rh.node_id
  UNION
 SELECT node_name|| ' Team' hierarchy_node_name,
        rh.node_id hierarchy_node_id,
        NVL (active_cnt, 0) active_cnt,
        NVL (inactive_cnt, 0) inactive_cnt,
        NVL (active_cnt + inactive_cnt, 0) total_cnt
  FROM (SELECT node_id detail_node_id,
               SUM(DECODE(status,'active',1,0)) active_cnt, 
               SUM(DECODE(status,'inactive',1,0)) inactive_cnt
          FROM ( SELECT DISTINCT red.node_id, red.user_id, status 
                   FROM rpt_enrollment_detail red
                  WHERE NVL(red.enrollment_date, trunc(sysdate)) BETWEEN TO_DATE(p_in_fromdate,p_in_localedatepattern) and TO_DATE(p_in_todate,p_in_localedatepattern)
                    AND red.status        = NVL(p_in_participantstatus,red.status)
                    AND red.job_position  = NVL(p_in_jobposition,red.job_position)
                    AND red.role          = NVL(p_in_role,red.role)
                    AND ((p_in_countryids IS NULL) OR (p_in_countryids IS NOT NULL AND red.country_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_countryids)))))
                    AND ((p_in_departments IS NULL) OR (p_in_departments IS NOT NULL AND red.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))
                    AND NVL(red.node_id,0) IN  (SELECT * FROM TABLE(get_array_varchar(p_in_parentnodeid))))
                  GROUP BY node_id    
        ) ras,
        rpt_hierarchy rh
  WHERE rh.node_id = ras.detail_node_id (+)
    AND NVL(node_id, 0) IN (SELECT * FROM TABLE(get_array_varchar( NVL(p_in_parentnodeid,0) ) ))
    AND rh.is_deleted = 0
  ) ;
  
  /* getEnrollmentSummaryResultsTotals */
OPEN p_out_totals_data FOR
SELECT NVL(SUM(active_cnt),0) active_cnt,
  NVL(SUM(inactive_cnt),0) inactive_cnt,
  NVL(SUM(total_cnt),0) total_cnt
FROM
  (SELECT REPLACE (node_name, '_', '') hierarchy_node_name,
       rh.node_id hierarchy_node_id,
       NVL (SUM(active_cnt), 0) active_cnt,
       NVL (SUM(inactive_cnt), 0) inactive_cnt,
       NVL (SUM(active_cnt) + SUM(inactive_cnt), 0) total_cnt
  FROM (SELECT node_id detail_node_id,
               SUM(DECODE(status,'active',1,0)) active_cnt, 
               SUM(DECODE(status,'inactive',1,0)) inactive_cnt
          FROM ( SELECT DISTINCT red.node_id, red.user_id, status 
                   FROM rpt_enrollment_detail red
                  WHERE NVL(red.enrollment_date, trunc(sysdate)) BETWEEN TO_DATE(p_in_fromdate,p_in_localedatepattern) and TO_DATE(p_in_todate,p_in_localedatepattern)
                    AND red.status        = NVL(p_in_participantstatus,red.status)
                    AND red.job_position  = NVL(p_in_jobposition,red.job_position)
                    AND red.role          = NVL(p_in_role,red.role)
                    AND ((p_in_countryids IS NULL) OR (p_in_countryids IS NOT NULL AND red.country_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_countryids)))))
                    AND ((p_in_departments IS NULL) OR (p_in_departments IS NOT NULL AND red.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))
                    AND NVL(red.node_id,0) IN  (SELECT child_node_id
                                                  FROM rpt_hierarchy_rollup
                                                 WHERE node_id  IN (SELECT * FROM TABLE(get_array_varchar(p_in_parentnodeid)))))
                  GROUP BY node_id    
        ) ras,
        rpt_hierarchy rh,
        rpt_hierarchy_rollup rhr
  WHERE rhr.child_node_id = ras.detail_node_id (+)
    AND rh.node_id = rhr.nodE_id 
    AND NVL(parent_node_id, 0) IN (SELECT * FROM TABLE(get_array_varchar( NVL(p_in_parentnodeid,0) ) ))
    AND rh.is_deleted = 0
  GROUP BY REPLACE (node_name, '_', '') ,
        rh.node_id
  UNION
 SELECT node_name|| ' Team' hierarchy_node_name,
        rh.node_id hierarchy_node_id,
        NVL (active_cnt, 0) active_cnt,
        NVL (inactive_cnt, 0) inactive_cnt,
        NVL (active_cnt + inactive_cnt, 0) total_cnt
  FROM (SELECT node_id detail_node_id,
               SUM(DECODE(status,'active',1,0)) active_cnt, 
               SUM(DECODE(status,'inactive',1,0)) inactive_cnt
          FROM ( SELECT DISTINCT red.node_id, red.user_id, status 
                   FROM rpt_enrollment_detail red
                  WHERE NVL(red.enrollment_date, trunc(sysdate)) BETWEEN TO_DATE(p_in_fromdate,p_in_localedatepattern) and TO_DATE(p_in_todate,p_in_localedatepattern)
                    AND red.status        = NVL(p_in_participantstatus,red.status)
                    AND red.job_position  = NVL(p_in_jobposition,red.job_position)
                    AND red.role          = NVL(p_in_role,red.role)
                    AND ((p_in_countryids IS NULL) OR (p_in_countryids IS NOT NULL AND red.country_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_countryids)))))
                    AND ((p_in_departments IS NULL) OR (p_in_departments IS NOT NULL AND red.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))
                    AND NVL(red.node_id,0) IN  (SELECT * FROM TABLE(get_array_varchar(p_in_parentnodeid))))
                  GROUP BY node_id    
        ) ras,
        rpt_hierarchy rh
  WHERE rh.node_id = ras.detail_node_id (+)
    AND NVL(node_id, 0) IN (SELECT * FROM TABLE(get_array_varchar( NVL(p_in_parentnodeid,0) ) ))
    AND rh.is_deleted = 0
  ) ;
    --04/07/2016 end Replaced query with new tunned query
    
   p_out_return_code := '00';
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;

         p_out_size_data := NULL;

         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;
   END prc_getEnrollmentSummary;
   ----
   /* getEnrollmentDetailsResults */
PROCEDURE prc_getEnrollmentDetails(
   p_in_countryIds          IN     VARCHAR,
   p_in_departments         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_jobPosition         IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_participantStatus   IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_role                IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT SYS_REFCURSOR,
   p_out_size_data          OUT NUMBER
   )
   IS
   /* getEnrollmentDetailsResults */
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getEnrollmentDetails' ;
      v_stage                   VARCHAR2 (500);
      v_sortCol                 VARCHAR2 (200);
      l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getEnrollmentDetails';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROWNUM RN, 
         rs.* 
       FROM 
         (SELECT red.last_name 
           || '' , '' 
           || red.first_name 
           || '' '' 
           || red.middle_init pax_name, 
           fnc_locale_to_char_dt(red.enrollment_date,'''||p_in_languageCode||''')                             AS enrollment_date, 
           fnc_cms_picklist_value(''picklist.participantstatus.items'',red.status,'''||p_in_languageCode||''')  AS status, 
           fnc_cms_picklist_value(''picklist.positiontype.items'',red.job_position,'''||p_in_languageCode||''') AS job_position, 
           DECODE(red.role, ''own'', ''Owner'', ''mgr'', ''Manager'', ''mbr'', ''Member'', red.role) role, 
           fnc_cms_picklist_value(''picklist.department.type.items'',red.department,'''||p_in_languageCode||''') AS department, 
           rh.node_name node_name, 
           fnc_cms_asset_code_val_extr( country.cm_asset_code, country.name_cm_key, '''||p_in_languageCode||''') AS country 
         FROM rpt_enrollment_Detail red, 
           rpt_characteristic rchar, 
           rpt_hierarchy rh, 
           country 
         WHERE red.user_id                         = rchar.user_nt_id (+) 
         AND rh.node_id                            = red.node_id 
         AND NVL(rchar.characteristic_type,''USER'') = ''USER'' 
         AND country.country_id                    =red.country_id 
         AND NVL(red.node_id,0)                    IN (SELECT * FROM TABLE(get_array_varchar( nvl('''||p_in_parentNodeId||''',red.node_id)  ) )) 
         AND NVL(TRUNC(enrollment_date), TRUNC(sysdate)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')        
         AND (('''||p_in_countryIds||''' IS NULL) OR country.country_id IN
        (SELECT * FROM TABLE(get_array_varchar('''||p_in_countryIds||'''))))      
         AND (('''||p_in_departments||''' IS NULL) OR red.department IN
        (SELECT * FROM TABLE(get_array_varchar('''||p_in_departments||'''))))
         AND UPPER(red.role)      = NVL(UPPER('''||p_in_role||'''),UPPER(red.role)) 
         AND NVL(red.job_position,''JOB'') = NVL('''||p_in_jobPosition||''',NVL(red.job_position,''JOB''))  
         AND red.status           = NVL('''||p_in_participantStatus||''',red.status) 
        ORDER BY '|| v_sortCol ||' ) rs
   ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
      OPEN p_out_data FOR l_query;
     
     /* getEnrollmentDetailsResultsSize */
SELECT COUNT (1) INTO p_out_size_data
FROM
  (SELECT red.last_name
    || ' , '
    || red.first_name
    || ' '
    || red.middle_init pax_name,
    red.enrollment_date AS enrollment_date,
    red.status          AS status,
    red.job_position    AS job_position,
    red.role role,
    red.department AS department,
    rh.node_name node_name,
    country.cm_asset_code AS COUNTRY
  FROM rpt_enrollment_Detail red,
    rpt_characteristic rchar,
    rpt_hierarchy rh,
    country
  WHERE red.user_id                         = rchar.user_nt_id (+)
  AND rh.node_id                            = red.node_id
  AND NVL(rchar.characteristic_type,'USER') = 'USER'
  AND country.country_id                    =red.country_id
  AND NVL(red.node_id,0)                   IN
    (SELECT * FROM TABLE(get_array_varchar( NVL(p_in_parentNodeId,red.node_id) ) )
    )
  AND NVL(TRUNC(enrollment_date), TRUNC(sysdate)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
        -- AND (country.country_id IN(p_in_countryIds) 
        -- OR 'N'                   = p_in_filterCountries) 
         AND ((p_in_countryIds IS NULL) OR country.country_id IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_countryIds)))) 
         --AND (red.department     IN(p_in_departments) 
         --OR 'N'                   = p_in_filterDepartments) 
         AND ((p_in_departments IS NULL) OR red.department IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  AND UPPER(red.role)             = NVL(UPPER(p_in_role),UPPER(red.role))
  AND NVL(red.job_position,'JOB') = NVL(p_in_jobPosition,NVL(red.job_position,'JOB'))
  AND red.status                  = NVL(p_in_participantStatus,red.status)
  ) ;
  
  p_out_return_code := '00';
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;

         p_out_size_data := NULL;
 
   END prc_getEnrollmentDetails;
   ----
/* CHARTS */

/* getTotalEnrollmentsBarResults */
PROCEDURE prc_getTotalEnrollmentBar(
   p_in_countryIds          IN     VARCHAR,
   p_in_departments         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_jobPosition         IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_participantStatus   IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_role                IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT SYS_REFCURSOR
   )
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getTotalEnrollmentBar' ;
      v_stage                   VARCHAR2 (500);
    
   BEGIN
    v_stage := 'getTotalEnrollmentBar';
         
      OPEN p_out_data FOR
   --SELECT * FROM (
SELECT month_trans.cms_name
  || '-'
  || YEAR as month_name,
  total_enrollment_count
FROM
  (WITH month_list AS
  (SELECT TO_CHAR ( ADD_MONTHS ( TRUNC ( TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM - 1), 'MON') MONTH,
    TO_CHAR ( ADD_MONTHS ( TRUNC ( TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM       - 1), 'yy') YEAR,
    ADD_MONTHS ( TRUNC ( TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM                 - 1) month_sort
  FROM DUAL
    CONNECT BY LEVEL <= MONTHS_BETWEEN ( TRUNC ( TO_DATE(p_in_toDate,p_in_localeDatePattern), 'mm'), TRUNC ( TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm')) + 1
  )
SELECT ml.month,
  ml.year,
  NVL (rpt_c.points_count, 0) total_enrollment_count
FROM
  (SELECT SUM (ENROLLMENT_COUNT) POINTS_COUNT,
    TO_CHAR (ENROLLMENT_DATE, 'MON') MONTH,
    TO_CHAR (ENROLLMENT_DATE, 'YY') YEAR
  FROM rpt_enrollment_summary ras
  WHERE ras.detail_node_id IN
    (SELECT child_node_id
    FROM rpt_hierarchy_rollup
    WHERE node_id IN
      (SELECT      *
      FROM TABLE(get_array_varchar( p_in_parentNodeId ) )
      )
    )
  AND TRUNC (enrollment_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
  AND ( (p_in_countryIds  IS NULL)
  OR (p_in_countryIds     IS NOT NULL
  AND ras.department IN
    (SELECT * FROM TABLE ( get_array_varchar ( p_in_countryIds))
    )))
  AND ( (p_in_departments IS NULL)
  OR (p_in_departments    IS NOT NULL
  AND ras.department IN
    (SELECT * FROM TABLE ( get_array_varchar ( p_in_departments))
    )))
  AND UPPER (ras.role)              = NVL (UPPER (p_in_role), UPPER (ras.role))
  AND NVL (ras.job_position, 'JOB') = NVL (p_in_jobPosition, NVL (ras.job_position, 'JOB'))
  AND record_type LIKE '%team%'
  AND ras.status = NVL (p_in_participantStatus, ras.status)
  GROUP BY TO_CHAR (enrollment_date, 'MON'),
    TO_CHAR (ENROLLMENT_DATE, 'YY')
  ) rpt_c,
  month_list ml
WHERE ml.month = rpt_c.month(+)
AND ml.year    = rpt_c.year(+)
ORDER BY ml.month_sort
  ) ss,
  (SELECT cms_code,
    cms_name
  FROM vw_cms_code_value vw
  WHERE asset_code = 'picklist.monthname.type.items'
  AND locale       =
    (SELECT string_val FROM os_propertyset WHERE entity_name = 'default.language'
    )
  AND NOT EXISTS
    (SELECT *
    FROM vw_cms_code_value
    WHERE asset_code = 'picklist.monthname.type.items'
    AND locale       = p_in_languageCode
    )
  UNION ALL
  SELECT cms_code,
    cms_name
  FROM vw_cms_code_value
  WHERE asset_code = 'picklist.monthname.type.items'
  AND locale       = p_in_languageCode
  ) month_trans
WHERE UPPER(month_trans.cms_code)=UPPER(ss.month) ;

   p_out_return_code := '00';
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;

   END prc_getTotalEnrollmentBar;
   ----
   
/* getEnrollmentStatusPercentageBarResults */
PROCEDURE prc_getEnrollStatusPercentBar(
   p_in_countryIds          IN     VARCHAR,
   p_in_departments         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_jobPosition         IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_participantStatus   IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_role                IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT SYS_REFCURSOR
   )
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getEnrollStatusPercentBar' ;
      v_stage                   VARCHAR2 (500);
    
   BEGIN
    v_stage := 'getEnrollStatusPercentBar';
         
      OPEN p_out_data FOR
     SELECT * FROM (
   SELECT hierarchy_node_name,
       active_cnt, 
       inactive_cnt, 
       total_cnt 
     FROM (SELECT node_name hierarchy_node_name, 
           NVL(PKG_REPORT_ENROLLMENT.FNC_GET_COUNT(detail_node_id, ras.record_type, TO_DATE(p_in_fromDate,p_in_localeDatePattern), TO_DATE(p_in_toDate,p_in_localeDatePattern), p_in_participantStatus, p_in_departments, p_in_countryIds, p_in_jobPosition, p_in_role, 'ACTIVE'),0) active_cnt, 
           NVL(PKG_REPORT_ENROLLMENT.FNC_GET_COUNT(detail_node_id, ras.record_type, TO_DATE(p_in_fromDate,p_in_localeDatePattern), TO_DATE(p_in_toDate,p_in_localeDatePattern), p_in_participantStatus, p_in_departments, p_in_countryIds, p_in_jobPosition, p_in_role, 'INACTIVE'),0) inactive_cnt, 
           NVL(PKG_REPORT_ENROLLMENT.FNC_GET_COUNT(detail_node_id, ras.record_type, TO_DATE(p_in_fromDate,p_in_localeDatePattern), TO_DATE(p_in_toDate,p_in_localeDatePattern), p_in_participantStatus, p_in_departments, p_in_countryIds, p_in_jobPosition, p_in_role, 'TOTAL'),0) total_cnt 
         FROM rpt_enrollment_summary ras, 
           rpt_hierarchy rh 
         WHERE detail_node_id = rh.node_id 
         AND ras.record_type LIKE '%node%' 
         AND NVL(header_node_id,0)      IN (SELECT * FROM TABLE(get_array_varchar( nvl(p_in_parentNodeId,0)  ) )) 
         --AND (ras.country_id            IN(p_in_countryIds) 
        -- OR 'N'                          = p_in_filterCountries) 
         AND ((p_in_countryIds IS NULL) OR ras.country_id IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_countryIds)))) 
        -- AND (ras.department            IN(p_in_departments) 
       --  OR 'N'                          = p_in_filterDepartments) 
         AND ((p_in_departments IS NULL) OR ras.department IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
         AND UPPER(ras.role)             = NVL(UPPER(p_in_role),UPPER(ras.role)) 
         AND NVL(ras.job_position,'JOB') = NVL(p_in_jobPosition,NVL(ras.job_position,'JOB')) 
         AND ras.status                  = NVL(p_in_participantStatus,ras.status) 
         AND rh.is_deleted               =0 
         AND p_in_nodeAndBelow = 1
         GROUP BY ras.record_type, 
           rh.node_name, 
           ras.detail_node_id 
         UNION 
         SELECT DECODE (instr(ras.record_type,'team'),0,node_name,node_name 
         || ' Team') hierarchy_node_name, 
         NVL(PKG_REPORT_ENROLLMENT.FNC_GET_COUNT(detail_node_id, ras.record_type, TO_DATE(p_in_fromDate,p_in_localeDatePattern), TO_DATE(p_in_toDate,p_in_localeDatePattern), p_in_participantStatus, p_in_departments, p_in_countryIds, p_in_jobPosition, p_in_role, 'ACTIVE'),0) active_cnt, 
         NVL(PKG_REPORT_ENROLLMENT.FNC_GET_COUNT(detail_node_id, ras.record_type, TO_DATE(p_in_fromDate,p_in_localeDatePattern), TO_DATE(p_in_toDate,p_in_localeDatePattern), p_in_participantStatus, p_in_departments, p_in_countryIds, p_in_jobPosition, p_in_role, 'INACTIVE'),0) inactive_cnt, 
         NVL(PKG_REPORT_ENROLLMENT.FNC_GET_COUNT(detail_node_id, ras.record_type, TO_DATE(p_in_fromDate,p_in_localeDatePattern), TO_DATE(p_in_toDate,p_in_localeDatePattern), p_in_participantStatus, p_in_departments, p_in_countryIds, p_in_jobPosition, p_in_role, 'TOTAL'),0) total_cnt 
       FROM rpt_enrollment_summary ras, 
         rpt_hierarchy rh 
       WHERE detail_node_id = rh.node_id 
       AND ras.record_type LIKE '%team%' 
       AND NVL(ras.detail_node_id,0)   IN (SELECT * FROM TABLE(get_array_varchar( nvl(p_in_parentNodeId,0)  ) )) 
       AND rh.is_deleted               =0 
       --AND (ras.country_id            IN(p_in_countryIds) 
        -- OR 'N'                          = p_in_filterCountries) 
         AND ((p_in_countryIds IS NULL) OR ras.country_id IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_countryIds)))) 
        -- AND (ras.department            IN(p_in_departments) 
       --  OR 'N'                          = p_in_filterDepartments) 
         AND ((p_in_departments IS NULL) OR ras.department IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
       AND UPPER(ras.role)             = NVL(UPPER(p_in_role),UPPER(ras.role)) 
       AND NVL(ras.job_position,'JOB') = NVL(p_in_jobPosition,NVL(ras.job_position,'JOB')) 
       AND ras.status                  = NVL(p_in_participantStatus,ras.status) 
       GROUP BY ras.record_type, 
         rh.node_name, 
         ras.detail_node_id 
       ORDER BY total_cnt DESC ))
       
     WHERE ROWNUM <=20  ;
        p_out_return_code := '00';
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;

   END prc_getEnrollStatusPercentBar;
   
  /* getEnrollmentActiveInactiveBarResults */
PROCEDURE prc_getEnrollActiveInactiveBar(
   p_in_countryIds          IN     VARCHAR,
   p_in_departments         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_jobPosition         IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_participantStatus   IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_role                IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getEnrollActiveInactiveBar' ;
      v_stage                   VARCHAR2 (500);
    
   BEGIN
    v_stage := 'getEnrollActiveInactiveBar';
         
      OPEN p_out_data FOR
   SELECT * FROM (
SELECT ROUND(DECODE(SUM(TOTAL),0,0,(SUM(active_cnt) /SUM(TOTAL)*100)),2) ACTIVE_COUNT,
  ROUND(DECODE(SUM(TOTAL),0,0,(SUM(inactive_cnt)    /SUM(TOTAL)*100)),2) INACTIVE_COUNT
FROM
  (SELECT NVL(PKG_REPORT_ENROLLMENT.FNC_GET_COUNT(detail_node_id, ras.record_type, TO_DATE(p_in_fromDate,p_in_localeDatePattern), TO_DATE(p_in_toDate,p_in_localeDatePattern), p_in_participantStatus, p_in_departments, p_in_countryIds, p_in_jobPosition, p_in_role, 'ACTIVE'),0) active_cnt,
    NVL(PKG_REPORT_ENROLLMENT.FNC_GET_COUNT(detail_node_id, ras.record_type, TO_DATE(p_in_fromDate,p_in_localeDatePattern), TO_DATE(p_in_toDate,p_in_localeDatePattern), p_in_participantStatus, p_in_departments, p_in_countryIds, p_in_jobPosition, p_in_role, 'INACTIVE'),0) inactive_cnt,
    NVL(PKG_REPORT_ENROLLMENT.FNC_GET_COUNT(detail_node_id, ras.record_type, TO_DATE(p_in_fromDate,p_in_localeDatePattern), TO_DATE(p_in_toDate,p_in_localeDatePattern), p_in_participantStatus, p_in_departments, p_in_countryIds, p_in_jobPosition, p_in_role, 'TOTAL'),0) TOTAL
  FROM rpt_enrollment_summary ras,
    rpt_hierarchy rh
  WHERE detail_node_id = rh.node_id
  AND ras.record_type LIKE '%node%'
  AND NVL(header_node_id,0) IN
    (SELECT * FROM TABLE(get_array_varchar( NVL(p_in_parentNodeId,0) ) )
    )
  AND rh.is_deleted               =0
   --AND (ras.country_id            IN(p_in_countryIds) 
        -- OR 'N'                          = p_in_filterCountries) 
         AND ((p_in_countryIds IS NULL) OR ras.country_id IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_countryIds)))) 
        -- AND (ras.department            IN(p_in_departments) 
       --  OR 'N'                          = p_in_filterDepartments) 
         AND ((p_in_departments IS NULL) OR ras.department IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  AND UPPER(ras.role)             = NVL(UPPER(p_in_role),UPPER(ras.role))
  AND NVL(ras.job_position,'JOB') = NVL(p_in_jobPosition,NVL(ras.job_position,'JOB'))
  AND ras.status                  = NVL(p_in_participantStatus,ras.status)
  GROUP BY ras.record_type,
    ras.detail_node_id
  UNION
  SELECT NVL(PKG_REPORT_ENROLLMENT.FNC_GET_COUNT(detail_node_id, ras.record_type, TO_DATE(p_in_fromDate,p_in_localeDatePattern), TO_DATE(p_in_toDate,p_in_localeDatePattern), p_in_participantStatus, p_in_departments, p_in_countryIds, p_in_jobPosition, p_in_role, 'ACTIVE'),0) active_cnt,
    NVL(PKG_REPORT_ENROLLMENT.FNC_GET_COUNT(detail_node_id, ras.record_type, TO_DATE(p_in_fromDate,p_in_localeDatePattern), TO_DATE(p_in_toDate,p_in_localeDatePattern), p_in_participantStatus, p_in_departments, p_in_countryIds, p_in_jobPosition, p_in_role, 'INACTIVE'),0) inactive_cnt,
    NVL(PKG_REPORT_ENROLLMENT.FNC_GET_COUNT(detail_node_id, ras.record_type, TO_DATE(p_in_fromDate,p_in_localeDatePattern), TO_DATE(p_in_toDate,p_in_localeDatePattern), p_in_participantStatus, p_in_departments, p_in_countryIds, p_in_jobPosition, p_in_role, 'TOTAL'),0) TOTAL
  FROM rpt_enrollment_summary ras,
    rpt_hierarchy rh
  WHERE detail_node_id = rh.node_id
  AND ras.record_type LIKE '%team%'
  AND NVL(ras.detail_node_id,0) IN
    (SELECT * FROM TABLE(get_array_varchar( NVL(p_in_parentNodeId,0) ) )
    )
  AND rh.is_deleted               =0
  --AND (ras.country_id            IN(p_in_countryIds) 
        -- OR 'N'                          = p_in_filterCountries) 
         AND ((p_in_countryIds IS NULL) OR ras.country_id IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_countryIds)))) 
        -- AND (ras.department            IN(p_in_departments) 
       --  OR 'N'                          = p_in_filterDepartments) 
         AND ((p_in_departments IS NULL) OR ras.department IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  AND UPPER(ras.role)             = NVL(UPPER(p_in_role),UPPER(ras.role))
  AND NVL(ras.job_position,'JOB') = NVL(p_in_jobPosition,NVL(ras.job_position,'JOB'))
  AND ras.status                  = NVL(p_in_participantStatus,ras.status)
  GROUP BY ras.record_type,
    ras.detail_node_id));

   p_out_return_code := '00';
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;

   END prc_getEnrollActiveInactiveBar;
   
   
/* getPieResults */
PROCEDURE prc_getPieResults(
   p_in_countryIds          IN     VARCHAR,
   p_in_departments         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_jobPosition         IN     VARCHAR,
   p_in_languageCode        IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_participantStatus   IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_in_role                IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT SYS_REFCURSOR
   )
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getPieResults' ;
      v_stage                   VARCHAR2 (500);
    
   BEGIN
    v_stage := 'getPieResults';
         
      OPEN p_out_data FOR
   SELECT * FROM (
 SELECT hierarchy_node_name, 
       active_cnt 
     FROM ( SELECT node_name hierarchy_node_name, 
           NVL(PKG_REPORT_ENROLLMENT.FNC_GET_COUNT(detail_node_id, ras.record_type, TO_DATE(p_in_fromDate,p_in_localeDatePattern), TO_DATE(p_in_toDate,p_in_localeDatePattern), p_in_participantStatus, p_in_departments,p_in_countryIds,p_in_jobPosition, p_in_role, 'ACTIVE'),0) active_cnt, 
           rh.node_name node_name, 
           ras.detail_node_id detail_node_id 
         FROM rpt_enrollment_summary ras, 
           rpt_hierarchy rh 
         WHERE detail_node_id = rh.node_id 
         AND ras.record_type LIKE '%node%' 
         AND NVL(header_node_id,0)       IN (SELECT * FROM TABLE(get_array_varchar( nvl(p_in_parentNodeId,0)  ) )) 
         --AND (ras.country_id            IN(p_in_countryIds) 
        -- OR 'N'                          = p_in_filterCountries) 
         AND ((p_in_countryIds IS NULL) OR ras.country_id IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_countryIds)))) 
        -- AND (ras.department            IN(p_in_departments) 
       --  OR 'N'                          = p_in_filterDepartments) 
         AND ((p_in_departments IS NULL) OR ras.department IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
         AND UPPER(ras.role)             = NVL(UPPER(p_in_role),UPPER(ras.role)) 
         AND NVL(ras.job_position,'JOB') = NVL(p_in_jobPosition,NVL(ras.job_position,'JOB')) 
         AND ras.status                  = NVL(p_in_participantStatus,ras.status) 
         AND rh.is_deleted               =0 
         AND p_in_nodeAndBelow = 1
         GROUP BY ras.record_type, 
           rh.node_name, 
           ras.detail_node_id 
         UNION 
         SELECT DECODE (instr(ras.record_type,'team'),0,node_name,node_name 
         || ' Team') hierarchy_node_name, 
         NVL(PKG_REPORT_ENROLLMENT.FNC_GET_COUNT(detail_node_id, ras.record_type, TO_DATE(p_in_fromDate,p_in_localeDatePattern), TO_DATE(p_in_toDate,p_in_localeDatePattern), p_in_participantStatus, p_in_departments,p_in_countryIds, p_in_jobPosition, p_in_role, 'ACTIVE'),0) active_cnt, 
         rh.node_name node_name, 
         ras.detail_node_id detail_node_id 
       FROM rpt_enrollment_summary ras, 
         rpt_hierarchy rh 
       WHERE detail_node_id = rh.node_id 
       AND ras.record_type LIKE '%team%' 
       AND NVL(ras.detail_node_id,0)  IN (SELECT * FROM TABLE(get_array_varchar( nvl(p_in_parentNodeId,0)  ) )) 
       --AND (ras.country_id            IN(p_in_countryIds) 
        -- OR 'N'                          = p_in_filterCountries) 
         AND ((p_in_countryIds IS NULL) OR ras.country_id IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_countryIds)))) 
        -- AND (ras.department            IN(p_in_departments) 
       --  OR 'N'                          = p_in_filterDepartments) 
         AND ((p_in_departments IS NULL) OR ras.department IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
       AND UPPER(ras.role)             = NVL(UPPER(p_in_role),UPPER(ras.role)) 
       AND NVL(ras.job_position,'JOB') = NVL(p_in_jobPosition,NVL(ras.job_position,'JOB')) 
       AND ras.status                  = NVL(p_in_participantStatus,ras.status) 
       AND rh.is_deleted               =0 
       GROUP BY ras.record_type, 
         rh.node_name, 
         ras.detail_node_id 
       ORDER BY active_cnt DESC 
       ) )
     WHERE ROWNUM <=20; 
   p_out_return_code := '00';
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;

   END prc_getPieResults;

END pkg_query_enroll_reports;
/