CREATE OR REPLACE FUNCTION fnc_promo_budget_meter (
   p_in_promotion_id   IN NUMBER,
   p_in_user           IN NUMBER)
   RETURN VARCHAR2
IS

--
-- Purpose: Function to identify if a user is eligible giver for a promotion.
--
-- MODIFICATION HISTORY
-- Person      Date        Comments
-- ---------   ------      -------------------------------------------      
-- Ravi Dhanekula 12/13/2012 Initital code
--Ravi Dhanekula 05/05/2014 Fixed the issue with promotions where we dont have any budgets. Default value will be 'NO' for this function.
--nagarajs       10/26/2015 promotion for the node based budget and restricted the giver and receiver audience the budget tile was still 
--                          appearing for all member roles even they are not a part of audience.
   p_aud_type   VARCHAR2 (100);
   p_user_aud   NUMBER := 0;
   v_return VARCHAR2(10);
BEGIN
   SELECT primary_audience_type
     INTO p_aud_type
     FROM promotion
    WHERE promotion_id = p_in_promotion_id;

   IF p_aud_type = 'allactivepaxaudience'
   THEN
      v_return := 'YES';
   ELSIF p_aud_type = 'specifyaudience'
   THEN
      SELECT COUNT (1)
        INTO p_user_aud
        FROM promo_audience PA, participant_audience PAA
       WHERE     pa.promotion_id = p_in_promotion_id
             AND pa.audience_id = paa.audience_id
             AND paa.user_id = p_in_user
             AND pa.promo_audience_type = 'PRIMARY'; --10/26/2015

      IF p_user_aud > 0
      THEN
      v_return := 'YES';        
      ELSE
         v_return := 'NO';
      END IF;
      
      ELSE 
      v_return := 'NO';      
   END IF;
   
   RETURN v_return;
EXCEPTION 
   WHEN OTHERS
   THEN
      RETURN 'NO';
END;
/