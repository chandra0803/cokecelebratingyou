CREATE OR REPLACE PACKAGE pkg_query_plateau_levels AS
/******************************************************************************
   NAME:       pkg_query_plateau_levels
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        7/30/2014      keshaboi       1. Created this package.
              7/31/2014      poddutur       1. Fixed the issue in prc_getAwardLevelSummary procedure to get results.
******************************************************************************/
/* getAwardLevelActivitySummaryResults */
PROCEDURE prc_getAwardLevelSummary(
   p_in_awardLevel          IN     VARCHAR,
   p_in_awardStatus         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR);
   /* getAwardLevelActivityTeamLevelResults */
PROCEDURE prc_getAwardLevelTeamLevel(
   p_in_awardLevel          IN     VARCHAR,
   p_in_awardStatus         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR);
   /* getPlateauAwardActivityChartResults */
PROCEDURE prc_getPlateauAward(
   p_in_awardLevel          IN     VARCHAR,
   p_in_awardStatus         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR);
   /* getPercentagePlateauAwardActivityChartResults */
PROCEDURE prc_getPercentagePlateauAward(
   p_in_awardLevel          IN     VARCHAR,
   p_in_awardStatus         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR);
   
   
END pkg_query_plateau_levels;

/
CREATE OR REPLACE PACKAGE BODY pkg_query_plateau_levels
AS
/* getAwardLevelActivitySummaryResults */
PROCEDURE prc_getAwardLevelSummary(
   p_in_awardLevel          IN     VARCHAR,
   p_in_awardStatus         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
    c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getAwardLevelSummary' ;
      v_stage                   VARCHAR2 (500);
      v_sortCol                 VARCHAR2 (200);
        l_query        VARCHAR2 (32767);
    BEGIN
    
    v_stage := 'getAwardLevelSummary';
    
     l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROWNUM RN, rs.* from (SELECT 
                    node_name, 
                    codes_issued, 
                    DECODE ('''||p_in_awardStatus||''', 
                            ''expired'', 0, 
                            ''unredeemed'', 0, 
                            Codes_Redeemed) 
                       codes_redeemed, 
                    DECODE ('''||p_in_awardStatus||''', 
                            ''expired'', 0, 
                            ''unredeemed'', 0, 
                            perc_redeemed) 
                       perc_redeemed, 
                    DECODE ('''||p_in_awardStatus||''', 
                            ''expired'', 0, 
                            ''redeemed'', 0, 
                            codes_unredeemed) 
                       codes_unredeemed, 
                    DECODE ('''||p_in_awardStatus||''', 
                            ''expired'', 0, 
                            ''redeemed'', 0, 
                            perc_unredeemed) 
                       perc_unredeemed, 
                    DECODE ('''||p_in_awardStatus||''', 
                            ''redeemed'', 0, 
                            ''unredeemed'', 0, 
                            codes_expired) 
                       codes_expired, 
                    DECODE ('''||p_in_awardStatus||''', 
                            ''redeemed'', 0, 
                            ''unredeemed'', 0, 
                            perc_expired) 
                       perc_expired, 
                   node_id 
               FROM (           
                         SELECT rh.node_name, 
                            res.Codes_Issued, 
                            res.Codes_Redeemed, 
                            res.perc_redeemed, 
                            res.Codes_Unredeemed, 
                            res.perc_Unredeemed, 
                            res.Codes_Expired, 
                            res.perc_expired, 
                            rh.node_id 
                       FROM rpt_hierarchy rh, 
                            (                          
                            SELECT h.node_id, 
                                      NVL (SUM (dtl.codes_issued), 0) Codes_Issued, 
                                      NVL (SUM (dtl.codes_redeemed), 0) 
                                         Codes_Redeemed, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_redeemed), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         perc_redeemed, 
                                      NVL (SUM (dtl.codes_unredeemed), 0) 
                                         Codes_Unredeemed, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_unredeemed), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         perc_Unredeemed, 
                                      NVL (SUM (dtl.codes_expired), 0) Codes_Expired, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_expired), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         AS perc_expired 
                                 FROM (SELECT child_node_id node_id, 
                                              node_id path_node_id 
                                         FROM rpt_hierarchy_rollup) npn, 
                                      rpt_hierarchy h, 
                                      (  SELECT rh.node_id, 
                                                rh.node_name primary_org_unit, 
                                                SUM (tot_num_issued) Codes_Issued, 
                                                SUM (tot_num_redeemed) Codes_Redeemed, 
                                                SUM (tot_num_unredeemed) 
                                                   Codes_Unredeemed, 
                                                SUM (tot_num_expired) Codes_Expired 
                                           FROM rpt_award_item_activity rad, 
                                                rpt_hierarchy rh 
                                          WHERE rh.node_id = rad.node_id 
                                                AND ( ('''||p_in_promotionId||''' IS NULL) 
                                                     OR ('''||p_in_promotionId||''' IS NOT NULL 
                                                         AND rad.promotion_id IN 
                                                                (SELECT * 
                                                                   FROM TABLE ( 
                                                                           get_array_varchar ( 
                                                                              '''||p_in_promotionId||'''))))) 
                                                AND NVL (rad.award_date, 
                                                         TRUNC (SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') 
                                                                              AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
                                                AND NVL (rad.om_level_name, ''X'') = 
                                                       NVL ( 
                                                          '''||p_in_awardLevel||''', 
                                                          NVL (rad.om_level_name, ''X'')) 
                                       GROUP BY rh.node_id, rh.node_name) dtl 
                                WHERE dtl.node_id(+) = npn.node_id 
                                      AND npn.path_node_id = h.node_id 
                             GROUP BY h.node_id, h.node_name                         
                             ) res 
                      WHERE res.node_id = rh.node_id 
                            AND NVL (rh.parent_node_id, 0) IN 
                                   (SELECT * 
                                      FROM TABLE ( 
                                              get_array_varchar ( 
                                                 NVL ('''||p_in_parentNodeId||''', 0)))) 
                     UNION                                   
                       SELECT rh.node_name || '' Team'' node_name, 
                              NVL (Codes_Issued, 0) Codes_Issued, 
                              NVL(Codes_Redeemed,0) Codes_Redeemed, 
                               NVL(perc_redeemed,0) perc_redeemed, 
                               NVL(Codes_Unredeemed,0) Codes_Unredeemed, 
                               NVL(perc_Unredeemed,0) perc_Unredeemed, 
                               NVL(Codes_Expired,0) Codes_Expired, 
                               NVL(perc_expired,0) perc_expired, 
                              rh.node_id 
                         FROM rpt_hierarchy rh, 
                         (                     
                         Select NVL (SUM (tot_num_issued), 0) Codes_Issued, 
                         rad.node_id, 
                              NVL (SUM (tot_num_redeemed), 0) Codes_Redeemed, 
                              DECODE ( 
                                 NVL (SUM (tot_num_issued), 0), 
                                 0, 0, 
                                 ROUND ( 
                                    (NVL (SUM (tot_num_redeemed), 0) 
                                     / SUM (tot_num_issued)) 
                                    * 100, 
                                    2)) 
                                 perc_redeemed, 
                              NVL (SUM (tot_num_unredeemed), 0) Codes_Unredeemed, 
                              DECODE ( 
                                 NVL (SUM (tot_num_issued), 0), 
                                 0, 0, 
                                 ROUND ( 
                                    (NVL (SUM (tot_num_unredeemed), 0) 
                                     / SUM (tot_num_issued)) 
                                    * 100, 
                                    2)) 
                                 perc_Unredeemed, 
                              NVL (SUM (tot_num_expired), 0) Codes_Expired, 
                              DECODE ( 
                                 NVL (SUM (tot_num_issued), 0), 
                                 0, 0, 
                                 ROUND ( 
                                    (NVL (SUM (tot_num_expired), 0) 
                                     / SUM (tot_num_issued)) 
                                    * 100, 
                                    2)) 
                                 perc_expired FROM rpt_award_item_activity rad  
                        WHERE                   
                               ( ('''||p_in_promotionId||''' IS NULL) OR ('''||p_in_promotionId||''' IS NOT NULL AND rad.promotion_id IN (SELECT * FROM TABLE (get_array_varchar ('''||p_in_promotionId||'''))))) 
                              AND NVL (rad.award_date, TRUNC (SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') 
                                                                            AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
                               AND NVL(rad.node_id,0) IN (SELECT * FROM TABLE(get_array_varchar( nvl('''||p_in_parentNodeId||''',0)  ) )) 
                              AND NVL (rad.om_level_name, ''X'') = 
                                     NVL ('''||p_in_awardLevel||''', NVL (rad.om_level_name, ''X'')) 
                     GROUP BY rad.node_id                 
               ) dtl  
             WHERE rh.node_id      = dtl.node_id (+)         
             AND NVL(rh.node_id,0) IN (SELECT * FROM TABLE(get_array_varchar( nvl('''||p_in_parentNodeId||''',0)  ) )))  rs                
         ORDER BY '|| v_sortCol ||'    
      )rs
   ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
      OPEN p_out_data FOR l_query;
  
  /* getAwardLevelActivitySummaryResultsSize */
 SELECT COUNT (1) INTO p_out_size_data
               FROM (           
                         SELECT rh.node_name, 
                            res.Codes_Issued, 
                            res.Codes_Redeemed, 
                            res.perc_redeemed, 
                            res.Codes_Unredeemed, 
                            res.perc_Unredeemed, 
                            res.Codes_Expired, 
                            res.perc_expired, 
                            rh.node_id 
                       FROM rpt_hierarchy rh, 
                            (                          
                            SELECT h.node_id, 
                                      NVL (SUM (dtl.codes_issued), 0) Codes_Issued, 
                                      NVL (SUM (dtl.codes_redeemed), 0) 
                                         Codes_Redeemed, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_redeemed), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         perc_redeemed, 
                                      NVL (SUM (dtl.codes_unredeemed), 0) 
                                         Codes_Unredeemed, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_unredeemed), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         perc_Unredeemed, 
                                      NVL (SUM (dtl.codes_expired), 0) Codes_Expired, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_expired), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         AS perc_expired 
                                 FROM (SELECT child_node_id node_id, 
                                              node_id path_node_id 
                                         FROM rpt_hierarchy_rollup) npn, 
                                      rpt_hierarchy h, 
                                      (  SELECT rh.node_id, 
                                                rh.node_name primary_org_unit, 
                                                SUM (tot_num_issued) Codes_Issued, 
                                                SUM (tot_num_redeemed) Codes_Redeemed, 
                                                SUM (tot_num_unredeemed) 
                                                   Codes_Unredeemed, 
                                                SUM (tot_num_expired) Codes_Expired 
                                           FROM rpt_award_item_activity rad, 
                                                rpt_hierarchy rh 
                                          WHERE rh.node_id = rad.node_id 
                                                AND ( (p_in_promotionId IS NULL) 
                                                     OR (p_in_promotionId IS NOT NULL 
                                                         AND rad.promotion_id IN 
                                                                (SELECT * 
                                                                   FROM TABLE ( 
                                                                           get_array_varchar ( 
                                                                              p_in_promotionId))))) 
                                                AND NVL (rad.award_date, 
                                                         TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) 
                                                                              AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
                                                AND NVL (rad.om_level_name, 'X') = 
                                                       NVL ( 
                                                          p_in_awardLevel, 
                                                          NVL (rad.om_level_name, 'X')) 
                                       GROUP BY rh.node_id, rh.node_name) dtl 
                                WHERE dtl.node_id(+) = npn.node_id 
                                      AND npn.path_node_id = h.node_id 
                             GROUP BY h.node_id, h.node_name                         
                             ) res 
                      WHERE res.node_id = rh.node_id 
                            AND NVL (rh.parent_node_id, 0) IN 
                                   (SELECT * 
                                      FROM TABLE ( 
                                             get_array_varchar ( 
                                                 NVL (p_in_parentNodeId, 0)))) 
                     UNION                                   
                       SELECT rh.node_name || ' Team' node_name, 
                              NVL (Codes_Issued, 0) Codes_Issued, 
                              NVL(Codes_Redeemed,0) Codes_Redeemed, 
                               NVL(perc_redeemed,0) perc_redeemed, 
                               NVL(Codes_Unredeemed,0) Codes_Unredeemed, 
                               NVL(perc_Unredeemed,0) perc_Unredeemed, 
                               NVL(Codes_Expired,0) Codes_Expired, 
                               NVL(perc_expired,0) perc_expired, 
                              rh.node_id 
                         FROM rpt_hierarchy rh, 
                         (                     
                         Select NVL (SUM (tot_num_issued), 0) Codes_Issued, 
                         rad.node_id, 
                              NVL (SUM (tot_num_redeemed), 0) Codes_Redeemed, 
                              DECODE ( 
                                 NVL (SUM (tot_num_issued), 0), 
                                 0, 0, 
                                 ROUND ( 
                                    (NVL (SUM (tot_num_redeemed), 0) 
                                     / SUM (tot_num_issued)) 
                                    * 100, 
                                    2)) 
                                 perc_redeemed, 
                              NVL (SUM (tot_num_unredeemed), 0) Codes_Unredeemed, 
                              DECODE ( 
                                 NVL (SUM (tot_num_issued), 0), 
                                 0, 0, 
                                 ROUND ( 
                                    (NVL (SUM (tot_num_unredeemed), 0) 
                                     / SUM (tot_num_issued)) 
                                    * 100, 
                                    2)) 
                                 perc_Unredeemed, 
                              NVL (SUM (tot_num_expired), 0) Codes_Expired, 
                              DECODE ( 
                                 NVL (SUM (tot_num_issued), 0), 
                                 0, 0, 
                                 ROUND ( 
                                    (NVL (SUM (tot_num_expired), 0) 
                                     / SUM (tot_num_issued)) 
                                    * 100, 
                                    2)) 
                                 perc_expired FROM rpt_award_item_activity rad  
                        WHERE                   
                               ( (p_in_promotionId IS NULL) OR (p_in_promotionId IS NOT NULL AND rad.promotion_id IN (SELECT * FROM TABLE (get_array_varchar (p_in_promotionId))))) 
                              AND NVL (rad.award_date, TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) 
                                                                            AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
                               AND NVL(rad.node_id,0) IN (SELECT * FROM TABLE(get_array_varchar( nvl(p_in_parentNodeId,0)  ) )) 
                              AND NVL (rad.om_level_name, 'X') = 
                                     NVL (p_in_awardLevel, NVL (rad.om_level_name, 'X')) 
                     GROUP BY rad.node_id                 
               ) dtl  
             WHERE rh.node_id      = dtl.node_id (+)         
             AND NVL(rh.node_id,0) IN (SELECT * FROM TABLE(get_array_varchar( nvl(p_in_parentNodeId,0)  ) ))                  
                     ) ;
/* getAwardLevelActivitySummaryResultsTotals */
 OPEN p_out_totals_data FOR
 SELECT SUM (codes_issued) codes_issued, 
            SUM (codes_redeemed) codes_redeemed, 
            DECODE ( 
               NVL (SUM (codes_issued), 0), 
               0, 0, 
               ROUND ( (NVL (SUM (codes_redeemed), 0) / SUM (codes_issued)) * 100, 
                      2)) 
               perc_redeemed, 
            SUM (codes_unredeemed) codes_unredeemed, 
            DECODE ( 
               NVL (SUM (codes_issued), 0), 
               0, 0, 
               ROUND ( 
                  (NVL (SUM (codes_unredeemed), 0) / SUM (codes_issued)) * 100, 
                  2)) 
               perc_unredeemed, 
            SUM (codes_expired) codes_expired, 
            DECODE ( 
               NVL (SUM (codes_issued), 0), 
               0, 0, 
               ROUND ( (NVL (SUM (codes_expired), 0) / SUM (codes_issued)) * 100, 
                      2)) 
               perc_expired 
       FROM (SELECT ROWNUM RN, 
                    node_name, 
                    codes_issued, 
                    DECODE (p_in_awardStatus, 
                            'expired', 0, 
                            'unredeemed', 0, 
                            Codes_Redeemed) 
                       codes_redeemed, 
                    DECODE (p_in_awardStatus, 
                            'expired', 0, 
                            'unredeemed', 0, 
                            perc_redeemed) 
                       perc_redeemed, 
                    DECODE (p_in_awardStatus, 
                            'expired', 0, 
                            'redeemed', 0, 
                            codes_unredeemed) 
                       codes_unredeemed, 
                    DECODE (p_in_awardStatus, 
                            'expired', 0, 
                            'redeemed', 0, 
                            perc_unredeemed) 
                       perc_unredeemed, 
                    DECODE (p_in_awardStatus, 
                            'redeemed', 0, 
                            'unredeemed', 0, 
                            codes_expired) 
                       codes_expired, 
                    DECODE (p_in_awardStatus, 
                            'redeemed', 0, 
                            'unredeemed', 0, 
                            perc_expired) 
                       perc_expired, 
                   node_id 
               FROM (           
                         SELECT rh.node_name, 
                            res.Codes_Issued, 
                            res.Codes_Redeemed, 
                            res.perc_redeemed, 
                            res.Codes_Unredeemed, 
                            res.perc_Unredeemed, 
                            res.Codes_Expired, 
                            res.perc_expired, 
                            rh.node_id 
                       FROM rpt_hierarchy rh, 
                            (                          
                            SELECT h.node_id, 
                                      NVL (SUM (dtl.codes_issued), 0) Codes_Issued, 
                                      NVL (SUM (dtl.codes_redeemed), 0) 
                                         Codes_Redeemed, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_redeemed), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         perc_redeemed, 
                                      NVL (SUM (dtl.codes_unredeemed), 0) 
                                         Codes_Unredeemed, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_unredeemed), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         perc_Unredeemed, 
                                      NVL (SUM (dtl.codes_expired), 0) Codes_Expired, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_expired), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         AS perc_expired 
                                 FROM (SELECT child_node_id node_id, 
                                              node_id path_node_id 
                                         FROM rpt_hierarchy_rollup) npn, 
                                      rpt_hierarchy h, 
                                      (  SELECT rh.node_id, 
                                                rh.node_name primary_org_unit, 
                                                SUM (tot_num_issued) Codes_Issued, 
                                                SUM (tot_num_redeemed) Codes_Redeemed, 
                                                SUM (tot_num_unredeemed) 
                                                   Codes_Unredeemed, 
                                                SUM (tot_num_expired) Codes_Expired 
                                           FROM rpt_award_item_activity rad, 
                                                rpt_hierarchy rh 
                                          WHERE rh.node_id = rad.node_id 
                                                AND ( (p_in_promotionId IS NULL) 
                                                     OR (p_in_promotionId IS NOT NULL 
                                                         AND rad.promotion_id IN 
                                                                (SELECT * 
                                                                   FROM TABLE ( 
                                                                           get_array_varchar ( 
                                                                              p_in_promotionId))))) 
                                                AND NVL (rad.award_date, 
                                                         TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) 
                                                                              AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
                                                AND NVL (rad.om_level_name, 'X') = 
                                                       NVL ( 
                                                          p_in_awardLevel, 
                                                          NVL (rad.om_level_name, 'X')) 
                                       GROUP BY rh.node_id, rh.node_name) dtl 
                                WHERE dtl.node_id(+) = npn.node_id 
                                      AND npn.path_node_id = h.node_id 
                             GROUP BY h.node_id, h.node_name                         
                             ) res 
                      WHERE res.node_id = rh.node_id 
                            AND NVL (rh.parent_node_id, 0) IN 
                                   (SELECT * 
                                      FROM TABLE ( 
                                              get_array_varchar ( 
                                                 NVL (p_in_parentNodeId, 0)))) 
                     UNION                                   
                       SELECT rh.node_name || ' Team' node_name, 
                              NVL (Codes_Issued, 0) Codes_Issued, 
                              NVL(Codes_Redeemed,0) Codes_Redeemed, 
                               NVL(perc_redeemed,0) perc_redeemed, 
                               NVL(Codes_Unredeemed,0) Codes_Unredeemed, 
                               NVL(perc_Unredeemed,0) perc_Unredeemed, 
                               NVL(Codes_Expired,0) Codes_Expired, 
                               NVL(perc_expired,0) perc_expired, 
                              rh.node_id 
                         FROM rpt_hierarchy rh, 
                         (                     
                         Select NVL (SUM (tot_num_issued), 0) Codes_Issued, 
                         rad.node_id, 
                              NVL (SUM (tot_num_redeemed), 0) Codes_Redeemed, 
                              DECODE ( 
                                 NVL (SUM (tot_num_issued), 0), 
                                 0, 0, 
                                 ROUND ( 
                                    (NVL (SUM (tot_num_redeemed), 0) 
                                     / SUM (tot_num_issued)) 
                                    * 100, 
                                    2)) 
                                 perc_redeemed, 
                              NVL (SUM (tot_num_unredeemed), 0) Codes_Unredeemed, 
                              DECODE ( 
                                 NVL (SUM (tot_num_issued), 0), 
                                 0, 0, 
                                 ROUND ( 
                                    (NVL (SUM (tot_num_unredeemed), 0) 
                                     / SUM (tot_num_issued)) 
                                    * 100, 
                                    2)) 
                                 perc_Unredeemed, 
                              NVL (SUM (tot_num_expired), 0) Codes_Expired, 
                              DECODE ( 
                                 NVL (SUM (tot_num_issued), 0), 
                                 0, 0, 
                                 ROUND ( 
                                    (NVL (SUM (tot_num_expired), 0) 
                                     / SUM (tot_num_issued)) 
                                    * 100, 
                                    2)) 
                                 perc_expired FROM rpt_award_item_activity rad  
                        WHERE                   
                               ( (p_in_promotionId IS NULL) OR (p_in_promotionId IS NOT NULL AND rad.promotion_id IN (SELECT * FROM TABLE (get_array_varchar (p_in_promotionId))))) 
                              AND NVL (rad.award_date, TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) 
                                                                            AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
                               AND NVL(rad.node_id,0) IN (SELECT * FROM TABLE(get_array_varchar( nvl(p_in_parentNodeId,0)  ) )) 
                              AND NVL (rad.om_level_name, 'X') = 
                                     NVL (p_in_awardLevel, NVL (rad.om_level_name, 'X')) 
                     GROUP BY rad.node_id                 
               ) dtl  
             WHERE rh.node_id      = dtl.node_id (+)         
             AND NVL(rh.node_id,0) IN (SELECT * FROM TABLE(get_array_varchar( nvl(p_in_parentNodeId,0)  ) ))                  
                     )) ;
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
 END prc_getAwardLevelSummary;
/* getAwardLevelActivityTeamLevelResults */
PROCEDURE prc_getAwardLevelTeamLevel(
   p_in_awardLevel          IN     VARCHAR,
   p_in_awardStatus         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
   /******************************************************************************
   NAME:       prc_getAwardLevelTeamLevel
   PURPOSE:

    Person             Date           Comments
  -----------     ----------   ------------------------------------------------
   1.0                  ??            ??               ??
  Sherif Basha     11/04/2016   Bug 69925 - Parse error is getting displayed when drill down the report for USA team
                                --error not a single group due to missing group by
******************************************************************************/
    c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getAwardLevelTeamLevel' ;
      v_stage                   VARCHAR2 (500);
      v_sortCol                 VARCHAR2 (200);
      l_query        VARCHAR2 (32767);
   BEGIN
      v_stage := 'getAwardLevelTeamLevel';    
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROWNUM RN,gs.* FROM (SELECT om_level_name level_name, 
                                      NVL (SUM (dtl.codes_issued), 0) Codes_Issued, 
                                      NVL (SUM (dtl.codes_redeemed), 0) 
                                         Codes_Redeemed, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_redeemed), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         perc_redeemed, 
                                      NVL (SUM (dtl.codes_unredeemed), 0) 
                                         Codes_Unredeemed, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_unredeemed), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         perc_Unredeemed, 
                                      NVL (SUM (dtl.codes_expired), 0) Codes_Expired, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_expired), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         AS perc_expired 
                                 FROM (                               
                                  SELECT om_level_name, 
                                                SUM (tot_num_issued) Codes_Issued, 
                                                SUM (tot_num_redeemed) Codes_Redeemed, 
                                                SUM (tot_num_unredeemed) 
                                                   Codes_Unredeemed, 
                                                SUM (tot_num_expired) Codes_Expired 
                                           FROM rpt_award_item_activity rad, 
                                                rpt_hierarchy rh 
                                          WHERE rh.node_id = rad.node_id 
                                                 AND rh.node_id = '''||p_in_parentNodeId||''' 
                                                AND ( ('''||p_in_promotionId||''' IS NULL) 
                                                     OR ('''||p_in_promotionId||''' IS NOT NULL 
                                                         AND rad.promotion_id IN 
                                                                (SELECT * 
                                                                   FROM TABLE ( 
                                                                           get_array_varchar ( 
                                                                              '''||p_in_promotionId||'''))))) 
                                                AND NVL (rad.award_date, 
                                                         TRUNC (SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') 
                                                                              AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
                                                AND NVL (rad.om_level_name, ''X'') = 
                                                       NVL ( 
                                                          '''||p_in_awardLevel||''', 
                                                          NVL (rad.om_level_name, ''X'')) 
                                       GROUP BY om_level_name)dtl 
                                       group by om_level_name) gs 
         ORDER BY 
        '|| v_sortCol ||'     
   ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
      OPEN p_out_data FOR l_query;
/* getAwardLevelActivityTeamLevelResultsSize */
SELECT COUNT (1) INTO p_out_size_data FROM (SELECT   om_level_name,       NVL (SUM (dtl.codes_issued), 0) Codes_Issued, 
                                      NVL (SUM (dtl.codes_redeemed), 0) 
                                         Codes_Redeemed, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_redeemed), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         perc_redeemed, 
                                      NVL (SUM (dtl.codes_unredeemed), 0) 
                                         Codes_Unredeemed, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_unredeemed), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         perc_Unredeemed, 
                                      NVL (SUM (dtl.codes_expired), 0) Codes_Expired, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_expired), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         AS perc_expired 
                                 FROM (                               
                                  SELECT om_level_name, 
                                                SUM (tot_num_issued) Codes_Issued, 
                                                SUM (tot_num_redeemed) Codes_Redeemed, 
                                                SUM (tot_num_unredeemed) 
                                                   Codes_Unredeemed, 
                                                SUM (tot_num_expired) Codes_Expired 
                                           FROM rpt_award_item_activity rad, 
                                                rpt_hierarchy rh 
                                          WHERE rh.node_id = rad.node_id 
                                                 AND rh.node_id = p_in_parentNodeId 
                                                AND ( (p_in_promotionId IS NULL) 
                                                     OR (p_in_promotionId IS NOT NULL 
                                                         AND rad.promotion_id IN 
                                                                (SELECT * 
                                                                   FROM TABLE ( 
                                                                           get_array_varchar ( 
                                                                              p_in_promotionId))))) 
                                                AND NVL (rad.award_date, 
                                                         TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) 
                                                                              AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
                                                AND NVL (rad.om_level_name, 'X') = 
                                                       NVL ( 
                                                          p_in_awardLevel, 
                                                          NVL (rad.om_level_name, 'X')) 
                                       GROUP BY om_level_name)dtl 
                                       group by om_level_name);
                                       
                                       
/* getAwardLevelActivityTeamLevelResultsTotals */
 OPEN p_out_totals_data FOR
 SELECT  NVL (SUM (dtl.codes_issued), 0) Codes_Issued, 
                                      NVL (SUM (dtl.codes_redeemed), 0) 
                                         Codes_Redeemed, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_redeemed), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         perc_redeemed, 
                                      NVL (SUM (dtl.codes_unredeemed), 0) 
                                         Codes_Unredeemed, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_unredeemed), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         perc_Unredeemed, 
                                      NVL (SUM (dtl.codes_expired), 0) Codes_Expired, 
                                      DECODE ( 
                                         NVL (SUM (dtl.codes_issued), 0), 
                                         0, 0, 
                                         ROUND ( 
                                            (NVL (SUM (dtl.codes_expired), 0) 
                                             / SUM (codes_issued)) 
                                            * 100, 
                                            2)) 
                                         AS perc_expired 
                                 FROM (                               
                                  SELECT om_level_name, 
                                                SUM (tot_num_issued) Codes_Issued, 
                                                SUM (tot_num_redeemed) Codes_Redeemed, 
                                                SUM (tot_num_unredeemed) 
                                                   Codes_Unredeemed, 
                                                SUM (tot_num_expired) Codes_Expired 
                                           FROM rpt_award_item_activity rad, 
                                                rpt_hierarchy rh 
                                          WHERE rh.node_id = rad.node_id 
                                                 AND rh.node_id = p_in_parentNodeId 
                                                AND ( (p_in_promotionId IS NULL) 
                                                     OR (p_in_promotionId IS NOT NULL 
                                                         AND rad.promotion_id IN 
                                                                (SELECT * 
                                                                   FROM TABLE ( 
                                                                           get_array_varchar ( 
                                                                              p_in_promotionId))))) 
                                                AND NVL (rad.award_date, 
                                                         TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) 
                                                                              AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
                                                AND NVL (rad.om_level_name, 'X') = 
                                                       NVL ( 
                                                          p_in_awardLevel, 
                                                          NVL (rad.om_level_name, 'X')) 
                                       GROUP BY om_level_name)dtl ;      --   11/04/2016   Bug 69925 Added the missing group
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
 END prc_getAwardLevelTeamLevel;
 /* CHARTS */
/* getPlateauAwardActivityChartResults */
PROCEDURE prc_getPlateauAward(
   p_in_awardLevel          IN     VARCHAR,
   p_in_awardStatus         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR)
   IS
    c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getPlateauAward' ;
      v_stage                   VARCHAR2 (500);
   BEGIN
      v_stage := 'getPlateauAward';
      OPEN p_out_data FOR
     select * from ( 
     SELECT level_name, 
             DECODE(p_in_awardStatus,'expired',0,'unredeemed',0,Codes_Redeemed) codes_redeemed, 
             DECODE(p_in_awardStatus,'expired',0,'redeemed',0,codes_unredeemed) Codes_Unredeemed, 
             DECODE(p_in_awardStatus,'redeemed',0,'unredeemed',0,codes_expired) Codes_Expired 
        FROM 
       (SELECT OM_LEVEL_NAME level_name, 
            NVL(SUM(tot_num_redeemed),0) Codes_Redeemed,     
            NVL(SUM(tot_num_unredeemed),0) Codes_Unredeemed,    
            NVL(SUM(tot_num_expired),0) Codes_Expired 
       FROM rpt_award_item_activity   
      WHERE NVL(om_level_name,'X') = NVL(p_in_awardLevel,NVL(om_level_name,'X')) 
        AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) 
        AND NVL(award_date, TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern)          
                                                   AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       AND ( ( p_in_nodeAndBelow = 1
       AND ( node_id  IN
      (SELECT child_node_id
      FROM rpt_hierarchy_rollup
      WHERE node_id IN
        (SELECT * FROM TABLE(get_array_varchar( NVL(p_in_parentNodeId,0) ) )
        )
      )) )
       OR ( p_in_nodeAndBelow = 0)
      AND node_id IN
      (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )
      ) )      
      GROUP BY om_level_name )
     order by codes_redeemed desc, Codes_Unredeemed desc , Codes_Expired desc )
     where rownum <= 20;
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
 END prc_getPlateauAward;
/* getPercentagePlateauAwardActivityChartResults */
PROCEDURE prc_getPercentagePlateauAward(
   p_in_awardLevel          IN     VARCHAR,
   p_in_awardStatus         IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_nodeAndBelow        IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_in_rowNumStart         IN     NUMBER,
   p_in_rowNumEnd           IN     NUMBER,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR)
   IS
    c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getPercentagePlateauAward' ;
      v_stage                   VARCHAR2 (500);
   BEGIN
      v_stage := 'getPercentagePlateauAward';
      OPEN p_out_data FOR
     SELECT * FROM ( 
     SELECT level_name, 
             DECODE(p_in_awardStatus,'expired',0,'unredeemed',0,perc_redeemed)  perc_redeemed, 
             DECODE(p_in_awardStatus,'expired',0,'redeemed',0,perc_unredeemed)  perc_Unredeemed,     
             DECODE(p_in_awardStatus,'redeemed',0,'unredeemed',0,perc_expired)  perc_expired 
      FROM 
       (SELECT OM_LEVEL_NAME level_name, 
            DECODE(NVL(SUM(tot_num_issued),0) ,0,0,ROUND(( NVL(SUM(tot_num_redeemed),0)/SUM(tot_num_issued))*100,2)) perc_redeemed, 
            DECODE(NVL(SUM(tot_num_issued),0) ,0,0,ROUND(( NVL(SUM(tot_num_unredeemed),0)/SUM(tot_num_issued))*100,2)) perc_Unredeemed, 
            DECODE(NVL(SUM(tot_num_issued),0) ,0,0,ROUND((NVL(SUM(tot_num_expired),0)/SUM(tot_num_issued))*100,2))  perc_expired      
       FROM rpt_award_item_activity   
      WHERE NVL(om_level_name,'X') = NVL(p_in_awardLevel,NVL(om_level_name,'X')) 
        AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) 
        AND NVL(award_date, TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern)          
                                                   AND TO_DATE(p_in_toDate,p_in_localeDatePattern)                                    
       AND ( ( p_in_nodeAndBelow = 1
       AND ( node_id  IN
      (SELECT child_node_id
      FROM rpt_hierarchy_rollup
      WHERE node_id IN
        (SELECT * FROM TABLE(get_array_varchar( NVL(p_in_parentNodeId,0) ) )
        )
      )) )
      OR ( p_in_nodeAndBelow = 0
      AND node_id IN
      (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )
      ) ) )
      GROUP BY om_level_name) 
      ORDER BY perc_redeemed desc, perc_unredeemed desc, perc_expired desc ) 
     WHERE rownum <= 20; 
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
 END prc_getPercentagePlateauAward;
END pkg_query_plateau_levels;
/
