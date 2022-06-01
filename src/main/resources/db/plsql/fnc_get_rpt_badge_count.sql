CREATE OR REPLACE FUNCTION fnc_get_rpt_badge_count
(p_user_id IN NUMBER,
 p_module       IN VARCHAR2,
 p_earned_date    IN DATE)
 RETURN NUMBER IS
 v_badge_count NUMBER;
  --murphyc 06/16/2017 Bug 73135 - Tuning [apply G6-1847]
BEGIN
 
	SELECT badge_rule_count 
	INTO   v_badge_count
	FROM   gtt_rpt_badge_count
	WHERE  user_id = p_user_id
	AND    date_created = p_earned_date
	AND    promo_type = p_module;
	
	RETURN v_badge_count;

EXCEPTION
  WHEN OTHERS THEN
    RETURN 0;
END;	-- fnc_get_rpt_badge_count
/