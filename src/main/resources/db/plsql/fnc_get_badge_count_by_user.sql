CREATE OR REPLACE function fnc_get_badge_count_by_user
(p_user_id IN NUMBER,
 p_module       IN VARCHAR2,
 earned_date    IN DATE)--recognition/nomination/quiz/null
 RETURN NUMBER is
 
 v_badge_count NUMBER:=0;
 
 BEGIN
 IF p_module IS NULL THEN
 SELECT COUNT(DISTINCT pb.badge_rule_id) INTO v_badge_count
FROM
participant_badge pb
WHERE
pb.participant_id=p_user_id
AND pb.date_created = trunc(earned_date)
AND NOT EXISTS (select 1 from badge_promotion where promotion_id=pb.promotion_id);

RETURN v_badge_count;
ELSIF p_module ='ALL' THEN

SELECT COUNT(DISTINCT pb.badge_rule_id) INTO v_badge_count
FROM
participant_badge pb,
badge_promotion bp,
promotion p
WHERE
pb.participant_id=p_user_id
AND pb.date_created = trunc(earned_date)
AND pb.promotion_id=bp.promotion_id
AND bp.eligible_promotion_id=p.promotion_id;

RETURN v_badge_count;
ELSE
SELECT COUNT(DISTINCT pb.badge_rule_id) INTO v_badge_count
FROM
participant_badge pb,
badge_promotion bp,
promotion p
WHERE
pb.participant_id=p_user_id
AND pb.date_created = trunc(earned_date)
AND pb.promotion_id=bp.promotion_id
AND bp.eligible_promotion_id=p.promotion_id
AND p.promotion_type=p_module;
RETURN v_badge_count;
END IF;

EXCEPTION WHEN OTHERS THEN
v_badge_count:=0;
RETURN v_badge_count;
END;
/
