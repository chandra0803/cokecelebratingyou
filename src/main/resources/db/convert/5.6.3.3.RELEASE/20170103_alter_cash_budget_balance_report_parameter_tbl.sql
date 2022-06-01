UPDATE report_parameter 
SET parameter_type = 'multiSelectQuery'
WHERE parameter_name = 'parentNodeId' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'cashBudgetBalance') 
/
UPDATE report_parameter 
SET parameter_type = 'multiSelectPick'
WHERE parameter_name = 'budgetStatus' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'cashBudgetBalance') 
/
UPDATE REPORT_CHART 
SET Show_Legend =0, Chart_Type = 'Column3D'
WHERE  caption_cm_key = 'UTILIZATION_PERCENT_CHART_CAPTION' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'cashBudgetBalance') 
/
UPDATE REPORT_CHART 
SET Show_Legend =0, Chart_Type = 'Column3D'
WHERE  caption_cm_key = 'UTILIZATION_LEVEL_CHART_CAPTION' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'cashBudgetBalance') 
/
DELETE report_dashboard_item_param WHERE report_parameter_id = (SELECT report_parameter_id FROM Report_Parameter WHERE parameter_name = 'promotionStatus' 
AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'cashBudgetBalance'))
/
DELETE report_parameter WHERE parameter_name = 'promotionStatus' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'cashBudgetBalance') 
/
DELETE report_dashboard_item_param WHERE report_parameter_id IN (SELECT report_parameter_id FROM Report_Parameter WHERE parameter_group = 'TIME_FRAME' 
AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'cashBudgetBalance'))
/
DELETE report_parameter WHERE parameter_group = 'TIME_FRAME' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'cashBudgetBalance') 
/
UPDATE report_parameter 
SET List_Definition ='picklist.budgetdistributiontype', Default_Value ='one_to_one'
WHERE parameter_name = 'budgetDistribution' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'cashBudgetBalance') 
/

