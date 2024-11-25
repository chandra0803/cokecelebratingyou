CREATE INDEX IDX_REPORT_CHART_REPORT_FK ON REPORT_CHART
(REPORT_ID)
/
CREATE INDEX IDX_REPORT_DASHBOARD_FK ON REPORT_DASHBOARD
(USER_ID)
/
CREATE INDEX IDX_REPORT_CHART_FK ON REPORT_DASHBOARD_ITEM
(REPORT_CHART_ID)
/
CREATE INDEX IDX_RPT_DSHBRD_ITEM_RPT_DSHBRD ON REPORT_DASHBOARD_ITEM
(REPORT_DASHBOARD_ID)
/
CREATE INDEX IDX_RPT_DSHBRD_PARAM_ITEM_FK ON REPORT_DASHBOARD_ITEM_PARAM
(REPORT_DASHBOARD_ITEM_ID)
/
CREATE INDEX IDX_RPT_DSHBRD_PARAM_RPT_PARAM ON REPORT_DASHBOARD_ITEM_PARAM
(REPORT_PARAMETER_ID)
/
CREATE INDEX IDX_REPORT_PARAMETER_REPORT_FK ON REPORT_PARAMETER
(REPORT_ID)
/