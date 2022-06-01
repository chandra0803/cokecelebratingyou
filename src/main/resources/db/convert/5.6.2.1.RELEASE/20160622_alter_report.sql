DELETE FROM report_parameter WHERE report_id in (select report_id from report where REPORT_CODE IN ('happinessPulse','confidentialFeedback'))
/
DELETE FROM report where REPORT_CODE IN ('happinessPulse','confidentialFeedback')
/