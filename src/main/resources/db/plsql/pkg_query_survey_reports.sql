CREATE OR REPLACE PACKAGE pkg_query_survey_reports AS
/******************************************************************************
   NAME:       pkg_query_survey_reports
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        7/29/2014      keshaboi       1. Created this package.
              09/09/2014     vinay          1. Created a procedure prc_getsurveyresponselist
             09/29/2014     Suresh J       Bug Fix 56885 - Performance Issue. Replaced all functional calls with Global Temp Table calls. 
******************************************************************************/
 /* getSurveyAnalysisByOrgResults */
  PROCEDURE prc_getSurveyAnalysisByOrg (
      p_in_languageCode        IN     VARCHAR,
      p_in_minimumResponse     IN     NUMBER,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER);
       /* getSurveyAnalysisByQuestionsResults */
 PROCEDURE prc_getSurveyAnalysisByQues(
      p_in_languageCode        IN     VARCHAR,
      p_in_minimumResponse     IN     NUMBER,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
      /* getSurveyStandardDeviationBarResults */
 PROCEDURE prc_getSurveyStandardDeviation(
      p_in_languageCode        IN     VARCHAR,
      p_in_minimumResponse     IN     NUMBER,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      /* getSurveyTotalResponsePercentBarResults */
PROCEDURE prc_getSurveyTotalResponseBar(
      p_in_languageCode        IN     VARCHAR,
      p_in_minimumResponse     IN     NUMBER,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      /* getSurveyResponseList */
PROCEDURE prc_getsurveyresponselist(
      p_in_languageCode        IN     VARCHAR,
      p_in_minimumResponse     IN     NUMBER,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_promotionId        IN      VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);

END pkg_query_survey_reports;
/
CREATE OR REPLACE PACKAGE BODY pkg_query_survey_reports
AS
  PROCEDURE prc_getSurveyAnalysisByOrg (
      p_in_languageCode        IN     VARCHAR,
      p_in_minimumResponse     IN     NUMBER,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getSurveyAnalysisByOrg' ;
      v_stage                VARCHAR2 (500);
      v_sortCol      VARCHAR2(200);
      l_query        VARCHAR2 (32767);
         /* getSurveyAnalysisByOrgResults */
   BEGIN
      v_stage   := 'getSurveyAnalysisByOrgResults';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;
      
    DELETE temp_table_session;
    INSERT INTO temp_table_session (asset_code, cms_name, cms_code)
    SELECT asset_code,cms_value,key FROM vw_cms_asset_value where key in ('QUESTION_RESPONSE','QUESTION_NAME') and locale = p_in_languageCode;
      
      l_query := l_query
         || '  '
         || 'SELECT ROWNUM RN,  
              rs.*  
            FROM ( 
            SELECT NODE_ID,       
            NODE_NAME,        
            NVL(ELIGIBLE_PARTICIPANTS,0) ELIGIBLE_PARTICIPANTS,           
            NVL(SURVEYS_TAKEN,0) SURVEYS_TAKEN,           
            PERC_SURVEY_TAKEN,        
            RESP_1_SELECTED,          
            PERC_RESP_1_SELECTED,         
            RESP_2_SELECTED,          
            PERC_RESP_2_SELECTED,         
            RESP_3_SELECTED,          
            PERC_RESP_3_SELECTED,         
            RESP_4_SELECTED,          
            PERC_RESP_4_SELECTED,         
            RESP_5_SELECTED,          
            PERC_RESP_5_SELECTED,         
            RESP_6_SELECTED,          
            PERC_RESP_6_SELECTED,         
            RESP_7_SELECTED,          
            PERC_RESP_7_SELECTED,         
            RESP_8_SELECTED,          
            PERC_RESP_8_SELECTED,         
            RESP_9_SELECTED,          
            PERC_RESP_9_SELECTED,         
            RESP_10_SELECTED,         
            PERC_RESP_10_SELECTED,            
            MEAN,
           DECODE (TOT_AVERAGE,1,0,0,0,(ROUND(SQRT((POWER(DECODE(RESP_1_SELECTED,0,0,(RESP_1_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_2_SELECTED,0,0,(RESP_2_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_3_SELECTED,0,0,(RESP_3_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_4_SELECTED,0,0,(RESP_4_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_5_SELECTED,0,0,(RESP_5_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_6_SELECTED,0,0,(RESP_6_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_7_SELECTED,0,0,(RESP_7_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_8_SELECTED,0,0,(RESP_8_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_9_SELECTED,0,0,(RESP_9_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_10_SELECTED,0,0,(RESP_10_SELECTED-MEAN)),2)
                                                                                   )/(TOT_AVERAGE-1)
           ),2))) STANDARD_DEVIATION
            FROM(        
           WITH pivot_set AS        
           ( SELECT SUM(NVL(rpc.eligible_participants,0)) ELIGIBLE_PARTICIPANTS         
           ,SUM(NVL(rpc.surveys_taken,0))  SURVEYS_TAKEN        
           ,rh.node_id AS node_id       
           ,rh.node_name node_name          
           ,resp.eligible_responses eligible_responses          
           ,resp.sequence_num           
           FROM         
           (SELECT rh.node_id AS node_id,       
           rh.node_name node_name       
           FROM rpt_hierarchy rh        
           WHERE NVL(parent_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ))          
           ) rh,        
           (       
           SELECT rh.node_id AS node_id        
           ,SUM(rpt.ELIGIBLE_SURVEYS) ELIGIBLE_PARTICIPANTS,
           CASE WHEN
           SUM(rpt.surveys_taken) >= '''||p_in_minimumResponse||''' THEN         
           SUM(rpt.SURVEYS_TAKEN)
           ELSE 0
           END  surveys_taken       
           FROM  rpt_survey_summary rpt,        
           rpt_hierarchy rh,        
           promotion p          
           WHERE  rpt.promotion_id    = p.promotion_id          
           AND detail_node_id      = rh.node_id             
           AND rpt.record_type LIKE ''%node%''            
           AND NVL(header_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ))     
           AND rpt.promotion_id = '''||p_in_promotionId||'''              
           AND NVL(p.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' ''))           
           GROUP BY rh.node_id       
           ) rpc,        
           (SELECT node_id,         
           resp.response,           
           resp.sequence_num,           
           respo.eligible_responses         
           FROM         
           ( SELECT RESPONSE, ROWNUM sequence_num   
           FROM (SELECT INITCAP(response) RESPONSE,COUNT(NO_SURVEY_RESPONSES) NO_SURVEY_RESPONSES   
           FROM (SELECT 
--                  lower(fnc_cms_asset_code_val_extr(sqr.cm_asset_name,''QUESTION_RESPONSE'','''||p_in_languageCode||''')) response,   --09/29/2014
                  lower((SELECT cms_name FROM temp_table_session where asset_code=sqr.cm_asset_name and  cms_code=''QUESTION_RESPONSE'' )) response,              
           PARTICIPANT_SURVEY_RESPONSE_ID NO_SURVEY_RESPONSES       
           FROM survey_question_response sqr, participant_survey_response psr,      
           survey_question sq,      
           promo_survey ps      
           WHERE ps.promotion_id = '''||p_in_promotionId||'''     
           AND psr.survey_question_response_id(+) = sqr.survey_question_response_id     
           AND ps.survey_id = sq.survey_id      
           AND psr.survey_question_response_id IS NOT NULL    
           AND sq.survey_question_id = sqr.survey_question_id)  
           GROUP BY INITCAP(response) ORDER BY  NO_SURVEY_RESPONSES desc)) resp,        
           (SELECT  rh.node_id node_id          
--           ,INITCAP(FNC_CMS_ASSET_CODE_VALUE( rpt.SURVEY_QUESTION_RESPONSE)) survey_question_response   --09/29/2014
         ,INITCAP(cm_qr.cms_name) survey_question_response           
           ,SUM(rpt.eligible_responses) eligible_responses          
           FROM rpt_survey_response_summary rpt,        
           rpt_hierarchy rh,            
           promotion p
           ,(SELECT asset_code,cms_name FROM temp_table_session where cms_code = ''QUESTION_RESPONSE'' ) cm_qr          
           WHERE rpt.promotion_id    = p.promotion_id           
           AND detail_node_id      = rh.node_id   
           AND rpt.SURVEY_QUESTION_RESPONSE =  cm_qr.asset_code          
           AND rpt.record_type LIKE ''%node%''            
           AND NVL(header_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ))
           AND EXISTS (SELECT * FROM rpt_survey_minimum_response WHERE promotion_id=P.promotion_id AND node_id = rh.node_id AND is_leaf=0)           
           AND rpt.promotion_id = '''||p_in_promotionId||'''              
           AND NVL(p.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' ''))           
           GROUP BY rh.node_id 
--           ,FNC_CMS_ASSET_CODE_VALUE( rpt.SURVEY_QUESTION_RESPONSE)   --09/29/2014
           ,INITCAP(cm_qr.cms_name)         
           order by survey_question_response)  respo                
           WHERE respo.survey_question_response = resp.response) resp           
           WHERE rh.node_id             = rpc.node_id (+)           
           AND rh.node_id             = resp.node_id(+)
           GROUP BY rh.node_id,rh.node_name,resp.eligible_responses,resp.response, resp.sequence_num        
           ORDER BY rh.node_name ,  resp.sequence_num                       
           )        
           SELECT  NODE_ID,         
           NODE_NAME,           
           ELIGIBLE_PARTICIPANTS,           
           SURVEYS_TAKEN,           
           DECODE(NVL(ELIGIBLE_PARTICIPANTS,0),0,0,ROUND((SURVEYS_TAKEN/ELIGIBLE_PARTICIPANTS)*100,2)) PERC_SURVEY_TAKEN,                   
           NVL(RESP_1_SELECTED,0) RESP_1_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_1_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_1_SELECTED,           
         NVL(RESP_2_SELECTED,0) RESP_2_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_2_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_2_SELECTED,           
           NVL(RESP_3_SELECTED,0) RESP_3_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_3_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_3_SELECTED,           
           NVL(RESP_4_SELECTED,0) RESP_4_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_4_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_4_SELECTED,           
           NVL(RESP_5_SELECTED,0) RESP_5_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_5_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_5_SELECTED,           
           NVL(RESP_6_SELECTED,0) RESP_6_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_6_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_6_SELECTED,           
           NVL(RESP_7_SELECTED,0) RESP_7_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_7_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_7_SELECTED,           
           NVL(RESP_8_SELECTED,0) RESP_8_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_8_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_8_SELECTED,           
           NVL(RESP_9_SELECTED,0) RESP_9_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_9_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_9_SELECTED,       
           NVL(RESP_10_SELECTED,0) RESP_10_SELECTED,            
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_10_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_10_SELECTED,             
           (DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+         
           DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+          
           DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+DECODE(RESP_10_SELECTED,NULL,0,1)) TOT_AVERAGE,
           DECODE(
           (DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+         
           DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+          
           DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+
           DECODE(RESP_10_SELECTED,NULL,0,1)),0,0,ROUND((NVL(RESP_1_SELECTED,0)+NVL(RESP_2_SELECTED,0)+NVL(RESP_3_SELECTED,0)+NVL(RESP_4_SELECTED,0)+NVL(RESP_5_SELECTED,0)+NVL(RESP_6_SELECTED,0)+NVL(RESP_7_SELECTED,0)+NVL(RESP_8_SELECTED,0)+NVL(RESP_9_SELECTED,0)+NVL(RESP_10_SELECTED,0))/  (DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+         
           DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+          
           DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+DECODE(RESP_10_SELECTED,NULL,0,1)),2)) MEAN            
            FROM pivot_set PIVOT (SUM(eligible_responses) AS selected FOR sequence_num IN (              
            1 AS resp_1,          
            2 AS resp_2,          
            3 AS resp_3,          
            4 AS resp_4,          
            5 AS resp_5,          
            6 AS resp_6,          
            7 AS resp_7,          
            8 AS resp_8,          
            9 AS resp_9,          
            10 AS resp_10))t)         
            UNION ALL     
            SELECT  NODE_ID,          
            NODE_NAME,            
            NVL(ELIGIBLE_PARTICIPANTS,0) ELIGIBLE_PARTICIPANTS,           
            NVL(SURVEYS_TAKEN,0) SURVEYS_TAKEN,           
            PERC_SURVEY_TAKEN,        
            RESP_1_SELECTED,          
            PERC_RESP_1_SELECTED,         
            RESP_2_SELECTED,          
            PERC_RESP_2_SELECTED,         
            RESP_3_SELECTED,          
            PERC_RESP_3_SELECTED,         
            RESP_4_SELECTED,          
            PERC_RESP_4_SELECTED,         
            RESP_5_SELECTED,          
            PERC_RESP_5_SELECTED,         
            RESP_6_SELECTED,          
            PERC_RESP_6_SELECTED,         
            RESP_7_SELECTED,          
            PERC_RESP_7_SELECTED,         
            RESP_8_SELECTED,          
            PERC_RESP_8_SELECTED,         
            RESP_9_SELECTED,          
            PERC_RESP_9_SELECTED,         
            RESP_10_SELECTED,             
            PERC_RESP_10_SELECTED,
            MEAN,
           DECODE (TOT_AVERAGE,1,0,0,0,(ROUND(SQRT((POWER(DECODE(RESP_1_SELECTED,0,0,(RESP_1_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_2_SELECTED,0,0,(RESP_2_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_3_SELECTED,0,0,(RESP_3_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_4_SELECTED,0,0,(RESP_4_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_5_SELECTED,0,0,(RESP_5_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_6_SELECTED,0,0,(RESP_6_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_7_SELECTED,0,0,(RESP_7_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_8_SELECTED,0,0,(RESP_8_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_9_SELECTED,0,0,(RESP_9_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_10_SELECTED,0,0,(RESP_10_SELECTED-MEAN)),2)
                                                                                   )/(TOT_AVERAGE-1)
           ),2))) STANDARD_DEVIATION FROM(                                                                            
           WITH pivot_set AS            
           ( SELECT SUM(NVL(rpc.eligible_participants,0)) ELIGIBLE_PARTICIPANTS         
           ,SUM(NVL(rpc.surveys_taken,0))  SURVEYS_TAKEN        
           ,rh.node_id AS node_id               
           ,rh.node_name node_name          
           ,resp.eligible_responses eligible_responses          
           ,resp.sequence_num           
           FROM         
           (SELECT rh.node_id AS node_id,           
           node_name ||'' Team'' node_name        
           FROM rpt_hierarchy rh        
           WHERE NVL(node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''')))               
           ) rh,            
           (SELECT rh.node_id AS node_id        
           ,SUM(rpt.ELIGIBLE_SURVEYS) ELIGIBLE_PARTICIPANTS,        
           CASE WHEN
           SUM(rpt.surveys_taken) >= '''||p_in_minimumResponse||''' THEN         
           SUM(rpt.SURVEYS_TAKEN)
           ELSE 0
           END  surveys_taken        
           FROM  rpt_survey_summary rpt,        
           rpt_hierarchy rh,            
           promotion p          
           WHERE  rpt.promotion_id    = p.promotion_id              
           AND detail_node_id      = rh.node_id             
           AND rpt.record_type LIKE ''%team%''            
           AND NVL(detail_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ))           
           AND rpt.promotion_id = '''||p_in_promotionId||'''              
           AND NVL(p.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' ''))           
           GROUP BY rh.node_id) rpc,        
           (SELECT node_id,         
           resp.response,           
           resp.sequence_num,           
           respo.eligible_responses         
           FROM         
           ( SELECT RESPONSE, ROWNUM sequence_num   
           FROM (SELECT INITCAP(response) RESPONSE,COUNT(NO_SURVEY_RESPONSES) NO_SURVEY_RESPONSES   
           FROM (SELECT 
--           lower(fnc_cms_asset_code_val_extr(sqr.cm_asset_name,''QUESTION_RESPONSE'','''||p_in_languageCode||''')) response,   --09/29/2014
           lower((SELECT cms_name FROM temp_table_session where asset_code=sqr.cm_asset_name and  cms_code=''QUESTION_RESPONSE'' )) response,   
           PARTICIPANT_SURVEY_RESPONSE_ID NO_SURVEY_RESPONSES       
           FROM survey_question_response sqr, participant_survey_response psr,      
           survey_question sq,      
           promo_survey ps      
           WHERE ps.promotion_id = '''||p_in_promotionId||'''     
           AND psr.survey_question_response_id(+) = sqr.survey_question_response_id     
           AND ps.survey_id = sq.survey_id      
           AND psr.survey_question_response_id IS NOT NULL    
           AND sq.survey_question_id = sqr.survey_question_id)  
           GROUP BY INITCAP(response) ORDER BY  NO_SURVEY_RESPONSES desc)) resp,        
           (SELECT  rh.node_id node_id          
--           ,INITCAP(FNC_CMS_ASSET_CODE_VALUE( rpt.SURVEY_QUESTION_RESPONSE)) survey_question_response   --09/29/2014
         ,INITCAP(cm_qr.cms_name) survey_question_response           
           ,SUM(rpt.eligible_responses) eligible_responses          
           FROM rpt_survey_response_summary rpt,        
           rpt_hierarchy rh,            
           promotion p 
          ,(SELECT asset_code,cms_name FROM temp_table_session where cms_code = ''QUESTION_RESPONSE'' ) cm_qr                    
           WHERE rpt.promotion_id    = p.promotion_id           
           AND detail_node_id      = rh.node_id             
           AND rpt.SURVEY_QUESTION_RESPONSE =  cm_qr.asset_code
           AND rpt.record_type LIKE ''%team%''            
           AND NVL(detail_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ))
           AND EXISTS (SELECT * FROM rpt_survey_minimum_response WHERE promotion_id=P.promotion_id AND node_id = rh.node_id AND is_leaf=1)           
           AND rpt.promotion_id = '''||p_in_promotionId||'''              
           AND NVL(p.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' ''))           
           GROUP BY rh.node_id 
--           ,FNC_CMS_ASSET_CODE_VALUE( rpt.SURVEY_QUESTION_RESPONSE)    --09/29/2014
           ,INITCAP(cm_qr.cms_name)         
           order by survey_question_response)  respo                
           WHERE respo.survey_question_response = resp.response) resp           
           WHERE rh.node_id             = rpc.node_id (+)           
           AND rh.node_id             = resp.node_id(+)         
           GROUP BY rh.node_id,rh.node_name,resp.eligible_responses,resp.response, resp.sequence_num        
           ORDER BY rh.node_name ,  resp.sequence_num                       
           )        
           SELECT  NODE_ID,         
           NODE_NAME,           
           ELIGIBLE_PARTICIPANTS,           
           SURVEYS_TAKEN,           
           DECODE(NVL(ELIGIBLE_PARTICIPANTS,0),0,0,ROUND((SURVEYS_TAKEN/ELIGIBLE_PARTICIPANTS)*100,2)) PERC_SURVEY_TAKEN,                   
           NVL(RESP_1_SELECTED,0) RESP_1_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_1_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_1_SELECTED,           
           NVL(RESP_2_SELECTED,0) RESP_2_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_2_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_2_SELECTED,           
           NVL(RESP_3_SELECTED,0) RESP_3_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_3_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_3_SELECTED,           
           NVL(RESP_4_SELECTED,0) RESP_4_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_4_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_4_SELECTED,           
           NVL(RESP_5_SELECTED,0) RESP_5_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_5_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_5_SELECTED,           
           NVL(RESP_6_SELECTED,0) RESP_6_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_6_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_6_SELECTED,           
           NVL(RESP_7_SELECTED,0) RESP_7_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_7_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_7_SELECTED,           
           NVL(RESP_8_SELECTED,0) RESP_8_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_8_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_8_SELECTED,           
           NVL(RESP_9_SELECTED,0) RESP_9_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_9_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_9_SELECTED,       
           NVL(RESP_10_SELECTED,0) RESP_10_SELECTED,            
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_10_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_10_SELECTED,             
           (DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+         
           DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+          
           DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+DECODE(RESP_10_SELECTED,NULL,0,1)) TOT_AVERAGE,
           DECODE(
           (DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+         
           DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+          
           DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+
           DECODE(RESP_10_SELECTED,NULL,0,1)),0,0,ROUND((NVL(RESP_1_SELECTED,0)+NVL(RESP_2_SELECTED,0)+NVL(RESP_3_SELECTED,0)+NVL(RESP_4_SELECTED,0)+NVL(RESP_5_SELECTED,0)+NVL(RESP_6_SELECTED,0)+NVL(RESP_7_SELECTED,0)+NVL(RESP_8_SELECTED,0)+NVL(RESP_9_SELECTED,0)+NVL(RESP_10_SELECTED,0))/  (DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+         
           DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+          
           DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+DECODE(RESP_10_SELECTED,NULL,0,1)),2)) MEAN                     
           FROM pivot_set PIVOT (SUM(eligible_responses) AS selected FOR sequence_num IN (      
            1 AS resp_1,          
            2 AS resp_2,          
            3 AS resp_3,          
            4 AS resp_4,          
            5 AS resp_5,          
            6 AS resp_6,          
            7 AS resp_7,          
            8 AS resp_8,          
            9 AS resp_9,          
            10 AS resp_10))t)
              ) rs  
         ORDER BY '|| v_sortCol ||'
   ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
      OPEN p_out_data FOR l_query;
     /* getSurveyAnalysisByOrgResultsSize */
SELECT COUNT (1) INTO p_out_size_data
FROM
  (SELECT rh.node_name node_name
  FROM rpt_hierarchy rh
  WHERE NVL(parent_node_id,0) IN
    (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId))
    )
  UNION ALL
  SELECT node_name
    ||' Team' node_name
  FROM rpt_hierarchy rh
  WHERE NVL(node_id,0) IN
    (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId))
    )
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
 END prc_getSurveyAnalysisByOrg;
 /* getSurveyAnalysisByQuestionsResults */
 /******************************************************************************
   NAME:       prc_getSurveyAnalysisByQues
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        7/29/2014      keshaboi       1. Created this package.
             09/09/2014   Suresh J         Bug Fix 56302, 56366 -  Added query for nodesum  
             09/25/2014   Suresh J         Bug Fix 56884 - Survey Question Table shows duplicated rows by question ID 
             09/28/2014   nagarajs        Bug 64084 - Standard deviation calculation wrong
******************************************************************************/
PROCEDURE prc_getSurveyAnalysisByQues (
      p_in_languageCode        IN     VARCHAR,
      p_in_minimumResponse     IN     NUMBER,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getSurveyAnalysisByQues' ;
      v_stage                VARCHAR2 (500);
      v_sortCol      VARCHAR2(200);
      l_query        VARCHAR2 (32767);
        /* getSurveyAnalysisByQuestions */
   BEGIN
      v_stage   := 'getSurveyAnalysisByQuestions';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
    
    DELETE temp_table_session;
    INSERT INTO temp_table_session (asset_code, cms_name, cms_code)
    SELECT asset_code,cms_value,key FROM vw_cms_asset_value where key in ('QUESTION_RESPONSE','QUESTION_NAME') and locale = p_in_languageCode;
      
      l_query := l_query
         || '  '
         || 'SELECT ROWNUM RN,  
              rs.*  
            FROM ( 
     SELECT survey_question_id, SURVEYQUESTION,             
                 ELIGIBLE_PARTICIPANTS,                 
                 SURVEYS_TAKEN   ,      
                 PERC_SURVEY_TAKEN,     
                 RESP_1_SELECTED    ,       
                 PERC_RESP_1_SELECTED,          
                 RESP_2_SELECTED    ,       
                 PERC_RESP_2_SELECTED,          
                 RESP_3_SELECTED    ,       
                 PERC_RESP_3_SELECTED,          
                 RESP_4_SELECTED    ,       
                 PERC_RESP_4_SELECTED,          
                 RESP_5_SELECTED    ,       
                 PERC_RESP_5_SELECTED,          
                 RESP_6_SELECTED    ,       
                 PERC_RESP_6_SELECTED,          
                 RESP_7_SELECTED    ,       
                 PERC_RESP_7_SELECTED,          
                 RESP_8_SELECTED    ,       
                 PERC_RESP_8_SELECTED,          
                 RESP_9_SELECTED    ,       
                 PERC_RESP_9_SELECTED,      
                 RESP_10_SELECTED    ,          
                 PERC_RESP_10_SELECTED,         
                 MEAN, 
                  DECODE (TOT_X,1,0,0,0,(ROUND(SQRT((POWER(DECODE(RESP_1_SELECTED,0,0,(RESP_1_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_2_SELECTED,0,0,(RESP_2_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_3_SELECTED,0,0,(RESP_3_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_4_SELECTED,0,0,(RESP_4_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_5_SELECTED,0,0,(RESP_5_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_6_SELECTED,0,0,(RESP_6_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_7_SELECTED,0,0,(RESP_7_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_8_SELECTED,0,0,(RESP_8_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_9_SELECTED,0,0,(RESP_9_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_10_SELECTED,0,0,(RESP_10_SELECTED-MEAN)),2)
                                                                                   )/(TOT_X-1)
           ),2))) STANDARD_DEVIATION      --09/29/2015
                 --DECODE(NVL(TOT_X,0),0,0,DECODE((TOT_X-1),0,0,ROUND(SQRT(((TOT_X * (TOT_X+1)*((2*TOT_X)+1))/ 6) - ((TOT_X*(TOT_X+1)/2) * (TOT_X * (TOT_X+1)/2)/ TOT_X))/(TOT_X-1),2))) STANDARD_DEVIATION --09/29/2015      
                 FROM       
                 (WITH pivot_set AS     
                 (SELECT ELIGIBLE_PARTICIPANTS      
                 ,SURVEYS_TAKEN     
                 ,SURVEYQUESTION    
                 ,ELIGIBLE_RESPONSES    
                 ,SEQUENCE_NUM 
                 ,survey_question_id      
                 FROM           
                 (
           SELECT ELIGIBLE_PARTICIPANTS, SURVEYQUESTION,survey_question_id,     
                 SURVEYQUESTIONRESPONSE,ELIGIBLE_RESPONSES,SURVEYS_TAKEN    
                 FROM(  
                 SELECT rp.promotion_id promotion_id            
                 ,NVL(rp.surveys_taken,0)  SURVEYS_TAKEN        
                 ,rpc.SurveyQuestion    
                 ,INITCAP(rpc.SurveyQuestionResponse) SurveyQuestionResponse, 
                 rpc.survey_question_id       
                 ,rpc.eligible_responses    
                 FROM       
                 (SELECT rh.node_id AS node_id,        
                 rh.node_name node_name         
                 FROM rpt_hierarchy rh      
                 WHERE NVL(parent_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ))) rh,          
                 (SELECT rh.node_id AS node_id , p.promotion_id         
                 ,SUM(rpt.SURVEYS_TAKEN)  SURVEYS_TAKEN         
                 FROM  rpt_survey_summary rpt,      
                 rpt_hierarchy rh,          
                 promotion p        
                 WHERE  rpt.promotion_id    = p.promotion_id            
                 AND detail_node_id      = rh.node_id           
                 AND rpt.record_type LIKE ''%node%''          
                 AND NVL(header_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ))         
                 AND rpt.promotion_id = '''||p_in_promotionId||'''            
                 AND NVL(p.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' ''))         
                 GROUP BY rh.node_id, p.promotion_id) rp,       
                 (SELECT rh.node_id AS node_id          
                 ,rh.node_name node_name        
--                 ,FNC_CMS_ASSET_CODE_VALUE(Q.CM_ASSET_NAME) SurveyQuestion,   --09/29/2014
                 ,cm_q.cms_name SurveyQuestion 
                 ,q.survey_question_id     
--                 ,FNC_CMS_ASSET_CODE_VALUE(SQ.CM_ASSET_NAME)SurveyQuestionResponse  --09/29/2014
                 ,cm_qr.cms_name SurveyQuestionResponse      
                 ,NVL(SUM(rpt.eligible_responses),0) eligible_responses     
                 FROM survey_question_response sq,      
                 survey_question q,         
                 rpt_survey_response_summary rpt,       
                 rpt_hierarchy rh,          
                 promotion p
                ,(SELECT asset_code,cms_name FROM temp_table_session where cms_code = ''QUESTION_RESPONSE'' ) cm_qr                  
                ,(SELECT asset_code,cms_name FROM temp_table_session where cms_code = ''QUESTION_NAME'' ) cm_q         
                 WHERE sq.cm_asset_name = rpt.survey_question_response(+)  
                 AND sq.cm_asset_name =  cm_qr.asset_code(+)
                 AND q.cm_asset_name =  cm_q.asset_code (+)      
                 AND q.survey_question_id = sq.survey_question_id(+)        
                 AND rpt.promotion_id    = p.promotion_id           
                 AND detail_node_id      = rh.node_id           
                 AND rpt.record_type LIKE ''%node%''          
                 AND NVL(header_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ))     
                 AND EXISTS (SELECT * FROM rpt_survey_minimum_response WHERE promotion_id=P.promotion_id AND node_id = rh.node_id AND is_leaf=0)            
                 AND rpt.promotion_id = '''||p_in_promotionId||'''            
                 AND NVL(p.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' ''))         
                 GROUP BY rh.node_id, rh.node_name
--                 , FNC_CMS_ASSET_CODE_VALUE(Q.CM_ASSET_NAME),FNC_CMS_ASSET_CODE_VALUE(SQ.CM_ASSET_NAME)  --09/29/2014
                  ,cm_qr.cms_name,cm_q.cms_name
                 ,q.survey_question_id   ) rpc       
                 WHERE rh.node_id             = rpc.node_id (+)         
                 AND rh.node_id             = rp.node_id (+)                 
            UNION ALL  --09/25/2014
                 SELECT rp.promotion_id promotion_id            
                 ,NVL(rp.surveys_taken,0)  SURVEYS_TAKEN        
                 ,rpc.SurveyQuestion    
                 ,INITCAP(rpc.SurveyQuestionResponse) SurveyQuestionResponse, 
                 rpc.survey_question_id       
                 ,rpc.eligible_responses    
                 FROM       
                 (SELECT rh.node_id AS node_id,        
                 rh.node_name node_name         
                 FROM rpt_hierarchy rh      
                 WHERE NVL(node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ))) rh,          
                 (SELECT rh.node_id AS node_id , p.promotion_id         
                 ,SUM(rpt.SURVEYS_TAKEN)  SURVEYS_TAKEN         
                 FROM  rpt_survey_summary rpt,      
                 rpt_hierarchy rh,          
                 promotion p        
                 WHERE  rpt.promotion_id    = p.promotion_id            
                 AND detail_node_id      = rh.node_id           
                 AND rpt.record_type LIKE ''%team%''          
                 AND NVL(detail_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ))         
                 AND rpt.promotion_id = '''||p_in_promotionId||'''            
                 AND NVL(p.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' ''))         
                 GROUP BY rh.node_id, p.promotion_id) rp,       
                 (SELECT rh.node_id AS node_id          
                 ,rh.node_name node_name        
--                 ,FNC_CMS_ASSET_CODE_VALUE(Q.CM_ASSET_NAME) SurveyQuestion,   --09/29/2014
                 ,cm_q.cms_name SurveyQuestion 
                 ,q.survey_question_id     
--               ,FNC_CMS_ASSET_CODE_VALUE(SQ.CM_ASSET_NAME)SurveyQuestionResponse   --09/29/2014
                 ,cm_qr.cms_name SurveyQuestionResponse        
                 ,NVL(SUM(rpt.eligible_responses),0) eligible_responses     
                 FROM survey_question_response sq,      
                 survey_question q,         
                 rpt_survey_response_summary rpt,       
                 rpt_hierarchy rh,          
                 promotion p
                 ,(SELECT asset_code,cms_name FROM temp_table_session where cms_code = ''QUESTION_RESPONSE'' ) cm_qr                  
                 ,(SELECT asset_code,cms_name FROM temp_table_session where cms_code = ''QUESTION_NAME'' ) cm_q         
                 WHERE sq.cm_asset_name = rpt.survey_question_response(+)       
                 AND sq.cm_asset_name =  cm_qr.asset_code(+)
                 AND q.cm_asset_name =  cm_q.asset_code (+)      
                 AND q.survey_question_id = sq.survey_question_id(+)        
                 AND rpt.promotion_id    = p.promotion_id           
                 AND detail_node_id      = rh.node_id           
                 AND rpt.record_type LIKE ''%team%''          
                 AND NVL(detail_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ))     
                 AND EXISTS (SELECT * FROM rpt_survey_minimum_response WHERE promotion_id=P.promotion_id AND node_id = rh.node_id AND is_leaf=1)            
                 AND rpt.promotion_id = '''||p_in_promotionId||'''            
                 AND NVL(p.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' ''))         
                 GROUP BY rh.node_id, rh.node_name
--                 , FNC_CMS_ASSET_CODE_VALUE(Q.CM_ASSET_NAME),FNC_CMS_ASSET_CODE_VALUE(SQ.CM_ASSET_NAME)   --09/29/2014
                 ,cm_qr.cms_name,cm_q.cms_name
                 ,q.survey_question_id   ) rpc       
                 WHERE rh.node_id             = rpc.node_id (+)         
                 AND rh.node_id             = rp.node_id (+)) rspn,     
                 (SELECT s.promotion_id , sum(s.ELIGIBLE_SURVEYS) ELIGIBLE_PARTICIPANTS     
                 FROM RPT_SURVEY_summary s,
                 RPT_HIERARCHY_ROLLUP hr     
                 WHERE hr.child_node_id = s.detail_node_id 
                 AND s.record_type like ''%team%''
                 AND '''||p_in_parentNodeId||''' = hr.node_id         
                 group by s.promotion_id)elig       
                 WHERE elig.promotion_id = rspn.promotion_id
                 ) res,      
                 (SELECT RESPONSE, ROWNUM sequence_num      
                 FROM (SELECT INITCAP(response) RESPONSE,COUNT(NO_SURVEY_RESPONSES) NO_SURVEY_RESPONSES     
                 FROM (SELECT 
--                 lower(fnc_cms_asset_code_val_extr(sqr.cm_asset_name,''QUESTION_RESPONSE'','''||p_in_languageCode||''')) response,  --09/29/2014
                  lower((SELECT cms_name FROM temp_table_session where asset_code=sqr.cm_asset_name and  cms_code=''QUESTION_RESPONSE'' )) response,     
                 PARTICIPANT_SURVEY_RESPONSE_ID NO_SURVEY_RESPONSES         
                 FROM survey_question_response sqr, participant_survey_response psr,        
                 survey_question sq,        
                 promo_survey ps        
                 WHERE ps.promotion_id = '''||p_in_promotionId||'''       
                 AND psr.survey_question_response_id(+) = sqr.survey_question_response_id       
                 AND ps.survey_id = sq.survey_id        
                 AND psr.survey_question_response_id IS NOT NULL      
                 AND sq.survey_question_id = sqr.survey_question_id)    
                 GROUP BY INITCAP(response) ORDER BY  NO_SURVEY_RESPONSES desc)) ref    
                 WHERE res.surveyquestionresponse = ref.response    
                 AND res.surveys_taken <> 0     
                 ORDER BY ref.sequence_num)     
                 SELECT SURVEYQUESTION,survey_question_id, 
                 NVL(ELIGIBLE_PARTICIPANTS,0) ELIGIBLE_PARTICIPANTS,        
                 NVL(SUM(SURVEYS_TAKEN),0) SURVEYS_TAKEN,       
                 DECODE( NVL(ELIGIBLE_PARTICIPANTS,0),0,0,ROUND(NVL(SUM(SURVEYS_TAKEN),0)/ NVL(ELIGIBLE_PARTICIPANTS,0) * 100,2)) PERC_SURVEY_TAKEN,    
                 NVL(SUM(RESP_1_SELECTED),0) RESP_1_SELECTED,       
                 DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_1_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_1_SELECTED,         
                 NVL(SUM(RESP_2_SELECTED),0) RESP_2_SELECTED,       
                 DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_2_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_2_SELECTED,         
                 NVL(SUM(RESP_3_SELECTED),0) RESP_3_SELECTED,       
                 DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_3_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_3_SELECTED,         
                 NVL(SUM(RESP_4_SELECTED),0) RESP_4_SELECTED,       
                 DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_4_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_4_SELECTED,         
                 NVL(SUM(RESP_5_SELECTED),0) RESP_5_SELECTED,       
                 DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_5_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_5_SELECTED,         
                 NVL(SUM(RESP_6_SELECTED),0) RESP_6_SELECTED,       
                 DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_6_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_6_SELECTED,         
                 NVL(SUM(RESP_7_SELECTED),0) RESP_7_SELECTED,       
                 DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_7_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_7_SELECTED,         
                 NVL(SUM(RESP_8_SELECTED),0) RESP_8_SELECTED,       
                 DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_8_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_8_SELECTED,         
                 NVL(SUM(RESP_9_SELECTED),0) RESP_9_SELECTED,       
                 DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_9_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_9_SELECTED,     
                 NVL(SUM(RESP_10_SELECTED),0) RESP_10_SELECTED,         
                 DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_10_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_10_SELECTED,  
                 DECODE( 
                 (SUM(DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+     
                 DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+           
                 DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+ 
                 DECODE(RESP_10_SELECTED,NULL,0,1))),0,0,ROUND((NVL(SUM(RESP_1_SELECTED),0)+NVL(SUM(RESP_2_SELECTED),0)+NVL(SUM(RESP_3_SELECTED),0)+NVL(SUM(RESP_4_SELECTED),0)+NVL(SUM(RESP_5_SELECTED),0)+NVL(SUM(RESP_6_SELECTED),0)+NVL(SUM(RESP_7_SELECTED),0)+NVL(SUM(RESP_8_SELECTED),0)+NVL(SUM(RESP_9_SELECTED),0)+NVL(SUM(RESP_10_SELECTED),0))/  SUM((DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+           
                 DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+            
                 DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+DECODE(RESP_10_SELECTED,NULL,0,1))),2)) MEAN,         
                 SUM(DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+        
                 DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+        
                 DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+    
                 DECODE(RESP_10_SELECTED,NULL,0,1)) TOT_X       
                 FROM pivot_set     
                 PIVOT (SUM(eligible_responses) AS SELECTED FOR sequence_num IN     
                 ( 1 AS resp_1,         
                 2 AS resp_2,       
                 3 AS resp_3,       
                 4 AS resp_4,       
                 5 AS resp_5,       
                 6 AS resp_6,       
                 7 AS resp_7,       
                 8 AS resp_8,       
                 9 AS resp_9,       
                 10 AS resp_10))t       
                 GROUP BY SURVEYQUESTION,ELIGIBLE_PARTICIPANTS,survey_question_id) 
              ) rs  
         ORDER BY '|| v_sortCol ||'
   ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;

      OPEN p_out_data FOR l_query;
     /* getSurveyAnalysisByQuestionsResultsSize */
 SELECT COUNT (DISTINCT SURVEY_QUESTION_ID) INTO p_out_size_data
FROM rpt_survey_response_detail r,
  promotion p
WHERE r.promotion_id = p.promotion_id
AND p.promotion_id   = p_in_promotionId
AND NVL(node_id,0)  IN
  (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )
  )
AND EXISTS
  (SELECT *
  FROM rpt_survey_minimum_response
  WHERE promotion_id=P.promotion_id
  AND node_id       = r.node_id
  AND is_leaf       =1
  )
AND NVL(p.promotion_status,' ') = NVL(p_in_promotionStatus,NVL(p.promotion_status,' ')) ;

/* getSurveyAnalysisByQuestionsResultsTotal */
 OPEN p_out_totals_data FOR
SELECT
           ELIGIBLE_PARTICIPANTS      ELIGIBLE_SURVEYS,          
                SURVEYS_TAKEN               SURVEYS_TAKEN,       
                PERC_SURVEY_TAKEN           PERC_SURVEY_TAKEN,                   
                RESP_1_SELECTED        RESP_1_SELECTED,         
                PERC_RESP_1_SELECTED   PERC_RESP_1_SELECTED,            
                RESP_2_SELECTED       RESP_2_SELECTED,         
                PERC_RESP_2_SELECTED   PERC_RESP_2_SELECTED,            
                RESP_3_SELECTED        RESP_3_SELECTED,         
                PERC_RESP_3_SELECTED   PERC_RESP_3_SELECTED,            
                RESP_4_SELECTED        RESP_4_SELECTED,         
                PERC_RESP_4_SELECTED   PERC_RESP_4_SELECTED,            
                RESP_5_SELECTED        RESP_5_SELECTED,         
                PERC_RESP_5_SELECTED   PERC_RESP_5_SELECTED,            
                RESP_6_SELECTED        RESP_6_SELECTED,         
                PERC_RESP_6_SELECTED   PERC_RESP_6_SELECTED,            
                RESP_7_SELECTED        RESP_7_SELECTED,         
                PERC_RESP_7_SELECTED   PERC_RESP_7_SELECTED,            
                RESP_8_SELECTED        RESP_8_SELECTED,         
                PERC_RESP_8_SELECTED  PERC_RESP_8_SELECTED,            
                RESP_9_SELECTED        RESP_9_SELECTED    ,         
                PERC_RESP_9_SELECTED   PERC_RESP_9_SELECTED,    
                RESP_10_SELECTED       RESP_10_SELECTED,        
                PERC_RESP_10_SELECTED  PERC_RESP_10_SELECTED,             
            MEAN MEAN,  
            DECODE (TOT_X,1,0,0,0,(ROUND(SQRT((POWER(DECODE(RESP_1_SELECTED,0,0,(RESP_1_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_2_SELECTED,0,0,(RESP_2_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_3_SELECTED,0,0,(RESP_3_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_4_SELECTED,0,0,(RESP_4_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_5_SELECTED,0,0,(RESP_5_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_6_SELECTED,0,0,(RESP_6_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_7_SELECTED,0,0,(RESP_7_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_8_SELECTED,0,0,(RESP_8_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_9_SELECTED,0,0,(RESP_9_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_10_SELECTED,0,0,(RESP_10_SELECTED-MEAN)),2)
                                                                                   )/(TOT_X-1)
           ),2))) STANDARD_DEVIATION      --09/29/2015
             --   DECODE(NVL(TOT_X,0),0,0,DECODE((TOT_X-1),0,0,ROUND(SQRT(((TOT_X * (TOT_X+1)*((2*TOT_X)+1))/ 6) - ((TOT_X*(TOT_X+1)/2) * (TOT_X * (TOT_X+1)/2)/ TOT_X))/(TOT_X-1),2))) STANDARD_DEVIATION  --09/29/2015  
           FROM     
           (WITH pivot_set AS   
           (SELECT ELIGIBLE_PARTICIPANTS    
           ,SURVEYS_TAKEN   
           ,SURVEYQUESTION  
           ,ELIGIBLE_RESPONSES  
           ,SEQUENCE_NUM    
           FROM         
           (SELECT ELIGIBLE_PARTICIPANTS, SURVEYQUESTION,   
           SURVEYQUESTIONRESPONSE,ELIGIBLE_RESPONSES,SURVEYS_TAKEN  
           FROM(
           SELECT rp.promotion_id promotion_id          
           ,NVL(rp.surveys_taken,0)  SURVEYS_TAKEN      
           ,rpc.SurveyQuestion  
           ,INITCAP(rpc.SurveyQuestionResponse) SurveyQuestionResponse          
           ,rpc.eligible_responses  
           FROM     
           (SELECT rh.node_id AS node_id,      
           rh.node_name node_name       
           FROM rpt_hierarchy rh    
           WHERE NVL(parent_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))) rh,        
           (SELECT rh.node_id AS node_id , p.promotion_id       
           ,SUM(rpt.SURVEYS_TAKEN)  SURVEYS_TAKEN       
           FROM  rpt_survey_summary rpt,    
           rpt_hierarchy rh,        
           promotion p      
           WHERE  rpt.promotion_id    = p.promotion_id          
           AND detail_node_id      = rh.node_id         
           AND rpt.record_type LIKE '%node%'        
           AND NVL(header_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))       
           AND rpt.promotion_id = p_in_promotionId          
           AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))       
           GROUP BY rh.node_id, p.promotion_id) rp,     
           (SELECT rh.node_id AS node_id        
           ,rh.node_name node_name      
--           ,FNC_CMS_ASSET_CODE_VALUE(Q.CM_ASSET_NAME) SurveyQuestion   --09/29/2014
           ,cm_q.cms_name SurveyQuestion    
--           ,FNC_CMS_ASSET_CODE_VALUE(SQ.CM_ASSET_NAME)SurveyQuestionResponse  --09/29/2014    
           ,cm_qr.cms_name SurveyQuestionResponse
           ,NVL(SUM(rpt.eligible_responses),0) eligible_responses   
           FROM survey_question_response sq,    
           survey_question q,       
           rpt_survey_response_summary rpt,     
           rpt_hierarchy rh,        
           promotion p      
         ,(SELECT asset_code,cms_name FROM temp_table_session where cms_code = 'QUESTION_RESPONSE' ) cm_qr                  
         ,(SELECT asset_code,cms_name FROM temp_table_session where cms_code = 'QUESTION_NAME' ) cm_q         
           WHERE sq.cm_asset_name = rpt.survey_question_response(+)     
           AND sq.cm_asset_name =  cm_qr.asset_code(+)
           AND q.cm_asset_name =  cm_q.asset_code (+)      
           AND q.survey_question_id = sq.survey_question_id(+)      
           AND rpt.promotion_id    = p.promotion_id         
           AND detail_node_id      = rh.node_id         
           AND rpt.record_type LIKE '%node%'        
           AND NVL(header_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))   
           AND EXISTS (SELECT * FROM rpt_survey_minimum_response WHERE promotion_id=P.promotion_id AND node_id = rh.node_id AND is_leaf=0)          
           AND rpt.promotion_id = p_in_promotionId          
           AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))       
           GROUP BY rh.node_id, rh.node_name
--           , FNC_CMS_ASSET_CODE_VALUE(Q.CM_ASSET_NAME),FNC_CMS_ASSET_CODE_VALUE(SQ.CM_ASSET_NAME)  --09/29/2014
           ,cm_qr.cms_name,cm_q.cms_name
           ) rpc     
           WHERE rh.node_id             = rpc.node_id (+)       
           AND rh.node_id             = rp.node_id (+)
      UNION ALL  --09/25/2014
           SELECT rp.promotion_id promotion_id          
           ,NVL(rp.surveys_taken,0)  SURVEYS_TAKEN      
           ,rpc.SurveyQuestion  
           ,INITCAP(rpc.SurveyQuestionResponse) SurveyQuestionResponse          
           ,rpc.eligible_responses  
           FROM     
           (SELECT rh.node_id AS node_id,      
           rh.node_name node_name       
           FROM rpt_hierarchy rh    
           WHERE NVL(node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))) rh,        
           (SELECT rh.node_id AS node_id , p.promotion_id       
           ,SUM(rpt.SURVEYS_TAKEN)  SURVEYS_TAKEN       
           FROM  rpt_survey_summary rpt,    
           rpt_hierarchy rh,        
           promotion p      
           WHERE  rpt.promotion_id    = p.promotion_id          
           AND detail_node_id      = rh.node_id         
           AND rpt.record_type LIKE '%team%'        
           AND NVL(detail_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))       
           AND rpt.promotion_id = p_in_promotionId          
           AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))       
           GROUP BY rh.node_id, p.promotion_id) rp,     
           (SELECT rh.node_id AS node_id        
           ,rh.node_name node_name      
--           ,FNC_CMS_ASSET_CODE_VALUE(Q.CM_ASSET_NAME) SurveyQuestion   --09/29/2014
           ,cm_q.cms_name SurveyQuestion    
--           ,FNC_CMS_ASSET_CODE_VALUE(SQ.CM_ASSET_NAME)SurveyQuestionResponse   --09/29/2014    
           ,cm_qr.cms_name SurveyQuestionResponse
           ,NVL(SUM(rpt.eligible_responses),0) eligible_responses   
           FROM survey_question_response sq,    
           survey_question q,       
           rpt_survey_response_summary rpt,     
           rpt_hierarchy rh,        
           promotion p      
         ,(SELECT asset_code,cms_name FROM temp_table_session where cms_code = 'QUESTION_RESPONSE' ) cm_qr                  
         ,(SELECT asset_code,cms_name FROM temp_table_session where cms_code = 'QUESTION_NAME' ) cm_q         
           WHERE sq.cm_asset_name = rpt.survey_question_response(+)     
           AND sq.cm_asset_name =  cm_qr.asset_code(+)
           AND q.cm_asset_name =  cm_q.asset_code (+)      
           AND q.survey_question_id = sq.survey_question_id(+)      
           AND rpt.promotion_id    = p.promotion_id         
           AND detail_node_id      = rh.node_id         
           AND rpt.record_type LIKE '%team%'        
           AND NVL(detail_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))   
           AND EXISTS (SELECT * FROM rpt_survey_minimum_response WHERE promotion_id=P.promotion_id AND node_id = rh.node_id AND is_leaf=1)          
           AND rpt.promotion_id = p_in_promotionId          
           AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))       
           GROUP BY rh.node_id, rh.node_name
--           , FNC_CMS_ASSET_CODE_VALUE(Q.CM_ASSET_NAME),FNC_CMS_ASSET_CODE_VALUE(SQ.CM_ASSET_NAME)   --09/29/2014
           ,cm_qr.cms_name,cm_q.cms_name
           ) rpc     
           WHERE rh.node_id             = rpc.node_id (+)       
           AND rh.node_id             = rp.node_id (+)) rspn,   
           (SELECT s.promotion_id , sum(s.ELIGIBLE_SURVEYS) ELIGIBLE_PARTICIPANTS   
           FROM RPT_SURVEY_summary s,
                RPT_HIERARCHY_ROLLUP hr   
           WHERE hr.child_node_id = s.detail_node_id
           AND p_in_parentNodeId = hr.node_id    
           AND s.record_type like '%team%'       
           group by s.promotion_id)elig     
           WHERE elig.promotion_id = rspn.promotion_id
           ) res,    
           (SELECT RESPONSE, ROWNUM sequence_num    
           FROM (SELECT INITCAP(response) RESPONSE,COUNT(NO_SURVEY_RESPONSES) NO_SURVEY_RESPONSES   
           FROM (SELECT 
--           lower(fnc_cms_asset_code_val_extr(sqr.cm_asset_name,'QUESTION_RESPONSE',p_in_languageCode)) response,  --09/29/2014
             lower((SELECT cms_name FROM temp_table_session where asset_code=sqr.cm_asset_name and  cms_code='QUESTION_RESPONSE' )) response,   
           PARTICIPANT_SURVEY_RESPONSE_ID NO_SURVEY_RESPONSES       
           FROM survey_question_response sqr, participant_survey_response psr,      
           survey_question sq,      
           promo_survey ps      
           WHERE ps.promotion_id = p_in_promotionId     
           AND psr.survey_question_response_id(+) = sqr.survey_question_response_id     
           AND ps.survey_id = sq.survey_id      
           AND psr.survey_question_response_id IS NOT NULL    
           AND sq.survey_question_id = sqr.survey_question_id)  
           GROUP BY INITCAP(response) ORDER BY  NO_SURVEY_RESPONSES desc)) ref  
           WHERE res.surveyquestionresponse = ref.response  
           AND res.surveys_taken <> 0   
           ORDER BY ref.sequence_num)   
           SELECT       
           NVL(ELIGIBLE_PARTICIPANTS,0) ELIGIBLE_PARTICIPANTS,      
           NVL(SUM(SURVEYS_TAKEN),0) SURVEYS_TAKEN,     
           DECODE( NVL(ELIGIBLE_PARTICIPANTS,0),0,0,ROUND(NVL(SUM(SURVEYS_TAKEN),0)/ NVL(ELIGIBLE_PARTICIPANTS,0) * 100,2)) PERC_SURVEY_TAKEN,  
           NVL(SUM(RESP_1_SELECTED),0) RESP_1_SELECTED,     
           DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_1_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_1_SELECTED,       
           NVL(SUM(RESP_2_SELECTED),0) RESP_2_SELECTED,     
           DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_2_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_2_SELECTED,       
           NVL(SUM(RESP_3_SELECTED),0) RESP_3_SELECTED,     
           DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_3_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_3_SELECTED,       
           NVL(SUM(RESP_4_SELECTED),0) RESP_4_SELECTED,     
           DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_4_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_4_SELECTED,       
           NVL(SUM(RESP_5_SELECTED),0) RESP_5_SELECTED,     
           DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_5_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_5_SELECTED,       
           NVL(SUM(RESP_6_SELECTED),0) RESP_6_SELECTED,     
           DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_6_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_6_SELECTED,       
           NVL(SUM(RESP_7_SELECTED),0) RESP_7_SELECTED,     
           DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_7_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_7_SELECTED,       
           NVL(SUM(RESP_8_SELECTED),0) RESP_8_SELECTED,     
           DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_8_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_8_SELECTED,       
           NVL(SUM(RESP_9_SELECTED),0) RESP_9_SELECTED,     
           DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_9_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_9_SELECTED,   
           NVL(SUM(RESP_10_SELECTED),0) RESP_10_SELECTED,       
           DECODE( NVL(SUM(SURVEYS_TAKEN),0),0,0,ROUND(NVL(SUM(RESP_10_SELECTED),0)/ NVL(SUM(SURVEYS_TAKEN),0) * 100,2)) PERC_RESP_10_SELECTED,
           DECODE(
           (DECODE(SUM(RESP_1_SELECTED),NULL,0,1)+DECODE(SUM(RESP_2_SELECTED),NULL,0,1)+DECODE(SUM(RESP_3_SELECTED),NULL,0,1)+         
           DECODE(SUM(RESP_4_SELECTED),NULL,0,1)+DECODE(SUM(RESP_5_SELECTED),NULL,0,1)+DECODE(SUM(RESP_6_SELECTED),NULL,0,1)+          
           DECODE(SUM(RESP_7_SELECTED),NULL,0,1)+DECODE(SUM(RESP_8_SELECTED),NULL,0,1)+DECODE(SUM(RESP_9_SELECTED),NULL,0,1)+
           DECODE(SUM(RESP_10_SELECTED),NULL,0,1)),0,0,ROUND((NVL(SUM(RESP_1_SELECTED),0)+NVL(SUM(RESP_2_SELECTED),0)+NVL(SUM(RESP_3_SELECTED),0)+NVL(SUM(RESP_4_SELECTED),0)+NVL(SUM(RESP_5_SELECTED),0)+NVL(SUM(RESP_6_SELECTED),0)+NVL(SUM(RESP_7_SELECTED),0)+NVL(SUM(RESP_8_SELECTED),0)+NVL(SUM(RESP_9_SELECTED),0)+NVL(SUM(RESP_10_SELECTED),0))/  (DECODE(SUM(RESP_1_SELECTED),NULL,0,1)+DECODE(SUM(RESP_2_SELECTED),NULL,0,1)+DECODE(SUM(RESP_3_SELECTED),NULL,0,1)+         
           DECODE(SUM(RESP_4_SELECTED),NULL,0,1)+DECODE(SUM(RESP_5_SELECTED),NULL,0,1)+DECODE(SUM(RESP_6_SELECTED),NULL,0,1)+          
           DECODE(SUM(RESP_7_SELECTED),NULL,0,1)+DECODE(SUM(RESP_8_SELECTED),NULL,0,1)+DECODE(SUM(RESP_9_SELECTED),NULL,0,1)+
           DECODE(SUM(RESP_10_SELECTED),NULL,0,1)),2)) MEAN,          
           SUM(DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+      
           DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+      
           DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+  
           DECODE(RESP_10_SELECTED,NULL,0,1)) TOT_X     
           FROM pivot_set   
           PIVOT (SUM(eligible_responses) AS SELECTED FOR sequence_num IN   
           ( 1 AS resp_1,       
           2 AS resp_2,     
           3 AS resp_3,     
           4 AS resp_4,     
           5 AS resp_5,     
           6 AS resp_6,     
           7 AS resp_7,     
           8 AS resp_8,     
           9 AS resp_9,     
           10 AS resp_10))t group by ELIGIBLE_PARTICIPANTS);
             
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
 END prc_getSurveyAnalysisByQues; 
/* CHARTS */
/* getSurveyStandardDeviationBarResults */
 PROCEDURE prc_getSurveyStandardDeviation(
      p_in_languageCode        IN     VARCHAR,
      p_in_minimumResponse     IN     NUMBER,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getSurveyStandardDeviation' ;
      v_stage                VARCHAR2 (500);
      /* getSurveyStandardDeviationBar */
 BEGIN
      v_stage   := 'getSurveyStandardDeviationBar';
      
   DELETE temp_table_session;  
   INSERT INTO temp_table_session (asset_code, cms_name, cms_code)
   SELECT asset_code,cms_value,key FROM vw_cms_asset_value where key in ('QUESTION_RESPONSE','QUESTION_NAME') and locale = p_in_languageCode;
     
      OPEN p_out_data FOR
 SELECT NODE_ID,       
            NODE_NAME,    
            MEAN,
            DECODE (TOT_AVERAGE,1,0,0,0,(ROUND(SQRT((POWER(DECODE(RESP_1_SELECTED,0,0,(RESP_1_SELECTED-MEAN)),2)
                                                                                    + POWER(DECODE(RESP_2_SELECTED,0,0,(RESP_2_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_3_SELECTED,0,0,(RESP_3_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_4_SELECTED,0,0,(RESP_4_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_5_SELECTED,0,0,(RESP_5_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_6_SELECTED,0,0,(RESP_6_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_7_SELECTED,0,0,(RESP_7_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_8_SELECTED,0,0,(RESP_8_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_9_SELECTED,0,0,(RESP_9_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_10_SELECTED,0,0,(RESP_10_SELECTED-MEAN)),2)
                                                                                   )/(TOT_AVERAGE-1)
           ),2))) STANDARD_DEVIATION FROM(        
            WITH pivot_set AS        
            ( SELECT SUM(NVL(rpc.eligible_participants,0)) ELIGIBLE_PARTICIPANTS         
            ,SUM(NVL(rpc.surveys_taken,0))  SURVEYS_TAKEN        
            ,rh.node_id AS node_id       
            ,rh.node_name node_name          
            ,resp.eligible_responses eligible_responses          
            ,resp.sequence_num           
            FROM         
            (SELECT rh.node_id AS node_id,       
            rh.node_name node_name       
            FROM rpt_hierarchy rh        
            WHERE NVL(parent_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))          
            ) rh,        
            (SELECT rh.node_id AS node_id        
            ,SUM(rpt.ELIGIBLE_SURVEYS) ELIGIBLE_PARTICIPANTS         
            ,SUM(rpt.SURVEYS_TAKEN)  surveys_taken       
            FROM  rpt_survey_summary rpt,        
            rpt_hierarchy rh,        
            promotion p          
            WHERE  rpt.promotion_id    = p.promotion_id          
            AND detail_node_id      = rh.node_id             
           AND rpt.record_type LIKE '%node%'            
           AND NVL(header_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))           
           AND rpt.promotion_id = p_in_promotionId              
           AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))           
           GROUP BY rh.node_id) rpc,        
           (SELECT node_id,         
           resp.response,           
           resp.sequence_num,           
           respo.eligible_responses         
           FROM         
           ( SELECT RESPONSE, ROWNUM sequence_num   
           FROM (SELECT INITCAP(response) RESPONSE,COUNT(NO_SURVEY_RESPONSES) NO_SURVEY_RESPONSES   
           FROM (SELECT 
--           lower(fnc_cms_asset_code_val_extr(sqr.cm_asset_name,'QUESTION_RESPONSE',p_in_languageCode)) response,   --09/29/2014
           lower((SELECT cms_name FROM temp_table_session where asset_code=sqr.cm_asset_name and  cms_code='QUESTION_RESPONSE' )) response,   
           PARTICIPANT_SURVEY_RESPONSE_ID NO_SURVEY_RESPONSES       
           FROM survey_question_response sqr, participant_survey_response psr,      
           survey_question sq,      
           promo_survey ps      
           WHERE ps.promotion_id = p_in_promotionId     
           AND psr.survey_question_response_id(+) = sqr.survey_question_response_id     
           AND ps.survey_id = sq.survey_id      
           AND psr.survey_question_response_id IS NOT NULL    
           AND sq.survey_question_id = sqr.survey_question_id)  
           GROUP BY INITCAP(response) ORDER BY  NO_SURVEY_RESPONSES desc)) resp,        
           (SELECT  rh.node_id node_id          
--           ,INITCAP(FNC_CMS_ASSET_CODE_VALUE( rpt.SURVEY_QUESTION_RESPONSE)) survey_question_response  --09/29/2014
           ,INITCAP(cm_qr.cms_name) survey_question_response           
           ,SUM(rpt.eligible_responses) eligible_responses          
           FROM rpt_survey_response_summary rpt,        
           rpt_hierarchy rh,            
           promotion p          
           ,(SELECT asset_code,cms_name FROM temp_table_session where cms_code = 'QUESTION_RESPONSE' ) cm_qr
           WHERE rpt.promotion_id    = p.promotion_id           
           AND detail_node_id      = rh.node_id             
           AND rpt.SURVEY_QUESTION_RESPONSE =  cm_qr.asset_code
           AND rpt.record_type LIKE '%node%'            
           AND NVL(header_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
           AND EXISTS (SELECT * FROM rpt_survey_minimum_response WHERE promotion_id=P.promotion_id AND node_id = rh.node_id AND is_leaf=0)            
           AND rpt.promotion_id = p_in_promotionId              
           AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))           
           GROUP BY rh.node_id 
--           ,FNC_CMS_ASSET_CODE_VALUE( rpt.SURVEY_QUESTION_RESPONSE)  --09/29/2014
           ,INITCAP(cm_qr.cms_name)         
           order by survey_question_response)  respo                
           WHERE respo.survey_question_response = resp.response) resp           
           WHERE rh.node_id             = rpc.node_id (+)           
           AND rh.node_id             = resp.node_id(+)         
           GROUP BY rh.node_id,rh.node_name,resp.eligible_responses,resp.response, resp.sequence_num        
           ORDER BY rh.node_name ,  resp.sequence_num                       
           )        
           SELECT  NODE_ID,         
           NODE_NAME,           
           ELIGIBLE_PARTICIPANTS,           
           SURVEYS_TAKEN,           
           DECODE(NVL(ELIGIBLE_PARTICIPANTS,0),0,0,ROUND((SURVEYS_TAKEN/ELIGIBLE_PARTICIPANTS)*100,2)) PERC_SURVEY_TAKEN,                   
           NVL(RESP_1_SELECTED,0) RESP_1_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_1_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_1_SELECTED,           
           NVL(RESP_2_SELECTED,0) RESP_2_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_2_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_2_SELECTED,           
           NVL(RESP_3_SELECTED,0) RESP_3_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_3_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_3_SELECTED,           
           NVL(RESP_4_SELECTED,0) RESP_4_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_4_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_4_SELECTED,           
           NVL(RESP_5_SELECTED,0) RESP_5_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_5_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_5_SELECTED,           
           NVL(RESP_6_SELECTED,0) RESP_6_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_6_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_6_SELECTED,           
           NVL(RESP_7_SELECTED,0) RESP_7_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_7_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_7_SELECTED,           
           NVL(RESP_8_SELECTED,0) RESP_8_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_8_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_8_SELECTED,           
           NVL(RESP_9_SELECTED,0) RESP_9_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_9_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_9_SELECTED,       
           NVL(RESP_10_SELECTED,0) RESP_10_SELECTED,            
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_10_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_10_SELECTED,             
           (DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+         
           DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+          
           DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+DECODE(RESP_10_SELECTED,NULL,0,1)) TOT_AVERAGE,
           DECODE(
           (DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+         
           DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+          
           DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+
           DECODE(RESP_10_SELECTED,NULL,0,1)),0,0,ROUND((NVL(RESP_1_SELECTED,0)+NVL(RESP_2_SELECTED,0)+NVL(RESP_3_SELECTED,0)+NVL(RESP_4_SELECTED,0)+NVL(RESP_5_SELECTED,0)+NVL(RESP_6_SELECTED,0)+NVL(RESP_7_SELECTED,0)+NVL(RESP_8_SELECTED,0)+NVL(RESP_9_SELECTED,0)+NVL(RESP_10_SELECTED,0))/  (DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+         
           DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+          
           DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+DECODE(RESP_10_SELECTED,NULL,0,1)),2)) MEAN            
           FROM pivot_set PIVOT (SUM(eligible_responses) AS selected FOR sequence_num IN (              
           1 AS resp_1,         
           2 AS resp_2,         
           3 AS resp_3,         
           4 AS resp_4,         
           5 AS resp_5,         
           6 AS resp_6,         
           7 AS resp_7,         
           8 AS resp_8,         
           9 AS resp_9,         
           10 AS resp_10))t)        
           UNION ALL    
           SELECT  NODE_ID,         
           NODE_NAME,               
           MEAN,
           DECODE (TOT_AVERAGE,1,0,0,0,(ROUND(SQRT((POWER(DECODE(RESP_1_SELECTED,0,0,(RESP_1_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_2_SELECTED,0,0,(RESP_2_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_3_SELECTED,0,0,(RESP_3_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_4_SELECTED,0,0,(RESP_4_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_5_SELECTED,0,0,(RESP_5_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_6_SELECTED,0,0,(RESP_6_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_7_SELECTED,0,0,(RESP_7_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_8_SELECTED,0,0,(RESP_8_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_9_SELECTED,0,0,(RESP_9_SELECTED-MEAN)),2)
                                                                                   + POWER(DECODE(RESP_10_SELECTED,0,0,(RESP_10_SELECTED-MEAN)),2)
                                                                                   )/(TOT_AVERAGE-1)
           ),2))) STANDARD_DEVIATION FROM(                                                                            
           WITH pivot_set AS            
           ( SELECT SUM(NVL(rpc.eligible_participants,0)) ELIGIBLE_PARTICIPANTS         
           ,SUM(NVL(rpc.surveys_taken,0))  SURVEYS_TAKEN        
           ,rh.node_id AS node_id               
           ,rh.node_name node_name          
           ,resp.eligible_responses eligible_responses          
           ,resp.sequence_num           
           FROM         
           (SELECT rh.node_id AS node_id,           
           node_name ||' Team' node_name        
           FROM rpt_hierarchy rh        
           WHERE NVL(node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId)))               
           ) rh,            
           (SELECT rh.node_id AS node_id        
           ,SUM(rpt.ELIGIBLE_SURVEYS) ELIGIBLE_PARTICIPANTS         
           ,SUM(rpt.SURVEYS_TAKEN)  surveys_taken           
           FROM  rpt_survey_summary rpt,        
           rpt_hierarchy rh,            
           promotion p          
           WHERE  rpt.promotion_id    = p.promotion_id              
           AND detail_node_id      = rh.node_id             
           AND rpt.record_type LIKE '%team%'            
           AND NVL(detail_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))           
           AND rpt.promotion_id = p_in_promotionId              
           AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))           
           GROUP BY rh.node_id) rpc,        
           (SELECT node_id,         
           resp.response,           
           resp.sequence_num,           
           respo.eligible_responses         
           FROM         
           ( SELECT RESPONSE, ROWNUM sequence_num   
           FROM (SELECT INITCAP(response) RESPONSE,COUNT(NO_SURVEY_RESPONSES) NO_SURVEY_RESPONSES   
           FROM (SELECT 
--           lower(fnc_cms_asset_code_val_extr(sqr.cm_asset_name,'QUESTION_RESPONSE',p_in_languageCode)) response,  --09/29/2014
             lower((SELECT cms_name FROM temp_table_session where asset_code=sqr.cm_asset_name and  cms_code='QUESTION_RESPONSE' )) response,   
           PARTICIPANT_SURVEY_RESPONSE_ID NO_SURVEY_RESPONSES       
           FROM survey_question_response sqr, participant_survey_response psr,      
           survey_question sq,      
           promo_survey ps      
           WHERE ps.promotion_id = p_in_promotionId     
           AND psr.survey_question_response_id(+) = sqr.survey_question_response_id     
           AND ps.survey_id = sq.survey_id      
           AND psr.survey_question_response_id IS NOT NULL    
           AND sq.survey_question_id = sqr.survey_question_id)  
           GROUP BY INITCAP(response) ORDER BY  NO_SURVEY_RESPONSES desc)) resp,        
           (SELECT  rh.node_id node_id          
--           ,INITCAP(FNC_CMS_ASSET_CODE_VALUE( rpt.SURVEY_QUESTION_RESPONSE)) survey_question_response  --09/29/2014
           ,INITCAP(cm_qr.cms_name) survey_question_response                    
           ,SUM(rpt.eligible_responses) eligible_responses          
           FROM rpt_survey_response_summary rpt,        
           rpt_hierarchy rh,            
           promotion p          
          ,(SELECT asset_code,cms_name FROM temp_table_session where cms_code = 'QUESTION_RESPONSE' ) cm_qr           
           WHERE rpt.promotion_id    = p.promotion_id           
           AND detail_node_id      = rh.node_id             
           AND rpt.SURVEY_QUESTION_RESPONSE =  cm_qr.asset_code
           AND rpt.record_type LIKE '%team%'            
           AND NVL(detail_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
           AND EXISTS (SELECT * FROM rpt_survey_minimum_response WHERE promotion_id=P.promotion_id AND node_id = rh.node_id AND is_leaf=1)           
           AND rpt.promotion_id = p_in_promotionId              
           AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))           
           GROUP BY rh.node_id 
--           ,FNC_CMS_ASSET_CODE_VALUE( rpt.SURVEY_QUESTION_RESPONSE)   --09/29/2014
           ,INITCAP(cm_qr.cms_name)         
           order by survey_question_response)  respo                
           WHERE respo.survey_question_response = resp.response) resp           
           WHERE rh.node_id             = rpc.node_id (+)           
           AND rh.node_id             = resp.node_id(+)         
           GROUP BY rh.node_id,rh.node_name,resp.eligible_responses,resp.response, resp.sequence_num        
           ORDER BY rh.node_name ,  resp.sequence_num                       
           )        
           SELECT  NODE_ID,         
           NODE_NAME,           
           ELIGIBLE_PARTICIPANTS,           
           SURVEYS_TAKEN,           
           DECODE(NVL(ELIGIBLE_PARTICIPANTS,0),0,0,ROUND((SURVEYS_TAKEN/ELIGIBLE_PARTICIPANTS)*100,2)) PERC_SURVEY_TAKEN,                   
           NVL(RESP_1_SELECTED,0) RESP_1_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_1_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_1_SELECTED,           
           NVL(RESP_2_SELECTED,0) RESP_2_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_2_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_2_SELECTED,           
           NVL(RESP_3_SELECTED,0) RESP_3_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_3_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_3_SELECTED,           
           NVL(RESP_4_SELECTED,0) RESP_4_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_4_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_4_SELECTED,           
           NVL(RESP_5_SELECTED,0) RESP_5_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_5_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_5_SELECTED,           
           NVL(RESP_6_SELECTED,0) RESP_6_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_6_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_6_SELECTED,           
           NVL(RESP_7_SELECTED,0) RESP_7_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_7_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_7_SELECTED,           
           NVL(RESP_8_SELECTED,0) RESP_8_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_8_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_8_SELECTED,           
           NVL(RESP_9_SELECTED,0) RESP_9_SELECTED,          
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_9_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_9_SELECTED,       
           NVL(RESP_10_SELECTED,0) RESP_10_SELECTED,            
           DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_10_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_10_SELECTED,             
           (DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+         
           DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+          
           DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+DECODE(RESP_10_SELECTED,NULL,0,1)) TOT_AVERAGE,
           DECODE(
           (DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+         
            DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+          
            DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+
            DECODE(RESP_10_SELECTED,NULL,0,1)),0,0,ROUND((NVL(RESP_1_SELECTED,0)+NVL(RESP_2_SELECTED,0)+NVL(RESP_3_SELECTED,0)+NVL(RESP_4_SELECTED,0)+NVL(RESP_5_SELECTED,0)+NVL(RESP_6_SELECTED,0)+NVL(RESP_7_SELECTED,0)+NVL(RESP_8_SELECTED,0)+NVL(RESP_9_SELECTED,0)+NVL(RESP_10_SELECTED,0))/  (DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+         
            DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+          
            DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+DECODE(RESP_10_SELECTED,NULL,0,1)),2)) MEAN            
            FROM pivot_set PIVOT (SUM(eligible_responses) AS selected FOR sequence_num IN (      
            1 AS resp_1,          
            2 AS resp_2,          
            3 AS resp_3,          
            4 AS resp_4,          
            5 AS resp_5,          
            6 AS resp_6,          
            7 AS resp_7,          
            8 AS resp_8,          
            9 AS resp_9,          
            10 AS resp_10))t);
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
 END prc_getSurveyStandardDeviation;
  /* getSurveyTotalResponsePercentBarResults */
PROCEDURE prc_getSurveyTotalResponseBar(
      p_in_languageCode        IN     VARCHAR,
      p_in_minimumResponse     IN     NUMBER,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getSurveyTotalResponseBar' ;
      v_stage                VARCHAR2 (500);
      /* getSurveyTotalResponsePercentBar */
 BEGIN
      v_stage   := 'getSurveyTotalResponsePercentBar';

   DELETE temp_table_session; 
   INSERT INTO temp_table_session (asset_code, cms_name, cms_code)
    SELECT asset_code,cms_value,key FROM vw_cms_asset_value where key in ('QUESTION_RESPONSE','QUESTION_NAME') and locale = p_in_languageCode;
      
      OPEN p_out_data FOR
 SELECT NODE_ID,       
            NODE_NAME,    
            PERC_RESP_1_SELECTED,     
            PERC_RESP_2_SELECTED,     
            PERC_RESP_3_SELECTED,     
            PERC_RESP_4_SELECTED,     
            PERC_RESP_5_SELECTED,     
            PERC_RESP_6_SELECTED,     
            PERC_RESP_7_SELECTED,     
            PERC_RESP_8_SELECTED,     
            PERC_RESP_9_SELECTED,     
            PERC_RESP_10_SELECTED     
            FROM(         
            WITH pivot_set AS         
            ( SELECT SUM(NVL(rpc.eligible_participants,0)) ELIGIBLE_PARTICIPANTS          
            ,SUM(NVL(rpc.surveys_taken,0))  SURVEYS_TAKEN         
            ,rh.node_id AS node_id        
            ,rh.node_name node_name           
            ,resp.eligible_responses eligible_responses           
            ,resp.sequence_num            
            FROM          
            (SELECT rh.node_id AS node_id,        
            rh.node_name node_name        
            FROM rpt_hierarchy rh         
            WHERE NVL(parent_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))           
            ) rh,         
            (SELECT rh.node_id AS node_id         
            ,SUM(rpt.ELIGIBLE_SURVEYS) ELIGIBLE_PARTICIPANTS          
            ,SUM(rpt.SURVEYS_TAKEN)  surveys_taken        
            FROM  rpt_survey_summary rpt,         
            rpt_hierarchy rh,         
            promotion p           
            WHERE  rpt.promotion_id    = p.promotion_id           
            AND detail_node_id      = rh.node_id              
            AND rpt.record_type LIKE '%node%'             
            AND NVL(header_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))            
            AND rpt.promotion_id = p_in_promotionId               
            AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))            
            GROUP BY rh.node_id) rpc,         
            (SELECT node_id,          
            resp.response,            
            resp.sequence_num,            
            respo.eligible_responses          
            FROM          
            ( SELECT RESPONSE, ROWNUM sequence_num    
            FROM (SELECT INITCAP(response) RESPONSE,COUNT(NO_SURVEY_RESPONSES) NO_SURVEY_RESPONSES    
            FROM (SELECT 
--            lower(fnc_cms_asset_code_val_extr(sqr.cm_asset_name,'QUESTION_RESPONSE',p_in_languageCode)) response,  --09/29/2014
            lower((SELECT cms_name FROM temp_table_session where asset_code=sqr.cm_asset_name and  cms_code='QUESTION_RESPONSE' )) response,    
            PARTICIPANT_SURVEY_RESPONSE_ID NO_SURVEY_RESPONSES        
            FROM survey_question_response sqr, participant_survey_response psr,       
            survey_question sq,       
            promo_survey ps       
            WHERE ps.promotion_id = p_in_promotionId      
            AND psr.survey_question_response_id(+) = sqr.survey_question_response_id      
            AND ps.survey_id = sq.survey_id       
            AND psr.survey_question_response_id IS NOT NULL     
            AND sq.survey_question_id = sqr.survey_question_id)   
            GROUP BY INITCAP(response) ORDER BY  NO_SURVEY_RESPONSES desc)) resp,         
            (SELECT  rh.node_id node_id           
--            ,INITCAP(FNC_CMS_ASSET_CODE_VALUE( rpt.SURVEY_QUESTION_RESPONSE)) survey_question_response   --09/29/2014
            ,INITCAP(cm_qr.cms_name) survey_question_response            
            ,SUM(rpt.eligible_responses) eligible_responses           
            FROM rpt_survey_response_summary rpt,         
            rpt_hierarchy rh,             
            promotion p           
          ,(SELECT asset_code,cms_name FROM temp_table_session where cms_code = 'QUESTION_RESPONSE' ) cm_qr            
            WHERE rpt.promotion_id    = p.promotion_id            
            AND detail_node_id      = rh.node_id              
            AND rpt.SURVEY_QUESTION_RESPONSE =  cm_qr.asset_code
            AND rpt.record_type LIKE '%node%'             
            AND NVL(header_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
            AND EXISTS (SELECT * FROM rpt_survey_minimum_response WHERE promotion_id=P.promotion_id AND node_id = rh.node_id AND is_leaf=0)                    
            AND rpt.promotion_id = p_in_promotionId               
            AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))            
            GROUP BY rh.node_id 
--            ,FNC_CMS_ASSET_CODE_VALUE( rpt.SURVEY_QUESTION_RESPONSE)  --09/29/2014
           ,INITCAP(cm_qr.cms_name)          
            order by survey_question_response)  respo                 
            WHERE respo.survey_question_response = resp.response) resp            
            WHERE rh.node_id             = rpc.node_id (+)            
            AND rh.node_id             = resp.node_id(+)          
            GROUP BY rh.node_id,rh.node_name,resp.eligible_responses,resp.response, resp.sequence_num         
            ORDER BY rh.node_name ,  resp.sequence_num                        
            )         
            SELECT  NODE_ID,          
            NODE_NAME,            
            ELIGIBLE_PARTICIPANTS,            
            SURVEYS_TAKEN,            
            DECODE(NVL(ELIGIBLE_PARTICIPANTS,0),0,0,ROUND((SURVEYS_TAKEN/ELIGIBLE_PARTICIPANTS)*100,2)) PERC_SURVEY_TAKEN,                    
            NVL(RESP_1_SELECTED,0) RESP_1_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_1_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_1_SELECTED,            
            NVL(RESP_2_SELECTED,0) RESP_2_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_2_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_2_SELECTED,            
            NVL(RESP_3_SELECTED,0) RESP_3_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_3_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_3_SELECTED,            
            NVL(RESP_4_SELECTED,0) RESP_4_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_4_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_4_SELECTED,            
            NVL(RESP_5_SELECTED,0) RESP_5_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_5_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_5_SELECTED,            
            NVL(RESP_6_SELECTED,0) RESP_6_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_6_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_6_SELECTED,            
            NVL(RESP_7_SELECTED,0) RESP_7_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_7_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_7_SELECTED,            
            NVL(RESP_8_SELECTED,0) RESP_8_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_8_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_8_SELECTED,            
            NVL(RESP_9_SELECTED,0) RESP_9_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_9_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_9_SELECTED,        
            NVL(RESP_10_SELECTED,0) RESP_10_SELECTED,             
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_10_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_10_SELECTED,              
            (DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+          
            DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+           
            DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+DECODE(RESP_10_SELECTED,NULL,0,1)) TOT_AVERAGE             
            FROM pivot_set PIVOT (SUM(eligible_responses) AS selected FOR sequence_num IN (               
            1 AS resp_1,          
            2 AS resp_2,          
            3 AS resp_3,          
            4 AS resp_4,          
            5 AS resp_5,          
            6 AS resp_6,          
            7 AS resp_7,          
            8 AS resp_8,          
            9 AS resp_9,          
            10 AS resp_10))t)         
            UNION ALL     
            SELECT NODE_ID,       
            NODE_NAME,    
            PERC_RESP_1_SELECTED,     
            PERC_RESP_2_SELECTED,     
            PERC_RESP_3_SELECTED,     
            PERC_RESP_4_SELECTED,     
            PERC_RESP_5_SELECTED,     
            PERC_RESP_6_SELECTED,     
            PERC_RESP_7_SELECTED,     
            PERC_RESP_8_SELECTED,     
            PERC_RESP_9_SELECTED,     
            PERC_RESP_10_SELECTED     
            FROM(                                                                             
            WITH pivot_set AS             
            ( SELECT SUM(NVL(rpc.eligible_participants,0)) ELIGIBLE_PARTICIPANTS          
            ,SUM(NVL(rpc.surveys_taken,0))  SURVEYS_TAKEN         
            ,rh.node_id AS node_id                
            ,rh.node_name node_name           
            ,resp.eligible_responses eligible_responses           
            ,resp.sequence_num            
            FROM          
            (SELECT rh.node_id AS node_id,            
            node_name ||' Team' node_name         
            FROM rpt_hierarchy rh         
            WHERE NVL(node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId)))                
            ) rh,             
            (SELECT rh.node_id AS node_id,         
            SUM(rpt.ELIGIBLE_SURVEYS) ELIGIBLE_PARTICIPANTS, 
            SUM(rpt.SURVEYS_TAKEN) surveys_taken            
            FROM  rpt_survey_summary rpt,         
            rpt_hierarchy rh,             
            promotion p           
            WHERE  rpt.promotion_id    = p.promotion_id               
            AND detail_node_id      = rh.node_id              
            AND rpt.record_type LIKE '%team%'             
            AND NVL(detail_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))            
            AND rpt.promotion_id = p_in_promotionId               
            AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))            
            GROUP BY rh.node_id) rpc,         
            (SELECT node_id,          
            resp.response,            
            resp.sequence_num,            
            respo.eligible_responses          
            FROM          
            ( SELECT RESPONSE, ROWNUM sequence_num    
            FROM (SELECT INITCAP(response) RESPONSE,COUNT(NO_SURVEY_RESPONSES) NO_SURVEY_RESPONSES    
            FROM (SELECT 
--            lower(fnc_cms_asset_code_val_extr(sqr.cm_asset_name,'QUESTION_RESPONSE',p_in_languageCode)) response,  --09/29/2014
            lower((SELECT cms_name FROM temp_table_session where asset_code=sqr.cm_asset_name and  cms_code='QUESTION_RESPONSE' )) response,    
            PARTICIPANT_SURVEY_RESPONSE_ID NO_SURVEY_RESPONSES        
            FROM survey_question_response sqr, participant_survey_response psr,       
            survey_question sq,       
            promo_survey ps       
            WHERE ps.promotion_id = p_in_promotionId      
            AND psr.survey_question_response_id(+) = sqr.survey_question_response_id      
            AND ps.survey_id = sq.survey_id       
            AND psr.survey_question_response_id IS NOT NULL     
            AND sq.survey_question_id = sqr.survey_question_id)   
            GROUP BY INITCAP(response) ORDER BY  NO_SURVEY_RESPONSES desc)) resp,         
            (SELECT  rh.node_id node_id           
--            ,INITCAP(FNC_CMS_ASSET_CODE_VALUE( rpt.SURVEY_QUESTION_RESPONSE)) survey_question_response   --09/29/2014
            ,INITCAP(cm_qr.cms_name) survey_question_response            
            ,SUM(rpt.eligible_responses) eligible_responses           
            FROM rpt_survey_response_summary rpt,         
            rpt_hierarchy rh,             
            promotion p 
           ,(SELECT asset_code,cms_name FROM temp_table_session where cms_code = 'QUESTION_RESPONSE' ) cm_qr                      
            WHERE rpt.promotion_id    = p.promotion_id            
            AND detail_node_id      = rh.node_id    
            AND rpt.SURVEY_QUESTION_RESPONSE =  cm_qr.asset_code                      
            AND rpt.record_type LIKE '%team%'             
            AND NVL(detail_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
            AND EXISTS (SELECT * FROM rpt_survey_minimum_response WHERE promotion_id=P.promotion_id AND node_id = rh.node_id AND is_leaf=1)                
            AND rpt.promotion_id = p_in_promotionId               
            AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))            
            GROUP BY rh.node_id 
--            ,FNC_CMS_ASSET_CODE_VALUE( rpt.SURVEY_QUESTION_RESPONSE)  --09/29/2014
           ,INITCAP(cm_qr.cms_name)          
            order by survey_question_response)  respo                 
            WHERE respo.survey_question_response = resp.response) resp            
            WHERE rh.node_id             = rpc.node_id (+)            
            AND rh.node_id             = resp.node_id(+)          
            GROUP BY rh.node_id,rh.node_name,resp.eligible_responses,resp.response, resp.sequence_num         
            ORDER BY rh.node_name ,  resp.sequence_num                        
            )         
            SELECT  NODE_ID,          
            NODE_NAME,            
            ELIGIBLE_PARTICIPANTS,            
            SURVEYS_TAKEN,            
            DECODE(NVL(ELIGIBLE_PARTICIPANTS,0),0,0,ROUND((SURVEYS_TAKEN/ELIGIBLE_PARTICIPANTS)*100,2)) PERC_SURVEY_TAKEN,                    
            NVL(RESP_1_SELECTED,0) RESP_1_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_1_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_1_SELECTED,            
            NVL(RESP_2_SELECTED,0) RESP_2_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_2_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_2_SELECTED,            
            NVL(RESP_3_SELECTED,0) RESP_3_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_3_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_3_SELECTED,            
            NVL(RESP_4_SELECTED,0) RESP_4_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_4_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_4_SELECTED,            
            NVL(RESP_5_SELECTED,0) RESP_5_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_5_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_5_SELECTED,            
            NVL(RESP_6_SELECTED,0) RESP_6_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_6_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_6_SELECTED,            
            NVL(RESP_7_SELECTED,0) RESP_7_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_7_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_7_SELECTED,            
            NVL(RESP_8_SELECTED,0) RESP_8_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_8_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_8_SELECTED,            
            NVL(RESP_9_SELECTED,0) RESP_9_SELECTED,           
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_9_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_9_SELECTED,        
            NVL(RESP_10_SELECTED,0) RESP_10_SELECTED,             
            DECODE(NVL(SURVEYS_TAKEN,0),0,0,ROUND((NVL(RESP_10_SELECTED,0)/SURVEYS_TAKEN)*100,2)) PERC_RESP_10_SELECTED,              
            (DECODE(RESP_1_SELECTED,NULL,0,1)+DECODE(RESP_2_SELECTED,NULL,0,1)+DECODE(RESP_3_SELECTED,NULL,0,1)+          
            DECODE(RESP_4_SELECTED,NULL,0,1)+DECODE(RESP_5_SELECTED,NULL,0,1)+DECODE(RESP_6_SELECTED,NULL,0,1)+           
            DECODE(RESP_7_SELECTED,NULL,0,1)+DECODE(RESP_8_SELECTED,NULL,0,1)+DECODE(RESP_9_SELECTED,NULL,0,1)+DECODE(RESP_10_SELECTED,NULL,0,1)) TOT_AVERAGE             
            FROM pivot_set PIVOT (SUM(eligible_responses) AS selected FOR sequence_num IN (       
            1 AS resp_1,          
            2 AS resp_2,          
            3 AS resp_3,          
            4 AS resp_4,          
            5 AS resp_5,          
            6 AS resp_6,          
            7 AS resp_7,          
            8 AS resp_8,          
            9 AS resp_9,          
            10 AS resp_10))t);      
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
 END prc_getSurveyTotalResponseBar;
  /* getSurveyResponseList */
PROCEDURE prc_getsurveyresponselist(
      p_in_languageCode        IN     VARCHAR,
      p_in_minimumResponse     IN     NUMBER,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getsurveyresponselist' ;
      v_stage                VARCHAR2 (500);
      /* getSurveyResponseList */
 -----------------------------------------------------------------------     
 -- Chidamba   09/13/2014    Bug#56377 - replaced QUESTION_NAME as QUESTION_RESPONSE, to lookup transalter content of response.
 -----------------------------------------------------------------------        
 BEGIN
 
 DELETE temp_table_session;
  INSERT INTO temp_table_session SELECT asset_code, cms_value, key FROM vw_cms_asset_value WHERE key = 'QUESTION_RESPONSE' AND locale = p_in_languageCode; --Bug#56377 - QUESTION_NAME

      v_stage   := 'getSurveyResponseList';
      OPEN p_out_data FOR
     SELECT *
FROM
  (SELECT (select cms_name from temp_table_session where asset_code=pe.response) response
  FROM
    (SELECT RANK() OVER (PARTITION BY pe.response ORDER BY pe.responseId,date_created) AS rec_rank,
      pe.*
    FROM
      (SELECT sqr.cm_asset_name response,
        sqr.survey_question_response_id responseId,
        psr.date_created
      FROM survey_question_response sqr,
        participant_survey_response psr,
        survey_question sq,
        promo_survey ps
      WHERE ps.promotion_id                  = p_in_promotionId
      AND psr.survey_question_response_id(+) = sqr.survey_question_response_id
      AND ps.survey_id                       = sq.survey_id
      AND psr.survey_question_response_id   IS NOT NULL
      AND sq.survey_question_id              = sqr.survey_question_id
      ) pe
    ) pe
  WHERE rec_rank=1
  ORDER BY responseId
  )
WHERE ROWNUM <= 10;      
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
 END prc_getsurveyresponselist;
END pkg_query_survey_reports;
/

