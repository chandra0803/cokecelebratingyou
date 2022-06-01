CREATE OR REPLACE FUNCTION fnc_getsubcategory_id(p_in_name IN VARCHAR2,p_in_category_name IN VARCHAR2)
  RETURN product_category.product_category_id%TYPE IS
  v_category_id product_category.product_category_id%TYPE;
  v_return      product_category.product_category_id%TYPE;
BEGIN

 BEGIN   
     SELECT product_category_id
  into v_category_id
      from product_category a
     where UPPER(a.product_category_name) = UPPER(p_in_name)
     AND parent_category_id = (select product_category_id from product_category where UPPER(product_category_name) = UPPER(p_in_category_name));
    v_return := v_category_id;
  exception
    when no_data_found then
      v_return := null;
    when others then
      v_return := null;
  END;
  return v_return;
end fnc_getsubcategory_id;
/