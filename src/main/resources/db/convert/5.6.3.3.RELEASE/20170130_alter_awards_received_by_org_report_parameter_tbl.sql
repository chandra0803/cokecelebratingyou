DELETE report_dashboard_item_param WHERE report_parameter_id = (SELECT report_parameter_id FROM Report_Parameter WHERE parameter_name = 'promotionStatus' 
AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'awardsByOrg'))
/
DELETE Report_Parameter WHERE parameter_name = 'promotionStatus' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'awardsByOrg') 
/
DELETE report_dashboard_item_param WHERE report_parameter_id = (SELECT report_parameter_id FROM Report_Parameter WHERE parameter_name = 'receivedAwardType' 
AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'awardsByOrg'))
/
DELETE report_parameter WHERE parameter_name = 'receivedAwardType' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'awardsByOrg') 
/
UPDATE Report_Parameter 
SET Parameter_Type= 'multiSelectQuery'
WHERE parameter_name = 'parentNodeId' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'awardsByOrg') 
/
UPDATE Report_Parameter 
SET Parameter_Type= 'multiSelectPick'
WHERE parameter_name = 'jobPosition' AND report_id = (SELECT report_id FROM REPORT  WHERE report_code = 'awardsByOrg') 
/
INSERT INTO Report_Parameter (Report_Parameter_Id, Report_Id, Parameter_Name, Parameter_Cm_Key, Database_Column,
Parameter_Type, List_Definition, Parameter_Group, 
Sequence_Num, 
Created_By, Date_Created, Modified_By, Date_Modified, Version, Collection_Name, Default_Value) 
VALUES ((SELECT MAX(Report_Parameter_Id)+1 FROM Report_Parameter),(SELECT Report_Id FROM Report  WHERE Report_Code = 'awardsByOrg'),'countryId','COUNTRY_FIELD','countryList',
'multiSelectQuery','com.biperf.core.domain.country.processParameterValueChoices.allLiveCountries','PARTICIPANTS',
(SELECT MAX(Sequence_Num)+1 FROM Report_Parameter WHERE Report_Id = (SELECT Report_Id FROM Report WHERE Report_Code = 'awardsByOrg')),
5661,SYSDATE ,5661,SYSDATE ,0,'countryList','show_all')
/
