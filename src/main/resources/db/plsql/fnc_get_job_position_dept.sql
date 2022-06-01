CREATE OR REPLACE FUNCTION fnc_get_job_position_dept
  ( p_user_id  NUMBER,
    p_delimeter VARCHAR2)
  RETURN  VARCHAR2  IS

  v_employer_count     NUMBER(10);
  v_job_position_department  VARCHAR2(200);

  BEGIN
  
     SELECT count(1)
       INTO v_employer_count   
       FROM participant_employer
      WHERE user_id = p_user_id
        AND (termination_date IS NULL OR termination_date >= trunc(SYSDATE));

     IF v_employer_count > 0 THEN         
        SELECT position_type||p_delimeter|| department_type
          INTO v_job_position_department
          FROM participant_employer
         WHERE user_id = p_user_id
           AND (termination_date IS NULL OR termination_date >= trunc(SYSDATE))
           AND ROWNUM = 1;
     ELSE
        BEGIN
          SELECT position_type||p_delimeter||department_type
            INTO v_job_position_department
            FROM participant_employer
           WHERE user_id = p_user_id
             AND participant_employer_index =(SELECT MAX (participant_employer_index)
                                          FROM participant_employer
                                          WHERE user_id = p_user_id);
        EXCEPTION WHEN OTHERS THEN
            v_job_position_department := NULL;
        END;
     END IF;
     
     RETURN v_job_position_department;      
  EXCEPTION
     WHEN OTHERS THEN
       prc_execution_log_entry('fnc_get_job_position_dept',1,'ERROR','Error getting Participant Employer info :'||SQLERRM,null);
       v_job_position_department := NULL;
       
       RETURN v_job_position_department;
  END ;
/
