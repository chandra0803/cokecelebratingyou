CREATE OR REPLACE FUNCTION fnc_get_manager_id
  (p_in_user_id          IN  application_user.user_id%TYPE,
   p_in_node_id          IN user_node.node_id%TYPE) 
   RETURN NUMBER IS

v_role USER_NODE.ROLE%TYPE;
p_out_user_id  NUMBER;
v_node_id  NUMBER;

 v_stage                       VARCHAR2(500);
BEGIN

v_node_id := p_in_node_id;

  LOOP
    p_out_user_id:=NULL;
      
    BEGIN
      v_stage := 'Get Owner user id for User: '||p_in_user_id;    
  
      SELECT role
        INTO v_role
        FROM user_node
       WHERE user_id = p_in_user_id
        AND node_id = p_in_node_id;
        
       END; 
BEGIN
         
       IF v_role IN ('mbr','mgr') then 
      SELECT ma.user_id
        INTO p_out_user_id          
      FROM  (SELECT au.user_id
        FROM user_node un,
             node n,
             application_user au
       WHERE un.node_id = n.node_id
         AND un.user_id = au.user_id
         AND un.status = 1
         AND au.is_active = 1
         AND un.role = 'own'
         AND un.user_id != p_in_user_id
         AND un.node_id = v_node_id
         ) ma;
         ELSE 
      SELECT ma.user_id
        INTO p_out_user_id
      FROM  (SELECT au.user_id
        FROM user_node un,
             node n,
             application_user au
       WHERE un.node_id = n.node_id
         AND un.user_id = au.user_id
         AND un.status = 1
         AND au.is_active = 1
         AND un.role = 'own'
         AND un.user_id != p_in_user_id
         AND un.node_id = v_node_id
         ) ma;
          END IF; 
        
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        BEGIN
         SELECT parent_node_id
           INTO v_node_id
           FROM node
          WHERE node_id = v_node_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
                 p_out_user_id := 0;
        END; 

    END;
    
    IF p_out_user_id IS NOT NULL THEN
      EXIT;
    END IF;  
    
 END LOOP;  

RETURN p_out_user_id;

EXCEPTION
  WHEN OTHERS THEN
    p_out_user_id := NULL;     
    
      prc_execution_log_entry('FNC_GET_MANAGER_ID',0,'ERROR','Function Failed  for User_id: '
                          ||p_in_user_id|| SQLERRM, null);     
                          RETURN p_out_user_id;
      
END fnc_get_manager_id;
/