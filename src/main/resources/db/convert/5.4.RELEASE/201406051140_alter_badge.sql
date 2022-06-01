ALTER TABLE PROMOTION DROP CONSTRAINT BADGE_SETUP_ID_FK
/
ALTER TABLE BADGE_PROMOTION DROP CONSTRAINT BADGE_PROMOTION__BADGEID_FK
/
ALTER TABLE BADGE_PROMOTION DROP CONSTRAINT BADGE_PROMOTION__PROMOID_FK
/
ALTER TABLE PARTICIPANT_BADGE DROP CONSTRAINT PARTICIPANT_BADGE__BADGEID_FK
/
ALTER TABLE BADGE_PROMOTION DROP CONSTRAINT BADGE_PROMOTION_PK
/
ALTER TABLE BADGE_PROMOTION RENAME COLUMN PROMOTION_ID TO ELIGIBLE_PROMOTION_ID
/
ALTER TABLE BADGE_PROMOTION RENAME COLUMN BADGE_ID TO PROMOTION_ID
/
ALTER TABLE BADGE_RULE ADD 
( 
    BADGE_POINTS    NUMBER(30),
    ELIGIBLE_FOR_SWEEPSTAKE  NUMBER(1) DEFAULT 0
)
/
ALTER TABLE BADGE_RULE DROP CONSTRAINT BADGE_RULE_BADGEID_FK
/
ALTER TABLE BADGE 
ADD PROMOTION_ID NUMBER(10)
/
ALTER TABLE BADGE_RULE RENAME COLUMN BADGE_ID TO PROMOTION_ID
/
DECLARE

BEGIN

FOR rec_badge_setup_name IN (SELECT badge_setup_name,DECODE(status,'I','expired','live') status from BADGE) LOOP

    INSERT INTO PROMOTION
       (PROMOTION_ID, PROMO_NAME_ASSET_CODE, PROMOTION_NAME, PROMOTION_TYPE, PROMOTION_STATUS, 
        IS_DELETED, PROMOTION_START_DATE, HAS_WEB_RULES, APPROVAL_START_DATE, 
        APPROVAL_TYPE, APPROVER_TYPE, IS_TAXABLE, IS_ONLINE_ENTRY, 
        IS_FILELOAD_ENTRY, PRIMARY_AUDIENCE_TYPE, SECONDARY_AUDIENCE_TYPE, AWARD_TYPE, SWEEPS_ACTIVE, 
        SWEEPS_WINNER_ELIGIBILITY_TYPE, SWEEPS_MULTIPLE_AWARD_TYPE, --GAME_ATTEMPTS_ACTIVE, 
        ALLOW_SELF_ENROLL, CREATED_BY, 
        DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION, BILLS_ACTIVE, 
        SWP_BILLS_ACTIVE)
     Values
       (PROMOTION_PK_SQ.NEXTVAL, 'promotion_name.'||CMS_UNIQUE_NAME_SQ.NEXTVAL,rec_badge_setup_name.badge_setup_name, 'badge', rec_badge_setup_name.status, 
        0, sysdate, 0, sysdate, 
        'manual', 'specific_approv',1, 0, 
        0, 'specifyaudience', 'allactivepaxaudience', 'points', 0, 
        'separate', 'multiple', --0, 
        0, 5662, 
        sysdate, null, null, 1, 0, 
        0);
        
        END LOOP;
END;
/

BEGIN

FOR rec_badge_promotions IN (select promotion_name,SUBSTR(promo_name_asset_code,INSTR(promo_name_asset_code, '.')+1) asset from promotion WHERE promotion_type ='badge') LOOP

prc_promo_name_insert_cms(rec_badge_promotions.promotion_name,rec_badge_promotions.asset);

END LOOP;

END;
/

BEGIN

FOR rec_badge_setup_name IN (SELECT p.promotion_id,b.badge_id FROM promotion p,badge b WHERE b.badge_setup_name = p.promotion_name AND p.promotion_type = 'badge' ORDER BY promotion_id DESC) LOOP

   UPDATE badge
   SET promotion_id = rec_badge_setup_name.promotion_id
   WHERE badge_id = rec_badge_setup_name.badge_id;
   
   UPDATE badge_promotion
   SET promotion_id = rec_badge_setup_name.promotion_id
   WHERE promotion_id = rec_badge_setup_name.badge_id;
   
   UPDATE badge_rule
   SET promotion_id = rec_badge_setup_name.promotion_id
   WHERE promotion_id = rec_badge_setup_name.badge_id;
   
   UPDATE promotion
   SET badge_setup_id = rec_badge_setup_name.promotion_id
   WHERE badge_setup_id = rec_badge_setup_name.badge_id;
   
   UPDATE participant_badge
   SET promotion_id = rec_badge_setup_name.promotion_id
   WHERE promotion_id = rec_badge_setup_name.badge_id;
        
        END LOOP;
        END;
/

UPDATE badge set badge_id = promotion_id
/

ALTER TABLE BADGE DROP 
  (             promotion_id,
               badge_setup_name, 
                 created_by, 
                 date_created ,
                 modified_by,
                 date_modified,
                 version 
  )
/
ALTER TABLE BADGE RENAME COLUMN BADGE_ID TO PROMOTION_ID
/
ALTER TABLE badge ADD ALL_BEHAVIOR_POINTS  NUMBER(30)
/
ALTER TABLE BADGE_RULE ADD (
  CONSTRAINT BADGE_RULE_PROMOID_FK
  FOREIGN KEY (PROMOTION_ID) 
  REFERENCES BADGE (PROMOTION_ID)
  ENABLE VALIDATE)
/
  ALTER TABLE BADGE_PROMOTION ADD (
  CONSTRAINT BADGE_PROMO_PROMOID_FK
  FOREIGN KEY (PROMOTION_ID) 
  REFERENCES BADGE (PROMOTION_ID)
  ENABLE VALIDATE)
/
ALTER TABLE BADGE_PROMOTION ADD (
  CONSTRAINT BADGE_PROMO_ELIGIBLEPROMOID_FK 
  FOREIGN KEY (ELIGIBLE_PROMOTION_ID) 
  REFERENCES PROMOTION (PROMOTION_ID)
  ENABLE VALIDATE)
/