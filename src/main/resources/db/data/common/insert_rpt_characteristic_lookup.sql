INSERT INTO rpt_characteristic_lookup
(SELECT * FROM 
  (   SELECT characteristic_type,characteristic_id, cm_asset_code,CHARACTERISTIC_DATA_TYPE,
            row_number() over (partition by characteristic_type ORDER BY instr('dateintbooleantxtmulti_selectsingle_select',characteristic_data_type)) display_order
       FROM characteristic
  )
WHERE display_order <= 5   )   
/
commit
/