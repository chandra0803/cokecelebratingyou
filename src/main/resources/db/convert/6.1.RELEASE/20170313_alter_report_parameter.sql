DELETE report_parameter WHERE parameter_name = 'parentNodeId' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'cashBudgetBalance')
/