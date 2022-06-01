--liquibase formatted sql

--changeset subramap:1
--comment Creating a view to pick all GQ promotions which has failed Gift Code
create or replace  view vw_merch_Gq as SELECT distinct a.promotion_id as  id
       
  FROM 
       activity  a,
       activity_merch_order am,
       merch_order m
WHERE 
    am.activity_id = a.activity_id
   AND m.merch_order_id = am.merch_order_id
   AND m.is_redeemed = 0
   AND a.claim_id IS NULL 
   AND m.gift_code = '{AES}';
   --rollback drop view vw_merch_Gq;