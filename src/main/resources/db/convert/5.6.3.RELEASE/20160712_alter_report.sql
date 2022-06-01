INSERT INTO REPORT
(REPORT_ID, REPORT_CODE, REPORT_NAME_CM_KEY, REPORT_CATEGORY_TYPE, REPORT_DESCRIPTION_CM_KEY, REPORT_URL, IS_ACTIVE, MODULE, CM_ASSET_CODE, FORCE_PARAMETERS, EXPORT_ONLY, INCLUDED_IN_PLATEAU, VERSION)
VALUES
(4041, 'cashBudgetBalance', 'REPORT_NAME', 'core', 'REPORT_DESCRIPTION', '/reports/displayCashBudgetBalanceReport.do?method=displaySummaryReport', 1, 'core', 'report.cash.budget.balance', 1, 0, 0, 0)
/
INSERT INTO REPORT
(REPORT_ID, REPORT_CODE, REPORT_NAME_CM_KEY, REPORT_CATEGORY_TYPE, REPORT_DESCRIPTION_CM_KEY, REPORT_URL, IS_ACTIVE, MODULE, CM_ASSET_CODE, FORCE_PARAMETERS, EXPORT_ONLY, INCLUDED_IN_PLATEAU, VERSION)
VALUES
(4042, 'nomAging', 'REPORT_NAME', 'nomination', 'REPORT_DESCRIPTION', '/reports/displayNominationAgingReport.do?method=displaySummaryReport', 1, 'install.nominations', 'report.nomination.aging', 1, 0, 0, 0)
/
UPDATE REPORT SET REPORT_CODE = 'behaviors', REPORT_CATEGORY_TYPE = 'core', REPORT_URL = '/reports/displayBehaviorsReport.do?method=displaySummaryReport', MODULE = 'core', CM_ASSET_CODE = 'report.behaviors' WHERE REPORT_ID = 4014
/
UPDATE REPORT_CHART SET CAPTION_CM_KEY = 'AWARDS_BARCHART_CAPTION', SUBCAPTION_CM_KEY = 'AWARDS_BARCHART_SUBCAPTION', CHART_DATA_URL = '/reports/displayAwardsByOrgReport.do?method=displayReceivedNotReceivedAwardsForOrgBarchart', CHART_TYPE = 'MSColumn3D', X_AXIS_NAME_CM_KEY = 'AWARDS_BARCHART_X_AXIS_NAME', Y_AXIS_NAME_CM_KEY = 'AWARDS_BARCHART_Y_AXIS_NAME' WHERE REPORT_CHART_ID = 3010
/
UPDATE REPORT_CHART SET CAPTION_CM_KEY = 'PERSONS_AWARDS_CHART_CAPTION', SUBCAPTION_CM_KEY = 'PERSONS_AWARDS_CHART_SUBCAPTION', CHART_DATA_URL = '/reports/displayAwardsByOrgReport.do?method=displayPersonsReceivingAwardsForOrgBarchart', CHART_TYPE = 'Column3D', SHOW_LABELS = 1, SHOW_PERCENT_VALUES = NULL, X_AXIS_NAME_CM_KEY = 'PERSONS_AWARDS_CHART_X_AXIS_NAME', Y_AXIS_NAME_CM_KEY = 'PERSONS_AWARDS_CHART_Y_AXIS_NAME' WHERE REPORT_CHART_ID = 3011
/
UPDATE REPORT_CHART SET CAPTION_CM_KEY = 'AWARDS_PIECHART_CAPTION', SUBCAPTION_CM_KEY = 'AWARDS_PIECHART_SUBCAPTION', CHART_DATA_URL = '/reports/displayAwardsByPaxReport.do?method=displayReceivedNotReceivedAwardsPaxPiechart', CHART_TYPE = 'Pie2D', SHOW_LABELS = 0, SHOW_PERCENT_VALUES = 1, X_AXIS_NAME_CM_KEY = NULL, Y_AXIS_NAME_CM_KEY = NULL, INCLUDED_IN_PLATEAU = 1 WHERE REPORT_CHART_ID = 3015
/
UPDATE REPORT_CHART SET CAPTION_CM_KEY = 'NOMINEES_CHART_CAPTION', SUBCAPTION_CM_KEY = 'NOMINEES_CHART_SUBCAPTION', CHART_DATA_URL = '/reports/displayNominationReceiverReport.do?method=displayNominationsTopNominee', X_AXIS_NAME_CM_KEY = 'NOMINEES_CHART_X_AXIS_NAME', Y_AXIS_NAME_CM_KEY = 'NOMINEES_CHART_Y_AXIS_NAME' WHERE REPORT_CHART_ID = 3038
/
UPDATE REPORT_CHART SET CAPTION_CM_KEY = 'NOMINATORS_CHART_CAPTION', SUBCAPTION_CM_KEY = 'NOMINATORS_CHART_SUBCAPTION', CHART_DATA_URL = '/reports/displayNominationGiverReport.do?method=displayTopNominatorsChart', X_AXIS_NAME_CM_KEY = 'NOMINATORS_CHART_X_AXIS_NAME', Y_AXIS_NAME_CM_KEY = 'NOMINATORS_CHART_Y_AXIS_NAME' WHERE REPORT_CHART_ID = 3045
/
UPDATE REPORT_CHART SET CAPTION_CM_KEY = 'NOMINATORS_CHART_CAPTION', SUBCAPTION_CM_KEY = 'NOMINATORS_CHART_SUBCAPTION', CHART_DATA_URL = '/reports/displayNominationGivenByOrgReport.do?method=displayNominatorsByOrgBarChart', X_AXIS_NAME_CM_KEY = 'NOMINATORS_CHART_X_AXIS_NAME', Y_AXIS_NAME_CM_KEY = 'NOMINATORS_CHART_Y_AXIS_NAME' WHERE REPORT_CHART_ID = 3050
/
UPDATE REPORT_CHART SET CAPTION_CM_KEY = 'NOMINEES_CHART_CAPTION', SUBCAPTION_CM_KEY = 'NOMINEES_CHART_SUBCAPTION', CHART_DATA_URL = '/reports/displayNominationGivenByOrgReport.do?method=displayNomineesByOrgBarChart', X_AXIS_NAME_CM_KEY = 'NOMINEES_CHART_X_AXIS_NAME', Y_AXIS_NAME_CM_KEY = 'NOMINEES_CHART_Y_AXIS_NAME' WHERE REPORT_CHART_ID = 3051
/
UPDATE REPORT_CHART SET CAPTION_CM_KEY = 'MONTHLY_CHART_CAPTION', SUBCAPTION_CM_KEY = 'MONTHLY_CHART_SUBCAPTION', CHART_DATA_URL = '/reports/displayNominationGivenByOrgReport.do?method=displayNominationsByMonthBarChart', CHART_TYPE = 'MSColumn3D', X_AXIS_NAME_CM_KEY = 'MONTHLY_CHART_X_AXIS_NAME', Y_AXIS_NAME_CM_KEY = 'MONTHLY_CHART_Y_AXIS_NAME' WHERE REPORT_CHART_ID = 3052
/
UPDATE REPORT_CHART SET CHART_DATA_URL = '/reports/displayBehaviorsReport.do?method=displayPiechart' WHERE REPORT_CHART_ID = 3056
/
UPDATE REPORT_CHART SET CHART_DATA_URL = '/reports/displayBehaviorsReport.do?method=displayBarchart' WHERE REPORT_CHART_ID = 3057
/
UPDATE REPORT_PARAMETER SET PARAMETER_TYPE = 'multiSelectQuery' WHERE REPORT_PARAMETER_ID = 2105
/
UPDATE REPORT_PARAMETER SET PARAMETER_TYPE = 'multiSelectPick' WHERE REPORT_PARAMETER_ID = 2107
/
UPDATE REPORT_PARAMETER SET HIDE_SHOW_ALL_OPTION = 1 WHERE REPORT_PARAMETER_ID = 2109
/
UPDATE REPORT_PARAMETER SET PARAMETER_TYPE = 'multiSelectQuery', LIST_DEFINITION = 'com.biperf.core.domain.node.reportParameterValueChoices.allLiveBehaviorPromotions' WHERE REPORT_PARAMETER_ID = 2130
/
INSERT INTO REPORT_CHART
(REPORT_CHART_ID, CAPTION_CM_KEY, SUBCAPTION_CM_KEY, CHART_DATA_URL, CHART_TYPE, REPORT_ID, SHOW_LABELS, SHOW_LEGEND, ENABLE_SMART_LABELS, SHOW_PERCENT_VALUES, X_AXIS_NAME_CM_KEY, Y_AXIS_NAME_CM_KEY, SEQUENCE_NUM, DISPLAY_LIMIT, INCLUDED_IN_PLATEAU, VERSION)
VALUES
(3124, 'UTILIZATION_PERCENT_CHART_CAPTION', 'UTILIZATION_PERCENT_CHART_SUBCAPTION', '/reports/displayCashBudgetBalanceReport.do?method=displayUtilizationPercentageChart', 'StackedBar3D', 4041, 1, 1, 0, 0, 'PROMO_NAME', 'BUDGET_PER_CHART_Y_AXIS_NAME', 0, 20, 0, 0)
/
INSERT INTO REPORT_CHART
(REPORT_CHART_ID, CAPTION_CM_KEY, SUBCAPTION_CM_KEY, CHART_DATA_URL, CHART_TYPE, REPORT_ID, SHOW_LABELS, SHOW_LEGEND, ENABLE_SMART_LABELS, SHOW_PERCENT_VALUES, X_AXIS_NAME_CM_KEY, Y_AXIS_NAME_CM_KEY, SEQUENCE_NUM, DISPLAY_LIMIT, INCLUDED_IN_PLATEAU, VERSION)
VALUES
(3125, 'UTILIZATION_LEVEL_CHART_CAPTION', 'UTILIZATION_LEVEL_CHART_SUBCAPTION', '/reports/displayCashBudgetBalanceReport.do?method=displayUtilizationLevelChart', 'StackedColumn3D', 4041, 1, 1, 0, 0, 'PROMO_NAME', 'BUDGET_UTIL_CHART_Y_AXIS_NAME', 1, 20, 0, 0)
/
INSERT INTO REPORT_CHART
(REPORT_CHART_ID, CAPTION_CM_KEY, SUBCAPTION_CM_KEY, CHART_DATA_URL, CHART_TYPE, REPORT_ID, SHOW_LABELS, SHOW_LEGEND, ENABLE_SMART_LABELS, SHOW_PERCENT_VALUES, X_AXIS_NAME_CM_KEY, Y_AXIS_NAME_CM_KEY, SEQUENCE_NUM, DISPLAY_LIMIT, INCLUDED_IN_PLATEAU, VERSION)
VALUES
(3126, 'STATUS_CHART_CAPTION', 'STATUS_CHART_SUBCAPTION', '/reports/displayNominationAgingReport.do?method=displayNominationStatusChart', 'Bar2D', 4042, 1, 1, 0, 0, 'STATUS_CHART_X_AXIS_NAME', 'STATUS_CHART_Y_AXIS_NAME', 0, 20, 0, 0)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2268, 4041, 'budgetDistribution', 'BUDGET_DISTRIBUTION_FIELD', 'budgetDistributionTypeList', 'selectPicklist', 'picklist.cashbudgetdistributiontype', 'BASICS', 0, 0, 'budgetDistributionTypeList', 'central', 0, 1, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2269, 4041, 'budgetStatus', 'BUDGET_STATUS_FIELD', 'budgetStatusTypeList', 'selectPicklist', 'picklist.budgetstatustype', 'BASICS', 1, 0, 'budgetStatusTypeList', 'show_all', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2270, 4041, 'promotionStatus', 'PROMOTION_STATUS_FIELD', 'reportPromotionStatusTypeList', 'selectPicklist', 'picklist.reportpromotion.statustype', 'BASICS', 2, 0, 'reportPromotionStatusTypeList', 'live', 0, 1, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2271, 4041, 'fromDate', 'START_FIELD', 'DATE_SUBMITTED', 'datePicker', NULL, 'TIME_FRAME', 3, 1, NULL, '01/01/2012', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2272, 4041, 'toDate', 'END_FIELD', 'DATE_SUBMITTED', 'datePicker', NULL, 'TIME_FRAME', 4, 1, NULL, '12/31/2012', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2273, 4041, 'parentNodeId', 'ORG_NAME', 'organizationList', 'selectQuery', 'com.biperf.core.domain.node.processParameterValueChoices.NodesAsHierarchyByUser', 'PARTICIPANTS', 5, 1, 'organizationList', 'show_all', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2274, 4041, 'promotionId', 'PROMOTION_FIELD', 'promotionTypeList', 'multiSelectQuery', 'com.biperf.core.domain.node.reportParameterValueChoices.allLivePromotionsWithCashBudget', 'BASICS', 6, 1, 'promotionTypeList', 'show_all', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2275, 4041, 'countryRatio', 'COUNTRY_RATIO', 'COUNTRY_RATIO', 'hidden', NULL, 'Hidden', 7, 0, 'COUNTRY_RATIO', NULL, 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2276, 4042, 'fromDate', 'START_FIELD', 'DATE_SUBMITTED', 'datePicker', NULL, 'TIME_FRAME', 0, 1, NULL, '01/01/2012', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2277, 4042, 'toDate', 'END_FIELD', 'DATE_SUBMITTED', 'datePicker', NULL, 'TIME_FRAME', 1, 1, NULL, '12/31/2012', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2278, 4042, 'nominationAudienceType', 'NOMINATION_AUDIENCE_FIELD', 'nominationAudienceTypeList', 'selectPicklist', 'picklist.nomination.audience.type', 'PARTICIPANTS', 2, 1, 'nominationAudienceTypeList', 'nominee', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2279, 4042, 'parentNodeId', 'ORG_NAME', 'organizationList', 'multiSelectQuery', 'com.biperf.core.domain.node.processParameterValueChoices.NodesAsHierarchyByUser', 'PARTICIPANTS', 3, 0, 'organizationList', 'show_all', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2280, 4042, 'department', 'DEPARTMENT_FIELD', 'departmentTypeList', 'multiSelectPick', 'picklist.department.type', 'PARTICIPANTS', 4, 0, 'departmentTypeList', 'show_all', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2281, 4042, 'jobPosition', 'JOB_POSITION_FIELD', 'positionTypeList', 'multiSelectPick', 'picklist.positiontype', 'PARTICIPANTS', 5, 0, 'jobPositionTypeList', 'show_all', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2282, 4042, 'participantStatus', 'PARTICIPANT_STATUS_FIELD', 'participantStatusList', 'selectPicklist', 'picklist.participantstatus', 'PARTICIPANTS', 6, 0, 'participantStatusList', 'active', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2283, 4042, 'countryId', 'COUNTRY_FIELD', 'countryList', 'multiSelectQuery', 'com.biperf.core.domain.country.processParameterValueChoices.allLiveCountries', 'PARTICIPANTS', 7, 0, 'countryList', 'show_all', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2284, 4042, 'promotionStatus', 'PROMOTION_STATUS_FIELD', 'reportPromotionStatusTypeList', 'selectPicklist', 'picklist.reportpromotion.statustype', 'BASICS', 8, 0, 'reportPromotionStatusTypeList', 'live', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2285, 4042, 'promotionId', 'PROMOTION_FIELD', 'promotionTypeList', 'multiSelectQuery', 'com.biperf.core.domain.node.reportParameterValueChoices.allLiveNominationPromotions', 'BASICS', 9, 1, 'promotionTypeList', 'show_all', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2286, 4042, 'nominationApprovalStatus', 'NOMINATION_APPROVAL_STATUS_FIELD', 'nominationApprovalStatusTypeList', 'multiSelectPick', 'picklist.nomination.approval.status', 'BASICS', 10, 0, 'nominationApprovalStatusTypeList', 'show_all', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(2287, 4042, 'nominationApprovalLevel', 'NOMINATION_APPROVAL_LEVEL_FIELD', 'nominationApprovalLevelList', 'multiSelectPick', 'picklist.approval.level.list', 'BASICS', 11, 0, 'nominationApprovalLevelList', 'show_all', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(3163, 4029, 'promotionStatus', 'PROMOTION_STATUS_FIELD', 'reportPromotionStatusTypeList', 'selectPicklist', 'picklist.reportpromotion.statustype', 'BASICS', 7, 0, 'reportPromotionStatusTypeList', 'live', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(3164, 4029, 'promotionId', 'PROMOTION_FIELD', 'promotionTypeList', 'multiSelectQuery', 'com.biperf.core.domain.node.reportParameterValueChoices.allBadgePromotions', 'BASICS', 8, 1, 'promotionTypeList', 'show_all', 0, 0, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(3165, 4002, 'awardType', 'AWARDS_TYPE', 'awardsTypeList', 'selectPicklist', 'picklist.promotion.awardstype', 'BASICS', 0, 0, 'awardsTypeList', 'points', 0, 1, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, DISPLAY_ON_DASHBOARD, COLLECTION_NAME, DEFAULT_VALUE, ADMIN_SELECT_ONLY, HIDE_SHOW_ALL_OPTION, VERSION, CREATED_BY, DATE_CREATED)
VALUES
(3166, 4003, 'awardType', 'AWARDS_TYPE', 'awardsTypeList', 'selectPicklist', 'picklist.promotion.awardstype', 'BASICS', 0, 0, 'awardsTypeList', 'points', 0, 1, 0, 0, SYSDATE)
/
INSERT INTO REPORT_PARAMETER 
(REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION, COLLECTION_NAME, DEFAULT_VALUE) 
VALUES 
(3167, 4012, 'receivedType', 'NOMINATED_FIELD', 'receivedTypeList', 'selectPicklist', 'picklist.nominated.type', 'EXPORT_SELECTION', 9,5661, to_date('20-AUG-12','DD-MON-RR'), 5661, to_date('20-AUG-12','DD-MON-RR'), 0, 'receivedTypeList', 'have')    
/
DELETE FROM REPORT_CHART WHERE REPORT_CHART_ID IN (3012, 3013, 3016, 3017, 3036, 3037, 3039, 3040, 3041, 3042, 3043, 3044, 3046, 3047, 3048, 3049)
/
DELETE FROM REPORT_PARAMETER WHERE REPORT_PARAMETER_ID IN (2073, 2074, 2075, 2076, 2077, 2078, 2079, 2080, 2091, 2092, 2093, 2094, 2095, 2096, 2097, 2098, 2099, 2100, 3158, 3160)
/
DELETE FROM REPORT WHERE REPORT_ID IN (4008, 4010)
/