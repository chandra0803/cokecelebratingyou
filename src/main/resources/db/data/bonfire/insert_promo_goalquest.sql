---
--- GOAL QUEST Data
---
insert into promotion ( promotion_id,PROMO_NAME_ASSET_CODE, promotion_name, promotion_type, promotion_status, award_type,
                        claim_form_id, promotion_start_date, promotion_end_date,
                        created_by, date_created, version, is_deleted, has_web_rules,
                        is_taxable, primary_audience_type, secondary_audience_type, sweeps_active, is_online_entry,
                        is_fileload_entry, allow_self_enroll, enroll_program_code, TILE_DISPLAY_START_DATE, TILE_DISPLAY_END_DATE, OVERVIEW  )
values (25, 'promotion_name.testname', 'Goal Quest Promotion - Template', 'goalquest', 'under_construction', 'points', 
  NULL, sysdate, sysdate+30, 0, sysdate, 0, 0, 0,   
  0 , 'allActivePaxAudience', 'specifyaudience', 0, 0, 0, 0, null, sysdate, sysdate+30, 'promo_overview.testgqoverview'   )
/
insert into promo_goalquest (promotion_id, GOAL_OBJECTIVE, GOAL_OBJECTIVE_ASSET_CODE,  goal_selection_start_date, goal_selection_end_date,
							 achievement_rule, payout_structure, rounding_method, achievement_precision,
							 override_structure, LEVEL1_MGR_AWARD, LEVEL2_MGR_AWARD, final_process_date, issue_awards_run, progress_load_type,BASEUNIT,BASEUNIT_POSITION )
values (25, 'fnc_cms_asset_code_value(goal_quest_cp_promo_objective.testobjv)', 'goal_quest_cp_promo_objective.testobjv', sysdate+2, sysdate+15, 'fixed', 'fixed', 'standard', 'zero', 'none', null,null, sysdate+28, 0, 'sales','goal_quest_cp_promo_baseunit.testgq','after' )
/
insert into goalquest_goallevel (goallevel_id, promotion_id, sequence_num, goallevel_discrim, 
								 created_by, date_created, version,
								 award, minimum_qualifier, incremental_quantity, bonus_award,
								 maximum_points, team_achievement_pct, manager_award,
								 GOAL_LEVEL_CM_ASSET_CODE,GOAL_LEVEL_NAME_KEY, GOAL_LEVEL_DESC_KEY, achievement_amount )
values (10, 25, 1, 'goal', -1, sysdate, 1, 100, null, null, null, null, null, null,'goal_quest_cp.goal_descrpit.testgqgold', 'GOALS', 'GOAL_DESCRIPTION', 125.25 )
/
insert into goalquest_goallevel (goallevel_id, promotion_id, sequence_num, goallevel_discrim, 
								 created_by, date_created, version,
								 award, minimum_qualifier, incremental_quantity, bonus_award,
								 maximum_points, team_achievement_pct, manager_award,
								 GOAL_LEVEL_CM_ASSET_CODE,GOAL_LEVEL_NAME_KEY, GOAL_LEVEL_DESC_KEY, achievement_amount )
values (20, 25, 2, 'goal', -1, sysdate, 1,  50, null, null, null, null, null, null, 'goal_quest_cp.goal_descrpit.testgqsilver', 'GOALS', 'GOAL_DESCRIPTION', 100.25 )
/
insert into goalquest_goallevel (goallevel_id, promotion_id, sequence_num, goallevel_discrim, 
								 created_by, date_created, version,
								 award, minimum_qualifier, incremental_quantity, bonus_award,
								 maximum_points, team_achievement_pct, manager_award,
								 GOAL_LEVEL_CM_ASSET_CODE,GOAL_LEVEL_NAME_KEY, GOAL_LEVEL_DESC_KEY, achievement_amount )
values (30, 25, 3, 'goal', -1, sysdate, 1,  25, null, null, null, null, null, null,  'goal_quest_cp.goal_descrpit.testgqcopper', 'GOALS', 'GOAL_DESCRIPTION', 85 )
/
insert into promo_notification (promo_notification_id, promotion_id, promotion_notification_type, notification_type, notification_message_id,
								claim_form_step_email_id, number_of_days, frequency_type, dayofweek_type, DAY_OF_MONTH, created_by, date_created,
								version, sequence_num )
values (26, 25, 'PROMOTION_NOTIFICATION_TYPE', 'program_launch', -1, null, null, null, null, 1, -1, sysdate, 1, 0)
/
insert into promo_notification (promo_notification_id, promotion_id, promotion_notification_type, notification_type, notification_message_id,
								claim_form_step_email_id, number_of_days, frequency_type, dayofweek_type, DAY_OF_MONTH, created_by, date_created,
								version, sequence_num )
values (27, 25, 'PROMOTION_NOTIFICATION_TYPE', 'goal_selected', -1, null, null, null, null, 2, -1, sysdate, 1, 1)
/
insert into promo_notification (promo_notification_id, promotion_id, promotion_notification_type, notification_type, notification_message_id,
								claim_form_step_email_id, number_of_days, frequency_type, dayofweek_type, DAY_OF_MONTH, created_by, date_created,
								version, sequence_num )
values (28, 25, 'PROMOTION_NOTIFICATION_TYPE', 'goal_not_selected', -1, null, null, null, null, 3, -1, sysdate, 1, 2)
/
insert into promo_notification (promo_notification_id, promotion_id, promotion_notification_type, notification_type, notification_message_id,
								claim_form_step_email_id, number_of_days, frequency_type, dayofweek_type, DAY_OF_MONTH, created_by, date_created,
								version, sequence_num )
values (29, 25, 'PROMOTION_NOTIFICATION_TYPE', 'progress_updated', -1, null, null, null, null, 4, -1, sysdate, 1, 3)
/
insert into promo_notification (promo_notification_id, promotion_id, promotion_notification_type, notification_type, notification_message_id,
								claim_form_step_email_id, number_of_days, frequency_type, dayofweek_type, DAY_OF_MONTH, created_by, date_created,
								version, sequence_num )
values (30, 25, 'PROMOTION_NOTIFICATION_TYPE', 'pax_inactivity', -1, null, null, null, null, 5, -1, sysdate, 1, 4)
/
insert into promo_notification (promo_notification_id, promotion_id, promotion_notification_type, notification_type, notification_message_id,
								claim_form_step_email_id, number_of_days, frequency_type, dayofweek_type, DAY_OF_MONTH, created_by, date_created,
								version, sequence_num )
values (31, 25, 'PROMOTION_NOTIFICATION_TYPE', 'program_end', -1, null, null, null, null, 6, -1, sysdate, 1, 5)
/
insert into promo_notification (promo_notification_id, promotion_id, promotion_notification_type, notification_type, notification_message_id,
								claim_form_step_email_id, number_of_days, frequency_type, dayofweek_type, DAY_OF_MONTH, created_by, date_created,
								version, sequence_num )
values (32, 25, 'PROMOTION_NOTIFICATION_TYPE', 'goal_achieved', -1, null, null, null, null, 7, -1, sysdate, 1, 6)
/
insert into promo_notification (promo_notification_id, promotion_id, promotion_notification_type, notification_type, notification_message_id,
								claim_form_step_email_id, number_of_days, frequency_type, dayofweek_type, DAY_OF_MONTH, created_by, date_created,
								version, sequence_num )
values (33, 25, 'PROMOTION_NOTIFICATION_TYPE', 'goal_not_achieved', -1	, null, null, null, null, 8, -1, sysdate, 1, 7)
/
