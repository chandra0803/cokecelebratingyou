CREATE OR REPLACE PACKAGE pkg_query_plateau_item_select AS
/******************************************************************************
   NAME:       pkg_query_plateau_item_select
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        7/30/2014      keshaboi       1. Created this package.
              7/31/2014      poddutur       1.Added Aliases to the procedures
******************************************************************************/
/* getItemSelectionSummaryResults */
  PROCEDURE prc_getItemSelectionSummary(
   p_in_awardLevel          IN     VARCHAR,
   p_in_countryIds          IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR);
   /* getPlateauAwardSelectionChartResults */
PROCEDURE prc_getPlateauAwardSelection(
   p_in_awardLevel          IN     VARCHAR,
   p_in_countryIds          IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR);
   /* getTopRedeemedAwardsChartResults */
PROCEDURE prc_getTopRedeemedAwards(
   p_in_awardLevel          IN     VARCHAR,
   p_in_countryIds          IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_promotionId         IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR);
   

END pkg_query_plateau_item_select;
/
CREATE OR REPLACE PACKAGE BODY pkg_query_plateau_item_select
AS
/******************************************************************************
   NAME:       pkg_query_plateau_item_select
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        7/30/2014      keshaboi      1. Created this package.
              7/31/2014      poddutur      1.Added Aliases to the procedures
              06/10/2015    Suresh J      Bug 62484 GoalQuest with Plateau Report Issue 
******************************************************************************/

/* getItemSelectionSummaryResults */
PROCEDURE prc_getItemSelectionSummary(
   p_in_awardLevel          IN     VARCHAR,
   p_in_countryIds          IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR,
   p_out_size_data          OUT    NUMBER,
   p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
    c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getItemSelectionSummary' ;
      v_stage                   VARCHAR2 (500);
      v_sortCol                 VARCHAR2 (200);
      l_query        VARCHAR2 (32767);
   BEGIN
      v_stage := 'getItemSelectionSummary';
      
          
     l_query := ' ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || '
 SELECT r.om_level_name as level_name, 
            r.item_name as item_name, 
            r.inv_item_id as inv_item_id, 
            SUM(r.item_count) as selection_cnt
       FROM rpt_award_item_selection r 
      WHERE (('''||p_in_promotionId||''' IS NULL) OR ('''||p_in_promotionId||''' is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_promotionId||''')))))            
        AND NVL(r.award_date, TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') 
                                                                            AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
        AND (('''||p_in_awardLevel||''' IS NULL) OR ('''||p_in_awardLevel||''' is NOT NULL AND r.om_level_name IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_awardLevel||'''))))) 
        AND (('''||p_in_parentNodeId||''' IS NULL) OR ('''||p_in_parentNodeId||''' is NOT NULL AND r.node_id IN (SELECT child_node_id FROM rpt_hierarchy_rollup WHERE node_id IN(SELECT * FROM TABLE(get_array_varchar('''||p_in_parentNodeId||''')))))) 
        AND NVL(r.country_id,0) = NVL('''||p_in_countryIds||''',NVL(r.country_id,0))   --06/10/2015
      GROUP BY r.om_level_name, 
               r.item_name, 
               r.inv_item_id 
      ORDER BY '|| v_sortCol;

      OPEN p_out_data FOR l_query;
/* getItemSelectionSummaryResultsSize */
SELECT COUNT (1) INTO p_out_size_data
     FROM ( 
     SELECT r.om_level_name, 
            r.item_name, 
            r.inv_item_id 
       FROM rpt_award_item_selection r 
      WHERE ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))            
        AND NVL(r.award_date, TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern)          
                                                    AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
        AND ((p_in_awardLevel IS NULL) OR (p_in_awardLevel is NOT NULL AND r.om_level_name IN (SELECT * FROM TABLE(get_array_varchar(p_in_awardLevel))))) 
        AND ((p_in_parentNodeId IS NULL) OR (p_in_parentNodeId is NOT NULL AND r.node_id IN (SELECT child_node_id FROM rpt_hierarchy_rollup WHERE node_id IN(SELECT * FROM TABLE(get_array_varchar(p_in_parentNodeId)))))) 
        AND NVL(r.country_id,0) = NVL(p_in_countryIds,NVL(r.country_id,0))  --06/10/2015
      GROUP BY r.om_level_name, 
               r.item_name, 
               r.inv_item_id );
/* getItemSelectionSummaryResultsTotals */
 OPEN p_out_totals_data FOR
 SELECT SUM(r.item_count) as selection_cnt
       FROM rpt_award_item_selection r 
      WHERE ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))            
        AND NVL(r.award_date, TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern)          
                                                    AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
        AND ((p_in_awardLevel IS NULL) OR (p_in_awardLevel is NOT NULL AND r.om_level_name IN (SELECT * FROM TABLE(get_array_varchar(p_in_awardLevel))))) 
        AND ((p_in_parentNodeId IS NULL) OR (p_in_parentNodeId is NOT NULL AND r.node_id IN (SELECT child_node_id FROM rpt_hierarchy_rollup WHERE node_id IN(SELECT * FROM TABLE(get_array_varchar(p_in_parentNodeId)))))) 
        AND NVL(r.country_id,0) = NVL(p_in_countryIds,NVL(r.country_id,0)) ;  --06/10/2015
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
 END prc_getItemSelectionSummary;
/* CHARTS */
/* getPlateauAwardSelectionChartResults */
PROCEDURE prc_getPlateauAwardSelection(
   p_in_awardLevel          IN     VARCHAR,
   p_in_countryIds          IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR)
   IS
    c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getPlateauAwardSelection' ;
      v_stage                   VARCHAR2 (500);
      v_sortCol                 VARCHAR2 (200);
   BEGIN
      v_stage := 'getItemSelectionSummary';
      v_sortCol := p_in_sortColName || ' ' || NVL (p_in_sortedBy, ' ');
      OPEN p_out_data FOR
 SELECT r.item_name as item_name, 
            round(100*ratio_to_report(sum(item_count)) over (), 2) as item_percentage 
       FROM rpt_award_item_selection r 
      WHERE ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))            
        AND NVL(r.award_date, TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern)          
                                                    AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
        AND ((p_in_awardLevel IS NULL) OR (p_in_awardLevel is NOT NULL AND r.om_level_name IN (SELECT * FROM TABLE(get_array_varchar(p_in_awardLevel))))) 
        AND ((p_in_parentNodeId IS NULL) OR (p_in_parentNodeId is NOT NULL AND r.node_id IN (SELECT child_node_id FROM rpt_hierarchy_rollup WHERE node_id IN(SELECT * FROM TABLE(get_array_varchar(p_in_parentNodeId)))))) 
        AND NVL(r.country_id,0) = NVL(p_in_countryIds,NVL(r.country_id,0))   --06/10/2015
       GROUP BY r.item_name ;
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
 END prc_getPlateauAwardSelection;
/* getTopRedeemedAwardsChartResults */
PROCEDURE prc_getTopRedeemedAwards(
   p_in_awardLevel          IN     VARCHAR,
   p_in_countryIds          IN     VARCHAR,
   p_in_fromDate            IN     VARCHAR,
   p_in_toDate              IN     VARCHAR,
   p_in_localeDatePattern   IN     VARCHAR,
   p_in_parentNodeId        IN     VARCHAR,
   p_in_promotionId        IN     VARCHAR,
   p_in_sortColName         IN     VARCHAR,
   p_in_sortedBy            IN     VARCHAR,
   p_out_return_code        OUT    NUMBER,
   p_out_data               OUT    SYS_REFCURSOR)
   IS
    c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getTopRedeemedAwards' ;
      v_stage                   VARCHAR2 (500);
   BEGIN
      v_stage := 'getTopRedeemedAwards';
      OPEN p_out_data FOR
 SELECT * From  
      (SELECT r.item_name as item_name, 
            SUM(r.item_count) as item_count 
       FROM rpt_award_item_selection r 
      WHERE ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))            
        AND NVL(r.award_date, TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern)          
                                                    AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
        AND ((p_in_awardLevel IS NULL) OR (p_in_awardLevel is NOT NULL AND r.om_level_name IN (SELECT * FROM TABLE(get_array_varchar(p_in_awardLevel))))) 
        AND ((p_in_parentNodeId IS NULL) OR (p_in_parentNodeId is NOT NULL AND r.node_id IN (SELECT child_node_id FROM rpt_hierarchy_rollup WHERE node_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_parentNodeId)))))) 
        AND NVL(r.country_id,0) = NVL(p_in_countryIds,NVL(r.country_id,0))  --06/10/2015
      GROUP BY r.item_name 
     ORDER BY item_count desc, r.item_name ) 
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
 END prc_getTopRedeemedAwards; 
END pkg_query_plateau_item_select;
/