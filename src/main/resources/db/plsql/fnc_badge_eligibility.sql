CREATE OR REPLACE FUNCTION fnc_badge_eligibility
(p_user_id IN NUMBER,
p_promotion_id IN VARCHAR2)
RETURN NUMBER IS
/******************************************************
-- Unknown       Unknown      Initial Creation
-- Chidamba     10/28/2013    1.Align code struncture 
                              2.BugFix# 49601 - added outerjoin to get the count even 
                                if the badge is not tied up to the promotion.
-- Chidamba     02/26/2014    BugFix #51776
-- Suresh J     07/08/2014    BugFix #53276 - Added start date condition check for badge  
--Ravi Dhanekula 06/25/2015   Bug # 62936 - Removed NOT IN condition that is blocking the pax who achived all the badges to show the tile.
******************************************************/

recCount NUMBER:=0;
v_badge_type BADGE.BADGE_TYPE%TYPE;

BEGIN
-----------------------------Start 02/26/2014----------------------
    BEGIN
       SELECT DISTINCT badge_type
         INTO v_badge_type
         FROM participant_badge pb, 
              badge b 
        WHERE b.promotion_id = pb.promotion_id 
          AND participant_id = p_user_id
          AND badge_type = 'progress';
    EXCEPTION 
    WHEN  NO_DATA_FOUND THEN
      v_badge_type := NULL;
    END;
    
    IF v_badge_type = 'progress' THEN
       SELECT COUNT(*) 
         INTO recCount 
         FROM 
              (SELECT pb.participant_badge_id
                FROM promo_audience pro,
                     badge_promotion bp,
                     badge b,
                     participant_badge pb
              WHERE b.promotion_id = pb.promotion_id
               and pro.promotion_id = bp.eligible_promotion_id
               AND bp.promotion_id = b.promotion_id
               and DECODE(pro.promo_audience_type,'PRIMARY',b.BADGE_COUNT_TYPE,'SECONDARY', b.BADGE_COUNT_TYPE)=DECODE(pro.promo_audience_type,'PRIMARY','given','SECONDARY','received')
               and b.BADGE_TYPE = 'progress'
               and pb.PARTICIPANT_ID = p_user_id
               and PRO.AUDIENCE_ID IN(SELECT AUDIENCE_ID FROM PARTICIPANT_AUDIENCE where user_id = p_user_id)
             UNION
              SELECT pb.participant_badge_id
                FROM promo_audience pro,
                     badge_promotion bp,
                     badge b,
                     participant_badge pb
              WHERE b.promotion_id = pb.promotion_id
               and pro.promotion_id = bp.eligible_promotion_id
               AND bp.promotion_id = b.promotion_id
               and DECODE(pro.promo_audience_type,'PRIMARY',b.BADGE_COUNT_TYPE,'SECONDARY', b.BADGE_COUNT_TYPE)=DECODE(pro.promo_audience_type,'PRIMARY','given','SECONDARY','received')
               and b.BADGE_TYPE = 'progress'
               and pb.PARTICIPANT_ID = p_user_id
               and PRO.AUDIENCE_ID NOT IN(SELECT AUDIENCE_ID FROM PARTICIPANT_AUDIENCE where user_id = p_user_id)
               AND IS_EARNED = 1);
    
    ELSE
---------------------END 02/26/2014---------------------------
    
       SELECT COUNT (PB.PARTICIPANT_BADGE_ID) INTO recCount
          FROM participant_badge pb, badge b
         WHERE participant_id = p_user_id 
           AND pb.promotion_id = b.promotion_id AND b.status = 'A'
           AND SYSDATE <= (SELECT MAX(DECODE(p.promotion_end_date,NULL, SYSDATE,
                                             p.promotion_end_date + b.display_end_days))
                             FROM promotion p, badge bb, badge_promotion bp
                            WHERE bp.eligible_promotion_id = p.promotion_id(+)  --10/28/2013 (+)
                              AND bb.promotion_id = bp.promotion_id(+)         --10/28/2013 (+)
                              AND bb.status = 'A'
                              AND bb.promotion_id = b.promotion_id);
    END IF;
                      
  IF recCount >0 THEN
    RETURN   recCount;
  ELSE
    SELECT COUNT(br.badge_rule_id)
      INTO recCount
      FROM badge b,
           badge_rule br,
           badge_promotion bpp,
           Promo_Behavior Pb,
           promotion p   --07/08/2014
     Where  b.promotion_id = br.promotion_id 
           And B.Promotion_Id = Bpp.Promotion_Id
           And B.Promotion_Id = P.Promotion_Id  --07/08/2014
           And P.Promotion_Type='badge'  --07/08/2014
           And P.promotion_start_date <= SYSDATE  --07/08/2014
           AND b.status = 'A'
           AND bpp.eligible_promotion_id  = pb.promotion_id (+)    --03/27/2015
           AND bpp.eligible_promotion_id IN (SELECT * FROM TABLE(get_array(p_promotion_id)))
           AND (b.badge_type = 'progress'
                OR (b.badge_type = 'behavior'
                AND pb.promotion_id = bpp.eligible_promotion_id
                AND pb.behavior_type = br.behavior_name))
           --AND br.badge_rule_id NOT IN --06/25/2015
           --       (SELECT badge_rule_id
           --          FROM participant_badge
           --         WHERE status = 'A' AND participant_id = p_user_id)
           AND SYSDATE <=
                  (SELECT MAX (
                             DECODE (
                                p.promotion_end_date,
                                NULL, SYSDATE,
                                p.promotion_end_date + b.display_end_days))
                     FROM promotion p, badge bb, badge_promotion bp
                    WHERE bp.eligible_promotion_id = p.promotion_id
                      AND bb.promotion_id = bp.promotion_id
                      AND bb.status = 'A'
                      AND bb.promotion_id = b.promotion_id);
    RETURN recCount;
  END IF;
END;
/
