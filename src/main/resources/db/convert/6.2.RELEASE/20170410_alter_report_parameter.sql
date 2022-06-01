update report_parameter
set parameter_type = 'multiSelectPick'
where parameter_name = 'jobPosition' and report_id = (
  select report_id
  from report
  where report_code = 'awardsByOrg')
/