DECLARE
  CURSOR cur_comp IS
    SELECT  decode(OBJECT_TYPE,
                   'PACKAGE BODY', 'alter package ' || a.object_name || ' compile body',
                   'alter ' || a.object_type || ' ' || a.object_name || ' compile')||CHR(0) comp_stmt
    FROM    user_objects a,
            (SELECT MAX(LEVEL) dlevel, object_id
             FROM sys.public_dependency
             CONNECT BY object_id = PRIOR referenced_object_id
             START WITH referenced_object_id IN (
                SELECT object_id
                FROM user_objects
                WHERE object_type IN ('PACKAGE BODY', 'PACKAGE', 'FUNCTION', 'PROCEDURE', 'TRIGGER', 'VIEW'))
             GROUP BY object_id)  b
    WHERE   a.object_id = b.object_id(+)
    AND     a.object_type IN ('PACKAGE BODY', 'PACKAGE', 'FUNCTION', 'PROCEDURE', 'TRIGGER', 'VIEW')
    AND     a.object_name NOT LIKE 'BIN$%'
    ORDER BY b.dlevel DESC,
            a.object_type,
            a.object_name;
  v_statement VARCHAR2(250);
BEGIN
  FOR rec_comp IN cur_comp LOOP
    v_statement := rec_comp.comp_stmt;
    BEGIN
      EXECUTE IMMEDIATE v_statement;
    EXCEPTION
      WHEN OTHERS THEN
        NULL;
    END;
  END LOOP;
END;
/