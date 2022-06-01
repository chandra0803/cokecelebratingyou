CREATE OR REPLACE FUNCTION fnc_get_recog_counts(p_type IN VARCHAR2,p_user_id IN NUMBER) 
return NUMBER IS

p_count NUMBER:=0;

BEGIN

IF p_type = 'GLOBAL' THEN

 select COUNT(*) INTO p_count  from claim c, promotion p, promo_recognition pr,
   claim_recipient cr, claim_item ci, participant rec
   where
   --promotion  
   C.PROMOTION_ID = P.promotion_id
   AND P.PROMOTION_STATUS ='live'
   AND PR.PROMOTION_ID = P.PROMOTION_ID
   AND PR.ALLOW_PUBLIC_RECOGNITION = 1   
    --recipient 
   AND CI.APPROVAL_STATUS_TYPE = 'approv' 
   AND CR.CLAIM_ITEM_ID = CI.CLAIM_ITEM_ID
   AND CI.CLAIM_ID = C.CLAIM_ID
   AND CR.PARTICIPANT_ID = REC.USER_ID
   AND REC.ALLOW_PUBLIC_RECOGNITION = 1
   --AND C.CLAIM_ID = rc.claim_id
   AND nvl(IS_ADD_POINTS_CLAIM,0)=0;
  -- AND C.HIDE_PUBLIC_RECOGNITION = 0    
   RETURN p_count;
ELSIF p_type = 'SELF' THEN
select COUNT(1) INTO p_count  from claim c, promotion p, promo_recognition pr,
   CLAIM_RECIPIENT cr, claim_item ci, participant rec
   where
   --promotion  
   C.PROMOTION_ID = P.promotion_id
   AND P.PROMOTION_STATUS ='live'
   AND PR.PROMOTION_ID = P.PROMOTION_ID
   AND PR.ALLOW_PUBLIC_RECOGNITION = 1   
    --recipient 
   AND CI.APPROVAL_STATUS_TYPE = 'approv' 
   AND CR.CLAIM_ITEM_ID = CI.CLAIM_ITEM_ID
   AND CI.CLAIM_ID = C.CLAIM_ID
   AND CR.PARTICIPANT_ID = REC.USER_ID
   AND REC.ALLOW_PUBLIC_RECOGNITION = 1
    AND nvl(IS_ADD_POINTS_CLAIM,0)=0
   AND REC.user_id = p_user_id;
   RETURN p_count;
   ELSIF  p_type ='FOLLOWED' THEN
   select COUNT(1) INTO p_count  from claim c, promotion p, promo_recognition pr,
   CLAIM_RECIPIENT cr, claim_item ci, participant rec
   where
   --promotion  
   C.PROMOTION_ID = P.promotion_id
   AND P.PROMOTION_STATUS ='live'
   AND PR.PROMOTION_ID = P.PROMOTION_ID
   AND PR.ALLOW_PUBLIC_RECOGNITION = 1   
    --recipient 
   AND CI.APPROVAL_STATUS_TYPE = 'approv' 
   AND CR.CLAIM_ITEM_ID = CI.CLAIM_ITEM_ID
   AND CI.CLAIM_ID = C.CLAIM_ID
   AND CR.PARTICIPANT_ID = REC.USER_ID
   AND REC.ALLOW_PUBLIC_RECOGNITION = 1
    AND nvl(IS_ADD_POINTS_CLAIM,0)=0
   AND REC.user_id in (select follower_id from  participant_followers pf where pf.participant_id = p_user_id ) ;
   RETURN p_count;
   ELSIF  p_type ='TEAM' THEN   
   select count(1)  INTO p_count from claim c, promotion p, promo_recognition pr,
   CLAIM_RECIPIENT cr, claim_item ci, participant rec,
   (select node_id from user_node where user_id= P_user_id ) un2, user_node un
   where
   --promotion  
   C.PROMOTION_ID = P.promotion_id
   AND P.PROMOTION_STATUS ='live'
   AND PR.PROMOTION_ID = P.PROMOTION_ID
   AND PR.ALLOW_PUBLIC_RECOGNITION = 1
    --recipient 
   AND CI.APPROVAL_STATUS_TYPE = 'approv' 
   AND CR.CLAIM_ITEM_ID = CI.CLAIM_ITEM_ID
   AND CI.CLAIM_ID = C.CLAIM_ID
   AND CR.PARTICIPANT_ID = REC.USER_ID
   AND REC.ALLOW_PUBLIC_RECOGNITION = 1
    AND nvl(IS_ADD_POINTS_CLAIM,0)=0
   AND rec.user_id = un.user_id
   AND UN.NODE_ID = un2.node_id;
   RETURN p_count;
   ELSE p_count:=0;
   RETURN p_count;
   END IF;
   EXCEPTION WHEN OTHERS THEN
   RETURN 0;
   END;
/