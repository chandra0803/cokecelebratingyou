DELETE Report_Parameter WHERE parameter_name = 'promtionStatus' AND report_id = (SELECT report_id from report WHERE report_code = 'nomGiverSummary' )
/
DELETE report_parameter WHERE parameter_name = 'submittedType'  AND report_id= (SELECT report_id from report WHERE report_code = 'nomGiverSummary' )
/
 