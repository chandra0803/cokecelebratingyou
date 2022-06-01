CREATE OR REPLACE FUNCTION fnc_get_node_id
  (p_in_nodename     IN VARCHAR2,
   p_in_hierarchy_id IN NUMBER)
   RETURN  VARCHAR2 IS


-- MODIFICATION HISTORY
-- Person      Date        Comments
-- ---------   ---------   -----------------------------------------------------
-- Percy M.   04/05/06     Added criteria to return node_id of the primary heirarchy only
-- Arun S     04/23/2010   Added in param p_in_hierarchy_id to select node_id 
--                         for the given hierarchy

--------------------------------------------------------------------------------

   v_node_id             VARCHAR2(30);

BEGIN

  SELECT node_id
    INTO v_node_id
    FROM node
   WHERE lower(NAME)  = lower(p_in_nodename)
     AND hierarchy_id = p_in_hierarchy_id;  --(SELECT hierarchy_id
                                            --   FROM HIERARCHY
                                            --  WHERE is_primary = 1);
    RETURN v_node_id ;

EXCEPTION
  WHEN no_data_found THEN
    v_node_id := 'X' ;
    RETURN v_node_id ;
  
  WHEN others THEN
    v_node_id := 'X' ;
    RETURN v_node_id ;
END;
/
