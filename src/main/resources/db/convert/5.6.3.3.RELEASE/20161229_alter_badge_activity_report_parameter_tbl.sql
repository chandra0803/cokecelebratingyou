UPDATE report_parameter 
SET parameter_type = 'multiSelectQuery'
WHERE parameter_name = 'parentNodeId' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'badgeActivityByOrg')
/
UPDATE report_parameter 
SET parameter_type = 'multiSelectPick' 
WHERE parameter_name = 'jobPosition' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'badgeActivityByOrg')
/
DELETE report_dashboard_item_param WHERE report_parameter_id = (SELECT report_parameter_id FROM Report_Parameter WHERE parameter_name = 'promotionStatus' 
AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'badgeActivityByOrg'))
/
DELETE report_parameter WHERE parameter_name = 'promotionStatus' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'badgeActivityByOrg')
/
DELETE report_dashboard_item_param WHERE report_parameter_id = (SELECT report_parameter_id FROM Report_Parameter WHERE parameter_name = 'receivedAwardType' 
AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'badgeActivityByOrg'))
/
DELETE Report_Parameter WHERE parameter_name = 'receivedAwardType' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'badgeActivityByOrg')
/
UPDATE Report_Parameter 
SET Parameter_Cm_Key = 'BADGE_PROMOTION_FIELD'
WHERE parameter_name = 'promotionId' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'badgeActivityByOrg')
/