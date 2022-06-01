
DELETE Report_Parameter WHERE parameter_name  = 'promotionStatus'  AND Report_Id = (SELECT report_id FROM report WHERE report_code = 'awardsByPax')
/
DELETE Report_Parameter WHERE parameter_name = 'receivedAwardType' AND Report_Id = (SELECT report_id FROM report WHERE report_code = 'awardsByPax')
/
UPDATE Report_Parameter
SET Parameter_Type = 'multiSelectQuery'
WHERE parameter_name ='parentNodeId' AND report_id= (SELECT report_id FROM report WHERE report_code = 'awardsByPax')
/
UPDATE Report_Parameter
SET Parameter_Type = 'multiSelectPick'
WHERE parameter_name ='jobPosition' AND report_id= (SELECT report_id FROM report WHERE report_code = 'awardsByPax')
/
