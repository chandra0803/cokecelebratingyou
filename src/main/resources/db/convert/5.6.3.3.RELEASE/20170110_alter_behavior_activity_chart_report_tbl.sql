UPDATE Report_Chart 
SET Chart_Type = 'Column3D'
WHERE caption_cm_key = 'BARCHART_CAPTION' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'behaviors') 
/
DELETE report_dashboard_item_param WHERE report_parameter_id = (SELECT report_parameter_id FROM Report_Parameter WHERE parameter_name = 'promotionStatus' 
AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'behaviors'))
/
DELETE report_parameter WHERE parameter_name= 'promotionStatus' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'behaviors') 
/
UPDATE Report_parameter
SET parameter_type = 'multiSelectQuery'
WHERE parameter_name ='parentNodeId' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'behaviors')
/
UPDATE Report_parameter
SET parameter_type = 'multiSelectPick'
WHERE parameter_name ='jobPosition' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'behaviors')
/
UPDATE Report_parameter
SET parameter_cm_key = 'BEHAVIORS_FIELD'
WHERE parameter_name ='giverReceiver' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'behaviors')
/