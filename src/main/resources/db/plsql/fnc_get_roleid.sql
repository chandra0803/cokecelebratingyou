CREATE OR REPLACE 
FUNCTION fnc_get_roleid
  ( p_in_role_name  IN varchar2)
  RETURN  varchar2 IS
/*******************************************************************************
-- Person      Date        Comments
-- ---------   ------      -------------------------------------------       
   Raju N      09/29/2005  Creation
*******************************************************************************/
   v_role_id      varchar2(70) ;
BEGIN 
    select role_id
       into v_role_id
       from role r
    where upper(r.name) = upper(p_in_role_name) ;
    RETURN v_role_id ;
EXCEPTION
   WHEN others THEN
       v_role_id := 'X' ;
    RETURN v_role_id ;
END;
/

