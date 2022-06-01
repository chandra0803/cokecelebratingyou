CREATE OR REPLACE FUNCTION fnc_check_promo_eligibility
(p_paxid IN NUMBER,
 p_promotionid IN NUMBER)
 RETURN NUMBER IS
 
 promo_elig NUMBER:=0;
 
 BEGIN
 --Check if the promotion has 'allactivepax' audience
 SELECT count(promotion_id) into promo_elig
  FROM promotion
 WHERE (   primary_audience_type = 'allactivepaxaudience'
        OR SECONDARY_AUDIENCE_TYPE = 'allactivepaxaudience'
        OR WEB_RULES_AUDIENCE_TYPE = 'allactivepaxaudience'
        OR PARTNER_AUDIENCE_TYPE = 'allactivepaxaudience')
        AND promotion_id = p_promotionid;
        
        IF promo_elig=1 THEN
        RETURN promo_elig;
        ELSE
        SELECT p.promotion_id into promo_elig
  FROM promo_audience pa, promotion p, participant_audience pax
 WHERE     pax.user_id = p_paxid
       AND pax.audience_id = pa.audience_id
       AND pa.promotion_id = p.promotion_id
       AND p.promotion_id = p_promotionid;
       IF promo_elig > 0 THEN      
       RETURN 1;
       ELSE
       RETURN promo_elig;
       END IF;
       END IF;
   EXCEPTION WHEN OTHERS THEN
   RETURN 99;      
 END;
/