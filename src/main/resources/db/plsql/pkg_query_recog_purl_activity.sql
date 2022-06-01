CREATE OR REPLACE PACKAGE pkg_query_recog_purl_activity
IS
    PROCEDURE prc_getSummaryTabularResults (
        p_in_departments         IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_department          IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR,
        p_out_totals_data        OUT    SYS_REFCURSOR);

    PROCEDURE prc_getPaxTabularResults (
        p_in_departments         IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_department          IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR,
        p_out_totals_data        OUT    SYS_REFCURSOR);

    PROCEDURE prc_getActivityChartResults (
        p_in_departments         IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_department          IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR);

    PROCEDURE prc_getContribChartResults (
        p_in_departments         IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_department          IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR);

    PROCEDURE prc_getRecipChartResults (
        p_in_departments         IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_department          IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR);
END pkg_query_recog_purl_activity;
/
CREATE OR REPLACE PACKAGE BODY pkg_query_recog_purl_activity
IS
 /******************************************************************************
  NAME:       pkg_query_recog_purl_activity (package body)  
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Suresh J             12/28/2015       Bug 61865 - Recognition PURL Activity Report - incorrect Recipients count  
 ******************************************************************************/

-- package constants

gc_null_id                       CONSTANT NUMBER(18) := pkg_const.gc_null_id;
gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
gc_pax_status_active             CONSTANT participant.status%TYPE := pkg_const.gc_pax_status_active;
gc_promotion_status_active       CONSTANT promotion.promotion_status%TYPE := pkg_const.gc_promotion_status_active;

gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
gc_node_name_team_suffix         CONSTANT VARCHAR2(30) := ' Team';
            

-----------------------
-- Private package processes
-----------------------
-- Stages the eligible count table based on whether input promotions have an all pax or specified audience.
-- Note: Process assumes the query parameters have already been staged to the temp ID list table.

PROCEDURE prc_getSummaryTabularResults (
        p_in_departments         IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_department          IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR,
        p_out_totals_data        OUT    SYS_REFCURSOR)
    IS
  /******************************************************************************
  NAME:       prc_get_byorgsummaryResults
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes   
   Loganathn       	   04/23/2019		Bug 79021 - Recognition PURL Activity Report is pulling off the date initiated instead of date awarded	                                   
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_get_byorgsummaryResults' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE;
  v_fetch_count         INTEGER;
  v_data_sort           VARCHAR2(50);

   CURSOR cur_query_ref IS
    SELECT CAST(NULL AS VARCHAR2(4000)) AS node_name,
             CAST(NULL AS NUMBER) AS node_id,
             CAST(NULL AS NUMBER) AS recipient_count,
             CAST(NULL AS NUMBER) AS contributors_invited,
             CAST(NULL AS NUMBER) AS contributed,
             CAST(NULL AS NUMBER) AS percent_contributed,
             CAST(NULL AS NUMBER) AS contributions_posted,
             CAST(NULL AS NUMBER) AS total_records,
             CAST(NULL AS NUMBER) AS rec_seq
        FROM dual;

   rec_query      cur_query_ref%ROWTYPE;    
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortedOn >' || p_in_sortedOn              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<';

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortedOn);
   ELSE
      v_data_sort := LOWER(p_in_sortedOn);
   END IF;
   
   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
                             
   OPEN p_out_data FOR
     WITH w_node AS
       (  -- get input parent node(s)
          SELECT h.node_id AS org_id,
                 h.node_name || gc_node_name_team_suffix AS org_name,
                 h.node_id
            FROM gtt_id_list gil,
                 rpt_hierarchy h
           WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
             AND gil.id = h.node_id 
             AND h.is_deleted = 0
           UNION ALL
          -- get child rollup nodes of input parent node(s) 
          SELECT h.node_id AS org_id,
                 h.node_name AS org_name,
                 hr.child_node_id AS node_id
            FROM gtt_id_list gil,
                 rpt_hierarchy h,
                 rpt_hierarchy_rollup hr
           WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
             AND gil.id = h.parent_node_id 
             AND h.is_deleted = 0
             AND h.node_id = hr.node_id
       )
       SELECT sd.*
         FROM ( -- sequence the data
                SELECT org.*,
                       COUNT(DISTINCT org.node_id) OVER() AS total_records,
                       -- calc record sort order
                       ROW_NUMBER() OVER (ORDER BY
                         -- sort totals record first
                         DECODE(org.node_id, NULL, 0, 99),
                         -- ascending sorts
                         DECODE(v_data_sort, 'node_id', org.node_id),
                         DECODE(v_data_sort, 'node_name', LOWER(org.node_name)),
                         DECODE(v_data_sort, 'recipient_count', org.recipient_count),
                         DECODE(v_data_sort, 'contributors_invited', org.contributors_invited),
                         DECODE(v_data_sort, 'contributed', org.contributed),
                         DECODE(v_data_sort, 'percent_contributed', org.percent_contributed),
                         DECODE(v_data_sort, 'contributions_posted', org.contributions_posted),
                         -- descending sorts
                         DECODE(v_data_sort, 'desc/node_id', org.node_id) DESC,
                         DECODE(v_data_sort, 'desc/node_name', LOWER(org.node_name)) DESC,
                         DECODE(v_data_sort, 'desc/recipient_count', org.recipient_count) DESC,
                         DECODE(v_data_sort, 'desc/contributors_invited', org.contributors_invited) DESC,
                         DECODE(v_data_sort, 'desc/contributed', org.contributed) DESC,
                         DECODE(v_data_sort, 'desc/percent_contributed', org.percent_contributed) DESC,
                         DECODE(v_data_sort, 'desc/contributions_posted', org.contributions_posted) DESC,
                         -- default sort fields
                         LOWER(org.node_name),
                         org.node_id
                       ) -1 AS rec_seq
                  FROM ( -- data grouped by org node
                        SELECT node_name, 
                               node_id, 
                               SUM(recipient_count) AS recipient_count,
                               NVL(SUM (contributors_invited),0) AS contributors_invited,
                               NVL(SUM (contributed),0) AS contributed,
                               DECODE (NVL (SUM (contributors_invited),0),0, 0,ROUND ((  SUM (contributed)/ SUM (contributors_invited))* 100,2)) AS percent_contributed,
                               NVL(SUM (contributions_posted),0) AS contributions_posted
                          FROM (SELECT wn.org_id AS node_id,
                                       wn.org_name AS node_name,
                                       COUNT(DISTINCT purl_recipient_id)  AS recipient_count,
                                       SUM (purl_contributor_count) AS contributors_invited,
                                       SUM (contributed_count) AS contributed,
                                       SUM (contribution_posted_count) AS contributions_posted
                                  FROM (SELECT wn2.org_id,
                                               wn2.node_id,
                                               rpd.purl_recipient_id,
                                               rpd.purl_contributor_count,
                                               rpd.contributed_count,
                                               rpd.contribution_posted_count
                                          FROM w_node wn2,
                                               rpt_purl_recipient_detail rpd, 
                                               promotion p,
                                               gtt_id_list gil_dt, -- department type
                                               gtt_id_list gil_p,  -- promotion
                                               gtt_id_list gil_pt  -- position type
                                         WHERE TRUNC(NVL (rpd.AWARD_DATE,SYSDATE)) BETWEEN v_from_date AND v_to_date --04/23/2019 Bug #79021 Changed award_date instead  purl_created date
                                           AND wn2.node_id = rpd.node_id
                                           AND rpd.promotion_id = p.promotion_id
                                           AND p.promotion_status = NVL (p_in_promotionStatus,p.promotion_status)
                                           AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                                           AND (  gil_p.ref_text_2 = gc_search_all_values
                                                OR gil_p.id = rpd.promotion_id )
                                           AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                           AND (  gil_dt.ref_text_2 = gc_search_all_values
                                                OR gil_dt.ref_text_2 = rpd.recipient_department )
                                           AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                           AND (  gil_pt.ref_text_2 = gc_search_all_values
                                                OR gil_pt.ref_text_2 = rpd.recipient_job_position )
                                           AND (  p_in_participantStatus IS NULL
                                                OR rpd.recipient_pax_status = p_in_participantStatus )
                                       ) rd,
                                       w_node wn
                                        -- restrict to just the org nodes
                                 WHERE wn.org_id = wn.node_id
                                      -- outer join so all queried org nodes appear in result set
                                   AND wn.org_id = rd.org_id (+)
                                 GROUP BY wn.org_id ,
                                          wn.org_name
                               ) 
                          GROUP  BY GROUPING SETS 
                               ((), 
                                (node_name,
                                 node_id)
                               )
                        ) org
                ) sd                   
       WHERE (  sd.rec_seq = 0   -- totals record
              OR -- reduce sequenced data set to just the output page's records
                 (   sd.rec_seq > p_in_rowNumStart
                 AND sd.rec_seq < p_in_rowNumEnd )
              )
        ORDER BY sd.rec_seq;                 
                        
   -- query totals data
   v_stage := 'FETCH p_out_data totals record';
   FETCH p_out_data INTO rec_query;
   v_fetch_count := p_out_data%ROWCOUNT;

   v_stage := 'OPEN p_out_totals_data';
   OPEN p_out_totals_data FOR
   SELECT rec_query.recipient_count         AS recipient_count,
          rec_query.contributors_invited    AS contributors_invited,
          rec_query.contributed             AS contributed,
          rec_query.percent_contributed     AS percent_contributed,
          rec_query.contributions_posted    AS contributions_posted
     FROM dual
    WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_data NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
      OPEN p_out_data FOR
      SELECT CAST(NULL AS VARCHAR2(4000)) AS node_name,
             CAST(NULL AS NUMBER) AS node_id,
             CAST(NULL AS NUMBER) AS recipient_count,
             CAST(NULL AS NUMBER) AS contributors_invited,
             CAST(NULL AS NUMBER) AS contributed,
             CAST(NULL AS NUMBER) AS percent_contributed,
             CAST(NULL AS NUMBER) AS contributions_posted,
             CAST(NULL AS NUMBER) AS total_records,
             CAST(NULL AS NUMBER) AS rec_seq
        FROM dual
       WHERE 0=1;
   END IF;

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
      OPEN p_out_totals_data FOR SELECT NULL FROM dual;
END prc_getSummaryTabularResults;                              
                        
    /* getPaxTabularResults */
PROCEDURE prc_getPaxTabularResults (
        p_in_departments         IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_department          IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR,
        p_out_totals_data        OUT    SYS_REFCURSOR)
    IS
  /******************************************************************************
  NAME:       prc_getPaxTabularResults
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes  
   Chidamba            10/11/2017       G6-3138 flip pax first name and last name
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_getPaxTabularResults' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE;
  v_fetch_count         INTEGER;
  v_default_locale      os_propertyset.string_val%TYPE;
  v_data_sort           VARCHAR2(50);

   CURSOR cur_query_ref IS
    SELECT CAST(NULL AS NUMBER) AS purl_recipient_id,
             CAST(NULL AS VARCHAR2(100)) AS recipient_name,
             CAST(NULL AS VARCHAR2(4000)) AS promotion_name,
             CAST(NULL AS VARCHAR2(1000)) AS recipient_country,
             CAST(NULL AS VARCHAR2(100)) AS manager_name,
             CAST(NULL AS DATE) AS award_date,
             CAST(NULL AS VARCHAR2(1000)) AS purl_status,
             CAST(NULL AS NUMBER) AS award,
             CAST(NULL AS NUMBER) AS contributors_invited,
             CAST(NULL AS NUMBER) AS actual_contributors,
             CAST(NULL AS NUMBER) AS contributions_percentage,
             CAST(NULL AS NUMBER) AS contributions_posted,
             CAST(NULL AS NUMBER) AS total_records,
             CAST(NULL AS NUMBER) AS rec_seq
        FROM dual;

   rec_query      cur_query_ref%ROWTYPE;      
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortedOn >' || p_in_sortedOn              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<';

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortedOn);
   ELSE
      v_data_sort := LOWER(p_in_sortedOn);
   END IF;
   
   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
   BEGIN
    SELECT string_val 
      INTO v_default_locale
      FROM os_propertyset
     WHERE entity_name = 'default.language';
   EXCEPTION
     WHEN OTHERS THEN
       v_default_locale := 'en_US';
   END;
   
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
   v_stage:= 'Insert temp_table_session';
  DELETE temp_table_session;
  INSERT INTO temp_table_session
   SELECT asset_code,cms_name,cms_code
     FROM (SELECT asset_code,cms_value as cms_name,key as cms_code ,  RANK() OVER(PARTITION BY asset_code ORDER BY DECODE(p_in_languageCode,locale,1,2)) rn
               FROM mv_cms_asset_value 
              WHERE key = 'COUNTRY_NAME'
                AND locale IN (v_default_locale, p_in_languageCode)
          )
     WHERE rn = 1;
                               
   OPEN p_out_data FOR
      SELECT sd.*
         FROM ( -- sequence the data
                SELECT org.*,
                       COUNT(DISTINCT org.purl_recipient_id) OVER() AS total_records,
                       -- calc record sort order
                       ROW_NUMBER() OVER (ORDER BY
                         -- sort totals record first
                         DECODE(org.purl_recipient_id, NULL, 0, 99),
                         -- ascending sorts
                         DECODE(v_data_sort, 'purl_recipient_id', org.purl_recipient_id),
                         DECODE(v_data_sort, 'recipient_name', LOWER(org.recipient_name)),
                         DECODE(v_data_sort, 'promotion_name', LOWER(org.promotion_name)),
                         DECODE(v_data_sort, 'recipient_country', LOWER(org.recipient_country)),
                         DECODE(v_data_sort, 'manager_name', LOWER(org.manager_name)),
                         DECODE(v_data_sort, 'award_date', org.award_date),
                         DECODE(v_data_sort, 'purl_status', LOWER(org.purl_status)),
                         DECODE(v_data_sort, 'award', org.award),
                         DECODE(v_data_sort, 'contributors_invited', org.contributors_invited),
                         DECODE(v_data_sort, 'actual_contributors', org.actual_contributors),
                         DECODE(v_data_sort, 'contributions_percentage', org.contributions_percentage),
                         DECODE(v_data_sort, 'contributions_posted', org.contributions_posted),
                         -- descending sorts
                         DECODE(v_data_sort, 'desc/purl_recipient_id', org.purl_recipient_id) DESC,
                         DECODE(v_data_sort, 'desc/recipient_name', LOWER(org.recipient_name)) DESC,
                         DECODE(v_data_sort, 'desc/promotion_name', LOWER(org.promotion_name)) DESC,
                         DECODE(v_data_sort, 'desc/recipient_country', LOWER(org.recipient_country)) DESC,
                         DECODE(v_data_sort, 'desc/manager_name', LOWER(org.manager_name)) DESC,
                         DECODE(v_data_sort, 'desc/award_date', org.award_date) DESC,
                         DECODE(v_data_sort, 'desc/purl_status', LOWER(org.purl_status)) DESC,
                         DECODE(v_data_sort, 'desc/award', org.award) DESC,
                         DECODE(v_data_sort, 'desc/contributors_invited', org.contributors_invited) DESC,
                         DECODE(v_data_sort, 'desc/actual_contributors', org.actual_contributors) DESC,
                         DECODE(v_data_sort, 'desc/contributions_percentage', org.contributions_percentage) DESC,
                         DECODE(v_data_sort, 'desc/contributions_posted', org.contributions_posted) DESC,
                         -- default sort fields
                         LOWER(org.recipient_name),
                         org.purl_recipient_id
                       ) -1 AS rec_seq
                  FROM ( -- data grouped by org node
                         SELECT purl_recipient_id,   
                                recipient_name,
                                promotion_name ,
                                recipient_country,
                                manager_name,
                                award_date,
                                purl_status ,
                                award,
                                SUM(NVL(contributors_invited,0)) AS contributors_invited,   
                                SUM (NVL(actual_contributors,0))   AS actual_contributors, 
                                CASE WHEN SUM (NVL(contributors_invited,0)) = 0 THEN 0
                                      ELSE ROUND ((SUM(NVL(actual_contributors,0)) / SUM (NVL(contributors_invited,0))) * 100, 2)  
                                END AS contributions_percentage,
                                SUM(NVL(contributions_posted,0)) AS contributions_posted
                           FROM ( SELECT cd.purl_recipient_id,   
                                         cd.recipient_last_name|| ' , '|| cd.recipient_first_name AS recipient_name, --10/11/2017 G6-3138
                                         cd.promotion_name ,
                                         NVL(tts_c.cms_name, ' ')  AS recipient_country,
                                         cd.manager_last_name|| ' , '|| cd.manager_first_name AS manager_name,  --10/11/2017 G6-3138
                                         award_date,
                                         cd.purl_status ,
                                         award_amount AS award,
                                         SUM(purl_contributor_count) AS contributors_invited,   
                                         SUM (contributed_count)   AS actual_contributors, 
                                         SUM(cd.contribution_posted_count) AS contributions_posted                                                            
                                    FROM (SELECT R.*, CASE  WHEN contribution_state = 'invitation' THEN 3
                                                         WHEN contribution_state = 'contribution' THEN 2
                                                         WHEN contribution_state = 'complete' THEN 1
                                                         WHEN contribution_state = 'expired' THEN 1
                                                         WHEN contribution_state = 'archived' THEN 1
                                                   END as purl_status_order,
                                                   CASE WHEN contribution_state = 'invitation' THEN 'Open'                               
                                                           WHEN contribution_state = 'contribution'THEN 'Open'                               
                                                           WHEN contribution_state = 'complete' THEN 'Complete'
                                                           WHEN contribution_state = 'expired' THEN 'Complete'
                                                           WHEN contribution_state = 'archived' THEN 'Complete'    
                                                   END PURL_STATUS
                                                FROM rpt_purl_recipient_detail R   
                                           ) cd,
                                         rpt_hierarchy rh,
                                         user_address ua,
                                         country c,
                                         promotion p,
                                         temp_table_session tts_c, -- cm country name
                                         gtt_id_list gil_dt, -- department type
                                         gtt_id_list gil_p,  -- promotion
                                         gtt_id_list gil_pt  -- position type
                                   WHERE rh.node_id = cd.node_id(+)
                                     AND cd.recipient_user_id = ua.user_id
                                     AND ua.is_primary = 1
                                     AND ua.country_id = c.country_id 
                                     AND cd.promotion_id = p.promotion_id
                                     AND p.promotion_status = NVL (p_in_promotionStatus, p.promotion_status)
                                     AND TRUNC(NVL (cd.purl_created_date,SYSDATE)) BETWEEN v_from_date AND v_to_date                         
                                     AND cd.node_id = p_in_parentNodeId
                                     AND c.cm_asset_code = tts_c.asset_code(+)
                                     AND c.name_cm_key = tts_c.cms_code (+)
                                     AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                                     AND (  gil_p.ref_text_2 = gc_search_all_values
                                          OR gil_p.id = cd.promotion_id )
                                     AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                     AND (  gil_dt.ref_text_2 = gc_search_all_values
                                          OR gil_dt.ref_text_2 = cd.recipient_department )
                                     AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                     AND (  gil_pt.ref_text_2 = gc_search_all_values
                                          OR gil_pt.ref_text_2 = cd.recipient_job_position )
                                     AND (  p_in_participantStatus IS NULL
                                          OR cd.recipient_pax_status = p_in_participantStatus )
                                GROUP BY cd.promotion_name,
                                         recipient_first_name,
                                         recipient_last_name,
                                         manager_first_name,
                                         manager_last_name,
                                         NVL(tts_c.cms_name, ' '),
                                         award_date,
                                         award_amount,
                                         ua.country_id,
                                         cd.purl_status,                   
                                         cd.purl_recipient_id
                                )
                          GROUP BY GROUPING SETS ((),
                                            (purl_recipient_id,   
                                             recipient_name,
                                             promotion_name ,
                                              recipient_country,
                                             manager_name,
                                             award_date,
                                             purl_status ,
                                             award))
                        ) org
              )  sd                   
       WHERE (  sd.rec_seq = 0   -- totals record
              OR -- reduce sequenced data set to just the output page's records
                 (   sd.rec_seq > p_in_rowNumStart
                 AND sd.rec_seq < p_in_rowNumEnd )
              )
        ORDER BY sd.rec_seq;                
                        
   -- query totals data
   v_stage := 'FETCH p_out_data totals record';
   FETCH p_out_data INTO rec_query;
   v_fetch_count := p_out_data%ROWCOUNT;

   v_stage := 'OPEN p_out_totals_data';
   OPEN p_out_totals_data FOR
   SELECT rec_query.contributors_invited         AS contributors_invited,
          rec_query.actual_contributors          AS actual_contributors,
          rec_query.contributions_percentage     AS contributions_percentage,
          rec_query.contributions_posted         AS contributions_posted
     FROM dual
    WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_data NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
      OPEN p_out_data FOR
      SELECT CAST(NULL AS NUMBER) AS purl_recipient_id,
             CAST(NULL AS VARCHAR2(100)) AS recipient_name,
             CAST(NULL AS VARCHAR2(4000)) AS promotion_name,
             CAST(NULL AS VARCHAR2(1000)) AS recipient_country,
             CAST(NULL AS VARCHAR2(100)) AS manager_name,
             CAST(NULL AS DATE) AS award_date,
             CAST(NULL AS VARCHAR2(1000)) AS purl_status,
             CAST(NULL AS NUMBER) AS award,
             CAST(NULL AS NUMBER) AS contributors_invited,
             CAST(NULL AS NUMBER) AS actual_contributors,
             CAST(NULL AS NUMBER) AS contributions_percentage,
             CAST(NULL AS NUMBER) AS contributions_posted,
             CAST(NULL AS NUMBER) AS total_records,
             CAST(NULL AS NUMBER) AS rec_seq
        FROM dual;
   END IF;

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
      OPEN p_out_totals_data FOR SELECT NULL FROM dual;
END prc_getPaxTabularResults;

    /* CHART QUERIES */
    /* getActivityChartResults */
PROCEDURE prc_getActivityChartResults (
        p_in_departments         IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_department          IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR)
    IS
  /******************************************************************************
  NAME:       prc_getActivityChartResults
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_getActivityChartResults' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE;
  v_data_sort           VARCHAR2(50); 
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortedOn >' || p_in_sortedOn              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<';

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortedOn);
   ELSE
      v_data_sort := LOWER(p_in_sortedOn);
   END IF;
   
   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
                             
   OPEN p_out_data FOR
     WITH w_node AS
       (  -- get input parent node(s)
          SELECT h.node_id AS org_id,
                 h.node_name || gc_node_name_team_suffix AS org_name,
                 h.node_id
            FROM gtt_id_list gil,
                 rpt_hierarchy h
           WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
             AND gil.id = h.node_id 
             AND h.is_deleted = 0
           UNION ALL
          -- get child rollup nodes of input parent node(s) 
          SELECT h.node_id AS org_id,
                 h.node_name AS org_name,
                 hr.child_node_id AS node_id
            FROM gtt_id_list gil,
                 rpt_hierarchy h,
                 rpt_hierarchy_rollup hr
           WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
             AND gil.id = h.parent_node_id 
             AND h.is_deleted = 0
             AND h.node_id = hr.node_id
       )
        SELECT wn.org_name AS node_name,
               NVL(SUM (purl_contributor_count),0) AS contributors_invited,
               NVL(SUM (contributed_count),0) AS contributed,
               NVL(SUM (purl_contributor_count),0) - NVL(SUM(contributed_count),0) AS not_contributed
          FROM (SELECT wn2.org_id,
                       wn2.node_id,
                       rpd.purl_recipient_id,
                       rpd.purl_contributor_count,
                       rpd.contributed_count,
                       rpd.contribution_posted_count
                  FROM w_node wn2,
                       rpt_purl_recipient_detail rpd, 
                       promotion p,
                       gtt_id_list gil_dt, -- department type
                       gtt_id_list gil_p,  -- promotion
                       gtt_id_list gil_pt  -- position type
                 WHERE TRUNC(NVL (rpd.purl_created_date,SYSDATE)) BETWEEN v_from_date AND v_to_date
                   AND wn2.node_id = rpd.node_id
                   AND rpd.promotion_id = p.promotion_id
                   AND p.promotion_status = NVL (p_in_promotionStatus,p.promotion_status)
                   AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                   AND (  gil_p.ref_text_2 = gc_search_all_values
                        OR gil_p.id = rpd.promotion_id )
                   AND gil_dt.ref_text_1 = gc_ref_text_department_type
                   AND (  gil_dt.ref_text_2 = gc_search_all_values
                        OR gil_dt.ref_text_2 = rpd.recipient_department )
                   AND gil_pt.ref_text_1 = gc_ref_text_position_type
                   AND (  gil_pt.ref_text_2 = gc_search_all_values
                        OR gil_pt.ref_text_2 = rpd.recipient_job_position )
                   AND (  p_in_participantStatus IS NULL
                        OR rpd.recipient_pax_status = p_in_participantStatus )
               ) rd,
               w_node wn
                -- restrict to just the org nodes
         WHERE wn.org_id = wn.node_id
              -- outer join so all queried org nodes appear in result set
           AND wn.org_id = rd.org_id (+)
         GROUP BY wn.org_name;               

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_getActivityChartResults;

    /* getOverallContribChartResults */
PROCEDURE prc_getContribChartResults (
        p_in_departments         IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_department          IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR)
    IS
  /******************************************************************************
  NAME:       prc_getContribChartResults
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_getContribChartResults' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE;
  v_data_sort           VARCHAR2(50); 
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortedOn >' || p_in_sortedOn              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<';

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortedOn);
   ELSE
      v_data_sort := LOWER(p_in_sortedOn);
   END IF;
   
   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
                             
   OPEN p_out_data FOR
     SELECT NVL(SUM (contributed_count),0) AS contributed,
            NVL(SUM (purl_contributor_count),0) - NVL(SUM(contributed_count),0) AS not_contributed
      FROM (SELECT rpd.purl_contributor_count,
                   rpd.contributed_count
              FROM rpt_purl_recipient_detail rpd, 
                   promotion p,
                   gtt_id_list gil_dt, -- department type
                   gtt_id_list gil_p,  -- promotion
                   gtt_id_list gil_pt  -- position type
             WHERE TRUNC(NVL (rpd.purl_created_date,SYSDATE)) BETWEEN v_from_date AND v_to_date
               AND rpd.promotion_id = p.promotion_id
               AND p.promotion_status = NVL (p_in_promotionStatus,p.promotion_status)
               AND gil_p.ref_text_1 = gc_ref_text_promotion_id
               AND (  gil_p.ref_text_2 = gc_search_all_values
                    OR gil_p.id = rpd.promotion_id )
               AND gil_dt.ref_text_1 = gc_ref_text_department_type
               AND (  gil_dt.ref_text_2 = gc_search_all_values
                    OR gil_dt.ref_text_2 = rpd.recipient_department )
               AND gil_pt.ref_text_1 = gc_ref_text_position_type
               AND (  gil_pt.ref_text_2 = gc_search_all_values
                    OR gil_pt.ref_text_2 = rpd.recipient_job_position )
               AND (  p_in_participantStatus IS NULL
                    OR rpd.recipient_pax_status = p_in_participantStatus )
               AND EXISTS (SELECT 1
                            FROM gtt_id_list gil,
                                 rpt_hierarchy h,
                                 rpt_hierarchy_rollup hr
                           WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
                             AND gil.id = h.node_id 
                             AND h.is_deleted = 0
                             AND h.node_id = hr.node_id
                             AND hr.child_node_id = rpd.node_id 
                           )
           ) rd;              

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_getContribChartResults;

    /* getPurlReceipientsChartResults */
PROCEDURE prc_getRecipChartResults (
        p_in_departments         IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_department          IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR)
    IS
   /******************************************************************************
  NAME:       getPurlReceipientsChartResults
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'getPurlReceipientsChartResults' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE;
  v_data_sort           VARCHAR2(50); 
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortedOn >' || p_in_sortedOn              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<';

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortedOn);
   ELSE
      v_data_sort := LOWER(p_in_sortedOn);
   END IF;
   
   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
                             
   OPEN p_out_data FOR
     WITH w_node AS
       (  -- get input parent node(s)
          SELECT h.node_id AS org_id,
                 h.node_name || gc_node_name_team_suffix AS org_name,
                 h.node_id
            FROM gtt_id_list gil,
                 rpt_hierarchy h
           WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
             AND gil.id = h.node_id 
             AND h.is_deleted = 0
           UNION ALL
          -- get child rollup nodes of input parent node(s) 
          SELECT h.node_id AS org_id,
                 h.node_name AS org_name,
                 hr.child_node_id AS node_id
            FROM gtt_id_list gil,
                 rpt_hierarchy h,
                 rpt_hierarchy_rollup hr
           WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
             AND gil.id = h.parent_node_id 
             AND h.is_deleted = 0
             AND h.node_id = hr.node_id
       )
        SELECT wn.org_name AS node_name,
               COUNT(DISTINCT purl_recipient_id) AS recipient_count
          FROM (SELECT wn2.org_id,
                       wn2.node_id,
                       rpd.purl_recipient_id
                  FROM w_node wn2,
                       rpt_purl_recipient_detail rpd, 
                       promotion p,
                       gtt_id_list gil_dt, -- department type
                       gtt_id_list gil_p,  -- promotion
                       gtt_id_list gil_pt  -- position type
                 WHERE TRUNC(NVL (rpd.purl_created_date,SYSDATE)) BETWEEN v_from_date AND v_to_date
                   AND wn2.node_id = rpd.node_id
                   AND rpd.promotion_id = p.promotion_id
                   AND p.promotion_status = NVL (p_in_promotionStatus,p.promotion_status)
                   AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                   AND (  gil_p.ref_text_2 = gc_search_all_values
                        OR gil_p.id = rpd.promotion_id )
                   AND gil_dt.ref_text_1 = gc_ref_text_department_type
                   AND (  gil_dt.ref_text_2 = gc_search_all_values
                        OR gil_dt.ref_text_2 = rpd.recipient_department )
                   AND gil_pt.ref_text_1 = gc_ref_text_position_type
                   AND (  gil_pt.ref_text_2 = gc_search_all_values
                        OR gil_pt.ref_text_2 = rpd.recipient_job_position )
                   AND (  p_in_participantStatus IS NULL
                        OR rpd.recipient_pax_status = p_in_participantStatus )
               ) rd,
               w_node wn
                -- restrict to just the org nodes
         WHERE wn.org_id = wn.node_id
              -- outer join so all queried org nodes appear in result set
           AND wn.org_id = rd.org_id (+)
         GROUP BY wn.org_name;               

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_getRecipChartResults;
END pkg_query_recog_purl_activity;
/
