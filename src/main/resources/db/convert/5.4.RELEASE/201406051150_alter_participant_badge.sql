ALTER TABLE participant_badge rename column BADGE_ID TO PROMOTION_ID
/
ALTER TABLE participant_badge ADD
(
	is_earned_All_Behavior_points    NUMBER(1) default 0,
 	is_earned_badge_points		     NUMBER(1) default 0
)
/
