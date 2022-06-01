CREATE OR REPLACE FUNCTION FNC_GET_MANAGER_NODE(p_in_cur_node IN NUMBER) RETURN NUMBER IS
v_mgr_node_id NUMBER;
/******************************************************************************
   NAME:       FNC_GET_MANAGER_NODE
   PURPOSE:   Get manager(ie) owner node for any given node. If there is no owner in current node
              check in levels above to find an owner else null 

 /******************************************************
   -- Sherif Basha  10/14/2016  Initial Creation
  ******************************************************/
BEGIN
    SELECT mgr_node INTO v_mgr_node_id 
      FROM (
            SELECT CASE WHEN MIN(lvl) OVER() = lvl THEN 
                        CASE WHEN cur_node_ownr_avlbl>0 THEN node_id ELSE parent_node_id END
                        ELSE NULL END mgr_node
              FROM (
                    SELECT level lvl,
                           (SELECT count(*) FROM user_node c WHERE c.node_id = a.node_id AND c.role = 'own') cur_node_ownr_avlbl,
                           (SELECT count(*) FROM user_node d WHERE d.node_id = a.parent_node_id AND d.role = 'own') par_node_ownr_avlbl,a.*
                     FROM node a
                     START WITH a.node_id = p_in_cur_node
                     CONNECT BY PRIOR a.parent_node_id = a.node_id)
             WHERE cur_node_ownr_avlbl > 0 OR par_node_ownr_avlbl > 0)
       WHERE mgr_node IS NOT NULL;
       
   RETURN v_mgr_node_id;
   EXCEPTION
     WHEN NO_DATA_FOUND THEN
      RETURN TO_NUMBER(NULL);
     WHEN OTHERS THEN
       -- Consider logging the error and then re-raise
      -- dbms_output.put_line(p_in_cur_node);
       RAISE;
END FNC_GET_MANAGER_NODE;
/
