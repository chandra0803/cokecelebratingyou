DECLARE
   CURSOR C1 IS
      SELECT promotion_id,
             0 sweeps_billcode,
             lower(track_bills_by) track_bill_by,
             primary_bill_code bill_code,
             0 sort_order,
             primary_custom_value custom_value
        FROM promotion p
       WHERE     bills_active = 1  AND   primary_bill_code IS NOT NULL 
             AND NOT EXISTS
                    (SELECT *
                       FROM promo_bill_code
                      WHERE promotion_id = p.promotion_id
                        and sweeps_billcode = 0
                        and sort_order = 0) -- primary is always 0
      UNION ALL
      SELECT promotion_id,
             0 sweeps_billcode,
             lower(track_bills_by) track_bill_by,
             secondary_bill_code bill_code,
             1 sort_order,
             secondary_custom_value custom_value
        FROM promotion p
       WHERE     bills_active = 1 AND secondary_bill_code IS NOT NULL 
       AND secondary_bill_code <>'notApplicable'
       AND ((secondary_bill_code = 'customValue' 
       AND secondary_custom_value IS NOT NULL) 
       OR secondary_bill_code <> 'customValue')
             AND NOT EXISTS
                    (SELECT *
                       FROM promo_bill_code
                      WHERE promotion_id = p.promotion_id
                        and sweeps_billcode = 0
                        and sort_order = 1) -- secondary is always 1
      UNION ALL
      SELECT promotion_id,
             1 sweeps_billcode,
             lower(track_bills_by) track_bill_by,
             swp_primary_bill_code bill_code,
             0 sort_order,
             swp_primary_custom_value custom_value
        FROM promotion p
       WHERE     swp_bills_active = 1 AND swp_primary_bill_code IS NOT NULL
             AND NOT EXISTS
                    (SELECT *
                       FROM promo_bill_code
                      WHERE promotion_id = p.promotion_id
                        and sweeps_billcode = 1
                        and sort_order = 0) -- primary is always 0
      UNION ALL
      SELECT promotion_id,
             1 sweeps_billcode,
             lower(track_bills_by) track_bill_by,
             swp_secondary_bill_code bill_code,
             1 sort_order,
             swp_secondary_custom_value custom_value
        FROM promotion p
       WHERE     swp_bills_active = 1 
       AND swp_secondary_bill_code IS NOT NULL AND swp_secondary_bill_code <>'notApplicable'
       AND ((swp_secondary_bill_code = 'customValue' 
       AND swp_secondary_custom_value IS NOT NULL) 
       OR swp_secondary_bill_code <> 'customValue')
             AND NOT EXISTS
                    (SELECT *
                       FROM promo_bill_code
                      WHERE promotion_id = p.promotion_id
                        and sweeps_billcode = 1
                        and sort_order = 1) -- secondary is always 1
      order by 1,2;

BEGIN

   FOR C1_R IN C1  LOOP

      INSERT INTO promo_bill_code (PROMO_BILL_CODE_ID,
                                   promotion_id,
                                   SWEEPS_BILLCODE,
                                   TRACK_BILLS_BY,
                                   BILL_CODE,
                                   SORT_ORDER,
                                   CUSTOM_VALUE,
                                   DATE_CREATED,
                                   CREATED_BY,
                                   VERSION)

           VALUES (promo_bill_code_pk_sq.NEXTVAL,
                   c1_r.promotion_id,
                   c1_r.sweeps_billcode,
                   c1_r.track_bill_by,
                   c1_r.bill_code,
                   c1_r.sort_order,
                   C1_R.custom_value,
                   SYSDATE,
                   5662,
                   0);
   END LOOP;
   
exception
  when others then
    dbms_output.put_line(sqlerrm);
END;