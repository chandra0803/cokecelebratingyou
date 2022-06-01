declare
 l_user         varchar2(100);
begin
 select user
   into l_user
   from dual;

   execute immediate 'CREATE TABLE ext_tab (
                             seqno       number(4),
                             data_value  VARCHAR2(99))
                             organization external
                             ( default directory '||l_user||'_EXTERNAL
                               access parameters
                              (RECORDS DELIMITED BY NEWLINE
                               NOBADFILE NODISCARDFILE NOLOGFILE
                               FIELDS TERMINATED BY ''|''
                              )
                              location (''external_file_1.txt'')  
                             )';



   execute immediate 'CREATE TABLE ext_tab_temp (
                             seqno       number(4),
                             data_value  VARCHAR2(99))
                             organization external
                             ( default directory '||l_user||'_EXTERNAL
                               access parameters
                               (RECORDS DELIMITED BY NEWLINE
                                NOBADFILE NODISCARDFILE NOLOGFILE
                                FIELDS TERMINATED BY ''|''
                               )
                               location (''external_file_1.txt'')  
                              )';

   execute immediate 'CREATE SEQUENCE ext_tab_sq
                      MINVALUE 1 INCREMENT BY 1';


end;
/