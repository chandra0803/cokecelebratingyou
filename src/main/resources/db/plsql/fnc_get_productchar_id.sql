CREATE OR REPLACE 
FUNCTION fnc_get_productchar_id(p_in_name IN varchar2,
     p_in_product_id IN NUMBER DEFAULT null, p_in_type IN VARCHAR2 DEFAULT 'PRODUCT' )
  return varchar2 IS
/*******************************************************************************
-- Purpose: The product and characteristic associations are stored PRODUCT_CHARACTERISTIC
--          PRODUCT_CHARACTERISTIC as the same characteristic can be shared by
--          multiple products. The uniqueness of the Chracteristic name is checked
--          within the JAVA code. This function would retrive the ID for a given 
--          product characteristic name. Typically used in the import at staging 
--          step.
--
--  Author                Date        Comments
--  --------------------------------------------------------------------------
--  Raju N             08/03/2006     Creation
*******************************************************************************/  
  v_characteristic_id number(18);
  v_return            varchar2(20);
BEGIN
  begin
    select characteristic_id
      into v_characteristic_id
      from vue_characteristic_name
     where cm_name = upper(p_in_name)
       and characteristic_type = p_in_type
       AND characteristic_id IN 
          (SELECT characteristic_id
            FROM product_characteristic
           WHERE product_id = p_in_product_id ) ;
    v_return := v_characteristic_id;
  exception
    when no_data_found then
      v_return := 'X';
    when others then
      v_return := 'X';
  END;
  return v_return;
end fnc_get_productchar_id ;
/
