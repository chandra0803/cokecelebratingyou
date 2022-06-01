DELETE report_parameter WHERE parameter_name = 'promotionStatus' AND report_id = (SELECT report_id FROM report WHERE report_code = 'nomGivenByOrg' )
/
DELETE Report_Parameter WHERE parameter_name = 'submittedType' AND Report_Id = (SELECT report_id FROM report WHERE report_code = 'nomGivenByOrg' )
/
DELETE report_parameter WHERE parameter_name = 'receivedType' AND report_id = (SELECT report_id FROM report WHERE report_code = 'nomGivenByOrg' )
/
UPDATE Report_Parameter
SET Parameter_Type = 'multiSelectQuery'
WHERE parameter_name = 'parentNodeId' AND Report_Id =(SELECT report_id FROM report WHERE report_code = 'nomGivenByOrg' )
/
UPDATE Report_Parameter
SET Parameter_Type = 'multiSelectPick'
WHERE parameter_name = 'jobPosition' AND Report_Id =(SELECT report_id FROM report WHERE report_code = 'nomGivenByOrg' )
/
