CREATE OR REPLACE 
FUNCTION fnc_get_node_path
  ( p_in_node_id  NUMBER )
  RETURN  VARCHAR2  IS
--
-- MODIFICATION HISTORY
-- Person      Date        Comments
-- ---------   ------      -------------------------------------------
-- Raju N      07/06/2006  Creation

   v_path    node.path%TYPE ;
   -- Declare program variables as shown above
BEGIN 
  SELECT path
    INTO v_path
    FROM node
   WHERE node_id = p_in_node_id;
   RETURN v_path ;
EXCEPTION
   WHEN others THEN
       v_path := 'X' ;
       RETURN v_path ;
END;
/
