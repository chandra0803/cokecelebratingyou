CREATE OR REPLACE FUNCTION fnc_getcharacteristic_id(p_in_name IN varchar2,p_in_nt_id IN NUMBER DEFAULT null, p_in_type IN VARCHAR2 DEFAULT 'NT' )
  return varchar2 is
  v_characteristic_id number(18);
  v_return            varchar2(20);

-- Chidamba      01/16/2012 Defect ID : 1508 - Fileload Participant-Update error message
--                           return N if no data found  

BEGIN
  begin
    select characteristic_id
      into v_characteristic_id
      from vue_characteristic_name
     where cm_name = upper(p_in_name)
       AND NVL(domain_id,1) = nvl(p_in_nt_id,1)
       and characteristic_type = p_in_type ;
    v_return := v_characteristic_id;
  exception
    when no_data_found then
      v_return := 'N';      --01/16/2012  --old - v_return := 'X'
    when others then
      v_return := 'X';
  END;
  return v_return;
end fnc_getcharacteristic_id;
/