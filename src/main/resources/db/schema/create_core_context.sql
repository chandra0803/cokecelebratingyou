BEGIN
execute immediate 'create or replace context '||user||'_runtime_variable using pkg_set_runtime_variable';

execute immediate 'create or replace context '||user||'_runtime_variable_temp using pkg_set_runtime_variable';
END;
/