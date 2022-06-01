CREATE OR REPLACE PROCEDURE prc_rpt_repository_extract
 (p_in_promotion_id   IN  VARCHAR2,
  p_in_locale         IN  VARCHAR2,
  p_out_report_path   OUT VARCHAR2, --07/23/2014
  p_out_return_code   OUT NUMBER) IS
/**********************************************************
Purpose - this process will generate survey extract for the incoming promotion,
          and place the extract file at the report path.
       Date         Name            Description
    -----------   ------------    -----------------------
    08/26/2013     Chidamba       Initial Creation      
    05/07/2014     Ravi Dhanekula Fixed issue with directory.
    07/23/2014     nagarajs       Return the path name to java
    07/25/2014     nagarajs       Bug 53365 - export all response type data and add 
                                  new column called response sequence number 
    08/05/2014    Ravi Dhanekula  Bug # 54930.
    08/19/2014    nagarajs        Reverted back the bug 54930
    09/19/2014    Chidamba        Bug # 54930 - added retrun code '98' -> 'Survey Has No Response Yet' 
                                  and '97' -> 'Exceeded Response''s Maximum Limit'    
    10/3/2014     Ravi Dhanekula Bug # 57174.
***********************************************************/
    v_header                    VARCHAR2(4000);
    v_survey_header             VARCHAR2(4000);
    v_stage                     VARCHAR2(100);
    v_extract                   VARCHAR2(4000);
    list_column_count           VARCHAR2(32767);
    v_response_result           VARCHAR2(4000);
    l_query                     LONG ;
    v_column_count              NUMBER;
    v_client_name               VARCHAR2(40);
    v_flag_start                BOOLEAN := TRUE;
    l_cursor                    SYS_REFCURSOR;
    v_promotion_id              VARCHAR2(200);

    file_handler                UTL_FILE.file_type;
    v_file_name                 VARCHAR2(80) := 'GQ-CPSurvey-'||to_char(SYSDATE,'YYYYMMDD-HHMISS')||'.csv';
    v_file_location             VARCHAR2(1000);
    v_resonse_header            VARCHAR2(100);  

    C_pipe_limit                VARCHAR2(5) := ',';
    C_dbquotes_limit            VARCHAR2(5) := '"';

    directory_error             exception;
    process_error               exception;

BEGIN
 p_out_return_code :=  0;
 prc_execution_log_entry('PRC_GQ_SURVEY_EXTRACT',1,'INFO','Process Started for promotion '||p_in_promotion_id||' for locale '||p_in_locale
                             ,null);

  BEGIN
--       SELECT directory_name
--         INTO v_file_location
--         FROM all_directories
--        WHERE owner = 'SYS'
--          AND directory_path IN(SELECT '/work/wip/' || LOWER(SUBSTR( db_env, 6, (INSTR(db_env, '.') - 6))) || '/' ||LOWER(USER)||'/reports' AS wip_dir_path
--                                  FROM (SELECT UPPER(gn.global_name) || '.' AS db_env
--                                          FROM global_name gn));
       SELECT directory_name, --05/07/2014
              directory_path  --07/23/2014
         INTO v_file_location ,
              p_out_report_path  --07/23/2014      
         FROM all_directories
        WHERE owner = 'SYS'
          AND directory_path LIKE '%/work/wip/%reports'
          AND ROWNUM = 1;
          
  EXCEPTION
    WHEN OTHERS THEN
        RAISE directory_error;
  END;

 SELECT fnc_cms_asset_code_val_extr('report.gqcpsurvey.extract','RESPONSE',p_in_locale)
   INTO v_resonse_header
   FROM dual;

 v_stage := 'Client Name';
 v_client_name := fnc_get_system_variable( 'client.name', 'Client Name');

 v_stage := 'Get Count of Survey Response';
 SELECT MAX(TO_NUMBER(survey_response_seq))
   INTO v_column_count
   FROM (SELECT sq.sequence_num + 1 survey_response_seq --ROW_NUMBER() OVER (PARTITION BY pa.user_id ORDER BY NAME,pqs.promotion_id,pa.user_id)   --07/25/2014
           FROM survey s,
                participant_survey_response psr,
                participant_survey pa,
                survey_question sq,
                survey_question_response sqr,
                promo_goalquest_survey pqs
          WHERE pqs.promotion_id   = pa.promotion_id
            AND s.survey_id               = sq.survey_id
            AND sq.survey_question_id     = psr.survey_question_id
            AND sqr.survey_question_response_id (+) = psr.survey_question_response_id --07/25/2014 Added Outer Join
            AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND pqs.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))
            AND psr.participant_survey_id = pa.participant_survey_id);

  prc_execution_log_entry('PRC_GQ_SURVEY_EXTRACT',1,'INFO',v_column_count ,null);

  IF v_column_count IS NULL THEN        --09/19/2014
    v_stage:= 'Survey Has No Response Yet';
    p_out_return_code := 98;
    RAISE process_error;
  ELSIF v_column_count > 50 THEN     --09/19/2014
    p_out_return_code :=  97;
    v_stage:= 'Exceeded Response''s Maximum Limit';
   RAISE process_error;
  END IF;

   v_stage := 'Get Survey Response';
  SELECT LISTAGG(in_number,' ,') WITHIN GROUP( ORDER BY in_number) column_count
    INTO list_column_count
    FROM (SELECT level||' as Q'||level in_number FROM dual
          CONNECT BY LEVEL <= v_column_count) ;

  /*SELECT REPLACE(REPLACE(SUBSTR( column_count,1, INSTR( column_count,'||',-1)-6),',',''),'#',',') --07/25/2014 Commented
    INTO v_response_result
    FROM (SELECT LISTAGG('''"''||DECODE(NVL(survey_data.Q'||level||'_survey_response#''*'')#''*''#''''#FNC_CMS_ASSET_CODE_VAL_EXTR(survey_data.Q'||level||'_SURVEY_RESPONSE#''QUESTION_RESPONSE''#'''||p_in_locale||'''))||''"''||''#''||',',') WITHIN GROUP( ORDER BY level) column_count
            FROM dual
         CONNECT BY LEVEL <= v_column_count);*/
         
  v_stage := 'Get Response data';
  FOR rec IN (SELECT DISTINCT sq.sequence_num + 1 sequence_num,  --07/25/2014 Added
                     response_type 
                FROM survey s,
                    participant_survey_response psr,
                    participant_survey pa,
                    survey_question sq,
                    survey_question_response sqr,
                    promo_goalquest_survey pqs
              WHERE pqs.promotion_id   = pa.promotion_id
                AND pqs.survey_id = s.survey_id
                AND s.survey_id               = sq.survey_id
                AND sq.survey_question_id     = psr.survey_question_id
                AND sqr.survey_question_response_id (+)= psr.survey_question_response_id
                AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND pqs.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))
                AND psr.participant_survey_id = pa.participant_survey_id
               ORDER by 1) LOOP
               
      IF rec.response_type NOT IN ('openEnded','sliderSelection') THEN
        v_response_result :=  v_response_result||'''"''||DECODE(NVL(survey_data.Q'||rec.sequence_num||'_survey_response#''*'')#''*''#''''#FNC_CMS_ASSET_CODE_VAL_EXTR(survey_data.Q'||rec.sequence_num||'_SURVEY_RESPONSE#''QUESTION_RESPONSE''#'''||p_in_locale||'''))||''"''||''#''||'||
                              '''"''||Q'||rec.sequence_num||'_res_sequence_num||''"''||''#''||';
      ELSE
        v_response_result := v_response_result||'''"''||Q'||rec.sequence_num||'_survey_response||''"''||''#''||'||'''"''||Q'||rec.sequence_num||'_res_sequence_num||''"''||''#''||';
      END IF;
      
  END LOOP;
 
 v_stage := 'Seperate the response data';
 SELECT REPLACE(REPLACE(SUBSTR( v_response_result,1, INSTR( v_response_result,'||',-1)-6),',',''),'#',',')  --07/25/2014 Added
   INTO v_response_result
   FROM dual;
  
  v_stage := 'Get Survey header';
  SELECT LISTAGG(C_dbquotes_limit||v_resonse_header||' '||level||C_dbquotes_limit
                 ||','||C_dbquotes_limit||v_resonse_header||' '||level||' Seq Number'||C_dbquotes_limit --07/25/2014 Added
                 ,',') WITHIN GROUP( ORDER BY level) column_count
    INTO v_survey_header
    FROM dual
  CONNECT BY LEVEL <= v_column_count;

 v_stage := 'Get header';
 SELECT C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','CLIENT_NAME', p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','USER_NAME', p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','PARTICIPANT_FIRST_NAME', p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','EMAIL_ADDRESS', p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','BASELINE', p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','GOAL_SELECTION', p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','ACTUAL_PERFORMANCE', p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','GOAL_ACHIEVED', p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','PROMOTION_NAME', p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','PROMOTION_START_DATE' , p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','PROMOTION_END_DATE' , p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','GOAL_VALUE'  , p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','REWARD_VALUE' , p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','PERCENTAGE_OF_GOAL' , p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','REWARD_TYPE'  , p_in_locale)||C_dbquotes_limit||C_pipe_limit
      ||C_dbquotes_limit||FNC_CMS_ASSET_CODE_VAL_EXTR('report.gqcpsurvey.extract','SURVEY_NUMBER' , p_in_locale)||C_dbquotes_limit||C_pipe_limit||v_survey_header
 INTO v_header
 FROM dual;
--DBMS_OUTPUT.PUT_LINE(v_header);
    C_pipe_limit      := ''',''';
    C_dbquotes_limit  := '''"''';

  v_stage := 'Get Data';
  l_query :=
'SELECT '||C_dbquotes_limit||'||REPLACE('''||v_client_name||''','''','''')||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||au.user_name||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||au.first_name||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||ue.email_addr||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||rpt.base_quantity||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||fnc_cms_asset_code_val_extr(gl.goal_level_cm_asset_code,gl.goal_level_name_key,'''||p_in_locale||''')||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||rpt.current_value||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||rpt.achieved||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||p.promotion_name||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||p.promotion_start_date||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||p.promotion_end_date||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||gl.achievement_amount||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||rpt.award||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||ROUND(DECODE(NVL(gl.achievement_amount,0),0,0,(rpt.current_value/gl.achievement_amount)*100),2)||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||p.award_type||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       C_dbquotes_limit||'||survey_data.name||'||C_dbquotes_limit||'||'||C_pipe_limit||'||'||
       --C_dbquotes_limit||'||'||v_response_result||C_dbquotes_limit|| --Bug # 54930. --
       v_response_result|| --08/19/2014 Bug #53365
        'FROM application_user au,
       ( -- get a single pax goal record
                     SELECT gq_pg.user_id,
                            gq_pg.promotion_id,
                            gq_pg.goallevel_id,
                            -- a value in the journal table indicates goal achieved
                            DECODE( COUNT(j.journal_id), 0, ''N'', ''Y'') AS achieved,
                            SUM(J.TRANSACTION_AMT) award,
                            SUM(gq_pg.base_quantity) base_quantity,
                            SUM(gq_pg.current_value) current_value
                       FROM goalquest_paxgoal gq_pg,
                            journal j
                      WHERE gq_pg.promotion_id = j.promotion_id (+)
                        AND gq_pg.user_id = j.user_id (+)
                      GROUP BY gq_pg.user_id,
                            gq_pg.promotion_id,
                            gq_pg.goallevel_id
       ) rpt,
       user_email_address ue,
       goalquest_goallevel gl,
       promotion p,
       (WITH pivot_record as    
        (SELECT s.name,
                pa.user_id,
                DECODE(response_type,''openEnded'',open_end_response,''standardResponse'',sqr.cm_asset_name,slider_response)  survey_response, --sqr.cm_asset_name  --07/25/2014 
                ROW_NUMBER() OVER (PARTITION BY pa.user_id,s.name ORDER BY s.name,pqs.promotion_id,pa.user_id,sq.sequence_num)  survey_response_seq,
                sqr.sequence_num + 1 res_sequence_num  --07/25/2014 
          FROM survey s,
               participant_survey_response psr,
               participant_survey pa,
               survey_question sq,
               survey_question_response sqr,
               promo_goalquest_survey pqs
         WHERE pqs.promotion_id   = pa.promotion_id
           AND s.survey_id               = sq.survey_id
           AND sq.survey_question_id     = psr.survey_question_id
           AND sqr.survey_question_response_id (+)  = psr.survey_question_response_id  --07/25/2014 
           AND psr.participant_survey_id = pa.participant_survey_id
           AND (('''||p_in_promotion_id||'''IS NULL) OR ('''||p_in_promotion_id||''' is NOT NULL AND pqs.promotion_id IN (SELECT * FROM TABLE(get_array('''||p_in_promotion_id||'''))))))
        SELECT *
          FROM pivot_record
         PIVOT (MIN(Survey_response) Survey_response, MIN(res_sequence_num) AS res_sequence_num FOR (survey_response_seq)IN ('||list_column_count||'))) survey_data
WHERE rpt.user_id     = au.user_id(+)
  AND rpt.user_id     = ue.user_id
  AND ue.is_primary = 1
  AND rpt.goallevel_id = gl.goallevel_id
  AND gl.promotion_id = rpt.promotion_id
  AND p.promotion_id  = rpt.promotion_id
  AND survey_data.user_id = rpt.user_id
  AND (('''||p_in_promotion_id||''' IS NULL) OR ('''||p_in_promotion_id||''' is NOT NULL AND p.promotion_id IN (SELECT * FROM TABLE(get_array('''||p_in_promotion_id||''')))))';

 v_stage := 'Open Data';
 OPEN l_cursor FOR l_query;
  LOOP
     v_stage := 'Fetch Data';
     FETCH l_cursor INTO v_extract;
     EXIT WHEN l_cursor%NOTFOUND;
     IF v_flag_start THEN
         file_handler := UTL_FILE.fopen(v_file_location, v_file_name, 'w',4096);
         v_stage := 'Write Header';
         UTL_FILE.put_line(file_handler, v_header);
         v_stage := 'Write Data';
         UTL_FILE.put_line(file_handler, v_extract);
         v_flag_start:= FALSE;
       ELSE
         UTL_FILE.put_line(file_handler, v_extract);
       END IF;
  END LOOP;

  UTL_FILE.fclose(file_handler);
  
  prc_execution_log_entry('PRC_GQ_SURVEY_EXTRACT',1,'INFO','Extract File : '||v_file_name||' in path : '||v_file_location
                             ,null);

EXCEPTION
    WHEN directory_error THEN
     p_out_return_code :=  99 ;
     prc_execution_log_entry('PRC_GQ_SURVEY_EXTRACT',1,'ERROR','Directory Issue'
                             ,null);
   WHEN process_error THEN
     prc_execution_log_entry('PRC_GQ_SURVEY_EXTRACT',1,'ERROR',v_stage,null);
    WHEN OTHERS  THEN
        p_out_return_code :=  99 ;
        IF UTL_FILE.IS_OPEN(file_handler) THEN
          UTL_FILE.fclose(file_handler);
        END IF;
        prc_execution_log_entry('PRC_GQ_SURVEY_EXTRACT',1,'ERROR',v_stage||'-->'||SQLERRM
                             ,null);
END;
/