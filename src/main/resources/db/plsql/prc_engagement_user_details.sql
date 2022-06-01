CREATE OR REPLACE PROCEDURE prc_engagement_user_details(
p_in_mode            IN    VARCHAR2,
p_in_user_id         IN    NUMBER,
P_in_time_frame    IN VARCHAR2,
p_in_end_month         IN    NUMBER,
p_in_end_year        IN       NUMBER,
p_out_result_set     OUT   sys_refcursor) AS


v_user_code         user_node.node_id%type;
v_user_node_list    varchar2(200);
v_field_sep         varchar2(1) := ',';

v_as_of_date DATE;
v_company_goal NUMBER;
v_score_is_active NUMBER;

CURSOR cur_node_for_user (v_user_id NUMBER)
IS
SELECT node_id FROM user_node
WHERE user_id = v_user_id
    AND role IN ('own','mgr');

BEGIN
    prc_execution_log_entry('prc_engagement_user_details',1,'INFO','START',null);  
      
      BEGIN
      SELECT prev_process_date,company_goal,is_score_active INTO v_as_of_date,v_company_goal,v_score_is_active  FROM promo_engagement pe WHERE EXISTS 
      (SELECT * FROM promotion WHERE promotion_status = 'live' AND promotion_id = pe.promotion_id  );
      END;

    IF upper(p_in_mode) = upper('user') THEN
                                
                      OPEN p_out_result_set FOR
                                SELECT  v_as_of_date as_of_date,v_company_goal company_goal,v_score_is_active is_score_active,AVG(d.score) score,                                
                                  sum(d.received_count) received_count,sum(d.sent_count) sent_count,sum(d.connected_to_count) connected_to_count,
                                 sum(d.connected_from_count) connected_from_count,sum(d.login_activity_count) login_activity_count,
                                 sum(d.received_target) received_target,sum(d.sent_target) sent_target,sum(d.connected_target) connected_target,
                                 sum(d.connected_from_target) connected_from_target,sum(d.login_activity_target) login_activity_target                                   
                          FROM engagement_score_detail d 
                          WHERE d.user_id = p_in_user_id
                               AND d.time_frame = p_in_time_frame   
                               AND trans_month = p_in_end_month
                               AND trans_year = p_in_end_year;
        
        
    ELSIF upper(p_in_mode) = upper('team') THEN
        OPEN cur_node_for_user(p_in_user_id);
        LOOP
            FETCH cur_node_for_user INTO v_user_code ;
            EXIT WHEN cur_node_for_user%NOTFOUND;
            v_user_node_list := v_user_node_list || v_user_code || v_field_sep;
        END LOOP;
            v_user_node_list := substr(v_user_node_list,1,length(v_user_node_list)-length(v_field_sep)) ;
        
        OPEN p_out_result_set FOR        
        SELECT v_as_of_date as_of_date,v_company_goal company_goal,v_score_is_active is_score_active,SUM (d.score * d.total_participant_count) / SUM(d.total_participant_count) score ,sum(d.received_count) received_count,sum(d.sent_count) sent_count,sum(d.connected_to_count) connected_to_count,
                                 sum(d.connected_from_count) connected_from_count,sum(d.login_activity_count) login_activity_count,
                                 sum(d.received_target) received_target,sum(d.sent_target) sent_target,sum(d.connected_target) connected_target,
                                 sum(d.connected_from_target) connected_from_target,sum(d.login_activity_target) login_activity_target 
                          FROM engagement_score_summary d 
                          WHERE d.time_frame = p_in_time_frame   AND   d.record_type ='nodesum' AND d.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))
                                AND trans_month = p_in_end_month
                               AND trans_year = p_in_end_year; 
        
    END IF;
    prc_execution_log_entry('prc_engagement_user_details',1,'INFO','START',null);
    
EXCEPTION
WHEN OTHERS THEN
    prc_execution_log_entry('prc_engagement_user_details',1,'INFO',SQLERRM,null);  

END prc_engagement_user_details;
/