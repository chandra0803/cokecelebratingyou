create or replace FUNCTION fnc_biw_only_role(p_in_role IN VARCHAR2)
 RETURN INTEGER IS
 --Purpose: input role name is in the new system variable (roles.for.biw.only) return 1; otherwise, return 0
 -----------------------------------------------------------------------------------
 --Person         Date                      Comments
 --Chidamba     06/14/2017      Initial created [WIP#33565]
 v_retrun_val INTEGER; 
BEGIN
 SELECT 1
     INTO  v_retrun_val 
   FROM (SELECT REPLACE((REGEXP_SUBSTR (string_val,       --05/25/2017 starts
                                    '[^,]+',
                                    1,
                                    LEVEL)),'''') biw_roles
                FROM (SELECT string_val
                        FROM os_propertyset
                       WHERE entity_name = 'roles.for.biw.only')
          CONNECT BY LEVEL <=
                          LENGTH (string_val)
                        - LENGTH (REPLACE (string_val, ','))  + 1) 
                        WHERE  biw_roles =  p_in_role;
RETURN v_retrun_val;  
 EXCEPTION 
 WHEN NO_DATA_FOUND THEN
  RETURN 0;
END ;