declare
 v_user         varchar2(100);
begin
 select user
   into v_user
   from dual;

   execute immediate 'CREATE OR REPLACE TRIGGER '||v_user||'_logon_trigger
                      AFTER LOGON ON DATABASE
                      BEGIN
                         IF ( user in ('''||v_user||''')) then
                            pkg_set_runtime_variable.prc_set_runtime_variable;
                         END IF;
                      END;';
end;
/

