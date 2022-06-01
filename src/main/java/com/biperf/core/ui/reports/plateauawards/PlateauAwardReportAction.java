
package com.biperf.core.ui.reports.plateauawards;

import java.util.Map;

import com.biperf.core.service.reports.PlateauAwardReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;

public abstract class PlateauAwardReportAction extends BaseReportsAction
{

  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "ORG_NAME",
                                                             "LEVEL_NAME",
                                                             "CODES_ISSUED",
                                                             "CODES_REDEEMED",
                                                             "PERC_REDEEMED",
                                                             "CODES_UNREDEEMED",
                                                             "PERC_UNREDEEMED",
                                                             "CODES_EXPIRED",
                                                             "PERC_EXPIRED" };

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.plateauaward.extract";
  }

  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map output = getPlateauAwardReportsService().getParticipantAwardLevelExtract( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, output );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.PLATEAU_AWARD_LEVEL_ACTIVITY_REPORT;
  }

  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

  protected PlateauAwardReportsService getPlateauAwardReportsService()
  {
    return (PlateauAwardReportsService)getService( PlateauAwardReportsService.BEAN_NAME );
  }

}
