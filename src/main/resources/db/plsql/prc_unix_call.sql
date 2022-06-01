CREATE OR REPLACE 
Procedure prc_unix_call (p_script_call IN varchar2,
                     p_return_code OUT integer) IS
/* *********************************************************************
  PRIME VERSION 3.0

  Procedure Name: prc_unix_call
  Project ID: 0015615
  Client: Test
  Purpose: Call function in another database which allows execution of a UNIX script from
           a form.
  Input Files:
  Output Files:


 MODIFICATION HISTORY
 Person      Date        Comments
 ---------   ----------  ------------------------------------------
 D Rahl      04/25/2000  Initial Creation
 M Glum      06/13/2001  Added lines of code use a function to read
                         a function that finds the database link from the
                         system_variable table and executes the dynamic
                         SQL call.
 Turnquist   08/08/2001  Corrected syntax of v_dynam_string. Added
                         parentheses and quotes around the variable p_script_call
************************************************************************/
v_dynam_string              VARCHAR2(5000);
v_link_string               VARCHAR2(1000);


BEGIN
  v_link_string := 'MS_COMMON' ;-- fnc_get_system_variable('DB_LINK','UNIX_HOST_CALL');

  IF v_link_string IS NOT NULL THEN
    v_dynam_string := 'SELECT funct_ora_unix_shell@'||v_link_string||'('''
                      ||p_script_call||''')'||' FROM dual';

    EXECUTE IMMEDIATE v_dynam_string INTO p_return_code;
  END IF;
END; -- Procedure prc_unix_call
/

