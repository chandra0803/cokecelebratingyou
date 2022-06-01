CREATE OR REPLACE PACKAGE pkg_query_throwdown_activity AS
/******************************************************************************
   NAME:       pkg_query_throwdown_activity
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        7/30/2014      keshaboi       1. Created this package.
******************************************************************************/

  /* getThrowdownActivityByPaxSummaryTabularResults */
   PROCEDURE prc_getByPaxSummary (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_roundNumber         IN     NUMBER,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_userId              IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
      
  /* getThrowdownActivityByPaxDetailTabularResults */
  PROCEDURE prc_getByPaxDetail (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_roundNumber         IN     NUMBER,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_userId              IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
      
      /* getThrowdownTotalActivityChartResults */
 PROCEDURE prc_getTotalActivityChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_roundNumber         IN     NUMBER,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_userId              IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      
      /* getThrowdownActivityByRoundChartResults */
 PROCEDURE prc_getByRoundChart(
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_roundNumber         IN     NUMBER,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_userId              IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      
      /* getPointsEarnedInThrowdownChartResults */
PROCEDURE prc_getPointsEarned (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_roundNumber         IN     NUMBER,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_userId              IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);

END pkg_query_throwdown_activity;



/
CREATE OR REPLACE PACKAGE BODY      pkg_query_throwdown_activity
AS
/* getThrowdownActivityByPaxSummaryTabularResults */
/******************************************************************************
  NAME:       prc_getByPaxSummary
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
                                        Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J             12/23/2014     Bug Fix 58546  - Summary Tab Result to include Promotion Name column                                    
  ******************************************************************************/

   PROCEDURE prc_getByPaxSummary (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_roundNumber         IN     NUMBER,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_userId              IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getByPaxSummary' ;
      v_stage          VARCHAR2 (500);
      v_sortCol        VARCHAR2(200);
       l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getByPaxSummary';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT rownum as rn, 
              rs.*
        FROM 
     ( WITH pax_rank 
          AS ( 
          SELECT promotion_id, user_id, rank FROM ( 
     select RANK() OVER (PARTITION BY pe.user_id,promotion_id ORDER BY pe.date_created DESC) as rec_rank, 
                     pe.* 
                FROM (SELECT ts.promotion_id, tsp.user_id, tsp.RANK,tsp.date_created 
                FROM throwdown_stackrank ts, 
                     throwdown_stackrank_node tsn, 
                     throwdown_stackrank_pax tsp 
               WHERE     (   ( '''||p_in_promotionId||''' IS NULL) 
                          OR (ts.promotion_id IN (SELECT * 
                                                    FROM TABLE ( 
                                                            get_array_varchar ( 
                                                               '''||p_in_promotionId||'''))))) 
                     AND tsn.node_id IS NULL 
                     AND ts.is_active = 1 
                     AND ts.stackrank_id = tsn.stackrank_id 
                     AND tsn.stackrank_node_id = tsp.stackrank_node_id) pe ) WHERE rec_rank=1 
                     ) 
       SELECT rpt.user_id, 
              last_name || '' , '' || first_name AS participant_name, 
              rpt.login_id, 
              cntry.cms_value AS COUNTRY, 
              status.cms_name user_status, 
              rpt.node_name, 
              dpt.cms_name department, 
              pn.cms_value as promo_name,   --12/23/2014              
              j.cms_name job_position, 
              NVL (SUM (DECODE (outcome, ''win'', 1, 0)), 0) Wins, 
              NVL (SUM (DECODE (outcome, ''tie'', 1, 0)), 0) Ties, 
              NVL (SUM (DECODE (outcome, ''loss'', 1, 0)), 0) Losses, 
              NVL (SUM (current_value), 0) activity, 
              pax_rank.RANK, 
              NVL (SUM (payout), 0) AS payout 
         FROM rpt_throwdown_activity rpt, 
              promotion p, 
              country c, 
              pax_rank 
     , ( SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value vw 
         WHERE asset_code = ''picklist.department.type.items'' and locale = (SELECT string_val FROM os_propertyset WHERE entity_name = ''default.language'') 
         AND NOT EXISTS (SELECT * FROM vw_cms_code_value WHERE asset_code = ''picklist.department.type.items'' and locale ='''||p_in_languageCode||''')  
         UNION ALL 
         SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value 
         WHERE asset_code = ''picklist.department.type.items'' and locale ='''||p_in_languageCode||''' ) dpt      
     , ( SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value vw 
         WHERE asset_code = ''picklist.positiontype.items'' and locale = (SELECT string_val FROM os_propertyset WHERE entity_name = ''default.language'') 
         AND NOT EXISTS (SELECT * FROM vw_cms_code_value WHERE asset_code = ''picklist.positiontype.items'' and locale ='''||p_in_languageCode||''')  
         UNION ALL 
         SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value 
         WHERE asset_code = ''picklist.positiontype.items'' and locale ='''||p_in_languageCode||'''  ) j,     
              (SELECT cms_code, cms_name 
       FROM vw_cms_code_value vw 
      WHERE     asset_code = ''picklist.participantstatus.items'' 
            AND locale = (SELECT string_val FROM os_propertyset WHERE entity_name = ''default.language'') AND NOT EXISTS  
              (SELECT * FROM vw_cms_code_value WHERE     asset_code = ''picklist.participantstatus.items'' AND locale = '''||p_in_languageCode||''') 
     UNION ALL 
     SELECT cms_code, cms_name 
       FROM vw_cms_code_value 
      WHERE     asset_code = ''picklist.participantstatus.items'' AND locale = '''||p_in_languageCode||''')  status, 
              (SELECT asset_code, cms_value FROM vw_cms_asset_value 
                WHERE key = ''COUNTRY_NAME'' AND locale = (SELECT string_val FROM os_propertyset WHERE entity_name = ''default.language'') AND NOT EXISTS  
              (SELECT * FROM vw_cms_asset_value WHERE    key = ''COUNTRY_NAME'' AND locale = '''||p_in_languageCode||''') 
     UNION ALL 
     SELECT asset_code, cms_value 
       FROM vw_cms_asset_value WHERE    key = ''COUNTRY_NAME'' AND locale = '''||p_in_languageCode||''') cntry,
     (SELECT asset_code, cms_value FROM vw_cms_asset_value            --12/23/2014
                WHERE key = ''PROMOTION_NAME_'' AND locale = (SELECT string_val FROM os_propertyset WHERE entity_name = ''default.language'') AND NOT EXISTS      
                      (SELECT * FROM vw_cms_asset_value WHERE    key = ''PROMOTION_NAME_'' AND locale = '''||p_in_languageCode||''')    
     UNION ALL 
     SELECT asset_code, cms_value 
       FROM vw_cms_asset_value WHERE    key = ''PROMOTION_NAME_'' AND locale = '''||p_in_languageCode||''') pn   --12/23/2014
        WHERE     rpt.promotion_id = p.promotion_id 
              AND p.promotion_status = NVL ( '''||p_in_promotionStatus||''', p.promotion_status) 
              AND rpt.user_status = NVL ( '''||p_in_participantStatus||''', rpt.user_status) 
              AND rpt.round_number <= NVL ( '''||p_in_roundNumber||''', rpt.round_number) 
              AND NVL (rpt.job_position, ''JOB'') = 
                     NVL ( '''||p_in_jobPosition||''', NVL (rpt.job_position, ''JOB'')) 
              AND rpt.job_position = j.cms_code(+) 
              AND rpt.department = dpt.cms_code(+) 
              AND rpt.user_status = status.cms_code(+) 
              AND rpt.country_id = c.country_id 
              AND c.cm_asset_code = cntry.asset_code 
              AND p.promo_name_asset_code = pn.asset_code (+)    --12/23/2014              
              AND rpt.promotion_id = pax_rank.promotion_id(+) 
              AND rpt.user_id = pax_rank.user_id(+) 
              AND (   ( '''||p_in_departments||''' IS NULL) 
                   OR (rpt.department IN (SELECT * 
                                            FROM TABLE ( 
                                                    get_array_varchar ( '''||p_in_departments||'''))))) 
              AND (   ( '''||p_in_promotionId||''' IS NULL) 
                   OR (p.promotion_id IN (SELECT * 
                                            FROM TABLE ( 
                                                    get_array_varchar ( 
                                                       '''||p_in_promotionId||'''))))) 
              AND rpt.node_id IN (SELECT child_node_id 
                                    FROM rpt_hierarchy_rollup 
                                   WHERE node_id IN (SELECT * 
                                                       FROM TABLE ( 
                                                               get_array_varchar ( 
                                                                  '''||p_in_parentNodeId||''')))) 
     GROUP BY rpt.user_id, 
              last_name, 
              first_name, 
              rpt.login_id, 
              cntry.cms_value, 
              rpt.node_name, 
              dpt.cms_name, 
              j.cms_name, 
              status.cms_name, 
              pax_rank.RANK 
              ,pn.cms_value   --12/23/2014
     ORDER BY '|| v_sortCol ||' ) rs
   ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
      OPEN p_out_data FOR l_query;
/* getThrowdownActivityByPaxSummaryTabularResultsSize */
  SELECT COUNT (1) INTO p_out_size_data
     FROM 
     ( WITH pax_rank 
          AS ( 
          SELECT promotion_id, user_id, rank FROM ( 
     select RANK() OVER (PARTITION BY pe.user_id,promotion_id ORDER BY pe.date_created DESC) as rec_rank, 
                     pe.* 
                FROM (SELECT ts.promotion_id, tsp.user_id, tsp.RANK,tsp.date_created 
                FROM throwdown_stackrank ts, 
                     throwdown_stackrank_node tsn, 
                     throwdown_stackrank_pax tsp 
               WHERE     (   ( p_in_promotionId IS NULL) 
                          OR (ts.promotion_id IN (SELECT * 
                                                    FROM TABLE ( 
                                                            get_array_varchar ( 
                                                               p_in_promotionId))))) 
                     AND tsn.node_id IS NULL 
                     AND ts.is_active = 1 
                     AND ts.stackrank_id = tsn.stackrank_id 
                     AND tsn.stackrank_node_id = tsp.stackrank_node_id) pe ) WHERE rec_rank=1 
                     ) 
       SELECT rpt.user_id, 
              last_name || ' , ' || first_name AS participant_name, 
              rpt.login_id, 
              cntry.cms_value AS COUNTRY, 
              status.cms_name user_status, 
              rpt.node_name, 
              dpt.cms_name department, 
              pn.cms_value as promo_name,   --12/23/2014              
              j.cms_name job_position, 
              NVL (SUM (DECODE (outcome, 'win', 1, 0)), 0) Wins, 
              NVL (SUM (DECODE (outcome, 'tie', 1, 0)), 0) Ties, 
              NVL (SUM (DECODE (outcome, 'loss', 1, 0)), 0) Losses, 
              NVL (SUM (current_value), 0) activity, 
              pax_rank.RANK, 
              NVL (SUM (payout), 0) AS payout 
         FROM rpt_throwdown_activity rpt, 
              promotion p, 
              country c, 
              pax_rank 
     , ( SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value vw 
         WHERE asset_code = 'picklist.department.type.items' and locale = (SELECT string_val FROM os_propertyset WHERE entity_name = 'default.language') 
         AND NOT EXISTS (SELECT * FROM vw_cms_code_value WHERE asset_code = 'picklist.department.type.items' and locale =p_in_languageCode)  
         UNION ALL 
         SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value 
         WHERE asset_code = 'picklist.department.type.items' and locale =p_in_languageCode ) dpt      
     , ( SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value vw 
         WHERE asset_code = 'picklist.positiontype.items' and locale = (SELECT string_val FROM os_propertyset WHERE entity_name = 'default.language') 
         AND NOT EXISTS (SELECT * FROM vw_cms_code_value WHERE asset_code = 'picklist.positiontype.items' and locale =p_in_languageCode)  
         UNION ALL 
         SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value 
         WHERE asset_code = 'picklist.positiontype.items' and locale =p_in_languageCode  ) j,     
              (SELECT cms_code, cms_name 
       FROM vw_cms_code_value vw 
      WHERE     asset_code = 'picklist.participantstatus.items' 
            AND locale = (SELECT string_val FROM os_propertyset WHERE entity_name = 'default.language') AND NOT EXISTS  
              (SELECT * FROM vw_cms_code_value WHERE     asset_code = 'picklist.participantstatus.items' AND locale = p_in_languageCode) 
     UNION ALL 
     SELECT cms_code, cms_name 
       FROM vw_cms_code_value 
      WHERE     asset_code = 'picklist.participantstatus.items' AND locale = p_in_languageCode)  status, 
              (SELECT asset_code, cms_value FROM vw_cms_asset_value 
                WHERE key = 'COUNTRY_NAME' AND locale = (SELECT string_val FROM os_propertyset WHERE entity_name = 'default.language') AND NOT EXISTS  
              (SELECT * FROM vw_cms_asset_value WHERE    key = 'COUNTRY_NAME' AND locale = p_in_languageCode) 
     UNION ALL 
     SELECT asset_code, cms_value 
       FROM vw_cms_asset_value WHERE    key = 'COUNTRY_NAME' AND locale = p_in_languageCode) cntry,
       (SELECT asset_code,cms_value,key FROM VW_CMS_ASSET_VALUE 
        WHERE key = 'PROMOTION_NAME_'
              AND locale = (SELECT string_val FROM os_propertyset WHERE entity_name = 'default.language') AND NOT EXISTS  
              (SELECT * FROM vw_cms_asset_value WHERE    key = 'PROMOTION_NAME_' AND locale = p_in_languageCode) 
        UNION ALL
        SELECT asset_code,cms_value,key FROM VW_CMS_ASSET_VALUE 
        WHERE key = 'PROMOTION_NAME_'
              AND locale = p_in_languageCode) pn  --12/23/2014
        WHERE     rpt.promotion_id = p.promotion_id 
              AND p.promotion_status = NVL ( p_in_promotionStatus, p.promotion_status) 
              AND rpt.user_status = NVL ( p_in_participantStatus, rpt.user_status) 
              AND rpt.round_number <= NVL ( p_in_roundNumber, rpt.round_number) 
              AND NVL (rpt.job_position, 'JOB') = 
                     NVL ( p_in_jobPosition, NVL (rpt.job_position, 'JOB')) 
              AND rpt.job_position = j.cms_code(+) 
              AND rpt.department = dpt.cms_code(+) 
              AND rpt.user_status = status.cms_code(+) 
              AND rpt.country_id = c.country_id 
              AND c.cm_asset_code = cntry.asset_code 
              AND p.promo_name_asset_code = pn.asset_code (+)    --12/23/2014
              AND rpt.promotion_id = pax_rank.promotion_id(+)
              AND rpt.user_id = pax_rank.user_id(+) 
              AND (   ( p_in_departments IS NULL) 
                   OR (rpt.department IN (SELECT * 
                                            FROM TABLE ( 
                                                    get_array_varchar ( p_in_departments))))) 
              AND (   ( p_in_promotionId IS NULL) 
                   OR (p.promotion_id IN (SELECT * 
                                            FROM TABLE ( 
                                                    get_array_varchar ( 
                                                       p_in_promotionId))))) 
              AND rpt.node_id IN (SELECT child_node_id 
                                    FROM rpt_hierarchy_rollup 
                                   WHERE node_id IN (SELECT * 
                                                       FROM TABLE ( 
                                                               get_array_varchar ( 
                                                                  p_in_parentNodeId)))) 
     GROUP BY rpt.user_id, 
              last_name, 
              first_name, 
              rpt.login_id, 
              cntry.cms_value, 
              rpt.node_name, 
              dpt.cms_name, 
              j.cms_name, 
              status.cms_name, 
              pax_rank.RANK
              ,pn.cms_value   --12/23/2014
        );
/* getThrowdownActivityByPaxSummaryTabularResultsTotals */
 OPEN p_out_totals_data FOR
 SELECT SUM (DECODE(outcome,'win',1,0)) Wins, 
       SUM (DECODE(outcome,'tie',1,0)) Ties, 
       SUM (DECODE(outcome,'loss',1,0)) Losses, 
       SUM (current_value) activity, 
       SUM(payout) AS payout 
     FROM rpt_throwdown_activity rpt, 
       promotion p, 
       country c 
     WHERE rpt.promotion_id          = p.promotion_id 
     AND p.promotion_status          = NVL (p_in_promotionStatus,p.promotion_status) 
     AND rpt.user_status             = NVL (p_in_participantStatus,rpt.user_status) 
     AND rpt.round_number           <= NVL(p_in_roundNumber,rpt.round_number) 
     AND NVL(rpt.job_position,'JOB') = NVL (p_in_jobPosition,NVL(rpt.job_position,'JOB')) 
     AND rpt.country_id              = c.country_id 
     AND ((p_in_departments              IS NULL) 
     OR ( rpt.department            IN 
       (SELECT * FROM TABLE(get_array_varchar(p_in_departments)) 
       ))) 
     AND ((p_in_promotionId IS NULL) 
     OR ( p.promotion_id IN 
       (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)) 
       ))) 
     AND rpt.node_id IN 
       (SELECT child_node_id 
       FROM rpt_hierarchy_rollup 
       WHERE node_id IN 
         (SELECT * FROM TABLE(get_array_varchar(p_in_parentNodeId)) 
         ) 
       ); 
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
 END prc_getByPaxSummary;       
/* getThrowdownActivityByPaxDetailTabularResults */

/******************************************************************************
  NAME:       prc_getByPaxDetail
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
                                        Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J               09/11/2014     Bug Fix 56365  - Fixed invalid column name issue                                   
  ******************************************************************************/

  PROCEDURE prc_getByPaxDetail (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_roundNumber         IN     NUMBER,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_userId              IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getByPaxDetail' ;
      v_stage          VARCHAR2 (500);
      v_sortCol        VARCHAR2(200);      
      l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getByPaxDetail';

/*
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT rownum as rn, 
              rs.*
        FROM 
     ( WITH pax_rank 

*/

      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT rownum as rn,     --09/11/2014 
              rs.*                    --09/11/2014
              FROM                    --09/11/2014
              ('                      --09/11/2014    
         || 'WITH pax_rank AS 
       (SELECT ts.promotion_id, 
         ts.round_number, 
         tsp.user_id, 
         tsp.rank 
       FROM throwdown_stackrank ts, 
         throwdown_stackrank_node tsn, 
         throwdown_stackrank_pax tsp 
       WHERE (('''||p_in_promotionId||''' IS NULL) 
       OR ( ts.promotion_id  IN 
         (SELECT * FROM TABLE(get_array_varchar('''||p_in_promotionId||''')) 
         ))) 
       AND tsn.node_id          IS NULL 
       AND ts.stackrank_id       = tsn.stackrank_id 
       AND ts.is_active          = 1 
       AND tsn.stackrank_node_id = tsp.stackrank_node_id 
       AND tsp.user_id           = '''||p_in_userId||''' 
       ) 
     SELECT rpt.user_id, 
       last_name 
       ||'' , '' 
       ||first_name AS participant_name, 
       rpt.login_id, 
       cntry.cms_value AS COUNTRY, 
       status.cms_name user_status, 
       rpt.node_name, 
       dpt.cms_name department, 
       j.cms_name job_position, 
       rpt.promotion_name, 
       rpt.round_number, 
       rpt.round_start_date, 
       rpt.round_end_date, 
       SUM (DECODE(outcome,''win'',1,0)) Wins, 
       SUM (DECODE(outcome,''tie'',1,0)) Ties, 
       SUM (DECODE(outcome,''loss'',1,0)) Losses, 
       SUM (current_value) activity, 
       pax_rank.RANK, 
       SUM(payout) AS payout 
     FROM rpt_throwdown_activity rpt, 
       promotion p, 
       country c, 
       pax_rank 
     , ( SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value vw 
         WHERE asset_code = ''picklist.department.type.items'' and locale = (SELECT string_val FROM os_propertyset WHERE entity_name = ''default.language'') 
         AND NOT EXISTS (SELECT * FROM vw_cms_code_value WHERE asset_code = ''picklist.department.type.items'' and locale ='''||p_in_languageCode||''')  
         UNION ALL 
         SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value 
         WHERE asset_code = ''picklist.department.type.items'' and locale ='''||p_in_languageCode||''' ) dpt      
     ,( SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value vw 
         WHERE asset_code = ''picklist.positiontype.items'' and locale = (SELECT string_val FROM os_propertyset WHERE entity_name = ''default.language'') 
         AND NOT EXISTS (SELECT * FROM vw_cms_code_value WHERE asset_code = ''picklist.positiontype.items'' and locale ='''||p_in_languageCode||''')  
         UNION ALL 
         SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value 
         WHERE asset_code = ''picklist.positiontype.items'' and locale ='''||p_in_languageCode||'''  ) j,     
       (SELECT cms_code, 
         cms_name 
       FROM vw_cms_code_value vw 
       WHERE asset_code = ''picklist.participantstatus.items'' 
       AND locale       = 
         (SELECT string_val FROM os_propertyset WHERE entity_name = ''default.language'' 
         ) 
       AND NOT EXISTS 
         (SELECT * 
         FROM vw_cms_code_value 
         WHERE asset_code = ''picklist.participantstatus.items'' 
         AND locale       = '''||p_in_languageCode||''' 
         ) 
       UNION ALL 
       SELECT cms_code, 
         cms_name 
       FROM vw_cms_code_value 
       WHERE asset_code = ''picklist.participantstatus.items'' 
       AND locale       = '''||p_in_languageCode||''' 
       ) status, 
       (SELECT asset_code, 
         cms_value 
       FROM vw_cms_asset_value 
       WHERE KEY  = ''COUNTRY_NAME'' 
       AND locale = 
         (SELECT string_val FROM os_propertyset WHERE entity_name = ''default.language'' 
         ) 
       AND NOT EXISTS 
         (SELECT * 
         FROM vw_cms_asset_value 
         WHERE KEY  = ''COUNTRY_NAME'' 
         AND locale = '''||p_in_languageCode||''' 
         ) 
       UNION ALL 
       SELECT asset_code, 
         cms_value 
       FROM vw_cms_asset_value 
       WHERE KEY  = ''COUNTRY_NAME'' 
       AND locale = '''||p_in_languageCode||''' 
       ) cntry 
     WHERE rpt.promotion_id          = p.promotion_id 
     AND p.promotion_status          = NVL ('''||p_in_promotionStatus||''',p.promotion_status) 
     AND rpt.user_status             = NVL ('''||p_in_participantStatus||''',rpt.user_status) 
     AND rpt.user_id                 = '''||p_in_userId||''' 
     AND rpt.round_number           <= NVL('''||p_in_roundNumber||''',rpt.round_number) 
     AND NVL(rpt.job_position,''JOB'') = NVL ('''||p_in_jobPosition||''',NVL(rpt.job_position,''JOB'')) 
     AND rpt.job_position            = j.cms_code(+) 
     AND rpt.department              = dpt.cms_code(+) 
     AND rpt.user_status             = status.cms_code(+) 
     AND rpt.country_id              = c.country_id 
     AND c.cm_asset_code             = cntry.asset_code 
     AND (('''||p_in_departments||'''              IS NULL) 
     OR ( rpt.department            IN 
       (SELECT * FROM TABLE(get_array_varchar('''||p_in_departments||''')) 
       ))) 
     AND (('''||p_in_promotionId||''' IS NULL) 
     OR ( p.promotion_id IN 
       (SELECT * FROM TABLE(get_array_varchar('''||p_in_promotionId||''')) 
       ))) 
     AND rpt.node_id IN 
       (SELECT child_node_id 
       FROM rpt_hierarchy_rollup 
       WHERE node_id IN 
         (SELECT * FROM TABLE(get_array_varchar('''||p_in_parentNodeId||''')) 
         ) 
       ) 
     AND rpt.promotion_id = pax_rank.promotion_id(+) 
     AND rpt.round_number = pax_rank.round_number(+) 
     GROUP BY rpt.user_id, 
       last_name, 
       first_name, 
       rpt.login_id, 
       cntry.cms_value, 
       rpt.node_name, 
       dpt.cms_name, 
       j.cms_name, 
       status.cms_name, 
       rpt.promotion_name, 
       rpt.round_number, 
       pax_rank.rank, 
       rpt.round_start_date, 
       rpt.round_end_date 
     ORDER BY '|| v_sortCol ||'
   ) rs ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;   --09/11/2014
  
      OPEN p_out_data FOR l_query;
/* getThrowdownActivityByPaxDetailTabularResultsSize */
 SELECT COUNT (1) INTO p_out_size_data
     FROM 
     ( WITH pax_rank AS 
       (SELECT ts.promotion_id, 
         ts.round_number, 
         tsp.user_id, 
         tsp.rank 
       FROM throwdown_stackrank ts, 
         throwdown_stackrank_node tsn, 
         throwdown_stackrank_pax tsp 
       WHERE ((p_in_promotionId IS NULL) 
       OR ( ts.promotion_id  IN 
         (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)) 
         ))) 
       AND tsn.node_id          IS NULL 
       AND ts.stackrank_id       = tsn.stackrank_id 
       AND ts.is_active          = 1 
       AND tsn.stackrank_node_id = tsp.stackrank_node_id 
       AND tsp.user_id           = p_in_userId 
       ) 
     SELECT rpt.user_id, 
       last_name 
       ||' , ' 
       ||first_name AS participant_name, 
       rpt.login_id, 
       cntry.cms_value AS COUNTRY, 
       status.cms_name user_status, 
       rpt.node_name, 
       dpt.cms_name department, 
       j.cms_name job_position, 
       rpt.promotion_name, 
       rpt.round_number, 
       rpt.round_start_date, 
       rpt.round_end_date, 
       SUM (DECODE(outcome,'win',1,0)) Wins, 
       SUM (DECODE(outcome,'tie',1,0)) Ties, 
       SUM (DECODE(outcome,'loss',1,0)) Losses, 
       SUM (current_value) activity, 
       pax_rank.RANK, 
       SUM(payout) AS payout 
     FROM rpt_throwdown_activity rpt, 
       promotion p, 
       country c, 
       pax_rank, 
      ( SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value vw 
         WHERE asset_code = 'picklist.department.type.items' and locale = (SELECT string_val FROM os_propertyset WHERE entity_name = 'default.language') 
         AND NOT EXISTS (SELECT * FROM vw_cms_code_value WHERE asset_code = 'picklist.department.type.items' and locale =p_in_languageCode)  
         UNION ALL 
         SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value 
         WHERE asset_code = 'picklist.department.type.items' and locale =p_in_languageCode ) dpt      
     , ( SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value vw 
         WHERE asset_code = 'picklist.positiontype.items' and locale = (SELECT string_val FROM os_propertyset WHERE entity_name = 'default.language') 
         AND NOT EXISTS (SELECT * FROM vw_cms_code_value WHERE asset_code = 'picklist.positiontype.items' and locale =p_in_languageCode)  
         UNION ALL 
         SELECT cms_code, 
           cms_name 
         FROM vw_cms_code_value 
         WHERE asset_code = 'picklist.positiontype.items' and locale =p_in_languageCode  ) j,     
       (SELECT cms_code, 
         cms_name 
       FROM vw_cms_code_value vw 
       WHERE asset_code = 'picklist.participantstatus.items' 
       AND locale       = 
         (SELECT string_val FROM os_propertyset WHERE entity_name = 'default.language' 
         ) 
       AND NOT EXISTS 
         (SELECT * 
         FROM vw_cms_code_value 
         WHERE asset_code = 'picklist.participantstatus.items' 
         AND locale       = p_in_languageCode 
         ) 
       UNION ALL 
       SELECT cms_code, 
         cms_name 
       FROM vw_cms_code_value 
       WHERE asset_code = 'picklist.participantstatus.items' 
       AND locale       = p_in_languageCode 
       ) status, 
       (SELECT asset_code, 
         cms_value 
       FROM vw_cms_asset_value 
       WHERE KEY  = 'COUNTRY_NAME' 
       AND locale = 
         (SELECT string_val FROM os_propertyset WHERE entity_name = 'default.language' 
         ) 
       AND NOT EXISTS 
         (SELECT * 
         FROM vw_cms_asset_value 
         WHERE KEY  = 'COUNTRY_NAME' 
         AND locale = p_in_languageCode 
         ) 
       UNION ALL 
       SELECT asset_code, 
         cms_value 
       FROM vw_cms_asset_value 
       WHERE KEY  = 'COUNTRY_NAME' 
       AND locale = p_in_languageCode 
       ) cntry 
     WHERE rpt.promotion_id          = p.promotion_id 
     AND p.promotion_status          = NVL (p_in_promotionStatus,p.promotion_status) 
     AND rpt.user_status             = NVL (p_in_participantStatus,rpt.user_status) 
     AND rpt.user_id                 = p_in_userId 
     AND rpt.round_number           <= NVL(p_in_roundNumber,rpt.round_number) 
     AND NVL(rpt.job_position,'JOB') = NVL (p_in_jobPosition,NVL(rpt.job_position,'JOB')) 
     AND rpt.job_position            = j.cms_code(+) 
     AND rpt.department              = dpt.cms_code(+) 
     AND rpt.user_status             = status.cms_code(+) 
     AND rpt.country_id              = c.country_id 
     AND c.cm_asset_code             = cntry.asset_code 
     AND ((p_in_departments              IS NULL) 
     OR ( rpt.department            IN 
       (SELECT * FROM TABLE(get_array_varchar(p_in_departments)) 
       ))) 
     AND ((p_in_promotionId IS NULL) 
     OR ( p.promotion_id IN 
       (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)) 
       ))) 
     AND rpt.node_id IN 
       (SELECT child_node_id 
       FROM rpt_hierarchy_rollup 
       WHERE node_id IN 
         (SELECT * FROM TABLE(get_array_varchar(p_in_parentNodeId)) 
         ) 
       ) 
     AND rpt.promotion_id = pax_rank.promotion_id(+) 
     AND rpt.round_number = pax_rank.round_number(+) 
     GROUP BY rpt.user_id, 
       last_name, 
       first_name, 
       rpt.login_id, 
       cntry.cms_value, 
       rpt.node_name, 
       dpt.cms_name, 
       j.cms_name, 
       status.cms_name, 
       rpt.promotion_name, 
       rpt.round_number, 
       pax_rank.rank, 
       rpt.round_start_date, 
       rpt.round_end_date 
       );
/* getThrowdownActivityByPaxDetailTabularResultsTotals */
OPEN p_out_totals_data FOR
 SELECT NVL(SUM(DECODE(outcome,'win',1,0)),0) Wins, 
       NVL(SUM(DECODE(outcome,'tie',1,0)),0) Ties, 
       NVL(SUM(DECODE(outcome,'loss',1,0)),0) Losses, 
       NVL(SUM(current_value),0) activity, 
       NVL(SUM(payout),0) AS payout 
     FROM rpt_throwdown_activity rpt, 
       promotion p, 
       country c 
     WHERE rpt.promotion_id          = p.promotion_id 
     AND p.promotion_status          = NVL (p_in_promotionStatus,p.promotion_status) 
     AND rpt.user_status             = NVL (p_in_participantStatus,rpt.user_status) 
     AND rpt.user_id                 = p_in_userId 
     AND rpt.round_number           <= NVL(p_in_roundNumber,rpt.round_number) 
     AND NVL(rpt.job_position,'JOB') = NVL (p_in_jobPosition,NVL(rpt.job_position,'JOB')) 
     AND rpt.country_id              = c.country_id 
     AND ((p_in_departments              IS NULL) 
     OR ( rpt.department            IN 
       (SELECT * FROM TABLE(get_array_varchar(p_in_departments)) 
       ))) 
     AND ((p_in_promotionId IS NULL) 
     OR ( p.promotion_id IN 
       (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)) 
       ))) 
     AND rpt.node_id IN 
       (SELECT child_node_id 
       FROM rpt_hierarchy_rollup 
       WHERE node_id IN 
         (SELECT * FROM TABLE(get_array_varchar(p_in_parentNodeId)) 
         ) 
       ); 
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
 END prc_getByPaxDetail;       
/* CHARTS */
/* getThrowdownTotalActivityChartResults */
 PROCEDURE prc_getTotalActivityChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_roundNumber         IN     NUMBER,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_userId              IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getTotalActivityChart' ;
      v_stage          VARCHAR2 (500);
 BEGIN
      v_stage   := 'getTotalActivityChart';
     OPEN p_out_data FOR     
 SELECT * 
     FROM 
       (SELECT last_name 
         ||' , ' 
         ||first_name AS participant_name, 
         SUM (current_value) activity 
       FROM rpt_throwdown_activity rpt, 
         promotion p 
       WHERE rpt.promotion_id          = p.promotion_id 
       AND p.promotion_status          = NVL (p_in_promotionStatus,p.promotion_status) 
       AND rpt.user_status             = NVL (p_in_participantStatus,rpt.user_status) 
       AND rpt.round_number           <= NVL(p_in_roundNumber,rpt.round_number) 
       AND NVL(rpt.job_position,'JOB') = NVL (p_in_jobPosition,NVL(rpt.job_position,'JOB')) 
       AND ((p_in_departments              IS NULL) 
       OR ( rpt.department            IN 
         (SELECT * FROM TABLE(get_array_varchar(p_in_departments)) 
         ))) 
       AND ((p_in_promotionId IS NULL) 
       OR ( p.promotion_id IN 
         (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)) 
         ))) 
       AND rpt.node_id IN 
         (SELECT child_node_id 
         FROM rpt_hierarchy_rollup 
         WHERE node_id IN 
           (SELECT * FROM TABLE(get_array_varchar(p_in_parentNodeId)) 
           ) 
         ) 
       GROUP BY user_id, 
         last_name, 
         first_name 
       ORDER BY SUM (current_value) DESC 
       ) 
     WHERE rownum <= 20 ;     
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
 END prc_getTotalActivityChart;       
/* getThrowdownActivityByRoundChartResults */
 PROCEDURE prc_getByRoundChart(
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_roundNumber         IN     NUMBER,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_userId              IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getByRoundChart' ;
      v_stage          VARCHAR2 (500);
 BEGIN
      v_stage   := 'getByRoundChart';
     OPEN p_out_data FOR     
 SELECT * 
     FROM 
       (SELECT round_number, 
         SUM (current_value) activity 
       FROM rpt_throwdown_activity rpt, 
         promotion p 
       WHERE rpt.promotion_id          = p.promotion_id 
       AND p.promotion_status          = NVL (p_in_promotionStatus,p.promotion_status) 
       AND rpt.user_status             = NVL (p_in_participantStatus,rpt.user_status) 
       AND rpt.round_number           <= NVL(p_in_roundNumber,rpt.round_number) 
       AND NVL(rpt.job_position,'JOB') = NVL (p_in_jobPosition,NVL(rpt.job_position,'JOB')) 
       AND ((p_in_departments              IS NULL) 
       OR ( rpt.department            IN 
         (SELECT * FROM TABLE(get_array_varchar(p_in_departments)) 
         ))) 
       AND ((p_in_promotionId IS NULL) 
       OR ( p.promotion_id IN 
         (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)) 
         ))) 
       AND rpt.node_id IN 
         (SELECT child_node_id 
         FROM rpt_hierarchy_rollup 
         WHERE node_id IN 
           (SELECT * FROM TABLE(get_array_varchar(p_in_parentNodeId)) 
           ) 
         ) 
       GROUP BY rpt.round_number 
       ORDER BY round_number ASC 
       ) 
     WHERE rownum <= 20 ;
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
 END prc_getByRoundChart;       

/******************************************************************************
  NAME:       prc_getByPaxDetail
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
                                        Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J             12/04/2014     Bug Fix 58491  - Points Earned in Throwdown chart shows all zero issue.                                   
  ******************************************************************************/

/* getPointsEarnedInThrowdownChartResults */
PROCEDURE prc_getPointsEarned (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_roundNumber         IN     NUMBER,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_userId              IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getPointsEarned' ;
      v_stage          VARCHAR2 (500);
 BEGIN
      v_stage   := 'getPointsEarned';
     OPEN p_out_data FOR     
 SELECT * 
     FROM 
       (SELECT last_name 
         ||' , ' 
         ||first_name AS participant_name, 
         NVL(SUM (payout),0) points    --12/04/2014
       FROM rpt_throwdown_activity rpt, 
         promotion p 
       WHERE rpt.promotion_id          = p.promotion_id 
       AND p.promotion_status          = NVL (p_in_promotionStatus,p.promotion_status) 
       AND rpt.user_status             = NVL (p_in_participantStatus,rpt.user_status) 
       AND rpt.round_number           <= NVL(p_in_roundNumber,rpt.round_number) 
       AND NVL(rpt.job_position,'JOB') = NVL (p_in_jobPosition,NVL(rpt.job_position,'JOB')) 
       AND ((p_in_departments              IS NULL) 
       OR ( rpt.department            IN 
         (SELECT * FROM TABLE(get_array_varchar(p_in_departments)) 
         ))) 
       AND ((p_in_promotionId IS NULL) 
       OR ( p.promotion_id IN 
         (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)) 
         ))) 
       AND rpt.node_id IN 
         (SELECT child_node_id 
         FROM rpt_hierarchy_rollup 
         WHERE node_id IN 
           (SELECT * FROM TABLE(get_array_varchar(p_in_parentNodeId)) 
           ) 
         ) 
       GROUP BY user_id, 
         last_name, 
         first_name 
       ORDER BY NVL(SUM (payout),0) DESC      --12/04/2014
       ) 
     WHERE rownum <= 20 ;
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
 END prc_getPointsEarned; 
 
END pkg_query_throwdown_activity;
/