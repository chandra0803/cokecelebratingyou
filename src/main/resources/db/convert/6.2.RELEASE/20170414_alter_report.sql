DELETE FROM REPORT_DASHBOARD_ITEM_PARAM WHERE report_parameter_id = (SELECT report_parameter_id FROM  report_parameter WHERE parameter_group = 'EXPORT_SELECTION' and report_id = (SELECT report_id FROM report WHERE report_code = 'claimByOrg'))
/
DELETE FROM report_parameter WHERE parameter_group = 'EXPORT_SELECTION' and report_id = (SELECT report_id FROM report WHERE report_code = 'claimByOrg')
/
DELETE FROM REPORT_DASHBOARD_ITEM_PARAM WHERE report_parameter_id = (SELECT report_parameter_id FROM  report_parameter WHERE parameter_group = 'EXPORT_SELECTION' and report_id = (SELECT report_id FROM report WHERE report_code = 'claimByPax'))
/
DELETE FROM report_parameter WHERE parameter_group = 'EXPORT_SELECTION' and report_id = (SELECT report_id FROM report WHERE report_code = 'claimByPax')
/
DELETE FROM REPORT_DASHBOARD_ITEM_PARAM WHERE report_parameter_id = (SELECT report_parameter_id FROM  report_parameter WHERE parameter_group = 'EXPORT_SELECTION' and report_id = (SELECT report_id FROM report WHERE report_code = 'recGivenByOrg'))
/
DELETE FROM report_parameter WHERE parameter_group = 'EXPORT_SELECTION' and report_id = (SELECT report_id FROM report WHERE report_code = 'recGivenByOrg')
/
DELETE FROM REPORT_DASHBOARD_ITEM_PARAM WHERE report_parameter_id = (SELECT report_parameter_id FROM  report_parameter WHERE parameter_group = 'EXPORT_SELECTION' and report_id = (SELECT report_id FROM report WHERE report_code = 'recReceivedByOrg'))
/
DELETE FROM report_parameter WHERE parameter_group = 'EXPORT_SELECTION' and report_id = (SELECT report_id FROM report WHERE report_code = 'recReceivedByOrg')
/
DELETE FROM REPORT_DASHBOARD_ITEM_PARAM WHERE report_parameter_id = (SELECT report_parameter_id FROM  report_parameter WHERE parameter_group = 'EXPORT_SELECTION' and report_id = (SELECT report_id FROM report WHERE report_code = 'recGivenByPax'))
/
DELETE FROM report_parameter WHERE parameter_group = 'EXPORT_SELECTION' and report_id = (SELECT report_id FROM report WHERE report_code = 'recGivenByPax')
/
DELETE FROM REPORT_DASHBOARD_ITEM_PARAM WHERE report_parameter_id = (SELECT report_parameter_id FROM  report_parameter WHERE parameter_group = 'EXPORT_SELECTION' and report_id = (SELECT report_id FROM report WHERE report_code = 'recReceivedByPax'))
/
DELETE FROM report_parameter WHERE parameter_group = 'EXPORT_SELECTION' and report_id = (SELECT report_id FROM report WHERE report_code = 'recReceivedByPax')
/