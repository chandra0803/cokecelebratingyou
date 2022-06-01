/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/recognition/RecognitionReportAction.java,v $
 *
 */

package com.biperf.core.ui.reports.recognition;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.service.reports.RecognitionReportsService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;


/**
 * RecognitionReportsAction <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>drahn</td>
 * <td>Aug 14, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author drahn
 *
 */
public abstract class RecognitionReportAction extends BaseReportsAction
{
  protected static final String[] EXTRACT_ALL_COLUMN_NAMES = { "DATE_SUBMITTED",
                                                               "FINAL_APPROVER",
                                                               "REASON_DENIED",
                                                               "PROMOTION_NAME",
                                                               "BEHAVIOR",
                                                               "RECIPIENT_FIRST_NAME",
                                                               "RECIPIENT_MIDDLE_NAME",
                                                               "RECIPIENT_LAST_NAME",
                                                               "RECIPIENT_LOGIN_ID",
                                                               "RECIPIENT_COUNTRY",
                                                               "RECIPIENT_PRIMARY_ORG_UNIT",
                                                               "RECIPIENT_JOB_POSITION",
                                                               "RECIPIENT_DEPARTMENT",
                                                               "GIVER_FIRST_NAME",
                                                               "GIVER_MIDDLE_NAME",
                                                               "GIVER_LAST_NAME",
                                                               "GIVER_LOGIN_ID",
                                                               "PROXY_NAME",
                                                               "GIVER_COUNTRY",
                                                               "GIVER_PRIMARY_ORG_UNIT",
                                                               "GIVER_JOB_POSITION",
                                                               "GIVER_DEPARTMENT",
                                                               "POINTS",
                                                               "PLATEAU_EARNED",
                                                               // "SWEEPSTAKES_WON",
                                                               "PAX_CHAR1",
                                                               "PAX_CHAR2",
                                                               "PAX_CHAR3",
                                                               "PAX_CHAR4",
                                                               "PAX_CHAR5",
                                                               "PAX_CHAR6",
                                                               "PAX_CHAR7",
                                                               "PAX_CHAR8",
                                                               "PAX_CHAR9",
                                                               "PAX_CHAR10",
                                                               "PAX_CHAR11",
                                                               "PAX_CHAR12",
                                                               "PAX_CHAR13",
                                                               "PAX_CHAR14",
                                                               "PAX_CHAR15",
 															  "PAX_CHAR16",
 															  "PAX_CHAR17",
 															  "PAX_CHAR18",
 															  "PAX_CHAR19",
 															  "PAX_CHAR20",
 															  "PAX_CHAR21",
 															  "PAX_CHAR22",
 															  "PAX_CHAR23",
 															  "PAX_CHAR24",
 															  "PAX_CHAR25",
 															  "PAX_CHAR26",
 															  "PAX_CHAR27",
 															  "PAX_CHAR28",
 															  "PAX_CHAR29",
 															  "PAX_CHAR30",
 															  "PAX_CHAR31",
 															  "PAX_CHAR32",
 															  "PAX_CHAR33",
 															  "PAX_CHAR34",
 															  "PAX_CHAR35",

                                                               "PUBLIC_OR_PRIVATE"};

  private static final String SUBMITTER_COMMENTS_COLUMN_HEADER = "SUBMITTER_COMMENTS";

  protected RecognitionReportsService getRecognitionReportsService()
  {
    return (RecognitionReportsService)getService( RecognitionReportsService.BEAN_NAME );
  }

  protected String[] getColumnHeaders()
  {
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.RECOGNITION_REPORT_COMMENT_ENABLED ).getBooleanVal() )
    {
      String[] EXTRACT_ALL_COLUMN_NAMES_INCLUDING_COMMENTS = Arrays.copyOf( EXTRACT_ALL_COLUMN_NAMES, EXTRACT_ALL_COLUMN_NAMES.length + 1 );
      EXTRACT_ALL_COLUMN_NAMES_INCLUDING_COMMENTS[EXTRACT_ALL_COLUMN_NAMES_INCLUDING_COMMENTS.length - 1] = SUBMITTER_COMMENTS_COLUMN_HEADER;
      return EXTRACT_ALL_COLUMN_NAMES_INCLUDING_COMMENTS;
    }
    return EXTRACT_ALL_COLUMN_NAMES;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.recognition.extract";
  }

  protected String getGiverReceiverType()
  {
    return "";
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    if ( reportParameters.get( "isBehavior" ) == null )
    {
      // For behaviors this value comes from screen
      reportParameters.put( "giverReceiver", getGiverReceiverType() );
    }
    Map reportExtractData = getRecognitionReportsService().getRecognitionActivityReportExtract( reportParameters );
    buildCSVColumnNames( contentBuf, getColumnHeaders() );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

//customization WIP 20160 start
@Override
 protected void launchReportExtract( Map<String, Object> reportParameters, String userFileName, boolean isSecondaryExtract,HttpServletRequest request )
  {
    if ( reportParameters.get( "isBehavior" ) == null )
    {
      // For behaviors this value comes from screen
      reportParameters.put( "giverReceiver", getGiverReceiverType() );
    }
    super.launchReportExtract( reportParameters, userFileName, false , request );
  }
//customization WIP 20160 end
  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.RECOGNITION_ACTIIVTY_REPORT;
  }
}
