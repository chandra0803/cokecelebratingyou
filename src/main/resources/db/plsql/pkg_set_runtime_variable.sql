CREATE OR REPLACE PACKAGE pkg_set_runtime_variable IS 

  PROCEDURE prc_set_runtime_variable;
  PROCEDURE prc_set_runtime_variable_temp;
  
END; -- End of Package Spec
/

CREATE OR REPLACE PACKAGE BODY pkg_set_runtime_variable IS 

  PROCEDURE set_variable(pi_context_name VARCHAR2, pi_variable_name VARCHAR2, pi_variable_value VARCHAR2)
  IS
  
  BEGIN
       dbms_session.set_context(pi_context_name,pi_variable_name, pi_variable_value);
  END;  
  PROCEDURE prc_set_runtime_variable
  IS
     cursor c_ext_tab IS
     SELECT * 
       FROM ext_tab
      ORDER BY seqno;

     v_variable_name     VARCHAR2(30);
     v_variable_value    EXT_TAB.DATA_VALUE%TYPE;
     v_context_name      VARCHAR2(30);

  BEGIN
     v_context_name := user||'_runtime_variable';
     IF sys_context(v_context_name,'variable1') is NULL THEN  
        FOR r_ext_tab IN c_ext_tab LOOP
           v_variable_name := 'variable'||r_ext_tab.seqno;
           v_variable_value := r_ext_tab.data_value;
           v_context_name   := user||'_runtime_variable';
           set_variable(v_context_name, v_variable_name, v_variable_value);
        END LOOP;
     END IF;        
  END;

  PROCEDURE prc_set_runtime_variable_temp
  IS
     cursor c_ext_tab_temp IS
     SELECT * 
       FROM ext_tab_temp
      ORDER BY seqno;

     v_variable_name     VARCHAR2(30);
     v_variable_value    EXT_TAB.DATA_VALUE%TYPE;
     v_context_name      VARCHAR2(30);

  BEGIN
     v_context_name := user||'_runtime_variable_temp';
     IF sys_context(v_context_name,'variable_temp1') IS NULL THEN
        FOR r_ext_tab_temp IN c_ext_tab_temp LOOP
           v_variable_name := 'variable_temp'||r_ext_tab_temp.seqno;
           v_variable_value := r_ext_tab_temp.data_value;
           v_context_name   := user||'_runtime_variable_temp';
           set_variable(v_context_name, v_variable_name, v_variable_value);
        END LOOP;
     END IF;        
  END;


END; -- End of Package Body  
/
