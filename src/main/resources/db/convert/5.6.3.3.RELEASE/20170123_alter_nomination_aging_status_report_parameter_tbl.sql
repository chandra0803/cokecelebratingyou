DELETE report_parameter WHERE parameter_name = 'promotionStatus' AND report_id = (SELECT report_id FROM report WHERE report_code = 'nomAging')
/
UPDATE REPORT_CHART 
SET Chart_Type = 'Column3D'
WHERE  caption_cm_key = 'STATUS_CHART_CAPTION' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'nomAging')
/