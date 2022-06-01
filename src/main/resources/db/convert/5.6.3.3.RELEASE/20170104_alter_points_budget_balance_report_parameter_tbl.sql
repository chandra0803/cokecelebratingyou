UPDATE report_parameter
SET parameter_type = 'multiSelectQuery'
WHERE parameter_name = 'parentNodeId' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'budgetBalance') 
/
UPDATE report_parameter
SET parameter_type = 'multiSelectPick'
WHERE parameter_name = 'budgetStatus' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'budgetBalance') 
/
DELETE report_dashboard_item_param WHERE report_parameter_id = (SELECT report_parameter_id FROM Report_Parameter WHERE parameter_name = 'promotionStatus' 
AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'budgetBalance'))
/
DELETE report_parameter WHERE parameter_name = 'promotionStatus' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'budgetBalance') 
/
DELETE report_dashboard_item_param WHERE report_parameter_id IN (SELECT report_parameter_id FROM Report_Parameter WHERE parameter_group= 'TIME_FRAME'
AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'budgetBalance'))
/
DELETE report_parameter WHERE parameter_group= 'TIME_FRAME' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'budgetBalance') 
/
UPDATE Report_Chart 
SET Chart_Type = 'Column3D', Show_Legend=0
WHERE caption_cm_key = 'UTILIZATION_PERCENT_CHART_CAPTION' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'budgetBalance') 
/
UPDATE Report_Chart 
SET Chart_Type = 'Column3D', Show_Legend=0
WHERE caption_cm_key = 'UTILIZATION_LEVEL_CHART_CAPTION' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'budgetBalance') 
/
UPDATE Report
SET Report_Url = '/reports/displayPointsBudgetBalanceReport.do?method=displaySummaryReport'
WHERE report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'budgetBalance')
/
UPDATE Report_Chart 
SET Chart_Data_Url = '/reports/displayPointsBudgetBalanceReport.do?method=displayUtilizationPercentageChart'
WHERE report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'budgetBalance') AND caption_cm_key = 'UTILIZATION_PERCENT_CHART_CAPTION'
/
UPDATE Report_Chart 
SET Chart_Data_Url = '/reports/displayPointsBudgetBalanceReport.do?method=displayUtilizationLevelChart'
WHERE report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'budgetBalance') AND caption_cm_key = 'UTILIZATION_LEVEL_CHART_CAPTION'
/