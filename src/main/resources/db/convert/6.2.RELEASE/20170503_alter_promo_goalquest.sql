update promo_goalquest
set program_id=null, merch_code_type=null
where promotion_id IN (
  select promotion_id
  from promotion
  where award_type='points')
/