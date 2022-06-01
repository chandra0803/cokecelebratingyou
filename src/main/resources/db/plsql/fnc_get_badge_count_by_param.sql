CREATE OR REPLACE function fnc_get_badge_count_by_param
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
               and rad.node_id in (select child_node_id from rpt_hierarchy_rollup WHERE node_id = p_in_node_id);
               
               return p_count;
              END;
/
