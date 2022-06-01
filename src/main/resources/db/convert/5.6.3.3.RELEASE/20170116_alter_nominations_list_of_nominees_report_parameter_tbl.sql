DELETE report_parameter WHERE report_id = (SELECT report_id FROM REPORT WHERE report_code ='nomReceiverSummary') AND parameter_name = 'receivedType'
/
DELETE report_parameter WHERE report_id = (SELECT report_id FROM REPORT WHERE report_code ='nomReceiverSummary') AND parameter_name = 'promotionStatus'
/
UPDATE Report_Parameter 
SET parameter_type = 'multiSelectQuery'
WHERE report_id = (SELECT report_id FROM REPORT WHERE report_code ='nomReceiverSummary') AND parameter_name = 'parentNodeId'
/
UPDATE Report_Parameter 
SET Parameter_Type = 'multiSelectPick'
WHERE parameter_name = 'jobPosition' AND report_id = (SELECT report_id FROM REPORT WHERE report_code ='nomReceiverSummary')
/

 